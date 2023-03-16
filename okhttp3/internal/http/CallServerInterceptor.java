/*     */ package okhttp3.internal.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.ProtocolException;
/*     */ import okhttp3.Call;
/*     */ import okhttp3.Interceptor;
/*     */ import okhttp3.Request;
/*     */ import okhttp3.Response;
/*     */ import okhttp3.internal.Internal;
/*     */ import okhttp3.internal.Util;
/*     */ import okhttp3.internal.connection.RealConnection;
/*     */ import okhttp3.internal.connection.StreamAllocation;
/*     */ import okio.Buffer;
/*     */ import okio.BufferedSink;
/*     */ import okio.ForwardingSink;
/*     */ import okio.Okio;
/*     */ import okio.Sink;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CallServerInterceptor
/*     */   implements Interceptor
/*     */ {
/*     */   private final boolean forWebSocket;
/*     */   
/*     */   public CallServerInterceptor(boolean forWebSocket) {
/*  40 */     this.forWebSocket = forWebSocket;
/*     */   }
/*     */   
/*     */   public Response intercept(Interceptor.Chain chain) throws IOException {
/*  44 */     RealInterceptorChain realChain = (RealInterceptorChain)chain;
/*  45 */     Call call = realChain.call();
/*  46 */     HttpCodec httpCodec = realChain.httpStream();
/*  47 */     StreamAllocation streamAllocation = realChain.streamAllocation();
/*  48 */     RealConnection connection = (RealConnection)realChain.connection();
/*  49 */     Request request = realChain.request();
/*     */     
/*  51 */     long sentRequestMillis = System.currentTimeMillis();
/*     */     
/*  53 */     realChain.eventListener().requestHeadersStart(call);
/*  54 */     httpCodec.writeRequestHeaders(request);
/*  55 */     realChain.eventListener().requestHeadersEnd(call, request);
/*     */     
/*  57 */     Response.Builder responseBuilder = null;
/*  58 */     if (HttpMethod.permitsRequestBody(request.method()) && request.body() != null) {
/*     */ 
/*     */ 
/*     */       
/*  62 */       if ("100-continue".equalsIgnoreCase(request.header("Expect"))) {
/*  63 */         httpCodec.flushRequest();
/*  64 */         realChain.eventListener().responseHeadersStart(call);
/*  65 */         responseBuilder = httpCodec.readResponseHeaders(true);
/*     */       } 
/*     */       
/*  68 */       if (responseBuilder == null) {
/*  69 */         if (request.body() instanceof okhttp3.internal.duplex.DuplexRequestBody) {
/*     */           
/*  71 */           httpCodec.flushRequest();
/*  72 */           CountingSink requestBodyOut = new CountingSink(httpCodec.createRequestBody(request, -1L));
/*  73 */           BufferedSink bufferedRequestBody = Okio.buffer((Sink)requestBodyOut);
/*  74 */           request.body().writeTo(bufferedRequestBody);
/*     */         } else {
/*     */           
/*  77 */           realChain.eventListener().requestBodyStart(call);
/*  78 */           long contentLength = request.body().contentLength();
/*     */           
/*  80 */           CountingSink requestBodyOut = new CountingSink(httpCodec.createRequestBody(request, contentLength));
/*  81 */           BufferedSink bufferedRequestBody = Okio.buffer((Sink)requestBodyOut);
/*     */           
/*  83 */           request.body().writeTo(bufferedRequestBody);
/*  84 */           bufferedRequestBody.close();
/*  85 */           realChain.eventListener().requestBodyEnd(call, requestBodyOut.successfulCount);
/*     */         } 
/*  87 */       } else if (!connection.isMultiplexed()) {
/*     */ 
/*     */ 
/*     */         
/*  91 */         streamAllocation.noNewStreams();
/*     */       } 
/*     */     } 
/*     */     
/*  95 */     if (!(request.body() instanceof okhttp3.internal.duplex.DuplexRequestBody)) {
/*  96 */       httpCodec.finishRequest();
/*     */     }
/*     */     
/*  99 */     if (responseBuilder == null) {
/* 100 */       realChain.eventListener().responseHeadersStart(call);
/* 101 */       responseBuilder = httpCodec.readResponseHeaders(false);
/*     */     } 
/*     */     
/* 104 */     responseBuilder
/* 105 */       .request(request)
/* 106 */       .handshake(streamAllocation.connection().handshake())
/* 107 */       .sentRequestAtMillis(sentRequestMillis)
/* 108 */       .receivedResponseAtMillis(System.currentTimeMillis());
/* 109 */     Internal.instance.initCodec(responseBuilder, httpCodec);
/* 110 */     Response response = responseBuilder.build();
/*     */     
/* 112 */     int code = response.code();
/* 113 */     if (code == 100) {
/*     */ 
/*     */       
/* 116 */       responseBuilder = httpCodec.readResponseHeaders(false);
/*     */       
/* 118 */       responseBuilder
/* 119 */         .request(request)
/* 120 */         .handshake(streamAllocation.connection().handshake())
/* 121 */         .sentRequestAtMillis(sentRequestMillis)
/* 122 */         .receivedResponseAtMillis(System.currentTimeMillis());
/* 123 */       Internal.instance.initCodec(responseBuilder, httpCodec);
/* 124 */       response = responseBuilder.build();
/*     */       
/* 126 */       code = response.code();
/*     */     } 
/*     */     
/* 129 */     realChain.eventListener().responseHeadersEnd(call, response);
/*     */     
/* 131 */     if (this.forWebSocket && code == 101) {
/*     */ 
/*     */ 
/*     */       
/* 135 */       response = response.newBuilder().body(Util.EMPTY_RESPONSE).build();
/*     */     }
/*     */     else {
/*     */       
/* 139 */       response = response.newBuilder().body(httpCodec.openResponseBody(response)).build();
/*     */     } 
/*     */     
/* 142 */     if ("close".equalsIgnoreCase(response.request().header("Connection")) || "close"
/* 143 */       .equalsIgnoreCase(response.header("Connection"))) {
/* 144 */       streamAllocation.noNewStreams();
/*     */     }
/*     */     
/* 147 */     if ((code == 204 || code == 205) && response.body().contentLength() > 0L) {
/* 148 */       throw new ProtocolException("HTTP " + code + " had non-zero Content-Length: " + response
/* 149 */           .body().contentLength());
/*     */     }
/*     */     
/* 152 */     return response;
/*     */   }
/*     */   
/*     */   static final class CountingSink extends ForwardingSink {
/*     */     long successfulCount;
/*     */     
/*     */     CountingSink(Sink delegate) {
/* 159 */       super(delegate);
/*     */     }
/*     */     
/*     */     public void write(Buffer source, long byteCount) throws IOException {
/* 163 */       super.write(source, byteCount);
/* 164 */       this.successfulCount += byteCount;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\http\CallServerInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */