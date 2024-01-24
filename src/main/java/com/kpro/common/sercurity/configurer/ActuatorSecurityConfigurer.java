package com.kpro.common.sercurity.configurer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collections;
import java.util.List;

public class ActuatorSecurityConfigurer implements HttpSecurityConfigurer {
    private static final Logger log = LoggerFactory.getLogger(ActuatorSecurityConfigurer.class);

    @Value("${management.security.roles:ACTUATOR")
    private List<String> actuatorsRole = Collections.singletonList("ACTUATOR");

    @Override
    public void configure(HttpSecurity httpSecurity) {
        try {
            log.info("{} start create configurer", getClass().getSimpleName());
            httpSecurity.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(new RequestMatcher[]{EndpointRequest.to(new Class[]{HealthEndpoint.class})}).permitAll()
                            .requestMatchers(new RequestMatcher[]{EndpointRequest.toAnyEndpoint()}).hasAnyRole(this.actuatorsRole.toArray(new String[0])));
        } catch (Exception e) {
            log.error("{} actuator configurer error {}", getClass().getSimpleName(), e);
        }
    }
}
