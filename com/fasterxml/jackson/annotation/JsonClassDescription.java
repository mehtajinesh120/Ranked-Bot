package com.fasterxml.jackson.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotation
public @interface JsonClassDescription {
  String value() default "";
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\annotation\JsonClassDescription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */