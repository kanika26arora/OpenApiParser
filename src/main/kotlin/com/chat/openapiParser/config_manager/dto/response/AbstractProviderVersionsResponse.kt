package com.chat.openapiParser.config_manager.dto.response

abstract class AbstractProviderVersionsResponse(
    open val id: String,
    open val name: String,
    open val logoUrl: String,
    open val description: String,
    open val moreInfoUrl: String,
    open val versions: List<Version>
)

data class Version(val id: String, val version: String, val latest: Boolean)
