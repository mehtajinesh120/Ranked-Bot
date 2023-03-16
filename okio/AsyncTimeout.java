/*     */ package okio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncTimeout
/*     */   extends Timeout
/*     */ {
/*     */   private static final int TIMEOUT_WRITE_SIZE = 65536;
/*  50 */   private static final long IDLE_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(60L);
/*  51 */   private static final long IDLE_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(IDLE_TIMEOUT_MILLIS);
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   static AsyncTimeout head;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean inQueue;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private AsyncTimeout next;
/*     */ 
/*     */   
/*     */   private long timeoutAt;
/*     */ 
/*     */ 
/*     */   
/*     */   public final void enter() {
/*  73 */     if (this.inQueue) throw new IllegalStateException("Unbalanced enter/exit"); 
/*  74 */     long timeoutNanos = timeoutNanos();
/*  75 */     boolean hasDeadline = hasDeadline();
/*  76 */     if (timeoutNanos == 0L && !hasDeadline) {
/*     */       return;
/*     */     }
/*  79 */     this.inQueue = true;
/*  80 */     scheduleTimeout(this, timeoutNanos, hasDeadline);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static synchronized void scheduleTimeout(AsyncTimeout node, long timeoutNanos, boolean hasDeadline) {
/*  86 */     if (head == null) {
/*  87 */       head = new AsyncTimeout();
/*  88 */       (new Watchdog()).start();
/*     */     } 
/*     */     
/*  91 */     long now = System.nanoTime();
/*  92 */     if (timeoutNanos != 0L && hasDeadline) {
/*     */ 
/*     */       
/*  95 */       node.timeoutAt = now + Math.min(timeoutNanos, node.deadlineNanoTime() - now);
/*  96 */     } else if (timeoutNanos != 0L) {
/*  97 */       node.timeoutAt = now + timeoutNanos;
/*  98 */     } else if (hasDeadline) {
/*  99 */       node.timeoutAt = node.deadlineNanoTime();
/*     */     } else {
/* 101 */       throw new AssertionError();
/*     */     } 
/*     */ 
/*     */     
/* 105 */     long remainingNanos = node.remainingNanos(now);
/* 106 */     for (AsyncTimeout prev = head;; prev = prev.next) {
/* 107 */       if (prev.next == null || remainingNanos < prev.next.remainingNanos(now)) {
/* 108 */         node.next = prev.next;
/* 109 */         prev.next = node;
/* 110 */         if (prev == head) {
/* 111 */           AsyncTimeout.class.notify();
/*     */         }
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean exit() {
/* 120 */     if (!this.inQueue) return false; 
/* 121 */     this.inQueue = false;
/* 122 */     return cancelScheduledTimeout(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static synchronized boolean cancelScheduledTimeout(AsyncTimeout node) {
/* 128 */     for (AsyncTimeout prev = head; prev != null; prev = prev.next) {
/* 129 */       if (prev.next == node) {
/* 130 */         prev.next = node.next;
/* 131 */         node.next = null;
/* 132 */         return false;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 137 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long remainingNanos(long now) {
/* 145 */     return this.timeoutAt - now;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void timedOut() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Sink sink(final Sink sink) {
/* 160 */     return new Sink() {
/*     */         public void write(Buffer source, long byteCount) throws IOException {
/* 162 */           Util.checkOffsetAndCount(source.size, 0L, byteCount);
/*     */           
/* 164 */           while (byteCount > 0L) {
/*     */             
/* 166 */             long toWrite = 0L;
/* 167 */             for (Segment s = source.head; toWrite < 65536L; s = s.next) {
/* 168 */               int segmentSize = s.limit - s.pos;
/* 169 */               toWrite += segmentSize;
/* 170 */               if (toWrite >= byteCount) {
/* 171 */                 toWrite = byteCount;
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */             
/* 177 */             boolean throwOnTimeout = false;
/* 178 */             AsyncTimeout.this.enter();
/*     */             try {
/* 180 */               sink.write(source, toWrite);
/* 181 */               byteCount -= toWrite;
/* 182 */               throwOnTimeout = true;
/* 183 */             } catch (IOException e) {
/* 184 */               throw AsyncTimeout.this.exit(e);
/*     */             } finally {
/* 186 */               AsyncTimeout.this.exit(throwOnTimeout);
/*     */             } 
/*     */           } 
/*     */         }
/*     */         
/*     */         public void flush() throws IOException {
/* 192 */           boolean throwOnTimeout = false;
/* 193 */           AsyncTimeout.this.enter();
/*     */           try {
/* 195 */             sink.flush();
/* 196 */             throwOnTimeout = true;
/* 197 */           } catch (IOException e) {
/* 198 */             throw AsyncTimeout.this.exit(e);
/*     */           } finally {
/* 200 */             AsyncTimeout.this.exit(throwOnTimeout);
/*     */           } 
/*     */         }
/*     */         
/*     */         public void close() throws IOException {
/* 205 */           boolean throwOnTimeout = false;
/* 206 */           AsyncTimeout.this.enter();
/*     */           try {
/* 208 */             sink.close();
/* 209 */             throwOnTimeout = true;
/* 210 */           } catch (IOException e) {
/* 211 */             throw AsyncTimeout.this.exit(e);
/*     */           } finally {
/* 213 */             AsyncTimeout.this.exit(throwOnTimeout);
/*     */           } 
/*     */         }
/*     */         
/*     */         public Timeout timeout() {
/* 218 */           return AsyncTimeout.this;
/*     */         }
/*     */         
/*     */         public String toString() {
/* 222 */           return "AsyncTimeout.sink(" + sink + ")";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Source source(final Source source) {
/* 232 */     return new Source() {
/*     */         public long read(Buffer sink, long byteCount) throws IOException {
/* 234 */           boolean throwOnTimeout = false;
/* 235 */           AsyncTimeout.this.enter();
/*     */           try {
/* 237 */             long result = source.read(sink, byteCount);
/* 238 */             throwOnTimeout = true;
/* 239 */             return result;
/* 240 */           } catch (IOException e) {
/* 241 */             throw AsyncTimeout.this.exit(e);
/*     */           } finally {
/* 243 */             AsyncTimeout.this.exit(throwOnTimeout);
/*     */           } 
/*     */         }
/*     */         
/*     */         public void close() throws IOException {
/* 248 */           boolean throwOnTimeout = false;
/* 249 */           AsyncTimeout.this.enter();
/*     */           try {
/* 251 */             source.close();
/* 252 */             throwOnTimeout = true;
/* 253 */           } catch (IOException e) {
/* 254 */             throw AsyncTimeout.this.exit(e);
/*     */           } finally {
/* 256 */             AsyncTimeout.this.exit(throwOnTimeout);
/*     */           } 
/*     */         }
/*     */         
/*     */         public Timeout timeout() {
/* 261 */           return AsyncTimeout.this;
/*     */         }
/*     */         
/*     */         public String toString() {
/* 265 */           return "AsyncTimeout.source(" + source + ")";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void exit(boolean throwOnTimeout) throws IOException {
/* 275 */     boolean timedOut = exit();
/* 276 */     if (timedOut && throwOnTimeout) throw newTimeoutException(null);
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final IOException exit(IOException cause) throws IOException {
/* 285 */     if (!exit()) return cause; 
/* 286 */     return newTimeoutException(cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected IOException newTimeoutException(@Nullable IOException cause) {
/* 295 */     InterruptedIOException e = new InterruptedIOException("timeout");
/* 296 */     if (cause != null) {
/* 297 */       e.initCause(cause);
/*     */     }
/* 299 */     return e;
/*     */   }
/*     */   
/*     */   private static final class Watchdog extends Thread {
/*     */     Watchdog() {
/* 304 */       super("Okio Watchdog");
/* 305 */       setDaemon(true);
/*     */     }
/*     */     
/*     */     public void run() {
/*     */       while (true) {
/*     */         try {
/*     */           AsyncTimeout timedOut;
/* 312 */           synchronized (AsyncTimeout.class) {
/* 313 */             timedOut = AsyncTimeout.awaitTimeout();
/*     */ 
/*     */             
/* 316 */             if (timedOut == null) {
/*     */               continue;
/*     */             }
/*     */             
/* 320 */             if (timedOut == AsyncTimeout.head) {
/* 321 */               AsyncTimeout.head = null;
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/*     */           
/* 327 */           timedOut.timedOut();
/* 328 */         } catch (InterruptedException timedOut) {}
/*     */       } 
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
/*     */   @Nullable
/*     */   static AsyncTimeout awaitTimeout() throws InterruptedException {
/* 343 */     AsyncTimeout node = head.next;
/*     */ 
/*     */     
/* 346 */     if (node == null) {
/* 347 */       long startNanos = System.nanoTime();
/* 348 */       AsyncTimeout.class.wait(IDLE_TIMEOUT_MILLIS);
/* 349 */       return (head.next == null && System.nanoTime() - startNanos >= IDLE_TIMEOUT_NANOS) ? 
/* 350 */         head : 
/* 351 */         null;
/*     */     } 
/*     */     
/* 354 */     long waitNanos = node.remainingNanos(System.nanoTime());
/*     */ 
/*     */     
/* 357 */     if (waitNanos > 0L) {
/*     */ 
/*     */       
/* 360 */       long waitMillis = waitNanos / 1000000L;
/* 361 */       waitNanos -= waitMillis * 1000000L;
/* 362 */       AsyncTimeout.class.wait(waitMillis, (int)waitNanos);
/* 363 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 367 */     head.next = node.next;
/* 368 */     node.next = null;
/* 369 */     return node;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okio\AsyncTimeout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */