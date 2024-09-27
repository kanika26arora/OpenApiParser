package com.chat.openapiParser.parser.s3

import java.io.InputStream
import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectResponse
import software.amazon.awssdk.services.s3.model.PutObjectRequest

abstract class AbstractS3Repository(open val s3Client: S3Client, open val bucket: String) {

  fun uploadFile(
      key: String,
      inputStream: InputStream,
      contentLength: Long,
      metadata: Map<String, String> = emptyMap()
  ) {

    inputStream.use {
      val putObjectRequest: PutObjectRequest =
          PutObjectRequest.builder()
              .bucket(bucket)
              .key(key)
              .contentLength(contentLength)
              .metadata(metadata)
              .build()

      s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength))
    }
  }

  fun downloadFile(key: String): ResponseInputStream<GetObjectResponse> {

    val getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(key).build()

    return s3Client.getObject(getObjectRequest)
  }

  fun deleteFile(key: String) {

    val deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucket).key(key).build()

    s3Client.deleteObject(deleteObjectRequest)
  }
}
