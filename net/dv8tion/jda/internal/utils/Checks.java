/*     */ package net.dv8tion.jda.internal.utils;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import java.util.regex.Pattern;
/*     */ import org.jetbrains.annotations.Contract;
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
/*     */ public class Checks
/*     */ {
/*  27 */   public static final Pattern ALPHANUMERIC_WITH_DASH = Pattern.compile("[\\w-]+", 256);
/*  28 */   public static final Pattern ALPHANUMERIC = Pattern.compile("[\\w]+", 256);
/*     */ 
/*     */   
/*     */   @Contract("null -> fail")
/*     */   public static void isSnowflake(String snowflake) {
/*  33 */     isSnowflake(snowflake, snowflake);
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract("null, _ -> fail")
/*     */   public static void isSnowflake(String snowflake, String message) {
/*  39 */     notNull(snowflake, message);
/*  40 */     if (snowflake.length() > 20 || !Helpers.isNumeric(snowflake)) {
/*  41 */       throw new IllegalArgumentException(message + " is not a valid snowflake value! Provided: \"" + snowflake + "\"");
/*     */     }
/*     */   }
/*     */   
/*     */   @Contract("false, _ -> fail")
/*     */   public static void check(boolean expression, String message) {
/*  47 */     if (!expression) {
/*  48 */       throw new IllegalArgumentException(message);
/*     */     }
/*     */   }
/*     */   
/*     */   @Contract("false, _, _ -> fail")
/*     */   public static void check(boolean expression, String message, Object... args) {
/*  54 */     if (!expression) {
/*  55 */       throw new IllegalArgumentException(String.format(message, args));
/*     */     }
/*     */   }
/*     */   
/*     */   @Contract("false, _, _ -> fail")
/*     */   public static void check(boolean expression, String message, Object arg) {
/*  61 */     if (!expression) {
/*  62 */       throw new IllegalArgumentException(String.format(message, new Object[] { arg }));
/*     */     }
/*     */   }
/*     */   
/*     */   @Contract("null, _ -> fail")
/*     */   public static void notNull(Object argument, String name) {
/*  68 */     if (argument == null) {
/*  69 */       throw new IllegalArgumentException(name + " may not be null");
/*     */     }
/*     */   }
/*     */   
/*     */   @Contract("null, _ -> fail")
/*     */   public static void notEmpty(CharSequence argument, String name) {
/*  75 */     notNull(argument, name);
/*  76 */     if (Helpers.isEmpty(argument)) {
/*  77 */       throw new IllegalArgumentException(name + " may not be empty");
/*     */     }
/*     */   }
/*     */   
/*     */   @Contract("null, _ -> fail")
/*     */   public static void notBlank(CharSequence argument, String name) {
/*  83 */     notNull(argument, name);
/*  84 */     if (Helpers.isBlank(argument)) {
/*  85 */       throw new IllegalArgumentException(name + " may not be blank");
/*     */     }
/*     */   }
/*     */   
/*     */   @Contract("null, _ -> fail")
/*     */   public static void noWhitespace(CharSequence argument, String name) {
/*  91 */     notNull(argument, name);
/*  92 */     if (Helpers.containsWhitespace(argument)) {
/*  93 */       throw new IllegalArgumentException(name + " may not contain blanks. Provided: \"" + argument + "\"");
/*     */     }
/*     */   }
/*     */   
/*     */   @Contract("null, _ -> fail")
/*     */   public static void notEmpty(Collection<?> argument, String name) {
/*  99 */     notNull(argument, name);
/* 100 */     if (argument.isEmpty()) {
/* 101 */       throw new IllegalArgumentException(name + " may not be empty");
/*     */     }
/*     */   }
/*     */   
/*     */   @Contract("null, _ -> fail")
/*     */   public static void notEmpty(Object[] argument, String name) {
/* 107 */     notNull(argument, name);
/* 108 */     if (argument.length == 0) {
/* 109 */       throw new IllegalArgumentException(name + " may not be empty");
/*     */     }
/*     */   }
/*     */   
/*     */   @Contract("null, _ -> fail")
/*     */   public static void noneNull(Collection<?> argument, String name) {
/* 115 */     notNull(argument, name);
/* 116 */     argument.forEach(it -> notNull(it, name));
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract("null, _ -> fail")
/*     */   public static void noneNull(Object[] argument, String name) {
/* 122 */     notNull(argument, name);
/* 123 */     for (Object it : argument) {
/* 124 */       notNull(it, name);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract("null, _ -> fail")
/*     */   public static <T extends CharSequence> void noneEmpty(Collection<T> argument, String name) {
/* 131 */     notNull(argument, name);
/* 132 */     argument.forEach(it -> notEmpty(it, name));
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract("null, _ -> fail")
/*     */   public static <T extends CharSequence> void noneBlank(Collection<T> argument, String name) {
/* 138 */     notNull(argument, name);
/* 139 */     argument.forEach(it -> notBlank(it, name));
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract("null, _ -> fail")
/*     */   public static <T extends CharSequence> void noneContainBlanks(Collection<T> argument, String name) {
/* 145 */     notNull(argument, name);
/* 146 */     argument.forEach(it -> noWhitespace(it, name));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void inRange(String input, int min, int max, String name) {
/* 151 */     notNull(input, name);
/* 152 */     int length = Helpers.codePointLength(input);
/* 153 */     check((min <= length && length <= max), "%s must be between %d and %d characters long! Provided: \"%s\"", new Object[] { name, 
/*     */           
/* 155 */           Integer.valueOf(min), Integer.valueOf(max), input });
/*     */   }
/*     */ 
/*     */   
/*     */   public static void notLonger(String input, int length, String name) {
/* 160 */     notNull(input, name);
/* 161 */     check((Helpers.codePointLength(input) <= length), "%s may not be longer than %d characters! Provided: \"%s\"", new Object[] { name, Integer.valueOf(length), input });
/*     */   }
/*     */ 
/*     */   
/*     */   public static void matches(String input, Pattern pattern, String name) {
/* 166 */     notNull(input, name);
/* 167 */     check(pattern.matcher(input).matches(), "%s must match regex ^%s$. Provided: \"%s\"", new Object[] { name, pattern.pattern(), input });
/*     */   }
/*     */ 
/*     */   
/*     */   public static void isLowercase(String input, String name) {
/* 172 */     notNull(input, name);
/* 173 */     check(input.toLowerCase(Locale.ROOT).equals(input), "%s must be lowercase only! Provided: \"%s\"", new Object[] { name, input });
/*     */   }
/*     */ 
/*     */   
/*     */   public static void positive(int n, String name) {
/* 178 */     if (n <= 0) {
/* 179 */       throw new IllegalArgumentException(name + " may not be negative or zero");
/*     */     }
/*     */   }
/*     */   
/*     */   public static void positive(long n, String name) {
/* 184 */     if (n <= 0L) {
/* 185 */       throw new IllegalArgumentException(name + " may not be negative or zero");
/*     */     }
/*     */   }
/*     */   
/*     */   public static void notNegative(int n, String name) {
/* 190 */     if (n < 0) {
/* 191 */       throw new IllegalArgumentException(name + " may not be negative");
/*     */     }
/*     */   }
/*     */   
/*     */   public static void notNegative(long n, String name) {
/* 196 */     if (n < 0L)
/* 197 */       throw new IllegalArgumentException(name + " may not be negative"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\Checks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */