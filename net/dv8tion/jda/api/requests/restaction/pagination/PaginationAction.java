/*     */ package net.dv8tion.jda.api.requests.restaction.pagination;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Queue;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.utils.Procedure;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface PaginationAction<T, M extends PaginationAction<T, M>>
/*     */   extends RestAction<List<T>>, Iterable<T>
/*     */ {
/*     */   @Nonnull
/*     */   default CompletableFuture<List<T>> takeWhileAsync(@Nonnull Predicate<? super T> rule) {
/* 304 */     Checks.notNull(rule, "Rule");
/* 305 */     return takeUntilAsync(rule.negate());
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
/*     */   @Nonnull
/*     */   default CompletableFuture<List<T>> takeWhileAsync(int limit, @Nonnull Predicate<? super T> rule) {
/* 329 */     Checks.notNull(rule, "Rule");
/* 330 */     return takeUntilAsync(limit, rule.negate());
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
/*     */   @Nonnull
/*     */   default CompletableFuture<List<T>> takeUntilAsync(@Nonnull Predicate<? super T> rule) {
/* 352 */     return takeUntilAsync(0, rule);
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
/*     */   @Nonnull
/*     */   default CompletableFuture<List<T>> takeUntilAsync(int limit, @Nonnull Predicate<? super T> rule) {
/* 376 */     Checks.notNull(rule, "Rule");
/* 377 */     Checks.notNegative(limit, "Limit");
/* 378 */     List<T> result = new ArrayList<>();
/* 379 */     CompletableFuture<List<T>> future = new CompletableFuture<>();
/* 380 */     CompletableFuture<?> handle = forEachAsync(element -> {
/*     */           if (rule.test(element))
/*     */             return false; 
/*     */           result.add(element);
/* 384 */           return (limit == 0 || limit > result.size());
/*     */         });
/* 386 */     handle.whenComplete((r, t) -> {
/*     */           if (t != null) {
/*     */             future.completeExceptionally(t);
/*     */           } else {
/*     */             future.complete(result);
/*     */           } 
/* 392 */         }); return future;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   default CompletableFuture<?> forEachAsync(@Nonnull Procedure<? super T> action) {
/* 460 */     return forEachAsync(action, RestActionImpl.getDefaultFailure());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   default CompletableFuture<?> forEachRemainingAsync(@Nonnull Procedure<? super T> action) {
/* 541 */     return forEachRemainingAsync(action, RestActionImpl.getDefaultFailure());
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
/*     */   @Nonnull
/*     */   M skipTo(long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getLastKey();
/*     */ 
/*     */ 
/*     */ 
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
/*     */   M setCheck(@Nullable BooleanSupplier paramBooleanSupplier);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Spliterator<T> spliterator() {
/* 599 */     return Spliterators.spliteratorUnknownSize(iterator(), 1024);
/*     */   } @Nonnull
/*     */   M timeout(long paramLong, @Nonnull TimeUnit paramTimeUnit); @Nonnull
/*     */   M deadline(long paramLong); int cacheSize();
/*     */   boolean isEmpty();
/*     */   @Nonnull
/*     */   List<T> getCached();
/*     */   @Nonnull
/*     */   T getLast();
/*     */   @Nonnull
/*     */   default Stream<T> stream() {
/* 610 */     return StreamSupport.stream(spliterator(), false);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   T getFirst();
/*     */   
/*     */   @Nonnull
/*     */   M limit(int paramInt);
/*     */   
/*     */   @Nonnull
/*     */   default Stream<T> parallelStream() {
/* 622 */     return StreamSupport.stream(spliterator(), true);
/*     */   }
/*     */   @Nonnull
/*     */   M cache(boolean paramBoolean);
/*     */   boolean isCacheEnabled();
/*     */   int getMaxLimit();
/*     */   
/*     */   int getMinLimit();
/*     */   
/*     */   int getLimit();
/*     */   
/*     */   @Nonnull
/*     */   CompletableFuture<List<T>> takeAsync(int paramInt);
/*     */   
/*     */   @Nonnull
/*     */   CompletableFuture<List<T>> takeRemainingAsync(int paramInt);
/*     */   
/*     */   @Nonnull
/*     */   CompletableFuture<?> forEachAsync(@Nonnull Procedure<? super T> paramProcedure, @Nonnull Consumer<? super Throwable> paramConsumer);
/*     */   
/*     */   @Nonnull
/*     */   CompletableFuture<?> forEachRemainingAsync(@Nonnull Procedure<? super T> paramProcedure, @Nonnull Consumer<? super Throwable> paramConsumer);
/*     */   
/*     */   void forEachRemaining(@Nonnull Procedure<? super T> paramProcedure);
/*     */   
/*     */   @Nonnull
/*     */   PaginationIterator<T> iterator();
/*     */   
/*     */   public static class PaginationIterator<E> implements Iterator<E> { public PaginationIterator(Collection<E> queue, Supplier<List<E>> supply) {
/* 651 */       this.items = new LinkedList<>(queue);
/* 652 */       this.supply = supply;
/*     */     }
/*     */     protected Queue<E> items;
/*     */     protected final Supplier<List<E>> supply;
/*     */     
/*     */     public boolean hasNext() {
/* 658 */       if (this.items == null)
/* 659 */         return false; 
/* 660 */       if (!hitEnd()) {
/* 661 */         return true;
/*     */       }
/* 663 */       if (this.items.addAll(this.supply.get())) {
/* 664 */         return true;
/*     */       }
/*     */       
/* 667 */       this.items = null;
/* 668 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public E next() {
/* 674 */       if (!hasNext())
/* 675 */         throw new NoSuchElementException("Reached End of pagination task!"); 
/* 676 */       return this.items.poll();
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean hitEnd() {
/* 681 */       return this.items.isEmpty();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\pagination\PaginationAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */