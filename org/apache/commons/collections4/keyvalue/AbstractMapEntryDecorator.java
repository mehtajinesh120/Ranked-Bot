/*    */ package org.apache.commons.collections4.keyvalue;
/*    */ 
/*    */ import java.util.Map;
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
/*    */ public abstract class AbstractMapEntryDecorator<K, V>
/*    */   implements Map.Entry<K, V>, KeyValue<K, V>
/*    */ {
/*    */   private final Map.Entry<K, V> entry;
/*    */   
/*    */   public AbstractMapEntryDecorator(Map.Entry<K, V> entry) {
/* 42 */     if (entry == null) {
/* 43 */       throw new NullPointerException("Map Entry must not be null.");
/*    */     }
/* 45 */     this.entry = entry;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Map.Entry<K, V> getMapEntry() {
/* 54 */     return this.entry;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public K getKey() {
/* 60 */     return this.entry.getKey();
/*    */   }
/*    */   
/*    */   public V getValue() {
/* 64 */     return this.entry.getValue();
/*    */   }
/*    */   
/*    */   public V setValue(V object) {
/* 68 */     return this.entry.setValue(object);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object object) {
/* 73 */     if (object == this) {
/* 74 */       return true;
/*    */     }
/* 76 */     return this.entry.equals(object);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 81 */     return this.entry.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 86 */     return this.entry.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\keyvalue\AbstractMapEntryDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */