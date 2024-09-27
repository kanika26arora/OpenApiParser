package com.chat.openapiParser.parser.dto.request

import com.chat.openapiParser.parser.common.enums.AuthType
import com.chat.openapiParser.parser.common.enums.GrantType
import com.chat.openapiParser.parser.common.dto.StaticSecretsDto

data class OAuthDetailsRequest(
    override val authType: AuthType,
    val grantType: GrantType,
    val authPath: String,
    val refreshPath: String,
    val tokenPath: String,
    val tokenExpirationDurationSeconds: Long,
    val scopes: List<String>,
    val authorizationHeaderName: String?,
    val staticSecrets: StaticSecretsDto?,
    val authorizationHeaderValuePrefix: String?
) : AuthDetailsRequest(authType)
