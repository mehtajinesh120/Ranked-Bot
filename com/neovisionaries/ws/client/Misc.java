/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URI;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.Collection;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Misc
/*     */ {
/*  40 */   private static final SecureRandom sRandom = new SecureRandom();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getBytesUTF8(String string) {
/*  53 */     if (string == null)
/*     */     {
/*  55 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*  60 */       return string.getBytes("UTF-8");
/*     */     }
/*  62 */     catch (UnsupportedEncodingException e) {
/*     */ 
/*     */       
/*  65 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toStringUTF8(byte[] bytes) {
/*  75 */     if (bytes == null)
/*     */     {
/*  77 */       return null;
/*     */     }
/*     */     
/*  80 */     return toStringUTF8(bytes, 0, bytes.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toStringUTF8(byte[] bytes, int offset, int length) {
/*  89 */     if (bytes == null)
/*     */     {
/*  91 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*  96 */       return new String(bytes, offset, length, "UTF-8");
/*     */     }
/*  98 */     catch (UnsupportedEncodingException e) {
/*     */ 
/*     */       
/* 101 */       return null;
/*     */     }
/* 103 */     catch (IndexOutOfBoundsException e) {
/*     */       
/* 105 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] nextBytes(byte[] buffer) {
/* 115 */     sRandom.nextBytes(buffer);
/*     */     
/* 117 */     return buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] nextBytes(int nBytes) {
/* 126 */     byte[] buffer = new byte[nBytes];
/*     */     
/* 128 */     return nextBytes(buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toOpcodeName(int opcode) {
/* 137 */     switch (opcode) {
/*     */       
/*     */       case 0:
/* 140 */         return "CONTINUATION";
/*     */       
/*     */       case 1:
/* 143 */         return "TEXT";
/*     */       
/*     */       case 2:
/* 146 */         return "BINARY";
/*     */       
/*     */       case 8:
/* 149 */         return "CLOSE";
/*     */       
/*     */       case 9:
/* 152 */         return "PING";
/*     */       
/*     */       case 10:
/* 155 */         return "PONG";
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     if (1 <= opcode && opcode <= 7)
/*     */     {
/* 163 */       return String.format("DATA(0x%X)", new Object[] { Integer.valueOf(opcode) });
/*     */     }
/*     */     
/* 166 */     if (8 <= opcode && opcode <= 15)
/*     */     {
/* 168 */       return String.format("CONTROL(0x%X)", new Object[] { Integer.valueOf(opcode) });
/*     */     }
/*     */     
/* 171 */     return String.format("0x%X", new Object[] { Integer.valueOf(opcode) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String readLine(InputStream in, String charset) throws IOException {
/* 180 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 185 */       int b = in.read();
/*     */ 
/*     */       
/* 188 */       if (b == -1) {
/*     */         
/* 190 */         if (baos.size() == 0)
/*     */         {
/*     */           
/* 193 */           return null;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         break;
/*     */       } 
/*     */ 
/*     */       
/* 202 */       if (b == 10) {
/*     */         break;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 208 */       if (b != 13) {
/*     */ 
/*     */         
/* 211 */         baos.write(b);
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 216 */       int b2 = in.read();
/*     */ 
/*     */       
/* 219 */       if (b2 == -1) {
/*     */ 
/*     */         
/* 222 */         baos.write(b);
/*     */ 
/*     */         
/*     */         break;
/*     */       } 
/*     */ 
/*     */       
/* 229 */       if (b2 == 10) {
/*     */         break;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 236 */       baos.write(b);
/*     */ 
/*     */       
/* 239 */       baos.write(b2);
/*     */     } 
/*     */ 
/*     */     
/* 243 */     return baos.toString(charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int min(int[] values) {
/* 252 */     int min = Integer.MAX_VALUE;
/*     */     
/* 254 */     for (int i = 0; i < values.length; i++) {
/*     */       
/* 256 */       if (values[i] < min)
/*     */       {
/* 258 */         min = values[i];
/*     */       }
/*     */     } 
/*     */     
/* 262 */     return min;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int max(int[] values) {
/* 271 */     int max = Integer.MIN_VALUE;
/*     */     
/* 273 */     for (int i = 0; i < values.length; i++) {
/*     */       
/* 275 */       if (max < values[i])
/*     */       {
/* 277 */         max = values[i];
/*     */       }
/*     */     } 
/*     */     
/* 281 */     return max;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String join(Collection<?> values, String delimiter) {
/* 287 */     StringBuilder builder = new StringBuilder();
/*     */     
/* 289 */     join(builder, values, delimiter);
/*     */     
/* 291 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void join(StringBuilder builder, Collection<?> values, String delimiter) {
/* 297 */     boolean first = true;
/*     */     
/* 299 */     for (Object value : values) {
/*     */       
/* 301 */       if (first) {
/*     */         
/* 303 */         first = false;
/*     */       }
/*     */       else {
/*     */         
/* 307 */         builder.append(delimiter);
/*     */       } 
/*     */       
/* 310 */       builder.append(value.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String extractHost(URI uri) {
/* 318 */     String host = uri.getHost();
/*     */     
/* 320 */     if (host != null)
/*     */     {
/* 322 */       return host;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 335 */     host = extractHostFromAuthorityPart(uri.getRawAuthority());
/*     */     
/* 337 */     if (host != null)
/*     */     {
/* 339 */       return host;
/*     */     }
/*     */ 
/*     */     
/* 343 */     return extractHostFromEntireUri(uri.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String extractHostFromAuthorityPart(String authority) {
/* 350 */     if (authority == null)
/*     */     {
/*     */       
/* 353 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 357 */     Matcher matcher = Pattern.compile("^(.*@)?([^:]+)(:\\d+)?$").matcher(authority);
/*     */ 
/*     */     
/* 360 */     if (matcher == null || !matcher.matches())
/*     */     {
/*     */       
/* 363 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 367 */     return matcher.group(2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static String extractHostFromEntireUri(String uri) {
/* 373 */     if (uri == null)
/*     */     {
/*     */       
/* 376 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 380 */     Matcher matcher = Pattern.compile("^\\w+://([^@/]*@)?([^:/]+)(:\\d+)?(/.*)?$").matcher(uri);
/*     */ 
/*     */     
/* 383 */     if (matcher == null || !matcher.matches())
/*     */     {
/*     */       
/* 386 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 390 */     return matcher.group(2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Constructor<?> getConstructor(String className, Class<?>[] parameterTypes) {
/*     */     try {
/* 398 */       return Class.forName(className).getConstructor(parameterTypes);
/*     */     }
/* 400 */     catch (Exception e) {
/*     */       
/* 402 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object newInstance(Constructor<?> constructor, Object... parameters) {
/* 409 */     if (constructor == null)
/*     */     {
/* 411 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 416 */       return constructor.newInstance(parameters);
/*     */     }
/* 418 */     catch (Exception e) {
/*     */       
/* 420 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method getMethod(String className, String methodName, Class<?>[] parameterTypes) {
/*     */     try {
/* 429 */       return Class.forName(className).getMethod(methodName, parameterTypes);
/*     */     }
/* 431 */     catch (Exception e) {
/*     */       
/* 433 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object invoke(Method method, Object object, Object... parameters) {
/* 440 */     if (method == null)
/*     */     {
/* 442 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 447 */       return method.invoke(object, parameters);
/*     */     }
/* 449 */     catch (Exception e) {
/*     */       
/* 451 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\Misc.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */