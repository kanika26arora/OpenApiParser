package com.chat.openapiParser.parser.common

import com.chat.openapiParser.parser.common.enums.ProviderEndpointType
import com.chat.openapiParser.parser.common.dto.ProviderRestEndpointConfigDto
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "providerEndpointType",
    visible = true)
@JsonSubTypes(JsonSubTypes.Type(ProviderRestEndpointConfigDto::class, name = "REST"))
abstract class ProviderEndpointConfigDto(val providerEndpointType: ProviderEndpointType)
