/*    */ package net.dv8tion.jda.api.interactions;
/*    */ 
/*    */ import javax.annotation.CheckReturnValue;
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
/*    */ public enum InteractionType
/*    */ {
/* 29 */   UNKNOWN(-1),
/* 30 */   PING(1),
/* 31 */   SLASH_COMMAND(2),
/* 32 */   COMPONENT(3);
/*    */ 
/*    */   
/*    */   private final int key;
/*    */ 
/*    */   
/*    */   InteractionType(int key) {
/* 39 */     this.key = key;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getKey() {
/* 44 */     return this.key;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   @CheckReturnValue
/*    */   public static InteractionType fromKey(int key) {
/* 51 */     switch (key) {
/*    */       
/*    */       case 1:
/* 54 */         return PING;
/*    */       case 2:
/* 56 */         return SLASH_COMMAND;
/*    */       case 3:
/* 58 */         return COMPONENT;
/*    */     } 
/* 60 */     return UNKNOWN;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\InteractionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */