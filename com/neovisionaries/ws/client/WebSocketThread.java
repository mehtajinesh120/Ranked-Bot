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
/*    */ abstract class WebSocketThread
/*    */   extends Thread
/*    */ {
/*    */   protected final WebSocket mWebSocket;
/*    */   private final ThreadType mThreadType;
/*    */   
/*    */   WebSocketThread(String name, WebSocket ws, ThreadType type) {
/* 27 */     super(name);
/*    */     
/* 29 */     this.mWebSocket = ws;
/* 30 */     this.mThreadType = type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 37 */     ListenerManager lm = this.mWebSocket.getListenerManager();
/*    */     
/* 39 */     if (lm != null)
/*    */     {
/*    */       
/* 42 */       lm.callOnThreadStarted(this.mThreadType, this);
/*    */     }
/*    */     
/* 45 */     runMain();
/*    */     
/* 47 */     if (lm != null)
/*    */     {
/*    */       
/* 50 */       lm.callOnThreadStopping(this.mThreadType, this);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void callOnThreadCreated() {
/* 57 */     ListenerManager lm = this.mWebSocket.getListenerManager();
/*    */     
/* 59 */     if (lm != null)
/*    */     {
/* 61 */       lm.callOnThreadCreated(this.mThreadType, this);
/*    */     }
/*    */   }
/*    */   
/*    */   protected abstract void runMain();
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\WebSocketThread.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */