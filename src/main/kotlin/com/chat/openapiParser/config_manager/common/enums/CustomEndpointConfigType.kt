package com.chat.openapiParser.config_manager.common.enums

enum class CustomEndpointConfigType(val typeId: Int) {

    RESPONSE(1),
    REQUEST(2);

    companion object{
        fun getByTypeId(typeId: Int) = CustomEndpointConfigType.values().find { it.typeId == typeId }
    }
}