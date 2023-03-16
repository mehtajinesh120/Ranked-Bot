/*    */ package net.dv8tion.jda.api;
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
/*    */ public enum OnlineStatus
/*    */ {
/* 26 */   ONLINE("online"),
/*    */ 
/*    */ 
/*    */   
/* 30 */   IDLE("idle"),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   DO_NOT_DISTURB("dnd"),
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   INVISIBLE("invisible"),
/*    */ 
/*    */ 
/*    */   
/* 46 */   OFFLINE("offline"),
/*    */ 
/*    */ 
/*    */   
/* 50 */   UNKNOWN("");
/*    */   
/*    */   private final String key;
/*    */ 
/*    */   
/*    */   OnlineStatus(String key) {
/* 56 */     this.key = key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 68 */     return this.key;
/*    */   }
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
/*    */   public static OnlineStatus fromKey(String key) {
/* 82 */     for (OnlineStatus onlineStatus : values()) {
/*    */       
/* 84 */       if (onlineStatus.key.equalsIgnoreCase(key))
/*    */       {
/* 86 */         return onlineStatus;
/*    */       }
/*    */     } 
/* 89 */     return UNKNOWN;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\OnlineStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */