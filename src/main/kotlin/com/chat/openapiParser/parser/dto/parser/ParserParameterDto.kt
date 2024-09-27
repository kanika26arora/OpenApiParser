package com.chat.openapiParser.parser.dto.parser

import com.chat.openapiParser.parser.common.enums.ParameterType
import com.chat.openapiParser.parser.common.enums.PlacementType
import com.chat.openapiParser.parser.common.enums.PresentationType

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
