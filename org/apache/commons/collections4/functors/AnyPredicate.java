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
/*     */ public final class AnyPredicate<T>
/*     */   extends AbstractQuantifierPredicate<T>
/*     */ {
/*     */   private static final long serialVersionUID = 7429999530934647542L;
/*     */   
/*     */   public static <T> Predicate<T> anyPredicate(Predicate<? super T>... predicates) {
/*  53 */     FunctorUtils.validate((Predicate<?>[])predicates);
/*  54 */     if (predicates.length == 0) {
/*  55 */       return FalsePredicate.falsePredicate();
/*     */     }
/*  57 */     if (predicates.length == 1) {
/*  58 */       return (Predicate)predicates[0];
/*     */     }
/*  60 */     return new AnyPredicate<T>((Predicate<? super T>[])FunctorUtils.copy(predicates));
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
/*     */   public static <T> Predicate<T> anyPredicate(Collection<? extends Predicate<? super T>> predicates) {
/*  77 */     Predicate[] arrayOfPredicate = (Predicate[])FunctorUtils.validate(predicates);
/*  78 */     if (arrayOfPredicate.length == 0) {
/*  79 */       return FalsePredicate.falsePredicate();
/*     */     }
/*  81 */     if (arrayOfPredicate.length == 1) {
/*  82 */       return arrayOfPredicate[0];
/*     */     }
/*  84 */     return new AnyPredicate<T>((Predicate<? super T>[])arrayOfPredicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnyPredicate(Predicate<? super T>... predicates) {
/*  94 */     super(predicates);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean evaluate(T object) {
/* 104 */     for (Predicate<? super T> iPredicate : this.iPredicates) {
/* 105 */       if (iPredicate.evaluate(object)) {
/* 106 */         return true;
/*     */       }
/*     */     } 
/* 109 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\AnyPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */