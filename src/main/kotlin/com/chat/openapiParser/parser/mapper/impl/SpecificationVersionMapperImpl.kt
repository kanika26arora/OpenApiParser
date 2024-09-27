package com.chat.openapiParser.parser.mapper.impl

import com.chat.openapiParser.parser.dto.request.ProviderSpecificationVersionRequest
import com.chat.openapiParser.parser.dto.request.ProviderSpecificationVersionUpdateRequest
import com.chat.openapiParser.parser.dto.response.ProviderSpecificationVersionResponse
import com.chat.openapiParser.parser.dto.response.SpecificationResponse
import com.chat.openapiParser.parser.mapper.SpecificationVersionMapper
import com.chat.openapiParser.parser.persistence.entity.ProviderSpecificationVersionEntity
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SpecificationVersionMapperImpl : SpecificationVersionMapper {

  override fun toEntity(
      providerId: String,
      providerSpecificationVersionRequest: ProviderSpecificationVersionRequest
  ): ProviderSpecificationVersionEntity {
    val specificationVersionEntity = ProviderSpecificationVersionEntity()

    specificationVersionEntity.id = UUID.randomUUID().toString()
    specificationVersionEntity.providerId = providerId
    specificationVersionEntity.versionLabel = providerSpecificationVersionRequest.versionLabel
    specificationVersionEntity.openApiSpecS3Key =
        providerSpecificationVersionRequest.openApiSpecS3Key
    return specificationVersionEntity
  }

  override fun toResponse(
      specificationVersionEntity: ProviderSpecificationVersionEntity
  ): ProviderSpecificationVersionResponse {

    return ProviderSpecificationVersionResponse(
        specificationVersionEntity.id,
        specificationVersionEntity.providerId,
        specificationVersionEntity.versionLabel)
  }

  override fun toSpecificationResponse(
      specificationVersionEntity: ProviderSpecificationVersionEntity
  ): SpecificationResponse {

    return SpecificationResponse(
        specificationVersionEntity.id,
        specificationVersionEntity.versionLabel,
        specificationVersionEntity.openApiSpecS3Key)
  }

  override fun merge(
      versionId: String,
      providerSpecificationVersionEntity: ProviderSpecificationVersionEntity,
      providerSpecificationVersionRequest: ProviderSpecificationVersionUpdateRequest
  ): ProviderSpecificationVersionEntity {
    val entity = ProviderSpecificationVersionEntity()
    val authDetails = providerSpecificationVersionRequest.authDetails
    entity.id = versionId
    entity.providerId = providerSpecificationVersionEntity.providerId
    entity.versionLabel =
        providerSpecificationVersionRequest.versionLabel
            ?: providerSpecificationVersionEntity.versionLabel

    entity.openApiSpecS3Key =
        providerSpecificationVersionRequest.openApiSpecS3Key
            ?: providerSpecificationVersionEntity.openApiSpecS3Key
    return entity
  }

}
