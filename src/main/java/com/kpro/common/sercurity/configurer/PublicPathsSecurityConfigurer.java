package com.kpro.common.sercurity.configurer;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.util.CollectionUtils;

public class PublicPathsSecurityConfigurer implements HttpSecurityConfigurer {
  private static final Logger log = LoggerFactory.getLogger(PublicPathsSecurityConfigurer.class);
  List<String> publicPaths;

  public PublicPathsSecurityConfigurer(List<String> publicPaths) {
    this.publicPaths = publicPaths;
  }

  public void setPublicPaths(List<String> publicPaths) {
    this.publicPaths = publicPaths;
  }

  @Override
  public void configure(HttpSecurity httpSecurity) {
    if (!CollectionUtils.isEmpty(this.publicPaths)) {
      String[] arr = this.publicPaths.toArray(new String[this.publicPaths.size()]);
      log.info("[{}] start create configurer [{}]", getClass().getSimpleName(), arr);
      try {
        httpSecurity.authorizeHttpRequests(
            authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry.requestMatchers(arr).permitAll());
      } catch (Exception e) {
        log.error(
            "[{}] public paths security configurer error [{}]",
            getClass().getSimpleName(),
            e.getMessage());
      }
    }
  }
}
