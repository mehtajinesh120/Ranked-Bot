/*     */ package org.apache.commons.collections4.iterators;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import org.apache.commons.collections4.ResettableListIterator;
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
/*     */ public class ReverseListIterator<E>
/*     */   implements ResettableListIterator<E>
/*     */ {
/*     */   private final List<E> list;
/*     */   private ListIterator<E> iterator;
/*     */   private boolean validForUpdate = true;
/*     */   
/*     */   public ReverseListIterator(List<E> list) {
/*  56 */     if (list == null) {
/*  57 */       throw new NullPointerException("List must not be null.");
/*     */     }
/*  59 */     this.list = list;
/*  60 */     this.iterator = list.listIterator(list.size());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/*  70 */     return this.iterator.hasPrevious();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E next() {
/*  80 */     E obj = this.iterator.previous();
/*  81 */     this.validForUpdate = true;
/*  82 */     return obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int nextIndex() {
/*  91 */     return this.iterator.previousIndex();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPrevious() {
/* 100 */     return this.iterator.hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E previous() {
/* 110 */     E obj = this.iterator.next();
/* 111 */     this.validForUpdate = true;
/* 112 */     return obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int previousIndex() {
/* 121 */     return this.iterator.nextIndex();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() {
/* 131 */     if (!this.validForUpdate) {
/* 132 */       throw new IllegalStateException("Cannot remove from list until next() or previous() called");
/*     */     }
/* 134 */     this.iterator.remove();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(E obj) {
/* 145 */     if (!this.validForUpdate) {
/* 146 */       throw new IllegalStateException("Cannot set to list until next() or previous() called");
/*     */     }
/* 148 */     this.iterator.set(obj);
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
/*     */   public void add(E obj) {
/* 161 */     if (!this.validForUpdate) {
/* 162 */       throw new IllegalStateException("Cannot add to list until next() or previous() called");
/*     */     }
/* 164 */     this.validForUpdate = false;
/* 165 */     this.iterator.add(obj);
/* 166 */     this.iterator.previous();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 174 */     this.iterator = this.list.listIterator(this.list.size());
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\iterators\ReverseListIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */