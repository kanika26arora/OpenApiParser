package com.chat.openapiParser.parser.dto

import java.time.Instant

data class ProviderVersionDto(
    val id: String,
    val name: String,
    val logoUrl: String,
    val description: String,
    val moreInfoUrl: String,
    val specificationVersionId: String,
    val specificationVersionLabel: String,
    val createdAt: Instant,
    val isConfigurationAdded: Boolean
)
