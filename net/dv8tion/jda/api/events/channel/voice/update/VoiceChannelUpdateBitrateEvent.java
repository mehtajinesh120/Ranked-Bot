/*    */ package net.dv8tion.jda.api.events.channel.voice.update;
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
/*    */ 
/*    */ 
/*    */ public class VoiceChannelUpdateBitrateEvent
/*    */   extends GenericVoiceChannelUpdateEvent<Integer>
/*    */ {
/*    */   public static final String IDENTIFIER = "bitrate";
/*    */   
/*    */   public VoiceChannelUpdateBitrateEvent(@Nonnull JDA api, long responseNumber, @Nonnull VoiceChannel channel, int oldBitrate) {
/* 36 */     super(api, responseNumber, channel, Integer.valueOf(oldBitrate), Integer.valueOf(channel.getBitrate()), "bitrate");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getOldBitrate() {
/* 46 */     return getOldValue().intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNewBitrate() {
/* 56 */     return getNewValue().intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\channel\voic\\update\VoiceChannelUpdateBitrateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */