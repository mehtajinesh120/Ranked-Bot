/*     */ package org.apache.commons.collections4.set;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractNavigableSetDecorator<E>
/*     */   extends AbstractSortedSetDecorator<E>
/*     */   implements NavigableSet<E>
/*     */ {
/*     */   private static final long serialVersionUID = 20150528L;
/*     */   
/*     */   protected AbstractNavigableSetDecorator() {}
/*     */   
/*     */   protected AbstractNavigableSetDecorator(NavigableSet<E> set) {
/*  52 */     super(set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected NavigableSet<E> decorated() {
/*  62 */     return (NavigableSet<E>)super.decorated();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E lower(E e) {
/*  69 */     return decorated().lower(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E floor(E e) {
/*  74 */     return decorated().floor(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E ceiling(E e) {
/*  79 */     return decorated().ceiling(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E higher(E e) {
/*  84 */     return decorated().higher(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E pollFirst() {
/*  89 */     return decorated().pollFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E pollLast() {
/*  94 */     return decorated().pollLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> descendingSet() {
/*  99 */     return decorated().descendingSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> descendingIterator() {
/* 104 */     return decorated().descendingIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 109 */     return decorated().subSet(fromElement, fromInclusive, toElement, toInclusive);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 114 */     return decorated().headSet(toElement, inclusive);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 119 */     return decorated().tailSet(fromElement, inclusive);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\set\AbstractNavigableSetDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */