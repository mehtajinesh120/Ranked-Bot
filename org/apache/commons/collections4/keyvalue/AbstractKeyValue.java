/*    */ package org.apache.commons.collections4.keyvalue;
/*    */ 
/*    */ import org.apache.commons.collections4.KeyValue;
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
/*    */ public abstract class AbstractKeyValue<K, V>
/*    */   implements KeyValue<K, V>
/*    */ {
/*    */   private K key;
/*    */   private V value;
/*    */   
/*    */   protected AbstractKeyValue(K key, V value) {
/* 43 */     this.key = key;
/* 44 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public K getKey() {
/* 53 */     return this.key;
/*    */   }
/*    */   
/*    */   protected K setKey(K key) {
/* 57 */     K old = this.key;
/* 58 */     this.key = key;
/* 59 */     return old;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public V getValue() {
/* 68 */     return this.value;
/*    */   }
/*    */   
/*    */   protected V setValue(V value) {
/* 72 */     V old = this.value;
/* 73 */     this.value = value;
/* 74 */     return old;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 84 */     return (new StringBuilder()).append(getKey()).append('=').append(getValue()).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\keyvalue\AbstractKeyValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */