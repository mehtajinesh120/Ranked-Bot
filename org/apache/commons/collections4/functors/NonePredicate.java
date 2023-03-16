/*    */ package org.apache.commons.collections4.functors;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import org.apache.commons.collections4.Predicate;
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
/*    */ 
/*    */ 
/*    */ public final class NonePredicate<T>
/*    */   extends AbstractQuantifierPredicate<T>
/*    */ {
/*    */   private static final long serialVersionUID = 2007613066565892961L;
/*    */   
/*    */   public static <T> Predicate<T> nonePredicate(Predicate<? super T>... predicates) {
/* 51 */     FunctorUtils.validate((Predicate<?>[])predicates);
/* 52 */     if (predicates.length == 0) {
/* 53 */       return TruePredicate.truePredicate();
/*    */     }
/* 55 */     return new NonePredicate<T>((Predicate<? super T>[])FunctorUtils.copy(predicates));
/*    */   }
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
/*    */   public static <T> Predicate<T> nonePredicate(Collection<? extends Predicate<? super T>> predicates) {
/* 70 */     Predicate[] arrayOfPredicate = (Predicate[])FunctorUtils.validate(predicates);
/* 71 */     if (arrayOfPredicate.length == 0) {
/* 72 */       return TruePredicate.truePredicate();
/*    */     }
/* 74 */     return new NonePredicate<T>((Predicate<? super T>[])arrayOfPredicate);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NonePredicate(Predicate<? super T>... predicates) {
/* 84 */     super(predicates);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean evaluate(T object) {
/* 94 */     for (Predicate<? super T> iPredicate : this.iPredicates) {
/* 95 */       if (iPredicate.evaluate(object)) {
/* 96 */         return false;
/*    */       }
/*    */     } 
/* 99 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\NonePredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */