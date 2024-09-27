package com.chat.openapiParser.config_manager.persistence.entity.base

import jakarta.persistence.*
import java.time.Instant

@MappedSuperclass
abstract class BaseEntity {

  @Id
  @Column(name = "id", unique = true, nullable = false, length = 36)
  open lateinit var id: String

  @Column(
      name = "created_date", nullable = false, updatable = false, columnDefinition = "datetime(3)")
  open lateinit var createdDate: Instant

  @Column(name = "modified_date", nullable = false, columnDefinition = "datetime(3)")
  open lateinit var modifiedDate: Instant

  @PrePersist
  open fun onCreate() {
    createdDate = Instant.now()
    modifiedDate = createdDate
  }

  @PreUpdate
  open fun onUpdate() {
    modifiedDate = Instant.now()
  }
}
