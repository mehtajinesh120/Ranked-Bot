/*    */ package org.apache.commons.collections4.keyvalue;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.commons.collections4.KeyValue;
/*    */ import org.apache.commons.collections4.Unmodifiable;
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
/*    */ public final class UnmodifiableMapEntry<K, V>
/*    */   extends AbstractMapEntry<K, V>
/*    */   implements Unmodifiable
/*    */ {
/*    */   public UnmodifiableMapEntry(K key, V value) {
/* 40 */     super(key, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnmodifiableMapEntry(KeyValue<? extends K, ? extends V> pair) {
/* 50 */     super((K)pair.getKey(), (V)pair.getValue());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnmodifiableMapEntry(Map.Entry<? extends K, ? extends V> entry) {
/* 60 */     super(entry.getKey(), entry.getValue());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public V setValue(V value) {
/* 72 */     throw new UnsupportedOperationException("setValue() is not supported");
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\keyvalue\UnmodifiableMapEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */