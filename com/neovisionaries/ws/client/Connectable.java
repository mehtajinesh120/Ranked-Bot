/*    */ package com.neovisionaries.ws.client;
/*    */ 
/*    */ import java.util.concurrent.Callable;
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
/*    */ class Connectable
/*    */   implements Callable<WebSocket>
/*    */ {
/*    */   private final WebSocket mWebSocket;
/*    */   
/*    */   public Connectable(WebSocket ws) {
/* 35 */     this.mWebSocket = ws;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WebSocket call() throws WebSocketException {
/* 42 */     return this.mWebSocket.connect();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\Connectable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */