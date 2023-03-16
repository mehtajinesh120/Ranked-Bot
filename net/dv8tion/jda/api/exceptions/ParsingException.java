/*    */ package net.dv8tion.jda.api.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParsingException
/*    */   extends IllegalStateException
/*    */ {
/*    */   public ParsingException(String message, Exception cause) {
/* 23 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public ParsingException(String message) {
/* 28 */     super(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public ParsingException(Exception cause) {
/* 33 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\exceptions\ParsingException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */