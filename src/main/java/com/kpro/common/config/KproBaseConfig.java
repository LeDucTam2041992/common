package com.kpro.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
    basePackages = {
      "com.kpro.common.sercurity",
      "com.kpro.common.communication",
      "com.kpro.common.servicemanager",
      "com.kpro.common.exception",
      "com.kpro.common.persistence.config",
      "com.kpro.common.minio",
      "com.kpro.common.locale",
    })
public class KproBaseConfig {}
