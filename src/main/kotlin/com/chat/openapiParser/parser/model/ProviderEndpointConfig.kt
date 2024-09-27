package com.chat.openapiParser.parser.model

import com.chat.openapiParser.parser.common.enums.ProviderEndpointType
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "providerEndpointType")
@JsonSubTypes(JsonSubTypes.Type(value = ProviderRestEndpointConfig::class, name = "REST"))
abstract class ProviderEndpointConfig(open var providerEndpointType: ProviderEndpointType)
