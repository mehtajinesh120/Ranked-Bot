package com.fasterxml.jackson.databind.jsonFormatVisitors;

import com.fasterxml.jackson.core.JsonParser;

public interface JsonIntegerFormatVisitor extends JsonValueFormatVisitor {
  void numberType(JsonParser.NumberType paramNumberType);
  
  public static class Base extends JsonValueFormatVisitor.Base implements JsonIntegerFormatVisitor {
    public void numberType(JsonParser.NumberType type) {}
  }
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\jsonFormatVisitors\JsonIntegerFormatVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */