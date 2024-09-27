package com.chat.openapiParser.config_manager.model

import com.chat.openapiParser.config_manager.common.enums.ParameterType

data class ResponseProperty(
    var sourceRef: String,
    var label: String?,
    var type: ParameterType,
    var isArray: Boolean
)
