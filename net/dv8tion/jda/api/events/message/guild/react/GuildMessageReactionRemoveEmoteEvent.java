/*     */ package net.dv8tion.jda.api.events.message.guild.react;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.MessageReaction;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuildMessageReactionRemoveEmoteEvent
/*     */   extends GenericGuildMessageEvent
/*     */ {
/*     */   private final MessageReaction reaction;
/*     */   
/*     */   public GuildMessageReactionRemoveEmoteEvent(@Nonnull JDA api, long responseNumber, @Nonnull TextChannel channel, @Nonnull MessageReaction reaction, long messageId) {
/*  43 */     super(api, responseNumber, messageId, channel);
/*     */     
/*  45 */     this.reaction = reaction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public TextChannel getChannel() {
/*  56 */     return this.channel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageReaction getReaction() {
/*  67 */     return this.reaction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageReaction.ReactionEmote getReactionEmote() {
/*  79 */     return this.reaction.getReactionEmote();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMessageIdLong() {
/*  89 */     return this.messageId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getMessageId() {
/* 100 */     return Long.toUnsignedString(this.messageId);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\message\guild\react\GuildMessageReactionRemoveEmoteEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */