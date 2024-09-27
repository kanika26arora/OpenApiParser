package com.chat.openapiParser.parser.dto

import com.chat.openapiParser.parser.common.enums.ParameterType

data class ResponsePropertyDto(
    val sourceRef: String,
    val label: String?,
    val type: ParameterType,
    val isArray: Boolean
)
