/*    */ package net.dv8tion.jda.api.events.message.priv.react;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.MessageReaction;
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
/*    */ public class PrivateMessageReactionAddEvent
/*    */   extends GenericPrivateMessageReactionEvent
/*    */ {
/*    */   public PrivateMessageReactionAddEvent(@Nonnull JDA api, long responseNumber, @Nonnull MessageReaction reaction, long userId) {
/* 37 */     super(api, responseNumber, reaction, userId);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\message\priv\react\PrivateMessageReactionAddEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */