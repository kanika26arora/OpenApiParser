package com.chat.openapiParser.config_manager.dto.response

import com.chat.openapiParser.config_manager.dto.ParameterInternalDto
import com.chat.openapiParser.config_manager.dto.ResponseDto

data class ProviderEndpointInternalResponse(
    val id: String,
    val operationName: String,
    val requestParameters: List<ParameterInternalDto>,
    val responseSample: List<ResponseDto>
)
