package com.kpro.common.sercurity.configurer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

public class CorsDisabledSecurityConfigurer implements HttpSecurityConfigurer {
  private static final Logger log = LoggerFactory.getLogger(CorsDisabledSecurityConfigurer.class);

  public CorsDisabledSecurityConfigurer() {}

  @Override
  public void configure(HttpSecurity httpSecurity) {
    try {
      log.info("{} start create configurer", getClass().getSimpleName());
      httpSecurity.cors(AbstractHttpConfigurer::disable);
    } catch (Exception e) {
      log.error("{} cors security disable error {}", getClass().getSimpleName(), e);
    }
  }
}
