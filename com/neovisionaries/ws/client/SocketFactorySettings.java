/*    */ package com.neovisionaries.ws.client;
/*    */ 
/*    */ import javax.net.SocketFactory;
/*    */ import javax.net.ssl.SSLContext;
/*    */ import javax.net.ssl.SSLSocketFactory;
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
/*    */ class SocketFactorySettings
/*    */ {
/*    */   private SocketFactory mSocketFactory;
/*    */   private SSLSocketFactory mSSLSocketFactory;
/*    */   private SSLContext mSSLContext;
/*    */   
/*    */   public SocketFactorySettings() {}
/*    */   
/*    */   public SocketFactorySettings(SocketFactorySettings settings) {
/* 35 */     this.mSocketFactory = settings.mSocketFactory;
/* 36 */     this.mSSLSocketFactory = settings.mSSLSocketFactory;
/* 37 */     this.mSSLContext = settings.mSSLContext;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SocketFactory getSocketFactory() {
/* 43 */     return this.mSocketFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSocketFactory(SocketFactory factory) {
/* 49 */     this.mSocketFactory = factory;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SSLSocketFactory getSSLSocketFactory() {
/* 55 */     return this.mSSLSocketFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSSLSocketFactory(SSLSocketFactory factory) {
/* 61 */     this.mSSLSocketFactory = factory;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SSLContext getSSLContext() {
/* 67 */     return this.mSSLContext;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSSLContext(SSLContext context) {
/* 73 */     this.mSSLContext = context;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SocketFactory selectSocketFactory(boolean secure) {
/* 79 */     if (secure) {
/*    */       
/* 81 */       if (this.mSSLContext != null)
/*    */       {
/* 83 */         return this.mSSLContext.getSocketFactory();
/*    */       }
/*    */       
/* 86 */       if (this.mSSLSocketFactory != null)
/*    */       {
/* 88 */         return this.mSSLSocketFactory;
/*    */       }
/*    */       
/* 91 */       return SSLSocketFactory.getDefault();
/*    */     } 
/*    */     
/* 94 */     if (this.mSocketFactory != null)
/*    */     {
/* 96 */       return this.mSocketFactory;
/*    */     }
/*    */     
/* 99 */     return SocketFactory.getDefault();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\SocketFactorySettings.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */