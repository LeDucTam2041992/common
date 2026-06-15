package com.kpro.common.persistence.config;

import com.kpro.common.sercurity.UserPrincipal;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@ConditionalOnClass(name = "jakarta.persistence.EntityListeners")
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class PersistenceConfig {

  @Bean
  public AuditorAware<String> auditorProvider() {
    return new AuditorAwareImpl();
  }

  public static class AuditorAwareImpl implements AuditorAware<String> {

    private static final String SYSTEM = "system";

    @Override
    public Optional<String> getCurrentAuditor() {
      return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
          .filter(auth -> auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken))
          .map(auth -> {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserPrincipal userPrincipal) {
              return userPrincipal.getUserId();
            }
            return principal.toString();
          })
          .or(() -> Optional.of(SYSTEM));
    }
  }
}
