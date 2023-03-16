/*     */ package org.apache.commons.collections4.iterators;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public class BoundedIterator<E>
/*     */   implements Iterator<E>
/*     */ {
/*     */   private final Iterator<? extends E> iterator;
/*     */   private final long offset;
/*     */   private final long max;
/*     */   private long pos;
/*     */   
/*     */   public BoundedIterator(Iterator<? extends E> iterator, long offset, long max) {
/*  64 */     if (iterator == null) {
/*  65 */       throw new NullPointerException("Iterator must not be null");
/*     */     }
/*  67 */     if (offset < 0L) {
/*  68 */       throw new IllegalArgumentException("Offset parameter must not be negative.");
/*     */     }
/*  70 */     if (max < 0L) {
/*  71 */       throw new IllegalArgumentException("Max parameter must not be negative.");
/*     */     }
/*     */     
/*  74 */     this.iterator = iterator;
/*  75 */     this.offset = offset;
/*  76 */     this.max = max;
/*  77 */     this.pos = 0L;
/*  78 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() {
/*  85 */     while (this.pos < this.offset && this.iterator.hasNext()) {
/*  86 */       this.iterator.next();
/*  87 */       this.pos++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/*  94 */     if (!checkBounds()) {
/*  95 */       return false;
/*     */     }
/*  97 */     return this.iterator.hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkBounds() {
/* 105 */     if (this.pos - this.offset + 1L > this.max) {
/* 106 */       return false;
/*     */     }
/* 108 */     return true;
/*     */   }
/*     */   
/*     */   public E next() {
/* 112 */     if (!checkBounds()) {
/* 113 */       throw new NoSuchElementException();
/*     */     }
/* 115 */     E next = this.iterator.next();
/* 116 */     this.pos++;
/* 117 */     return next;
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
/*     */   public void remove() {
/* 129 */     if (this.pos <= this.offset) {
/* 130 */       throw new IllegalStateException("remove() can not be called before calling next()");
/*     */     }
/* 132 */     this.iterator.remove();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\iterators\BoundedIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */