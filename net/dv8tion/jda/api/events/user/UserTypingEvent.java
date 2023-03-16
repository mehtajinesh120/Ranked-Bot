/*     */ package net.dv8tion.jda.api.events.user;
/*     */ 
/*     */ import java.time.OffsetDateTime;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.MessageChannel;
/*     */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.User;
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
/*     */ public class UserTypingEvent
/*     */   extends GenericUserEvent
/*     */ {
/*     */   private final Member member;
/*     */   private final MessageChannel channel;
/*     */   private final OffsetDateTime timestamp;
/*     */   
/*     */   public UserTypingEvent(@Nonnull JDA api, long responseNumber, @Nonnull User user, @Nonnull MessageChannel channel, @Nonnull OffsetDateTime timestamp, @Nullable Member member) {
/*  45 */     super(api, responseNumber, user);
/*  46 */     this.member = member;
/*  47 */     this.channel = channel;
/*  48 */     this.timestamp = timestamp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public OffsetDateTime getTimestamp() {
/*  59 */     return this.timestamp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageChannel getChannel() {
/*  70 */     return this.channel;
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
/*     */   public boolean isFromType(@Nonnull ChannelType type) {
/*  83 */     return (this.channel.getType() == type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelType getType() {
/*  94 */     return this.channel.getType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PrivateChannel getPrivateChannel() {
/* 106 */     return isFromType(ChannelType.PRIVATE) ? (PrivateChannel)this.channel : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public TextChannel getTextChannel() {
/* 118 */     return isFromType(ChannelType.TEXT) ? (TextChannel)this.channel : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Guild getGuild() {
/* 130 */     return isFromType(ChannelType.TEXT) ? this.member.getGuild() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Member getMember() {
/* 141 */     return this.member;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\event\\user\UserTypingEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */