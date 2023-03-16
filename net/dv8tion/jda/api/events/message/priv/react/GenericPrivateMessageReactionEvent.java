/*     */ package net.dv8tion.jda.api.events.message.priv.react;
/*     */ 
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageReaction;
/*     */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.events.message.priv.GenericPrivateMessageEvent;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.internal.requests.CompletedRestAction;
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
/*     */ public class GenericPrivateMessageReactionEvent
/*     */   extends GenericPrivateMessageEvent
/*     */ {
/*     */   protected final long userId;
/*     */   protected final MessageReaction reaction;
/*     */   
/*     */   public GenericPrivateMessageReactionEvent(@Nonnull JDA api, long responseNumber, @Nonnull MessageReaction reaction, long userId) {
/*  47 */     super(api, responseNumber, reaction.getMessageIdLong(), (PrivateChannel)reaction.getChannel());
/*  48 */     this.userId = userId;
/*  49 */     this.reaction = reaction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getUserId() {
/*  60 */     return Long.toUnsignedString(this.userId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getUserIdLong() {
/*  70 */     return this.userId;
/*     */   }
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
/*     */   @Nullable
/*     */   public User getUser() {
/*  84 */     return (this.userId == getJDA().getSelfUser().getIdLong()) ? 
/*  85 */       (User)getJDA().getSelfUser() : 
/*  86 */       getChannel().getUser();
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
/*  97 */     return this.reaction;
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
/* 109 */     return this.reaction.getReactionEmote();
/*     */   }
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RestAction<User> retrieveUser() {
/* 124 */     User user = getUser();
/* 125 */     if (user != null)
/* 126 */       return (RestAction<User>)new CompletedRestAction(getJDA(), user); 
/* 127 */     return getJDA().retrieveUserById(getUserIdLong());
/*     */   }
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RestAction<Message> retrieveMessage() {
/* 144 */     return getChannel().retrieveMessageById(getMessageId());
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\message\priv\react\GenericPrivateMessageReactionEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */