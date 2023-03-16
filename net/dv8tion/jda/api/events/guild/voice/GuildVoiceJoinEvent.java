/*    */ package net.dv8tion.jda.api.events.guild.voice;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.VoiceChannel;
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
/*    */ public class GuildVoiceJoinEvent
/*    */   extends GenericGuildVoiceUpdateEvent
/*    */ {
/*    */   public GuildVoiceJoinEvent(@Nonnull JDA api, long responseNumber, @Nonnull Member member) {
/* 48 */     super(api, responseNumber, member, null, member.getVoiceState().getChannel());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public VoiceChannel getChannelJoined() {
/* 55 */     return super.getChannelJoined();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public VoiceChannel getNewValue() {
/* 62 */     return super.getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\voice\GuildVoiceJoinEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */