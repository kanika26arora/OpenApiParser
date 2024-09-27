package com.chat.openapiParser.parser.parser

import com.chat.openapiParser.parser.dto.parser.ParserEndpointInfoDto

interface OpenApiParser {
  fun parse(content: String): List<ParserEndpointInfoDto>
}
