package com.chat.openapiParser.parser.dto

import com.chat.openapiParser.parser.common.enums.PresentationType

data class CustomParameterDto(
    val id: String,
    val label: String?,
    val placeholder: String?,
    val default: String?,
    val constraints: List<ConstraintDto>?,
    val availableOptions: List<AvailableOptionDto>?,
    var presentationType: PresentationType?,
    var repeatableGroupName: String?,
)
