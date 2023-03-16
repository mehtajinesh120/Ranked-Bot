/*    */ package net.dv8tion.jda.api.events.message.react;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.MessageChannel;
/*    */ import net.dv8tion.jda.api.events.message.GenericMessageEvent;
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
/*    */ public class MessageReactionRemoveAllEvent
/*    */   extends GenericMessageEvent
/*    */ {
/*    */   public MessageReactionRemoveAllEvent(@Nonnull JDA api, long responseNumber, long messageId, @Nonnull MessageChannel channel) {
/* 42 */     super(api, responseNumber, messageId, channel);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\message\react\MessageReactionRemoveAllEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */