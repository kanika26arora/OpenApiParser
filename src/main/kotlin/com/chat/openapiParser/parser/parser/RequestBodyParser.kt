package com.chat.openapiParser.parser.parser

import com.chat.openapiParser.parser.dto.parser.ParserParameterDto
import io.swagger.v3.oas.models.parameters.RequestBody

interface RequestBodyParser {
  fun parse(requestBody: RequestBody?): List<ParserParameterDto>

  fun getContentTypes(requestBody: RequestBody?): List<String>?
}
