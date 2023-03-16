/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import javax.net.SocketFactory;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocket;
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
/*     */ class SocketConnector
/*     */ {
/*     */   private final SocketFactory mSocketFactory;
/*     */   private final Address mAddress;
/*     */   private final int mConnectionTimeout;
/*     */   private final int mSocketTimeout;
/*     */   private final String[] mServerNames;
/*     */   private final ProxyHandshaker mProxyHandshaker;
/*     */   private final SSLSocketFactory mSSLSocketFactory;
/*     */   private final String mHost;
/*     */   private final int mPort;
/*  48 */   private DualStackMode mDualStackMode = DualStackMode.BOTH;
/*  49 */   private int mDualStackFallbackDelay = 250;
/*     */   
/*     */   private boolean mVerifyHostname;
/*     */   private Socket mSocket;
/*     */   
/*     */   SocketConnector(SocketFactory socketFactory, Address address, int timeout, String[] serverNames, int socketTimeout) {
/*  55 */     this(socketFactory, address, timeout, socketTimeout, serverNames, null, null, null, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SocketConnector(SocketFactory socketFactory, Address address, int timeout, int socketTimeout, String[] serverNames, ProxyHandshaker handshaker, SSLSocketFactory sslSocketFactory, String host, int port) {
/*  64 */     this.mSocketFactory = socketFactory;
/*  65 */     this.mAddress = address;
/*  66 */     this.mConnectionTimeout = timeout;
/*  67 */     this.mSocketTimeout = socketTimeout;
/*  68 */     this.mServerNames = serverNames;
/*  69 */     this.mProxyHandshaker = handshaker;
/*  70 */     this.mSSLSocketFactory = sslSocketFactory;
/*  71 */     this.mHost = host;
/*  72 */     this.mPort = port;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getConnectionTimeout() {
/*  78 */     return this.mConnectionTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket getSocket() {
/*  84 */     return this.mSocket;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket getConnectedSocket() throws WebSocketException {
/*  91 */     if (this.mSocket == null)
/*     */     {
/*  93 */       connectSocket();
/*     */     }
/*     */     
/*  96 */     return this.mSocket;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void connectSocket() throws WebSocketException {
/* 103 */     SocketInitiator socketInitiator = new SocketInitiator(this.mSocketFactory, this.mAddress, this.mConnectionTimeout, this.mServerNames, this.mDualStackMode, this.mDualStackFallbackDelay);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     InetAddress[] addresses = resolveHostname();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 114 */       this.mSocket = socketInitiator.establish(addresses);
/*     */     }
/* 116 */     catch (Exception e) {
/*     */ 
/*     */       
/* 119 */       boolean proxied = (this.mProxyHandshaker != null);
/*     */ 
/*     */       
/* 122 */       String message = String.format("Failed to connect to %s'%s': %s", new Object[] { proxied ? "the proxy " : "", this.mAddress, e
/* 123 */             .getMessage() });
/*     */ 
/*     */       
/* 126 */       throw new WebSocketException(WebSocketError.SOCKET_CONNECT_ERROR, message, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private InetAddress[] resolveHostname() throws WebSocketException {
/* 133 */     InetAddress[] addresses = null;
/* 134 */     UnknownHostException exception = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 139 */       addresses = InetAddress.getAllByName(this.mAddress.getHostname());
/*     */ 
/*     */       
/* 142 */       Arrays.sort(addresses, new Comparator<InetAddress>() {
/*     */             public int compare(InetAddress left, InetAddress right) {
/* 144 */               if (left.getClass() == right.getClass())
/*     */               {
/* 146 */                 return 0;
/*     */               }
/* 148 */               if (left instanceof java.net.Inet6Address)
/*     */               {
/* 150 */                 return -1;
/*     */               }
/*     */ 
/*     */               
/* 154 */               return 1;
/*     */             }
/*     */           });
/*     */     
/*     */     }
/* 159 */     catch (UnknownHostException e) {
/*     */       
/* 161 */       exception = e;
/*     */     } 
/*     */ 
/*     */     
/* 165 */     if (addresses != null && addresses.length > 0)
/*     */     {
/* 167 */       return addresses;
/*     */     }
/*     */     
/* 170 */     if (exception == null)
/*     */     {
/* 172 */       exception = new UnknownHostException("No IP addresses found");
/*     */     }
/*     */ 
/*     */     
/* 176 */     String message = String.format("Failed to resolve hostname %s: %s", new Object[] { this.mAddress, exception
/* 177 */           .getMessage() });
/*     */ 
/*     */     
/* 180 */     throw new WebSocketException(WebSocketError.SOCKET_CONNECT_ERROR, message, exception);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket connect() throws WebSocketException {
/*     */     try {
/* 189 */       doConnect();
/* 190 */       assert this.mSocket != null;
/* 191 */       return this.mSocket;
/*     */     }
/* 193 */     catch (WebSocketException e) {
/*     */ 
/*     */ 
/*     */       
/* 197 */       if (this.mSocket != null) {
/*     */         
/*     */         try {
/*     */ 
/*     */           
/* 202 */           this.mSocket.close();
/*     */         }
/* 204 */         catch (IOException iOException) {}
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 210 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   SocketConnector setDualStackSettings(DualStackMode mode, int fallbackDelay) {
/* 217 */     this.mDualStackMode = mode;
/* 218 */     this.mDualStackFallbackDelay = fallbackDelay;
/*     */     
/* 220 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   SocketConnector setVerifyHostname(boolean verifyHostname) {
/* 226 */     this.mVerifyHostname = verifyHostname;
/*     */     
/* 228 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doConnect() throws WebSocketException {
/* 235 */     boolean proxied = (this.mProxyHandshaker != null);
/*     */ 
/*     */     
/* 238 */     connectSocket();
/* 239 */     assert this.mSocket != null;
/*     */     
/* 241 */     if (this.mSocketTimeout > 0)
/*     */     {
/*     */       
/* 244 */       setSoTimeout(this.mSocketTimeout);
/*     */     }
/*     */     
/* 247 */     if (this.mSocket instanceof SSLSocket)
/*     */     {
/*     */ 
/*     */       
/* 251 */       verifyHostname((SSLSocket)this.mSocket, this.mAddress.getHostname());
/*     */     }
/*     */ 
/*     */     
/* 255 */     if (proxied)
/*     */     {
/*     */ 
/*     */       
/* 259 */       handshake();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setSoTimeout(int timeout) throws WebSocketException {
/* 267 */     assert this.mSocket != null;
/*     */     
/*     */     try {
/* 270 */       this.mSocket.setSoTimeout(timeout);
/*     */     }
/* 272 */     catch (SocketException e) {
/*     */ 
/*     */       
/* 275 */       String message = String.format("Failed to set SO_TIMEOUT: %s", new Object[] { e
/* 276 */             .getMessage() });
/* 277 */       throw new WebSocketException(WebSocketError.SOCKET_CONNECT_ERROR, message, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void verifyHostname(SSLSocket socket, String hostname) throws HostnameUnverifiedException {
/* 284 */     if (!this.mVerifyHostname) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 291 */     OkHostnameVerifier verifier = OkHostnameVerifier.INSTANCE;
/*     */ 
/*     */     
/* 294 */     SSLSession session = socket.getSession();
/*     */ 
/*     */     
/* 297 */     if (verifier.verify(hostname, session)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 304 */     throw new HostnameUnverifiedException(socket, hostname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handshake() throws WebSocketException {
/* 314 */     assert this.mSocket != null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 319 */       this.mProxyHandshaker.perform(this.mSocket);
/*     */     }
/* 321 */     catch (IOException e) {
/*     */ 
/*     */       
/* 324 */       String message = String.format("Handshake with the proxy server (%s) failed: %s", new Object[] { this.mAddress, e
/* 325 */             .getMessage() });
/*     */ 
/*     */       
/* 328 */       throw new WebSocketException(WebSocketError.PROXY_HANDSHAKE_ERROR, message, e);
/*     */     } 
/*     */     
/* 331 */     if (this.mSSLSocketFactory == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 340 */       this.mSocket = this.mSSLSocketFactory.createSocket(this.mSocket, this.mHost, this.mPort, true);
/*     */     }
/* 342 */     catch (IOException e) {
/*     */ 
/*     */       
/* 345 */       String message = "Failed to overlay an existing socket: " + e.getMessage();
/*     */ 
/*     */       
/* 348 */       throw new WebSocketException(WebSocketError.SOCKET_OVERLAY_ERROR, message, e);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 355 */       ((SSLSocket)this.mSocket).startHandshake();
/*     */ 
/*     */ 
/*     */       
/* 359 */       verifyHostname((SSLSocket)this.mSocket, this.mProxyHandshaker.getProxiedHostname());
/*     */     }
/* 361 */     catch (IOException e) {
/*     */ 
/*     */       
/* 364 */       String message = String.format("SSL handshake with the WebSocket endpoint (%s) failed: %s", new Object[] { this.mAddress, e
/* 365 */             .getMessage() });
/*     */ 
/*     */       
/* 368 */       throw new WebSocketException(WebSocketError.SSL_HANDSHAKE_ERROR, message, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void closeSilently() {
/* 375 */     if (this.mSocket != null)
/*     */       
/*     */       try {
/*     */         
/* 379 */         this.mSocket.close();
/*     */       }
/* 381 */       catch (Throwable throwable) {} 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\SocketConnector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */