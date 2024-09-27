package com.chat.openapiParser.parser.dto.response

import com.chat.openapiParser.parser.dto.EndpointResponseTemplateDto
import com.chat.openapiParser.parser.dto.ParameterDto
import com.chat.openapiParser.parser.dto.ResponseDto

data class ProviderEndpointResponse(
    val id: String,
    val operationName: String,
    val requestParameters: List<ParameterDto>,
    val responseSample: List<ResponseDto>,
    val endpointResponseTemplate: List<EndpointResponseTemplateDto>?
)
