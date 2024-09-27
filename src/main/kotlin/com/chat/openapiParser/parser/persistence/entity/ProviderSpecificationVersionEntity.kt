package com.chat.openapiParser.parser.persistence.entity

import com.chat.openapiParser.parser.persistence.entity.base.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "provider_specification_version")
@Entity
open class ProviderSpecificationVersionEntity : BaseEntity() {

  @Column(name = "provider_id", nullable = false, length = 32) open lateinit var providerId: String

  @Column(name = "version_label", nullable = false, length = 32)
  open lateinit var versionLabel: String

  @Column(name = "open_api_spec_s3_key", nullable = false)
  open lateinit var openApiSpecS3Key: String
}
