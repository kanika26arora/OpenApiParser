package com.chat.openapiParser.config_manager.dto.parser

import com.chat.openapiParser.config_manager.common.enums.ParameterType

data class ParserResponsePropertyDto(
    val sourceRef: String,
    val label: String?,
    val type: ParameterType,
    val isArray: Boolean
)
