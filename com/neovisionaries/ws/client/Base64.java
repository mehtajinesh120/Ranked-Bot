/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Base64
/*     */ {
/*  21 */   private static final byte[] INDEX_TABLE = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encode(String data) {
/*  31 */     if (data == null)
/*     */     {
/*  33 */       return null;
/*     */     }
/*     */     
/*  36 */     return encode(Misc.getBytesUTF8(data));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encode(byte[] data) {
/*  42 */     if (data == null)
/*     */     {
/*  44 */       return null;
/*     */     }
/*     */     
/*  47 */     int capacity = ((data.length * 8 + 5) / 6 + 3) / 4 * 4;
/*     */     
/*  49 */     StringBuilder builder = new StringBuilder(capacity);
/*     */     
/*  51 */     for (int bitIndex = 0;; bitIndex += 6) {
/*     */       
/*  53 */       int bits = extractBits(data, bitIndex);
/*     */       
/*  55 */       if (bits < 0) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/*  60 */       builder.append((char)INDEX_TABLE[bits]);
/*     */     } 
/*     */     
/*  63 */     for (int i = builder.length(); i < capacity; i++)
/*     */     {
/*  65 */       builder.append('=');
/*     */     }
/*     */     
/*  68 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static int extractBits(byte[] data, int bitIndex) {
/*     */     byte nextByte;
/*  74 */     int byteIndex = bitIndex / 8;
/*     */ 
/*     */     
/*  77 */     if (data.length <= byteIndex)
/*     */     {
/*  79 */       return -1;
/*     */     }
/*  81 */     if (data.length - 1 == byteIndex) {
/*     */       
/*  83 */       nextByte = 0;
/*     */     }
/*     */     else {
/*     */       
/*  87 */       nextByte = data[byteIndex + 1];
/*     */     } 
/*     */     
/*  90 */     switch (bitIndex % 24 / 6) {
/*     */       
/*     */       case 0:
/*  93 */         return data[byteIndex] >> 2 & 0x3F;
/*     */       
/*     */       case 1:
/*  96 */         return data[byteIndex] << 4 & 0x30 | nextByte >> 4 & 0xF;
/*     */       
/*     */       case 2:
/*  99 */         return data[byteIndex] << 2 & 0x3C | nextByte >> 6 & 0x3;
/*     */       
/*     */       case 3:
/* 102 */         return data[byteIndex] & 0x3F;
/*     */     } 
/*     */ 
/*     */     
/* 106 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\Base64.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */