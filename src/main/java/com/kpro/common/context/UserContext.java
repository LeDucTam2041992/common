package com.kpro.common.context;

import java.util.List;

public class UserContext {
  private static final ThreadLocal<String> currentUser = new ThreadLocal<>();
  private static final ThreadLocal<List<String>> languages = new ThreadLocal<>();

  private UserContext() {
    throw new IllegalStateException("Utility class");
  }

  public static String getUser() {
    return currentUser.get();
  }

  public static void setUser(String username) {
    currentUser.set(username);
  }

  public static List<String> getLanguages() {
    return languages.get();
  }

  public static void setLanguages(List<String> locales) {
    languages.set(locales);
  }

  public static void clearAll() {
    currentUser.remove();
    languages.remove();
  }
}
