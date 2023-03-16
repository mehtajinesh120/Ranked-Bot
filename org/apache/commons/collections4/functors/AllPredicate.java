/*     */ package org.apache.commons.collections4.functors;
/*     */ 
/*     */ import java.util.Collection;
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
/*     */ public final class AllPredicate<T>
/*     */   extends AbstractQuantifierPredicate<T>
/*     */ {
/*     */   private static final long serialVersionUID = -3094696765038308799L;
/*     */   
/*     */   public static <T> Predicate<T> allPredicate(Predicate<? super T>... predicates) {
/*  56 */     FunctorUtils.validate((Predicate<?>[])predicates);
/*  57 */     if (predicates.length == 0) {
/*  58 */       return TruePredicate.truePredicate();
/*     */     }
/*  60 */     if (predicates.length == 1) {
/*  61 */       return FunctorUtils.coerce(predicates[0]);
/*     */     }
/*     */     
/*  64 */     return new AllPredicate<T>((Predicate<? super T>[])FunctorUtils.copy(predicates));
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
/*     */   public static <T> Predicate<T> allPredicate(Collection<? extends Predicate<? super T>> predicates) {
/*  80 */     Predicate[] arrayOfPredicate = (Predicate[])FunctorUtils.validate(predicates);
/*  81 */     if (arrayOfPredicate.length == 0) {
/*  82 */       return TruePredicate.truePredicate();
/*     */     }
/*  84 */     if (arrayOfPredicate.length == 1) {
/*  85 */       return FunctorUtils.coerce(arrayOfPredicate[0]);
/*     */     }
/*  87 */     return new AllPredicate<T>((Predicate<? super T>[])arrayOfPredicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AllPredicate(Predicate<? super T>... predicates) {
/*  97 */     super(predicates);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean evaluate(T object) {
/* 107 */     for (Predicate<? super T> iPredicate : this.iPredicates) {
/* 108 */       if (!iPredicate.evaluate(object)) {
/* 109 */         return false;
/*     */       }
/*     */     } 
/* 112 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\AllPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */