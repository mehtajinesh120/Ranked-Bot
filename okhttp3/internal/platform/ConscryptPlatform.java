/*     */ package okhttp3.internal.platform;
/*     */ 
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.Provider;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import okhttp3.Protocol;
/*     */ import org.conscrypt.Conscrypt;
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
/*     */ public class ConscryptPlatform
/*     */   extends Platform
/*     */ {
/*     */   private Provider getProvider() {
/*  39 */     return Conscrypt.newProviderBuilder().provideTrustManager().build();
/*     */   }
/*     */   @Nullable
/*     */   public X509TrustManager trustManager(SSLSocketFactory sslSocketFactory) {
/*  43 */     if (!Conscrypt.isConscrypt(sslSocketFactory)) {
/*  44 */       return super.trustManager(sslSocketFactory);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  50 */       Object sp = readFieldOrNull(sslSocketFactory, Object.class, "sslParameters");
/*     */       
/*  52 */       if (sp != null) {
/*  53 */         return readFieldOrNull(sp, X509TrustManager.class, "x509TrustManager");
/*     */       }
/*     */       
/*  56 */       return null;
/*  57 */     } catch (Exception e) {
/*  58 */       throw new UnsupportedOperationException("clientBuilder.sslSocketFactory(SSLSocketFactory) not supported on Conscrypt", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void configureTlsExtensions(SSLSocket sslSocket, String hostname, List<Protocol> protocols) {
/*  65 */     if (Conscrypt.isConscrypt(sslSocket)) {
/*     */       
/*  67 */       if (hostname != null) {
/*  68 */         Conscrypt.setUseSessionTickets(sslSocket, true);
/*  69 */         Conscrypt.setHostname(sslSocket, hostname);
/*     */       } 
/*     */ 
/*     */       
/*  73 */       List<String> names = Platform.alpnProtocolNames(protocols);
/*  74 */       Conscrypt.setApplicationProtocols(sslSocket, names.<String>toArray(new String[0]));
/*     */     } else {
/*  76 */       super.configureTlsExtensions(sslSocket, hostname, protocols);
/*     */     } 
/*     */   }
/*     */   @Nullable
/*     */   public String getSelectedProtocol(SSLSocket sslSocket) {
/*  81 */     if (Conscrypt.isConscrypt(sslSocket)) {
/*  82 */       return Conscrypt.getApplicationProtocol(sslSocket);
/*     */     }
/*  84 */     return super.getSelectedProtocol(sslSocket);
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLContext getSSLContext() {
/*     */     try {
/*  90 */       return SSLContext.getInstance("TLSv1.3", getProvider());
/*  91 */     } catch (NoSuchAlgorithmException e) {
/*     */       
/*     */       try {
/*  94 */         return SSLContext.getInstance("TLS", getProvider());
/*  95 */       } catch (NoSuchAlgorithmException e2) {
/*  96 */         throw new IllegalStateException("No TLS provider", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static ConscryptPlatform buildIfSupported() {
/*     */     try {
/* 104 */       Class.forName("org.conscrypt.Conscrypt");
/*     */       
/* 106 */       if (!Conscrypt.isAvailable()) {
/* 107 */         return null;
/*     */       }
/*     */       
/* 110 */       return new ConscryptPlatform();
/* 111 */     } catch (ClassNotFoundException e) {
/* 112 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void configureSslSocketFactory(SSLSocketFactory socketFactory) {
/* 118 */     if (Conscrypt.isConscrypt(socketFactory))
/* 119 */       Conscrypt.setUseEngineSocket(socketFactory, true); 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\platform\ConscryptPlatform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */