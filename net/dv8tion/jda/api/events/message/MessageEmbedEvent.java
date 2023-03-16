/*    */ package net.dv8tion.jda.api.events.message;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.MessageChannel;
/*    */ import net.dv8tion.jda.api.entities.MessageEmbed;
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
/*    */ public class MessageEmbedEvent
/*    */   extends GenericMessageEvent
/*    */ {
/*    */   private final List<MessageEmbed> embeds;
/*    */   
/*    */   public MessageEmbedEvent(@Nonnull JDA api, long responseNumber, long messageId, @Nonnull MessageChannel channel, @Nonnull List<MessageEmbed> embeds) {
/* 46 */     super(api, responseNumber, messageId, channel);
/* 47 */     this.embeds = Collections.unmodifiableList(embeds);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public List<MessageEmbed> getMessageEmbeds() {
/* 58 */     return this.embeds;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\message\MessageEmbedEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */