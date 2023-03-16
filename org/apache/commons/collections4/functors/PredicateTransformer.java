/*    */ package org.apache.commons.collections4.functors;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.commons.collections4.Predicate;
/*    */ import org.apache.commons.collections4.Transformer;
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
/*    */ public class PredicateTransformer<T>
/*    */   implements Transformer<T, Boolean>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 5278818408044349346L;
/*    */   private final Predicate<? super T> iPredicate;
/*    */   
/*    */   public static <T> Transformer<T, Boolean> predicateTransformer(Predicate<? super T> predicate) {
/* 48 */     if (predicate == null) {
/* 49 */       throw new IllegalArgumentException("Predicate must not be null");
/*    */     }
/* 51 */     return new PredicateTransformer<T>(predicate);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PredicateTransformer(Predicate<? super T> predicate) {
/* 62 */     this.iPredicate = predicate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Boolean transform(T input) {
/* 72 */     return Boolean.valueOf(this.iPredicate.evaluate(input));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Predicate<? super T> getPredicate() {
/* 82 */     return this.iPredicate;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\PredicateTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */