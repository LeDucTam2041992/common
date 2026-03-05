package com.kpro.common.sercurity.configurer;

import jakarta.servlet.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class DefaultSecurityConfigurer implements HttpSecurityConfigurer {
  private static final Logger log = LoggerFactory.getLogger(DefaultSecurityConfigurer.class);
  private final Filter filter;

  public DefaultSecurityConfigurer(Filter filter) {
    this.filter = filter;
  }

  @Override
  public void configure(HttpSecurity httpSecurity) {
    try {
      log.info("[{}] start create configurer", getClass().getSimpleName());
      httpSecurity
          //                    .addFilterBefore(filter, BasicAuthenticationFilter.class)
          .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
          .authorizeHttpRequests(
              authorizationManagerRequestMatcherRegistry ->
                  authorizationManagerRequestMatcherRegistry.anyRequest().authenticated());
    } catch (Exception e) {
      log.error("{} default security configurer error {}", getClass().getSimpleName(), e);
    }
  }
}
