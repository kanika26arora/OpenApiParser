package com.chat.openapiParser.config_manager.dto

data class ResponseDto(
    val statusCode: Int,
    val properties: List<ResponsePropertyDto>?,
    val parentLabels: List<ParentLabelDto>?,
    val primaryFields: List<String>?
)
