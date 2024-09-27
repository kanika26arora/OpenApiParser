package com.chat.openapiParser.parser.mapper

import com.chat.openapiParser.parser.dto.request.ProviderSpecificationVersionRequest
import com.chat.openapiParser.parser.dto.request.ProviderSpecificationVersionUpdateRequest
import com.chat.openapiParser.parser.dto.response.ProviderSpecificationVersionResponse
import com.chat.openapiParser.parser.dto.response.SpecificationResponse
import com.chat.openapiParser.parser.persistence.entity.ProviderSpecificationVersionEntity

interface SpecificationVersionMapper {

  fun toEntity(
      providerId: String,
      providerSpecificationVersionRequest: ProviderSpecificationVersionRequest
  ): ProviderSpecificationVersionEntity

  fun toResponse(
      specificationVersionEntity: ProviderSpecificationVersionEntity
  ): ProviderSpecificationVersionResponse

  fun toSpecificationResponse(
      specificationVersionEntity: ProviderSpecificationVersionEntity
  ): SpecificationResponse

  fun merge(
      versionId: String,
      providerSpecificationVersionEntity: ProviderSpecificationVersionEntity,
      providerSpecificationVersionRequest: ProviderSpecificationVersionUpdateRequest
  ): ProviderSpecificationVersionEntity
}
