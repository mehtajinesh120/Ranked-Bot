/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import javax.net.SocketFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSocketFactory;
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
/*     */ public class WebSocketFactory
/*     */ {
/*     */   private final SocketFactorySettings mSocketFactorySettings;
/*     */   private final ProxySettings mProxySettings;
/*     */   private int mConnectionTimeout;
/*     */   private int mSocketTimeout;
/*  38 */   private DualStackMode mDualStackMode = DualStackMode.BOTH;
/*  39 */   private int mDualStackFallbackDelay = 250;
/*     */ 
/*     */   
/*     */   private boolean mVerifyHostname = true;
/*     */ 
/*     */   
/*     */   private String[] mServerNames;
/*     */ 
/*     */   
/*     */   public WebSocketFactory() {
/*  49 */     this.mSocketFactorySettings = new SocketFactorySettings();
/*  50 */     this.mProxySettings = new ProxySettings(this);
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
/*     */   public WebSocketFactory(WebSocketFactory other) {
/*  67 */     if (other == null)
/*     */     {
/*  69 */       throw new IllegalArgumentException("The given WebSocketFactory is null");
/*     */     }
/*     */     
/*  72 */     this.mSocketFactorySettings = new SocketFactorySettings(other.mSocketFactorySettings);
/*  73 */     this.mProxySettings = new ProxySettings(this, other.mProxySettings);
/*  74 */     this.mConnectionTimeout = other.mConnectionTimeout;
/*  75 */     this.mSocketTimeout = other.mSocketTimeout;
/*  76 */     this.mDualStackMode = other.mDualStackMode;
/*  77 */     this.mDualStackFallbackDelay = other.mDualStackFallbackDelay;
/*  78 */     this.mVerifyHostname = other.mVerifyHostname;
/*     */     
/*  80 */     if (other.mServerNames != null) {
/*     */       
/*  82 */       this.mServerNames = new String[other.mServerNames.length];
/*  83 */       System.arraycopy(other.mServerNames, 0, this.mServerNames, 0, this.mServerNames.length);
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
/*     */   
/*     */   public SocketFactory getSocketFactory() {
/*  97 */     return this.mSocketFactorySettings.getSocketFactory();
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
/*     */   public WebSocketFactory setSocketFactory(SocketFactory factory) {
/* 113 */     this.mSocketFactorySettings.setSocketFactory(factory);
/*     */     
/* 115 */     return this;
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
/*     */   public SSLSocketFactory getSSLSocketFactory() {
/* 128 */     return this.mSocketFactorySettings.getSSLSocketFactory();
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
/*     */   public WebSocketFactory setSSLSocketFactory(SSLSocketFactory factory) {
/* 144 */     this.mSocketFactorySettings.setSSLSocketFactory(factory);
/*     */     
/* 146 */     return this;
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
/*     */   public SSLContext getSSLContext() {
/* 158 */     return this.mSocketFactorySettings.getSSLContext();
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
/*     */   public WebSocketFactory setSSLContext(SSLContext context) {
/* 174 */     this.mSocketFactorySettings.setSSLContext(context);
/*     */     
/* 176 */     return this;
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
/*     */   public ProxySettings getProxySettings() {
/* 192 */     return this.mProxySettings;
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
/*     */   public int getConnectionTimeout() {
/* 213 */     return this.mConnectionTimeout;
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
/*     */   public WebSocketFactory setConnectionTimeout(int timeout) {
/* 234 */     if (timeout < 0)
/*     */     {
/* 236 */       throw new IllegalArgumentException("timeout value cannot be negative.");
/*     */     }
/*     */     
/* 239 */     this.mConnectionTimeout = timeout;
/*     */     
/* 241 */     return this;
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
/*     */   public int getSocketTimeout() {
/* 262 */     return this.mSocketTimeout;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebSocketFactory setSocketTimeout(int timeout) {
/* 289 */     if (timeout < 0)
/*     */     {
/* 291 */       throw new IllegalArgumentException("timeout value cannot be negative.");
/*     */     }
/*     */     
/* 294 */     this.mSocketTimeout = timeout;
/*     */     
/* 296 */     return this;
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
/*     */   public DualStackMode getDualStackMode() {
/* 315 */     return this.mDualStackMode;
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
/*     */   public WebSocketFactory setDualStackMode(DualStackMode mode) {
/* 331 */     this.mDualStackMode = mode;
/*     */     
/* 333 */     return this;
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
/*     */   public int getDualStackFallbackDelay() {
/* 354 */     return this.mDualStackFallbackDelay;
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
/*     */   public WebSocketFactory setDualStackFallbackDelay(int delay) {
/* 371 */     if (delay < 0)
/*     */     {
/* 373 */       throw new IllegalArgumentException("delay value cannot be negative.");
/*     */     }
/*     */     
/* 376 */     this.mDualStackFallbackDelay = delay;
/*     */     
/* 378 */     return this;
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
/*     */   public boolean getVerifyHostname() {
/* 396 */     return this.mVerifyHostname;
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
/*     */   public WebSocketFactory setVerifyHostname(boolean verifyHostname) {
/* 432 */     this.mVerifyHostname = verifyHostname;
/*     */     
/* 434 */     return this;
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
/*     */   public String[] getServerNames() {
/* 448 */     return this.mServerNames;
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
/*     */   
/*     */   public WebSocketFactory setServerNames(String[] serverNames) {
/* 470 */     this.mServerNames = serverNames;
/*     */     
/* 472 */     return this;
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
/*     */   public WebSocketFactory setServerName(String serverName) {
/* 493 */     return setServerNames(new String[] { serverName });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebSocket createSocket(String uri) throws IOException {
/* 520 */     return createSocket(uri, getConnectionTimeout());
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
/*     */   public WebSocket createSocket(String uri, int timeout) throws IOException {
/* 554 */     if (uri == null)
/*     */     {
/* 556 */       throw new IllegalArgumentException("The given URI is null.");
/*     */     }
/*     */     
/* 559 */     if (timeout < 0)
/*     */     {
/* 561 */       throw new IllegalArgumentException("The given timeout value is negative.");
/*     */     }
/*     */     
/* 564 */     return createSocket(URI.create(uri), timeout);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebSocket createSocket(URL url) throws IOException {
/* 591 */     return createSocket(url, getConnectionTimeout());
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
/*     */   public WebSocket createSocket(URL url, int timeout) throws IOException {
/* 624 */     if (url == null)
/*     */     {
/* 626 */       throw new IllegalArgumentException("The given URL is null.");
/*     */     }
/*     */     
/* 629 */     if (timeout < 0)
/*     */     {
/* 631 */       throw new IllegalArgumentException("The given timeout value is negative.");
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 636 */       return createSocket(url.toURI(), timeout);
/*     */     }
/* 638 */     catch (URISyntaxException e) {
/*     */       
/* 640 */       throw new IllegalArgumentException("Failed to convert the given URL into a URI.");
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
/*     */   public WebSocket createSocket(URI uri) throws IOException {
/* 699 */     return createSocket(uri, getConnectionTimeout());
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
/*     */   public WebSocket createSocket(URI uri, int timeout) throws IOException {
/* 762 */     if (uri == null)
/*     */     {
/* 764 */       throw new IllegalArgumentException("The given URI is null.");
/*     */     }
/*     */     
/* 767 */     if (timeout < 0)
/*     */     {
/* 769 */       throw new IllegalArgumentException("The given timeout value is negative.");
/*     */     }
/*     */ 
/*     */     
/* 773 */     String scheme = uri.getScheme();
/* 774 */     String userInfo = uri.getUserInfo();
/* 775 */     String host = Misc.extractHost(uri);
/* 776 */     int port = uri.getPort();
/* 777 */     String path = uri.getRawPath();
/* 778 */     String query = uri.getRawQuery();
/*     */     
/* 780 */     return createSocket(scheme, userInfo, host, port, path, query, timeout);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private WebSocket createSocket(String scheme, String userInfo, String host, int port, String path, String query, int timeout) throws IOException {
/* 789 */     boolean secure = isSecureConnectionRequired(scheme);
/*     */ 
/*     */     
/* 792 */     if (host == null || host.length() == 0)
/*     */     {
/* 794 */       throw new IllegalArgumentException("The host part is empty.");
/*     */     }
/*     */ 
/*     */     
/* 798 */     path = determinePath(path);
/*     */ 
/*     */     
/* 801 */     SocketConnector connector = createRawSocket(host, port, secure, timeout);
/*     */ 
/*     */     
/* 804 */     return createWebSocket(secure, userInfo, host, port, path, query, connector);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isSecureConnectionRequired(String scheme) {
/* 810 */     if (scheme == null || scheme.length() == 0)
/*     */     {
/* 812 */       throw new IllegalArgumentException("The scheme part is empty.");
/*     */     }
/*     */     
/* 815 */     if ("wss".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))
/*     */     {
/* 817 */       return true;
/*     */     }
/*     */     
/* 820 */     if ("ws".equalsIgnoreCase(scheme) || "http".equalsIgnoreCase(scheme))
/*     */     {
/* 822 */       return false;
/*     */     }
/*     */     
/* 825 */     throw new IllegalArgumentException("Bad scheme: " + scheme);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String determinePath(String path) {
/* 831 */     if (path == null || path.length() == 0)
/*     */     {
/* 833 */       return "/";
/*     */     }
/*     */     
/* 836 */     if (path.startsWith("/"))
/*     */     {
/* 838 */       return path;
/*     */     }
/*     */ 
/*     */     
/* 842 */     return "/" + path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SocketConnector createRawSocket(String host, int port, boolean secure, int timeout) throws IOException {
/* 852 */     port = determinePort(port, secure);
/*     */ 
/*     */     
/* 855 */     boolean proxied = (this.mProxySettings.getHost() != null);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 860 */     if (proxied)
/*     */     {
/*     */       
/* 863 */       return createProxiedRawSocket(host, port, secure, timeout);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 868 */     return createDirectRawSocket(host, port, secure, timeout);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SocketConnector createProxiedRawSocket(String host, int port, boolean secure, int timeout) {
/* 879 */     int proxyPort = determinePort(this.mProxySettings.getPort(), this.mProxySettings.isSecure());
/*     */ 
/*     */     
/* 882 */     SocketFactory factory = this.mProxySettings.selectSocketFactory();
/*     */ 
/*     */     
/* 885 */     Address address = new Address(this.mProxySettings.getHost(), proxyPort);
/*     */ 
/*     */     
/* 888 */     ProxyHandshaker handshaker = new ProxyHandshaker(host, port, this.mProxySettings);
/*     */ 
/*     */ 
/*     */     
/* 892 */     SSLSocketFactory sslSocketFactory = secure ? (SSLSocketFactory)this.mSocketFactorySettings.selectSocketFactory(secure) : null;
/*     */ 
/*     */     
/* 895 */     return (new SocketConnector(factory, address, timeout, this.mSocketTimeout, this.mProxySettings
/* 896 */         .getServerNames(), handshaker, sslSocketFactory, host, port))
/*     */       
/* 898 */       .setDualStackSettings(this.mDualStackMode, this.mDualStackFallbackDelay)
/* 899 */       .setVerifyHostname(this.mVerifyHostname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SocketConnector createDirectRawSocket(String host, int port, boolean secure, int timeout) {
/* 906 */     SocketFactory factory = this.mSocketFactorySettings.selectSocketFactory(secure);
/*     */ 
/*     */     
/* 909 */     Address address = new Address(host, port);
/*     */ 
/*     */     
/* 912 */     return (new SocketConnector(factory, address, timeout, this.mServerNames, this.mSocketTimeout))
/* 913 */       .setDualStackSettings(this.mDualStackMode, this.mDualStackFallbackDelay)
/* 914 */       .setVerifyHostname(this.mVerifyHostname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int determinePort(int port, boolean secure) {
/* 920 */     if (0 <= port)
/*     */     {
/* 922 */       return port;
/*     */     }
/*     */     
/* 925 */     if (secure)
/*     */     {
/* 927 */       return 443;
/*     */     }
/*     */ 
/*     */     
/* 931 */     return 80;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private WebSocket createWebSocket(boolean secure, String userInfo, String host, int port, String path, String query, SocketConnector connector) {
/* 941 */     if (0 <= port)
/*     */     {
/* 943 */       host = host + ":" + port;
/*     */     }
/*     */ 
/*     */     
/* 947 */     if (query != null)
/*     */     {
/* 949 */       path = path + "?" + query;
/*     */     }
/*     */     
/* 952 */     return new WebSocket(this, secure, userInfo, host, path, connector);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\WebSocketFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */