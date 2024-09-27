package com.chat.openapiParser.config_manager.dto.parser

data class ParserEndpointInfoDto(
    val operationId: Short,
    val name: String,
    val requestParameters: List<ParserParameterDto>,
    val response: ParserResponseSampleDto,
    val config: ParserProviderEndpointConfigDto
)
