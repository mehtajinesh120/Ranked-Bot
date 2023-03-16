/*    */ package net.dv8tion.jda.api.events.message.react;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.MessageChannel;
/*    */ import net.dv8tion.jda.api.entities.MessageReaction;
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
/*    */ 
/*    */ 
/*    */ public class MessageReactionRemoveEmoteEvent
/*    */   extends GenericMessageEvent
/*    */ {
/*    */   private final MessageReaction reaction;
/*    */   
/*    */   public MessageReactionRemoveEmoteEvent(@Nonnull JDA api, long responseNumber, long messageId, @Nonnull MessageChannel channel, @Nonnull MessageReaction reaction) {
/* 47 */     super(api, responseNumber, messageId, channel);
/* 48 */     this.reaction = reaction;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public MessageReaction getReaction() {
/* 59 */     return this.reaction;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public MessageReaction.ReactionEmote getReactionEmote() {
/* 71 */     return this.reaction.getReactionEmote();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\message\react\MessageReactionRemoveEmoteEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */