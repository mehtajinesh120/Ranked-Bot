/*     */ package net.dv8tion.jda.api.utils;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @FunctionalInterface
/*     */ public interface ChunkingFilter
/*     */ {
/*     */   public static final ChunkingFilter ALL = x -> true;
/*     */   public static final ChunkingFilter NONE = x -> false;
/*     */   
/*     */   @Nonnull
/*     */   static ChunkingFilter include(@Nonnull long... ids) {
/*  71 */     Checks.notNull(ids, "ID array");
/*  72 */     if (ids.length == 0)
/*  73 */       return NONE; 
/*  74 */     return guild -> {
/*     */         for (long id : ids) {
/*     */           if (id == guild) {
/*     */             return true;
/*     */           }
/*     */         } 
/*     */         return false;
/*     */       };
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
/*     */   static ChunkingFilter exclude(@Nonnull long... ids) {
/*  99 */     Checks.notNull(ids, "ID array");
/* 100 */     if (ids.length == 0)
/* 101 */       return ALL; 
/* 102 */     return guild -> {
/*     */         for (long id : ids) {
/*     */           if (id == guild)
/*     */             return false; 
/*     */         } 
/*     */         return true;
/*     */       };
/*     */   }
/*     */   
/*     */   boolean filter(long paramLong);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\ChunkingFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */