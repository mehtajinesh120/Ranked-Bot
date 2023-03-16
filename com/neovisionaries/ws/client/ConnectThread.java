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
/*    */ class ConnectThread
/*    */   extends WebSocketThread
/*    */ {
/*    */   public ConnectThread(WebSocket ws) {
/* 23 */     super("ConnectThread", ws, ThreadType.CONNECT_THREAD);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void runMain() {
/*    */     try {
/* 32 */       this.mWebSocket.connect();
/*    */     }
/* 34 */     catch (WebSocketException e) {
/*    */       
/* 36 */       handleError(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void handleError(WebSocketException cause) {
/* 43 */     ListenerManager manager = this.mWebSocket.getListenerManager();
/*    */     
/* 45 */     manager.callOnError(cause);
/* 46 */     manager.callOnConnectError(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\ConnectThread.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */