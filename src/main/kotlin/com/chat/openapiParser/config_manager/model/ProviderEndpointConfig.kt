package com.chat.openapiParser.config_manager.model

import com.chat.openapiParser.config_manager.common.enums.ProviderEndpointType
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "providerEndpointType")
@JsonSubTypes(JsonSubTypes.Type(value = ProviderRestEndpointConfig::class, name = "REST"))
abstract class ProviderEndpointConfig(open var providerEndpointType: ProviderEndpointType)
