/*     */ package org.apache.commons.collections4.multiset;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.MultiSet;
/*     */ import org.apache.commons.collections4.Predicate;
/*     */ import org.apache.commons.collections4.collection.PredicatedCollection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PredicatedMultiSet<E>
/*     */   extends PredicatedCollection<E>
/*     */   implements MultiSet<E>
/*     */ {
/*     */   private static final long serialVersionUID = 20150629L;
/*     */   
/*     */   public static <E> PredicatedMultiSet<E> predicatedMultiSet(MultiSet<E> multiset, Predicate<? super E> predicate) {
/*  64 */     return new PredicatedMultiSet<E>(multiset, predicate);
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
/*     */   protected PredicatedMultiSet(MultiSet<E> multiset, Predicate<? super E> predicate) {
/*  80 */     super((Collection)multiset, predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MultiSet<E> decorated() {
/*  90 */     return (MultiSet<E>)super.decorated();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/*  95 */     return (object == this || decorated().equals(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 100 */     return decorated().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int add(E object, int count) {
/* 107 */     validate(object);
/* 108 */     return decorated().add(object, count);
/*     */   }
/*     */ 
/*     */   
/*     */   public int remove(Object object, int count) {
/* 113 */     return decorated().remove(object, count);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCount(Object object) {
/* 118 */     return decorated().getCount(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int setCount(E object, int count) {
/* 123 */     validate(object);
/* 124 */     return decorated().setCount(object, count);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> uniqueSet() {
/* 129 */     return decorated().uniqueSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<MultiSet.Entry<E>> entrySet() {
/* 134 */     return decorated().entrySet();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\multiset\PredicatedMultiSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */