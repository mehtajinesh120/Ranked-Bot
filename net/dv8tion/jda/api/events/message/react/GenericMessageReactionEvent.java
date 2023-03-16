/*     */ package net.dv8tion.jda.api.events.message.react;
/*     */ 
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageReaction;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.events.message.GenericMessageEvent;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GenericMessageReactionEvent
/*     */   extends GenericMessageEvent
/*     */ {
/*     */   protected final long userId;
/*     */   protected User issuer;
/*     */   protected Member member;
/*     */   protected MessageReaction reaction;
/*     */   
/*     */   public GenericMessageReactionEvent(@Nonnull JDA api, long responseNumber, @Nullable User user, @Nullable Member member, @Nonnull MessageReaction reaction, long userId) {
/*  53 */     super(api, responseNumber, reaction.getMessageIdLong(), reaction.getChannel());
/*  54 */     this.userId = userId;
/*  55 */     this.issuer = user;
/*  56 */     this.member = member;
/*  57 */     this.reaction = reaction;
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
/*  68 */     return Long.toUnsignedString(this.userId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getUserIdLong() {
/*  78 */     return this.userId;
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
/*     */   public User getUser() {
/*  91 */     return (this.issuer == null && isFromType(ChannelType.PRIVATE)) ? 
/*  92 */       getPrivateChannel().getUser() : 
/*  93 */       this.issuer;
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
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Member getMember() {
/* 113 */     return this.member;
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
/* 124 */     return this.reaction;
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
/* 136 */     return this.reaction.getReactionEmote();
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
/* 151 */     User user = getUser();
/* 152 */     if (user != null)
/* 153 */       return (RestAction<User>)new CompletedRestAction(getJDA(), user); 
/* 154 */     return getJDA().retrieveUserById(getUserIdLong());
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RestAction<Member> retrieveMember() {
/* 176 */     if (this.member != null)
/* 177 */       return (RestAction<Member>)new CompletedRestAction(getJDA(), this.member); 
/* 178 */     return getGuild().retrieveMemberById(getUserIdLong());
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
/* 196 */     return getChannel().retrieveMessageById(getMessageId());
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\message\react\GenericMessageReactionEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */