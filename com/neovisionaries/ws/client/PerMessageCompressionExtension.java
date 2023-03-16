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
/*    */ 
/*    */ abstract class PerMessageCompressionExtension
/*    */   extends WebSocketExtension
/*    */ {
/*    */   public PerMessageCompressionExtension(String name) {
/* 28 */     super(name);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public PerMessageCompressionExtension(WebSocketExtension source) {
/* 34 */     super(source);
/*    */   }
/*    */   
/*    */   protected abstract byte[] decompress(byte[] paramArrayOfbyte) throws WebSocketException;
/*    */   
/*    */   protected abstract byte[] compress(byte[] paramArrayOfbyte) throws WebSocketException;
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\PerMessageCompressionExtension.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */