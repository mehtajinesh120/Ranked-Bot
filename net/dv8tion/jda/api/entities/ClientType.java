/*    */ package net.dv8tion.jda.api.entities;
/*    */ 
/*    */ import javax.annotation.Nonnull;
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
/*    */ public enum ClientType
/*    */ {
/* 29 */   DESKTOP("desktop"),
/*    */   
/* 31 */   MOBILE("mobile"),
/*    */   
/* 33 */   WEB("web"),
/*    */   
/* 35 */   UNKNOWN("unknown");
/*    */ 
/*    */   
/*    */   private final String key;
/*    */ 
/*    */   
/*    */   ClientType(String key) {
/* 42 */     this.key = key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 52 */     return this.key;
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
/*    */   @Nonnull
/*    */   public static ClientType fromKey(@Nonnull String key) {
/* 66 */     for (ClientType type : values()) {
/*    */       
/* 68 */       if (type.key.equals(key))
/* 69 */         return type; 
/*    */     } 
/* 71 */     return UNKNOWN;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\ClientType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */