package okhttp3.internal.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;

@Documented
@Nonnull
@TypeQualifierDefault({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EverythingIsNonNull {}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\annotations\EverythingIsNonNull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */