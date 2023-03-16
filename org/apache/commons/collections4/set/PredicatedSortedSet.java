/*     */ package org.apache.commons.collections4.set;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PredicatedSortedSet<E>
/*     */   extends PredicatedSet<E>
/*     */   implements SortedSet<E>
/*     */ {
/*     */   private static final long serialVersionUID = -9110948148132275052L;
/*     */   
/*     */   public static <E> PredicatedSortedSet<E> predicatedSortedSet(SortedSet<E> set, Predicate<? super E> predicate) {
/*  65 */     return new PredicatedSortedSet<E>(set, predicate);
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
/*     */   protected PredicatedSortedSet(SortedSet<E> set, Predicate<? super E> predicate) {
/*  81 */     super(set, predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedSet<E> decorated() {
/*  91 */     return (SortedSet<E>)super.decorated();
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/*  96 */     return decorated().comparator();
/*     */   }
/*     */   
/*     */   public E first() {
/* 100 */     return decorated().first();
/*     */   }
/*     */   
/*     */   public E last() {
/* 104 */     return decorated().last();
/*     */   }
/*     */   
/*     */   public SortedSet<E> subSet(E fromElement, E toElement) {
/* 108 */     SortedSet<E> sub = decorated().subSet(fromElement, toElement);
/* 109 */     return new PredicatedSortedSet(sub, this.predicate);
/*     */   }
/*     */   
/*     */   public SortedSet<E> headSet(E toElement) {
/* 113 */     SortedSet<E> head = decorated().headSet(toElement);
/* 114 */     return new PredicatedSortedSet(head, this.predicate);
/*     */   }
/*     */   
/*     */   public SortedSet<E> tailSet(E fromElement) {
/* 118 */     SortedSet<E> tail = decorated().tailSet(fromElement);
/* 119 */     return new PredicatedSortedSet(tail, this.predicate);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\set\PredicatedSortedSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */