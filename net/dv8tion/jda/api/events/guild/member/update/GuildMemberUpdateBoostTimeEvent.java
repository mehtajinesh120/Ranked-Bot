/*    */ package net.dv8tion.jda.api.events.guild.member.update;
/*    */ 
/*    */ import java.time.OffsetDateTime;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
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
/*    */ public class GuildMemberUpdateBoostTimeEvent
/*    */   extends GenericGuildMemberUpdateEvent<OffsetDateTime>
/*    */ {
/*    */   public static final String IDENTIFIER = "boost_time";
/*    */   
/*    */   public GuildMemberUpdateBoostTimeEvent(@Nonnull JDA api, long responseNumber, @Nonnull Member member, @Nullable OffsetDateTime previous) {
/* 51 */     super(api, responseNumber, member, previous, member.getTimeBoosted(), "boost_time");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public OffsetDateTime getOldTimeBoosted() {
/* 62 */     return getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public OffsetDateTime getNewTimeBoosted() {
/* 73 */     return getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\membe\\update\GuildMemberUpdateBoostTimeEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */