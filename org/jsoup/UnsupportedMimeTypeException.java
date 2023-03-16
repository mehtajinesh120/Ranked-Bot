/*    */ package org.jsoup;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class UnsupportedMimeTypeException
/*    */   extends IOException
/*    */ {
/*    */   private final String mimeType;
/*    */   private final String url;
/*    */   
/*    */   public UnsupportedMimeTypeException(String message, String mimeType, String url) {
/* 13 */     super(message);
/* 14 */     this.mimeType = mimeType;
/* 15 */     this.url = url;
/*    */   }
/*    */   
/*    */   public String getMimeType() {
/* 19 */     return this.mimeType;
/*    */   }
/*    */   
/*    */   public String getUrl() {
/* 23 */     return this.url;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 28 */     return super.toString() + ". Mimetype=" + this.mimeType + ", URL=" + this.url;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\UnsupportedMimeTypeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */