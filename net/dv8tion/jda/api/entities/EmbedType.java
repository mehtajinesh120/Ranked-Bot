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
/*    */ public enum EmbedType
/*    */ {
/* 27 */   IMAGE("image"),
/* 28 */   VIDEO("video"),
/* 29 */   LINK("link"),
/* 30 */   RICH("rich"),
/* 31 */   UNKNOWN("");
/*    */   
/*    */   private final String key;
/*    */   
/*    */   EmbedType(String key) {
/* 36 */     this.key = key;
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
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public static EmbedType fromKey(String key) {
/* 53 */     for (EmbedType type : values()) {
/*    */       
/* 55 */       if (type.key.equals(key))
/* 56 */         return type; 
/*    */     } 
/* 58 */     return UNKNOWN;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\EmbedType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */