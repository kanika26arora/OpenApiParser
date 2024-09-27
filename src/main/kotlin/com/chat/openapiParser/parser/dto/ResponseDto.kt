package com.chat.openapiParser.parser.dto

data class ResponseDto(
    val statusCode: Int,
    val properties: List<ResponsePropertyDto>?,
    val parentLabels: List<ParentLabelDto>?,
    val primaryFields: List<String>?
)
