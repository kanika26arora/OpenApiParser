package com.chat.openapiParser.parser.persistence.repository

import com.chat.openapiParser.parser.persistence.entity.ProviderSpecificationVersionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProviderSpecificationVersionRepository :
    JpaRepository<ProviderSpecificationVersionEntity, String> {

  fun findFirstByProviderIdOrderByCreatedDateDesc(
      providerId: String
  ): ProviderSpecificationVersionEntity?

  fun findByProviderIdAndVersionLabel(
      providerId: String,
      versionLabel: String
  ): ProviderSpecificationVersionEntity?

  fun findAllByProviderId(providerId: String): List<ProviderSpecificationVersionEntity>
}
