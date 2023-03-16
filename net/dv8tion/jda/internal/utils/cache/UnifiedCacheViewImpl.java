/*     */ package net.dv8tion.jda.internal.utils.cache;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.ISnowflake;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.utils.ClosableIterator;
/*     */ import net.dv8tion.jda.api.utils.cache.CacheView;
/*     */ import net.dv8tion.jda.api.utils.cache.MemberCacheView;
/*     */ import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
/*     */ import net.dv8tion.jda.api.utils.cache.UnifiedMemberCacheView;
/*     */ import net.dv8tion.jda.internal.utils.ChainedClosableIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnifiedCacheViewImpl<T, E extends CacheView<T>>
/*     */   implements CacheView<T>
/*     */ {
/*     */   protected final Supplier<? extends Stream<? extends E>> generator;
/*     */   
/*     */   public UnifiedCacheViewImpl(Supplier<? extends Stream<? extends E>> generator) {
/*  43 */     this.generator = generator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long size() {
/*  49 */     return distinctStream().mapToLong(CacheView::size).sum();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  55 */     return distinctStream().allMatch(CacheView::isEmpty);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super T> action) {
/*  61 */     Objects.requireNonNull(action);
/*  62 */     ChainedClosableIterator<T> chainedClosableIterator = lockedIterator();
/*     */     
/*  64 */     try { while (chainedClosableIterator.hasNext())
/*  65 */         action.accept((T)chainedClosableIterator.next()); 
/*  66 */       if (chainedClosableIterator != null) chainedClosableIterator.close();  }
/*     */     catch (Throwable throwable) { if (chainedClosableIterator != null)
/*     */         try {
/*     */           chainedClosableIterator.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }   throw throwable; }
/*  73 */      } @Nonnull public List<T> asList() { List<T> list = new LinkedList<>();
/*  74 */     Objects.requireNonNull(list); forEach(list::add);
/*  75 */     return Collections.unmodifiableList(list); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Set<T> asSet() {
/*  82 */     ChainedClosableIterator<T> it = lockedIterator();
/*     */ 
/*     */ 
/*     */     
/*  86 */     try { for (; it.hasNext(); it.next());
/*  87 */       Set<?> set = Collections.unmodifiableSet(it.getItems());
/*  88 */       if (it != null) it.close();  return (Set)set; }
/*     */     catch (Throwable throwable) { if (it != null)
/*     */         try {
/*     */           it.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }   throw throwable; }
/*  95 */      } @Nonnull public ChainedClosableIterator<T> lockedIterator() { Iterator<? extends E> gen = ((Stream<? extends E>)this.generator.get()).iterator();
/*  96 */     return new ChainedClosableIterator(gen); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<T> getElementsByName(@Nonnull String name, boolean ignoreCase) {
/* 103 */     return Collections.unmodifiableList((List<? extends T>)distinctStream()
/* 104 */         .flatMap(view -> view.getElementsByName(name, ignoreCase).stream())
/* 105 */         .distinct()
/* 106 */         .collect(Collectors.toList()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Stream<T> stream() {
/* 113 */     return distinctStream().<T>flatMap(CacheView::stream).distinct();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Stream<T> parallelStream() {
/* 120 */     return distinctStream().<T>flatMap(CacheView::parallelStream).distinct();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Iterator<T> iterator() {
/* 127 */     return stream().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Stream<? extends E> distinctStream() {
/* 132 */     return ((Stream<? extends E>)this.generator.get()).distinct();
/*     */   }
/*     */   
/*     */   public static class UnifiedSnowflakeCacheView<T extends ISnowflake>
/*     */     extends UnifiedCacheViewImpl<T, SnowflakeCacheView<T>>
/*     */     implements SnowflakeCacheView<T>
/*     */   {
/*     */     public UnifiedSnowflakeCacheView(Supplier<? extends Stream<? extends SnowflakeCacheView<T>>> generator) {
/* 140 */       super(generator);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public T getElementById(long id) {
/* 146 */       return (T)((Stream)this.generator.get())
/* 147 */         .map(view -> view.getElementById(id))
/* 148 */         .filter(Objects::nonNull)
/* 149 */         .findFirst().orElse(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class UnifiedMemberCacheViewImpl
/*     */     extends UnifiedCacheViewImpl<Member, MemberCacheView>
/*     */     implements UnifiedMemberCacheView
/*     */   {
/*     */     public UnifiedMemberCacheViewImpl(Supplier<? extends Stream<? extends MemberCacheView>> generator) {
/* 159 */       super(generator);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public List<Member> getElementsById(long id) {
/* 166 */       return Collections.unmodifiableList((List<? extends Member>)distinctStream()
/* 167 */           .map(view -> view.getElementById(id))
/* 168 */           .filter(Objects::nonNull)
/* 169 */           .collect(Collectors.toList()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public List<Member> getElementsByUsername(@Nonnull String name, boolean ignoreCase) {
/* 176 */       return Collections.unmodifiableList((List<? extends Member>)distinctStream()
/* 177 */           .flatMap(view -> view.getElementsByUsername(name, ignoreCase).stream())
/* 178 */           .collect(Collectors.toList()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public List<Member> getElementsByNickname(@Nullable String name, boolean ignoreCase) {
/* 185 */       return Collections.unmodifiableList((List<? extends Member>)distinctStream()
/* 186 */           .flatMap(view -> view.getElementsByNickname(name, ignoreCase).stream())
/* 187 */           .collect(Collectors.toList()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public List<Member> getElementsWithRoles(@Nonnull Role... roles) {
/* 194 */       return Collections.unmodifiableList((List<? extends Member>)distinctStream()
/* 195 */           .flatMap(view -> view.getElementsWithRoles(roles).stream())
/* 196 */           .collect(Collectors.toList()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public List<Member> getElementsWithRoles(@Nonnull Collection<Role> roles) {
/* 203 */       return Collections.unmodifiableList((List<? extends Member>)distinctStream()
/* 204 */           .flatMap(view -> view.getElementsWithRoles(roles).stream())
/* 205 */           .collect(Collectors.toList()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\cache\UnifiedCacheViewImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */