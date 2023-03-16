/*     */ package org.apache.commons.collections4.iterators;
/*     */ 
/*     */ import org.apache.commons.collections4.OrderedMapIterator;
/*     */ import org.apache.commons.collections4.Unmodifiable;
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
/*     */ public final class UnmodifiableOrderedMapIterator<K, V>
/*     */   implements OrderedMapIterator<K, V>, Unmodifiable
/*     */ {
/*     */   private final OrderedMapIterator<? extends K, ? extends V> iterator;
/*     */   
/*     */   public static <K, V> OrderedMapIterator<K, V> unmodifiableOrderedMapIterator(OrderedMapIterator<K, ? extends V> iterator) {
/*  49 */     if (iterator == null) {
/*  50 */       throw new NullPointerException("OrderedMapIterator must not be null");
/*     */     }
/*  52 */     if (iterator instanceof Unmodifiable)
/*     */     {
/*  54 */       return (OrderedMapIterator)iterator;
/*     */     }
/*     */     
/*  57 */     return new UnmodifiableOrderedMapIterator<K, V>(iterator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UnmodifiableOrderedMapIterator(OrderedMapIterator<K, ? extends V> iterator) {
/*  68 */     this.iterator = iterator;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/*  73 */     return this.iterator.hasNext();
/*     */   }
/*     */   
/*     */   public K next() {
/*  77 */     return (K)this.iterator.next();
/*     */   }
/*     */   
/*     */   public boolean hasPrevious() {
/*  81 */     return this.iterator.hasPrevious();
/*     */   }
/*     */   
/*     */   public K previous() {
/*  85 */     return (K)this.iterator.previous();
/*     */   }
/*     */   
/*     */   public K getKey() {
/*  89 */     return (K)this.iterator.getKey();
/*     */   }
/*     */   
/*     */   public V getValue() {
/*  93 */     return (V)this.iterator.getValue();
/*     */   }
/*     */   
/*     */   public V setValue(V value) {
/*  97 */     throw new UnsupportedOperationException("setValue() is not supported");
/*     */   }
/*     */   
/*     */   public void remove() {
/* 101 */     throw new UnsupportedOperationException("remove() is not supported");
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\iterators\UnmodifiableOrderedMapIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */