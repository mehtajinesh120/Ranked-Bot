/*     */ package org.apache.commons.collections4.iterators;
/*     */ 
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
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
/*     */ public class PushbackIterator<E>
/*     */   implements Iterator<E>
/*     */ {
/*     */   private final Iterator<? extends E> iterator;
/*  42 */   private Deque<E> items = new ArrayDeque<E>();
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
/*     */   public static <E> PushbackIterator<E> pushbackIterator(Iterator<? extends E> iterator) {
/*  56 */     if (iterator == null) {
/*  57 */       throw new NullPointerException("Iterator must not be null");
/*     */     }
/*  59 */     if (iterator instanceof PushbackIterator) {
/*     */       
/*  61 */       PushbackIterator<E> it = (PushbackIterator)iterator;
/*  62 */       return it;
/*     */     } 
/*  64 */     return new PushbackIterator<E>(iterator);
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
/*     */   public PushbackIterator(Iterator<? extends E> iterator) {
/*  76 */     this.iterator = iterator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pushback(E item) {
/*  87 */     this.items.push(item);
/*     */   }
/*     */   
/*     */   public boolean hasNext() {
/*  91 */     return !this.items.isEmpty() ? true : this.iterator.hasNext();
/*     */   }
/*     */   
/*     */   public E next() {
/*  95 */     return !this.items.isEmpty() ? this.items.pop() : this.iterator.next();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() {
/* 104 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\iterators\PushbackIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */