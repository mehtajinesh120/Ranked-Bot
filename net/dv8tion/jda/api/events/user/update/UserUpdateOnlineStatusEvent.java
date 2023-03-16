/*     */ package net.dv8tion.jda.api.events.user.update;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.OnlineStatus;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Member;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UserUpdateOnlineStatusEvent
/*     */   extends GenericUserUpdateEvent<OnlineStatus>
/*     */   implements GenericUserPresenceEvent
/*     */ {
/*     */   public static final String IDENTIFIER = "status";
/*     */   private final Guild guild;
/*     */   private final Member member;
/*     */   
/*     */   public UserUpdateOnlineStatusEvent(@Nonnull JDA api, long responseNumber, @Nonnull Member member, @Nonnull OnlineStatus oldOnlineStatus) {
/*  53 */     super(api, responseNumber, member.getUser(), oldOnlineStatus, member.getOnlineStatus(), "status");
/*  54 */     this.guild = member.getGuild();
/*  55 */     this.member = member;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild getGuild() {
/*  62 */     return this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Member getMember() {
/*  69 */     return this.member;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public OnlineStatus getOldOnlineStatus() {
/*  80 */     return getOldValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public OnlineStatus getNewOnlineStatus() {
/*  91 */     return getNewValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public OnlineStatus getOldValue() {
/*  98 */     return super.getOldValue();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public OnlineStatus getNewValue() {
/* 104 */     return super.getNewValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\event\\use\\update\UserUpdateOnlineStatusEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */