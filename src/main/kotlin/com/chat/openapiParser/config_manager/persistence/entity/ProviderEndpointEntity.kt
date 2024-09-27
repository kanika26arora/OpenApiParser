package com.chat.openapiParser.config_manager.persistence.entity

import com.chat.openapiParser.config_manager.model.CustomEndpointConfig
import com.chat.openapiParser.config_manager.persistence.model.ResponseSample
import com.chat.openapiParser.config_manager.persistence.entity.base.BaseEntity
import com.chat.openapiParser.config_manager.model.Parameter
import com.chat.openapiParser.config_manager.model.ProviderEndpointConfig
import com.vladmihalcea.hibernate.type.json.JsonType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "provider_endpoint")
@Convert(attributeName = "json", converter = JsonType::class)
open class ProviderEndpointEntity() : BaseEntity() {

  @Column(name = "operation_name", nullable = false, length = 100)
  open lateinit var operationName: String

  @Column(name = "provider_specification_version_id", nullable = false, length = 32)
  open lateinit var providerSpecificationVersionId: String

  @Column(name = "operation_id", nullable = false) open var operationId: Short = 0

  @Column(name = "request_parameters", columnDefinition = "json", nullable = false)
  @JdbcTypeCode(SqlTypes.JSON)
  open lateinit var requestParameters: List<Parameter>

  @Column(name = "endpoint_response", columnDefinition = "json", nullable = false)
  @JdbcTypeCode(SqlTypes.JSON)
  open lateinit var endpointResponse: List<ResponseSample>

  @Column(name = "endpoint_config", columnDefinition = "json", nullable = false)
  @JdbcTypeCode(SqlTypes.JSON)
  open lateinit var endpointConfig: ProviderEndpointConfig

  @Column(name = "endpoint_response_template", columnDefinition = "json", nullable = true)
  @JdbcTypeCode(SqlTypes.JSON)
  open var endpointResponseTemplate: List<CustomEndpointConfig>? = null
}
