/*    */ package org.apache.commons.collections4.iterators;
/*    */ 
/*    */ import java.util.Iterator;
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
/*    */ 
/*    */ public final class UnmodifiableIterator<E>
/*    */   implements Iterator<E>, Unmodifiable
/*    */ {
/*    */   private final Iterator<? extends E> iterator;
/*    */   
/*    */   public static <E> Iterator<E> unmodifiableIterator(Iterator<? extends E> iterator) {
/* 48 */     if (iterator == null) {
/* 49 */       throw new NullPointerException("Iterator must not be null");
/*    */     }
/* 51 */     if (iterator instanceof Unmodifiable)
/*    */     {
/* 53 */       return (Iterator)iterator;
/*    */     }
/*    */     
/* 56 */     return new UnmodifiableIterator<E>(iterator);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private UnmodifiableIterator(Iterator<? extends E> iterator) {
/* 67 */     this.iterator = iterator;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasNext() {
/* 72 */     return this.iterator.hasNext();
/*    */   }
/*    */   
/*    */   public E next() {
/* 76 */     return this.iterator.next();
/*    */   }
/*    */   
/*    */   public void remove() {
/* 80 */     throw new UnsupportedOperationException("remove() is not supported");
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\iterators\UnmodifiableIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */