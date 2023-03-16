/*    */ package net.dv8tion.jda.api.utils.cache;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.JDA;
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
/*    */ 
/*    */ 
/*    */ public interface ShardCacheView
/*    */   extends CacheView<JDA>
/*    */ {
/*    */   @Nullable
/*    */   JDA getElementById(int paramInt);
/*    */   
/*    */   @Nullable
/*    */   default JDA getElementById(@Nonnull String id) {
/* 59 */     return getElementById(Integer.parseUnsignedInt(id));
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\cache\ShardCacheView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */