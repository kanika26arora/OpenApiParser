package com.chat.openapiParser.config_manager.model

import com.chat.openapiParser.config_manager.common.enums.ProviderEndpointType
import org.springframework.http.HttpMethod

class ProviderRestEndpointConfig(
    var url: String,
    var httpMethod: HttpMethod,
    var contentTypes: List<String>?
) : ProviderEndpointConfig(ProviderEndpointType.REST) {

    fun getHttpMethod(): String {
        return httpMethod.name()
    }
}
