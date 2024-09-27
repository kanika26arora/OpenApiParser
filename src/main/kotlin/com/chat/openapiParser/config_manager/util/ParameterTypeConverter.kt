package com.chat.openapiParser.config_manager.util

import com.chat.openapiParser.config_manager.common.enums.ParameterType
import com.chat.openapiParser.config_manager.common.enums.SchemaType
import com.chat.openapiParser.config_manager.common.exception.ValidationFailedException

object ParameterTypeConverter {

  fun convert(value: String): ParameterType {
    return when (value.uppercase()) {
      SchemaType.NUMBER.name,
      SchemaType.INTEGER.name -> ParameterType.NUMBER
      SchemaType.STRING.name -> ParameterType.STRING
      SchemaType.BOOLEAN.name -> ParameterType.BOOLEAN
      else -> {
        throw ValidationFailedException("Cannot convert to the parameter type for $value")
      }
    }
  }
}
