package com.chat.openapiParser.parser.persistence.repository

import com.chat.openapiParser.parser.dto.EndpointInfoShortDto
import com.chat.openapiParser.parser.persistence.entity.ProviderEndpointEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProviderEndpointRepository : JpaRepository<ProviderEndpointEntity, String> {

  @Query(
      """
        SELECT new com.chat.openapiParser.parser.dto.EndpointInfoShortDto(ei.id, ei.operationName) 
        FROM ProviderEndpointEntity ei
        WHERE ei.providerSpecificationVersionId = ?1
    """)
  fun fetchEndpointsByProviderSpecificationVersionId(
      providerSpecificationVersionId: String
  ): List<EndpointInfoShortDto>

  fun findByProviderSpecificationVersionId(
      providerSpecificationVersionId: String
  ): List<ProviderEndpointEntity>
}
