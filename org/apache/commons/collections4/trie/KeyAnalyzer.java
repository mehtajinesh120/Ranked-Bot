/*     */ package org.apache.commons.collections4.trie;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class KeyAnalyzer<K>
/*     */   implements Comparator<K>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -20497563720380683L;
/*     */   public static final int NULL_BIT_KEY = -1;
/*     */   public static final int EQUAL_BIT_KEY = -2;
/*     */   public static final int OUT_OF_BOUNDS_BIT_KEY = -3;
/*     */   
/*     */   static boolean isOutOfBoundsIndex(int bitIndex) {
/*  58 */     return (bitIndex == -3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isEqualBitKey(int bitIndex) {
/*  65 */     return (bitIndex == -2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isNullBitKey(int bitIndex) {
/*  72 */     return (bitIndex == -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isValidBitIndex(int bitIndex) {
/*  80 */     return (bitIndex >= 0);
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
/*     */   public abstract int bitsPerElement();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int lengthInBits(K paramK);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isBitSet(K paramK, int paramInt1, int paramInt2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int bitIndex(K paramK1, int paramInt1, int paramInt2, K paramK2, int paramInt3, int paramInt4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isPrefix(K paramK1, int paramInt1, int paramInt2, K paramK2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(K o1, K o2) {
/* 139 */     if (o1 == null)
/* 140 */       return (o2 == null) ? 0 : -1; 
/* 141 */     if (o2 == null) {
/* 142 */       return 1;
/*     */     }
/*     */     
/* 145 */     return ((Comparable<K>)o1).compareTo(o2);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\trie\KeyAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */