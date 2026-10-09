package com.kpro.common.log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ResponseLogAspect {

  private final ObjectMapper objectMapper;

  @Around(
      "@within(ResponseLog) || @annotation(ResponseLog)"
  )
  public Object logResponse(ProceedingJoinPoint joinPoint)
      throws Throwable {

    MethodSignature signature =
        (MethodSignature) joinPoint.getSignature();

    Method method = AopUtils.getMostSpecificMethod(
        signature.getMethod(),
        joinPoint.getTarget().getClass()
    );

    ResponseLog methodConfig =
        AnnotatedElementUtils.findMergedAnnotation(
            method, ResponseLog.class
        );

    ResponseLog classConfig =
        AnnotatedElementUtils.findMergedAnnotation(
            joinPoint.getTarget().getClass(),
            ResponseLog.class
        );

    ResponseLog.Mode mode = resolveMode(
        methodConfig, classConfig
    );

    if (mode != ResponseLog.Mode.ENABLE) {
      return joinPoint.proceed();
    }

    List<String> maskFields = resolveMaskFields(
        methodConfig, classConfig
    );

    Object response = joinPoint.proceed();

    try {
      JsonNode json = objectMapper.valueToTree(response);

      maskFields(json, maskFields);

      log.info(
          "[ResponseLog] {}.{} response={}",
          method.getDeclaringClass().getSimpleName(),
          method.getName(),
          objectMapper.writeValueAsString(json)
      );
    } catch (Exception e) {
      // Logging failure must not change the API response.
      log.warn(
          "[ResponseLog] Failed to serialize response for {}.{}",
          method.getDeclaringClass().getSimpleName(),
          method.getName(),
          e
      );
    }

    return response;
  }

  private ResponseLog.Mode resolveMode(
      ResponseLog methodConfig,
      ResponseLog classConfig) {

    if (methodConfig != null
        && methodConfig.mode() != ResponseLog.Mode.INHERIT) {
      return methodConfig.mode();
    }

    return classConfig != null
        ? classConfig.mode()
        : ResponseLog.Mode.DISABLE;
  }

  private List<String> resolveMaskFields(
      ResponseLog methodConfig,
      ResponseLog classConfig) {

    if (methodConfig != null
        && methodConfig.maskFields().length > 0) {
      return toLowerCaseList(methodConfig.maskFields());
    }

    if (classConfig != null) {
      return toLowerCaseList(classConfig.maskFields());
    }

    return List.of();
  }

  private List<String> toLowerCaseList(String[] fields) {
    return Arrays.stream(fields)
        .map(s -> s.toLowerCase(Locale.ROOT))
        .toList();
  }

  private void maskFields(
      JsonNode node,
      List<String> maskFields) {

    if (node instanceof ObjectNode objectNode) {
      var fields = objectNode.fields();

      while (fields.hasNext()) {
        var entry = fields.next();
        String fieldName =
            entry.getKey().toLowerCase(Locale.ROOT);

        boolean shouldMask = maskFields.stream()
            .anyMatch(fieldName::contains);

        if (shouldMask) {
          objectNode.put(entry.getKey(), "***");
        } else {
          maskFields(entry.getValue(), maskFields);
        }
      }
    } else if (node instanceof ArrayNode arrayNode) {
      for (JsonNode item : arrayNode) {
        maskFields(item, maskFields);
      }
    }
  }
}

