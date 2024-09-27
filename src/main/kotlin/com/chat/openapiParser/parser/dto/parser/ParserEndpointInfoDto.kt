package com.chat.openapiParser.parser.dto.parser

data class ParserEndpointInfoDto(
    val operationId: Short,
    val name: String,
    val requestParameters: List<ParserParameterDto>,
    val response: ParserResponseSampleDto,
    val config: ParserProviderEndpointConfigDto
)
