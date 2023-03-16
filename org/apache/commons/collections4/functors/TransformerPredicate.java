/*    */ package org.apache.commons.collections4.functors;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.commons.collections4.FunctorException;
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
/*    */ public final class TransformerPredicate<T>
/*    */   implements Predicate<T>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -2407966402920578741L;
/*    */   private final Transformer<? super T, Boolean> iTransformer;
/*    */   
/*    */   public static <T> Predicate<T> transformerPredicate(Transformer<? super T, Boolean> transformer) {
/* 48 */     if (transformer == null) {
/* 49 */       throw new NullPointerException("The transformer to call must not be null");
/*    */     }
/* 51 */     return new TransformerPredicate<T>(transformer);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TransformerPredicate(Transformer<? super T, Boolean> transformer) {
/* 62 */     this.iTransformer = transformer;
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
/* 73 */     Boolean result = (Boolean)this.iTransformer.transform(object);
/* 74 */     if (result == null) {
/* 75 */       throw new FunctorException("Transformer must return an instanceof Boolean, it was a null object");
/*    */     }
/*    */     
/* 78 */     return result.booleanValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Transformer<? super T, Boolean> getTransformer() {
/* 88 */     return this.iTransformer;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\TransformerPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */