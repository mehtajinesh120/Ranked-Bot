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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class AndPredicate<T>
/*    */   implements PredicateDecorator<T>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 4189014213763186912L;
/*    */   private final Predicate<? super T> iPredicate1;
/*    */   private final Predicate<? super T> iPredicate2;
/*    */   
/*    */   public static <T> Predicate<T> andPredicate(Predicate<? super T> predicate1, Predicate<? super T> predicate2) {
/* 50 */     if (predicate1 == null || predicate2 == null) {
/* 51 */       throw new NullPointerException("Predicate must not be null");
/*    */     }
/* 53 */     return new AndPredicate<T>(predicate1, predicate2);
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
/*    */   public AndPredicate(Predicate<? super T> predicate1, Predicate<? super T> predicate2) {
/* 65 */     this.iPredicate1 = predicate1;
/* 66 */     this.iPredicate2 = predicate2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean evaluate(T object) {
/* 76 */     return (this.iPredicate1.evaluate(object) && this.iPredicate2.evaluate(object));
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
/* 87 */     return (Predicate<? super T>[])new Predicate[] { this.iPredicate1, this.iPredicate2 };
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\AndPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */