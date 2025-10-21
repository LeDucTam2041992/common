package com.kpro.common.locale;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kpro.i18n")
public class LocaleProperties {
  private List<String> locales = List.of("vi", "en");
}
