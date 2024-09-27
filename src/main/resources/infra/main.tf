provider "aws" {
  access_key                  = "test"
  secret_key                  = "test"
  region                      = "us-east-1"

  endpoints {
    s3             = "http://s3.localhost.localstack.cloud:4566"
  }
}

resource "aws_s3_bucket" "parser-provider-spec-bucket" {
  bucket = "local-interact-parser-provider-spec"
}

resource "aws_s3_bucket" "parser-file-upload-bucket" {
  bucket = "local-interact-parser-file-upload"
}

