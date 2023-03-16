/*     */ package org.apache.commons.collections4.iterators;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
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
/*     */ public class EnumerationIterator<E>
/*     */   implements Iterator<E>
/*     */ {
/*     */   private final Collection<? super E> collection;
/*     */   private Enumeration<? extends E> enumeration;
/*     */   private E last;
/*     */   
/*     */   public EnumerationIterator() {
/*  46 */     this(null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EnumerationIterator(Enumeration<? extends E> enumeration) {
/*  56 */     this(enumeration, null);
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
/*     */   public EnumerationIterator(Enumeration<? extends E> enumeration, Collection<? super E> collection) {
/*  68 */     this.enumeration = enumeration;
/*  69 */     this.collection = collection;
/*  70 */     this.last = null;
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
/*  82 */     return this.enumeration.hasMoreElements();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E next() {
/*  92 */     this.last = this.enumeration.nextElement();
/*  93 */     return this.last;
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
/*     */   public void remove() {
/* 107 */     if (this.collection != null) {
/* 108 */       if (this.last != null) {
/* 109 */         this.collection.remove(this.last);
/*     */       } else {
/* 111 */         throw new IllegalStateException("next() must have been called for remove() to function");
/*     */       } 
/*     */     } else {
/* 114 */       throw new UnsupportedOperationException("No Collection associated with this Iterator");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration<? extends E> getEnumeration() {
/* 126 */     return this.enumeration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnumeration(Enumeration<? extends E> enumeration) {
/* 135 */     this.enumeration = enumeration;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\iterators\EnumerationIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */