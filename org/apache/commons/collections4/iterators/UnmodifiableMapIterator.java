/*    */ package org.apache.commons.collections4.iterators;
/*    */ 
/*    */ import org.apache.commons.collections4.MapIterator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class UnmodifiableMapIterator<K, V>
/*    */   implements MapIterator<K, V>, Unmodifiable
/*    */ {
/*    */   private final MapIterator<? extends K, ? extends V> iterator;
/*    */   
/*    */   public static <K, V> MapIterator<K, V> unmodifiableMapIterator(MapIterator<? extends K, ? extends V> iterator) {
/* 47 */     if (iterator == null) {
/* 48 */       throw new NullPointerException("MapIterator must not be null");
/*    */     }
/* 50 */     if (iterator instanceof Unmodifiable)
/*    */     {
/* 52 */       return (MapIterator)iterator;
/*    */     }
/*    */     
/* 55 */     return new UnmodifiableMapIterator<K, V>(iterator);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private UnmodifiableMapIterator(MapIterator<? extends K, ? extends V> iterator) {
/* 66 */     this.iterator = iterator;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasNext() {
/* 71 */     return this.iterator.hasNext();
/*    */   }
/*    */   
/*    */   public K next() {
/* 75 */     return (K)this.iterator.next();
/*    */   }
/*    */   
/*    */   public K getKey() {
/* 79 */     return (K)this.iterator.getKey();
/*    */   }
/*    */   
/*    */   public V getValue() {
/* 83 */     return (V)this.iterator.getValue();
/*    */   }
/*    */   
/*    */   public V setValue(V value) {
/* 87 */     throw new UnsupportedOperationException("setValue() is not supported");
/*    */   }
/*    */   
/*    */   public void remove() {
/* 91 */     throw new UnsupportedOperationException("remove() is not supported");
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\iterators\UnmodifiableMapIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */