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
import com.kpro.common.minio.AwsFileService;
import com.kpro.common.minio.impl.AwsFileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(AwsS3Config.class)
@Slf4j
public class StorageConfig {

  private final AwsS3Config awsS3Config;

  public StorageConfig(AwsS3Config awsS3Config) {
    this.awsS3Config = awsS3Config;
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
            .withRegion(awsS3Config.getRegion())
            .build();
    if (client == null) {
      throw new IllegalStateException("Unexpected error when initializing Amazon S3 client.");
    } else {
      checkAndEnableBucketTransferAcceleration(client, awsS3Config.getBucket());
      return client;
    }
  }

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
      havingValue = "minio")
  @Primary
  public AmazonS3 minioS3Client() {
    log.info("Initializing Amazon S3 client for Minio");
    AWSStaticCredentialsProvider credentials =
        new AWSStaticCredentialsProvider(
            new BasicAWSCredentials(
                awsS3Config.getCredentialsAccessKey(), awsS3Config.getCredentialsSecretKey()));
    AwsClientBuilder.EndpointConfiguration endpointConfiguration =
        new AwsClientBuilder.EndpointConfiguration(
            awsS3Config.getEndpoint(), awsS3Config.getRegion());
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

  @Bean
  @ConditionalOnBean(name = "amazonS3")
  public AwsFileService awsFileService(final AmazonS3 amazonS3) {
    return new AwsFileServiceImpl(awsS3Config, amazonS3);
  }
}
