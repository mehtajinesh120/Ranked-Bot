/*      */ package net.dv8tion.jda.internal.requests;
/*      */ import com.neovisionaries.ws.client.ThreadType;
/*      */ import com.neovisionaries.ws.client.WebSocket;
/*      */ import com.neovisionaries.ws.client.WebSocketException;
/*      */ import com.neovisionaries.ws.client.WebSocketFactory;
/*      */ import com.neovisionaries.ws.client.WebSocketFrame;
/*      */ import com.neovisionaries.ws.client.WebSocketListener;
/*      */ import gnu.trove.iterator.TLongObjectIterator;
/*      */ import gnu.trove.map.TLongObjectMap;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.net.Socket;
/*      */ import java.net.SocketException;
/*      */ import java.time.OffsetDateTime;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.RejectedExecutionException;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.zip.DataFormatException;
/*      */ import javax.annotation.Nonnull;
/*      */ import net.dv8tion.jda.api.GatewayEncoding;
/*      */ import net.dv8tion.jda.api.JDA;
/*      */ import net.dv8tion.jda.api.JDAInfo;
/*      */ import net.dv8tion.jda.api.Permission;
/*      */ import net.dv8tion.jda.api.audio.hooks.ConnectionListener;
/*      */ import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
/*      */ import net.dv8tion.jda.api.entities.Guild;
/*      */ import net.dv8tion.jda.api.entities.GuildChannel;
/*      */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*      */ import net.dv8tion.jda.api.events.ExceptionEvent;
/*      */ import net.dv8tion.jda.api.events.GenericEvent;
/*      */ import net.dv8tion.jda.api.events.RawGatewayEvent;
/*      */ import net.dv8tion.jda.api.events.ShutdownEvent;
/*      */ import net.dv8tion.jda.api.exceptions.ParsingException;
/*      */ import net.dv8tion.jda.api.managers.AudioManager;
/*      */ import net.dv8tion.jda.api.requests.CloseCode;
/*      */ import net.dv8tion.jda.api.utils.Compression;
/*      */ import net.dv8tion.jda.api.utils.data.DataArray;
/*      */ import net.dv8tion.jda.api.utils.data.DataObject;
/*      */ import net.dv8tion.jda.internal.JDAImpl;
/*      */ import net.dv8tion.jda.internal.audio.ConnectionRequest;
/*      */ import net.dv8tion.jda.internal.audio.ConnectionStage;
/*      */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*      */ import net.dv8tion.jda.internal.handle.GuildBanHandler;
/*      */ import net.dv8tion.jda.internal.handle.GuildMemberAddHandler;
/*      */ import net.dv8tion.jda.internal.handle.GuildRoleCreateHandler;
/*      */ import net.dv8tion.jda.internal.handle.GuildSetupController;
/*      */ import net.dv8tion.jda.internal.handle.InviteCreateHandler;
/*      */ import net.dv8tion.jda.internal.handle.MessageReactionHandler;
/*      */ import net.dv8tion.jda.internal.handle.PresenceUpdateHandler;
/*      */ import net.dv8tion.jda.internal.handle.SocketHandler;
/*      */ import net.dv8tion.jda.internal.handle.UserUpdateHandler;
/*      */ import net.dv8tion.jda.internal.managers.AudioManagerImpl;
/*      */ import net.dv8tion.jda.internal.managers.PresenceImpl;
/*      */ import net.dv8tion.jda.internal.utils.UnlockHook;
/*      */ import net.dv8tion.jda.internal.utils.cache.AbstractCacheView;
/*      */ import net.dv8tion.jda.internal.utils.compress.Decompressor;
/*      */ import net.dv8tion.jda.internal.utils.compress.ZlibDecompressor;
/*      */ import org.slf4j.MDC;
/*      */ 
/*      */ public class WebSocketClient extends WebSocketAdapter implements WebSocketListener {
/*   73 */   public static final ThreadLocal<Boolean> WS_THREAD = ThreadLocal.withInitial(() -> Boolean.valueOf(false));
/*   74 */   public static final Logger LOG = JDALogger.getLog(WebSocketClient.class);
/*      */   
/*      */   public static final int IDENTIFY_DELAY = 5;
/*      */   public static final int ZLIB_SUFFIX = 65535;
/*      */   protected static final String INVALIDATE_REASON = "INVALIDATE_SESSION";
/*   79 */   protected static final long IDENTIFY_BACKOFF = TimeUnit.SECONDS.toMillis(5L);
/*      */   
/*      */   protected final JDAImpl api;
/*      */   protected final JDA.ShardInfo shardInfo;
/*   83 */   protected final Map<String, SocketHandler> handlers = new HashMap<>();
/*      */   
/*      */   protected final Compression compression;
/*      */   protected final int gatewayIntents;
/*      */   protected final MemberChunkManager chunkManager;
/*      */   protected final GatewayEncoding encoding;
/*      */   public WebSocket socket;
/*   90 */   protected volatile String sessionId = null;
/*   91 */   protected final Object readLock = new Object();
/*      */   
/*      */   protected Decompressor decompressor;
/*   94 */   protected final ReentrantLock queueLock = new ReentrantLock();
/*      */   
/*      */   protected final ScheduledExecutorService executor;
/*      */   
/*      */   protected WebSocketSendingThread ratelimitThread;
/*      */   protected volatile Future<?> keepAliveThread;
/*      */   protected boolean initiating;
/*  101 */   protected int missedHeartbeats = 0;
/*  102 */   protected int reconnectTimeoutS = 2;
/*      */   protected long heartbeatStartTime;
/*  104 */   protected long identifyTime = 0L;
/*      */   
/*  106 */   protected final TLongObjectMap<ConnectionRequest> queuedAudioConnections = MiscUtil.newLongMap();
/*  107 */   protected final Queue<DataObject> chunkSyncQueue = new ConcurrentLinkedQueue<>();
/*  108 */   protected final Queue<DataObject> ratelimitQueue = new ConcurrentLinkedQueue<>();
/*      */   
/*      */   protected volatile long ratelimitResetTime;
/*  111 */   protected final AtomicInteger messagesSent = new AtomicInteger(0);
/*      */   
/*      */   protected volatile boolean shutdown = false;
/*      */   
/*      */   protected boolean shouldReconnect;
/*      */   
/*      */   protected boolean handleIdentifyRateLimit = false;
/*      */   
/*      */   protected boolean connected = false;
/*      */   protected volatile boolean printedRateLimitMessage = false;
/*      */   protected volatile boolean sentAuthInfo = false;
/*      */   protected boolean firstInit = true;
/*      */   protected boolean processingReady = true;
/*      */   protected volatile ConnectNode connectNode;
/*      */   
/*      */   public WebSocketClient(JDAImpl api, Compression compression, int gatewayIntents, GatewayEncoding encoding) {
/*  127 */     this.api = api;
/*  128 */     this.executor = api.getGatewayPool();
/*  129 */     this.shardInfo = api.getShardInfo();
/*  130 */     this.compression = compression;
/*  131 */     this.gatewayIntents = gatewayIntents;
/*  132 */     this.chunkManager = new MemberChunkManager(this);
/*  133 */     this.encoding = encoding;
/*  134 */     this.shouldReconnect = api.isAutoReconnect();
/*  135 */     this.connectNode = new StartingNode();
/*  136 */     setupHandlers();
/*      */     
/*      */     try {
/*  139 */       api.getSessionController().appendSession(this.connectNode);
/*      */     }
/*  141 */     catch (RuntimeException|Error e) {
/*      */       
/*  143 */       LOG.error("Failed to append new session to session controller queue. Shutting down!", e);
/*  144 */       this.api.setStatus(JDA.Status.SHUTDOWN);
/*  145 */       this.api.handleEvent((GenericEvent)new ShutdownEvent((JDA)api, 
/*  146 */             OffsetDateTime.now(), 1006));
/*  147 */       if (e instanceof RuntimeException) {
/*  148 */         throw (RuntimeException)e;
/*      */       }
/*  150 */       throw (Error)e;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public JDA getJDA() {
/*  156 */     return (JDA)this.api;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAutoReconnect(boolean reconnect) {
/*  161 */     this.shouldReconnect = reconnect;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isConnected() {
/*  166 */     return this.connected;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getGatewayIntents() {
/*  171 */     return this.gatewayIntents;
/*      */   }
/*      */ 
/*      */   
/*      */   public MemberChunkManager getChunkManager() {
/*  176 */     return this.chunkManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public void ready() {
/*  181 */     if (this.initiating) {
/*      */       
/*  183 */       this.initiating = false;
/*  184 */       this.processingReady = false;
/*  185 */       if (this.firstInit)
/*      */       {
/*  187 */         this.firstInit = false;
/*  188 */         if (this.api.getGuilds().size() >= 2000) {
/*      */           
/*  190 */           JDAImpl.LOG.warn(" __      __ _    ___  _  _  ___  _  _   ___  _ ");
/*  191 */           JDAImpl.LOG.warn(" \\ \\    / //_\\  | _ \\| \\| ||_ _|| \\| | / __|| |");
/*  192 */           JDAImpl.LOG.warn("  \\ \\/\\/ // _ \\ |   /| .` | | | | .` || (_ ||_|");
/*  193 */           JDAImpl.LOG.warn("   \\_/\\_//_/ \\_\\|_|_\\|_|\\_||___||_|\\_| \\___|(_)");
/*  194 */           JDAImpl.LOG.warn("You're running a session with over 2000 connected");
/*  195 */           JDAImpl.LOG.warn("guilds. You should shard the connection in order");
/*  196 */           JDAImpl.LOG.warn("to split the load or things like resuming");
/*  197 */           JDAImpl.LOG.warn("connection might not work as expected.");
/*  198 */           JDAImpl.LOG.warn("For more info see https://git.io/vrFWP");
/*      */         } 
/*  200 */         JDAImpl.LOG.info("Finished Loading!");
/*  201 */         this.api.handleEvent((GenericEvent)new ReadyEvent((JDA)this.api, this.api.getResponseTotal()));
/*      */       }
/*      */       else
/*      */       {
/*  205 */         updateAudioManagerReferences();
/*  206 */         JDAImpl.LOG.info("Finished (Re)Loading!");
/*  207 */         this.api.handleEvent((GenericEvent)new ReconnectedEvent((JDA)this.api, this.api.getResponseTotal()));
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  212 */       JDAImpl.LOG.debug("Successfully resumed Session!");
/*  213 */       this.api.handleEvent((GenericEvent)new ResumedEvent((JDA)this.api, this.api.getResponseTotal()));
/*      */     } 
/*  215 */     this.api.setStatus(JDA.Status.CONNECTED);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReady() {
/*  220 */     return !this.initiating;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSession() {
/*  225 */     return (this.sessionId != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handle(List<DataObject> events) {
/*  230 */     events.forEach(this::onDispatch);
/*      */   }
/*      */ 
/*      */   
/*      */   public void send(DataObject message) {
/*  235 */     locked("Interrupted while trying to add request to queue", () -> Boolean.valueOf(this.ratelimitQueue.add(message)));
/*      */   }
/*      */ 
/*      */   
/*      */   public void cancelChunkRequest(String nonce) {
/*  240 */     locked("Interrupted while trying to cancel chunk request", () -> Boolean.valueOf(this.chunkSyncQueue.removeIf(())));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendChunkRequest(DataObject request) {
/*  246 */     locked("Interrupted while trying to add chunk request", () -> Boolean.valueOf(this.chunkSyncQueue.add(request)));
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean send(DataObject message, boolean skipQueue) {
/*  251 */     if (!this.connected) {
/*  252 */       return false;
/*      */     }
/*  254 */     long now = System.currentTimeMillis();
/*      */     
/*  256 */     if (this.ratelimitResetTime <= now) {
/*      */       
/*  258 */       this.messagesSent.set(0);
/*  259 */       this.ratelimitResetTime = now + 60000L;
/*  260 */       this.printedRateLimitMessage = false;
/*      */     } 
/*      */ 
/*      */     
/*  264 */     if (this.messagesSent.get() <= 115 || (skipQueue && this.messagesSent.get() <= 119)) {
/*      */       
/*  266 */       LOG.trace("<- {}", message);
/*  267 */       if (this.encoding == GatewayEncoding.ETF) {
/*  268 */         this.socket.sendBinary(message.toETF());
/*      */       } else {
/*  270 */         this.socket.sendText(message.toString());
/*  271 */       }  this.messagesSent.getAndIncrement();
/*  272 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  276 */     if (!this.printedRateLimitMessage) {
/*      */       
/*  278 */       LOG.warn("Hit the WebSocket RateLimit! This can be caused by too many presence or voice status updates (connect/disconnect/mute/deaf). Regular: {} Voice: {} Chunking: {}", new Object[] {
/*  279 */             Integer.valueOf(this.ratelimitQueue.size()), Integer.valueOf(this.queuedAudioConnections.size()), Integer.valueOf(this.chunkSyncQueue.size()) });
/*  280 */       this.printedRateLimitMessage = true;
/*      */     } 
/*  282 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setupSendingThread() {
/*  288 */     this.ratelimitThread = new WebSocketSendingThread(this);
/*  289 */     this.ratelimitThread.start();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void prepareClose() {
/*      */     try {
/*  296 */       if (this.socket != null) {
/*      */         
/*  298 */         Socket rawSocket = this.socket.getSocket();
/*  299 */         if (rawSocket != null) {
/*  300 */           rawSocket.setSoTimeout(10000);
/*      */         }
/*      */       } 
/*  303 */     } catch (SocketException socketException) {}
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() {
/*  308 */     prepareClose();
/*  309 */     if (this.socket != null) {
/*  310 */       this.socket.sendClose(1000);
/*      */     }
/*      */   }
/*      */   
/*      */   public void close(int code) {
/*  315 */     prepareClose();
/*  316 */     if (this.socket != null) {
/*  317 */       this.socket.sendClose(code);
/*      */     }
/*      */   }
/*      */   
/*      */   public void close(int code, String reason) {
/*  322 */     prepareClose();
/*  323 */     if (this.socket != null) {
/*  324 */       this.socket.sendClose(code, reason);
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized void shutdown() {
/*  329 */     this.shutdown = true;
/*  330 */     this.shouldReconnect = false;
/*  331 */     if (this.connectNode != null)
/*  332 */       this.api.getSessionController().removeSession(this.connectNode); 
/*  333 */     close(1000, "Shutting down");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected synchronized void connect() {
/*  342 */     if (this.api.getStatus() != JDA.Status.ATTEMPTING_TO_RECONNECT)
/*  343 */       this.api.setStatus(JDA.Status.CONNECTING_TO_WEBSOCKET); 
/*  344 */     if (this.shutdown)
/*  345 */       throw new RejectedExecutionException("JDA is shutdown!"); 
/*  346 */     this.initiating = true;
/*      */ 
/*      */     
/*  349 */     String url = this.api.getGatewayUrl() + "?encoding=" + this.encoding.name().toLowerCase() + "&v=" + '\b';
/*      */     
/*  351 */     if (this.compression != Compression.NONE) {
/*      */       
/*  353 */       url = url + "&compress=" + this.compression.getKey();
/*  354 */       switch (this.compression) {
/*      */         
/*      */         case DISCONNECT:
/*  357 */           if (this.decompressor == null || this.decompressor.getType() != Compression.ZLIB)
/*  358 */             this.decompressor = (Decompressor)new ZlibDecompressor(this.api.getMaxBufferSize()); 
/*      */           break;
/*      */         default:
/*  361 */           throw new IllegalStateException("Unknown compression");
/*      */       } 
/*      */ 
/*      */     
/*      */     } 
/*      */     try {
/*  367 */       WebSocketFactory socketFactory = new WebSocketFactory(this.api.getWebSocketFactory());
/*  368 */       IOUtil.setServerName(socketFactory, url);
/*  369 */       if (socketFactory.getSocketTimeout() > 0) {
/*  370 */         socketFactory.setSocketTimeout(Math.max(1000, socketFactory.getSocketTimeout()));
/*      */       } else {
/*  372 */         socketFactory.setSocketTimeout(10000);
/*  373 */       }  this.socket = socketFactory.createSocket(url);
/*  374 */       this.socket.setDirectTextMessage(true);
/*  375 */       this.socket.addHeader("Accept-Encoding", "gzip")
/*  376 */         .addListener(this)
/*  377 */         .connect();
/*      */     }
/*  379 */     catch (IOException|WebSocketException e) {
/*      */       
/*  381 */       this.api.resetGatewayUrl();
/*      */       
/*  383 */       throw new IllegalStateException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onThreadStarted(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {
/*  390 */     this.api.setContext();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
/*  396 */     prepareClose();
/*  397 */     this.api.setStatus(JDA.Status.IDENTIFYING_SESSION);
/*  398 */     if (this.sessionId == null) {
/*      */       
/*  400 */       LOG.info("Connected to WebSocket");
/*      */       
/*  402 */       LOG.debug("Connected with gateway intents: {}", Integer.toBinaryString(this.gatewayIntents));
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  407 */       LOG.debug("Connected to WebSocket");
/*      */     } 
/*  409 */     this.connected = true;
/*      */     
/*  411 */     this.messagesSent.set(0);
/*  412 */     this.ratelimitResetTime = System.currentTimeMillis() + 60000L;
/*  413 */     if (this.sessionId == null) {
/*  414 */       sendIdentify();
/*      */     } else {
/*  416 */       sendResume();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
/*  422 */     this.sentAuthInfo = false;
/*  423 */     this.connected = false;
/*      */     
/*  425 */     if (Thread.currentThread().isInterrupted()) {
/*      */       
/*  427 */       Thread thread = new Thread(() -> handleDisconnect(websocket, serverCloseFrame, clientCloseFrame, closedByServer));
/*      */       
/*  429 */       thread.setName(this.api.getIdentifierString() + " MainWS-ReconnectThread");
/*  430 */       thread.start();
/*      */     }
/*      */     else {
/*      */       
/*  434 */       handleDisconnect(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void handleDisconnect(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
/*  440 */     this.api.setStatus(JDA.Status.DISCONNECTED);
/*  441 */     CloseCode closeCode = null;
/*  442 */     int rawCloseCode = 1005;
/*      */ 
/*      */     
/*  445 */     boolean isInvalidate = false;
/*      */     
/*  447 */     if (this.keepAliveThread != null) {
/*      */       
/*  449 */       this.keepAliveThread.cancel(false);
/*  450 */       this.keepAliveThread = null;
/*      */     } 
/*  452 */     if (closedByServer && serverCloseFrame != null) {
/*      */       
/*  454 */       rawCloseCode = serverCloseFrame.getCloseCode();
/*  455 */       String rawCloseReason = serverCloseFrame.getCloseReason();
/*  456 */       closeCode = CloseCode.from(rawCloseCode);
/*  457 */       if (closeCode == CloseCode.RATE_LIMITED) {
/*  458 */         LOG.error("WebSocket connection closed due to ratelimit! Sent more than 120 websocket messages in under 60 seconds!");
/*  459 */       } else if (closeCode == CloseCode.UNKNOWN_ERROR) {
/*  460 */         LOG.error("WebSocket connection closed due to server error! {}: {}", Integer.valueOf(rawCloseCode), rawCloseReason);
/*  461 */       } else if (closeCode != null) {
/*  462 */         LOG.debug("WebSocket connection closed with code {}", closeCode);
/*  463 */       } else if (rawCloseReason != null) {
/*  464 */         LOG.warn("WebSocket connection closed with code {}: {}", Integer.valueOf(rawCloseCode), rawCloseReason);
/*      */       } else {
/*  466 */         LOG.warn("WebSocket connection closed with unknown meaning for close-code {}", Integer.valueOf(rawCloseCode));
/*      */       } 
/*  468 */     } else if (clientCloseFrame != null) {
/*      */       
/*  470 */       rawCloseCode = clientCloseFrame.getCloseCode();
/*  471 */       if (rawCloseCode == 1000 && "INVALIDATE_SESSION".equals(clientCloseFrame.getCloseReason()))
/*      */       {
/*      */ 
/*      */         
/*  475 */         isInvalidate = true;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  480 */     boolean closeCodeIsReconnect = (closeCode == null || closeCode.isReconnect());
/*  481 */     if (!this.shouldReconnect || !closeCodeIsReconnect || this.executor.isShutdown()) {
/*      */       
/*  483 */       if (this.ratelimitThread != null) {
/*      */         
/*  485 */         this.ratelimitThread.shutdown();
/*  486 */         this.ratelimitThread = null;
/*      */       } 
/*      */       
/*  489 */       if (!closeCodeIsReconnect)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/*  494 */         LOG.error("WebSocket connection was closed and cannot be recovered due to identification issues\n{}", closeCode);
/*      */       }
/*      */       
/*  497 */       if (this.decompressor != null)
/*  498 */         this.decompressor.shutdown(); 
/*  499 */       this.api.shutdownInternals();
/*  500 */       this.api.handleEvent((GenericEvent)new ShutdownEvent((JDA)this.api, OffsetDateTime.now(), rawCloseCode));
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  505 */       synchronized (this.readLock) {
/*      */         
/*  507 */         if (this.decompressor != null)
/*  508 */           this.decompressor.reset(); 
/*      */       } 
/*  510 */       if (isInvalidate)
/*  511 */         invalidate(); 
/*  512 */       this.api.handleEvent((GenericEvent)new DisconnectEvent((JDA)this.api, serverCloseFrame, clientCloseFrame, closedByServer, OffsetDateTime.now()));
/*      */       
/*      */       try {
/*  515 */         handleReconnect(rawCloseCode);
/*      */       }
/*  517 */       catch (InterruptedException e) {
/*      */         
/*  519 */         LOG.error("Failed to resume due to interrupted thread", e);
/*  520 */         invalidate();
/*  521 */         queueReconnect();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void handleReconnect(int code) throws InterruptedException {
/*  528 */     if (this.sessionId == null) {
/*      */       
/*  530 */       if (this.handleIdentifyRateLimit) {
/*      */         
/*  532 */         long backoff = calculateIdentifyBackoff();
/*  533 */         if (backoff > 0L) {
/*      */ 
/*      */           
/*  536 */           LOG.error("Encountered IDENTIFY Rate Limit! Waiting {} milliseconds before trying again!", Long.valueOf(backoff));
/*  537 */           Thread.sleep(backoff);
/*      */         }
/*      */         else {
/*      */           
/*  541 */           LOG.error("Encountered IDENTIFY Rate Limit!");
/*      */         } 
/*      */       } 
/*  544 */       LOG.warn("Got disconnected from WebSocket (Code {}). Appending to reconnect queue", Integer.valueOf(code));
/*  545 */       queueReconnect();
/*      */     }
/*      */     else {
/*      */       
/*  549 */       LOG.debug("Got disconnected from WebSocket (Code: {}). Attempting to resume session", Integer.valueOf(code));
/*  550 */       reconnect();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected long calculateIdentifyBackoff() {
/*  556 */     long currentTime = System.currentTimeMillis();
/*      */     
/*  558 */     return currentTime - this.identifyTime + IDENTIFY_BACKOFF;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void queueReconnect() {
/*      */     try {
/*  565 */       this.api.setStatus(JDA.Status.RECONNECT_QUEUED);
/*  566 */       this.connectNode = new ReconnectNode();
/*  567 */       this.api.getSessionController().appendSession(this.connectNode);
/*      */     }
/*  569 */     catch (IllegalStateException ex) {
/*      */       
/*  571 */       LOG.error("Reconnect queue rejected session. Shutting down...");
/*  572 */       this.api.setStatus(JDA.Status.SHUTDOWN);
/*  573 */       this.api.handleEvent((GenericEvent)new ShutdownEvent((JDA)this.api, OffsetDateTime.now(), 1006));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void reconnect() throws InterruptedException {
/*  579 */     reconnect(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void reconnect(boolean callFromQueue) throws InterruptedException {
/*  591 */     Set<MDC.MDCCloseable> contextEntries = null;
/*  592 */     Map<String, String> previousContext = null;
/*      */     
/*  594 */     ConcurrentMap<String, String> contextMap = this.api.getContextMap();
/*  595 */     if (callFromQueue && contextMap != null) {
/*      */       
/*  597 */       previousContext = MDC.getCopyOfContextMap();
/*      */ 
/*      */       
/*  600 */       contextEntries = (Set<MDC.MDCCloseable>)contextMap.entrySet().stream().map(entry -> MDC.putCloseable((String)entry.getKey(), (String)entry.getValue())).collect(Collectors.toSet());
/*      */     } 
/*      */     
/*  603 */     if (this.shutdown) {
/*      */       
/*  605 */       this.api.setStatus(JDA.Status.SHUTDOWN);
/*  606 */       this.api.handleEvent((GenericEvent)new ShutdownEvent((JDA)this.api, OffsetDateTime.now(), 1000));
/*      */       return;
/*      */     } 
/*  609 */     String message = "";
/*  610 */     if (callFromQueue)
/*  611 */       message = String.format("Queue is attempting to reconnect a shard...%s ", new Object[] { (this.shardInfo != null) ? (" Shard: " + this.shardInfo.getShardString()) : "" }); 
/*  612 */     if (this.sessionId != null)
/*  613 */       this.reconnectTimeoutS = 0; 
/*  614 */     LOG.debug("{}Attempting to reconnect in {}s", message, Integer.valueOf(this.reconnectTimeoutS));
/*  615 */     while (this.shouldReconnect) {
/*      */       
/*  617 */       this.api.setStatus(JDA.Status.WAITING_TO_RECONNECT);
/*  618 */       int delay = this.reconnectTimeoutS;
/*      */       
/*  620 */       this.reconnectTimeoutS = (this.reconnectTimeoutS == 0) ? 2 : Math.min(this.reconnectTimeoutS << 1, this.api.getMaxReconnectDelay());
/*  621 */       Thread.sleep((delay * 1000));
/*  622 */       this.handleIdentifyRateLimit = false;
/*  623 */       this.api.setStatus(JDA.Status.ATTEMPTING_TO_RECONNECT);
/*  624 */       LOG.debug("Attempting to reconnect!");
/*      */       
/*      */       try {
/*  627 */         connect();
/*      */         
/*      */         break;
/*  630 */       } catch (RejectedExecutionException ex) {
/*      */ 
/*      */         
/*  633 */         this.api.setStatus(JDA.Status.SHUTDOWN);
/*  634 */         this.api.handleEvent((GenericEvent)new ShutdownEvent((JDA)this.api, OffsetDateTime.now(), 1000));
/*      */         
/*      */         return;
/*  637 */       } catch (RuntimeException ex) {
/*      */ 
/*      */         
/*  640 */         LOG.warn("Reconnect failed! Next attempt in {}s", Integer.valueOf(this.reconnectTimeoutS));
/*      */       } 
/*      */     } 
/*  643 */     if (contextEntries != null)
/*  644 */       contextEntries.forEach(MDC.MDCCloseable::close); 
/*  645 */     if (previousContext != null) {
/*  646 */       previousContext.forEach(MDC::put);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setupKeepAlive(int timeout) {
/*      */     try {
/*  653 */       Socket rawSocket = this.socket.getSocket();
/*  654 */       if (rawSocket != null) {
/*  655 */         rawSocket.setSoTimeout(timeout + 10000);
/*      */       }
/*  657 */     } catch (SocketException ex) {
/*      */       
/*  659 */       LOG.warn("Failed to setup timeout for socket", ex);
/*      */     } 
/*      */     
/*  662 */     this.keepAliveThread = this.executor.scheduleAtFixedRate(() -> { this.api.setContext(); if (this.connected) sendKeepAlive();  }0L, timeout, TimeUnit.MILLISECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void sendKeepAlive() {
/*  675 */     DataObject keepAlivePacket = DataObject.empty().put("op", Integer.valueOf(1)).put("d", Long.valueOf(this.api.getResponseTotal()));
/*      */ 
/*      */     
/*  678 */     if (this.missedHeartbeats >= 2) {
/*      */       
/*  680 */       this.missedHeartbeats = 0;
/*  681 */       LOG.warn("Missed 2 heartbeats! Trying to reconnect...");
/*  682 */       prepareClose();
/*  683 */       this.socket.disconnect(4900, "ZOMBIE CONNECTION");
/*      */     }
/*      */     else {
/*      */       
/*  687 */       this.missedHeartbeats++;
/*  688 */       send(keepAlivePacket, true);
/*  689 */       this.heartbeatStartTime = System.currentTimeMillis();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void sendIdentify() {
/*  695 */     LOG.debug("Sending Identify-packet...");
/*  696 */     PresenceImpl presenceObj = (PresenceImpl)this.api.getPresence();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  702 */     DataObject connectionProperties = DataObject.empty().put("$os", System.getProperty("os.name")).put("$browser", "JDA").put("$device", "JDA").put("$referring_domain", "").put("$referrer", "");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  708 */     DataObject payload = DataObject.empty().put("presence", presenceObj.getFullPresence()).put("token", getToken()).put("properties", connectionProperties).put("v", Integer.valueOf(8)).put("large_threshold", Integer.valueOf(this.api.getLargeThreshold()));
/*  709 */     payload.put("intents", Integer.valueOf(this.gatewayIntents));
/*      */ 
/*      */ 
/*      */     
/*  713 */     DataObject identify = DataObject.empty().put("op", Integer.valueOf(2)).put("d", payload);
/*  714 */     if (this.shardInfo != null)
/*      */     {
/*  716 */       payload
/*  717 */         .put("shard", DataArray.empty()
/*  718 */           .add(Integer.valueOf(this.shardInfo.getShardId()))
/*  719 */           .add(Integer.valueOf(this.shardInfo.getShardTotal())));
/*      */     }
/*  721 */     send(identify, true);
/*  722 */     this.handleIdentifyRateLimit = true;
/*  723 */     this.identifyTime = System.currentTimeMillis();
/*  724 */     this.sentAuthInfo = true;
/*  725 */     this.api.setStatus(JDA.Status.AWAITING_LOGIN_CONFIRMATION);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void sendResume() {
/*  730 */     LOG.debug("Sending Resume-packet...");
/*      */ 
/*      */     
/*  733 */     DataObject resume = DataObject.empty().put("op", Integer.valueOf(6)).put("d", DataObject.empty()
/*  734 */         .put("session_id", this.sessionId)
/*  735 */         .put("token", getToken())
/*  736 */         .put("seq", Long.valueOf(this.api.getResponseTotal())));
/*  737 */     send(resume, true);
/*      */     
/*  739 */     this.api.setStatus(JDA.Status.AWAITING_LOGIN_CONFIRMATION);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void invalidate() {
/*  744 */     this.sessionId = null;
/*  745 */     this.sentAuthInfo = false;
/*      */     
/*  747 */     Objects.requireNonNull(this.chunkSyncQueue); locked("Interrupted while trying to invalidate chunk/sync queue", this.chunkSyncQueue::clear);
/*      */     
/*  749 */     this.api.getTextChannelsView().clear();
/*  750 */     this.api.getVoiceChannelsView().clear();
/*  751 */     this.api.getStoreChannelsView().clear();
/*  752 */     this.api.getCategoriesView().clear();
/*  753 */     this.api.getGuildsView().clear();
/*  754 */     this.api.getUsersView().clear();
/*  755 */     this.api.getPrivateChannelsView().clear();
/*  756 */     this.api.getEventCache().clear();
/*  757 */     this.api.getGuildSetupController().clearCache();
/*  758 */     this.chunkManager.clear();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateAudioManagerReferences() {
/*  763 */     AbstractCacheView<AudioManager> managerView = this.api.getAudioManagersView();
/*  764 */     UnlockHook hook = managerView.writeLock();
/*      */     
/*  766 */     try { TLongObjectMap<AudioManager> managerMap = managerView.getMap();
/*  767 */       if (managerMap.size() > 0) {
/*  768 */         LOG.trace("Updating AudioManager references");
/*      */       }
/*  770 */       for (TLongObjectIterator<AudioManager> it = managerMap.iterator(); it.hasNext(); ) {
/*      */         
/*  772 */         it.advance();
/*  773 */         long guildId = it.key();
/*  774 */         AudioManagerImpl mng = (AudioManagerImpl)it.value();
/*      */         
/*  776 */         GuildImpl guild = (GuildImpl)this.api.getGuildById(guildId);
/*  777 */         if (guild == null) {
/*      */ 
/*      */           
/*  780 */           this.queuedAudioConnections.remove(guildId);
/*  781 */           mng.closeAudioConnection(ConnectionStatus.DISCONNECTED_REMOVED_DURING_RECONNECT);
/*  782 */           it.remove();
/*      */         } 
/*      */       } 
/*  785 */       if (hook != null) hook.close();  }
/*      */     catch (Throwable throwable) { if (hook != null)
/*      */         try { hook.close(); }
/*      */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*      */           throw throwable; }
/*  790 */      } protected String getToken() { if (this.api.getAccountType() == AccountType.BOT)
/*  791 */       return this.api.getToken().substring("Bot ".length()); 
/*  792 */     return this.api.getToken(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected List<DataObject> convertPresencesReplace(long responseTotal, DataArray array) {
/*  798 */     List<DataObject> output = new LinkedList<>();
/*  799 */     for (int i = 0; i < array.length(); i++) {
/*      */       
/*  801 */       DataObject presence = array.getObject(i);
/*  802 */       DataObject obj = DataObject.empty();
/*  803 */       obj.put("comment", "This was constructed from a PRESENCES_REPLACE payload")
/*  804 */         .put("op", Integer.valueOf(0))
/*  805 */         .put("s", Long.valueOf(responseTotal))
/*  806 */         .put("d", presence)
/*  807 */         .put("t", "PRESENCE_UPDATE");
/*  808 */       output.add(obj);
/*      */     } 
/*  810 */     return output;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleEvent(DataObject content) {
/*      */     try {
/*  817 */       onEvent(content);
/*      */     }
/*  819 */     catch (Exception ex) {
/*      */       
/*  821 */       LOG.error("Encountered exception on lifecycle level\nJSON: {}", content, ex);
/*  822 */       this.api.handleEvent((GenericEvent)new ExceptionEvent((JDA)this.api, ex, true));
/*      */     } 
/*      */   } protected void onEvent(DataObject content) {
/*      */     boolean isResume;
/*      */     int closeCode;
/*      */     DataObject data;
/*  828 */     WS_THREAD.set(Boolean.valueOf(true));
/*  829 */     int opCode = content.getInt("op");
/*      */     
/*  831 */     if (!content.isNull("s"))
/*      */     {
/*  833 */       this.api.setResponseTotal(content.getInt("s"));
/*      */     }
/*      */     
/*  836 */     switch (opCode) {
/*      */       
/*      */       case 0:
/*  839 */         onDispatch(content);
/*      */         return;
/*      */       case 1:
/*  842 */         LOG.debug("Got Keep-Alive request (OP 1). Sending response...");
/*  843 */         sendKeepAlive();
/*      */         return;
/*      */       case 7:
/*  846 */         LOG.debug("Got Reconnect request (OP 7). Closing connection now...");
/*  847 */         close(4900, "OP 7: RECONNECT");
/*      */         return;
/*      */       case 9:
/*  850 */         LOG.debug("Got Invalidate request (OP 9). Invalidating...");
/*  851 */         this.handleIdentifyRateLimit = (this.handleIdentifyRateLimit && System.currentTimeMillis() - this.identifyTime < IDENTIFY_BACKOFF);
/*      */         
/*  853 */         this.sentAuthInfo = false;
/*  854 */         isResume = content.getBoolean("d");
/*      */ 
/*      */         
/*  857 */         closeCode = isResume ? 4900 : 1000;
/*  858 */         if (isResume) {
/*  859 */           LOG.debug("Session can be recovered... Closing and sending new RESUME request");
/*      */         } else {
/*  861 */           invalidate();
/*      */         } 
/*  863 */         close(closeCode, "INVALIDATE_SESSION");
/*      */         return;
/*      */       case 10:
/*  866 */         LOG.debug("Got HELLO packet (OP 10). Initializing keep-alive.");
/*  867 */         data = content.getObject("d");
/*  868 */         setupKeepAlive(data.getInt("heartbeat_interval"));
/*      */         return;
/*      */       case 11:
/*  871 */         LOG.trace("Got Heartbeat Ack (OP 11).");
/*  872 */         this.missedHeartbeats = 0;
/*  873 */         this.api.setGatewayPing(System.currentTimeMillis() - this.heartbeatStartTime);
/*      */         return;
/*      */     } 
/*  876 */     LOG.debug("Got unknown op-code: {} with content: {}", Integer.valueOf(opCode), content);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onDispatch(DataObject raw) {
/*  882 */     String type = raw.getString("t");
/*  883 */     long responseTotal = this.api.getResponseTotal();
/*      */     
/*  885 */     if (!raw.isType("d", DataType.OBJECT)) {
/*      */ 
/*      */       
/*  888 */       if (type.equals("PRESENCES_REPLACE")) {
/*      */         
/*  890 */         DataArray payload = raw.getArray("d");
/*  891 */         List<DataObject> converted = convertPresencesReplace(responseTotal, payload);
/*  892 */         SocketHandler handler = getHandler("PRESENCE_UPDATE");
/*  893 */         LOG.trace("{} -> {}", type, payload);
/*  894 */         for (DataObject o : converted) {
/*      */           
/*  896 */           handler.handle(responseTotal, o);
/*      */           
/*  898 */           if (this.api.isRawEvents()) {
/*  899 */             this.api.handleEvent((GenericEvent)new RawGatewayEvent((JDA)this.api, responseTotal, o));
/*      */           }
/*      */         } 
/*      */       } else {
/*      */         
/*  904 */         LOG.debug("Received event with unhandled body type JSON: {}", raw);
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*  909 */     DataObject content = raw.getObject("d");
/*  910 */     LOG.trace("{} -> {}", type, content);
/*      */     
/*  912 */     JDAImpl jda = (JDAImpl)getJDA(); try {
/*      */       long guildId;
/*      */       SocketHandler handler;
/*  915 */       switch (type) {
/*      */ 
/*      */         
/*      */         case "READY":
/*  919 */           this.reconnectTimeoutS = 2;
/*  920 */           this.api.setStatus(JDA.Status.LOADING_SUBSYSTEMS);
/*  921 */           this.processingReady = true;
/*  922 */           this.handleIdentifyRateLimit = false;
/*      */ 
/*      */ 
/*      */           
/*  926 */           ((SocketHandler)this.handlers.get("READY")).handle(responseTotal, raw);
/*  927 */           this.sessionId = content.getString("session_id");
/*      */           break;
/*      */         case "RESUMED":
/*  930 */           this.reconnectTimeoutS = 2;
/*  931 */           this.sentAuthInfo = true;
/*  932 */           if (!this.processingReady) {
/*      */             
/*  934 */             this.initiating = false;
/*  935 */             ready();
/*      */             
/*      */             break;
/*      */           } 
/*  939 */           LOG.debug("Resumed while still processing initial ready");
/*  940 */           jda.setStatus(JDA.Status.LOADING_SUBSYSTEMS);
/*      */           break;
/*      */         
/*      */         default:
/*  944 */           guildId = content.getLong("guild_id", 0L);
/*  945 */           if (this.api.isUnavailable(guildId) && !type.equals("GUILD_CREATE") && !type.equals("GUILD_DELETE")) {
/*      */             
/*  947 */             LOG.debug("Ignoring {} for unavailable guild with id {}. JSON: {}", new Object[] { type, Long.valueOf(guildId), content });
/*      */             break;
/*      */           } 
/*  950 */           handler = this.handlers.get(type);
/*  951 */           if (handler != null) {
/*  952 */             handler.handle(responseTotal, raw); break;
/*      */           } 
/*  954 */           LOG.debug("Unrecognized event:\n{}", raw);
/*      */           break;
/*      */       } 
/*  957 */       if (this.api.isRawEvents()) {
/*  958 */         this.api.handleEvent((GenericEvent)new RawGatewayEvent((JDA)this.api, responseTotal, raw));
/*      */       }
/*  960 */     } catch (ParsingException ex) {
/*      */       
/*  962 */       LOG.warn("Got an unexpected Json-parse error. Please redirect the following message to the devs:\n\tJDA {}\n\t{}\n\t{} -> {}", new Object[] { JDAInfo.VERSION, ex
/*  963 */             .getMessage(), type, content, ex });
/*      */     }
/*  965 */     catch (Exception ex) {
/*      */       
/*  967 */       LOG.error("Got an unexpected error. Please redirect the following message to the devs:\n\tJDA {}\n\t{} -> {}", new Object[] { JDAInfo.VERSION, type, content, ex });
/*      */     } 
/*      */ 
/*      */     
/*  971 */     if (responseTotal % 100L == 0L) {
/*  972 */       jda.getEventCache().timeout(responseTotal);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void onTextMessage(WebSocket websocket, byte[] data) {
/*  978 */     handleEvent(DataObject.fromJson(data));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onBinaryMessage(WebSocket websocket, byte[] binary) throws DataFormatException {
/*      */     DataObject message;
/*  986 */     synchronized (this.readLock) {
/*      */       
/*  988 */       message = handleBinary(binary);
/*      */     } 
/*  990 */     if (message != null)
/*  991 */       handleEvent(message); 
/*      */   }
/*      */   
/*      */   protected DataObject handleBinary(byte[] binary) throws DataFormatException {
/*      */     byte[] data;
/*  996 */     if (this.decompressor == null) {
/*      */       
/*  998 */       if (this.encoding == GatewayEncoding.ETF)
/*  999 */         return DataObject.fromETF(binary); 
/* 1000 */       throw new IllegalStateException("Cannot decompress binary message due to unknown compression algorithm: " + this.compression);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1006 */       data = this.decompressor.decompress(binary);
/* 1007 */       if (data == null) {
/* 1008 */         return null;
/*      */       }
/* 1010 */     } catch (DataFormatException e) {
/*      */       
/* 1012 */       close(4900, "MALFORMED_PACKAGE");
/* 1013 */       throw e;
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 1018 */       if (this.encoding == GatewayEncoding.ETF) {
/* 1019 */         return DataObject.fromETF(data);
/*      */       }
/* 1021 */       return DataObject.fromJson(data);
/*      */     }
/* 1023 */     catch (ParsingException e) {
/*      */       
/* 1025 */       String jsonString = "malformed";
/*      */       
/*      */       try {
/* 1028 */         jsonString = new String(data, StandardCharsets.UTF_8);
/*      */       }
/* 1030 */       catch (Exception exception) {}
/*      */       
/* 1032 */       LOG.error("Failed to parse json: {}", jsonString);
/* 1033 */       throw e;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
/* 1040 */     if (cause.getCause() instanceof java.net.SocketTimeoutException) {
/*      */       
/* 1042 */       LOG.debug("Socket timed out");
/*      */     }
/* 1044 */     else if (cause.getCause() instanceof IOException) {
/*      */       
/* 1046 */       LOG.debug("Encountered I/O error", (Throwable)cause);
/*      */     }
/*      */     else {
/*      */       
/* 1050 */       LOG.error("There was an error in the WebSocket connection", (Throwable)cause);
/* 1051 */       this.api.handleEvent((GenericEvent)new ExceptionEvent((JDA)this.api, (Throwable)cause, true));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onThreadCreated(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {
/* 1058 */     String identifier = this.api.getIdentifierString();
/* 1059 */     switch (threadType) {
/*      */       
/*      */       case DISCONNECT:
/* 1062 */         thread.setName(identifier + " MainWS-ConnectThread");
/*      */         return;
/*      */       case RECONNECT:
/* 1065 */         thread.setName(identifier + " MainWS-FinishThread");
/*      */         return;
/*      */       case null:
/* 1068 */         thread.setName(identifier + " MainWS-ReadThread");
/*      */         return;
/*      */       case null:
/* 1071 */         thread.setName(identifier + " MainWS-WriteThread");
/*      */         return;
/*      */     } 
/* 1074 */     thread.setName(identifier + " MainWS-" + threadType);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void maybeUnlock() {
/* 1080 */     if (this.queueLock.isHeldByCurrentThread()) {
/* 1081 */       this.queueLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void locked(String comment, Runnable task) {
/*      */     try {
/* 1088 */       if (!this.queueLock.tryLock() && !this.queueLock.tryLock(10L, TimeUnit.SECONDS))
/* 1089 */         throw new IllegalStateException("Could not acquire lock in reasonable timeframe! (10 seconds)"); 
/* 1090 */       task.run();
/*      */     }
/* 1092 */     catch (InterruptedException e) {
/*      */       
/* 1094 */       LOG.error(comment, e);
/*      */     }
/*      */     finally {
/*      */       
/* 1098 */       maybeUnlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected <T> T locked(String comment, Supplier<T> task) {
/*      */     try {
/* 1106 */       if (!this.queueLock.tryLock() && !this.queueLock.tryLock(10L, TimeUnit.SECONDS))
/* 1107 */         throw new IllegalStateException("Could not acquire lock in reasonable timeframe! (10 seconds)"); 
/* 1108 */       return task.get();
/*      */     }
/* 1110 */     catch (InterruptedException e) {
/*      */       
/* 1112 */       LOG.error(comment, e);
/* 1113 */       return null;
/*      */     }
/*      */     finally {
/*      */       
/* 1117 */       maybeUnlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void queueAudioReconnect(VoiceChannel channel) {
/* 1123 */     locked("There was an error queueing the audio reconnect", () -> {
/*      */           long guildId = channel.getGuild().getIdLong();
/*      */           ConnectionRequest request = (ConnectionRequest)this.queuedAudioConnections.get(guildId);
/*      */           if (request == null) {
/*      */             request = new ConnectionRequest(channel, ConnectionStage.RECONNECT);
/*      */             this.queuedAudioConnections.put(guildId, request);
/*      */           } else {
/*      */             request.setStage(ConnectionStage.RECONNECT);
/*      */           } 
/*      */           request.setChannel(channel);
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void queueAudioConnect(VoiceChannel channel) {
/* 1146 */     locked("There was an error queueing the audio connect", () -> {
/*      */           long guildId = channel.getGuild().getIdLong();
/*      */           ConnectionRequest request = (ConnectionRequest)this.queuedAudioConnections.get(guildId);
/*      */           if (request == null) {
/*      */             request = new ConnectionRequest(channel, ConnectionStage.CONNECT);
/*      */             this.queuedAudioConnections.put(guildId, request);
/*      */           } else if (request.getStage() == ConnectionStage.DISCONNECT) {
/*      */             request.setStage(ConnectionStage.RECONNECT);
/*      */           } 
/*      */           request.setChannel(channel);
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void queueAudioDisconnect(Guild guild) {
/* 1170 */     locked("There was an error queueing the audio disconnect", () -> {
/*      */           long guildId = guild.getIdLong();
/*      */           ConnectionRequest request = (ConnectionRequest)this.queuedAudioConnections.get(guildId);
/*      */           if (request == null) {
/*      */             this.queuedAudioConnections.put(guildId, new ConnectionRequest(guild));
/*      */           } else {
/*      */             request.setStage(ConnectionStage.DISCONNECT);
/*      */           } 
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConnectionRequest removeAudioConnection(long guildId) {
/* 1193 */     return locked("There was an error cleaning up audio connections for deleted guild", () -> (ConnectionRequest)this.queuedAudioConnections.remove(guildId));
/*      */   }
/*      */ 
/*      */   
/*      */   public ConnectionRequest updateAudioConnection(long guildId, VoiceChannel connectedChannel) {
/* 1198 */     return locked("There was an error updating the audio connection", () -> updateAudioConnection0(guildId, connectedChannel));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConnectionRequest updateAudioConnection0(long guildId, VoiceChannel connectedChannel) {
/* 1206 */     ConnectionRequest request = (ConnectionRequest)this.queuedAudioConnections.get(guildId);
/*      */     
/* 1208 */     if (request == null)
/* 1209 */       return null; 
/* 1210 */     ConnectionStage requestStage = request.getStage();
/* 1211 */     if (connectedChannel == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1217 */       switch (requestStage) {
/*      */         
/*      */         case DISCONNECT:
/* 1220 */           return (ConnectionRequest)this.queuedAudioConnections.remove(guildId);
/*      */         case RECONNECT:
/* 1222 */           request.setStage(ConnectionStage.CONNECT);
/* 1223 */           request.setNextAttemptEpoch(System.currentTimeMillis()); break;
/*      */       } 
/* 1225 */       return null;
/*      */     } 
/*      */     
/* 1228 */     if (requestStage == ConnectionStage.CONNECT)
/*      */     {
/*      */ 
/*      */       
/* 1232 */       if (request.getChannelId() == connectedChannel.getIdLong()) {
/* 1233 */         return (ConnectionRequest)this.queuedAudioConnections.remove(guildId);
/*      */       }
/*      */     }
/* 1236 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private SoftReference<ByteArrayOutputStream> newDecompressBuffer() {
/* 1241 */     return new SoftReference<>(new ByteArrayOutputStream(1024));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected ConnectionRequest getNextAudioConnectRequest() {
/* 1247 */     if (this.sessionId == null) {
/* 1248 */       return null;
/*      */     }
/* 1250 */     long now = System.currentTimeMillis();
/* 1251 */     AtomicReference<ConnectionRequest> request = new AtomicReference<>();
/* 1252 */     this.queuedAudioConnections.retainEntries((guildId, audioRequest) -> {
/*      */           if (audioRequest.getNextAttemptEpoch() < now) {
/*      */             Guild guild = this.api.getGuildById(guildId);
/*      */             
/*      */             if (guild == null) {
/*      */               GuildSetupController controller = this.api.getGuildSetupController();
/*      */               
/*      */               if (!controller.isKnown(guildId)) {
/*      */                 LOG.debug("Removing audio connection request because the guild has been removed. {}", audioRequest);
/*      */                 
/*      */                 return false;
/*      */               } 
/*      */               
/*      */               return true;
/*      */             } 
/*      */             
/*      */             ConnectionListener listener = guild.getAudioManager().getConnectionListener();
/*      */             
/*      */             if (audioRequest.getStage() != ConnectionStage.DISCONNECT) {
/*      */               VoiceChannel channel = guild.getVoiceChannelById(audioRequest.getChannelId());
/*      */               
/*      */               if (channel == null) {
/*      */                 if (listener != null) {
/*      */                   listener.onStatusChange(ConnectionStatus.DISCONNECTED_CHANNEL_DELETED);
/*      */                 }
/*      */                 
/*      */                 return false;
/*      */               } 
/*      */               
/*      */               if (!guild.getSelfMember().hasPermission((GuildChannel)channel, new Permission[] { Permission.VOICE_CONNECT })) {
/*      */                 if (listener != null) {
/*      */                   listener.onStatusChange(ConnectionStatus.DISCONNECTED_LOST_PERMISSION);
/*      */                 }
/*      */                 
/*      */                 return false;
/*      */               } 
/*      */             } 
/*      */             
/*      */             request.compareAndSet(null, audioRequest);
/*      */           } 
/*      */           
/*      */           return true;
/*      */         });
/*      */     
/* 1296 */     return request.get();
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, SocketHandler> getHandlers() {
/* 1301 */     return this.handlers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends SocketHandler> T getHandler(String type) {
/*      */     try {
/* 1309 */       return (T)this.handlers.get(type);
/*      */     }
/* 1311 */     catch (ClassCastException e) {
/*      */       
/* 1313 */       throw new IllegalStateException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setupHandlers() {
/* 1319 */     SocketHandler.NOPHandler nopHandler = new SocketHandler.NOPHandler(this.api);
/* 1320 */     this.handlers.put("APPLICATION_COMMAND_UPDATE", new ApplicationCommandUpdateHandler(this.api));
/* 1321 */     this.handlers.put("APPLICATION_COMMAND_DELETE", new ApplicationCommandDeleteHandler(this.api));
/* 1322 */     this.handlers.put("APPLICATION_COMMAND_CREATE", new ApplicationCommandCreateHandler(this.api));
/* 1323 */     this.handlers.put("CHANNEL_CREATE", new ChannelCreateHandler(this.api));
/* 1324 */     this.handlers.put("CHANNEL_DELETE", new ChannelDeleteHandler(this.api));
/* 1325 */     this.handlers.put("CHANNEL_UPDATE", new ChannelUpdateHandler(this.api));
/* 1326 */     this.handlers.put("GUILD_BAN_ADD", new GuildBanHandler(this.api, true));
/* 1327 */     this.handlers.put("GUILD_BAN_REMOVE", new GuildBanHandler(this.api, false));
/* 1328 */     this.handlers.put("GUILD_CREATE", new GuildCreateHandler(this.api));
/* 1329 */     this.handlers.put("GUILD_DELETE", new GuildDeleteHandler(this.api));
/* 1330 */     this.handlers.put("GUILD_EMOJIS_UPDATE", new GuildEmojisUpdateHandler(this.api));
/* 1331 */     this.handlers.put("GUILD_MEMBER_ADD", new GuildMemberAddHandler(this.api));
/* 1332 */     this.handlers.put("GUILD_MEMBER_REMOVE", new GuildMemberRemoveHandler(this.api));
/* 1333 */     this.handlers.put("GUILD_MEMBER_UPDATE", new GuildMemberUpdateHandler(this.api));
/* 1334 */     this.handlers.put("GUILD_MEMBERS_CHUNK", new GuildMembersChunkHandler(this.api));
/* 1335 */     this.handlers.put("GUILD_ROLE_CREATE", new GuildRoleCreateHandler(this.api));
/* 1336 */     this.handlers.put("GUILD_ROLE_DELETE", new GuildRoleDeleteHandler(this.api));
/* 1337 */     this.handlers.put("GUILD_ROLE_UPDATE", new GuildRoleUpdateHandler(this.api));
/* 1338 */     this.handlers.put("GUILD_SYNC", new GuildSyncHandler(this.api));
/* 1339 */     this.handlers.put("GUILD_UPDATE", new GuildUpdateHandler(this.api));
/* 1340 */     this.handlers.put("INTERACTION_CREATE", new InteractionCreateHandler(this.api));
/* 1341 */     this.handlers.put("INVITE_CREATE", new InviteCreateHandler(this.api));
/* 1342 */     this.handlers.put("INVITE_DELETE", new InviteDeleteHandler(this.api));
/* 1343 */     this.handlers.put("MESSAGE_CREATE", new MessageCreateHandler(this.api));
/* 1344 */     this.handlers.put("MESSAGE_DELETE", new MessageDeleteHandler(this.api));
/* 1345 */     this.handlers.put("MESSAGE_DELETE_BULK", new MessageBulkDeleteHandler(this.api));
/* 1346 */     this.handlers.put("MESSAGE_REACTION_ADD", new MessageReactionHandler(this.api, true));
/* 1347 */     this.handlers.put("MESSAGE_REACTION_REMOVE", new MessageReactionHandler(this.api, false));
/* 1348 */     this.handlers.put("MESSAGE_REACTION_REMOVE_ALL", new MessageReactionBulkRemoveHandler(this.api));
/* 1349 */     this.handlers.put("MESSAGE_REACTION_REMOVE_EMOJI", new MessageReactionClearEmoteHandler(this.api));
/* 1350 */     this.handlers.put("MESSAGE_UPDATE", new MessageUpdateHandler(this.api));
/* 1351 */     this.handlers.put("READY", new ReadyHandler(this.api));
/* 1352 */     this.handlers.put("STAGE_INSTANCE_CREATE", new StageInstanceCreateHandler(this.api));
/* 1353 */     this.handlers.put("STAGE_INSTANCE_DELETE", new StageInstanceDeleteHandler(this.api));
/* 1354 */     this.handlers.put("STAGE_INSTANCE_UPDATE", new StageInstanceUpdateHandler(this.api));
/* 1355 */     this.handlers.put("USER_UPDATE", new UserUpdateHandler(this.api));
/* 1356 */     this.handlers.put("VOICE_SERVER_UPDATE", new VoiceServerUpdateHandler(this.api));
/* 1357 */     this.handlers.put("VOICE_STATE_UPDATE", new VoiceStateUpdateHandler(this.api));
/* 1358 */     this.handlers.put("PRESENCE_UPDATE", new PresenceUpdateHandler(this.api));
/* 1359 */     this.handlers.put("TYPING_START", new TypingStartHandler(this.api));
/*      */ 
/*      */     
/* 1362 */     this.handlers.put("CHANNEL_PINS_ACK", nopHandler);
/* 1363 */     this.handlers.put("CHANNEL_PINS_UPDATE", nopHandler);
/* 1364 */     this.handlers.put("GUILD_INTEGRATIONS_UPDATE", nopHandler);
/* 1365 */     this.handlers.put("PRESENCES_REPLACE", nopHandler);
/* 1366 */     this.handlers.put("WEBHOOKS_UPDATE", nopHandler);
/*      */   }
/*      */ 
/*      */   
/*      */   protected abstract class ConnectNode
/*      */     implements SessionController.SessionConnectNode
/*      */   {
/*      */     @Nonnull
/*      */     public JDA getJDA() {
/* 1375 */       return (JDA)WebSocketClient.this.api;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @Nonnull
/*      */     public JDA.ShardInfo getShardInfo() {
/* 1382 */       return WebSocketClient.this.api.getShardInfo();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected class StartingNode
/*      */     extends ConnectNode
/*      */   {
/*      */     public boolean isReconnect() {
/* 1391 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void run(boolean isLast) throws InterruptedException {
/* 1397 */       if (WebSocketClient.this.shutdown)
/*      */         return; 
/* 1399 */       WebSocketClient.this.setupSendingThread();
/* 1400 */       WebSocketClient.this.connect();
/* 1401 */       if (isLast) {
/*      */         return;
/*      */       }
/*      */       try {
/* 1405 */         WebSocketClient.this.api.awaitStatus(JDA.Status.LOADING_SUBSYSTEMS, new JDA.Status[] { JDA.Status.RECONNECT_QUEUED });
/*      */       }
/* 1407 */       catch (IllegalStateException ex) {
/*      */         
/* 1409 */         WebSocketClient.this.close();
/* 1410 */         WebSocketClient.LOG.debug("Shutdown while trying to connect");
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1417 */       return Objects.hash(new Object[] { "C", getJDA() });
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1423 */       if (obj == this)
/* 1424 */         return true; 
/* 1425 */       if (!(obj instanceof StartingNode))
/* 1426 */         return false; 
/* 1427 */       StartingNode node = (StartingNode)obj;
/* 1428 */       return node.getJDA().equals(getJDA());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected class ReconnectNode
/*      */     extends ConnectNode
/*      */   {
/*      */     public boolean isReconnect() {
/* 1437 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void run(boolean isLast) throws InterruptedException {
/* 1443 */       if (WebSocketClient.this.shutdown)
/*      */         return; 
/* 1445 */       WebSocketClient.this.reconnect(true);
/* 1446 */       if (isLast) {
/*      */         return;
/*      */       }
/*      */       try {
/* 1450 */         WebSocketClient.this.api.awaitStatus(JDA.Status.LOADING_SUBSYSTEMS, new JDA.Status[] { JDA.Status.RECONNECT_QUEUED });
/*      */       }
/* 1452 */       catch (IllegalStateException ex) {
/*      */         
/* 1454 */         WebSocketClient.this.close();
/* 1455 */         WebSocketClient.LOG.debug("Shutdown while trying to reconnect");
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1462 */       return Objects.hash(new Object[] { "R", getJDA() });
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1468 */       if (obj == this)
/* 1469 */         return true; 
/* 1470 */       if (!(obj instanceof ReconnectNode))
/* 1471 */         return false; 
/* 1472 */       ReconnectNode node = (ReconnectNode)obj;
/* 1473 */       return node.getJDA().equals(getJDA());
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\WebSocketClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */