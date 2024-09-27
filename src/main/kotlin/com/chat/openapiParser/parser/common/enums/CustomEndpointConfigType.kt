package com.chat.openapiParser.parser.common.enums

enum class CustomEndpointConfigType(val typeId: Int) {

    RESPONSE(1),
    REQUEST(2);

    companion object{
        fun getByTypeId(typeId: Int) = CustomEndpointConfigType.values().find { it.typeId == typeId }
    }
}