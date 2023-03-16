/*     */ package okhttp3.internal.http;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.net.HttpRetryException;
/*     */ import java.net.ProtocolException;
/*     */ import java.net.Proxy;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import okhttp3.Address;
/*     */ import okhttp3.Call;
/*     */ import okhttp3.CertificatePinner;
/*     */ import okhttp3.EventListener;
/*     */ import okhttp3.HttpUrl;
/*     */ import okhttp3.Interceptor;
/*     */ import okhttp3.OkHttpClient;
/*     */ import okhttp3.Request;
/*     */ import okhttp3.RequestBody;
/*     */ import okhttp3.Response;
/*     */ import okhttp3.Route;
/*     */ import okhttp3.internal.Util;
/*     */ import okhttp3.internal.connection.RouteException;
/*     */ import okhttp3.internal.connection.StreamAllocation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RetryAndFollowUpInterceptor
/*     */   implements Interceptor
/*     */ {
/*     */   private static final int MAX_FOLLOW_UPS = 20;
/*     */   private final OkHttpClient client;
/*     */   private volatile StreamAllocation streamAllocation;
/*     */   private Object callStackTrace;
/*     */   private volatile boolean canceled;
/*     */   
/*     */   public RetryAndFollowUpInterceptor(OkHttpClient client) {
/*  74 */     this.client = client;
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
/*     */   public void cancel() {
/*  87 */     this.canceled = true;
/*  88 */     StreamAllocation streamAllocation = this.streamAllocation;
/*  89 */     if (streamAllocation != null) streamAllocation.cancel(); 
/*     */   }
/*     */   
/*     */   public boolean isCanceled() {
/*  93 */     return this.canceled;
/*     */   }
/*     */   
/*     */   public void setCallStackTrace(Object callStackTrace) {
/*  97 */     this.callStackTrace = callStackTrace;
/*     */   }
/*     */   
/*     */   public StreamAllocation streamAllocation() {
/* 101 */     return this.streamAllocation;
/*     */   }
/*     */   
/*     */   public Response intercept(Interceptor.Chain chain) throws IOException {
/* 105 */     Request request = chain.request();
/* 106 */     RealInterceptorChain realChain = (RealInterceptorChain)chain;
/* 107 */     Call call = realChain.call();
/* 108 */     EventListener eventListener = realChain.eventListener();
/*     */ 
/*     */     
/* 111 */     StreamAllocation streamAllocation = new StreamAllocation(this.client.connectionPool(), createAddress(request.url()), call, eventListener, this.callStackTrace);
/* 112 */     this.streamAllocation = streamAllocation;
/*     */     
/* 114 */     int followUpCount = 0;
/* 115 */     Response priorResponse = null; while (true) {
/*     */       Response response; Request followUp;
/* 117 */       if (this.canceled) {
/* 118 */         streamAllocation.release(true);
/* 119 */         throw new IOException("Canceled");
/*     */       } 
/*     */ 
/*     */       
/* 123 */       boolean releaseConnection = true;
/*     */       try {
/* 125 */         response = realChain.proceed(request, streamAllocation, null, null);
/* 126 */         releaseConnection = false;
/* 127 */       } catch (RouteException e) {
/*     */         
/* 129 */         if (!recover(e.getLastConnectException(), streamAllocation, false, request)) {
/* 130 */           throw e.getFirstConnectException();
/*     */         }
/* 132 */         releaseConnection = false;
/*     */         continue;
/* 134 */       } catch (IOException e) {
/*     */         
/* 136 */         boolean requestSendStarted = !(e instanceof okhttp3.internal.http2.ConnectionShutdownException);
/* 137 */         if (!recover(e, streamAllocation, requestSendStarted, request)) throw e; 
/* 138 */         releaseConnection = false;
/*     */         
/*     */         continue;
/*     */       } finally {
/* 142 */         if (releaseConnection) {
/* 143 */           streamAllocation.streamFailed(null);
/* 144 */           streamAllocation.release(true);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 149 */       if (priorResponse != null)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 154 */         response = response.newBuilder().priorResponse(priorResponse.newBuilder().body(null).build()).build();
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 159 */         followUp = followUpRequest(response, streamAllocation.route());
/* 160 */       } catch (IOException e) {
/* 161 */         streamAllocation.release(true);
/* 162 */         throw e;
/*     */       } 
/*     */       
/* 165 */       if (followUp == null) {
/* 166 */         streamAllocation.release(true);
/* 167 */         return response;
/*     */       } 
/*     */       
/* 170 */       Util.closeQuietly((Closeable)response.body());
/*     */       
/* 172 */       if (++followUpCount > 20) {
/* 173 */         streamAllocation.release(true);
/* 174 */         throw new ProtocolException("Too many follow-up requests: " + followUpCount);
/*     */       } 
/*     */       
/* 177 */       if (followUp.body() instanceof UnrepeatableRequestBody) {
/* 178 */         streamAllocation.release(true);
/* 179 */         throw new HttpRetryException("Cannot retry streamed HTTP body", response.code());
/*     */       } 
/*     */       
/* 182 */       if (!sameConnection(response, followUp.url())) {
/* 183 */         streamAllocation.release(false);
/*     */         
/* 185 */         streamAllocation = new StreamAllocation(this.client.connectionPool(), createAddress(followUp.url()), call, eventListener, this.callStackTrace);
/* 186 */         this.streamAllocation = streamAllocation;
/* 187 */       } else if (streamAllocation.codec() != null) {
/* 188 */         throw new IllegalStateException("Closing the body of " + response + " didn't close its backing stream. Bad interceptor?");
/*     */       } 
/*     */ 
/*     */       
/* 192 */       request = followUp;
/* 193 */       priorResponse = response;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Address createAddress(HttpUrl url) {
/* 198 */     SSLSocketFactory sslSocketFactory = null;
/* 199 */     HostnameVerifier hostnameVerifier = null;
/* 200 */     CertificatePinner certificatePinner = null;
/* 201 */     if (url.isHttps()) {
/* 202 */       sslSocketFactory = this.client.sslSocketFactory();
/* 203 */       hostnameVerifier = this.client.hostnameVerifier();
/* 204 */       certificatePinner = this.client.certificatePinner();
/*     */     } 
/*     */     
/* 207 */     return new Address(url.host(), url.port(), this.client.dns(), this.client.socketFactory(), sslSocketFactory, hostnameVerifier, certificatePinner, this.client
/* 208 */         .proxyAuthenticator(), this.client
/* 209 */         .proxy(), this.client.protocols(), this.client.connectionSpecs(), this.client.proxySelector());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean recover(IOException e, StreamAllocation streamAllocation, boolean requestSendStarted, Request userRequest) {
/* 220 */     streamAllocation.streamFailed(e);
/*     */ 
/*     */     
/* 223 */     if (!this.client.retryOnConnectionFailure()) return false;
/*     */ 
/*     */     
/* 226 */     if (requestSendStarted && requestIsUnrepeatable(e, userRequest)) return false;
/*     */ 
/*     */     
/* 229 */     if (!isRecoverable(e, requestSendStarted)) return false;
/*     */ 
/*     */     
/* 232 */     if (!streamAllocation.hasMoreRoutes()) return false;
/*     */ 
/*     */     
/* 235 */     return true;
/*     */   }
/*     */   
/*     */   private boolean requestIsUnrepeatable(IOException e, Request userRequest) {
/* 239 */     return (userRequest.body() instanceof UnrepeatableRequestBody || e instanceof java.io.FileNotFoundException);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isRecoverable(IOException e, boolean requestSendStarted) {
/* 245 */     if (e instanceof ProtocolException) {
/* 246 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 251 */     if (e instanceof java.io.InterruptedIOException) {
/* 252 */       return (e instanceof java.net.SocketTimeoutException && !requestSendStarted);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 257 */     if (e instanceof javax.net.ssl.SSLHandshakeException)
/*     */     {
/*     */       
/* 260 */       if (e.getCause() instanceof java.security.cert.CertificateException) {
/* 261 */         return false;
/*     */       }
/*     */     }
/* 264 */     if (e instanceof javax.net.ssl.SSLPeerUnverifiedException)
/*     */     {
/* 266 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 272 */     return true;
/*     */   }
/*     */   
/*     */   private Request followUpRequest(Response userResponse, Route route) throws IOException {
/*     */     Proxy selectedProxy;
/*     */     String location;
/*     */     HttpUrl url;
/*     */     boolean sameScheme;
/*     */     Request.Builder requestBuilder;
/* 281 */     if (userResponse == null) throw new IllegalStateException(); 
/* 282 */     int responseCode = userResponse.code();
/*     */     
/* 284 */     String method = userResponse.request().method();
/* 285 */     switch (responseCode) {
/*     */ 
/*     */       
/*     */       case 407:
/* 289 */         selectedProxy = (route != null) ? route.proxy() : this.client.proxy();
/* 290 */         if (selectedProxy.type() != Proxy.Type.HTTP) {
/* 291 */           throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
/*     */         }
/* 293 */         return this.client.proxyAuthenticator().authenticate(route, userResponse);
/*     */       
/*     */       case 401:
/* 296 */         return this.client.authenticator().authenticate(route, userResponse);
/*     */ 
/*     */ 
/*     */       
/*     */       case 307:
/*     */       case 308:
/* 302 */         if (!method.equals("GET") && !method.equals("HEAD")) {
/* 303 */           return null;
/*     */         }
/*     */ 
/*     */       
/*     */       case 300:
/*     */       case 301:
/*     */       case 302:
/*     */       case 303:
/* 311 */         if (!this.client.followRedirects()) return null;
/*     */         
/* 313 */         location = userResponse.header("Location");
/* 314 */         if (location == null) return null; 
/* 315 */         url = userResponse.request().url().resolve(location);
/*     */ 
/*     */         
/* 318 */         if (url == null) return null;
/*     */ 
/*     */         
/* 321 */         sameScheme = url.scheme().equals(userResponse.request().url().scheme());
/* 322 */         if (!sameScheme && !this.client.followSslRedirects()) return null;
/*     */ 
/*     */         
/* 325 */         requestBuilder = userResponse.request().newBuilder();
/* 326 */         if (HttpMethod.permitsRequestBody(method)) {
/* 327 */           boolean maintainBody = HttpMethod.redirectsWithBody(method);
/* 328 */           if (HttpMethod.redirectsToGet(method)) {
/* 329 */             requestBuilder.method("GET", null);
/*     */           } else {
/* 331 */             RequestBody requestBody = maintainBody ? userResponse.request().body() : null;
/* 332 */             requestBuilder.method(method, requestBody);
/*     */           } 
/* 334 */           if (!maintainBody) {
/* 335 */             requestBuilder.removeHeader("Transfer-Encoding");
/* 336 */             requestBuilder.removeHeader("Content-Length");
/* 337 */             requestBuilder.removeHeader("Content-Type");
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 344 */         if (!sameConnection(userResponse, url)) {
/* 345 */           requestBuilder.removeHeader("Authorization");
/*     */         }
/*     */         
/* 348 */         return requestBuilder.url(url).build();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 408:
/* 354 */         if (!this.client.retryOnConnectionFailure())
/*     */         {
/* 356 */           return null;
/*     */         }
/*     */         
/* 359 */         if (userResponse.request().body() instanceof UnrepeatableRequestBody) {
/* 360 */           return null;
/*     */         }
/*     */         
/* 363 */         if (userResponse.priorResponse() != null && userResponse
/* 364 */           .priorResponse().code() == 408)
/*     */         {
/* 366 */           return null;
/*     */         }
/*     */         
/* 369 */         if (retryAfter(userResponse, 0) > 0) {
/* 370 */           return null;
/*     */         }
/*     */         
/* 373 */         return userResponse.request();
/*     */       
/*     */       case 503:
/* 376 */         if (userResponse.priorResponse() != null && userResponse
/* 377 */           .priorResponse().code() == 503)
/*     */         {
/* 379 */           return null;
/*     */         }
/*     */         
/* 382 */         if (retryAfter(userResponse, 2147483647) == 0)
/*     */         {
/* 384 */           return userResponse.request();
/*     */         }
/*     */         
/* 387 */         return null;
/*     */     } 
/*     */     
/* 390 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private int retryAfter(Response userResponse, int defaultDelay) {
/* 395 */     String header = userResponse.header("Retry-After");
/*     */     
/* 397 */     if (header == null) {
/* 398 */       return defaultDelay;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 403 */     if (header.matches("\\d+")) {
/* 404 */       return Integer.valueOf(header).intValue();
/*     */     }
/*     */     
/* 407 */     return Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean sameConnection(Response response, HttpUrl followUp) {
/* 415 */     HttpUrl url = response.request().url();
/* 416 */     return (url.host().equals(followUp.host()) && url
/* 417 */       .port() == followUp.port() && url
/* 418 */       .scheme().equals(followUp.scheme()));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\http\RetryAndFollowUpInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */