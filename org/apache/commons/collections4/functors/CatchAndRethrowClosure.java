/*    */ package org.apache.commons.collections4.functors;
/*    */ 
/*    */ import org.apache.commons.collections4.Closure;
/*    */ import org.apache.commons.collections4.FunctorException;
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
/*    */ public abstract class CatchAndRethrowClosure<E>
/*    */   implements Closure<E>
/*    */ {
/*    */   public void execute(E input) {
/*    */     try {
/* 60 */       executeAndThrow(input);
/* 61 */     } catch (RuntimeException ex) {
/* 62 */       throw ex;
/* 63 */     } catch (Throwable t) {
/* 64 */       throw new FunctorException(t);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected abstract void executeAndThrow(E paramE) throws Throwable;
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\CatchAndRethrowClosure.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */