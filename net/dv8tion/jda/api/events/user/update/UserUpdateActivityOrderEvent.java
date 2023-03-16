/*    */ package net.dv8tion.jda.api.events.user.update;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Activity;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
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
/*    */ 
/*    */ 
/*    */ public class UserUpdateActivityOrderEvent
/*    */   extends GenericUserUpdateEvent<List<Activity>>
/*    */   implements GenericUserPresenceEvent
/*    */ {
/*    */   public static final String IDENTIFIER = "activity_order";
/*    */   private final Member member;
/*    */   
/*    */   public UserUpdateActivityOrderEvent(@Nonnull JDAImpl api, long responseNumber, @Nonnull List<Activity> previous, @Nonnull Member member) {
/* 53 */     super((JDA)api, responseNumber, member.getUser(), previous, member.getActivities(), "activity_order");
/* 54 */     this.member = member;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Guild getGuild() {
/* 61 */     return this.member.getGuild();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Member getMember() {
/* 68 */     return this.member;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public List<Activity> getOldValue() {
/* 75 */     return super.getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public List<Activity> getNewValue() {
/* 82 */     return super.getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\event\\use\\update\UserUpdateActivityOrderEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */