/*     */ package okhttp3.internal;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.IDN;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.AccessControlException;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.KeyStore;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import okhttp3.Headers;
/*     */ import okhttp3.HttpUrl;
/*     */ import okhttp3.RequestBody;
/*     */ import okhttp3.ResponseBody;
/*     */ import okhttp3.internal.http2.Header;
/*     */ import okio.Buffer;
/*     */ import okio.BufferedSource;
/*     */ import okio.ByteString;
/*     */ import okio.Options;
/*     */ import okio.Source;
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
/*     */ public final class Util
/*     */ {
/*  65 */   public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*  66 */   public static final String[] EMPTY_STRING_ARRAY = new String[0];
/*  67 */   public static final Headers EMPTY_HEADERS = Headers.of(new String[0]);
/*     */   
/*  69 */   public static final ResponseBody EMPTY_RESPONSE = ResponseBody.create(null, EMPTY_BYTE_ARRAY);
/*  70 */   public static final RequestBody EMPTY_REQUEST = RequestBody.create(null, EMPTY_BYTE_ARRAY);
/*     */ 
/*     */   
/*  73 */   private static final Options UNICODE_BOMS = Options.of(new ByteString[] {
/*  74 */         ByteString.decodeHex("efbbbf"), 
/*  75 */         ByteString.decodeHex("feff"), 
/*  76 */         ByteString.decodeHex("fffe"), 
/*  77 */         ByteString.decodeHex("0000ffff"), 
/*  78 */         ByteString.decodeHex("ffff0000")
/*     */       });
/*     */   
/*  81 */   private static final Charset UTF_32BE = Charset.forName("UTF-32BE");
/*  82 */   private static final Charset UTF_32LE = Charset.forName("UTF-32LE");
/*     */ 
/*     */   
/*  85 */   public static final TimeZone UTC = TimeZone.getTimeZone("GMT");
/*     */   
/*  87 */   public static final Comparator<String> NATURAL_ORDER = String::compareTo;
/*     */   
/*     */   private static final Method addSuppressedExceptionMethod;
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  94 */       m = Throwable.class.getDeclaredMethod("addSuppressed", new Class[] { Throwable.class });
/*  95 */     } catch (Exception e) {
/*  96 */       m = null;
/*     */     } 
/*  98 */     addSuppressedExceptionMethod = m;
/*     */   } static {
/*     */     Method m;
/*     */   } public static void addSuppressedIfPossible(Throwable e, Throwable suppressed) {
/* 102 */     if (addSuppressedExceptionMethod != null) {
/*     */       try {
/* 104 */         addSuppressedExceptionMethod.invoke(e, new Object[] { suppressed });
/* 105 */       } catch (InvocationTargetException|IllegalAccessException invocationTargetException) {}
/*     */     }
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
/* 120 */   private static final Pattern VERIFY_AS_IP_ADDRESS = Pattern.compile("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkOffsetAndCount(long arrayLength, long offset, long count) {
/* 127 */     if ((offset | count) < 0L || offset > arrayLength || arrayLength - offset < count) {
/* 128 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeQuietly(Closeable closeable) {
/* 137 */     if (closeable != null) {
/*     */       try {
/* 139 */         closeable.close();
/* 140 */       } catch (RuntimeException rethrown) {
/* 141 */         throw rethrown;
/* 142 */       } catch (Exception exception) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeQuietly(Socket socket) {
/* 152 */     if (socket != null) {
/*     */       try {
/* 154 */         socket.close();
/* 155 */       } catch (AssertionError e) {
/* 156 */         if (!isAndroidGetsocknameError(e)) throw e; 
/* 157 */       } catch (RuntimeException rethrown) {
/* 158 */         throw rethrown;
/* 159 */       } catch (Exception exception) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeQuietly(ServerSocket serverSocket) {
/* 169 */     if (serverSocket != null) {
/*     */       try {
/* 171 */         serverSocket.close();
/* 172 */       } catch (RuntimeException rethrown) {
/* 173 */         throw rethrown;
/* 174 */       } catch (Exception exception) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean discard(Source source, int timeout, TimeUnit timeUnit) {
/*     */     try {
/* 186 */       return skipAll(source, timeout, timeUnit);
/* 187 */     } catch (IOException e) {
/* 188 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean skipAll(Source source, int duration, TimeUnit timeUnit) throws IOException {
/* 197 */     long now = System.nanoTime();
/*     */ 
/*     */     
/* 200 */     long originalDuration = source.timeout().hasDeadline() ? (source.timeout().deadlineNanoTime() - now) : Long.MAX_VALUE;
/* 201 */     source.timeout().deadlineNanoTime(now + Math.min(originalDuration, timeUnit.toNanos(duration)));
/*     */     try {
/* 203 */       Buffer skipBuffer = new Buffer();
/* 204 */       while (source.read(skipBuffer, 8192L) != -1L) {
/* 205 */         skipBuffer.clear();
/*     */       }
/* 207 */       return true;
/* 208 */     } catch (InterruptedIOException e) {
/* 209 */       return false;
/*     */     } finally {
/* 211 */       if (originalDuration == Long.MAX_VALUE) {
/* 212 */         source.timeout().clearDeadline();
/*     */       } else {
/* 214 */         source.timeout().deadlineNanoTime(now + originalDuration);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> List<T> immutableList(List<T> list) {
/* 221 */     return Collections.unmodifiableList(new ArrayList<>(list));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> Map<K, V> immutableMap(Map<K, V> map) {
/* 226 */     return map.isEmpty() ? 
/* 227 */       Collections.<K, V>emptyMap() : 
/* 228 */       Collections.<K, V>unmodifiableMap(new LinkedHashMap<>(map));
/*     */   }
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public static <T> List<T> immutableList(T... elements) {
/* 234 */     return Collections.unmodifiableList(Arrays.asList((T[])elements.clone()));
/*     */   }
/*     */   
/*     */   public static ThreadFactory threadFactory(String name, boolean daemon) {
/* 238 */     return runnable -> {
/*     */         Thread result = new Thread(runnable, name);
/*     */         result.setDaemon(daemon);
/*     */         return result;
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] intersect(Comparator<? super String> comparator, String[] first, String[] second) {
/* 251 */     List<String> result = new ArrayList<>();
/* 252 */     for (String a : first) {
/* 253 */       for (String b : second) {
/* 254 */         if (comparator.compare(a, b) == 0) {
/* 255 */           result.add(a);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 260 */     return result.<String>toArray(new String[result.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean nonEmptyIntersection(Comparator<String> comparator, String[] first, String[] second) {
/* 271 */     if (first == null || second == null || first.length == 0 || second.length == 0) {
/* 272 */       return false;
/*     */     }
/* 274 */     for (String a : first) {
/* 275 */       for (String b : second) {
/* 276 */         if (comparator.compare(a, b) == 0) {
/* 277 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 281 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String hostHeader(HttpUrl url, boolean includeDefaultPort) {
/* 287 */     String host = url.host().contains(":") ? ("[" + url.host() + "]") : url.host();
/* 288 */     return (includeDefaultPort || url.port() != HttpUrl.defaultPort(url.scheme())) ? (
/* 289 */       host + ":" + url.port()) : 
/* 290 */       host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAndroidGetsocknameError(AssertionError e) {
/* 298 */     return (e.getCause() != null && e.getMessage() != null && e
/* 299 */       .getMessage().contains("getsockname failed"));
/*     */   }
/*     */   
/*     */   public static int indexOf(Comparator<String> comparator, String[] array, String value) {
/* 303 */     for (int i = 0, size = array.length; i < size; i++) {
/* 304 */       if (comparator.compare(array[i], value) == 0) return i; 
/*     */     } 
/* 306 */     return -1;
/*     */   }
/*     */   
/*     */   public static String[] concat(String[] array, String value) {
/* 310 */     String[] result = new String[array.length + 1];
/* 311 */     System.arraycopy(array, 0, result, 0, array.length);
/* 312 */     result[result.length - 1] = value;
/* 313 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int skipLeadingAsciiWhitespace(String input, int pos, int limit) {
/* 321 */     for (int i = pos; i < limit; i++) {
/* 322 */       switch (input.charAt(i)) {
/*     */         case '\t':
/*     */         case '\n':
/*     */         case '\f':
/*     */         case '\r':
/*     */         case ' ':
/*     */           break;
/*     */         default:
/* 330 */           return i;
/*     */       } 
/*     */     } 
/* 333 */     return limit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int skipTrailingAsciiWhitespace(String input, int pos, int limit) {
/* 341 */     for (int i = limit - 1; i >= pos; i--) {
/* 342 */       switch (input.charAt(i)) {
/*     */         case '\t':
/*     */         case '\n':
/*     */         case '\f':
/*     */         case '\r':
/*     */         case ' ':
/*     */           break;
/*     */         default:
/* 350 */           return i + 1;
/*     */       } 
/*     */     } 
/* 353 */     return pos;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String trimSubstring(String string, int pos, int limit) {
/* 358 */     int start = skipLeadingAsciiWhitespace(string, pos, limit);
/* 359 */     int end = skipTrailingAsciiWhitespace(string, start, limit);
/* 360 */     return string.substring(start, end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int delimiterOffset(String input, int pos, int limit, String delimiters) {
/* 368 */     for (int i = pos; i < limit; i++) {
/* 369 */       if (delimiters.indexOf(input.charAt(i)) != -1) return i; 
/*     */     } 
/* 371 */     return limit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int delimiterOffset(String input, int pos, int limit, char delimiter) {
/* 379 */     for (int i = pos; i < limit; i++) {
/* 380 */       if (input.charAt(i) == delimiter) return i; 
/*     */     } 
/* 382 */     return limit;
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
/*     */   public static String canonicalizeHost(String host) {
/* 395 */     if (host.contains(":")) {
/*     */ 
/*     */ 
/*     */       
/* 399 */       InetAddress inetAddress = (host.startsWith("[") && host.endsWith("]")) ? decodeIpv6(host, 1, host.length() - 1) : decodeIpv6(host, 0, host.length());
/* 400 */       if (inetAddress == null) return null; 
/* 401 */       byte[] address = inetAddress.getAddress();
/* 402 */       if (address.length == 16) return inet6AddressToAscii(address); 
/* 403 */       if (address.length == 4) return inetAddress.getHostAddress(); 
/* 404 */       throw new AssertionError("Invalid IPv6 address: '" + host + "'");
/*     */     } 
/*     */     
/*     */     try {
/* 408 */       String result = IDN.toASCII(host).toLowerCase(Locale.US);
/* 409 */       if (result.isEmpty()) return null;
/*     */ 
/*     */       
/* 412 */       if (containsInvalidHostnameAsciiCodes(result)) {
/* 413 */         return null;
/*     */       }
/*     */       
/* 416 */       return result;
/* 417 */     } catch (IllegalArgumentException e) {
/* 418 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean containsInvalidHostnameAsciiCodes(String hostnameAscii) {
/* 423 */     for (int i = 0; i < hostnameAscii.length(); i++) {
/* 424 */       char c = hostnameAscii.charAt(i);
/*     */ 
/*     */ 
/*     */       
/* 428 */       if (c <= '\037' || c >= '') {
/* 429 */         return true;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 434 */       if (" #%/:?@[\\]".indexOf(c) != -1) {
/* 435 */         return true;
/*     */       }
/*     */     } 
/* 438 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int indexOfControlOrNonAscii(String input) {
/* 447 */     for (int i = 0, length = input.length(); i < length; i++) {
/* 448 */       char c = input.charAt(i);
/* 449 */       if (c <= '\037' || c >= '') {
/* 450 */         return i;
/*     */       }
/*     */     } 
/* 453 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean verifyAsIpAddress(String host) {
/* 458 */     return VERIFY_AS_IP_ADDRESS.matcher(host).matches();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String format(String format, Object... args) {
/* 463 */     return String.format(Locale.US, format, args);
/*     */   }
/*     */   
/*     */   public static Charset bomAwareCharset(BufferedSource source, Charset charset) throws IOException {
/* 467 */     switch (source.select(UNICODE_BOMS)) { case 0:
/* 468 */         return StandardCharsets.UTF_8;
/* 469 */       case 1: return StandardCharsets.UTF_16BE;
/* 470 */       case 2: return StandardCharsets.UTF_16LE;
/* 471 */       case 3: return UTF_32BE;
/* 472 */       case 4: return UTF_32LE;
/* 473 */       case -1: return charset; }
/* 474 */      throw new AssertionError();
/*     */   }
/*     */ 
/*     */   
/*     */   public static int checkDuration(String name, long duration, TimeUnit unit) {
/* 479 */     if (duration < 0L) throw new IllegalArgumentException(name + " < 0"); 
/* 480 */     if (unit == null) throw new NullPointerException("unit == null"); 
/* 481 */     long millis = unit.toMillis(duration);
/* 482 */     if (millis > 2147483647L) throw new IllegalArgumentException(name + " too large."); 
/* 483 */     if (millis == 0L && duration > 0L) throw new IllegalArgumentException(name + " too small."); 
/* 484 */     return (int)millis;
/*     */   }
/*     */   
/*     */   public static int decodeHexDigit(char c) {
/* 488 */     if (c >= '0' && c <= '9') return c - 48; 
/* 489 */     if (c >= 'a' && c <= 'f') return c - 97 + 10; 
/* 490 */     if (c >= 'A' && c <= 'F') return c - 65 + 10; 
/* 491 */     return -1;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static InetAddress decodeIpv6(String input, int pos, int limit) {
/* 496 */     byte[] address = new byte[16];
/* 497 */     int b = 0;
/* 498 */     int compress = -1;
/* 499 */     int groupOffset = -1;
/*     */     
/* 501 */     for (int i = pos; i < limit; ) {
/* 502 */       if (b == address.length) return null;
/*     */ 
/*     */       
/* 505 */       if (i + 2 <= limit && input.regionMatches(i, "::", 0, 2))
/*     */       
/* 507 */       { if (compress != -1) return null; 
/* 508 */         i += 2;
/* 509 */         b += 2;
/* 510 */         compress = b;
/* 511 */         if (i == limit)
/* 512 */           break;  } else if (b != 0)
/*     */       
/* 514 */       { if (input.regionMatches(i, ":", 0, 1))
/* 515 */         { i++; }
/* 516 */         else { if (input.regionMatches(i, ".", 0, 1)) {
/*     */             
/* 518 */             if (!decodeIpv4Suffix(input, groupOffset, limit, address, b - 2)) return null; 
/* 519 */             b += 2;
/*     */             break;
/*     */           } 
/* 522 */           return null; }
/*     */          }
/*     */ 
/*     */ 
/*     */       
/* 527 */       int value = 0;
/* 528 */       groupOffset = i;
/* 529 */       for (; i < limit; i++) {
/* 530 */         char c = input.charAt(i);
/* 531 */         int hexDigit = decodeHexDigit(c);
/* 532 */         if (hexDigit == -1)
/* 533 */           break;  value = (value << 4) + hexDigit;
/*     */       } 
/* 535 */       int groupLength = i - groupOffset;
/* 536 */       if (groupLength == 0 || groupLength > 4) return null;
/*     */ 
/*     */       
/* 539 */       address[b++] = (byte)(value >>> 8 & 0xFF);
/* 540 */       address[b++] = (byte)(value & 0xFF);
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
/* 552 */     if (b != address.length) {
/* 553 */       if (compress == -1) return null; 
/* 554 */       System.arraycopy(address, compress, address, address.length - b - compress, b - compress);
/* 555 */       Arrays.fill(address, compress, compress + address.length - b, (byte)0);
/*     */     } 
/*     */     
/*     */     try {
/* 559 */       return InetAddress.getByAddress(address);
/* 560 */     } catch (UnknownHostException e) {
/* 561 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean decodeIpv4Suffix(String input, int pos, int limit, byte[] address, int addressOffset) {
/* 568 */     int b = addressOffset;
/*     */     
/* 570 */     for (int i = pos; i < limit; ) {
/* 571 */       if (b == address.length) return false;
/*     */ 
/*     */       
/* 574 */       if (b != addressOffset) {
/* 575 */         if (input.charAt(i) != '.') return false; 
/* 576 */         i++;
/*     */       } 
/*     */ 
/*     */       
/* 580 */       int value = 0;
/* 581 */       int groupOffset = i;
/* 582 */       for (; i < limit; i++) {
/* 583 */         char c = input.charAt(i);
/* 584 */         if (c < '0' || c > '9')
/* 585 */           break;  if (value == 0 && groupOffset != i) return false; 
/* 586 */         value = value * 10 + c - 48;
/* 587 */         if (value > 255) return false; 
/*     */       } 
/* 589 */       int groupLength = i - groupOffset;
/* 590 */       if (groupLength == 0) return false;
/*     */ 
/*     */       
/* 593 */       address[b++] = (byte)value;
/*     */     } 
/*     */     
/* 596 */     if (b != addressOffset + 4) return false; 
/* 597 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String inet6AddressToAscii(byte[] address) {
/* 605 */     int longestRunOffset = -1;
/* 606 */     int longestRunLength = 0;
/* 607 */     for (int i = 0; i < address.length; i += 2) {
/* 608 */       int currentRunOffset = i;
/* 609 */       while (i < 16 && address[i] == 0 && address[i + 1] == 0) {
/* 610 */         i += 2;
/*     */       }
/* 612 */       int currentRunLength = i - currentRunOffset;
/* 613 */       if (currentRunLength > longestRunLength && currentRunLength >= 4) {
/* 614 */         longestRunOffset = currentRunOffset;
/* 615 */         longestRunLength = currentRunLength;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 620 */     Buffer result = new Buffer();
/* 621 */     for (int j = 0; j < address.length; ) {
/* 622 */       if (j == longestRunOffset) {
/* 623 */         result.writeByte(58);
/* 624 */         j += longestRunLength;
/* 625 */         if (j == 16) result.writeByte(58);  continue;
/*     */       } 
/* 627 */       if (j > 0) result.writeByte(58); 
/* 628 */       int group = (address[j] & 0xFF) << 8 | address[j + 1] & 0xFF;
/* 629 */       result.writeHexadecimalUnsignedLong(group);
/* 630 */       j += 2;
/*     */     } 
/*     */     
/* 633 */     return result.readUtf8();
/*     */   }
/*     */   
/*     */   public static X509TrustManager platformTrustManager() {
/*     */     try {
/* 638 */       TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
/* 639 */           TrustManagerFactory.getDefaultAlgorithm());
/* 640 */       trustManagerFactory.init((KeyStore)null);
/* 641 */       TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
/* 642 */       if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
/* 643 */         throw new IllegalStateException("Unexpected default trust managers:" + 
/* 644 */             Arrays.toString(trustManagers));
/*     */       }
/* 646 */       return (X509TrustManager)trustManagers[0];
/* 647 */     } catch (GeneralSecurityException e) {
/* 648 */       throw new AssertionError("No System TLS", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Headers toHeaders(List<Header> headerBlock) {
/* 653 */     Headers.Builder builder = new Headers.Builder();
/* 654 */     for (Header header : headerBlock) {
/* 655 */       Internal.instance.addLenient(builder, header.name.utf8(), header.value.utf8());
/*     */     }
/* 657 */     return builder.build();
/*     */   }
/*     */   
/*     */   public static List<Header> toHeaderBlock(Headers headers) {
/* 661 */     List<Header> result = new ArrayList<>();
/* 662 */     for (int i = 0; i < headers.size(); i++) {
/* 663 */       result.add(new Header(headers.name(i), headers.value(i)));
/*     */     }
/* 665 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getSystemProperty(String key, @Nullable String defaultValue) {
/*     */     String value;
/*     */     try {
/* 675 */       value = System.getProperty(key);
/* 676 */     } catch (AccessControlException ex) {
/* 677 */       return defaultValue;
/*     */     } 
/* 679 */     return (value != null) ? value : defaultValue;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */