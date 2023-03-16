/*     */ package org.jsoup.internal;
/*     */ 
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Stack;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.Nullable;
/*     */ import org.jsoup.helper.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StringUtil
/*     */ {
/*  20 */   static final String[] padding = new String[] { "", " ", "  ", "   ", "    ", "     ", "      ", "       ", "        ", "         ", "          ", "           ", "            ", "             ", "              ", "               ", "                ", "                 ", "                  ", "                   ", "                    " };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String join(Collection<?> strings, String sep) {
/*  31 */     return join(strings.iterator(), sep);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String join(Iterator<?> strings, String sep) {
/*  41 */     if (!strings.hasNext()) {
/*  42 */       return "";
/*     */     }
/*  44 */     String start = strings.next().toString();
/*  45 */     if (!strings.hasNext()) {
/*  46 */       return start;
/*     */     }
/*  48 */     StringJoiner j = new StringJoiner(sep);
/*  49 */     j.add(start);
/*  50 */     while (strings.hasNext()) {
/*  51 */       j.add(strings.next());
/*     */     }
/*  53 */     return j.complete();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String join(String[] strings, String sep) {
/*  63 */     return join(Arrays.asList((Object[])strings), sep);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class StringJoiner
/*     */   {
/*     */     @Nullable
/*  71 */     StringBuilder sb = StringUtil.borrowBuilder();
/*     */ 
/*     */     
/*     */     final String separator;
/*     */ 
/*     */     
/*     */     boolean first = true;
/*     */ 
/*     */ 
/*     */     
/*     */     public StringJoiner(String separator) {
/*  82 */       this.separator = separator;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public StringJoiner add(Object stringy) {
/*  89 */       Validate.notNull(this.sb);
/*  90 */       if (!this.first)
/*  91 */         this.sb.append(this.separator); 
/*  92 */       this.sb.append(stringy);
/*  93 */       this.first = false;
/*  94 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public StringJoiner append(Object stringy) {
/* 101 */       Validate.notNull(this.sb);
/* 102 */       this.sb.append(stringy);
/* 103 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String complete() {
/* 110 */       String string = StringUtil.releaseBuilder(this.sb);
/* 111 */       this.sb = null;
/* 112 */       return string;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String padding(int width) {
/* 123 */     return padding(width, 30);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String padding(int width, int maxPaddingWidth) {
/* 133 */     Validate.isTrue((width >= 0), "width must be >= 0");
/* 134 */     Validate.isTrue((maxPaddingWidth >= -1));
/* 135 */     if (maxPaddingWidth != -1)
/* 136 */       width = Math.min(width, maxPaddingWidth); 
/* 137 */     if (width < padding.length)
/* 138 */       return padding[width]; 
/* 139 */     char[] out = new char[width];
/* 140 */     for (int i = 0; i < width; i++)
/* 141 */       out[i] = ' '; 
/* 142 */     return String.valueOf(out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isBlank(String string) {
/* 151 */     if (string == null || string.length() == 0) {
/* 152 */       return true;
/*     */     }
/* 154 */     int l = string.length();
/* 155 */     for (int i = 0; i < l; i++) {
/* 156 */       if (!isWhitespace(string.codePointAt(i)))
/* 157 */         return false; 
/*     */     } 
/* 159 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean startsWithNewline(String string) {
/* 168 */     if (string == null || string.length() == 0)
/* 169 */       return false; 
/* 170 */     return (string.charAt(0) == '\n');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNumeric(String string) {
/* 179 */     if (string == null || string.length() == 0) {
/* 180 */       return false;
/*     */     }
/* 182 */     int l = string.length();
/* 183 */     for (int i = 0; i < l; i++) {
/* 184 */       if (!Character.isDigit(string.codePointAt(i)))
/* 185 */         return false; 
/*     */     } 
/* 187 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isWhitespace(int c) {
/* 197 */     return (c == 32 || c == 9 || c == 10 || c == 12 || c == 13);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isActuallyWhitespace(int c) {
/* 206 */     return (c == 32 || c == 9 || c == 10 || c == 12 || c == 13 || c == 160);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isInvisibleChar(int c) {
/* 211 */     return (c == 8203 || c == 173);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String normaliseWhitespace(String string) {
/* 222 */     StringBuilder sb = borrowBuilder();
/* 223 */     appendNormalisedWhitespace(sb, string, false);
/* 224 */     return releaseBuilder(sb);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void appendNormalisedWhitespace(StringBuilder accum, String string, boolean stripLeading) {
/* 234 */     boolean lastWasWhite = false;
/* 235 */     boolean reachedNonWhite = false;
/*     */     
/* 237 */     int len = string.length();
/*     */     int i;
/* 239 */     for (i = 0; i < len; i += Character.charCount(c)) {
/* 240 */       int c = string.codePointAt(i);
/* 241 */       if (isActuallyWhitespace(c)) {
/* 242 */         if ((!stripLeading || reachedNonWhite) && !lastWasWhite) {
/*     */           
/* 244 */           accum.append(' ');
/* 245 */           lastWasWhite = true;
/*     */         } 
/* 247 */       } else if (!isInvisibleChar(c)) {
/* 248 */         accum.appendCodePoint(c);
/* 249 */         lastWasWhite = false;
/* 250 */         reachedNonWhite = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean in(String needle, String... haystack) {
/* 256 */     int len = haystack.length;
/* 257 */     for (int i = 0; i < len; i++) {
/* 258 */       if (haystack[i].equals(needle))
/* 259 */         return true; 
/*     */     } 
/* 261 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean inSorted(String needle, String[] haystack) {
/* 265 */     return (Arrays.binarySearch((Object[])haystack, needle) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAscii(String string) {
/* 274 */     Validate.notNull(string);
/* 275 */     for (int i = 0; i < string.length(); i++) {
/* 276 */       int c = string.charAt(i);
/* 277 */       if (c > 127) {
/* 278 */         return false;
/*     */       }
/*     */     } 
/* 281 */     return true;
/*     */   }
/*     */   
/* 284 */   private static final Pattern extraDotSegmentsPattern = Pattern.compile("^/((\\.{1,2}/)+)");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL resolve(URL base, String relUrl) throws MalformedURLException {
/* 293 */     relUrl = stripControlChars(relUrl);
/*     */     
/* 295 */     if (relUrl.startsWith("?")) {
/* 296 */       relUrl = base.getPath() + relUrl;
/*     */     }
/* 298 */     URL url = new URL(base, relUrl);
/* 299 */     String fixedFile = extraDotSegmentsPattern.matcher(url.getFile()).replaceFirst("/");
/* 300 */     if (url.getRef() != null) {
/* 301 */       fixedFile = fixedFile + "#" + url.getRef();
/*     */     }
/* 303 */     return new URL(url.getProtocol(), url.getHost(), url.getPort(), fixedFile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String resolve(String baseUrl, String relUrl) {
/* 314 */     baseUrl = stripControlChars(baseUrl); relUrl = stripControlChars(relUrl);
/*     */     try {
/*     */       URL base;
/*     */       try {
/* 318 */         base = new URL(baseUrl);
/* 319 */       } catch (MalformedURLException e) {
/*     */         
/* 321 */         URL abs = new URL(relUrl);
/* 322 */         return abs.toExternalForm();
/*     */       } 
/* 324 */       return resolve(base, relUrl).toExternalForm();
/* 325 */     } catch (MalformedURLException e) {
/*     */       URL base;
/*     */       
/* 328 */       return validUriScheme.matcher(relUrl).find() ? relUrl : "";
/*     */     } 
/*     */   }
/* 331 */   private static final Pattern validUriScheme = Pattern.compile("^[a-zA-Z][a-zA-Z0-9+-.]*:");
/*     */   
/* 333 */   private static final Pattern controlChars = Pattern.compile("[\\x00-\\x1f]*");
/*     */   private static String stripControlChars(String input) {
/* 335 */     return controlChars.matcher(input).replaceAll("");
/*     */   }
/*     */   
/* 338 */   private static final ThreadLocal<Stack<StringBuilder>> threadLocalBuilders = new ThreadLocal<Stack<StringBuilder>>()
/*     */     {
/*     */       protected Stack<StringBuilder> initialValue() {
/* 341 */         return new Stack<>();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MaxCachedBuilderSize = 8192;
/*     */   
/*     */   private static final int MaxIdleBuilders = 8;
/*     */ 
/*     */   
/*     */   public static StringBuilder borrowBuilder() {
/* 353 */     Stack<StringBuilder> builders = threadLocalBuilders.get();
/* 354 */     return builders.empty() ? 
/* 355 */       new StringBuilder(8192) : 
/* 356 */       builders.pop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String releaseBuilder(StringBuilder sb) {
/* 366 */     Validate.notNull(sb);
/* 367 */     String string = sb.toString();
/*     */     
/* 369 */     if (sb.length() > 8192) {
/* 370 */       sb = new StringBuilder(8192);
/*     */     } else {
/* 372 */       sb.delete(0, sb.length());
/*     */     } 
/* 374 */     Stack<StringBuilder> builders = threadLocalBuilders.get();
/* 375 */     builders.push(sb);
/*     */     
/* 377 */     while (builders.size() > 8) {
/* 378 */       builders.pop();
/*     */     }
/* 380 */     return string;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\internal\StringUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */