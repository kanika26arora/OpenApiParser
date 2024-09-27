package com.chat.openapiParser.config_manager.parser.impl

import com.chat.openapiParser.config_manager.common.enums.ParameterType
import com.chat.openapiParser.config_manager.common.enums.PlacementType
import com.chat.openapiParser.config_manager.common.enums.SchemaType
import com.chat.openapiParser.config_manager.common.exception.ValidationFailedException
import com.chat.openapiParser.config_manager.common.logging.slf4j
import com.chat.openapiParser.config_manager.dto.parser.ParserParameterDto
import com.chat.openapiParser.config_manager.parser.RequestBodyParser
import com.chat.openapiParser.config_manager.parser.config.OpenApiParserMediaTypeConfig
import com.chat.openapiParser.config_manager.util.ParsingUtil
import com.chat.openapiParser.config_manager.validator.OpenApiValidator
import io.swagger.v3.oas.models.media.BinarySchema
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import org.springframework.stereotype.Component
import io.swagger.v3.oas.models.parameters.RequestBody

@Component
class RequestBodyParserImpl(private val mediaTypeConfig: OpenApiParserMediaTypeConfig) :
    RequestBodyParser {
  override fun parse(requestBody: RequestBody?): List<ParserParameterDto> {
    log.info("Parsing requestBody")
    return requestBody?.let { parseContents(it.content.entries) } ?: return emptyList()
  }
  /** Get the list of content types in the open api spec such as application/json etc */
  override fun getContentTypes(requestBody: RequestBody?): List<String>? {
    return requestBody?.let { it.content?.map { entry -> entry.key } }
  }
  /** Parses the method into the supported application content types and schema. */
  private fun parseContents(
      contentEntries: MutableSet<MutableMap.MutableEntry<String, MediaType>>
  ): List<ParserParameterDto>? {
    val jsonOrBinaryEntry =
        OpenApiValidator.validateAndFilterEntries(contentEntries, mediaTypeConfig.requestBody)
    val jsonOrBinarySchema = jsonOrBinaryEntry.value.schema
    log.info("Parsing Request Body content, schema type ${jsonOrBinarySchema.type}")
    return if (jsonOrBinarySchema is BinarySchema && !ParsingUtil.isReadOnly(jsonOrBinarySchema)) {
      val required = jsonOrBinarySchema.required?.isNotEmpty() ?: !IS_REQUIRED
      listOf(
          ParserParameterDto(
              null,
              PlacementType.BODY,
              FILE,
              null,
              null,
              ParameterType.FILE,
              null,
              !IS_ARRAY,
              required,
              null,
              null,
              null)
      )
    } else {

      jsonOrBinarySchema.properties?.let {
        parseProperties(it, jsonOrBinarySchema.required?.toSet() ?: emptySet())
      }
          ?: jsonOrBinarySchema?.items?.let { parseArrayItems(it) }
    }
  }
  /** Parses the individual properties of the json object. */
  private fun parseProperties(
      properties: Map<String, Schema<Any>>,
      requiredFields: Set<String>
  ): MutableList<ParserParameterDto> {
    val parserParameterDtos = mutableListOf<ParserParameterDto>()
    properties.entries.stream().forEach {
      val prefix = it.key

      val bodyParameterSchemaStack = Stack<Pair<BodyParameterDto, Schema<*>>>()
      log.debug("Pushing to bodyParameterSchemaStack size ${bodyParameterSchemaStack.size}")
      pushToStack(
          bodyParameterSchemaStack,
          BodyParameterDto(prefix, !ParsingUtil.IS_ARRAY, requiredFields.contains(prefix), null),
          it.value)
      parserParameterDtos.addAll(parseSchema(bodyParameterSchemaStack))
    }
    return parserParameterDtos
  }

  /**
   * Handles the case when request body is an array of items such as
   * [ { "message": "string", "errorCode": "string"}]
   */
  private fun parseArrayItems(items: Schema<*>): MutableList<ParserParameterDto> {
    val parserParameterDtos = mutableListOf<ParserParameterDto>()

    val bodyParameterSchemaStack = Stack<Pair<BodyParameterDto, Schema<*>>>()
    log.debug("Pushing to bodyParameterSchemaStack size ${bodyParameterSchemaStack.size}")
    pushToStack(
        bodyParameterSchemaStack,
        BodyParameterDto(
            items.name ?: ParsingUtil.ARRAY_DISCRIMINATOR,
            !ParsingUtil.IS_ARRAY,
            !IS_REQUIRED,
            null),
        items)
    parserParameterDtos.addAll(parseSchema(bodyParameterSchemaStack))

    return parserParameterDtos
  }
  /**
   * Generates the final ParserParameterDto list by individually reading and parsing the json
   * properties until all the nested levels have been processed
   */
  private fun parseSchema(
      bodyParameterSchemaStack: Stack<Pair<BodyParameterDto, Schema<*>>>
  ): List<ParserParameterDto> {
    val parserParameterDtos = mutableListOf<ParserParameterDto>()
    val repeatableGroupId = AtomicInteger(0)
    log.debug("Parsing request body schema")
    while (bodyParameterSchemaStack.isNotEmpty()) {
      val bodyParameterDtoSchemaPair = bodyParameterSchemaStack.pop()

      val bodyParameterDto = bodyParameterDtoSchemaPair.first
      val schema = bodyParameterDtoSchemaPair.second
      log.info(
          "Parsing bodyParameterSchemaStack, bodyParameterDto $bodyParameterDto, schema type ${schema.type}")
      when (schema.type?.uppercase()
          ?: SchemaType.OBJECT
              .name) { // this is done to make allOf work as the allOf comes without type
        SchemaType.NUMBER.name,
        SchemaType.INTEGER.name ->
            parserParameterDtos.add(
                createParserParameterDto(bodyParameterDto, ParameterType.NUMBER))
        SchemaType.BOOLEAN.name ->
            parserParameterDtos.add(
                createParserParameterDto(bodyParameterDto, ParameterType.BOOLEAN))
        SchemaType.STRING.name ->
            parserParameterDtos.add(
                createParserParameterDto(bodyParameterDto, ParameterType.STRING))
        SchemaType.BINARY.name ->
            parserParameterDtos.add(createParserParameterDto(bodyParameterDto, ParameterType.FILE))
        SchemaType.OBJECT.name ->
            handleObjectSchema(schema, bodyParameterSchemaStack, bodyParameterDto)
        SchemaType.ARRAY.name ->
            handleArraySchema(schema, bodyParameterSchemaStack, bodyParameterDto, repeatableGroupId)
        else -> throw ValidationFailedException("Unable to parse schema type ${schema.type}")
      }
    }
    return parserParameterDtos
  }

  private fun createParserParameterDto(
      bodyParameterDto: BodyParameterDto,
      parameterType: ParameterType
  ): ParserParameterDto =
      ParserParameterDto(
          null,
          PlacementType.BODY,
          bodyParameterDto.destinationPath,
          null,
          null,
          parameterType,
          null,
          bodyParameterDto.isArray,
          bodyParameterDto.required,
          null,
          bodyParameterDto.repeatableGroupId,
          null)

  /**
   * Handles the object schema type, mainly each object sub property is pushed to the stack and
   * destination path is computed
   */
  private fun handleObjectSchema(
      schema: Schema<*>,
      bodyParameterSchemaStack: Stack<Pair<BodyParameterDto, Schema<*>>>,
      bodyParameterDto: BodyParameterDto
  ) {
    val requiredFields = schema.required?.toSet() ?: emptySet()

    if (!ParsingUtil.isReadOnly(schema)) {
      schema.properties?.let {
        it.entries.stream().forEach { entry ->
          pushToStack(
              bodyParameterSchemaStack,
              BodyParameterDto(
                  "${bodyParameterDto.destinationPath}/${ParsingUtil.formKey(entry)}",
                  !ParsingUtil.IS_ARRAY,
                  requiredFields.contains(entry.key),
                  null),
              entry.value)
        }
      }
    }
  }
  /**
   * Handles the array schema type, mainly each array item is pushed to the stack and destination
   * path is computed
   */
  private fun handleArraySchema(
      schema: Schema<*>,
      bodyParameterSchemaStack: Stack<Pair<BodyParameterDto, Schema<*>>>,
      bodyParameterDto: BodyParameterDto,
      repeatableGroupId: AtomicInteger
  ) {
    val requiredFields = schema.required?.toSet() ?: emptySet()

    if (!ParsingUtil.isReadOnly(schema)) {
      schema.items?.let {
        it.properties?.let { property ->
          repeatableGroupId.incrementAndGet()
          property.entries.stream().forEach { entry ->
            pushToStack(
                bodyParameterSchemaStack,
                createBodyParameterDto(bodyParameterDto, entry, requiredFields, repeatableGroupId),
                entry.value)
          }
        }
            ?: handlePrimitiveArrays(bodyParameterSchemaStack, bodyParameterDto, it)
      }
    }
  }

  private fun createBodyParameterDto(
      bodyParameterDto: BodyParameterDto,
      entry: MutableMap.MutableEntry<String, Schema<Any>>,
      requiredFields: Set<String>,
      repeatableGroupId: AtomicInteger
  ) =
      BodyParameterDto(
          "${ParsingUtil.formDestinationPath(bodyParameterDto.destinationPath)}/${ParsingUtil.formKey(entry)}",
          !ParsingUtil.IS_ARRAY,
          requiredFields.contains(entry.key),
          repeatableGroupId.get())

  private fun pushToStack(
      sourceRefIsArraySchemaStack: Stack<Pair<BodyParameterDto, Schema<*>>>,
      bodyParameterDto: BodyParameterDto,
      schema: Schema<*>
  ) {
    if (!ParsingUtil.isReadOnly(schema))
        sourceRefIsArraySchemaStack.push(Pair(bodyParameterDto, schema))
  }
  /**
   * Handles the array of primitive types, it does not need to have $ discriminator hence it needs
   * different handling than array of objects
   */
  private fun handlePrimitiveArrays(
      sourceRefIsArraySchemaStack: Stack<Pair<BodyParameterDto, Schema<*>>>,
      bodyParameterDto: BodyParameterDto,
      schema: Schema<*>
  ) =
      pushToStack(
          sourceRefIsArraySchemaStack,
          BodyParameterDto(
              ParsingUtil.formPrimitiveArrayDestinationPath(bodyParameterDto.destinationPath),
              ParsingUtil.IS_ARRAY,
              !IS_REQUIRED,
              null),
          schema)
  companion object {
      private const val FILE = "file"

      private const val IS_REQUIRED = true
      private const val IS_ARRAY = true
      private val log = slf4j<RequestBodyParserImpl>()

      data class BodyParameterDto(
          val destinationPath: String,
          val isArray: Boolean,
          val required: Boolean,
          val repeatableGroupId: Int?
      )
  }
}
