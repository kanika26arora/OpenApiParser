package com.chat.openapiParser.parser.dto

import com.chat.openapiParser.parser.common.enums.ParameterType
import com.chat.openapiParser.parser.common.enums.PresentationType

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
