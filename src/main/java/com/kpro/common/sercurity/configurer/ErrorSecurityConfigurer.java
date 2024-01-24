package com.kpro.common.sercurity.configurer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class ErrorSecurityConfigurer implements HttpSecurityConfigurer {
    private static final Logger log = LoggerFactory.getLogger(ErrorSecurityConfigurer.class);

    @Override
    public void configure(HttpSecurity httpSecurity) {
        try {
            log.info("{} start create configurer", getClass().getSimpleName());
            httpSecurity
                    .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                            authorizationManagerRequestMatcherRegistry.requestMatchers("/error").permitAll())
                    .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                            httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint((request, response, authException) -> {
                                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                response.setHeader("WWW-Authenticate", "Bearer error=\"invalid_token");
                            })
                    );
        } catch (Exception e) {
            log.error("{} error security configurer error {}", getClass().getSimpleName(), e);
        }
    }
}
