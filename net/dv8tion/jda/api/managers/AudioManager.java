/*     */ package net.dv8tion.jda.api.managers;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.annotations.DeprecatedSince;
/*     */ import net.dv8tion.jda.annotations.ForRemoval;
/*     */ import net.dv8tion.jda.annotations.Incubating;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.audio.AudioReceiveHandler;
/*     */ import net.dv8tion.jda.api.audio.AudioSendHandler;
/*     */ import net.dv8tion.jda.api.audio.SpeakingMode;
/*     */ import net.dv8tion.jda.api.audio.hooks.ConnectionListener;
/*     */ import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.JDALogger;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface AudioManager
/*     */ {
/*     */   public static final long DEFAULT_CONNECTION_TIMEOUT = 10000L;
/*  50 */   public static final Logger LOG = JDALogger.getLog(AudioManager.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void openAudioConnection(VoiceChannel paramVoiceChannel);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void closeAudioConnection();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Incubating
/*     */   void setSpeakingMode(@Nonnull Collection<SpeakingMode> paramCollection);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Incubating
/*     */   void setSpeakingMode(@Nonnull SpeakingMode... mode) {
/* 128 */     Checks.notNull(mode, "Speaking Mode");
/* 129 */     setSpeakingMode(Arrays.asList(mode));
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   @Incubating
/*     */   EnumSet<SpeakingMode> getSpeakingMode();
/*     */   
/*     */   void setSpeakingDelay(int paramInt);
/*     */   
/*     */   @Nonnull
/*     */   JDA getJDA();
/*     */   
/*     */   @Nonnull
/*     */   Guild getGuild();
/*     */   
/*     */   @Deprecated
/*     */   @ForRemoval(deadline = "5.0.0")
/*     */   @DeprecatedSince("4.2.0")
/*     */   boolean isAttemptingToConnect();
/*     */   
/*     */   @Nullable
/*     */   @Deprecated
/*     */   @ForRemoval(deadline = "5.0.0")
/*     */   @DeprecatedSince("4.2.0")
/*     */   VoiceChannel getQueuedAudioConnection();
/*     */   
/*     */   @Nullable
/*     */   VoiceChannel getConnectedChannel();
/*     */   
/*     */   boolean isConnected();
/*     */   
/*     */   void setConnectTimeout(long paramLong);
/*     */   
/*     */   long getConnectTimeout();
/*     */   
/*     */   void setSendingHandler(@Nullable AudioSendHandler paramAudioSendHandler);
/*     */   
/*     */   @Nullable
/*     */   AudioSendHandler getSendingHandler();
/*     */   
/*     */   void setReceivingHandler(@Nullable AudioReceiveHandler paramAudioReceiveHandler);
/*     */   
/*     */   @Nullable
/*     */   AudioReceiveHandler getReceivingHandler();
/*     */   
/*     */   void setConnectionListener(@Nullable ConnectionListener paramConnectionListener);
/*     */   
/*     */   @Nullable
/*     */   ConnectionListener getConnectionListener();
/*     */   
/*     */   @Nonnull
/*     */   ConnectionStatus getConnectionStatus();
/*     */   
/*     */   void setAutoReconnect(boolean paramBoolean);
/*     */   
/*     */   boolean isAutoReconnect();
/*     */   
/*     */   void setSelfMuted(boolean paramBoolean);
/*     */   
/*     */   boolean isSelfMuted();
/*     */   
/*     */   void setSelfDeafened(boolean paramBoolean);
/*     */   
/*     */   boolean isSelfDeafened();
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\managers\AudioManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */