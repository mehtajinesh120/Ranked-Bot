/*    */ package com.neovisionaries.ws.client;
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
/*    */ public class WebSocketException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final WebSocketError mError;
/*    */   
/*    */   public WebSocketException(WebSocketError error) {
/* 30 */     this.mError = error;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public WebSocketException(WebSocketError error, String message) {
/* 36 */     super(message);
/*    */     
/* 38 */     this.mError = error;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public WebSocketException(WebSocketError error, Throwable cause) {
/* 44 */     super(cause);
/*    */     
/* 46 */     this.mError = error;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public WebSocketException(WebSocketError error, String message, Throwable cause) {
/* 52 */     super(message, cause);
/*    */     
/* 54 */     this.mError = error;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public WebSocketError getError() {
/* 60 */     return this.mError;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\WebSocketException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */