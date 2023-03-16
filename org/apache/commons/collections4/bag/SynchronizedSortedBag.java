/*     */ package org.apache.commons.collections4.bag;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import org.apache.commons.collections4.Bag;
/*     */ import org.apache.commons.collections4.SortedBag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SynchronizedSortedBag<E>
/*     */   extends SynchronizedBag<E>
/*     */   implements SortedBag<E>
/*     */ {
/*     */   private static final long serialVersionUID = 722374056718497858L;
/*     */   
/*     */   public static <E> SynchronizedSortedBag<E> synchronizedSortedBag(SortedBag<E> bag) {
/*  51 */     return new SynchronizedSortedBag<E>(bag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SynchronizedSortedBag(SortedBag<E> bag) {
/*  62 */     super((Bag<E>)bag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SynchronizedSortedBag(Bag<E> bag, Object lock) {
/*  73 */     super(bag, lock);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedBag<E> getSortedBag() {
/*  82 */     return (SortedBag<E>)decorated();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized E first() {
/*  89 */     synchronized (this.lock) {
/*  90 */       return (E)getSortedBag().first();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized E last() {
/*  96 */     synchronized (this.lock) {
/*  97 */       return (E)getSortedBag().last();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Comparator<? super E> comparator() {
/* 103 */     synchronized (this.lock) {
/* 104 */       return getSortedBag().comparator();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\bag\SynchronizedSortedBag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */