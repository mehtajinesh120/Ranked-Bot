/*     */ package net.dv8tion.jda.internal.utils.cache;
/*     */ 
/*     */ import gnu.trove.TIntCollection;
/*     */ import gnu.trove.map.TIntObjectMap;
/*     */ import gnu.trove.map.hash.TIntObjectHashMap;
/*     */ import gnu.trove.set.TIntSet;
/*     */ import gnu.trove.set.hash.TIntHashSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.utils.ClosableIterator;
/*     */ import net.dv8tion.jda.api.utils.LockIterator;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.api.utils.cache.CacheView;
/*     */ import net.dv8tion.jda.api.utils.cache.ShardCacheView;
/*     */ import net.dv8tion.jda.internal.utils.ChainedClosableIterator;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.UnlockHook;
/*     */ import org.apache.commons.collections4.iterators.ObjectArrayIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShardCacheViewImpl
/*     */   extends ReadWriteLockCache<JDA>
/*     */   implements ShardCacheView
/*     */ {
/*  44 */   protected static final JDA[] EMPTY_ARRAY = new JDA[0];
/*     */   
/*     */   protected final TIntObjectMap<JDA> elements;
/*     */   
/*     */   public ShardCacheViewImpl() {
/*  49 */     this.elements = (TIntObjectMap<JDA>)new TIntObjectHashMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public ShardCacheViewImpl(int initialCapacity) {
/*  54 */     this.elements = (TIntObjectMap<JDA>)new TIntObjectHashMap(initialCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  59 */     UnlockHook hook = writeLock();
/*     */     
/*  61 */     try { this.elements.clear();
/*  62 */       if (hook != null) hook.close();  }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  67 */      } public JDA remove(int shardId) { UnlockHook hook = writeLock();
/*     */     
/*  69 */     try { JDA jDA = (JDA)this.elements.remove(shardId);
/*  70 */       if (hook != null) hook.close();  return jDA; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  75 */      } public TIntObjectMap<JDA> getMap() { if (!this.lock.writeLock().isHeldByCurrentThread())
/*  76 */       throw new IllegalStateException("Cannot access map without holding write lock!"); 
/*  77 */     return this.elements; }
/*     */ 
/*     */ 
/*     */   
/*     */   public TIntSet keySet() {
/*  82 */     UnlockHook hook = readLock();
/*     */     
/*  84 */     try { TIntHashSet tIntHashSet = new TIntHashSet((TIntCollection)this.elements.keySet());
/*  85 */       if (hook != null) hook.close();  return (TIntSet)tIntHashSet; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); }
/*     */         catch (Throwable throwable1)
/*     */         { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  91 */      } public void forEach(Consumer<? super JDA> action) { Objects.requireNonNull(action);
/*  92 */     UnlockHook hook = readLock();
/*     */     
/*  94 */     try { for (JDA shard : this.elements.valueCollection())
/*     */       {
/*  96 */         action.accept(shard);
/*     */       }
/*  98 */       if (hook != null) hook.close();  }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try {
/*     */           hook.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }   throw throwable; }
/* 105 */      } @Nonnull public List<JDA> asList() { if (isEmpty())
/* 106 */       return Collections.emptyList(); 
/* 107 */     UnlockHook hook = readLock();
/*     */     
/* 109 */     try { List<JDA> list = getCachedList();
/* 110 */       if (list != null)
/* 111 */       { List<JDA> list2 = list;
/*     */         
/* 113 */         if (hook != null) hook.close();  return list2; }  List<JDA> list1 = cache(new ArrayList<>(this.elements.valueCollection())); if (hook != null) hook.close();  return list1; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try {
/*     */           hook.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }   throw throwable; }
/* 120 */      } @Nonnull public Set<JDA> asSet() { if (isEmpty())
/* 121 */       return Collections.emptySet(); 
/* 122 */     UnlockHook hook = readLock();
/*     */     
/* 124 */     try { Set<JDA> set = getCachedSet();
/* 125 */       if (set != null)
/* 126 */       { Set<JDA> set2 = set;
/*     */         
/* 128 */         if (hook != null) hook.close();  return set2; }  Set<JDA> set1 = cache(new HashSet<>(this.elements.valueCollection())); if (hook != null) hook.close();  return set1; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try {
/*     */           hook.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }   throw throwable; }
/* 135 */      } @Nonnull public LockIterator<JDA> lockedIterator() { ReentrantReadWriteLock.ReadLock readLock = this.lock.readLock();
/* 136 */     MiscUtil.tryLock(readLock);
/*     */     
/*     */     try {
/* 139 */       Iterator<JDA> directIterator = this.elements.valueCollection().iterator();
/* 140 */       return new LockIterator(directIterator, readLock);
/*     */     }
/* 142 */     catch (Throwable t) {
/*     */       
/* 144 */       readLock.unlock();
/* 145 */       throw t;
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long size() {
/* 152 */     return this.elements.size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 158 */     return this.elements.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<JDA> getElementsByName(@Nonnull String name, boolean ignoreCase) {
/* 165 */     Checks.notEmpty(name, "Name");
/* 166 */     if (this.elements.isEmpty()) {
/* 167 */       return Collections.emptyList();
/*     */     }
/* 169 */     UnlockHook hook = readLock();
/*     */     
/* 171 */     try { List<JDA> list = new LinkedList<>();
/* 172 */       for (JDA elem : this.elements.valueCollection()) {
/*     */         
/* 174 */         String elementName = elem.getShardInfo().getShardString();
/* 175 */         if (elementName != null) {
/*     */           
/* 177 */           if (ignoreCase) {
/*     */             
/* 179 */             if (elementName.equalsIgnoreCase(name)) {
/* 180 */               list.add(elem);
/*     */             }
/*     */             continue;
/*     */           } 
/* 184 */           if (elementName.equals(name)) {
/* 185 */             list.add(elem);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 190 */       List<JDA> list1 = list;
/* 191 */       if (hook != null) hook.close();  return list1; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); }
/*     */         catch (Throwable throwable1)
/*     */         { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 197 */      } public Spliterator<JDA> spliterator() { UnlockHook hook = readLock();
/*     */     
/* 199 */     try { Spliterator<JDA> spliterator = Spliterators.spliterator(iterator(), size(), 1280);
/* 200 */       if (hook != null) hook.close();  return spliterator; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try {
/*     */           hook.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }   throw throwable; }
/* 207 */      } @Nonnull public Stream<JDA> stream() { return StreamSupport.stream(spliterator(), false); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Stream<JDA> parallelStream() {
/* 214 */     return StreamSupport.stream(spliterator(), true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Iterator<JDA> iterator() {
/* 221 */     UnlockHook hook = readLock();
/*     */     
/* 223 */     try { JDA[] arr = (JDA[])this.elements.values((Object[])EMPTY_ARRAY);
/* 224 */       ObjectArrayIterator objectArrayIterator = new ObjectArrayIterator((Object[])arr);
/* 225 */       if (hook != null) hook.close();  return (Iterator<JDA>)objectArrayIterator; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); }
/*     */         catch (Throwable throwable1)
/*     */         { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 231 */      } public JDA getElementById(int id) { UnlockHook hook = readLock();
/*     */     
/* 233 */     try { JDA jDA = (JDA)this.elements.get(id);
/* 234 */       if (hook != null) hook.close();  return jDA; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); }
/*     */         catch (Throwable throwable1)
/*     */         { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 240 */      } public int hashCode() { UnlockHook hook = readLock();
/*     */     
/* 242 */     try { int i = this.elements.hashCode();
/* 243 */       if (hook != null) hook.close();  return i; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); }
/*     */         catch (Throwable throwable1)
/*     */         { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 249 */      } public boolean equals(Object obj) { if (obj == this)
/* 250 */       return true; 
/* 251 */     if (!(obj instanceof ShardCacheViewImpl))
/* 252 */       return false; 
/* 253 */     ShardCacheViewImpl view = (ShardCacheViewImpl)obj;
/* 254 */     UnlockHook hook = readLock(); try { UnlockHook otherHook = view.readLock();
/*     */       
/* 256 */       try { boolean bool = this.elements.equals(view.elements);
/* 257 */         if (otherHook != null) otherHook.close();  if (hook != null) hook.close();  return bool; } catch (Throwable throwable) { if (otherHook != null)
/*     */           try { otherHook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 263 */      } public String toString() { return asList().toString(); }
/*     */ 
/*     */   
/*     */   public static class UnifiedShardCacheViewImpl
/*     */     implements ShardCacheView
/*     */   {
/*     */     protected final Supplier<? extends Stream<? extends ShardCacheView>> generator;
/*     */     
/*     */     public UnifiedShardCacheViewImpl(Supplier<? extends Stream<? extends ShardCacheView>> generator) {
/* 272 */       this.generator = generator;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public long size() {
/* 278 */       return distinctStream().mapToLong(CacheView::size).sum();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 284 */       return ((Stream)this.generator.get()).allMatch(CacheView::isEmpty);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public List<JDA> asList() {
/* 291 */       List<JDA> list = new ArrayList<>();
/* 292 */       Objects.requireNonNull(list); stream().forEach(list::add);
/* 293 */       return Collections.unmodifiableList(list);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Set<JDA> asSet() {
/* 300 */       Set<JDA> set = new HashSet<>();
/* 301 */       Objects.requireNonNull(set); ((Stream)this.generator.get()).flatMap(CacheView::stream).forEach(set::add);
/* 302 */       return Collections.unmodifiableSet(set);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public ClosableIterator<JDA> lockedIterator() {
/* 309 */       Iterator<? extends ShardCacheView> gen = ((Stream<? extends ShardCacheView>)this.generator.get()).iterator();
/* 310 */       return (ClosableIterator<JDA>)new ChainedClosableIterator(gen);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public List<JDA> getElementsByName(@Nonnull String name, boolean ignoreCase) {
/* 317 */       return Collections.unmodifiableList((List<? extends JDA>)distinctStream()
/* 318 */           .flatMap(view -> view.getElementsByName(name, ignoreCase).stream())
/* 319 */           .collect(Collectors.toList()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JDA getElementById(int id) {
/* 325 */       return ((Stream)this.generator.get())
/* 326 */         .map(view -> view.getElementById(id))
/* 327 */         .filter(Objects::nonNull)
/* 328 */         .findFirst().orElse(null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Stream<JDA> stream() {
/* 335 */       return ((Stream)this.generator.get()).flatMap(CacheView::stream).distinct();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Stream<JDA> parallelStream() {
/* 342 */       return ((Stream)this.generator.get()).flatMap(CacheView::parallelStream).distinct();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Iterator<JDA> iterator() {
/* 349 */       return stream().iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     protected Stream<? extends ShardCacheView> distinctStream() {
/* 354 */       return ((Stream<? extends ShardCacheView>)this.generator.get()).distinct();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\cache\ShardCacheViewImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */