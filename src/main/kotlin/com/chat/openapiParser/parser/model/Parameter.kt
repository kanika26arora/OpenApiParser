package com.chat.openapiParser.parser.model

import com.chat.openapiParser.parser.common.enums.ParameterType
import com.chat.openapiParser.parser.common.enums.PlacementType
import com.chat.openapiParser.parser.common.enums.PresentationType

data class Parameter(
    var id: String,
    var label: String?,
    var placementType: PlacementType,
    var destinationPath: String,
    var placeholder: String?,
    var default: String?,
    var required: Boolean,
    var constraints: List<Constraint>?,
    var parameterType: ParameterType,
    var availableOptions: List<AvailableSelectOptions>?,
    var isArray: Boolean?,
    var presentationType: PresentationType?,
    var repeatableGroupId: Int?,
    var repeatableGroupName: String?
)
