package com.chat.openapiParser.parser.model

import com.chat.openapiParser.parser.common.enums.ParameterType

data class ResponseProperty(
    var sourceRef: String,
    var label: String?,
    var type: ParameterType,
    var isArray: Boolean
)
