package com.chat.openapiParser.parser.parser.impl

import com.chat.openapiParser.parser.dto.parser.ParserEndpointInfoDto
import com.chat.openapiParser.parser.dto.parser.ParserProviderRestEndpointConfigDto
import com.chat.openapiParser.parser.parser.OpenApiParser
import com.chat.openapiParser.parser.parser.RequestBodyParser
import com.chat.openapiParser.parser.parser.ResponseParser
import com.chat.openapiParser.parser.parser.UrlParameterParser
import com.chat.openapiParser.parser.validator.OpenApiValidator
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.parser.OpenAPIV3Parser
import io.swagger.v3.parser.core.models.ParseOptions
import java.util.stream.Collectors
import org.springframework.stereotype.Component

@Component
class OpenApiParserImpl(
    private val urlParameterParser: UrlParameterParser,
    private val requestBodyParser: RequestBodyParser,
    private val responseParser: ResponseParser
) : OpenApiParser {
  override fun parse(content: String): List<ParserEndpointInfoDto> {
    val openAPI = parseOpenAPI(content)
    return openAPI.paths.flatMap { parseForPath(it.key, it.value.readOperationsMap()) }
  }

  private fun parseForPath(
      name: String,
      operationsMap: Map<PathItem.HttpMethod, Operation>
  ): List<ParserEndpointInfoDto> {
    return operationsMap.entries
        .stream()
        .map {
          ParserEndpointInfoDto(
              OpenApiValidator.validateAndGet(it.value.operationId, "operationId").toShort(),
              OpenApiValidator.validateAndGet(it.value.summary, "summary"),
              getRequestParameters(it),
              responseParser.parse(it.value.responses),
              getProviderRestEndpointConfigDto(name, it))
        }
        .collect(Collectors.toList())
  }

  private fun getProviderRestEndpointConfigDto(
      name: String,
      it: Map.Entry<PathItem.HttpMethod, Operation>
  ) =
      ParserProviderRestEndpointConfigDto(
          name, it.key.name, requestBodyParser.getContentTypes(it.value.requestBody))

  private fun getRequestParameters(it: Map.Entry<PathItem.HttpMethod, Operation>) =
      urlParameterParser.parse(it.value.parameters) + requestBodyParser.parse(it.value.requestBody)

  private fun parseOpenAPI(content: String): OpenAPI {
    return OpenAPIV3Parser().readContents(content, null, parseOptions).openAPI
  }

  companion object {
    private val parseOptions = ParseOptions()

    init {
      parseOptions.isResolve = true
      parseOptions.isResolveFully = true
    }
  }
}
