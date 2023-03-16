/*     */ package org.apache.commons.collections4.iterators;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import org.apache.commons.collections4.ResettableIterator;
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
/*     */ public class IteratorIterable<E>
/*     */   implements Iterable<E>
/*     */ {
/*     */   private final Iterator<? extends E> iterator;
/*     */   private final Iterator<E> typeSafeIterator;
/*     */   
/*     */   private static <E> Iterator<E> createTypesafeIterator(final Iterator<? extends E> iterator) {
/*  70 */     return new Iterator<E>() {
/*     */         public boolean hasNext() {
/*  72 */           return iterator.hasNext();
/*     */         }
/*     */         
/*     */         public E next() {
/*  76 */           return iterator.next();
/*     */         }
/*     */         
/*     */         public void remove() {
/*  80 */           iterator.remove();
/*     */         }
/*     */       };
/*     */   }
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
/*     */   public IteratorIterable(Iterator<? extends E> iterator) {
/*  98 */     this(iterator, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IteratorIterable(Iterator<? extends E> iterator, boolean multipleUse) {
/* 110 */     if (multipleUse && !(iterator instanceof ResettableIterator)) {
/* 111 */       this.iterator = (Iterator<? extends E>)new ListIteratorWrapper<E>(iterator);
/*     */     } else {
/* 113 */       this.iterator = iterator;
/*     */     } 
/* 115 */     this.typeSafeIterator = createTypesafeIterator(this.iterator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 124 */     if (this.iterator instanceof ResettableIterator) {
/* 125 */       ((ResettableIterator)this.iterator).reset();
/*     */     }
/* 127 */     return this.typeSafeIterator;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\iterators\IteratorIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */