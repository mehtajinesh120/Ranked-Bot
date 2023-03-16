/*    */ package org.apache.commons.collections4.set;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Comparator;
/*    */ import java.util.Set;
/*    */ import java.util.SortedSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractSortedSetDecorator<E>
/*    */   extends AbstractSetDecorator<E>
/*    */   implements SortedSet<E>
/*    */ {
/*    */   private static final long serialVersionUID = -3462240946294214398L;
/*    */   
/*    */   protected AbstractSortedSetDecorator() {}
/*    */   
/*    */   protected AbstractSortedSetDecorator(Set<E> set) {
/* 54 */     super(set);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected SortedSet<E> decorated() {
/* 64 */     return (SortedSet<E>)super.decorated();
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSet<E> subSet(E fromElement, E toElement) {
/* 69 */     return decorated().subSet(fromElement, toElement);
/*    */   }
/*    */   
/*    */   public SortedSet<E> headSet(E toElement) {
/* 73 */     return decorated().headSet(toElement);
/*    */   }
/*    */   
/*    */   public SortedSet<E> tailSet(E fromElement) {
/* 77 */     return decorated().tailSet(fromElement);
/*    */   }
/*    */   
/*    */   public E first() {
/* 81 */     return decorated().first();
/*    */   }
/*    */   
/*    */   public E last() {
/* 85 */     return decorated().last();
/*    */   }
/*    */   
/*    */   public Comparator<? super E> comparator() {
/* 89 */     return decorated().comparator();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\set\AbstractSortedSetDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */