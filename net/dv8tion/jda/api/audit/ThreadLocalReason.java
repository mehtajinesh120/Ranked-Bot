/*     */ package net.dv8tion.jda.api.audit;
/*     */ 
/*     */ import javax.annotation.Nonnull;
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
/*     */ public final class ThreadLocalReason
/*     */ {
/*     */   private static ThreadLocal<String> currentReason;
/*     */   
/*     */   private ThreadLocalReason() {
/*  68 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setCurrent(@Nullable String reason) {
/*  79 */     if (reason != null) {
/*     */       
/*  81 */       if (currentReason == null)
/*  82 */         currentReason = new ThreadLocal<>(); 
/*  83 */       currentReason.set(reason);
/*     */     }
/*  85 */     else if (currentReason != null) {
/*     */       
/*  87 */       currentReason.remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void resetCurrent() {
/*  96 */     if (currentReason != null) {
/*  97 */       currentReason.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static String getCurrent() {
/* 108 */     return (currentReason == null) ? null : currentReason.get();
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
/*     */   public static Closable closable(@Nullable String reason) {
/* 123 */     return new Closable(reason);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Closable
/*     */     implements AutoCloseable
/*     */   {
/*     */     private final String previous;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Closable(@Nullable String reason) {
/* 146 */       this.previous = ThreadLocalReason.getCurrent();
/* 147 */       ThreadLocalReason.setCurrent(reason);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() {
/* 153 */       ThreadLocalReason.setCurrent(this.previous);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\audit\ThreadLocalReason.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */