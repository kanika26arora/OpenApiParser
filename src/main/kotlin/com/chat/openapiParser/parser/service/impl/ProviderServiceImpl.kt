package com.chat.openapiParser.parser.service.impl

import com.chat.openapiParser.parser.common.exception.NotFoundException
import com.chat.openapiParser.parser.common.exception.PreconditionFailedException
import com.chat.openapiParser.parser.dto.ProviderVersionDto
import com.chat.openapiParser.parser.dto.request.ProviderRequest
import com.chat.openapiParser.parser.dto.response.InternalProviderVersionsResponse
import com.chat.openapiParser.parser.dto.response.ProviderResponse
import com.chat.openapiParser.parser.dto.response.Version
import com.chat.openapiParser.parser.mapper.ProviderMapper
import com.chat.openapiParser.parser.persistence.entity.ProviderEntity
import com.chat.openapiParser.parser.persistence.repository.ProviderRepository
import com.chat.openapiParser.parser.service.ProviderService
import org.springframework.stereotype.Service

@Service
class ProviderServiceImpl(
    private val providerRepository: ProviderRepository,
    private val providerMapper: ProviderMapper
) : ProviderService {

  override fun createProvider(provider: ProviderRequest): ProviderResponse {
    val providerEntity = providerMapper.toEntity(provider)
    val savedEntity = validateAndSaveProvider(providerEntity)
    return providerMapper.toResponse(savedEntity)
  }

  override fun updateProvider(id: String, provider: ProviderRequest): ProviderResponse {

    val providerEntity =
        providerRepository.findById(id).orElseThrow { NotFoundException("Provider not found") }

    val mergedProviderEntity = providerMapper.merge(provider, providerEntity)
    val savedEntity = validateAndSaveProvider(mergedProviderEntity)
    return providerMapper.toResponse(savedEntity)
  }
    override fun getAllProvidersWithSpecificationVersions(): List<InternalProviderVersionsResponse> {
        val providers: List<ProviderVersionDto> =
            providerRepository.fetchAllProvidersWithSpecificationVersions()
        return toConfiguredProvidersResponse(providers)
    }

    private fun toConfiguredProvidersResponse(providers: List<ProviderVersionDto>) =
        providers
            .groupBy { it.id }
            .map {
                val specificationsForProvider = it.value
                val specification = specificationsForProvider.first()
                InternalProviderVersionsResponse(
                    specification.id,
                    specification.name,
                    specification.logoUrl,
                    specification.description,
                    specification.moreInfoUrl,
                    mapVersions(specificationsForProvider))
            }

    private fun mapVersions(specificationsForProvider: List<ProviderVersionDto>): List<Version> {

        val sortedByDescending = specificationsForProvider.sortedByDescending { it.createdAt }

        val latest = sortedByDescending.first()
        val latestVersion = Version(latest.specificationVersionLabel, latest.name, true)

        val otherVersions =
            sortedByDescending.drop(1).map { Version(it.specificationVersionLabel, it.name, false) }

        return listOf(latestVersion) + otherVersions
    }

  override fun existsById(id: String): Boolean {
    return providerRepository.existsById(id)
  }

  override fun getAllProviders() =
      providerRepository.findAll().map { providerMapper.toResponse(it) }


  override fun getProviderDetails(providerId: String): ProviderResponse {
    val providerEntity =
        providerRepository.findById(providerId).orElseThrow {
          NotFoundException("Provider does not exist")
        }
    return providerMapper.toResponse(providerEntity)
  }

  private fun validateAndSaveProvider(providerEntity: ProviderEntity): ProviderEntity {

    val someOtherProviderExistsWithTheName =
        providerRepository.existsByIdNotAndName(providerEntity.id, providerEntity.name)

    if (someOtherProviderExistsWithTheName) {
      throw PreconditionFailedException("Provider name is already taken")
    }

    return providerRepository.save(providerEntity)
  }
}
