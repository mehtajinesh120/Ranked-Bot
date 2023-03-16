/*     */ package net.dv8tion.jda.api.utils;
/*     */ 
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.time.temporal.TemporalAccessor;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum TimeFormat
/*     */ {
/*  44 */   TIME_SHORT("t"),
/*     */   
/*  46 */   TIME_LONG("T"),
/*     */   
/*  48 */   DATE_SHORT("d"),
/*     */   
/*  50 */   DATE_LONG("D"),
/*     */   
/*  52 */   DATE_TIME_SHORT("f"),
/*     */   
/*  54 */   DATE_TIME_LONG("F"),
/*     */   
/*  56 */   RELATIVE("R");
/*     */   public static final TimeFormat DEFAULT;
/*     */   public static final Pattern MARKDOWN;
/*     */   private final String style;
/*     */   
/*     */   static {
/*  62 */     DEFAULT = DATE_TIME_SHORT;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     MARKDOWN = Pattern.compile("<t:(?<time>-?\\d{1,17})(?::(?<style>[tTdDfFR]))?>");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   TimeFormat(String style) {
/* 100 */     this.style = style;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getStyle() {
/* 112 */     return this.style;
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
/*     */   @Nonnull
/*     */   public static TimeFormat fromStyle(@Nonnull String style) {
/* 129 */     Checks.notEmpty(style, "Style");
/* 130 */     Checks.notLonger(style, 1, "Style");
/* 131 */     for (TimeFormat format : values()) {
/*     */       
/* 133 */       if (format.style.equals(style))
/* 134 */         return format; 
/*     */     } 
/* 136 */     return DEFAULT;
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
/*     */   @Nonnull
/*     */   public static Timestamp parse(@Nonnull String markdown) {
/* 154 */     Checks.notNull(markdown, "Markdown");
/* 155 */     Matcher matcher = MARKDOWN.matcher(markdown.trim());
/* 156 */     if (!matcher.find())
/* 157 */       throw new IllegalArgumentException("Invalid markdown format! Provided: " + markdown); 
/* 158 */     String format = matcher.group("style");
/* 159 */     return new Timestamp((format == null) ? DEFAULT : fromStyle(format), Long.parseLong(matcher.group("time")) * 1000L);
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
/*     */   @Nonnull
/*     */   public String format(@Nonnull TemporalAccessor temporal) {
/* 180 */     Checks.notNull(temporal, "Temporal");
/* 181 */     long timestamp = Instant.from(temporal).toEpochMilli();
/* 182 */     return format(timestamp);
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
/*     */   @Nonnull
/*     */   public String format(long timestamp) {
/* 197 */     return "<t:" + (timestamp / 1000L) + ":" + this.style + ">";
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
/*     */   @Nonnull
/*     */   public Timestamp atInstant(@Nonnull Instant instant) {
/* 219 */     Checks.notNull(instant, "Instant");
/* 220 */     return new Timestamp(this, instant.toEpochMilli());
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
/*     */   @Nonnull
/*     */   public Timestamp atTimestamp(long timestamp) {
/* 237 */     return new Timestamp(this, timestamp);
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
/*     */   @Nonnull
/*     */   public Timestamp now() {
/* 251 */     return new Timestamp(this, System.currentTimeMillis());
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
/*     */   @Nonnull
/*     */   public Timestamp after(@Nonnull Duration duration) {
/* 271 */     return now().plus(duration);
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
/*     */   @Nonnull
/*     */   public Timestamp after(long millis) {
/* 288 */     return now().plus(millis);
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
/*     */   @Nonnull
/*     */   public Timestamp before(@Nonnull Duration duration) {
/* 308 */     return now().minus(duration);
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
/*     */   @Nonnull
/*     */   public Timestamp before(long millis) {
/* 325 */     return now().minus(millis);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\TimeFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */