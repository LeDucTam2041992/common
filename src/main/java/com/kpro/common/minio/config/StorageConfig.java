package com.kpro.common.minio.config;

import com.amazonaws.services.s3.AmazonS3;
import com.kpro.common.minio.AwsFileService;
import com.kpro.common.minio.impl.AwsFileServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(AwsS3Config.class)
public class StorageConfig {

    @Bean
    @ConditionalOnBean(value = AmazonS3.class)
    public AwsFileService awsFileService(final AwsS3Config awsS3Config, final AmazonS3 amazonS3) {
        return new AwsFileServiceImpl(awsS3Config, amazonS3);
    }
}
