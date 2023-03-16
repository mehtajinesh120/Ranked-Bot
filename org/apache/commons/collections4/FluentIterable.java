/*     */ package org.apache.commons.collections4;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.commons.collections4.iterators.SingletonIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FluentIterable<E>
/*     */   implements Iterable<E>
/*     */ {
/*     */   private final Iterable<E> iterable;
/*     */   
/*     */   public static <T> FluentIterable<T> empty() {
/*  82 */     return IterableUtils.EMPTY_ITERABLE;
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
/*     */   public static <T> FluentIterable<T> of(T singleton) {
/*  95 */     return of(IteratorUtils.asIterable((Iterator<? extends T>)new SingletonIterator(singleton, false)));
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
/*     */   public static <T> FluentIterable<T> of(T... elements) {
/* 108 */     return of(Arrays.asList(elements));
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
/*     */   public static <T> FluentIterable<T> of(Iterable<T> iterable) {
/* 125 */     IterableUtils.checkNotNull(iterable);
/* 126 */     if (iterable instanceof FluentIterable) {
/* 127 */       return (FluentIterable<T>)iterable;
/*     */     }
/* 129 */     return new FluentIterable<T>(iterable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   FluentIterable() {
/* 140 */     this.iterable = this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private FluentIterable(Iterable<E> iterable) {
/* 148 */     this.iterable = iterable;
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
/*     */   public FluentIterable<E> append(E... elements) {
/* 163 */     return append(Arrays.asList(elements));
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
/*     */   public FluentIterable<E> append(Iterable<? extends E> other) {
/* 176 */     return of(IterableUtils.chainedIterable(this.iterable, other));
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
/*     */   public FluentIterable<E> collate(Iterable<? extends E> other) {
/* 198 */     return of(IterableUtils.collatedIterable(this.iterable, other));
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
/*     */   public FluentIterable<E> collate(Iterable<? extends E> other, Comparator<? super E> comparator) {
/* 224 */     return of(IterableUtils.collatedIterable(comparator, this.iterable, other));
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
/*     */   public FluentIterable<E> eval() {
/* 241 */     return of(toList());
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
/*     */   public FluentIterable<E> filter(Predicate<? super E> predicate) {
/* 253 */     return of(IterableUtils.filteredIterable(this.iterable, predicate));
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
/*     */   public FluentIterable<E> limit(long maxSize) {
/* 265 */     return of(IterableUtils.boundedIterable(this.iterable, maxSize));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FluentIterable<E> loop() {
/* 275 */     return of(IterableUtils.loopingIterable(this.iterable));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FluentIterable<E> reverse() {
/* 285 */     return of(IterableUtils.reversedIterable(this.iterable));
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
/*     */   public FluentIterable<E> skip(long elementsToSkip) {
/* 298 */     return of(IterableUtils.skippingIterable(this.iterable, elementsToSkip));
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
/*     */   public <O> FluentIterable<O> transform(Transformer<? super E, ? extends O> transformer) {
/* 311 */     return of(IterableUtils.transformedIterable(this.iterable, transformer));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FluentIterable<E> unique() {
/* 321 */     return of(IterableUtils.uniqueIterable(this.iterable));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FluentIterable<E> unmodifiable() {
/* 331 */     return of(IterableUtils.unmodifiableIterable(this.iterable));
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
/*     */   public FluentIterable<E> zip(Iterable<? extends E> other) {
/* 344 */     return of(IterableUtils.zippingIterable(this.iterable, other));
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
/*     */   public FluentIterable<E> zip(Iterable<? extends E>... others) {
/* 357 */     return of(IterableUtils.zippingIterable(this.iterable, others));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 366 */     return this.iterable.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration<E> asEnumeration() {
/* 376 */     return IteratorUtils.asEnumeration(iterator());
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
/*     */   public boolean allMatch(Predicate<? super E> predicate) {
/* 391 */     return IterableUtils.matchesAll(this.iterable, predicate);
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
/*     */   public boolean anyMatch(Predicate<? super E> predicate) {
/* 405 */     return IterableUtils.matchesAny(this.iterable, predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 414 */     return IterableUtils.isEmpty(this.iterable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/* 424 */     return IterableUtils.contains(this.iterable, object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void forEach(Closure<? super E> closure) {
/* 434 */     IterableUtils.forEach(this.iterable, closure);
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
/*     */   public E get(int position) {
/* 448 */     return IterableUtils.get(this.iterable, position);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 458 */     return IterableUtils.size(this.iterable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyInto(Collection<? super E> collection) {
/* 469 */     if (collection == null) {
/* 470 */       throw new NullPointerException("Collection must not be null");
/*     */     }
/* 472 */     CollectionUtils.addAll(collection, this.iterable);
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
/*     */   public E[] toArray(Class<E> arrayClass) {
/* 484 */     return IteratorUtils.toArray(iterator(), arrayClass);
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
/*     */   public List<E> toList() {
/* 496 */     return IterableUtils.toList(this.iterable);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 502 */     return IterableUtils.toString(this.iterable);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\FluentIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */