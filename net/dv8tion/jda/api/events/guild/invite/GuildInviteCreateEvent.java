/*    */ package net.dv8tion.jda.api.events.guild.invite;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.GuildChannel;
/*    */ import net.dv8tion.jda.api.entities.Invite;
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
/*    */ public class GuildInviteCreateEvent
/*    */   extends GenericGuildInviteEvent
/*    */ {
/*    */   private final Invite invite;
/*    */   
/*    */   public GuildInviteCreateEvent(@Nonnull JDA api, long responseNumber, @Nonnull Invite invite, @Nonnull GuildChannel channel) {
/* 41 */     super(api, responseNumber, invite.getCode(), channel);
/* 42 */     this.invite = invite;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Invite getInvite() {
/* 53 */     return this.invite;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\invite\GuildInviteCreateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */