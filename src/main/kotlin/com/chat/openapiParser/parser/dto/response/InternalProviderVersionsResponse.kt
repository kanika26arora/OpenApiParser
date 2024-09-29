package com.chat.openapiParser.parser.dto.response

data class InternalProviderVersionsResponse(
    override val id: String,
    override val name: String,
    override val logoUrl: String,
    override val description: String,
    override val moreInfoUrl: String,
    override val versions: List<Version>
) : AbstractProviderVersionsResponse(id, name, logoUrl, description, moreInfoUrl, versions)
