/*     */ package net.dv8tion.jda.api.events.message.guild.react;
/*     */ 
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageReaction;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
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
/*     */ public abstract class GenericGuildMessageReactionEvent
/*     */   extends GenericGuildMessageEvent
/*     */ {
/*     */   protected final long userId;
/*     */   protected final Member issuer;
/*     */   protected final MessageReaction reaction;
/*     */   
/*     */   public GenericGuildMessageReactionEvent(@Nonnull JDA api, long responseNumber, @Nullable Member user, @Nonnull MessageReaction reaction, long userId) {
/*  46 */     super(api, responseNumber, reaction.getMessageIdLong(), (TextChannel)reaction.getChannel());
/*  47 */     this.issuer = user;
/*  48 */     this.reaction = reaction;
/*  49 */     this.userId = userId;
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
/*     */   
/*     */   @Nullable
/*     */   public User getUser() {
/*  85 */     return (this.issuer == null) ? getJDA().getUserById(this.userId) : this.issuer.getUser();
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
/*     */   @Nullable
/*     */   public Member getMember() {
/*  98 */     return this.issuer;
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
/* 109 */     return this.reaction;
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
/* 121 */     return this.reaction.getReactionEmote();
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
/* 136 */     if (this.issuer != null)
/* 137 */       return (RestAction<User>)new CompletedRestAction(getJDA(), this.issuer.getUser()); 
/* 138 */     return getJDA().retrieveUserById(getUserIdLong());
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
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RestAction<Member> retrieveMember() {
/* 157 */     if (this.issuer != null)
/* 158 */       return (RestAction<Member>)new CompletedRestAction(getJDA(), this.issuer); 
/* 159 */     return getGuild().retrieveMemberById(getUserIdLong());
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
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RestAction<Message> retrieveMessage() {
/* 177 */     return getChannel().retrieveMessageById(getMessageId());
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\message\guild\react\GenericGuildMessageReactionEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */