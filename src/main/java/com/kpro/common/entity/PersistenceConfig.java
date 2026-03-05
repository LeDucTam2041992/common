//package com.kpro.common.entity;
//
//import java.util.Optional;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//@Configuration
//@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
//@Slf4j
//public class PersistenceConfig {
//
//  @Bean
//  AuditorAware<String> auditorProvider() {
//    return new AuditorAwareImpl();
//  }
//
//  public static class AuditorAwareImpl implements AuditorAware<String> {
//
//    @Override
//    public Optional<String> getCurrentAuditor() {
//      try {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String user = (String) authentication.getPrincipal();
//        return Optional.ofNullable(user);
//
//      } catch (Exception e) {
//        log.error("PersistenceConfig get user by auditorProvider error [{}]", e.getMessage());
//        throw e;
//      }
//    }
//  }
//}
