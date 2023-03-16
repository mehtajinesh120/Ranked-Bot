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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DeflateDecompressor
/*     */ {
/*     */   public static void decompress(ByteArray input, ByteArray output) throws FormatException {
/*  27 */     decompress(input, 0, output);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void decompress(ByteArray input, int index, ByteArray output) throws FormatException {
/*  34 */     int[] bitIndex = new int[1];
/*  35 */     bitIndex[0] = index * 8;
/*     */ 
/*     */ 
/*     */     
/*  39 */     while (inflateBlock(input, bitIndex, output));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean inflateBlock(ByteArray input, int[] bitIndex, ByteArray output) throws FormatException {
/*     */     String message;
/*  50 */     boolean last = input.readBit(bitIndex);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     int type = input.readBits(bitIndex, 2);
/*     */     
/*  62 */     switch (type) {
/*     */ 
/*     */       
/*     */       case 0:
/*  66 */         inflatePlainBlock(input, bitIndex, output);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1:
/*  71 */         inflateFixedBlock(input, bitIndex, output);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/*  76 */         inflateDynamicBlock(input, bitIndex, output);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       default:
/*  82 */         message = String.format("[%s] Bad compression type '11' at the bit index '%d'.", new Object[] { DeflateDecompressor.class
/*     */               
/*  84 */               .getSimpleName(), Integer.valueOf(bitIndex[0]) });
/*     */         
/*  86 */         throw new FormatException(message);
/*     */     } 
/*     */ 
/*     */     
/*  90 */     if (input.length() <= bitIndex[0] / 8)
/*     */     {
/*     */       
/*  93 */       last = true;
/*     */     }
/*     */ 
/*     */     
/*  97 */     return !last;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void inflatePlainBlock(ByteArray input, int[] bitIndex, ByteArray output) {
/* 106 */     int bi = bitIndex[0] + 7 & 0xFFFFFFF8;
/*     */ 
/*     */ 
/*     */     
/* 110 */     int index = bi / 8;
/*     */ 
/*     */     
/* 113 */     int len = (input.get(index) & 0xFF) + (input.get(index + 1) & 0xFF) * 256;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     index += 4;
/*     */ 
/*     */     
/* 121 */     output.put(input, index, len);
/*     */ 
/*     */ 
/*     */     
/* 125 */     bitIndex[0] = (index + len) * 8;
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
/*     */   private static void inflateFixedBlock(ByteArray input, int[] bitIndex, ByteArray output) throws FormatException {
/* 144 */     inflateData(input, bitIndex, output, 
/* 145 */         FixedLiteralLengthHuffman.getInstance(), 
/* 146 */         FixedDistanceHuffman.getInstance());
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
/*     */   private static void inflateDynamicBlock(ByteArray input, int[] bitIndex, ByteArray output) throws FormatException {
/* 158 */     Huffman[] tables = new Huffman[2];
/* 159 */     DeflateUtil.readDynamicTables(input, bitIndex, tables);
/*     */ 
/*     */ 
/*     */     
/* 163 */     inflateData(input, bitIndex, output, tables[0], tables[1]);
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
/*     */   private static void inflateData(ByteArray input, int[] bitIndex, ByteArray output, Huffman literalLengthHuffman, Huffman distanceHuffman) throws FormatException {
/*     */     while (true) {
/* 176 */       int literalLength = literalLengthHuffman.readSym(input, bitIndex);
/*     */ 
/*     */       
/* 179 */       if (literalLength == 256) {
/*     */         break;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 186 */       if (0 <= literalLength && literalLength <= 255) {
/*     */ 
/*     */         
/* 189 */         output.put(literalLength);
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */ 
/*     */       
/* 196 */       int length = DeflateUtil.readLength(input, bitIndex, literalLength);
/*     */ 
/*     */       
/* 199 */       int distance = DeflateUtil.readDistance(input, bitIndex, distanceHuffman);
/*     */ 
/*     */       
/* 202 */       duplicate(length, distance, output);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void duplicate(int length, int distance, ByteArray output) {
/* 210 */     int sourceLength = output.length();
/*     */ 
/*     */     
/* 213 */     byte[] target = new byte[length];
/*     */ 
/*     */     
/* 216 */     int initialPosition = sourceLength - distance;
/* 217 */     int sourceIndex = initialPosition;
/*     */     
/* 219 */     for (int targetIndex = 0; targetIndex < length; targetIndex++, sourceIndex++) {
/*     */       
/* 221 */       if (sourceLength <= sourceIndex)
/*     */       {
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
/* 234 */         sourceIndex = initialPosition;
/*     */       }
/*     */       
/* 237 */       target[targetIndex] = output.get(sourceIndex);
/*     */     } 
/*     */ 
/*     */     
/* 241 */     output.put(target);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\DeflateDecompressor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */