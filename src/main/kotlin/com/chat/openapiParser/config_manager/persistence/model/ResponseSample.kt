package com.chat.openapiParser.config_manager.persistence.model

import com.chat.openapiParser.config_manager.model.ResponseProperty

data class ResponseSample(
    var statusCode: Int, var properties: List<ResponseProperty>?,
    var labels: List<ParentLabel>?, var primaryFields: List<String>?
)