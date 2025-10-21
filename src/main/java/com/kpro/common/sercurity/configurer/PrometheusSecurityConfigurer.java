package com.kpro.common.sercurity.configurer;

import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class PrometheusSecurityConfigurer implements HttpSecurityConfigurer {
  private static final Logger log = LoggerFactory.getLogger(PrometheusSecurityConfigurer.class);

  @Value("${management.security.roles:ACTUATOR")
  private final List<String> actuatorsRole = Collections.singletonList("ACTUATOR");

  @Override
  public void configure(HttpSecurity httpSecurity) {
    try {
      log.info("{} start create configurer", getClass().getSimpleName());
      httpSecurity.authorizeHttpRequests(
          authorizationManagerRequestMatcherRegistry ->
              authorizationManagerRequestMatcherRegistry
                  .requestMatchers(
                      new RequestMatcher[] {
                        EndpointRequest.to(PrometheusScrapeEndpoint.class)
                      })
                  .permitAll());
    } catch (Exception e) {
      log.error("{} actuator configurer error {}", getClass().getSimpleName(), e);
    }
  }
}
