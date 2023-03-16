/*    */ package net.dv8tion.jda.api.events.user.update;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Activity;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UserUpdateActivitiesEvent
/*    */   extends GenericUserUpdateEvent<List<Activity>>
/*    */   implements GenericUserPresenceEvent
/*    */ {
/*    */   public static final String IDENTIFIER = "activities";
/*    */   private final Member member;
/*    */   
/*    */   public UserUpdateActivitiesEvent(@Nonnull JDA api, long responseNumber, @Nonnull Member member, @Nullable List<Activity> previous) {
/* 60 */     super(api, responseNumber, member.getUser(), previous, member.getActivities(), "activities");
/* 61 */     this.member = member;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Guild getGuild() {
/* 68 */     return this.member.getGuild();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Member getMember() {
/* 75 */     return this.member;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\event\\use\\update\UserUpdateActivitiesEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */