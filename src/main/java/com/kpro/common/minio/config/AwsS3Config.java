package com.kpro.common.minio.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kpro.storage")
public class AwsS3Config {
  public static final String STORAGE_PROVIDER_AWS_S3 = "aws-s3";
  public static final String STORAGE_PROVIDER_MINIO = "minio";

  private String provider = STORAGE_PROVIDER_MINIO;
  private String endpoint;
  private String region = "ap-southeast-1";
  private String credentialsAccessKey;
  private String credentialsSecretKey;
  private String bucket;

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getCredentialsAccessKey() {
    return credentialsAccessKey;
  }

  public void setCredentialsAccessKey(String credentialsAccessKey) {
    this.credentialsAccessKey = credentialsAccessKey;
  }

  public String getCredentialsSecretKey() {
    return credentialsSecretKey;
  }

  public void setCredentialsSecretKey(String credentialsSecretKey) {
    this.credentialsSecretKey = credentialsSecretKey;
  }

  public String getBucket() {
    return bucket;
  }

  public void setBucket(String bucket) {
    this.bucket = bucket;
  }
}
