package com.chat.openapiParser.parser.common.dto

import com.chat.openapiParser.parser.common.enums.ProviderEndpointType

data class ProviderRestEndpointConfigDto(
    val url: String,
    val httpMethod: String,
    val contentTypes: List<String>?
) : ProviderEndpointConfigDto(ProviderEndpointType.REST)
