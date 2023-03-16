/*    */ package net.dv8tion.jda.internal.audio;
/*    */ 
/*    */ import net.dv8tion.jda.api.utils.data.DataArray;
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
/*    */ public enum AudioEncryption
/*    */ {
/* 25 */   XSALSA20_POLY1305_LITE,
/* 26 */   XSALSA20_POLY1305_SUFFIX,
/* 27 */   XSALSA20_POLY1305;
/*    */   
/*    */   private final String key;
/*    */ 
/*    */   
/*    */   AudioEncryption() {
/* 33 */     this.key = name().toLowerCase();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 38 */     return this.key;
/*    */   }
/*    */ 
/*    */   
/*    */   public static AudioEncryption getPreferredMode(DataArray array) {
/* 43 */     AudioEncryption encryption = null;
/* 44 */     for (Object o : array) {
/*    */ 
/*    */       
/*    */       try {
/* 48 */         String name = String.valueOf(o).toUpperCase();
/* 49 */         AudioEncryption e = valueOf(name);
/* 50 */         if (encryption == null || e.ordinal() < encryption.ordinal()) {
/* 51 */           encryption = e;
/*    */         }
/* 53 */       } catch (IllegalArgumentException illegalArgumentException) {}
/*    */     } 
/* 55 */     return encryption;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\audio\AudioEncryption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */