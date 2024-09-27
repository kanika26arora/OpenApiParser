package com.chat.openapiParser.config_manager.mapper

import com.chat.openapiParser.config_manager.dto.request.ProviderSpecificationVersionRequest
import com.chat.openapiParser.config_manager.dto.request.ProviderSpecificationVersionUpdateRequest
import com.chat.openapiParser.config_manager.dto.response.ProviderSpecificationVersionResponse
import com.chat.openapiParser.config_manager.dto.response.SpecificationResponse
import com.chat.openapiParser.config_manager.persistence.entity.ProviderSpecificationVersionEntity

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
