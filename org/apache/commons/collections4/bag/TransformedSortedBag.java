/*     */ package org.apache.commons.collections4.bag;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import org.apache.commons.collections4.Bag;
/*     */ import org.apache.commons.collections4.SortedBag;
/*     */ import org.apache.commons.collections4.Transformer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TransformedSortedBag<E>
/*     */   extends TransformedBag<E>
/*     */   implements SortedBag<E>
/*     */ {
/*     */   private static final long serialVersionUID = -251737742649401930L;
/*     */   
/*     */   public static <E> TransformedSortedBag<E> transformingSortedBag(SortedBag<E> bag, Transformer<? super E, ? extends E> transformer) {
/*  57 */     return new TransformedSortedBag<E>(bag, transformer);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> TransformedSortedBag<E> transformedSortedBag(SortedBag<E> bag, Transformer<? super E, ? extends E> transformer) {
/*  78 */     TransformedSortedBag<E> decorated = new TransformedSortedBag<E>(bag, transformer);
/*  79 */     if (bag.size() > 0) {
/*     */       
/*  81 */       E[] values = (E[])bag.toArray();
/*  82 */       bag.clear();
/*  83 */       for (E value : values) {
/*  84 */         decorated.decorated().add(transformer.transform(value));
/*     */       }
/*     */     } 
/*  87 */     return decorated;
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
/*     */   protected TransformedSortedBag(SortedBag<E> bag, Transformer<? super E, ? extends E> transformer) {
/* 102 */     super((Bag<E>)bag, transformer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedBag<E> getSortedBag() {
/* 111 */     return (SortedBag<E>)decorated();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E first() {
/* 118 */     return (E)getSortedBag().first();
/*     */   }
/*     */ 
/*     */   
/*     */   public E last() {
/* 123 */     return (E)getSortedBag().last();
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/* 128 */     return getSortedBag().comparator();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\bag\TransformedSortedBag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */