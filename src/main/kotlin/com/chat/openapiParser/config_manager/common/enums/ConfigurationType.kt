package com.chat.openapiParser.config_manager.common.enums

enum class ConfigurationType(val type: Int) {
  PRODUCTION(1),
  SANDBOX(2);

  companion object {
    fun getByValue(value: Int) = ConfigurationType.values().find { it.type == value }
  }
}
