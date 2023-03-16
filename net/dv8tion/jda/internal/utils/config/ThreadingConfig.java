/*     */ package net.dv8tion.jda.internal.utils.config;
/*     */ 
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.ForkJoinPool;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.internal.utils.concurrent.CountingThreadFactory;
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
/*     */ public class ThreadingConfig
/*     */ {
/*  28 */   private final Object audioLock = new Object();
/*     */   
/*     */   private ScheduledExecutorService rateLimitPool;
/*     */   
/*     */   private ScheduledExecutorService gatewayPool;
/*     */   
/*     */   private ExecutorService callbackPool;
/*     */   private ExecutorService eventPool;
/*     */   private ScheduledExecutorService audioPool;
/*     */   private boolean shutdownRateLimitPool;
/*     */   private boolean shutdownGatewayPool;
/*     */   private boolean shutdownCallbackPool;
/*     */   private boolean shutdownEventPool;
/*     */   private boolean shutdownAudioPool;
/*     */   
/*     */   public ThreadingConfig() {
/*  44 */     this.callbackPool = ForkJoinPool.commonPool();
/*     */     
/*  46 */     this.shutdownRateLimitPool = true;
/*  47 */     this.shutdownGatewayPool = true;
/*  48 */     this.shutdownCallbackPool = false;
/*  49 */     this.shutdownAudioPool = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRateLimitPool(@Nullable ScheduledExecutorService executor, boolean shutdown) {
/*  54 */     this.rateLimitPool = executor;
/*  55 */     this.shutdownRateLimitPool = shutdown;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGatewayPool(@Nullable ScheduledExecutorService executor, boolean shutdown) {
/*  60 */     this.gatewayPool = executor;
/*  61 */     this.shutdownGatewayPool = shutdown;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCallbackPool(@Nullable ExecutorService executor, boolean shutdown) {
/*  66 */     this.callbackPool = (executor == null) ? ForkJoinPool.commonPool() : executor;
/*  67 */     this.shutdownCallbackPool = shutdown;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEventPool(@Nullable ExecutorService executor, boolean shutdown) {
/*  72 */     this.eventPool = executor;
/*  73 */     this.shutdownEventPool = shutdown;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAudioPool(@Nullable ScheduledExecutorService executor, boolean shutdown) {
/*  78 */     this.audioPool = executor;
/*  79 */     this.shutdownAudioPool = shutdown;
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(@Nonnull Supplier<String> identifier) {
/*  84 */     if (this.rateLimitPool == null)
/*  85 */       this.rateLimitPool = newScheduler(5, identifier, "RateLimit", false); 
/*  86 */     if (this.gatewayPool == null) {
/*  87 */       this.gatewayPool = newScheduler(1, identifier, "Gateway");
/*     */     }
/*     */   }
/*     */   
/*     */   public void shutdown() {
/*  92 */     if (this.shutdownCallbackPool)
/*  93 */       this.callbackPool.shutdown(); 
/*  94 */     if (this.shutdownGatewayPool)
/*  95 */       this.gatewayPool.shutdown(); 
/*  96 */     if (this.shutdownEventPool && this.eventPool != null)
/*  97 */       this.eventPool.shutdown(); 
/*  98 */     if (this.shutdownAudioPool && this.audioPool != null)
/*  99 */       this.audioPool.shutdown(); 
/* 100 */     if (this.shutdownRateLimitPool)
/*     */     {
/* 102 */       if (this.rateLimitPool instanceof ScheduledThreadPoolExecutor) {
/*     */         
/* 104 */         ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor)this.rateLimitPool;
/* 105 */         executor.setKeepAliveTime(5L, TimeUnit.SECONDS);
/* 106 */         executor.allowCoreThreadTimeOut(true);
/*     */       }
/*     */       else {
/*     */         
/* 110 */         this.rateLimitPool.shutdown();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdownRequester() {
/* 117 */     if (this.shutdownRateLimitPool) {
/* 118 */       this.rateLimitPool.shutdown();
/*     */     }
/*     */   }
/*     */   
/*     */   public void shutdownNow() {
/* 123 */     if (this.shutdownCallbackPool)
/* 124 */       this.callbackPool.shutdownNow(); 
/* 125 */     if (this.shutdownGatewayPool)
/* 126 */       this.gatewayPool.shutdownNow(); 
/* 127 */     if (this.shutdownRateLimitPool)
/* 128 */       this.rateLimitPool.shutdownNow(); 
/* 129 */     if (this.shutdownEventPool && this.eventPool != null)
/* 130 */       this.eventPool.shutdownNow(); 
/* 131 */     if (this.shutdownAudioPool && this.audioPool != null) {
/* 132 */       this.audioPool.shutdownNow();
/*     */     }
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   public ScheduledExecutorService getRateLimitPool() {
/* 138 */     return this.rateLimitPool;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ScheduledExecutorService getGatewayPool() {
/* 144 */     return this.gatewayPool;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ExecutorService getCallbackPool() {
/* 150 */     return this.callbackPool;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ExecutorService getEventPool() {
/* 156 */     return this.eventPool;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ScheduledExecutorService getAudioPool(@Nonnull Supplier<String> identifier) {
/* 162 */     ScheduledExecutorService pool = this.audioPool;
/* 163 */     if (pool == null)
/*     */     {
/* 165 */       synchronized (this.audioLock) {
/*     */         
/* 167 */         pool = this.audioPool;
/* 168 */         if (pool == null)
/* 169 */           pool = this.audioPool = newScheduler(1, identifier, "AudioLifeCycle"); 
/*     */       } 
/*     */     }
/* 172 */     return pool;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShutdownRateLimitPool() {
/* 177 */     return this.shutdownRateLimitPool;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShutdownGatewayPool() {
/* 182 */     return this.shutdownGatewayPool;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShutdownCallbackPool() {
/* 187 */     return this.shutdownCallbackPool;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShutdownEventPool() {
/* 192 */     return this.shutdownEventPool;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShutdownAudioPool() {
/* 197 */     return this.shutdownAudioPool;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static ScheduledThreadPoolExecutor newScheduler(int coreSize, Supplier<String> identifier, String baseName) {
/* 203 */     return newScheduler(coreSize, identifier, baseName, true);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static ScheduledThreadPoolExecutor newScheduler(int coreSize, Supplier<String> identifier, String baseName, boolean daemon) {
/* 209 */     return new ScheduledThreadPoolExecutor(coreSize, (ThreadFactory)new CountingThreadFactory(identifier, baseName, daemon));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static ThreadingConfig getDefault() {
/* 215 */     return new ThreadingConfig();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\config\ThreadingConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */