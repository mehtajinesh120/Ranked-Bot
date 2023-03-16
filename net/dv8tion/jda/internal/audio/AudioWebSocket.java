/*     */ package net.dv8tion.jda.internal.audio;
/*     */ 
/*     */ import com.neovisionaries.ws.client.ThreadType;
/*     */ import com.neovisionaries.ws.client.WebSocket;
/*     */ import com.neovisionaries.ws.client.WebSocketAdapter;
/*     */ import com.neovisionaries.ws.client.WebSocketException;
/*     */ import com.neovisionaries.ws.client.WebSocketFactory;
/*     */ import com.neovisionaries.ws.client.WebSocketFrame;
/*     */ import com.neovisionaries.ws.client.WebSocketListener;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.NoRouteToHostException;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.Consumer;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.audio.SpeakingMode;
/*     */ import net.dv8tion.jda.api.audio.hooks.ConnectionListener;
/*     */ import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ import net.dv8tion.jda.api.events.ExceptionEvent;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.managers.AudioManagerImpl;
/*     */ import net.dv8tion.jda.internal.utils.Helpers;
/*     */ import net.dv8tion.jda.internal.utils.IOUtil;
/*     */ import net.dv8tion.jda.internal.utils.JDALogger;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class AudioWebSocket
/*     */   extends WebSocketAdapter
/*     */ {
/*  53 */   public static final Logger LOG = JDALogger.getLog(AudioWebSocket.class);
/*     */   public static final int DISCORD_SECRET_KEY_LENGTH = 32;
/*  55 */   private static final byte[] UDP_KEEP_ALIVE = new byte[] { -55, 0, 0, 0, 0, 0, 0, 0, 0 };
/*     */   
/*     */   protected volatile AudioEncryption encryption;
/*     */   
/*     */   protected WebSocket socket;
/*     */   
/*     */   private final AudioConnection audioConnection;
/*     */   private final ConnectionListener listener;
/*     */   private final ScheduledExecutorService keepAlivePool;
/*     */   private final Guild guild;
/*     */   private final String sessionId;
/*     */   private final String token;
/*     */   private final String wssEndpoint;
/*  68 */   private volatile ConnectionStatus connectionStatus = ConnectionStatus.NOT_CONNECTED;
/*     */   
/*     */   private boolean ready = false;
/*     */   
/*     */   private boolean reconnecting = false;
/*     */   
/*     */   private boolean shouldReconnect;
/*     */   
/*     */   private int ssrc;
/*     */   private byte[] secretKey;
/*     */   private Future<?> keepAliveHandle;
/*     */   private InetSocketAddress address;
/*     */   private volatile boolean shutdown = false;
/*     */   
/*     */   protected AudioWebSocket(AudioConnection audioConnection, ConnectionListener listener, String endpoint, Guild guild, String sessionId, String token, boolean shouldReconnect) {
/*  83 */     this.audioConnection = audioConnection;
/*  84 */     this.listener = listener;
/*  85 */     this.guild = guild;
/*  86 */     this.sessionId = sessionId;
/*  87 */     this.token = token;
/*  88 */     this.shouldReconnect = shouldReconnect;
/*     */     
/*  90 */     this.keepAlivePool = getJDA().getAudioLifeCyclePool();
/*     */ 
/*     */     
/*  93 */     this.wssEndpoint = Helpers.format("wss://%s/?v=%d", new Object[] { endpoint, Integer.valueOf(4) });
/*     */     
/*  95 */     if (sessionId == null || sessionId.isEmpty())
/*  96 */       throw new IllegalArgumentException("Cannot create a voice connection using a null/empty sessionId!"); 
/*  97 */     if (token == null || token.isEmpty()) {
/*  98 */       throw new IllegalArgumentException("Cannot create a voice connection using a null/empty token!");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void send(String message) {
/* 105 */     LOG.trace("<- {}", message);
/* 106 */     this.socket.sendText(message);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void send(int op, Object data) {
/* 111 */     send(DataObject.empty()
/* 112 */         .put("op", Integer.valueOf(op))
/* 113 */         .put("d", data)
/* 114 */         .toString());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void startConnection() {
/* 119 */     if (!this.reconnecting && this.socket != null) {
/* 120 */       throw new IllegalStateException("Somehow, someway, this AudioWebSocket has already attempted to start a connection!");
/*     */     }
/*     */     
/*     */     try {
/* 124 */       WebSocketFactory socketFactory = new WebSocketFactory(getJDA().getWebSocketFactory());
/* 125 */       IOUtil.setServerName(socketFactory, this.wssEndpoint);
/* 126 */       if (socketFactory.getSocketTimeout() > 0) {
/* 127 */         socketFactory.setSocketTimeout(Math.max(1000, socketFactory.getSocketTimeout()));
/*     */       } else {
/* 129 */         socketFactory.setSocketTimeout(10000);
/* 130 */       }  this.socket = socketFactory.createSocket(this.wssEndpoint);
/* 131 */       this.socket.setDirectTextMessage(true);
/* 132 */       this.socket.addListener((WebSocketListener)this);
/* 133 */       changeStatus(ConnectionStatus.CONNECTING_AWAITING_WEBSOCKET_CONNECT);
/* 134 */       this.socket.connectAsynchronously();
/*     */     }
/* 136 */     catch (IOException e) {
/*     */       
/* 138 */       LOG.warn("Encountered IOException while attempting to connect to {}: {}\nClosing connection and attempting to reconnect.", this.wssEndpoint, e
/* 139 */           .getMessage());
/* 140 */       close(ConnectionStatus.ERROR_WEBSOCKET_UNABLE_TO_CONNECT);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void close(ConnectionStatus closeStatus) {
/* 147 */     if (this.shutdown)
/*     */       return; 
/* 149 */     locked(manager -> {
/*     */           if (this.shutdown) {
/*     */             return;
/*     */           }
/*     */           ConnectionStatus status = closeStatus;
/*     */           this.ready = false;
/*     */           this.shutdown = true;
/*     */           stopKeepAlive();
/*     */           if (this.audioConnection.udpSocket != null) {
/*     */             this.audioConnection.udpSocket.close();
/*     */           }
/*     */           if (this.socket != null) {
/*     */             this.socket.sendClose();
/*     */           }
/*     */           this.audioConnection.shutdown();
/*     */           VoiceChannel disconnectedChannel = manager.getConnectedChannel();
/*     */           manager.setAudioConnection(null);
/*     */           JDAImpl api = getJDA();
/*     */           if (status == ConnectionStatus.DISCONNECTED_KICKED_FROM_CHANNEL && (!api.getClient().isSession() || !api.getClient().isConnected())) {
/*     */             LOG.debug("Connection was closed due to session invalidate!");
/*     */             status = ConnectionStatus.ERROR_CANNOT_RESUME;
/*     */           } else if (status == ConnectionStatus.ERROR_LOST_CONNECTION || status == ConnectionStatus.DISCONNECTED_KICKED_FROM_CHANNEL) {
/*     */             Guild connGuild = api.getGuildById(this.guild.getIdLong());
/*     */             if (connGuild != null) {
/*     */               if (connGuild.getVoiceChannelById(this.audioConnection.getChannel().getIdLong()) == null) {
/*     */                 status = ConnectionStatus.DISCONNECTED_CHANNEL_DELETED;
/*     */               }
/*     */             }
/*     */           } 
/*     */           changeStatus(status);
/*     */           if (this.shouldReconnect && status.shouldReconnect() && status != ConnectionStatus.AUDIO_REGION_CHANGE) {
/*     */             if (disconnectedChannel == null) {
/*     */               LOG.debug("Cannot reconnect due to null voice channel");
/*     */               return;
/*     */             } 
/*     */             api.getDirectAudioController().reconnect(disconnectedChannel);
/*     */           } else if (status == ConnectionStatus.DISCONNECTED_REMOVED_FROM_GUILD) {
/*     */             api.getAudioManagersView().remove(this.guild.getIdLong());
/*     */           } else if (status != ConnectionStatus.AUDIO_REGION_CHANGE && status != ConnectionStatus.DISCONNECTED_KICKED_FROM_CHANNEL) {
/*     */             api.getDirectAudioController().disconnect(this.guild);
/*     */           } 
/*     */         });
/*     */   }
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
/*     */   protected void changeStatus(ConnectionStatus newStatus) {
/* 215 */     this.connectionStatus = newStatus;
/* 216 */     this.listener.onStatusChange(newStatus);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setAutoReconnect(boolean shouldReconnect) {
/* 221 */     this.shouldReconnect = shouldReconnect;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ConnectionStatus getConnectionStatus() {
/* 226 */     return this.connectionStatus;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InetSocketAddress getAddress() {
/* 231 */     return this.address;
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] getSecretKey() {
/* 236 */     return this.secretKey;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSSRC() {
/* 241 */     return this.ssrc;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isReady() {
/* 246 */     return this.ready;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onThreadStarted(WebSocket websocket, ThreadType threadType, Thread thread) {
/* 254 */     getJDA().setContext();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
/* 260 */     if (this.shutdown) {
/*     */ 
/*     */ 
/*     */       
/* 264 */       this.socket.sendClose(1000);
/*     */       
/*     */       return;
/*     */     } 
/* 268 */     if (this.reconnecting) {
/* 269 */       resume();
/*     */     } else {
/* 271 */       identify();
/* 272 */     }  changeStatus(ConnectionStatus.CONNECTING_AWAITING_AUTHENTICATION);
/* 273 */     this.audioConnection.prepareReady();
/* 274 */     this.reconnecting = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onTextMessage(WebSocket websocket, byte[] data) {
/*     */     try {
/* 282 */       handleEvent(DataObject.fromJson(data));
/*     */     }
/* 284 */     catch (Exception ex) {
/*     */       
/* 286 */       String message = "malformed";
/*     */       
/*     */       try {
/* 289 */         message = new String(data, StandardCharsets.UTF_8);
/*     */       }
/* 291 */       catch (Exception exception) {}
/* 292 */       LOG.error("Encountered exception trying to handle an event message: {}", message, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
/* 299 */     if (this.shutdown)
/*     */       return; 
/* 301 */     LOG.debug("The Audio connection was closed!\nBy remote? {}", Boolean.valueOf(closedByServer));
/* 302 */     if (serverCloseFrame != null) {
/*     */       
/* 304 */       LOG.debug("Reason: {}\nClose code: {}", serverCloseFrame.getCloseReason(), Integer.valueOf(serverCloseFrame.getCloseCode()));
/* 305 */       int code = serverCloseFrame.getCloseCode();
/* 306 */       VoiceCode.Close closeCode = VoiceCode.Close.from(code);
/* 307 */       switch (closeCode) {
/*     */         
/*     */         case CONNECT_THREAD:
/*     */         case FINISH_THREAD:
/*     */         case WRITING_THREAD:
/* 312 */           close(ConnectionStatus.ERROR_CANNOT_RESUME);
/*     */           return;
/*     */         case READING_THREAD:
/* 315 */           close(ConnectionStatus.DISCONNECTED_AUTHENTICATION_FAILURE);
/*     */           return;
/*     */         case null:
/* 318 */           close(ConnectionStatus.DISCONNECTED_KICKED_FROM_CHANNEL);
/*     */           return;
/*     */       } 
/* 321 */       reconnect();
/*     */       
/*     */       return;
/*     */     } 
/* 325 */     if (clientCloseFrame != null) {
/*     */       
/* 327 */       LOG.debug("ClientReason: {}\nClientCode: {}", clientCloseFrame.getCloseReason(), Integer.valueOf(clientCloseFrame.getCloseCode()));
/* 328 */       if (clientCloseFrame.getCloseCode() != 1000) {
/*     */ 
/*     */         
/* 331 */         reconnect();
/*     */         return;
/*     */       } 
/*     */     } 
/* 335 */     close(ConnectionStatus.NOT_CONNECTED);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onUnexpectedError(WebSocket websocket, WebSocketException cause) {
/* 341 */     handleCallbackError(websocket, (Throwable)cause);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleCallbackError(WebSocket websocket, Throwable cause) {
/* 347 */     LOG.error("There was some audio websocket error", cause);
/* 348 */     JDAImpl api = getJDA();
/* 349 */     api.handleEvent((GenericEvent)new ExceptionEvent((JDA)api, cause, true));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onThreadCreated(WebSocket websocket, ThreadType threadType, Thread thread) {
/* 355 */     String identifier = getJDA().getIdentifierString();
/* 356 */     String guildId = this.guild.getId();
/* 357 */     switch (threadType) {
/*     */       
/*     */       case CONNECT_THREAD:
/* 360 */         thread.setName(identifier + " AudioWS-ConnectThread (guildId: " + guildId + ')');
/*     */         return;
/*     */       case FINISH_THREAD:
/* 363 */         thread.setName(identifier + " AudioWS-FinishThread (guildId: " + guildId + ')');
/*     */         return;
/*     */       case WRITING_THREAD:
/* 366 */         thread.setName(identifier + " AudioWS-WriteThread (guildId: " + guildId + ')');
/*     */         return;
/*     */       case READING_THREAD:
/* 369 */         thread.setName(identifier + " AudioWS-ReadThread (guildId: " + guildId + ')');
/*     */         return;
/*     */     } 
/* 372 */     thread.setName(identifier + " AudioWS-" + threadType + " (guildId: " + guildId + ')');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onConnectError(WebSocket webSocket, WebSocketException e) {
/* 379 */     LOG.warn("Failed to establish websocket connection to {}: {} - {}\nClosing connection and attempting to reconnect.", new Object[] { this.wssEndpoint, e
/* 380 */           .getError(), e.getMessage() });
/* 381 */     close(ConnectionStatus.ERROR_WEBSOCKET_UNABLE_TO_CONNECT); } private void handleEvent(DataObject contentAll) { DataObject dataObject2, dataObject1; DataArray keyArray; long ping; DataObject content, payload; int interval, port, i; EnumSet<SpeakingMode> speaking; long userId; String ip; int ssrc;
/*     */     DataArray modes;
/*     */     long l1;
/*     */     InetSocketAddress externalIpAndPort;
/*     */     int tries;
/*     */     User user;
/*     */     DataObject object;
/* 388 */     int opCode = contentAll.getInt("op");
/*     */     
/* 390 */     switch (opCode) {
/*     */ 
/*     */       
/*     */       case 8:
/* 394 */         LOG.trace("-> HELLO {}", contentAll);
/* 395 */         dataObject2 = contentAll.getObject("d");
/* 396 */         interval = dataObject2.getInt("heartbeat_interval");
/* 397 */         stopKeepAlive();
/* 398 */         setupKeepAlive(interval);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 403 */         LOG.trace("-> READY {}", contentAll);
/* 404 */         dataObject1 = contentAll.getObject("d");
/* 405 */         this.ssrc = dataObject1.getInt("ssrc");
/* 406 */         port = dataObject1.getInt("port");
/* 407 */         ip = dataObject1.getString("ip");
/* 408 */         modes = dataObject1.getArray("modes");
/* 409 */         this.encryption = AudioEncryption.getPreferredMode(modes);
/* 410 */         if (this.encryption == null) {
/*     */           
/* 412 */           close(ConnectionStatus.ERROR_UNSUPPORTED_ENCRYPTION_MODES);
/* 413 */           LOG.error("None of the provided encryption modes are supported: {}", modes);
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 418 */         LOG.debug("Using encryption mode " + this.encryption.getKey());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 424 */         changeStatus(ConnectionStatus.CONNECTING_ATTEMPTING_UDP_DISCOVERY);
/* 425 */         tries = 0;
/*     */         
/*     */         do {
/* 428 */           externalIpAndPort = handleUdpDiscovery(new InetSocketAddress(ip, port), this.ssrc);
/* 429 */           tries++;
/* 430 */           if (externalIpAndPort == null && tries > 5) {
/*     */             
/* 432 */             close(ConnectionStatus.ERROR_UDP_UNABLE_TO_CONNECT);
/*     */             return;
/*     */           } 
/* 435 */         } while (externalIpAndPort == null);
/*     */ 
/*     */ 
/*     */         
/* 439 */         object = DataObject.empty().put("protocol", "udp").put("data", DataObject.empty()
/* 440 */             .put("address", externalIpAndPort.getHostString())
/* 441 */             .put("port", Integer.valueOf(externalIpAndPort.getPort()))
/* 442 */             .put("mode", this.encryption.getKey()));
/* 443 */         send(1, object);
/* 444 */         changeStatus(ConnectionStatus.CONNECTING_AWAITING_READY);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 449 */         LOG.trace("-> RESUMED {}", contentAll);
/* 450 */         LOG.debug("Successfully resumed session!");
/* 451 */         changeStatus(ConnectionStatus.CONNECTED);
/* 452 */         this.ready = true;
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 457 */         LOG.trace("-> SESSION_DESCRIPTION {}", contentAll);
/* 458 */         send(5, 
/* 459 */             DataObject.empty()
/* 460 */             .put("delay", Integer.valueOf(0))
/* 461 */             .put("speaking", Integer.valueOf(0))
/* 462 */             .put("ssrc", Integer.valueOf(this.ssrc)));
/*     */         
/* 464 */         keyArray = contentAll.getObject("d").getArray("secret_key");
/*     */         
/* 466 */         this.secretKey = new byte[32];
/* 467 */         for (i = 0; i < keyArray.length(); i++) {
/* 468 */           this.secretKey[i] = (byte)keyArray.getInt(i);
/*     */         }
/* 470 */         LOG.debug("Audio connection has finished connecting!");
/* 471 */         this.ready = true;
/* 472 */         changeStatus(ConnectionStatus.CONNECTED);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 477 */         LOG.trace("-> HEARTBEAT {}", contentAll);
/* 478 */         send(3, Long.valueOf(System.currentTimeMillis()));
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 483 */         LOG.trace("-> HEARTBEAT_ACK {}", contentAll);
/* 484 */         ping = System.currentTimeMillis() - contentAll.getLong("d");
/* 485 */         this.listener.onPing(ping);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 490 */         LOG.trace("-> USER_SPEAKING_UPDATE {}", contentAll);
/* 491 */         content = contentAll.getObject("d");
/* 492 */         speaking = SpeakingMode.getModes(content.getInt("speaking"));
/* 493 */         ssrc = content.getInt("ssrc");
/* 494 */         l1 = content.getLong("user_id");
/*     */         
/* 496 */         user = getUser(l1);
/* 497 */         if (user == null) {
/*     */ 
/*     */           
/* 500 */           LOG.trace("Got an Audio USER_SPEAKING_UPDATE for a non-existent User. JSON: {}", contentAll);
/*     */         }
/*     */         else {
/*     */           
/* 504 */           this.audioConnection.updateUserSSRC(ssrc, l1);
/* 505 */           this.listener.onUserSpeaking(user, speaking);
/*     */         } 
/*     */         return;
/*     */       
/*     */       case 13:
/* 510 */         LOG.trace("-> USER_DISCONNECT {}", contentAll);
/* 511 */         payload = contentAll.getObject("d");
/* 512 */         userId = payload.getLong("user_id");
/* 513 */         this.audioConnection.removeUserSSRC(userId);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 12:
/*     */       case 14:
/* 519 */         LOG.trace("-> OP {} {}", Integer.valueOf(opCode), contentAll);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 524 */     LOG.debug("Unknown Audio OP code.\n{}", contentAll); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void identify() {
/* 534 */     DataObject connectObj = DataObject.empty().put("server_id", this.guild.getId()).put("user_id", getJDA().getSelfUser().getId()).put("session_id", this.sessionId).put("token", this.token);
/* 535 */     send(0, connectObj);
/*     */   }
/*     */ 
/*     */   
/*     */   private void resume() {
/* 540 */     LOG.debug("Sending resume payload...");
/*     */ 
/*     */ 
/*     */     
/* 544 */     DataObject resumeObj = DataObject.empty().put("server_id", this.guild.getId()).put("session_id", this.sessionId).put("token", this.token);
/* 545 */     send(7, resumeObj);
/*     */   }
/*     */ 
/*     */   
/*     */   private JDAImpl getJDA() {
/* 550 */     return this.audioConnection.getJDA();
/*     */   }
/*     */ 
/*     */   
/*     */   private void locked(Consumer<AudioManagerImpl> consumer) {
/* 555 */     AudioManagerImpl manager = (AudioManagerImpl)this.guild.getAudioManager();
/* 556 */     MiscUtil.locked(manager.CONNECTION_LOCK, () -> consumer.accept(manager));
/*     */   }
/*     */ 
/*     */   
/*     */   private void reconnect() {
/* 561 */     if (this.shutdown)
/*     */       return; 
/* 563 */     locked(unused -> {
/*     */           if (this.shutdown) {
/*     */             return;
/*     */           }
/*     */           this.ready = false;
/*     */           this.reconnecting = true;
/*     */           changeStatus(ConnectionStatus.ERROR_LOST_CONNECTION);
/*     */           startConnection();
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InetSocketAddress handleUdpDiscovery(InetSocketAddress address, int ssrc) {
/*     */     try {
/* 581 */       if (this.audioConnection.udpSocket != null) {
/* 582 */         this.audioConnection.udpSocket.close();
/*     */       }
/* 584 */       this.audioConnection.udpSocket = new DatagramSocket();
/*     */ 
/*     */       
/* 587 */       ByteBuffer buffer = ByteBuffer.allocate(70);
/* 588 */       buffer.putShort((short)1);
/* 589 */       buffer.putShort((short)70);
/* 590 */       buffer.putInt(ssrc);
/*     */ 
/*     */ 
/*     */       
/* 594 */       DatagramPacket discoveryPacket = new DatagramPacket(buffer.array(), (buffer.array()).length, address);
/* 595 */       this.audioConnection.udpSocket.send(discoveryPacket);
/*     */ 
/*     */       
/* 598 */       DatagramPacket receivedPacket = new DatagramPacket(new byte[70], 70);
/* 599 */       this.audioConnection.udpSocket.setSoTimeout(1000);
/* 600 */       this.audioConnection.udpSocket.receive(receivedPacket);
/*     */ 
/*     */ 
/*     */       
/* 604 */       byte[] received = receivedPacket.getData();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 612 */       String ourIP = new String(received, 4, received.length - 6);
/*     */       
/* 614 */       ourIP = ourIP.trim();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 624 */       int ourPort = IOUtil.getShortBigEndian(received, received.length - 2) & 0xFFFF;
/* 625 */       this.address = address;
/* 626 */       return new InetSocketAddress(ourIP, ourPort);
/*     */     }
/* 628 */     catch (IOException e) {
/*     */ 
/*     */       
/* 631 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void stopKeepAlive() {
/* 637 */     if (this.keepAliveHandle != null)
/* 638 */       this.keepAliveHandle.cancel(true); 
/* 639 */     this.keepAliveHandle = null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setupKeepAlive(int keepAliveInterval) {
/* 644 */     if (this.keepAliveHandle != null) {
/* 645 */       LOG.error("Setting up a KeepAlive runnable while the previous one seems to still be active!!");
/*     */     }
/*     */     
/*     */     try {
/* 649 */       if (this.socket != null) {
/*     */         
/* 651 */         Socket rawSocket = this.socket.getSocket();
/* 652 */         if (rawSocket != null) {
/* 653 */           rawSocket.setSoTimeout(keepAliveInterval + 10000);
/*     */         }
/*     */       } 
/* 656 */     } catch (SocketException ex) {
/*     */       
/* 658 */       LOG.warn("Failed to setup timeout for socket", ex);
/*     */     } 
/*     */     
/* 661 */     Runnable keepAliveRunnable = () -> {
/*     */         getJDA().setContext();
/*     */         
/*     */         if (this.socket != null && this.socket.isOpen()) {
/*     */           send(3, Long.valueOf(System.currentTimeMillis()));
/*     */         }
/*     */         
/*     */         if (this.audioConnection.udpSocket != null && !this.audioConnection.udpSocket.isClosed()) {
/*     */           try {
/*     */             DatagramPacket keepAlivePacket = new DatagramPacket(UDP_KEEP_ALIVE, UDP_KEEP_ALIVE.length, this.address);
/*     */             
/*     */             this.audioConnection.udpSocket.send(keepAlivePacket);
/* 673 */           } catch (NoRouteToHostException e) {
/*     */             LOG.warn("Closing AudioConnection due to inability to ping audio packets.");
/*     */ 
/*     */             
/*     */             LOG.warn("Cannot send audio packet because JDA navigate the route to Discord.\nAre you sure you have internet connection? It is likely that you've lost connection.");
/*     */             
/*     */             close(ConnectionStatus.ERROR_LOST_CONNECTION);
/* 680 */           } catch (IOException e) {
/*     */             LOG.error("There was some error sending an audio keepalive packet", e);
/*     */           } 
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 689 */       this.keepAliveHandle = this.keepAlivePool.scheduleAtFixedRate(keepAliveRunnable, 0L, keepAliveInterval, TimeUnit.MILLISECONDS);
/*     */     }
/* 691 */     catch (RejectedExecutionException rejectedExecutionException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private User getUser(long userId) {
/* 697 */     return getJDA().getUserById(userId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finalize() {
/* 704 */     if (!this.shutdown) {
/*     */       
/* 706 */       LOG.error("Finalization hook of AudioWebSocket was triggered without properly shutting down");
/* 707 */       close(ConnectionStatus.NOT_CONNECTED);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\audio\AudioWebSocket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */