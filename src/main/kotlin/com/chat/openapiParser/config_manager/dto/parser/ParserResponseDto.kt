package com.chat.openapiParser.config_manager.dto.parser

import com.chat.openapiParser.config_manager.dto.parser.ParserResponsePropertyDto

data class ParserResponseDto(val statusCode: Int, val properties: List<ParserResponsePropertyDto>?)
