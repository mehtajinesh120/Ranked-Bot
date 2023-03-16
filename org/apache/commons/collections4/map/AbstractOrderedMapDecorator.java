/*    */ package org.apache.commons.collections4.map;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.commons.collections4.MapIterator;
/*    */ import org.apache.commons.collections4.OrderedMap;
/*    */ import org.apache.commons.collections4.OrderedMapIterator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractOrderedMapDecorator<K, V>
/*    */   extends AbstractMapDecorator<K, V>
/*    */   implements OrderedMap<K, V>
/*    */ {
/*    */   protected AbstractOrderedMapDecorator() {}
/*    */   
/*    */   public AbstractOrderedMapDecorator(OrderedMap<K, V> map) {
/* 55 */     super((Map<K, V>)map);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected OrderedMap<K, V> decorated() {
/* 65 */     return (OrderedMap<K, V>)super.decorated();
/*    */   }
/*    */ 
/*    */   
/*    */   public K firstKey() {
/* 70 */     return (K)decorated().firstKey();
/*    */   }
/*    */   
/*    */   public K lastKey() {
/* 74 */     return (K)decorated().lastKey();
/*    */   }
/*    */   
/*    */   public K nextKey(K key) {
/* 78 */     return (K)decorated().nextKey(key);
/*    */   }
/*    */   
/*    */   public K previousKey(K key) {
/* 82 */     return (K)decorated().previousKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public OrderedMapIterator<K, V> mapIterator() {
/* 87 */     return decorated().mapIterator();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\AbstractOrderedMapDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */