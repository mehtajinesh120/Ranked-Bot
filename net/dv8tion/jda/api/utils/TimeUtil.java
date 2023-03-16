/*    */ package net.dv8tion.jda.api.utils;
/*    */ 
/*    */ import java.time.OffsetDateTime;
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import java.util.Calendar;
/*    */ import java.util.TimeZone;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.entities.ISnowflake;
/*    */ import net.dv8tion.jda.internal.utils.Checks;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TimeUtil
/*    */ {
/*    */   public static final long DISCORD_EPOCH = 1420070400000L;
/*    */   public static final long TIMESTAMP_OFFSET = 22L;
/* 32 */   private static final DateTimeFormatter dtFormatter = DateTimeFormatter.RFC_1123_DATE_TIME;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static long getDiscordTimestamp(long millisTimestamp) {
/* 45 */     return millisTimestamp - 1420070400000L << 22L;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public static OffsetDateTime getTimeCreated(long entityId) {
/* 60 */     long timestamp = (entityId >>> 22L) + 1420070400000L;
/* 61 */     Calendar gmt = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 62 */     gmt.setTimeInMillis(timestamp);
/* 63 */     return OffsetDateTime.ofInstant(gmt.toInstant(), gmt.getTimeZone().toZoneId());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public static OffsetDateTime getTimeCreated(@Nonnull ISnowflake entity) {
/* 81 */     Checks.notNull(entity, "Entity");
/* 82 */     return getTimeCreated(entity.getIdLong());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public static String getDateTimeString(@Nonnull OffsetDateTime time) {
/* 96 */     return time.format(dtFormatter);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\TimeUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */