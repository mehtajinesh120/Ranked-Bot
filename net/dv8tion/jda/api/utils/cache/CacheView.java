/*     */ package net.dv8tion.jda.api.utils.cache;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.utils.ClosableIterator;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.cache.AbstractCacheView;
/*     */ import net.dv8tion.jda.internal.utils.cache.ShardCacheViewImpl;
/*     */ import net.dv8tion.jda.internal.utils.cache.UnifiedCacheViewImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface CacheView<T>
/*     */   extends Iterable<T>
/*     */ {
/*     */   @Nonnull
/*     */   List<T> asList();
/*     */   
/*     */   @Nonnull
/*     */   Set<T> asSet();
/*     */   
/*     */   @Nonnull
/*     */   ClosableIterator<T> lockedIterator();
/*     */   
/*     */   default void forEachUnordered(@Nonnull Consumer<? super T> action) {
/* 113 */     forEach(action);
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
/*     */   @Nullable
/*     */   default <R> R applyStream(@Nonnull Function<? super Stream<T>, ? extends R> action) {
/* 146 */     Checks.notNull(action, "Action");
/* 147 */     ClosableIterator<T> it = lockedIterator();
/*     */     try {
/* 149 */       Spliterator<T> spliterator = Spliterators.spliterator((Iterator<? extends T>)it, size(), 1280);
/* 150 */       Stream<T> stream = StreamSupport.stream(spliterator, false);
/* 151 */       R r = action.apply(stream);
/* 152 */       if (it != null) it.close();
/*     */       
/*     */       return r;
/*     */     } catch (Throwable throwable) {
/*     */       if (it != null) {
/*     */         try {
/*     */           it.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */       }
/*     */       throw throwable;
/*     */     } 
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
/*     */   default void acceptStream(@Nonnull Consumer<? super Stream<T>> action) {
/* 179 */     Checks.notNull(action, "Action");
/* 180 */     ClosableIterator<T> it = lockedIterator();
/*     */     try {
/* 182 */       Spliterator<T> spliterator = Spliterators.spliterator((Iterator<? extends T>)it, size(), 1280);
/* 183 */       Stream<T> stream = StreamSupport.stream(spliterator, false);
/* 184 */       action.accept(stream);
/* 185 */       if (it != null) it.close();
/*     */     
/*     */     } catch (Throwable throwable) {
/*     */       if (it != null) {
/*     */         try {
/*     */           it.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */       }
/*     */       throw throwable;
/*     */     } 
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
/*     */   long size();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isEmpty();
/*     */ 
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
/*     */   List<T> getElementsByName(@Nonnull String paramString, boolean paramBoolean);
/*     */ 
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
/*     */   default List<T> getElementsByName(@Nonnull String name) {
/* 246 */     return getElementsByName(name, false);
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
/*     */   @Nonnull
/*     */   Stream<T> stream();
/*     */ 
/*     */ 
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
/*     */   Stream<T> parallelStream();
/*     */ 
/*     */ 
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
/*     */   default <R, A> R collect(@Nonnull Collector<? super T, A, R> collector) {
/* 288 */     return stream().collect(collector);
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
/*     */   @Nonnull
/*     */   static <E> CacheView<E> all(@Nonnull Collection<? extends CacheView<E>> cacheViews) {
/* 307 */     Checks.noneNull(cacheViews, "Collection");
/* 308 */     Objects.requireNonNull(cacheViews); return (CacheView<E>)new UnifiedCacheViewImpl(cacheViews::stream);
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
/*     */   @Nonnull
/*     */   static <E> CacheView<E> all(@Nonnull Supplier<? extends Stream<? extends CacheView<E>>> generator) {
/* 327 */     Checks.notNull(generator, "Generator");
/* 328 */     return (CacheView<E>)new UnifiedCacheViewImpl(generator);
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
/*     */   @Nonnull
/*     */   static ShardCacheView allShards(@Nonnull Collection<ShardCacheView> cacheViews) {
/* 343 */     Checks.noneNull(cacheViews, "Collection");
/* 344 */     Objects.requireNonNull(cacheViews); return (ShardCacheView)new ShardCacheViewImpl.UnifiedShardCacheViewImpl(cacheViews::stream);
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
/*     */   @Nonnull
/*     */   static ShardCacheView allShards(@Nonnull Supplier<? extends Stream<? extends ShardCacheView>> generator) {
/* 359 */     Checks.notNull(generator, "Generator");
/* 360 */     return (ShardCacheView)new ShardCacheViewImpl.UnifiedShardCacheViewImpl(generator);
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
/*     */   @Nonnull
/*     */   static <E extends net.dv8tion.jda.api.entities.ISnowflake> SnowflakeCacheView<E> allSnowflakes(@Nonnull Collection<? extends SnowflakeCacheView<E>> cacheViews) {
/* 379 */     Checks.noneNull(cacheViews, "Collection");
/* 380 */     Objects.requireNonNull(cacheViews); return (SnowflakeCacheView<E>)new UnifiedCacheViewImpl.UnifiedSnowflakeCacheView(cacheViews::stream);
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
/*     */   @Nonnull
/*     */   static <E extends net.dv8tion.jda.api.entities.ISnowflake> SnowflakeCacheView<E> allSnowflakes(@Nonnull Supplier<? extends Stream<? extends SnowflakeCacheView<E>>> generator) {
/* 399 */     Checks.notNull(generator, "Generator");
/* 400 */     return (SnowflakeCacheView<E>)new UnifiedCacheViewImpl.UnifiedSnowflakeCacheView(generator);
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
/*     */   @Nonnull
/*     */   static UnifiedMemberCacheView allMembers(@Nonnull Collection<? extends MemberCacheView> cacheViews) {
/* 416 */     Checks.noneNull(cacheViews, "Collection");
/* 417 */     Objects.requireNonNull(cacheViews); return (UnifiedMemberCacheView)new UnifiedCacheViewImpl.UnifiedMemberCacheViewImpl(cacheViews::stream);
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
/*     */   @Nonnull
/*     */   static UnifiedMemberCacheView allMembers(@Nonnull Supplier<? extends Stream<? extends MemberCacheView>> generator) {
/* 433 */     Checks.notNull(generator, "Generator");
/* 434 */     return (UnifiedMemberCacheView)new UnifiedCacheViewImpl.UnifiedMemberCacheViewImpl(generator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SimpleCacheView<T>
/*     */     extends AbstractCacheView<T>
/*     */   {
/*     */     public SimpleCacheView(@Nonnull Class<T> type, @Nullable Function<T, String> nameMapper) {
/* 448 */       super(type, nameMapper);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\cache\CacheView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */