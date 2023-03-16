/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProxySettings
/*     */ {
/*     */   private final WebSocketFactory mWebSocketFactory;
/*     */   private final Map<String, List<String>> mHeaders;
/*     */   private final SocketFactorySettings mSocketFactorySettings;
/*     */   private boolean mSecure;
/*     */   private String mHost;
/*     */   private int mPort;
/*     */   private String mId;
/*     */   private String mPassword;
/*     */   private String[] mServerNames;
/*     */   
/*     */   ProxySettings(WebSocketFactory factory) {
/* 102 */     this.mWebSocketFactory = factory;
/* 103 */     this.mHeaders = new TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER);
/* 104 */     this.mSocketFactorySettings = new SocketFactorySettings();
/*     */     
/* 106 */     reset();
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
/*     */   ProxySettings(WebSocketFactory factory, ProxySettings settings) {
/* 123 */     this(factory);
/*     */     
/* 125 */     this.mHeaders.putAll(settings.mHeaders);
/* 126 */     this.mSecure = settings.mSecure;
/* 127 */     this.mHost = settings.mHost;
/* 128 */     this.mPort = settings.mPort;
/* 129 */     this.mId = settings.mId;
/* 130 */     this.mPassword = settings.mPassword;
/*     */     
/* 132 */     if (settings.mServerNames != null) {
/*     */       
/* 134 */       this.mServerNames = new String[settings.mServerNames.length];
/* 135 */       System.arraycopy(settings.mServerNames, 0, this.mServerNames, 0, this.mServerNames.length);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebSocketFactory getWebSocketFactory() {
/* 145 */     return this.mWebSocketFactory;
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
/*     */   public ProxySettings reset() {
/* 207 */     this.mSecure = false;
/* 208 */     this.mHost = null;
/* 209 */     this.mPort = -1;
/* 210 */     this.mId = null;
/* 211 */     this.mPassword = null;
/* 212 */     this.mHeaders.clear();
/* 213 */     this.mServerNames = null;
/*     */     
/* 215 */     return this;
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
/*     */   public boolean isSecure() {
/* 228 */     return this.mSecure;
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
/*     */   public ProxySettings setSecure(boolean secure) {
/* 244 */     this.mSecure = secure;
/*     */     
/* 246 */     return this;
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
/*     */   public String getHost() {
/* 263 */     return this.mHost;
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
/*     */   public ProxySettings setHost(String host) {
/* 282 */     this.mHost = host;
/*     */     
/* 284 */     return this;
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
/*     */   public int getPort() {
/* 303 */     return this.mPort;
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
/*     */   public ProxySettings setPort(int port) {
/* 324 */     this.mPort = port;
/*     */     
/* 326 */     return this;
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
/*     */   public String getId() {
/* 347 */     return this.mId;
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
/*     */   public ProxySettings setId(String id) {
/* 370 */     this.mId = id;
/*     */     
/* 372 */     return this;
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
/*     */   public String getPassword() {
/* 384 */     return this.mPassword;
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
/*     */   public ProxySettings setPassword(String password) {
/* 399 */     this.mPassword = password;
/*     */     
/* 401 */     return this;
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
/*     */   public ProxySettings setCredentials(String id, String password) {
/* 422 */     return setId(id).setPassword(password);
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
/*     */   public ProxySettings setServer(String uri) {
/* 442 */     if (uri == null)
/*     */     {
/* 444 */       return this;
/*     */     }
/*     */     
/* 447 */     return setServer(URI.create(uri));
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
/*     */   public ProxySettings setServer(URL url) {
/* 467 */     if (url == null)
/*     */     {
/* 469 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 474 */       return setServer(url.toURI());
/*     */     }
/* 476 */     catch (URISyntaxException e) {
/*     */       
/* 478 */       throw new IllegalArgumentException(e);
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
/*     */   public ProxySettings setServer(URI uri) {
/* 524 */     if (uri == null)
/*     */     {
/* 526 */       return this;
/*     */     }
/*     */     
/* 529 */     String scheme = uri.getScheme();
/* 530 */     String userInfo = uri.getUserInfo();
/* 531 */     String host = uri.getHost();
/* 532 */     int port = uri.getPort();
/*     */     
/* 534 */     return setServer(scheme, userInfo, host, port);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ProxySettings setServer(String scheme, String userInfo, String host, int port) {
/* 540 */     setByScheme(scheme);
/* 541 */     setByUserInfo(userInfo);
/* 542 */     this.mHost = host;
/* 543 */     this.mPort = port;
/*     */     
/* 545 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void setByScheme(String scheme) {
/* 551 */     if ("http".equalsIgnoreCase(scheme)) {
/*     */       
/* 553 */       this.mSecure = false;
/*     */     }
/* 555 */     else if ("https".equalsIgnoreCase(scheme)) {
/*     */       
/* 557 */       this.mSecure = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void setByUserInfo(String userInfo) {
/*     */     String id, pw;
/* 564 */     if (userInfo == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 569 */     String[] pair = userInfo.split(":", 2);
/*     */ 
/*     */ 
/*     */     
/* 573 */     switch (pair.length) {
/*     */       
/*     */       case 2:
/* 576 */         id = pair[0];
/* 577 */         pw = pair[1];
/*     */         break;
/*     */       
/*     */       case 1:
/* 581 */         id = pair[0];
/* 582 */         pw = null;
/*     */         break;
/*     */       
/*     */       default:
/*     */         return;
/*     */     } 
/*     */     
/* 589 */     if (id.length() == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 594 */     this.mId = id;
/* 595 */     this.mPassword = pw;
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
/*     */   public Map<String, List<String>> getHeaders() {
/* 609 */     return this.mHeaders;
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
/*     */   public ProxySettings addHeader(String name, String value) {
/* 629 */     if (name == null || name.length() == 0)
/*     */     {
/* 631 */       return this;
/*     */     }
/*     */     
/* 634 */     List<String> list = this.mHeaders.get(name);
/*     */     
/* 636 */     if (list == null) {
/*     */       
/* 638 */       list = new ArrayList<String>();
/* 639 */       this.mHeaders.put(name, list);
/*     */     } 
/*     */     
/* 642 */     list.add(value);
/*     */     
/* 644 */     return this;
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
/* 657 */     return this.mSocketFactorySettings.getSocketFactory();
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
/*     */   public ProxySettings setSocketFactory(SocketFactory factory) {
/* 672 */     this.mSocketFactorySettings.setSocketFactory(factory);
/*     */     
/* 674 */     return this;
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
/* 687 */     return this.mSocketFactorySettings.getSSLSocketFactory();
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
/*     */   public ProxySettings setSSLSocketFactory(SSLSocketFactory factory) {
/* 702 */     this.mSocketFactorySettings.setSSLSocketFactory(factory);
/*     */     
/* 704 */     return this;
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
/* 716 */     return this.mSocketFactorySettings.getSSLContext();
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
/*     */   public ProxySettings setSSLContext(SSLContext context) {
/* 731 */     this.mSocketFactorySettings.setSSLContext(context);
/*     */     
/* 733 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   SocketFactory selectSocketFactory() {
/* 739 */     return this.mSocketFactorySettings.selectSocketFactory(this.mSecure);
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
/* 753 */     return this.mServerNames;
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
/*     */   public ProxySettings setServerNames(String[] serverNames) {
/* 775 */     this.mServerNames = serverNames;
/*     */     
/* 777 */     return this;
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
/*     */   public ProxySettings setServerName(String serverName) {
/* 798 */     return setServerNames(new String[] { serverName });
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\ProxySettings.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */