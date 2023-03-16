/*      */ package okhttp3;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.net.Proxy;
/*      */ import java.net.ProxySelector;
/*      */ import java.net.Socket;
/*      */ import java.security.GeneralSecurityException;
/*      */ import java.time.Duration;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import javax.annotation.Nullable;
/*      */ import javax.net.SocketFactory;
/*      */ import javax.net.ssl.HostnameVerifier;
/*      */ import javax.net.ssl.SSLContext;
/*      */ import javax.net.ssl.SSLSocket;
/*      */ import javax.net.ssl.SSLSocketFactory;
/*      */ import javax.net.ssl.TrustManager;
/*      */ import javax.net.ssl.X509TrustManager;
/*      */ import okhttp3.internal.Internal;
/*      */ import okhttp3.internal.Util;
/*      */ import okhttp3.internal.cache.InternalCache;
/*      */ import okhttp3.internal.connection.RealConnection;
/*      */ import okhttp3.internal.connection.RouteDatabase;
/*      */ import okhttp3.internal.connection.StreamAllocation;
/*      */ import okhttp3.internal.http.HttpCodec;
/*      */ import okhttp3.internal.platform.Platform;
/*      */ import okhttp3.internal.proxy.NullProxySelector;
/*      */ import okhttp3.internal.tls.CertificateChainCleaner;
/*      */ import okhttp3.internal.tls.OkHostnameVerifier;
/*      */ import okhttp3.internal.ws.RealWebSocket;
/*      */ import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class OkHttpClient
/*      */   implements Cloneable, Call.Factory, WebSocket.Factory
/*      */ {
/*  127 */   static final List<Protocol> DEFAULT_PROTOCOLS = Util.immutableList((Object[])new Protocol[] { Protocol.HTTP_2, Protocol.HTTP_1_1 });
/*      */ 
/*      */   
/*  130 */   static final List<ConnectionSpec> DEFAULT_CONNECTION_SPECS = Util.immutableList((Object[])new ConnectionSpec[] { ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT }); final Dispatcher dispatcher; @Nullable
/*      */   final Proxy proxy; final List<Protocol> protocols;
/*      */   
/*      */   static {
/*  134 */     Internal.instance = new Internal() {
/*      */         public void addLenient(Headers.Builder builder, String line) {
/*  136 */           builder.addLenient(line);
/*      */         }
/*      */         
/*      */         public void addLenient(Headers.Builder builder, String name, String value) {
/*  140 */           builder.addLenient(name, value);
/*      */         }
/*      */         
/*      */         public void setCache(OkHttpClient.Builder builder, InternalCache internalCache) {
/*  144 */           builder.setInternalCache(internalCache);
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean connectionBecameIdle(ConnectionPool pool, RealConnection connection) {
/*  149 */           return pool.connectionBecameIdle(connection);
/*      */         }
/*      */ 
/*      */         
/*      */         public void acquire(ConnectionPool pool, Address address, StreamAllocation streamAllocation, @Nullable Route route) {
/*  154 */           pool.acquire(address, streamAllocation, route);
/*      */         }
/*      */         
/*      */         public boolean equalsNonHost(Address a, Address b) {
/*  158 */           return a.equalsNonHost(b);
/*      */         }
/*      */         
/*      */         @Nullable
/*      */         public Socket deduplicate(ConnectionPool pool, Address address, StreamAllocation streamAllocation) {
/*  163 */           return pool.deduplicate(address, streamAllocation);
/*      */         }
/*      */         
/*      */         public void put(ConnectionPool pool, RealConnection connection) {
/*  167 */           pool.put(connection);
/*      */         }
/*      */         
/*      */         public RouteDatabase routeDatabase(ConnectionPool connectionPool) {
/*  171 */           return connectionPool.routeDatabase;
/*      */         }
/*      */         
/*      */         public int code(Response.Builder responseBuilder) {
/*  175 */           return responseBuilder.code;
/*      */         }
/*      */ 
/*      */         
/*      */         public void apply(ConnectionSpec tlsConfiguration, SSLSocket sslSocket, boolean isFallback) {
/*  180 */           tlsConfiguration.apply(sslSocket, isFallback);
/*      */         }
/*      */         
/*      */         public boolean isInvalidHttpUrlHost(IllegalArgumentException e) {
/*  184 */           return e.getMessage().startsWith("Invalid URL host");
/*      */         }
/*      */         
/*      */         public StreamAllocation streamAllocation(Call call) {
/*  188 */           return ((RealCall)call).streamAllocation();
/*      */         }
/*      */         @Nullable
/*      */         public IOException timeoutExit(Call call, @Nullable IOException e) {
/*  192 */           return ((RealCall)call).timeoutExit(e);
/*      */         }
/*      */         
/*      */         public Call newWebSocketCall(OkHttpClient client, Request originalRequest) {
/*  196 */           return RealCall.newRealCall(client, originalRequest, true);
/*      */         }
/*      */         
/*      */         public void initCodec(Response.Builder responseBuilder, HttpCodec httpCodec) {
/*  200 */           responseBuilder.initCodec(httpCodec);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   final List<ConnectionSpec> connectionSpecs;
/*      */   final List<Interceptor> interceptors;
/*      */   final List<Interceptor> networkInterceptors;
/*      */   final EventListener.Factory eventListenerFactory;
/*      */   final ProxySelector proxySelector;
/*      */   final CookieJar cookieJar;
/*      */   @Nullable
/*      */   final Cache cache;
/*      */   @Nullable
/*      */   final InternalCache internalCache;
/*      */   final SocketFactory socketFactory;
/*      */   final SSLSocketFactory sslSocketFactory;
/*      */   final CertificateChainCleaner certificateChainCleaner;
/*      */   final HostnameVerifier hostnameVerifier;
/*      */   final CertificatePinner certificatePinner;
/*      */   final Authenticator proxyAuthenticator;
/*      */   final Authenticator authenticator;
/*      */   final ConnectionPool connectionPool;
/*      */   final Dns dns;
/*      */   final boolean followSslRedirects;
/*      */   final boolean followRedirects;
/*      */   final boolean retryOnConnectionFailure;
/*      */   final int callTimeout;
/*      */   final int connectTimeout;
/*      */   final int readTimeout;
/*      */   final int writeTimeout;
/*      */   final int pingInterval;
/*      */   
/*      */   public OkHttpClient() {
/*  235 */     this(new Builder());
/*      */   }
/*      */   
/*      */   OkHttpClient(Builder builder) {
/*  239 */     this.dispatcher = builder.dispatcher;
/*  240 */     this.proxy = builder.proxy;
/*  241 */     this.protocols = builder.protocols;
/*  242 */     this.connectionSpecs = builder.connectionSpecs;
/*  243 */     this.interceptors = Util.immutableList(builder.interceptors);
/*  244 */     this.networkInterceptors = Util.immutableList(builder.networkInterceptors);
/*  245 */     this.eventListenerFactory = builder.eventListenerFactory;
/*  246 */     this.proxySelector = builder.proxySelector;
/*  247 */     this.cookieJar = builder.cookieJar;
/*  248 */     this.cache = builder.cache;
/*  249 */     this.internalCache = builder.internalCache;
/*  250 */     this.socketFactory = builder.socketFactory;
/*      */     
/*  252 */     boolean isTLS = false;
/*  253 */     for (ConnectionSpec spec : this.connectionSpecs) {
/*  254 */       isTLS = (isTLS || spec.isTls());
/*      */     }
/*      */     
/*  257 */     if (builder.sslSocketFactory != null || !isTLS) {
/*  258 */       this.sslSocketFactory = builder.sslSocketFactory;
/*  259 */       this.certificateChainCleaner = builder.certificateChainCleaner;
/*      */     } else {
/*  261 */       X509TrustManager trustManager = Util.platformTrustManager();
/*  262 */       this.sslSocketFactory = newSslSocketFactory(trustManager);
/*  263 */       this.certificateChainCleaner = CertificateChainCleaner.get(trustManager);
/*      */     } 
/*      */     
/*  266 */     if (this.sslSocketFactory != null) {
/*  267 */       Platform.get().configureSslSocketFactory(this.sslSocketFactory);
/*      */     }
/*      */     
/*  270 */     this.hostnameVerifier = builder.hostnameVerifier;
/*  271 */     this.certificatePinner = builder.certificatePinner.withCertificateChainCleaner(this.certificateChainCleaner);
/*      */     
/*  273 */     this.proxyAuthenticator = builder.proxyAuthenticator;
/*  274 */     this.authenticator = builder.authenticator;
/*  275 */     this.connectionPool = builder.connectionPool;
/*  276 */     this.dns = builder.dns;
/*  277 */     this.followSslRedirects = builder.followSslRedirects;
/*  278 */     this.followRedirects = builder.followRedirects;
/*  279 */     this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
/*  280 */     this.callTimeout = builder.callTimeout;
/*  281 */     this.connectTimeout = builder.connectTimeout;
/*  282 */     this.readTimeout = builder.readTimeout;
/*  283 */     this.writeTimeout = builder.writeTimeout;
/*  284 */     this.pingInterval = builder.pingInterval;
/*      */     
/*  286 */     if (this.interceptors.contains(null)) {
/*  287 */       throw new IllegalStateException("Null interceptor: " + this.interceptors);
/*      */     }
/*  289 */     if (this.networkInterceptors.contains(null)) {
/*  290 */       throw new IllegalStateException("Null network interceptor: " + this.networkInterceptors);
/*      */     }
/*      */   }
/*      */   
/*      */   private static SSLSocketFactory newSslSocketFactory(X509TrustManager trustManager) {
/*      */     try {
/*  296 */       SSLContext sslContext = Platform.get().getSSLContext();
/*  297 */       sslContext.init(null, new TrustManager[] { trustManager }, null);
/*  298 */       return sslContext.getSocketFactory();
/*  299 */     } catch (GeneralSecurityException e) {
/*  300 */       throw new AssertionError("No System TLS", e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int callTimeoutMillis() {
/*  309 */     return this.callTimeout;
/*      */   }
/*      */ 
/*      */   
/*      */   public int connectTimeoutMillis() {
/*  314 */     return this.connectTimeout;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readTimeoutMillis() {
/*  319 */     return this.readTimeout;
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeTimeoutMillis() {
/*  324 */     return this.writeTimeout;
/*      */   }
/*      */ 
/*      */   
/*      */   public int pingIntervalMillis() {
/*  329 */     return this.pingInterval;
/*      */   }
/*      */   @Nullable
/*      */   public Proxy proxy() {
/*  333 */     return this.proxy;
/*      */   }
/*      */   
/*      */   public ProxySelector proxySelector() {
/*  337 */     return this.proxySelector;
/*      */   }
/*      */   
/*      */   public CookieJar cookieJar() {
/*  341 */     return this.cookieJar;
/*      */   }
/*      */   @Nullable
/*      */   public Cache cache() {
/*  345 */     return this.cache;
/*      */   }
/*      */   @Nullable
/*      */   InternalCache internalCache() {
/*  349 */     return (this.cache != null) ? this.cache.internalCache : this.internalCache;
/*      */   }
/*      */   
/*      */   public Dns dns() {
/*  353 */     return this.dns;
/*      */   }
/*      */   
/*      */   public SocketFactory socketFactory() {
/*  357 */     return this.socketFactory;
/*      */   }
/*      */   
/*      */   public SSLSocketFactory sslSocketFactory() {
/*  361 */     return this.sslSocketFactory;
/*      */   }
/*      */   
/*      */   public HostnameVerifier hostnameVerifier() {
/*  365 */     return this.hostnameVerifier;
/*      */   }
/*      */   
/*      */   public CertificatePinner certificatePinner() {
/*  369 */     return this.certificatePinner;
/*      */   }
/*      */   
/*      */   public Authenticator authenticator() {
/*  373 */     return this.authenticator;
/*      */   }
/*      */   
/*      */   public Authenticator proxyAuthenticator() {
/*  377 */     return this.proxyAuthenticator;
/*      */   }
/*      */   
/*      */   public ConnectionPool connectionPool() {
/*  381 */     return this.connectionPool;
/*      */   }
/*      */   
/*      */   public boolean followSslRedirects() {
/*  385 */     return this.followSslRedirects;
/*      */   }
/*      */   
/*      */   public boolean followRedirects() {
/*  389 */     return this.followRedirects;
/*      */   }
/*      */   
/*      */   public boolean retryOnConnectionFailure() {
/*  393 */     return this.retryOnConnectionFailure;
/*      */   }
/*      */   
/*      */   public Dispatcher dispatcher() {
/*  397 */     return this.dispatcher;
/*      */   }
/*      */   
/*      */   public List<Protocol> protocols() {
/*  401 */     return this.protocols;
/*      */   }
/*      */   
/*      */   public List<ConnectionSpec> connectionSpecs() {
/*  405 */     return this.connectionSpecs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Interceptor> interceptors() {
/*  414 */     return this.interceptors;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Interceptor> networkInterceptors() {
/*  423 */     return this.networkInterceptors;
/*      */   }
/*      */   
/*      */   public EventListener.Factory eventListenerFactory() {
/*  427 */     return this.eventListenerFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Call newCall(Request request) {
/*  434 */     return RealCall.newRealCall(this, request, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public WebSocket newWebSocket(Request request, WebSocketListener listener) {
/*  441 */     RealWebSocket webSocket = new RealWebSocket(request, listener, new Random(), this.pingInterval);
/*  442 */     webSocket.connect(this);
/*  443 */     return (WebSocket)webSocket;
/*      */   }
/*      */   
/*      */   public Builder newBuilder() {
/*  447 */     return new Builder(this);
/*      */   }
/*      */   
/*      */   public static final class Builder { Dispatcher dispatcher;
/*      */     @Nullable
/*      */     Proxy proxy;
/*      */     List<Protocol> protocols;
/*      */     List<ConnectionSpec> connectionSpecs;
/*  455 */     final List<Interceptor> interceptors = new ArrayList<>();
/*  456 */     final List<Interceptor> networkInterceptors = new ArrayList<>();
/*      */     
/*      */     EventListener.Factory eventListenerFactory;
/*      */     
/*      */     ProxySelector proxySelector;
/*      */     
/*      */     CookieJar cookieJar;
/*      */     
/*      */     @Nullable
/*      */     Cache cache;
/*      */     
/*      */     @Nullable
/*      */     InternalCache internalCache;
/*      */     
/*      */     SocketFactory socketFactory;
/*      */     
/*      */     @Nullable
/*      */     SSLSocketFactory sslSocketFactory;
/*      */     @Nullable
/*      */     CertificateChainCleaner certificateChainCleaner;
/*      */     HostnameVerifier hostnameVerifier;
/*      */     CertificatePinner certificatePinner;
/*      */     Authenticator proxyAuthenticator;
/*      */     
/*      */     public Builder() {
/*  481 */       this.dispatcher = new Dispatcher();
/*  482 */       this.protocols = OkHttpClient.DEFAULT_PROTOCOLS;
/*  483 */       this.connectionSpecs = OkHttpClient.DEFAULT_CONNECTION_SPECS;
/*  484 */       this.eventListenerFactory = EventListener.factory(EventListener.NONE);
/*  485 */       this.proxySelector = ProxySelector.getDefault();
/*  486 */       if (this.proxySelector == null) {
/*  487 */         this.proxySelector = (ProxySelector)new NullProxySelector();
/*      */       }
/*  489 */       this.cookieJar = CookieJar.NO_COOKIES;
/*  490 */       this.socketFactory = SocketFactory.getDefault();
/*  491 */       this.hostnameVerifier = (HostnameVerifier)OkHostnameVerifier.INSTANCE;
/*  492 */       this.certificatePinner = CertificatePinner.DEFAULT;
/*  493 */       this.proxyAuthenticator = Authenticator.NONE;
/*  494 */       this.authenticator = Authenticator.NONE;
/*  495 */       this.connectionPool = new ConnectionPool();
/*  496 */       this.dns = Dns.SYSTEM;
/*  497 */       this.followSslRedirects = true;
/*  498 */       this.followRedirects = true;
/*  499 */       this.retryOnConnectionFailure = true;
/*  500 */       this.callTimeout = 0;
/*  501 */       this.connectTimeout = 10000;
/*  502 */       this.readTimeout = 10000;
/*  503 */       this.writeTimeout = 10000;
/*  504 */       this.pingInterval = 0;
/*      */     }
/*      */     Authenticator authenticator; ConnectionPool connectionPool; Dns dns; boolean followSslRedirects; boolean followRedirects; boolean retryOnConnectionFailure; int callTimeout; int connectTimeout; int readTimeout; int writeTimeout; int pingInterval;
/*      */     Builder(OkHttpClient okHttpClient) {
/*  508 */       this.dispatcher = okHttpClient.dispatcher;
/*  509 */       this.proxy = okHttpClient.proxy;
/*  510 */       this.protocols = okHttpClient.protocols;
/*  511 */       this.connectionSpecs = okHttpClient.connectionSpecs;
/*  512 */       this.interceptors.addAll(okHttpClient.interceptors);
/*  513 */       this.networkInterceptors.addAll(okHttpClient.networkInterceptors);
/*  514 */       this.eventListenerFactory = okHttpClient.eventListenerFactory;
/*  515 */       this.proxySelector = okHttpClient.proxySelector;
/*  516 */       this.cookieJar = okHttpClient.cookieJar;
/*  517 */       this.internalCache = okHttpClient.internalCache;
/*  518 */       this.cache = okHttpClient.cache;
/*  519 */       this.socketFactory = okHttpClient.socketFactory;
/*  520 */       this.sslSocketFactory = okHttpClient.sslSocketFactory;
/*  521 */       this.certificateChainCleaner = okHttpClient.certificateChainCleaner;
/*  522 */       this.hostnameVerifier = okHttpClient.hostnameVerifier;
/*  523 */       this.certificatePinner = okHttpClient.certificatePinner;
/*  524 */       this.proxyAuthenticator = okHttpClient.proxyAuthenticator;
/*  525 */       this.authenticator = okHttpClient.authenticator;
/*  526 */       this.connectionPool = okHttpClient.connectionPool;
/*  527 */       this.dns = okHttpClient.dns;
/*  528 */       this.followSslRedirects = okHttpClient.followSslRedirects;
/*  529 */       this.followRedirects = okHttpClient.followRedirects;
/*  530 */       this.retryOnConnectionFailure = okHttpClient.retryOnConnectionFailure;
/*  531 */       this.callTimeout = okHttpClient.callTimeout;
/*  532 */       this.connectTimeout = okHttpClient.connectTimeout;
/*  533 */       this.readTimeout = okHttpClient.readTimeout;
/*  534 */       this.writeTimeout = okHttpClient.writeTimeout;
/*  535 */       this.pingInterval = okHttpClient.pingInterval;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder callTimeout(long timeout, TimeUnit unit) {
/*  549 */       this.callTimeout = Util.checkDuration("timeout", timeout, unit);
/*  550 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @IgnoreJRERequirement
/*      */     public Builder callTimeout(Duration duration) {
/*  565 */       this.callTimeout = Util.checkDuration("timeout", duration.toMillis(), TimeUnit.MILLISECONDS);
/*  566 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder connectTimeout(long timeout, TimeUnit unit) {
/*  578 */       this.connectTimeout = Util.checkDuration("timeout", timeout, unit);
/*  579 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @IgnoreJRERequirement
/*      */     public Builder connectTimeout(Duration duration) {
/*  592 */       this.connectTimeout = Util.checkDuration("timeout", duration.toMillis(), TimeUnit.MILLISECONDS);
/*  593 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder readTimeout(long timeout, TimeUnit unit) {
/*  607 */       this.readTimeout = Util.checkDuration("timeout", timeout, unit);
/*  608 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @IgnoreJRERequirement
/*      */     public Builder readTimeout(Duration duration) {
/*  623 */       this.readTimeout = Util.checkDuration("timeout", duration.toMillis(), TimeUnit.MILLISECONDS);
/*  624 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder writeTimeout(long timeout, TimeUnit unit) {
/*  637 */       this.writeTimeout = Util.checkDuration("timeout", timeout, unit);
/*  638 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @IgnoreJRERequirement
/*      */     public Builder writeTimeout(Duration duration) {
/*  652 */       this.writeTimeout = Util.checkDuration("timeout", duration.toMillis(), TimeUnit.MILLISECONDS);
/*  653 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder pingInterval(long interval, TimeUnit unit) {
/*  670 */       this.pingInterval = Util.checkDuration("interval", interval, unit);
/*  671 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @IgnoreJRERequirement
/*      */     public Builder pingInterval(Duration duration) {
/*  689 */       this.pingInterval = Util.checkDuration("timeout", duration.toMillis(), TimeUnit.MILLISECONDS);
/*  690 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder proxy(@Nullable Proxy proxy) {
/*  699 */       this.proxy = proxy;
/*  700 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder proxySelector(ProxySelector proxySelector) {
/*  712 */       if (proxySelector == null) throw new NullPointerException("proxySelector == null"); 
/*  713 */       this.proxySelector = proxySelector;
/*  714 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder cookieJar(CookieJar cookieJar) {
/*  724 */       if (cookieJar == null) throw new NullPointerException("cookieJar == null"); 
/*  725 */       this.cookieJar = cookieJar;
/*  726 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     void setInternalCache(@Nullable InternalCache internalCache) {
/*  731 */       this.internalCache = internalCache;
/*  732 */       this.cache = null;
/*      */     }
/*      */ 
/*      */     
/*      */     public Builder cache(@Nullable Cache cache) {
/*  737 */       this.cache = cache;
/*  738 */       this.internalCache = null;
/*  739 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder dns(Dns dns) {
/*  748 */       if (dns == null) throw new NullPointerException("dns == null"); 
/*  749 */       this.dns = dns;
/*  750 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder socketFactory(SocketFactory socketFactory) {
/*  762 */       if (socketFactory == null) throw new NullPointerException("socketFactory == null"); 
/*  763 */       if (socketFactory instanceof SSLSocketFactory) {
/*  764 */         throw new IllegalArgumentException("socketFactory instanceof SSLSocketFactory");
/*      */       }
/*  766 */       this.socketFactory = socketFactory;
/*  767 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
/*  780 */       if (sslSocketFactory == null) throw new NullPointerException("sslSocketFactory == null"); 
/*  781 */       this.sslSocketFactory = sslSocketFactory;
/*  782 */       this.certificateChainCleaner = Platform.get().buildCertificateChainCleaner(sslSocketFactory);
/*  783 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder sslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
/*  818 */       if (sslSocketFactory == null) throw new NullPointerException("sslSocketFactory == null"); 
/*  819 */       if (trustManager == null) throw new NullPointerException("trustManager == null"); 
/*  820 */       this.sslSocketFactory = sslSocketFactory;
/*  821 */       this.certificateChainCleaner = CertificateChainCleaner.get(trustManager);
/*  822 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder hostnameVerifier(HostnameVerifier hostnameVerifier) {
/*  832 */       if (hostnameVerifier == null) throw new NullPointerException("hostnameVerifier == null"); 
/*  833 */       this.hostnameVerifier = hostnameVerifier;
/*  834 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder certificatePinner(CertificatePinner certificatePinner) {
/*  843 */       if (certificatePinner == null) throw new NullPointerException("certificatePinner == null"); 
/*  844 */       this.certificatePinner = certificatePinner;
/*  845 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder authenticator(Authenticator authenticator) {
/*  855 */       if (authenticator == null) throw new NullPointerException("authenticator == null"); 
/*  856 */       this.authenticator = authenticator;
/*  857 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder proxyAuthenticator(Authenticator proxyAuthenticator) {
/*  867 */       if (proxyAuthenticator == null) throw new NullPointerException("proxyAuthenticator == null"); 
/*  868 */       this.proxyAuthenticator = proxyAuthenticator;
/*  869 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder connectionPool(ConnectionPool connectionPool) {
/*  878 */       if (connectionPool == null) throw new NullPointerException("connectionPool == null"); 
/*  879 */       this.connectionPool = connectionPool;
/*  880 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder followSslRedirects(boolean followProtocolRedirects) {
/*  890 */       this.followSslRedirects = followProtocolRedirects;
/*  891 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public Builder followRedirects(boolean followRedirects) {
/*  896 */       this.followRedirects = followRedirects;
/*  897 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder retryOnConnectionFailure(boolean retryOnConnectionFailure) {
/*  919 */       this.retryOnConnectionFailure = retryOnConnectionFailure;
/*  920 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder dispatcher(Dispatcher dispatcher) {
/*  927 */       if (dispatcher == null) throw new IllegalArgumentException("dispatcher == null"); 
/*  928 */       this.dispatcher = dispatcher;
/*  929 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder protocols(List<Protocol> protocols) {
/*  965 */       protocols = new ArrayList<>(protocols);
/*      */ 
/*      */       
/*  968 */       if (!protocols.contains(Protocol.H2_PRIOR_KNOWLEDGE) && 
/*  969 */         !protocols.contains(Protocol.HTTP_1_1)) {
/*  970 */         throw new IllegalArgumentException("protocols must contain h2_prior_knowledge or http/1.1: " + protocols);
/*      */       }
/*      */       
/*  973 */       if (protocols.contains(Protocol.H2_PRIOR_KNOWLEDGE) && protocols.size() > 1) {
/*  974 */         throw new IllegalArgumentException("protocols containing h2_prior_knowledge cannot use other protocols: " + protocols);
/*      */       }
/*      */       
/*  977 */       if (protocols.contains(Protocol.HTTP_1_0)) {
/*  978 */         throw new IllegalArgumentException("protocols must not contain http/1.0: " + protocols);
/*      */       }
/*  980 */       if (protocols.contains(null)) {
/*  981 */         throw new IllegalArgumentException("protocols must not contain null");
/*      */       }
/*      */ 
/*      */       
/*  985 */       protocols.remove(Protocol.SPDY_3);
/*      */ 
/*      */       
/*  988 */       this.protocols = Collections.unmodifiableList(protocols);
/*  989 */       return this;
/*      */     }
/*      */     
/*      */     public Builder connectionSpecs(List<ConnectionSpec> connectionSpecs) {
/*  993 */       this.connectionSpecs = Util.immutableList(connectionSpecs);
/*  994 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public List<Interceptor> interceptors() {
/* 1003 */       return this.interceptors;
/*      */     }
/*      */     
/*      */     public Builder addInterceptor(Interceptor interceptor) {
/* 1007 */       if (interceptor == null) throw new IllegalArgumentException("interceptor == null"); 
/* 1008 */       this.interceptors.add(interceptor);
/* 1009 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public List<Interceptor> networkInterceptors() {
/* 1018 */       return this.networkInterceptors;
/*      */     }
/*      */     
/*      */     public Builder addNetworkInterceptor(Interceptor interceptor) {
/* 1022 */       if (interceptor == null) throw new IllegalArgumentException("interceptor == null"); 
/* 1023 */       this.networkInterceptors.add(interceptor);
/* 1024 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder eventListener(EventListener eventListener) {
/* 1034 */       if (eventListener == null) throw new NullPointerException("eventListener == null"); 
/* 1035 */       this.eventListenerFactory = EventListener.factory(eventListener);
/* 1036 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder eventListenerFactory(EventListener.Factory eventListenerFactory) {
/* 1046 */       if (eventListenerFactory == null) {
/* 1047 */         throw new NullPointerException("eventListenerFactory == null");
/*      */       }
/* 1049 */       this.eventListenerFactory = eventListenerFactory;
/* 1050 */       return this;
/*      */     }
/*      */     
/*      */     public OkHttpClient build() {
/* 1054 */       return new OkHttpClient(this);
/*      */     } }
/*      */ 
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\OkHttpClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */