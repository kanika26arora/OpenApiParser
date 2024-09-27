package com.chat.openapiParser.config_manager.model

import com.chat.openapiParser.config_manager.common.enums.CustomEndpointConfigType

data class CustomEndpointResponse(var sourceRef: String, var order: Int) :
        CustomEndpointConfig(CustomEndpointConfigType.RESPONSE)