/*    */ package net.dv8tion.jda.api.events.guild.member.update;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.annotations.Incubating;
/*    */ import net.dv8tion.jda.api.JDA;
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
/*    */ @Incubating
/*    */ public class GuildMemberUpdatePendingEvent
/*    */   extends GenericGuildMemberUpdateEvent<Boolean>
/*    */ {
/*    */   public static final String IDENTIFIER = "pending";
/*    */   
/*    */   public GuildMemberUpdatePendingEvent(@Nonnull JDA api, long responseNumber, @Nonnull Member member, boolean previous) {
/* 54 */     super(api, responseNumber, member, Boolean.valueOf(previous), Boolean.valueOf(member.isPending()), "pending");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean getOldPending() {
/* 64 */     return getOldValue().booleanValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean getNewPending() {
/* 74 */     return getNewValue().booleanValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\membe\\update\GuildMemberUpdatePendingEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */