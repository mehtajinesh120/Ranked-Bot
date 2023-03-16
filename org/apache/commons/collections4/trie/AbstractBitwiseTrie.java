/*     */ package org.apache.commons.collections4.trie;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.collections4.Trie;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractBitwiseTrie<K, V>
/*     */   extends AbstractMap<K, V>
/*     */   implements Trie<K, V>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5826987063535505652L;
/*     */   private final KeyAnalyzer<? super K> keyAnalyzer;
/*     */   
/*     */   protected AbstractBitwiseTrie(KeyAnalyzer<? super K> keyAnalyzer) {
/*  49 */     if (keyAnalyzer == null) {
/*  50 */       throw new NullPointerException("keyAnalyzer");
/*     */     }
/*     */     
/*  53 */     this.keyAnalyzer = keyAnalyzer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected KeyAnalyzer<? super K> getKeyAnalyzer() {
/*  61 */     return this.keyAnalyzer;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  66 */     StringBuilder buffer = new StringBuilder();
/*  67 */     buffer.append("Trie[").append(size()).append("]={\n");
/*  68 */     for (Map.Entry<K, V> entry : entrySet()) {
/*  69 */       buffer.append("  ").append(entry).append("\n");
/*     */     }
/*  71 */     buffer.append("}\n");
/*  72 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final K castKey(Object key) {
/*  80 */     return (K)key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final int lengthInBits(K key) {
/*  89 */     if (key == null) {
/*  90 */       return 0;
/*     */     }
/*     */     
/*  93 */     return this.keyAnalyzer.lengthInBits(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final int bitsPerElement() {
/* 102 */     return this.keyAnalyzer.bitsPerElement();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean isBitSet(K key, int bitIndex, int lengthInBits) {
/* 111 */     if (key == null) {
/* 112 */       return false;
/*     */     }
/* 114 */     return this.keyAnalyzer.isBitSet(key, bitIndex, lengthInBits);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final int bitIndex(K key, K foundKey) {
/* 121 */     return this.keyAnalyzer.bitIndex(key, 0, lengthInBits(key), foundKey, 0, lengthInBits(foundKey));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean compareKeys(K key, K other) {
/* 128 */     if (key == null)
/* 129 */       return (other == null); 
/* 130 */     if (other == null) {
/* 131 */       return false;
/*     */     }
/*     */     
/* 134 */     return (this.keyAnalyzer.compare(key, other) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean compare(Object a, Object b) {
/* 141 */     return (a == null) ? ((b == null)) : a.equals(b);
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class BasicEntry<K, V>
/*     */     implements Map.Entry<K, V>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -944364551314110330L;
/*     */     
/*     */     protected K key;
/*     */     
/*     */     protected V value;
/*     */ 
/*     */     
/*     */     public BasicEntry(K key) {
/* 156 */       this.key = key;
/*     */     }
/*     */     
/*     */     public BasicEntry(K key, V value) {
/* 160 */       this.key = key;
/* 161 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public V setKeyValue(K key, V value) {
/* 168 */       this.key = key;
/* 169 */       return setValue(value);
/*     */     }
/*     */     
/*     */     public K getKey() {
/* 173 */       return this.key;
/*     */     }
/*     */     
/*     */     public V getValue() {
/* 177 */       return this.value;
/*     */     }
/*     */     
/*     */     public V setValue(V value) {
/* 181 */       V previous = this.value;
/* 182 */       this.value = value;
/* 183 */       return previous;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 188 */       return ((getKey() == null) ? 0 : getKey().hashCode()) ^ ((getValue() == null) ? 0 : getValue().hashCode());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 194 */       if (o == this)
/* 195 */         return true; 
/* 196 */       if (!(o instanceof Map.Entry)) {
/* 197 */         return false;
/*     */       }
/*     */       
/* 200 */       Map.Entry<?, ?> other = (Map.Entry<?, ?>)o;
/* 201 */       if (AbstractBitwiseTrie.compare(this.key, other.getKey()) && AbstractBitwiseTrie.compare(this.value, other.getValue()))
/*     */       {
/* 203 */         return true;
/*     */       }
/* 205 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 210 */       return (new StringBuilder()).append(this.key).append("=").append(this.value).toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\trie\AbstractBitwiseTrie.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */