package com.kpro.common.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseLog {

  Mode mode() default Mode.INHERIT;

  /**
   * Field names containing any configured string will be masked. An empty method-level list
   * inherits the class-level list.
   */
  String[] maskFields() default {};

  enum Mode {
    INHERIT,
    ENABLE,
    DISABLE
  }
}
