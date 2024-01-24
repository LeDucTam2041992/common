package com.kpro.common.entity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class PersistenceConfig {

  @Bean
  AuditorAware<String> auditorProvider() {
    return new AuditorAwareImpl();
  }

  public static class AuditorAwareImpl implements AuditorAware<String> {
    private static final String ADMIN = "tamld";
    @Override
    public Optional<String> getCurrentAuditor() {
      try {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
          String user = (String) authentication.getPrincipal();
          if (user == null) return Optional.of(ADMIN);
          return Optional.of(user);
        }
        return Optional.of(ADMIN);
      } catch (Exception e) {
        return Optional.of(ADMIN);
      }
    }
  }
}
