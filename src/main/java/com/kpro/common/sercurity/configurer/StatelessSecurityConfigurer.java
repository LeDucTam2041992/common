package com.kpro.common.sercurity.configurer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

public class StatelessSecurityConfigurer implements HttpSecurityConfigurer {
  private static final Logger log = LoggerFactory.getLogger(StatelessSecurityConfigurer.class);

  public StatelessSecurityConfigurer() {}

  @Override
  public void configure(HttpSecurity httpSecurity) {
    try {
      log.info("{} start create configurer", getClass().getSimpleName());
      httpSecurity.sessionManagement(
          httpSecuritySessionManagementConfigurer ->
              httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                  SessionCreationPolicy.STATELESS));
    } catch (Exception e) {
      log.error("{} stateless security configurer error {}", getClass().getSimpleName(), e);
    }
  }
}
