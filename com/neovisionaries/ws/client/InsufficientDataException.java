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
/*    */ class InsufficientDataException
/*    */   extends WebSocketException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final int mRequestedByteCount;
/*    */   private final int mReadByteCount;
/*    */   
/*    */   public InsufficientDataException(int requestedByteCount, int readByteCount) {
/* 31 */     super(WebSocketError.INSUFFICENT_DATA, "The end of the stream has been reached unexpectedly.");
/*    */     
/* 33 */     this.mRequestedByteCount = requestedByteCount;
/* 34 */     this.mReadByteCount = readByteCount;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRequestedByteCount() {
/* 40 */     return this.mRequestedByteCount;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getReadByteCount() {
/* 46 */     return this.mReadByteCount;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\InsufficientDataException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */