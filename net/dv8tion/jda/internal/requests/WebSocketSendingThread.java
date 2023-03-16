/*     */ package net.dv8tion.jda.internal.requests;
/*     */ 
/*     */ import gnu.trove.map.TLongObjectMap;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildVoiceState;
/*     */ import net.dv8tion.jda.api.managers.AudioManager;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.audio.ConnectionRequest;
/*     */ import net.dv8tion.jda.internal.audio.ConnectionStage;
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
/*     */ class WebSocketSendingThread
/*     */   implements Runnable
/*     */ {
/*  40 */   private static final Logger LOG = WebSocketClient.LOG;
/*     */   
/*     */   private final WebSocketClient client;
/*     */   
/*     */   private final JDAImpl api;
/*     */   
/*     */   private final ReentrantLock queueLock;
/*     */   private final Queue<DataObject> chunkQueue;
/*     */   private final Queue<DataObject> ratelimitQueue;
/*     */   private final TLongObjectMap<ConnectionRequest> queuedAudioConnections;
/*     */   private final ScheduledExecutorService executor;
/*     */   private Future<?> handle;
/*     */   private boolean needRateLimit = false;
/*     */   private boolean attemptedToSend = false;
/*     */   private boolean shutdown = false;
/*     */   
/*     */   WebSocketSendingThread(WebSocketClient client) {
/*  57 */     this.client = client;
/*  58 */     this.api = client.api;
/*  59 */     this.queueLock = client.queueLock;
/*  60 */     this.chunkQueue = client.chunkSyncQueue;
/*  61 */     this.ratelimitQueue = client.ratelimitQueue;
/*  62 */     this.queuedAudioConnections = client.queuedAudioConnections;
/*  63 */     this.executor = client.executor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() {
/*  68 */     this.shutdown = true;
/*  69 */     if (this.handle != null) {
/*  70 */       this.handle.cancel(false);
/*     */     }
/*     */   }
/*     */   
/*     */   public void start() {
/*  75 */     this.shutdown = false;
/*  76 */     this.handle = this.executor.submit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private void scheduleIdle() {
/*  81 */     if (this.shutdown)
/*     */       return; 
/*  83 */     this.handle = this.executor.schedule(this, 500L, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */   
/*     */   private void scheduleSentMessage() {
/*  88 */     if (this.shutdown)
/*     */       return; 
/*  90 */     this.handle = this.executor.schedule(this, 10L, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */   
/*     */   private void scheduleRateLimit() {
/*  95 */     if (this.shutdown)
/*     */       return; 
/*  97 */     this.handle = this.executor.schedule(this, 1L, TimeUnit.MINUTES);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 104 */     if (!this.client.sentAuthInfo) {
/*     */       
/* 106 */       scheduleIdle();
/*     */       
/*     */       return;
/*     */     } 
/* 110 */     ConnectionRequest audioRequest = null;
/* 111 */     DataObject chunkRequest = null;
/*     */     
/*     */     try {
/* 114 */       this.api.setContext();
/* 115 */       this.attemptedToSend = false;
/* 116 */       this.needRateLimit = false;
/*     */       
/* 118 */       audioRequest = this.client.getNextAudioConnectRequest();
/* 119 */       if (!this.queueLock.tryLock() && !this.queueLock.tryLock(10L, TimeUnit.SECONDS)) {
/*     */         
/* 121 */         scheduleNext();
/*     */         
/*     */         return;
/*     */       } 
/* 125 */       chunkRequest = this.chunkQueue.peek();
/* 126 */       if (chunkRequest != null) {
/* 127 */         handleChunkSync(chunkRequest);
/* 128 */       } else if (audioRequest != null) {
/* 129 */         handleAudioRequest(audioRequest);
/*     */       } else {
/* 131 */         handleNormalRequest();
/*     */       } 
/* 133 */     } catch (InterruptedException ignored) {
/*     */       
/* 135 */       LOG.debug("Main WS send thread interrupted. Most likely JDA is disconnecting the websocket.");
/*     */       
/*     */       return;
/* 138 */     } catch (Throwable ex) {
/*     */ 
/*     */       
/* 141 */       LOG.error("Encountered error in gateway worker", ex);
/*     */       
/* 143 */       if (!this.attemptedToSend)
/*     */       {
/*     */         
/* 146 */         if (chunkRequest != null) {
/* 147 */           this.client.chunkSyncQueue.remove(chunkRequest);
/* 148 */         } else if (audioRequest != null) {
/* 149 */           this.client.removeAudioConnection(audioRequest.getGuildIdLong());
/*     */         } 
/*     */       }
/*     */       
/* 153 */       if (ex instanceof Error) {
/* 154 */         throw (Error)ex;
/*     */       }
/*     */     }
/*     */     finally {
/*     */       
/* 159 */       this.client.maybeUnlock();
/*     */     } 
/*     */     
/* 162 */     scheduleNext();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void scheduleNext() {
/*     */     try {
/* 169 */       if (this.needRateLimit) {
/* 170 */         scheduleRateLimit();
/* 171 */       } else if (!this.attemptedToSend) {
/* 172 */         scheduleIdle();
/*     */       } else {
/* 174 */         scheduleSentMessage();
/*     */       } 
/* 176 */     } catch (RejectedExecutionException ex) {
/*     */       
/* 178 */       if (this.api.getStatus() == JDA.Status.SHUTTING_DOWN || this.api.getStatus() == JDA.Status.SHUTDOWN) {
/* 179 */         LOG.debug("Rejected task after shutdown", ex);
/*     */       } else {
/* 181 */         LOG.error("Was unable to schedule next packet due to rejected execution by threadpool", ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleChunkSync(DataObject chunkOrSyncRequest) {
/* 187 */     LOG.debug("Sending chunk/sync request {}", chunkOrSyncRequest);
/* 188 */     boolean success = send(
/* 189 */         DataObject.empty()
/* 190 */         .put("op", Integer.valueOf(8))
/* 191 */         .put("d", chunkOrSyncRequest));
/*     */ 
/*     */     
/* 194 */     if (success)
/* 195 */       this.chunkQueue.remove(); 
/*     */   }
/*     */   
/*     */   private void handleAudioRequest(ConnectionRequest audioRequest) {
/*     */     DataObject packet;
/* 200 */     long channelId = audioRequest.getChannelId();
/* 201 */     long guildId = audioRequest.getGuildIdLong();
/* 202 */     Guild guild = this.api.getGuildById(guildId);
/* 203 */     if (guild == null) {
/*     */       
/* 205 */       LOG.debug("Discarding voice request due to null guild {}", Long.valueOf(guildId));
/*     */       
/* 207 */       this.queuedAudioConnections.remove(guildId);
/*     */       return;
/*     */     } 
/* 210 */     ConnectionStage stage = audioRequest.getStage();
/* 211 */     AudioManager audioManager = guild.getAudioManager();
/*     */     
/* 213 */     switch (stage) {
/*     */       
/*     */       case RECONNECT:
/*     */       case DISCONNECT:
/* 217 */         packet = newVoiceClose(guildId);
/*     */         break;
/*     */       
/*     */       default:
/* 221 */         packet = newVoiceOpen(audioManager, channelId, guild.getIdLong()); break;
/*     */     } 
/* 223 */     LOG.debug("Sending voice request {}", packet);
/* 224 */     if (send(packet)) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 229 */       audioRequest.setNextAttemptEpoch(System.currentTimeMillis() + 10000L);
/*     */ 
/*     */ 
/*     */       
/* 233 */       GuildVoiceState voiceState = guild.getSelfMember().getVoiceState();
/* 234 */       this.client.updateAudioConnection0(guild.getIdLong(), voiceState.getChannel());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleNormalRequest() {
/* 240 */     DataObject message = this.ratelimitQueue.peek();
/* 241 */     if (message != null) {
/*     */       
/* 243 */       LOG.debug("Sending normal message {}", message);
/* 244 */       if (send(message)) {
/* 245 */         this.ratelimitQueue.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean send(DataObject request) {
/* 252 */     this.needRateLimit = !this.client.send(request, false);
/* 253 */     this.attemptedToSend = true;
/* 254 */     return !this.needRateLimit;
/*     */   }
/*     */ 
/*     */   
/*     */   protected DataObject newVoiceClose(long guildId) {
/* 259 */     return DataObject.empty()
/* 260 */       .put("op", Integer.valueOf(4))
/* 261 */       .put("d", DataObject.empty()
/* 262 */         .put("guild_id", Long.toUnsignedString(guildId))
/* 263 */         .putNull("channel_id")
/* 264 */         .put("self_mute", Boolean.valueOf(false))
/* 265 */         .put("self_deaf", Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected DataObject newVoiceOpen(AudioManager manager, long channel, long guild) {
/* 270 */     return DataObject.empty()
/* 271 */       .put("op", Integer.valueOf(4))
/* 272 */       .put("d", DataObject.empty()
/* 273 */         .put("guild_id", Long.valueOf(guild))
/* 274 */         .put("channel_id", Long.valueOf(channel))
/* 275 */         .put("self_mute", Boolean.valueOf(manager.isSelfMuted()))
/* 276 */         .put("self_deaf", Boolean.valueOf(manager.isSelfDeafened())));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\WebSocketSendingThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */