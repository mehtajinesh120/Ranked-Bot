/*     */ package org.sqlite.date;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.Format;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ 
/*     */ 
/*     */ abstract class FormatCache<F extends Format>
/*     */ {
/*     */   static final int NONE = -1;
/*  40 */   private final ConcurrentMap<MultipartKey, F> cInstanceCache = new ConcurrentHashMap<>(7);
/*     */ 
/*     */   
/*  43 */   private static final ConcurrentMap<MultipartKey, String> cDateTimeInstanceCache = new ConcurrentHashMap<>(7);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public F getInstance() {
/*  52 */     return getDateTimeInstance(3, 3, 
/*  53 */         TimeZone.getDefault(), Locale.getDefault());
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
/*     */   public F getInstance(String pattern, TimeZone timeZone, Locale locale) {
/*  66 */     if (pattern == null) {
/*  67 */       throw new NullPointerException("pattern must not be null");
/*     */     }
/*  69 */     if (timeZone == null) {
/*  70 */       timeZone = TimeZone.getDefault();
/*     */     }
/*  72 */     if (locale == null) {
/*  73 */       locale = Locale.getDefault();
/*     */     }
/*  75 */     MultipartKey key = new MultipartKey(new Object[] { pattern, timeZone, locale });
/*  76 */     Format format = (Format)this.cInstanceCache.get(key);
/*  77 */     if (format == null) {
/*  78 */       format = (Format)createInstance(pattern, timeZone, locale);
/*  79 */       Format format1 = (Format)this.cInstanceCache.putIfAbsent(key, (F)format);
/*  80 */       if (format1 != null)
/*     */       {
/*     */         
/*  83 */         format = format1;
/*     */       }
/*     */     } 
/*  86 */     return (F)format;
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
/*     */   protected abstract F createInstance(String paramString, TimeZone paramTimeZone, Locale paramLocale);
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
/*     */   private F getDateTimeInstance(Integer dateStyle, Integer timeStyle, TimeZone timeZone, Locale locale) {
/* 117 */     if (locale == null) {
/* 118 */       locale = Locale.getDefault();
/*     */     }
/* 120 */     String pattern = getPatternForStyle(dateStyle, timeStyle, locale);
/* 121 */     return getInstance(pattern, timeZone, locale);
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
/*     */   F getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {
/* 141 */     return getDateTimeInstance(
/* 142 */         Integer.valueOf(dateStyle), Integer.valueOf(timeStyle), timeZone, locale);
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
/*     */   F getDateInstance(int dateStyle, TimeZone timeZone, Locale locale) {
/* 157 */     return getDateTimeInstance(Integer.valueOf(dateStyle), (Integer)null, timeZone, locale);
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
/*     */   F getTimeInstance(int timeStyle, TimeZone timeZone, Locale locale) {
/* 172 */     return getDateTimeInstance((Integer)null, Integer.valueOf(timeStyle), timeZone, locale);
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
/*     */   static String getPatternForStyle(Integer dateStyle, Integer timeStyle, Locale locale) {
/* 187 */     MultipartKey key = new MultipartKey(new Object[] { dateStyle, timeStyle, locale });
/*     */     
/* 189 */     String pattern = cDateTimeInstanceCache.get(key);
/* 190 */     if (pattern == null) {
/*     */       try {
/*     */         DateFormat formatter;
/* 193 */         if (dateStyle == null) {
/* 194 */           formatter = DateFormat.getTimeInstance(timeStyle.intValue(), locale);
/* 195 */         } else if (timeStyle == null) {
/* 196 */           formatter = DateFormat.getDateInstance(dateStyle.intValue(), locale);
/*     */         } else {
/*     */           
/* 199 */           formatter = DateFormat.getDateTimeInstance(dateStyle
/* 200 */               .intValue(), timeStyle.intValue(), locale);
/*     */         } 
/* 202 */         pattern = ((SimpleDateFormat)formatter).toPattern();
/* 203 */         String previous = cDateTimeInstanceCache.putIfAbsent(key, pattern);
/* 204 */         if (previous != null)
/*     */         {
/*     */ 
/*     */           
/* 208 */           pattern = previous;
/*     */         }
/* 210 */       } catch (ClassCastException ex) {
/* 211 */         throw new IllegalArgumentException("No date time pattern for locale: " + locale);
/*     */       } 
/*     */     }
/* 214 */     return pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MultipartKey
/*     */   {
/*     */     private final Object[] keys;
/*     */ 
/*     */     
/*     */     private int hashCode;
/*     */ 
/*     */ 
/*     */     
/*     */     public MultipartKey(Object... keys) {
/* 229 */       this.keys = keys;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 238 */       return Arrays.equals(this.keys, ((MultipartKey)obj).keys);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 244 */       if (this.hashCode == 0) {
/* 245 */         int rc = 0;
/* 246 */         for (Object key : this.keys) {
/* 247 */           if (key != null) {
/* 248 */             rc = rc * 7 + key.hashCode();
/*     */           }
/*     */         } 
/* 251 */         this.hashCode = rc;
/*     */       } 
/* 253 */       return this.hashCode;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\date\FormatCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */