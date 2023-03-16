/*     */ package net.dv8tion.jda.internal.requests.restaction;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import okhttp3.RequestBody;
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
/*     */ public class TriggerRestAction<T>
/*     */   extends RestActionImpl<T>
/*     */ {
/*  39 */   private final ReentrantLock mutex = new ReentrantLock();
/*  40 */   private final List<Runnable> callbacks = new LinkedList<>();
/*     */   
/*     */   private volatile boolean isReady;
/*     */   private volatile Throwable exception;
/*     */   
/*     */   public TriggerRestAction(JDA api, Route.CompiledRoute route) {
/*  46 */     super(api, route);
/*     */   }
/*     */ 
/*     */   
/*     */   public TriggerRestAction(JDA api, Route.CompiledRoute route, DataObject data) {
/*  51 */     super(api, route, data);
/*     */   }
/*     */ 
/*     */   
/*     */   public TriggerRestAction(JDA api, Route.CompiledRoute route, RequestBody data) {
/*  56 */     super(api, route, data);
/*     */   }
/*     */ 
/*     */   
/*     */   public TriggerRestAction(JDA api, Route.CompiledRoute route, BiFunction<Response, Request<T>, T> handler) {
/*  61 */     super(api, route, handler);
/*     */   }
/*     */ 
/*     */   
/*     */   public TriggerRestAction(JDA api, Route.CompiledRoute route, DataObject data, BiFunction<Response, Request<T>, T> handler) {
/*  66 */     super(api, route, data, handler);
/*     */   }
/*     */ 
/*     */   
/*     */   public TriggerRestAction(JDA api, Route.CompiledRoute route, RequestBody data, BiFunction<Response, Request<T>, T> handler) {
/*  71 */     super(api, route, data, handler);
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  76 */     MiscUtil.locked(this.mutex, () -> {
/*     */           this.isReady = true;
/*     */           this.callbacks.forEach(Runnable::run);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void fail(Throwable throwable) {
/*  84 */     MiscUtil.locked(this.mutex, () -> {
/*     */           this.exception = throwable;
/*     */           this.callbacks.forEach(Runnable::run);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onReady(Runnable callback) {
/*  92 */     MiscUtil.locked(this.mutex, () -> {
/*     */           if (this.isReady || this.exception != null) {
/*     */             callback.run();
/*     */           } else {
/*     */             this.callbacks.add(callback);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void queue(Consumer<? super T> success, Consumer<? super Throwable> failure) {
/* 103 */     if (this.isReady)
/* 104 */     { super.queue(success, failure); }
/* 105 */     else { onReady(() -> {
/*     */             if (this.exception != null) {
/*     */               if (failure != null) {
/*     */                 failure.accept(this.exception);
/*     */               } else {
/*     */                 RestAction.getDefaultFailure().accept(this.exception);
/*     */               } 
/*     */             } else {
/*     */               super.queue(success, failure);
/*     */             } 
/*     */           }); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CompletableFuture<T> submit(boolean shouldQueue) {
/* 124 */     if (this.isReady)
/* 125 */       return super.submit(shouldQueue); 
/* 126 */     CompletableFuture<T> future = new CompletableFuture<>();
/*     */     
/* 128 */     onReady(() -> {
/*     */           if (this.exception != null) {
/*     */             future.completeExceptionally(this.exception);
/*     */ 
/*     */ 
/*     */             
/*     */             return;
/*     */           } 
/*     */ 
/*     */           
/*     */           CompletableFuture<T> handle = super.submit(shouldQueue);
/*     */ 
/*     */           
/*     */           handle.whenComplete(());
/*     */ 
/*     */           
/*     */           future.whenComplete(());
/*     */         });
/*     */ 
/*     */     
/* 148 */     return future;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\TriggerRestAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */