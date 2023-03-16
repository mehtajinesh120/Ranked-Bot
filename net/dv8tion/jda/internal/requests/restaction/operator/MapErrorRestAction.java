/*     */ package net.dv8tion.jda.internal.requests.restaction.operator;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiFunction;
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
/*     */ public class MapErrorRestAction<T>
/*     */   extends RestActionOperator<T, T>
/*     */ {
/*     */   private final Predicate<? super Throwable> check;
/*     */   private final Function<? super Throwable, ? extends T> map;
/*     */   
/*     */   public MapErrorRestAction(RestAction<T> action, Predicate<? super Throwable> check, Function<? super Throwable, ? extends T> map) {
/*  39 */     super(action);
/*  40 */     this.check = check;
/*  41 */     this.map = map;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void queue(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure) {
/*  47 */     this.action.queue(success, contextWrap(error -> {
/*     */ 
/*     */             
/*     */             try {
/*     */               if (this.check.test(error)) {
/*     */                 doSuccess(success, this.map.apply(error));
/*     */               } else {
/*     */                 doFailure(failure, error);
/*     */               } 
/*  56 */             } catch (Throwable e) {
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
/*  68 */       return (T)this.action.complete(shouldQueue);
/*     */     }
/*  70 */     catch (Throwable error) {
/*     */ 
/*     */       
/*     */       try {
/*  74 */         if (this.check.test(error)) {
/*  75 */           return this.map.apply(error);
/*     */         }
/*  77 */       } catch (Throwable e) {
/*     */         
/*  79 */         fail(Helpers.appendCause(e, error));
/*     */       } 
/*  81 */       if (error instanceof RateLimitedException) {
/*  82 */         throw (RateLimitedException)error;
/*     */       }
/*  84 */       fail(error);
/*     */       
/*  86 */       throw new AssertionError("Unreachable");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CompletableFuture<T> submit(boolean shouldQueue) {
/*  93 */     return this.action.submit(shouldQueue).handle((value, error) -> {
/*     */           T result = (T)value;
/*     */           
/*     */           if (error != null) {
/*  97 */             error = (error instanceof java.util.concurrent.CompletionException && error.getCause() != null) ? error.getCause() : error;
/*     */             if (this.check.test(error)) {
/*     */               result = this.map.apply(error);
/*     */             } else {
/*     */               fail(error);
/*     */             } 
/*     */           } 
/*     */           return (BiFunction)result;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract("_ -> fail")
/*     */   private void fail(Throwable error) {
/* 112 */     if (error instanceof RuntimeException)
/* 113 */       throw (RuntimeException)error; 
/* 114 */     if (error instanceof Error) {
/* 115 */       throw (Error)error;
/*     */     }
/* 117 */     throw new RuntimeException(error);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\operator\MapErrorRestAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */