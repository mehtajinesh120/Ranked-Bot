/*      */ package okio;
/*      */ 
/*      */ import java.io.Closeable;
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.ByteChannel;
/*      */ import java.nio.charset.Charset;
/*      */ import java.security.InvalidKeyException;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import javax.annotation.Nullable;
/*      */ import javax.crypto.Mac;
/*      */ import javax.crypto.spec.SecretKeySpec;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Buffer
/*      */   implements BufferedSource, BufferedSink, Cloneable, ByteChannel
/*      */ {
/*   55 */   private static final byte[] DIGITS = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
/*      */ 
/*      */   
/*      */   static final int REPLACEMENT_CHARACTER = 65533;
/*      */   
/*      */   @Nullable
/*      */   Segment head;
/*      */   
/*      */   long size;
/*      */ 
/*      */   
/*      */   public final long size() {
/*   67 */     return this.size;
/*      */   }
/*      */   
/*      */   public Buffer buffer() {
/*   71 */     return this;
/*      */   }
/*      */   
/*      */   public Buffer getBuffer() {
/*   75 */     return this;
/*      */   }
/*      */   
/*      */   public OutputStream outputStream() {
/*   79 */     return new OutputStream() {
/*      */         public void write(int b) {
/*   81 */           Buffer.this.writeByte((byte)b);
/*      */         }
/*      */         
/*      */         public void write(byte[] data, int offset, int byteCount) {
/*   85 */           Buffer.this.write(data, offset, byteCount);
/*      */         }
/*      */ 
/*      */         
/*      */         public void flush() {}
/*      */ 
/*      */         
/*      */         public void close() {}
/*      */         
/*      */         public String toString() {
/*   95 */           return Buffer.this + ".outputStream()";
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   public Buffer emitCompleteSegments() {
/*  101 */     return this;
/*      */   }
/*      */   
/*      */   public BufferedSink emit() {
/*  105 */     return this;
/*      */   }
/*      */   
/*      */   public boolean exhausted() {
/*  109 */     return (this.size == 0L);
/*      */   }
/*      */   
/*      */   public void require(long byteCount) throws EOFException {
/*  113 */     if (this.size < byteCount) throw new EOFException(); 
/*      */   }
/*      */   
/*      */   public boolean request(long byteCount) {
/*  117 */     return (this.size >= byteCount);
/*      */   }
/*      */   
/*      */   public BufferedSource peek() {
/*  121 */     return Okio.buffer(new PeekSource(this));
/*      */   }
/*      */   
/*      */   public InputStream inputStream() {
/*  125 */     return new InputStream() {
/*      */         public int read() {
/*  127 */           if (Buffer.this.size > 0L) return Buffer.this.readByte() & 0xFF; 
/*  128 */           return -1;
/*      */         }
/*      */         
/*      */         public int read(byte[] sink, int offset, int byteCount) {
/*  132 */           return Buffer.this.read(sink, offset, byteCount);
/*      */         }
/*      */         
/*      */         public int available() {
/*  136 */           return (int)Math.min(Buffer.this.size, 2147483647L);
/*      */         }
/*      */ 
/*      */         
/*      */         public void close() {}
/*      */         
/*      */         public String toString() {
/*  143 */           return Buffer.this + ".inputStream()";
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public final Buffer copyTo(OutputStream out) throws IOException {
/*  150 */     return copyTo(out, 0L, this.size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Buffer copyTo(OutputStream out, long offset, long byteCount) throws IOException {
/*  158 */     if (out == null) throw new IllegalArgumentException("out == null"); 
/*  159 */     Util.checkOffsetAndCount(this.size, offset, byteCount);
/*  160 */     if (byteCount == 0L) return this;
/*      */ 
/*      */     
/*  163 */     Segment s = this.head;
/*  164 */     for (; offset >= (s.limit - s.pos); s = s.next) {
/*  165 */       offset -= (s.limit - s.pos);
/*      */     }
/*      */ 
/*      */     
/*  169 */     for (; byteCount > 0L; s = s.next) {
/*  170 */       int pos = (int)(s.pos + offset);
/*  171 */       int toCopy = (int)Math.min((s.limit - pos), byteCount);
/*  172 */       out.write(s.data, pos, toCopy);
/*  173 */       byteCount -= toCopy;
/*  174 */       offset = 0L;
/*      */     } 
/*      */     
/*  177 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Buffer copyTo(Buffer out, long offset, long byteCount) {
/*  182 */     if (out == null) throw new IllegalArgumentException("out == null"); 
/*  183 */     Util.checkOffsetAndCount(this.size, offset, byteCount);
/*  184 */     if (byteCount == 0L) return this;
/*      */     
/*  186 */     out.size += byteCount;
/*      */ 
/*      */     
/*  189 */     Segment s = this.head;
/*  190 */     for (; offset >= (s.limit - s.pos); s = s.next) {
/*  191 */       offset -= (s.limit - s.pos);
/*      */     }
/*      */ 
/*      */     
/*  195 */     for (; byteCount > 0L; s = s.next) {
/*  196 */       Segment copy = s.sharedCopy();
/*  197 */       copy.pos = (int)(copy.pos + offset);
/*  198 */       copy.limit = Math.min(copy.pos + (int)byteCount, copy.limit);
/*  199 */       if (out.head == null) {
/*  200 */         out.head = copy.next = copy.prev = copy;
/*      */       } else {
/*  202 */         out.head.prev.push(copy);
/*      */       } 
/*  204 */       byteCount -= (copy.limit - copy.pos);
/*  205 */       offset = 0L;
/*      */     } 
/*      */     
/*  208 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Buffer writeTo(OutputStream out) throws IOException {
/*  213 */     return writeTo(out, this.size);
/*      */   }
/*      */ 
/*      */   
/*      */   public final Buffer writeTo(OutputStream out, long byteCount) throws IOException {
/*  218 */     if (out == null) throw new IllegalArgumentException("out == null"); 
/*  219 */     Util.checkOffsetAndCount(this.size, 0L, byteCount);
/*      */     
/*  221 */     Segment s = this.head;
/*  222 */     while (byteCount > 0L) {
/*  223 */       int toCopy = (int)Math.min(byteCount, (s.limit - s.pos));
/*  224 */       out.write(s.data, s.pos, toCopy);
/*      */       
/*  226 */       s.pos += toCopy;
/*  227 */       this.size -= toCopy;
/*  228 */       byteCount -= toCopy;
/*      */       
/*  230 */       if (s.pos == s.limit) {
/*  231 */         Segment toRecycle = s;
/*  232 */         this.head = s = toRecycle.pop();
/*  233 */         SegmentPool.recycle(toRecycle);
/*      */       } 
/*      */     } 
/*      */     
/*  237 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Buffer readFrom(InputStream in) throws IOException {
/*  242 */     readFrom(in, Long.MAX_VALUE, true);
/*  243 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Buffer readFrom(InputStream in, long byteCount) throws IOException {
/*  248 */     if (byteCount < 0L) throw new IllegalArgumentException("byteCount < 0: " + byteCount); 
/*  249 */     readFrom(in, byteCount, false);
/*  250 */     return this;
/*      */   }
/*      */   
/*      */   private void readFrom(InputStream in, long byteCount, boolean forever) throws IOException {
/*  254 */     if (in == null) throw new IllegalArgumentException("in == null"); 
/*  255 */     while (byteCount > 0L || forever) {
/*  256 */       Segment tail = writableSegment(1);
/*  257 */       int maxToCopy = (int)Math.min(byteCount, (8192 - tail.limit));
/*  258 */       int bytesRead = in.read(tail.data, tail.limit, maxToCopy);
/*  259 */       if (bytesRead == -1) {
/*  260 */         if (forever)
/*  261 */           return;  throw new EOFException();
/*      */       } 
/*  263 */       tail.limit += bytesRead;
/*  264 */       this.size += bytesRead;
/*  265 */       byteCount -= bytesRead;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long completeSegmentByteCount() {
/*  275 */     long result = this.size;
/*  276 */     if (result == 0L) return 0L;
/*      */ 
/*      */     
/*  279 */     Segment tail = this.head.prev;
/*  280 */     if (tail.limit < 8192 && tail.owner) {
/*  281 */       result -= (tail.limit - tail.pos);
/*      */     }
/*      */     
/*  284 */     return result;
/*      */   }
/*      */   
/*      */   public byte readByte() {
/*  288 */     if (this.size == 0L) throw new IllegalStateException("size == 0");
/*      */     
/*  290 */     Segment segment = this.head;
/*  291 */     int pos = segment.pos;
/*  292 */     int limit = segment.limit;
/*      */     
/*  294 */     byte[] data = segment.data;
/*  295 */     byte b = data[pos++];
/*  296 */     this.size--;
/*      */     
/*  298 */     if (pos == limit) {
/*  299 */       this.head = segment.pop();
/*  300 */       SegmentPool.recycle(segment);
/*      */     } else {
/*  302 */       segment.pos = pos;
/*      */     } 
/*      */     
/*  305 */     return b;
/*      */   }
/*      */ 
/*      */   
/*      */   public final byte getByte(long pos) {
/*  310 */     Util.checkOffsetAndCount(this.size, pos, 1L);
/*  311 */     if (this.size - pos > pos) {
/*  312 */       for (Segment segment = this.head;; segment = segment.next) {
/*  313 */         int segmentByteCount = segment.limit - segment.pos;
/*  314 */         if (pos < segmentByteCount) return segment.data[segment.pos + (int)pos]; 
/*  315 */         pos -= segmentByteCount;
/*      */       } 
/*      */     }
/*  318 */     pos -= this.size;
/*  319 */     for (Segment s = this.head.prev;; s = s.prev) {
/*  320 */       pos += (s.limit - s.pos);
/*  321 */       if (pos >= 0L) return s.data[s.pos + (int)pos];
/*      */     
/*      */     } 
/*      */   }
/*      */   
/*      */   public short readShort() {
/*  327 */     if (this.size < 2L) throw new IllegalStateException("size < 2: " + this.size);
/*      */     
/*  329 */     Segment segment = this.head;
/*  330 */     int pos = segment.pos;
/*  331 */     int limit = segment.limit;
/*      */ 
/*      */     
/*  334 */     if (limit - pos < 2) {
/*      */       
/*  336 */       int i = (readByte() & 0xFF) << 8 | readByte() & 0xFF;
/*  337 */       return (short)i;
/*      */     } 
/*      */     
/*  340 */     byte[] data = segment.data;
/*  341 */     int s = (data[pos++] & 0xFF) << 8 | data[pos++] & 0xFF;
/*      */     
/*  343 */     this.size -= 2L;
/*      */     
/*  345 */     if (pos == limit) {
/*  346 */       this.head = segment.pop();
/*  347 */       SegmentPool.recycle(segment);
/*      */     } else {
/*  349 */       segment.pos = pos;
/*      */     } 
/*      */     
/*  352 */     return (short)s;
/*      */   }
/*      */   
/*      */   public int readInt() {
/*  356 */     if (this.size < 4L) throw new IllegalStateException("size < 4: " + this.size);
/*      */     
/*  358 */     Segment segment = this.head;
/*  359 */     int pos = segment.pos;
/*  360 */     int limit = segment.limit;
/*      */ 
/*      */     
/*  363 */     if (limit - pos < 4) {
/*  364 */       return (readByte() & 0xFF) << 24 | (
/*  365 */         readByte() & 0xFF) << 16 | (
/*  366 */         readByte() & 0xFF) << 8 | 
/*  367 */         readByte() & 0xFF;
/*      */     }
/*      */     
/*  370 */     byte[] data = segment.data;
/*  371 */     int i = (data[pos++] & 0xFF) << 24 | (data[pos++] & 0xFF) << 16 | (data[pos++] & 0xFF) << 8 | data[pos++] & 0xFF;
/*      */ 
/*      */ 
/*      */     
/*  375 */     this.size -= 4L;
/*      */     
/*  377 */     if (pos == limit) {
/*  378 */       this.head = segment.pop();
/*  379 */       SegmentPool.recycle(segment);
/*      */     } else {
/*  381 */       segment.pos = pos;
/*      */     } 
/*      */     
/*  384 */     return i;
/*      */   }
/*      */   
/*      */   public long readLong() {
/*  388 */     if (this.size < 8L) throw new IllegalStateException("size < 8: " + this.size);
/*      */     
/*  390 */     Segment segment = this.head;
/*  391 */     int pos = segment.pos;
/*  392 */     int limit = segment.limit;
/*      */ 
/*      */     
/*  395 */     if (limit - pos < 8) {
/*  396 */       return (readInt() & 0xFFFFFFFFL) << 32L | 
/*  397 */         readInt() & 0xFFFFFFFFL;
/*      */     }
/*      */     
/*  400 */     byte[] data = segment.data;
/*  401 */     long v = (data[pos++] & 0xFFL) << 56L | (data[pos++] & 0xFFL) << 48L | (data[pos++] & 0xFFL) << 40L | (data[pos++] & 0xFFL) << 32L | (data[pos++] & 0xFFL) << 24L | (data[pos++] & 0xFFL) << 16L | (data[pos++] & 0xFFL) << 8L | data[pos++] & 0xFFL;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  409 */     this.size -= 8L;
/*      */     
/*  411 */     if (pos == limit) {
/*  412 */       this.head = segment.pop();
/*  413 */       SegmentPool.recycle(segment);
/*      */     } else {
/*  415 */       segment.pos = pos;
/*      */     } 
/*      */     
/*  418 */     return v;
/*      */   }
/*      */   
/*      */   public short readShortLe() {
/*  422 */     return Util.reverseBytesShort(readShort());
/*      */   }
/*      */   
/*      */   public int readIntLe() {
/*  426 */     return Util.reverseBytesInt(readInt());
/*      */   }
/*      */   
/*      */   public long readLongLe() {
/*  430 */     return Util.reverseBytesLong(readLong());
/*      */   }
/*      */   
/*      */   public long readDecimalLong() {
/*  434 */     if (this.size == 0L) throw new IllegalStateException("size == 0");
/*      */ 
/*      */     
/*  437 */     long value = 0L;
/*  438 */     int seen = 0;
/*  439 */     boolean negative = false;
/*  440 */     boolean done = false;
/*      */     
/*  442 */     long overflowZone = -922337203685477580L;
/*  443 */     long overflowDigit = -7L;
/*      */     
/*      */     do {
/*  446 */       Segment segment = this.head;
/*      */       
/*  448 */       byte[] data = segment.data;
/*  449 */       int pos = segment.pos;
/*  450 */       int limit = segment.limit;
/*      */       
/*  452 */       for (; pos < limit; pos++, seen++) {
/*  453 */         byte b = data[pos];
/*  454 */         if (b >= 48 && b <= 57) {
/*  455 */           int digit = 48 - b;
/*      */ 
/*      */           
/*  458 */           if (value < overflowZone || (value == overflowZone && digit < overflowDigit)) {
/*  459 */             Buffer buffer = (new Buffer()).writeDecimalLong(value).writeByte(b);
/*  460 */             if (!negative) buffer.readByte(); 
/*  461 */             throw new NumberFormatException("Number too large: " + buffer.readUtf8());
/*      */           } 
/*  463 */           value *= 10L;
/*  464 */           value += digit;
/*  465 */         } else if (b == 45 && seen == 0) {
/*  466 */           negative = true;
/*  467 */           overflowDigit--;
/*      */         } else {
/*  469 */           if (seen == 0) {
/*  470 */             throw new NumberFormatException("Expected leading [0-9] or '-' character but was 0x" + 
/*  471 */                 Integer.toHexString(b));
/*      */           }
/*      */           
/*  474 */           done = true;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  479 */       if (pos == limit) {
/*  480 */         this.head = segment.pop();
/*  481 */         SegmentPool.recycle(segment);
/*      */       } else {
/*  483 */         segment.pos = pos;
/*      */       } 
/*  485 */     } while (!done && this.head != null);
/*      */     
/*  487 */     this.size -= seen;
/*  488 */     return negative ? value : -value;
/*      */   }
/*      */   
/*      */   public long readHexadecimalUnsignedLong() {
/*  492 */     if (this.size == 0L) throw new IllegalStateException("size == 0");
/*      */     
/*  494 */     long value = 0L;
/*  495 */     int seen = 0;
/*  496 */     boolean done = false;
/*      */     
/*      */     do {
/*  499 */       Segment segment = this.head;
/*      */       
/*  501 */       byte[] data = segment.data;
/*  502 */       int pos = segment.pos;
/*  503 */       int limit = segment.limit;
/*      */       
/*  505 */       for (; pos < limit; pos++, seen++) {
/*      */         int digit;
/*      */         
/*  508 */         byte b = data[pos];
/*  509 */         if (b >= 48 && b <= 57) {
/*  510 */           digit = b - 48;
/*  511 */         } else if (b >= 97 && b <= 102) {
/*  512 */           digit = b - 97 + 10;
/*  513 */         } else if (b >= 65 && b <= 70) {
/*  514 */           digit = b - 65 + 10;
/*      */         } else {
/*  516 */           if (seen == 0) {
/*  517 */             throw new NumberFormatException("Expected leading [0-9a-fA-F] character but was 0x" + 
/*  518 */                 Integer.toHexString(b));
/*      */           }
/*      */           
/*  521 */           done = true;
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/*  526 */         if ((value & 0xF000000000000000L) != 0L) {
/*  527 */           Buffer buffer = (new Buffer()).writeHexadecimalUnsignedLong(value).writeByte(b);
/*  528 */           throw new NumberFormatException("Number too large: " + buffer.readUtf8());
/*      */         } 
/*      */         
/*  531 */         value <<= 4L;
/*  532 */         value |= digit;
/*      */       } 
/*      */       
/*  535 */       if (pos == limit) {
/*  536 */         this.head = segment.pop();
/*  537 */         SegmentPool.recycle(segment);
/*      */       } else {
/*  539 */         segment.pos = pos;
/*      */       } 
/*  541 */     } while (!done && this.head != null);
/*      */     
/*  543 */     this.size -= seen;
/*  544 */     return value;
/*      */   }
/*      */   
/*      */   public ByteString readByteString() {
/*  548 */     return new ByteString(readByteArray());
/*      */   }
/*      */   
/*      */   public ByteString readByteString(long byteCount) throws EOFException {
/*  552 */     return new ByteString(readByteArray(byteCount));
/*      */   }
/*      */   
/*      */   public int select(Options options) {
/*  556 */     int index = selectPrefix(options, false);
/*  557 */     if (index == -1) return -1;
/*      */ 
/*      */     
/*  560 */     int selectedSize = options.byteStrings[index].size();
/*      */     try {
/*  562 */       skip(selectedSize);
/*  563 */     } catch (EOFException e) {
/*  564 */       throw new AssertionError();
/*      */     } 
/*  566 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int selectPrefix(Options options, boolean selectTruncated) {
/*  582 */     Segment head = this.head;
/*  583 */     if (head == null) {
/*  584 */       if (selectTruncated) return -2; 
/*  585 */       return options.indexOf(ByteString.EMPTY);
/*      */     } 
/*      */     
/*  588 */     Segment s = head;
/*  589 */     byte[] data = head.data;
/*  590 */     int pos = head.pos;
/*  591 */     int limit = head.limit;
/*      */     
/*  593 */     int[] trie = options.trie;
/*  594 */     int triePos = 0;
/*      */     
/*  596 */     int prefixIndex = -1;
/*      */ 
/*      */     
/*      */     while (true) {
/*  600 */       int scanOrSelect = trie[triePos++];
/*      */       
/*  602 */       int possiblePrefixIndex = trie[triePos++];
/*  603 */       if (possiblePrefixIndex != -1) {
/*  604 */         prefixIndex = possiblePrefixIndex;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  609 */       if (s == null)
/*      */         break; 
/*  611 */       if (scanOrSelect < 0)
/*      */       
/*  613 */       { int scanByteCount = -1 * scanOrSelect;
/*  614 */         int trieLimit = triePos + scanByteCount;
/*      */         while (true)
/*  616 */         { int i = data[pos++] & 0xFF;
/*  617 */           if (i != trie[triePos++]) return prefixIndex; 
/*  618 */           boolean scanComplete = (triePos == trieLimit);
/*      */ 
/*      */           
/*  621 */           if (pos == limit) {
/*  622 */             s = s.next;
/*  623 */             pos = s.pos;
/*  624 */             data = s.data;
/*  625 */             limit = s.limit;
/*  626 */             if (s == head) {
/*  627 */               if (!scanComplete)
/*  628 */                 break;  s = null;
/*      */             } 
/*      */           } 
/*      */           
/*  632 */           if (scanComplete)
/*  633 */           { int nextStep = trie[triePos];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  665 */             if (nextStep >= 0) return nextStep; 
/*  666 */             triePos = -nextStep; }  }  break; }  int selectChoiceCount = scanOrSelect; int b = data[pos++] & 0xFF; int selectLimit = triePos + selectChoiceCount; while (true) { if (triePos == selectLimit)
/*      */           return prefixIndex;  if (b == trie[triePos]) { int nextStep = trie[triePos + selectChoiceCount]; break; }  triePos++; }  if (pos == limit) { s = s.next; pos = s.pos; data = s.data; limit = s.limit; if (s == head) { s = null; continue; }
/*      */          continue; }
/*      */        continue;
/*  670 */     }  if (selectTruncated) return -2; 
/*  671 */     return prefixIndex;
/*      */   }
/*      */   
/*      */   public void readFully(Buffer sink, long byteCount) throws EOFException {
/*  675 */     if (this.size < byteCount) {
/*  676 */       sink.write(this, this.size);
/*  677 */       throw new EOFException();
/*      */     } 
/*  679 */     sink.write(this, byteCount);
/*      */   }
/*      */   
/*      */   public long readAll(Sink sink) throws IOException {
/*  683 */     long byteCount = this.size;
/*  684 */     if (byteCount > 0L) {
/*  685 */       sink.write(this, byteCount);
/*      */     }
/*  687 */     return byteCount;
/*      */   }
/*      */   
/*      */   public String readUtf8() {
/*      */     try {
/*  692 */       return readString(this.size, Util.UTF_8);
/*  693 */     } catch (EOFException e) {
/*  694 */       throw new AssertionError(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public String readUtf8(long byteCount) throws EOFException {
/*  699 */     return readString(byteCount, Util.UTF_8);
/*      */   }
/*      */   
/*      */   public String readString(Charset charset) {
/*      */     try {
/*  704 */       return readString(this.size, charset);
/*  705 */     } catch (EOFException e) {
/*  706 */       throw new AssertionError(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public String readString(long byteCount, Charset charset) throws EOFException {
/*  711 */     Util.checkOffsetAndCount(this.size, 0L, byteCount);
/*  712 */     if (charset == null) throw new IllegalArgumentException("charset == null"); 
/*  713 */     if (byteCount > 2147483647L) {
/*  714 */       throw new IllegalArgumentException("byteCount > Integer.MAX_VALUE: " + byteCount);
/*      */     }
/*  716 */     if (byteCount == 0L) return "";
/*      */     
/*  718 */     Segment s = this.head;
/*  719 */     if (s.pos + byteCount > s.limit)
/*      */     {
/*  721 */       return new String(readByteArray(byteCount), charset);
/*      */     }
/*      */     
/*  724 */     String result = new String(s.data, s.pos, (int)byteCount, charset);
/*  725 */     s.pos = (int)(s.pos + byteCount);
/*  726 */     this.size -= byteCount;
/*      */     
/*  728 */     if (s.pos == s.limit) {
/*  729 */       this.head = s.pop();
/*  730 */       SegmentPool.recycle(s);
/*      */     } 
/*      */     
/*  733 */     return result;
/*      */   }
/*      */   @Nullable
/*      */   public String readUtf8Line() throws EOFException {
/*  737 */     long newline = indexOf((byte)10);
/*      */     
/*  739 */     if (newline == -1L) {
/*  740 */       return (this.size != 0L) ? readUtf8(this.size) : null;
/*      */     }
/*      */     
/*  743 */     return readUtf8Line(newline);
/*      */   }
/*      */   
/*      */   public String readUtf8LineStrict() throws EOFException {
/*  747 */     return readUtf8LineStrict(Long.MAX_VALUE);
/*      */   }
/*      */   
/*      */   public String readUtf8LineStrict(long limit) throws EOFException {
/*  751 */     if (limit < 0L) throw new IllegalArgumentException("limit < 0: " + limit); 
/*  752 */     long scanLength = (limit == Long.MAX_VALUE) ? Long.MAX_VALUE : (limit + 1L);
/*  753 */     long newline = indexOf((byte)10, 0L, scanLength);
/*  754 */     if (newline != -1L) return readUtf8Line(newline); 
/*  755 */     if (scanLength < size() && 
/*  756 */       getByte(scanLength - 1L) == 13 && getByte(scanLength) == 10) {
/*  757 */       return readUtf8Line(scanLength);
/*      */     }
/*  759 */     Buffer data = new Buffer();
/*  760 */     copyTo(data, 0L, Math.min(32L, size()));
/*  761 */     throw new EOFException("\\n not found: limit=" + Math.min(size(), limit) + " content=" + data
/*  762 */         .readByteString().hex() + '…');
/*      */   }
/*      */   
/*      */   String readUtf8Line(long newline) throws EOFException {
/*  766 */     if (newline > 0L && getByte(newline - 1L) == 13) {
/*      */       
/*  768 */       String str = readUtf8(newline - 1L);
/*  769 */       skip(2L);
/*  770 */       return str;
/*      */     } 
/*      */ 
/*      */     
/*  774 */     String result = readUtf8(newline);
/*  775 */     skip(1L);
/*  776 */     return result;
/*      */   }
/*      */   
/*      */   public int readUtf8CodePoint() throws EOFException {
/*      */     int codePoint, byteCount, min;
/*  781 */     if (this.size == 0L) throw new EOFException();
/*      */     
/*  783 */     byte b0 = getByte(0L);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  788 */     if ((b0 & 0x80) == 0) {
/*      */       
/*  790 */       codePoint = b0 & Byte.MAX_VALUE;
/*  791 */       byteCount = 1;
/*  792 */       min = 0;
/*      */     }
/*  794 */     else if ((b0 & 0xE0) == 192) {
/*      */       
/*  796 */       codePoint = b0 & 0x1F;
/*  797 */       byteCount = 2;
/*  798 */       min = 128;
/*      */     }
/*  800 */     else if ((b0 & 0xF0) == 224) {
/*      */       
/*  802 */       codePoint = b0 & 0xF;
/*  803 */       byteCount = 3;
/*  804 */       min = 2048;
/*      */     }
/*  806 */     else if ((b0 & 0xF8) == 240) {
/*      */       
/*  808 */       codePoint = b0 & 0x7;
/*  809 */       byteCount = 4;
/*  810 */       min = 65536;
/*      */     }
/*      */     else {
/*      */       
/*  814 */       skip(1L);
/*  815 */       return 65533;
/*      */     } 
/*      */     
/*  818 */     if (this.size < byteCount) {
/*  819 */       throw new EOFException("size < " + byteCount + ": " + this.size + " (to read code point prefixed 0x" + 
/*  820 */           Integer.toHexString(b0) + ")");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  826 */     for (int i = 1; i < byteCount; i++) {
/*  827 */       byte b = getByte(i);
/*  828 */       if ((b & 0xC0) == 128) {
/*      */         
/*  830 */         codePoint <<= 6;
/*  831 */         codePoint |= b & 0x3F;
/*      */       } else {
/*  833 */         skip(i);
/*  834 */         return 65533;
/*      */       } 
/*      */     } 
/*      */     
/*  838 */     skip(byteCount);
/*      */     
/*  840 */     if (codePoint > 1114111) {
/*  841 */       return 65533;
/*      */     }
/*      */     
/*  844 */     if (codePoint >= 55296 && codePoint <= 57343) {
/*  845 */       return 65533;
/*      */     }
/*      */     
/*  848 */     if (codePoint < min) {
/*  849 */       return 65533;
/*      */     }
/*      */     
/*  852 */     return codePoint;
/*      */   }
/*      */   
/*      */   public byte[] readByteArray() {
/*      */     try {
/*  857 */       return readByteArray(this.size);
/*  858 */     } catch (EOFException e) {
/*  859 */       throw new AssertionError(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public byte[] readByteArray(long byteCount) throws EOFException {
/*  864 */     Util.checkOffsetAndCount(this.size, 0L, byteCount);
/*  865 */     if (byteCount > 2147483647L) {
/*  866 */       throw new IllegalArgumentException("byteCount > Integer.MAX_VALUE: " + byteCount);
/*      */     }
/*      */     
/*  869 */     byte[] result = new byte[(int)byteCount];
/*  870 */     readFully(result);
/*  871 */     return result;
/*      */   }
/*      */   
/*      */   public int read(byte[] sink) {
/*  875 */     return read(sink, 0, sink.length);
/*      */   }
/*      */   
/*      */   public void readFully(byte[] sink) throws EOFException {
/*  879 */     int offset = 0;
/*  880 */     while (offset < sink.length) {
/*  881 */       int read = read(sink, offset, sink.length - offset);
/*  882 */       if (read == -1) throw new EOFException(); 
/*  883 */       offset += read;
/*      */     } 
/*      */   }
/*      */   
/*      */   public int read(byte[] sink, int offset, int byteCount) {
/*  888 */     Util.checkOffsetAndCount(sink.length, offset, byteCount);
/*      */     
/*  890 */     Segment s = this.head;
/*  891 */     if (s == null) return -1; 
/*  892 */     int toCopy = Math.min(byteCount, s.limit - s.pos);
/*  893 */     System.arraycopy(s.data, s.pos, sink, offset, toCopy);
/*      */     
/*  895 */     s.pos += toCopy;
/*  896 */     this.size -= toCopy;
/*      */     
/*  898 */     if (s.pos == s.limit) {
/*  899 */       this.head = s.pop();
/*  900 */       SegmentPool.recycle(s);
/*      */     } 
/*      */     
/*  903 */     return toCopy;
/*      */   }
/*      */   
/*      */   public int read(ByteBuffer sink) throws IOException {
/*  907 */     Segment s = this.head;
/*  908 */     if (s == null) return -1;
/*      */     
/*  910 */     int toCopy = Math.min(sink.remaining(), s.limit - s.pos);
/*  911 */     sink.put(s.data, s.pos, toCopy);
/*      */     
/*  913 */     s.pos += toCopy;
/*  914 */     this.size -= toCopy;
/*      */     
/*  916 */     if (s.pos == s.limit) {
/*  917 */       this.head = s.pop();
/*  918 */       SegmentPool.recycle(s);
/*      */     } 
/*      */     
/*  921 */     return toCopy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void clear() {
/*      */     try {
/*  930 */       skip(this.size);
/*  931 */     } catch (EOFException e) {
/*  932 */       throw new AssertionError(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void skip(long byteCount) throws EOFException {
/*  938 */     while (byteCount > 0L) {
/*  939 */       if (this.head == null) throw new EOFException();
/*      */       
/*  941 */       int toSkip = (int)Math.min(byteCount, (this.head.limit - this.head.pos));
/*  942 */       this.size -= toSkip;
/*  943 */       byteCount -= toSkip;
/*  944 */       this.head.pos += toSkip;
/*      */       
/*  946 */       if (this.head.pos == this.head.limit) {
/*  947 */         Segment toRecycle = this.head;
/*  948 */         this.head = toRecycle.pop();
/*  949 */         SegmentPool.recycle(toRecycle);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public Buffer write(ByteString byteString) {
/*  955 */     if (byteString == null) throw new IllegalArgumentException("byteString == null"); 
/*  956 */     byteString.write(this);
/*  957 */     return this;
/*      */   }
/*      */   
/*      */   public Buffer writeUtf8(String string) {
/*  961 */     return writeUtf8(string, 0, string.length());
/*      */   }
/*      */   
/*      */   public Buffer writeUtf8(String string, int beginIndex, int endIndex) {
/*  965 */     if (string == null) throw new IllegalArgumentException("string == null"); 
/*  966 */     if (beginIndex < 0) throw new IllegalArgumentException("beginIndex < 0: " + beginIndex); 
/*  967 */     if (endIndex < beginIndex) {
/*  968 */       throw new IllegalArgumentException("endIndex < beginIndex: " + endIndex + " < " + beginIndex);
/*      */     }
/*  970 */     if (endIndex > string.length()) {
/*  971 */       throw new IllegalArgumentException("endIndex > string.length: " + endIndex + " > " + string
/*  972 */           .length());
/*      */     }
/*      */ 
/*      */     
/*  976 */     for (int i = beginIndex; i < endIndex; ) {
/*  977 */       int c = string.charAt(i);
/*      */       
/*  979 */       if (c < 128) {
/*  980 */         Segment tail = writableSegment(1);
/*  981 */         byte[] data = tail.data;
/*  982 */         int segmentOffset = tail.limit - i;
/*  983 */         int runLimit = Math.min(endIndex, 8192 - segmentOffset);
/*      */ 
/*      */         
/*  986 */         data[segmentOffset + i++] = (byte)c;
/*      */ 
/*      */ 
/*      */         
/*  990 */         while (i < runLimit) {
/*  991 */           c = string.charAt(i);
/*  992 */           if (c >= 128)
/*  993 */             break;  data[segmentOffset + i++] = (byte)c;
/*      */         } 
/*      */         
/*  996 */         int runSize = i + segmentOffset - tail.limit;
/*  997 */         tail.limit += runSize;
/*  998 */         this.size += runSize; continue;
/*      */       } 
/* 1000 */       if (c < 2048) {
/*      */         
/* 1002 */         writeByte(c >> 6 | 0xC0);
/* 1003 */         writeByte(c & 0x3F | 0x80);
/* 1004 */         i++; continue;
/*      */       } 
/* 1006 */       if (c < 55296 || c > 57343) {
/*      */         
/* 1008 */         writeByte(c >> 12 | 0xE0);
/* 1009 */         writeByte(c >> 6 & 0x3F | 0x80);
/* 1010 */         writeByte(c & 0x3F | 0x80);
/* 1011 */         i++;
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/* 1016 */       int low = (i + 1 < endIndex) ? string.charAt(i + 1) : 0;
/* 1017 */       if (c > 56319 || low < 56320 || low > 57343) {
/* 1018 */         writeByte(63);
/* 1019 */         i++;
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/*      */ 
/*      */       
/* 1026 */       int codePoint = 65536 + ((c & 0xFFFF27FF) << 10 | low & 0xFFFF23FF);
/*      */ 
/*      */       
/* 1029 */       writeByte(codePoint >> 18 | 0xF0);
/* 1030 */       writeByte(codePoint >> 12 & 0x3F | 0x80);
/* 1031 */       writeByte(codePoint >> 6 & 0x3F | 0x80);
/* 1032 */       writeByte(codePoint & 0x3F | 0x80);
/* 1033 */       i += 2;
/*      */     } 
/*      */ 
/*      */     
/* 1037 */     return this;
/*      */   }
/*      */   
/*      */   public Buffer writeUtf8CodePoint(int codePoint) {
/* 1041 */     if (codePoint < 128) {
/*      */       
/* 1043 */       writeByte(codePoint);
/*      */     }
/* 1045 */     else if (codePoint < 2048) {
/*      */       
/* 1047 */       writeByte(codePoint >> 6 | 0xC0);
/* 1048 */       writeByte(codePoint & 0x3F | 0x80);
/*      */     }
/* 1050 */     else if (codePoint < 65536) {
/* 1051 */       if (codePoint >= 55296 && codePoint <= 57343) {
/*      */         
/* 1053 */         writeByte(63);
/*      */       } else {
/*      */         
/* 1056 */         writeByte(codePoint >> 12 | 0xE0);
/* 1057 */         writeByte(codePoint >> 6 & 0x3F | 0x80);
/* 1058 */         writeByte(codePoint & 0x3F | 0x80);
/*      */       }
/*      */     
/* 1061 */     } else if (codePoint <= 1114111) {
/*      */       
/* 1063 */       writeByte(codePoint >> 18 | 0xF0);
/* 1064 */       writeByte(codePoint >> 12 & 0x3F | 0x80);
/* 1065 */       writeByte(codePoint >> 6 & 0x3F | 0x80);
/* 1066 */       writeByte(codePoint & 0x3F | 0x80);
/*      */     } else {
/*      */       
/* 1069 */       throw new IllegalArgumentException("Unexpected code point: " + 
/* 1070 */           Integer.toHexString(codePoint));
/*      */     } 
/*      */     
/* 1073 */     return this;
/*      */   }
/*      */   
/*      */   public Buffer writeString(String string, Charset charset) {
/* 1077 */     return writeString(string, 0, string.length(), charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public Buffer writeString(String string, int beginIndex, int endIndex, Charset charset) {
/* 1082 */     if (string == null) throw new IllegalArgumentException("string == null"); 
/* 1083 */     if (beginIndex < 0) throw new IllegalAccessError("beginIndex < 0: " + beginIndex); 
/* 1084 */     if (endIndex < beginIndex) {
/* 1085 */       throw new IllegalArgumentException("endIndex < beginIndex: " + endIndex + " < " + beginIndex);
/*      */     }
/* 1087 */     if (endIndex > string.length()) {
/* 1088 */       throw new IllegalArgumentException("endIndex > string.length: " + endIndex + " > " + string
/* 1089 */           .length());
/*      */     }
/* 1091 */     if (charset == null) throw new IllegalArgumentException("charset == null"); 
/* 1092 */     if (charset.equals(Util.UTF_8)) return writeUtf8(string, beginIndex, endIndex); 
/* 1093 */     byte[] data = string.substring(beginIndex, endIndex).getBytes(charset);
/* 1094 */     return write(data, 0, data.length);
/*      */   }
/*      */   
/*      */   public Buffer write(byte[] source) {
/* 1098 */     if (source == null) throw new IllegalArgumentException("source == null"); 
/* 1099 */     return write(source, 0, source.length);
/*      */   }
/*      */   
/*      */   public Buffer write(byte[] source, int offset, int byteCount) {
/* 1103 */     if (source == null) throw new IllegalArgumentException("source == null"); 
/* 1104 */     Util.checkOffsetAndCount(source.length, offset, byteCount);
/*      */     
/* 1106 */     int limit = offset + byteCount;
/* 1107 */     while (offset < limit) {
/* 1108 */       Segment tail = writableSegment(1);
/*      */       
/* 1110 */       int toCopy = Math.min(limit - offset, 8192 - tail.limit);
/* 1111 */       System.arraycopy(source, offset, tail.data, tail.limit, toCopy);
/*      */       
/* 1113 */       offset += toCopy;
/* 1114 */       tail.limit += toCopy;
/*      */     } 
/*      */     
/* 1117 */     this.size += byteCount;
/* 1118 */     return this;
/*      */   }
/*      */   
/*      */   public int write(ByteBuffer source) throws IOException {
/* 1122 */     if (source == null) throw new IllegalArgumentException("source == null");
/*      */     
/* 1124 */     int byteCount = source.remaining();
/* 1125 */     int remaining = byteCount;
/* 1126 */     while (remaining > 0) {
/* 1127 */       Segment tail = writableSegment(1);
/*      */       
/* 1129 */       int toCopy = Math.min(remaining, 8192 - tail.limit);
/* 1130 */       source.get(tail.data, tail.limit, toCopy);
/*      */       
/* 1132 */       remaining -= toCopy;
/* 1133 */       tail.limit += toCopy;
/*      */     } 
/*      */     
/* 1136 */     this.size += byteCount;
/* 1137 */     return byteCount;
/*      */   }
/*      */   
/*      */   public long writeAll(Source source) throws IOException {
/* 1141 */     if (source == null) throw new IllegalArgumentException("source == null"); 
/* 1142 */     long totalBytesRead = 0L; long readCount;
/* 1143 */     while ((readCount = source.read(this, 8192L)) != -1L) {
/* 1144 */       totalBytesRead += readCount;
/*      */     }
/* 1146 */     return totalBytesRead;
/*      */   }
/*      */   
/*      */   public BufferedSink write(Source source, long byteCount) throws IOException {
/* 1150 */     while (byteCount > 0L) {
/* 1151 */       long read = source.read(this, byteCount);
/* 1152 */       if (read == -1L) throw new EOFException(); 
/* 1153 */       byteCount -= read;
/*      */     } 
/* 1155 */     return this;
/*      */   }
/*      */   
/*      */   public Buffer writeByte(int b) {
/* 1159 */     Segment tail = writableSegment(1);
/* 1160 */     tail.data[tail.limit++] = (byte)b;
/* 1161 */     this.size++;
/* 1162 */     return this;
/*      */   }
/*      */   
/*      */   public Buffer writeShort(int s) {
/* 1166 */     Segment tail = writableSegment(2);
/* 1167 */     byte[] data = tail.data;
/* 1168 */     int limit = tail.limit;
/* 1169 */     data[limit++] = (byte)(s >>> 8 & 0xFF);
/* 1170 */     data[limit++] = (byte)(s & 0xFF);
/* 1171 */     tail.limit = limit;
/* 1172 */     this.size += 2L;
/* 1173 */     return this;
/*      */   }
/*      */   
/*      */   public Buffer writeShortLe(int s) {
/* 1177 */     return writeShort(Util.reverseBytesShort((short)s));
/*      */   }
/*      */   
/*      */   public Buffer writeInt(int i) {
/* 1181 */     Segment tail = writableSegment(4);
/* 1182 */     byte[] data = tail.data;
/* 1183 */     int limit = tail.limit;
/* 1184 */     data[limit++] = (byte)(i >>> 24 & 0xFF);
/* 1185 */     data[limit++] = (byte)(i >>> 16 & 0xFF);
/* 1186 */     data[limit++] = (byte)(i >>> 8 & 0xFF);
/* 1187 */     data[limit++] = (byte)(i & 0xFF);
/* 1188 */     tail.limit = limit;
/* 1189 */     this.size += 4L;
/* 1190 */     return this;
/*      */   }
/*      */   
/*      */   public Buffer writeIntLe(int i) {
/* 1194 */     return writeInt(Util.reverseBytesInt(i));
/*      */   }
/*      */   
/*      */   public Buffer writeLong(long v) {
/* 1198 */     Segment tail = writableSegment(8);
/* 1199 */     byte[] data = tail.data;
/* 1200 */     int limit = tail.limit;
/* 1201 */     data[limit++] = (byte)(int)(v >>> 56L & 0xFFL);
/* 1202 */     data[limit++] = (byte)(int)(v >>> 48L & 0xFFL);
/* 1203 */     data[limit++] = (byte)(int)(v >>> 40L & 0xFFL);
/* 1204 */     data[limit++] = (byte)(int)(v >>> 32L & 0xFFL);
/* 1205 */     data[limit++] = (byte)(int)(v >>> 24L & 0xFFL);
/* 1206 */     data[limit++] = (byte)(int)(v >>> 16L & 0xFFL);
/* 1207 */     data[limit++] = (byte)(int)(v >>> 8L & 0xFFL);
/* 1208 */     data[limit++] = (byte)(int)(v & 0xFFL);
/* 1209 */     tail.limit = limit;
/* 1210 */     this.size += 8L;
/* 1211 */     return this;
/*      */   }
/*      */   
/*      */   public Buffer writeLongLe(long v) {
/* 1215 */     return writeLong(Util.reverseBytesLong(v));
/*      */   }
/*      */   
/*      */   public Buffer writeDecimalLong(long v) {
/* 1219 */     if (v == 0L)
/*      */     {
/* 1221 */       return writeByte(48);
/*      */     }
/*      */     
/* 1224 */     boolean negative = false;
/* 1225 */     if (v < 0L) {
/* 1226 */       v = -v;
/* 1227 */       if (v < 0L) {
/* 1228 */         return writeUtf8("-9223372036854775808");
/*      */       }
/* 1230 */       negative = true;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1252 */     int width = (v < 100000000L) ? ((v < 10000L) ? ((v < 100L) ? ((v < 10L) ? 1 : 2) : ((v < 1000L) ? 3 : 4)) : ((v < 1000000L) ? ((v < 100000L) ? 5 : 6) : ((v < 10000000L) ? 7 : 8))) : ((v < 1000000000000L) ? ((v < 10000000000L) ? ((v < 1000000000L) ? 9 : 10) : ((v < 100000000000L) ? 11 : 12)) : ((v < 1000000000000000L) ? ((v < 10000000000000L) ? 13 : ((v < 100000000000000L) ? 14 : 15)) : ((v < 100000000000000000L) ? ((v < 10000000000000000L) ? 16 : 17) : ((v < 1000000000000000000L) ? 18 : 19))));
/* 1253 */     if (negative) {
/* 1254 */       width++;
/*      */     }
/*      */     
/* 1257 */     Segment tail = writableSegment(width);
/* 1258 */     byte[] data = tail.data;
/* 1259 */     int pos = tail.limit + width;
/* 1260 */     while (v != 0L) {
/* 1261 */       int digit = (int)(v % 10L);
/* 1262 */       data[--pos] = DIGITS[digit];
/* 1263 */       v /= 10L;
/*      */     } 
/* 1265 */     if (negative) {
/* 1266 */       data[--pos] = 45;
/*      */     }
/*      */     
/* 1269 */     tail.limit += width;
/* 1270 */     this.size += width;
/* 1271 */     return this;
/*      */   }
/*      */   
/*      */   public Buffer writeHexadecimalUnsignedLong(long v) {
/* 1275 */     if (v == 0L)
/*      */     {
/* 1277 */       return writeByte(48);
/*      */     }
/*      */     
/* 1280 */     int width = Long.numberOfTrailingZeros(Long.highestOneBit(v)) / 4 + 1;
/*      */     
/* 1282 */     Segment tail = writableSegment(width);
/* 1283 */     byte[] data = tail.data;
/* 1284 */     for (int pos = tail.limit + width - 1, start = tail.limit; pos >= start; pos--) {
/* 1285 */       data[pos] = DIGITS[(int)(v & 0xFL)];
/* 1286 */       v >>>= 4L;
/*      */     } 
/* 1288 */     tail.limit += width;
/* 1289 */     this.size += width;
/* 1290 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Segment writableSegment(int minimumCapacity) {
/* 1298 */     if (minimumCapacity < 1 || minimumCapacity > 8192) throw new IllegalArgumentException();
/*      */     
/* 1300 */     if (this.head == null) {
/* 1301 */       this.head = SegmentPool.take();
/* 1302 */       return this.head.next = this.head.prev = this.head;
/*      */     } 
/*      */     
/* 1305 */     Segment tail = this.head.prev;
/* 1306 */     if (tail.limit + minimumCapacity > 8192 || !tail.owner) {
/* 1307 */       tail = tail.push(SegmentPool.take());
/*      */     }
/* 1309 */     return tail;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write(Buffer source, long byteCount) {
/* 1363 */     if (source == null) throw new IllegalArgumentException("source == null"); 
/* 1364 */     if (source == this) throw new IllegalArgumentException("source == this"); 
/* 1365 */     Util.checkOffsetAndCount(source.size, 0L, byteCount);
/*      */     
/* 1367 */     while (byteCount > 0L) {
/*      */       
/* 1369 */       if (byteCount < (source.head.limit - source.head.pos)) {
/* 1370 */         Segment tail = (this.head != null) ? this.head.prev : null;
/* 1371 */         if (tail != null && tail.owner && byteCount + tail.limit - (tail.shared ? 
/* 1372 */           0L : tail.pos) <= 8192L) {
/*      */           
/* 1374 */           source.head.writeTo(tail, (int)byteCount);
/* 1375 */           source.size -= byteCount;
/* 1376 */           this.size += byteCount;
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/* 1381 */         source.head = source.head.split((int)byteCount);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1386 */       Segment segmentToMove = source.head;
/* 1387 */       long movedByteCount = (segmentToMove.limit - segmentToMove.pos);
/* 1388 */       source.head = segmentToMove.pop();
/* 1389 */       if (this.head == null) {
/* 1390 */         this.head = segmentToMove;
/* 1391 */         this.head.next = this.head.prev = this.head;
/*      */       } else {
/* 1393 */         Segment tail = this.head.prev;
/* 1394 */         tail = tail.push(segmentToMove);
/* 1395 */         tail.compact();
/*      */       } 
/* 1397 */       source.size -= movedByteCount;
/* 1398 */       this.size += movedByteCount;
/* 1399 */       byteCount -= movedByteCount;
/*      */     } 
/*      */   }
/*      */   
/*      */   public long read(Buffer sink, long byteCount) {
/* 1404 */     if (sink == null) throw new IllegalArgumentException("sink == null"); 
/* 1405 */     if (byteCount < 0L) throw new IllegalArgumentException("byteCount < 0: " + byteCount); 
/* 1406 */     if (this.size == 0L) return -1L; 
/* 1407 */     if (byteCount > this.size) byteCount = this.size; 
/* 1408 */     sink.write(this, byteCount);
/* 1409 */     return byteCount;
/*      */   }
/*      */   
/*      */   public long indexOf(byte b) {
/* 1413 */     return indexOf(b, 0L, Long.MAX_VALUE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long indexOf(byte b, long fromIndex) {
/* 1421 */     return indexOf(b, fromIndex, Long.MAX_VALUE);
/*      */   }
/*      */   public long indexOf(byte b, long fromIndex, long toIndex) {
/*      */     long offset;
/* 1425 */     if (fromIndex < 0L || toIndex < fromIndex) {
/* 1426 */       throw new IllegalArgumentException(
/* 1427 */           String.format("size=%s fromIndex=%s toIndex=%s", new Object[] { Long.valueOf(this.size), Long.valueOf(fromIndex), Long.valueOf(toIndex) }));
/*      */     }
/*      */     
/* 1430 */     if (toIndex > this.size) toIndex = this.size; 
/* 1431 */     if (fromIndex == toIndex) return -1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1439 */     Segment s = this.head;
/* 1440 */     if (s == null)
/*      */     {
/* 1442 */       return -1L; } 
/* 1443 */     if (this.size - fromIndex < fromIndex) {
/*      */       
/* 1445 */       offset = this.size;
/* 1446 */       while (offset > fromIndex) {
/* 1447 */         s = s.prev;
/* 1448 */         offset -= (s.limit - s.pos);
/*      */       } 
/*      */     } else {
/*      */       
/* 1452 */       offset = 0L; long nextOffset;
/* 1453 */       while ((nextOffset = offset + (s.limit - s.pos)) < fromIndex) {
/* 1454 */         s = s.next;
/* 1455 */         offset = nextOffset;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1461 */     while (offset < toIndex) {
/* 1462 */       byte[] data = s.data;
/* 1463 */       int limit = (int)Math.min(s.limit, s.pos + toIndex - offset);
/* 1464 */       int pos = (int)(s.pos + fromIndex - offset);
/* 1465 */       for (; pos < limit; pos++) {
/* 1466 */         if (data[pos] == b) {
/* 1467 */           return (pos - s.pos) + offset;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1472 */       offset += (s.limit - s.pos);
/* 1473 */       fromIndex = offset;
/* 1474 */       s = s.next;
/*      */     } 
/*      */     
/* 1477 */     return -1L;
/*      */   }
/*      */   
/*      */   public long indexOf(ByteString bytes) throws IOException {
/* 1481 */     return indexOf(bytes, 0L);
/*      */   }
/*      */   public long indexOf(ByteString bytes, long fromIndex) throws IOException {
/*      */     long offset;
/* 1485 */     if (bytes.size() == 0) throw new IllegalArgumentException("bytes is empty"); 
/* 1486 */     if (fromIndex < 0L) throw new IllegalArgumentException("fromIndex < 0");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1494 */     Segment s = this.head;
/* 1495 */     if (s == null)
/*      */     {
/* 1497 */       return -1L; } 
/* 1498 */     if (this.size - fromIndex < fromIndex) {
/*      */       
/* 1500 */       offset = this.size;
/* 1501 */       while (offset > fromIndex) {
/* 1502 */         s = s.prev;
/* 1503 */         offset -= (s.limit - s.pos);
/*      */       } 
/*      */     } else {
/*      */       
/* 1507 */       offset = 0L; long nextOffset;
/* 1508 */       while ((nextOffset = offset + (s.limit - s.pos)) < fromIndex) {
/* 1509 */         s = s.next;
/* 1510 */         offset = nextOffset;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1517 */     byte b0 = bytes.getByte(0);
/* 1518 */     int bytesSize = bytes.size();
/* 1519 */     long resultLimit = this.size - bytesSize + 1L;
/* 1520 */     while (offset < resultLimit) {
/*      */       
/* 1522 */       byte[] data = s.data;
/* 1523 */       int segmentLimit = (int)Math.min(s.limit, s.pos + resultLimit - offset);
/* 1524 */       for (int pos = (int)(s.pos + fromIndex - offset); pos < segmentLimit; pos++) {
/* 1525 */         if (data[pos] == b0 && rangeEquals(s, pos + 1, bytes, 1, bytesSize)) {
/* 1526 */           return (pos - s.pos) + offset;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1531 */       offset += (s.limit - s.pos);
/* 1532 */       fromIndex = offset;
/* 1533 */       s = s.next;
/*      */     } 
/*      */     
/* 1536 */     return -1L;
/*      */   }
/*      */   
/*      */   public long indexOfElement(ByteString targetBytes) {
/* 1540 */     return indexOfElement(targetBytes, 0L);
/*      */   }
/*      */   public long indexOfElement(ByteString targetBytes, long fromIndex) {
/*      */     long offset;
/* 1544 */     if (fromIndex < 0L) throw new IllegalArgumentException("fromIndex < 0");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1552 */     Segment s = this.head;
/* 1553 */     if (s == null)
/*      */     {
/* 1555 */       return -1L; } 
/* 1556 */     if (this.size - fromIndex < fromIndex) {
/*      */       
/* 1558 */       offset = this.size;
/* 1559 */       while (offset > fromIndex) {
/* 1560 */         s = s.prev;
/* 1561 */         offset -= (s.limit - s.pos);
/*      */       } 
/*      */     } else {
/*      */       
/* 1565 */       offset = 0L; long nextOffset;
/* 1566 */       while ((nextOffset = offset + (s.limit - s.pos)) < fromIndex) {
/* 1567 */         s = s.next;
/* 1568 */         offset = nextOffset;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1576 */     if (targetBytes.size() == 2) {
/*      */       
/* 1578 */       byte b0 = targetBytes.getByte(0);
/* 1579 */       byte b1 = targetBytes.getByte(1);
/* 1580 */       while (offset < this.size) {
/* 1581 */         byte[] data = s.data;
/* 1582 */         for (int pos = (int)(s.pos + fromIndex - offset), limit = s.limit; pos < limit; pos++) {
/* 1583 */           int b = data[pos];
/* 1584 */           if (b == b0 || b == b1) {
/* 1585 */             return (pos - s.pos) + offset;
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/* 1590 */         offset += (s.limit - s.pos);
/* 1591 */         fromIndex = offset;
/* 1592 */         s = s.next;
/*      */       } 
/*      */     } else {
/*      */       
/* 1596 */       byte[] targetByteArray = targetBytes.internalArray();
/* 1597 */       while (offset < this.size) {
/* 1598 */         byte[] data = s.data;
/* 1599 */         for (int pos = (int)(s.pos + fromIndex - offset), limit = s.limit; pos < limit; pos++) {
/* 1600 */           int b = data[pos];
/* 1601 */           for (byte t : targetByteArray) {
/* 1602 */             if (b == t) return (pos - s.pos) + offset;
/*      */           
/*      */           } 
/*      */         } 
/*      */         
/* 1607 */         offset += (s.limit - s.pos);
/* 1608 */         fromIndex = offset;
/* 1609 */         s = s.next;
/*      */       } 
/*      */     } 
/*      */     
/* 1613 */     return -1L;
/*      */   }
/*      */   
/*      */   public boolean rangeEquals(long offset, ByteString bytes) {
/* 1617 */     return rangeEquals(offset, bytes, 0, bytes.size());
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean rangeEquals(long offset, ByteString bytes, int bytesOffset, int byteCount) {
/* 1622 */     if (offset < 0L || bytesOffset < 0 || byteCount < 0 || this.size - offset < byteCount || bytes
/*      */ 
/*      */ 
/*      */       
/* 1626 */       .size() - bytesOffset < byteCount) {
/* 1627 */       return false;
/*      */     }
/* 1629 */     for (int i = 0; i < byteCount; i++) {
/* 1630 */       if (getByte(offset + i) != bytes.getByte(bytesOffset + i)) {
/* 1631 */         return false;
/*      */       }
/*      */     } 
/* 1634 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean rangeEquals(Segment segment, int segmentPos, ByteString bytes, int bytesOffset, int bytesLimit) {
/* 1643 */     int segmentLimit = segment.limit;
/* 1644 */     byte[] data = segment.data;
/*      */     
/* 1646 */     for (int i = bytesOffset; i < bytesLimit; ) {
/* 1647 */       if (segmentPos == segmentLimit) {
/* 1648 */         segment = segment.next;
/* 1649 */         data = segment.data;
/* 1650 */         segmentPos = segment.pos;
/* 1651 */         segmentLimit = segment.limit;
/*      */       } 
/*      */       
/* 1654 */       if (data[segmentPos] != bytes.getByte(i)) {
/* 1655 */         return false;
/*      */       }
/*      */       
/* 1658 */       segmentPos++;
/* 1659 */       i++;
/*      */     } 
/*      */     
/* 1662 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void flush() {}
/*      */   
/*      */   public boolean isOpen() {
/* 1669 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() {}
/*      */   
/*      */   public Timeout timeout() {
/* 1676 */     return Timeout.NONE;
/*      */   }
/*      */ 
/*      */   
/*      */   List<Integer> segmentSizes() {
/* 1681 */     if (this.head == null) return Collections.emptyList(); 
/* 1682 */     List<Integer> result = new ArrayList<>();
/* 1683 */     result.add(Integer.valueOf(this.head.limit - this.head.pos));
/* 1684 */     for (Segment s = this.head.next; s != this.head; s = s.next) {
/* 1685 */       result.add(Integer.valueOf(s.limit - s.pos));
/*      */     }
/* 1687 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteString md5() {
/* 1692 */     return digest("MD5");
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteString sha1() {
/* 1697 */     return digest("SHA-1");
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteString sha256() {
/* 1702 */     return digest("SHA-256");
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteString sha512() {
/* 1707 */     return digest("SHA-512");
/*      */   }
/*      */   
/*      */   private ByteString digest(String algorithm) {
/*      */     try {
/* 1712 */       MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
/* 1713 */       if (this.head != null) {
/* 1714 */         messageDigest.update(this.head.data, this.head.pos, this.head.limit - this.head.pos);
/* 1715 */         for (Segment s = this.head.next; s != this.head; s = s.next) {
/* 1716 */           messageDigest.update(s.data, s.pos, s.limit - s.pos);
/*      */         }
/*      */       } 
/* 1719 */       return ByteString.of(messageDigest.digest());
/* 1720 */     } catch (NoSuchAlgorithmException e) {
/* 1721 */       throw new AssertionError();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteString hmacSha1(ByteString key) {
/* 1727 */     return hmac("HmacSHA1", key);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteString hmacSha256(ByteString key) {
/* 1732 */     return hmac("HmacSHA256", key);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteString hmacSha512(ByteString key) {
/* 1737 */     return hmac("HmacSHA512", key);
/*      */   }
/*      */   
/*      */   private ByteString hmac(String algorithm, ByteString key) {
/*      */     try {
/* 1742 */       Mac mac = Mac.getInstance(algorithm);
/* 1743 */       mac.init(new SecretKeySpec(key.toByteArray(), algorithm));
/* 1744 */       if (this.head != null) {
/* 1745 */         mac.update(this.head.data, this.head.pos, this.head.limit - this.head.pos);
/* 1746 */         for (Segment s = this.head.next; s != this.head; s = s.next) {
/* 1747 */           mac.update(s.data, s.pos, s.limit - s.pos);
/*      */         }
/*      */       } 
/* 1750 */       return ByteString.of(mac.doFinal());
/* 1751 */     } catch (NoSuchAlgorithmException e) {
/* 1752 */       throw new AssertionError();
/* 1753 */     } catch (InvalidKeyException e) {
/* 1754 */       throw new IllegalArgumentException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean equals(Object o) {
/* 1759 */     if (this == o) return true; 
/* 1760 */     if (!(o instanceof Buffer)) return false; 
/* 1761 */     Buffer that = (Buffer)o;
/* 1762 */     if (this.size != that.size) return false; 
/* 1763 */     if (this.size == 0L) return true;
/*      */     
/* 1765 */     Segment sa = this.head;
/* 1766 */     Segment sb = that.head;
/* 1767 */     int posA = sa.pos;
/* 1768 */     int posB = sb.pos;
/*      */     long pos;
/* 1770 */     for (pos = 0L; pos < this.size; pos += count) {
/* 1771 */       long count = Math.min(sa.limit - posA, sb.limit - posB);
/*      */       
/* 1773 */       for (int i = 0; i < count; i++) {
/* 1774 */         if (sa.data[posA++] != sb.data[posB++]) return false;
/*      */       
/*      */       } 
/* 1777 */       if (posA == sa.limit) {
/* 1778 */         sa = sa.next;
/* 1779 */         posA = sa.pos;
/*      */       } 
/*      */       
/* 1782 */       if (posB == sb.limit) {
/* 1783 */         sb = sb.next;
/* 1784 */         posB = sb.pos;
/*      */       } 
/*      */     } 
/*      */     
/* 1788 */     return true;
/*      */   }
/*      */   
/*      */   public int hashCode() {
/* 1792 */     Segment s = this.head;
/* 1793 */     if (s == null) return 0; 
/* 1794 */     int result = 1;
/*      */     while (true) {
/* 1796 */       for (int pos = s.pos, limit = s.limit; pos < limit; pos++) {
/* 1797 */         result = 31 * result + s.data[pos];
/*      */       }
/* 1799 */       s = s.next;
/* 1800 */       if (s == this.head) {
/* 1801 */         return result;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1809 */     return snapshot().toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public Buffer clone() {
/* 1814 */     Buffer result = new Buffer();
/* 1815 */     if (this.size == 0L) return result;
/*      */     
/* 1817 */     result.head = this.head.sharedCopy();
/* 1818 */     result.head.next = result.head.prev = result.head;
/* 1819 */     for (Segment s = this.head.next; s != this.head; s = s.next) {
/* 1820 */       result.head.prev.push(s.sharedCopy());
/*      */     }
/* 1822 */     result.size = this.size;
/* 1823 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ByteString snapshot() {
/* 1828 */     if (this.size > 2147483647L) {
/* 1829 */       throw new IllegalArgumentException("size > Integer.MAX_VALUE: " + this.size);
/*      */     }
/* 1831 */     return snapshot((int)this.size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ByteString snapshot(int byteCount) {
/* 1838 */     if (byteCount == 0) return ByteString.EMPTY; 
/* 1839 */     return new SegmentedByteString(this, byteCount);
/*      */   }
/*      */   
/*      */   public final UnsafeCursor readUnsafe() {
/* 1843 */     return readUnsafe(new UnsafeCursor());
/*      */   }
/*      */   
/*      */   public final UnsafeCursor readUnsafe(UnsafeCursor unsafeCursor) {
/* 1847 */     if (unsafeCursor.buffer != null) {
/* 1848 */       throw new IllegalStateException("already attached to a buffer");
/*      */     }
/*      */     
/* 1851 */     unsafeCursor.buffer = this;
/* 1852 */     unsafeCursor.readWrite = false;
/* 1853 */     return unsafeCursor;
/*      */   }
/*      */   
/*      */   public final UnsafeCursor readAndWriteUnsafe() {
/* 1857 */     return readAndWriteUnsafe(new UnsafeCursor());
/*      */   }
/*      */   
/*      */   public final UnsafeCursor readAndWriteUnsafe(UnsafeCursor unsafeCursor) {
/* 1861 */     if (unsafeCursor.buffer != null) {
/* 1862 */       throw new IllegalStateException("already attached to a buffer");
/*      */     }
/*      */     
/* 1865 */     unsafeCursor.buffer = this;
/* 1866 */     unsafeCursor.readWrite = true;
/* 1867 */     return unsafeCursor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class UnsafeCursor
/*      */     implements Closeable
/*      */   {
/*      */     public Buffer buffer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean readWrite;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Segment segment;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2075 */     public long offset = -1L;
/*      */     public byte[] data;
/* 2077 */     public int start = -1;
/* 2078 */     public int end = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final int next() {
/* 2086 */       if (this.offset == this.buffer.size) throw new IllegalStateException(); 
/* 2087 */       if (this.offset == -1L) return seek(0L); 
/* 2088 */       return seek(this.offset + (this.end - this.start));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final int seek(long offset) {
/*      */       Segment next;
/*      */       long nextOffset;
/* 2097 */       if (offset < -1L || offset > this.buffer.size) {
/* 2098 */         throw new ArrayIndexOutOfBoundsException(
/* 2099 */             String.format("offset=%s > size=%s", new Object[] { Long.valueOf(offset), Long.valueOf(this.buffer.size) }));
/*      */       }
/*      */       
/* 2102 */       if (offset == -1L || offset == this.buffer.size) {
/* 2103 */         this.segment = null;
/* 2104 */         this.offset = offset;
/* 2105 */         this.data = null;
/* 2106 */         this.start = -1;
/* 2107 */         this.end = -1;
/* 2108 */         return -1;
/*      */       } 
/*      */ 
/*      */       
/* 2112 */       long min = 0L;
/* 2113 */       long max = this.buffer.size;
/* 2114 */       Segment head = this.buffer.head;
/* 2115 */       Segment tail = this.buffer.head;
/* 2116 */       if (this.segment != null) {
/* 2117 */         long segmentOffset = this.offset - (this.start - this.segment.pos);
/* 2118 */         if (segmentOffset > offset) {
/*      */           
/* 2120 */           max = segmentOffset;
/* 2121 */           tail = this.segment;
/*      */         } else {
/*      */           
/* 2124 */           min = segmentOffset;
/* 2125 */           head = this.segment;
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2131 */       if (max - offset > offset - min) {
/*      */         
/* 2133 */         next = head;
/* 2134 */         nextOffset = min;
/* 2135 */         while (offset >= nextOffset + (next.limit - next.pos)) {
/* 2136 */           nextOffset += (next.limit - next.pos);
/* 2137 */           next = next.next;
/*      */         } 
/*      */       } else {
/*      */         
/* 2141 */         next = tail;
/* 2142 */         nextOffset = max;
/* 2143 */         while (nextOffset > offset) {
/* 2144 */           next = next.prev;
/* 2145 */           nextOffset -= (next.limit - next.pos);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 2150 */       if (this.readWrite && next.shared) {
/* 2151 */         Segment unsharedNext = next.unsharedCopy();
/* 2152 */         if (this.buffer.head == next) {
/* 2153 */           this.buffer.head = unsharedNext;
/*      */         }
/* 2155 */         next = next.push(unsharedNext);
/* 2156 */         next.prev.pop();
/*      */       } 
/*      */ 
/*      */       
/* 2160 */       this.segment = next;
/* 2161 */       this.offset = offset;
/* 2162 */       this.data = next.data;
/* 2163 */       this.start = next.pos + (int)(offset - nextOffset);
/* 2164 */       this.end = next.limit;
/* 2165 */       return this.end - this.start;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final long resizeBuffer(long newSize) {
/* 2186 */       if (this.buffer == null) {
/* 2187 */         throw new IllegalStateException("not attached to a buffer");
/*      */       }
/* 2189 */       if (!this.readWrite) {
/* 2190 */         throw new IllegalStateException("resizeBuffer() only permitted for read/write buffers");
/*      */       }
/*      */       
/* 2193 */       long oldSize = this.buffer.size;
/* 2194 */       if (newSize <= oldSize) {
/* 2195 */         if (newSize < 0L) {
/* 2196 */           throw new IllegalArgumentException("newSize < 0: " + newSize);
/*      */         }
/*      */         
/* 2199 */         for (long bytesToSubtract = oldSize - newSize; bytesToSubtract > 0L; ) {
/* 2200 */           Segment tail = this.buffer.head.prev;
/* 2201 */           int tailSize = tail.limit - tail.pos;
/* 2202 */           if (tailSize <= bytesToSubtract) {
/* 2203 */             this.buffer.head = tail.pop();
/* 2204 */             SegmentPool.recycle(tail);
/* 2205 */             bytesToSubtract -= tailSize; continue;
/*      */           } 
/* 2207 */           tail.limit = (int)(tail.limit - bytesToSubtract);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 2212 */         this.segment = null;
/* 2213 */         this.offset = newSize;
/* 2214 */         this.data = null;
/* 2215 */         this.start = -1;
/* 2216 */         this.end = -1;
/* 2217 */       } else if (newSize > oldSize) {
/*      */         
/* 2219 */         boolean needsToSeek = true;
/* 2220 */         for (long bytesToAdd = newSize - oldSize; bytesToAdd > 0L; ) {
/* 2221 */           Segment tail = this.buffer.writableSegment(1);
/* 2222 */           int segmentBytesToAdd = (int)Math.min(bytesToAdd, (8192 - tail.limit));
/* 2223 */           tail.limit += segmentBytesToAdd;
/* 2224 */           bytesToAdd -= segmentBytesToAdd;
/*      */ 
/*      */           
/* 2227 */           if (needsToSeek) {
/* 2228 */             this.segment = tail;
/* 2229 */             this.offset = oldSize;
/* 2230 */             this.data = tail.data;
/* 2231 */             this.start = tail.limit - segmentBytesToAdd;
/* 2232 */             this.end = tail.limit;
/* 2233 */             needsToSeek = false;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 2238 */       this.buffer.size = newSize;
/*      */       
/* 2240 */       return oldSize;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final long expandBuffer(int minByteCount) {
/* 2267 */       if (minByteCount <= 0) {
/* 2268 */         throw new IllegalArgumentException("minByteCount <= 0: " + minByteCount);
/*      */       }
/* 2270 */       if (minByteCount > 8192) {
/* 2271 */         throw new IllegalArgumentException("minByteCount > Segment.SIZE: " + minByteCount);
/*      */       }
/* 2273 */       if (this.buffer == null) {
/* 2274 */         throw new IllegalStateException("not attached to a buffer");
/*      */       }
/* 2276 */       if (!this.readWrite) {
/* 2277 */         throw new IllegalStateException("expandBuffer() only permitted for read/write buffers");
/*      */       }
/*      */       
/* 2280 */       long oldSize = this.buffer.size;
/* 2281 */       Segment tail = this.buffer.writableSegment(minByteCount);
/* 2282 */       int result = 8192 - tail.limit;
/* 2283 */       tail.limit = 8192;
/* 2284 */       this.buffer.size = oldSize + result;
/*      */ 
/*      */       
/* 2287 */       this.segment = tail;
/* 2288 */       this.offset = oldSize;
/* 2289 */       this.data = tail.data;
/* 2290 */       this.start = 8192 - result;
/* 2291 */       this.end = 8192;
/*      */       
/* 2293 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public void close() {
/* 2298 */       if (this.buffer == null) {
/* 2299 */         throw new IllegalStateException("not attached to a buffer");
/*      */       }
/*      */       
/* 2302 */       this.buffer = null;
/* 2303 */       this.segment = null;
/* 2304 */       this.offset = -1L;
/* 2305 */       this.data = null;
/* 2306 */       this.start = -1;
/* 2307 */       this.end = -1;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okio\Buffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */