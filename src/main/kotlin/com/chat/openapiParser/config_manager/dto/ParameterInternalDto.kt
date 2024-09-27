package com.chat.openapiParser.config_manager.dto

import com.chat.openapiParser.config_manager.common.enums.ParameterType
import com.chat.openapiParser.config_manager.common.enums.PresentationType
import com.chat.openapiParser.config_manager.dto.AvailableOptionDto
import com.chat.openapiParser.config_manager.dto.ConstraintDto

data class ParameterInternalDto(
    val id: String,
    val label: String?,
    val placeholder: String?,
    val default: String?,
    val required: Boolean,
    val constraints: List<ConstraintDto>?,
    val parameterType: ParameterType,
    val availableOptions: List<AvailableOptionDto>?,
    var isArray: Boolean?,
    var presentationType: PresentationType?,
    var repeatableGroupId: Int?,
    var repeatableGroupName: String?
)
