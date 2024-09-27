package com.chat.openapiParser.config_manager.mapper

import com.chat.openapiParser.config_manager.dto.*
import com.chat.openapiParser.config_manager.model.CustomEndpointConfig
import com.chat.openapiParser.config_manager.dto.parser.ParserEndpointInfoDto
import com.chat.openapiParser.config_manager.dto.response.ProviderEndpointInternalResponse
import com.chat.openapiParser.config_manager.dto.response.ProviderEndpointResponse
import com.chat.openapiParser.config_manager.persistence.entity.ProviderEndpointEntity
import com.chat.openapiParser.config_manager.model.Parameter
import com.chat.openapiParser.config_manager.persistence.model.ResponseSample

interface EndpointInfoMapper {

  fun toEntity(
      specificationVersionId: String,
      parserEndpoints: List<ParserEndpointInfoDto>
  ): List<ProviderEndpointEntity>

  fun toFullEndpoint(entity: ProviderEndpointEntity): ProviderEndpointResponse

  fun toFullInternalEndpoint(entity: ProviderEndpointEntity): ProviderEndpointInternalResponse

  fun toShortEndpoints(entities: List<EndpointInfoShortDto>): List<ProviderEndpointInfoShortDto>

  fun toParameterDto(parameter: Parameter): ParameterDto

  fun toCustomParameterDto(parameter: Parameter): CustomParameterDto

  fun toResponseSample(responseSample: ResponseSample): ResponseDto

  fun toCustomResponseSample(responseSample: ResponseSample): CustomResponseDto

  fun merge(
      specificationVersionId: String,
      existingDbProviderEndpointEntities: List<ProviderEndpointEntity>,
      parserEndpoints: List<ParserEndpointInfoDto>
  ): List<ProviderEndpointEntity>

  fun toEndpointResponseTemplateDto(
      customEndpointConfig: CustomEndpointConfig
  ): EndpointResponseTemplateDto
}
