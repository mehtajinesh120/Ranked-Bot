/*    */ package net.dv8tion.jda.api.interactions.components;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ButtonStyle
/*    */ {
/* 32 */   UNKNOWN(-1),
/*    */   
/* 34 */   PRIMARY(1),
/*    */   
/* 36 */   SECONDARY(2),
/*    */   
/* 38 */   SUCCESS(3),
/*    */   
/* 40 */   DANGER(4),
/*    */   
/* 42 */   LINK(5);
/*    */ 
/*    */   
/*    */   private final int key;
/*    */ 
/*    */   
/*    */   ButtonStyle(int key) {
/* 49 */     this.key = key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getKey() {
/* 59 */     return this.key;
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
/*    */   public static ButtonStyle fromKey(int key) {
/* 73 */     for (ButtonStyle style : values()) {
/*    */       
/* 75 */       if (style.key == key)
/* 76 */         return style; 
/*    */     } 
/* 78 */     return UNKNOWN;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\components\ButtonStyle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */