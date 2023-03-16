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
/*    */ public class GuildVoiceGuildDeafenEvent
/*    */   extends GenericGuildVoiceEvent
/*    */ {
/*    */   protected final boolean guildDeafened;
/*    */   
/*    */   public GuildVoiceGuildDeafenEvent(@Nonnull JDA api, long responseNumber, @Nonnull Member member) {
/* 47 */     super(api, responseNumber, member);
/* 48 */     this.guildDeafened = member.getVoiceState().isGuildDeafened();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isGuildDeafened() {
/* 59 */     return this.guildDeafened;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\voice\GuildVoiceGuildDeafenEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */