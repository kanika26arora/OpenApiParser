package com.chat.openapiParser.parser.dto.request

data class ProviderSpecificationVersionUpdateRequest(
    val versionLabel: String?,
    val openApiSpecS3Key: String?,
    val authDetails: OAuthDetailsUpdateRequest?
)
