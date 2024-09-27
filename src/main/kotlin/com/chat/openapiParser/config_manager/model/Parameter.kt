package com.chat.openapiParser.config_manager.model

import com.chat.openapiParser.config_manager.model.AvailableSelectOptions
import com.chat.openapiParser.config_manager.model.Constraint
import com.chat.openapiParser.config_manager.common.enums.ParameterType
import com.chat.openapiParser.config_manager.common.enums.PlacementType
import com.chat.openapiParser.config_manager.common.enums.PresentationType

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
