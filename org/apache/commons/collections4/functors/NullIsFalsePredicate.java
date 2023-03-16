/*    */ package org.apache.commons.collections4.functors;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public final class NullIsFalsePredicate<T>
/*    */   implements PredicateDecorator<T>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -2997501534564735525L;
/*    */   private final Predicate<? super T> iPredicate;
/*    */   
/*    */   public static <T> Predicate<T> nullIsFalsePredicate(Predicate<? super T> predicate) {
/* 46 */     if (predicate == null) {
/* 47 */       throw new NullPointerException("Predicate must not be null");
/*    */     }
/* 49 */     return new NullIsFalsePredicate<T>(predicate);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NullIsFalsePredicate(Predicate<? super T> predicate) {
/* 60 */     this.iPredicate = predicate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean evaluate(T object) {
/* 71 */     if (object == null) {
/* 72 */       return false;
/*    */     }
/* 74 */     return this.iPredicate.evaluate(object);
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
/* 85 */     return (Predicate<? super T>[])new Predicate[] { this.iPredicate };
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\NullIsFalsePredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */