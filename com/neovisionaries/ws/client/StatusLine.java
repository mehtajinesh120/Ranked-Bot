/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StatusLine
/*     */ {
/*     */   private final String mHttpVersion;
/*     */   private final int mStatusCode;
/*     */   private final String mReasonPhrase;
/*     */   private final String mString;
/*     */   
/*     */   StatusLine(String line) {
/*  70 */     String[] elements = line.split(" +", 3);
/*     */     
/*  72 */     if (elements.length < 2)
/*     */     {
/*  74 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/*  77 */     this.mHttpVersion = elements[0];
/*  78 */     this.mStatusCode = Integer.parseInt(elements[1]);
/*  79 */     this.mReasonPhrase = (elements.length == 3) ? elements[2] : null;
/*  80 */     this.mString = line;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHttpVersion() {
/*  92 */     return this.mHttpVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStatusCode() {
/* 104 */     return this.mStatusCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReasonPhrase() {
/* 116 */     return this.mReasonPhrase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 131 */     return this.mString;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\StatusLine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */