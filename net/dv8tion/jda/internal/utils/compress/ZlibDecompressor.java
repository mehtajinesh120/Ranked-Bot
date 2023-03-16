/*     */ package net.dv8tion.jda.internal.utils.compress;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ import java.util.zip.DataFormatException;
/*     */ import java.util.zip.Inflater;
/*     */ import java.util.zip.InflaterOutputStream;
/*     */ import net.dv8tion.jda.api.utils.Compression;
/*     */ import net.dv8tion.jda.internal.utils.IOUtil;
/*     */ import net.dv8tion.jda.internal.utils.JDALogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ZlibDecompressor
/*     */   implements Decompressor
/*     */ {
/*     */   private static final int Z_SYNC_FLUSH = 65535;
/*     */   private final int maxBufferSize;
/*  37 */   private final Inflater inflater = new Inflater();
/*  38 */   private ByteBuffer flushBuffer = null;
/*  39 */   private SoftReference<ByteArrayOutputStream> decompressBuffer = null;
/*     */ 
/*     */   
/*     */   public ZlibDecompressor(int maxBufferSize) {
/*  43 */     this.maxBufferSize = maxBufferSize;
/*     */   }
/*     */ 
/*     */   
/*     */   private SoftReference<ByteArrayOutputStream> newDecompressBuffer() {
/*  48 */     return new SoftReference<>(new ByteArrayOutputStream(Math.min(1024, this.maxBufferSize)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ByteArrayOutputStream getDecompressBuffer() {
/*  54 */     if (this.decompressBuffer == null) {
/*  55 */       this.decompressBuffer = newDecompressBuffer();
/*     */     }
/*  57 */     ByteArrayOutputStream buffer = this.decompressBuffer.get();
/*  58 */     if (buffer == null)
/*  59 */       this.decompressBuffer = new SoftReference<>(buffer = new ByteArrayOutputStream(Math.min(1024, this.maxBufferSize))); 
/*  60 */     return buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isFlush(byte[] data) {
/*  65 */     if (data.length < 4)
/*  66 */       return false; 
/*  67 */     int suffix = IOUtil.getIntBigEndian(data, data.length - 4);
/*  68 */     return (suffix == 65535);
/*     */   }
/*     */ 
/*     */   
/*     */   private void buffer(byte[] data) {
/*  73 */     if (this.flushBuffer == null) {
/*  74 */       this.flushBuffer = ByteBuffer.allocate(data.length * 2);
/*     */     }
/*     */     
/*  77 */     if (this.flushBuffer.capacity() < data.length + this.flushBuffer.position()) {
/*     */ 
/*     */       
/*  80 */       this.flushBuffer.flip();
/*     */       
/*  82 */       this.flushBuffer = IOUtil.reallocate(this.flushBuffer, (this.flushBuffer.capacity() + data.length) * 2);
/*     */     } 
/*     */     
/*  85 */     this.flushBuffer.put(data);
/*     */   }
/*     */ 
/*     */   
/*     */   private Object lazy(byte[] data) {
/*  90 */     return JDALogger.getLazyString(() -> Arrays.toString(data));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Compression getType() {
/*  96 */     return Compression.ZLIB;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 102 */     this.inflater.reset();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 108 */     reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] decompress(byte[] data) throws DataFormatException {
/* 115 */     if (!isFlush(data)) {
/*     */ 
/*     */       
/* 118 */       LOG.debug("Received incomplete data, writing to buffer. Length: {}", Integer.valueOf(data.length));
/* 119 */       buffer(data);
/* 120 */       return null;
/*     */     } 
/* 122 */     if (this.flushBuffer != null) {
/*     */ 
/*     */ 
/*     */       
/* 126 */       LOG.debug("Received final part of incomplete data");
/* 127 */       buffer(data);
/* 128 */       byte[] arr = this.flushBuffer.array();
/* 129 */       data = new byte[this.flushBuffer.position()];
/* 130 */       System.arraycopy(arr, 0, data, 0, data.length);
/* 131 */       this.flushBuffer = null;
/*     */     } 
/* 133 */     LOG.trace("Decompressing data {}", lazy(data));
/*     */ 
/*     */     
/* 136 */     ByteArrayOutputStream buffer = getDecompressBuffer(); try {
/* 137 */       InflaterOutputStream decompressor = new InflaterOutputStream(buffer, this.inflater);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 144 */     catch (IOException e) {
/*     */ 
/*     */       
/* 147 */       throw (DataFormatException)(new DataFormatException("Malformed")).initCause(e);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 152 */       if (buffer.size() > this.maxBufferSize) {
/* 153 */         this.decompressBuffer = newDecompressBuffer();
/*     */       } else {
/* 155 */         buffer.reset();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\compress\ZlibDecompressor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */