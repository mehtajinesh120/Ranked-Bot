/*     */ package okhttp3.internal.http2;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import okhttp3.Headers;
/*     */ import okhttp3.internal.Util;
/*     */ import okio.AsyncTimeout;
/*     */ import okio.Buffer;
/*     */ import okio.BufferedSource;
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
/*     */ public final class Http2Stream
/*     */ {
/*  45 */   long unacknowledgedBytesRead = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long bytesLeftInWriteWindow;
/*     */ 
/*     */ 
/*     */   
/*     */   final int id;
/*     */ 
/*     */ 
/*     */   
/*     */   final Http2Connection connection;
/*     */ 
/*     */ 
/*     */   
/*  62 */   private final Deque<Headers> headersQueue = new ArrayDeque<>();
/*     */   
/*     */   private boolean hasResponseHeaders;
/*     */   
/*     */   private final FramingSource source;
/*     */   
/*     */   final FramingSink sink;
/*  69 */   final StreamTimeout readTimeout = new StreamTimeout();
/*  70 */   final StreamTimeout writeTimeout = new StreamTimeout();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   ErrorCode errorCode = null;
/*     */ 
/*     */   
/*     */   Http2Stream(int id, Http2Connection connection, boolean outFinished, boolean inFinished, @Nullable Headers headers) {
/*  81 */     if (connection == null) throw new NullPointerException("connection == null");
/*     */     
/*  83 */     this.id = id;
/*  84 */     this.connection = connection;
/*  85 */     this
/*  86 */       .bytesLeftInWriteWindow = connection.peerSettings.getInitialWindowSize();
/*  87 */     this.source = new FramingSource(connection.okHttpSettings.getInitialWindowSize());
/*  88 */     this.sink = new FramingSink();
/*  89 */     this.source.finished = inFinished;
/*  90 */     this.sink.finished = outFinished;
/*  91 */     if (headers != null) {
/*  92 */       this.headersQueue.add(headers);
/*     */     }
/*     */     
/*  95 */     if (isLocallyInitiated() && headers != null)
/*  96 */       throw new IllegalStateException("locally-initiated streams shouldn't have headers yet"); 
/*  97 */     if (!isLocallyInitiated() && headers == null) {
/*  98 */       throw new IllegalStateException("remotely-initiated streams should have headers");
/*     */     }
/*     */   }
/*     */   
/*     */   public int getId() {
/* 103 */     return this.id;
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
/*     */   public synchronized boolean isOpen() {
/* 118 */     if (this.errorCode != null) {
/* 119 */       return false;
/*     */     }
/* 121 */     if ((this.source.finished || this.source.closed) && (this.sink.finished || this.sink.closed) && this.hasResponseHeaders)
/*     */     {
/*     */       
/* 124 */       return false;
/*     */     }
/* 126 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLocallyInitiated() {
/* 131 */     boolean streamIsClient = ((this.id & 0x1) == 1);
/* 132 */     return (this.connection.client == streamIsClient);
/*     */   }
/*     */   
/*     */   public Http2Connection getConnection() {
/* 136 */     return this.connection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Headers takeHeaders() throws IOException {
/* 145 */     this.readTimeout.enter();
/*     */     try {
/* 147 */       while (this.headersQueue.isEmpty() && this.errorCode == null) {
/* 148 */         waitForIo();
/*     */       }
/*     */     } finally {
/* 151 */       this.readTimeout.exitAndThrowIfTimedOut();
/*     */     } 
/* 153 */     if (!this.headersQueue.isEmpty()) {
/* 154 */       return this.headersQueue.removeFirst();
/*     */     }
/* 156 */     throw new StreamResetException(this.errorCode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Headers trailers() throws IOException {
/* 164 */     if (this.errorCode != null) {
/* 165 */       throw new StreamResetException(this.errorCode);
/*     */     }
/* 167 */     if (!this.source.finished || !this.source.receiveBuffer.exhausted() || !this.source.readBuffer.exhausted()) {
/* 168 */       throw new IllegalStateException("too early; can't read the trailers yet");
/*     */     }
/* 170 */     return (this.source.trailers != null) ? this.source.trailers : Util.EMPTY_HEADERS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized ErrorCode getErrorCode() {
/* 178 */     return this.errorCode;
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
/*     */   public void writeHeaders(List<Header> responseHeaders, boolean outFinished, boolean flushHeaders) throws IOException {
/* 191 */     assert !Thread.holdsLock(this);
/* 192 */     if (responseHeaders == null) {
/* 193 */       throw new NullPointerException("headers == null");
/*     */     }
/* 195 */     synchronized (this) {
/* 196 */       this.hasResponseHeaders = true;
/* 197 */       if (outFinished) {
/* 198 */         this.sink.finished = true;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 204 */     if (!flushHeaders) {
/* 205 */       synchronized (this.connection) {
/* 206 */         flushHeaders = (this.connection.bytesLeftInWriteWindow == 0L);
/*     */       } 
/*     */     }
/*     */     
/* 210 */     this.connection.writeHeaders(this.id, outFinished, responseHeaders);
/*     */     
/* 212 */     if (flushHeaders) {
/* 213 */       this.connection.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   public void enqueueTrailers(Headers trailers) {
/* 218 */     synchronized (this) {
/* 219 */       if (this.sink.finished) throw new IllegalStateException("already finished"); 
/* 220 */       if (trailers.size() == 0) throw new IllegalArgumentException("trailers.size() == 0"); 
/* 221 */       this.sink.trailers = trailers;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Timeout readTimeout() {
/* 226 */     return (Timeout)this.readTimeout;
/*     */   }
/*     */   
/*     */   public Timeout writeTimeout() {
/* 230 */     return (Timeout)this.writeTimeout;
/*     */   }
/*     */ 
/*     */   
/*     */   public Source getSource() {
/* 235 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Sink getSink() {
/* 245 */     synchronized (this) {
/* 246 */       if (!this.hasResponseHeaders && !isLocallyInitiated()) {
/* 247 */         throw new IllegalStateException("reply before requesting the sink");
/*     */       }
/*     */     } 
/* 250 */     return this.sink;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close(ErrorCode rstStatusCode) throws IOException {
/* 258 */     if (!closeInternal(rstStatusCode)) {
/*     */       return;
/*     */     }
/* 261 */     this.connection.writeSynReset(this.id, rstStatusCode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeLater(ErrorCode errorCode) {
/* 269 */     if (!closeInternal(errorCode)) {
/*     */       return;
/*     */     }
/* 272 */     this.connection.writeSynResetLater(this.id, errorCode);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean closeInternal(ErrorCode errorCode) {
/* 277 */     assert !Thread.holdsLock(this);
/* 278 */     synchronized (this) {
/* 279 */       if (this.errorCode != null) {
/* 280 */         return false;
/*     */       }
/* 282 */       if (this.source.finished && this.sink.finished) {
/* 283 */         return false;
/*     */       }
/* 285 */       this.errorCode = errorCode;
/* 286 */       notifyAll();
/*     */     } 
/* 288 */     this.connection.removeStream(this.id);
/* 289 */     return true;
/*     */   }
/*     */   
/*     */   void receiveData(BufferedSource in, int length) throws IOException {
/* 293 */     assert !Thread.holdsLock(this);
/* 294 */     this.source.receive(in, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void receiveHeaders(Headers headers, boolean inFinished) {
/*     */     boolean open;
/* 302 */     assert !Thread.holdsLock(this);
/*     */     
/* 304 */     synchronized (this) {
/* 305 */       if (!this.hasResponseHeaders || !inFinished) {
/* 306 */         this.hasResponseHeaders = true;
/* 307 */         this.headersQueue.add(headers);
/*     */       } else {
/* 309 */         this.source.trailers = headers;
/*     */       } 
/* 311 */       if (inFinished) {
/* 312 */         this.source.finished = true;
/*     */       }
/* 314 */       open = isOpen();
/* 315 */       notifyAll();
/*     */     } 
/* 317 */     if (!open) {
/* 318 */       this.connection.removeStream(this.id);
/*     */     }
/*     */   }
/*     */   
/*     */   synchronized void receiveRstStream(ErrorCode errorCode) {
/* 323 */     if (this.errorCode == null) {
/* 324 */       this.errorCode = errorCode;
/* 325 */       notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class FramingSource
/*     */     implements Source
/*     */   {
/* 336 */     private final Buffer receiveBuffer = new Buffer();
/*     */ 
/*     */     
/* 339 */     private final Buffer readBuffer = new Buffer();
/*     */ 
/*     */ 
/*     */     
/*     */     private final long maxByteCount;
/*     */ 
/*     */ 
/*     */     
/*     */     private Headers trailers;
/*     */ 
/*     */ 
/*     */     
/*     */     boolean closed;
/*     */ 
/*     */ 
/*     */     
/*     */     boolean finished;
/*     */ 
/*     */ 
/*     */     
/*     */     FramingSource(long maxByteCount) {
/* 360 */       this.maxByteCount = maxByteCount;
/*     */     } public long read(Buffer sink, long byteCount) throws IOException {
/*     */       long readBytesDelivered;
/*     */       ErrorCode errorCodeToDeliver;
/* 364 */       if (byteCount < 0L) throw new IllegalArgumentException("byteCount < 0: " + byteCount);
/*     */       
/*     */       while (true) {
/* 367 */         readBytesDelivered = -1L;
/* 368 */         errorCodeToDeliver = null;
/*     */ 
/*     */ 
/*     */         
/* 372 */         synchronized (Http2Stream.this) {
/* 373 */           Http2Stream.this.readTimeout.enter();
/*     */           
/* 375 */           try { if (Http2Stream.this.errorCode != null)
/*     */             {
/* 377 */               errorCodeToDeliver = Http2Stream.this.errorCode;
/*     */             }
/*     */             
/* 380 */             if (this.closed) {
/* 381 */               throw new IOException("stream closed");
/*     */             }
/* 383 */             if (this.readBuffer.size() > 0L)
/*     */             
/* 385 */             { readBytesDelivered = this.readBuffer.read(sink, Math.min(byteCount, this.readBuffer.size()));
/* 386 */               Http2Stream.this.unacknowledgedBytesRead += readBytesDelivered;
/*     */               
/* 388 */               if (errorCodeToDeliver == null && Http2Stream.this.unacknowledgedBytesRead >= (Http2Stream.this.connection.okHttpSettings
/*     */                 
/* 390 */                 .getInitialWindowSize() / 2)) {
/*     */ 
/*     */                 
/* 393 */                 Http2Stream.this.connection.writeWindowUpdateLater(Http2Stream.this.id, Http2Stream.this.unacknowledgedBytesRead);
/* 394 */                 Http2Stream.this.unacknowledgedBytesRead = 0L;
/*     */               }  }
/* 396 */             else if (!this.finished && errorCodeToDeliver == null)
/*     */             
/* 398 */             { Http2Stream.this.waitForIo();
/*     */ 
/*     */ 
/*     */               
/* 402 */               Http2Stream.this.readTimeout.exitAndThrowIfTimedOut(); continue; }  } finally { Http2Stream.this.readTimeout.exitAndThrowIfTimedOut(); }
/*     */         
/*     */         } 
/*     */         
/*     */         break;
/*     */       } 
/* 408 */       if (readBytesDelivered != -1L) {
/*     */         
/* 410 */         updateConnectionFlowControl(readBytesDelivered);
/* 411 */         return readBytesDelivered;
/*     */       } 
/*     */       
/* 414 */       if (errorCodeToDeliver != null)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 419 */         throw new StreamResetException(errorCodeToDeliver);
/*     */       }
/*     */       
/* 422 */       return -1L;
/*     */     }
/*     */ 
/*     */     
/*     */     private void updateConnectionFlowControl(long read) {
/* 427 */       assert !Thread.holdsLock(Http2Stream.this);
/* 428 */       Http2Stream.this.connection.updateConnectionFlowControl(read);
/*     */     }
/*     */     
/*     */     void receive(BufferedSource in, long byteCount) throws IOException {
/* 432 */       assert !Thread.holdsLock(Http2Stream.this);
/*     */       
/* 434 */       while (byteCount > 0L) {
/*     */         boolean finished, flowControlError;
/*     */         
/* 437 */         synchronized (Http2Stream.this) {
/* 438 */           finished = this.finished;
/* 439 */           flowControlError = (byteCount + this.readBuffer.size() > this.maxByteCount);
/*     */         } 
/*     */ 
/*     */         
/* 443 */         if (flowControlError) {
/* 444 */           in.skip(byteCount);
/* 445 */           Http2Stream.this.closeLater(ErrorCode.FLOW_CONTROL_ERROR);
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 450 */         if (finished) {
/* 451 */           in.skip(byteCount);
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 456 */         long read = in.read(this.receiveBuffer, byteCount);
/* 457 */         if (read == -1L) throw new EOFException(); 
/* 458 */         byteCount -= read;
/*     */ 
/*     */         
/* 461 */         synchronized (Http2Stream.this) {
/* 462 */           boolean wasEmpty = (this.readBuffer.size() == 0L);
/* 463 */           this.readBuffer.writeAll((Source)this.receiveBuffer);
/* 464 */           if (wasEmpty) {
/* 465 */             Http2Stream.this.notifyAll();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public Timeout timeout() {
/* 472 */       return (Timeout)Http2Stream.this.readTimeout;
/*     */     }
/*     */     
/*     */     public void close() throws IOException {
/*     */       long bytesDiscarded;
/* 477 */       synchronized (Http2Stream.this) {
/* 478 */         this.closed = true;
/* 479 */         bytesDiscarded = this.readBuffer.size();
/* 480 */         this.readBuffer.clear();
/* 481 */         Http2Stream.this.notifyAll();
/*     */       } 
/* 483 */       if (bytesDiscarded > 0L) {
/* 484 */         updateConnectionFlowControl(bytesDiscarded);
/*     */       }
/* 486 */       Http2Stream.this.cancelStreamIfNecessary();
/*     */     } }
/*     */   void cancelStreamIfNecessary() throws IOException {
/*     */     boolean open;
/*     */     boolean cancel;
/* 491 */     assert !Thread.holdsLock(this);
/*     */ 
/*     */     
/* 494 */     synchronized (this) {
/* 495 */       cancel = (!this.source.finished && this.source.closed && (this.sink.finished || this.sink.closed));
/* 496 */       open = isOpen();
/*     */     } 
/* 498 */     if (cancel) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 503 */       close(ErrorCode.CANCEL);
/* 504 */     } else if (!open) {
/* 505 */       this.connection.removeStream(this.id);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final class FramingSink
/*     */     implements Sink
/*     */   {
/*     */     private static final long EMIT_BUFFER_SIZE = 16384L;
/*     */ 
/*     */     
/* 517 */     private final Buffer sendBuffer = new Buffer();
/*     */ 
/*     */     
/*     */     private Headers trailers;
/*     */ 
/*     */     
/*     */     boolean closed;
/*     */ 
/*     */     
/*     */     boolean finished;
/*     */ 
/*     */     
/*     */     public void write(Buffer source, long byteCount) throws IOException {
/* 530 */       assert !Thread.holdsLock(Http2Stream.this);
/* 531 */       this.sendBuffer.write(source, byteCount);
/* 532 */       while (this.sendBuffer.size() >= 16384L) {
/* 533 */         emitFrame(false);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void emitFrame(boolean outFinishedOnLastFrame) throws IOException {
/*     */       long toWrite;
/* 543 */       synchronized (Http2Stream.this) {
/* 544 */         Http2Stream.this.writeTimeout.enter();
/*     */         try {
/* 546 */           while (Http2Stream.this.bytesLeftInWriteWindow <= 0L && !this.finished && !this.closed && Http2Stream.this.errorCode == null) {
/* 547 */             Http2Stream.this.waitForIo();
/*     */           }
/*     */         } finally {
/* 550 */           Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
/*     */         } 
/*     */         
/* 553 */         Http2Stream.this.checkOutNotClosed();
/* 554 */         toWrite = Math.min(Http2Stream.this.bytesLeftInWriteWindow, this.sendBuffer.size());
/* 555 */         Http2Stream.this.bytesLeftInWriteWindow -= toWrite;
/*     */       } 
/*     */       
/* 558 */       Http2Stream.this.writeTimeout.enter();
/*     */       try {
/* 560 */         boolean outFinished = (outFinishedOnLastFrame && toWrite == this.sendBuffer.size());
/* 561 */         Http2Stream.this.connection.writeData(Http2Stream.this.id, outFinished, this.sendBuffer, toWrite);
/*     */       } finally {
/* 563 */         Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void flush() throws IOException {
/* 568 */       assert !Thread.holdsLock(Http2Stream.this);
/* 569 */       synchronized (Http2Stream.this) {
/* 570 */         Http2Stream.this.checkOutNotClosed();
/*     */       } 
/* 572 */       while (this.sendBuffer.size() > 0L) {
/* 573 */         emitFrame(false);
/* 574 */         Http2Stream.this.connection.flush();
/*     */       } 
/*     */     }
/*     */     
/*     */     public Timeout timeout() {
/* 579 */       return (Timeout)Http2Stream.this.writeTimeout;
/*     */     }
/*     */     
/*     */     public void close() throws IOException {
/* 583 */       assert !Thread.holdsLock(Http2Stream.this);
/* 584 */       synchronized (Http2Stream.this) {
/* 585 */         if (this.closed)
/*     */           return; 
/* 587 */       }  if (!Http2Stream.this.sink.finished) {
/*     */ 
/*     */ 
/*     */         
/* 591 */         boolean hasData = (this.sendBuffer.size() > 0L);
/* 592 */         boolean hasTrailers = (this.trailers != null);
/* 593 */         if (hasTrailers) {
/* 594 */           while (this.sendBuffer.size() > 0L) {
/* 595 */             emitFrame(false);
/*     */           }
/* 597 */           Http2Stream.this.connection.writeHeaders(Http2Stream.this.id, true, Util.toHeaderBlock(this.trailers));
/* 598 */         } else if (hasData) {
/* 599 */           while (this.sendBuffer.size() > 0L) {
/* 600 */             emitFrame(true);
/*     */           }
/*     */         } else {
/* 603 */           Http2Stream.this.connection.writeData(Http2Stream.this.id, true, null, 0L);
/*     */         } 
/*     */       } 
/* 606 */       synchronized (Http2Stream.this) {
/* 607 */         this.closed = true;
/*     */       } 
/* 609 */       Http2Stream.this.connection.flush();
/* 610 */       Http2Stream.this.cancelStreamIfNecessary();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void addBytesToWriteWindow(long delta) {
/* 618 */     this.bytesLeftInWriteWindow += delta;
/* 619 */     if (delta > 0L) notifyAll(); 
/*     */   }
/*     */   
/*     */   void checkOutNotClosed() throws IOException {
/* 623 */     if (this.sink.closed)
/* 624 */       throw new IOException("stream closed"); 
/* 625 */     if (this.sink.finished)
/* 626 */       throw new IOException("stream finished"); 
/* 627 */     if (this.errorCode != null) {
/* 628 */       throw new StreamResetException(this.errorCode);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void waitForIo() throws InterruptedIOException {
/*     */     try {
/* 638 */       wait();
/* 639 */     } catch (InterruptedException e) {
/* 640 */       Thread.currentThread().interrupt();
/* 641 */       throw new InterruptedIOException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   class StreamTimeout
/*     */     extends AsyncTimeout
/*     */   {
/*     */     protected void timedOut() {
/* 651 */       Http2Stream.this.closeLater(ErrorCode.CANCEL);
/*     */     }
/*     */     
/*     */     protected IOException newTimeoutException(IOException cause) {
/* 655 */       SocketTimeoutException socketTimeoutException = new SocketTimeoutException("timeout");
/* 656 */       if (cause != null) {
/* 657 */         socketTimeoutException.initCause(cause);
/*     */       }
/* 659 */       return socketTimeoutException;
/*     */     }
/*     */     
/*     */     public void exitAndThrowIfTimedOut() throws IOException {
/* 663 */       if (exit()) throw newTimeoutException(null); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\http2\Http2Stream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */