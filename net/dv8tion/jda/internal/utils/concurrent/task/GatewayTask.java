/*     */ package net.dv8tion.jda.internal.utils.concurrent.task;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.exceptions.ContextException;
/*     */ import net.dv8tion.jda.api.utils.concurrent.Task;
/*     */ import net.dv8tion.jda.internal.requests.WebSocketClient;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.JDALogger;
/*     */ import org.slf4j.Logger;
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
/*     */ public class GatewayTask<T>
/*     */   implements Task<T>
/*     */ {
/*  32 */   private static final Logger log = JDALogger.getLog(Task.class);
/*     */   
/*     */   private final Runnable onCancel;
/*     */   private final CompletableFuture<T> future;
/*     */   
/*     */   public GatewayTask(CompletableFuture<T> future, Runnable onCancel) {
/*  38 */     this.future = future;
/*  39 */     this.onCancel = onCancel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/*  45 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Task<T> onError(@Nonnull Consumer<? super Throwable> callback) {
/*  52 */     Checks.notNull(callback, "Callback");
/*  53 */     Consumer<Throwable> failureHandler = ContextException.here(error -> log.error("Task Failure callback threw error", error));
/*  54 */     this.future.exceptionally(error -> {
/*     */ 
/*     */           
/*     */           try {
/*     */             callback.accept(error);
/*  59 */           } catch (Throwable e) {
/*     */             failureHandler.accept(e);
/*     */             if (e instanceof Error) {
/*     */               throw e;
/*     */             }
/*     */           } 
/*     */           return null;
/*     */         });
/*  67 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Task<T> onSuccess(@Nonnull Consumer<? super T> callback) {
/*  74 */     Checks.notNull(callback, "Callback");
/*  75 */     Consumer<Throwable> failureHandler = ContextException.here(error -> log.error("Task Success callback threw error", error));
/*  76 */     this.future.thenAccept(result -> {
/*     */ 
/*     */           
/*     */           try {
/*     */             callback.accept(result);
/*  81 */           } catch (Throwable error) {
/*     */             failureHandler.accept(error);
/*     */             if (error instanceof Error) {
/*     */               throw error;
/*     */             }
/*     */           } 
/*     */         });
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public T get() {
/*  95 */     if (((Boolean)WebSocketClient.WS_THREAD.get()).booleanValue())
/*  96 */       throw new UnsupportedOperationException("Blocking operations are not permitted on the gateway thread"); 
/*  97 */     return this.future.join();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 103 */     this.onCancel.run();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\concurrent\task\GatewayTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */