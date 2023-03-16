/*     */ package okhttp3.internal.http2;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import okhttp3.Headers;
/*     */ import okhttp3.internal.NamedRunnable;
/*     */ import okhttp3.internal.Util;
/*     */ import okhttp3.internal.platform.Platform;
/*     */ import okio.Buffer;
/*     */ import okio.BufferedSink;
/*     */ import okio.BufferedSource;
/*     */ import okio.ByteString;
/*     */ import okio.Okio;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Http2Connection
/*     */   implements Closeable
/*     */ {
/*     */   static final int OKHTTP_CLIENT_WINDOW_SIZE = 16777216;
/*  81 */   private static final ExecutorService listenerExecutor = new ThreadPoolExecutor(0, 2147483647, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), 
/*     */       
/*  83 */       Util.threadFactory("OkHttp Http2Connection", true));
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean client;
/*     */ 
/*     */   
/*     */   final Listener listener;
/*     */ 
/*     */   
/*  93 */   final Map<Integer, Http2Stream> streams = new LinkedHashMap<>();
/*     */ 
/*     */   
/*     */   final String connectionName;
/*     */ 
/*     */   
/*     */   int lastGoodStreamId;
/*     */ 
/*     */   
/*     */   int nextStreamId;
/*     */ 
/*     */   
/*     */   boolean shutdown;
/*     */ 
/*     */   
/*     */   private final ScheduledExecutorService writerExecutor;
/*     */   
/*     */   private final ExecutorService pushExecutor;
/*     */   
/*     */   final PushObserver pushObserver;
/*     */   
/*     */   private boolean awaitingPong;
/*     */   
/* 116 */   long unacknowledgedBytesRead = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long bytesLeftInWriteWindow;
/*     */ 
/*     */ 
/*     */   
/* 125 */   Settings okHttpSettings = new Settings();
/*     */ 
/*     */ 
/*     */   
/* 129 */   final Settings peerSettings = new Settings();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean receivedInitialPeerSettings = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final Socket socket;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final Http2Writer writer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final ReaderRunnable readerRunnable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final Set<Integer> currentPushRequests;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int openStreamCount() {
/* 181 */     return this.streams.size();
/*     */   }
/*     */   
/*     */   synchronized Http2Stream getStream(int id) {
/* 185 */     return this.streams.get(Integer.valueOf(id));
/*     */   }
/*     */   
/*     */   synchronized Http2Stream removeStream(int streamId) {
/* 189 */     Http2Stream stream = this.streams.remove(Integer.valueOf(streamId));
/* 190 */     notifyAll();
/* 191 */     return stream;
/*     */   }
/*     */   
/*     */   public synchronized int maxConcurrentStreams() {
/* 195 */     return this.peerSettings.getMaxConcurrentStreams(2147483647);
/*     */   }
/*     */   
/*     */   synchronized void updateConnectionFlowControl(long read) {
/* 199 */     this.unacknowledgedBytesRead += read;
/* 200 */     if (this.unacknowledgedBytesRead >= (this.okHttpSettings.getInitialWindowSize() / 2)) {
/* 201 */       writeWindowUpdateLater(0, this.unacknowledgedBytesRead);
/* 202 */       this.unacknowledgedBytesRead = 0L;
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
/*     */   public Http2Stream pushStream(int associatedStreamId, List<Header> requestHeaders, boolean out) throws IOException {
/* 215 */     if (this.client) throw new IllegalStateException("Client cannot push requests."); 
/* 216 */     return newStream(associatedStreamId, requestHeaders, out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2Stream newStream(List<Header> requestHeaders, boolean out) throws IOException {
/* 225 */     return newStream(0, requestHeaders, out);
/*     */   }
/*     */   private Http2Stream newStream(int associatedStreamId, List<Header> requestHeaders, boolean out) throws IOException {
/*     */     boolean flushHeaders;
/*     */     Http2Stream stream;
/* 230 */     boolean outFinished = !out;
/* 231 */     boolean inFinished = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 236 */     synchronized (this.writer) {
/* 237 */       int streamId; synchronized (this) {
/* 238 */         if (this.nextStreamId > 1073741823) {
/* 239 */           shutdown(ErrorCode.REFUSED_STREAM);
/*     */         }
/* 241 */         if (this.shutdown) {
/* 242 */           throw new ConnectionShutdownException();
/*     */         }
/* 244 */         streamId = this.nextStreamId;
/* 245 */         this.nextStreamId += 2;
/* 246 */         stream = new Http2Stream(streamId, this, outFinished, inFinished, null);
/* 247 */         flushHeaders = (!out || this.bytesLeftInWriteWindow == 0L || stream.bytesLeftInWriteWindow == 0L);
/* 248 */         if (stream.isOpen()) {
/* 249 */           this.streams.put(Integer.valueOf(streamId), stream);
/*     */         }
/*     */       } 
/* 252 */       if (associatedStreamId == 0)
/* 253 */       { this.writer.headers(outFinished, streamId, requestHeaders); }
/* 254 */       else { if (this.client) {
/* 255 */           throw new IllegalArgumentException("client streams shouldn't have associated stream IDs");
/*     */         }
/* 257 */         this.writer.pushPromise(associatedStreamId, streamId, requestHeaders); }
/*     */     
/*     */     } 
/*     */     
/* 261 */     if (flushHeaders) {
/* 262 */       this.writer.flush();
/*     */     }
/*     */     
/* 265 */     return stream;
/*     */   }
/*     */ 
/*     */   
/*     */   void writeHeaders(int streamId, boolean outFinished, List<Header> alternating) throws IOException {
/* 270 */     this.writer.headers(outFinished, streamId, alternating);
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
/*     */   public void writeData(int streamId, boolean outFinished, Buffer buffer, long byteCount) throws IOException {
/* 287 */     if (byteCount == 0L) {
/* 288 */       this.writer.data(outFinished, streamId, buffer, 0);
/*     */       
/*     */       return;
/*     */     } 
/* 292 */     while (byteCount > 0L) {
/*     */       int toWrite;
/* 294 */       synchronized (this) { while (true) {
/*     */           try {
/* 296 */             if (this.bytesLeftInWriteWindow <= 0L) {
/*     */ 
/*     */               
/* 299 */               if (!this.streams.containsKey(Integer.valueOf(streamId))) {
/* 300 */                 throw new IOException("stream closed");
/*     */               }
/* 302 */               wait(); continue;
/*     */             } 
/* 304 */           } catch (InterruptedException e) {
/* 305 */             Thread.currentThread().interrupt();
/* 306 */             throw new InterruptedIOException();
/*     */           }  break;
/*     */         } 
/* 309 */         toWrite = (int)Math.min(byteCount, this.bytesLeftInWriteWindow);
/* 310 */         toWrite = Math.min(toWrite, this.writer.maxDataLength());
/* 311 */         this.bytesLeftInWriteWindow -= toWrite; }
/*     */ 
/*     */       
/* 314 */       byteCount -= toWrite;
/* 315 */       this.writer.data((outFinished && byteCount == 0L), streamId, buffer, toWrite);
/*     */     } 
/*     */   }
/*     */   
/*     */   void writeSynResetLater(final int streamId, final ErrorCode errorCode) {
/*     */     try {
/* 321 */       this.writerExecutor.execute((Runnable)new NamedRunnable("OkHttp %s stream %d", new Object[] { this.connectionName, Integer.valueOf(streamId) }) {
/*     */             public void execute() {
/*     */               try {
/* 324 */                 Http2Connection.this.writeSynReset(streamId, errorCode);
/* 325 */               } catch (IOException e) {
/* 326 */                 Http2Connection.this.failConnection();
/*     */               } 
/*     */             }
/*     */           });
/* 330 */     } catch (RejectedExecutionException rejectedExecutionException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void writeSynReset(int streamId, ErrorCode statusCode) throws IOException {
/* 336 */     this.writer.rstStream(streamId, statusCode);
/*     */   }
/*     */   
/*     */   void writeWindowUpdateLater(final int streamId, final long unacknowledgedBytesRead) {
/*     */     try {
/* 341 */       this.writerExecutor.execute((Runnable)new NamedRunnable("OkHttp Window Update %s stream %d", new Object[] { this.connectionName, 
/* 342 */               Integer.valueOf(streamId) }) {
/*     */             public void execute() {
/*     */               try {
/* 345 */                 Http2Connection.this.writer.windowUpdate(streamId, unacknowledgedBytesRead);
/* 346 */               } catch (IOException e) {
/* 347 */                 Http2Connection.this.failConnection();
/*     */               } 
/*     */             }
/*     */           });
/* 351 */     } catch (RejectedExecutionException rejectedExecutionException) {}
/*     */   }
/*     */   
/*     */   final class PingRunnable
/*     */     extends NamedRunnable
/*     */   {
/*     */     final boolean reply;
/*     */     final int payload1;
/*     */     final int payload2;
/*     */     
/*     */     PingRunnable(boolean reply, int payload1, int payload2) {
/* 362 */       super("OkHttp %s ping %08x%08x", new Object[] { this$0.connectionName, Integer.valueOf(payload1), Integer.valueOf(payload2) });
/* 363 */       this.reply = reply;
/* 364 */       this.payload1 = payload1;
/* 365 */       this.payload2 = payload2;
/*     */     }
/*     */     
/*     */     public void execute() {
/* 369 */       Http2Connection.this.writePing(this.reply, this.payload1, this.payload2);
/*     */     }
/*     */   }
/*     */   
/*     */   void writePing(boolean reply, int payload1, int payload2) {
/* 374 */     if (!reply) {
/*     */       boolean failedDueToMissingPong;
/* 376 */       synchronized (this) {
/* 377 */         failedDueToMissingPong = this.awaitingPong;
/* 378 */         this.awaitingPong = true;
/*     */       } 
/* 380 */       if (failedDueToMissingPong) {
/* 381 */         failConnection();
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     try {
/* 387 */       this.writer.ping(reply, payload1, payload2);
/* 388 */     } catch (IOException e) {
/* 389 */       IOException iOException1; failConnection();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void writePingAndAwaitPong() throws InterruptedException {
/* 395 */     writePing(false, 1330343787, -257978967);
/* 396 */     awaitPong();
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized void awaitPong() throws InterruptedException {
/* 401 */     while (this.awaitingPong) {
/* 402 */       wait();
/*     */     }
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/* 407 */     this.writer.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown(ErrorCode statusCode) throws IOException {
/* 416 */     synchronized (this.writer) {
/*     */       int lastGoodStreamId;
/* 418 */       synchronized (this) {
/* 419 */         if (this.shutdown) {
/*     */           return;
/*     */         }
/* 422 */         this.shutdown = true;
/* 423 */         lastGoodStreamId = this.lastGoodStreamId;
/*     */       } 
/*     */ 
/*     */       
/* 427 */       this.writer.goAway(lastGoodStreamId, statusCode, Util.EMPTY_BYTE_ARRAY);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 436 */     close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
/*     */   }
/*     */   
/*     */   void close(ErrorCode connectionCode, ErrorCode streamCode) throws IOException {
/* 440 */     assert !Thread.holdsLock(this);
/* 441 */     IOException thrown = null;
/*     */     try {
/* 443 */       shutdown(connectionCode);
/* 444 */     } catch (IOException e) {
/* 445 */       thrown = e;
/*     */     } 
/*     */     
/* 448 */     Http2Stream[] streamsToClose = null;
/* 449 */     synchronized (this) {
/* 450 */       if (!this.streams.isEmpty()) {
/* 451 */         streamsToClose = (Http2Stream[])this.streams.values().toArray((Object[])new Http2Stream[this.streams.size()]);
/* 452 */         this.streams.clear();
/*     */       } 
/*     */     } 
/*     */     
/* 456 */     if (streamsToClose != null) {
/* 457 */       for (Http2Stream stream : streamsToClose) {
/*     */         try {
/* 459 */           stream.close(streamCode);
/* 460 */         } catch (IOException e) {
/* 461 */           if (thrown != null) thrown = e;
/*     */         
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     try {
/* 468 */       this.writer.close();
/* 469 */     } catch (IOException e) {
/* 470 */       if (thrown == null) thrown = e;
/*     */     
/*     */     } 
/*     */     
/*     */     try {
/* 475 */       this.socket.close();
/* 476 */     } catch (IOException e) {
/* 477 */       thrown = e;
/*     */     } 
/*     */ 
/*     */     
/* 481 */     this.writerExecutor.shutdown();
/* 482 */     this.pushExecutor.shutdown();
/*     */     
/* 484 */     if (thrown != null) throw thrown; 
/*     */   }
/*     */   
/*     */   private void failConnection() {
/*     */     try {
/* 489 */       close(ErrorCode.PROTOCOL_ERROR, ErrorCode.PROTOCOL_ERROR);
/* 490 */     } catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws IOException {
/* 499 */     start(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void start(boolean sendConnectionPreface) throws IOException {
/* 507 */     if (sendConnectionPreface) {
/* 508 */       this.writer.connectionPreface();
/* 509 */       this.writer.settings(this.okHttpSettings);
/* 510 */       int windowSize = this.okHttpSettings.getInitialWindowSize();
/* 511 */       if (windowSize != 65535) {
/* 512 */         this.writer.windowUpdate(0, (windowSize - 65535));
/*     */       }
/*     */     } 
/* 515 */     (new Thread((Runnable)this.readerRunnable)).start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSettings(Settings settings) throws IOException {
/* 520 */     synchronized (this.writer) {
/* 521 */       synchronized (this) {
/* 522 */         if (this.shutdown) {
/* 523 */           throw new ConnectionShutdownException();
/*     */         }
/* 525 */         this.okHttpSettings.merge(settings);
/*     */       } 
/* 527 */       this.writer.settings(settings);
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized boolean isShutdown() {
/* 532 */     return this.shutdown;
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     Socket socket;
/*     */     String connectionName;
/*     */     BufferedSource source;
/*     */     BufferedSink sink;
/* 540 */     Http2Connection.Listener listener = Http2Connection.Listener.REFUSE_INCOMING_STREAMS;
/* 541 */     PushObserver pushObserver = PushObserver.CANCEL;
/*     */ 
/*     */     
/*     */     boolean client;
/*     */     
/*     */     int pingIntervalMillis;
/*     */ 
/*     */     
/*     */     public Builder(boolean client) {
/* 550 */       this.client = client;
/*     */     }
/*     */     
/*     */     public Builder socket(Socket socket) throws IOException {
/* 554 */       SocketAddress remoteSocketAddress = socket.getRemoteSocketAddress();
/*     */ 
/*     */       
/* 557 */       String connectionName = (remoteSocketAddress instanceof InetSocketAddress) ? ((InetSocketAddress)remoteSocketAddress).getHostName() : remoteSocketAddress.toString();
/* 558 */       return socket(socket, connectionName, 
/* 559 */           Okio.buffer(Okio.source(socket)), Okio.buffer(Okio.sink(socket)));
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder socket(Socket socket, String connectionName, BufferedSource source, BufferedSink sink) {
/* 564 */       this.socket = socket;
/* 565 */       this.connectionName = connectionName;
/* 566 */       this.source = source;
/* 567 */       this.sink = sink;
/* 568 */       return this;
/*     */     }
/*     */     
/*     */     public Builder listener(Http2Connection.Listener listener) {
/* 572 */       this.listener = listener;
/* 573 */       return this;
/*     */     }
/*     */     
/*     */     public Builder pushObserver(PushObserver pushObserver) {
/* 577 */       this.pushObserver = pushObserver;
/* 578 */       return this;
/*     */     }
/*     */     
/*     */     public Builder pingIntervalMillis(int pingIntervalMillis) {
/* 582 */       this.pingIntervalMillis = pingIntervalMillis;
/* 583 */       return this;
/*     */     }
/*     */     
/*     */     public Http2Connection build() {
/* 587 */       return new Http2Connection(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   class ReaderRunnable
/*     */     extends NamedRunnable
/*     */     implements Http2Reader.Handler
/*     */   {
/*     */     final Http2Reader reader;
/*     */     
/*     */     ReaderRunnable(Http2Reader reader) {
/* 599 */       super("OkHttp %s", new Object[] { this$0.connectionName });
/* 600 */       this.reader = reader;
/*     */     }
/*     */     
/*     */     protected void execute() {
/* 604 */       ErrorCode connectionErrorCode = ErrorCode.INTERNAL_ERROR;
/* 605 */       ErrorCode streamErrorCode = ErrorCode.INTERNAL_ERROR;
/*     */       try {
/* 607 */         this.reader.readConnectionPreface(this);
/* 608 */         while (this.reader.nextFrame(false, this));
/*     */         
/* 610 */         connectionErrorCode = ErrorCode.NO_ERROR;
/* 611 */         streamErrorCode = ErrorCode.CANCEL;
/* 612 */       } catch (IOException e) {
/* 613 */         connectionErrorCode = ErrorCode.PROTOCOL_ERROR;
/* 614 */         streamErrorCode = ErrorCode.PROTOCOL_ERROR;
/*     */       } finally {
/*     */         try {
/* 617 */           Http2Connection.this.close(connectionErrorCode, streamErrorCode);
/* 618 */         } catch (IOException iOException) {}
/*     */         
/* 620 */         Util.closeQuietly(this.reader);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void data(boolean inFinished, int streamId, BufferedSource source, int length) throws IOException {
/* 626 */       if (Http2Connection.this.pushedStream(streamId)) {
/* 627 */         Http2Connection.this.pushDataLater(streamId, source, length, inFinished);
/*     */         return;
/*     */       } 
/* 630 */       Http2Stream dataStream = Http2Connection.this.getStream(streamId);
/* 631 */       if (dataStream == null) {
/* 632 */         Http2Connection.this.writeSynResetLater(streamId, ErrorCode.PROTOCOL_ERROR);
/* 633 */         Http2Connection.this.updateConnectionFlowControl(length);
/* 634 */         source.skip(length);
/*     */         return;
/*     */       } 
/* 637 */       dataStream.receiveData(source, length);
/* 638 */       if (inFinished) {
/* 639 */         dataStream.receiveHeaders(Util.EMPTY_HEADERS, true);
/*     */       }
/*     */     }
/*     */     
/*     */     public void headers(boolean inFinished, int streamId, int associatedStreamId, List<Header> headerBlock) {
/*     */       Http2Stream stream;
/* 645 */       if (Http2Connection.this.pushedStream(streamId)) {
/* 646 */         Http2Connection.this.pushHeadersLater(streamId, headerBlock, inFinished);
/*     */         
/*     */         return;
/*     */       } 
/* 650 */       synchronized (Http2Connection.this) {
/* 651 */         stream = Http2Connection.this.getStream(streamId);
/*     */         
/* 653 */         if (stream == null) {
/*     */           
/* 655 */           if (Http2Connection.this.shutdown) {
/*     */             return;
/*     */           }
/* 658 */           if (streamId <= Http2Connection.this.lastGoodStreamId) {
/*     */             return;
/*     */           }
/* 661 */           if (streamId % 2 == Http2Connection.this.nextStreamId % 2) {
/*     */             return;
/*     */           }
/* 664 */           Headers headers = Util.toHeaders(headerBlock);
/* 665 */           final Http2Stream newStream = new Http2Stream(streamId, Http2Connection.this, false, inFinished, headers);
/*     */           
/* 667 */           Http2Connection.this.lastGoodStreamId = streamId;
/* 668 */           Http2Connection.this.streams.put(Integer.valueOf(streamId), newStream);
/* 669 */           Http2Connection.listenerExecutor.execute((Runnable)new NamedRunnable("OkHttp %s stream %d", new Object[] { this.this$0.connectionName, 
/* 670 */                   Integer.valueOf(streamId) }) {
/*     */                 public void execute() {
/*     */                   try {
/* 673 */                     Http2Connection.this.listener.onStream(newStream);
/* 674 */                   } catch (IOException e) {
/* 675 */                     Platform.get().log(4, "Http2Connection.Listener failure for " + Http2Connection.this.connectionName, e);
/*     */                     
/*     */                     try {
/* 678 */                       newStream.close(ErrorCode.PROTOCOL_ERROR);
/* 679 */                     } catch (IOException iOException) {}
/*     */                   } 
/*     */                 }
/*     */               });
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */       
/* 689 */       stream.receiveHeaders(Util.toHeaders(headerBlock), inFinished);
/*     */     }
/*     */     
/*     */     public void rstStream(int streamId, ErrorCode errorCode) {
/* 693 */       if (Http2Connection.this.pushedStream(streamId)) {
/* 694 */         Http2Connection.this.pushResetLater(streamId, errorCode);
/*     */         return;
/*     */       } 
/* 697 */       Http2Stream rstStream = Http2Connection.this.removeStream(streamId);
/* 698 */       if (rstStream != null) {
/* 699 */         rstStream.receiveRstStream(errorCode);
/*     */       }
/*     */     }
/*     */     
/*     */     public void settings(boolean clearPrevious, Settings newSettings) {
/* 704 */       long delta = 0L;
/* 705 */       Http2Stream[] streamsToNotify = null;
/* 706 */       synchronized (Http2Connection.this) {
/* 707 */         int priorWriteWindowSize = Http2Connection.this.peerSettings.getInitialWindowSize();
/* 708 */         if (clearPrevious) Http2Connection.this.peerSettings.clear(); 
/* 709 */         Http2Connection.this.peerSettings.merge(newSettings);
/* 710 */         applyAndAckSettings(newSettings);
/* 711 */         int peerInitialWindowSize = Http2Connection.this.peerSettings.getInitialWindowSize();
/* 712 */         if (peerInitialWindowSize != -1 && peerInitialWindowSize != priorWriteWindowSize) {
/* 713 */           delta = (peerInitialWindowSize - priorWriteWindowSize);
/* 714 */           if (!Http2Connection.this.receivedInitialPeerSettings) {
/* 715 */             Http2Connection.this.receivedInitialPeerSettings = true;
/*     */           }
/* 717 */           if (!Http2Connection.this.streams.isEmpty()) {
/* 718 */             streamsToNotify = (Http2Stream[])Http2Connection.this.streams.values().toArray((Object[])new Http2Stream[Http2Connection.this.streams.size()]);
/*     */           }
/*     */         } 
/* 721 */         Http2Connection.listenerExecutor.execute((Runnable)new NamedRunnable("OkHttp %s settings", new Object[] { this.this$0.connectionName }) {
/*     */               public void execute() {
/* 723 */                 Http2Connection.this.listener.onSettings(Http2Connection.this);
/*     */               }
/*     */             });
/*     */       } 
/* 727 */       if (streamsToNotify != null && delta != 0L) {
/* 728 */         for (Http2Stream stream : streamsToNotify) {
/* 729 */           synchronized (stream) {
/* 730 */             stream.addBytesToWriteWindow(delta);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/*     */     private void applyAndAckSettings(final Settings peerSettings) {
/*     */       try {
/* 738 */         Http2Connection.this.writerExecutor.execute((Runnable)new NamedRunnable("OkHttp %s ACK Settings", new Object[] { this.this$0.connectionName }) {
/*     */               public void execute() {
/*     */                 try {
/* 741 */                   Http2Connection.this.writer.applyAndAckSettings(peerSettings);
/* 742 */                 } catch (IOException e) {
/* 743 */                   Http2Connection.this.failConnection();
/*     */                 } 
/*     */               }
/*     */             });
/* 747 */       } catch (RejectedExecutionException rejectedExecutionException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void ackSettings() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void ping(boolean reply, int payload1, int payload2) {
/* 757 */       if (reply) {
/* 758 */         synchronized (Http2Connection.this) {
/* 759 */           Http2Connection.this.awaitingPong = false;
/* 760 */           Http2Connection.this.notifyAll();
/*     */         } 
/*     */       } else {
/*     */         
/*     */         try {
/* 765 */           Http2Connection.this.writerExecutor.execute((Runnable)new Http2Connection.PingRunnable(true, payload1, payload2));
/* 766 */         } catch (RejectedExecutionException rejectedExecutionException) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void goAway(int lastGoodStreamId, ErrorCode errorCode, ByteString debugData) {
/*     */       Http2Stream[] streamsCopy;
/* 773 */       if (debugData.size() > 0);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 778 */       synchronized (Http2Connection.this) {
/* 779 */         streamsCopy = (Http2Stream[])Http2Connection.this.streams.values().toArray((Object[])new Http2Stream[Http2Connection.this.streams.size()]);
/* 780 */         Http2Connection.this.shutdown = true;
/*     */       } 
/*     */ 
/*     */       
/* 784 */       for (Http2Stream http2Stream : streamsCopy) {
/* 785 */         if (http2Stream.getId() > lastGoodStreamId && http2Stream.isLocallyInitiated()) {
/* 786 */           http2Stream.receiveRstStream(ErrorCode.REFUSED_STREAM);
/* 787 */           Http2Connection.this.removeStream(http2Stream.getId());
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void windowUpdate(int streamId, long windowSizeIncrement) {
/* 793 */       if (streamId == 0) {
/* 794 */         synchronized (Http2Connection.this) {
/* 795 */           Http2Connection.this.bytesLeftInWriteWindow += windowSizeIncrement;
/* 796 */           Http2Connection.this.notifyAll();
/*     */         } 
/*     */       } else {
/* 799 */         Http2Stream stream = Http2Connection.this.getStream(streamId);
/* 800 */         if (stream != null) {
/* 801 */           synchronized (stream) {
/* 802 */             stream.addBytesToWriteWindow(windowSizeIncrement);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void priority(int streamId, int streamDependency, int weight, boolean exclusive) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void pushPromise(int streamId, int promisedStreamId, List<Header> requestHeaders) {
/* 815 */       Http2Connection.this.pushRequestLater(promisedStreamId, requestHeaders);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void alternateService(int streamId, String origin, ByteString protocol, String host, int port, long maxAge) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean pushedStream(int streamId) {
/* 826 */     return (streamId != 0 && (streamId & 0x1) == 0);
/*     */   }
/*     */   
/*     */   Http2Connection(Builder builder) {
/* 830 */     this.currentPushRequests = new LinkedHashSet<>(); this.pushObserver = builder.pushObserver; this.client = builder.client; this.listener = builder.listener; this.nextStreamId = builder.client ? 1 : 2; if (builder.client)
/*     */       this.nextStreamId += 2;  if (builder.client)
/*     */       this.okHttpSettings.set(7, 16777216);  this.connectionName = builder.connectionName; this.writerExecutor = new ScheduledThreadPoolExecutor(1, Util.threadFactory(Util.format("OkHttp %s Writer", new Object[] { this.connectionName }), false)); if (builder.pingIntervalMillis != 0)
/* 833 */       this.writerExecutor.scheduleAtFixedRate((Runnable)new PingRunnable(false, 0, 0), builder.pingIntervalMillis, builder.pingIntervalMillis, TimeUnit.MILLISECONDS);  this.pushExecutor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), Util.threadFactory(Util.format("OkHttp %s Push Observer", new Object[] { this.connectionName }), true)); this.peerSettings.set(7, 65535); this.peerSettings.set(5, 16384); this.bytesLeftInWriteWindow = this.peerSettings.getInitialWindowSize(); this.socket = builder.socket; this.writer = new Http2Writer(builder.sink, this.client); this.readerRunnable = new ReaderRunnable(new Http2Reader(builder.source, this.client)); } void pushRequestLater(final int streamId, final List<Header> requestHeaders) { synchronized (this) {
/* 834 */       if (this.currentPushRequests.contains(Integer.valueOf(streamId))) {
/* 835 */         writeSynResetLater(streamId, ErrorCode.PROTOCOL_ERROR);
/*     */         return;
/*     */       } 
/* 838 */       this.currentPushRequests.add(Integer.valueOf(streamId));
/*     */     } 
/*     */     try {
/* 841 */       pushExecutorExecute(new NamedRunnable("OkHttp %s Push Request[%s]", new Object[] { this.connectionName, 
/* 842 */               Integer.valueOf(streamId) }) {
/*     */             public void execute() {
/* 844 */               boolean cancel = Http2Connection.this.pushObserver.onRequest(streamId, requestHeaders);
/*     */               try {
/* 846 */                 if (cancel) {
/* 847 */                   Http2Connection.this.writer.rstStream(streamId, ErrorCode.CANCEL);
/* 848 */                   synchronized (Http2Connection.this) {
/* 849 */                     Http2Connection.this.currentPushRequests.remove(Integer.valueOf(streamId));
/*     */                   } 
/*     */                 } 
/* 852 */               } catch (IOException iOException) {}
/*     */             }
/*     */           });
/*     */     }
/* 856 */     catch (RejectedExecutionException rejectedExecutionException) {} }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void pushHeadersLater(final int streamId, final List<Header> requestHeaders, final boolean inFinished) {
/*     */     try {
/* 864 */       pushExecutorExecute(new NamedRunnable("OkHttp %s Push Headers[%s]", new Object[] { this.connectionName, 
/* 865 */               Integer.valueOf(streamId) }) {
/*     */             public void execute() {
/* 867 */               boolean cancel = Http2Connection.this.pushObserver.onHeaders(streamId, requestHeaders, inFinished);
/*     */               try {
/* 869 */                 if (cancel) Http2Connection.this.writer.rstStream(streamId, ErrorCode.CANCEL); 
/* 870 */                 if (cancel || inFinished) {
/* 871 */                   synchronized (Http2Connection.this) {
/* 872 */                     Http2Connection.this.currentPushRequests.remove(Integer.valueOf(streamId));
/*     */                   } 
/*     */                 }
/* 875 */               } catch (IOException iOException) {}
/*     */             }
/*     */           });
/*     */     }
/* 879 */     catch (RejectedExecutionException rejectedExecutionException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void pushDataLater(final int streamId, BufferedSource source, final int byteCount, final boolean inFinished) throws IOException {
/* 890 */     final Buffer buffer = new Buffer();
/* 891 */     source.require(byteCount);
/* 892 */     source.read(buffer, byteCount);
/* 893 */     if (buffer.size() != byteCount) throw new IOException(buffer.size() + " != " + byteCount); 
/* 894 */     pushExecutorExecute(new NamedRunnable("OkHttp %s Push Data[%s]", new Object[] { this.connectionName, Integer.valueOf(streamId) }) {
/*     */           public void execute() {
/*     */             try {
/* 897 */               boolean cancel = Http2Connection.this.pushObserver.onData(streamId, (BufferedSource)buffer, byteCount, inFinished);
/* 898 */               if (cancel) Http2Connection.this.writer.rstStream(streamId, ErrorCode.CANCEL); 
/* 899 */               if (cancel || inFinished) {
/* 900 */                 synchronized (Http2Connection.this) {
/* 901 */                   Http2Connection.this.currentPushRequests.remove(Integer.valueOf(streamId));
/*     */                 } 
/*     */               }
/* 904 */             } catch (IOException iOException) {}
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   void pushResetLater(final int streamId, final ErrorCode errorCode) {
/* 911 */     pushExecutorExecute(new NamedRunnable("OkHttp %s Push Reset[%s]", new Object[] { this.connectionName, Integer.valueOf(streamId) }) {
/*     */           public void execute() {
/* 913 */             Http2Connection.this.pushObserver.onReset(streamId, errorCode);
/* 914 */             synchronized (Http2Connection.this) {
/* 915 */               Http2Connection.this.currentPushRequests.remove(Integer.valueOf(streamId));
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private synchronized void pushExecutorExecute(NamedRunnable namedRunnable) {
/* 922 */     if (!isShutdown()) {
/* 923 */       this.pushExecutor.execute((Runnable)namedRunnable);
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract class Listener
/*     */   {
/* 929 */     public static final Listener REFUSE_INCOMING_STREAMS = new Listener() {
/*     */         public void onStream(Http2Stream stream) throws IOException {
/* 931 */           stream.close(ErrorCode.REFUSED_STREAM);
/*     */         }
/*     */       };
/*     */     
/*     */     public abstract void onStream(Http2Stream param1Http2Stream) throws IOException;
/*     */     
/*     */     public void onSettings(Http2Connection connection) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\http2\Http2Connection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */