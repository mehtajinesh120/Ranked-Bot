/*    */ package org.apache.commons.collections4.bidimap;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import java.util.Map;
/*    */ import java.util.SortedMap;
/*    */ import org.apache.commons.collections4.BidiMap;
/*    */ import org.apache.commons.collections4.OrderedBidiMap;
/*    */ import org.apache.commons.collections4.SortedBidiMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractSortedBidiMapDecorator<K, V>
/*    */   extends AbstractOrderedBidiMapDecorator<K, V>
/*    */   implements SortedBidiMap<K, V>
/*    */ {
/*    */   public AbstractSortedBidiMapDecorator(SortedBidiMap<K, V> map) {
/* 49 */     super((OrderedBidiMap<K, V>)map);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected SortedBidiMap<K, V> decorated() {
/* 59 */     return (SortedBidiMap<K, V>)super.decorated();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SortedBidiMap<V, K> inverseBidiMap() {
/* 65 */     return decorated().inverseBidiMap();
/*    */   }
/*    */ 
/*    */   
/*    */   public Comparator<? super K> comparator() {
/* 70 */     return decorated().comparator();
/*    */   }
/*    */ 
/*    */   
/*    */   public Comparator<? super V> valueComparator() {
/* 75 */     return decorated().valueComparator();
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 80 */     return decorated().subMap(fromKey, toKey);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedMap<K, V> headMap(K toKey) {
/* 85 */     return decorated().headMap(toKey);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedMap<K, V> tailMap(K fromKey) {
/* 90 */     return decorated().tailMap(fromKey);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\bidimap\AbstractSortedBidiMapDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */