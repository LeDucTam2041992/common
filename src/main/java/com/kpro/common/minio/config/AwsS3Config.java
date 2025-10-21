package com.kpro.common.minio.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.BucketAccelerateConfiguration;
import com.amazonaws.services.s3.model.BucketAccelerateStatus;
import com.amazonaws.services.s3.model.GetBucketAccelerateConfigurationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@ConfigurationProperties(prefix = "kpro.storage")
public class AwsS3Config {
  public static final String STORAGE_PROVIDER_AWS_S3 = "aws-s3";
  public static final String STORAGE_PROVIDER_MINIO = "minio";
  private static final Logger log = LoggerFactory.getLogger(AwsS3Config.class);
  private String provider = STORAGE_PROVIDER_MINIO;
  private String endpoint;
  private String region = "ap-southeast-1";
  private String credentialsAccessKey;
  private String credentialsSecretKey;
  private String bucket;

  public AwsS3Config() {}

  private static void checkAndEnableBucketTransferAcceleration(
      final AmazonS3 client, final String bucketName) {
    try {
      BucketAccelerateConfiguration bucketAccelerateConfiguration =
          client.getBucketAccelerateConfiguration(
              new GetBucketAccelerateConfigurationRequest(bucketName));
      if (!bucketAccelerateConfiguration.isAccelerateEnabled()) {
        log.warn("Bucket '{}' transfer acceleration is suspended.", bucketName);
      }

      client.setBucketAccelerateConfiguration(
          bucketName, new BucketAccelerateConfiguration(BucketAccelerateStatus.Enabled));
      log.info("Bucket '{}' transfer acceleration is enabled.", bucketName);
    } catch (AmazonS3Exception var3) {
      log.error(
          "Error happened while checking and enabling transfer acceleration for bucket '{}': {}.",
          bucketName,
          var3.getMessage());
    }
  }

  @Bean({"amazonS3"})
  @ConditionalOnProperty(
      prefix = "kpro.storage",
      name = {"provider"},
      havingValue = "aws-s3")
  public AmazonS3 amazonS3Client() {
    log.info("Initializing Amazon S3 client with transfer acceleration support.");
    AmazonS3 client =
        AmazonS3ClientBuilder.standard()
            .enableForceGlobalBucketAccess()
            .enableAccelerateMode()
            .withRegion(this.getRegion())
            .build();
    if (client == null) {
      throw new IllegalStateException("Unexpected error when initializing Amazon S3 client.");
    } else {
      checkAndEnableBucketTransferAcceleration(client, this.getBucket());
      return client;
    }
  }

  @Bean({"amazonS3"})
  @ConditionalOnProperty(
      prefix = "kpro.storage",
      name = {"provider"},
      havingValue = "minio")
  @Primary
  public AmazonS3 minioS3Client() {
    log.info("Initializing Amazon S3 client for Minio");
    AWSStaticCredentialsProvider credentials =
        new AWSStaticCredentialsProvider(
            new BasicAWSCredentials(
                this.getCredentialsAccessKey(), this.getCredentialsSecretKey()));
    AwsClientBuilder.EndpointConfiguration endpointConfiguration =
        new AwsClientBuilder.EndpointConfiguration(this.getEndpoint(), this.getRegion());
    ClientConfiguration clientConfiguration = new ClientConfiguration();
    clientConfiguration.setSignerOverride("AWSS3V4SignerType");
    AmazonS3 client =
        AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(endpointConfiguration)
            .withPathStyleAccessEnabled(true)
            .withClientConfiguration(clientConfiguration)
            .withCredentials(credentials)
            .build();
    if (client == null) {
      throw new IllegalStateException(
          "Unexpected error when initializing Amazon S3 client for Minio.");
    } else {
      return client;
    }
  }

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
