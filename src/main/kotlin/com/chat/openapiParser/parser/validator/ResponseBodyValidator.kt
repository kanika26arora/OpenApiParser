package com.chat.openapiParser.parser.validator

import com.chat.openapiParser.parser.common.exception.ValidationFailedException
import io.swagger.v3.oas.models.responses.ApiResponses

private const val SUCCESS_HTTP_STATUS_CODE_PREFIX = "2"

object ResponseBodyValidator {

  fun validate2xxStatusCode(apiResponses: ApiResponses) {
    val result =
        apiResponses.entries
            .stream()
            .filter { it.key.startsWith(SUCCESS_HTTP_STATUS_CODE_PREFIX) }
            .findAny()
    if (!result.isPresent) {
      throw ValidationFailedException("Atleast one 2xx response schema should be present")
    }
  }
}
