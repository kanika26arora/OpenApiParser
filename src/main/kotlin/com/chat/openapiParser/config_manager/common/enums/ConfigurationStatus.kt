package com.chat.openapiParser.config_manager.common.enums

enum class ConfigurationStatus(val status: Int, val active: Boolean) {
  AUTH_PENDING(0, false),
  ACTIVE(1, true),
  DISABLED(2, false),
  EXPIRING_SOON(3, true),
  EXPIRED(4, false);

  companion object {
    fun getByValue(value: Int) = ConfigurationStatus.values().find { it.status == value }
    fun getStatusIds(active: Boolean) = ConfigurationStatus.values().filter { it.active == active }
  }
}
