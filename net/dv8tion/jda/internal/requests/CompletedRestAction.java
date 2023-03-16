/*     */ package net.dv8tion.jda.internal.requests;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.exceptions.RateLimitedException;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
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
/*     */ public class CompletedRestAction<T>
/*     */   implements AuditableRestAction<T>
/*     */ {
/*     */   private final JDA api;
/*     */   private final T value;
/*     */   private final Throwable error;
/*     */   
/*     */   public CompletedRestAction(JDA api, T value, Throwable error) {
/*  39 */     this.api = api;
/*  40 */     this.value = value;
/*  41 */     this.error = error;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletedRestAction(JDA api, T value) {
/*  46 */     this(api, value, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletedRestAction(JDA api, Throwable error) {
/*  51 */     this(api, null, error);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<T> reason(@Nullable String reason) {
/*  59 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/*  66 */     return this.api;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<T> setCheck(@Nullable BooleanSupplier checks) {
/*  73 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<T> timeout(long timeout, @Nonnull TimeUnit unit) {
/*  80 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<T> deadline(long timestamp) {
/*  87 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void queue(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure) {
/*  93 */     if (this.error == null) {
/*     */       
/*  95 */       if (success == null) {
/*  96 */         RestAction.getDefaultSuccess().accept(this.value);
/*     */       } else {
/*  98 */         success.accept(this.value);
/*     */       }
/*     */     
/*     */     }
/* 102 */     else if (failure == null) {
/* 103 */       RestAction.getDefaultFailure().accept(this.error);
/*     */     } else {
/* 105 */       failure.accept(this.error);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public T complete(boolean shouldQueue) throws RateLimitedException {
/* 112 */     if (this.error != null) {
/*     */       
/* 114 */       if (this.error instanceof RateLimitedException)
/* 115 */         throw (RateLimitedException)this.error; 
/* 116 */       if (this.error instanceof RuntimeException)
/* 117 */         throw (RuntimeException)this.error; 
/* 118 */       if (this.error instanceof Error)
/* 119 */         throw (Error)this.error; 
/* 120 */       throw new IllegalStateException(this.error);
/*     */     } 
/* 122 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CompletableFuture<T> submit(boolean shouldQueue) {
/* 129 */     CompletableFuture<T> future = new CompletableFuture<>();
/* 130 */     if (this.error != null) {
/* 131 */       future.completeExceptionally(this.error);
/*     */     } else {
/* 133 */       future.complete(this.value);
/* 134 */     }  return future;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\CompletedRestAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */