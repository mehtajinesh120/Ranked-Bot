package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import java.lang.reflect.Type;

public abstract class TypeModifier {
  public abstract JavaType modifyType(JavaType paramJavaType, Type paramType, TypeBindings paramTypeBindings, TypeFactory paramTypeFactory);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\type\TypeModifier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */