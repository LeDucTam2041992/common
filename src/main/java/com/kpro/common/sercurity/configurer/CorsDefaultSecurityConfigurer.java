package com.kpro.common.sercurity.configurer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class CorsDefaultSecurityConfigurer implements HttpSecurityConfigurer {
  private static final Logger log = LoggerFactory.getLogger(CorsDefaultSecurityConfigurer.class);

  @Override
  public void configure(HttpSecurity httpSecurity) {
    try {
      log.info("[{}] start create configurer", getClass().getSimpleName());
      httpSecurity.cors(Customizer.withDefaults());
    } catch (Exception e) {
      log.error("[{}] cors security default error [{}]", getClass().getSimpleName(), e);
    }
  }
}
