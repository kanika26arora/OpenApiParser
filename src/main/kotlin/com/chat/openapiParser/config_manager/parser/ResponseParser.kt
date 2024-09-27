package com.chat.openapiParser.config_manager.parser

import com.chat.openapiParser.config_manager.dto.parser.ParserResponseSampleDto

interface ResponseParser {
  fun parse(apiResponses: io.swagger.v3.oas.models.responses.ApiResponses): ParserResponseSampleDto
}
