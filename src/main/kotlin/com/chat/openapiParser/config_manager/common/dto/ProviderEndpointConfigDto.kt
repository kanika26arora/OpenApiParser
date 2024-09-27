package com.chat.openapiParser.config_manager.common.dto

import com.chat.openapiParser.config_manager.common.dto.ProviderRestEndpointConfigDto
import com.chat.openapiParser.config_manager.common.enums.ProviderEndpointType
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "providerEndpointType",
    visible = true)
@JsonSubTypes(JsonSubTypes.Type(ProviderRestEndpointConfigDto::class, name = "REST"))
abstract class ProviderEndpointConfigDto(val providerEndpointType: ProviderEndpointType)
