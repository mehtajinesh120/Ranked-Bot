/*     */ package org.jsoup.internal;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.jsoup.helper.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ConstrainableInputStream
/*     */   extends BufferedInputStream
/*     */ {
/*     */   private static final int DefaultSize = 32768;
/*     */   private final boolean capped;
/*     */   private final int maxSize;
/*     */   private long startTime;
/*  22 */   private long timeout = 0L;
/*     */   private int remaining;
/*     */   private boolean interrupted;
/*     */   
/*     */   private ConstrainableInputStream(InputStream in, int bufferSize, int maxSize) {
/*  27 */     super(in, bufferSize);
/*  28 */     Validate.isTrue((maxSize >= 0));
/*  29 */     this.maxSize = maxSize;
/*  30 */     this.remaining = maxSize;
/*  31 */     this.capped = (maxSize != 0);
/*  32 */     this.startTime = System.nanoTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConstrainableInputStream wrap(InputStream in, int bufferSize, int maxSize) {
/*  43 */     return (in instanceof ConstrainableInputStream) ? 
/*  44 */       (ConstrainableInputStream)in : 
/*  45 */       new ConstrainableInputStream(in, bufferSize, maxSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*  50 */     if (this.interrupted || (this.capped && this.remaining <= 0))
/*  51 */       return -1; 
/*  52 */     if (Thread.interrupted()) {
/*     */       
/*  54 */       this.interrupted = true;
/*  55 */       return -1;
/*     */     } 
/*  57 */     if (expired()) {
/*  58 */       throw new SocketTimeoutException("Read timeout");
/*     */     }
/*  60 */     if (this.capped && len > this.remaining) {
/*  61 */       len = this.remaining;
/*     */     }
/*     */     try {
/*  64 */       int read = super.read(b, off, len);
/*  65 */       this.remaining -= read;
/*  66 */       return read;
/*  67 */     } catch (SocketTimeoutException e) {
/*  68 */       return 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer readToByteBuffer(int max) throws IOException {
/*  77 */     Validate.isTrue((max >= 0), "maxSize must be 0 (unlimited) or larger");
/*  78 */     boolean localCapped = (max > 0);
/*  79 */     int bufferSize = (localCapped && max < 32768) ? max : 32768;
/*  80 */     byte[] readBuffer = new byte[bufferSize];
/*  81 */     ByteArrayOutputStream outStream = new ByteArrayOutputStream(bufferSize);
/*     */ 
/*     */     
/*  84 */     int remaining = max;
/*     */     while (true) {
/*  86 */       int read = read(readBuffer, 0, localCapped ? Math.min(remaining, bufferSize) : bufferSize);
/*  87 */       if (read == -1)
/*  88 */         break;  if (localCapped) {
/*  89 */         if (read >= remaining) {
/*  90 */           outStream.write(readBuffer, 0, remaining);
/*     */           break;
/*     */         } 
/*  93 */         remaining -= read;
/*     */       } 
/*  95 */       outStream.write(readBuffer, 0, read);
/*     */     } 
/*  97 */     return ByteBuffer.wrap(outStream.toByteArray());
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() throws IOException {
/* 102 */     super.reset();
/* 103 */     this.remaining = this.maxSize - this.markpos;
/*     */   }
/*     */   
/*     */   public ConstrainableInputStream timeout(long startTimeNanos, long timeoutMillis) {
/* 107 */     this.startTime = startTimeNanos;
/* 108 */     this.timeout = timeoutMillis * 1000000L;
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   private boolean expired() {
/* 113 */     if (this.timeout == 0L) {
/* 114 */       return false;
/*     */     }
/* 116 */     long now = System.nanoTime();
/* 117 */     long dur = now - this.startTime;
/* 118 */     return (dur > this.timeout);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\internal\ConstrainableInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */