package com.kpro.common.sercurity.configurer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

public class CsrfDisabledSecurityConfigurer implements HttpSecurityConfigurer {
  private static final Logger log = LoggerFactory.getLogger(CsrfDisabledSecurityConfigurer.class);

  public CsrfDisabledSecurityConfigurer() {}

  @Override
  public void configure(HttpSecurity httpSecurity) {
    try {
      log.info("{} start create configurer", getClass().getSimpleName());
      httpSecurity.csrf(AbstractHttpConfigurer::disable);
    } catch (Exception e) {
      log.error("{} csrf security disable error {}", getClass().getSimpleName(), e);
    }
  }
}
