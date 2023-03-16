/*     */ package okhttp3;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.net.ssl.SSLSocket;
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
/*     */ public final class ConnectionSpec
/*     */ {
/*  51 */   private static final CipherSuite[] RESTRICTED_CIPHER_SUITES = new CipherSuite[] { CipherSuite.TLS_AES_128_GCM_SHA256, CipherSuite.TLS_AES_256_GCM_SHA384, CipherSuite.TLS_CHACHA20_POLY1305_SHA256, CipherSuite.TLS_AES_128_CCM_SHA256, CipherSuite.TLS_AES_256_CCM_8_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256 };
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
/*  70 */   private static final CipherSuite[] APPROVED_CIPHER_SUITES = new CipherSuite[] { CipherSuite.TLS_AES_128_GCM_SHA256, CipherSuite.TLS_AES_256_GCM_SHA384, CipherSuite.TLS_CHACHA20_POLY1305_SHA256, CipherSuite.TLS_AES_128_CCM_SHA256, CipherSuite.TLS_AES_256_CCM_8_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA };
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
/*  98 */   public static final ConnectionSpec RESTRICTED_TLS = (new Builder(true))
/*  99 */     .cipherSuites(RESTRICTED_CIPHER_SUITES)
/* 100 */     .tlsVersions(new TlsVersion[] { TlsVersion.TLS_1_3, TlsVersion.TLS_1_2
/* 101 */       }).supportsTlsExtensions(true)
/* 102 */     .build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public static final ConnectionSpec MODERN_TLS = (new Builder(true))
/* 109 */     .cipherSuites(APPROVED_CIPHER_SUITES)
/* 110 */     .tlsVersions(new TlsVersion[] { TlsVersion.TLS_1_3, TlsVersion.TLS_1_2
/* 111 */       }).supportsTlsExtensions(true)
/* 112 */     .build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public static final ConnectionSpec COMPATIBLE_TLS = (new Builder(true))
/* 120 */     .cipherSuites(APPROVED_CIPHER_SUITES)
/* 121 */     .tlsVersions(new TlsVersion[] { TlsVersion.TLS_1_3, TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0
/* 122 */       }).supportsTlsExtensions(true)
/* 123 */     .build();
/*     */ 
/*     */   
/* 126 */   public static final ConnectionSpec CLEARTEXT = (new Builder(false)).build(); final boolean tls;
/*     */   final boolean supportsTlsExtensions;
/*     */   @Nullable
/*     */   final String[] cipherSuites;
/*     */   @Nullable
/*     */   final String[] tlsVersions;
/*     */   
/*     */   ConnectionSpec(Builder builder) {
/* 134 */     this.tls = builder.tls;
/* 135 */     this.cipherSuites = builder.cipherSuites;
/* 136 */     this.tlsVersions = builder.tlsVersions;
/* 137 */     this.supportsTlsExtensions = builder.supportsTlsExtensions;
/*     */   }
/*     */   
/*     */   public boolean isTls() {
/* 141 */     return this.tls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<CipherSuite> cipherSuites() {
/* 149 */     return (this.cipherSuites != null) ? CipherSuite.forJavaNames(this.cipherSuites) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<TlsVersion> tlsVersions() {
/* 157 */     return (this.tlsVersions != null) ? TlsVersion.forJavaNames(this.tlsVersions) : null;
/*     */   }
/*     */   
/*     */   public boolean supportsTlsExtensions() {
/* 161 */     return this.supportsTlsExtensions;
/*     */   }
/*     */ 
/*     */   
/*     */   void apply(SSLSocket sslSocket, boolean isFallback) {
/* 166 */     ConnectionSpec specToApply = supportedSpec(sslSocket, isFallback);
/*     */     
/* 168 */     if (specToApply.tlsVersions != null) {
/* 169 */       sslSocket.setEnabledProtocols(specToApply.tlsVersions);
/*     */     }
/* 171 */     if (specToApply.cipherSuites != null) {
/* 172 */       sslSocket.setEnabledCipherSuites(specToApply.cipherSuites);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ConnectionSpec supportedSpec(SSLSocket sslSocket, boolean isFallback) {
/* 183 */     String[] cipherSuitesIntersection = (this.cipherSuites != null) ? Util.intersect(CipherSuite.ORDER_BY_NAME, sslSocket.getEnabledCipherSuites(), this.cipherSuites) : sslSocket.getEnabledCipherSuites();
/*     */ 
/*     */     
/* 186 */     String[] tlsVersionsIntersection = (this.tlsVersions != null) ? Util.intersect(Util.NATURAL_ORDER, sslSocket.getEnabledProtocols(), this.tlsVersions) : sslSocket.getEnabledProtocols();
/*     */ 
/*     */ 
/*     */     
/* 190 */     String[] supportedCipherSuites = sslSocket.getSupportedCipherSuites();
/* 191 */     int indexOfFallbackScsv = Util.indexOf(CipherSuite.ORDER_BY_NAME, supportedCipherSuites, "TLS_FALLBACK_SCSV");
/*     */     
/* 193 */     if (isFallback && indexOfFallbackScsv != -1) {
/* 194 */       cipherSuitesIntersection = Util.concat(cipherSuitesIntersection, supportedCipherSuites[indexOfFallbackScsv]);
/*     */     }
/*     */ 
/*     */     
/* 198 */     return (new Builder(this))
/* 199 */       .cipherSuites(cipherSuitesIntersection)
/* 200 */       .tlsVersions(tlsVersionsIntersection)
/* 201 */       .build();
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
/*     */   public boolean isCompatible(SSLSocket socket) {
/* 216 */     if (!this.tls) {
/* 217 */       return false;
/*     */     }
/*     */     
/* 220 */     if (this.tlsVersions != null && !Util.nonEmptyIntersection(Util.NATURAL_ORDER, this.tlsVersions, socket
/* 221 */         .getEnabledProtocols())) {
/* 222 */       return false;
/*     */     }
/*     */     
/* 225 */     if (this.cipherSuites != null && !Util.nonEmptyIntersection(CipherSuite.ORDER_BY_NAME, this.cipherSuites, socket
/* 226 */         .getEnabledCipherSuites())) {
/* 227 */       return false;
/*     */     }
/*     */     
/* 230 */     return true;
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 234 */     if (!(other instanceof ConnectionSpec)) return false; 
/* 235 */     if (other == this) return true;
/*     */     
/* 237 */     ConnectionSpec that = (ConnectionSpec)other;
/* 238 */     if (this.tls != that.tls) return false;
/*     */     
/* 240 */     if (this.tls) {
/* 241 */       if (!Arrays.equals((Object[])this.cipherSuites, (Object[])that.cipherSuites)) return false; 
/* 242 */       if (!Arrays.equals((Object[])this.tlsVersions, (Object[])that.tlsVersions)) return false; 
/* 243 */       if (this.supportsTlsExtensions != that.supportsTlsExtensions) return false;
/*     */     
/*     */     } 
/* 246 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 250 */     int result = 17;
/* 251 */     if (this.tls) {
/* 252 */       result = 31 * result + Arrays.hashCode((Object[])this.cipherSuites);
/* 253 */       result = 31 * result + Arrays.hashCode((Object[])this.tlsVersions);
/* 254 */       result = 31 * result + (this.supportsTlsExtensions ? 0 : 1);
/*     */     } 
/* 256 */     return result;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 260 */     if (!this.tls) {
/* 261 */       return "ConnectionSpec()";
/*     */     }
/*     */     
/* 264 */     return "ConnectionSpec(cipherSuites=" + 
/* 265 */       Objects.toString(cipherSuites(), "[all enabled]") + ", tlsVersions=" + 
/* 266 */       Objects.toString(tlsVersions(), "[all enabled]") + ", supportsTlsExtensions=" + this.supportsTlsExtensions + ")";
/*     */   }
/*     */   
/*     */   public static final class Builder {
/*     */     boolean tls;
/*     */     @Nullable
/*     */     String[] cipherSuites;
/*     */     @Nullable
/*     */     String[] tlsVersions;
/*     */     boolean supportsTlsExtensions;
/*     */     
/*     */     Builder(boolean tls) {
/* 278 */       this.tls = tls;
/*     */     }
/*     */     
/*     */     public Builder(ConnectionSpec connectionSpec) {
/* 282 */       this.tls = connectionSpec.tls;
/* 283 */       this.cipherSuites = connectionSpec.cipherSuites;
/* 284 */       this.tlsVersions = connectionSpec.tlsVersions;
/* 285 */       this.supportsTlsExtensions = connectionSpec.supportsTlsExtensions;
/*     */     }
/*     */     
/*     */     public Builder allEnabledCipherSuites() {
/* 289 */       if (!this.tls) throw new IllegalStateException("no cipher suites for cleartext connections"); 
/* 290 */       this.cipherSuites = null;
/* 291 */       return this;
/*     */     }
/*     */     
/*     */     public Builder cipherSuites(CipherSuite... cipherSuites) {
/* 295 */       if (!this.tls) throw new IllegalStateException("no cipher suites for cleartext connections");
/*     */       
/* 297 */       String[] strings = new String[cipherSuites.length];
/* 298 */       for (int i = 0; i < cipherSuites.length; i++) {
/* 299 */         strings[i] = (cipherSuites[i]).javaName;
/*     */       }
/* 301 */       return cipherSuites(strings);
/*     */     }
/*     */     
/*     */     public Builder cipherSuites(String... cipherSuites) {
/* 305 */       if (!this.tls) throw new IllegalStateException("no cipher suites for cleartext connections");
/*     */       
/* 307 */       if (cipherSuites.length == 0) {
/* 308 */         throw new IllegalArgumentException("At least one cipher suite is required");
/*     */       }
/*     */       
/* 311 */       this.cipherSuites = (String[])cipherSuites.clone();
/* 312 */       return this;
/*     */     }
/*     */     
/*     */     public Builder allEnabledTlsVersions() {
/* 316 */       if (!this.tls) throw new IllegalStateException("no TLS versions for cleartext connections"); 
/* 317 */       this.tlsVersions = null;
/* 318 */       return this;
/*     */     }
/*     */     
/*     */     public Builder tlsVersions(TlsVersion... tlsVersions) {
/* 322 */       if (!this.tls) throw new IllegalStateException("no TLS versions for cleartext connections");
/*     */       
/* 324 */       String[] strings = new String[tlsVersions.length];
/* 325 */       for (int i = 0; i < tlsVersions.length; i++) {
/* 326 */         strings[i] = (tlsVersions[i]).javaName;
/*     */       }
/*     */       
/* 329 */       return tlsVersions(strings);
/*     */     }
/*     */     
/*     */     public Builder tlsVersions(String... tlsVersions) {
/* 333 */       if (!this.tls) throw new IllegalStateException("no TLS versions for cleartext connections");
/*     */       
/* 335 */       if (tlsVersions.length == 0) {
/* 336 */         throw new IllegalArgumentException("At least one TLS version is required");
/*     */       }
/*     */       
/* 339 */       this.tlsVersions = (String[])tlsVersions.clone();
/* 340 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder supportsTlsExtensions(boolean supportsTlsExtensions) {
/* 349 */       if (!this.tls) throw new IllegalStateException("no TLS extensions for cleartext connections"); 
/* 350 */       this.supportsTlsExtensions = supportsTlsExtensions;
/* 351 */       return this;
/*     */     }
/*     */     
/*     */     public ConnectionSpec build() {
/* 355 */       return new ConnectionSpec(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\ConnectionSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */