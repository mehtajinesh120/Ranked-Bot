/*      */ package net.dv8tion.jda.internal;
/*      */ import gnu.trove.set.TLongSet;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.EnumSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import javax.annotation.Nonnull;
/*      */ import javax.security.auth.login.LoginException;
/*      */ import net.dv8tion.jda.api.AccountType;
/*      */ import net.dv8tion.jda.api.GatewayEncoding;
/*      */ import net.dv8tion.jda.api.JDA;
/*      */ import net.dv8tion.jda.api.Permission;
/*      */ import net.dv8tion.jda.api.audio.factory.IAudioSendFactory;
/*      */ import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
/*      */ import net.dv8tion.jda.api.entities.AbstractChannel;
/*      */ import net.dv8tion.jda.api.entities.ApplicationInfo;
/*      */ import net.dv8tion.jda.api.entities.Category;
/*      */ import net.dv8tion.jda.api.entities.Guild;
/*      */ import net.dv8tion.jda.api.entities.Icon;
/*      */ import net.dv8tion.jda.api.entities.Member;
/*      */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*      */ import net.dv8tion.jda.api.entities.SelfUser;
/*      */ import net.dv8tion.jda.api.entities.StoreChannel;
/*      */ import net.dv8tion.jda.api.entities.TextChannel;
/*      */ import net.dv8tion.jda.api.entities.User;
/*      */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*      */ import net.dv8tion.jda.api.entities.Webhook;
/*      */ import net.dv8tion.jda.api.events.GenericEvent;
/*      */ import net.dv8tion.jda.api.exceptions.AccountTypeException;
/*      */ import net.dv8tion.jda.api.exceptions.RateLimitedException;
/*      */ import net.dv8tion.jda.api.hooks.IEventManager;
/*      */ import net.dv8tion.jda.api.interactions.commands.Command;
/*      */ import net.dv8tion.jda.api.interactions.commands.build.CommandData;
/*      */ import net.dv8tion.jda.api.managers.AudioManager;
/*      */ import net.dv8tion.jda.api.managers.DirectAudioController;
/*      */ import net.dv8tion.jda.api.managers.Presence;
/*      */ import net.dv8tion.jda.api.requests.GatewayIntent;
/*      */ import net.dv8tion.jda.api.requests.Request;
/*      */ import net.dv8tion.jda.api.requests.Response;
/*      */ import net.dv8tion.jda.api.requests.RestAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.CommandEditAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.GuildAction;
/*      */ import net.dv8tion.jda.api.sharding.ShardManager;
/*      */ import net.dv8tion.jda.api.utils.ChunkingFilter;
/*      */ import net.dv8tion.jda.api.utils.Compression;
/*      */ import net.dv8tion.jda.api.utils.MemberCachePolicy;
/*      */ import net.dv8tion.jda.api.utils.MiscUtil;
/*      */ import net.dv8tion.jda.api.utils.SessionController;
/*      */ import net.dv8tion.jda.api.utils.cache.CacheFlag;
/*      */ import net.dv8tion.jda.api.utils.cache.CacheView;
/*      */ import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
/*      */ import net.dv8tion.jda.api.utils.data.DataArray;
/*      */ import net.dv8tion.jda.api.utils.data.DataObject;
/*      */ import net.dv8tion.jda.internal.entities.EntityBuilder;
/*      */ import net.dv8tion.jda.internal.entities.UserImpl;
/*      */ import net.dv8tion.jda.internal.handle.EventCache;
/*      */ import net.dv8tion.jda.internal.handle.GuildSetupController;
/*      */ import net.dv8tion.jda.internal.hooks.EventManagerProxy;
/*      */ import net.dv8tion.jda.internal.managers.AudioManagerImpl;
/*      */ import net.dv8tion.jda.internal.managers.DirectAudioControllerImpl;
/*      */ import net.dv8tion.jda.internal.managers.PresenceImpl;
/*      */ import net.dv8tion.jda.internal.requests.DeferredRestAction;
/*      */ import net.dv8tion.jda.internal.requests.Requester;
/*      */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*      */ import net.dv8tion.jda.internal.requests.Route;
/*      */ import net.dv8tion.jda.internal.requests.WebSocketClient;
/*      */ import net.dv8tion.jda.internal.requests.restaction.CommandCreateActionImpl;
/*      */ import net.dv8tion.jda.internal.requests.restaction.GuildActionImpl;
/*      */ import net.dv8tion.jda.internal.utils.Checks;
/*      */ import net.dv8tion.jda.internal.utils.Helpers;
/*      */ import net.dv8tion.jda.internal.utils.UnlockHook;
/*      */ import net.dv8tion.jda.internal.utils.cache.AbstractCacheView;
/*      */ import net.dv8tion.jda.internal.utils.cache.SnowflakeCacheViewImpl;
/*      */ import net.dv8tion.jda.internal.utils.config.AuthorizationConfig;
/*      */ import net.dv8tion.jda.internal.utils.config.MetaConfig;
/*      */ import net.dv8tion.jda.internal.utils.config.SessionConfig;
/*      */ import net.dv8tion.jda.internal.utils.config.ThreadingConfig;
/*      */ import okhttp3.OkHttpClient;
/*      */ import org.slf4j.MDC;
/*      */ 
/*      */ public class JDAImpl implements JDA {
/*   93 */   public static final Logger LOG = JDALogger.getLog(JDA.class);
/*      */   
/*   95 */   protected final SnowflakeCacheViewImpl<User> userCache = new SnowflakeCacheViewImpl(User.class, User::getName);
/*   96 */   protected final SnowflakeCacheViewImpl<Guild> guildCache = new SnowflakeCacheViewImpl(Guild.class, Guild::getName);
/*   97 */   protected final SnowflakeCacheViewImpl<Category> categories = new SnowflakeCacheViewImpl(Category.class, AbstractChannel::getName);
/*   98 */   protected final SnowflakeCacheViewImpl<StoreChannel> storeChannelCache = new SnowflakeCacheViewImpl(StoreChannel.class, AbstractChannel::getName);
/*   99 */   protected final SnowflakeCacheViewImpl<TextChannel> textChannelCache = new SnowflakeCacheViewImpl(TextChannel.class, AbstractChannel::getName);
/*  100 */   protected final SnowflakeCacheViewImpl<VoiceChannel> voiceChannelCache = new SnowflakeCacheViewImpl(VoiceChannel.class, AbstractChannel::getName);
/*  101 */   protected final SnowflakeCacheViewImpl<PrivateChannel> privateChannelCache = new SnowflakeCacheViewImpl(PrivateChannel.class, AbstractChannel::getName); protected final AbstractCacheView<AudioManager> audioManagers; protected final PresenceImpl presence; protected final Thread shutdownHook; protected final EntityBuilder entityBuilder; protected final EventCache eventCache; protected final EventManagerProxy eventManager; protected final GuildSetupController guildSetupController; protected final DirectAudioControllerImpl audioController; protected final AuthorizationConfig authConfig; protected final ThreadingConfig threadConfig; protected final SessionConfig sessionConfig; protected final MetaConfig metaConfig; protected WebSocketClient client;
/*  102 */   protected final LinkedList<Long> privateChannelLRU = new LinkedList<>(); protected Requester requester; protected IAudioSendFactory audioSendFactory; protected JDA.Status status; protected SelfUser selfUser; protected JDA.ShardInfo shardInfo; protected long responseTotal; protected long gatewayPing; protected String gatewayUrl; protected ChunkingFilter chunkingFilter; protected String clientId; protected String requiredScopes; protected ShardManager shardManager; protected MemberCachePolicy memberCachePolicy;
/*      */   public JDAImpl(AuthorizationConfig authConfig, SessionConfig sessionConfig, ThreadingConfig threadConfig, MetaConfig metaConfig) {
/*  104 */     this.audioManagers = (AbstractCacheView<AudioManager>)new CacheView.SimpleCacheView(AudioManager.class, m -> m.getGuild().getName());
/*      */ 
/*      */ 
/*      */     
/*  108 */     this.entityBuilder = new EntityBuilder(this);
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
/*      */     
/*  122 */     this.audioSendFactory = (IAudioSendFactory)new DefaultSendFactory();
/*  123 */     this.status = JDA.Status.INITIALIZING;
/*      */ 
/*      */ 
/*      */     
/*  127 */     this.gatewayPing = -1L;
/*      */ 
/*      */ 
/*      */     
/*  131 */     this.clientId = null; this.requiredScopes = "bot";
/*  132 */     this.shardManager = null;
/*  133 */     this.memberCachePolicy = MemberCachePolicy.ALL;
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
/*  144 */     this.authConfig = authConfig;
/*  145 */     this.threadConfig = (threadConfig == null) ? ThreadingConfig.getDefault() : threadConfig;
/*  146 */     this.sessionConfig = (sessionConfig == null) ? SessionConfig.getDefault() : sessionConfig;
/*  147 */     this.metaConfig = (metaConfig == null) ? MetaConfig.getDefault() : metaConfig;
/*  148 */     this.shutdownHook = this.metaConfig.isUseShutdownHook() ? new Thread(this::shutdown, "JDA Shutdown Hook") : null;
/*  149 */     this.presence = new PresenceImpl(this);
/*  150 */     this.requester = new Requester(this);
/*  151 */     this.requester.setRetryOnTimeout(this.sessionConfig.isRetryOnTimeout());
/*  152 */     this.guildSetupController = new GuildSetupController(this);
/*  153 */     this.audioController = new DirectAudioControllerImpl(this);
/*  154 */     this.eventCache = new EventCache();
/*  155 */     this.eventManager = new EventManagerProxy((IEventManager)new InterfacedEventManager(), this.threadConfig.getEventPool());
/*      */   } public JDAImpl(AuthorizationConfig authConfig) {
/*      */     this(authConfig, (SessionConfig)null, (ThreadingConfig)null, (MetaConfig)null);
/*      */   }
/*      */   public void handleEvent(@Nonnull GenericEvent event) {
/*  160 */     this.eventManager.handle(event);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isRawEvents() {
/*  165 */     return this.sessionConfig.isRawEvents();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isRelativeRateLimit() {
/*  170 */     return this.sessionConfig.isRelativeRateLimit();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCacheFlagSet(CacheFlag flag) {
/*  175 */     return this.metaConfig.getCacheFlags().contains(flag);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isIntent(GatewayIntent intent) {
/*  180 */     int raw = intent.getRawValue();
/*  181 */     return ((this.client.getGatewayIntents() & raw) == raw);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getLargeThreshold() {
/*  186 */     return this.sessionConfig.getLargeThreshold();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxBufferSize() {
/*  191 */     return this.metaConfig.getMaxBufferSize();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean chunkGuild(long id) {
/*      */     try {
/*  198 */       return (isIntent(GatewayIntent.GUILD_MEMBERS) && this.chunkingFilter.filter(id));
/*      */     }
/*  200 */     catch (Exception e) {
/*      */       
/*  202 */       LOG.error("Uncaught exception from chunking filter", e);
/*  203 */       return true;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setChunkingFilter(ChunkingFilter filter) {
/*  209 */     this.chunkingFilter = filter;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean cacheMember(Member member) {
/*      */     try {
/*  216 */       return (member.getUser().equals(getSelfUser()) || 
/*  217 */         chunkGuild(member.getGuild().getIdLong()) || this.memberCachePolicy
/*  218 */         .cacheMember(member));
/*      */     }
/*  220 */     catch (Exception e) {
/*      */       
/*  222 */       LOG.error("Uncaught exception from member cache policy", e);
/*  223 */       return true;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMemberCachePolicy(MemberCachePolicy policy) {
/*  229 */     this.memberCachePolicy = policy;
/*      */   }
/*      */ 
/*      */   
/*      */   public SessionController getSessionController() {
/*  234 */     return this.sessionConfig.getSessionController();
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildSetupController getGuildSetupController() {
/*  239 */     return this.guildSetupController;
/*      */   }
/*      */ 
/*      */   
/*      */   public VoiceDispatchInterceptor getVoiceInterceptor() {
/*  244 */     return this.sessionConfig.getVoiceDispatchInterceptor();
/*      */   }
/*      */ 
/*      */   
/*      */   public void usedPrivateChannel(long id) {
/*  249 */     synchronized (this.privateChannelLRU) {
/*      */       
/*  251 */       this.privateChannelLRU.remove(Long.valueOf(id));
/*  252 */       this.privateChannelLRU.addFirst(Long.valueOf(id));
/*  253 */       if (this.privateChannelLRU.size() > 10) {
/*      */         
/*  255 */         long removed = ((Long)this.privateChannelLRU.removeLast()).longValue();
/*  256 */         this.privateChannelCache.remove(removed);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int login() throws LoginException {
/*  263 */     return login((String)null, (JDA.ShardInfo)null, Compression.ZLIB, true, GatewayIntent.ALL_INTENTS, GatewayEncoding.JSON);
/*      */   }
/*      */ 
/*      */   
/*      */   public int login(JDA.ShardInfo shardInfo, Compression compression, boolean validateToken, int intents, GatewayEncoding encoding) throws LoginException {
/*  268 */     return login((String)null, shardInfo, compression, validateToken, intents, encoding);
/*      */   }
/*      */ 
/*      */   
/*      */   public int login(String gatewayUrl, JDA.ShardInfo shardInfo, Compression compression, boolean validateToken, int intents, GatewayEncoding encoding) throws LoginException {
/*  273 */     this.shardInfo = shardInfo;
/*  274 */     this.threadConfig.init(this::getIdentifierString);
/*  275 */     this.requester.getRateLimiter().init();
/*  276 */     this.gatewayUrl = (gatewayUrl == null) ? getGateway() : gatewayUrl;
/*  277 */     Checks.notNull(this.gatewayUrl, "Gateway URL");
/*      */     
/*  279 */     String token = this.authConfig.getToken();
/*  280 */     setStatus(JDA.Status.LOGGING_IN);
/*  281 */     if (token == null || token.isEmpty()) {
/*  282 */       throw new LoginException("Provided token was null or empty!");
/*      */     }
/*  284 */     Map<String, String> previousContext = null;
/*  285 */     ConcurrentMap<String, String> contextMap = this.metaConfig.getMdcContextMap();
/*  286 */     if (contextMap != null) {
/*      */       
/*  288 */       if (shardInfo != null) {
/*      */         
/*  290 */         contextMap.put("jda.shard", shardInfo.getShardString());
/*  291 */         contextMap.put("jda.shard.id", String.valueOf(shardInfo.getShardId()));
/*  292 */         contextMap.put("jda.shard.total", String.valueOf(shardInfo.getShardTotal()));
/*      */       } 
/*      */       
/*  295 */       previousContext = MDC.getCopyOfContextMap();
/*  296 */       contextMap.forEach(MDC::put);
/*  297 */       this.requester.setContextReady(true);
/*      */     } 
/*  299 */     if (validateToken) {
/*      */       
/*  301 */       verifyToken();
/*  302 */       LOG.info("Login Successful!");
/*      */     } 
/*      */     
/*  305 */     this.client = new WebSocketClient(this, compression, intents, encoding);
/*      */     
/*  307 */     if (previousContext != null) {
/*  308 */       previousContext.forEach(MDC::put);
/*      */     }
/*  310 */     if (this.shutdownHook != null) {
/*  311 */       Runtime.getRuntime().addShutdownHook(this.shutdownHook);
/*      */     }
/*  313 */     return (shardInfo == null) ? -1 : shardInfo.getShardTotal();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getGateway() {
/*  318 */     return getSessionController().getGateway(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SessionController.ShardedGateway getShardedGateway() {
/*  325 */     return getSessionController().getShardedGateway(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public ConcurrentMap<String, String> getContextMap() {
/*  330 */     return (this.metaConfig.getMdcContextMap() == null) ? null : new ConcurrentHashMap<>(this.metaConfig.getMdcContextMap());
/*      */   }
/*      */ 
/*      */   
/*      */   public void setContext() {
/*  335 */     if (this.metaConfig.getMdcContextMap() != null) {
/*  336 */       this.metaConfig.getMdcContextMap().forEach(MDC::put);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setToken(String token) {
/*  341 */     this.authConfig.setToken(token);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStatus(JDA.Status status) {
/*  347 */     synchronized (this.status) {
/*      */       
/*  349 */       JDA.Status oldStatus = this.status;
/*  350 */       this.status = status;
/*      */       
/*  352 */       handleEvent((GenericEvent)new StatusChangeEvent(this, status, oldStatus));
/*      */     } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void verifyToken() throws LoginException {
/*  372 */     RestActionImpl<DataObject> login = (new RestActionImpl<DataObject>(this, Route.Self.GET_SELF.compile(new String[0])) { public void handleResponse(Response response, Request<DataObject> request) { if (response.isOk()) { request.onSuccess(response.getObject()); } else if (response.isRateLimit()) { request.onFailure((Throwable)new RateLimitedException(request.getRoute(), response.retryAfter)); } else if (response.code == 401) { request.onSuccess(null); } else { request.onFailure(response); }  } }).priority();
/*      */     
/*  374 */     DataObject userResponse = (DataObject)login.complete();
/*  375 */     if (userResponse != null) {
/*      */       
/*  377 */       getEntityBuilder().createSelfUser(userResponse);
/*      */       return;
/*      */     } 
/*  380 */     shutdownNow();
/*  381 */     throw new LoginException("The provided token is invalid!");
/*      */   }
/*      */ 
/*      */   
/*      */   public AuthorizationConfig getAuthorizationConfig() {
/*  386 */     return this.authConfig;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public String getToken() {
/*  393 */     return this.authConfig.getToken();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBulkDeleteSplittingEnabled() {
/*  400 */     return this.sessionConfig.isBulkDeleteSplittingEnabled();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoReconnect(boolean autoReconnect) {
/*  406 */     this.sessionConfig.setAutoReconnect(autoReconnect);
/*  407 */     WebSocketClient client = getClient();
/*  408 */     if (client != null) {
/*  409 */       client.setAutoReconnect(autoReconnect);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRequestTimeoutRetry(boolean retryOnTimeout) {
/*  415 */     this.requester.setRetryOnTimeout(retryOnTimeout);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAutoReconnect() {
/*  421 */     return this.sessionConfig.isAutoReconnect();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDA.Status getStatus() {
/*  428 */     return this.status;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public EnumSet<GatewayIntent> getGatewayIntents() {
/*  435 */     return GatewayIntent.getIntents(this.client.getGatewayIntents());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public EnumSet<CacheFlag> getCacheFlags() {
/*  442 */     return Helpers.copyEnumSet(CacheFlag.class, this.metaConfig.getCacheFlags());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean unloadUser(long userId) {
/*  448 */     if (userId == this.selfUser.getIdLong())
/*  449 */       return false; 
/*  450 */     User user = getUserById(userId);
/*  451 */     if (user == null) {
/*  452 */       return false;
/*      */     }
/*      */     
/*  455 */     return 
/*      */       
/*  457 */       (getGuildCache().stream().filter(guild -> guild.unloadMember(userId)).count() > 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getGatewayPing() {
/*  463 */     return this.gatewayPing;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDA awaitStatus(@Nonnull JDA.Status status, @Nonnull JDA.Status... failOn) throws InterruptedException {
/*  470 */     Checks.notNull(status, "Status");
/*  471 */     Checks.check(status.isInit(), "Cannot await the status %s as it is not part of the login cycle!", status);
/*  472 */     if (getStatus() == JDA.Status.CONNECTED)
/*  473 */       return this; 
/*  474 */     List<JDA.Status> failStatus = Arrays.asList(failOn);
/*  475 */     while (!getStatus().isInit() || 
/*  476 */       getStatus().ordinal() < status.ordinal()) {
/*      */       
/*  478 */       if (getStatus() == JDA.Status.SHUTDOWN)
/*  479 */         throw new IllegalStateException("Was shutdown trying to await status"); 
/*  480 */       if (failStatus.contains(getStatus()))
/*  481 */         return this; 
/*  482 */       Thread.sleep(50L);
/*      */     } 
/*  484 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int cancelRequests() {
/*  490 */     return this.requester.getRateLimiter().cancelRequests();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public ScheduledExecutorService getRateLimitPool() {
/*  497 */     return this.threadConfig.getRateLimitPool();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public ScheduledExecutorService getGatewayPool() {
/*  504 */     return this.threadConfig.getGatewayPool();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public ExecutorService getCallbackPool() {
/*  511 */     return this.threadConfig.getCallbackPool();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public OkHttpClient getHttpClient() {
/*  519 */     return this.sessionConfig.getHttpClient();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public DirectAudioControllerImpl getDirectAudioController() {
/*  526 */     if (!isIntent(GatewayIntent.GUILD_VOICE_STATES))
/*  527 */       throw new IllegalStateException("Cannot use audio features with disabled GUILD_VOICE_STATES intent!"); 
/*  528 */     return this.audioController;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public List<Guild> getMutualGuilds(@Nonnull User... users) {
/*  535 */     Checks.notNull(users, "users");
/*  536 */     return getMutualGuilds(Arrays.asList(users));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public List<Guild> getMutualGuilds(@Nonnull Collection<User> users) {
/*  543 */     Checks.notNull(users, "users");
/*  544 */     for (User u : users)
/*  545 */       Checks.notNull(u, "All users"); 
/*  546 */     return Collections.unmodifiableList((List<? extends Guild>)getGuilds().stream()
/*  547 */         .filter(guild -> { Objects.requireNonNull(guild); return users.stream().allMatch(guild::isMember);
/*  548 */           }).collect(Collectors.toList()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<User> retrieveUserById(@Nonnull String id) {
/*  555 */     return retrieveUserById(MiscUtil.parseSnowflake(id));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<User> retrieveUserById(long id, boolean update) {
/*  562 */     if (id == getSelfUser().getIdLong()) {
/*  563 */       return (RestAction<User>)new CompletedRestAction(this, getSelfUser());
/*      */     }
/*  565 */     AccountTypeException.check(getAccountType(), AccountType.BOT);
/*  566 */     return (RestAction<User>)new DeferredRestAction(this, User.class, () -> 
/*  567 */         (!update || isIntent(GatewayIntent.GUILD_MEMBERS) || isIntent(GatewayIntent.GUILD_PRESENCES)) ? getUserById(id) : null, () -> {
/*      */           Route.CompiledRoute route = Route.Users.GET_USER.compile(new String[] { Long.toUnsignedString(id) });
/*      */           return new RestActionImpl(this, route, ());
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public CacheView<AudioManager> getAudioManagerCache() {
/*  579 */     return (CacheView<AudioManager>)this.audioManagers;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public SnowflakeCacheView<Guild> getGuildCache() {
/*  586 */     return (SnowflakeCacheView<Guild>)this.guildCache;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Set<String> getUnavailableGuilds() {
/*  593 */     TLongSet unavailableGuilds = this.guildSetupController.getUnavailableGuilds();
/*  594 */     Set<String> copy = new HashSet<>();
/*  595 */     unavailableGuilds.forEach(id -> copy.add(Long.toUnsignedString(id)));
/*  596 */     return copy;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUnavailable(long guildId) {
/*  602 */     return this.guildSetupController.isUnavailable(guildId);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public SnowflakeCacheView<Role> getRoleCache() {
/*  609 */     return CacheView.allSnowflakes(() -> this.guildCache.stream().map(Guild::getRoleCache));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public SnowflakeCacheView<Emote> getEmoteCache() {
/*  616 */     return CacheView.allSnowflakes(() -> this.guildCache.stream().map(Guild::getEmoteCache));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public SnowflakeCacheView<Category> getCategoryCache() {
/*  623 */     return (SnowflakeCacheView<Category>)this.categories;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public SnowflakeCacheView<StoreChannel> getStoreChannelCache() {
/*  630 */     return (SnowflakeCacheView<StoreChannel>)this.storeChannelCache;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public SnowflakeCacheView<TextChannel> getTextChannelCache() {
/*  637 */     return (SnowflakeCacheView<TextChannel>)this.textChannelCache;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public SnowflakeCacheView<VoiceChannel> getVoiceChannelCache() {
/*  644 */     return (SnowflakeCacheView<VoiceChannel>)this.voiceChannelCache;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public SnowflakeCacheView<PrivateChannel> getPrivateChannelCache() {
/*  651 */     return (SnowflakeCacheView<PrivateChannel>)this.privateChannelCache;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public PrivateChannel getPrivateChannelById(@Nonnull String id) {
/*  657 */     return getPrivateChannelById(MiscUtil.parseSnowflake(id));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public PrivateChannel getPrivateChannelById(long id) {
/*  663 */     PrivateChannel channel = super.getPrivateChannelById(id);
/*  664 */     if (channel != null)
/*  665 */       usedPrivateChannel(id); 
/*  666 */     return channel;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<PrivateChannel> openPrivateChannelById(long userId) {
/*  673 */     if (this.selfUser != null && userId == this.selfUser.getIdLong())
/*  674 */       throw new UnsupportedOperationException("Cannot open private channel with yourself!"); 
/*  675 */     return (RestAction<PrivateChannel>)new DeferredRestAction(this, PrivateChannel.class, () -> {
/*      */           User user = getUserById(userId);
/*      */           return (user instanceof UserImpl) ? ((UserImpl)user).getPrivateChannel() : null;
/*      */         }() -> {
/*      */           Route.CompiledRoute route = Route.Self.CREATE_PRIVATE_CHANNEL.compile(new String[0]);
/*      */           DataObject body = DataObject.empty().put("recipient_id", Long.valueOf(userId));
/*      */           return new RestActionImpl(this, route, body, ());
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public SnowflakeCacheView<User> getUserCache() {
/*  692 */     return (SnowflakeCacheView<User>)this.userCache;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasSelfUser() {
/*  697 */     return (this.selfUser != null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public SelfUser getSelfUser() {
/*  704 */     if (this.selfUser == null)
/*  705 */       throw new IllegalStateException("Session is not yet ready!"); 
/*  706 */     return this.selfUser;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void shutdownNow() {
/*  712 */     this.requester.shutdown();
/*  713 */     shutdown();
/*  714 */     this.threadConfig.shutdownNow();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void shutdown() {
/*  720 */     if (this.status == JDA.Status.SHUTDOWN || this.status == JDA.Status.SHUTTING_DOWN) {
/*      */       return;
/*      */     }
/*  723 */     setStatus(JDA.Status.SHUTTING_DOWN);
/*  724 */     shutdownInternals();
/*      */     
/*  726 */     WebSocketClient client = getClient();
/*  727 */     if (client != null) {
/*      */       
/*  729 */       client.getChunkManager().shutdown();
/*  730 */       client.shutdown();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void shutdownInternals() {
/*  736 */     if (this.status == JDA.Status.SHUTDOWN) {
/*      */       return;
/*      */     }
/*  739 */     closeAudioConnections();
/*  740 */     this.guildSetupController.close();
/*      */ 
/*      */     
/*  743 */     if (this.requester.stop())
/*  744 */       shutdownRequester(); 
/*  745 */     this.threadConfig.shutdown();
/*      */     
/*  747 */     if (this.shutdownHook != null) {
/*      */       
/*      */       try {
/*      */         
/*  751 */         Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
/*      */       }
/*  753 */       catch (Exception exception) {}
/*      */     }
/*      */     
/*  756 */     setStatus(JDA.Status.SHUTDOWN);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void shutdownRequester() {
/*  762 */     this.requester.shutdown();
/*  763 */     this.threadConfig.shutdownRequester();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void closeAudioConnections() {
/*  770 */     Objects.requireNonNull(AudioManagerImpl.class); getAudioManagerCache().stream().map(AudioManagerImpl.class::cast)
/*  771 */       .forEach(m -> m.closeAudioConnection(ConnectionStatus.SHUTTING_DOWN));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getResponseTotal() {
/*  777 */     return this.responseTotal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxReconnectDelay() {
/*  783 */     return this.sessionConfig.getMaxReconnectDelay();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDA.ShardInfo getShardInfo() {
/*  790 */     return (this.shardInfo == null) ? JDA.ShardInfo.SINGLE : this.shardInfo;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Presence getPresence() {
/*  797 */     return (Presence)this.presence;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public IEventManager getEventManager() {
/*  804 */     return this.eventManager.getSubject();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AccountType getAccountType() {
/*  811 */     return this.authConfig.getAccountType();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEventManager(IEventManager eventManager) {
/*  817 */     this.eventManager.setSubject(eventManager);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addEventListener(@Nonnull Object... listeners) {
/*  823 */     Checks.noneNull(listeners, "listeners");
/*      */     
/*  825 */     for (Object listener : listeners) {
/*  826 */       this.eventManager.register(listener);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeEventListener(@Nonnull Object... listeners) {
/*  832 */     Checks.noneNull(listeners, "listeners");
/*      */     
/*  834 */     for (Object listener : listeners) {
/*  835 */       this.eventManager.unregister(listener);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public List<Object> getRegisteredListeners() {
/*  842 */     return this.eventManager.getRegisteredListeners();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<List<Command>> retrieveCommands() {
/*  849 */     Route.CompiledRoute route = Route.Interactions.GET_COMMANDS.compile(new String[] { getSelfUser().getApplicationId() });
/*  850 */     return (RestAction<List<Command>>)new RestActionImpl(this, route, (response, request) -> (List)response.getArray().stream(DataArray::getObject).map(()).collect(Collectors.toList()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Command> retrieveCommandById(@Nonnull String id) {
/*  862 */     Checks.isSnowflake(id);
/*  863 */     Route.CompiledRoute route = Route.Interactions.GET_COMMAND.compile(new String[] { getSelfUser().getApplicationId(), id });
/*  864 */     return (RestAction<Command>)new RestActionImpl(this, route, (response, request) -> new Command(this, null, response.getObject()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public CommandCreateAction upsertCommand(@Nonnull CommandData command) {
/*  871 */     Checks.notNull(command, "CommandData");
/*  872 */     return (CommandCreateAction)new CommandCreateActionImpl(this, command);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public CommandListUpdateAction updateCommands() {
/*  879 */     Route.CompiledRoute route = Route.Interactions.UPDATE_COMMANDS.compile(new String[] { getSelfUser().getApplicationId() });
/*  880 */     return (CommandListUpdateAction)new CommandListUpdateActionImpl(this, null, route);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public CommandEditAction editCommandById(@Nonnull String id) {
/*  887 */     Checks.isSnowflake(id);
/*  888 */     return (CommandEditAction)new CommandEditActionImpl(this, id);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Void> deleteCommandById(@Nonnull String commandId) {
/*  895 */     Checks.isSnowflake(commandId);
/*  896 */     Route.CompiledRoute route = Route.Interactions.DELETE_COMMAND.compile(new String[] { getSelfUser().getApplicationId(), commandId });
/*  897 */     return (RestAction<Void>)new RestActionImpl(this, route);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public GuildActionImpl createGuild(@Nonnull String name) {
/*  904 */     if (this.guildCache.size() >= 10L)
/*  905 */       throw new IllegalStateException("Cannot create a Guild with a Bot in 10 or more guilds!"); 
/*  906 */     return new GuildActionImpl(this, name);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Void> createGuildFromTemplate(@Nonnull String code, @Nonnull String name, Icon icon) {
/*  913 */     if (this.guildCache.size() >= 10L) {
/*  914 */       throw new IllegalStateException("Cannot create a Guild with a Bot in 10 or more guilds!");
/*      */     }
/*  916 */     Checks.notBlank(code, "Template code");
/*  917 */     Checks.notBlank(name, "Name");
/*  918 */     name = name.trim();
/*  919 */     Checks.notLonger(name, 100, "Name");
/*      */     
/*  921 */     Route.CompiledRoute route = Route.Templates.CREATE_GUILD_FROM_TEMPLATE.compile(new String[] { code });
/*      */     
/*  923 */     DataObject object = DataObject.empty();
/*  924 */     object.put("name", name);
/*  925 */     if (icon != null) {
/*  926 */       object.put("icon", icon.getEncoding());
/*      */     }
/*  928 */     return (RestAction<Void>)new RestActionImpl(this, route, object);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Webhook> retrieveWebhookById(@Nonnull String webhookId) {
/*  935 */     Checks.isSnowflake(webhookId, "Webhook ID");
/*      */     
/*  937 */     Route.CompiledRoute route = Route.Webhooks.GET_WEBHOOK.compile(new String[] { webhookId });
/*      */     
/*  939 */     return (RestAction<Webhook>)new RestActionImpl(this, route, (response, request) -> {
/*      */           DataObject object = response.getObject();
/*      */           EntityBuilder builder = getEntityBuilder();
/*      */           return (Webhook)builder.createWebhook(object);
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<ApplicationInfo> retrieveApplicationInfo() {
/*  951 */     AccountTypeException.check(getAccountType(), AccountType.BOT);
/*  952 */     Route.CompiledRoute route = Route.Applications.GET_BOT_APPLICATION.compile(new String[0]);
/*  953 */     return (RestAction<ApplicationInfo>)new RestActionImpl(this, route, (response, request) -> {
/*      */           ApplicationInfo info = getEntityBuilder().createApplicationInfo(response.getObject());
/*      */           this.clientId = info.getId();
/*      */           return info;
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDA setRequiredScopes(@Nonnull Collection<String> scopes) {
/*  965 */     Checks.noneNull(scopes, "Scopes");
/*  966 */     this.requiredScopes = String.join("+", (Iterable)scopes);
/*  967 */     if (!this.requiredScopes.contains("bot"))
/*      */     {
/*  969 */       if (this.requiredScopes.isEmpty()) {
/*  970 */         this.requiredScopes = "bot";
/*      */       } else {
/*  972 */         this.requiredScopes += "+bot";
/*      */       }  } 
/*  974 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public String getInviteUrl(Permission... permissions) {
/*  981 */     StringBuilder builder = buildBaseInviteUrl();
/*  982 */     if (permissions != null && permissions.length > 0)
/*  983 */       builder.append("&permissions=").append(Permission.getRaw(permissions)); 
/*  984 */     return builder.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public String getInviteUrl(Collection<Permission> permissions) {
/*  991 */     StringBuilder builder = buildBaseInviteUrl();
/*  992 */     if (permissions != null && !permissions.isEmpty())
/*  993 */       builder.append("&permissions=").append(Permission.getRaw(permissions)); 
/*  994 */     return builder.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private StringBuilder buildBaseInviteUrl() {
/*  999 */     if (this.clientId == null)
/*      */     {
/* 1001 */       if (this.selfUser != null) {
/* 1002 */         this.clientId = this.selfUser.getApplicationId();
/*      */       } else {
/* 1004 */         retrieveApplicationInfo().complete();
/*      */       }  } 
/* 1006 */     StringBuilder builder = new StringBuilder("https://discord.com/oauth2/authorize?client_id=");
/* 1007 */     builder.append(this.clientId);
/* 1008 */     builder.append("&scope=").append(this.requiredScopes);
/* 1009 */     return builder;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setShardManager(ShardManager shardManager) {
/* 1014 */     this.shardManager = shardManager;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ShardManager getShardManager() {
/* 1020 */     return this.shardManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityBuilder getEntityBuilder() {
/* 1025 */     return this.entityBuilder;
/*      */   }
/*      */ 
/*      */   
/*      */   public IAudioSendFactory getAudioSendFactory() {
/* 1030 */     return this.audioSendFactory;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAudioSendFactory(IAudioSendFactory factory) {
/* 1035 */     Checks.notNull(factory, "Provided IAudioSendFactory");
/* 1036 */     this.audioSendFactory = factory;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setGatewayPing(long ping) {
/* 1041 */     long oldPing = this.gatewayPing;
/* 1042 */     this.gatewayPing = ping;
/* 1043 */     handleEvent((GenericEvent)new GatewayPingEvent(this, oldPing));
/*      */   }
/*      */ 
/*      */   
/*      */   public Requester getRequester() {
/* 1048 */     return this.requester;
/*      */   }
/*      */ 
/*      */   
/*      */   public WebSocketFactory getWebSocketFactory() {
/* 1053 */     return this.sessionConfig.getWebSocketFactory();
/*      */   }
/*      */ 
/*      */   
/*      */   public WebSocketClient getClient() {
/* 1058 */     return this.client;
/*      */   }
/*      */ 
/*      */   
/*      */   public SnowflakeCacheViewImpl<User> getUsersView() {
/* 1063 */     return this.userCache;
/*      */   }
/*      */ 
/*      */   
/*      */   public SnowflakeCacheViewImpl<Guild> getGuildsView() {
/* 1068 */     return this.guildCache;
/*      */   }
/*      */ 
/*      */   
/*      */   public SnowflakeCacheViewImpl<Category> getCategoriesView() {
/* 1073 */     return this.categories;
/*      */   }
/*      */ 
/*      */   
/*      */   public SnowflakeCacheViewImpl<StoreChannel> getStoreChannelsView() {
/* 1078 */     return this.storeChannelCache;
/*      */   }
/*      */ 
/*      */   
/*      */   public SnowflakeCacheViewImpl<TextChannel> getTextChannelsView() {
/* 1083 */     return this.textChannelCache;
/*      */   }
/*      */ 
/*      */   
/*      */   public SnowflakeCacheViewImpl<VoiceChannel> getVoiceChannelsView() {
/* 1088 */     return this.voiceChannelCache;
/*      */   }
/*      */ 
/*      */   
/*      */   public SnowflakeCacheViewImpl<PrivateChannel> getPrivateChannelsView() {
/* 1093 */     return this.privateChannelCache;
/*      */   }
/*      */ 
/*      */   
/*      */   public AbstractCacheView<AudioManager> getAudioManagersView() {
/* 1098 */     return this.audioManagers;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSelfUser(SelfUser selfUser) {
/* 1103 */     UnlockHook hook = this.userCache.writeLock();
/*      */     
/* 1105 */     try { this.userCache.getMap().put(selfUser.getIdLong(), selfUser);
/* 1106 */       if (hook != null) hook.close();  } catch (Throwable throwable) { if (hook != null)
/* 1107 */         try { hook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  this.selfUser = selfUser;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setResponseTotal(int responseTotal) {
/* 1112 */     this.responseTotal = responseTotal;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getIdentifierString() {
/* 1117 */     if (this.shardInfo != null) {
/* 1118 */       return "JDA " + this.shardInfo.getShardString();
/*      */     }
/* 1120 */     return "JDA";
/*      */   }
/*      */ 
/*      */   
/*      */   public EventCache getEventCache() {
/* 1125 */     return this.eventCache;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getGatewayUrl() {
/* 1130 */     if (this.gatewayUrl == null)
/* 1131 */       return this.gatewayUrl = getGateway(); 
/* 1132 */     return this.gatewayUrl;
/*      */   }
/*      */ 
/*      */   
/*      */   public void resetGatewayUrl() {
/* 1137 */     this.gatewayUrl = null;
/*      */   }
/*      */ 
/*      */   
/*      */   public ScheduledExecutorService getAudioLifeCyclePool() {
/* 1142 */     return this.threadConfig.getAudioPool(this::getIdentifierString);
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\JDAImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */