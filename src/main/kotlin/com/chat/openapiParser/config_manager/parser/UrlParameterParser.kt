package com.chat.openapiParser.config_manager.parser

import com.chat.openapiParser.config_manager.dto.parser.ParserParameterDto

interface UrlParameterParser {
  fun parse(parameters: MutableList<io.swagger.v3.oas.models.parameters.Parameter>?): List<ParserParameterDto>
}
