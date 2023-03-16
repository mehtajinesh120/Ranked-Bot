/*    */ package net.dv8tion.jda.api.sharding;
/*    */ 
/*    */ import javax.annotation.Nullable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ThreadPoolProvider<T extends java.util.concurrent.ExecutorService>
/*    */ {
/*    */   @Nullable
/*    */   T provide(int paramInt);
/*    */   
/*    */   default boolean shouldShutdownAutomatically(int shardId) {
/* 53 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\sharding\ThreadPoolProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */