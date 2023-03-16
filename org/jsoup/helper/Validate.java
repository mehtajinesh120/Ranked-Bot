/*     */ package org.jsoup.helper;
/*     */ 
/*     */ import javax.annotation.Nullable;
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
/*     */ public final class Validate
/*     */ {
/*     */   public static void notNull(@Nullable Object obj) {
/*  18 */     if (obj == null) {
/*  19 */       throw new ValidationException("Object must not be null");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void notNullParam(@Nullable Object obj, String param) {
/*  30 */     if (obj == null) {
/*  31 */       throw new ValidationException(String.format("The parameter '%s' must not be null.", new Object[] { param }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void notNull(@Nullable Object obj, String msg) {
/*  41 */     if (obj == null) {
/*  42 */       throw new ValidationException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object ensureNotNull(@Nullable Object obj) {
/*  53 */     if (obj == null)
/*  54 */       throw new ValidationException("Object must not be null"); 
/*  55 */     return obj;
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
/*     */   public static Object ensureNotNull(@Nullable Object obj, String msg, Object... args) {
/*  68 */     if (obj == null)
/*  69 */       throw new ValidationException(String.format(msg, args)); 
/*  70 */     return obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void isTrue(boolean val) {
/*  79 */     if (!val) {
/*  80 */       throw new ValidationException("Must be true");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void isTrue(boolean val, String msg) {
/*  90 */     if (!val) {
/*  91 */       throw new ValidationException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void isFalse(boolean val) {
/* 100 */     if (val) {
/* 101 */       throw new ValidationException("Must be false");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void isFalse(boolean val, String msg) {
/* 111 */     if (val) {
/* 112 */       throw new ValidationException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void noNullElements(Object[] objects) {
/* 121 */     noNullElements(objects, "Array must not contain any null objects");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void noNullElements(Object[] objects, String msg) {
/* 131 */     for (Object obj : objects) {
/* 132 */       if (obj == null) {
/* 133 */         throw new ValidationException(msg);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void notEmpty(@Nullable String string) {
/* 142 */     if (string == null || string.length() == 0) {
/* 143 */       throw new ValidationException("String must not be empty");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void notEmptyParam(@Nullable String string, String param) {
/* 153 */     if (string == null || string.length() == 0) {
/* 154 */       throw new ValidationException(String.format("The '%s' parameter must not be empty.", new Object[] { param }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void notEmpty(@Nullable String string, String msg) {
/* 164 */     if (string == null || string.length() == 0) {
/* 165 */       throw new ValidationException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void wtf(String msg) {
/* 174 */     throw new IllegalStateException(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void fail(String msg) {
/* 183 */     throw new ValidationException(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void fail(String msg, Object... args) {
/* 193 */     throw new ValidationException(String.format(msg, args));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\helper\Validate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */