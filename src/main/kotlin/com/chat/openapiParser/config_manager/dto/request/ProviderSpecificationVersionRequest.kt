package com.chat.openapiParser.config_manager.dto.request

import com.chat.openapiParser.config_manager.dto.request.AuthDetailsRequest

data class ProviderSpecificationVersionRequest(
    val versionLabel: String,
    val openApiSpecS3Key: String
)
