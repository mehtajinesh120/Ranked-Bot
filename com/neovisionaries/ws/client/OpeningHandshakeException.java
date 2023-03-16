/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class OpeningHandshakeException
/*     */   extends WebSocketException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final StatusLine mStatusLine;
/*     */   private final Map<String, List<String>> mHeaders;
/*     */   private final byte[] mBody;
/*     */   
/*     */   OpeningHandshakeException(WebSocketError error, String message, StatusLine statusLine, Map<String, List<String>> headers) {
/*  43 */     this(error, message, statusLine, headers, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   OpeningHandshakeException(WebSocketError error, String message, StatusLine statusLine, Map<String, List<String>> headers, byte[] body) {
/*  51 */     super(error, message);
/*     */     
/*  53 */     this.mStatusLine = statusLine;
/*  54 */     this.mHeaders = headers;
/*  55 */     this.mBody = body;
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
/*     */   public StatusLine getStatusLine() {
/*  68 */     return this.mStatusLine;
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
/*     */   public Map<String, List<String>> getHeaders() {
/*  83 */     return this.mHeaders;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBody() {
/* 104 */     return this.mBody;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\OpeningHandshakeException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */