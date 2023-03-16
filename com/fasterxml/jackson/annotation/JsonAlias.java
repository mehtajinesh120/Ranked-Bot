package com.fasterxml.jackson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonAlias {
  String[] value() default {};
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\annotation\JsonAlias.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */