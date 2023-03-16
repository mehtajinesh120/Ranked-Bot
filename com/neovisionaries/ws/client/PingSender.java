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
/*    */ class PingSender
/*    */   extends PeriodicalFrameSender
/*    */ {
/*    */   private static final String TIMER_NAME = "PingSender";
/*    */   
/*    */   public PingSender(WebSocket webSocket, PayloadGenerator generator) {
/* 26 */     super(webSocket, "PingSender", generator);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected WebSocketFrame createFrame(byte[] payload) {
/* 33 */     return WebSocketFrame.createPingFrame(payload);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\PingSender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */