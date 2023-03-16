/*     */ package org.apache.commons.collections4.trie;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import org.apache.commons.collections4.MapIterator;
/*     */ import org.apache.commons.collections4.OrderedMapIterator;
/*     */ import org.apache.commons.collections4.Trie;
/*     */ import org.apache.commons.collections4.Unmodifiable;
/*     */ import org.apache.commons.collections4.iterators.UnmodifiableOrderedMapIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnmodifiableTrie<K, V>
/*     */   implements Trie<K, V>, Serializable, Unmodifiable
/*     */ {
/*     */   private static final long serialVersionUID = -7156426030315945159L;
/*     */   private final Trie<K, V> delegate;
/*     */   
/*     */   public static <K, V> Trie<K, V> unmodifiableTrie(Trie<K, ? extends V> trie) {
/*  55 */     if (trie instanceof Unmodifiable)
/*     */     {
/*  57 */       return (Trie)trie;
/*     */     }
/*     */     
/*  60 */     return new UnmodifiableTrie<K, V>(trie);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnmodifiableTrie(Trie<K, ? extends V> trie) {
/*  71 */     if (trie == null) {
/*  72 */       throw new NullPointerException("Trie must not be null");
/*     */     }
/*     */     
/*  75 */     Trie<K, ? extends V> trie1 = trie;
/*  76 */     this.delegate = (Trie)trie1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/*  82 */     return Collections.unmodifiableSet(this.delegate.entrySet());
/*     */   }
/*     */   
/*     */   public Set<K> keySet() {
/*  86 */     return Collections.unmodifiableSet(this.delegate.keySet());
/*     */   }
/*     */   
/*     */   public Collection<V> values() {
/*  90 */     return Collections.unmodifiableCollection(this.delegate.values());
/*     */   }
/*     */   
/*     */   public void clear() {
/*  94 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  98 */     return this.delegate.containsKey(key);
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 102 */     return this.delegate.containsValue(value);
/*     */   }
/*     */   
/*     */   public V get(Object key) {
/* 106 */     return (V)this.delegate.get(key);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 110 */     return this.delegate.isEmpty();
/*     */   }
/*     */   
/*     */   public V put(K key, V value) {
/* 114 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> m) {
/* 118 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public V remove(Object key) {
/* 122 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public int size() {
/* 126 */     return this.delegate.size();
/*     */   }
/*     */   
/*     */   public K firstKey() {
/* 130 */     return (K)this.delegate.firstKey();
/*     */   }
/*     */   
/*     */   public SortedMap<K, V> headMap(K toKey) {
/* 134 */     return Collections.unmodifiableSortedMap(this.delegate.headMap(toKey));
/*     */   }
/*     */   
/*     */   public K lastKey() {
/* 138 */     return (K)this.delegate.lastKey();
/*     */   }
/*     */   
/*     */   public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 142 */     return Collections.unmodifiableSortedMap(this.delegate.subMap(fromKey, toKey));
/*     */   }
/*     */   
/*     */   public SortedMap<K, V> tailMap(K fromKey) {
/* 146 */     return Collections.unmodifiableSortedMap(this.delegate.tailMap(fromKey));
/*     */   }
/*     */   
/*     */   public SortedMap<K, V> prefixMap(K key) {
/* 150 */     return Collections.unmodifiableSortedMap(this.delegate.prefixMap(key));
/*     */   }
/*     */   
/*     */   public Comparator<? super K> comparator() {
/* 154 */     return this.delegate.comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public OrderedMapIterator<K, V> mapIterator() {
/* 159 */     OrderedMapIterator<K, V> it = this.delegate.mapIterator();
/* 160 */     return UnmodifiableOrderedMapIterator.unmodifiableOrderedMapIterator(it);
/*     */   }
/*     */   
/*     */   public K nextKey(K key) {
/* 164 */     return (K)this.delegate.nextKey(key);
/*     */   }
/*     */   
/*     */   public K previousKey(K key) {
/* 168 */     return (K)this.delegate.previousKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 174 */     return this.delegate.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 179 */     return this.delegate.equals(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 184 */     return this.delegate.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\trie\UnmodifiableTrie.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */