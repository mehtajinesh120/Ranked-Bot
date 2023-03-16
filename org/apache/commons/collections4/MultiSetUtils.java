/*     */ package org.apache.commons.collections4;
/*     */ 
/*     */ import org.apache.commons.collections4.multiset.HashMultiSet;
/*     */ import org.apache.commons.collections4.multiset.PredicatedMultiSet;
/*     */ import org.apache.commons.collections4.multiset.SynchronizedMultiSet;
/*     */ import org.apache.commons.collections4.multiset.UnmodifiableMultiSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultiSetUtils
/*     */ {
/*  36 */   public static final MultiSet EMPTY_MULTISET = UnmodifiableMultiSet.unmodifiableMultiSet((MultiSet)new HashMultiSet());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> MultiSet<E> synchronizedMultiSet(MultiSet<E> multiset) {
/*  72 */     return (MultiSet<E>)SynchronizedMultiSet.synchronizedMultiSet(multiset);
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
/*     */   public static <E> MultiSet<E> unmodifiableMultiSet(MultiSet<? extends E> multiset) {
/*  85 */     return UnmodifiableMultiSet.unmodifiableMultiSet(multiset);
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
/*     */   public static <E> MultiSet<E> predicatedMultiSet(MultiSet<E> multiset, Predicate<? super E> predicate) {
/* 105 */     return (MultiSet<E>)PredicatedMultiSet.predicatedMultiSet(multiset, predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> MultiSet<E> emptyMultiSet() {
/* 116 */     return EMPTY_MULTISET;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\MultiSetUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */