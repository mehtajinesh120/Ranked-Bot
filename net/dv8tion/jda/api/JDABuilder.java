/*      */ package net.dv8tion.jda.api;
/*      */ 
/*      */ import com.neovisionaries.ws.client.WebSocketFactory;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.stream.Collectors;
/*      */ import javax.annotation.CheckReturnValue;
/*      */ import javax.annotation.Nonnull;
/*      */ import javax.annotation.Nullable;
/*      */ import javax.security.auth.login.LoginException;
/*      */ import net.dv8tion.jda.annotations.DeprecatedSince;
/*      */ import net.dv8tion.jda.annotations.ForRemoval;
/*      */ import net.dv8tion.jda.annotations.ReplaceWith;
/*      */ import net.dv8tion.jda.api.audio.factory.IAudioSendFactory;
/*      */ import net.dv8tion.jda.api.entities.Activity;
/*      */ import net.dv8tion.jda.api.hooks.IEventManager;
/*      */ import net.dv8tion.jda.api.hooks.VoiceDispatchInterceptor;
/*      */ import net.dv8tion.jda.api.requests.GatewayIntent;
/*      */ import net.dv8tion.jda.api.utils.ChunkingFilter;
/*      */ import net.dv8tion.jda.api.utils.Compression;
/*      */ import net.dv8tion.jda.api.utils.ConcurrentSessionController;
/*      */ import net.dv8tion.jda.api.utils.MemberCachePolicy;
/*      */ import net.dv8tion.jda.api.utils.SessionController;
/*      */ import net.dv8tion.jda.api.utils.cache.CacheFlag;
/*      */ import net.dv8tion.jda.internal.JDAImpl;
/*      */ import net.dv8tion.jda.internal.managers.PresenceImpl;
/*      */ import net.dv8tion.jda.internal.utils.Checks;
/*      */ import net.dv8tion.jda.internal.utils.IOUtil;
/*      */ import net.dv8tion.jda.internal.utils.config.AuthorizationConfig;
/*      */ import net.dv8tion.jda.internal.utils.config.MetaConfig;
/*      */ import net.dv8tion.jda.internal.utils.config.SessionConfig;
/*      */ import net.dv8tion.jda.internal.utils.config.ThreadingConfig;
/*      */ import net.dv8tion.jda.internal.utils.config.flags.ConfigFlag;
/*      */ import okhttp3.OkHttpClient;
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
/*      */ public class JDABuilder
/*      */ {
/*   61 */   public static final int GUILD_SUBSCRIPTIONS = GatewayIntent.getRaw(GatewayIntent.GUILD_MEMBERS, new GatewayIntent[] { GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING });
/*   62 */   protected final List<Object> listeners = new LinkedList();
/*   63 */   protected final EnumSet<CacheFlag> automaticallyDisabled = EnumSet.noneOf(CacheFlag.class);
/*      */   
/*   65 */   protected ScheduledExecutorService rateLimitPool = null;
/*      */   protected boolean shutdownRateLimitPool = true;
/*   67 */   protected ScheduledExecutorService mainWsPool = null;
/*      */   protected boolean shutdownMainWsPool = true;
/*   69 */   protected ExecutorService callbackPool = null;
/*      */   protected boolean shutdownCallbackPool = true;
/*   71 */   protected ExecutorService eventPool = null;
/*      */   protected boolean shutdownEventPool = true;
/*   73 */   protected ScheduledExecutorService audioPool = null;
/*      */   protected boolean shutdownAudioPool = true;
/*   75 */   protected EnumSet<CacheFlag> cacheFlags = EnumSet.allOf(CacheFlag.class);
/*   76 */   protected ConcurrentMap<String, String> contextMap = null;
/*   77 */   protected SessionController controller = null;
/*   78 */   protected VoiceDispatchInterceptor voiceDispatchInterceptor = null;
/*   79 */   protected OkHttpClient.Builder httpClientBuilder = null;
/*   80 */   protected OkHttpClient httpClient = null;
/*   81 */   protected WebSocketFactory wsFactory = null;
/*   82 */   protected String token = null;
/*   83 */   protected IEventManager eventManager = null;
/*   84 */   protected IAudioSendFactory audioSendFactory = null;
/*   85 */   protected JDA.ShardInfo shardInfo = null;
/*   86 */   protected Compression compression = Compression.ZLIB;
/*   87 */   protected Activity activity = null;
/*   88 */   protected OnlineStatus status = OnlineStatus.ONLINE;
/*      */   protected boolean idle = false;
/*   90 */   protected int maxReconnectDelay = 900;
/*   91 */   protected int largeThreshold = 250;
/*   92 */   protected int maxBufferSize = 2048;
/*   93 */   protected int intents = -1;
/*   94 */   protected EnumSet<ConfigFlag> flags = ConfigFlag.getDefault();
/*   95 */   protected ChunkingFilter chunkingFilter = ChunkingFilter.ALL;
/*   96 */   protected MemberCachePolicy memberCachePolicy = MemberCachePolicy.ALL;
/*   97 */   protected GatewayEncoding encoding = GatewayEncoding.JSON;
/*      */ 
/*      */   
/*      */   private JDABuilder(@Nullable String token, int intents) {
/*  101 */     this.token = token;
/*  102 */     this.intents = 0x1 | intents;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   public static JDABuilder createDefault(@Nullable String token) {
/*  128 */     return (new JDABuilder(token, GatewayIntent.DEFAULT)).applyDefault();
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   public static JDABuilder createDefault(@Nullable String token, @Nonnull GatewayIntent intent, @Nonnull GatewayIntent... intents) {
/*  169 */     Checks.notNull(intent, "GatewayIntent");
/*  170 */     Checks.noneNull((Object[])intents, "GatewayIntent");
/*  171 */     return createDefault(token, EnumSet.of(intent, intents));
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
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   public static JDABuilder createDefault(@Nullable String token, @Nonnull Collection<GatewayIntent> intents) {
/*  210 */     return create(token, intents).applyDefault();
/*      */   }
/*      */ 
/*      */   
/*      */   private JDABuilder applyDefault() {
/*  215 */     return setMemberCachePolicy(MemberCachePolicy.DEFAULT)
/*  216 */       .setChunkingFilter(ChunkingFilter.NONE)
/*  217 */       .disableCache(CacheFlag.getPrivileged())
/*  218 */       .setLargeThreshold(250);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   public static JDABuilder createLight(@Nullable String token) {
/*  244 */     return (new JDABuilder(token, GatewayIntent.DEFAULT)).applyLight();
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
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   public static JDABuilder createLight(@Nullable String token, @Nonnull GatewayIntent intent, @Nonnull GatewayIntent... intents) {
/*  282 */     Checks.notNull(intent, "GatewayIntent");
/*  283 */     Checks.noneNull((Object[])intents, "GatewayIntent");
/*  284 */     return createLight(token, EnumSet.of(intent, intents));
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   public static JDABuilder createLight(@Nullable String token, @Nonnull Collection<GatewayIntent> intents) {
/*  320 */     return create(token, intents).applyLight();
/*      */   }
/*      */ 
/*      */   
/*      */   private JDABuilder applyLight() {
/*  325 */     return setMemberCachePolicy(MemberCachePolicy.NONE)
/*  326 */       .setChunkingFilter(ChunkingFilter.NONE)
/*  327 */       .disableCache(EnumSet.allOf(CacheFlag.class))
/*  328 */       .setLargeThreshold(50);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   public static JDABuilder create(@Nonnull GatewayIntent intent, @Nonnull GatewayIntent... intents) {
/*  364 */     return create(null, intent, intents);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   public static JDABuilder create(@Nonnull Collection<GatewayIntent> intents) {
/*  397 */     return create((String)null, intents);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   public static JDABuilder create(@Nullable String token, @Nonnull GatewayIntent intent, @Nonnull GatewayIntent... intents) {
/*  431 */     return (new JDABuilder(token, GatewayIntent.getRaw(intent, intents))).applyIntents();
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   public static JDABuilder create(@Nullable String token, @Nonnull Collection<GatewayIntent> intents) {
/*  462 */     return (new JDABuilder(token, GatewayIntent.getRaw(intents))).applyIntents();
/*      */   }
/*      */ 
/*      */   
/*      */   private JDABuilder applyIntents() {
/*  467 */     EnumSet<CacheFlag> disabledCache = EnumSet.allOf(CacheFlag.class);
/*  468 */     for (CacheFlag flag : CacheFlag.values()) {
/*      */       
/*  470 */       GatewayIntent requiredIntent = flag.getRequiredIntent();
/*  471 */       if (requiredIntent == null || (requiredIntent.getRawValue() & this.intents) != 0) {
/*  472 */         disabledCache.remove(flag);
/*      */       }
/*      */     } 
/*  475 */     boolean enableMembers = ((this.intents & GatewayIntent.GUILD_MEMBERS.getRawValue()) != 0);
/*  476 */     return setChunkingFilter(enableMembers ? ChunkingFilter.ALL : ChunkingFilter.NONE)
/*  477 */       .setMemberCachePolicy(enableMembers ? MemberCachePolicy.ALL : MemberCachePolicy.DEFAULT)
/*  478 */       .setDisabledCache(disabledCache);
/*      */   }
/*      */ 
/*      */   
/*      */   private JDABuilder setDisabledCache(EnumSet<CacheFlag> flags) {
/*  483 */     disableCache(flags);
/*  484 */     this.automaticallyDisabled.addAll(flags);
/*  485 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setGatewayEncoding(@Nonnull GatewayEncoding encoding) {
/*  504 */     Checks.notNull(encoding, "GatewayEncoding");
/*  505 */     this.encoding = encoding;
/*  506 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setRawEventsEnabled(boolean enable) {
/*  523 */     return setFlag(ConfigFlag.RAW_EVENTS, enable);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder setRelativeRateLimit(boolean enable) {
/*  548 */     return setFlag(ConfigFlag.USE_RELATIVE_RATELIMIT, enable);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @Deprecated
/*      */   @ForRemoval(deadline = "5.0.0")
/*      */   @ReplaceWith("enableCache(flags) and disableCache(flags)")
/*      */   @DeprecatedSince("4.2.0")
/*      */   public JDABuilder setEnabledCacheFlags(@Nullable EnumSet<CacheFlag> flags) {
/*  576 */     this.cacheFlags = (flags == null) ? EnumSet.<CacheFlag>noneOf(CacheFlag.class) : EnumSet.<CacheFlag>copyOf(flags);
/*  577 */     return this;
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
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder enableCache(@Nonnull Collection<CacheFlag> flags) {
/*  598 */     Checks.noneNull(flags, "CacheFlags");
/*  599 */     this.cacheFlags.addAll(flags);
/*  600 */     return this;
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
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder enableCache(@Nonnull CacheFlag flag, @Nonnull CacheFlag... flags) {
/*  623 */     Checks.notNull(flag, "CacheFlag");
/*  624 */     Checks.noneNull((Object[])flags, "CacheFlag");
/*  625 */     this.cacheFlags.addAll(EnumSet.of(flag, flags));
/*  626 */     return this;
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
/*      */   @Nonnull
/*      */   @Deprecated
/*      */   @ForRemoval(deadline = "5.0.0")
/*      */   @ReplaceWith("enableCache(flags) and disableCache(flags)")
/*      */   @DeprecatedSince("4.2.0")
/*      */   public JDABuilder setDisabledCacheFlags(@Nullable EnumSet<CacheFlag> flags) {
/*  650 */     return setEnabledCacheFlags((flags == null) ? EnumSet.<CacheFlag>allOf(CacheFlag.class) : EnumSet.<CacheFlag>complementOf(flags));
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
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder disableCache(@Nonnull Collection<CacheFlag> flags) {
/*  671 */     Checks.noneNull(flags, "CacheFlags");
/*  672 */     this.automaticallyDisabled.removeAll(flags);
/*  673 */     this.cacheFlags.removeAll(flags);
/*  674 */     return this;
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
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder disableCache(@Nonnull CacheFlag flag, @Nonnull CacheFlag... flags) {
/*  697 */     Checks.notNull(flag, "CacheFlag");
/*  698 */     Checks.noneNull((Object[])flags, "CacheFlag");
/*  699 */     return disableCache(EnumSet.of(flag, flags));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder setMemberCachePolicy(@Nullable MemberCachePolicy policy) {
/*  741 */     if (policy == null) {
/*  742 */       this.memberCachePolicy = MemberCachePolicy.ALL;
/*      */     } else {
/*  744 */       this.memberCachePolicy = policy;
/*  745 */     }  return this;
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
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder setContextMap(@Nullable ConcurrentMap<String, String> map) {
/*  767 */     this.contextMap = map;
/*  768 */     if (map != null)
/*  769 */       setContextEnabled(true); 
/*  770 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setContextEnabled(boolean enable) {
/*  788 */     return setFlag(ConfigFlag.MDC_CONTEXT, enable);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder setCompression(@Nonnull Compression compression) {
/*  814 */     Checks.notNull(compression, "Compression");
/*  815 */     this.compression = compression;
/*  816 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setRequestTimeoutRetry(boolean retryOnTimeout) {
/*  834 */     return setFlag(ConfigFlag.RETRY_TIMEOUT, retryOnTimeout);
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
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder setToken(@Nullable String token) {
/*  857 */     this.token = token;
/*  858 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setHttpClientBuilder(@Nullable OkHttpClient.Builder builder) {
/*  873 */     this.httpClientBuilder = builder;
/*  874 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setHttpClient(@Nullable OkHttpClient client) {
/*  889 */     this.httpClient = client;
/*  890 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setWebsocketFactory(@Nullable WebSocketFactory factory) {
/*  905 */     this.wsFactory = factory;
/*  906 */     return this;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder setRateLimitPool(@Nullable ScheduledExecutorService pool) {
/*  930 */     return setRateLimitPool(pool, (pool == null));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder setRateLimitPool(@Nullable ScheduledExecutorService pool, boolean automaticShutdown) {
/*  954 */     this.rateLimitPool = pool;
/*  955 */     this.shutdownRateLimitPool = automaticShutdown;
/*  956 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setGatewayPool(@Nullable ScheduledExecutorService pool) {
/*  988 */     return setGatewayPool(pool, (pool == null));
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
/*      */   @Nonnull
/*      */   public JDABuilder setGatewayPool(@Nullable ScheduledExecutorService pool, boolean automaticShutdown) {
/* 1020 */     this.mainWsPool = pool;
/* 1021 */     this.shutdownMainWsPool = automaticShutdown;
/* 1022 */     return this;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder setCallbackPool(@Nullable ExecutorService executor) {
/* 1046 */     return setCallbackPool(executor, (executor == null));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder setCallbackPool(@Nullable ExecutorService executor, boolean automaticShutdown) {
/* 1070 */     this.callbackPool = executor;
/* 1071 */     this.shutdownCallbackPool = automaticShutdown;
/* 1072 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setEventPool(@Nullable ExecutorService executor) {
/* 1092 */     return setEventPool(executor, (executor == null));
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
/*      */   @Nonnull
/*      */   public JDABuilder setEventPool(@Nullable ExecutorService executor, boolean automaticShutdown) {
/* 1111 */     this.eventPool = executor;
/* 1112 */     this.shutdownEventPool = automaticShutdown;
/* 1113 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setAudioPool(@Nullable ScheduledExecutorService pool) {
/* 1133 */     return setAudioPool(pool, (pool == null));
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
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder setAudioPool(@Nullable ScheduledExecutorService pool, boolean automaticShutdown) {
/* 1155 */     this.audioPool = pool;
/* 1156 */     this.shutdownAudioPool = automaticShutdown;
/* 1157 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setBulkDeleteSplittingEnabled(boolean enabled) {
/* 1175 */     return setFlag(ConfigFlag.BULK_DELETE_SPLIT, enabled);
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
/*      */   @Nonnull
/*      */   public JDABuilder setEnableShutdownHook(boolean enable) {
/* 1193 */     return setFlag(ConfigFlag.SHUTDOWN_HOOK, enable);
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
/*      */   @Nonnull
/*      */   public JDABuilder setAutoReconnect(boolean autoReconnect) {
/* 1210 */     return setFlag(ConfigFlag.AUTO_RECONNECT, autoReconnect);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder setEventManager(@Nullable IEventManager manager) {
/* 1234 */     this.eventManager = manager;
/* 1235 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setAudioSendFactory(@Nullable IAudioSendFactory factory) {
/* 1252 */     this.audioSendFactory = factory;
/* 1253 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setIdle(boolean idle) {
/* 1270 */     this.idle = idle;
/* 1271 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setActivity(@Nullable Activity activity) {
/* 1291 */     this.activity = activity;
/* 1292 */     return this;
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
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder setStatus(@Nonnull OnlineStatus status) {
/* 1313 */     if (status == null || status == OnlineStatus.UNKNOWN)
/* 1314 */       throw new IllegalArgumentException("OnlineStatus cannot be null or unknown!"); 
/* 1315 */     this.status = status;
/* 1316 */     return this;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder addEventListeners(@Nonnull Object... listeners) {
/* 1341 */     Checks.noneNull(listeners, "listeners");
/*      */     
/* 1343 */     Collections.addAll(this.listeners, listeners);
/* 1344 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder removeEventListeners(@Nonnull Object... listeners) {
/* 1363 */     Checks.noneNull(listeners, "listeners");
/*      */     
/* 1365 */     this.listeners.removeAll(Arrays.asList(listeners));
/* 1366 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setMaxReconnectDelay(int maxReconnectDelay) {
/* 1386 */     Checks.check((maxReconnectDelay >= 32), "Max reconnect delay must be 32 seconds or greater. You provided %d.", Integer.valueOf(maxReconnectDelay));
/*      */     
/* 1388 */     this.maxReconnectDelay = maxReconnectDelay;
/* 1389 */     return this;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder useSharding(int shardId, int shardTotal) {
/* 1417 */     Checks.notNegative(shardId, "Shard ID");
/* 1418 */     Checks.positive(shardTotal, "Shard Total");
/* 1419 */     Checks.check((shardId < shardTotal), "The shard ID must be lower than the shardTotal! Shard IDs are 0-based.");
/*      */     
/* 1421 */     this.shardInfo = new JDA.ShardInfo(shardId, shardTotal);
/* 1422 */     return this;
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
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder setSessionController(@Nullable SessionController controller) {
/* 1443 */     this.controller = controller;
/* 1444 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setVoiceDispatchInterceptor(@Nullable VoiceDispatchInterceptor interceptor) {
/* 1462 */     this.voiceDispatchInterceptor = interceptor;
/* 1463 */     return this;
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
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder setChunkingFilter(@Nullable ChunkingFilter filter) {
/* 1485 */     this.chunkingFilter = (filter == null) ? ChunkingFilter.ALL : filter;
/* 1486 */     return this;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @Deprecated
/*      */   @ForRemoval(deadline = "5.0.0")
/*      */   @ReplaceWith("setDisabledIntents(...).setMemberCachePolicy(...)")
/*      */   @DeprecatedSince("4.2.0")
/*      */   public JDABuilder setGuildSubscriptionsEnabled(boolean enabled) {
/* 1517 */     if (!enabled) {
/*      */       
/* 1519 */       setMemberCachePolicy(MemberCachePolicy.VOICE);
/* 1520 */       this.intents &= GUILD_SUBSCRIPTIONS ^ 0xFFFFFFFF;
/*      */     } 
/* 1522 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setDisabledIntents(@Nonnull GatewayIntent intent, @Nonnull GatewayIntent... intents) {
/* 1553 */     Checks.notNull(intent, "Intents");
/* 1554 */     Checks.noneNull((Object[])intents, "Intents");
/* 1555 */     return setDisabledIntents(EnumSet.of(intent, intents));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder setDisabledIntents(@Nullable Collection<GatewayIntent> intents) {
/* 1581 */     this.intents = GatewayIntent.ALL_INTENTS;
/* 1582 */     if (intents != null)
/* 1583 */       this.intents &= GatewayIntent.getRaw(intents) ^ 0xFFFFFFFF; 
/* 1584 */     return this;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder disableIntents(@Nonnull Collection<GatewayIntent> intents) {
/* 1608 */     Checks.noneNull(intents, "GatewayIntent");
/* 1609 */     int raw = GatewayIntent.getRaw(intents);
/* 1610 */     this.intents &= raw ^ 0xFFFFFFFF;
/* 1611 */     return this;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder disableIntents(@Nonnull GatewayIntent intent, @Nonnull GatewayIntent... intents) {
/* 1637 */     Checks.notNull(intent, "GatewayIntent");
/* 1638 */     Checks.noneNull((Object[])intents, "GatewayIntent");
/* 1639 */     int raw = GatewayIntent.getRaw(intent, intents);
/* 1640 */     this.intents &= raw ^ 0xFFFFFFFF;
/* 1641 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setEnabledIntents(@Nonnull GatewayIntent intent, @Nonnull GatewayIntent... intents) {
/* 1672 */     Checks.notNull(intent, "Intents");
/* 1673 */     Checks.noneNull((Object[])intents, "Intents");
/* 1674 */     EnumSet<GatewayIntent> set = EnumSet.of(intent, intents);
/* 1675 */     return setDisabledIntents(EnumSet.complementOf(set));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder setEnabledIntents(@Nullable Collection<GatewayIntent> intents) {
/* 1701 */     if (intents == null || intents.isEmpty()) {
/* 1702 */       setDisabledIntents(EnumSet.allOf(GatewayIntent.class));
/* 1703 */     } else if (intents instanceof EnumSet) {
/* 1704 */       setDisabledIntents(EnumSet.complementOf((EnumSet<GatewayIntent>)intents));
/*      */     } else {
/* 1706 */       setDisabledIntents(EnumSet.complementOf(EnumSet.copyOf(intents)));
/* 1707 */     }  return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder enableIntents(@Nonnull Collection<GatewayIntent> intents) {
/* 1727 */     Checks.noneNull(intents, "GatewayIntent");
/* 1728 */     int raw = GatewayIntent.getRaw(intents);
/* 1729 */     this.intents |= raw;
/* 1730 */     return this;
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
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder enableIntents(@Nonnull GatewayIntent intent, @Nonnull GatewayIntent... intents) {
/* 1752 */     Checks.notNull(intent, "GatewayIntent");
/* 1753 */     Checks.noneNull((Object[])intents, "GatewayIntent");
/* 1754 */     int raw = GatewayIntent.getRaw(intent, intents);
/* 1755 */     this.intents |= raw;
/* 1756 */     return this;
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
/*      */   @Nonnull
/*      */   public JDABuilder setLargeThreshold(int threshold) {
/* 1775 */     this.largeThreshold = Math.max(50, Math.min(250, threshold));
/* 1776 */     return this;
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
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDABuilder setMaxBufferSize(int bufferSize) {
/* 1798 */     Checks.notNegative(bufferSize, "The buffer size");
/* 1799 */     this.maxBufferSize = bufferSize;
/* 1800 */     return this;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDA build() throws LoginException {
/* 1828 */     checkIntents();
/* 1829 */     OkHttpClient httpClient = this.httpClient;
/* 1830 */     if (httpClient == null) {
/*      */       
/* 1832 */       if (this.httpClientBuilder == null)
/* 1833 */         this.httpClientBuilder = IOUtil.newHttpClientBuilder(); 
/* 1834 */       httpClient = this.httpClientBuilder.build();
/*      */     } 
/*      */     
/* 1837 */     WebSocketFactory wsFactory = (this.wsFactory == null) ? new WebSocketFactory() : this.wsFactory;
/*      */     
/* 1839 */     if (this.controller == null && this.shardInfo != null) {
/* 1840 */       this.controller = (SessionController)new ConcurrentSessionController();
/*      */     }
/* 1842 */     AuthorizationConfig authConfig = new AuthorizationConfig(this.token);
/* 1843 */     ThreadingConfig threadingConfig = new ThreadingConfig();
/* 1844 */     threadingConfig.setCallbackPool(this.callbackPool, this.shutdownCallbackPool);
/* 1845 */     threadingConfig.setGatewayPool(this.mainWsPool, this.shutdownMainWsPool);
/* 1846 */     threadingConfig.setRateLimitPool(this.rateLimitPool, this.shutdownRateLimitPool);
/* 1847 */     threadingConfig.setEventPool(this.eventPool, this.shutdownEventPool);
/* 1848 */     threadingConfig.setAudioPool(this.audioPool, this.shutdownAudioPool);
/* 1849 */     SessionConfig sessionConfig = new SessionConfig(this.controller, httpClient, wsFactory, this.voiceDispatchInterceptor, this.flags, this.maxReconnectDelay, this.largeThreshold);
/* 1850 */     MetaConfig metaConfig = new MetaConfig(this.maxBufferSize, this.contextMap, this.cacheFlags, this.flags);
/*      */     
/* 1852 */     JDAImpl jda = new JDAImpl(authConfig, sessionConfig, threadingConfig, metaConfig);
/* 1853 */     jda.setMemberCachePolicy(this.memberCachePolicy);
/*      */     
/* 1855 */     if ((this.intents & GatewayIntent.GUILD_MEMBERS.getRawValue()) == 0) {
/* 1856 */       jda.setChunkingFilter(ChunkingFilter.NONE);
/*      */     } else {
/* 1858 */       jda.setChunkingFilter(this.chunkingFilter);
/*      */     } 
/* 1860 */     if (this.eventManager != null) {
/* 1861 */       jda.setEventManager(this.eventManager);
/*      */     }
/* 1863 */     if (this.audioSendFactory != null) {
/* 1864 */       jda.setAudioSendFactory(this.audioSendFactory);
/*      */     }
/* 1866 */     Objects.requireNonNull(jda); this.listeners.forEach(xva$0 -> rec$.addEventListener(new Object[] { xva$0 }));
/* 1867 */     jda.setStatus(JDA.Status.INITIALIZED);
/*      */ 
/*      */     
/* 1870 */     ((PresenceImpl)jda.getPresence())
/* 1871 */       .setCacheActivity(this.activity)
/* 1872 */       .setCacheIdle(this.idle)
/* 1873 */       .setCacheStatus(this.status);
/* 1874 */     jda.login(this.shardInfo, this.compression, true, this.intents, this.encoding);
/* 1875 */     return (JDA)jda;
/*      */   }
/*      */ 
/*      */   
/*      */   private JDABuilder setFlag(ConfigFlag flag, boolean enable) {
/* 1880 */     if (enable) {
/* 1881 */       this.flags.add(flag);
/*      */     } else {
/* 1883 */       this.flags.remove(flag);
/* 1884 */     }  return this;
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkIntents() {
/* 1889 */     boolean membersIntent = ((this.intents & GatewayIntent.GUILD_MEMBERS.getRawValue()) != 0);
/* 1890 */     if (!membersIntent && this.memberCachePolicy == MemberCachePolicy.ALL)
/* 1891 */       throw new IllegalStateException("Cannot use MemberCachePolicy.ALL without GatewayIntent.GUILD_MEMBERS enabled!"); 
/* 1892 */     if (!membersIntent && this.chunkingFilter != ChunkingFilter.NONE) {
/* 1893 */       JDAImpl.LOG.warn("Member chunking is disabled due to missing GUILD_MEMBERS intent.");
/*      */     }
/* 1895 */     if (!this.automaticallyDisabled.isEmpty()) {
/*      */       
/* 1897 */       JDAImpl.LOG.warn("Automatically disabled CacheFlags due to missing intents");
/*      */ 
/*      */ 
/*      */       
/* 1901 */       Objects.requireNonNull(JDAImpl.LOG); this.automaticallyDisabled.stream().map(it -> "Disabled CacheFlag." + it + " (missing GatewayIntent." + it.getRequiredIntent() + ")").forEach(JDAImpl.LOG::warn);
/*      */ 
/*      */       
/* 1904 */       JDAImpl.LOG.warn("You can manually disable these flags to remove this warning by using disableCache({}) on your JDABuilder", this.automaticallyDisabled
/* 1905 */           .stream()
/* 1906 */           .map(it -> "CacheFlag." + it)
/* 1907 */           .collect(Collectors.joining(", ")));
/*      */       
/* 1909 */       this.automaticallyDisabled.clear();
/*      */     } 
/*      */     
/* 1912 */     if (this.cacheFlags.isEmpty()) {
/*      */       return;
/*      */     }
/* 1915 */     EnumSet<GatewayIntent> providedIntents = GatewayIntent.getIntents(this.intents);
/* 1916 */     for (CacheFlag flag : this.cacheFlags) {
/*      */       
/* 1918 */       GatewayIntent intent = flag.getRequiredIntent();
/* 1919 */       if (intent != null && !providedIntents.contains(intent))
/* 1920 */         throw new IllegalArgumentException("Cannot use CacheFlag." + flag + " without GatewayIntent." + intent + "!"); 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\JDABuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */