/*     */ package okhttp3.internal.connection;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.UnknownServiceException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import okhttp3.ConnectionSpec;
/*     */ import okhttp3.internal.Internal;
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
/*     */ public final class ConnectionSpecSelector
/*     */ {
/*     */   private final List<ConnectionSpec> connectionSpecs;
/*     */   private int nextModeIndex;
/*     */   private boolean isFallbackPossible;
/*     */   private boolean isFallback;
/*     */   
/*     */   public ConnectionSpecSelector(List<ConnectionSpec> connectionSpecs) {
/*  45 */     this.nextModeIndex = 0;
/*  46 */     this.connectionSpecs = connectionSpecs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnectionSpec configureSecureSocket(SSLSocket sslSocket) throws IOException {
/*  56 */     ConnectionSpec tlsConfiguration = null;
/*  57 */     for (int i = this.nextModeIndex, size = this.connectionSpecs.size(); i < size; i++) {
/*  58 */       ConnectionSpec connectionSpec = this.connectionSpecs.get(i);
/*  59 */       if (connectionSpec.isCompatible(sslSocket)) {
/*  60 */         tlsConfiguration = connectionSpec;
/*  61 */         this.nextModeIndex = i + 1;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  66 */     if (tlsConfiguration == null)
/*     */     {
/*     */ 
/*     */       
/*  70 */       throw new UnknownServiceException("Unable to find acceptable protocols. isFallback=" + this.isFallback + ", modes=" + this.connectionSpecs + ", supported protocols=" + 
/*     */ 
/*     */           
/*  73 */           Arrays.toString(sslSocket.getEnabledProtocols()));
/*     */     }
/*     */     
/*  76 */     this.isFallbackPossible = isFallbackPossible(sslSocket);
/*     */     
/*  78 */     Internal.instance.apply(tlsConfiguration, sslSocket, this.isFallback);
/*     */     
/*  80 */     return tlsConfiguration;
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
/*     */   public boolean connectionFailed(IOException e) {
/*  92 */     this.isFallback = true;
/*     */     
/*  94 */     if (!this.isFallbackPossible) {
/*  95 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  99 */     if (e instanceof java.net.ProtocolException) {
/* 100 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     if (e instanceof java.io.InterruptedIOException) {
/* 107 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 112 */     if (e instanceof javax.net.ssl.SSLHandshakeException)
/*     */     {
/* 114 */       if (e.getCause() instanceof java.security.cert.CertificateException) {
/* 115 */         return false;
/*     */       }
/*     */     }
/* 118 */     if (e instanceof javax.net.ssl.SSLPeerUnverifiedException)
/*     */     {
/* 120 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 124 */     return e instanceof javax.net.ssl.SSLException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isFallbackPossible(SSLSocket socket) {
/* 133 */     for (int i = this.nextModeIndex; i < this.connectionSpecs.size(); i++) {
/* 134 */       if (((ConnectionSpec)this.connectionSpecs.get(i)).isCompatible(socket)) {
/* 135 */         return true;
/*     */       }
/*     */     } 
/* 138 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\connection\ConnectionSpecSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */