package com.chat.openapiParser.parser.dto.response

import com.chat.openapiParser.parser.dto.ParameterInternalDto
import com.chat.openapiParser.parser.dto.ResponseDto

data class ProviderEndpointInternalResponse(
    val id: String,
    val operationName: String,
    val requestParameters: List<ParameterInternalDto>,
    val responseSample: List<ResponseDto>
)
