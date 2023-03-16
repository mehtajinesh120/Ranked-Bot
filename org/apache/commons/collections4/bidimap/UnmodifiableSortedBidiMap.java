/*     */ package org.apache.commons.collections4.bidimap;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import org.apache.commons.collections4.BidiMap;
/*     */ import org.apache.commons.collections4.MapIterator;
/*     */ import org.apache.commons.collections4.OrderedBidiMap;
/*     */ import org.apache.commons.collections4.OrderedMapIterator;
/*     */ import org.apache.commons.collections4.SortedBidiMap;
/*     */ import org.apache.commons.collections4.Unmodifiable;
/*     */ import org.apache.commons.collections4.iterators.UnmodifiableOrderedMapIterator;
/*     */ import org.apache.commons.collections4.map.UnmodifiableEntrySet;
/*     */ import org.apache.commons.collections4.map.UnmodifiableSortedMap;
/*     */ import org.apache.commons.collections4.set.UnmodifiableSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UnmodifiableSortedBidiMap<K, V>
/*     */   extends AbstractSortedBidiMapDecorator<K, V>
/*     */   implements Unmodifiable
/*     */ {
/*     */   private UnmodifiableSortedBidiMap<V, K> inverse;
/*     */   
/*     */   public static <K, V> SortedBidiMap<K, V> unmodifiableSortedBidiMap(SortedBidiMap<K, ? extends V> map) {
/*  58 */     if (map instanceof Unmodifiable)
/*     */     {
/*  60 */       return (SortedBidiMap)map;
/*     */     }
/*     */     
/*  63 */     return new UnmodifiableSortedBidiMap<K, V>(map);
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
/*     */   private UnmodifiableSortedBidiMap(SortedBidiMap<K, ? extends V> map) {
/*  75 */     super((SortedBidiMap)map);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  81 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/*  86 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> mapToCopy) {
/*  91 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/*  96 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 101 */     Set<Map.Entry<K, V>> set = super.entrySet();
/* 102 */     return UnmodifiableEntrySet.unmodifiableEntrySet(set);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 107 */     Set<K> set = super.keySet();
/* 108 */     return UnmodifiableSet.unmodifiableSet(set);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<V> values() {
/* 113 */     Set<V> set = super.values();
/* 114 */     return UnmodifiableSet.unmodifiableSet(set);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public K removeValue(Object value) {
/* 120 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public OrderedMapIterator<K, V> mapIterator() {
/* 126 */     OrderedMapIterator<K, V> it = decorated().mapIterator();
/* 127 */     return UnmodifiableOrderedMapIterator.unmodifiableOrderedMapIterator(it);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedBidiMap<V, K> inverseBidiMap() {
/* 133 */     if (this.inverse == null) {
/* 134 */       this.inverse = new UnmodifiableSortedBidiMap(decorated().inverseBidiMap());
/* 135 */       this.inverse.inverse = this;
/*     */     } 
/* 137 */     return this.inverse;
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 142 */     SortedMap<K, V> sm = decorated().subMap(fromKey, toKey);
/* 143 */     return UnmodifiableSortedMap.unmodifiableSortedMap(sm);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> headMap(K toKey) {
/* 148 */     SortedMap<K, V> sm = decorated().headMap(toKey);
/* 149 */     return UnmodifiableSortedMap.unmodifiableSortedMap(sm);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> tailMap(K fromKey) {
/* 154 */     SortedMap<K, V> sm = decorated().tailMap(fromKey);
/* 155 */     return UnmodifiableSortedMap.unmodifiableSortedMap(sm);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\bidimap\UnmodifiableSortedBidiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */