/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMapDecorator<K, V>
/*     */   extends AbstractIterableMap<K, V>
/*     */ {
/*     */   transient Map<K, V> map;
/*     */   
/*     */   protected AbstractMapDecorator() {}
/*     */   
/*     */   protected AbstractMapDecorator(Map<K, V> map) {
/*  61 */     if (map == null) {
/*  62 */       throw new NullPointerException("Map must not be null.");
/*     */     }
/*  64 */     this.map = map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<K, V> decorated() {
/*  73 */     return this.map;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  78 */     decorated().clear();
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  82 */     return decorated().containsKey(key);
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  86 */     return decorated().containsValue(value);
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/*  90 */     return decorated().entrySet();
/*     */   }
/*     */   
/*     */   public V get(Object key) {
/*  94 */     return decorated().get(key);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  98 */     return decorated().isEmpty();
/*     */   }
/*     */   
/*     */   public Set<K> keySet() {
/* 102 */     return decorated().keySet();
/*     */   }
/*     */   
/*     */   public V put(K key, V value) {
/* 106 */     return decorated().put(key, value);
/*     */   }
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> mapToCopy) {
/* 110 */     decorated().putAll(mapToCopy);
/*     */   }
/*     */   
/*     */   public V remove(Object key) {
/* 114 */     return decorated().remove(key);
/*     */   }
/*     */   
/*     */   public int size() {
/* 118 */     return decorated().size();
/*     */   }
/*     */   
/*     */   public Collection<V> values() {
/* 122 */     return decorated().values();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 127 */     if (object == this) {
/* 128 */       return true;
/*     */     }
/* 130 */     return decorated().equals(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 135 */     return decorated().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 140 */     return decorated().toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\AbstractMapDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */