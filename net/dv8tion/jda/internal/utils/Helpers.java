/*     */ package net.dv8tion.jda.internal.utils;
/*     */ 
/*     */ import java.time.Instant;
/*     */ import java.time.OffsetDateTime;
/*     */ import java.time.ZoneOffset;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
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
/*     */ public final class Helpers
/*     */ {
/*  32 */   private static final ZoneOffset OFFSET = ZoneOffset.of("+00:00");
/*     */   
/*     */   private static final Consumer EMPTY_CONSUMER = v -> {
/*     */     
/*     */     };
/*     */   
/*     */   public static <T> Consumer<T> emptyConsumer() {
/*  39 */     return EMPTY_CONSUMER;
/*     */   }
/*     */ 
/*     */   
/*     */   public static OffsetDateTime toOffset(long instant) {
/*  44 */     return OffsetDateTime.ofInstant(Instant.ofEpochMilli(instant), OFFSET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String format(String format, Object... args) {
/*  51 */     return String.format(Locale.ROOT, format, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEmpty(CharSequence seq) {
/*  58 */     return (seq == null || seq.length() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean containsWhitespace(CharSequence seq) {
/*  63 */     if (isEmpty(seq))
/*  64 */       return false; 
/*  65 */     for (int i = 0; i < seq.length(); i++) {
/*     */       
/*  67 */       if (Character.isWhitespace(seq.charAt(i)))
/*  68 */         return true; 
/*     */     } 
/*  70 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isBlank(CharSequence seq) {
/*  75 */     if (isEmpty(seq))
/*  76 */       return true; 
/*  77 */     for (int i = 0; i < seq.length(); i++) {
/*     */       
/*  79 */       if (!Character.isWhitespace(seq.charAt(i)))
/*  80 */         return false; 
/*     */     } 
/*  82 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int countMatches(CharSequence seq, char c) {
/*  87 */     if (isEmpty(seq))
/*  88 */       return 0; 
/*  89 */     int count = 0;
/*  90 */     for (int i = 0; i < seq.length(); i++) {
/*     */       
/*  92 */       if (seq.charAt(i) == c)
/*  93 */         count++; 
/*     */     } 
/*  95 */     return count;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String truncate(String input, int maxWidth) {
/* 100 */     if (input == null)
/* 101 */       return null; 
/* 102 */     Checks.notNegative(maxWidth, "maxWidth");
/* 103 */     if (input.length() <= maxWidth)
/* 104 */       return input; 
/* 105 */     if (maxWidth == 0)
/* 106 */       return ""; 
/* 107 */     return input.substring(0, maxWidth);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String rightPad(String input, int size) {
/* 112 */     int pads = size - input.length();
/* 113 */     if (pads <= 0)
/* 114 */       return input; 
/* 115 */     StringBuilder out = new StringBuilder(input);
/* 116 */     for (int i = pads; i > 0; i--)
/* 117 */       out.append(' '); 
/* 118 */     return out.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String leftPad(String input, int size) {
/* 123 */     int pads = size - input.length();
/* 124 */     if (pads <= 0)
/* 125 */       return input; 
/* 126 */     StringBuilder out = new StringBuilder();
/* 127 */     for (int i = pads; i > 0; i--)
/* 128 */       out.append(' '); 
/* 129 */     return out.append(input).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isNumeric(String input) {
/* 134 */     if (isEmpty(input))
/* 135 */       return false; 
/* 136 */     for (char c : input.toCharArray()) {
/*     */       
/* 138 */       if (!Character.isDigit(c))
/* 139 */         return false; 
/*     */     } 
/* 141 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int codePointLength(String string) {
/* 146 */     return string.codePointCount(0, string.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean deepEquals(Collection<?> first, Collection<?> second) {
/* 153 */     if (first == second)
/* 154 */       return true; 
/* 155 */     if (first == null || second == null || first.size() != second.size())
/* 156 */       return false; 
/* 157 */     for (Iterator<?> itFirst = first.iterator(), itSecond = second.iterator(); itFirst.hasNext(); ) {
/*     */       
/* 159 */       Object elementFirst = itFirst.next();
/* 160 */       Object elementSecond = itSecond.next();
/* 161 */       if (!Objects.equals(elementFirst, elementSecond))
/* 162 */         return false; 
/*     */     } 
/* 164 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean deepEqualsUnordered(Collection<?> first, Collection<?> second) {
/* 169 */     if (first == second) return true; 
/* 170 */     if (first == null || second == null) return false; 
/* 171 */     return (first.size() == second.size() && second.containsAll(first));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E extends Enum<E>> EnumSet<E> copyEnumSet(Class<E> clazz, Collection<E> col) {
/* 176 */     return (col == null || col.isEmpty()) ? EnumSet.<E>noneOf(clazz) : EnumSet.<E>copyOf(col);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Throwable> T appendCause(T throwable, Throwable cause) {
/*     */     Throwable throwable1;
/* 183 */     T t = throwable;
/* 184 */     while (t.getCause() != null)
/* 185 */       throwable1 = t.getCause(); 
/* 186 */     throwable1.initCause(cause);
/* 187 */     return throwable;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean hasCause(Throwable throwable, Class<? extends Throwable> cause) {
/* 192 */     Throwable cursor = throwable;
/* 193 */     while (cursor != null) {
/*     */       
/* 195 */       if (cause.isInstance(cursor))
/* 196 */         return true; 
/* 197 */       cursor = cursor.getCause();
/*     */     } 
/* 199 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\Helpers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */