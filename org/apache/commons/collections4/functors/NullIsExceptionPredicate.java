/*    */ package org.apache.commons.collections4.functors;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.commons.collections4.FunctorException;
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
/*    */ public final class NullIsExceptionPredicate<T>
/*    */   implements PredicateDecorator<T>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 3243449850504576071L;
/*    */   private final Predicate<? super T> iPredicate;
/*    */   
/*    */   public static <T> Predicate<T> nullIsExceptionPredicate(Predicate<? super T> predicate) {
/* 47 */     if (predicate == null) {
/* 48 */       throw new NullPointerException("Predicate must not be null");
/*    */     }
/* 50 */     return new NullIsExceptionPredicate<T>(predicate);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NullIsExceptionPredicate(Predicate<? super T> predicate) {
/* 61 */     this.iPredicate = predicate;
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
/*    */   public boolean evaluate(T object) {
/* 73 */     if (object == null) {
/* 74 */       throw new FunctorException("Input Object must not be null");
/*    */     }
/* 76 */     return this.iPredicate.evaluate(object);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Predicate<? super T>[] getPredicates() {
/* 87 */     return (Predicate<? super T>[])new Predicate[] { this.iPredicate };
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\NullIsExceptionPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */