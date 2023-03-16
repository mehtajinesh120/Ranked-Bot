/*     */ package net.dv8tion.jda.api.utils.concurrent;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Delayed;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.Function;
/*     */ import javax.annotation.Nonnull;
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
/*     */ public class DelayedCompletableFuture<T>
/*     */   extends CompletableFuture<T>
/*     */   implements ScheduledFuture<T>
/*     */ {
/*     */   private ScheduledFuture<?> future;
/*     */   
/*     */   @Nonnull
/*     */   public static <E> DelayedCompletableFuture<E> make(@Nonnull ScheduledExecutorService executor, long delay, @Nonnull TimeUnit unit, @Nonnull Function<? super DelayedCompletableFuture<E>, ? extends Runnable> mapping) {
/*  59 */     DelayedCompletableFuture<E> handle = new DelayedCompletableFuture<>();
/*  60 */     ScheduledFuture<?> future = executor.schedule(mapping.apply(handle), delay, unit);
/*  61 */     handle.initProxy(future);
/*  62 */     return handle;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initProxy(ScheduledFuture<?> future) {
/*  79 */     if (this.future == null) {
/*  80 */       this.future = future;
/*     */     } else {
/*  82 */       throw new IllegalStateException("Cannot initialize twice");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  88 */     if (this.future != null && !this.future.isDone())
/*  89 */       this.future.cancel(mayInterruptIfRunning); 
/*  90 */     return super.cancel(mayInterruptIfRunning);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDelay(@Nonnull TimeUnit unit) {
/*  96 */     return this.future.getDelay(unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(@Nonnull Delayed o) {
/* 102 */     return this.future.compareTo(o);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\concurrent\DelayedCompletableFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */