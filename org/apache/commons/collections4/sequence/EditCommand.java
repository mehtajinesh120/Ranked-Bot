/*    */ package org.apache.commons.collections4.sequence;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class EditCommand<T>
/*    */ {
/*    */   private final T object;
/*    */   
/*    */   protected EditCommand(T object) {
/* 60 */     this.object = object;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected T getObject() {
/* 69 */     return this.object;
/*    */   }
/*    */   
/*    */   public abstract void accept(CommandVisitor<T> paramCommandVisitor);
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\sequence\EditCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */