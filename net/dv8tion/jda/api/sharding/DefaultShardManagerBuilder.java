/*      */ package net.dv8tion.jda.api.sharding;
/*      */ 
/*      */ import com.neovisionaries.ws.client.WebSocketFactory;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.concurrent.ThreadFactory;
/*      */ import java.util.function.IntFunction;
/*      */ import java.util.stream.Collector;
/*      */ import java.util.stream.Collectors;
/*      */ import javax.annotation.CheckReturnValue;
/*      */ import javax.annotation.Nonnull;
/*      */ import javax.annotation.Nullable;
/*      */ import javax.security.auth.login.LoginException;
/*      */ import net.dv8tion.jda.annotations.DeprecatedSince;
/*      */ import net.dv8tion.jda.annotations.ForRemoval;
/*      */ import net.dv8tion.jda.annotations.ReplaceWith;
/*      */ import net.dv8tion.jda.api.GatewayEncoding;
/*      */ import net.dv8tion.jda.api.JDABuilder;
/*      */ import net.dv8tion.jda.api.OnlineStatus;
/*      */ import net.dv8tion.jda.api.audio.factory.IAudioSendFactory;
/*      */ import net.dv8tion.jda.api.entities.Activity;
/*      */ import net.dv8tion.jda.api.hooks.IEventManager;
/*      */ import net.dv8tion.jda.api.hooks.VoiceDispatchInterceptor;
/*      */ import net.dv8tion.jda.api.requests.GatewayIntent;
/*      */ import net.dv8tion.jda.api.utils.ChunkingFilter;
/*      */ import net.dv8tion.jda.api.utils.Compression;
/*      */ import net.dv8tion.jda.api.utils.MemberCachePolicy;
/*      */ import net.dv8tion.jda.api.utils.SessionController;
/*      */ import net.dv8tion.jda.api.utils.cache.CacheFlag;
/*      */ import net.dv8tion.jda.internal.JDAImpl;
/*      */ import net.dv8tion.jda.internal.utils.Checks;
/*      */ import net.dv8tion.jda.internal.utils.config.flags.ConfigFlag;
/*      */ import net.dv8tion.jda.internal.utils.config.flags.ShardingConfigFlag;
/*      */ import net.dv8tion.jda.internal.utils.config.sharding.EventConfig;
/*      */ import net.dv8tion.jda.internal.utils.config.sharding.PresenceProviderConfig;
/*      */ import net.dv8tion.jda.internal.utils.config.sharding.ShardingConfig;
/*      */ import net.dv8tion.jda.internal.utils.config.sharding.ShardingMetaConfig;
/*      */ import net.dv8tion.jda.internal.utils.config.sharding.ShardingSessionConfig;
/*      */ import net.dv8tion.jda.internal.utils.config.sharding.ThreadingProviderConfig;
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
/*      */ public class DefaultShardManagerBuilder
/*      */ {
/*   64 */   protected final List<Object> listeners = new ArrayList();
/*   65 */   protected final List<IntFunction<Object>> listenerProviders = new ArrayList<>();
/*   66 */   protected final EnumSet<CacheFlag> automaticallyDisabled = EnumSet.noneOf(CacheFlag.class);
/*   67 */   protected SessionController sessionController = null;
/*   68 */   protected VoiceDispatchInterceptor voiceDispatchInterceptor = null;
/*   69 */   protected EnumSet<CacheFlag> cacheFlags = EnumSet.allOf(CacheFlag.class);
/*   70 */   protected EnumSet<ConfigFlag> flags = ConfigFlag.getDefault();
/*   71 */   protected EnumSet<ShardingConfigFlag> shardingFlags = ShardingConfigFlag.getDefault();
/*   72 */   protected Compression compression = Compression.ZLIB;
/*   73 */   protected GatewayEncoding encoding = GatewayEncoding.JSON;
/*   74 */   protected int shardsTotal = -1;
/*   75 */   protected int maxReconnectDelay = 900;
/*   76 */   protected int largeThreshold = 250;
/*   77 */   protected int maxBufferSize = 2048;
/*   78 */   protected int intents = -1;
/*   79 */   protected String token = null;
/*   80 */   protected IntFunction<Boolean> idleProvider = null;
/*   81 */   protected IntFunction<OnlineStatus> statusProvider = null;
/*   82 */   protected IntFunction<? extends Activity> activityProvider = null;
/*   83 */   protected IntFunction<? extends ConcurrentMap<String, String>> contextProvider = null;
/*   84 */   protected IntFunction<? extends IEventManager> eventManagerProvider = null;
/*   85 */   protected ThreadPoolProvider<? extends ScheduledExecutorService> rateLimitPoolProvider = null;
/*   86 */   protected ThreadPoolProvider<? extends ScheduledExecutorService> gatewayPoolProvider = null;
/*   87 */   protected ThreadPoolProvider<? extends ExecutorService> callbackPoolProvider = null;
/*   88 */   protected ThreadPoolProvider<? extends ExecutorService> eventPoolProvider = null;
/*   89 */   protected ThreadPoolProvider<? extends ScheduledExecutorService> audioPoolProvider = null;
/*   90 */   protected Collection<Integer> shards = null;
/*   91 */   protected OkHttpClient.Builder httpClientBuilder = null;
/*   92 */   protected OkHttpClient httpClient = null;
/*   93 */   protected WebSocketFactory wsFactory = null;
/*   94 */   protected IAudioSendFactory audioSendFactory = null;
/*   95 */   protected ThreadFactory threadFactory = null;
/*      */   protected ChunkingFilter chunkingFilter;
/*   97 */   protected MemberCachePolicy memberCachePolicy = MemberCachePolicy.ALL;
/*      */ 
/*      */   
/*      */   private DefaultShardManagerBuilder(@Nullable String token, int intents) {
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
/*      */   public static DefaultShardManagerBuilder createDefault(@Nullable String token) {
/*  128 */     return (new DefaultShardManagerBuilder(token, GatewayIntent.DEFAULT)).applyDefault();
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
/*      */   public static DefaultShardManagerBuilder createDefault(@Nullable String token, @Nonnull GatewayIntent intent, @Nonnull GatewayIntent... intents) {
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
/*      */   public static DefaultShardManagerBuilder createDefault(@Nullable String token, @Nonnull Collection<GatewayIntent> intents) {
/*  210 */     return create(token, intents).applyDefault();
/*      */   }
/*      */ 
/*      */   
/*      */   private DefaultShardManagerBuilder applyDefault() {
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
/*      */   public static DefaultShardManagerBuilder createLight(@Nullable String token) {
/*  244 */     return (new DefaultShardManagerBuilder(token, GatewayIntent.DEFAULT)).applyLight();
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
/*      */   public static DefaultShardManagerBuilder createLight(@Nullable String token, @Nonnull GatewayIntent intent, @Nonnull GatewayIntent... intents) {
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
/*      */   public static DefaultShardManagerBuilder createLight(@Nullable String token, @Nonnull Collection<GatewayIntent> intents) {
/*  320 */     return create(token, intents).applyLight();
/*      */   }
/*      */ 
/*      */   
/*      */   private DefaultShardManagerBuilder applyLight() {
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
/*      */   public static DefaultShardManagerBuilder create(@Nonnull GatewayIntent intent, @Nonnull GatewayIntent... intents) {
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   public static DefaultShardManagerBuilder create(@Nonnull Collection<GatewayIntent> intents) {
/*  396 */     return create((String)null, intents);
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
/*      */   public static DefaultShardManagerBuilder create(@Nullable String token, @Nonnull GatewayIntent intent, @Nonnull GatewayIntent... intents) {
/*  430 */     return (new DefaultShardManagerBuilder(token, GatewayIntent.getRaw(intent, intents))).applyIntents();
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
/*      */   public static DefaultShardManagerBuilder create(@Nullable String token, @Nonnull Collection<GatewayIntent> intents) {
/*  461 */     return (new DefaultShardManagerBuilder(token, GatewayIntent.getRaw(intents))).applyIntents();
/*      */   }
/*      */ 
/*      */   
/*      */   private DefaultShardManagerBuilder applyIntents() {
/*  466 */     EnumSet<CacheFlag> disabledCache = EnumSet.allOf(CacheFlag.class);
/*  467 */     for (CacheFlag flag : CacheFlag.values()) {
/*      */       
/*  469 */       GatewayIntent requiredIntent = flag.getRequiredIntent();
/*  470 */       if (requiredIntent == null || (requiredIntent.getRawValue() & this.intents) != 0) {
/*  471 */         disabledCache.remove(flag);
/*      */       }
/*      */     } 
/*  474 */     boolean enableMembers = ((this.intents & GatewayIntent.GUILD_MEMBERS.getRawValue()) != 0);
/*  475 */     return setChunkingFilter(enableMembers ? ChunkingFilter.ALL : ChunkingFilter.NONE)
/*  476 */       .setMemberCachePolicy(enableMembers ? MemberCachePolicy.ALL : MemberCachePolicy.DEFAULT)
/*  477 */       .setDisabledCache(disabledCache);
/*      */   }
/*      */ 
/*      */   
/*      */   private DefaultShardManagerBuilder setDisabledCache(EnumSet<CacheFlag> flags) {
/*  482 */     disableCache(flags);
/*  483 */     this.automaticallyDisabled.addAll(flags);
/*  484 */     return this;
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
/*      */   public DefaultShardManagerBuilder setGatewayEncoding(@Nonnull GatewayEncoding encoding) {
/*  503 */     Checks.notNull(encoding, "GatewayEncoding");
/*  504 */     this.encoding = encoding;
/*  505 */     return this;
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
/*      */   public DefaultShardManagerBuilder setRawEventsEnabled(boolean enable) {
/*  522 */     return setFlag(ConfigFlag.RAW_EVENTS, enable);
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
/*      */   public DefaultShardManagerBuilder setRelativeRateLimit(boolean enable) {
/*  547 */     return setFlag(ConfigFlag.USE_RELATIVE_RATELIMIT, enable);
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
/*      */   @Deprecated
/*      */   @ForRemoval(deadline = "5.0.0")
/*      */   @ReplaceWith("enableCache(flags) and disableCache(flags)")
/*      */   @DeprecatedSince("4.2.0")
/*      */   public DefaultShardManagerBuilder setEnabledCacheFlags(@Nullable EnumSet<CacheFlag> flags) {
/*  573 */     this.cacheFlags = (flags == null) ? EnumSet.<CacheFlag>noneOf(CacheFlag.class) : EnumSet.<CacheFlag>copyOf(flags);
/*  574 */     return this;
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
/*      */   public DefaultShardManagerBuilder enableCache(@Nonnull Collection<CacheFlag> flags) {
/*  595 */     Checks.noneNull(flags, "CacheFlags");
/*  596 */     this.cacheFlags.addAll(flags);
/*  597 */     return this;
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
/*      */   public DefaultShardManagerBuilder enableCache(@Nonnull CacheFlag flag, @Nonnull CacheFlag... flags) {
/*  620 */     Checks.notNull(flag, "CacheFlag");
/*  621 */     Checks.noneNull((Object[])flags, "CacheFlag");
/*  622 */     this.cacheFlags.addAll(EnumSet.of(flag, flags));
/*  623 */     return this;
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
/*      */   public DefaultShardManagerBuilder setDisabledCacheFlags(@Nullable EnumSet<CacheFlag> flags) {
/*  647 */     return setEnabledCacheFlags((flags == null) ? EnumSet.<CacheFlag>allOf(CacheFlag.class) : EnumSet.<CacheFlag>complementOf(flags));
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
/*      */   public DefaultShardManagerBuilder disableCache(@Nonnull Collection<CacheFlag> flags) {
/*  668 */     Checks.noneNull(flags, "CacheFlags");
/*  669 */     this.automaticallyDisabled.removeAll(flags);
/*  670 */     this.cacheFlags.removeAll(flags);
/*  671 */     return this;
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
/*      */   public DefaultShardManagerBuilder disableCache(@Nonnull CacheFlag flag, @Nonnull CacheFlag... flags) {
/*  694 */     Checks.notNull(flag, "CacheFlag");
/*  695 */     Checks.noneNull((Object[])flags, "CacheFlag");
/*  696 */     return disableCache(EnumSet.of(flag, flags));
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
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public DefaultShardManagerBuilder setMemberCachePolicy(@Nullable MemberCachePolicy policy) {
/*  740 */     if (policy == null) {
/*  741 */       this.memberCachePolicy = MemberCachePolicy.ALL;
/*      */     } else {
/*  743 */       this.memberCachePolicy = policy;
/*  744 */     }  return this;
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
/*      */   public DefaultShardManagerBuilder setSessionController(@Nullable SessionController controller) {
/*  762 */     this.sessionController = controller;
/*  763 */     return this;
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
/*      */   public DefaultShardManagerBuilder setVoiceDispatchInterceptor(@Nullable VoiceDispatchInterceptor interceptor) {
/*  781 */     this.voiceDispatchInterceptor = interceptor;
/*  782 */     return this;
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
/*      */   public DefaultShardManagerBuilder setContextMap(@Nullable IntFunction<? extends ConcurrentMap<String, String>> provider) {
/*  804 */     this.contextProvider = provider;
/*  805 */     if (provider != null)
/*  806 */       setContextEnabled(true); 
/*  807 */     return this;
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
/*      */   public DefaultShardManagerBuilder setContextEnabled(boolean enable) {
/*  825 */     return setFlag(ConfigFlag.MDC_CONTEXT, enable);
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
/*      */   public DefaultShardManagerBuilder setCompression(@Nonnull Compression compression) {
/*  851 */     Checks.notNull(compression, "Compression");
/*  852 */     this.compression = compression;
/*  853 */     return this;
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
/*      */   public DefaultShardManagerBuilder addEventListeners(@Nonnull Object... listeners) {
/*  875 */     return addEventListeners(Arrays.asList(listeners));
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
/*      */   public DefaultShardManagerBuilder addEventListeners(@Nonnull Collection<Object> listeners) {
/*  897 */     Checks.noneNull(listeners, "listeners");
/*      */     
/*  899 */     this.listeners.addAll(listeners);
/*  900 */     return this;
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
/*      */   @Nonnull
/*      */   public DefaultShardManagerBuilder removeEventListeners(@Nonnull Object... listeners) {
/*  916 */     return removeEventListeners(Arrays.asList(listeners));
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
/*      */   @Nonnull
/*      */   public DefaultShardManagerBuilder removeEventListeners(@Nonnull Collection<Object> listeners) {
/*  932 */     Checks.noneNull(listeners, "listeners");
/*      */     
/*  934 */     this.listeners.removeAll(listeners);
/*  935 */     return this;
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
/*      */   public DefaultShardManagerBuilder addEventListenerProvider(@Nonnull IntFunction<Object> listenerProvider) {
/*  958 */     return addEventListenerProviders(Collections.singleton(listenerProvider));
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
/*      */   public DefaultShardManagerBuilder addEventListenerProviders(@Nonnull Collection<IntFunction<Object>> listenerProviders) {
/*  981 */     Checks.noneNull(listenerProviders, "listener providers");
/*      */     
/*  983 */     this.listenerProviders.addAll(listenerProviders);
/*  984 */     return this;
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
/*      */   @Nonnull
/*      */   public DefaultShardManagerBuilder removeEventListenerProvider(@Nonnull IntFunction<Object> listenerProvider) {
/*  998 */     return removeEventListenerProviders(Collections.singleton(listenerProvider));
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
/*      */   @Nonnull
/*      */   public DefaultShardManagerBuilder removeEventListenerProviders(@Nonnull Collection<IntFunction<Object>> listenerProviders) {
/* 1012 */     Checks.noneNull(listenerProviders, "listener providers");
/*      */     
/* 1014 */     this.listenerProviders.removeAll(listenerProviders);
/* 1015 */     return this;
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
/*      */   public DefaultShardManagerBuilder setAudioSendFactory(@Nullable IAudioSendFactory factory) {
/* 1032 */     this.audioSendFactory = factory;
/* 1033 */     return this;
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
/*      */   public DefaultShardManagerBuilder setAutoReconnect(boolean autoReconnect) {
/* 1050 */     return setFlag(ConfigFlag.AUTO_RECONNECT, autoReconnect);
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
/*      */   public DefaultShardManagerBuilder setBulkDeleteSplittingEnabled(boolean enabled) {
/* 1068 */     return setFlag(ConfigFlag.BULK_DELETE_SPLIT, enabled);
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
/*      */   public DefaultShardManagerBuilder setEnableShutdownHook(boolean enable) {
/* 1086 */     return setFlag(ConfigFlag.SHUTDOWN_HOOK, enable);
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
/*      */   @Deprecated
/*      */   @ForRemoval(deadline = "5.0.0")
/*      */   @DeprecatedSince("3.8.1")
/*      */   @ReplaceWith("setEventManagerProvider((id) -> manager)")
/*      */   public DefaultShardManagerBuilder setEventManager(@Nonnull IEventManager manager) {
/* 1116 */     Checks.notNull(manager, "manager");
/*      */     
/* 1118 */     return setEventManagerProvider(id -> manager);
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
/*      */   public DefaultShardManagerBuilder setEventManagerProvider(@Nonnull IntFunction<? extends IEventManager> eventManagerProvider) {
/* 1142 */     Checks.notNull(eventManagerProvider, "eventManagerProvider");
/* 1143 */     this.eventManagerProvider = eventManagerProvider;
/* 1144 */     return this;
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
/*      */   public DefaultShardManagerBuilder setActivity(@Nullable Activity activity) {
/* 1165 */     return setActivityProvider(id -> activity);
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
/*      */   public DefaultShardManagerBuilder setActivityProvider(@Nullable IntFunction<? extends Activity> activityProvider) {
/* 1186 */     this.activityProvider = activityProvider;
/* 1187 */     return this;
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
/*      */   public DefaultShardManagerBuilder setIdle(boolean idle) {
/* 1205 */     return setIdleProvider(id -> Boolean.valueOf(idle));
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
/*      */   public DefaultShardManagerBuilder setIdleProvider(@Nullable IntFunction<Boolean> idleProvider) {
/* 1223 */     this.idleProvider = idleProvider;
/* 1224 */     return this;
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
/*      */   public DefaultShardManagerBuilder setStatus(@Nullable OnlineStatus status) {
/* 1244 */     Checks.notNull(status, "status");
/* 1245 */     Checks.check((status != OnlineStatus.UNKNOWN), "OnlineStatus cannot be unknown!");
/*      */     
/* 1247 */     return setStatusProvider(id -> status);
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
/*      */   public DefaultShardManagerBuilder setStatusProvider(@Nullable IntFunction<OnlineStatus> statusProvider) {
/* 1267 */     this.statusProvider = statusProvider;
/* 1268 */     return this;
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
/*      */   @Nonnull
/*      */   public DefaultShardManagerBuilder setThreadFactory(@Nullable ThreadFactory threadFactory) {
/* 1284 */     this.threadFactory = threadFactory;
/* 1285 */     return this;
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
/*      */   public DefaultShardManagerBuilder setHttpClientBuilder(@Nullable OkHttpClient.Builder builder) {
/* 1300 */     this.httpClientBuilder = builder;
/* 1301 */     return this;
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
/*      */   public DefaultShardManagerBuilder setHttpClient(@Nullable OkHttpClient client) {
/* 1316 */     this.httpClient = client;
/* 1317 */     return this;
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
/*      */   public DefaultShardManagerBuilder setRateLimitPool(@Nullable ScheduledExecutorService pool) {
/* 1342 */     return setRateLimitPool(pool, (pool == null));
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
/*      */   public DefaultShardManagerBuilder setRateLimitPool(@Nullable ScheduledExecutorService pool, boolean automaticShutdown) {
/* 1367 */     return setRateLimitPoolProvider((pool == null) ? null : new ThreadPoolProviderImpl<>(pool, automaticShutdown));
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
/*      */   public DefaultShardManagerBuilder setRateLimitPoolProvider(@Nullable ThreadPoolProvider<? extends ScheduledExecutorService> provider) {
/* 1389 */     this.rateLimitPoolProvider = provider;
/* 1390 */     return this;
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
/*      */   public DefaultShardManagerBuilder setGatewayPool(@Nullable ScheduledExecutorService pool) {
/* 1422 */     return setGatewayPool(pool, (pool == null));
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
/*      */   public DefaultShardManagerBuilder setGatewayPool(@Nullable ScheduledExecutorService pool, boolean automaticShutdown) {
/* 1454 */     return setGatewayPoolProvider((pool == null) ? null : new ThreadPoolProviderImpl<>(pool, automaticShutdown));
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
/*      */   @Nonnull
/*      */   public DefaultShardManagerBuilder setGatewayPoolProvider(@Nullable ThreadPoolProvider<? extends ScheduledExecutorService> provider) {
/* 1483 */     this.gatewayPoolProvider = provider;
/* 1484 */     return this;
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
/*      */   public DefaultShardManagerBuilder setCallbackPool(@Nullable ExecutorService executor) {
/* 1508 */     return setCallbackPool(executor, (executor == null));
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
/*      */   public DefaultShardManagerBuilder setCallbackPool(@Nullable ExecutorService executor, boolean automaticShutdown) {
/* 1532 */     return setCallbackPoolProvider((executor == null) ? null : new ThreadPoolProviderImpl<>(executor, automaticShutdown));
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
/*      */   public DefaultShardManagerBuilder setCallbackPoolProvider(@Nullable ThreadPoolProvider<? extends ExecutorService> provider) {
/* 1554 */     this.callbackPoolProvider = provider;
/* 1555 */     return this;
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
/*      */   public DefaultShardManagerBuilder setEventPool(@Nullable ExecutorService executor) {
/* 1575 */     return setEventPool(executor, (executor == null));
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
/*      */   public DefaultShardManagerBuilder setEventPool(@Nullable ExecutorService executor, boolean automaticShutdown) {
/* 1594 */     return setEventPoolProvider((executor == null) ? null : new ThreadPoolProviderImpl<>(executor, automaticShutdown));
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
/*      */   public DefaultShardManagerBuilder setEventPoolProvider(@Nullable ThreadPoolProvider<? extends ExecutorService> provider) {
/* 1618 */     this.eventPoolProvider = provider;
/* 1619 */     return this;
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
/*      */   public DefaultShardManagerBuilder setAudioPool(@Nullable ScheduledExecutorService pool) {
/* 1639 */     return setAudioPool(pool, (pool == null));
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
/*      */   public DefaultShardManagerBuilder setAudioPool(@Nullable ScheduledExecutorService pool, boolean automaticShutdown) {
/* 1661 */     return setAudioPoolProvider((pool == null) ? null : new ThreadPoolProviderImpl<>(pool, automaticShutdown));
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
/*      */   public DefaultShardManagerBuilder setAudioPoolProvider(@Nullable ThreadPoolProvider<? extends ScheduledExecutorService> provider) {
/* 1681 */     this.audioPoolProvider = provider;
/* 1682 */     return this;
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
/*      */   public DefaultShardManagerBuilder setMaxReconnectDelay(int maxReconnectDelay) {
/* 1702 */     Checks.check((maxReconnectDelay >= 32), "Max reconnect delay must be 32 seconds or greater. You provided %d.", Integer.valueOf(maxReconnectDelay));
/*      */     
/* 1704 */     this.maxReconnectDelay = maxReconnectDelay;
/* 1705 */     return this;
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
/*      */   public DefaultShardManagerBuilder setRequestTimeoutRetry(boolean retryOnTimeout) {
/* 1723 */     return setFlag(ConfigFlag.RETRY_TIMEOUT, retryOnTimeout);
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
/*      */   @Nonnull
/*      */   public DefaultShardManagerBuilder setShards(int... shardIds) {
/* 1739 */     Checks.notNull(shardIds, "shardIds");
/* 1740 */     for (int id : shardIds) {
/*      */       
/* 1742 */       Checks.notNegative(id, "minShardId");
/* 1743 */       Checks.check((id < this.shardsTotal), "maxShardId must be lower than shardsTotal");
/*      */     } 
/*      */     
/* 1746 */     this.shards = Arrays.stream(shardIds).boxed().collect((Collector)Collectors.toSet());
/*      */     
/* 1748 */     return this;
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
/*      */   public DefaultShardManagerBuilder setShards(int minShardId, int maxShardId) {
/* 1772 */     Checks.notNegative(minShardId, "minShardId");
/* 1773 */     Checks.check((maxShardId < this.shardsTotal), "maxShardId must be lower than shardsTotal");
/* 1774 */     Checks.check((minShardId <= maxShardId), "minShardId must be lower than or equal to maxShardId");
/*      */     
/* 1776 */     List<Integer> shards = new ArrayList<>(maxShardId - minShardId + 1);
/* 1777 */     for (int i = minShardId; i <= maxShardId; i++) {
/* 1778 */       shards.add(Integer.valueOf(i));
/*      */     }
/* 1780 */     this.shards = shards;
/*      */     
/* 1782 */     return this;
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
/*      */   public DefaultShardManagerBuilder setShards(@Nonnull Collection<Integer> shardIds) {
/* 1803 */     Checks.notNull(shardIds, "shardIds");
/* 1804 */     for (Integer id : shardIds) {
/*      */       
/* 1806 */       Checks.notNegative(id.intValue(), "minShardId");
/* 1807 */       Checks.check((id.intValue() < this.shardsTotal), "maxShardId must be lower than shardsTotal");
/*      */     } 
/*      */     
/* 1810 */     this.shards = new ArrayList<>(shardIds);
/*      */     
/* 1812 */     return this;
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
/*      */   public DefaultShardManagerBuilder setShardsTotal(int shardsTotal) {
/* 1829 */     Checks.check((shardsTotal == -1 || shardsTotal > 0), "shardsTotal must either be -1 or greater than 0");
/* 1830 */     this.shardsTotal = shardsTotal;
/*      */     
/* 1832 */     return this;
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
/*      */   public DefaultShardManagerBuilder setToken(@Nonnull String token) {
/* 1858 */     Checks.notBlank(token, "token");
/*      */     
/* 1860 */     this.token = token;
/* 1861 */     return this;
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
/*      */   public DefaultShardManagerBuilder setUseShutdownNow(boolean useShutdownNow) {
/* 1881 */     return setFlag(ShardingConfigFlag.SHUTDOWN_NOW, useShutdownNow);
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
/*      */   public DefaultShardManagerBuilder setWebsocketFactory(@Nullable WebSocketFactory factory) {
/* 1896 */     this.wsFactory = factory;
/* 1897 */     return this;
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
/*      */   public DefaultShardManagerBuilder setChunkingFilter(@Nullable ChunkingFilter filter) {
/* 1920 */     this.chunkingFilter = filter;
/* 1921 */     return this;
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
/*      */   public DefaultShardManagerBuilder setGuildSubscriptionsEnabled(boolean enabled) {
/* 1952 */     if (!enabled) {
/*      */       
/* 1954 */       setMemberCachePolicy(MemberCachePolicy.VOICE);
/* 1955 */       this.intents &= JDABuilder.GUILD_SUBSCRIPTIONS ^ 0xFFFFFFFF;
/*      */     } 
/* 1957 */     return this;
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
/*      */   public DefaultShardManagerBuilder setDisabledIntents(@Nonnull GatewayIntent intent, @Nonnull GatewayIntent... intents) {
/* 1988 */     Checks.notNull(intent, "Intent");
/* 1989 */     Checks.noneNull((Object[])intents, "Intent");
/* 1990 */     EnumSet<GatewayIntent> set = EnumSet.of(intent, intents);
/* 1991 */     return setDisabledIntents(set);
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
/*      */   public DefaultShardManagerBuilder setDisabledIntents(@Nullable Collection<GatewayIntent> intents) {
/* 2017 */     this.intents = GatewayIntent.ALL_INTENTS;
/* 2018 */     if (intents != null)
/* 2019 */       this.intents &= GatewayIntent.getRaw(intents) ^ 0xFFFFFFFF; 
/* 2020 */     return this;
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
/*      */   public DefaultShardManagerBuilder disableIntents(@Nonnull Collection<GatewayIntent> intents) {
/* 2044 */     Checks.noneNull(intents, "GatewayIntent");
/* 2045 */     int raw = GatewayIntent.getRaw(intents);
/* 2046 */     this.intents &= raw ^ 0xFFFFFFFF;
/* 2047 */     return this;
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
/*      */   public DefaultShardManagerBuilder disableIntents(@Nonnull GatewayIntent intent, @Nonnull GatewayIntent... intents) {
/* 2073 */     Checks.notNull(intent, "GatewayIntent");
/* 2074 */     Checks.noneNull((Object[])intents, "GatewayIntent");
/* 2075 */     int raw = GatewayIntent.getRaw(intent, intents);
/* 2076 */     this.intents &= raw ^ 0xFFFFFFFF;
/* 2077 */     return this;
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
/*      */   public DefaultShardManagerBuilder setEnabledIntents(@Nonnull GatewayIntent intent, @Nonnull GatewayIntent... intents) {
/* 2108 */     Checks.notNull(intent, "Intent");
/* 2109 */     Checks.noneNull((Object[])intents, "Intent");
/* 2110 */     EnumSet<GatewayIntent> set = EnumSet.of(intent, intents);
/* 2111 */     return setDisabledIntents(EnumSet.complementOf(set));
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
/*      */   public DefaultShardManagerBuilder setEnabledIntents(@Nullable Collection<GatewayIntent> intents) {
/* 2137 */     if (intents == null || intents.isEmpty()) {
/* 2138 */       setDisabledIntents(EnumSet.allOf(GatewayIntent.class));
/* 2139 */     } else if (intents instanceof EnumSet) {
/* 2140 */       setDisabledIntents(EnumSet.complementOf((EnumSet<GatewayIntent>)intents));
/*      */     } else {
/* 2142 */       setDisabledIntents(EnumSet.complementOf(EnumSet.copyOf(intents)));
/* 2143 */     }  return this;
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
/*      */   public DefaultShardManagerBuilder enableIntents(@Nonnull Collection<GatewayIntent> intents) {
/* 2163 */     Checks.noneNull(intents, "GatewayIntent");
/* 2164 */     int raw = GatewayIntent.getRaw(intents);
/* 2165 */     this.intents |= raw;
/* 2166 */     return this;
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
/*      */   public DefaultShardManagerBuilder enableIntents(@Nonnull GatewayIntent intent, @Nonnull GatewayIntent... intents) {
/* 2188 */     Checks.notNull(intent, "GatewayIntent");
/* 2189 */     Checks.noneNull((Object[])intents, "GatewayIntent");
/* 2190 */     int raw = GatewayIntent.getRaw(intent, intents);
/* 2191 */     this.intents |= raw;
/* 2192 */     return this;
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
/*      */   public DefaultShardManagerBuilder setLargeThreshold(int threshold) {
/* 2211 */     this.largeThreshold = Math.max(50, Math.min(250, threshold));
/* 2212 */     return this;
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
/*      */   public DefaultShardManagerBuilder setMaxBufferSize(int bufferSize) {
/* 2234 */     Checks.notNegative(bufferSize, "The buffer size");
/* 2235 */     this.maxBufferSize = bufferSize;
/* 2236 */     return this;
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
/*      */   public ShardManager build() throws LoginException, IllegalArgumentException {
/* 2261 */     return build(true);
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
/*      */   public ShardManager build(boolean login) throws LoginException, IllegalArgumentException {
/* 2289 */     checkIntents();
/* 2290 */     boolean useShutdownNow = this.shardingFlags.contains(ShardingConfigFlag.SHUTDOWN_NOW);
/* 2291 */     ShardingConfig shardingConfig = new ShardingConfig(this.shardsTotal, useShutdownNow, this.intents, this.memberCachePolicy);
/* 2292 */     EventConfig eventConfig = new EventConfig(this.eventManagerProvider);
/* 2293 */     Objects.requireNonNull(eventConfig); this.listeners.forEach(eventConfig::addEventListener);
/* 2294 */     Objects.requireNonNull(eventConfig); this.listenerProviders.forEach(eventConfig::addEventListenerProvider);
/* 2295 */     PresenceProviderConfig presenceConfig = new PresenceProviderConfig();
/* 2296 */     presenceConfig.setActivityProvider(this.activityProvider);
/* 2297 */     presenceConfig.setStatusProvider(this.statusProvider);
/* 2298 */     presenceConfig.setIdleProvider(this.idleProvider);
/* 2299 */     ThreadingProviderConfig threadingConfig = new ThreadingProviderConfig(this.rateLimitPoolProvider, this.gatewayPoolProvider, this.callbackPoolProvider, this.eventPoolProvider, this.audioPoolProvider, this.threadFactory);
/* 2300 */     ShardingSessionConfig sessionConfig = new ShardingSessionConfig(this.sessionController, this.voiceDispatchInterceptor, this.httpClient, this.httpClientBuilder, this.wsFactory, this.audioSendFactory, this.flags, this.shardingFlags, this.maxReconnectDelay, this.largeThreshold);
/* 2301 */     ShardingMetaConfig metaConfig = new ShardingMetaConfig(this.maxBufferSize, this.contextProvider, this.cacheFlags, this.flags, this.compression, this.encoding);
/* 2302 */     DefaultShardManager manager = new DefaultShardManager(this.token, this.shards, shardingConfig, eventConfig, presenceConfig, threadingConfig, sessionConfig, metaConfig, this.chunkingFilter);
/*      */     
/* 2304 */     if (login) {
/* 2305 */       manager.login();
/*      */     }
/* 2307 */     return manager;
/*      */   }
/*      */ 
/*      */   
/*      */   private DefaultShardManagerBuilder setFlag(ConfigFlag flag, boolean enable) {
/* 2312 */     if (enable) {
/* 2313 */       this.flags.add(flag);
/*      */     } else {
/* 2315 */       this.flags.remove(flag);
/* 2316 */     }  return this;
/*      */   }
/*      */ 
/*      */   
/*      */   private DefaultShardManagerBuilder setFlag(ShardingConfigFlag flag, boolean enable) {
/* 2321 */     if (enable) {
/* 2322 */       this.shardingFlags.add(flag);
/*      */     } else {
/* 2324 */       this.shardingFlags.remove(flag);
/* 2325 */     }  return this;
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkIntents() {
/* 2330 */     boolean membersIntent = ((this.intents & GatewayIntent.GUILD_MEMBERS.getRawValue()) != 0);
/* 2331 */     if (!membersIntent && this.memberCachePolicy == MemberCachePolicy.ALL)
/* 2332 */       throw new IllegalStateException("Cannot use MemberCachePolicy.ALL without GatewayIntent.GUILD_MEMBERS enabled!"); 
/* 2333 */     if (!membersIntent && this.chunkingFilter != ChunkingFilter.NONE) {
/* 2334 */       DefaultShardManager.LOG.warn("Member chunking is disabled due to missing GUILD_MEMBERS intent.");
/*      */     }
/* 2336 */     if (!this.automaticallyDisabled.isEmpty()) {
/*      */       
/* 2338 */       JDAImpl.LOG.warn("Automatically disabled CacheFlags due to missing intents");
/*      */ 
/*      */ 
/*      */       
/* 2342 */       Objects.requireNonNull(JDAImpl.LOG); this.automaticallyDisabled.stream().map(it -> "Disabled CacheFlag." + it + " (missing GatewayIntent." + it.getRequiredIntent() + ")").forEach(JDAImpl.LOG::warn);
/*      */ 
/*      */       
/* 2345 */       JDAImpl.LOG.warn("You can manually disable these flags to remove this warning by using disableCache({}) on your DefaultShardManagerBuilder", this.automaticallyDisabled
/* 2346 */           .stream()
/* 2347 */           .map(it -> "CacheFlag." + it)
/* 2348 */           .collect(Collectors.joining(", ")));
/*      */       
/* 2350 */       this.automaticallyDisabled.clear();
/*      */     } 
/*      */     
/* 2353 */     if (this.cacheFlags.isEmpty()) {
/*      */       return;
/*      */     }
/* 2356 */     EnumSet<GatewayIntent> providedIntents = GatewayIntent.getIntents(this.intents);
/* 2357 */     for (CacheFlag flag : this.cacheFlags) {
/*      */       
/* 2359 */       GatewayIntent intent = flag.getRequiredIntent();
/* 2360 */       if (intent != null && !providedIntents.contains(intent))
/* 2361 */         throw new IllegalArgumentException("Cannot use CacheFlag." + flag + " without GatewayIntent." + intent + "!"); 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static class ThreadPoolProviderImpl<T extends ExecutorService>
/*      */     implements ThreadPoolProvider<T>
/*      */   {
/*      */     private final boolean autoShutdown;
/*      */     private final T pool;
/*      */     
/*      */     public ThreadPoolProviderImpl(T pool, boolean autoShutdown) {
/* 2372 */       this.autoShutdown = autoShutdown;
/* 2373 */       this.pool = pool;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public T provide(int shardId) {
/* 2379 */       return this.pool;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean shouldShutdownAutomatically(int shardId) {
/* 2385 */       return this.autoShutdown;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\sharding\DefaultShardManagerBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */