package com.chat.openapiParser.parser.config

import java.net.URI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client

@Configuration
class PersistenceConfig {

//  @Bean
//  @ConditionalOnProperty("aws.s3.region")
//  @Profile("!local")
//  fun s3Client(
//      @Value("\${aws.s3.region}") region: String,
//  ): S3Client {
//
//    return S3Client.builder().region(Region.of(region)).build()
//  }

  @Bean
  @Profile("local")
  fun s3ClientLocal(): S3Client {

    val localCredentials = AwsBasicCredentials.create("test", "test")
    val localAwsStaticCredentialsProvider = StaticCredentialsProvider.create(localCredentials)

    return S3Client.builder()
        .region(Region.US_EAST_1)
        .endpointOverride(URI.create("http://s3.localhost.localstack.cloud:4566"))
        .credentialsProvider(localAwsStaticCredentialsProvider)
        .build()
  }
}
