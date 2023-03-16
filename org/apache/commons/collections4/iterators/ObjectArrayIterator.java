/*     */ package org.apache.commons.collections4.iterators;
/*     */ 
/*     */ import java.util.NoSuchElementException;
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
/*     */ public class ObjectArrayIterator<E>
/*     */   implements ResettableIterator<E>
/*     */ {
/*     */   final E[] array;
/*     */   final int startIndex;
/*     */   final int endIndex;
/*  44 */   int index = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectArrayIterator(E... array) {
/*  55 */     this(array, 0, array.length);
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
/*     */   public ObjectArrayIterator(E[] array, int start) {
/*  68 */     this(array, start, array.length);
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
/*     */   public ObjectArrayIterator(E[] array, int start, int end) {
/*  84 */     if (start < 0) {
/*  85 */       throw new ArrayIndexOutOfBoundsException("Start index must not be less than zero");
/*     */     }
/*  87 */     if (end > array.length) {
/*  88 */       throw new ArrayIndexOutOfBoundsException("End index must not be greater than the array length");
/*     */     }
/*  90 */     if (start > array.length) {
/*  91 */       throw new ArrayIndexOutOfBoundsException("Start index must not be greater than the array length");
/*     */     }
/*  93 */     if (end < start) {
/*  94 */       throw new IllegalArgumentException("End index must not be less than start index");
/*     */     }
/*  96 */     this.array = array;
/*  97 */     this.startIndex = start;
/*  98 */     this.endIndex = end;
/*  99 */     this.index = start;
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
/*     */   public boolean hasNext() {
/* 111 */     return (this.index < this.endIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E next() {
/* 122 */     if (!hasNext()) {
/* 123 */       throw new NoSuchElementException();
/*     */     }
/* 125 */     return this.array[this.index++];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() {
/* 134 */     throw new UnsupportedOperationException("remove() method is not supported for an ObjectArrayIterator");
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
/*     */   public E[] getArray() {
/* 146 */     return this.array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStartIndex() {
/* 155 */     return this.startIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEndIndex() {
/* 164 */     return this.endIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 171 */     this.index = this.startIndex;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\iterators\ObjectArrayIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */