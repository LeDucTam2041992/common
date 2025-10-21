package com.kpro.common.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(KproBaseConfig.class)
public @interface EnableKproApplication {
  @AliasFor(annotation = Import.class, attribute = "value")
  Class<?>[] value() default {KproBaseConfig.class};
}
