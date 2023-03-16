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
/*    */ class Address
/*    */ {
/*    */   private final String mHost;
/*    */   private final int mPort;
/*    */   private transient String mString;
/*    */   
/*    */   Address(String host, int port) {
/* 31 */     this.mHost = host;
/* 32 */     this.mPort = port;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   String getHostname() {
/* 38 */     return this.mHost;
/*    */   }
/*    */ 
/*    */   
/*    */   int getPort() {
/* 43 */     return this.mPort;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 50 */     if (this.mString == null)
/*    */     {
/* 52 */       this.mString = String.format("%s:%d", new Object[] { this.mHost, Integer.valueOf(this.mPort) });
/*    */     }
/*    */     
/* 55 */     return this.mString;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\Address.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */