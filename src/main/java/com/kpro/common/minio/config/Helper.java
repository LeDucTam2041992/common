package com.kpro.common.minio.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class Helper {
  private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  private static final SecureRandom rnd = new SecureRandom();

  private Helper() {}

  public static ObjectMapper getMapper() {
    return new ObjectMapper();
  }

  public static String randomString(final int maxLength) {
    StringBuilder sb = new StringBuilder(maxLength);

    for (int i = 0; i < maxLength; ++i) {
      sb.append(AB.charAt(rnd.nextInt(AB.length())));
    }

    return sb.toString();
  }

  public static Date nextDays(int days) {
    Calendar calendar = new GregorianCalendar();
    calendar.add(5, days);
    return calendar.getTime();
  }

  public static Date generateEndTime(Date start, Long duration) {
    long time = start.getTime();
    return new Date(time + duration);
  }

  public static String createConsumerS3Path(Object profileId, String appId, String fileName) {
    return "consumer/consumers/" + profileId + "/" + appId + "/" + fileName;
  }

  public static String createEmployeeS3Path(Object employeeId, String fileName) {
    return "consumer/employees/" + employeeId + "/" + fileName;
  }

  public static String retrieveUsernameFromEmail(String email) {
    int at = email.indexOf(64);
    return email.substring(0, at);
  }

  public static <T> String toJsonString(T object) {
    ObjectMapper mapper = new ObjectMapper();

    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException var3) {
      return "";
    }
  }

  public static <T> T toObject(String data, Class<T> clazz) {
    ObjectMapper mapper = new ObjectMapper();

    try {
      return mapper.readValue(data, clazz);
    } catch (IOException var4) {
      return null;
    }
  }

  public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
    Map<Object, Boolean> map = new ConcurrentHashMap<>();
    return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
  }
}
