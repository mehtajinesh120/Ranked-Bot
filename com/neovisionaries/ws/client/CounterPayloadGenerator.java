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
/*    */ class CounterPayloadGenerator
/*    */   implements PayloadGenerator
/*    */ {
/*    */   private long mCount;
/*    */   
/*    */   public byte[] generate() {
/* 27 */     return Misc.getBytesUTF8(String.valueOf(increment()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private long increment() {
/* 34 */     this.mCount = Math.max(this.mCount + 1L, 1L);
/*    */     
/* 36 */     return this.mCount;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\CounterPayloadGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */