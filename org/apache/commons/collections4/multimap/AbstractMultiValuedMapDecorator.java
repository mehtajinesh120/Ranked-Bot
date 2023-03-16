/*     */ package org.apache.commons.collections4.multimap;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.MapIterator;
/*     */ import org.apache.commons.collections4.MultiSet;
/*     */ import org.apache.commons.collections4.MultiValuedMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMultiValuedMapDecorator<K, V>
/*     */   implements MultiValuedMap<K, V>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 20150612L;
/*     */   private final MultiValuedMap<K, V> map;
/*     */   
/*     */   protected AbstractMultiValuedMapDecorator(MultiValuedMap<K, V> map) {
/*  58 */     if (map == null) {
/*  59 */       throw new NullPointerException("MultiValuedMap must not be null.");
/*     */     }
/*  61 */     this.map = map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MultiValuedMap<K, V> decorated() {
/*  71 */     return this.map;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  77 */     return decorated().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  82 */     return decorated().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  87 */     return decorated().containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  92 */     return decorated().containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsMapping(Object key, Object value) {
/*  97 */     return decorated().containsMapping(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> get(K key) {
/* 102 */     return decorated().get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> remove(Object key) {
/* 107 */     return decorated().remove(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeMapping(Object key, Object item) {
/* 112 */     return decorated().removeMapping(key, item);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 117 */     decorated().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean put(K key, V value) {
/* 122 */     return decorated().put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 127 */     return decorated().keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<Map.Entry<K, V>> entries() {
/* 132 */     return decorated().entries();
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiSet<K> keys() {
/* 137 */     return decorated().keys();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 142 */     return decorated().values();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<K, Collection<V>> asMap() {
/* 147 */     return decorated().asMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean putAll(K key, Iterable<? extends V> values) {
/* 152 */     return decorated().putAll(key, values);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean putAll(Map<? extends K, ? extends V> map) {
/* 157 */     return decorated().putAll(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean putAll(MultiValuedMap<? extends K, ? extends V> map) {
/* 162 */     return decorated().putAll(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public MapIterator<K, V> mapIterator() {
/* 167 */     return decorated().mapIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 172 */     if (object == this) {
/* 173 */       return true;
/*     */     }
/* 175 */     return decorated().equals(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 180 */     return decorated().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 185 */     return decorated().toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\multimap\AbstractMultiValuedMapDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */