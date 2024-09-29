package com.chat.openapiParser.parser.service

import com.chat.openapiParser.parser.dto.request.ProviderRequest
import com.chat.openapiParser.parser.dto.response.InternalProviderVersionsResponse
import com.chat.openapiParser.parser.dto.response.ProviderResponse
import com.chat.openapiParser.parser.dto.response.ProviderVersionsResponse
interface ProviderService {

  fun createProvider(provider: ProviderRequest): ProviderResponse
  fun updateProvider(id: String, provider: ProviderRequest): ProviderResponse
  fun existsById(id: String): Boolean
  fun getAllProviders(): List<ProviderResponse>
  fun getProviderDetails(providerId: String): ProviderResponse
  fun getAllProvidersWithSpecificationVersions(): List<InternalProviderVersionsResponse>
}
