package com.chat.openapiParser.parser.dto

data class CustomResponseDto(
    val statusCode: Int,
    val properties: List<CustomResponsePropertyDto>?,
    val parentLabels: List<ParentLabelDto>?,
    val primaryFields: List<String>?
)
