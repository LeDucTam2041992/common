package com.kpro.common.servicemanager;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(AccessControlConfig.class)
public class CustomPermissionSecurityConfig {

  @Bean({"functionalAccessControlDefault"})
  @ConditionalOnMissingBean
  public FunctionalAccessControl functionalAccessControlDefault(
      @Qualifier("interRestTemplate") RestTemplate interRestTemplate, AccessControlConfig config) {
    return new FunctionalAccessControlImpl(interRestTemplate, config.getUrlCheckPermission());
  }

  @Bean
  public MethodSecurityExpressionHandler methodSecurityExpressionHandler(
      @Qualifier("functionalAccessControlDefault")
          FunctionalAccessControl functionalAccessControl) {
    return new CustomMethodSecurityExpressionHandler(functionalAccessControl);
  }
}
