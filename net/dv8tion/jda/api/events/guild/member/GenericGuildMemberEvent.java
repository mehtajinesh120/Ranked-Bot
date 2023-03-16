/*    */ package net.dv8tion.jda.api.events.guild.member;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.User;
/*    */ import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
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
/*    */ public abstract class GenericGuildMemberEvent
/*    */   extends GenericGuildEvent
/*    */ {
/*    */   private final Member member;
/*    */   
/*    */   public GenericGuildMemberEvent(@Nonnull JDA api, long responseNumber, @Nonnull Member member) {
/* 41 */     super(api, responseNumber, member.getGuild());
/* 42 */     this.member = member;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public User getUser() {
/* 54 */     return getMember().getUser();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Member getMember() {
/* 65 */     return this.member;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\member\GenericGuildMemberEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */