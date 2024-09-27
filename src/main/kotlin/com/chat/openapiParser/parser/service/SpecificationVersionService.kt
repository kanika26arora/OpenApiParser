package com.chat.openapiParser.parser.service

import com.chat.openapiParser.parser.dto.request.ProviderSpecificationVersionRequest
import com.chat.openapiParser.parser.dto.request.ProviderSpecificationVersionUpdateRequest
import com.chat.openapiParser.parser.dto.response.ProviderSpecificationVersionResponse
import com.chat.openapiParser.parser.dto.response.SpecificationResponse
import org.springframework.web.multipart.MultipartFile

interface SpecificationVersionService {

  fun getSpecificationVersion(specificationVersionId: String): SpecificationResponse

  fun getNewestSpecificationVersionByProviderId(providerId: String): SpecificationResponse

  fun uploadSpecification(providerId: String, version: String, file: MultipartFile): String

  fun createSpecificationForProvider(
      providerId: String,
      specificationRequest: ProviderSpecificationVersionRequest
  ): ProviderSpecificationVersionResponse

  fun updateSpecificationForProvider(
      versionId: String,
      specificationRequest: ProviderSpecificationVersionUpdateRequest
  ): ProviderSpecificationVersionResponse

  fun getSpecificationsForProvider(providerId: String): List<SpecificationResponse>

  fun getSpecificVersion(version: String?, providerId: String): String

  fun findByProviderIdAndVersion(providerId: String, version: String): String

  fun findFirstByProviderIdOrderByCreatedDateDesc(providerId: String): String
}
