/*     */ package okhttp3.internal.http1;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.net.ProtocolException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import okhttp3.Headers;
/*     */ import okhttp3.HttpUrl;
/*     */ import okhttp3.OkHttpClient;
/*     */ import okhttp3.Request;
/*     */ import okhttp3.Response;
/*     */ import okhttp3.ResponseBody;
/*     */ import okhttp3.internal.Internal;
/*     */ import okhttp3.internal.Util;
/*     */ import okhttp3.internal.connection.RealConnection;
/*     */ import okhttp3.internal.connection.StreamAllocation;
/*     */ import okhttp3.internal.http.HttpCodec;
/*     */ import okhttp3.internal.http.HttpHeaders;
/*     */ import okhttp3.internal.http.RealResponseBody;
/*     */ import okhttp3.internal.http.RequestLine;
/*     */ import okhttp3.internal.http.StatusLine;
/*     */ import okio.Buffer;
/*     */ import okio.BufferedSink;
/*     */ import okio.BufferedSource;
/*     */ import okio.ForwardingTimeout;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Http1Codec
/*     */   implements HttpCodec
/*     */ {
/*     */   private static final int STATE_IDLE = 0;
/*     */   private static final int STATE_OPEN_REQUEST_BODY = 1;
/*     */   private static final int STATE_WRITING_REQUEST_BODY = 2;
/*     */   private static final int STATE_READ_RESPONSE_HEADERS = 3;
/*     */   private static final int STATE_OPEN_RESPONSE_BODY = 4;
/*     */   private static final int STATE_READING_RESPONSE_BODY = 5;
/*     */   private static final int STATE_CLOSED = 6;
/*     */   private static final int HEADER_LIMIT = 262144;
/*     */   final OkHttpClient client;
/*     */   final StreamAllocation streamAllocation;
/*     */   final BufferedSource source;
/*     */   final BufferedSink sink;
/*  86 */   int state = 0;
/*  87 */   private long headerLimit = 262144L;
/*     */ 
/*     */ 
/*     */   
/*     */   private Headers trailers;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http1Codec(OkHttpClient client, StreamAllocation streamAllocation, BufferedSource source, BufferedSink sink) {
/*  97 */     this.client = client;
/*  98 */     this.streamAllocation = streamAllocation;
/*  99 */     this.source = source;
/* 100 */     this.sink = sink;
/*     */   }
/*     */   
/*     */   public Sink createRequestBody(Request request, long contentLength) {
/* 104 */     if ("chunked".equalsIgnoreCase(request.header("Transfer-Encoding")))
/*     */     {
/* 106 */       return newChunkedSink();
/*     */     }
/*     */     
/* 109 */     if (contentLength != -1L)
/*     */     {
/* 111 */       return newFixedLengthSink(contentLength);
/*     */     }
/*     */     
/* 114 */     throw new IllegalStateException("Cannot stream a request body without chunked encoding or a known content length!");
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 119 */     RealConnection connection = this.streamAllocation.connection();
/* 120 */     if (connection != null) connection.cancel();
/*     */   
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
/*     */   public void writeRequestHeaders(Request request) throws IOException {
/* 134 */     String requestLine = RequestLine.get(request, this.streamAllocation
/* 135 */         .connection().route().proxy().type());
/* 136 */     writeRequest(request.headers(), requestLine);
/*     */   }
/*     */   
/*     */   public ResponseBody openResponseBody(Response response) throws IOException {
/* 140 */     this.streamAllocation.eventListener.responseBodyStart(this.streamAllocation.call);
/* 141 */     String contentType = response.header("Content-Type");
/*     */     
/* 143 */     if (!HttpHeaders.hasBody(response)) {
/* 144 */       Source source = newFixedLengthSource(0L);
/* 145 */       return (ResponseBody)new RealResponseBody(contentType, 0L, Okio.buffer(source));
/*     */     } 
/*     */     
/* 148 */     if ("chunked".equalsIgnoreCase(response.header("Transfer-Encoding"))) {
/* 149 */       Source source = newChunkedSource(response.request().url());
/* 150 */       return (ResponseBody)new RealResponseBody(contentType, -1L, Okio.buffer(source));
/*     */     } 
/*     */     
/* 153 */     long contentLength = HttpHeaders.contentLength(response);
/* 154 */     if (contentLength != -1L) {
/* 155 */       Source source = newFixedLengthSource(contentLength);
/* 156 */       return (ResponseBody)new RealResponseBody(contentType, contentLength, Okio.buffer(source));
/*     */     } 
/*     */     
/* 159 */     return (ResponseBody)new RealResponseBody(contentType, -1L, Okio.buffer(newUnknownLengthSource()));
/*     */   }
/*     */   
/*     */   public Headers trailers() throws IOException {
/* 163 */     if (this.state != 6) {
/* 164 */       throw new IllegalStateException("too early; can't read the trailers yet");
/*     */     }
/* 166 */     return (this.trailers != null) ? this.trailers : Util.EMPTY_HEADERS;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/* 171 */     return (this.state == 6);
/*     */   }
/*     */   
/*     */   public void flushRequest() throws IOException {
/* 175 */     this.sink.flush();
/*     */   }
/*     */   
/*     */   public void finishRequest() throws IOException {
/* 179 */     this.sink.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeRequest(Headers headers, String requestLine) throws IOException {
/* 184 */     if (this.state != 0) throw new IllegalStateException("state: " + this.state); 
/* 185 */     this.sink.writeUtf8(requestLine).writeUtf8("\r\n");
/* 186 */     for (int i = 0, size = headers.size(); i < size; i++) {
/* 187 */       this.sink.writeUtf8(headers.name(i))
/* 188 */         .writeUtf8(": ")
/* 189 */         .writeUtf8(headers.value(i))
/* 190 */         .writeUtf8("\r\n");
/*     */     }
/* 192 */     this.sink.writeUtf8("\r\n");
/* 193 */     this.state = 1;
/*     */   }
/*     */   
/*     */   public Response.Builder readResponseHeaders(boolean expectContinue) throws IOException {
/* 197 */     if (this.state != 1 && this.state != 3) {
/* 198 */       throw new IllegalStateException("state: " + this.state);
/*     */     }
/*     */     
/*     */     try {
/* 202 */       StatusLine statusLine = StatusLine.parse(readHeaderLine());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 208 */       Response.Builder responseBuilder = (new Response.Builder()).protocol(statusLine.protocol).code(statusLine.code).message(statusLine.message).headers(readHeaders());
/*     */       
/* 210 */       if (expectContinue && statusLine.code == 100)
/* 211 */         return null; 
/* 212 */       if (statusLine.code == 100) {
/* 213 */         this.state = 3;
/* 214 */         return responseBuilder;
/*     */       } 
/*     */       
/* 217 */       this.state = 4;
/* 218 */       return responseBuilder;
/* 219 */     } catch (EOFException e) {
/*     */       
/* 221 */       throw new IOException("unexpected end of stream on " + this.streamAllocation, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String readHeaderLine() throws IOException {
/* 226 */     String line = this.source.readUtf8LineStrict(this.headerLimit);
/* 227 */     this.headerLimit -= line.length();
/* 228 */     return line;
/*     */   }
/*     */ 
/*     */   
/*     */   public Headers readHeaders() throws IOException {
/* 233 */     Headers.Builder headers = new Headers.Builder();
/*     */     String line;
/* 235 */     while ((line = readHeaderLine()).length() != 0) {
/* 236 */       Internal.instance.addLenient(headers, line);
/*     */     }
/* 238 */     return headers.build();
/*     */   }
/*     */   
/*     */   public Sink newChunkedSink() {
/* 242 */     if (this.state != 1) throw new IllegalStateException("state: " + this.state); 
/* 243 */     this.state = 2;
/* 244 */     return new ChunkedSink();
/*     */   }
/*     */   
/*     */   public Sink newFixedLengthSink(long contentLength) {
/* 248 */     if (this.state != 1) throw new IllegalStateException("state: " + this.state); 
/* 249 */     this.state = 2;
/* 250 */     return new FixedLengthSink(contentLength);
/*     */   }
/*     */   
/*     */   public Source newFixedLengthSource(long length) throws IOException {
/* 254 */     if (this.state != 4) throw new IllegalStateException("state: " + this.state); 
/* 255 */     this.state = 5;
/* 256 */     return new FixedLengthSource(length);
/*     */   }
/*     */   
/*     */   public Source newChunkedSource(HttpUrl url) throws IOException {
/* 260 */     if (this.state != 4) throw new IllegalStateException("state: " + this.state); 
/* 261 */     this.state = 5;
/* 262 */     return new ChunkedSource(url);
/*     */   }
/*     */   
/*     */   public Source newUnknownLengthSource() throws IOException {
/* 266 */     if (this.state != 4) throw new IllegalStateException("state: " + this.state); 
/* 267 */     if (this.streamAllocation == null) throw new IllegalStateException("streamAllocation == null"); 
/* 268 */     this.state = 5;
/* 269 */     this.streamAllocation.noNewStreams();
/* 270 */     return new UnknownLengthSource();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void detachTimeout(ForwardingTimeout timeout) {
/* 279 */     Timeout oldDelegate = timeout.delegate();
/* 280 */     timeout.setDelegate(Timeout.NONE);
/* 281 */     oldDelegate.clearDeadline();
/* 282 */     oldDelegate.clearTimeout();
/*     */   }
/*     */   
/*     */   private final class FixedLengthSink
/*     */     implements Sink {
/* 287 */     private final ForwardingTimeout timeout = new ForwardingTimeout(Http1Codec.this.sink.timeout());
/*     */     private boolean closed;
/*     */     private long bytesRemaining;
/*     */     
/*     */     FixedLengthSink(long bytesRemaining) {
/* 292 */       this.bytesRemaining = bytesRemaining;
/*     */     }
/*     */     
/*     */     public Timeout timeout() {
/* 296 */       return (Timeout)this.timeout;
/*     */     }
/*     */     
/*     */     public void write(Buffer source, long byteCount) throws IOException {
/* 300 */       if (this.closed) throw new IllegalStateException("closed"); 
/* 301 */       Util.checkOffsetAndCount(source.size(), 0L, byteCount);
/* 302 */       if (byteCount > this.bytesRemaining) {
/* 303 */         throw new ProtocolException("expected " + this.bytesRemaining + " bytes but received " + byteCount);
/*     */       }
/*     */       
/* 306 */       Http1Codec.this.sink.write(source, byteCount);
/* 307 */       this.bytesRemaining -= byteCount;
/*     */     }
/*     */     
/*     */     public void flush() throws IOException {
/* 311 */       if (this.closed)
/* 312 */         return;  Http1Codec.this.sink.flush();
/*     */     }
/*     */     
/*     */     public void close() throws IOException {
/* 316 */       if (this.closed)
/* 317 */         return;  this.closed = true;
/* 318 */       if (this.bytesRemaining > 0L) throw new ProtocolException("unexpected end of stream"); 
/* 319 */       Http1Codec.this.detachTimeout(this.timeout);
/* 320 */       Http1Codec.this.state = 3;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class ChunkedSink
/*     */     implements Sink
/*     */   {
/* 329 */     private final ForwardingTimeout timeout = new ForwardingTimeout(Http1Codec.this.sink.timeout());
/*     */ 
/*     */     
/*     */     private boolean closed;
/*     */ 
/*     */     
/*     */     public Timeout timeout() {
/* 336 */       return (Timeout)this.timeout;
/*     */     }
/*     */     
/*     */     public void write(Buffer source, long byteCount) throws IOException {
/* 340 */       if (this.closed) throw new IllegalStateException("closed"); 
/* 341 */       if (byteCount == 0L)
/*     */         return; 
/* 343 */       Http1Codec.this.sink.writeHexadecimalUnsignedLong(byteCount);
/* 344 */       Http1Codec.this.sink.writeUtf8("\r\n");
/* 345 */       Http1Codec.this.sink.write(source, byteCount);
/* 346 */       Http1Codec.this.sink.writeUtf8("\r\n");
/*     */     }
/*     */     
/*     */     public synchronized void flush() throws IOException {
/* 350 */       if (this.closed)
/* 351 */         return;  Http1Codec.this.sink.flush();
/*     */     }
/*     */     
/*     */     public synchronized void close() throws IOException {
/* 355 */       if (this.closed)
/* 356 */         return;  this.closed = true;
/* 357 */       Http1Codec.this.sink.writeUtf8("0\r\n\r\n");
/* 358 */       Http1Codec.this.detachTimeout(this.timeout);
/* 359 */       Http1Codec.this.state = 3;
/*     */     }
/*     */   }
/*     */   
/*     */   private abstract class AbstractSource implements Source {
/* 364 */     protected final ForwardingTimeout timeout = new ForwardingTimeout(Http1Codec.this.source.timeout());
/*     */     protected boolean closed;
/* 366 */     protected long bytesRead = 0L;
/*     */     
/*     */     public Timeout timeout() {
/* 369 */       return (Timeout)this.timeout;
/*     */     }
/*     */     
/*     */     public long read(Buffer sink, long byteCount) throws IOException {
/*     */       try {
/* 374 */         long read = Http1Codec.this.source.read(sink, byteCount);
/* 375 */         if (read > 0L) {
/* 376 */           this.bytesRead += read;
/*     */         }
/* 378 */         return read;
/* 379 */       } catch (IOException e) {
/* 380 */         endOfInput(false, e);
/* 381 */         throw e;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected final void endOfInput(boolean reuseConnection, IOException e) throws IOException {
/* 390 */       if (Http1Codec.this.state == 6)
/* 391 */         return;  if (Http1Codec.this.state != 5) throw new IllegalStateException("state: " + Http1Codec.this.state);
/*     */       
/* 393 */       Http1Codec.this.detachTimeout(this.timeout);
/*     */       
/* 395 */       Http1Codec.this.state = 6;
/* 396 */       if (Http1Codec.this.streamAllocation != null)
/* 397 */         Http1Codec.this.streamAllocation.streamFinished(!reuseConnection, Http1Codec.this, this.bytesRead, e); 
/*     */     }
/*     */     
/*     */     private AbstractSource() {}
/*     */   }
/*     */   
/*     */   private class FixedLengthSource extends AbstractSource {
/*     */     private long bytesRemaining;
/*     */     
/*     */     FixedLengthSource(long length) throws IOException {
/* 407 */       this.bytesRemaining = length;
/* 408 */       if (this.bytesRemaining == 0L) {
/* 409 */         endOfInput(true, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public long read(Buffer sink, long byteCount) throws IOException {
/* 414 */       if (byteCount < 0L) throw new IllegalArgumentException("byteCount < 0: " + byteCount); 
/* 415 */       if (this.closed) throw new IllegalStateException("closed"); 
/* 416 */       if (this.bytesRemaining == 0L) return -1L;
/*     */       
/* 418 */       long read = super.read(sink, Math.min(this.bytesRemaining, byteCount));
/* 419 */       if (read == -1L) {
/* 420 */         ProtocolException e = new ProtocolException("unexpected end of stream");
/* 421 */         endOfInput(false, e);
/* 422 */         throw e;
/*     */       } 
/*     */       
/* 425 */       this.bytesRemaining -= read;
/* 426 */       if (this.bytesRemaining == 0L) {
/* 427 */         endOfInput(true, null);
/*     */       }
/* 429 */       return read;
/*     */     }
/*     */     
/*     */     public void close() throws IOException {
/* 433 */       if (this.closed)
/*     */         return; 
/* 435 */       if (this.bytesRemaining != 0L && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
/* 436 */         endOfInput(false, null);
/*     */       }
/*     */       
/* 439 */       this.closed = true;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ChunkedSource
/*     */     extends AbstractSource {
/*     */     private static final long NO_CHUNK_YET = -1L;
/*     */     private final HttpUrl url;
/* 447 */     private long bytesRemainingInChunk = -1L;
/*     */     private boolean hasMoreChunks = true;
/*     */     
/*     */     ChunkedSource(HttpUrl url) {
/* 451 */       this.url = url;
/*     */     }
/*     */     
/*     */     public long read(Buffer sink, long byteCount) throws IOException {
/* 455 */       if (byteCount < 0L) throw new IllegalArgumentException("byteCount < 0: " + byteCount); 
/* 456 */       if (this.closed) throw new IllegalStateException("closed"); 
/* 457 */       if (!this.hasMoreChunks) return -1L;
/*     */       
/* 459 */       if (this.bytesRemainingInChunk == 0L || this.bytesRemainingInChunk == -1L) {
/* 460 */         readChunkSize();
/* 461 */         if (!this.hasMoreChunks) return -1L;
/*     */       
/*     */       } 
/* 464 */       long read = super.read(sink, Math.min(byteCount, this.bytesRemainingInChunk));
/* 465 */       if (read == -1L) {
/* 466 */         ProtocolException e = new ProtocolException("unexpected end of stream");
/* 467 */         endOfInput(false, e);
/* 468 */         throw e;
/*     */       } 
/* 470 */       this.bytesRemainingInChunk -= read;
/* 471 */       return read;
/*     */     }
/*     */ 
/*     */     
/*     */     private void readChunkSize() throws IOException {
/* 476 */       if (this.bytesRemainingInChunk != -1L) {
/* 477 */         Http1Codec.this.source.readUtf8LineStrict();
/*     */       }
/*     */       try {
/* 480 */         this.bytesRemainingInChunk = Http1Codec.this.source.readHexadecimalUnsignedLong();
/* 481 */         String extensions = Http1Codec.this.source.readUtf8LineStrict().trim();
/* 482 */         if (this.bytesRemainingInChunk < 0L || (!extensions.isEmpty() && !extensions.startsWith(";"))) {
/* 483 */           throw new ProtocolException("expected chunk size and optional extensions but was \"" + this.bytesRemainingInChunk + extensions + "\"");
/*     */         }
/*     */       }
/* 486 */       catch (NumberFormatException e) {
/* 487 */         throw new ProtocolException(e.getMessage());
/*     */       } 
/* 489 */       if (this.bytesRemainingInChunk == 0L) {
/* 490 */         this.hasMoreChunks = false;
/* 491 */         Http1Codec.this.trailers = Http1Codec.this.readHeaders();
/* 492 */         HttpHeaders.receiveHeaders(Http1Codec.this.client.cookieJar(), this.url, Http1Codec.this.trailers);
/* 493 */         endOfInput(true, null);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void close() throws IOException {
/* 498 */       if (this.closed)
/* 499 */         return;  if (this.hasMoreChunks && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
/* 500 */         endOfInput(false, null);
/*     */       }
/* 502 */       this.closed = true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class UnknownLengthSource
/*     */     extends AbstractSource
/*     */   {
/*     */     private boolean inputExhausted;
/*     */ 
/*     */     
/*     */     public long read(Buffer sink, long byteCount) throws IOException {
/* 515 */       if (byteCount < 0L) throw new IllegalArgumentException("byteCount < 0: " + byteCount); 
/* 516 */       if (this.closed) throw new IllegalStateException("closed"); 
/* 517 */       if (this.inputExhausted) return -1L;
/*     */       
/* 519 */       long read = super.read(sink, byteCount);
/* 520 */       if (read == -1L) {
/* 521 */         this.inputExhausted = true;
/* 522 */         endOfInput(true, null);
/* 523 */         return -1L;
/*     */       } 
/* 525 */       return read;
/*     */     }
/*     */     
/*     */     public void close() throws IOException {
/* 529 */       if (this.closed)
/* 530 */         return;  if (!this.inputExhausted) {
/* 531 */         endOfInput(false, null);
/*     */       }
/* 533 */       this.closed = true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\http1\Http1Codec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */