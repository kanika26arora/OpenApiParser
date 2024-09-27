package com.chat.openapiParser.config_manager.model

import com.chat.openapiParser.config_manager.common.enums.CustomEndpointConfigType
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "customEndpointConfigType"
)
@JsonSubTypes(JsonSubTypes.Type(value = CustomEndpointResponse::class, name = "RESPONSE"))
abstract class CustomEndpointConfig(open var customEndpointConfigType: CustomEndpointConfigType) {
}