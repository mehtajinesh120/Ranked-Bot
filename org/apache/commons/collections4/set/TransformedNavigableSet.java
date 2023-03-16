/*     */ package org.apache.commons.collections4.set;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
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
/*     */ public class TransformedNavigableSet<E>
/*     */   extends TransformedSortedSet<E>
/*     */   implements NavigableSet<E>
/*     */ {
/*     */   private static final long serialVersionUID = 20150528L;
/*     */   
/*     */   public static <E> TransformedNavigableSet<E> transformingNavigableSet(NavigableSet<E> set, Transformer<? super E, ? extends E> transformer) {
/*  55 */     return new TransformedNavigableSet<E>(set, transformer);
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
/*     */   public static <E> TransformedNavigableSet<E> transformedNavigableSet(NavigableSet<E> set, Transformer<? super E, ? extends E> transformer) {
/*  75 */     TransformedNavigableSet<E> decorated = new TransformedNavigableSet<E>(set, transformer);
/*  76 */     if (set.size() > 0) {
/*     */       
/*  78 */       E[] values = (E[])set.toArray();
/*  79 */       set.clear();
/*  80 */       for (E value : values) {
/*  81 */         decorated.decorated().add(transformer.transform(value));
/*     */       }
/*     */     } 
/*  84 */     return decorated;
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
/*     */   protected TransformedNavigableSet(NavigableSet<E> set, Transformer<? super E, ? extends E> transformer) {
/* 100 */     super(set, transformer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected NavigableSet<E> decorated() {
/* 110 */     return (NavigableSet<E>)super.decorated();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E lower(E e) {
/* 117 */     return decorated().lower(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E floor(E e) {
/* 122 */     return decorated().floor(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E ceiling(E e) {
/* 127 */     return decorated().ceiling(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E higher(E e) {
/* 132 */     return decorated().higher(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E pollFirst() {
/* 137 */     return decorated().pollFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E pollLast() {
/* 142 */     return decorated().pollLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> descendingSet() {
/* 147 */     return transformingNavigableSet(decorated().descendingSet(), this.transformer);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> descendingIterator() {
/* 152 */     return decorated().descendingIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 157 */     NavigableSet<E> sub = decorated().subSet(fromElement, fromInclusive, toElement, toInclusive);
/* 158 */     return transformingNavigableSet(sub, this.transformer);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 163 */     NavigableSet<E> head = decorated().headSet(toElement, inclusive);
/* 164 */     return transformingNavigableSet(head, this.transformer);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 169 */     NavigableSet<E> tail = decorated().tailSet(fromElement, inclusive);
/* 170 */     return transformingNavigableSet(tail, this.transformer);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\set\TransformedNavigableSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */