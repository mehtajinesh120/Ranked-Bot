/*    */ package org.apache.commons.collections4.iterators;
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
/*    */ public abstract class AbstractEmptyMapIterator<K, V>
/*    */   extends AbstractEmptyIterator<K>
/*    */ {
/*    */   public K getKey() {
/* 35 */     throw new IllegalStateException("Iterator contains no elements");
/*    */   }
/*    */   
/*    */   public V getValue() {
/* 39 */     throw new IllegalStateException("Iterator contains no elements");
/*    */   }
/*    */   
/*    */   public V setValue(V value) {
/* 43 */     throw new IllegalStateException("Iterator contains no elements");
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\iterators\AbstractEmptyMapIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */