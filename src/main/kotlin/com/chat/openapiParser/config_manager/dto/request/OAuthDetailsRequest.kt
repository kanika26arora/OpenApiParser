package com.chat.openapiParser.config_manager.dto.request

import com.chat.openapiParser.config_manager.common.enums.AuthType
import com.chat.openapiParser.config_manager.common.enums.GrantType
import com.chat.openapiParser.config_manager.common.dto.StaticSecretsDto

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
