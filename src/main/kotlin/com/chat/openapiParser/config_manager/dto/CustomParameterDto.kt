package com.chat.openapiParser.config_manager.dto

import com.chat.openapiParser.config_manager.common.enums.PresentationType
import com.chat.openapiParser.config_manager.dto.AvailableOptionDto
import com.chat.openapiParser.config_manager.dto.ConstraintDto

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
