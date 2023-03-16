/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import javax.net.ssl.SSLSocket;
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
/*     */ public class HostnameUnverifiedException
/*     */   extends WebSocketException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final SSLSocket mSSLSocket;
/*     */   private final String mHostname;
/*     */   
/*     */   public HostnameUnverifiedException(SSLSocket socket, String hostname) {
/*  57 */     super(WebSocketError.HOSTNAME_UNVERIFIED, 
/*  58 */         String.format("The certificate of the peer%s does not match the expected hostname (%s)", new Object[] {
/*  59 */             stringifyPrincipal(socket), hostname
/*     */           }));
/*  61 */     this.mSSLSocket = socket;
/*  62 */     this.mHostname = hostname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String stringifyPrincipal(SSLSocket socket) {
/*     */     try {
/*  70 */       return String.format(" (%s)", new Object[] { socket.getSession().getPeerPrincipal().toString() });
/*     */     }
/*  72 */     catch (Exception e) {
/*     */ 
/*     */       
/*  75 */       return "";
/*     */     } 
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
/*     */   public SSLSocket getSSLSocket() {
/*  88 */     return this.mSSLSocket;
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
/*     */   public String getHostname() {
/* 100 */     return this.mHostname;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\HostnameUnverifiedException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */