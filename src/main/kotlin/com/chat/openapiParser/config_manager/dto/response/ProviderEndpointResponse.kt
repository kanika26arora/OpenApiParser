package com.chat.openapiParser.config_manager.dto.response

import com.chat.openapiParser.config_manager.dto.EndpointResponseTemplateDto
import com.chat.openapiParser.config_manager.dto.ParameterDto
import com.chat.openapiParser.config_manager.dto.ResponseDto

data class ProviderEndpointResponse(
    val id: String,
    val operationName: String,
    val requestParameters: List<ParameterDto>,
    val responseSample: List<ResponseDto>,
    val endpointResponseTemplate: List<EndpointResponseTemplateDto>?
)
