/*      */ package net.dv8tion.jda.api.requests;
/*      */ 
/*      */ import java.time.Duration;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.concurrent.ScheduledFuture;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.BooleanSupplier;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.Collector;
/*      */ import java.util.stream.Collectors;
/*      */ import javax.annotation.CheckReturnValue;
/*      */ import javax.annotation.Nonnull;
/*      */ import javax.annotation.Nullable;
/*      */ import net.dv8tion.jda.api.JDA;
/*      */ import net.dv8tion.jda.api.exceptions.ContextException;
/*      */ import net.dv8tion.jda.api.exceptions.RateLimitedException;
/*      */ import net.dv8tion.jda.api.utils.Result;
/*      */ import net.dv8tion.jda.api.utils.concurrent.DelayedCompletableFuture;
/*      */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*      */ import net.dv8tion.jda.internal.requests.restaction.operator.CombineRestAction;
/*      */ import net.dv8tion.jda.internal.requests.restaction.operator.DelayRestAction;
/*      */ import net.dv8tion.jda.internal.requests.restaction.operator.FlatMapErrorRestAction;
/*      */ import net.dv8tion.jda.internal.requests.restaction.operator.FlatMapRestAction;
/*      */ import net.dv8tion.jda.internal.requests.restaction.operator.MapErrorRestAction;
/*      */ import net.dv8tion.jda.internal.requests.restaction.operator.MapRestAction;
/*      */ import net.dv8tion.jda.internal.utils.Checks;
/*      */ import net.dv8tion.jda.internal.utils.ContextRunnable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public interface RestAction<T>
/*      */ {
/*      */   static void setPassContext(boolean enable) {
/*  172 */     RestActionImpl.setPassContext(enable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isPassContext() {
/*  186 */     return RestActionImpl.isPassContext();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void setDefaultFailure(@Nullable Consumer<? super Throwable> callback) {
/*  197 */     RestActionImpl.setDefaultFailure(callback);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void setDefaultSuccess(@Nullable Consumer<Object> callback) {
/*  208 */     RestActionImpl.setDefaultSuccess(callback);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void setDefaultTimeout(long timeout, @Nonnull TimeUnit unit) {
/*  228 */     RestActionImpl.setDefaultTimeout(timeout, unit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static long getDefaultTimeout() {
/*  241 */     return RestActionImpl.getDefaultTimeout();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   static Consumer<? super Throwable> getDefaultFailure() {
/*  252 */     return RestActionImpl.getDefaultFailure();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   static Consumer<Object> getDefaultSuccess() {
/*  263 */     return RestActionImpl.getDefaultSuccess();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @SafeVarargs
/*      */   @CheckReturnValue
/*      */   static <E> RestAction<List<E>> allOf(@Nonnull RestAction<? extends E> first, @Nonnull RestAction<? extends E>... others) {
/*  293 */     Checks.notNull(first, "RestAction");
/*  294 */     Checks.noneNull((Object[])others, "RestAction");
/*  295 */     List<RestAction<? extends E>> list = new ArrayList<>(others.length + 1);
/*  296 */     list.add(first);
/*  297 */     Collections.addAll(list, others);
/*  298 */     return allOf(list);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   static <E> RestAction<List<E>> allOf(@Nonnull Collection<? extends RestAction<? extends E>> actions) {
/*  325 */     return accumulate(actions, (Collector)Collectors.toList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   static <E, A, O> RestAction<O> accumulate(@Nonnull Collection<? extends RestAction<? extends E>> actions, @Nonnull Collector<? super E, A, ? extends O> collector) {
/*  358 */     Checks.noneNull(actions, "RestAction");
/*  359 */     Checks.notEmpty(actions, "RestActions");
/*  360 */     Checks.notNull(collector, "Collector");
/*  361 */     Supplier<A> accumulator = collector.supplier();
/*  362 */     BiConsumer<A, ? super E> add = collector.accumulator();
/*  363 */     Function<A, ? extends O> output = collector.finisher();
/*      */     
/*  365 */     actions = new LinkedHashSet<>(actions);
/*  366 */     Iterator<? extends RestAction<? extends E>> iterator = actions.iterator();
/*  367 */     RestAction<A> result = ((RestAction)iterator.next()).map(it -> {
/*      */           A list = accumulator.get();
/*      */           
/*      */           add.accept(list, it);
/*      */           return (Function)list;
/*      */         });
/*  373 */     while (iterator.hasNext()) {
/*      */       
/*  375 */       RestAction<? extends E> next = iterator.next();
/*  376 */       result = result.and(next, (list, b) -> {
/*      */             add.accept(list, b);
/*      */             
/*      */             return list;
/*      */           });
/*      */     } 
/*  382 */     return result.map(output);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default BooleanSupplier getCheck() {
/*  421 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<T> addCheck(@Nonnull BooleanSupplier checks) {
/*  443 */     Checks.notNull(checks, "Checks");
/*  444 */     BooleanSupplier check = getCheck();
/*  445 */     return setCheck(() -> ((check == null || check.getAsBoolean()) && checks.getAsBoolean()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default RestAction<T> timeout(long timeout, @Nonnull TimeUnit unit) {
/*  476 */     Checks.notNull(unit, "TimeUnit");
/*  477 */     return deadline((timeout <= 0L) ? 0L : (System.currentTimeMillis() + unit.toMillis(timeout)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default RestAction<T> deadline(long timestamp) {
/*  505 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default void queue() {
/*  539 */     queue(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default void queue(@Nullable Consumer<? super T> success) {
/*  573 */     queue(success, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default T complete() {
/*      */     try {
/*  633 */       return complete(true);
/*      */     }
/*  635 */     catch (RateLimitedException e) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  640 */       throw new AssertionError(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default CompletableFuture<T> submit() {
/*  694 */     return submit(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<Result<T>> mapToResult() {
/*  733 */     return map(Result::success).onErrorMap(Result::failure);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default <O> RestAction<O> map(@Nonnull Function<? super T, ? extends O> map) {
/*  764 */     Checks.notNull(map, "Function");
/*  765 */     return (RestAction<O>)new MapRestAction(this, map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<T> onErrorMap(@Nonnull Function<? super Throwable, ? extends T> map) {
/*  798 */     return onErrorMap(null, map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<T> onErrorMap(@Nullable Predicate<? super Throwable> condition, @Nonnull Function<? super Throwable, ? extends T> map) {
/*  836 */     Checks.notNull(map, "Function");
/*  837 */     return (RestAction<T>)new MapErrorRestAction(this, (condition == null) ? (x -> true) : condition, map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<T> onErrorFlatMap(@Nonnull Function<? super Throwable, ? extends RestAction<? extends T>> map) {
/*  871 */     return onErrorFlatMap(null, map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<T> onErrorFlatMap(@Nullable Predicate<? super Throwable> condition, @Nonnull Function<? super Throwable, ? extends RestAction<? extends T>> map) {
/*  910 */     Checks.notNull(map, "Function");
/*  911 */     return (RestAction<T>)new FlatMapErrorRestAction(this, (condition == null) ? (x -> true) : condition, map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default <O> RestAction<O> flatMap(@Nonnull Function<? super T, ? extends RestAction<O>> flatMap) {
/*  947 */     return flatMap(null, flatMap);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default <O> RestAction<O> flatMap(@Nullable Predicate<? super T> condition, @Nonnull Function<? super T, ? extends RestAction<O>> flatMap) {
/*  993 */     Checks.notNull(flatMap, "Function");
/*  994 */     return (RestAction<O>)new FlatMapRestAction(this, condition, flatMap);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default <U, O> RestAction<O> and(@Nonnull RestAction<U> other, @Nonnull BiFunction<? super T, ? super U, ? extends O> accumulator) {
/* 1024 */     Checks.notNull(other, "RestAction");
/* 1025 */     Checks.notNull(accumulator, "Accumulator");
/* 1026 */     return (RestAction<O>)new CombineRestAction(this, other, accumulator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default <U> RestAction<Void> and(@Nonnull RestAction<U> other) {
/* 1051 */     return and(other, (a, b) -> null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<List<T>> zip(@Nonnull RestAction<? extends T> first, @Nonnull RestAction<? extends T>... other) {
/* 1080 */     Checks.notNull(first, "RestAction");
/* 1081 */     Checks.noneNull((Object[])other, "RestAction");
/* 1082 */     List<RestAction<? extends T>> list = new ArrayList<>();
/* 1083 */     list.add(this);
/* 1084 */     list.add(first);
/* 1085 */     Collections.addAll(list, other);
/* 1086 */     return allOf(list);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<T> delay(@Nonnull Duration duration) {
/* 1118 */     return delay(duration, (ScheduledExecutorService)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<T> delay(@Nonnull Duration duration, @Nullable ScheduledExecutorService scheduler) {
/* 1152 */     Checks.notNull(duration, "Duration");
/* 1153 */     return (RestAction<T>)new DelayRestAction(this, TimeUnit.MILLISECONDS, duration.toMillis(), scheduler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<T> delay(long delay, @Nonnull TimeUnit unit) {
/* 1187 */     return delay(delay, unit, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<T> delay(long delay, @Nonnull TimeUnit unit, @Nullable ScheduledExecutorService scheduler) {
/* 1223 */     Checks.notNull(unit, "TimeUnit");
/* 1224 */     return (RestAction<T>)new DelayRestAction(this, unit, delay, scheduler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default DelayedCompletableFuture<T> submitAfter(long delay, @Nonnull TimeUnit unit) {
/* 1253 */     return submitAfter(delay, unit, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default DelayedCompletableFuture<T> submitAfter(long delay, @Nonnull TimeUnit unit, @Nullable ScheduledExecutorService executor) {
/* 1283 */     Checks.notNull(unit, "TimeUnit");
/* 1284 */     if (executor == null)
/* 1285 */       executor = getJDA().getRateLimitPool(); 
/* 1286 */     return DelayedCompletableFuture.make(executor, delay, unit, task -> {
/*      */           Consumer<? super Throwable> onFailure;
/*      */           if (isPassContext()) {
/*      */             Objects.requireNonNull(task);
/*      */             onFailure = ContextException.here(task::completeExceptionally);
/*      */           } else {
/*      */             Objects.requireNonNull(task);
/*      */             onFailure = task::completeExceptionally;
/*      */           } 
/*      */           return (Runnable)new ContextRunnable(());
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default T completeAfter(long delay, @Nonnull TimeUnit unit) {
/* 1317 */     Checks.notNull(unit, "TimeUnit");
/*      */     
/*      */     try {
/* 1320 */       unit.sleep(delay);
/* 1321 */       return complete();
/*      */     }
/* 1323 */     catch (InterruptedException e) {
/*      */       
/* 1325 */       throw new RuntimeException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default ScheduledFuture<?> queueAfter(long delay, @Nonnull TimeUnit unit) {
/* 1355 */     return queueAfter(delay, unit, null, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default ScheduledFuture<?> queueAfter(long delay, @Nonnull TimeUnit unit, @Nullable Consumer<? super T> success) {
/* 1387 */     return queueAfter(delay, unit, success, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default ScheduledFuture<?> queueAfter(long delay, @Nonnull TimeUnit unit, @Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure) {
/* 1421 */     return queueAfter(delay, unit, success, failure, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default ScheduledFuture<?> queueAfter(long delay, @Nonnull TimeUnit unit, @Nullable ScheduledExecutorService executor) {
/* 1452 */     return queueAfter(delay, unit, null, null, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default ScheduledFuture<?> queueAfter(long delay, @Nonnull TimeUnit unit, @Nullable Consumer<? super T> success, @Nullable ScheduledExecutorService executor) {
/* 1486 */     return queueAfter(delay, unit, success, null, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default ScheduledFuture<?> queueAfter(long delay, @Nonnull TimeUnit unit, @Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure, @Nullable ScheduledExecutorService executor) {
/*      */     Consumer<? super Throwable> onFailure;
/* 1522 */     Checks.notNull(unit, "TimeUnit");
/* 1523 */     if (executor == null) {
/* 1524 */       executor = getJDA().getRateLimitPool();
/*      */     }
/*      */     
/* 1527 */     if (isPassContext()) {
/* 1528 */       onFailure = ContextException.here((failure == null) ? getDefaultFailure() : failure);
/*      */     } else {
/* 1530 */       onFailure = failure;
/*      */     } 
/* 1532 */     ContextRunnable contextRunnable = new ContextRunnable(() -> queue(success, onFailure));
/* 1533 */     return executor.schedule((Runnable)contextRunnable, delay, unit);
/*      */   }
/*      */   
/*      */   @Nonnull
/*      */   JDA getJDA();
/*      */   
/*      */   @Nonnull
/*      */   RestAction<T> setCheck(@Nullable BooleanSupplier paramBooleanSupplier);
/*      */   
/*      */   void queue(@Nullable Consumer<? super T> paramConsumer, @Nullable Consumer<? super Throwable> paramConsumer1);
/*      */   
/*      */   T complete(boolean paramBoolean) throws RateLimitedException;
/*      */   
/*      */   @Nonnull
/*      */   CompletableFuture<T> submit(boolean paramBoolean);
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\RestAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */