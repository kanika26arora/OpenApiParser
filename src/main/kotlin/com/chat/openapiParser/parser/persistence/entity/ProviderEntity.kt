package com.chat.openapiParser.parser.persistence.entity

import com.chat.openapiParser.parser.persistence.entity.base.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "provider")
@Entity
open class ProviderEntity : BaseEntity() {

  @Column(name = "name", nullable = false, length = 100) open lateinit var name: String

  @Column(name = "logo_url", nullable = false) open lateinit var logoUrl: String

  @Column(name = "more_info_url") open lateinit var moreInfoUrl: String
}
