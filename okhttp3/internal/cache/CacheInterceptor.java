/*     */ package okhttp3.internal.cache;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.annotation.Nullable;
/*     */ import okhttp3.Headers;
/*     */ import okhttp3.Interceptor;
/*     */ import okhttp3.Protocol;
/*     */ import okhttp3.Request;
/*     */ import okhttp3.Response;
/*     */ import okhttp3.ResponseBody;
/*     */ import okhttp3.internal.Internal;
/*     */ import okhttp3.internal.Util;
/*     */ import okhttp3.internal.http.HttpHeaders;
/*     */ import okhttp3.internal.http.HttpMethod;
/*     */ import okhttp3.internal.http.RealResponseBody;
/*     */ import okio.Buffer;
/*     */ import okio.BufferedSink;
/*     */ import okio.BufferedSource;
/*     */ import okio.Okio;
/*     */ import okio.Sink;
/*     */ import okio.Source;
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
/*     */ public final class CacheInterceptor
/*     */   implements Interceptor
/*     */ {
/*     */   @Nullable
/*     */   final InternalCache cache;
/*     */   
/*     */   public CacheInterceptor(@Nullable InternalCache cache) {
/*  50 */     this.cache = cache;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Response intercept(Interceptor.Chain chain) throws IOException {
/*  56 */     Response cacheCandidate = (this.cache != null) ? this.cache.get(chain.request()) : null;
/*     */     
/*  58 */     long now = System.currentTimeMillis();
/*     */     
/*  60 */     CacheStrategy strategy = (new CacheStrategy.Factory(now, chain.request(), cacheCandidate)).get();
/*  61 */     Request networkRequest = strategy.networkRequest;
/*  62 */     Response cacheResponse = strategy.cacheResponse;
/*     */     
/*  64 */     if (this.cache != null) {
/*  65 */       this.cache.trackResponse(strategy);
/*     */     }
/*     */     
/*  68 */     if (cacheCandidate != null && cacheResponse == null) {
/*  69 */       Util.closeQuietly((Closeable)cacheCandidate.body());
/*     */     }
/*     */ 
/*     */     
/*  73 */     if (networkRequest == null && cacheResponse == null) {
/*  74 */       return (new Response.Builder())
/*  75 */         .request(chain.request())
/*  76 */         .protocol(Protocol.HTTP_1_1)
/*  77 */         .code(504)
/*  78 */         .message("Unsatisfiable Request (only-if-cached)")
/*  79 */         .body(Util.EMPTY_RESPONSE)
/*  80 */         .sentRequestAtMillis(-1L)
/*  81 */         .receivedResponseAtMillis(System.currentTimeMillis())
/*  82 */         .build();
/*     */     }
/*     */ 
/*     */     
/*  86 */     if (networkRequest == null) {
/*  87 */       return cacheResponse.newBuilder()
/*  88 */         .cacheResponse(stripBody(cacheResponse))
/*  89 */         .build();
/*     */     }
/*     */     
/*  92 */     Response networkResponse = null;
/*     */     try {
/*  94 */       networkResponse = chain.proceed(networkRequest);
/*     */     } finally {
/*     */       
/*  97 */       if (networkResponse == null && cacheCandidate != null) {
/*  98 */         Util.closeQuietly((Closeable)cacheCandidate.body());
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 103 */     if (cacheResponse != null) {
/* 104 */       if (networkResponse.code() == 304) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 111 */         Response response1 = cacheResponse.newBuilder().headers(combine(cacheResponse.headers(), networkResponse.headers())).sentRequestAtMillis(networkResponse.sentRequestAtMillis()).receivedResponseAtMillis(networkResponse.receivedResponseAtMillis()).cacheResponse(stripBody(cacheResponse)).networkResponse(stripBody(networkResponse)).build();
/* 112 */         networkResponse.body().close();
/*     */ 
/*     */ 
/*     */         
/* 116 */         this.cache.trackConditionalCacheHit();
/* 117 */         this.cache.update(cacheResponse, response1);
/* 118 */         return response1;
/*     */       } 
/* 120 */       Util.closeQuietly((Closeable)cacheResponse.body());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 127 */     Response response = networkResponse.newBuilder().cacheResponse(stripBody(cacheResponse)).networkResponse(stripBody(networkResponse)).build();
/*     */     
/* 129 */     if (this.cache != null) {
/* 130 */       if (HttpHeaders.hasBody(response) && CacheStrategy.isCacheable(response, networkRequest)) {
/*     */         
/* 132 */         CacheRequest cacheRequest = this.cache.put(response);
/* 133 */         return cacheWritingResponse(cacheRequest, response);
/*     */       } 
/*     */       
/* 136 */       if (HttpMethod.invalidatesCache(networkRequest.method())) {
/*     */         try {
/* 138 */           this.cache.remove(networkRequest);
/* 139 */         } catch (IOException iOException) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 145 */     return response;
/*     */   }
/*     */   
/*     */   private static Response stripBody(Response response) {
/* 149 */     return (response != null && response.body() != null) ? 
/* 150 */       response.newBuilder().body(null).build() : 
/* 151 */       response;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Response cacheWritingResponse(final CacheRequest cacheRequest, Response response) throws IOException {
/* 162 */     if (cacheRequest == null) return response; 
/* 163 */     Sink cacheBodyUnbuffered = cacheRequest.body();
/* 164 */     if (cacheBodyUnbuffered == null) return response;
/*     */     
/* 166 */     final BufferedSource source = response.body().source();
/* 167 */     final BufferedSink cacheBody = Okio.buffer(cacheBodyUnbuffered);
/*     */     
/* 169 */     Source cacheWritingSource = new Source() {
/*     */         boolean cacheRequestClosed;
/*     */         
/*     */         public long read(Buffer sink, long byteCount) throws IOException {
/*     */           long bytesRead;
/*     */           try {
/* 175 */             bytesRead = source.read(sink, byteCount);
/* 176 */           } catch (IOException e) {
/* 177 */             if (!this.cacheRequestClosed) {
/* 178 */               this.cacheRequestClosed = true;
/* 179 */               cacheRequest.abort();
/*     */             } 
/* 181 */             throw e;
/*     */           } 
/*     */           
/* 184 */           if (bytesRead == -1L) {
/* 185 */             if (!this.cacheRequestClosed) {
/* 186 */               this.cacheRequestClosed = true;
/* 187 */               cacheBody.close();
/*     */             } 
/* 189 */             return -1L;
/*     */           } 
/*     */           
/* 192 */           sink.copyTo(cacheBody.buffer(), sink.size() - bytesRead, bytesRead);
/* 193 */           cacheBody.emitCompleteSegments();
/* 194 */           return bytesRead;
/*     */         }
/*     */         
/*     */         public Timeout timeout() {
/* 198 */           return source.timeout();
/*     */         }
/*     */         
/*     */         public void close() throws IOException {
/* 202 */           if (!this.cacheRequestClosed && 
/* 203 */             !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
/* 204 */             this.cacheRequestClosed = true;
/* 205 */             cacheRequest.abort();
/*     */           } 
/* 207 */           source.close();
/*     */         }
/*     */       };
/*     */     
/* 211 */     String contentType = response.header("Content-Type");
/* 212 */     long contentLength = response.body().contentLength();
/* 213 */     return response.newBuilder()
/* 214 */       .body((ResponseBody)new RealResponseBody(contentType, contentLength, Okio.buffer(cacheWritingSource)))
/* 215 */       .build();
/*     */   }
/*     */ 
/*     */   
/*     */   private static Headers combine(Headers cachedHeaders, Headers networkHeaders) {
/* 220 */     Headers.Builder result = new Headers.Builder();
/*     */     int i, size;
/* 222 */     for (i = 0, size = cachedHeaders.size(); i < size; i++) {
/* 223 */       String fieldName = cachedHeaders.name(i);
/* 224 */       String value = cachedHeaders.value(i);
/* 225 */       if (!"Warning".equalsIgnoreCase(fieldName) || !value.startsWith("1"))
/*     */       {
/*     */         
/* 228 */         if (isContentSpecificHeader(fieldName) || 
/* 229 */           !isEndToEnd(fieldName) || networkHeaders
/* 230 */           .get(fieldName) == null) {
/* 231 */           Internal.instance.addLenient(result, fieldName, value);
/*     */         }
/*     */       }
/*     */     } 
/* 235 */     for (i = 0, size = networkHeaders.size(); i < size; i++) {
/* 236 */       String fieldName = networkHeaders.name(i);
/* 237 */       if (!isContentSpecificHeader(fieldName) && isEndToEnd(fieldName)) {
/* 238 */         Internal.instance.addLenient(result, fieldName, networkHeaders.value(i));
/*     */       }
/*     */     } 
/*     */     
/* 242 */     return result.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isEndToEnd(String fieldName) {
/* 250 */     return (!"Connection".equalsIgnoreCase(fieldName) && 
/* 251 */       !"Keep-Alive".equalsIgnoreCase(fieldName) && 
/* 252 */       !"Proxy-Authenticate".equalsIgnoreCase(fieldName) && 
/* 253 */       !"Proxy-Authorization".equalsIgnoreCase(fieldName) && 
/* 254 */       !"TE".equalsIgnoreCase(fieldName) && 
/* 255 */       !"Trailers".equalsIgnoreCase(fieldName) && 
/* 256 */       !"Transfer-Encoding".equalsIgnoreCase(fieldName) && 
/* 257 */       !"Upgrade".equalsIgnoreCase(fieldName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isContentSpecificHeader(String fieldName) {
/* 265 */     return ("Content-Length".equalsIgnoreCase(fieldName) || "Content-Encoding"
/* 266 */       .equalsIgnoreCase(fieldName) || "Content-Type"
/* 267 */       .equalsIgnoreCase(fieldName));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\cache\CacheInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */