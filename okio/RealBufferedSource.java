/*     */ package okio;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
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
/*     */ final class RealBufferedSource
/*     */   implements BufferedSource
/*     */ {
/*  28 */   public final Buffer buffer = new Buffer();
/*     */   public final Source source;
/*     */   boolean closed;
/*     */   
/*     */   RealBufferedSource(Source source) {
/*  33 */     if (source == null) throw new NullPointerException("source == null"); 
/*  34 */     this.source = source;
/*     */   }
/*     */   
/*     */   public Buffer buffer() {
/*  38 */     return this.buffer;
/*     */   }
/*     */   
/*     */   public Buffer getBuffer() {
/*  42 */     return this.buffer;
/*     */   }
/*     */   
/*     */   public long read(Buffer sink, long byteCount) throws IOException {
/*  46 */     if (sink == null) throw new IllegalArgumentException("sink == null"); 
/*  47 */     if (byteCount < 0L) throw new IllegalArgumentException("byteCount < 0: " + byteCount); 
/*  48 */     if (this.closed) throw new IllegalStateException("closed");
/*     */     
/*  50 */     if (this.buffer.size == 0L) {
/*  51 */       long read = this.source.read(this.buffer, 8192L);
/*  52 */       if (read == -1L) return -1L;
/*     */     
/*     */     } 
/*  55 */     long toRead = Math.min(byteCount, this.buffer.size);
/*  56 */     return this.buffer.read(sink, toRead);
/*     */   }
/*     */   
/*     */   public boolean exhausted() throws IOException {
/*  60 */     if (this.closed) throw new IllegalStateException("closed"); 
/*  61 */     return (this.buffer.exhausted() && this.source.read(this.buffer, 8192L) == -1L);
/*     */   }
/*     */   
/*     */   public void require(long byteCount) throws IOException {
/*  65 */     if (!request(byteCount)) throw new EOFException(); 
/*     */   }
/*     */   
/*     */   public boolean request(long byteCount) throws IOException {
/*  69 */     if (byteCount < 0L) throw new IllegalArgumentException("byteCount < 0: " + byteCount); 
/*  70 */     if (this.closed) throw new IllegalStateException("closed"); 
/*  71 */     while (this.buffer.size < byteCount) {
/*  72 */       if (this.source.read(this.buffer, 8192L) == -1L) return false; 
/*     */     } 
/*  74 */     return true;
/*     */   }
/*     */   
/*     */   public byte readByte() throws IOException {
/*  78 */     require(1L);
/*  79 */     return this.buffer.readByte();
/*     */   }
/*     */   
/*     */   public ByteString readByteString() throws IOException {
/*  83 */     this.buffer.writeAll(this.source);
/*  84 */     return this.buffer.readByteString();
/*     */   }
/*     */   
/*     */   public ByteString readByteString(long byteCount) throws IOException {
/*  88 */     require(byteCount);
/*  89 */     return this.buffer.readByteString(byteCount);
/*     */   }
/*     */   public int select(Options options) throws IOException {
/*     */     int index;
/*  93 */     if (this.closed) throw new IllegalStateException("closed");
/*     */     
/*     */     while (true) {
/*  96 */       index = this.buffer.selectPrefix(options, true);
/*  97 */       if (index == -1) return -1; 
/*  98 */       if (index == -2) {
/*     */         
/* 100 */         if (this.source.read(this.buffer, 8192L) == -1L) return -1;  continue;
/*     */       }  break;
/*     */     } 
/* 103 */     int selectedSize = options.byteStrings[index].size();
/* 104 */     this.buffer.skip(selectedSize);
/* 105 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] readByteArray() throws IOException {
/* 111 */     this.buffer.writeAll(this.source);
/* 112 */     return this.buffer.readByteArray();
/*     */   }
/*     */   
/*     */   public byte[] readByteArray(long byteCount) throws IOException {
/* 116 */     require(byteCount);
/* 117 */     return this.buffer.readByteArray(byteCount);
/*     */   }
/*     */   
/*     */   public int read(byte[] sink) throws IOException {
/* 121 */     return read(sink, 0, sink.length);
/*     */   }
/*     */   
/*     */   public void readFully(byte[] sink) throws IOException {
/*     */     try {
/* 126 */       require(sink.length);
/* 127 */     } catch (EOFException e) {
/*     */       
/* 129 */       int offset = 0;
/* 130 */       while (this.buffer.size > 0L) {
/* 131 */         int read = this.buffer.read(sink, offset, (int)this.buffer.size);
/* 132 */         if (read == -1) throw new AssertionError(); 
/* 133 */         offset += read;
/*     */       } 
/* 135 */       throw e;
/*     */     } 
/* 137 */     this.buffer.readFully(sink);
/*     */   }
/*     */   
/*     */   public int read(byte[] sink, int offset, int byteCount) throws IOException {
/* 141 */     Util.checkOffsetAndCount(sink.length, offset, byteCount);
/*     */     
/* 143 */     if (this.buffer.size == 0L) {
/* 144 */       long read = this.source.read(this.buffer, 8192L);
/* 145 */       if (read == -1L) return -1;
/*     */     
/*     */     } 
/* 148 */     int toRead = (int)Math.min(byteCount, this.buffer.size);
/* 149 */     return this.buffer.read(sink, offset, toRead);
/*     */   }
/*     */   
/*     */   public int read(ByteBuffer sink) throws IOException {
/* 153 */     if (this.buffer.size == 0L) {
/* 154 */       long read = this.source.read(this.buffer, 8192L);
/* 155 */       if (read == -1L) return -1;
/*     */     
/*     */     } 
/* 158 */     return this.buffer.read(sink);
/*     */   }
/*     */   
/*     */   public void readFully(Buffer sink, long byteCount) throws IOException {
/*     */     try {
/* 163 */       require(byteCount);
/* 164 */     } catch (EOFException e) {
/*     */       
/* 166 */       sink.writeAll(this.buffer);
/* 167 */       throw e;
/*     */     } 
/* 169 */     this.buffer.readFully(sink, byteCount);
/*     */   }
/*     */   
/*     */   public long readAll(Sink sink) throws IOException {
/* 173 */     if (sink == null) throw new IllegalArgumentException("sink == null");
/*     */     
/* 175 */     long totalBytesWritten = 0L;
/* 176 */     while (this.source.read(this.buffer, 8192L) != -1L) {
/* 177 */       long emitByteCount = this.buffer.completeSegmentByteCount();
/* 178 */       if (emitByteCount > 0L) {
/* 179 */         totalBytesWritten += emitByteCount;
/* 180 */         sink.write(this.buffer, emitByteCount);
/*     */       } 
/*     */     } 
/* 183 */     if (this.buffer.size() > 0L) {
/* 184 */       totalBytesWritten += this.buffer.size();
/* 185 */       sink.write(this.buffer, this.buffer.size());
/*     */     } 
/* 187 */     return totalBytesWritten;
/*     */   }
/*     */   
/*     */   public String readUtf8() throws IOException {
/* 191 */     this.buffer.writeAll(this.source);
/* 192 */     return this.buffer.readUtf8();
/*     */   }
/*     */   
/*     */   public String readUtf8(long byteCount) throws IOException {
/* 196 */     require(byteCount);
/* 197 */     return this.buffer.readUtf8(byteCount);
/*     */   }
/*     */   
/*     */   public String readString(Charset charset) throws IOException {
/* 201 */     if (charset == null) throw new IllegalArgumentException("charset == null");
/*     */     
/* 203 */     this.buffer.writeAll(this.source);
/* 204 */     return this.buffer.readString(charset);
/*     */   }
/*     */   
/*     */   public String readString(long byteCount, Charset charset) throws IOException {
/* 208 */     require(byteCount);
/* 209 */     if (charset == null) throw new IllegalArgumentException("charset == null"); 
/* 210 */     return this.buffer.readString(byteCount, charset);
/*     */   }
/*     */   @Nullable
/*     */   public String readUtf8Line() throws IOException {
/* 214 */     long newline = indexOf((byte)10);
/*     */     
/* 216 */     if (newline == -1L) {
/* 217 */       return (this.buffer.size != 0L) ? readUtf8(this.buffer.size) : null;
/*     */     }
/*     */     
/* 220 */     return this.buffer.readUtf8Line(newline);
/*     */   }
/*     */   
/*     */   public String readUtf8LineStrict() throws IOException {
/* 224 */     return readUtf8LineStrict(Long.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public String readUtf8LineStrict(long limit) throws IOException {
/* 228 */     if (limit < 0L) throw new IllegalArgumentException("limit < 0: " + limit); 
/* 229 */     long scanLength = (limit == Long.MAX_VALUE) ? Long.MAX_VALUE : (limit + 1L);
/* 230 */     long newline = indexOf((byte)10, 0L, scanLength);
/* 231 */     if (newline != -1L) return this.buffer.readUtf8Line(newline); 
/* 232 */     if (scanLength < Long.MAX_VALUE && 
/* 233 */       request(scanLength) && this.buffer.getByte(scanLength - 1L) == 13 && 
/* 234 */       request(scanLength + 1L) && this.buffer.getByte(scanLength) == 10) {
/* 235 */       return this.buffer.readUtf8Line(scanLength);
/*     */     }
/* 237 */     Buffer data = new Buffer();
/* 238 */     this.buffer.copyTo(data, 0L, Math.min(32L, this.buffer.size()));
/* 239 */     throw new EOFException("\\n not found: limit=" + Math.min(this.buffer.size(), limit) + " content=" + data
/* 240 */         .readByteString().hex() + '…');
/*     */   }
/*     */   
/*     */   public int readUtf8CodePoint() throws IOException {
/* 244 */     require(1L);
/*     */     
/* 246 */     byte b0 = this.buffer.getByte(0L);
/* 247 */     if ((b0 & 0xE0) == 192) {
/* 248 */       require(2L);
/* 249 */     } else if ((b0 & 0xF0) == 224) {
/* 250 */       require(3L);
/* 251 */     } else if ((b0 & 0xF8) == 240) {
/* 252 */       require(4L);
/*     */     } 
/*     */     
/* 255 */     return this.buffer.readUtf8CodePoint();
/*     */   }
/*     */   
/*     */   public short readShort() throws IOException {
/* 259 */     require(2L);
/* 260 */     return this.buffer.readShort();
/*     */   }
/*     */   
/*     */   public short readShortLe() throws IOException {
/* 264 */     require(2L);
/* 265 */     return this.buffer.readShortLe();
/*     */   }
/*     */   
/*     */   public int readInt() throws IOException {
/* 269 */     require(4L);
/* 270 */     return this.buffer.readInt();
/*     */   }
/*     */   
/*     */   public int readIntLe() throws IOException {
/* 274 */     require(4L);
/* 275 */     return this.buffer.readIntLe();
/*     */   }
/*     */   
/*     */   public long readLong() throws IOException {
/* 279 */     require(8L);
/* 280 */     return this.buffer.readLong();
/*     */   }
/*     */   
/*     */   public long readLongLe() throws IOException {
/* 284 */     require(8L);
/* 285 */     return this.buffer.readLongLe();
/*     */   }
/*     */   
/*     */   public long readDecimalLong() throws IOException {
/* 289 */     require(1L);
/*     */     
/* 291 */     for (int pos = 0; request((pos + 1)); pos++) {
/* 292 */       byte b = this.buffer.getByte(pos);
/* 293 */       if ((b < 48 || b > 57) && (pos != 0 || b != 45)) {
/*     */         
/* 295 */         if (pos == 0) {
/* 296 */           throw new NumberFormatException(String.format("Expected leading [0-9] or '-' character but was %#x", new Object[] {
/* 297 */                   Byte.valueOf(b)
/*     */                 }));
/*     */         }
/*     */         break;
/*     */       } 
/*     */     } 
/* 303 */     return this.buffer.readDecimalLong();
/*     */   }
/*     */   
/*     */   public long readHexadecimalUnsignedLong() throws IOException {
/* 307 */     require(1L);
/*     */     
/* 309 */     for (int pos = 0; request((pos + 1)); pos++) {
/* 310 */       byte b = this.buffer.getByte(pos);
/* 311 */       if ((b < 48 || b > 57) && (b < 97 || b > 102) && (b < 65 || b > 70)) {
/*     */         
/* 313 */         if (pos == 0) {
/* 314 */           throw new NumberFormatException(String.format("Expected leading [0-9a-fA-F] character but was %#x", new Object[] {
/* 315 */                   Byte.valueOf(b)
/*     */                 }));
/*     */         }
/*     */         break;
/*     */       } 
/*     */     } 
/* 321 */     return this.buffer.readHexadecimalUnsignedLong();
/*     */   }
/*     */   
/*     */   public void skip(long byteCount) throws IOException {
/* 325 */     if (this.closed) throw new IllegalStateException("closed"); 
/* 326 */     while (byteCount > 0L) {
/* 327 */       if (this.buffer.size == 0L && this.source.read(this.buffer, 8192L) == -1L) {
/* 328 */         throw new EOFException();
/*     */       }
/* 330 */       long toSkip = Math.min(byteCount, this.buffer.size());
/* 331 */       this.buffer.skip(toSkip);
/* 332 */       byteCount -= toSkip;
/*     */     } 
/*     */   }
/*     */   
/*     */   public long indexOf(byte b) throws IOException {
/* 337 */     return indexOf(b, 0L, Long.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public long indexOf(byte b, long fromIndex) throws IOException {
/* 341 */     return indexOf(b, fromIndex, Long.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public long indexOf(byte b, long fromIndex, long toIndex) throws IOException {
/* 345 */     if (this.closed) throw new IllegalStateException("closed"); 
/* 346 */     if (fromIndex < 0L || toIndex < fromIndex) {
/* 347 */       throw new IllegalArgumentException(
/* 348 */           String.format("fromIndex=%s toIndex=%s", new Object[] { Long.valueOf(fromIndex), Long.valueOf(toIndex) }));
/*     */     }
/*     */     
/* 351 */     while (fromIndex < toIndex) {
/* 352 */       long result = this.buffer.indexOf(b, fromIndex, toIndex);
/* 353 */       if (result != -1L) return result;
/*     */ 
/*     */ 
/*     */       
/* 357 */       long lastBufferSize = this.buffer.size;
/* 358 */       if (lastBufferSize >= toIndex || this.source.read(this.buffer, 8192L) == -1L) return -1L;
/*     */ 
/*     */       
/* 361 */       fromIndex = Math.max(fromIndex, lastBufferSize);
/*     */     } 
/* 363 */     return -1L;
/*     */   }
/*     */   
/*     */   public long indexOf(ByteString bytes) throws IOException {
/* 367 */     return indexOf(bytes, 0L);
/*     */   }
/*     */   
/*     */   public long indexOf(ByteString bytes, long fromIndex) throws IOException {
/* 371 */     if (this.closed) throw new IllegalStateException("closed");
/*     */     
/*     */     while (true) {
/* 374 */       long result = this.buffer.indexOf(bytes, fromIndex);
/* 375 */       if (result != -1L) return result;
/*     */       
/* 377 */       long lastBufferSize = this.buffer.size;
/* 378 */       if (this.source.read(this.buffer, 8192L) == -1L) return -1L;
/*     */ 
/*     */       
/* 381 */       fromIndex = Math.max(fromIndex, lastBufferSize - bytes.size() + 1L);
/*     */     } 
/*     */   }
/*     */   
/*     */   public long indexOfElement(ByteString targetBytes) throws IOException {
/* 386 */     return indexOfElement(targetBytes, 0L);
/*     */   }
/*     */   
/*     */   public long indexOfElement(ByteString targetBytes, long fromIndex) throws IOException {
/* 390 */     if (this.closed) throw new IllegalStateException("closed");
/*     */     
/*     */     while (true) {
/* 393 */       long result = this.buffer.indexOfElement(targetBytes, fromIndex);
/* 394 */       if (result != -1L) return result;
/*     */       
/* 396 */       long lastBufferSize = this.buffer.size;
/* 397 */       if (this.source.read(this.buffer, 8192L) == -1L) return -1L;
/*     */ 
/*     */       
/* 400 */       fromIndex = Math.max(fromIndex, lastBufferSize);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean rangeEquals(long offset, ByteString bytes) throws IOException {
/* 405 */     return rangeEquals(offset, bytes, 0, bytes.size());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean rangeEquals(long offset, ByteString bytes, int bytesOffset, int byteCount) throws IOException {
/* 411 */     if (this.closed) throw new IllegalStateException("closed");
/*     */     
/* 413 */     if (offset < 0L || bytesOffset < 0 || byteCount < 0 || bytes
/*     */ 
/*     */       
/* 416 */       .size() - bytesOffset < byteCount) {
/* 417 */       return false;
/*     */     }
/* 419 */     for (int i = 0; i < byteCount; i++) {
/* 420 */       long bufferOffset = offset + i;
/* 421 */       if (!request(bufferOffset + 1L)) return false; 
/* 422 */       if (this.buffer.getByte(bufferOffset) != bytes.getByte(bytesOffset + i)) return false; 
/*     */     } 
/* 424 */     return true;
/*     */   }
/*     */   
/*     */   public BufferedSource peek() {
/* 428 */     return Okio.buffer(new PeekSource(this));
/*     */   }
/*     */   
/*     */   public InputStream inputStream() {
/* 432 */     return new InputStream() {
/*     */         public int read() throws IOException {
/* 434 */           if (RealBufferedSource.this.closed) throw new IOException("closed"); 
/* 435 */           if (RealBufferedSource.this.buffer.size == 0L) {
/* 436 */             long count = RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 8192L);
/* 437 */             if (count == -1L) return -1; 
/*     */           } 
/* 439 */           return RealBufferedSource.this.buffer.readByte() & 0xFF;
/*     */         }
/*     */         
/*     */         public int read(byte[] data, int offset, int byteCount) throws IOException {
/* 443 */           if (RealBufferedSource.this.closed) throw new IOException("closed"); 
/* 444 */           Util.checkOffsetAndCount(data.length, offset, byteCount);
/*     */           
/* 446 */           if (RealBufferedSource.this.buffer.size == 0L) {
/* 447 */             long count = RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 8192L);
/* 448 */             if (count == -1L) return -1;
/*     */           
/*     */           } 
/* 451 */           return RealBufferedSource.this.buffer.read(data, offset, byteCount);
/*     */         }
/*     */         
/*     */         public int available() throws IOException {
/* 455 */           if (RealBufferedSource.this.closed) throw new IOException("closed"); 
/* 456 */           return (int)Math.min(RealBufferedSource.this.buffer.size, 2147483647L);
/*     */         }
/*     */         
/*     */         public void close() throws IOException {
/* 460 */           RealBufferedSource.this.close();
/*     */         }
/*     */         
/*     */         public String toString() {
/* 464 */           return RealBufferedSource.this + ".inputStream()";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public boolean isOpen() {
/* 470 */     return !this.closed;
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 474 */     if (this.closed)
/* 475 */       return;  this.closed = true;
/* 476 */     this.source.close();
/* 477 */     this.buffer.clear();
/*     */   }
/*     */   
/*     */   public Timeout timeout() {
/* 481 */     return this.source.timeout();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 485 */     return "buffer(" + this.source + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okio\RealBufferedSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */