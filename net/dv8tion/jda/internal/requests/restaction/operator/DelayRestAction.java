/*    */ package net.dv8tion.jda.internal.requests.restaction.operator;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.ScheduledExecutorService;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.function.Consumer;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.exceptions.RateLimitedException;
/*    */ import net.dv8tion.jda.api.requests.RestAction;
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
/*    */ public class DelayRestAction<T>
/*    */   extends RestActionOperator<T, T>
/*    */ {
/*    */   private final TimeUnit unit;
/*    */   private final long delay;
/*    */   private final ScheduledExecutorService scheduler;
/*    */   
/*    */   public DelayRestAction(RestAction<T> action, TimeUnit unit, long delay, ScheduledExecutorService scheduler) {
/* 37 */     super(action);
/* 38 */     this.unit = unit;
/* 39 */     this.delay = delay;
/* 40 */     this.scheduler = (scheduler == null) ? action.getJDA().getRateLimitPool() : scheduler;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void queue(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure) {
/* 46 */     this.action.queue(result -> this.scheduler.schedule((), this.delay, this.unit), 
/*    */ 
/*    */ 
/*    */         
/* 50 */         contextWrap(failure));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public T complete(boolean shouldQueue) throws RateLimitedException {
/* 56 */     T result = (T)this.action.complete(shouldQueue);
/*    */     
/*    */     try {
/* 59 */       this.unit.sleep(this.delay);
/* 60 */       return result;
/*    */     }
/* 62 */     catch (InterruptedException e) {
/*    */       
/* 64 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public CompletableFuture<T> submit(boolean shouldQueue) {
/* 72 */     CompletableFuture<T> future = new CompletableFuture<>();
/* 73 */     Objects.requireNonNull(future); Objects.requireNonNull(future); queue(future::complete, future::completeExceptionally);
/* 74 */     return future;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\operator\DelayRestAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */