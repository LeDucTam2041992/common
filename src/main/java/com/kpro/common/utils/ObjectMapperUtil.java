package com.kpro.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectMapperUtil {

  private static final ObjectMapper INSTANCE;

  static {
    INSTANCE = new ObjectMapper();
    // Register JavaTimeModule to handle LocalDateTime, LocalDate, etc.
    INSTANCE.registerModule(new JavaTimeModule());

    // standard configurations
    INSTANCE.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    INSTANCE.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  private ObjectMapperUtil() {
    // Private constructor to prevent instantiation
  }

  /**
   * Convert an Object (DTO, Map, etc.) to a JSON String
   */
  public static String toJson(Object object) {
    try {
      return INSTANCE.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("Error converting object to JSON string", e);
      return null;
    }
  }

  /**
   * Convert a JSON String to a specific Class
   */
  public static <T> Optional<T> toObject(String json, Class<T> clazz) {
    if (json == null || json.isEmpty()) {
      return Optional.empty();
    }
    try {
      return Optional.of(INSTANCE.readValue(json, clazz));
    } catch (JsonProcessingException e) {
      log.error("Error converting JSON string to object of type {}", clazz.getName(), e);
      return Optional.empty();
    }
  }

  /**
   * Convert a JSON String to a TypeReference (e.g., Map<String, String>)
   */
  public static <T> T toReference(String json, TypeReference<T> typeReference) {
    if (json == null || json.isEmpty()) {
      return null;
    }
    try {
      return INSTANCE.readValue(json, typeReference);
    } catch (JsonProcessingException e) {
      log.error("Error converting JSON string to TypeReference", e);
      return null;
    }
  }

  public static ObjectMapper getMapper() {
    return INSTANCE;
  }
}
