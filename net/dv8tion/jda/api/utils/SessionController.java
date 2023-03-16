/*     */ package net.dv8tion.jda.api.utils;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.annotations.DeprecatedSince;
/*     */ import net.dv8tion.jda.annotations.ForRemoval;
/*     */ import net.dv8tion.jda.annotations.ReplaceWith;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.internal.utils.tuple.Pair;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface SessionController
/*     */ {
/*     */   public static final int IDENTIFY_DELAY = 5;
/*     */   
/*     */   default void setConcurrency(int level) {}
/*     */   
/*     */   void appendSession(@Nonnull SessionConnectNode paramSessionConnectNode);
/*     */   
/*     */   void removeSession(@Nonnull SessionConnectNode paramSessionConnectNode);
/*     */   
/*     */   long getGlobalRatelimit();
/*     */   
/*     */   void setGlobalRatelimit(long paramLong);
/*     */   
/*     */   @Nonnull
/*     */   String getGateway(@Nonnull JDA paramJDA);
/*     */   
/*     */   @Nonnull
/*     */   @Deprecated
/*     */   @ForRemoval(deadline = "4.4.0")
/*     */   @DeprecatedSince("4.0.0")
/*     */   @ReplaceWith("getShardedGateway(api)")
/*     */   Pair<String, Integer> getGatewayBot(@Nonnull JDA paramJDA);
/*     */   
/*     */   @Nonnull
/*     */   default ShardedGateway getShardedGateway(@Nonnull JDA api) {
/* 196 */     Pair<String, Integer> tuple = getGatewayBot(api);
/* 197 */     return new ShardedGateway((String)tuple.getLeft(), ((Integer)tuple.getRight()).intValue());
/*     */   }
/*     */   
/*     */   public static interface SessionConnectNode
/*     */   {
/*     */     boolean isReconnect();
/*     */     
/*     */     @Nonnull
/*     */     JDA getJDA();
/*     */     
/*     */     @Nonnull
/*     */     JDA.ShardInfo getShardInfo();
/*     */     
/*     */     void run(boolean param1Boolean) throws InterruptedException;
/*     */   }
/*     */   
/*     */   public static class ShardedGateway {
/*     */     private final String url;
/*     */     private final int shardTotal;
/*     */     private final int concurrency;
/*     */     
/*     */     public ShardedGateway(String url, int shardTotal) {
/* 219 */       this(url, shardTotal, 1);
/*     */     }
/*     */ 
/*     */     
/*     */     public ShardedGateway(String url, int shardTotal, int concurrency) {
/* 224 */       this.url = url;
/* 225 */       this.shardTotal = shardTotal;
/* 226 */       this.concurrency = concurrency;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getUrl() {
/* 236 */       return this.url;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getShardTotal() {
/* 246 */       return this.shardTotal;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getConcurrency() {
/* 260 */       return this.concurrency;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\SessionController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */