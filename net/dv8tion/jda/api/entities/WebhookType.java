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
/*    */ public enum WebhookType
/*    */ {
/* 24 */   UNKNOWN(-1),
/*    */   
/* 26 */   INCOMING(1),
/*    */   
/* 28 */   FOLLOWER(2);
/*    */   
/*    */   private final int key;
/*    */ 
/*    */   
/*    */   WebhookType(int key) {
/* 34 */     this.key = key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getKey() {
/* 44 */     return this.key;
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
/*    */   public static WebhookType fromKey(int key) {
/* 58 */     for (WebhookType type : values()) {
/*    */       
/* 60 */       if (type.key == key)
/* 61 */         return type; 
/*    */     } 
/* 63 */     return UNKNOWN;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\WebhookType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */