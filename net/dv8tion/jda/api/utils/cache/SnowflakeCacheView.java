/*    */ package net.dv8tion.jda.api.utils.cache;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.utils.MiscUtil;
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
/*    */ public interface SnowflakeCacheView<T extends net.dv8tion.jda.api.entities.ISnowflake>
/*    */   extends CacheView<T>
/*    */ {
/*    */   @Nullable
/*    */   T getElementById(long paramLong);
/*    */   
/*    */   @Nullable
/*    */   default T getElementById(@Nonnull String id) {
/* 59 */     return getElementById(MiscUtil.parseSnowflake(id));
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\cache\SnowflakeCacheView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */