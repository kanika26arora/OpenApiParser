package com.chat.openapiParser.config_manager.dto.request

import com.chat.openapiParser.config_manager.common.enums.AuthType
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "authType",
    visible = true)
@JsonSubTypes(JsonSubTypes.Type(OAuthDetailsUpdateRequest::class, name = "OAUTH"))
abstract class AuthDetailsUpdateRequest(open val authType: AuthType)
