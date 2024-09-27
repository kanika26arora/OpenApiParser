package com.chat.openapiParser.parser.parser

import com.chat.openapiParser.parser.dto.parser.ParserParameterDto

interface UrlParameterParser {
  fun parse(parameters: MutableList<io.swagger.v3.oas.models.parameters.Parameter>?): List<ParserParameterDto>
}
