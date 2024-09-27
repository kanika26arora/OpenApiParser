package com.chat.openapiParser.parser.model

import com.chat.openapiParser.parser.common.enums.ConstraintType

data class Constraint(var constraintType: ConstraintType, var value: String)
