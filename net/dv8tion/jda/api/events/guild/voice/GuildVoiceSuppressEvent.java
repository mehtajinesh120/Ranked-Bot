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
/*    */ 
/*    */ 
/*    */ public class GuildVoiceSuppressEvent
/*    */   extends GenericGuildVoiceEvent
/*    */ {
/*    */   protected final boolean suppressed;
/*    */   
/*    */   public GuildVoiceSuppressEvent(@Nonnull JDA api, long responseNumber, @Nonnull Member member) {
/* 49 */     super(api, responseNumber, member);
/* 50 */     this.suppressed = member.getVoiceState().isSuppressed();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSuppressed() {
/* 61 */     return this.suppressed;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\voice\GuildVoiceSuppressEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */