package com.chat.openapiParser.parser.mapper

import com.chat.openapiParser.parser.dto.request.ProviderRequest
import com.chat.openapiParser.parser.dto.response.ProviderResponse
import com.chat.openapiParser.parser.persistence.entity.ProviderEntity


interface ProviderMapper {

  fun toEntity(providerRequest: ProviderRequest): ProviderEntity

  fun toEntity(id: String, providerRequest: ProviderRequest): ProviderEntity

  fun toResponse(providerEntity: ProviderEntity): ProviderResponse

  fun merge(providerRequest: ProviderRequest, providerEntity: ProviderEntity): ProviderEntity
}
