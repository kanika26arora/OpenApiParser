package com.chat.openapiParser.config_manager.common.event

import java.time.Instant

data class ConfigAuthEvent(
    val configurationId: String,
    val secretName: String,
    val accessTokenExpiryTime: Instant?
)
