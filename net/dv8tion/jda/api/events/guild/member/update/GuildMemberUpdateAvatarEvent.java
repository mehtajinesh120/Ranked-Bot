/*    */ package net.dv8tion.jda.api.events.guild.member.update;
/*    */ 
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
/*    */ public class GuildMemberUpdateAvatarEvent
/*    */   extends GenericGuildMemberUpdateEvent<String>
/*    */ {
/*    */   public static final String IDENTIFIER = "avatar";
/*    */   
/*    */   public GuildMemberUpdateAvatarEvent(@Nonnull JDA api, long responseNumber, @Nonnull Member member, @Nullable String oldAvatarId) {
/* 49 */     super(api, responseNumber, member, oldAvatarId, member.getAvatarId(), "avatar");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getOldAvatarId() {
/* 60 */     return getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getOldAvatarUrl() {
/* 70 */     return (this.previous == null) ? null : String.format("https://cdn.discordapp.com/guilds/%s/users/%s/avatars/%s.%s", new Object[] { getMember().getGuild().getId(), getMember().getId(), this.previous, this.previous.startsWith("a_") ? "gif" : "png" });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getNewAvatarId() {
/* 81 */     return getNewValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getNewAvatarUrl() {
/* 91 */     return (this.next == null) ? null : String.format("https://cdn.discordapp.com/guilds/%s/users/%s/avatars/%s.%s", new Object[] { getMember().getGuild().getId(), getMember().getId(), this.next, this.next.startsWith("a_") ? "gif" : "png" });
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\membe\\update\GuildMemberUpdateAvatarEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */