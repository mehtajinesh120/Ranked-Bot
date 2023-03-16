/*    */ package net.dv8tion.jda.internal.utils.config.flags;
/*    */ 
/*    */ import java.util.EnumSet;
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
/*    */ public enum ShardingConfigFlag
/*    */ {
/* 23 */   SHUTDOWN_NOW;
/*    */ 
/*    */   
/*    */   public static EnumSet<ShardingConfigFlag> getDefault() {
/* 27 */     return EnumSet.noneOf(ShardingConfigFlag.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\config\flags\ShardingConfigFlag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */