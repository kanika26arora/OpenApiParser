package com.chat.openapiParser.parser.util

import io.swagger.v3.oas.models.media.ArraySchema
import io.swagger.v3.oas.models.media.Schema

object ParsingUtil {
  const val ARRAY_DISCRIMINATOR = "$"
  const val IS_ARRAY = true

  fun formPrimitiveArrayDestinationPath(destinationPath: String): String {
    val lastIndex = destinationPath.lastIndexOf(ARRAY_DISCRIMINATOR)
    if (lastIndex != -1) {
      return removeLastOccurrence(destinationPath, lastIndex)
    }
    return destinationPath
  }

  private fun removeLastOccurrence(destinationPath: String, lastIndex: Int) =
      "${destinationPath.substring(0, lastIndex)}${destinationPath.substring(lastIndex + 1, destinationPath.length)}"

  fun formDestinationPath(value: String) =
      if (value.contains(ARRAY_DISCRIMINATOR)) value else "$ARRAY_DISCRIMINATOR$value"

  fun formKey(entry: MutableMap.MutableEntry<String, Schema<Any>>) =
      if (entry.value is ArraySchema) "$ARRAY_DISCRIMINATOR${entry.key}" else entry.key

  fun isWriteOnly(schema: Schema<*>) = schema.writeOnly ?: false

  fun isReadOnly(schema: Schema<*>) = schema.readOnly ?: false
}
