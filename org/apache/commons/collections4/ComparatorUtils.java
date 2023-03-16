/*     */ package org.apache.commons.collections4;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import org.apache.commons.collections4.comparators.BooleanComparator;
/*     */ import org.apache.commons.collections4.comparators.ComparableComparator;
/*     */ import org.apache.commons.collections4.comparators.ComparatorChain;
/*     */ import org.apache.commons.collections4.comparators.NullComparator;
/*     */ import org.apache.commons.collections4.comparators.ReverseComparator;
/*     */ import org.apache.commons.collections4.comparators.TransformingComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ComparatorUtils
/*     */ {
/*  54 */   public static final Comparator NATURAL_COMPARATOR = (Comparator)ComparableComparator.comparableComparator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> Comparator<E> naturalComparator() {
/*  64 */     return NATURAL_COMPARATOR;
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
/*     */   public static <E> Comparator<E> chainedComparator(Comparator<E>... comparators) {
/*  78 */     ComparatorChain<E> chain = new ComparatorChain();
/*  79 */     for (Comparator<E> comparator : comparators) {
/*  80 */       if (comparator == null) {
/*  81 */         throw new NullPointerException("Comparator cannot be null");
/*     */       }
/*  83 */       chain.addComparator(comparator);
/*     */     } 
/*  85 */     return (Comparator<E>)chain;
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
/*     */   public static <E> Comparator<E> chainedComparator(Collection<Comparator<E>> comparators) {
/* 102 */     return chainedComparator(comparators.<Comparator<E>>toArray((Comparator<E>[])new Comparator[comparators.size()]));
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
/*     */   public static <E> Comparator<E> reversedComparator(Comparator<E> comparator) {
/* 116 */     return (Comparator<E>)new ReverseComparator(comparator);
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
/*     */   public static Comparator<Boolean> booleanComparator(boolean trueFirst) {
/* 132 */     return (Comparator<Boolean>)BooleanComparator.booleanComparator(trueFirst);
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
/*     */   public static <E> Comparator<E> nullLowComparator(Comparator<E> comparator) {
/* 149 */     if (comparator == null) {
/* 150 */       comparator = NATURAL_COMPARATOR;
/*     */     }
/* 152 */     return (Comparator<E>)new NullComparator(comparator, false);
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
/*     */   public static <E> Comparator<E> nullHighComparator(Comparator<E> comparator) {
/* 169 */     if (comparator == null) {
/* 170 */       comparator = NATURAL_COMPARATOR;
/*     */     }
/* 172 */     return (Comparator<E>)new NullComparator(comparator, true);
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
/*     */   public static <I, O> Comparator<I> transformedComparator(Comparator<O> comparator, Transformer<? super I, ? extends O> transformer) {
/* 193 */     if (comparator == null) {
/* 194 */       comparator = NATURAL_COMPARATOR;
/*     */     }
/* 196 */     return (Comparator<I>)new TransformingComparator(transformer, comparator);
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
/*     */   public static <E> E min(E o1, E o2, Comparator<E> comparator) {
/* 212 */     if (comparator == null) {
/* 213 */       comparator = NATURAL_COMPARATOR;
/*     */     }
/* 215 */     int c = comparator.compare(o1, o2);
/* 216 */     return (c < 0) ? o1 : o2;
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
/*     */   public static <E> E max(E o1, E o2, Comparator<E> comparator) {
/* 232 */     if (comparator == null) {
/* 233 */       comparator = NATURAL_COMPARATOR;
/*     */     }
/* 235 */     int c = comparator.compare(o1, o2);
/* 236 */     return (c > 0) ? o1 : o2;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\ComparatorUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */