package com.chat.openapiParser.config_manager.common.dto

import com.chat.openapiParser.config_manager.common.enums.ProviderEndpointType

data class ProviderRestEndpointConfigDto(
    val url: String,
    val httpMethod: String,
    val contentTypes: List<String>?
) : ProviderEndpointConfigDto(ProviderEndpointType.REST)
