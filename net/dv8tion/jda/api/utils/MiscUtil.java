/*     */ package net.dv8tion.jda.api.utils;
/*     */ 
/*     */ import gnu.trove.impl.sync.TSynchronizedLongObjectMap;
/*     */ import gnu.trove.map.TLongObjectMap;
/*     */ import gnu.trove.map.hash.TLongObjectHashMap;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.util.Formatter;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.function.Supplier;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.Helpers;
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
/*     */ public class MiscUtil
/*     */ {
/*     */   public static int getShardForGuild(long guildId, int shards) {
/*  51 */     return (int)((guildId >>> 22L) % shards);
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
/*     */   public static int getShardForGuild(String guildId, int shards) {
/*  70 */     return getShardForGuild(parseSnowflake(guildId), shards);
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
/*     */   public static int getShardForGuild(Guild guild, int shards) {
/*  89 */     return getShardForGuild(guild.getIdLong(), shards);
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
/*     */   public static <T> TLongObjectMap<T> newLongMap() {
/* 102 */     return (TLongObjectMap<T>)new TSynchronizedLongObjectMap((TLongObjectMap)new TLongObjectHashMap(), new Object());
/*     */   }
/*     */ 
/*     */   
/*     */   public static long parseLong(String input) {
/* 107 */     if (input.startsWith("-")) {
/* 108 */       return Long.parseLong(input);
/*     */     }
/* 110 */     return Long.parseUnsignedLong(input);
/*     */   }
/*     */ 
/*     */   
/*     */   public static long parseSnowflake(String input) {
/* 115 */     Checks.notEmpty(input, "ID");
/*     */     
/*     */     try {
/* 118 */       return parseLong(input);
/*     */     }
/* 120 */     catch (NumberFormatException ex) {
/*     */       
/* 122 */       throw new NumberFormatException(
/* 123 */           Helpers.format("The specified ID is not a valid snowflake (%s). Expecting a valid long value!", new Object[] { input }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> E locked(ReentrantLock lock, Supplier<E> task) {
/*     */     try {
/* 131 */       tryLock(lock);
/* 132 */       return task.get();
/*     */     }
/*     */     finally {
/*     */       
/* 136 */       if (lock.isHeldByCurrentThread()) {
/* 137 */         lock.unlock();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void locked(ReentrantLock lock, Runnable task) {
/*     */     try {
/* 145 */       tryLock(lock);
/* 146 */       task.run();
/*     */     }
/*     */     finally {
/*     */       
/* 150 */       if (lock.isHeldByCurrentThread()) {
/* 151 */         lock.unlock();
/*     */       }
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
/*     */   public static void tryLock(Lock lock) {
/*     */     try {
/* 168 */       if (!lock.tryLock() && !lock.tryLock(10L, TimeUnit.SECONDS)) {
/* 169 */         throw new IllegalStateException("Could not acquire lock in a reasonable timeframe! (10 seconds)");
/*     */       }
/* 171 */     } catch (InterruptedException e) {
/*     */       
/* 173 */       throw new IllegalStateException("Unable to acquire lock while thread is interrupted!");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void appendTo(Formatter formatter, int width, int precision, boolean leftJustified, String out) {
/*     */     try {
/* 195 */       Appendable appendable = formatter.out();
/* 196 */       if (precision > -1 && out.length() > precision) {
/*     */         
/* 198 */         appendable.append(Helpers.truncate(out, precision));
/*     */         
/*     */         return;
/*     */       } 
/* 202 */       if (leftJustified) {
/* 203 */         appendable.append(Helpers.rightPad(out, width));
/*     */       } else {
/* 205 */         appendable.append(Helpers.leftPad(out, width));
/*     */       } 
/* 207 */     } catch (IOException e) {
/*     */       
/* 209 */       throw new UncheckedIOException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\MiscUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */