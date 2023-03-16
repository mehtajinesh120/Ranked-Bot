/*     */ package org.apache.commons.collections4.functors;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ComparatorPredicate<T>
/*     */   implements Predicate<T>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -1863209236504077399L;
/*     */   private final T object;
/*     */   private final Comparator<T> comparator;
/*     */   private final Criterion criterion;
/*     */   
/*     */   public enum Criterion
/*     */   {
/*  84 */     EQUAL, GREATER, LESS, GREATER_OR_EQUAL, LESS_OR_EQUAL;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> comparatorPredicate(T object, Comparator<T> comparator) {
/* 108 */     return comparatorPredicate(object, comparator, Criterion.EQUAL);
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
/*     */   public static <T> Predicate<T> comparatorPredicate(T object, Comparator<T> comparator, Criterion criterion) {
/* 123 */     if (comparator == null) {
/* 124 */       throw new NullPointerException("Comparator must not be null.");
/*     */     }
/* 126 */     if (criterion == null) {
/* 127 */       throw new NullPointerException("Criterion must not be null.");
/*     */     }
/* 129 */     return new ComparatorPredicate<T>(object, comparator, criterion);
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
/*     */   public ComparatorPredicate(T object, Comparator<T> comparator, Criterion criterion) {
/* 142 */     this.object = object;
/* 143 */     this.comparator = comparator;
/* 144 */     this.criterion = criterion;
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
/*     */ 
/*     */   
/*     */   public boolean evaluate(T target) {
/* 167 */     boolean result = false;
/* 168 */     int comparison = this.comparator.compare(this.object, target);
/* 169 */     switch (this.criterion) {
/*     */       case EQUAL:
/* 171 */         result = (comparison == 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 189 */         return result;case GREATER: result = (comparison > 0); return result;case LESS: result = (comparison < 0); return result;case GREATER_OR_EQUAL: result = (comparison >= 0); return result;case LESS_OR_EQUAL: result = (comparison <= 0); return result;
/*     */     } 
/*     */     throw new IllegalStateException("The current criterion '" + this.criterion + "' is invalid.");
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\ComparatorPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */