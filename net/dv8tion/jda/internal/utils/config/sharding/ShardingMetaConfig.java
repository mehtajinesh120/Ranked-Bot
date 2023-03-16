/*    */ package net.dv8tion.jda.internal.utils.config.sharding;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import java.util.function.IntFunction;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.GatewayEncoding;
/*    */ import net.dv8tion.jda.api.utils.Compression;
/*    */ import net.dv8tion.jda.api.utils.cache.CacheFlag;
/*    */ import net.dv8tion.jda.internal.utils.config.MetaConfig;
/*    */ import net.dv8tion.jda.internal.utils.config.flags.ConfigFlag;
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
/*    */ public class ShardingMetaConfig
/*    */   extends MetaConfig
/*    */ {
/* 33 */   private static final ShardingMetaConfig defaultConfig = new ShardingMetaConfig(2048, null, null, ConfigFlag.getDefault(), Compression.ZLIB, GatewayEncoding.JSON);
/*    */ 
/*    */   
/*    */   private final Compression compression;
/*    */   
/*    */   private final GatewayEncoding encoding;
/*    */   
/*    */   private final IntFunction<? extends ConcurrentMap<String, String>> contextProvider;
/*    */ 
/*    */   
/*    */   public ShardingMetaConfig(int maxBufferSize, @Nullable IntFunction<? extends ConcurrentMap<String, String>> contextProvider, @Nullable EnumSet<CacheFlag> cacheFlags, EnumSet<ConfigFlag> flags, Compression compression, GatewayEncoding encoding) {
/* 44 */     super(maxBufferSize, null, cacheFlags, flags);
/*    */     
/* 46 */     this.compression = compression;
/* 47 */     this.contextProvider = contextProvider;
/* 48 */     this.encoding = encoding;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public ConcurrentMap<String, String> getContextMap(int shardId) {
/* 54 */     return (this.contextProvider == null) ? null : this.contextProvider.apply(shardId);
/*    */   }
/*    */ 
/*    */   
/*    */   public Compression getCompression() {
/* 59 */     return this.compression;
/*    */   }
/*    */ 
/*    */   
/*    */   public GatewayEncoding getEncoding() {
/* 64 */     return this.encoding;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public IntFunction<? extends ConcurrentMap<String, String>> getContextProvider() {
/* 70 */     return this.contextProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public static ShardingMetaConfig getDefault() {
/* 76 */     return defaultConfig;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\config\sharding\ShardingMetaConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */