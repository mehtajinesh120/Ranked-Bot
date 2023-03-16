/*     */ package org.apache.commons.collections4.trie.analyzer;
/*     */ 
/*     */ import org.apache.commons.collections4.trie.KeyAnalyzer;
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
/*     */ public class StringKeyAnalyzer
/*     */   extends KeyAnalyzer<String>
/*     */ {
/*     */   private static final long serialVersionUID = -7032449491269434877L;
/*  32 */   public static final StringKeyAnalyzer INSTANCE = new StringKeyAnalyzer();
/*     */ 
/*     */   
/*     */   public static final int LENGTH = 16;
/*     */ 
/*     */   
/*     */   private static final int MSB = 32768;
/*     */ 
/*     */   
/*     */   private static int mask(int bit) {
/*  42 */     return 32768 >>> bit;
/*     */   }
/*     */ 
/*     */   
/*     */   public int bitsPerElement() {
/*  47 */     return 16;
/*     */   }
/*     */ 
/*     */   
/*     */   public int lengthInBits(String key) {
/*  52 */     return (key != null) ? (key.length() * 16) : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int bitIndex(String key, int offsetInBits, int lengthInBits, String other, int otherOffsetInBits, int otherLengthInBits) {
/*  59 */     boolean allNull = true;
/*     */     
/*  61 */     if (offsetInBits % 16 != 0 || otherOffsetInBits % 16 != 0 || lengthInBits % 16 != 0 || otherLengthInBits % 16 != 0)
/*     */     {
/*  63 */       throw new IllegalArgumentException("The offsets and lengths must be at Character boundaries");
/*     */     }
/*     */     
/*  66 */     int beginIndex1 = offsetInBits / 16;
/*  67 */     int beginIndex2 = otherOffsetInBits / 16;
/*     */     
/*  69 */     int endIndex1 = beginIndex1 + lengthInBits / 16;
/*  70 */     int endIndex2 = beginIndex2 + otherLengthInBits / 16;
/*     */     
/*  72 */     int length = Math.max(endIndex1, endIndex2);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     char k = Character.MIN_VALUE, f = Character.MIN_VALUE;
/*  78 */     for (int i = 0; i < length; i++) {
/*  79 */       int index1 = beginIndex1 + i;
/*  80 */       int index2 = beginIndex2 + i;
/*     */       
/*  82 */       if (index1 >= endIndex1) {
/*  83 */         k = Character.MIN_VALUE;
/*     */       } else {
/*  85 */         k = key.charAt(index1);
/*     */       } 
/*     */       
/*  88 */       if (other == null || index2 >= endIndex2) {
/*  89 */         f = Character.MIN_VALUE;
/*     */       } else {
/*  91 */         f = other.charAt(index2);
/*     */       } 
/*     */       
/*  94 */       if (k != f) {
/*  95 */         int x = k ^ f;
/*  96 */         return i * 16 + Integer.numberOfLeadingZeros(x) - 16;
/*     */       } 
/*     */       
/*  99 */       if (k != '\000') {
/* 100 */         allNull = false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 105 */     if (allNull) {
/* 106 */       return -1;
/*     */     }
/*     */ 
/*     */     
/* 110 */     return -2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBitSet(String key, int bitIndex, int lengthInBits) {
/* 115 */     if (key == null || bitIndex >= lengthInBits) {
/* 116 */       return false;
/*     */     }
/*     */     
/* 119 */     int index = bitIndex / 16;
/* 120 */     int bit = bitIndex % 16;
/*     */     
/* 122 */     return ((key.charAt(index) & mask(bit)) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrefix(String prefix, int offsetInBits, int lengthInBits, String key) {
/* 128 */     if (offsetInBits % 16 != 0 || lengthInBits % 16 != 0) {
/* 129 */       throw new IllegalArgumentException("Cannot determine prefix outside of Character boundaries");
/*     */     }
/*     */ 
/*     */     
/* 133 */     String s1 = prefix.substring(offsetInBits / 16, lengthInBits / 16);
/* 134 */     return key.startsWith(s1);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\trie\analyzer\StringKeyAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */