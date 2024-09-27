package com.chat.openapiParser.parser.dto.parser

import com.chat.openapiParser.parser.common.enums.ProviderEndpointType

data class ParserProviderRestEndpointConfigDto(
    val url: String,
    val httpMethod: String,
    val contentTypes: List<String>?
) : ParserProviderEndpointConfigDto(ProviderEndpointType.REST)
