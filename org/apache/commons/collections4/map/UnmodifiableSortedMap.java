/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import org.apache.commons.collections4.Unmodifiable;
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
/*     */ public final class UnmodifiableSortedMap<K, V>
/*     */   extends AbstractSortedMapDecorator<K, V>
/*     */   implements Unmodifiable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5805344239827376360L;
/*     */   
/*     */   public static <K, V> SortedMap<K, V> unmodifiableSortedMap(SortedMap<K, ? extends V> map) {
/*  61 */     if (map instanceof Unmodifiable)
/*     */     {
/*  63 */       return (SortedMap)map;
/*     */     }
/*     */     
/*  66 */     return (SortedMap<K, V>)new UnmodifiableSortedMap<K, V>(map);
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
/*     */   private UnmodifiableSortedMap(SortedMap<K, ? extends V> map) {
/*  78 */     super((SortedMap)map);
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
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/*  90 */     out.defaultWriteObject();
/*  91 */     out.writeObject(this.map);
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
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 104 */     in.defaultReadObject();
/* 105 */     this.map = (Map<K, V>)in.readObject();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 111 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/* 116 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> mapToCopy) {
/* 121 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/* 126 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 131 */     return UnmodifiableEntrySet.unmodifiableEntrySet(super.entrySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 136 */     return UnmodifiableSet.unmodifiableSet(super.keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 141 */     return UnmodifiableCollection.unmodifiableCollection(super.values());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public K firstKey() {
/* 147 */     return decorated().firstKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public K lastKey() {
/* 152 */     return decorated().lastKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super K> comparator() {
/* 157 */     return decorated().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 162 */     return (SortedMap<K, V>)new UnmodifiableSortedMap(decorated().subMap(fromKey, toKey));
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> headMap(K toKey) {
/* 167 */     return (SortedMap<K, V>)new UnmodifiableSortedMap(decorated().headMap(toKey));
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> tailMap(K fromKey) {
/* 172 */     return (SortedMap<K, V>)new UnmodifiableSortedMap(decorated().tailMap(fromKey));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\UnmodifiableSortedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */