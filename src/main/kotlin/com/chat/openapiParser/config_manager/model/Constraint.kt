package com.chat.openapiParser.config_manager.model

import com.chat.openapiParser.config_manager.common.enums.ConstraintType

data class Constraint(var constraintType: ConstraintType, var value: String)
