package com.kpro.common.utils;

import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexPatternUtils {
  private static final String VIETNAMESE_DIACRITIC_CHARACTERS =
      "ẮẰẲẴẶĂẤẦẨẪẬÂÁÀÃẢẠĐẾỀỂỄỆÊÉÈẺẼẸÍÌỈĨỊỐỒỔỖỘÔỚỜỞỠỢƠÓÒÕỎỌỨỪỬỮỰƯÚÙỦŨỤÝỲỶỸỴ";
  private static final String ALPHABET_CHARACTERS = "^[a-zA-Z0-9]*$";

  public static Pattern getViPattern() {
    return Pattern.compile(
        "(?:[" + VIETNAMESE_DIACRITIC_CHARACTERS + "a-z0-9\\s])++",
        Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
  }

  public static Pattern getAlphabetPattern() {
    return Pattern.compile(ALPHABET_CHARACTERS);
  }

  public static Pattern getViPatternWithDotAndComma() {
    return Pattern.compile(
        "(?:[" + VIETNAMESE_DIACRITIC_CHARACTERS + ".,a-z0-9\\s])++",
        Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
  }

  public static Pattern getViPatternWithUnderscore() {
    return Pattern.compile(
        "(?:[" + VIETNAMESE_DIACRITIC_CHARACTERS + "_a-z0-9\\s])++",
        Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
  }
}
