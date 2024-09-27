package com.chat.openapiParser.parser.parser

import com.chat.openapiParser.parser.dto.parser.ParserResponseSampleDto

interface ResponseParser {
  fun parse(apiResponses: io.swagger.v3.oas.models.responses.ApiResponses): ParserResponseSampleDto
}
