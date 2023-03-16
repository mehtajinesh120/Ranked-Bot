/*     */ package okhttp3;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.annotation.Nullable;
/*     */ import okhttp3.internal.NamedRunnable;
/*     */ import okhttp3.internal.cache.CacheInterceptor;
/*     */ import okhttp3.internal.connection.ConnectInterceptor;
/*     */ import okhttp3.internal.connection.StreamAllocation;
/*     */ import okhttp3.internal.http.BridgeInterceptor;
/*     */ import okhttp3.internal.http.CallServerInterceptor;
/*     */ import okhttp3.internal.http.RealInterceptorChain;
/*     */ import okhttp3.internal.http.RetryAndFollowUpInterceptor;
/*     */ import okhttp3.internal.platform.Platform;
/*     */ import okio.AsyncTimeout;
/*     */ import okio.Timeout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class RealCall
/*     */   implements Call
/*     */ {
/*     */   final OkHttpClient client;
/*     */   final RetryAndFollowUpInterceptor retryAndFollowUpInterceptor;
/*     */   final AsyncTimeout timeout;
/*     */   @Nullable
/*     */   private EventListener eventListener;
/*     */   final Request originalRequest;
/*     */   final boolean forWebSocket;
/*     */   private boolean executed;
/*     */   
/*     */   private RealCall(OkHttpClient client, Request originalRequest, boolean forWebSocket) {
/*  60 */     this.client = client;
/*  61 */     this.originalRequest = originalRequest;
/*  62 */     this.forWebSocket = forWebSocket;
/*  63 */     this.retryAndFollowUpInterceptor = new RetryAndFollowUpInterceptor(client);
/*  64 */     this.timeout = new AsyncTimeout() {
/*     */         protected void timedOut() {
/*  66 */           RealCall.this.cancel();
/*     */         }
/*     */       };
/*  69 */     this.timeout.timeout(client.callTimeoutMillis(), TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */   
/*     */   static RealCall newRealCall(OkHttpClient client, Request originalRequest, boolean forWebSocket) {
/*  74 */     RealCall call = new RealCall(client, originalRequest, forWebSocket);
/*  75 */     call.eventListener = client.eventListenerFactory().create(call);
/*  76 */     return call;
/*     */   }
/*     */   
/*     */   public Request request() {
/*  80 */     return this.originalRequest;
/*     */   }
/*     */   
/*     */   public Response execute() throws IOException {
/*  84 */     synchronized (this) {
/*  85 */       if (this.executed) throw new IllegalStateException("Already Executed"); 
/*  86 */       this.executed = true;
/*     */     } 
/*  88 */     captureCallStackTrace();
/*  89 */     this.timeout.enter();
/*  90 */     this.eventListener.callStart(this);
/*     */     try {
/*  92 */       this.client.dispatcher().executed(this);
/*  93 */       Response result = getResponseWithInterceptorChain();
/*  94 */       if (result == null) throw new IOException("Canceled"); 
/*  95 */       return result;
/*  96 */     } catch (IOException e) {
/*  97 */       e = timeoutExit(e);
/*  98 */       this.eventListener.callFailed(this, e);
/*  99 */       throw e;
/*     */     } finally {
/* 101 */       this.client.dispatcher().finished(this);
/*     */     } 
/*     */   }
/*     */   @Nullable
/*     */   IOException timeoutExit(@Nullable IOException cause) {
/* 106 */     if (!this.timeout.exit()) return cause;
/*     */     
/* 108 */     InterruptedIOException e = new InterruptedIOException("timeout");
/* 109 */     if (cause != null) {
/* 110 */       e.initCause(cause);
/*     */     }
/* 112 */     return e;
/*     */   }
/*     */   
/*     */   private void captureCallStackTrace() {
/* 116 */     Object callStackTrace = Platform.get().getStackTraceForCloseable("response.body().close()");
/* 117 */     this.retryAndFollowUpInterceptor.setCallStackTrace(callStackTrace);
/*     */   }
/*     */   
/*     */   public void enqueue(Callback responseCallback) {
/* 121 */     synchronized (this) {
/* 122 */       if (this.executed) throw new IllegalStateException("Already Executed"); 
/* 123 */       this.executed = true;
/*     */     } 
/* 125 */     captureCallStackTrace();
/* 126 */     this.eventListener.callStart(this);
/* 127 */     this.client.dispatcher().enqueue(new AsyncCall(responseCallback));
/*     */   }
/*     */   
/*     */   public void cancel() {
/* 131 */     this.retryAndFollowUpInterceptor.cancel();
/*     */   }
/*     */   
/*     */   public Timeout timeout() {
/* 135 */     return (Timeout)this.timeout;
/*     */   }
/*     */   
/*     */   public synchronized boolean isExecuted() {
/* 139 */     return this.executed;
/*     */   }
/*     */   
/*     */   public boolean isCanceled() {
/* 143 */     return this.retryAndFollowUpInterceptor.isCanceled();
/*     */   }
/*     */ 
/*     */   
/*     */   public RealCall clone() {
/* 148 */     return newRealCall(this.client, this.originalRequest, this.forWebSocket);
/*     */   }
/*     */   
/*     */   StreamAllocation streamAllocation() {
/* 152 */     return this.retryAndFollowUpInterceptor.streamAllocation();
/*     */   }
/*     */   
/*     */   final class AsyncCall extends NamedRunnable {
/*     */     private final Callback responseCallback;
/* 157 */     private volatile AtomicInteger callsPerHost = new AtomicInteger(0);
/*     */     
/*     */     AsyncCall(Callback responseCallback) {
/* 160 */       super("OkHttp %s", new Object[] { this$0.redactedUrl() });
/* 161 */       this.responseCallback = responseCallback;
/*     */     }
/*     */     
/*     */     AtomicInteger callsPerHost() {
/* 165 */       return this.callsPerHost;
/*     */     }
/*     */     
/*     */     void reuseCallsPerHostFrom(AsyncCall other) {
/* 169 */       this.callsPerHost = other.callsPerHost;
/*     */     }
/*     */     
/*     */     String host() {
/* 173 */       return RealCall.this.originalRequest.url().host();
/*     */     }
/*     */     
/*     */     Request request() {
/* 177 */       return RealCall.this.originalRequest;
/*     */     }
/*     */     
/*     */     RealCall get() {
/* 181 */       return RealCall.this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void executeOn(ExecutorService executorService) {
/* 189 */       assert !Thread.holdsLock(RealCall.this.client.dispatcher());
/* 190 */       boolean success = false;
/*     */       try {
/* 192 */         executorService.execute((Runnable)this);
/* 193 */         success = true;
/* 194 */       } catch (RejectedExecutionException e) {
/* 195 */         InterruptedIOException ioException = new InterruptedIOException("executor rejected");
/* 196 */         ioException.initCause(e);
/* 197 */         RealCall.this.eventListener.callFailed(RealCall.this, ioException);
/* 198 */         this.responseCallback.onFailure(RealCall.this, ioException);
/*     */       } finally {
/* 200 */         if (!success) {
/* 201 */           RealCall.this.client.dispatcher().finished(this);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void execute() {
/* 207 */       boolean signalledCallback = false;
/* 208 */       RealCall.this.timeout.enter();
/*     */       try {
/* 210 */         Response response = RealCall.this.getResponseWithInterceptorChain();
/* 211 */         if (RealCall.this.retryAndFollowUpInterceptor.isCanceled()) {
/* 212 */           signalledCallback = true;
/* 213 */           this.responseCallback.onFailure(RealCall.this, new IOException("Canceled"));
/*     */         } else {
/* 215 */           signalledCallback = true;
/* 216 */           this.responseCallback.onResponse(RealCall.this, response);
/*     */         } 
/* 218 */       } catch (IOException e) {
/* 219 */         e = RealCall.this.timeoutExit(e);
/* 220 */         if (signalledCallback) {
/*     */           
/* 222 */           Platform.get().log(4, "Callback failure for " + RealCall.this.toLoggableString(), e);
/*     */         } else {
/* 224 */           RealCall.this.eventListener.callFailed(RealCall.this, e);
/* 225 */           this.responseCallback.onFailure(RealCall.this, e);
/*     */         } 
/*     */       } finally {
/* 228 */         RealCall.this.client.dispatcher().finished(this);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String toLoggableString() {
/* 238 */     return (isCanceled() ? "canceled " : "") + (
/* 239 */       this.forWebSocket ? "web socket" : "call") + " to " + 
/* 240 */       redactedUrl();
/*     */   }
/*     */   
/*     */   String redactedUrl() {
/* 244 */     return this.originalRequest.url().redact();
/*     */   }
/*     */ 
/*     */   
/*     */   Response getResponseWithInterceptorChain() throws IOException {
/* 249 */     List<Interceptor> interceptors = new ArrayList<>();
/* 250 */     interceptors.addAll(this.client.interceptors());
/* 251 */     interceptors.add(this.retryAndFollowUpInterceptor);
/* 252 */     interceptors.add(new BridgeInterceptor(this.client.cookieJar()));
/* 253 */     interceptors.add(new CacheInterceptor(this.client.internalCache()));
/* 254 */     interceptors.add(new ConnectInterceptor(this.client));
/* 255 */     if (!this.forWebSocket) {
/* 256 */       interceptors.addAll(this.client.networkInterceptors());
/*     */     }
/* 258 */     interceptors.add(new CallServerInterceptor(this.forWebSocket));
/*     */ 
/*     */ 
/*     */     
/* 262 */     RealInterceptorChain realInterceptorChain = new RealInterceptorChain(interceptors, null, null, null, 0, this.originalRequest, this, this.eventListener, this.client.connectTimeoutMillis(), this.client.readTimeoutMillis(), this.client.writeTimeoutMillis());
/*     */     
/* 264 */     return realInterceptorChain.proceed(this.originalRequest);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\RealCall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */