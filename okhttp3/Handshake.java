/*     */ package okhttp3;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.Principal;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.net.ssl.SSLPeerUnverifiedException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import okhttp3.internal.Util;
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
/*     */ public final class Handshake
/*     */ {
/*     */   private final TlsVersion tlsVersion;
/*     */   private final CipherSuite cipherSuite;
/*     */   private final List<Certificate> peerCertificates;
/*     */   private final List<Certificate> localCertificates;
/*     */   
/*     */   private Handshake(TlsVersion tlsVersion, CipherSuite cipherSuite, List<Certificate> peerCertificates, List<Certificate> localCertificates) {
/*  45 */     this.tlsVersion = tlsVersion;
/*  46 */     this.cipherSuite = cipherSuite;
/*  47 */     this.peerCertificates = peerCertificates;
/*  48 */     this.localCertificates = localCertificates;
/*     */   }
/*     */   public static Handshake get(SSLSession session) throws IOException {
/*     */     Certificate[] peerCertificates;
/*  52 */     String cipherSuiteString = session.getCipherSuite();
/*  53 */     if (cipherSuiteString == null) throw new IllegalStateException("cipherSuite == null"); 
/*  54 */     if ("SSL_NULL_WITH_NULL_NULL".equals(cipherSuiteString)) {
/*  55 */       throw new IOException("cipherSuite == SSL_NULL_WITH_NULL_NULL");
/*     */     }
/*  57 */     CipherSuite cipherSuite = CipherSuite.forJavaName(cipherSuiteString);
/*     */     
/*  59 */     String tlsVersionString = session.getProtocol();
/*  60 */     if (tlsVersionString == null) throw new IllegalStateException("tlsVersion == null"); 
/*  61 */     if ("NONE".equals(tlsVersionString)) throw new IOException("tlsVersion == NONE"); 
/*  62 */     TlsVersion tlsVersion = TlsVersion.forJavaName(tlsVersionString);
/*     */ 
/*     */     
/*     */     try {
/*  66 */       peerCertificates = session.getPeerCertificates();
/*  67 */     } catch (SSLPeerUnverifiedException ignored) {
/*  68 */       peerCertificates = null;
/*     */     } 
/*     */ 
/*     */     
/*  72 */     List<Certificate> peerCertificatesList = (peerCertificates != null) ? Util.immutableList((Object[])peerCertificates) : Collections.<Certificate>emptyList();
/*     */     
/*  74 */     Certificate[] localCertificates = session.getLocalCertificates();
/*     */ 
/*     */     
/*  77 */     List<Certificate> localCertificatesList = (localCertificates != null) ? Util.immutableList((Object[])localCertificates) : Collections.<Certificate>emptyList();
/*     */     
/*  79 */     return new Handshake(tlsVersion, cipherSuite, peerCertificatesList, localCertificatesList);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Handshake get(TlsVersion tlsVersion, CipherSuite cipherSuite, List<Certificate> peerCertificates, List<Certificate> localCertificates) {
/*  84 */     if (tlsVersion == null) throw new NullPointerException("tlsVersion == null"); 
/*  85 */     if (cipherSuite == null) throw new NullPointerException("cipherSuite == null"); 
/*  86 */     return new Handshake(tlsVersion, cipherSuite, Util.immutableList(peerCertificates), 
/*  87 */         Util.immutableList(localCertificates));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TlsVersion tlsVersion() {
/*  95 */     return this.tlsVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public CipherSuite cipherSuite() {
/* 100 */     return this.cipherSuite;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Certificate> peerCertificates() {
/* 105 */     return this.peerCertificates;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Principal peerPrincipal() {
/* 110 */     return !this.peerCertificates.isEmpty() ? (
/* 111 */       (X509Certificate)this.peerCertificates.get(0)).getSubjectX500Principal() : 
/* 112 */       null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Certificate> localCertificates() {
/* 117 */     return this.localCertificates;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Principal localPrincipal() {
/* 122 */     return !this.localCertificates.isEmpty() ? (
/* 123 */       (X509Certificate)this.localCertificates.get(0)).getSubjectX500Principal() : 
/* 124 */       null;
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 128 */     if (!(other instanceof Handshake)) return false; 
/* 129 */     Handshake that = (Handshake)other;
/* 130 */     return (this.tlsVersion.equals(that.tlsVersion) && this.cipherSuite
/* 131 */       .equals(that.cipherSuite) && this.peerCertificates
/* 132 */       .equals(that.peerCertificates) && this.localCertificates
/* 133 */       .equals(that.localCertificates));
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 137 */     int result = 17;
/* 138 */     result = 31 * result + this.tlsVersion.hashCode();
/* 139 */     result = 31 * result + this.cipherSuite.hashCode();
/* 140 */     result = 31 * result + this.peerCertificates.hashCode();
/* 141 */     result = 31 * result + this.localCertificates.hashCode();
/* 142 */     return result;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 146 */     return "Handshake{tlsVersion=" + this.tlsVersion + " cipherSuite=" + this.cipherSuite + " peerCertificates=" + 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 152 */       names(this.peerCertificates) + " localCertificates=" + 
/*     */       
/* 154 */       names(this.localCertificates) + '}';
/*     */   }
/*     */ 
/*     */   
/*     */   private List<String> names(List<Certificate> certificates) {
/* 159 */     ArrayList<String> strings = new ArrayList<>();
/*     */     
/* 161 */     for (Certificate cert : certificates) {
/* 162 */       if (cert instanceof X509Certificate) {
/* 163 */         strings.add(String.valueOf(((X509Certificate)cert).getSubjectDN())); continue;
/*     */       } 
/* 165 */       strings.add(cert.getType());
/*     */     } 
/*     */ 
/*     */     
/* 169 */     return strings;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\Handshake.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */