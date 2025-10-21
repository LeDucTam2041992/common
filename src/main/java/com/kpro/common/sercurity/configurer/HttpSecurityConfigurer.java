package com.kpro.common.sercurity.configurer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@FunctionalInterface
public interface HttpSecurityConfigurer {
  void configure(HttpSecurity httpSecurity);
}
