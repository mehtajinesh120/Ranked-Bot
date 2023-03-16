/*    */ package net.dv8tion.jda.api.utils;
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
/*    */ public enum Compression
/*    */ {
/* 28 */   NONE(""),
/*    */   
/* 30 */   ZLIB("zlib-stream");
/*    */   
/*    */   private final String key;
/*    */ 
/*    */   
/*    */   Compression(String key) {
/* 36 */     this.key = key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 46 */     return this.key;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\Compression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */