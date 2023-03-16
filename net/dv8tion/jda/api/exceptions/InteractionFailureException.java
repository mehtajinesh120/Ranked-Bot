/*    */ package net.dv8tion.jda.api.exceptions;
/*    */ 
/*    */ import java.util.concurrent.CancellationException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InteractionFailureException
/*    */   extends CancellationException
/*    */ {
/*    */   public InteractionFailureException() {
/* 33 */     super("Cascading failure caused by interaction callback failure");
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\exceptions\InteractionFailureException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */