package com.chat.openapiParser.config_manager.parser

import com.chat.openapiParser.config_manager.dto.parser.ParserEndpointInfoDto

interface OpenApiParser {
  fun parse(content: String): List<ParserEndpointInfoDto>
}
