package com.kpro.common.servicemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(AccessControlConfig.class)
public class CustomPermissionSecurityConfig extends GlobalMethodSecurityConfiguration {

  @Autowired @Lazy FunctionalAccessControl functionalAccessControl;

  public CustomPermissionSecurityConfig() {}

  @Override
  protected MethodSecurityExpressionHandler createExpressionHandler() {
    return new CustomMethodSecurityExpressionHandler(functionalAccessControl);
  }

  @Bean({"functionalAccessControlDefault"})
  @ConditionalOnMissingBean
  public FunctionalAccessControl functionalAccessControlDefault(
      @Qualifier("interRestTemplate") RestTemplate interRestTemplate, AccessControlConfig config) {
    return new FunctionalAccessControlImpl(interRestTemplate, config.getUrlCheckPermission());
  }
}
