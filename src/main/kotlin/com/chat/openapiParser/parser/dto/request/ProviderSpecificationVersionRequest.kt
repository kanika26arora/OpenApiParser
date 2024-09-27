package com.chat.openapiParser.parser.dto.request

data class ProviderSpecificationVersionRequest(
    val versionLabel: String,
    val openApiSpecS3Key: String
)
