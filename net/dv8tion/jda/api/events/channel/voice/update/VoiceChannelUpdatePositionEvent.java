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
/*    */ public class VoiceChannelUpdatePositionEvent
/*    */   extends GenericVoiceChannelUpdateEvent<Integer>
/*    */ {
/*    */   public static final String IDENTIFIER = "position";
/*    */   
/*    */   public VoiceChannelUpdatePositionEvent(@Nonnull JDA api, long responseNumber, @Nonnull VoiceChannel channel, int oldPosition) {
/* 36 */     super(api, responseNumber, channel, Integer.valueOf(oldPosition), Integer.valueOf(channel.getPositionRaw()), "position");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getOldPosition() {
/* 46 */     return getOldValue().intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNewPosition() {
/* 56 */     return getNewValue().intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\channel\voic\\update\VoiceChannelUpdatePositionEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */