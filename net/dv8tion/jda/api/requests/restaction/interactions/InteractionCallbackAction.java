/*    */ package net.dv8tion.jda.api.requests.restaction.interactions;
/*    */ 
/*    */ import net.dv8tion.jda.api.interactions.InteractionHook;
/*    */ import net.dv8tion.jda.api.requests.RestAction;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface InteractionCallbackAction
/*    */   extends RestAction<InteractionHook>
/*    */ {
/*    */   public enum ResponseType
/*    */   {
/* 34 */     CHANNEL_MESSAGE_WITH_SOURCE(4),
/*    */     
/* 36 */     DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE(5),
/*    */     
/* 38 */     DEFERRED_MESSAGE_UPDATE(6),
/*    */     
/* 40 */     MESSAGE_UPDATE(7);
/*    */     
/*    */     private final int raw;
/*    */ 
/*    */     
/*    */     ResponseType(int raw) {
/* 46 */       this.raw = raw;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public int getRaw() {
/* 56 */       return this.raw;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\interactions\InteractionCallbackAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */