/*    */ package org.jsoup;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class HttpStatusException
/*    */   extends IOException
/*    */ {
/*    */   private final int statusCode;
/*    */   private final String url;
/*    */   
/*    */   public HttpStatusException(String message, int statusCode, String url) {
/* 13 */     super(message + ". Status=" + statusCode + ", URL=[" + url + "]");
/* 14 */     this.statusCode = statusCode;
/* 15 */     this.url = url;
/*    */   }
/*    */   
/*    */   public int getStatusCode() {
/* 19 */     return this.statusCode;
/*    */   }
/*    */   
/*    */   public String getUrl() {
/* 23 */     return this.url;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\HttpStatusException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */