package com.chat.openapiParser.parser.model

import com.chat.openapiParser.parser.common.enums.CustomEndpointConfigType

data class CustomEndpointResponse(var sourceRef: String, var order: Int) :
        CustomEndpointConfig(CustomEndpointConfigType.RESPONSE)