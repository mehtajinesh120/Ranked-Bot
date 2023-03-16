/*    */ package org.apache.commons.collections4.functors;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.commons.collections4.Closure;
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
/*    */ 
/*    */ public class TransformerClosure<E>
/*    */   implements Closure<E>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -5194992589193388969L;
/*    */   private final Transformer<? super E, ?> iTransformer;
/*    */   
/*    */   public static <E> Closure<E> transformerClosure(Transformer<? super E, ?> transformer) {
/* 49 */     if (transformer == null) {
/* 50 */       return NOPClosure.nopClosure();
/*    */     }
/* 52 */     return new TransformerClosure<E>(transformer);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TransformerClosure(Transformer<? super E, ?> transformer) {
/* 63 */     this.iTransformer = transformer;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(E input) {
/* 72 */     this.iTransformer.transform(input);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Transformer<? super E, ?> getTransformer() {
/* 82 */     return this.iTransformer;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\TransformerClosure.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */