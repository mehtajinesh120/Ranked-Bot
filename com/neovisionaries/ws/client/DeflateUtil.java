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
/*     */ class DeflateUtil
/*     */ {
/*  24 */   private static int[] INDICES_FROM_CODE_LENGTH_ORDER = new int[] { 16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void readDynamicTables(ByteArray input, int[] bitIndex, Huffman[] tables) throws FormatException {
/*  34 */     int hlit = input.readBits(bitIndex, 5) + 257;
/*     */ 
/*     */     
/*  37 */     int hdist = input.readBits(bitIndex, 5) + 1;
/*     */ 
/*     */     
/*  40 */     int hclen = input.readBits(bitIndex, 4) + 4;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  47 */     int[] codeLengthsFromCodeLengthValue = new int[19];
/*  48 */     for (int i = 0; i < hclen; i++) {
/*     */       
/*  50 */       byte codeLengthOfCodeLengthValue = (byte)input.readBits(bitIndex, 3);
/*     */ 
/*     */       
/*  53 */       int index = codeLengthOrderToIndex(i);
/*     */       
/*  55 */       codeLengthsFromCodeLengthValue[index] = codeLengthOfCodeLengthValue;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  60 */     Huffman codeLengthHuffman = new Huffman(codeLengthsFromCodeLengthValue);
/*     */ 
/*     */ 
/*     */     
/*  64 */     int[] codeLengthsFromLiteralLengthCode = new int[hlit];
/*  65 */     readCodeLengths(input, bitIndex, codeLengthsFromLiteralLengthCode, codeLengthHuffman);
/*     */ 
/*     */ 
/*     */     
/*  69 */     Huffman literalLengthHuffman = new Huffman(codeLengthsFromLiteralLengthCode);
/*     */ 
/*     */ 
/*     */     
/*  73 */     int[] codeLengthsFromDistanceCode = new int[hdist];
/*  74 */     readCodeLengths(input, bitIndex, codeLengthsFromDistanceCode, codeLengthHuffman);
/*     */ 
/*     */ 
/*     */     
/*  78 */     Huffman distanceHuffman = new Huffman(codeLengthsFromDistanceCode);
/*     */     
/*  80 */     tables[0] = literalLengthHuffman;
/*  81 */     tables[1] = distanceHuffman;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void readCodeLengths(ByteArray input, int[] bitIndex, int[] codeLengths, Huffman codeLengthHuffman) throws FormatException {
/*  91 */     for (int i = 0; i < codeLengths.length; i++) {
/*     */ 
/*     */       
/*  94 */       int codeLength = codeLengthHuffman.readSym(input, bitIndex);
/*     */ 
/*     */ 
/*     */       
/*  98 */       if (0 <= codeLength && codeLength <= 15) {
/*     */ 
/*     */         
/* 101 */         codeLengths[i] = codeLength;
/*     */       } else {
/*     */         int repeatCount;
/*     */         
/*     */         String message;
/*     */         
/* 107 */         switch (codeLength) {
/*     */ 
/*     */ 
/*     */           
/*     */           case 16:
/* 112 */             codeLength = codeLengths[i - 1];
/* 113 */             repeatCount = input.readBits(bitIndex, 2) + 3;
/*     */             break;
/*     */ 
/*     */ 
/*     */           
/*     */           case 17:
/* 119 */             codeLength = 0;
/* 120 */             repeatCount = input.readBits(bitIndex, 3) + 3;
/*     */             break;
/*     */ 
/*     */ 
/*     */           
/*     */           case 18:
/* 126 */             codeLength = 0;
/* 127 */             repeatCount = input.readBits(bitIndex, 7) + 11;
/*     */             break;
/*     */ 
/*     */           
/*     */           default:
/* 132 */             message = String.format("[%s] Bad code length '%d' at the bit index '%d'.", new Object[] { DeflateUtil.class
/*     */                   
/* 134 */                   .getSimpleName(), Integer.valueOf(codeLength), bitIndex });
/*     */             
/* 136 */             throw new FormatException(message);
/*     */         } 
/*     */ 
/*     */         
/* 140 */         for (int j = 0; j < repeatCount; j++)
/*     */         {
/* 142 */           codeLengths[i + j] = codeLength;
/*     */         }
/*     */ 
/*     */         
/* 146 */         i += repeatCount - 1;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int codeLengthOrderToIndex(int order) {
/* 157 */     return INDICES_FROM_CODE_LENGTH_ORDER[order];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int readLength(ByteArray input, int[] bitIndex, int literalLength) throws FormatException {
/*     */     int baseValue, nBits, n, i;
/* 169 */     switch (literalLength) {
/*     */       
/*     */       case 257:
/*     */       case 258:
/*     */       case 259:
/*     */       case 260:
/*     */       case 261:
/*     */       case 262:
/*     */       case 263:
/*     */       case 264:
/* 179 */         return literalLength - 254;
/*     */       case 265:
/* 181 */         baseValue = 11; nBits = 1;
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
/* 212 */         n = input.readBits(bitIndex, nBits);
/*     */         
/* 214 */         return baseValue + n;case 266: baseValue = 13; nBits = 1; i = input.readBits(bitIndex, nBits); return baseValue + i;case 267: baseValue = 15; nBits = 1; i = input.readBits(bitIndex, nBits); return baseValue + i;case 268: baseValue = 17; nBits = 1; i = input.readBits(bitIndex, nBits); return baseValue + i;case 269: baseValue = 19; nBits = 2; i = input.readBits(bitIndex, nBits); return baseValue + i;case 270: baseValue = 23; nBits = 2; i = input.readBits(bitIndex, nBits); return baseValue + i;case 271: baseValue = 27; nBits = 2; i = input.readBits(bitIndex, nBits); return baseValue + i;case 272: baseValue = 31; nBits = 2; i = input.readBits(bitIndex, nBits); return baseValue + i;case 273: baseValue = 35; nBits = 3; i = input.readBits(bitIndex, nBits); return baseValue + i;case 274: baseValue = 43; nBits = 3; i = input.readBits(bitIndex, nBits); return baseValue + i;case 275: baseValue = 51; nBits = 3; i = input.readBits(bitIndex, nBits); return baseValue + i;case 276: baseValue = 59; nBits = 3; i = input.readBits(bitIndex, nBits); return baseValue + i;case 277: baseValue = 67; nBits = 4; i = input.readBits(bitIndex, nBits); return baseValue + i;case 278: baseValue = 83; nBits = 4; i = input.readBits(bitIndex, nBits); return baseValue + i;case 279: baseValue = 99; nBits = 4; i = input.readBits(bitIndex, nBits); return baseValue + i;case 280: baseValue = 115; nBits = 4; i = input.readBits(bitIndex, nBits); return baseValue + i;case 281: baseValue = 131; nBits = 5; i = input.readBits(bitIndex, nBits); return baseValue + i;case 282: baseValue = 163; nBits = 5; i = input.readBits(bitIndex, nBits); return baseValue + i;case 283: baseValue = 195; nBits = 5; i = input.readBits(bitIndex, nBits); return baseValue + i;case 284: baseValue = 227; nBits = 5; i = input.readBits(bitIndex, nBits); return baseValue + i;
/*     */       case 285:
/*     */         return 258;
/*     */     } 
/*     */     String str = String.format("[%s] Bad literal/length code '%d' at the bit index '%d'.", new Object[] { DeflateUtil.class.getSimpleName(), Integer.valueOf(literalLength), Integer.valueOf(bitIndex[0]) });
/*     */     throw new FormatException(str);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int readDistance(ByteArray input, int[] bitIndex, Huffman distanceHuffman) throws FormatException {
/* 225 */     int baseValue, nBits, n, i, code = distanceHuffman.readSym(input, bitIndex);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 230 */     switch (code) {
/*     */       
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/* 236 */         return code + 1;
/*     */       case 4:
/* 238 */         baseValue = 5; nBits = 1;
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
/* 277 */         n = input.readBits(bitIndex, nBits);
/*     */         
/* 279 */         return baseValue + n;case 5: baseValue = 7; nBits = 1; i = input.readBits(bitIndex, nBits); return baseValue + i;case 6: baseValue = 9; nBits = 2; i = input.readBits(bitIndex, nBits); return baseValue + i;case 7: baseValue = 13; nBits = 2; i = input.readBits(bitIndex, nBits); return baseValue + i;case 8: baseValue = 17; nBits = 3; i = input.readBits(bitIndex, nBits); return baseValue + i;case 9: baseValue = 25; nBits = 3; i = input.readBits(bitIndex, nBits); return baseValue + i;case 10: baseValue = 33; nBits = 4; i = input.readBits(bitIndex, nBits); return baseValue + i;case 11: baseValue = 49; nBits = 4; i = input.readBits(bitIndex, nBits); return baseValue + i;case 12: baseValue = 65; nBits = 5; i = input.readBits(bitIndex, nBits); return baseValue + i;case 13: baseValue = 97; nBits = 5; i = input.readBits(bitIndex, nBits); return baseValue + i;case 14: baseValue = 129; nBits = 6; i = input.readBits(bitIndex, nBits); return baseValue + i;case 15: baseValue = 193; nBits = 6; i = input.readBits(bitIndex, nBits); return baseValue + i;case 16: baseValue = 257; nBits = 7; i = input.readBits(bitIndex, nBits); return baseValue + i;case 17: baseValue = 385; nBits = 7; i = input.readBits(bitIndex, nBits); return baseValue + i;case 18: baseValue = 513; nBits = 8; i = input.readBits(bitIndex, nBits); return baseValue + i;case 19: baseValue = 769; nBits = 8; i = input.readBits(bitIndex, nBits); return baseValue + i;case 20: baseValue = 1025; nBits = 9; i = input.readBits(bitIndex, nBits); return baseValue + i;case 21: baseValue = 1537; nBits = 9; i = input.readBits(bitIndex, nBits); return baseValue + i;case 22: baseValue = 2049; nBits = 10; i = input.readBits(bitIndex, nBits); return baseValue + i;case 23: baseValue = 3073; nBits = 10; i = input.readBits(bitIndex, nBits); return baseValue + i;case 24: baseValue = 4097; nBits = 11; i = input.readBits(bitIndex, nBits); return baseValue + i;case 25: baseValue = 6145; nBits = 11; i = input.readBits(bitIndex, nBits); return baseValue + i;case 26: baseValue = 8193; nBits = 12; i = input.readBits(bitIndex, nBits); return baseValue + i;case 27: baseValue = 12289; nBits = 12; i = input.readBits(bitIndex, nBits); return baseValue + i;case 28: baseValue = 16385; nBits = 13; i = input.readBits(bitIndex, nBits); return baseValue + i;case 29: baseValue = 24577; nBits = 13; i = input.readBits(bitIndex, nBits); return baseValue + i;
/*     */     } 
/*     */     String str = String.format("[%s] Bad distance code '%d' at the bit index '%d'.", new Object[] { DeflateUtil.class.getSimpleName(), Integer.valueOf(code), Integer.valueOf(bitIndex[0]) });
/*     */     throw new FormatException(str);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\DeflateUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */