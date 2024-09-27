package com.chat.openapiParser.config_manager.parser.impl

import com.chat.openapiParser.config_manager.common.enums.ParameterType
import com.chat.openapiParser.config_manager.common.enums.SchemaType
import com.chat.openapiParser.config_manager.common.exception.ValidationFailedException
import com.chat.openapiParser.config_manager.common.logging.slf4j
import com.chat.openapiParser.config_manager.dto.parser.ParserResponseDto
import com.chat.openapiParser.config_manager.dto.parser.ParserResponsePropertyDto
import com.chat.openapiParser.config_manager.dto.parser.ParserResponseSampleDto
import com.chat.openapiParser.config_manager.parser.ResponseParser
import com.chat.openapiParser.config_manager.parser.config.OpenApiParserMediaTypeConfig
import com.chat.openapiParser.config_manager.util.ParsingUtil
import com.chat.openapiParser.config_manager.util.ParsingUtil.ARRAY_DISCRIMINATOR
import com.chat.openapiParser.config_manager.util.ParsingUtil.IS_ARRAY
import com.chat.openapiParser.config_manager.validator.OpenApiValidator
import com.chat.openapiParser.config_manager.validator.ResponseBodyValidator
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponses
import java.util.*
import java.util.stream.Collectors
import org.springframework.stereotype.Component

@Component
class ResponseParserImpl(private val mediaTypeConfig: OpenApiParserMediaTypeConfig) :
    ResponseParser {
  override fun parse(apiResponses: ApiResponses): ParserResponseSampleDto {
    log.info("Parsing apiResponses")
    val responses = OpenApiValidator.validateAndGet(apiResponses, "apiResponses")
    ResponseBodyValidator.validate2xxStatusCode(responses)
    return ParserResponseSampleDto(
        responses.entries
            .stream()
            .map {
              ParserResponseDto(
                  Integer.valueOf(it.key),
                  it.value?.content?.entries?.let { contentEntries ->
                    parseContents(contentEntries)
                  })
            }
            .collect(Collectors.toList()))
  }
  /** Parses the method into the supported application content types and schema. */
  private fun parseContents(
      contentEntries: MutableSet<MutableMap.MutableEntry<String, MediaType>>
  ): List<ParserResponsePropertyDto>? {
    val jsonEntry =
        OpenApiValidator.validateAndFilterEntries(contentEntries, mediaTypeConfig.responseBody)
    log.info("Parsing response body content")
    return jsonEntry.value.schema?.properties?.let { parseProperties(it) }
        ?: jsonEntry.value.schema?.items?.let { parseArrayItems(it) }
  }

  /** Parses the individual properties of the json object. */
  private fun parseProperties(
      properties: Map<String, Schema<Any>>
  ): MutableList<ParserResponsePropertyDto> {
    val parserResponsePropertyDtos = mutableListOf<ParserResponsePropertyDto>()
    properties.entries.stream().forEach {
      val prefix = it.key

      val sourceRefIsArraySchemaStack = Stack<Triple<String, Boolean, Schema<*>>>()
      pushToStack(sourceRefIsArraySchemaStack, prefix, !IS_ARRAY, it.value)
      log.debug("Pushing to bodyParameterSchemaStack size ${sourceRefIsArraySchemaStack.size}")
      parserResponsePropertyDtos.addAll(parseSchema(sourceRefIsArraySchemaStack))
    }
    return parserResponsePropertyDtos
  }
  private fun parseArrayItems(items: Schema<*>): MutableList<ParserResponsePropertyDto> {
    val parserResponsePropertyDtos = mutableListOf<ParserResponsePropertyDto>()
    val sourceRefIsArraySchemaStack = Stack<Triple<String, Boolean, Schema<*>>>()
    pushToStack(sourceRefIsArraySchemaStack, items.name ?: ARRAY_DISCRIMINATOR, !IS_ARRAY, items)
    log.debug("Pushing to bodyParameterSchemaStack size ${sourceRefIsArraySchemaStack.size}")
    parserResponsePropertyDtos.addAll(parseSchema(sourceRefIsArraySchemaStack))

    return parserResponsePropertyDtos
  }

  /**
   * Generates the final ParserResponsePropertyDto list by individually reading and parsing the json
   * properties until all the nested levels have been processed
   */
  private fun parseSchema(
      sourceRefIsArraySchemaStack: Stack<Triple<String, Boolean, Schema<*>>>
  ): MutableList<ParserResponsePropertyDto> {
    val parserResponsePropertyDtos = mutableListOf<ParserResponsePropertyDto>()
    log.debug("Parsing response body schema")
    while (sourceRefIsArraySchemaStack.isNotEmpty()) {
      val sourceRefSchemaTriple = sourceRefIsArraySchemaStack.pop()

      val rootSourceRef = sourceRefSchemaTriple.first
      val isArray = sourceRefSchemaTriple.second
      val schema = sourceRefSchemaTriple.third
      log.info(
          "Parsing sourceRefIsArraySchemaStack, rootSourceRef $rootSourceRef,isArray $isArray, schema type ${schema.type}")
      when (schema.type?.uppercase()
          ?: SchemaType.OBJECT
              .name) { // this is done to make allOf work as the allOf comes without type
        SchemaType.NUMBER.name,
        SchemaType.INTEGER.name ->
            parserResponsePropertyDtos.add(
                ParserResponsePropertyDto(rootSourceRef, null, ParameterType.NUMBER, isArray)
            )
        SchemaType.BOOLEAN.name ->
            parserResponsePropertyDtos.add(
                ParserResponsePropertyDto(rootSourceRef, null, ParameterType.BOOLEAN, isArray)
            )
        SchemaType.STRING.name ->
            parserResponsePropertyDtos.add(
                ParserResponsePropertyDto(rootSourceRef, null, ParameterType.STRING, isArray)
            )
        SchemaType.OBJECT.name ->
            handleObjectSchema(schema, sourceRefIsArraySchemaStack, rootSourceRef)
        SchemaType.ARRAY.name ->
            handleArraySchema(schema, sourceRefIsArraySchemaStack, rootSourceRef)
        else -> throw ValidationFailedException("Unable to parse schema type ${schema.type}")
      }
    }
    return parserResponsePropertyDtos
  }
  /**
   * Handles the object schema type, mainly each object sub property is pushed to the stack and
   * sourceRef is computed
   */
  private fun handleObjectSchema(
      schema: Schema<*>,
      sourceRefIsArraySchemaStack: Stack<Triple<String, Boolean, Schema<*>>>,
      rootSourceRef: String
  ) {
    if (!ParsingUtil.isWriteOnly(schema)) {
      schema.properties?.let {
        it.entries.stream().forEach { entry ->
          pushToStack(
              sourceRefIsArraySchemaStack,
              "$rootSourceRef/${ParsingUtil.formKey(entry)}",
              !IS_ARRAY,
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
      sourceRefIsArraySchemaStack: Stack<Triple<String, Boolean, Schema<*>>>,
      rootSourceRef: String
  ) {
    if (!ParsingUtil.isWriteOnly(schema)) {
      schema.items?.let {
        it.properties?.let { property ->
          property.entries.stream().forEach { entry ->
            pushToStack(
                sourceRefIsArraySchemaStack,
                "${ParsingUtil.formDestinationPath(rootSourceRef)}/${ParsingUtil.formKey(entry)}",
                !IS_ARRAY,
                entry.value)
          }
        }
            ?: handlePrimitiveArrays(sourceRefIsArraySchemaStack, rootSourceRef, it)
      }
    }
  }
  private fun pushToStack(
      sourceRefIsArraySchemaStack: Stack<Triple<String, Boolean, Schema<*>>>,
      rootSourceRef: String,
      isArray: Boolean,
      schema: Schema<*>
  ) {
    if (!ParsingUtil.isWriteOnly(schema))
        sourceRefIsArraySchemaStack.push(Triple(rootSourceRef, isArray, schema))
  }
  /**
   * Handles the array of primitive types, it does not need to have $ discriminator hence it needs
   * different handling than array of objects
   */
  private fun handlePrimitiveArrays(
      sourceRefIsArraySchemaStack: Stack<Triple<String, Boolean, Schema<*>>>,
      rootSourceRef: String,
      schema: Schema<*>
  ) =
      pushToStack(
          sourceRefIsArraySchemaStack,
          ParsingUtil.formPrimitiveArrayDestinationPath(rootSourceRef),
          IS_ARRAY,
          schema)

  companion object {
    private val log = slf4j<ResponseParserImpl>()
  }
}
