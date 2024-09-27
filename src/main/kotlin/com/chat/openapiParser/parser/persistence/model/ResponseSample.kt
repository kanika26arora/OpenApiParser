package com.chat.openapiParser.parser.persistence.model

import com.chat.openapiParser.parser.model.ResponseProperty

data class ResponseSample(
    var statusCode: Int, var properties: List<ResponseProperty>?,
    var labels: List<ParentLabel>?, var primaryFields: List<String>?
)