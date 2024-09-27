package com.chat.openapiParser.config_manager.dto.parser

import com.chat.openapiParser.config_manager.dto.parser.ParserProviderEndpointConfigDto
import com.chat.openapiParser.config_manager.common.enums.ProviderEndpointType

data class ParserProviderRestEndpointConfigDto(
    val url: String,
    val httpMethod: String,
    val contentTypes: List<String>?
) : ParserProviderEndpointConfigDto(ProviderEndpointType.REST)
