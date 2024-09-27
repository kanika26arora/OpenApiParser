package com.chat.openapiParser.parser.common.dto

import com.chat.openapiParser.parser.common.enums.AuthType
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "authType",
    visible = true)
@JsonSubTypes(JsonSubTypes.Type(ProviderRestEndpointConfigDto::class, name = "OAUTH"))
abstract class AuthDetailsDto(val authType: AuthType)
