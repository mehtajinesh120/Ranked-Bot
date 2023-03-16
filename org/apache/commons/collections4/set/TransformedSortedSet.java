/*     */ package org.apache.commons.collections4.set;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.SortedSet;
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
/*     */ 
/*     */ 
/*     */ public class TransformedSortedSet<E>
/*     */   extends TransformedSet<E>
/*     */   implements SortedSet<E>
/*     */ {
/*     */   private static final long serialVersionUID = -1675486811351124386L;
/*     */   
/*     */   public static <E> TransformedSortedSet<E> transformingSortedSet(SortedSet<E> set, Transformer<? super E, ? extends E> transformer) {
/*  58 */     return new TransformedSortedSet<E>(set, transformer);
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
/*     */   public static <E> TransformedSortedSet<E> transformedSortedSet(SortedSet<E> set, Transformer<? super E, ? extends E> transformer) {
/*  79 */     TransformedSortedSet<E> decorated = new TransformedSortedSet<E>(set, transformer);
/*  80 */     if (set.size() > 0) {
/*     */       
/*  82 */       E[] values = (E[])set.toArray();
/*  83 */       set.clear();
/*  84 */       for (E value : values) {
/*  85 */         decorated.decorated().add(transformer.transform(value));
/*     */       }
/*     */     } 
/*  88 */     return decorated;
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
/*     */   protected TransformedSortedSet(SortedSet<E> set, Transformer<? super E, ? extends E> transformer) {
/* 103 */     super(set, transformer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedSet<E> getSortedSet() {
/* 112 */     return (SortedSet<E>)decorated();
/*     */   }
/*     */ 
/*     */   
/*     */   public E first() {
/* 117 */     return getSortedSet().first();
/*     */   }
/*     */   
/*     */   public E last() {
/* 121 */     return getSortedSet().last();
/*     */   }
/*     */   
/*     */   public Comparator<? super E> comparator() {
/* 125 */     return getSortedSet().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSet<E> subSet(E fromElement, E toElement) {
/* 130 */     SortedSet<E> set = getSortedSet().subSet(fromElement, toElement);
/* 131 */     return new TransformedSortedSet(set, this.transformer);
/*     */   }
/*     */   
/*     */   public SortedSet<E> headSet(E toElement) {
/* 135 */     SortedSet<E> set = getSortedSet().headSet(toElement);
/* 136 */     return new TransformedSortedSet(set, this.transformer);
/*     */   }
/*     */   
/*     */   public SortedSet<E> tailSet(E fromElement) {
/* 140 */     SortedSet<E> set = getSortedSet().tailSet(fromElement);
/* 141 */     return new TransformedSortedSet(set, this.transformer);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\set\TransformedSortedSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */