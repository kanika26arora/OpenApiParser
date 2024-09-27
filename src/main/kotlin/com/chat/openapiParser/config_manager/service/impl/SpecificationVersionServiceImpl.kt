package com.chat.openapiParser.config_manager.service.impl

import com.chat.openapiParser.config_manager.common.exception.NotFoundException
import com.chat.openapiParser.config_manager.common.exception.ValidationFailedException
import com.chat.openapiParser.config_manager.common.logging.slf4j
import com.chat.openapiParser.config_manager.dto.parser.ParserEndpointInfoDto
import com.chat.openapiParser.config_manager.dto.request.ProviderSpecificationVersionRequest
import com.chat.openapiParser.config_manager.dto.request.ProviderSpecificationVersionUpdateRequest
import com.chat.openapiParser.config_manager.dto.response.ProviderSpecificationVersionResponse
import com.chat.openapiParser.config_manager.dto.response.SpecificationResponse
import com.chat.openapiParser.config_manager.mapper.EndpointInfoMapper
import com.chat.openapiParser.config_manager.mapper.SpecificationVersionMapper
import com.chat.openapiParser.config_manager.parser.OpenApiParser
import com.chat.openapiParser.config_manager.service.SpecificationVersionService
import com.chat.openapiParser.config_manager.persistence.entity.ProviderEndpointEntity
import com.chat.openapiParser.config_manager.persistence.entity.ProviderSpecificationVersionEntity
import com.chat.openapiParser.config_manager.persistence.repository.ProviderEndpointRepository
import com.chat.openapiParser.config_manager.persistence.repository.ProviderSpecificationVersionRepository
import com.chat.openapiParser.config_manager.s3.OpenSpecificationS3Bucket
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.utils.IoUtils
import java.io.File

@Service
open class SpecificationVersionServiceImpl(
    private val specificationVersionRepository: ProviderSpecificationVersionRepository,
    private val endpointInfoRepository: ProviderEndpointRepository,
    private val specificationVersionMapper: SpecificationVersionMapper,
    private val endpointInfoMapper: EndpointInfoMapper,
    private val openApiParser: OpenApiParser,
    private val openSpecificationS3Bucket: OpenSpecificationS3Bucket,
) : SpecificationVersionService {

  override fun getSpecificationVersion(specificationVersionId: String) =
      specificationVersionRepository.findByIdOrNull(specificationVersionId)?.let {
        specificationVersionMapper.toSpecificationResponse(it)
      }
          ?: throw NotFoundException("Specification is not found by id $specificationVersionId")

  override fun getNewestSpecificationVersionByProviderId(providerId: String) =
      specificationVersionRepository.findFirstByProviderIdOrderByCreatedDateDesc(providerId)?.let {
        specificationVersionMapper.toSpecificationResponse(it)
      }
          ?: throw NotFoundException("Specification is not found for provider: $providerId")

  override fun uploadSpecification(
      providerId: String,
      version: String,
      file: MultipartFile
  ): String {
    val key = buildSpecificationS3Key(providerId, version)
    openSpecificationS3Bucket.uploadFile(key, file.inputStream, file.size)
    return key
  }

  @Transactional
  override fun createSpecificationForProvider(
      providerId: String,
      specificationRequest: ProviderSpecificationVersionRequest
  ): ProviderSpecificationVersionResponse {
    checkExistingSpecificationVersionLabelInDb(providerId, specificationRequest)

    val specificationEntity = specificationVersionMapper.toEntity(providerId, specificationRequest)

    val endpointsDto = processOpenApi(specificationEntity)

    val savedSpecificationEntity = specificationVersionRepository.save(specificationEntity)
    val endpointsEntity = endpointInfoMapper.toEntity(savedSpecificationEntity.id, endpointsDto)

    return saveEndpointEntities(endpointsEntity, savedSpecificationEntity)
  }

  private fun checkExistingSpecificationVersionLabelInDb(
      providerId: String,
      specificationRequest: ProviderSpecificationVersionRequest
  ) =
      specificationVersionRepository
          .findByProviderIdAndVersionLabel(providerId, specificationRequest.versionLabel)
          ?.let {
            throw ValidationFailedException(
                "ProviderSpecificationVersion record already exists in the database for providerId $providerId, versionLabel ${specificationRequest.versionLabel}")
          }

  @Transactional
  override fun updateSpecificationForProvider(
      versionId: String,
      specificationRequest: ProviderSpecificationVersionUpdateRequest
  ): ProviderSpecificationVersionResponse {
    val mergedSpecificationEntity = mergeSpecificationEntity(versionId, specificationRequest)

    checkExistingVersionLabelInDb(specificationRequest, mergedSpecificationEntity, versionId)

    val existingProviderEndpointEntities =
        endpointInfoRepository.findByProviderSpecificationVersionId(versionId)

    val parsedEndpointsDto = processOpenApi(mergedSpecificationEntity)

    val savedSpecificationEntity = specificationVersionRepository.save(mergedSpecificationEntity)

    val mergedEndpointEntity =
        endpointInfoMapper.merge(
            savedSpecificationEntity.id, existingProviderEndpointEntities, parsedEndpointsDto)

    val savedEndpoints = saveEndpointEntities(mergedEndpointEntity, savedSpecificationEntity)

    val endpointIdList = mutableListOf<String>()
    existingProviderEndpointEntities.forEach { endpointIdList.add(it.id) }

//    publishRedisEventService.publishProviderSpecificationVersionCacheEvictEvent(
//        savedSpecificationEntity.id)

    return savedEndpoints
  }

  private fun checkExistingVersionLabelInDb(
      specificationRequest: ProviderSpecificationVersionUpdateRequest,
      mergedSpecificationEntity: ProviderSpecificationVersionEntity,
      versionId: String
  ) {
    specificationRequest.versionLabel?.let {
      val specificationVersionEntity =
          specificationVersionRepository.findByProviderIdAndVersionLabel(
              mergedSpecificationEntity.providerId, it)
      specificationVersionEntity?.let { version ->
        if (version.id != versionId) {
          throw ValidationFailedException(
              "VersionLabel $it already exists in the database for a different specificationVersionId ${specificationVersionEntity.id}")
        }
      }
    }
  }

  private fun mergeSpecificationEntity(
      versionId: String,
      specificationRequest: ProviderSpecificationVersionUpdateRequest
  ): ProviderSpecificationVersionEntity {
    val specificationEntity =
        specificationVersionRepository.findById(versionId).orElseThrow {
          NotFoundException("Specification version not found")
        }
    return specificationVersionMapper.merge(versionId, specificationEntity, specificationRequest)
  }

  private fun saveEndpointEntities(
      endpointsEntity: List<ProviderEndpointEntity>,
      savedSpecificationEntity: ProviderSpecificationVersionEntity
  ): ProviderSpecificationVersionResponse {
    endpointInfoRepository.saveAll(endpointsEntity)
    return specificationVersionMapper.toResponse(savedSpecificationEntity)
  }

  override fun getSpecificationsForProvider(providerId: String): List<SpecificationResponse> {
    val specificationEntities: List<ProviderSpecificationVersionEntity> =
        specificationVersionRepository.findAllByProviderId(providerId)
    if (specificationEntities.isEmpty()) {
      throw NotFoundException("Specifications not found")
    }
    return specificationEntities.map { specificationVersionMapper.toSpecificationResponse(it) }
  }

  override fun getSpecificVersion(version: String?, providerId: String): String {
    return if (version == null) {
      findFirstByProviderIdOrderByCreatedDateDesc(providerId)
    } else {
      findByProviderIdAndVersion(providerId, version)
    }
  }

  override fun findByProviderIdAndVersion(providerId: String, version: String): String {
    return specificationVersionRepository.findByProviderIdAndVersionLabel(providerId, version)?.id
        ?: throw NotFoundException("Provider ID or Version ID not found for Provider $providerId")
  }

  override fun findFirstByProviderIdOrderByCreatedDateDesc(providerId: String): String {
    return specificationVersionRepository
        .findFirstByProviderIdOrderByCreatedDateDesc(providerId)
        ?.id
        ?: throw NotFoundException("Provider ID or Version ID not found for Provider $providerId")
  }

  private fun processOpenApi(
      specificationEntity: ProviderSpecificationVersionEntity
  ): List<ParserEndpointInfoDto> {

//      val classLoader = this::class.java.classLoader
//      val file = classLoader.getResource("files/Salesforce.yaml")?.file
//          ?: throw IllegalArgumentException("File not found")

      val specificationInputStream =
          openSpecificationS3Bucket.downloadFile(
              buildSpecificationS3Key(
                  specificationEntity.providerId, specificationEntity.versionLabel))
      val endpointsDto = openApiParser.parse(IoUtils.toUtf8String(specificationInputStream))
   // val endpointsDto = openApiParser.parse(File(file).readText(Charsets.UTF_8))
    log.info("Parsed open api spec,endpointsDto size ${endpointsDto.size}")
    return endpointsDto
  }

  private fun buildSpecificationS3Key(providerId: String, version: String) = "/$providerId/$version"

  companion object {
    private val log = slf4j<SpecificationVersionServiceImpl>()
  }
}
