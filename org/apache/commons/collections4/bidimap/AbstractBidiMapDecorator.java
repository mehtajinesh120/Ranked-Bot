/*    */ package org.apache.commons.collections4.bidimap;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.apache.commons.collections4.BidiMap;
/*    */ import org.apache.commons.collections4.MapIterator;
/*    */ import org.apache.commons.collections4.map.AbstractMapDecorator;
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
/*    */ public abstract class AbstractBidiMapDecorator<K, V>
/*    */   extends AbstractMapDecorator<K, V>
/*    */   implements BidiMap<K, V>
/*    */ {
/*    */   protected AbstractBidiMapDecorator(BidiMap<K, V> map) {
/* 50 */     super((Map)map);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected BidiMap<K, V> decorated() {
/* 60 */     return (BidiMap<K, V>)super.decorated();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public MapIterator<K, V> mapIterator() {
/* 66 */     return decorated().mapIterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public K getKey(Object value) {
/* 71 */     return (K)decorated().getKey(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public K removeValue(Object value) {
/* 76 */     return (K)decorated().removeValue(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public BidiMap<V, K> inverseBidiMap() {
/* 81 */     return decorated().inverseBidiMap();
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<V> values() {
/* 86 */     return decorated().values();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\bidimap\AbstractBidiMapDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */