package com.chat.openapiParser.config_manager.dto

import com.chat.openapiParser.config_manager.common.enums.ParameterType

data class ResponsePropertyDto(
    val sourceRef: String,
    val label: String?,
    val type: ParameterType,
    val isArray: Boolean
)
