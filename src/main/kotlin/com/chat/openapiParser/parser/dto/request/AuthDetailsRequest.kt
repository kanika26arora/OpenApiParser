package com.chat.openapiParser.parser.dto.request

import com.chat.openapiParser.parser.common.enums.AuthType
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "authType",
    visible = true)
@JsonSubTypes(JsonSubTypes.Type(OAuthDetailsRequest::class, name = "OAUTH"))
abstract class AuthDetailsRequest(open val authType: AuthType)
