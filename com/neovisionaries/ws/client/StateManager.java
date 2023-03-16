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
/*    */ class StateManager
/*    */ {
/*    */   private WebSocketState mState;
/*    */   
/*    */   enum CloseInitiator
/*    */   {
/* 27 */     NONE,
/* 28 */     SERVER,
/* 29 */     CLIENT;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 34 */   private CloseInitiator mCloseInitiator = CloseInitiator.NONE;
/*    */ 
/*    */ 
/*    */   
/*    */   public StateManager() {
/* 39 */     this.mState = WebSocketState.CREATED;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public WebSocketState getState() {
/* 45 */     return this.mState;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setState(WebSocketState state) {
/* 51 */     this.mState = state;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void changeToClosing(CloseInitiator closeInitiator) {
/* 57 */     this.mState = WebSocketState.CLOSING;
/*    */ 
/*    */     
/* 60 */     if (this.mCloseInitiator == CloseInitiator.NONE)
/*    */     {
/* 62 */       this.mCloseInitiator = closeInitiator;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean getClosedByServer() {
/* 69 */     return (this.mCloseInitiator == CloseInitiator.SERVER);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\StateManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */