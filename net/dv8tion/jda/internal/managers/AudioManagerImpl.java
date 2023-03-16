/*     */ package net.dv8tion.jda.internal.managers;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.audio.AudioReceiveHandler;
/*     */ import net.dv8tion.jda.api.audio.AudioSendHandler;
/*     */ import net.dv8tion.jda.api.audio.SpeakingMode;
/*     */ import net.dv8tion.jda.api.audio.hooks.ConnectionListener;
/*     */ import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
/*     */ import net.dv8tion.jda.api.audio.hooks.ListenerProxy;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.managers.AudioManager;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.audio.AudioConnection;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.PermissionUtil;
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
/*     */ public class AudioManagerImpl
/*     */   implements AudioManager
/*     */ {
/*  43 */   public final ReentrantLock CONNECTION_LOCK = new ReentrantLock();
/*     */   
/*  45 */   protected final ListenerProxy connectionListener = new ListenerProxy();
/*     */   protected final GuildImpl guild;
/*  47 */   protected AudioConnection audioConnection = null;
/*  48 */   protected EnumSet<SpeakingMode> speakingModes = EnumSet.of(SpeakingMode.VOICE);
/*     */   
/*     */   protected AudioSendHandler sendHandler;
/*     */   protected AudioReceiveHandler receiveHandler;
/*  52 */   protected long queueTimeout = 100L;
/*     */   
/*     */   protected boolean shouldReconnect = true;
/*     */   
/*     */   protected boolean selfMuted = false;
/*     */   protected boolean selfDeafened = false;
/*  58 */   protected long timeout = 10000L;
/*  59 */   protected int speakingDelay = 0;
/*     */ 
/*     */   
/*     */   public AudioManagerImpl(GuildImpl guild) {
/*  63 */     this.guild = guild;
/*     */   }
/*     */ 
/*     */   
/*     */   public AudioConnection getAudioConnection() {
/*  68 */     return this.audioConnection;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void openAudioConnection(VoiceChannel channel) {
/*  74 */     Checks.notNull(channel, "Provided VoiceChannel");
/*     */ 
/*     */ 
/*     */     
/*  78 */     if (!getGuild().equals(channel.getGuild())) {
/*  79 */       throw new IllegalArgumentException("The provided VoiceChannel is not a part of the Guild that this AudioManager handles.Please provide a VoiceChannel from the proper Guild");
/*     */     }
/*  81 */     Member self = getGuild().getSelfMember();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     if (this.audioConnection != null && channel.equals(this.audioConnection.getChannel())) {
/*     */       return;
/*     */     }
/*  89 */     checkChannel(channel, self);
/*     */     
/*  91 */     getJDA().getDirectAudioController().connect(channel);
/*  92 */     if (this.audioConnection != null) {
/*  93 */       this.audioConnection.setChannel(channel);
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkChannel(VoiceChannel channel, Member self) {
/*  98 */     EnumSet<Permission> perms = Permission.getPermissions(PermissionUtil.getEffectivePermission((GuildChannel)channel, self));
/*  99 */     if (!perms.contains(Permission.VOICE_CONNECT))
/* 100 */       throw new InsufficientPermissionException(channel, Permission.VOICE_CONNECT); 
/* 101 */     int userLimit = channel.getUserLimit();
/* 102 */     if (userLimit > 0 && !perms.contains(Permission.ADMINISTRATOR))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 110 */       if (userLimit <= channel.getMembers().size() && 
/* 111 */         !perms.contains(Permission.VOICE_MOVE_OTHERS))
/*     */       {
/* 113 */         throw new InsufficientPermissionException(channel, Permission.VOICE_MOVE_OTHERS, "Unable to connect to VoiceChannel due to userlimit! Requires permission VOICE_MOVE_OTHERS to bypass");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeAudioConnection() {
/* 122 */     getJDA().getAudioLifeCyclePool().execute(() -> {
/*     */           getJDA().setContext();
/*     */           closeAudioConnection(ConnectionStatus.NOT_CONNECTED);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeAudioConnection(ConnectionStatus reason) {
/* 130 */     MiscUtil.locked(this.CONNECTION_LOCK, () -> {
/*     */           if (this.audioConnection != null) {
/*     */             this.audioConnection.close(reason);
/*     */           } else if (reason != ConnectionStatus.DISCONNECTED_REMOVED_FROM_GUILD) {
/*     */             getJDA().getDirectAudioController().disconnect((Guild)getGuild());
/*     */           } 
/*     */           this.audioConnection = null;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpeakingMode(@Nonnull Collection<SpeakingMode> mode) {
/* 143 */     Checks.notEmpty(mode, "Speaking Mode");
/* 144 */     this.speakingModes = EnumSet.copyOf(mode);
/* 145 */     if (this.audioConnection != null) {
/* 146 */       this.audioConnection.setSpeakingMode(this.speakingModes);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<SpeakingMode> getSpeakingMode() {
/* 153 */     return EnumSet.copyOf(this.speakingModes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpeakingDelay(int millis) {
/* 159 */     this.speakingDelay = millis;
/* 160 */     if (this.audioConnection != null) {
/* 161 */       this.audioConnection.setSpeakingDelay(millis);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDAImpl getJDA() {
/* 168 */     return getGuild().getJDA();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public GuildImpl getGuild() {
/* 175 */     return this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean isAttemptingToConnect() {
/* 182 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public VoiceChannel getQueuedAudioConnection() {
/* 189 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VoiceChannel getConnectedChannel() {
/* 195 */     return (this.audioConnection == null) ? null : this.audioConnection.getChannel();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConnected() {
/* 201 */     return (this.audioConnection != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectTimeout(long timeout) {
/* 207 */     this.timeout = timeout;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getConnectTimeout() {
/* 213 */     return this.timeout;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSendingHandler(AudioSendHandler handler) {
/* 219 */     this.sendHandler = handler;
/* 220 */     if (this.audioConnection != null) {
/* 221 */       this.audioConnection.setSendingHandler(handler);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public AudioSendHandler getSendingHandler() {
/* 227 */     return this.sendHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReceivingHandler(AudioReceiveHandler handler) {
/* 233 */     this.receiveHandler = handler;
/* 234 */     if (this.audioConnection != null) {
/* 235 */       this.audioConnection.setReceivingHandler(handler);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public AudioReceiveHandler getReceivingHandler() {
/* 241 */     return this.receiveHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectionListener(ConnectionListener listener) {
/* 247 */     this.connectionListener.setListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnectionListener getConnectionListener() {
/* 253 */     return this.connectionListener.getListener();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ConnectionStatus getConnectionStatus() {
/* 260 */     if (this.audioConnection != null) {
/* 261 */       return this.audioConnection.getConnectionStatus();
/*     */     }
/* 263 */     return ConnectionStatus.NOT_CONNECTED;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoReconnect(boolean shouldReconnect) {
/* 269 */     this.shouldReconnect = shouldReconnect;
/* 270 */     if (this.audioConnection != null) {
/* 271 */       this.audioConnection.setAutoReconnect(shouldReconnect);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAutoReconnect() {
/* 277 */     return this.shouldReconnect;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSelfMuted(boolean muted) {
/* 283 */     if (this.selfMuted != muted) {
/*     */       
/* 285 */       this.selfMuted = muted;
/* 286 */       updateVoiceState();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSelfMuted() {
/* 293 */     return this.selfMuted;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSelfDeafened(boolean deafened) {
/* 299 */     if (this.selfDeafened != deafened) {
/*     */       
/* 301 */       this.selfDeafened = deafened;
/* 302 */       updateVoiceState();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSelfDeafened() {
/* 310 */     return this.selfDeafened;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConnectionListener getListenerProxy() {
/* 315 */     return (ConnectionListener)this.connectionListener;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAudioConnection(AudioConnection audioConnection) {
/* 320 */     if (audioConnection == null) {
/*     */       
/* 322 */       this.audioConnection = null;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 327 */     if (this.audioConnection != null)
/* 328 */       closeAudioConnection(ConnectionStatus.AUDIO_REGION_CHANGE); 
/* 329 */     this.audioConnection = audioConnection;
/* 330 */     audioConnection.setSendingHandler(this.sendHandler);
/* 331 */     audioConnection.setReceivingHandler(this.receiveHandler);
/* 332 */     audioConnection.setQueueTimeout(this.queueTimeout);
/* 333 */     audioConnection.setSpeakingMode(this.speakingModes);
/* 334 */     audioConnection.setSpeakingDelay(this.speakingDelay);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setConnectedChannel(VoiceChannel channel) {
/* 339 */     if (this.audioConnection != null) {
/* 340 */       this.audioConnection.setChannel(channel);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setQueueTimeout(long queueTimeout) {
/* 345 */     this.queueTimeout = queueTimeout;
/* 346 */     if (this.audioConnection != null) {
/* 347 */       this.audioConnection.setQueueTimeout(queueTimeout);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void updateVoiceState() {
/* 352 */     VoiceChannel channel = getConnectedChannel();
/* 353 */     if (channel != null)
/*     */     {
/*     */       
/* 356 */       getJDA().getDirectAudioController().connect(channel);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finalize() {
/* 364 */     if (this.audioConnection != null) {
/*     */       
/* 366 */       LOG.warn("Finalized AudioManager with active audio connection. GuildId: {}", getGuild().getId());
/* 367 */       this.audioConnection.close(ConnectionStatus.DISCONNECTED_REMOVED_FROM_GUILD);
/*     */     } 
/* 369 */     this.audioConnection = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\managers\AudioManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */