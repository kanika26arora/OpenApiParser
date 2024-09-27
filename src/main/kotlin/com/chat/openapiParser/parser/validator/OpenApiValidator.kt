package com.chat.openapiParser.parser.validator

import com.chat.openapiParser.parser.common.exception.ValidationFailedException
import io.swagger.v3.oas.models.media.MediaType

object OpenApiValidator {
  fun <T> validateAndGet(value: T?, parameterName: String): T {
    return value ?: throw ValidationFailedException("Open api $parameterName is empty")
  }

  fun validateAndFilterEntries(
      contentEntries: MutableSet<MutableMap.MutableEntry<String, MediaType>>,
      mediaTypes: List<String>
  ): MutableMap.MutableEntry<String, MediaType> {
    if (contentEntries.stream().anyMatch { !mediaTypes.contains(it.key) }) {
      throw ValidationFailedException("Unsupported media types found")
    }
    return contentEntries.random()
  }
}
