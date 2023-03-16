/*    */ package net.dv8tion.jda.api.events.channel.voice.update;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.Region;
/*    */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.jetbrains.annotations.Nullable;
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
/*    */ public class VoiceChannelUpdateRegionEvent
/*    */   extends GenericVoiceChannelUpdateEvent<String>
/*    */ {
/*    */   public static final String IDENTIFIER = "region";
/*    */   
/*    */   public VoiceChannelUpdateRegionEvent(@NotNull JDA api, long responseNumber, @NotNull VoiceChannel channel, @Nullable String oldRegion) {
/* 39 */     super(api, responseNumber, channel, oldRegion, channel.getRegionRaw(), "region");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Region getOldRegion() {
/* 50 */     return (getOldValue() == null) ? Region.AUTOMATIC : Region.fromKey(getOldValue());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Region getNewRegion() {
/* 61 */     return (getNewValue() == null) ? Region.AUTOMATIC : Region.fromKey(getNewValue());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getOldRegionRaw() {
/* 72 */     return getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getNewRegionRaw() {
/* 83 */     return getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\channel\voic\\update\VoiceChannelUpdateRegionEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */