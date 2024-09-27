package com.chat.openapiParser

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableJpaRepositories
@EntityScan
class OpenapiParserApplication

fun main(args: Array<String>) {
	runApplication<OpenapiParserApplication>(*args)
}
