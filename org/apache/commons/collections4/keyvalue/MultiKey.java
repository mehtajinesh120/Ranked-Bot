/*     */ package org.apache.commons.collections4.keyvalue;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
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
/*     */ public class MultiKey<K>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4465448607415788805L;
/*     */   private final K[] keys;
/*     */   private transient int hashCode;
/*     */   
/*     */   public MultiKey(K key1, K key2) {
/*  67 */     this((K[])new Object[] { key1, key2 }, false);
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
/*     */   public MultiKey(K key1, K key2, K key3) {
/*  82 */     this((K[])new Object[] { key1, key2, key3 }, false);
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
/*     */   public MultiKey(K key1, K key2, K key3, K key4) {
/*  98 */     this((K[])new Object[] { key1, key2, key3, key4 }, false);
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
/*     */   public MultiKey(K key1, K key2, K key3, K key4, K key5) {
/* 115 */     this((K[])new Object[] { key1, key2, key3, key4, key5 }, false);
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
/*     */   public MultiKey(K[] keys) {
/* 130 */     this(keys, true);
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
/*     */   public MultiKey(K[] keys, boolean makeClone) {
/* 159 */     if (keys == null) {
/* 160 */       throw new IllegalArgumentException("The array of keys must not be null");
/*     */     }
/* 162 */     if (makeClone) {
/* 163 */       this.keys = (K[])keys.clone();
/*     */     } else {
/* 165 */       this.keys = keys;
/*     */     } 
/*     */     
/* 168 */     calculateHashCode((Object[])keys);
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
/*     */   public K[] getKeys() {
/* 181 */     return (K[])this.keys.clone();
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
/*     */   public K getKey(int index) {
/* 196 */     return this.keys[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 206 */     return this.keys.length;
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
/*     */   public boolean equals(Object other) {
/* 221 */     if (other == this) {
/* 222 */       return true;
/*     */     }
/* 224 */     if (other instanceof MultiKey) {
/* 225 */       MultiKey<?> otherMulti = (MultiKey)other;
/* 226 */       return Arrays.equals((Object[])this.keys, (Object[])otherMulti.keys);
/*     */     } 
/* 228 */     return false;
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
/*     */   public int hashCode() {
/* 243 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 253 */     return "MultiKey" + Arrays.toString((Object[])this.keys);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void calculateHashCode(Object[] keys) {
/* 262 */     int total = 0;
/* 263 */     for (Object key : keys) {
/* 264 */       if (key != null) {
/* 265 */         total ^= key.hashCode();
/*     */       }
/*     */     } 
/* 268 */     this.hashCode = total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object readResolve() {
/* 278 */     calculateHashCode((Object[])this.keys);
/* 279 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\keyvalue\MultiKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */