package com.chat.openapiParser.config_manager.persistence.repository

import com.chat.openapiParser.config_manager.persistence.entity.ProviderSpecificationVersionEntity
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
