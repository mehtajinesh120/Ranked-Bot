/*     */ package net.dv8tion.jda.api.exceptions;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.requests.ErrorResponse;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
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
/*     */ public class ErrorHandler
/*     */   implements Consumer<Throwable>
/*     */ {
/*     */   private static final Consumer<? super Throwable> empty = e -> {
/*     */     
/*     */     };
/*     */   private final Consumer<? super Throwable> base;
/*  58 */   private final Map<Predicate<? super Throwable>, Consumer<? super Throwable>> cases = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ErrorHandler() {
/*  66 */     this(RestAction.getDefaultFailure());
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
/*     */   public ErrorHandler(@Nonnull Consumer<? super Throwable> base) {
/*  78 */     Checks.notNull(base, "Consumer");
/*  79 */     this.base = base;
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
/*     */   @Nonnull
/*     */   public ErrorHandler ignore(@Nonnull ErrorResponse ignored, @Nonnull ErrorResponse... errorResponses) {
/* 109 */     Checks.notNull(ignored, "ErrorResponse");
/* 110 */     Checks.noneNull((Object[])errorResponses, "ErrorResponse");
/* 111 */     return ignore(EnumSet.of(ignored, errorResponses));
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
/*     */   @Nonnull
/*     */   public ErrorHandler ignore(@Nonnull Collection<ErrorResponse> errorResponses) {
/* 140 */     return handle(errorResponses, (Consumer)empty);
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
/*     */   @Nonnull
/*     */   public ErrorHandler ignore(@Nonnull Class<?> clazz, @Nonnull Class<?>... classes) {
/* 169 */     Checks.notNull(clazz, "Classes");
/* 170 */     Checks.noneNull((Object[])classes, "Classes");
/* 171 */     return ignore(it -> {
/*     */           if (clazz.isInstance(it)) {
/*     */             return true;
/*     */           }
/*     */           for (Class<?> e : classes) {
/*     */             if (e.isInstance(it)) {
/*     */               return true;
/*     */             }
/*     */           } 
/*     */           return false;
/*     */         });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ErrorHandler ignore(@Nonnull Predicate<? super Throwable> condition) {
/* 207 */     return handle(condition, empty);
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
/*     */   @Nonnull
/*     */   public ErrorHandler handle(@Nonnull ErrorResponse response, @Nonnull Consumer<? super ErrorResponseException> handler) {
/* 238 */     Checks.notNull(response, "ErrorResponse");
/* 239 */     return handle(EnumSet.of(response), handler);
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
/*     */   @Nonnull
/*     */   public ErrorHandler handle(@Nonnull Collection<ErrorResponse> errorResponses, @Nonnull Consumer<? super ErrorResponseException> handler) {
/* 270 */     Checks.notNull(handler, "Handler");
/* 271 */     Checks.noneNull(errorResponses, "ErrorResponse");
/* 272 */     return handle(ErrorResponseException.class, it -> errorResponses.contains(it.getErrorResponse()), handler);
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
/*     */   @Nonnull
/*     */   public <T> ErrorHandler handle(@Nonnull Class<T> clazz, @Nonnull Consumer<? super T> handler) {
/* 301 */     Checks.notNull(clazz, "Class");
/* 302 */     Checks.notNull(handler, "Handler");
/* 303 */     Objects.requireNonNull(clazz); return handle(clazz::isInstance, ex -> handler.accept(clazz.cast(ex)));
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
/*     */   @Nonnull
/*     */   public <T> ErrorHandler handle(@Nonnull Class<T> clazz, @Nonnull Predicate<? super T> condition, @Nonnull Consumer<? super T> handler) {
/* 335 */     Checks.notNull(clazz, "Class");
/* 336 */     Checks.notNull(handler, "Handler");
/* 337 */     return handle(it -> 
/* 338 */         (clazz.isInstance(it) && condition.test(clazz.cast(it))), ex -> handler.accept(clazz.cast(ex)));
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
/*     */   @Nonnull
/*     */   public ErrorHandler handle(@Nonnull Collection<Class<?>> clazz, @Nullable Predicate<? super Throwable> condition, @Nonnull Consumer<? super Throwable> handler) {
/* 368 */     Checks.noneNull(clazz, "Class");
/* 369 */     Checks.notNull(handler, "Handler");
/* 370 */     List<Class<?>> classes = new ArrayList<>(clazz);
/* 371 */     Predicate<? super Throwable> check = it -> (classes.stream().anyMatch(()) && (condition == null || condition.test(it)));
/* 372 */     return handle(check, handler);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ErrorHandler handle(@Nonnull Predicate<? super Throwable> condition, @Nonnull Consumer<? super Throwable> handler) {
/* 398 */     Checks.notNull(condition, "Condition");
/* 399 */     Checks.notNull(handler, "Handler");
/* 400 */     this.cases.put(condition, handler);
/* 401 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept(Throwable t) {
/* 407 */     for (Map.Entry<Predicate<? super Throwable>, Consumer<? super Throwable>> entry : this.cases.entrySet()) {
/*     */       
/* 409 */       Predicate<? super Throwable> condition = entry.getKey();
/* 410 */       Consumer<? super Throwable> callback = entry.getValue();
/* 411 */       if (condition.test(t)) {
/*     */         
/* 413 */         callback.accept(t);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 418 */     this.base.accept(t);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\exceptions\ErrorHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */