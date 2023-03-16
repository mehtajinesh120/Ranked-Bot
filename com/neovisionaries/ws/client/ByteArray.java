/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ByteArray
/*     */ {
/*     */   private static final int ADDITIONAL_BUFFER_SIZE = 1024;
/*     */   private ByteBuffer mBuffer;
/*     */   private int mLength;
/*     */   
/*     */   public ByteArray(int capacity) {
/*  44 */     this.mBuffer = ByteBuffer.allocate(capacity);
/*  45 */     this.mLength = 0;
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
/*     */   public ByteArray(byte[] data) {
/*  58 */     this.mBuffer = ByteBuffer.wrap(data);
/*  59 */     this.mLength = data.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/*  68 */     return this.mLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte get(int index) throws IndexOutOfBoundsException {
/*  77 */     if (index < 0 || this.mLength <= index)
/*     */     {
/*     */       
/*  80 */       throw new IndexOutOfBoundsException(
/*  81 */           String.format("Bad index: index=%d, length=%d", new Object[] { Integer.valueOf(index), Integer.valueOf(this.mLength) }));
/*     */     }
/*     */     
/*  84 */     return this.mBuffer.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void expandBuffer(int newBufferSize) {
/*  94 */     ByteBuffer newBuffer = ByteBuffer.allocate(newBufferSize);
/*     */ 
/*     */     
/*  97 */     int oldPosition = this.mBuffer.position();
/*  98 */     this.mBuffer.position(0);
/*  99 */     newBuffer.put(this.mBuffer);
/* 100 */     newBuffer.position(oldPosition);
/*     */ 
/*     */     
/* 103 */     this.mBuffer = newBuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(int data) {
/* 113 */     if (this.mBuffer.capacity() < this.mLength + 1)
/*     */     {
/* 115 */       expandBuffer(this.mLength + 1024);
/*     */     }
/*     */     
/* 118 */     this.mBuffer.put((byte)data);
/* 119 */     this.mLength++;
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
/*     */   public void put(byte[] source) {
/* 132 */     if (this.mBuffer.capacity() < this.mLength + source.length)
/*     */     {
/* 134 */       expandBuffer(this.mLength + source.length + 1024);
/*     */     }
/*     */     
/* 137 */     this.mBuffer.put(source);
/* 138 */     this.mLength += source.length;
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
/*     */ 
/*     */   
/*     */   public void put(byte[] source, int index, int length) {
/* 157 */     if (this.mBuffer.capacity() < this.mLength + length)
/*     */     {
/* 159 */       expandBuffer(this.mLength + length + 1024);
/*     */     }
/*     */     
/* 162 */     this.mBuffer.put(source, index, length);
/* 163 */     this.mLength += length;
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
/*     */   
/*     */   public void put(ByteArray source, int index, int length) {
/* 181 */     put(source.mBuffer.array(), index, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toBytes() {
/* 190 */     return toBytes(0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toBytes(int beginIndex) {
/* 196 */     return toBytes(beginIndex, length());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toBytes(int beginIndex, int endIndex) {
/* 202 */     int len = endIndex - beginIndex;
/*     */     
/* 204 */     if (len < 0 || beginIndex < 0 || this.mLength < endIndex)
/*     */     {
/* 206 */       throw new IllegalArgumentException(
/* 207 */           String.format("Bad range: beginIndex=%d, endIndex=%d, length=%d", new Object[] {
/* 208 */               Integer.valueOf(beginIndex), Integer.valueOf(endIndex), Integer.valueOf(this.mLength)
/*     */             }));
/*     */     }
/* 211 */     byte[] bytes = new byte[len];
/*     */     
/* 213 */     if (len != 0)
/*     */     {
/* 215 */       System.arraycopy(this.mBuffer.array(), beginIndex, bytes, 0, len);
/*     */     }
/*     */     
/* 218 */     return bytes;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 224 */     this.mBuffer.clear();
/* 225 */     this.mBuffer.position(0);
/* 226 */     this.mLength = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void shrink(int size) {
/* 232 */     if (this.mBuffer.capacity() <= size) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 237 */     int endIndex = this.mLength;
/* 238 */     int beginIndex = this.mLength - size;
/*     */     
/* 240 */     byte[] bytes = toBytes(beginIndex, endIndex);
/*     */     
/* 242 */     this.mBuffer = ByteBuffer.wrap(bytes);
/* 243 */     this.mBuffer.position(bytes.length);
/* 244 */     this.mLength = bytes.length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getBit(int bitIndex) {
/* 250 */     int index = bitIndex / 8;
/* 251 */     int shift = bitIndex % 8;
/* 252 */     int value = get(index);
/*     */ 
/*     */     
/* 255 */     return ((value & 1 << shift) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBits(int bitIndex, int nBits) {
/* 261 */     int number = 0;
/* 262 */     int weight = 1;
/*     */ 
/*     */     
/* 265 */     for (int i = 0; i < nBits; i++, weight *= 2) {
/*     */ 
/*     */       
/* 268 */       if (getBit(bitIndex + i))
/*     */       {
/* 270 */         number += weight;
/*     */       }
/*     */     } 
/*     */     
/* 274 */     return number;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHuffmanBits(int bitIndex, int nBits) {
/* 280 */     int number = 0;
/* 281 */     int weight = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 292 */     for (int i = nBits - 1; 0 <= i; i--, weight *= 2) {
/*     */ 
/*     */       
/* 295 */       if (getBit(bitIndex + i))
/*     */       {
/* 297 */         number += weight;
/*     */       }
/*     */     } 
/*     */     
/* 301 */     return number;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean readBit(int[] bitIndex) {
/* 307 */     boolean result = getBit(bitIndex[0]);
/*     */     
/* 309 */     bitIndex[0] = bitIndex[0] + 1;
/*     */     
/* 311 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int readBits(int[] bitIndex, int nBits) {
/* 317 */     int number = getBits(bitIndex[0], nBits);
/*     */     
/* 319 */     bitIndex[0] = bitIndex[0] + nBits;
/*     */     
/* 321 */     return number;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBit(int bitIndex, boolean bit) {
/* 327 */     int index = bitIndex / 8;
/* 328 */     int shift = bitIndex % 8;
/* 329 */     int value = get(index);
/*     */     
/* 331 */     if (bit) {
/*     */       
/* 333 */       value |= 1 << shift;
/*     */     }
/*     */     else {
/*     */       
/* 337 */       value &= 1 << shift ^ 0xFFFFFFFF;
/*     */     } 
/*     */     
/* 340 */     this.mBuffer.put(index, (byte)value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearBit(int bitIndex) {
/* 346 */     setBit(bitIndex, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\ByteArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */