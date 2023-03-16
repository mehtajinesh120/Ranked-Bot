/*     */ package org.apache.commons.collections4.keyvalue;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.collections4.KeyValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TiedMapEntry<K, V>
/*     */   implements Map.Entry<K, V>, KeyValue<K, V>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -8453869361373831205L;
/*     */   private final Map<K, V> map;
/*     */   private final K key;
/*     */   
/*     */   public TiedMapEntry(Map<K, V> map, K key) {
/*  52 */     this.map = map;
/*  53 */     this.key = key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K getKey() {
/*  64 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V getValue() {
/*  73 */     return this.map.get(this.key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V setValue(V value) {
/*  84 */     if (value == this) {
/*  85 */       throw new IllegalArgumentException("Cannot set value to this map entry");
/*     */     }
/*  87 */     return this.map.put(this.key, value);
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
/*     */   public boolean equals(Object obj) {
/* 100 */     if (obj == this) {
/* 101 */       return true;
/*     */     }
/* 103 */     if (!(obj instanceof Map.Entry)) {
/* 104 */       return false;
/*     */     }
/* 106 */     Map.Entry<?, ?> other = (Map.Entry<?, ?>)obj;
/* 107 */     Object value = getValue();
/* 108 */     return (((this.key == null) ? (other.getKey() == null) : this.key.equals(other.getKey())) && ((value == null) ? (other.getValue() == null) : value.equals(other.getValue())));
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
/*     */   public int hashCode() {
/* 122 */     Object value = getValue();
/* 123 */     return ((getKey() == null) ? 0 : getKey().hashCode()) ^ ((value == null) ? 0 : value.hashCode());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 134 */     return (new StringBuilder()).append(getKey()).append("=").append(getValue()).toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\keyvalue\TiedMapEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */