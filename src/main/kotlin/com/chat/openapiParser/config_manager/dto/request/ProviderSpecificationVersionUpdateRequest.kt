package com.chat.openapiParser.config_manager.dto.request

import com.chat.openapiParser.config_manager.dto.request.OAuthDetailsUpdateRequest

data class ProviderSpecificationVersionUpdateRequest(
    val versionLabel: String?,
    val openApiSpecS3Key: String?,
    val authDetails: OAuthDetailsUpdateRequest?
)
