/*     */ package okhttp3;
/*     */ 
/*     */ import java.net.Proxy;
/*     */ import java.net.ProxySelector;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.net.SocketFactory;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLSocketFactory;
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
/*     */ public final class Address
/*     */ {
/*     */   final HttpUrl url;
/*     */   final Dns dns;
/*     */   final SocketFactory socketFactory;
/*     */   final Authenticator proxyAuthenticator;
/*     */   final List<Protocol> protocols;
/*     */   final List<ConnectionSpec> connectionSpecs;
/*     */   final ProxySelector proxySelector;
/*     */   @Nullable
/*     */   final Proxy proxy;
/*     */   @Nullable
/*     */   final SSLSocketFactory sslSocketFactory;
/*     */   @Nullable
/*     */   final HostnameVerifier hostnameVerifier;
/*     */   @Nullable
/*     */   final CertificatePinner certificatePinner;
/*     */   
/*     */   public Address(String uriHost, int uriPort, Dns dns, SocketFactory socketFactory, @Nullable SSLSocketFactory sslSocketFactory, @Nullable HostnameVerifier hostnameVerifier, @Nullable CertificatePinner certificatePinner, Authenticator proxyAuthenticator, @Nullable Proxy proxy, List<Protocol> protocols, List<ConnectionSpec> connectionSpecs, ProxySelector proxySelector) {
/*  55 */     this
/*     */ 
/*     */ 
/*     */       
/*  59 */       .url = (new HttpUrl.Builder()).scheme((sslSocketFactory != null) ? "https" : "http").host(uriHost).port(uriPort).build();
/*     */     
/*  61 */     if (dns == null) throw new NullPointerException("dns == null"); 
/*  62 */     this.dns = dns;
/*     */     
/*  64 */     if (socketFactory == null) throw new NullPointerException("socketFactory == null"); 
/*  65 */     this.socketFactory = socketFactory;
/*     */     
/*  67 */     if (proxyAuthenticator == null) {
/*  68 */       throw new NullPointerException("proxyAuthenticator == null");
/*     */     }
/*  70 */     this.proxyAuthenticator = proxyAuthenticator;
/*     */     
/*  72 */     if (protocols == null) throw new NullPointerException("protocols == null"); 
/*  73 */     this.protocols = Util.immutableList(protocols);
/*     */     
/*  75 */     if (connectionSpecs == null) throw new NullPointerException("connectionSpecs == null"); 
/*  76 */     this.connectionSpecs = Util.immutableList(connectionSpecs);
/*     */     
/*  78 */     if (proxySelector == null) throw new NullPointerException("proxySelector == null"); 
/*  79 */     this.proxySelector = proxySelector;
/*     */     
/*  81 */     this.proxy = proxy;
/*  82 */     this.sslSocketFactory = sslSocketFactory;
/*  83 */     this.hostnameVerifier = hostnameVerifier;
/*  84 */     this.certificatePinner = certificatePinner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpUrl url() {
/*  92 */     return this.url;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dns dns() {
/*  97 */     return this.dns;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketFactory socketFactory() {
/* 102 */     return this.socketFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public Authenticator proxyAuthenticator() {
/* 107 */     return this.proxyAuthenticator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Protocol> protocols() {
/* 115 */     return this.protocols;
/*     */   }
/*     */   
/*     */   public List<ConnectionSpec> connectionSpecs() {
/* 119 */     return this.connectionSpecs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxySelector proxySelector() {
/* 127 */     return this.proxySelector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Proxy proxy() {
/* 135 */     return this.proxy;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public SSLSocketFactory sslSocketFactory() {
/* 140 */     return this.sslSocketFactory;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public HostnameVerifier hostnameVerifier() {
/* 145 */     return this.hostnameVerifier;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public CertificatePinner certificatePinner() {
/* 150 */     return this.certificatePinner;
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 154 */     return (other instanceof Address && this.url
/* 155 */       .equals(((Address)other).url) && 
/* 156 */       equalsNonHost((Address)other));
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 160 */     int result = 17;
/* 161 */     result = 31 * result + this.url.hashCode();
/* 162 */     result = 31 * result + this.dns.hashCode();
/* 163 */     result = 31 * result + this.proxyAuthenticator.hashCode();
/* 164 */     result = 31 * result + this.protocols.hashCode();
/* 165 */     result = 31 * result + this.connectionSpecs.hashCode();
/* 166 */     result = 31 * result + this.proxySelector.hashCode();
/* 167 */     result = 31 * result + Objects.hashCode(this.proxy);
/* 168 */     result = 31 * result + Objects.hashCode(this.sslSocketFactory);
/* 169 */     result = 31 * result + Objects.hashCode(this.hostnameVerifier);
/* 170 */     result = 31 * result + Objects.hashCode(this.certificatePinner);
/* 171 */     return result;
/*     */   }
/*     */   
/*     */   boolean equalsNonHost(Address that) {
/* 175 */     return (this.dns.equals(that.dns) && this.proxyAuthenticator
/* 176 */       .equals(that.proxyAuthenticator) && this.protocols
/* 177 */       .equals(that.protocols) && this.connectionSpecs
/* 178 */       .equals(that.connectionSpecs) && this.proxySelector
/* 179 */       .equals(that.proxySelector) && 
/* 180 */       Objects.equals(this.proxy, that.proxy) && 
/* 181 */       Objects.equals(this.sslSocketFactory, that.sslSocketFactory) && 
/* 182 */       Objects.equals(this.hostnameVerifier, that.hostnameVerifier) && 
/* 183 */       Objects.equals(this.certificatePinner, that.certificatePinner) && 
/* 184 */       url().port() == that.url().port());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 190 */     StringBuilder result = (new StringBuilder()).append("Address{").append(this.url.host()).append(":").append(this.url.port());
/*     */     
/* 192 */     if (this.proxy != null) {
/* 193 */       result.append(", proxy=").append(this.proxy);
/*     */     } else {
/* 195 */       result.append(", proxySelector=").append(this.proxySelector);
/*     */     } 
/*     */     
/* 198 */     result.append("}");
/* 199 */     return result.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\Address.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */