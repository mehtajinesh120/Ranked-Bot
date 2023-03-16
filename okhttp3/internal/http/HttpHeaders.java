/*     */ package okhttp3.internal.http;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import okhttp3.Challenge;
/*     */ import okhttp3.Cookie;
/*     */ import okhttp3.CookieJar;
/*     */ import okhttp3.Headers;
/*     */ import okhttp3.HttpUrl;
/*     */ import okhttp3.Request;
/*     */ import okhttp3.Response;
/*     */ import okhttp3.internal.Util;
/*     */ import okio.Buffer;
/*     */ import okio.ByteString;
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
/*     */ public final class HttpHeaders
/*     */ {
/*  45 */   private static final ByteString QUOTED_STRING_DELIMITERS = ByteString.encodeUtf8("\"\\");
/*  46 */   private static final ByteString TOKEN_DELIMITERS = ByteString.encodeUtf8("\t ,=");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long contentLength(Response response) {
/*  52 */     return contentLength(response.headers());
/*     */   }
/*     */   
/*     */   public static long contentLength(Headers headers) {
/*  56 */     return stringToLong(headers.get("Content-Length"));
/*     */   }
/*     */   
/*     */   private static long stringToLong(String s) {
/*  60 */     if (s == null) return -1L; 
/*     */     try {
/*  62 */       return Long.parseLong(s);
/*  63 */     } catch (NumberFormatException e) {
/*  64 */       return -1L;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean varyMatches(Response cachedResponse, Headers cachedRequest, Request newRequest) {
/*  74 */     for (String field : varyFields(cachedResponse)) {
/*  75 */       if (!Objects.equals(cachedRequest.values(field), newRequest.headers(field))) return false; 
/*     */     } 
/*  77 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasVaryAll(Response response) {
/*  84 */     return hasVaryAll(response.headers());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasVaryAll(Headers responseHeaders) {
/*  91 */     return varyFields(responseHeaders).contains("*");
/*     */   }
/*     */   
/*     */   private static Set<String> varyFields(Response response) {
/*  95 */     return varyFields(response.headers());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<String> varyFields(Headers responseHeaders) {
/* 102 */     Set<String> result = Collections.emptySet();
/* 103 */     for (int i = 0, size = responseHeaders.size(); i < size; i++) {
/* 104 */       if ("Vary".equalsIgnoreCase(responseHeaders.name(i))) {
/*     */         
/* 106 */         String value = responseHeaders.value(i);
/* 107 */         if (result.isEmpty()) {
/* 108 */           result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
/*     */         }
/* 110 */         for (String varyField : value.split(","))
/* 111 */           result.add(varyField.trim()); 
/*     */       } 
/*     */     } 
/* 114 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Headers varyHeaders(Response response) {
/* 125 */     Headers requestHeaders = response.networkResponse().request().headers();
/* 126 */     Headers responseHeaders = response.headers();
/* 127 */     return varyHeaders(requestHeaders, responseHeaders);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Headers varyHeaders(Headers requestHeaders, Headers responseHeaders) {
/* 135 */     Set<String> varyFields = varyFields(responseHeaders);
/* 136 */     if (varyFields.isEmpty()) return Util.EMPTY_HEADERS;
/*     */     
/* 138 */     Headers.Builder result = new Headers.Builder();
/* 139 */     for (int i = 0, size = requestHeaders.size(); i < size; i++) {
/* 140 */       String fieldName = requestHeaders.name(i);
/* 141 */       if (varyFields.contains(fieldName)) {
/* 142 */         result.add(fieldName, requestHeaders.value(i));
/*     */       }
/*     */     } 
/* 145 */     return result.build();
/*     */   }
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
/*     */   public static List<Challenge> parseChallenges(Headers responseHeaders, String headerName) {
/* 170 */     List<Challenge> result = new ArrayList<>();
/* 171 */     for (int h = 0; h < responseHeaders.size(); h++) {
/* 172 */       if (headerName.equalsIgnoreCase(responseHeaders.name(h))) {
/* 173 */         Buffer header = (new Buffer()).writeUtf8(responseHeaders.value(h));
/* 174 */         parseChallengeHeader(result, header);
/*     */       } 
/*     */     } 
/* 177 */     return result;
/*     */   }
/*     */   
/*     */   private static void parseChallengeHeader(List<Challenge> result, Buffer header) {
/* 181 */     String peek = null;
/*     */ 
/*     */     
/*     */     while (true) {
/* 185 */       if (peek == null) {
/* 186 */         skipWhitespaceAndCommas(header);
/* 187 */         peek = readToken(header);
/* 188 */         if (peek == null)
/*     */           return; 
/*     */       } 
/* 191 */       String schemeName = peek;
/*     */ 
/*     */       
/* 194 */       boolean commaPrefixed = skipWhitespaceAndCommas(header);
/* 195 */       peek = readToken(header);
/* 196 */       if (peek == null) {
/* 197 */         if (!header.exhausted())
/* 198 */           return;  result.add(new Challenge(schemeName, Collections.emptyMap()));
/*     */         
/*     */         return;
/*     */       } 
/* 202 */       int eqCount = skipAll(header, (byte)61);
/* 203 */       boolean commaSuffixed = skipWhitespaceAndCommas(header);
/*     */ 
/*     */       
/* 206 */       if (!commaPrefixed && (commaSuffixed || header.exhausted())) {
/* 207 */         result.add(new Challenge(schemeName, Collections.singletonMap(null, peek + 
/* 208 */                 repeat('=', eqCount))));
/* 209 */         peek = null;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 214 */       Map<String, String> parameters = new LinkedHashMap<>();
/* 215 */       eqCount += skipAll(header, (byte)61);
/*     */       while (true) {
/* 217 */         if (peek == null) {
/* 218 */           peek = readToken(header);
/* 219 */           if (skipWhitespaceAndCommas(header))
/* 220 */             break;  eqCount = skipAll(header, (byte)61);
/*     */         } 
/* 222 */         if (eqCount == 0)
/* 223 */           break;  if (eqCount > 1)
/* 224 */           return;  if (skipWhitespaceAndCommas(header)) {
/*     */           return;
/*     */         }
/*     */         
/* 228 */         String parameterValue = (!header.exhausted() && header.getByte(0L) == 34) ? readQuotedString(header) : readToken(header);
/* 229 */         if (parameterValue == null)
/* 230 */           return;  String replaced = parameters.put(peek, parameterValue);
/* 231 */         peek = null;
/* 232 */         if (replaced != null)
/* 233 */           return;  if (!skipWhitespaceAndCommas(header) && !header.exhausted())
/*     */           return; 
/* 235 */       }  result.add(new Challenge(schemeName, parameters));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean skipWhitespaceAndCommas(Buffer buffer) {
/* 241 */     boolean commaFound = false;
/* 242 */     while (!buffer.exhausted()) {
/* 243 */       byte b = buffer.getByte(0L);
/* 244 */       if (b == 44) {
/* 245 */         buffer.readByte();
/* 246 */         commaFound = true; continue;
/* 247 */       }  if (b == 32 || b == 9) {
/* 248 */         buffer.readByte();
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 253 */     return commaFound;
/*     */   }
/*     */   
/*     */   private static int skipAll(Buffer buffer, byte b) {
/* 257 */     int count = 0;
/* 258 */     while (!buffer.exhausted() && buffer.getByte(0L) == b) {
/* 259 */       count++;
/* 260 */       buffer.readByte();
/*     */     } 
/* 262 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String readQuotedString(Buffer buffer) {
/* 271 */     if (buffer.readByte() != 34) throw new IllegalArgumentException(); 
/* 272 */     Buffer result = new Buffer();
/*     */     while (true) {
/* 274 */       long i = buffer.indexOfElement(QUOTED_STRING_DELIMITERS);
/* 275 */       if (i == -1L) return null;
/*     */       
/* 277 */       if (buffer.getByte(i) == 34) {
/* 278 */         result.write(buffer, i);
/* 279 */         buffer.readByte();
/* 280 */         return result.readUtf8();
/*     */       } 
/*     */       
/* 283 */       if (buffer.size() == i + 1L) return null; 
/* 284 */       result.write(buffer, i);
/* 285 */       buffer.readByte();
/* 286 */       result.write(buffer, 1L);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String readToken(Buffer buffer) {
/*     */     try {
/* 296 */       long tokenSize = buffer.indexOfElement(TOKEN_DELIMITERS);
/* 297 */       if (tokenSize == -1L) tokenSize = buffer.size();
/*     */       
/* 299 */       return (tokenSize != 0L) ? 
/* 300 */         buffer.readUtf8(tokenSize) : 
/* 301 */         null;
/* 302 */     } catch (EOFException e) {
/* 303 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String repeat(char c, int count) {
/* 308 */     char[] array = new char[count];
/* 309 */     Arrays.fill(array, c);
/* 310 */     return new String(array);
/*     */   }
/*     */   
/*     */   public static void receiveHeaders(CookieJar cookieJar, HttpUrl url, Headers headers) {
/* 314 */     if (cookieJar == CookieJar.NO_COOKIES)
/*     */       return; 
/* 316 */     List<Cookie> cookies = Cookie.parseAll(url, headers);
/* 317 */     if (cookies.isEmpty())
/*     */       return; 
/* 319 */     cookieJar.saveFromResponse(url, cookies);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasBody(Response response) {
/* 325 */     if (response.request().method().equals("HEAD")) {
/* 326 */       return false;
/*     */     }
/*     */     
/* 329 */     int responseCode = response.code();
/* 330 */     if ((responseCode < 100 || responseCode >= 200) && responseCode != 204 && responseCode != 304)
/*     */     {
/*     */       
/* 333 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 338 */     if (contentLength(response) != -1L || "chunked"
/* 339 */       .equalsIgnoreCase(response.header("Transfer-Encoding"))) {
/* 340 */       return true;
/*     */     }
/*     */     
/* 343 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int skipUntil(String input, int pos, String characters) {
/* 351 */     for (; pos < input.length() && 
/* 352 */       characters.indexOf(input.charAt(pos)) == -1; pos++);
/*     */ 
/*     */ 
/*     */     
/* 356 */     return pos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int skipWhitespace(String input, int pos) {
/* 364 */     for (; pos < input.length(); pos++) {
/* 365 */       char c = input.charAt(pos);
/* 366 */       if (c != ' ' && c != '\t') {
/*     */         break;
/*     */       }
/*     */     } 
/* 370 */     return pos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int parseSeconds(String value, int defaultValue) {
/*     */     try {
/* 379 */       long seconds = Long.parseLong(value);
/* 380 */       if (seconds > 2147483647L)
/* 381 */         return Integer.MAX_VALUE; 
/* 382 */       if (seconds < 0L) {
/* 383 */         return 0;
/*     */       }
/* 385 */       return (int)seconds;
/*     */     }
/* 387 */     catch (NumberFormatException e) {
/* 388 */       return defaultValue;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\http\HttpHeaders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */