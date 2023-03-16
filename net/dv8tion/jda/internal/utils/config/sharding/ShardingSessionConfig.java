/*    */ package net.dv8tion.jda.internal.utils.config.sharding;
/*    */ 
/*    */ import com.neovisionaries.ws.client.WebSocketFactory;
/*    */ import java.util.EnumSet;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.audio.factory.IAudioSendFactory;
/*    */ import net.dv8tion.jda.api.hooks.VoiceDispatchInterceptor;
/*    */ import net.dv8tion.jda.api.utils.SessionController;
/*    */ import net.dv8tion.jda.internal.utils.IOUtil;
/*    */ import net.dv8tion.jda.internal.utils.config.SessionConfig;
/*    */ import net.dv8tion.jda.internal.utils.config.flags.ConfigFlag;
/*    */ import net.dv8tion.jda.internal.utils.config.flags.ShardingConfigFlag;
/*    */ import okhttp3.OkHttpClient;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ShardingSessionConfig
/*    */   extends SessionConfig
/*    */ {
/*    */   private final OkHttpClient.Builder builder;
/*    */   private final IAudioSendFactory audioSendFactory;
/*    */   private final EnumSet<ShardingConfigFlag> shardingFlags;
/*    */   
/*    */   public ShardingSessionConfig(@Nullable SessionController sessionController, @Nullable VoiceDispatchInterceptor interceptor, @Nullable OkHttpClient httpClient, @Nullable OkHttpClient.Builder httpClientBuilder, @Nullable WebSocketFactory webSocketFactory, @Nullable IAudioSendFactory audioSendFactory, EnumSet<ConfigFlag> flags, EnumSet<ShardingConfigFlag> shardingFlags, int maxReconnectDelay, int largeThreshold) {
/* 46 */     super(sessionController, httpClient, webSocketFactory, interceptor, flags, maxReconnectDelay, largeThreshold);
/* 47 */     if (httpClient == null) {
/* 48 */       this.builder = (httpClientBuilder == null) ? IOUtil.newHttpClientBuilder() : httpClientBuilder;
/*    */     } else {
/* 50 */       this.builder = null;
/* 51 */     }  this.audioSendFactory = audioSendFactory;
/* 52 */     this.shardingFlags = shardingFlags;
/*    */   }
/*    */ 
/*    */   
/*    */   public SessionConfig toSessionConfig(OkHttpClient client) {
/* 57 */     return new SessionConfig(getSessionController(), client, getWebSocketFactory(), getVoiceDispatchInterceptor(), getFlags(), getMaxReconnectDelay(), getLargeThreshold());
/*    */   }
/*    */ 
/*    */   
/*    */   public EnumSet<ShardingConfigFlag> getShardingFlags() {
/* 62 */     return this.shardingFlags;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public OkHttpClient.Builder getHttpBuilder() {
/* 68 */     return this.builder;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public IAudioSendFactory getAudioSendFactory() {
/* 74 */     return this.audioSendFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public static ShardingSessionConfig getDefault() {
/* 80 */     return new ShardingSessionConfig(null, null, new OkHttpClient(), null, null, null, ConfigFlag.getDefault(), ShardingConfigFlag.getDefault(), 900, 250);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\config\sharding\ShardingSessionConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */