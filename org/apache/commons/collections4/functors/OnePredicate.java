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
/*     */ public final class OnePredicate<T>
/*     */   extends AbstractQuantifierPredicate<T>
/*     */ {
/*     */   private static final long serialVersionUID = -8125389089924745785L;
/*     */   
/*     */   public static <T> Predicate<T> onePredicate(Predicate<? super T>... predicates) {
/*  53 */     FunctorUtils.validate((Predicate<?>[])predicates);
/*  54 */     if (predicates.length == 0) {
/*  55 */       return FalsePredicate.falsePredicate();
/*     */     }
/*  57 */     if (predicates.length == 1) {
/*  58 */       return (Predicate)predicates[0];
/*     */     }
/*  60 */     return new OnePredicate<T>((Predicate<? super T>[])FunctorUtils.copy(predicates));
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
/*     */   public static <T> Predicate<T> onePredicate(Collection<? extends Predicate<? super T>> predicates) {
/*  73 */     Predicate[] arrayOfPredicate = (Predicate[])FunctorUtils.validate(predicates);
/*  74 */     return new OnePredicate<T>((Predicate<? super T>[])arrayOfPredicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OnePredicate(Predicate<? super T>... predicates) {
/*  84 */     super(predicates);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean evaluate(T object) {
/*  95 */     boolean match = false;
/*  96 */     for (Predicate<? super T> iPredicate : this.iPredicates) {
/*  97 */       if (iPredicate.evaluate(object)) {
/*  98 */         if (match) {
/*  99 */           return false;
/*     */         }
/* 101 */         match = true;
/*     */       } 
/*     */     } 
/* 104 */     return match;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\OnePredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */