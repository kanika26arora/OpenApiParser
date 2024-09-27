package com.chat.openapiParser.config_manager.dto

import com.chat.openapiParser.config_manager.common.enums.ConstraintType

data class ConstraintDto(val type: ConstraintType, val value: String)
