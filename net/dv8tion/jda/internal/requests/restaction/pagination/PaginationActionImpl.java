/*     */ package net.dv8tion.jda.internal.requests.restaction.pagination;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.pagination.PaginationAction;
/*     */ import net.dv8tion.jda.api.utils.Procedure;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
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
/*     */ public abstract class PaginationActionImpl<T, M extends PaginationAction<T, M>>
/*     */   extends RestActionImpl<List<T>>
/*     */   implements PaginationAction<T, M>
/*     */ {
/*  40 */   protected final List<T> cached = new CopyOnWriteArrayList<>();
/*     */   
/*     */   protected final int maxLimit;
/*     */   protected final int minLimit;
/*     */   protected final AtomicInteger limit;
/*  45 */   protected volatile long iteratorIndex = 0L;
/*  46 */   protected volatile long lastKey = 0L;
/*  47 */   protected volatile T last = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected volatile boolean useCache = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PaginationActionImpl(JDA api, Route.CompiledRoute route, int minLimit, int maxLimit, int initialLimit) {
/*  66 */     super(api, route);
/*  67 */     this.maxLimit = maxLimit;
/*  68 */     this.minLimit = minLimit;
/*  69 */     this.limit = new AtomicInteger(initialLimit);
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
/*     */   public PaginationActionImpl(JDA api) {
/*  82 */     super(api, null);
/*  83 */     this.maxLimit = 0;
/*  84 */     this.minLimit = 0;
/*  85 */     this.limit = new AtomicInteger(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M skipTo(long id) {
/*  93 */     if (!this.cached.isEmpty()) {
/*     */       
/*  95 */       int cmp = Long.compareUnsigned(this.lastKey, id);
/*  96 */       if (cmp < 0)
/*  97 */         throw new IllegalArgumentException("Cannot jump to that id, it is newer than the current oldest element."); 
/*     */     } 
/*  99 */     if (this.lastKey != id)
/* 100 */       this.last = null; 
/* 101 */     this.iteratorIndex = id;
/* 102 */     this.lastKey = id;
/* 103 */     return (M)this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastKey() {
/* 109 */     return this.lastKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M setCheck(BooleanSupplier checks) {
/* 117 */     return (M)super.setCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M timeout(long timeout, @Nonnull TimeUnit unit) {
/* 125 */     return (M)super.timeout(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M deadline(long timestamp) {
/* 133 */     return (M)super.deadline(timestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int cacheSize() {
/* 139 */     return this.cached.size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 145 */     return this.cached.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<T> getCached() {
/* 152 */     return Collections.unmodifiableList(this.cached);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public T getLast() {
/* 159 */     T last = this.last;
/* 160 */     if (last == null)
/* 161 */       throw new NoSuchElementException("No entities have been retrieved yet."); 
/* 162 */     return last;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public T getFirst() {
/* 169 */     if (this.cached.isEmpty())
/* 170 */       throw new NoSuchElementException("No entities have been retrieved yet."); 
/* 171 */     return this.cached.get(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M limit(int limit) {
/* 179 */     Checks.check((this.maxLimit == 0 || limit <= this.maxLimit), "Limit must not exceed %d!", Integer.valueOf(this.maxLimit));
/* 180 */     Checks.check((this.minLimit == 0 || limit >= this.minLimit), "Limit must be greater or equal to %d", Integer.valueOf(this.minLimit));
/* 181 */     this.limit.set(limit);
/* 182 */     return (M)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M cache(boolean enableCache) {
/* 190 */     this.useCache = enableCache;
/* 191 */     return (M)this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCacheEnabled() {
/* 197 */     return this.useCache;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getMaxLimit() {
/* 203 */     return this.maxLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getMinLimit() {
/* 209 */     return this.minLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getLimit() {
/* 215 */     return this.limit.get();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CompletableFuture<List<T>> takeAsync(int amount) {
/* 222 */     return takeAsync0(amount, (task, list) -> {
/*     */           Objects.requireNonNull(task);
/*     */           return forEachAsync((), task::completeExceptionally);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CompletableFuture<List<T>> takeRemainingAsync(int amount) {
/* 232 */     return takeAsync0(amount, (task, list) -> {
/*     */           Objects.requireNonNull(task);
/*     */           return forEachRemainingAsync((), task::completeExceptionally);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private CompletableFuture<List<T>> takeAsync0(int amount, BiFunction<CompletableFuture<?>, List<T>, CompletableFuture<?>> converter) {
/* 240 */     CompletableFuture<List<T>> task = new CompletableFuture<>();
/* 241 */     List<T> list = new ArrayList<>(amount);
/* 242 */     CompletableFuture<?> promise = converter.apply(task, list);
/* 243 */     promise.thenRun(() -> task.complete(list));
/* 244 */     return task;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public PaginationAction.PaginationIterator<T> iterator() {
/* 251 */     return new PaginationAction.PaginationIterator(this.cached, this::getNextChunk);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CompletableFuture<?> forEachAsync(@Nonnull Procedure<? super T> action, @Nonnull Consumer<? super Throwable> failure) {
/* 258 */     Checks.notNull(action, "Procedure");
/* 259 */     Checks.notNull(failure, "Failure Consumer");
/*     */     
/* 261 */     CompletableFuture<?> task = new CompletableFuture();
/* 262 */     Consumer<List<T>> acceptor = new ChainedConsumer(task, action, throwable -> {
/*     */           task.completeExceptionally(throwable);
/*     */           
/*     */           failure.accept(throwable);
/*     */         });
/*     */     
/*     */     try {
/* 269 */       acceptor.accept(this.cached);
/*     */     }
/* 271 */     catch (Exception ex) {
/*     */       
/* 273 */       failure.accept(ex);
/* 274 */       task.completeExceptionally(ex);
/*     */     } 
/* 276 */     return task;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CompletableFuture<?> forEachRemainingAsync(@Nonnull Procedure<? super T> action, @Nonnull Consumer<? super Throwable> failure) {
/* 283 */     Checks.notNull(action, "Procedure");
/* 284 */     Checks.notNull(failure, "Failure Consumer");
/*     */     
/* 286 */     CompletableFuture<?> task = new CompletableFuture();
/* 287 */     Consumer<List<T>> acceptor = new ChainedConsumer(task, action, throwable -> {
/*     */           task.completeExceptionally(throwable);
/*     */           
/*     */           failure.accept(throwable);
/*     */         });
/*     */     
/*     */     try {
/* 294 */       acceptor.accept(getRemainingCache());
/*     */     }
/* 296 */     catch (Exception ex) {
/*     */       
/* 298 */       failure.accept(ex);
/* 299 */       task.completeExceptionally(ex);
/*     */     } 
/* 301 */     return task;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void forEachRemaining(@Nonnull Procedure<? super T> action) {
/* 307 */     Checks.notNull(action, "Procedure");
/* 308 */     Queue<T> queue = new LinkedList<>();
/* 309 */     while (queue.addAll(getNextChunk())) {
/*     */       
/* 311 */       while (!queue.isEmpty()) {
/*     */         
/* 313 */         T it = queue.poll();
/* 314 */         if (!action.execute(it)) {
/*     */ 
/*     */           
/* 317 */           updateIndex(it);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<T> getRemainingCache() {
/* 326 */     int index = getIteratorIndex();
/* 327 */     if (this.useCache && index > -1 && index < this.cached.size())
/* 328 */       return this.cached.subList(index, this.cached.size()); 
/* 329 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<T> getNextChunk() {
/* 334 */     List<T> list = getRemainingCache();
/* 335 */     if (!list.isEmpty()) {
/* 336 */       return list;
/*     */     }
/* 338 */     int current = this.limit.getAndSet(getMaxLimit());
/* 339 */     list = (List<T>)complete();
/* 340 */     this.limit.set(current);
/* 341 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getIteratorIndex() {
/* 348 */     for (int i = 0; i < this.cached.size(); i++) {
/*     */       
/* 350 */       if (getKey(this.cached.get(i)) == this.iteratorIndex)
/* 351 */         return i + 1; 
/*     */     } 
/* 353 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateIndex(T it) {
/* 358 */     long key = getKey(it);
/* 359 */     this.iteratorIndex = key;
/* 360 */     if (!this.useCache) {
/*     */       
/* 362 */       this.lastKey = key;
/* 363 */       this.last = it;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract long getKey(T paramT);
/*     */   
/*     */   protected class ChainedConsumer
/*     */     implements Consumer<List<T>> {
/*     */     protected final CompletableFuture<?> task;
/*     */     protected final Procedure<? super T> action;
/*     */     protected final Consumer<Throwable> throwableConsumer;
/*     */     protected boolean initial = true;
/*     */     
/*     */     protected ChainedConsumer(CompletableFuture<?> task, Procedure<? super T> action, Consumer<Throwable> throwableConsumer) {
/* 377 */       this.task = task;
/* 378 */       this.action = action;
/* 379 */       this.throwableConsumer = throwableConsumer;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void accept(List<T> list) {
/* 385 */       if (list.isEmpty() && !this.initial) {
/*     */         
/* 387 */         this.task.complete(null);
/*     */         return;
/*     */       } 
/* 390 */       this.initial = false;
/*     */       
/* 392 */       T previous = null;
/* 393 */       for (T it : list) {
/*     */         
/* 395 */         if (this.task.isCancelled()) {
/*     */           
/* 397 */           if (previous != null)
/* 398 */             PaginationActionImpl.this.updateIndex(previous); 
/*     */           return;
/*     */         } 
/* 401 */         if (this.action.execute(it)) {
/*     */           
/* 403 */           previous = it;
/*     */           
/*     */           continue;
/*     */         } 
/* 407 */         PaginationActionImpl.this.updateIndex(it);
/* 408 */         this.task.complete(null);
/*     */         
/*     */         return;
/*     */       } 
/* 412 */       int currentLimit = PaginationActionImpl.this.limit.getAndSet(PaginationActionImpl.this.maxLimit);
/* 413 */       PaginationActionImpl.this.queue(this, this.throwableConsumer);
/* 414 */       PaginationActionImpl.this.limit.set(currentLimit);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\pagination\PaginationActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */