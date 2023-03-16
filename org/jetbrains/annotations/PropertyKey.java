package org.jetbrains.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.FIELD})
public @interface PropertyKey {
  String resourceBundle();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jetbrains\annotations\PropertyKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */