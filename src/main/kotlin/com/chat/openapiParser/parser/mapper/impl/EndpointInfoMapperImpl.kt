package com.chat.openapiParser.parser.mapper.impl

import com.chat.openapiParser.parser.dto.*
import com.chat.openapiParser.parser.model.*
import com.chat.openapiParser.parser.dto.parser.ParserEndpointInfoDto
import com.chat.openapiParser.parser.dto.parser.ParserParameterDto
import com.chat.openapiParser.parser.dto.parser.ParserProviderRestEndpointConfigDto
import com.chat.openapiParser.parser.dto.parser.ParserResponsePropertyDto
import com.chat.openapiParser.parser.dto.response.ProviderEndpointInternalResponse
import com.chat.openapiParser.parser.dto.response.ProviderEndpointResponse
import com.chat.openapiParser.parser.mapper.EndpointInfoMapper
import com.chat.openapiParser.parser.persistence.entity.ProviderEndpointEntity
import com.chat.openapiParser.parser.persistence.model.ResponseSample
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import java.util.UUID

// TODO: remove after adding MapStruct
@Service
class EndpointInfoMapperImpl : EndpointInfoMapper {

  override fun toEntity(
      specificationVersionId: String,
      parserEndpoints: List<ParserEndpointInfoDto>
  ): List<ProviderEndpointEntity> {
    return parserEndpoints.map { toEndpointInfoEntity(specificationVersionId, it) }
  }

  override fun toFullEndpoint(entity: ProviderEndpointEntity): ProviderEndpointResponse {
    return ProviderEndpointResponse(
        entity.id,
        entity.operationName,
        entity.requestParameters.map { toParameterDto(it) },
        entity.endpointResponse.map { toResponseSample(it) },
        entity.endpointResponseTemplate?.map { toEndpointResponseTemplate(it) })
  }

  private fun toEndpointResponseTemplate(
      customEndpointConfig: CustomEndpointConfig
  ): EndpointResponseTemplateDto {
    customEndpointConfig as CustomEndpointResponse
    return EndpointResponseTemplateDto(customEndpointConfig.sourceRef, customEndpointConfig.order)
  }

  override fun toFullInternalEndpoint(
      entity: ProviderEndpointEntity
  ): ProviderEndpointInternalResponse {
    return ProviderEndpointInternalResponse(
        entity.id,
        entity.operationName,
        entity.requestParameters.map { toParameterInternalDto(it) },
        entity.endpointResponse.map { toResponseSample(it) })
  }

  override fun toShortEndpoints(
      entities: List<EndpointInfoShortDto>
  ): List<ProviderEndpointInfoShortDto> {
    return entities.map { ProviderEndpointInfoShortDto(it.id, it.name) }
  }

  override fun toParameterDto(parameter: Parameter): ParameterDto {
    return ParameterDto(
        parameter.id,
        parameter.label,
        parameter.placeholder,
        parameter.default,
        parameter.required,
        parameter.constraints?.map { constraint ->
          ConstraintDto(constraint.constraintType, constraint.value)
        },
        parameter.parameterType,
        parameter.availableOptions?.map { availableSelectOptions ->
          AvailableOptionDto(availableSelectOptions.label, availableSelectOptions.value)
        },
        parameter.isArray,
        parameter.presentationType,
        parameter.repeatableGroupId,
        parameter.repeatableGroupName,
        parameter.placementType,
        parameter.destinationPath)
  }

  override fun toCustomParameterDto(parameter: Parameter): CustomParameterDto {
    return CustomParameterDto(
        parameter.id,
        parameter.label,
        parameter.placeholder,
        parameter.default,
        parameter.constraints?.map { constraint ->
          ConstraintDto(constraint.constraintType, constraint.value)
        },
        parameter.availableOptions?.map { availableSelectOptions ->
          AvailableOptionDto(availableSelectOptions.label, availableSelectOptions.value)
        },
        parameter.presentationType,
        parameter.repeatableGroupName)
  }

  override fun toResponseSample(responseSample: ResponseSample): ResponseDto {
    return ResponseDto(
        responseSample.statusCode,
        responseSample.properties?.map { property ->
          ResponsePropertyDto(property.sourceRef, property.label, property.type, property.isArray)
        },
        responseSample.labels?.map { parentLabel ->
            ParentLabelDto(parentLabel.location, parentLabel.text)
        },
        responseSample.primaryFields)
  }
  override fun merge(
      specificationVersionId: String,
      existingDbProviderEndpointEntities: List<ProviderEndpointEntity>,
      parserEndpoints: List<ParserEndpointInfoDto>
  ): List<ProviderEndpointEntity> {

    val existingOperationIdProviderEndPointMapping =
        getExistingOperationIdProviderEndPointMapping(existingDbProviderEndpointEntities)

    return parserEndpoints.map { parserEndpoint ->
      if (existingOperationIdProviderEndPointMapping.containsKey(parserEndpoint.operationId)) {

        val existingEndpointInfoEntity =
            existingOperationIdProviderEndPointMapping.getValue(parserEndpoint.operationId)

        val endpointInfoEntity = ProviderEndpointEntity()
        endpointInfoEntity.id = existingEndpointInfoEntity.id
        endpointInfoEntity.operationName = parserEndpoint.name
        endpointInfoEntity.operationId = parserEndpoint.operationId
        endpointInfoEntity.providerSpecificationVersionId = specificationVersionId

        endpointInfoEntity.requestParameters =
            mergeParameter(
                parserEndpoint,
                getExistingDestinationPathRequestParameterMapping(existingEndpointInfoEntity))

        endpointInfoEntity.endpointResponse =
            mergeResponse(
                parserEndpoint,
                getExistingSourceRefParserEndpointMapping(existingEndpointInfoEntity))
        endpointInfoEntity.endpointConfig = getRestEndpointConfig(parserEndpoint)

        endpointInfoEntity
      } else {

        toEndpointInfoEntity(specificationVersionId, parserEndpoint)
      }
    }
  }

  override fun toEndpointResponseTemplateDto(
      customEndpointConfig: CustomEndpointConfig
  ): EndpointResponseTemplateDto {
    customEndpointConfig as CustomEndpointResponse
    return EndpointResponseTemplateDto(customEndpointConfig.sourceRef, customEndpointConfig.order)
  }

  private fun getExistingDestinationPathRequestParameterMapping(
      existingEndpointInfoEntity: ProviderEndpointEntity
  ) = existingEndpointInfoEntity.requestParameters.associateBy({ it.destinationPath }, { it })

  private fun getExistingOperationIdProviderEndPointMapping(
      existingDbProviderEndpointEntities: List<ProviderEndpointEntity>
  ) = existingDbProviderEndpointEntities.associateBy({ it.operationId }, { it })

  private fun getExistingSourceRefParserEndpointMapping(
      providerEndpointEntity: ProviderEndpointEntity?
  ): Map<String, ResponseProperty> {
    val sourceRefParserEndpointMapping = HashMap<String, ResponseProperty>()

    providerEndpointEntity?.endpointResponse?.forEach { responseSample ->
      responseSample.properties?.associateBy({ it.sourceRef }, { it })?.let { map ->
        sourceRefParserEndpointMapping.putAll(map)
      }
    }
    return sourceRefParserEndpointMapping
  }

  private fun mergeResponse(
      parserEndpoint: ParserEndpointInfoDto,
      existingSourceRefParserEndpointMapping: Map<String, ResponseProperty>
  ): List<ResponseSample> =
      parserEndpoint.response.responses.map { response ->
        ResponseSample(
            response.statusCode,
            response.properties?.map { property ->
              if (existingSourceRefParserEndpointMapping.containsKey(property.sourceRef)) {
                val existingResponseProperty =
                    existingSourceRefParserEndpointMapping[property.sourceRef]
                ResponseProperty(
                    property.sourceRef,
                    existingResponseProperty?.label,
                    property.type,
                    property.isArray)
              } else {
                toResponseProperty(property)
              }
            },
            null,
            null)
      }

  private fun mergeParameter(
      parserEndpoint: ParserEndpointInfoDto,
      destinationPathRequestParameterMapping: Map<String, Parameter>
  ) =
      parserEndpoint.requestParameters.map { parameter ->
        if (destinationPathRequestParameterMapping.containsKey(parameter.destinationPath)) {
          val existingParameter =
              destinationPathRequestParameterMapping.getValue(parameter.destinationPath)
          Parameter(
              existingParameter.id,
              existingParameter.label,
              parameter.placementType,
              parameter.destinationPath,
              existingParameter.placeholder,
              existingParameter.default,
              parameter.required,
              existingParameter.constraints,
              parameter.parameterType,
              existingParameter.availableOptions,
              parameter.isArray ?: false,
              existingParameter.presentationType,
              parameter.repeatableGroupId,
              existingParameter.repeatableGroupName)
        } else {
          toParameter(parameter)
        }
      }

  override fun toCustomResponseSample(responseSample: ResponseSample): CustomResponseDto {
    return CustomResponseDto(
        responseSample.statusCode,
        responseSample.properties?.map { property ->
          CustomResponsePropertyDto(property.sourceRef, property.label)
        },
        responseSample.labels?.map { parentLabel ->
          ParentLabelDto(parentLabel.location, parentLabel.text)
        },
        responseSample.primaryFields)
  }

  private fun toEndpointInfoEntity(
      specificationVersionId: String,
      endpoint: ParserEndpointInfoDto
  ): ProviderEndpointEntity {
    val endpointInfoEntity = ProviderEndpointEntity()
    endpointInfoEntity.id = UUID.randomUUID().toString()
    endpointInfoEntity.operationId = endpoint.operationId
    endpointInfoEntity.operationName = endpoint.name
    endpointInfoEntity.providerSpecificationVersionId = specificationVersionId
    endpointInfoEntity.requestParameters =
        endpoint.requestParameters.map { parameter -> toParameter(parameter) }
    endpointInfoEntity.endpointResponse =
        endpoint.response.responses.map { response ->
          ResponseSample(
              response.statusCode,
              response.properties?.map { property -> toResponseProperty(property) },
              null,
              null)
        }
    endpointInfoEntity.endpointConfig = getRestEndpointConfig(endpoint)
    return endpointInfoEntity
  }

  private fun toResponseProperty(property: ParserResponsePropertyDto) =
      ResponseProperty(property.sourceRef, property.label, property.type, property.isArray)

  private fun toParameter(parameter: ParserParameterDto) =
      Parameter(
          UUID.randomUUID().toString(),
          parameter.label,
          parameter.placementType,
          parameter.destinationPath,
          parameter.placeholder,
          parameter.default,
          parameter.required,
          emptyList(),
          parameter.parameterType,
          parameter.availableOptions?.map { options ->
            AvailableSelectOptions(options.label, options.value)
          },
          parameter.isArray ?: false,
          parameter.presentationType,
          parameter.repeatableGroupId,
          parameter.repeatableGroupName)

  private fun getRestEndpointConfig(endpoint: ParserEndpointInfoDto): ProviderRestEndpointConfig {
    val restEndpointConfig = endpoint.config as ParserProviderRestEndpointConfigDto
    return ProviderRestEndpointConfig(
        restEndpointConfig.url,
        HttpMethod.valueOf(restEndpointConfig.httpMethod),
        restEndpointConfig.contentTypes)
  }

  private fun toParameterInternalDto(parameter: Parameter): ParameterInternalDto {
    return ParameterInternalDto(
        parameter.id,
        parameter.label,
        parameter.placeholder,
        parameter.default,
        parameter.required,
        parameter.constraints?.map { constraint ->
          ConstraintDto(constraint.constraintType, constraint.value)
        },
        parameter.parameterType,
        parameter.availableOptions?.map { availableSelectOptions ->
          AvailableOptionDto(availableSelectOptions.label, availableSelectOptions.value)
        },
        parameter.isArray,
        parameter.presentationType,
        parameter.repeatableGroupId,
        parameter.repeatableGroupName)
  }
}
