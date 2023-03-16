/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import org.apache.commons.collections4.BoundedMap;
/*     */ import org.apache.commons.collections4.CollectionUtils;
/*     */ import org.apache.commons.collections4.collection.UnmodifiableCollection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FixedSizeSortedMap<K, V>
/*     */   extends AbstractSortedMapDecorator<K, V>
/*     */   implements BoundedMap<K, V>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3126019624511683653L;
/*     */   
/*     */   public static <K, V> FixedSizeSortedMap<K, V> fixedSizeSortedMap(SortedMap<K, V> map) {
/*  75 */     return new FixedSizeSortedMap<K, V>(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FixedSizeSortedMap(SortedMap<K, V> map) {
/*  86 */     super(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedMap<K, V> getSortedMap() {
/*  95 */     return (SortedMap<K, V>)this.map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 103 */     out.defaultWriteObject();
/* 104 */     out.writeObject(this.map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 112 */     in.defaultReadObject();
/* 113 */     this.map = (Map<K, V>)in.readObject();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/* 119 */     if (!this.map.containsKey(key)) {
/* 120 */       throw new IllegalArgumentException("Cannot put new key/value pair - Map is fixed size");
/*     */     }
/* 122 */     return this.map.put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> mapToCopy) {
/* 127 */     if (CollectionUtils.isSubCollection(mapToCopy.keySet(), keySet())) {
/* 128 */       throw new IllegalArgumentException("Cannot put new key/value pair - Map is fixed size");
/*     */     }
/* 130 */     this.map.putAll(mapToCopy);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 135 */     throw new UnsupportedOperationException("Map is fixed size");
/*     */   }
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/* 140 */     throw new UnsupportedOperationException("Map is fixed size");
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 145 */     return UnmodifiableSet.unmodifiableSet(this.map.entrySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 150 */     return UnmodifiableSet.unmodifiableSet(this.map.keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 155 */     return UnmodifiableCollection.unmodifiableCollection(this.map.values());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 161 */     return (SortedMap<K, V>)new FixedSizeSortedMap(getSortedMap().subMap(fromKey, toKey));
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> headMap(K toKey) {
/* 166 */     return (SortedMap<K, V>)new FixedSizeSortedMap(getSortedMap().headMap(toKey));
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> tailMap(K fromKey) {
/* 171 */     return (SortedMap<K, V>)new FixedSizeSortedMap(getSortedMap().tailMap(fromKey));
/*     */   }
/*     */   
/*     */   public boolean isFull() {
/* 175 */     return true;
/*     */   }
/*     */   
/*     */   public int maxSize() {
/* 179 */     return size();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\FixedSizeSortedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */