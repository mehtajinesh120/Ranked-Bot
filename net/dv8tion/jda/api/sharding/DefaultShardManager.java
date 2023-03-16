/*     */ package net.dv8tion.jda.api.sharding;
/*     */ 
/*     */ import gnu.trove.set.TIntSet;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.security.auth.login.LoginException;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.OnlineStatus;
/*     */ import net.dv8tion.jda.api.entities.Activity;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.SelfUser;
/*     */ import net.dv8tion.jda.api.requests.GatewayIntent;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.utils.ChunkingFilter;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.api.utils.SessionController;
/*     */ import net.dv8tion.jda.api.utils.cache.ShardCacheView;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.SelfUserImpl;
/*     */ import net.dv8tion.jda.internal.managers.PresenceImpl;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.JDALogger;
/*     */ import net.dv8tion.jda.internal.utils.UnlockHook;
/*     */ import net.dv8tion.jda.internal.utils.cache.ShardCacheViewImpl;
/*     */ import net.dv8tion.jda.internal.utils.config.AuthorizationConfig;
/*     */ import net.dv8tion.jda.internal.utils.config.MetaConfig;
/*     */ import net.dv8tion.jda.internal.utils.config.SessionConfig;
/*     */ import net.dv8tion.jda.internal.utils.config.ThreadingConfig;
/*     */ import net.dv8tion.jda.internal.utils.config.sharding.EventConfig;
/*     */ import net.dv8tion.jda.internal.utils.config.sharding.PresenceProviderConfig;
/*     */ import net.dv8tion.jda.internal.utils.config.sharding.ShardingConfig;
/*     */ import net.dv8tion.jda.internal.utils.config.sharding.ShardingMetaConfig;
/*     */ import net.dv8tion.jda.internal.utils.config.sharding.ShardingSessionConfig;
/*     */ import net.dv8tion.jda.internal.utils.config.sharding.ThreadingProviderConfig;
/*     */ import okhttp3.OkHttpClient;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultShardManager
/*     */   implements ShardManager
/*     */ {
/*  66 */   public static final Logger LOG = JDALogger.getLog(ShardManager.class); public static final ThreadFactory DEFAULT_THREAD_FACTORY; protected final ScheduledExecutorService executor; static {
/*  67 */     DEFAULT_THREAD_FACTORY = (r -> {
/*     */         Thread t = new Thread(r, "DefaultShardManager");
/*     */         t.setPriority(6);
/*     */         return t;
/*     */       });
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
/*  82 */   protected final Queue<Integer> queue = new ConcurrentLinkedQueue<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ShardCacheViewImpl shards;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   protected final AtomicBoolean shutdown = new AtomicBoolean(false);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Thread shutdownHook;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String token;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Future<?> worker;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String gatewayURL;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final PresenceProviderConfig presenceConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final EventConfig eventConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ShardingConfig shardingConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ThreadingProviderConfig threadingConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ShardingSessionConfig sessionConfig;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ShardingMetaConfig metaConfig;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ChunkingFilter chunkingFilter;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultShardManager(@Nonnull String token) {
/* 151 */     this(token, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultShardManager(@Nonnull String token, @Nullable Collection<Integer> shardIds) {
/* 156 */     this(token, shardIds, null, null, null, null, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultShardManager(@Nonnull String token, @Nullable Collection<Integer> shardIds, @Nullable ShardingConfig shardingConfig, @Nullable EventConfig eventConfig, @Nullable PresenceProviderConfig presenceConfig, @Nullable ThreadingProviderConfig threadingConfig, @Nullable ShardingSessionConfig sessionConfig, @Nullable ShardingMetaConfig metaConfig, @Nullable ChunkingFilter chunkingFilter) {
/* 166 */     this.token = token;
/* 167 */     this.eventConfig = (eventConfig == null) ? EventConfig.getDefault() : eventConfig;
/* 168 */     this.shardingConfig = (shardingConfig == null) ? ShardingConfig.getDefault() : shardingConfig;
/* 169 */     this.threadingConfig = (threadingConfig == null) ? ThreadingProviderConfig.getDefault() : threadingConfig;
/* 170 */     this.sessionConfig = (sessionConfig == null) ? ShardingSessionConfig.getDefault() : sessionConfig;
/* 171 */     this.presenceConfig = (presenceConfig == null) ? PresenceProviderConfig.getDefault() : presenceConfig;
/* 172 */     this.metaConfig = (metaConfig == null) ? ShardingMetaConfig.getDefault() : metaConfig;
/* 173 */     this.chunkingFilter = (chunkingFilter == null) ? ChunkingFilter.ALL : chunkingFilter;
/* 174 */     this.executor = createExecutor(this.threadingConfig.getThreadFactory());
/* 175 */     this.shutdownHook = this.metaConfig.isUseShutdownHook() ? new Thread(this::shutdown, "JDA Shutdown Hook") : null;
/*     */     
/* 177 */     synchronized (this.queue) {
/*     */       
/* 179 */       if (getShardsTotal() != -1)
/*     */       {
/* 181 */         if (shardIds == null) {
/*     */           
/* 183 */           this.shards = new ShardCacheViewImpl(getShardsTotal());
/* 184 */           for (int i = 0; i < getShardsTotal(); i++) {
/* 185 */             this.queue.add(Integer.valueOf(i));
/*     */           }
/*     */         } else {
/*     */           
/* 189 */           this.shards = new ShardCacheViewImpl(shardIds.size());
/* 190 */           Objects.requireNonNull(this.queue); shardIds.stream().distinct().sorted().forEach(this.queue::add);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<GatewayIntent> getGatewayIntents() {
/* 200 */     return GatewayIntent.getIntents(this.shardingConfig.getIntents());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addEventListener(@Nonnull Object... listeners) {
/* 206 */     super.addEventListener(listeners);
/* 207 */     for (Object o : listeners) {
/* 208 */       this.eventConfig.addEventListener(o);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeEventListener(@Nonnull Object... listeners) {
/* 214 */     super.removeEventListener(listeners);
/* 215 */     for (Object o : listeners) {
/* 216 */       this.eventConfig.removeEventListener(o);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEventListeners(@Nonnull IntFunction<Object> eventListenerProvider) {
/* 222 */     super.addEventListeners(eventListenerProvider);
/* 223 */     this.eventConfig.addEventListenerProvider(eventListenerProvider);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeEventListenerProvider(@Nonnull IntFunction<Object> eventListenerProvider) {
/* 229 */     this.eventConfig.removeEventListenerProvider(eventListenerProvider);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getShardsQueued() {
/* 235 */     return this.queue.size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getShardsTotal() {
/* 241 */     return this.shardingConfig.getShardsTotal();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Guild getGuildById(long id) {
/* 247 */     int shardId = MiscUtil.getShardForGuild(id, getShardsTotal());
/* 248 */     JDA shard = getShardById(shardId);
/* 249 */     return (shard == null) ? null : shard.getGuildById(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ShardCacheView getShardCache() {
/* 256 */     return (ShardCacheView)this.shards;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void login() throws LoginException {
/* 263 */     JDAImpl jda = null;
/*     */     
/*     */     try {
/* 266 */       int shardId = this.queue.isEmpty() ? 0 : ((Integer)this.queue.peek()).intValue();
/*     */       
/* 268 */       jda = buildInstance(shardId);
/* 269 */       UnlockHook hook = this.shards.writeLock();
/*     */       
/* 271 */       try { this.shards.getMap().put(shardId, jda);
/* 272 */         if (hook != null) hook.close();  } catch (Throwable throwable) { if (hook != null)
/* 273 */           try { hook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  synchronized (this.queue)
/*     */       {
/* 275 */         this.queue.remove(Integer.valueOf(shardId));
/*     */       }
/*     */     
/* 278 */     } catch (Exception e) {
/*     */       
/* 280 */       if (jda != null)
/*     */       {
/* 282 */         if (this.shardingConfig.isUseShutdownNow()) {
/* 283 */           jda.shutdownNow();
/*     */         } else {
/* 285 */           jda.shutdown();
/*     */         } 
/*     */       }
/* 288 */       throw e;
/*     */     } 
/*     */     
/* 291 */     runQueueWorker();
/*     */ 
/*     */     
/* 294 */     if (this.shutdownHook != null) {
/* 295 */       Runtime.getRuntime().addShutdownHook(this.shutdownHook);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void restart(int shardId) {
/* 301 */     Checks.notNegative(shardId, "shardId");
/* 302 */     Checks.check((shardId < getShardsTotal()), "shardId must be lower than shardsTotal");
/*     */     
/* 304 */     JDA jda = this.shards.remove(shardId);
/* 305 */     if (jda != null)
/*     */     {
/* 307 */       if (this.shardingConfig.isUseShutdownNow()) {
/* 308 */         jda.shutdownNow();
/*     */       } else {
/* 310 */         jda.shutdown();
/*     */       } 
/*     */     }
/* 313 */     enqueueShard(shardId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void restart() {
/* 319 */     TIntSet map = this.shards.keySet();
/*     */     
/* 321 */     Arrays.stream(map.toArray())
/* 322 */       .sorted()
/* 323 */       .forEach(this::restart);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 329 */     if (this.shutdown.getAndSet(true)) {
/*     */       return;
/*     */     }
/* 332 */     if (this.worker != null && !this.worker.isDone()) {
/* 333 */       this.worker.cancel(true);
/*     */     }
/* 335 */     if (this.shutdownHook != null) {
/*     */       
/*     */       try {
/*     */         
/* 339 */         Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
/*     */       }
/* 341 */       catch (Exception exception) {}
/*     */     }
/*     */     
/* 344 */     if (this.shards != null) {
/*     */       
/* 346 */       this.executor.execute(() -> {
/*     */             synchronized (this.queue) {
/*     */               this.shards.forEach(());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               this.queue.clear();
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             this.executor.shutdown();
/*     */           });
/*     */     } else {
/* 363 */       this.executor.shutdown();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown(int shardId) {
/* 370 */     JDA jda = this.shards.remove(shardId);
/* 371 */     if (jda != null)
/*     */     {
/* 373 */       if (this.shardingConfig.isUseShutdownNow()) {
/* 374 */         jda.shutdownNow();
/*     */       } else {
/* 376 */         jda.shutdown();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void start(int shardId) {
/* 383 */     Checks.notNegative(shardId, "shardId");
/* 384 */     Checks.check((shardId < getShardsTotal()), "shardId must be lower than shardsTotal");
/* 385 */     enqueueShard(shardId);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void enqueueShard(int shardId) {
/* 390 */     synchronized (this.queue) {
/*     */       
/* 392 */       this.queue.add(Integer.valueOf(shardId));
/* 393 */       runQueueWorker();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void runQueueWorker() {
/* 399 */     if (this.shutdown.get())
/* 400 */       throw new RejectedExecutionException("ShardManager is already shutdown!"); 
/* 401 */     if (this.worker != null)
/*     */       return; 
/* 403 */     this.worker = this.executor.submit(() -> {
/*     */           while (!this.queue.isEmpty() && !Thread.currentThread().isInterrupted()) {
/*     */             processQueue();
/*     */           }
/*     */           this.gatewayURL = null;
/*     */           synchronized (this.queue) {
/*     */             this.worker = null;
/*     */             if (!this.shutdown.get() && !this.queue.isEmpty()) {
/*     */               runQueueWorker();
/*     */             }
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processQueue() {
/*     */     int shardId;
/*     */     JDAImpl api;
/* 421 */     if (this.shards == null) {
/*     */       
/* 423 */       shardId = 0;
/*     */     }
/*     */     else {
/*     */       
/* 427 */       Integer tmp = this.queue.peek();
/*     */       
/* 429 */       shardId = (tmp == null) ? -1 : tmp.intValue();
/*     */     } 
/*     */     
/* 432 */     if (shardId == -1) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 438 */       api = (this.shards == null) ? null : (JDAImpl)this.shards.getElementById(shardId);
/*     */       
/* 440 */       if (api == null) {
/* 441 */         api = buildInstance(shardId);
/*     */       }
/* 443 */     } catch (CompletionException e) {
/*     */       
/* 445 */       if (e.getCause() instanceof InterruptedException) {
/* 446 */         LOG.debug("The worker thread was interrupted");
/*     */       } else {
/* 448 */         LOG.error("Caught an exception in queue processing thread", e);
/*     */       } 
/*     */       return;
/* 451 */     } catch (LoginException e) {
/*     */ 
/*     */ 
/*     */       
/* 455 */       LOG.warn("The token has been invalidated and the ShardManager will shutdown!", e);
/* 456 */       shutdown();
/*     */       
/*     */       return;
/* 459 */     } catch (Exception e) {
/*     */       
/* 461 */       LOG.error("Caught an exception in the queue processing thread", e);
/*     */       
/*     */       return;
/*     */     } 
/* 465 */     UnlockHook hook = this.shards.writeLock();
/*     */     
/* 467 */     try { this.shards.getMap().put(shardId, api);
/* 468 */       if (hook != null) hook.close();  } catch (Throwable throwable) { if (hook != null)
/* 469 */         try { hook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  synchronized (this.queue) {
/*     */       
/* 471 */       this.queue.remove(Integer.valueOf(shardId));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected JDAImpl buildInstance(int shardId) throws LoginException {
/*     */     SelfUserImpl selfUserImpl;
/* 477 */     OkHttpClient httpClient = this.sessionConfig.getHttpClient();
/* 478 */     if (httpClient == null)
/*     */     {
/*     */ 
/*     */       
/* 482 */       httpClient = this.sessionConfig.getHttpBuilder().build();
/*     */     }
/*     */ 
/*     */     
/* 486 */     ExecutorPair<ScheduledExecutorService> rateLimitPair = resolveExecutor(this.threadingConfig.getRateLimitPoolProvider(), shardId);
/* 487 */     ScheduledExecutorService rateLimitPool = (ScheduledExecutorService)rateLimitPair.executor;
/* 488 */     boolean shutdownRateLimitPool = rateLimitPair.automaticShutdown;
/*     */     
/* 490 */     ExecutorPair<ScheduledExecutorService> gatewayPair = resolveExecutor(this.threadingConfig.getGatewayPoolProvider(), shardId);
/* 491 */     ScheduledExecutorService gatewayPool = (ScheduledExecutorService)gatewayPair.executor;
/* 492 */     boolean shutdownGatewayPool = gatewayPair.automaticShutdown;
/*     */     
/* 494 */     ExecutorPair<ExecutorService> callbackPair = resolveExecutor(this.threadingConfig.getCallbackPoolProvider(), shardId);
/* 495 */     E e1 = callbackPair.executor;
/* 496 */     boolean shutdownCallbackPool = callbackPair.automaticShutdown;
/*     */     
/* 498 */     ExecutorPair<ExecutorService> eventPair = resolveExecutor(this.threadingConfig.getEventPoolProvider(), shardId);
/* 499 */     E e2 = eventPair.executor;
/* 500 */     boolean shutdownEventPool = eventPair.automaticShutdown;
/*     */     
/* 502 */     ExecutorPair<ScheduledExecutorService> audioPair = resolveExecutor(this.threadingConfig.getAudioPoolProvider(), shardId);
/* 503 */     ScheduledExecutorService audioPool = (ScheduledExecutorService)audioPair.executor;
/* 504 */     boolean shutdownAudioPool = audioPair.automaticShutdown;
/*     */     
/* 506 */     AuthorizationConfig authConfig = new AuthorizationConfig(this.token);
/* 507 */     SessionConfig sessionConfig = this.sessionConfig.toSessionConfig(httpClient);
/* 508 */     ThreadingConfig threadingConfig = new ThreadingConfig();
/* 509 */     threadingConfig.setRateLimitPool(rateLimitPool, shutdownRateLimitPool);
/* 510 */     threadingConfig.setGatewayPool(gatewayPool, shutdownGatewayPool);
/* 511 */     threadingConfig.setCallbackPool((ExecutorService)e1, shutdownCallbackPool);
/* 512 */     threadingConfig.setEventPool((ExecutorService)e2, shutdownEventPool);
/* 513 */     threadingConfig.setAudioPool(audioPool, shutdownAudioPool);
/* 514 */     MetaConfig metaConfig = new MetaConfig(this.metaConfig.getMaxBufferSize(), this.metaConfig.getContextMap(shardId), this.metaConfig.getCacheFlags(), this.sessionConfig.getFlags());
/* 515 */     JDAImpl jda = new JDAImpl(authConfig, sessionConfig, threadingConfig, metaConfig);
/* 516 */     jda.setMemberCachePolicy(this.shardingConfig.getMemberCachePolicy());
/* 517 */     Objects.requireNonNull(jda); threadingConfig.init(jda::getIdentifierString);
/*     */     
/* 519 */     if ((this.shardingConfig.getIntents() & GatewayIntent.GUILD_MEMBERS.getRawValue()) == 0) {
/* 520 */       jda.setChunkingFilter(ChunkingFilter.NONE);
/*     */     } else {
/* 522 */       jda.setChunkingFilter(this.chunkingFilter);
/*     */     } 
/* 524 */     jda.setShardManager(this);
/*     */     
/* 526 */     if (this.eventConfig.getEventManagerProvider() != null) {
/* 527 */       jda.setEventManager(this.eventConfig.getEventManagerProvider().apply(shardId));
/*     */     }
/* 529 */     if (this.sessionConfig.getAudioSendFactory() != null) {
/* 530 */       jda.setAudioSendFactory(this.sessionConfig.getAudioSendFactory());
/*     */     }
/* 532 */     Objects.requireNonNull(jda); this.eventConfig.getListeners().forEach(xva$0 -> rec$.addEventListener(new Object[] { xva$0 }));
/* 533 */     this.eventConfig.getListenerProviders().forEach(provider -> jda.addEventListener(new Object[] { provider.apply(shardId) }));
/*     */ 
/*     */     
/* 536 */     PresenceImpl presence = (PresenceImpl)jda.getPresence();
/* 537 */     if (this.presenceConfig.getActivityProvider() != null)
/* 538 */       presence.setCacheActivity(this.presenceConfig.getActivityProvider().apply(shardId)); 
/* 539 */     if (this.presenceConfig.getIdleProvider() != null)
/* 540 */       presence.setCacheIdle(((Boolean)this.presenceConfig.getIdleProvider().apply(shardId)).booleanValue()); 
/* 541 */     if (this.presenceConfig.getStatusProvider() != null) {
/* 542 */       presence.setCacheStatus(this.presenceConfig.getStatusProvider().apply(shardId));
/*     */     }
/* 544 */     if (this.gatewayURL == null) {
/*     */       
/*     */       try {
/*     */         
/* 548 */         SessionController.ShardedGateway gateway = jda.getShardedGateway();
/* 549 */         this.sessionConfig.getSessionController().setConcurrency(gateway.getConcurrency());
/* 550 */         this.gatewayURL = gateway.getUrl();
/* 551 */         if (this.gatewayURL == null) {
/* 552 */           LOG.error("Acquired null gateway url from SessionController");
/*     */         } else {
/* 554 */           LOG.info("Login Successful!");
/*     */         } 
/* 556 */         if (getShardsTotal() == -1) {
/*     */           
/* 558 */           this.shardingConfig.setShardsTotal(gateway.getShardTotal());
/* 559 */           this.shards = new ShardCacheViewImpl(getShardsTotal());
/*     */           
/* 561 */           synchronized (this.queue) {
/*     */             
/* 563 */             for (int i = 0; i < getShardsTotal(); i++) {
/* 564 */               this.queue.add(Integer.valueOf(i));
/*     */             }
/*     */           } 
/*     */         } 
/* 568 */       } catch (CompletionException e) {
/*     */         
/* 570 */         if (e.getCause() instanceof LoginException)
/* 571 */           throw (LoginException)e.getCause(); 
/* 572 */         throw e;
/*     */       } 
/*     */     }
/*     */     
/* 576 */     JDA.ShardInfo shardInfo = new JDA.ShardInfo(shardId, getShardsTotal());
/*     */ 
/*     */     
/* 579 */     SelfUser selfUser = (SelfUser)getShardCache().applyStream(s -> (SelfUser)s.map(JDA::getSelfUser).findFirst().orElse(null));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 585 */     if (selfUser == null) {
/* 586 */       selfUser = retrieveSelfUser(jda);
/*     */     } else {
/* 588 */       selfUserImpl = SelfUserImpl.copyOf((SelfUserImpl)selfUser, jda);
/*     */     } 
/* 590 */     jda.setSelfUser((SelfUser)selfUserImpl);
/* 591 */     jda.setStatus(JDA.Status.INITIALIZED);
/*     */     
/* 593 */     int shardTotal = jda.login(this.gatewayURL, shardInfo, this.metaConfig.getCompression(), false, this.shardingConfig.getIntents(), this.metaConfig.getEncoding());
/* 594 */     if (getShardsTotal() == -1) {
/* 595 */       this.shardingConfig.setShardsTotal(shardTotal);
/*     */     }
/* 597 */     return jda;
/*     */   }
/*     */ 
/*     */   
/*     */   private SelfUser retrieveSelfUser(JDAImpl jda) {
/* 602 */     Route.CompiledRoute route = Route.Self.GET_SELF.compile(new String[0]);
/* 603 */     return (SelfUser)(new RestActionImpl((JDA)jda, route, (response, request) -> jda.getEntityBuilder().createSelfUser(response.getObject())))
/*     */       
/* 605 */       .complete();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setActivityProvider(IntFunction<? extends Activity> activityProvider) {
/* 611 */     super.setActivityProvider(activityProvider);
/* 612 */     this.presenceConfig.setActivityProvider(activityProvider);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIdleProvider(@Nonnull IntFunction<Boolean> idleProvider) {
/* 618 */     super.setIdleProvider(idleProvider);
/* 619 */     this.presenceConfig.setIdleProvider(idleProvider);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPresenceProvider(IntFunction<OnlineStatus> statusProvider, IntFunction<? extends Activity> activityProvider) {
/* 625 */     super.setPresenceProvider(statusProvider, activityProvider);
/* 626 */     this.presenceConfig.setStatusProvider(statusProvider);
/* 627 */     this.presenceConfig.setActivityProvider(activityProvider);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatusProvider(IntFunction<OnlineStatus> statusProvider) {
/* 633 */     super.setStatusProvider(statusProvider);
/* 634 */     this.presenceConfig.setStatusProvider(statusProvider);
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
/*     */   protected ScheduledExecutorService createExecutor(ThreadFactory threadFactory) {
/* 647 */     ThreadFactory factory = (threadFactory == null) ? DEFAULT_THREAD_FACTORY : threadFactory;
/*     */     
/* 649 */     return Executors.newSingleThreadScheduledExecutor(factory);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static <E extends ExecutorService> ExecutorPair<E> resolveExecutor(ThreadPoolProvider<? extends E> provider, int shardId) {
/* 654 */     E executor = null;
/* 655 */     boolean automaticShutdown = true;
/* 656 */     if (provider != null) {
/*     */       
/* 658 */       executor = provider.provide(shardId);
/* 659 */       automaticShutdown = provider.shouldShutdownAutomatically(shardId);
/*     */     } 
/* 661 */     return new ExecutorPair<>(executor, automaticShutdown);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static class ExecutorPair<E extends ExecutorService>
/*     */   {
/*     */     protected final E executor;
/*     */     protected final boolean automaticShutdown;
/*     */     
/*     */     protected ExecutorPair(E executor, boolean automaticShutdown) {
/* 671 */       this.executor = executor;
/* 672 */       this.automaticShutdown = automaticShutdown;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\sharding\DefaultShardManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */