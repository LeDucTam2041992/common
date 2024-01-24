package com.kpro.common.sercurity;

import com.kpro.common.sercurity.config.PublicPathConfigProperties;
import com.kpro.common.sercurity.configurer.*;
import com.kpro.common.sercurity.filter.JwtAuthenticationFilter;
import com.kpro.common.sercurity.utils.*;
import jakarta.servlet.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@ConditionalOnProperty(
        name = "kpro.security.enable",
        havingValue = "true",
        matchIfMissing = true
)
@EnableWebSecurity
@EnableConfigurationProperties({PublicPathConfigProperties.class, JwtAlgorithmConfig.class})
@Import(CoreSecurityConfigurerAdapter.class)
public class HttpSecurityConfiguration {
    private static final Logger log = LoggerFactory.getLogger(HttpSecurityConfiguration.class);
    public HttpSecurityConfiguration() {
        log.info("{} start create security config", getClass().getSimpleName());
    }

    @Bean
    @ConditionalOnProperty(
            name = "kpro.security.error-config.enable",
            havingValue = "true",
            matchIfMissing = true
    )
    @Order(10)
    public HttpSecurityConfigurer errorHttpSecurityConfig() {
        return new ErrorSecurityConfigurer();
    }

    @Bean
    @ConditionalOnProperty(
            name = "kpro.security.stateless.enable",
            havingValue = "true",
            matchIfMissing = true
    )
    @Order(15)
    public HttpSecurityConfigurer statelessConfig() {
        return new StatelessSecurityConfigurer();
    }

    @Bean
    @ConditionalOnProperty(
            name = "kpro.security.public-path.enable",
            havingValue = "true",
            matchIfMissing = true
    )
    @Order(20)
    public HttpSecurityConfigurer publicPathConfig(PublicPathConfigProperties pathConfigProperties) {
        return new PublicPathsSecurityConfigurer(pathConfigProperties.getPaths());
    }

    @Bean
    @ConditionalOnProperty(
            name = "kpro.security.default.enable",
            havingValue = "true",
            matchIfMissing = true
    )
    @Order(50)
    public HttpSecurityConfigurer defaultAuth(@Qualifier("jwtAuthenticationFilter") Filter jwtAuthenticationFilter) {
        return new DefaultSecurityConfigurer(jwtAuthenticationFilter);
    }

    @Bean
    @ConditionalOnProperty(
            name = "kpro.security.csrf.enable",
            havingValue = "true",
            matchIfMissing = true
    )
    @Order(101)
    public HttpSecurityConfigurer disableCsrfConfigurer() {
        return new CsrfDisabledSecurityConfigurer();
    }

    @Bean
    @ConditionalOnProperty(
            name = "kpro.security.cors.enable",
            havingValue = "true",
            matchIfMissing = true
    )
    @Order(100)
    public HttpSecurityConfigurer disableCorsConfigurer() {
        return new CorsDisabledSecurityConfigurer();
    }

    @Bean
    @ConditionalOnProperty(
            name = "kpro.jwt.config.algorithm",
            havingValue = "HS",
            matchIfMissing = true
    )
    @Qualifier("jWSProvider")
    @Order(42)
    public JWSProvider HSJWSProvider(JwtAlgorithmConfig algorithmConfig) {
        return new HSJWSAlgorithmProvider(algorithmConfig);
    }

    @Bean
    @ConditionalOnProperty(
            name = "kpro.security.jwt.config.algorithm",
            havingValue = "RSA"
    )
    @Qualifier("jWSProvider")
    @Order(43)
    public JWSProvider RSAJWSProvider(JwtAlgorithmConfig algorithmConfig) {
        return new RSAJWSAlgorithmProvider(algorithmConfig);
    }

    @Bean
    @Qualifier("tokenUtils")
    @Order(44)
    public InternalTokenUtils tokenUtils(@Qualifier("jWSProvider") JWSProvider jwsProvider) {
        return new InternalTokenUtils(jwsProvider);
    }

    @Bean
    @Qualifier("jwtAuthenticationFilter")
    @Order(45)
    public Filter jwtAuthenticationFilter(@Qualifier("tokenUtils") InternalTokenUtils tokenUtils, PublicPathConfigProperties configProperties) {
        return new JwtAuthenticationFilter(tokenUtils, configProperties);
    }
}
