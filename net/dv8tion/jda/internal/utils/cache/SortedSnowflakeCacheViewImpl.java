/*     */ package net.dv8tion.jda.internal.utils.cache;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.TreeSet;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.entities.ISnowflake;
/*     */ import net.dv8tion.jda.api.utils.cache.SortedSnowflakeCacheView;
/*     */ import net.dv8tion.jda.internal.utils.UnlockHook;
/*     */ import org.apache.commons.collections4.iterators.ObjectArrayIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SortedSnowflakeCacheViewImpl<T extends ISnowflake & Comparable<? super T>>
/*     */   extends SnowflakeCacheViewImpl<T>
/*     */   implements SortedSnowflakeCacheView<T>
/*     */ {
/*     */   protected static final int SPLIT_CHARACTERISTICS = 1296;
/*     */   protected final Comparator<T> comparator;
/*     */   
/*     */   public SortedSnowflakeCacheViewImpl(Class<T> type, Comparator<T> comparator) {
/*  39 */     this(type, (Function<T, String>)null, comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSnowflakeCacheViewImpl(Class<T> type, Function<T, String> nameMapper, Comparator<T> comparator) {
/*  44 */     super(type, nameMapper);
/*  45 */     this.comparator = comparator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void forEach(@Nonnull Consumer<? super T> action) {
/*  51 */     UnlockHook hook = readLock();
/*     */     
/*  53 */     try { iterator().forEachRemaining(action);
/*  54 */       if (hook != null) hook.close();  }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); }
/*     */         catch (Throwable throwable1)
/*     */         { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  60 */      } public void forEachUnordered(@Nonnull Consumer<? super T> action) { super.forEach(action); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<T> asList() {
/*  67 */     if (isEmpty())
/*  68 */       return Collections.emptyList(); 
/*  69 */     UnlockHook hook = readLock();
/*     */     
/*  71 */     try { List<T> list = getCachedList();
/*  72 */       if (list != null)
/*  73 */       { List<T> list2 = list;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  78 */         if (hook != null) hook.close();  return list2; }  list = new ArrayList<>(this.elements.size()); Objects.requireNonNull(list); this.elements.forEachValue(x$0 -> rec$.add(x$0)); list.sort(this.comparator); List<T> list1 = cache(list); if (hook != null) hook.close();  return list1; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try {
/*     */           hook.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }   throw throwable; }
/*  85 */      } @Nonnull public NavigableSet<T> asSet() { if (isEmpty())
/*  86 */       return Collections.emptyNavigableSet(); 
/*  87 */     UnlockHook hook = readLock();
/*     */     
/*  89 */     try { NavigableSet<T> set = (NavigableSet<T>)getCachedSet();
/*  90 */       if (set != null)
/*  91 */       { NavigableSet<T> navigableSet = set;
/*     */ 
/*     */ 
/*     */         
/*  95 */         if (hook != null) hook.close();  return navigableSet; }  set = new TreeSet<>(this.comparator); Objects.requireNonNull(set); this.elements.forEachValue(x$0 -> rec$.add(x$0)); NavigableSet<T> navigableSet1 = cache(set); if (hook != null) hook.close();  return navigableSet1; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try {
/*     */           hook.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }   throw throwable; }
/* 102 */      } @Nonnull public List<T> getElementsByName(@Nonnull String name, boolean ignoreCase) { List<T> filtered = super.getElementsByName(name, ignoreCase);
/* 103 */     filtered.sort(this.comparator);
/* 104 */     return filtered; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Spliterator<T> spliterator() {
/* 110 */     UnlockHook hook = readLock();
/*     */     
/* 112 */     try { Spliterator<T> spliterator = Spliterators.spliterator(iterator(), this.elements.size(), 1296);
/* 113 */       if (hook != null) hook.close();  return spliterator; }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try {
/*     */           hook.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }   throw throwable; }
/* 120 */      } @Nonnull public Stream<T> streamUnordered() { return super.stream(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Stream<T> parallelStreamUnordered() {
/* 127 */     return super.parallelStream();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Stream<T> stream() {
/* 134 */     return super.stream().sorted(this.comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Stream<T> parallelStream() {
/* 141 */     return super.parallelStream().sorted(this.comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Iterator<T> iterator() {
/* 148 */     UnlockHook hook = readLock();
/*     */     try {
/* 150 */       ISnowflake[] arrayOfISnowflake = (ISnowflake[])this.elements.values((Object[])this.emptyArray);
/* 151 */       Arrays.sort(arrayOfISnowflake, this.comparator);
/* 152 */       ObjectArrayIterator objectArrayIterator = new ObjectArrayIterator((Object[])arrayOfISnowflake);
/* 153 */       if (hook != null) hook.close(); 
/*     */       return (Iterator<T>)objectArrayIterator;
/*     */     } catch (Throwable throwable) {
/*     */       if (hook != null)
/*     */         try {
/*     */           hook.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\cache\SortedSnowflakeCacheViewImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */