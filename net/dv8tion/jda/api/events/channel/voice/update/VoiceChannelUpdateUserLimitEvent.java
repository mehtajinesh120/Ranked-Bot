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
/*    */ public class VoiceChannelUpdateUserLimitEvent
/*    */   extends GenericVoiceChannelUpdateEvent<Integer>
/*    */ {
/*    */   public static final String IDENTIFIER = "userlimit";
/*    */   
/*    */   public VoiceChannelUpdateUserLimitEvent(@Nonnull JDA api, long responseNumber, @Nonnull VoiceChannel channel, int oldUserLimit) {
/* 36 */     super(api, responseNumber, channel, Integer.valueOf(oldUserLimit), Integer.valueOf(channel.getUserLimit()), "userlimit");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getOldUserLimit() {
/* 46 */     return getOldValue().intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNewUserLimit() {
/* 56 */     return getNewValue().intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\channel\voic\\update\VoiceChannelUpdateUserLimitEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */