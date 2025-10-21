package com.kpro.common.locale;

import java.util.Locale;

public interface LocaleService {

  String translate(Locale locale, String key, String defaultMessage);

  String translate(Locale locale, String key, String defaultMessage, Object... args);
}
