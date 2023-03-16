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
/*    */ public class ClosureTransformer<T>
/*    */   implements Transformer<T, T>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 478466901448617286L;
/*    */   private final Closure<? super T> iClosure;
/*    */   
/*    */   public static <T> Transformer<T, T> closureTransformer(Closure<? super T> closure) {
/* 48 */     if (closure == null) {
/* 49 */       throw new NullPointerException("Closure must not be null");
/*    */     }
/* 51 */     return new ClosureTransformer<T>(closure);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClosureTransformer(Closure<? super T> closure) {
/* 62 */     this.iClosure = closure;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public T transform(T input) {
/* 72 */     this.iClosure.execute(input);
/* 73 */     return input;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Closure<? super T> getClosure() {
/* 83 */     return this.iClosure;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\ClosureTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */