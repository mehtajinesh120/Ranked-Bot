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
/*     */ 
/*     */ 
/*     */ class Huffman
/*     */ {
/*     */   private final int mMinCodeLen;
/*     */   private final int mMaxCodeLen;
/*     */   private final int[] mMaxCodeValsFromCodeLen;
/*     */   private final int[] mSymsFromCodeVal;
/*     */   
/*     */   public Huffman(int[] codeLensFromSym) {
/*  34 */     this.mMinCodeLen = Math.max(Misc.min(codeLensFromSym), 1);
/*  35 */     this.mMaxCodeLen = Misc.max(codeLensFromSym);
/*     */ 
/*     */     
/*  38 */     int[] countsFromCodeLen = createCountsFromCodeLen(codeLensFromSym, this.mMaxCodeLen);
/*     */ 
/*     */     
/*  41 */     Object[] out = new Object[2];
/*  42 */     this.mMaxCodeValsFromCodeLen = createMaxCodeValsFromCodeLen(countsFromCodeLen, this.mMaxCodeLen, out);
/*     */ 
/*     */     
/*  45 */     int[] codeValsFromCodeLen = (int[])out[0];
/*  46 */     int maxCodeVal = ((Integer)out[1]).intValue();
/*  47 */     this.mSymsFromCodeVal = createSymsFromCodeVal(codeLensFromSym, codeValsFromCodeLen, maxCodeVal);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] createIntArray(int size, int initialValue) {
/*  56 */     int[] array = new int[size];
/*     */     
/*  58 */     for (int i = 0; i < size; i++)
/*     */     {
/*  60 */       array[i] = initialValue;
/*     */     }
/*     */     
/*  63 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] createCountsFromCodeLen(int[] codeLensFromSym, int maxCodeLen) {
/*  69 */     int[] countsFromCodeLen = new int[maxCodeLen + 1];
/*     */ 
/*     */ 
/*     */     
/*  73 */     for (int symbol = 0; symbol < codeLensFromSym.length; symbol++) {
/*     */       
/*  75 */       int codeLength = codeLensFromSym[symbol];
/*  76 */       countsFromCodeLen[codeLength] = countsFromCodeLen[codeLength] + 1;
/*     */     } 
/*     */     
/*  79 */     return countsFromCodeLen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] createMaxCodeValsFromCodeLen(int[] countsFromCodeLen, int maxCodeLen, Object[] out) {
/*  88 */     int[] maxCodeValsFromCodeLen = createIntArray(maxCodeLen + 1, -1);
/*     */ 
/*     */ 
/*     */     
/*  92 */     int minCodeVal = 0;
/*  93 */     int maxCodeVal = 0;
/*  94 */     countsFromCodeLen[0] = 0;
/*  95 */     int[] codeValsFromCodeLen = new int[maxCodeLen + 1];
/*  96 */     for (int codeLen = 1; codeLen < countsFromCodeLen.length; codeLen++) {
/*     */ 
/*     */       
/*  99 */       int prevCount = countsFromCodeLen[codeLen - 1];
/* 100 */       minCodeVal = minCodeVal + prevCount << 1;
/* 101 */       codeValsFromCodeLen[codeLen] = minCodeVal;
/*     */ 
/*     */       
/* 104 */       maxCodeVal = minCodeVal + countsFromCodeLen[codeLen] - 1;
/* 105 */       maxCodeValsFromCodeLen[codeLen] = maxCodeVal;
/*     */     } 
/*     */     
/* 108 */     out[0] = codeValsFromCodeLen;
/* 109 */     out[1] = Integer.valueOf(maxCodeVal);
/*     */     
/* 111 */     return maxCodeValsFromCodeLen;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] createSymsFromCodeVal(int[] codeLensFromSym, int[] codeValsFromCodeLen, int maxCodeVal) {
/* 117 */     int[] symsFromCodeVal = new int[maxCodeVal + 1];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     for (int sym = 0; sym < codeLensFromSym.length; sym++) {
/*     */       
/* 124 */       int codeLen = codeLensFromSym[sym];
/*     */       
/* 126 */       if (codeLen != 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 131 */         codeValsFromCodeLen[codeLen] = codeValsFromCodeLen[codeLen] + 1; int codeVal = codeValsFromCodeLen[codeLen];
/* 132 */         symsFromCodeVal[codeVal] = sym;
/*     */       } 
/*     */     } 
/* 135 */     return symsFromCodeVal;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int readSym(ByteArray data, int[] bitIndex) throws FormatException {
/* 141 */     for (int codeLen = this.mMinCodeLen; codeLen <= this.mMaxCodeLen; codeLen++) {
/*     */ 
/*     */ 
/*     */       
/* 145 */       int maxCodeVal = this.mMaxCodeValsFromCodeLen[codeLen];
/*     */       
/* 147 */       if (maxCodeVal >= 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 154 */         int codeVal = data.getHuffmanBits(bitIndex[0], codeLen);
/*     */         
/* 156 */         if (maxCodeVal >= codeVal) {
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
/* 176 */           int sym = this.mSymsFromCodeVal[codeVal];
/*     */ 
/*     */           
/* 179 */           bitIndex[0] = bitIndex[0] + codeLen;
/*     */           
/* 181 */           return sym;
/*     */         } 
/*     */       } 
/*     */     } 
/* 185 */     String message = String.format("[%s] Bad code at the bit index '%d'.", new Object[] {
/*     */           
/* 187 */           getClass().getSimpleName(), Integer.valueOf(bitIndex[0])
/*     */         });
/* 189 */     throw new FormatException(message);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\Huffman.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */