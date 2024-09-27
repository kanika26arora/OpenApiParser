package com.chat.openapiParser.parser.dto.parser

import com.chat.openapiParser.parser.common.enums.ParameterType

data class ParserResponsePropertyDto(
    val sourceRef: String,
    val label: String?,
    val type: ParameterType,
    val isArray: Boolean
)
