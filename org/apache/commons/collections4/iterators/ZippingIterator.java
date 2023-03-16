/*     */ package org.apache.commons.collections4.iterators;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.commons.collections4.FluentIterable;
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
/*     */ public class ZippingIterator<E>
/*     */   implements Iterator<E>
/*     */ {
/*     */   private final Iterator<Iterator<? extends E>> iterators;
/*  43 */   private Iterator<? extends E> nextIterator = null;
/*     */ 
/*     */   
/*  46 */   private Iterator<? extends E> lastReturned = null;
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
/*     */   public ZippingIterator(Iterator<? extends E> a, Iterator<? extends E> b) {
/*  61 */     this((Iterator<? extends E>[])new Iterator[] { a, b });
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
/*     */   public ZippingIterator(Iterator<? extends E> a, Iterator<? extends E> b, Iterator<? extends E> c) {
/*  77 */     this((Iterator<? extends E>[])new Iterator[] { a, b, c });
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
/*     */   public ZippingIterator(Iterator<? extends E>... iterators) {
/*  89 */     List<Iterator<? extends E>> list = new ArrayList<Iterator<? extends E>>();
/*  90 */     for (Iterator<? extends E> iterator : iterators) {
/*  91 */       if (iterator == null) {
/*  92 */         throw new NullPointerException("Iterator must not be null.");
/*     */       }
/*  94 */       list.add(iterator);
/*     */     } 
/*  96 */     this.iterators = FluentIterable.of(list).loop().iterator();
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
/*     */   public boolean hasNext() {
/* 110 */     if (this.nextIterator != null) {
/* 111 */       return true;
/*     */     }
/*     */     
/* 114 */     while (this.iterators.hasNext()) {
/* 115 */       Iterator<? extends E> childIterator = this.iterators.next();
/* 116 */       if (childIterator.hasNext()) {
/* 117 */         this.nextIterator = childIterator;
/* 118 */         return true;
/*     */       } 
/*     */       
/* 121 */       this.iterators.remove();
/*     */     } 
/*     */     
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E next() throws NoSuchElementException {
/* 134 */     if (!hasNext()) {
/* 135 */       throw new NoSuchElementException();
/*     */     }
/*     */     
/* 138 */     E val = this.nextIterator.next();
/* 139 */     this.lastReturned = this.nextIterator;
/* 140 */     this.nextIterator = null;
/* 141 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() {
/* 151 */     if (this.lastReturned == null) {
/* 152 */       throw new IllegalStateException("No value can be removed at present");
/*     */     }
/* 154 */     this.lastReturned.remove();
/* 155 */     this.lastReturned = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\iterators\ZippingIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */