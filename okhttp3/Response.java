/*     */ package okhttp3;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import okhttp3.internal.http.HttpCodec;
/*     */ import okhttp3.internal.http.HttpHeaders;
/*     */ import okio.Buffer;
/*     */ import okio.BufferedSource;
/*     */ import okio.Source;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Response
/*     */   implements Closeable
/*     */ {
/*     */   final Request request;
/*     */   final Protocol protocol;
/*     */   final int code;
/*     */   final String message;
/*     */   @Nullable
/*     */   final Handshake handshake;
/*     */   final Headers headers;
/*     */   @Nullable
/*     */   final ResponseBody body;
/*     */   @Nullable
/*     */   final Response networkResponse;
/*     */   @Nullable
/*     */   final Response cacheResponse;
/*     */   @Nullable
/*     */   final Response priorResponse;
/*     */   final long sentRequestAtMillis;
/*     */   final long receivedResponseAtMillis;
/*     */   @Nullable
/*     */   final HttpCodec httpCodec;
/*     */   @Nullable
/*     */   private volatile CacheControl cacheControl;
/*     */   
/*     */   Response(Builder builder) {
/*  62 */     this.request = builder.request;
/*  63 */     this.protocol = builder.protocol;
/*  64 */     this.code = builder.code;
/*  65 */     this.message = builder.message;
/*  66 */     this.handshake = builder.handshake;
/*  67 */     this.headers = builder.headers.build();
/*  68 */     this.body = builder.body;
/*  69 */     this.networkResponse = builder.networkResponse;
/*  70 */     this.cacheResponse = builder.cacheResponse;
/*  71 */     this.priorResponse = builder.priorResponse;
/*  72 */     this.sentRequestAtMillis = builder.sentRequestAtMillis;
/*  73 */     this.receivedResponseAtMillis = builder.receivedResponseAtMillis;
/*  74 */     this.httpCodec = builder.httpCodec;
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
/*     */   public Request request() {
/*  89 */     return this.request;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Protocol protocol() {
/*  96 */     return this.protocol;
/*     */   }
/*     */ 
/*     */   
/*     */   public int code() {
/* 101 */     return this.code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSuccessful() {
/* 109 */     return (this.code >= 200 && this.code < 300);
/*     */   }
/*     */ 
/*     */   
/*     */   public String message() {
/* 114 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Handshake handshake() {
/* 122 */     return this.handshake;
/*     */   }
/*     */   
/*     */   public List<String> headers(String name) {
/* 126 */     return this.headers.values(name);
/*     */   }
/*     */   @Nullable
/*     */   public String header(String name) {
/* 130 */     return header(name, null);
/*     */   }
/*     */   @Nullable
/*     */   public String header(String name, @Nullable String defaultValue) {
/* 134 */     String result = this.headers.get(name);
/* 135 */     return (result != null) ? result : defaultValue;
/*     */   }
/*     */   
/*     */   public Headers headers() {
/* 139 */     return this.headers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Headers trailers() throws IOException {
/* 147 */     return this.httpCodec.trailers();
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
/*     */   public ResponseBody peekBody(long byteCount) throws IOException {
/* 162 */     BufferedSource peeked = this.body.source().peek();
/* 163 */     Buffer buffer = new Buffer();
/* 164 */     peeked.request(byteCount);
/* 165 */     buffer.write((Source)peeked, Math.min(byteCount, peeked.getBuffer().size()));
/* 166 */     return ResponseBody.create(this.body.contentType(), buffer.size(), (BufferedSource)buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ResponseBody body() {
/* 178 */     return this.body;
/*     */   }
/*     */   
/*     */   public Builder newBuilder() {
/* 182 */     return new Builder(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRedirect() {
/* 187 */     switch (this.code) {
/*     */       case 300:
/*     */       case 301:
/*     */       case 302:
/*     */       case 303:
/*     */       case 307:
/*     */       case 308:
/* 194 */         return true;
/*     */     } 
/* 196 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Response networkResponse() {
/* 206 */     return this.networkResponse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Response cacheResponse() {
/* 215 */     return this.cacheResponse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Response priorResponse() {
/* 225 */     return this.priorResponse;
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
/*     */   public List<Challenge> challenges() {
/*     */     String responseField;
/* 241 */     if (this.code == 401) {
/* 242 */       responseField = "WWW-Authenticate";
/* 243 */     } else if (this.code == 407) {
/* 244 */       responseField = "Proxy-Authenticate";
/*     */     } else {
/* 246 */       return Collections.emptyList();
/*     */     } 
/* 248 */     return HttpHeaders.parseChallenges(headers(), responseField);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheControl cacheControl() {
/* 256 */     CacheControl result = this.cacheControl;
/* 257 */     return (result != null) ? result : (this.cacheControl = CacheControl.parse(this.headers));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long sentRequestAtMillis() {
/* 266 */     return this.sentRequestAtMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long receivedResponseAtMillis() {
/* 275 */     return this.receivedResponseAtMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 286 */     if (this.body == null) {
/* 287 */       throw new IllegalStateException("response is not eligible for a body and must not be closed");
/*     */     }
/* 289 */     this.body.close();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 293 */     return "Response{protocol=" + this.protocol + ", code=" + this.code + ", message=" + this.message + ", url=" + this.request
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 300 */       .url() + '}';
/*     */   }
/*     */   
/*     */   public static class Builder { @Nullable
/*     */     Request request;
/*     */     @Nullable
/*     */     Protocol protocol;
/* 307 */     int code = -1;
/*     */     
/*     */     String message;
/*     */     
/*     */     @Nullable
/*     */     Handshake handshake;
/*     */     Headers.Builder headers;
/*     */     @Nullable
/*     */     ResponseBody body;
/*     */     @Nullable
/*     */     Response networkResponse;
/*     */     
/*     */     public Builder() {
/* 320 */       this.headers = new Headers.Builder(); } @Nullable
/*     */     Response cacheResponse; @Nullable
/*     */     Response priorResponse; long sentRequestAtMillis; long receivedResponseAtMillis; @Nullable
/*     */     HttpCodec httpCodec; Builder(Response response) {
/* 324 */       this.request = response.request;
/* 325 */       this.protocol = response.protocol;
/* 326 */       this.code = response.code;
/* 327 */       this.message = response.message;
/* 328 */       this.handshake = response.handshake;
/* 329 */       this.headers = response.headers.newBuilder();
/* 330 */       this.body = response.body;
/* 331 */       this.networkResponse = response.networkResponse;
/* 332 */       this.cacheResponse = response.cacheResponse;
/* 333 */       this.priorResponse = response.priorResponse;
/* 334 */       this.sentRequestAtMillis = response.sentRequestAtMillis;
/* 335 */       this.receivedResponseAtMillis = response.receivedResponseAtMillis;
/* 336 */       this.httpCodec = response.httpCodec;
/*     */     }
/*     */     
/*     */     public Builder request(Request request) {
/* 340 */       this.request = request;
/* 341 */       return this;
/*     */     }
/*     */     
/*     */     public Builder protocol(Protocol protocol) {
/* 345 */       this.protocol = protocol;
/* 346 */       return this;
/*     */     }
/*     */     
/*     */     public Builder code(int code) {
/* 350 */       this.code = code;
/* 351 */       return this;
/*     */     }
/*     */     
/*     */     public Builder message(String message) {
/* 355 */       this.message = message;
/* 356 */       return this;
/*     */     }
/*     */     
/*     */     public Builder handshake(@Nullable Handshake handshake) {
/* 360 */       this.handshake = handshake;
/* 361 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder header(String name, String value) {
/* 369 */       this.headers.set(name, value);
/* 370 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addHeader(String name, String value) {
/* 378 */       this.headers.add(name, value);
/* 379 */       return this;
/*     */     }
/*     */     
/*     */     public Builder removeHeader(String name) {
/* 383 */       this.headers.removeAll(name);
/* 384 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder headers(Headers headers) {
/* 389 */       this.headers = headers.newBuilder();
/* 390 */       return this;
/*     */     }
/*     */     
/*     */     public Builder body(@Nullable ResponseBody body) {
/* 394 */       this.body = body;
/* 395 */       return this;
/*     */     }
/*     */     
/*     */     public Builder networkResponse(@Nullable Response networkResponse) {
/* 399 */       if (networkResponse != null) checkSupportResponse("networkResponse", networkResponse); 
/* 400 */       this.networkResponse = networkResponse;
/* 401 */       return this;
/*     */     }
/*     */     
/*     */     public Builder cacheResponse(@Nullable Response cacheResponse) {
/* 405 */       if (cacheResponse != null) checkSupportResponse("cacheResponse", cacheResponse); 
/* 406 */       this.cacheResponse = cacheResponse;
/* 407 */       return this;
/*     */     }
/*     */     
/*     */     private void checkSupportResponse(String name, Response response) {
/* 411 */       if (response.body != null)
/* 412 */         throw new IllegalArgumentException(name + ".body != null"); 
/* 413 */       if (response.networkResponse != null)
/* 414 */         throw new IllegalArgumentException(name + ".networkResponse != null"); 
/* 415 */       if (response.cacheResponse != null)
/* 416 */         throw new IllegalArgumentException(name + ".cacheResponse != null"); 
/* 417 */       if (response.priorResponse != null) {
/* 418 */         throw new IllegalArgumentException(name + ".priorResponse != null");
/*     */       }
/*     */     }
/*     */     
/*     */     public Builder priorResponse(@Nullable Response priorResponse) {
/* 423 */       if (priorResponse != null) checkPriorResponse(priorResponse); 
/* 424 */       this.priorResponse = priorResponse;
/* 425 */       return this;
/*     */     }
/*     */     
/*     */     private void checkPriorResponse(Response response) {
/* 429 */       if (response.body != null) {
/* 430 */         throw new IllegalArgumentException("priorResponse.body != null");
/*     */       }
/*     */     }
/*     */     
/*     */     public Builder sentRequestAtMillis(long sentRequestAtMillis) {
/* 435 */       this.sentRequestAtMillis = sentRequestAtMillis;
/* 436 */       return this;
/*     */     }
/*     */     
/*     */     public Builder receivedResponseAtMillis(long receivedResponseAtMillis) {
/* 440 */       this.receivedResponseAtMillis = receivedResponseAtMillis;
/* 441 */       return this;
/*     */     }
/*     */     
/*     */     void initCodec(HttpCodec httpCodec) {
/* 445 */       this.httpCodec = httpCodec;
/*     */     }
/*     */     
/*     */     public Response build() {
/* 449 */       if (this.request == null) throw new IllegalStateException("request == null"); 
/* 450 */       if (this.protocol == null) throw new IllegalStateException("protocol == null"); 
/* 451 */       if (this.code < 0) throw new IllegalStateException("code < 0: " + this.code); 
/* 452 */       if (this.message == null) throw new IllegalStateException("message == null"); 
/* 453 */       return new Response(this);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\Response.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */