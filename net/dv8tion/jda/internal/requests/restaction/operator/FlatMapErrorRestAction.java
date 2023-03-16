/*     */ package net.dv8tion.jda.internal.requests.restaction.operator;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.exceptions.RateLimitedException;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.internal.utils.Helpers;
/*     */ import org.jetbrains.annotations.Contract;
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
/*     */ public class FlatMapErrorRestAction<T>
/*     */   extends RestActionOperator<T, T>
/*     */ {
/*     */   private final Predicate<? super Throwable> check;
/*     */   private final Function<? super Throwable, ? extends RestAction<? extends T>> map;
/*     */   
/*     */   public FlatMapErrorRestAction(RestAction<T> action, Predicate<? super Throwable> check, Function<? super Throwable, ? extends RestAction<? extends T>> map) {
/*  38 */     super(action);
/*  39 */     this.check = check;
/*  40 */     this.map = map;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void queue(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure) {
/*  46 */     Consumer<? super Throwable> contextFailure = contextWrap(failure);
/*  47 */     this.action.queue(success, contextWrap(error -> {
/*     */             try {
/*     */               if (this.check.test(error)) {
/*     */                 RestAction<? extends T> then = this.map.apply(error);
/*     */ 
/*     */                 
/*     */                 if (then == null) {
/*     */                   doFailure(failure, new IllegalStateException("FlatMapError operand is null", error));
/*     */                 } else {
/*     */                   then.queue(success, contextFailure);
/*     */                 } 
/*     */               } else {
/*     */                 doFailure(failure, error);
/*     */               } 
/*  61 */             } catch (Throwable e) {
/*     */               doFailure(failure, Helpers.appendCause(e, error));
/*     */             } 
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T complete(boolean shouldQueue) throws RateLimitedException {
/*     */     try {
/*  73 */       return (T)this.action.complete(shouldQueue);
/*     */     }
/*  75 */     catch (Throwable error) {
/*     */ 
/*     */       
/*     */       try {
/*  79 */         if (this.check.test(error))
/*     */         {
/*  81 */           RestAction<? extends T> then = this.map.apply(error);
/*  82 */           if (then == null)
/*  83 */             throw new IllegalStateException("FlatMapError operand is null", error); 
/*  84 */           return (T)then.complete(shouldQueue);
/*     */         }
/*     */       
/*  87 */       } catch (Throwable e) {
/*     */         
/*  89 */         if (e instanceof IllegalStateException && e.getCause() == error)
/*  90 */           throw (IllegalStateException)e; 
/*  91 */         if (e instanceof RateLimitedException) {
/*  92 */           throw (RateLimitedException)Helpers.appendCause(e, error);
/*     */         }
/*  94 */         fail(Helpers.appendCause(e, error));
/*     */       } 
/*  96 */       fail(error);
/*     */       
/*  98 */       throw new AssertionError("Unreachable");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CompletableFuture<T> submit(boolean shouldQueue) {
/* 105 */     return this.action.submit(shouldQueue)
/* 106 */       .handle((result, error) -> this.check.test(error) ? ((RestAction)this.map.apply(error)).submit(shouldQueue).thenApply(()) : CompletableFuture.completedFuture(result))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 111 */       .thenCompose(Function.identity());
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract("_ -> fail")
/*     */   private void fail(Throwable error) {
/* 117 */     if (error instanceof RuntimeException)
/* 118 */       throw (RuntimeException)error; 
/* 119 */     if (error instanceof Error) {
/* 120 */       throw (Error)error;
/*     */     }
/* 122 */     throw new RuntimeException(error);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\operator\FlatMapErrorRestAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */