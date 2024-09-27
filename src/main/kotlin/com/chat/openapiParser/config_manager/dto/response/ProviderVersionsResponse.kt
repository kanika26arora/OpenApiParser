package com.chat.openapiParser.config_manager.dto.response

import com.chat.openapiParser.config_manager.dto.response.AbstractProviderVersionsResponse
import com.chat.openapiParser.config_manager.dto.response.Version

data class ProviderVersionsResponse(
    override val id: String,
    override val name: String,
    override val logoUrl: String,
    override val description: String,
    override val moreInfoUrl: String,
    override val versions: List<Version>,
    val isAdded: Boolean
) : AbstractProviderVersionsResponse(id, name, logoUrl, description, moreInfoUrl, versions)
