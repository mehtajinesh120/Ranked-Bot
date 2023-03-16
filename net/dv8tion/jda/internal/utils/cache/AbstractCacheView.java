/*     */ package net.dv8tion.jda.internal.utils.cache;
/*     */ 
/*     */ import gnu.trove.TLongCollection;
/*     */ import gnu.trove.map.TLongObjectMap;
/*     */ import gnu.trove.map.hash.TLongObjectHashMap;
/*     */ import gnu.trove.set.TLongSet;
/*     */ import gnu.trove.set.hash.TLongHashSet;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.utils.ClosableIterator;
/*     */ import net.dv8tion.jda.api.utils.LockIterator;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.api.utils.cache.CacheView;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.UnlockHook;
/*     */ import org.apache.commons.collections4.iterators.ObjectArrayIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractCacheView<T>
/*     */   extends ReadWriteLockCache<T>
/*     */   implements CacheView<T>
/*     */ {
/*  41 */   protected final TLongObjectMap<T> elements = (TLongObjectMap<T>)new TLongObjectHashMap();
/*     */   
/*     */   protected final T[] emptyArray;
/*     */   
/*     */   protected final Function<T, String> nameMapper;
/*     */   protected final Class<T> type;
/*     */   
/*     */   protected AbstractCacheView(Class<T> type, Function<T, String> nameMapper) {
/*  49 */     this.nameMapper = nameMapper;
/*  50 */     this.type = type;
/*  51 */     this.emptyArray = (T[])Array.newInstance(type, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  56 */     UnlockHook hook = writeLock();
/*     */     
/*  58 */     try { this.elements.clear();
/*  59 */       if (hook != null) hook.close();  }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  64 */      } public TLongObjectMap<T> getMap() { if (!this.lock.writeLock().isHeldByCurrentThread())
/*  65 */       throw new IllegalStateException("Cannot access map directly without holding write lock!"); 
/*  66 */     return this.elements; }
/*     */ 
/*     */ 
/*     */   
/*     */   public T get(long id) {
/*  71 */     UnlockHook hook = readLock();
/*     */     
/*  73 */     try { Object object = this.elements.get(id);
/*  74 */       if (hook != null) hook.close();  return (T)object; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  79 */      } public T remove(long id) { UnlockHook hook = writeLock();
/*     */     
/*  81 */     try { Object object = this.elements.remove(id);
/*  82 */       if (hook != null) hook.close();  return (T)object; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  87 */      } public TLongSet keySet() { UnlockHook hook = readLock();
/*     */     
/*  89 */     try { TLongHashSet tLongHashSet = new TLongHashSet((TLongCollection)this.elements.keySet());
/*  90 */       if (hook != null) hook.close();  return (TLongSet)tLongHashSet; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); }
/*     */         catch (Throwable throwable1)
/*     */         { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  96 */      } public void forEach(Consumer<? super T> action) { Objects.requireNonNull(action);
/*  97 */     UnlockHook hook = readLock();
/*     */     
/*  99 */     try { for (T elem : this.elements.valueCollection())
/*     */       {
/* 101 */         action.accept(elem);
/*     */       }
/* 103 */       if (hook != null) hook.close();  }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try {
/*     */           hook.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }   throw throwable; }
/* 110 */      } @Nonnull public LockIterator<T> lockedIterator() { ReentrantReadWriteLock.ReadLock readLock = this.lock.readLock();
/* 111 */     MiscUtil.tryLock(readLock);
/*     */     
/*     */     try {
/* 114 */       Iterator<T> directIterator = this.elements.valueCollection().iterator();
/* 115 */       return new LockIterator(directIterator, readLock);
/*     */     }
/* 117 */     catch (Throwable t) {
/*     */       
/* 119 */       readLock.unlock();
/* 120 */       throw t;
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<T> asList() {
/* 128 */     if (isEmpty())
/* 129 */       return Collections.emptyList(); 
/* 130 */     UnlockHook hook = readLock();
/*     */     
/* 132 */     try { List<T> list = getCachedList();
/* 133 */       if (list != null)
/* 134 */       { List<T> list2 = list;
/*     */ 
/*     */ 
/*     */         
/* 138 */         if (hook != null) hook.close();  return list2; }  list = new ArrayList<>(this.elements.size()); Objects.requireNonNull(list); this.elements.forEachValue(list::add); List<T> list1 = cache(list); if (hook != null) hook.close();  return list1; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try {
/*     */           hook.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }   throw throwable; }
/* 145 */      } @Nonnull public Set<T> asSet() { if (isEmpty())
/* 146 */       return Collections.emptySet(); 
/* 147 */     UnlockHook hook = readLock();
/*     */     
/* 149 */     try { Set<T> set = getCachedSet();
/* 150 */       if (set != null)
/* 151 */       { Set<T> set2 = set;
/*     */ 
/*     */ 
/*     */         
/* 155 */         if (hook != null) hook.close();  return set2; }  set = new HashSet<>(this.elements.size()); Objects.requireNonNull(set); this.elements.forEachValue(set::add); Set<T> set1 = cache(set); if (hook != null) hook.close();  return set1; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); }
/*     */         catch (Throwable throwable1)
/*     */         { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 161 */      } public long size() { return this.elements.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 167 */     return this.elements.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<T> getElementsByName(@Nonnull String name, boolean ignoreCase) {
/* 174 */     Checks.notEmpty(name, "Name");
/* 175 */     if (this.elements.isEmpty())
/* 176 */       return Collections.emptyList(); 
/* 177 */     if (this.nameMapper == null)
/* 178 */       throw new UnsupportedOperationException("The contained elements are not assigned with names."); 
/* 179 */     if (isEmpty())
/* 180 */       return Collections.emptyList(); 
/* 181 */     List<T> list = new ArrayList<>();
/* 182 */     forEach(elem -> {
/*     */           String elementName = this.nameMapper.apply((T)elem);
/*     */           if (elementName != null && equals(ignoreCase, elementName, name)) {
/*     */             list.add(elem);
/*     */           }
/*     */         });
/* 188 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Spliterator<T> spliterator() {
/* 194 */     UnlockHook hook = readLock();
/*     */     
/* 196 */     try { Spliterator<?> spliterator = Spliterators.spliterator(this.elements.values(), 1024);
/* 197 */       if (hook != null) hook.close();  return (Spliterator)spliterator; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try {
/*     */           hook.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }   throw throwable; }
/* 204 */      } @Nonnull public Stream<T> stream() { return StreamSupport.stream(spliterator(), false); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Stream<T> parallelStream() {
/* 211 */     return StreamSupport.stream(spliterator(), true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Iterator<T> iterator() {
/* 218 */     UnlockHook hook = readLock();
/*     */     
/* 220 */     try { ObjectArrayIterator objectArrayIterator = new ObjectArrayIterator(this.elements.values((Object[])this.emptyArray));
/* 221 */       if (hook != null) hook.close();  return (Iterator<T>)objectArrayIterator; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); }
/*     */         catch (Throwable throwable1)
/*     */         { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 227 */      } public String toString() { return asList().toString(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 233 */     UnlockHook hook = readLock();
/*     */     
/* 235 */     try { int i = this.elements.hashCode();
/* 236 */       if (hook != null) hook.close();  return i; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); }
/*     */         catch (Throwable throwable1)
/*     */         { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 242 */      } public boolean equals(Object obj) { if (obj == this)
/* 243 */       return true; 
/* 244 */     if (!(obj instanceof AbstractCacheView))
/* 245 */       return false; 
/* 246 */     AbstractCacheView view = (AbstractCacheView)obj;
/* 247 */     UnlockHook hook = readLock(); try { UnlockHook otherHook = view.readLock();
/*     */       
/* 249 */       try { boolean bool = this.elements.equals(view.elements);
/* 250 */         if (otherHook != null) otherHook.close();  if (hook != null) hook.close();  return bool; } catch (Throwable throwable) { if (otherHook != null)
/*     */           try { otherHook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 255 */      } protected boolean equals(boolean ignoreCase, String first, String second) { return ignoreCase ? first.equalsIgnoreCase(second) : first.equals(second); }
/*     */ 
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\cache\AbstractCacheView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */