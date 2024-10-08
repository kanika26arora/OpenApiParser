package com.chat.openapiParser.parser.parser.impl

import com.chat.openapiParser.parser.common.logging.slf4j
import com.chat.openapiParser.parser.dto.parser.ParserParameterDto
import com.chat.openapiParser.parser.parser.UrlParameterParser
import com.chat.openapiParser.parser.util.ParameterTypeConverter
import com.chat.openapiParser.parser.util.PlacementTypeConverter
import io.swagger.v3.oas.models.parameters.Parameter
import java.util.stream.Collectors
import org.springframework.stereotype.Component

/** Parses the query, path and header parameters from open api spec */
@Component
class UrlParameterParserImpl : UrlParameterParser {
  override fun parse(parameters: MutableList<Parameter>?): List<ParserParameterDto> {
    if (parameters == null) {
      return emptyList()
    }
    log.info("Parsing url parameters, parameters list size ${parameters.size}")
    return parameters
        .stream()
        .map {
          ParserParameterDto(
              null,
              PlacementTypeConverter.convert(it.`in`),
              it.name,
              null,
              null,
              ParameterTypeConverter.convert(it.schema.type),
              null,
              null,
              it.required,
              null,
              null,
              null)
        }
        .collect(Collectors.toList())
  }
  companion object {
    private val log = slf4j<UrlParameterParserImpl>()
  }
}
