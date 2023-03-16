/*    */ package net.dv8tion.jda.internal.utils.config;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.utils.cache.CacheFlag;
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
/*    */ 
/*    */ public class MetaConfig
/*    */ {
/* 30 */   private static final MetaConfig defaultConfig = new MetaConfig(2048, null, EnumSet.allOf(CacheFlag.class), ConfigFlag.getDefault());
/*    */   
/*    */   private final ConcurrentMap<String, String> mdcContextMap;
/*    */   
/*    */   private final EnumSet<CacheFlag> cacheFlags;
/*    */   
/*    */   private final boolean enableMDC;
/*    */   
/*    */   private final boolean useShutdownHook;
/*    */   private final int maxBufferSize;
/*    */   
/*    */   public MetaConfig(int maxBufferSize, @Nullable ConcurrentMap<String, String> mdcContextMap, @Nullable EnumSet<CacheFlag> cacheFlags, EnumSet<ConfigFlag> flags) {
/* 42 */     this.maxBufferSize = maxBufferSize;
/* 43 */     this.cacheFlags = (cacheFlags == null) ? EnumSet.<CacheFlag>allOf(CacheFlag.class) : cacheFlags;
/* 44 */     this.enableMDC = flags.contains(ConfigFlag.MDC_CONTEXT);
/* 45 */     if (this.enableMDC) {
/* 46 */       this.mdcContextMap = (mdcContextMap == null) ? new ConcurrentHashMap<>() : null;
/*    */     } else {
/* 48 */       this.mdcContextMap = null;
/* 49 */     }  this.useShutdownHook = flags.contains(ConfigFlag.SHUTDOWN_HOOK);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public ConcurrentMap<String, String> getMdcContextMap() {
/* 55 */     return this.mdcContextMap;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public EnumSet<CacheFlag> getCacheFlags() {
/* 61 */     return this.cacheFlags;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEnableMDC() {
/* 66 */     return this.enableMDC;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isUseShutdownHook() {
/* 71 */     return this.useShutdownHook;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxBufferSize() {
/* 76 */     return this.maxBufferSize;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public static MetaConfig getDefault() {
/* 82 */     return defaultConfig;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\config\MetaConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */