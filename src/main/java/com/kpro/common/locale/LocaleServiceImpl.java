package com.kpro.common.locale;

import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public record LocaleServiceImpl(MessageSource messageSource,
                                LocaleProperties localeProperties) implements
    LocaleService {

  @Override
  public String translate(Locale locale, String key, String defaultMessage) {
    try {
      return this.messageSource.getMessage(key, null, locale);
    } catch (Exception ex) {
      log.warn("Cannot get message source with locale [{}] key [{}] error [{}]", locale, key,
          ex.getMessage());
      return defaultMessage;
    }
  }

  @Override
  public String translate(Locale locale, String key, String defaultMessage, Object... args) {
    try {
      return this.messageSource.getMessage(key, args, locale);
    } catch (Exception ex) {
      log.warn("Cannot get message source with locale [{}] key [{}] error [{}]", locale, key,
          ex.getMessage());
      return defaultMessage;
    }
  }
}

