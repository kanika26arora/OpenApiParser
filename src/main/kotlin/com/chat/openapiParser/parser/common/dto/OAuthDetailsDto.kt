package com.chat.openapiParser.parser.common.dto

import com.chat.openapiParser.parser.common.enums.AuthType
import com.chat.openapiParser.parser.common.enums.GrantType

data class OAuthDetailsDto(
    val grantType: GrantType,
    val authPath: String,
    val refreshPath: String,
    val tokenPath: String,
    val tokenExpirationDurationSeconds: Long,
    val scopes: List<String>,
    val staticSecrets: StaticSecretsDto?,
    val authorizationHeaderValuePrefix: String?,
    val authorizationHeaderName: String?
) : AuthDetailsDto(AuthType.OAUTH)
