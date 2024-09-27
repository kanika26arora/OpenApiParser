package com.chat.openapiParser.parser.util

import com.chat.openapiParser.parser.common.enums.PlacementType
import com.chat.openapiParser.parser.common.exception.ValidationFailedException

object PlacementTypeConverter {

  fun convert(value: String): PlacementType {
    try {
      return PlacementType.valueOf(value.uppercase())
    } catch (exception: IllegalArgumentException) {
      throw ValidationFailedException("Cannot convert to the placement type for $value")
    }
  }
}
