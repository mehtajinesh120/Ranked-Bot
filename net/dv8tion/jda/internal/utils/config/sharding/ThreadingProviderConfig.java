/*    */ package net.dv8tion.jda.internal.utils.config.sharding;
/*    */ 
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.ScheduledExecutorService;
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.sharding.ThreadPoolProvider;
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
/*    */ 
/*    */ 
/*    */ public class ThreadingProviderConfig
/*    */ {
/*    */   private final ThreadPoolProvider<? extends ScheduledExecutorService> rateLimitPoolProvider;
/*    */   private final ThreadPoolProvider<? extends ScheduledExecutorService> gatewayPoolProvider;
/*    */   private final ThreadPoolProvider<? extends ExecutorService> callbackPoolProvider;
/*    */   private final ThreadPoolProvider<? extends ExecutorService> eventPoolProvider;
/*    */   private final ThreadPoolProvider<? extends ScheduledExecutorService> audioPoolProvider;
/*    */   private final ThreadFactory threadFactory;
/*    */   
/*    */   public ThreadingProviderConfig(@Nullable ThreadPoolProvider<? extends ScheduledExecutorService> rateLimitPoolProvider, @Nullable ThreadPoolProvider<? extends ScheduledExecutorService> gatewayPoolProvider, @Nullable ThreadPoolProvider<? extends ExecutorService> callbackPoolProvider, @Nullable ThreadPoolProvider<? extends ExecutorService> eventPoolProvider, @Nullable ThreadPoolProvider<? extends ScheduledExecutorService> audioPoolProvider, @Nullable ThreadFactory threadFactory) {
/* 44 */     this.rateLimitPoolProvider = rateLimitPoolProvider;
/* 45 */     this.gatewayPoolProvider = gatewayPoolProvider;
/* 46 */     this.callbackPoolProvider = callbackPoolProvider;
/* 47 */     this.eventPoolProvider = eventPoolProvider;
/* 48 */     this.audioPoolProvider = audioPoolProvider;
/* 49 */     this.threadFactory = threadFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public ThreadFactory getThreadFactory() {
/* 55 */     return this.threadFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public ThreadPoolProvider<? extends ScheduledExecutorService> getRateLimitPoolProvider() {
/* 61 */     return this.rateLimitPoolProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public ThreadPoolProvider<? extends ScheduledExecutorService> getGatewayPoolProvider() {
/* 67 */     return this.gatewayPoolProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public ThreadPoolProvider<? extends ExecutorService> getCallbackPoolProvider() {
/* 73 */     return this.callbackPoolProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public ThreadPoolProvider<? extends ExecutorService> getEventPoolProvider() {
/* 79 */     return this.eventPoolProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public ThreadPoolProvider<? extends ScheduledExecutorService> getAudioPoolProvider() {
/* 85 */     return this.audioPoolProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public static ThreadingProviderConfig getDefault() {
/* 91 */     return new ThreadingProviderConfig(null, null, null, null, null, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\config\sharding\ThreadingProviderConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */