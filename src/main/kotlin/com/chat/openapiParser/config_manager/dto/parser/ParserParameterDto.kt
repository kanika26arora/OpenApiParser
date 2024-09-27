package com.chat.openapiParser.config_manager.dto.parser

import com.chat.openapiParser.config_manager.common.enums.ParameterType
import com.chat.openapiParser.config_manager.common.enums.PlacementType
import com.chat.openapiParser.config_manager.common.enums.PresentationType

data class ParserParameterDto(
    val label: String?,
    val placementType: PlacementType,
    val destinationPath: String,
    val placeholder: String?,
    val default: String?,
    val parameterType: ParameterType,
    val availableOptions: List<ParserAvailableOptionDto>?,
    var isArray: Boolean?,
    var required: Boolean,
    var presentationType: PresentationType?,
    var repeatableGroupId: Int?,
    var repeatableGroupName: String?
)
