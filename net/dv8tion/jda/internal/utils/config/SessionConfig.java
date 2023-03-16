/*     */ package net.dv8tion.jda.internal.utils.config;
/*     */ 
/*     */ import com.neovisionaries.ws.client.WebSocketFactory;
/*     */ import java.util.EnumSet;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.hooks.VoiceDispatchInterceptor;
/*     */ import net.dv8tion.jda.api.utils.ConcurrentSessionController;
/*     */ import net.dv8tion.jda.api.utils.SessionController;
/*     */ import net.dv8tion.jda.internal.utils.config.flags.ConfigFlag;
/*     */ import okhttp3.OkHttpClient;
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
/*     */ public class SessionConfig
/*     */ {
/*     */   private final SessionController sessionController;
/*     */   private final OkHttpClient httpClient;
/*     */   private final WebSocketFactory webSocketFactory;
/*     */   private final VoiceDispatchInterceptor interceptor;
/*     */   private final int largeThreshold;
/*     */   private EnumSet<ConfigFlag> flags;
/*     */   private int maxReconnectDelay;
/*     */   
/*     */   public SessionConfig(@Nullable SessionController sessionController, @Nullable OkHttpClient httpClient, @Nullable WebSocketFactory webSocketFactory, @Nullable VoiceDispatchInterceptor interceptor, EnumSet<ConfigFlag> flags, int maxReconnectDelay, int largeThreshold) {
/*  45 */     this.sessionController = (sessionController == null) ? (SessionController)new ConcurrentSessionController() : sessionController;
/*  46 */     this.httpClient = httpClient;
/*  47 */     this.webSocketFactory = (webSocketFactory == null) ? newWebSocketFactory() : webSocketFactory;
/*  48 */     this.interceptor = interceptor;
/*  49 */     this.flags = flags;
/*  50 */     this.maxReconnectDelay = maxReconnectDelay;
/*  51 */     this.largeThreshold = largeThreshold;
/*     */   }
/*     */ 
/*     */   
/*     */   private static WebSocketFactory newWebSocketFactory() {
/*  56 */     return (new WebSocketFactory()).setConnectionTimeout(10000);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAutoReconnect(boolean autoReconnect) {
/*  61 */     if (autoReconnect) {
/*  62 */       this.flags.add(ConfigFlag.AUTO_RECONNECT);
/*     */     } else {
/*  64 */       this.flags.remove(ConfigFlag.AUTO_RECONNECT);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   public SessionController getSessionController() {
/*  70 */     return this.sessionController;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public OkHttpClient getHttpClient() {
/*  76 */     return this.httpClient;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebSocketFactory getWebSocketFactory() {
/*  82 */     return this.webSocketFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public VoiceDispatchInterceptor getVoiceDispatchInterceptor() {
/*  88 */     return this.interceptor;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAutoReconnect() {
/*  93 */     return this.flags.contains(ConfigFlag.AUTO_RECONNECT);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRetryOnTimeout() {
/*  98 */     return this.flags.contains(ConfigFlag.RETRY_TIMEOUT);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBulkDeleteSplittingEnabled() {
/* 103 */     return this.flags.contains(ConfigFlag.BULK_DELETE_SPLIT);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRawEvents() {
/* 108 */     return this.flags.contains(ConfigFlag.RAW_EVENTS);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRelativeRateLimit() {
/* 113 */     return this.flags.contains(ConfigFlag.USE_RELATIVE_RATELIMIT);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxReconnectDelay() {
/* 118 */     return this.maxReconnectDelay;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLargeThreshold() {
/* 123 */     return this.largeThreshold;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumSet<ConfigFlag> getFlags() {
/* 128 */     return this.flags;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static SessionConfig getDefault() {
/* 134 */     return new SessionConfig(null, new OkHttpClient(), null, null, ConfigFlag.getDefault(), 900, 250);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\config\SessionConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */