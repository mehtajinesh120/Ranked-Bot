/*    */ package net.dv8tion.jda.api.events.channel.voice;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
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
/*    */ public class VoiceChannelCreateEvent
/*    */   extends GenericVoiceChannelEvent
/*    */ {
/*    */   public VoiceChannelCreateEvent(@Nonnull JDA api, long responseNumber, @Nonnull VoiceChannel channel) {
/* 32 */     super(api, responseNumber, channel);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\channel\voice\VoiceChannelCreateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */