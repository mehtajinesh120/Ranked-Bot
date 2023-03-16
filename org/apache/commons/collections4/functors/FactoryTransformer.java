/*    */ package org.apache.commons.collections4.functors;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.commons.collections4.Factory;
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
/*    */ public class FactoryTransformer<I, O>
/*    */   implements Transformer<I, O>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -6817674502475353160L;
/*    */   private final Factory<? extends O> iFactory;
/*    */   
/*    */   public static <I, O> Transformer<I, O> factoryTransformer(Factory<? extends O> factory) {
/* 48 */     if (factory == null) {
/* 49 */       throw new NullPointerException("Factory must not be null");
/*    */     }
/* 51 */     return new FactoryTransformer<I, O>(factory);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FactoryTransformer(Factory<? extends O> factory) {
/* 62 */     this.iFactory = factory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public O transform(I input) {
/* 73 */     return (O)this.iFactory.create();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Factory<? extends O> getFactory() {
/* 83 */     return this.iFactory;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\FactoryTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */