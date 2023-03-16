/*     */ package okio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Pipe
/*     */ {
/*     */   final long maxBufferSize;
/*  39 */   final Buffer buffer = new Buffer();
/*     */   boolean sinkClosed;
/*     */   boolean sourceClosed;
/*  42 */   private final Sink sink = new PipeSink();
/*  43 */   private final Source source = new PipeSource();
/*     */ 
/*     */   
/*     */   public Pipe(long maxBufferSize) {
/*  47 */     if (maxBufferSize < 1L) {
/*  48 */       throw new IllegalArgumentException("maxBufferSize < 1: " + maxBufferSize);
/*     */     }
/*  50 */     this.maxBufferSize = maxBufferSize;
/*     */   } @Nullable
/*     */   private Sink foldedSink;
/*     */   public final Source source() {
/*  54 */     return this.source;
/*     */   }
/*     */   
/*     */   public final Sink sink() {
/*  58 */     return this.sink;
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
/*     */   public void fold(Sink sink) throws IOException {
/*     */     while (true) {
/*     */       Buffer sinkBuffer;
/*  72 */       synchronized (this.buffer) {
/*  73 */         if (this.foldedSink != null) throw new IllegalStateException("sink already folded");
/*     */         
/*  75 */         if (this.buffer.exhausted()) {
/*  76 */           this.sourceClosed = true;
/*  77 */           this.foldedSink = sink;
/*     */           
/*     */           return;
/*     */         } 
/*  81 */         sinkBuffer = new Buffer();
/*  82 */         sinkBuffer.write(this.buffer, this.buffer.size);
/*  83 */         this.buffer.notifyAll();
/*     */       } 
/*     */       
/*  86 */       boolean success = false;
/*     */       try {
/*  88 */         sink.write(sinkBuffer, sinkBuffer.size);
/*  89 */         sink.flush();
/*  90 */         success = true;
/*     */       } finally {
/*  92 */         if (!success)
/*  93 */           synchronized (this.buffer) {
/*  94 */             this.sourceClosed = true;
/*  95 */             this.buffer.notifyAll();
/*     */           }  
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   final class PipeSink
/*     */     implements Sink {
/* 103 */     final PushableTimeout timeout = new PushableTimeout();
/*     */     
/*     */     public void write(Buffer source, long byteCount) throws IOException {
/* 106 */       Sink delegate = null;
/* 107 */       synchronized (Pipe.this.buffer) {
/* 108 */         if (Pipe.this.sinkClosed) throw new IllegalStateException("closed");
/*     */         
/* 110 */         while (byteCount > 0L) {
/* 111 */           if (Pipe.this.foldedSink != null) {
/* 112 */             delegate = Pipe.this.foldedSink;
/*     */             
/*     */             break;
/*     */           } 
/* 116 */           if (Pipe.this.sourceClosed) throw new IOException("source is closed");
/*     */           
/* 118 */           long bufferSpaceAvailable = Pipe.this.maxBufferSize - Pipe.this.buffer.size();
/* 119 */           if (bufferSpaceAvailable == 0L) {
/* 120 */             this.timeout.waitUntilNotified(Pipe.this.buffer);
/*     */             
/*     */             continue;
/*     */           } 
/* 124 */           long bytesToWrite = Math.min(bufferSpaceAvailable, byteCount);
/* 125 */           Pipe.this.buffer.write(source, bytesToWrite);
/* 126 */           byteCount -= bytesToWrite;
/* 127 */           Pipe.this.buffer.notifyAll();
/*     */         } 
/*     */       } 
/*     */       
/* 131 */       if (delegate != null) {
/* 132 */         this.timeout.push(delegate.timeout());
/*     */         try {
/* 134 */           delegate.write(source, byteCount);
/*     */         } finally {
/* 136 */           this.timeout.pop();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void flush() throws IOException {
/* 142 */       Sink delegate = null;
/* 143 */       synchronized (Pipe.this.buffer) {
/* 144 */         if (Pipe.this.sinkClosed) throw new IllegalStateException("closed");
/*     */         
/* 146 */         if (Pipe.this.foldedSink != null) {
/* 147 */           delegate = Pipe.this.foldedSink;
/* 148 */         } else if (Pipe.this.sourceClosed && Pipe.this.buffer.size() > 0L) {
/* 149 */           throw new IOException("source is closed");
/*     */         } 
/*     */       } 
/*     */       
/* 153 */       if (delegate != null) {
/* 154 */         this.timeout.push(delegate.timeout());
/*     */         try {
/* 156 */           delegate.flush();
/*     */         } finally {
/* 158 */           this.timeout.pop();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void close() throws IOException {
/* 164 */       Sink delegate = null;
/* 165 */       synchronized (Pipe.this.buffer) {
/* 166 */         if (Pipe.this.sinkClosed)
/*     */           return; 
/* 168 */         if (Pipe.this.foldedSink != null) {
/* 169 */           delegate = Pipe.this.foldedSink;
/*     */         } else {
/* 171 */           if (Pipe.this.sourceClosed && Pipe.this.buffer.size() > 0L) throw new IOException("source is closed"); 
/* 172 */           Pipe.this.sinkClosed = true;
/* 173 */           Pipe.this.buffer.notifyAll();
/*     */         } 
/*     */       } 
/*     */       
/* 177 */       if (delegate != null) {
/* 178 */         this.timeout.push(delegate.timeout());
/*     */         try {
/* 180 */           delegate.close();
/*     */         } finally {
/* 182 */           this.timeout.pop();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public Timeout timeout() {
/* 188 */       return this.timeout;
/*     */     }
/*     */   }
/*     */   
/*     */   final class PipeSource implements Source {
/* 193 */     final Timeout timeout = new Timeout();
/*     */     
/*     */     public long read(Buffer sink, long byteCount) throws IOException {
/* 196 */       synchronized (Pipe.this.buffer) {
/* 197 */         if (Pipe.this.sourceClosed) throw new IllegalStateException("closed");
/*     */         
/* 199 */         while (Pipe.this.buffer.size() == 0L) {
/* 200 */           if (Pipe.this.sinkClosed) return -1L; 
/* 201 */           this.timeout.waitUntilNotified(Pipe.this.buffer);
/*     */         } 
/*     */         
/* 204 */         long result = Pipe.this.buffer.read(sink, byteCount);
/* 205 */         Pipe.this.buffer.notifyAll();
/* 206 */         return result;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void close() throws IOException {
/* 211 */       synchronized (Pipe.this.buffer) {
/* 212 */         Pipe.this.sourceClosed = true;
/* 213 */         Pipe.this.buffer.notifyAll();
/*     */       } 
/*     */     }
/*     */     
/*     */     public Timeout timeout() {
/* 218 */       return this.timeout;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okio\Pipe.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */