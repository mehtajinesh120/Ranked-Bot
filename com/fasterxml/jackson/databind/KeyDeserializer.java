package com.fasterxml.jackson.databind;

import java.io.IOException;

public abstract class KeyDeserializer {
  public abstract Object deserializeKey(String paramString, DeserializationContext paramDeserializationContext) throws IOException;
  
  public static abstract class None extends KeyDeserializer {}
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\KeyDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */