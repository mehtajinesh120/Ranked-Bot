/*     */ package org.apache.commons.collections4.set;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import org.apache.commons.collections4.Predicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PredicatedNavigableSet<E>
/*     */   extends PredicatedSortedSet<E>
/*     */   implements NavigableSet<E>
/*     */ {
/*     */   private static final long serialVersionUID = 20150528L;
/*     */   
/*     */   public static <E> PredicatedNavigableSet<E> predicatedNavigableSet(NavigableSet<E> set, Predicate<? super E> predicate) {
/*  63 */     return new PredicatedNavigableSet<E>(set, predicate);
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
/*     */   protected PredicatedNavigableSet(NavigableSet<E> set, Predicate<? super E> predicate) {
/*  79 */     super(set, predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected NavigableSet<E> decorated() {
/*  89 */     return (NavigableSet<E>)super.decorated();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E lower(E e) {
/*  96 */     return decorated().lower(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E floor(E e) {
/* 101 */     return decorated().floor(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E ceiling(E e) {
/* 106 */     return decorated().ceiling(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E higher(E e) {
/* 111 */     return decorated().higher(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E pollFirst() {
/* 116 */     return decorated().pollFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E pollLast() {
/* 121 */     return decorated().pollLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> descendingSet() {
/* 126 */     return predicatedNavigableSet(decorated().descendingSet(), this.predicate);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> descendingIterator() {
/* 131 */     return decorated().descendingIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 136 */     NavigableSet<E> sub = decorated().subSet(fromElement, fromInclusive, toElement, toInclusive);
/* 137 */     return predicatedNavigableSet(sub, this.predicate);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 142 */     NavigableSet<E> head = decorated().headSet(toElement, inclusive);
/* 143 */     return predicatedNavigableSet(head, this.predicate);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 148 */     NavigableSet<E> tail = decorated().tailSet(fromElement, inclusive);
/* 149 */     return predicatedNavigableSet(tail, this.predicate);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\set\PredicatedNavigableSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */