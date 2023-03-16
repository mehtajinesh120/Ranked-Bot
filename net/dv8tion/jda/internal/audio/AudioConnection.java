/*     */ package net.dv8tion.jda.internal.audio;
/*     */ import com.iwebpp.crypto.TweetNaclFast;
/*     */ import com.neovisionaries.ws.client.WebSocket;
/*     */ import com.sun.jna.ptr.PointerByReference;
/*     */ import gnu.trove.map.TIntLongMap;
/*     */ import gnu.trove.map.TIntObjectMap;
/*     */ import gnu.trove.map.hash.TIntLongHashMap;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.ShortBuffer;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.audio.AudioNatives;
/*     */ import net.dv8tion.jda.api.audio.AudioReceiveHandler;
/*     */ import net.dv8tion.jda.api.audio.AudioSendHandler;
/*     */ import net.dv8tion.jda.api.audio.CombinedAudio;
/*     */ import net.dv8tion.jda.api.audio.OpusPacket;
/*     */ import net.dv8tion.jda.api.audio.SpeakingMode;
/*     */ import net.dv8tion.jda.api.audio.UserAudio;
/*     */ import net.dv8tion.jda.api.audio.factory.IAudioSendFactory;
/*     */ import net.dv8tion.jda.api.audio.factory.IAudioSendSystem;
/*     */ import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ import net.dv8tion.jda.api.events.ExceptionEvent;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.managers.AudioManagerImpl;
/*     */ import net.dv8tion.jda.internal.utils.IOUtil;
/*     */ import net.dv8tion.jda.internal.utils.JDALogger;
/*     */ import org.slf4j.Logger;
/*     */ import tomp2p.opuswrapper.Opus;
/*     */ 
/*     */ public class AudioConnection {
/*  55 */   public static final Logger LOG = JDALogger.getLog(AudioConnection.class);
/*     */   
/*     */   public static final long MAX_UINT_32 = 4294967295L;
/*     */   
/*     */   private static final int NOT_SPEAKING = 0;
/*  60 */   private static final ByteBuffer silenceBytes = ByteBuffer.wrap(new byte[] { -8, -1, -2 });
/*     */   
/*     */   private static boolean printedError = false;
/*     */   
/*     */   protected volatile DatagramSocket udpSocket;
/*  65 */   private final TIntLongMap ssrcMap = (TIntLongMap)new TIntLongHashMap();
/*  66 */   private final TIntObjectMap<Decoder> opusDecoders = (TIntObjectMap<Decoder>)new TIntObjectHashMap();
/*  67 */   private final HashMap<User, Queue<AudioData>> combinedQueue = new HashMap<>();
/*     */   
/*     */   private final String threadIdentifier;
/*     */   private final AudioWebSocket webSocket;
/*     */   private final JDAImpl api;
/*     */   private VoiceChannel channel;
/*     */   private PointerByReference opusEncoder;
/*     */   private ScheduledExecutorService combinedAudioExecutor;
/*     */   private IAudioSendSystem sendSystem;
/*     */   private Thread receiveThread;
/*     */   private long queueTimeout;
/*     */   private boolean sentSilenceOnConnect = false;
/*  79 */   private int speakingDelay = 10;
/*     */   
/*  81 */   private volatile AudioSendHandler sendHandler = null;
/*  82 */   private volatile AudioReceiveHandler receiveHandler = null;
/*     */   
/*     */   private volatile boolean couldReceive = false;
/*     */   private volatile boolean speaking = false;
/*  86 */   private volatile int speakingMode = SpeakingMode.VOICE.getRaw();
/*  87 */   private volatile int silenceCounter = 0;
/*     */ 
/*     */   
/*     */   public AudioConnection(AudioManagerImpl manager, String endpoint, String sessionId, String token, VoiceChannel channel) {
/*  91 */     this.api = (JDAImpl)channel.getJDA();
/*  92 */     this.channel = channel;
/*  93 */     JDAImpl api = (JDAImpl)channel.getJDA();
/*  94 */     this.threadIdentifier = api.getIdentifierString() + " AudioConnection Guild: " + channel.getGuild().getId();
/*  95 */     this.webSocket = new AudioWebSocket(this, manager.getListenerProxy(), endpoint, channel.getGuild(), sessionId, token, manager.isAutoReconnect());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startConnection() {
/* 102 */     this.webSocket.startConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   public ConnectionStatus getConnectionStatus() {
/* 107 */     return this.webSocket.getConnectionStatus();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAutoReconnect(boolean shouldReconnect) {
/* 112 */     this.webSocket.setAutoReconnect(shouldReconnect);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSpeakingDelay(int millis) {
/* 117 */     this.speakingDelay = Math.max(millis / 20, 10);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSendingHandler(AudioSendHandler handler) {
/* 122 */     this.sendHandler = handler;
/* 123 */     if (this.webSocket.isReady()) {
/* 124 */       setupSendSystem();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setReceivingHandler(AudioReceiveHandler handler) {
/* 129 */     this.receiveHandler = handler;
/* 130 */     if (this.webSocket.isReady()) {
/* 131 */       setupReceiveSystem();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setSpeakingMode(EnumSet<SpeakingMode> mode) {
/* 136 */     int raw = SpeakingMode.getRaw(mode);
/* 137 */     if (raw != this.speakingMode && this.speaking)
/* 138 */       setSpeaking(raw); 
/* 139 */     this.speakingMode = raw;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setQueueTimeout(long queueTimeout) {
/* 144 */     this.queueTimeout = queueTimeout;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoiceChannel getChannel() {
/* 149 */     return this.channel;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChannel(VoiceChannel channel) {
/* 154 */     this.channel = channel;
/*     */   }
/*     */ 
/*     */   
/*     */   public JDAImpl getJDA() {
/* 159 */     return this.api;
/*     */   }
/*     */ 
/*     */   
/*     */   public Guild getGuild() {
/* 164 */     return getChannel().getGuild();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(ConnectionStatus closeStatus) {
/* 169 */     shutdown();
/* 170 */     this.webSocket.close(closeStatus);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void shutdown() {
/* 175 */     if (this.sendSystem != null) {
/*     */       
/* 177 */       this.sendSystem.shutdown();
/* 178 */       this.sendSystem = null;
/*     */     } 
/* 180 */     if (this.receiveThread != null) {
/*     */       
/* 182 */       this.receiveThread.interrupt();
/* 183 */       this.receiveThread = null;
/*     */     } 
/* 185 */     if (this.combinedAudioExecutor != null) {
/*     */       
/* 187 */       this.combinedAudioExecutor.shutdownNow();
/* 188 */       this.combinedAudioExecutor = null;
/*     */     } 
/* 190 */     if (this.opusEncoder != null) {
/*     */       
/* 192 */       Opus.INSTANCE.opus_encoder_destroy(this.opusEncoder);
/* 193 */       this.opusEncoder = null;
/*     */     } 
/*     */     
/* 196 */     this.opusDecoders.valueCollection().forEach(Decoder::close);
/* 197 */     this.opusDecoders.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public WebSocket getWebSocket() {
/* 202 */     return this.webSocket.socket;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void prepareReady() {
/* 209 */     Thread readyThread = new Thread(() -> {
/*     */           getJDA().setContext();
/*     */ 
/*     */           
/*     */           long timeout = getGuild().getAudioManager().getConnectTimeout();
/*     */           
/*     */           long started = System.currentTimeMillis();
/*     */           
/*     */           while (!this.webSocket.isReady()) {
/*     */             if (timeout > 0L && System.currentTimeMillis() - started > timeout) {
/*     */               break;
/*     */             }
/*     */             
/*     */             try {
/*     */               Thread.sleep(10L);
/* 224 */             } catch (InterruptedException e) {
/*     */               LOG.error("AudioConnection ready thread got interrupted while sleeping", e);
/*     */               
/*     */               Thread.currentThread().interrupt();
/*     */             } 
/*     */           } 
/*     */           
/*     */           if (this.webSocket.isReady()) {
/*     */             setupSendSystem();
/*     */             
/*     */             setupReceiveSystem();
/*     */           } else {
/*     */             this.webSocket.close(ConnectionStatus.ERROR_CONNECTION_TIMEOUT);
/*     */           } 
/*     */         });
/*     */     
/* 240 */     readyThread.setUncaughtExceptionHandler((thread, throwable) -> {
/*     */           LOG.error("Uncaught exception in Audio ready-thread", throwable);
/*     */           
/*     */           JDAImpl api = getJDA();
/*     */           api.handleEvent((GenericEvent)new ExceptionEvent((JDA)api, throwable, true));
/*     */         });
/* 246 */     readyThread.setDaemon(true);
/* 247 */     readyThread.setName(this.threadIdentifier + " Ready Thread");
/* 248 */     readyThread.start();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void removeUserSSRC(long userId) {
/* 253 */     AtomicInteger ssrcRef = new AtomicInteger(0);
/* 254 */     boolean modified = this.ssrcMap.retainEntries((ssrc, id) -> {
/*     */           boolean isEntry = (id == userId);
/*     */           
/*     */           if (isEntry) {
/*     */             ssrcRef.set(ssrc);
/*     */           }
/*     */           return !isEntry;
/*     */         });
/* 262 */     if (!modified)
/*     */       return; 
/* 264 */     Decoder decoder = (Decoder)this.opusDecoders.remove(ssrcRef.get());
/* 265 */     if (decoder != null) {
/* 266 */       decoder.close();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void updateUserSSRC(int ssrc, long userId) {
/* 271 */     if (this.ssrcMap.containsKey(ssrc)) {
/*     */       
/* 273 */       long previousId = this.ssrcMap.get(ssrc);
/* 274 */       if (previousId != userId)
/*     */       {
/*     */ 
/*     */         
/* 278 */         LOG.error("Yeah.. So.. JDA received a UserSSRC update for an ssrc that already had a User set. Inform DV8FromTheWorld.\nChannelId: {} SSRC: {} oldId: {} newId: {}", new Object[] { this.channel
/* 279 */               .getId(), Integer.valueOf(ssrc), Long.valueOf(previousId), Long.valueOf(userId) });
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 284 */       this.ssrcMap.put(ssrc, userId);
/*     */ 
/*     */       
/* 287 */       if (this.receiveThread != null && AudioNatives.ensureOpus()) {
/* 288 */         this.opusDecoders.put(ssrc, new Decoder(ssrc));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void setupSendSystem() {
/* 296 */     if (this.udpSocket != null && !this.udpSocket.isClosed() && this.sendHandler != null && this.sendSystem == null) {
/*     */       
/* 298 */       IAudioSendFactory factory = getJDA().getAudioSendFactory();
/* 299 */       this.sendSystem = factory.createSendSystem(new PacketProvider(new TweetNaclFast.SecretBox(this.webSocket.getSecretKey())));
/* 300 */       this.sendSystem.setContextMap(getJDA().getContextMap());
/* 301 */       this.sendSystem.start();
/*     */     }
/* 303 */     else if (this.sendHandler == null && this.sendSystem != null) {
/*     */       
/* 305 */       this.sendSystem.shutdown();
/* 306 */       this.sendSystem = null;
/*     */       
/* 308 */       if (this.opusEncoder != null) {
/*     */         
/* 310 */         Opus.INSTANCE.opus_encoder_destroy(this.opusEncoder);
/* 311 */         this.opusEncoder = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized void setupReceiveSystem() {
/* 318 */     if (this.udpSocket != null && !this.udpSocket.isClosed() && this.receiveHandler != null && this.receiveThread == null) {
/*     */       
/* 320 */       setupReceiveThread();
/*     */     }
/* 322 */     else if (this.receiveHandler == null && this.receiveThread != null) {
/*     */       
/* 324 */       this.receiveThread.interrupt();
/* 325 */       this.receiveThread = null;
/*     */       
/* 327 */       if (this.combinedAudioExecutor != null) {
/*     */         
/* 329 */         this.combinedAudioExecutor.shutdownNow();
/* 330 */         this.combinedAudioExecutor = null;
/*     */       } 
/*     */       
/* 333 */       this.opusDecoders.valueCollection().forEach(Decoder::close);
/* 334 */       this.opusDecoders.clear();
/*     */     }
/* 336 */     else if (this.receiveHandler != null && !this.receiveHandler.canReceiveCombined() && this.combinedAudioExecutor != null) {
/*     */       
/* 338 */       this.combinedAudioExecutor.shutdownNow();
/* 339 */       this.combinedAudioExecutor = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized void setupReceiveThread() {
/* 345 */     if (this.receiveThread == null) {
/*     */       
/* 347 */       this.receiveThread = new Thread(() -> {
/*     */             getJDA().setContext();
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/*     */               this.udpSocket.setSoTimeout(1000);
/* 354 */             } catch (SocketException e) {
/*     */               LOG.error("Couldn't set SO_TIMEOUT for UDP socket", e);
/*     */             } 
/*     */ 
/*     */             
/*     */             while (!this.udpSocket.isClosed() && !Thread.currentThread().isInterrupted()) {
/*     */               DatagramPacket receivedPacket = new DatagramPacket(new byte[1920], 1920);
/*     */               
/*     */               try {
/*     */                 this.udpSocket.receive(receivedPacket);
/*     */                 
/* 365 */                 boolean shouldDecode = (this.receiveHandler != null && (this.receiveHandler.canReceiveUser() || this.receiveHandler.canReceiveCombined()));
/* 366 */                 boolean canReceive = (this.receiveHandler != null && (this.receiveHandler.canReceiveUser() || this.receiveHandler.canReceiveCombined() || this.receiveHandler.canReceiveEncoded()));
/*     */                 
/*     */                 if (canReceive && this.webSocket.getSecretKey() != null) {
/*     */                   if (!this.couldReceive) {
/*     */                     this.couldReceive = true;
/*     */                     
/*     */                     sendSilentPackets();
/*     */                   } 
/*     */                   
/*     */                   AudioPacket decryptedPacket = AudioPacket.decryptAudioPacket(this.webSocket.encryption, receivedPacket, this.webSocket.getSecretKey());
/*     */                   
/*     */                   if (decryptedPacket == null) {
/*     */                     continue;
/*     */                   }
/*     */                   
/*     */                   int ssrc = decryptedPacket.getSSRC();
/*     */                   
/*     */                   long userId = this.ssrcMap.get(ssrc);
/*     */                   
/*     */                   Decoder decoder = (Decoder)this.opusDecoders.get(ssrc);
/*     */                   
/*     */                   if (userId == this.ssrcMap.getNoEntryValue()) {
/*     */                     ByteBuffer audio = decryptedPacket.getEncodedAudio();
/*     */                     
/*     */                     if (!audio.equals(silenceBytes)) {
/*     */                       LOG.debug("Received audio data with an unknown SSRC id. Ignoring");
/*     */                     }
/*     */                     
/*     */                     continue;
/*     */                   } 
/*     */                   
/*     */                   if (decoder == null) {
/*     */                     if (AudioNatives.ensureOpus()) {
/*     */                       this.opusDecoders.put(ssrc, decoder = new Decoder(ssrc));
/*     */                     } else if (!this.receiveHandler.canReceiveEncoded()) {
/*     */                       LOG.error("Unable to decode audio due to missing opus binaries!");
/*     */                       
/*     */                       break;
/*     */                     } 
/*     */                   }
/*     */                   
/*     */                   OpusPacket opusPacket = new OpusPacket(decryptedPacket, userId, decoder);
/*     */                   
/*     */                   if (this.receiveHandler.canReceiveEncoded()) {
/*     */                     this.receiveHandler.handleEncodedAudio(opusPacket);
/*     */                   }
/*     */                   
/*     */                   if (!shouldDecode || !opusPacket.canDecode()) {
/*     */                     continue;
/*     */                   }
/*     */                   
/*     */                   User user = getJDA().getUserById(userId);
/*     */                   
/*     */                   if (user == null) {
/*     */                     LOG.warn("Received audio data with a known SSRC, but the userId associate with the SSRC is unknown to JDA!");
/*     */                     continue;
/*     */                   } 
/*     */                   short[] decodedAudio = opusPacket.decode();
/*     */                   if (decodedAudio == null) {
/*     */                     continue;
/*     */                   }
/*     */                   if (this.receiveHandler.canReceiveUser()) {
/*     */                     this.receiveHandler.handleUserAudio(new UserAudio(user, decodedAudio));
/*     */                   }
/*     */                   if (this.receiveHandler.canReceiveCombined() && this.receiveHandler.includeUserInCombinedAudio(user)) {
/*     */                     Queue<AudioData> queue = this.combinedQueue.get(user);
/*     */                     if (queue == null) {
/*     */                       queue = new ConcurrentLinkedQueue<>();
/*     */                       this.combinedQueue.put(user, queue);
/*     */                     } 
/*     */                     queue.add(new AudioData(decodedAudio));
/*     */                   } 
/*     */                   continue;
/*     */                 } 
/*     */                 if (this.couldReceive) {
/*     */                   this.couldReceive = false;
/*     */                   sendSilentPackets();
/*     */                 } 
/* 444 */               } catch (SocketTimeoutException socketTimeoutException) {
/*     */ 
/*     */               
/*     */               }
/* 448 */               catch (SocketException socketException) {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               }
/* 454 */               catch (Exception e) {
/*     */                 LOG.error("There was some random exception while waiting for udp packets", e);
/*     */               } 
/*     */             } 
/*     */           });
/*     */       
/* 460 */       this.receiveThread.setUncaughtExceptionHandler((thread, throwable) -> {
/*     */             LOG.error("There was some uncaught exception in the audio receive thread", throwable);
/*     */             
/*     */             JDAImpl api = getJDA();
/*     */             api.handleEvent((GenericEvent)new ExceptionEvent((JDA)api, throwable, true));
/*     */           });
/* 466 */       this.receiveThread.setDaemon(true);
/* 467 */       this.receiveThread.setName(this.threadIdentifier + " Receiving Thread");
/* 468 */       this.receiveThread.start();
/*     */     } 
/*     */     
/* 471 */     if (this.receiveHandler.canReceiveCombined())
/*     */     {
/* 473 */       setupCombinedExecutor();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized void setupCombinedExecutor() {
/* 479 */     if (this.combinedAudioExecutor == null) {
/*     */       
/* 481 */       this.combinedAudioExecutor = Executors.newSingleThreadScheduledExecutor(task -> {
/*     */             Thread t = new Thread(task, this.threadIdentifier + " Combined Thread");
/*     */ 
/*     */             
/*     */             t.setDaemon(true);
/*     */ 
/*     */             
/*     */             t.setUncaughtExceptionHandler(());
/*     */             
/*     */             return t;
/*     */           });
/*     */       
/* 493 */       this.combinedAudioExecutor.scheduleAtFixedRate(() -> {
/*     */             getJDA().setContext();
/*     */             
/*     */             try {
/*     */               List<User> users = new LinkedList<>();
/*     */               
/*     */               List<short[]> audioParts = (List)new LinkedList<>();
/*     */               
/*     */               if (this.receiveHandler != null && this.receiveHandler.canReceiveCombined()) {
/*     */                 long currentTime = System.currentTimeMillis();
/*     */                 
/*     */                 for (Map.Entry<User, Queue<AudioData>> entry : this.combinedQueue.entrySet()) {
/*     */                   User user = entry.getKey();
/*     */                   
/*     */                   Queue<AudioData> queue = entry.getValue();
/*     */                   
/*     */                   if (queue.isEmpty()) {
/*     */                     continue;
/*     */                   }
/*     */                   
/*     */                   AudioData audioData = queue.poll();
/*     */                   
/*     */                   while (audioData != null && currentTime - audioData.time > this.queueTimeout) {
/*     */                     audioData = queue.poll();
/*     */                   }
/*     */                   
/*     */                   if (audioData == null) {
/*     */                     continue;
/*     */                   }
/*     */                   
/*     */                   users.add(user);
/*     */                   
/*     */                   audioParts.add(audioData.data);
/*     */                 } 
/*     */                 
/*     */                 if (!audioParts.isEmpty()) {
/*     */                   int audioLength = audioParts.stream().mapToInt(()).max().getAsInt();
/*     */                   
/*     */                   short[] mix = new short[1920];
/*     */                   
/*     */                   for (int i = 0; i < audioLength; i++) {
/*     */                     int sample = 0;
/*     */                     
/*     */                     Iterator<short[]> iterator = (Iterator)audioParts.iterator();
/*     */                     
/*     */                     while (iterator.hasNext()) {
/*     */                       short[] audio = iterator.next();
/*     */                       if (i < audio.length) {
/*     */                         sample += audio[i];
/*     */                         continue;
/*     */                       } 
/*     */                       iterator.remove();
/*     */                     } 
/*     */                     if (sample > 32767) {
/*     */                       mix[i] = Short.MAX_VALUE;
/*     */                     } else if (sample < -32768) {
/*     */                       mix[i] = Short.MIN_VALUE;
/*     */                     } else {
/*     */                       mix[i] = (short)sample;
/*     */                     } 
/*     */                   } 
/*     */                   this.receiveHandler.handleCombinedAudio(new CombinedAudio(users, mix));
/*     */                 } else {
/*     */                   this.receiveHandler.handleCombinedAudio(new CombinedAudio(Collections.emptyList(), new short[1920]));
/*     */                 } 
/*     */               } 
/* 559 */             } catch (Exception e) {
/*     */               LOG.error("There was some unexpected exception in the combinedAudioExecutor!", e);
/*     */             } 
/*     */           }0L, 20L, TimeUnit.MILLISECONDS);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ByteBuffer encodeToOpus(ByteBuffer rawAudio) {
/* 569 */     ShortBuffer nonEncodedBuffer = ShortBuffer.allocate(rawAudio.remaining() / 2);
/* 570 */     ByteBuffer encoded = ByteBuffer.allocate(4096);
/* 571 */     for (int i = rawAudio.position(); i < rawAudio.limit(); i += 2) {
/*     */       
/* 573 */       int firstByte = 0xFF & rawAudio.get(i);
/* 574 */       int secondByte = 0xFF & rawAudio.get(i + 1);
/*     */ 
/*     */       
/* 577 */       short toShort = (short)(firstByte << 8 | secondByte);
/*     */       
/* 579 */       nonEncodedBuffer.put(toShort);
/*     */     } 
/* 581 */     nonEncodedBuffer.flip();
/*     */     
/* 583 */     int result = Opus.INSTANCE.opus_encode(this.opusEncoder, nonEncodedBuffer, 960, encoded, encoded.capacity());
/* 584 */     if (result <= 0) {
/*     */       
/* 586 */       LOG.error("Received error code from opus_encode(...): {}", Integer.valueOf(result));
/* 587 */       return null;
/*     */     } 
/*     */     
/* 590 */     encoded.position(0).limit(result);
/* 591 */     return encoded;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setSpeaking(int raw) {
/* 596 */     this.speaking = (raw != 0);
/*     */ 
/*     */ 
/*     */     
/* 600 */     DataObject obj = DataObject.empty().put("speaking", Integer.valueOf(raw)).put("ssrc", Integer.valueOf(this.webSocket.getSSRC())).put("delay", Integer.valueOf(0));
/* 601 */     this.webSocket.send(5, obj);
/*     */   }
/*     */ 
/*     */   
/*     */   private void sendSilentPackets() {
/* 606 */     this.silenceCounter = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finalize() {
/* 613 */     shutdown();
/*     */   }
/*     */   
/*     */   private class PacketProvider
/*     */     implements IPacketProvider {
/* 618 */     private char seq = Character.MIN_VALUE;
/* 619 */     private int timestamp = 0;
/*     */     private TweetNaclFast.SecretBox boxer;
/* 621 */     private long nonce = 0L;
/* 622 */     private ByteBuffer buffer = ByteBuffer.allocate(512);
/* 623 */     private ByteBuffer encryptionBuffer = ByteBuffer.allocate(512);
/* 624 */     private final byte[] nonceBuffer = new byte[24];
/*     */ 
/*     */     
/*     */     public PacketProvider(TweetNaclFast.SecretBox boxer) {
/* 628 */       this.boxer = boxer;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getIdentifier() {
/* 635 */       return AudioConnection.this.threadIdentifier;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public VoiceChannel getConnectedChannel() {
/* 642 */       return AudioConnection.this.getChannel();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public DatagramSocket getUdpSocket() {
/* 649 */       return AudioConnection.this.udpSocket;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public InetSocketAddress getSocketAddress() {
/* 656 */       return AudioConnection.this.webSocket.getAddress();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public DatagramPacket getNextPacket(boolean changeTalking) {
/* 662 */       ByteBuffer buffer = getNextPacketRaw(changeTalking);
/* 663 */       return (buffer == null) ? null : getDatagramPacket(buffer);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ByteBuffer getNextPacketRaw(boolean changeTalking) {
/* 669 */       ByteBuffer nextPacket = null;
/*     */ 
/*     */       
/* 672 */       try { if (AudioConnection.this.sentSilenceOnConnect && AudioConnection.this.sendHandler != null && AudioConnection.this.sendHandler.canProvide())
/*     */         
/* 674 */         { AudioConnection.this.silenceCounter = -1;
/* 675 */           ByteBuffer rawAudio = AudioConnection.this.sendHandler.provide20MsAudio();
/* 676 */           if (rawAudio != null && !rawAudio.hasArray())
/*     */           {
/*     */             
/* 679 */             AudioConnection.LOG.error("AudioSendHandler provided ByteBuffer without a backing array! This is unsupported.");
/*     */           }
/* 681 */           if (rawAudio == null || !rawAudio.hasRemaining() || !rawAudio.hasArray())
/*     */           
/* 683 */           { if (AudioConnection.this.speaking && changeTalking) {
/* 684 */               AudioConnection.this.sendSilentPackets();
/*     */             } }
/*     */           else
/*     */           
/* 688 */           { if (!AudioConnection.this.sendHandler.isOpus())
/*     */             
/* 690 */             { rawAudio = encodeAudio(rawAudio);
/* 691 */               if (rawAudio == null)
/*     */               
/*     */               { 
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
/* 734 */                 if (nextPacket != null) {
/* 735 */                   this.timestamp += 960;
/*     */                 }
/* 737 */                 return nextPacket; }  }  nextPacket = getPacketData(rawAudio); if (!AudioConnection.this.speaking) AudioConnection.this.setSpeaking(AudioConnection.this.speakingMode);  if (this.seq + 1 > 65535) { this.seq = Character.MIN_VALUE; } else { this.seq = (char)(this.seq + 1); }  }  } else if (AudioConnection.this.silenceCounter > -1) { nextPacket = getPacketData(AudioConnection.silenceBytes); if (this.seq + 1 > 65535) { this.seq = Character.MIN_VALUE; } else { this.seq = (char)(this.seq + 1); }  AudioConnection.this.silenceCounter++; if ((!AudioConnection.this.sentSilenceOnConnect && AudioConnection.this.silenceCounter > 10) || AudioConnection.this.silenceCounter > AudioConnection.this.speakingDelay) { if (AudioConnection.this.sentSilenceOnConnect) AudioConnection.this.setSpeaking(0);  AudioConnection.this.silenceCounter = -1; AudioConnection.this.sentSilenceOnConnect = true; }  } else if (AudioConnection.this.speaking && changeTalking) { AudioConnection.this.sendSilentPackets(); }  } catch (Exception e) { AudioConnection.LOG.error("There was an error while getting next audio packet", e); }  if (nextPacket != null) this.timestamp += 960;  return nextPacket;
/*     */     }
/*     */ 
/*     */     
/*     */     private ByteBuffer encodeAudio(ByteBuffer rawAudio) {
/* 742 */       if (AudioConnection.this.opusEncoder == null) {
/*     */         
/* 744 */         if (!AudioNatives.ensureOpus()) {
/*     */           
/* 746 */           if (!AudioConnection.printedError)
/* 747 */             AudioConnection.LOG.error("Unable to process PCM audio without opus binaries!"); 
/* 748 */           AudioConnection.printedError = true;
/* 749 */           return null;
/*     */         } 
/* 751 */         IntBuffer error = IntBuffer.allocate(1);
/* 752 */         AudioConnection.this.opusEncoder = Opus.INSTANCE.opus_encoder_create(48000, 2, 2049, error);
/* 753 */         if (error.get() != 0 && AudioConnection.this.opusEncoder == null) {
/*     */           
/* 755 */           AudioConnection.LOG.error("Received error status from opus_encoder_create(...): {}", Integer.valueOf(error.get()));
/* 756 */           return null;
/*     */         } 
/*     */       } 
/* 759 */       return AudioConnection.this.encodeToOpus(rawAudio);
/*     */     }
/*     */ 
/*     */     
/*     */     private DatagramPacket getDatagramPacket(ByteBuffer b) {
/* 764 */       byte[] data = b.array();
/* 765 */       int offset = b.arrayOffset() + b.position();
/* 766 */       int length = b.remaining();
/* 767 */       return new DatagramPacket(data, offset, length, AudioConnection.this.webSocket.getAddress());
/*     */     }
/*     */     
/*     */     private ByteBuffer getPacketData(ByteBuffer rawAudio) {
/*     */       int nlen;
/* 772 */       ensureEncryptionBuffer(rawAudio);
/* 773 */       AudioPacket packet = new AudioPacket(this.encryptionBuffer, this.seq, this.timestamp, AudioConnection.this.webSocket.getSSRC(), rawAudio);
/*     */       
/* 775 */       switch (AudioConnection.this.webSocket.encryption) {
/*     */         
/*     */         case XSALSA20_POLY1305:
/* 778 */           nlen = 0;
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
/* 794 */           return this.buffer = packet.asEncryptedPacket(this.boxer, this.buffer, this.nonceBuffer, nlen);case XSALSA20_POLY1305_LITE: loadNextNonce(this.nonce = 0L); loadNextNonce(++this.nonce); nlen = 4; return this.buffer = packet.asEncryptedPacket(this.boxer, this.buffer, this.nonceBuffer, nlen);case XSALSA20_POLY1305_SUFFIX: ThreadLocalRandom.current().nextBytes(this.nonceBuffer); nlen = 24; return this.buffer = packet.asEncryptedPacket(this.boxer, this.buffer, this.nonceBuffer, nlen);
/*     */       } 
/*     */       throw new IllegalStateException("Encryption mode [" + AudioConnection.this.webSocket.encryption + "] is not supported!");
/*     */     }
/*     */     private void ensureEncryptionBuffer(ByteBuffer data) {
/* 799 */       this.encryptionBuffer.clear();
/* 800 */       int currentCapacity = this.encryptionBuffer.remaining();
/* 801 */       int requiredCapacity = 12 + data.remaining();
/* 802 */       if (currentCapacity < requiredCapacity) {
/* 803 */         this.encryptionBuffer = ByteBuffer.allocate(requiredCapacity);
/*     */       }
/*     */     }
/*     */     
/*     */     private void loadNextNonce(long nonce) {
/* 808 */       IOUtil.setIntBigEndian(this.nonceBuffer, 0, (int)nonce);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onConnectionError(@Nonnull ConnectionStatus status) {
/* 814 */       AudioConnection.LOG.warn("IAudioSendSystem reported a connection error of: {}", status);
/* 815 */       AudioConnection.LOG.warn("Shutting down AudioConnection.");
/* 816 */       AudioConnection.this.webSocket.close(status);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onConnectionLost() {
/* 822 */       AudioConnection.LOG.warn("Closing AudioConnection due to inability to send audio packets.");
/* 823 */       AudioConnection.LOG.warn("Cannot send audio packet because JDA cannot navigate the route to Discord.\nAre you sure you have internet connection? It is likely that you've lost connection.");
/*     */       
/* 825 */       AudioConnection.this.webSocket.close(ConnectionStatus.ERROR_LOST_CONNECTION);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class AudioData
/*     */   {
/*     */     private final long time;
/*     */     private final short[] data;
/*     */     
/*     */     public AudioData(short[] data) {
/* 836 */       this.time = System.currentTimeMillis();
/* 837 */       this.data = data;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\audio\AudioConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */