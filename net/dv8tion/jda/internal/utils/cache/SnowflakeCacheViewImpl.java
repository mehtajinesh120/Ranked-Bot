/*    */ package net.dv8tion.jda.internal.utils.cache;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.dv8tion.jda.api.entities.ISnowflake;
/*    */ import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
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
/*    */ public class SnowflakeCacheViewImpl<T extends ISnowflake>
/*    */   extends AbstractCacheView<T>
/*    */   implements SnowflakeCacheView<T>
/*    */ {
/*    */   public SnowflakeCacheViewImpl(Class<T> type, Function<T, String> nameMapper) {
/* 28 */     super(type, nameMapper);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public T getElementById(long id) {
/* 34 */     if (this.elements.isEmpty())
/* 35 */       return null; 
/* 36 */     return get(id);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\cache\SnowflakeCacheViewImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */