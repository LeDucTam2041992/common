package com.kpro.common.sercurity;

import com.kpro.common.sercurity.configurer.HttpSecurityConfigurer;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnProperty(name = "kpro.security.enable", havingValue = "true", matchIfMissing = true)
public class CoreSecurityConfigurerAdapter {
  private static final Logger log = LoggerFactory.getLogger(CoreSecurityConfigurerAdapter.class);
  @Autowired private List<HttpSecurityConfigurer> configurers;

  public CoreSecurityConfigurerAdapter() {
    log.info("{} start create core security config", getClass().getSimpleName());
  }

  public void setConfigurers(List<HttpSecurityConfigurer> configurers) {
    this.configurers = configurers;
  }

  @Bean
  @ConditionalOnBean(CoreSecurityConfigurerAdapter.class)
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
    log.info("{} start create security filter chain", getClass().getSimpleName());
    for (HttpSecurityConfigurer configurer : this.configurers) {
      configurer.configure(httpSecurity);
    }
    return httpSecurity.build();
  }
}
