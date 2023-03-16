/*    */ package org.apache.commons.collections4.functors;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.commons.collections4.Factory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConstantFactory<T>
/*    */   implements Factory<T>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -3520677225766901240L;
/* 40 */   public static final Factory NULL_INSTANCE = new ConstantFactory(null);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final T iConstant;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Factory<T> constantFactory(T constantToReturn) {
/* 54 */     if (constantToReturn == null) {
/* 55 */       return NULL_INSTANCE;
/*    */     }
/* 57 */     return new ConstantFactory<T>(constantToReturn);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ConstantFactory(T constantToReturn) {
/* 68 */     this.iConstant = constantToReturn;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public T create() {
/* 77 */     return this.iConstant;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public T getConstant() {
/* 87 */     return this.iConstant;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\ConstantFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */