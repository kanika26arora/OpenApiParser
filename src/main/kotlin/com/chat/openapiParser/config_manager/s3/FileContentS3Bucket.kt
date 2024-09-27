package com.chat.openapiParser.config_manager.s3

import com.chat.openapiParser.config_manager.s3.AbstractS3Repository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.s3.S3Client

@Repository
@ConditionalOnProperty("aws.s3.buckets.file-upload-bucket")
class FileContentS3Bucket(
    s3Client: S3Client,
    @Value("\${aws.s3.buckets.file-upload-bucket}") bucket: String
) : AbstractS3Repository(s3Client, bucket)
