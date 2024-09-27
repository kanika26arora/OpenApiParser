package com.chat.openapiParser.config_manager.parser

import com.chat.openapiParser.config_manager.dto.parser.ParserParameterDto
import io.swagger.v3.oas.models.parameters.RequestBody

interface RequestBodyParser {
  fun parse(requestBody: RequestBody?): List<ParserParameterDto>

  fun getContentTypes(requestBody: RequestBody?): List<String>?
}
