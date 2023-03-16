/*     */ package okhttp3;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.Flushable;
/*     */ import java.io.IOException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.annotation.Nullable;
/*     */ import okhttp3.internal.Util;
/*     */ import okhttp3.internal.cache.CacheRequest;
/*     */ import okhttp3.internal.cache.CacheStrategy;
/*     */ import okhttp3.internal.cache.DiskLruCache;
/*     */ import okhttp3.internal.cache.InternalCache;
/*     */ import okhttp3.internal.http.HttpHeaders;
/*     */ import okhttp3.internal.http.HttpMethod;
/*     */ import okhttp3.internal.http.StatusLine;
/*     */ import okhttp3.internal.io.FileSystem;
/*     */ import okhttp3.internal.platform.Platform;
/*     */ import okio.Buffer;
/*     */ import okio.BufferedSink;
/*     */ import okio.BufferedSource;
/*     */ import okio.ByteString;
/*     */ import okio.ForwardingSink;
/*     */ import okio.ForwardingSource;
/*     */ import okio.Okio;
/*     */ import okio.Sink;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Cache
/*     */   implements Closeable, Flushable
/*     */ {
/*     */   private static final int VERSION = 201105;
/*     */   private static final int ENTRY_METADATA = 0;
/*     */   private static final int ENTRY_BODY = 1;
/*     */   private static final int ENTRY_COUNT = 2;
/*     */   
/* 143 */   final InternalCache internalCache = new InternalCache() { @Nullable
/*     */       public Response get(Request request) throws IOException {
/* 145 */         return Cache.this.get(request);
/*     */       }
/*     */       @Nullable
/*     */       public CacheRequest put(Response response) throws IOException {
/* 149 */         return Cache.this.put(response);
/*     */       }
/*     */       
/*     */       public void remove(Request request) throws IOException {
/* 153 */         Cache.this.remove(request);
/*     */       }
/*     */       
/*     */       public void update(Response cached, Response network) {
/* 157 */         Cache.this.update(cached, network);
/*     */       }
/*     */       
/*     */       public void trackConditionalCacheHit() {
/* 161 */         Cache.this.trackConditionalCacheHit();
/*     */       }
/*     */       
/*     */       public void trackResponse(CacheStrategy cacheStrategy) {
/* 165 */         Cache.this.trackResponse(cacheStrategy);
/*     */       } }
/*     */   ;
/*     */ 
/*     */   
/*     */   final DiskLruCache cache;
/*     */   
/*     */   int writeSuccessCount;
/*     */   
/*     */   int writeAbortCount;
/*     */   
/*     */   private int networkCount;
/*     */   
/*     */   private int hitCount;
/*     */   private int requestCount;
/*     */   
/*     */   public Cache(File directory, long maxSize) {
/* 182 */     this(directory, maxSize, FileSystem.SYSTEM);
/*     */   }
/*     */   
/*     */   Cache(File directory, long maxSize, FileSystem fileSystem) {
/* 186 */     this.cache = DiskLruCache.create(fileSystem, directory, 201105, 2, maxSize);
/*     */   }
/*     */   
/*     */   public static String key(HttpUrl url) {
/* 190 */     return ByteString.encodeUtf8(url.toString()).md5().hex(); } @Nullable
/*     */   Response get(Request request) {
/*     */     DiskLruCache.Snapshot snapshot;
/*     */     Entry entry;
/* 194 */     String key = key(request.url());
/*     */ 
/*     */     
/*     */     try {
/* 198 */       snapshot = this.cache.get(key);
/* 199 */       if (snapshot == null) {
/* 200 */         return null;
/*     */       }
/* 202 */     } catch (IOException e) {
/*     */       
/* 204 */       return null;
/*     */     } 
/*     */     
/*     */     try {
/* 208 */       entry = new Entry(snapshot.getSource(0));
/* 209 */     } catch (IOException e) {
/* 210 */       Util.closeQuietly((Closeable)snapshot);
/* 211 */       return null;
/*     */     } 
/*     */     
/* 214 */     Response response = entry.response(snapshot);
/*     */     
/* 216 */     if (!entry.matches(request, response)) {
/* 217 */       Util.closeQuietly(response.body());
/* 218 */       return null;
/*     */     } 
/*     */     
/* 221 */     return response;
/*     */   }
/*     */   @Nullable
/*     */   CacheRequest put(Response response) {
/* 225 */     String requestMethod = response.request().method();
/*     */     
/* 227 */     if (HttpMethod.invalidatesCache(response.request().method())) {
/*     */       try {
/* 229 */         remove(response.request());
/* 230 */       } catch (IOException iOException) {}
/*     */ 
/*     */       
/* 233 */       return null;
/*     */     } 
/* 235 */     if (!requestMethod.equals("GET"))
/*     */     {
/*     */ 
/*     */       
/* 239 */       return null;
/*     */     }
/*     */     
/* 242 */     if (HttpHeaders.hasVaryAll(response)) {
/* 243 */       return null;
/*     */     }
/*     */     
/* 246 */     Entry entry = new Entry(response);
/* 247 */     DiskLruCache.Editor editor = null;
/*     */     try {
/* 249 */       editor = this.cache.edit(key(response.request().url()));
/* 250 */       if (editor == null) {
/* 251 */         return null;
/*     */       }
/* 253 */       entry.writeTo(editor);
/* 254 */       return new CacheRequestImpl(editor);
/* 255 */     } catch (IOException e) {
/* 256 */       abortQuietly(editor);
/* 257 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   void remove(Request request) throws IOException {
/* 262 */     this.cache.remove(key(request.url()));
/*     */   }
/*     */   
/*     */   void update(Response cached, Response network) {
/* 266 */     Entry entry = new Entry(network);
/* 267 */     DiskLruCache.Snapshot snapshot = ((CacheResponseBody)cached.body()).snapshot;
/* 268 */     DiskLruCache.Editor editor = null;
/*     */     try {
/* 270 */       editor = snapshot.edit();
/* 271 */       if (editor != null) {
/* 272 */         entry.writeTo(editor);
/* 273 */         editor.commit();
/*     */       } 
/* 275 */     } catch (IOException e) {
/* 276 */       abortQuietly(editor);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void abortQuietly(@Nullable DiskLruCache.Editor editor) {
/*     */     try {
/* 283 */       if (editor != null) {
/* 284 */         editor.abort();
/*     */       }
/* 286 */     } catch (IOException iOException) {}
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
/*     */   public void initialize() throws IOException {
/* 302 */     this.cache.initialize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete() throws IOException {
/* 310 */     this.cache.delete();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void evictAll() throws IOException {
/* 318 */     this.cache.evictAll();
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
/*     */   public Iterator<String> urls() throws IOException {
/* 331 */     return new Iterator<String>() {
/* 332 */         final Iterator<DiskLruCache.Snapshot> delegate = Cache.this.cache.snapshots();
/*     */         @Nullable
/*     */         String nextUrl;
/*     */         boolean canRemove;
/*     */         
/*     */         public boolean hasNext() {
/* 338 */           if (this.nextUrl != null) return true;
/*     */           
/* 340 */           this.canRemove = false;
/* 341 */           while (this.delegate.hasNext()) {
/* 342 */             try (DiskLruCache.Snapshot snapshot = (DiskLruCache.Snapshot)this.delegate.next()) {
/* 343 */               BufferedSource metadata = Okio.buffer(snapshot.getSource(0));
/* 344 */               this.nextUrl = metadata.readUtf8LineStrict();
/* 345 */               return true;
/* 346 */             } catch (IOException iOException) {}
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 352 */           return false;
/*     */         }
/*     */         
/*     */         public String next() {
/* 356 */           if (!hasNext()) throw new NoSuchElementException(); 
/* 357 */           String result = this.nextUrl;
/* 358 */           this.nextUrl = null;
/* 359 */           this.canRemove = true;
/* 360 */           return result;
/*     */         }
/*     */         
/*     */         public void remove() {
/* 364 */           if (!this.canRemove) throw new IllegalStateException("remove() before next()"); 
/* 365 */           this.delegate.remove();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public synchronized int writeAbortCount() {
/* 371 */     return this.writeAbortCount;
/*     */   }
/*     */   
/*     */   public synchronized int writeSuccessCount() {
/* 375 */     return this.writeSuccessCount;
/*     */   }
/*     */   
/*     */   public long size() throws IOException {
/* 379 */     return this.cache.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public long maxSize() {
/* 384 */     return this.cache.getMaxSize();
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/* 388 */     this.cache.flush();
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 392 */     this.cache.close();
/*     */   }
/*     */   
/*     */   public File directory() {
/* 396 */     return this.cache.getDirectory();
/*     */   }
/*     */   
/*     */   public boolean isClosed() {
/* 400 */     return this.cache.isClosed();
/*     */   }
/*     */   
/*     */   synchronized void trackResponse(CacheStrategy cacheStrategy) {
/* 404 */     this.requestCount++;
/*     */     
/* 406 */     if (cacheStrategy.networkRequest != null) {
/*     */       
/* 408 */       this.networkCount++;
/* 409 */     } else if (cacheStrategy.cacheResponse != null) {
/*     */       
/* 411 */       this.hitCount++;
/*     */     } 
/*     */   }
/*     */   
/*     */   synchronized void trackConditionalCacheHit() {
/* 416 */     this.hitCount++;
/*     */   }
/*     */   
/*     */   public synchronized int networkCount() {
/* 420 */     return this.networkCount;
/*     */   }
/*     */   
/*     */   public synchronized int hitCount() {
/* 424 */     return this.hitCount;
/*     */   }
/*     */   
/*     */   public synchronized int requestCount() {
/* 428 */     return this.requestCount;
/*     */   }
/*     */   
/*     */   private final class CacheRequestImpl implements CacheRequest {
/*     */     private final DiskLruCache.Editor editor;
/*     */     private Sink cacheOut;
/*     */     private Sink body;
/*     */     boolean done;
/*     */     
/*     */     CacheRequestImpl(final DiskLruCache.Editor editor) {
/* 438 */       this.editor = editor;
/* 439 */       this.cacheOut = editor.newSink(1);
/* 440 */       this.body = (Sink)new ForwardingSink(this.cacheOut) {
/*     */           public void close() throws IOException {
/* 442 */             synchronized (Cache.this) {
/* 443 */               if (Cache.CacheRequestImpl.this.done) {
/*     */                 return;
/*     */               }
/* 446 */               Cache.CacheRequestImpl.this.done = true;
/* 447 */               Cache.this.writeSuccessCount++;
/*     */             } 
/* 449 */             super.close();
/* 450 */             editor.commit();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public void abort() {
/* 456 */       synchronized (Cache.this) {
/* 457 */         if (this.done) {
/*     */           return;
/*     */         }
/* 460 */         this.done = true;
/* 461 */         Cache.this.writeAbortCount++;
/*     */       } 
/* 463 */       Util.closeQuietly((Closeable)this.cacheOut);
/*     */       try {
/* 465 */         this.editor.abort();
/* 466 */       } catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */     
/*     */     public Sink body() {
/* 471 */       return this.body;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Entry
/*     */   {
/* 477 */     private static final String SENT_MILLIS = Platform.get().getPrefix() + "-Sent-Millis";
/*     */ 
/*     */     
/* 480 */     private static final String RECEIVED_MILLIS = Platform.get().getPrefix() + "-Received-Millis";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String url;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Headers varyHeaders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String requestMethod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Protocol protocol;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int code;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String message;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Headers responseHeaders;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private final Handshake handshake;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final long sentRequestMillis;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final long receivedResponseMillis;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Entry(Source in) throws IOException {
/*     */       try {
/* 543 */         BufferedSource source = Okio.buffer(in);
/* 544 */         this.url = source.readUtf8LineStrict();
/* 545 */         this.requestMethod = source.readUtf8LineStrict();
/* 546 */         Headers.Builder varyHeadersBuilder = new Headers.Builder();
/* 547 */         int varyRequestHeaderLineCount = Cache.readInt(source);
/* 548 */         for (int i = 0; i < varyRequestHeaderLineCount; i++) {
/* 549 */           varyHeadersBuilder.addLenient(source.readUtf8LineStrict());
/*     */         }
/* 551 */         this.varyHeaders = varyHeadersBuilder.build();
/*     */         
/* 553 */         StatusLine statusLine = StatusLine.parse(source.readUtf8LineStrict());
/* 554 */         this.protocol = statusLine.protocol;
/* 555 */         this.code = statusLine.code;
/* 556 */         this.message = statusLine.message;
/* 557 */         Headers.Builder responseHeadersBuilder = new Headers.Builder();
/* 558 */         int responseHeaderLineCount = Cache.readInt(source);
/* 559 */         for (int j = 0; j < responseHeaderLineCount; j++) {
/* 560 */           responseHeadersBuilder.addLenient(source.readUtf8LineStrict());
/*     */         }
/* 562 */         String sendRequestMillisString = responseHeadersBuilder.get(SENT_MILLIS);
/* 563 */         String receivedResponseMillisString = responseHeadersBuilder.get(RECEIVED_MILLIS);
/* 564 */         responseHeadersBuilder.removeAll(SENT_MILLIS);
/* 565 */         responseHeadersBuilder.removeAll(RECEIVED_MILLIS);
/* 566 */         this
/*     */           
/* 568 */           .sentRequestMillis = (sendRequestMillisString != null) ? Long.parseLong(sendRequestMillisString) : 0L;
/* 569 */         this
/*     */           
/* 571 */           .receivedResponseMillis = (receivedResponseMillisString != null) ? Long.parseLong(receivedResponseMillisString) : 0L;
/* 572 */         this.responseHeaders = responseHeadersBuilder.build();
/*     */         
/* 574 */         if (isHttps()) {
/* 575 */           String blank = source.readUtf8LineStrict();
/* 576 */           if (blank.length() > 0) {
/* 577 */             throw new IOException("expected \"\" but was \"" + blank + "\"");
/*     */           }
/* 579 */           String cipherSuiteString = source.readUtf8LineStrict();
/* 580 */           CipherSuite cipherSuite = CipherSuite.forJavaName(cipherSuiteString);
/* 581 */           List<Certificate> peerCertificates = readCertificateList(source);
/* 582 */           List<Certificate> localCertificates = readCertificateList(source);
/*     */ 
/*     */           
/* 585 */           TlsVersion tlsVersion = !source.exhausted() ? TlsVersion.forJavaName(source.readUtf8LineStrict()) : TlsVersion.SSL_3_0;
/* 586 */           this.handshake = Handshake.get(tlsVersion, cipherSuite, peerCertificates, localCertificates);
/*     */         } else {
/* 588 */           this.handshake = null;
/*     */         } 
/*     */       } finally {
/* 591 */         in.close();
/*     */       } 
/*     */     }
/*     */     
/*     */     Entry(Response response) {
/* 596 */       this.url = response.request().url().toString();
/* 597 */       this.varyHeaders = HttpHeaders.varyHeaders(response);
/* 598 */       this.requestMethod = response.request().method();
/* 599 */       this.protocol = response.protocol();
/* 600 */       this.code = response.code();
/* 601 */       this.message = response.message();
/* 602 */       this.responseHeaders = response.headers();
/* 603 */       this.handshake = response.handshake();
/* 604 */       this.sentRequestMillis = response.sentRequestAtMillis();
/* 605 */       this.receivedResponseMillis = response.receivedResponseAtMillis();
/*     */     }
/*     */     
/*     */     public void writeTo(DiskLruCache.Editor editor) throws IOException {
/* 609 */       BufferedSink sink = Okio.buffer(editor.newSink(0));
/*     */       
/* 611 */       sink.writeUtf8(this.url)
/* 612 */         .writeByte(10);
/* 613 */       sink.writeUtf8(this.requestMethod)
/* 614 */         .writeByte(10);
/* 615 */       sink.writeDecimalLong(this.varyHeaders.size())
/* 616 */         .writeByte(10); int i, size;
/* 617 */       for (i = 0, size = this.varyHeaders.size(); i < size; i++) {
/* 618 */         sink.writeUtf8(this.varyHeaders.name(i))
/* 619 */           .writeUtf8(": ")
/* 620 */           .writeUtf8(this.varyHeaders.value(i))
/* 621 */           .writeByte(10);
/*     */       }
/*     */       
/* 624 */       sink.writeUtf8((new StatusLine(this.protocol, this.code, this.message)).toString())
/* 625 */         .writeByte(10);
/* 626 */       sink.writeDecimalLong((this.responseHeaders.size() + 2))
/* 627 */         .writeByte(10);
/* 628 */       for (i = 0, size = this.responseHeaders.size(); i < size; i++) {
/* 629 */         sink.writeUtf8(this.responseHeaders.name(i))
/* 630 */           .writeUtf8(": ")
/* 631 */           .writeUtf8(this.responseHeaders.value(i))
/* 632 */           .writeByte(10);
/*     */       }
/* 634 */       sink.writeUtf8(SENT_MILLIS)
/* 635 */         .writeUtf8(": ")
/* 636 */         .writeDecimalLong(this.sentRequestMillis)
/* 637 */         .writeByte(10);
/* 638 */       sink.writeUtf8(RECEIVED_MILLIS)
/* 639 */         .writeUtf8(": ")
/* 640 */         .writeDecimalLong(this.receivedResponseMillis)
/* 641 */         .writeByte(10);
/*     */       
/* 643 */       if (isHttps()) {
/* 644 */         sink.writeByte(10);
/* 645 */         sink.writeUtf8(this.handshake.cipherSuite().javaName())
/* 646 */           .writeByte(10);
/* 647 */         writeCertList(sink, this.handshake.peerCertificates());
/* 648 */         writeCertList(sink, this.handshake.localCertificates());
/* 649 */         sink.writeUtf8(this.handshake.tlsVersion().javaName()).writeByte(10);
/*     */       } 
/* 651 */       sink.close();
/*     */     }
/*     */     
/*     */     private boolean isHttps() {
/* 655 */       return this.url.startsWith("https://");
/*     */     }
/*     */     
/*     */     private List<Certificate> readCertificateList(BufferedSource source) throws IOException {
/* 659 */       int length = Cache.readInt(source);
/* 660 */       if (length == -1) return Collections.emptyList();
/*     */       
/*     */       try {
/* 663 */         CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
/* 664 */         List<Certificate> result = new ArrayList<>(length);
/* 665 */         for (int i = 0; i < length; i++) {
/* 666 */           String line = source.readUtf8LineStrict();
/* 667 */           Buffer bytes = new Buffer();
/* 668 */           bytes.write(ByteString.decodeBase64(line));
/* 669 */           result.add(certificateFactory.generateCertificate(bytes.inputStream()));
/*     */         } 
/* 671 */         return result;
/* 672 */       } catch (CertificateException e) {
/* 673 */         throw new IOException(e.getMessage());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void writeCertList(BufferedSink sink, List<Certificate> certificates) throws IOException {
/*     */       try {
/* 680 */         sink.writeDecimalLong(certificates.size())
/* 681 */           .writeByte(10);
/* 682 */         for (int i = 0, size = certificates.size(); i < size; i++) {
/* 683 */           byte[] bytes = ((Certificate)certificates.get(i)).getEncoded();
/* 684 */           String line = ByteString.of(bytes).base64();
/* 685 */           sink.writeUtf8(line)
/* 686 */             .writeByte(10);
/*     */         } 
/* 688 */       } catch (CertificateEncodingException e) {
/* 689 */         throw new IOException(e.getMessage());
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean matches(Request request, Response response) {
/* 694 */       return (this.url.equals(request.url().toString()) && this.requestMethod
/* 695 */         .equals(request.method()) && 
/* 696 */         HttpHeaders.varyMatches(response, this.varyHeaders, request));
/*     */     }
/*     */     
/*     */     public Response response(DiskLruCache.Snapshot snapshot) {
/* 700 */       String contentType = this.responseHeaders.get("Content-Type");
/* 701 */       String contentLength = this.responseHeaders.get("Content-Length");
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 706 */       Request cacheRequest = (new Request.Builder()).url(this.url).method(this.requestMethod, null).headers(this.varyHeaders).build();
/* 707 */       return (new Response.Builder())
/* 708 */         .request(cacheRequest)
/* 709 */         .protocol(this.protocol)
/* 710 */         .code(this.code)
/* 711 */         .message(this.message)
/* 712 */         .headers(this.responseHeaders)
/* 713 */         .body(new Cache.CacheResponseBody(snapshot, contentType, contentLength))
/* 714 */         .handshake(this.handshake)
/* 715 */         .sentRequestAtMillis(this.sentRequestMillis)
/* 716 */         .receivedResponseAtMillis(this.receivedResponseMillis)
/* 717 */         .build();
/*     */     }
/*     */   }
/*     */   
/*     */   static int readInt(BufferedSource source) throws IOException {
/*     */     try {
/* 723 */       long result = source.readDecimalLong();
/* 724 */       String line = source.readUtf8LineStrict();
/* 725 */       if (result < 0L || result > 2147483647L || !line.isEmpty()) {
/* 726 */         throw new IOException("expected an int but was \"" + result + line + "\"");
/*     */       }
/* 728 */       return (int)result;
/* 729 */     } catch (NumberFormatException e) {
/* 730 */       throw new IOException(e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class CacheResponseBody extends ResponseBody { final DiskLruCache.Snapshot snapshot;
/*     */     private final BufferedSource bodySource;
/*     */     @Nullable
/*     */     private final String contentType;
/*     */     @Nullable
/*     */     private final String contentLength;
/*     */     
/*     */     CacheResponseBody(final DiskLruCache.Snapshot snapshot, String contentType, String contentLength) {
/* 742 */       this.snapshot = snapshot;
/* 743 */       this.contentType = contentType;
/* 744 */       this.contentLength = contentLength;
/*     */       
/* 746 */       Source source = snapshot.getSource(1);
/* 747 */       this.bodySource = Okio.buffer((Source)new ForwardingSource(source) {
/*     */             public void close() throws IOException {
/* 749 */               snapshot.close();
/* 750 */               super.close();
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public MediaType contentType() {
/* 756 */       return (this.contentType != null) ? MediaType.parse(this.contentType) : null;
/*     */     }
/*     */     
/*     */     public long contentLength() {
/*     */       try {
/* 761 */         return (this.contentLength != null) ? Long.parseLong(this.contentLength) : -1L;
/* 762 */       } catch (NumberFormatException e) {
/* 763 */         return -1L;
/*     */       } 
/*     */     }
/*     */     
/*     */     public BufferedSource source() {
/* 768 */       return this.bodySource;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\Cache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */