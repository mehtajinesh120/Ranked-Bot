/*    */ package net.dv8tion.jda.api.events.guild.voice;
/*    */ 
/*    */ import javax.annotation.Nonnull;
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
/*    */ public class GuildVoiceGuildMuteEvent
/*    */   extends GenericGuildVoiceEvent
/*    */ {
/*    */   protected final boolean guildMuted;
/*    */   
/*    */   public GuildVoiceGuildMuteEvent(@Nonnull JDA api, long responseNumber, @Nonnull Member member) {
/* 47 */     super(api, responseNumber, member);
/* 48 */     this.guildMuted = member.getVoiceState().isGuildMuted();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isGuildMuted() {
/* 59 */     return this.guildMuted;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\voice\GuildVoiceGuildMuteEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */