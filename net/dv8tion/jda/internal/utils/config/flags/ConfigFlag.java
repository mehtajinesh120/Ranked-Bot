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
/*    */ public enum ConfigFlag
/*    */ {
/* 23 */   RAW_EVENTS,
/* 24 */   USE_RELATIVE_RATELIMIT(true),
/* 25 */   RETRY_TIMEOUT(true),
/* 26 */   BULK_DELETE_SPLIT(true),
/* 27 */   SHUTDOWN_HOOK(true),
/* 28 */   MDC_CONTEXT(true),
/* 29 */   AUTO_RECONNECT(true);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final boolean isDefault;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   ConfigFlag(boolean isDefault) {
/* 40 */     this.isDefault = isDefault;
/*    */   }
/*    */ 
/*    */   
/*    */   public static EnumSet<ConfigFlag> getDefault() {
/* 45 */     EnumSet<ConfigFlag> set = EnumSet.noneOf(ConfigFlag.class);
/* 46 */     for (ConfigFlag flag : values()) {
/*    */       
/* 48 */       if (flag.isDefault)
/* 49 */         set.add(flag); 
/*    */     } 
/* 51 */     return set;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\config\flags\ConfigFlag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */