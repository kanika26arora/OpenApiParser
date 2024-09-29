package com.chat.openapiParser.parser.persistence.repository

import com.chat.openapiParser.parser.dto.ProviderVersionDto
import com.chat.openapiParser.parser.persistence.entity.ProviderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProviderRepository : JpaRepository<ProviderEntity, String> {


  @Query(
      """
            SELECT new com.chat.openapiParser.parser.dto.ProviderVersionDto(p.id, p.name, p.logoUrl, p.description, p.moreInfoUrl, sv.id, sv.versionLabel, sv.createdDate, false)
            FROM ProviderEntity p 
            JOIN ProviderSpecificationVersionEntity sv 
                ON p.id = sv.providerId 
      """)
  fun fetchAllProvidersWithSpecificationVersions(): List<ProviderVersionDto>

  fun existsByIdNotAndName(providerId: String, providerName: String): Boolean
}
