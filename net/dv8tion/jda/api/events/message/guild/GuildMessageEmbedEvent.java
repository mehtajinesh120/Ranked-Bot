/*    */ package net.dv8tion.jda.api.events.message.guild;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
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
/*    */ public class GuildMessageEmbedEvent
/*    */   extends GenericGuildMessageEvent
/*    */ {
/*    */   private final List<MessageEmbed> embeds;
/*    */   
/*    */   public GuildMessageEmbedEvent(@Nonnull JDA api, long responseNumber, long messageId, @Nonnull TextChannel channel, @Nonnull List<MessageEmbed> embeds) {
/* 41 */     super(api, responseNumber, messageId, channel);
/* 42 */     this.embeds = embeds;
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
/* 53 */     return this.embeds;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\message\guild\GuildMessageEmbedEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */