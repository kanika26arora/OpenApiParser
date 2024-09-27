package com.chat.openapiParser.config_manager.util

import com.chat.openapiParser.config_manager.common.enums.PlacementType
import com.chat.openapiParser.config_manager.common.exception.ValidationFailedException

object PlacementTypeConverter {

  fun convert(value: String): PlacementType {
    try {
      return PlacementType.valueOf(value.uppercase())
    } catch (exception: IllegalArgumentException) {
      throw ValidationFailedException("Cannot convert to the placement type for $value")
    }
  }
}
