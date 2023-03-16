/*    */ package net.dv8tion.jda.internal.utils.config.sharding;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.requests.GatewayIntent;
/*    */ import net.dv8tion.jda.api.utils.MemberCachePolicy;
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
/*    */ public class ShardingConfig
/*    */ {
/*    */   private int shardsTotal;
/*    */   private int intents;
/*    */   private MemberCachePolicy memberCachePolicy;
/*    */   private final boolean useShutdownNow;
/*    */   
/*    */   public ShardingConfig(int shardsTotal, boolean useShutdownNow, int intents, MemberCachePolicy memberCachePolicy) {
/* 33 */     this.shardsTotal = shardsTotal;
/* 34 */     this.useShutdownNow = useShutdownNow;
/* 35 */     this.intents = intents;
/* 36 */     this.memberCachePolicy = memberCachePolicy;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setShardsTotal(int shardsTotal) {
/* 41 */     this.shardsTotal = shardsTotal;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getShardsTotal() {
/* 46 */     return this.shardsTotal;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getIntents() {
/* 51 */     return this.intents;
/*    */   }
/*    */ 
/*    */   
/*    */   public MemberCachePolicy getMemberCachePolicy() {
/* 56 */     return this.memberCachePolicy;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isUseShutdownNow() {
/* 61 */     return this.useShutdownNow;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public static ShardingConfig getDefault() {
/* 67 */     return new ShardingConfig(1, false, GatewayIntent.ALL_INTENTS, MemberCachePolicy.ALL);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\config\sharding\ShardingConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */