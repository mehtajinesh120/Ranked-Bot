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
/*    */ public abstract class AbstractQuantifierPredicate<T>
/*    */   implements PredicateDecorator<T>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -3094696765038308799L;
/*    */   protected final Predicate<? super T>[] iPredicates;
/*    */   
/*    */   public AbstractQuantifierPredicate(Predicate<? super T>... predicates) {
/* 43 */     this.iPredicates = predicates;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Predicate<? super T>[] getPredicates() {
/* 53 */     return (Predicate<? super T>[])FunctorUtils.copy(this.iPredicates);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\AbstractQuantifierPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */