/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import org.apache.commons.collections4.IterableSortedMap;
/*     */ import org.apache.commons.collections4.MapIterator;
/*     */ import org.apache.commons.collections4.OrderedMapIterator;
/*     */ import org.apache.commons.collections4.iterators.ListIteratorWrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractSortedMapDecorator<K, V>
/*     */   extends AbstractMapDecorator<K, V>
/*     */   implements IterableSortedMap<K, V>
/*     */ {
/*     */   protected AbstractSortedMapDecorator() {}
/*     */   
/*     */   public AbstractSortedMapDecorator(SortedMap<K, V> map) {
/*  65 */     super(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedMap<K, V> decorated() {
/*  75 */     return (SortedMap<K, V>)super.decorated();
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super K> comparator() {
/*  80 */     return decorated().comparator();
/*     */   }
/*     */   
/*     */   public K firstKey() {
/*  84 */     return decorated().firstKey();
/*     */   }
/*     */   
/*     */   public K lastKey() {
/*  88 */     return decorated().lastKey();
/*     */   }
/*     */   
/*     */   public SortedMap<K, V> subMap(K fromKey, K toKey) {
/*  92 */     return decorated().subMap(fromKey, toKey);
/*     */   }
/*     */   
/*     */   public SortedMap<K, V> headMap(K toKey) {
/*  96 */     return decorated().headMap(toKey);
/*     */   }
/*     */   
/*     */   public SortedMap<K, V> tailMap(K fromKey) {
/* 100 */     return decorated().tailMap(fromKey);
/*     */   }
/*     */   
/*     */   public K previousKey(K key) {
/* 104 */     SortedMap<K, V> headMap = headMap(key);
/* 105 */     return headMap.isEmpty() ? null : headMap.lastKey();
/*     */   }
/*     */   
/*     */   public K nextKey(K key) {
/* 109 */     Iterator<K> it = tailMap(key).keySet().iterator();
/* 110 */     it.next();
/* 111 */     return it.hasNext() ? it.next() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OrderedMapIterator<K, V> mapIterator() {
/* 119 */     return new SortedMapIterator<K, V>(entrySet());
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
/*     */   protected static class SortedMapIterator<K, V>
/*     */     extends EntrySetToMapIteratorAdapter<K, V>
/*     */     implements OrderedMapIterator<K, V>
/*     */   {
/*     */     protected SortedMapIterator(Set<Map.Entry<K, V>> entrySet) {
/* 136 */       super(entrySet);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized void reset() {
/* 144 */       super.reset();
/* 145 */       this.iterator = (Iterator<Map.Entry<K, V>>)new ListIteratorWrapper(this.iterator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasPrevious() {
/* 152 */       return ((ListIterator)this.iterator).hasPrevious();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public K previous() {
/* 159 */       this.entry = ((ListIterator<Map.Entry<K, V>>)this.iterator).previous();
/* 160 */       return getKey();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\AbstractSortedMapDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */