/*    */ package net.dv8tion.jda.api.events.message.priv;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*    */ import net.dv8tion.jda.api.entities.PrivateChannel;
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
/*    */ public class PrivateMessageEmbedEvent
/*    */   extends GenericPrivateMessageEvent
/*    */ {
/*    */   private final List<MessageEmbed> embeds;
/*    */   
/*    */   public PrivateMessageEmbedEvent(@Nonnull JDA api, long responseNumber, long messageId, @Nonnull PrivateChannel channel, @Nonnull List<MessageEmbed> embeds) {
/* 40 */     super(api, responseNumber, messageId, channel);
/* 41 */     this.embeds = embeds;
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
/* 52 */     return this.embeds;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\message\priv\PrivateMessageEmbedEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */