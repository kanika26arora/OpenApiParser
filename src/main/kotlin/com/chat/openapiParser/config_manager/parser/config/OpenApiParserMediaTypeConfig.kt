package com.chat.openapiParser.config_manager.parser.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "open-api-parser.media-types")
@Component
class OpenApiParserMediaTypeConfig {
  var requestBody: List<String> = emptyList()
  var responseBody: List<String> = emptyList()
}
