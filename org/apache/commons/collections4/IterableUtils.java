/*      */ package org.apache.commons.collections4;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.collections4.functors.EqualPredicate;
/*      */ import org.apache.commons.collections4.iterators.LazyIteratorChain;
/*      */ import org.apache.commons.collections4.iterators.ReverseListIterator;
/*      */ import org.apache.commons.collections4.iterators.UniqueFilterIterator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class IterableUtils
/*      */ {
/*   56 */   static final FluentIterable EMPTY_ITERABLE = new FluentIterable()
/*      */     {
/*      */       public Iterator<Object> iterator() {
/*   59 */         return IteratorUtils.emptyIterator();
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> Iterable<E> emptyIterable() {
/*   76 */     return EMPTY_ITERABLE;
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
/*      */   public static <E> Iterable<E> chainedIterable(Iterable<? extends E> a, Iterable<? extends E> b) {
/*  101 */     return chainedIterable((Iterable<? extends E>[])new Iterable[] { a, b });
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
/*      */   public static <E> Iterable<E> chainedIterable(Iterable<? extends E> a, Iterable<? extends E> b, Iterable<? extends E> c) {
/*  125 */     return chainedIterable((Iterable<? extends E>[])new Iterable[] { a, b, c });
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
/*      */   public static <E> Iterable<E> chainedIterable(Iterable<? extends E> a, Iterable<? extends E> b, Iterable<? extends E> c, Iterable<? extends E> d) {
/*  151 */     return chainedIterable((Iterable<? extends E>[])new Iterable[] { a, b, c, d });
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
/*      */   public static <E> Iterable<E> chainedIterable(Iterable<? extends E>... iterables) {
/*  170 */     checkNotNull((Iterable<?>[])iterables);
/*  171 */     return new FluentIterable<E>()
/*      */       {
/*      */         public Iterator<E> iterator() {
/*  174 */           return (Iterator<E>)new LazyIteratorChain<E>()
/*      */             {
/*      */               protected Iterator<? extends E> nextIterator(int count) {
/*  177 */                 if (count > iterables.length) {
/*  178 */                   return null;
/*      */                 }
/*  180 */                 return iterables[count - 1].iterator();
/*      */               }
/*      */             };
/*      */         }
/*      */       };
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
/*      */   public static <E> Iterable<E> collatedIterable(final Iterable<? extends E> a, final Iterable<? extends E> b) {
/*  206 */     checkNotNull((Iterable<?>[])new Iterable[] { a, b });
/*  207 */     return new FluentIterable<E>()
/*      */       {
/*      */         public Iterator<E> iterator() {
/*  210 */           return IteratorUtils.collatedIterator(null, a.iterator(), b.iterator());
/*      */         }
/*      */       };
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
/*      */   public static <E> Iterable<E> collatedIterable(final Comparator<? super E> comparator, final Iterable<? extends E> a, final Iterable<? extends E> b) {
/*  234 */     checkNotNull((Iterable<?>[])new Iterable[] { a, b });
/*  235 */     return new FluentIterable<E>()
/*      */       {
/*      */         public Iterator<E> iterator() {
/*  238 */           return IteratorUtils.collatedIterator(comparator, a.iterator(), b.iterator());
/*      */         }
/*      */       };
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
/*      */   public static <E> Iterable<E> filteredIterable(final Iterable<E> iterable, final Predicate<? super E> predicate) {
/*  261 */     checkNotNull(iterable);
/*  262 */     if (predicate == null) {
/*  263 */       throw new NullPointerException("Predicate must not be null.");
/*      */     }
/*  265 */     return new FluentIterable<E>()
/*      */       {
/*      */         public Iterator<E> iterator() {
/*  268 */           return IteratorUtils.filteredIterator(IterableUtils.emptyIteratorIfNull(iterable), predicate);
/*      */         }
/*      */       };
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
/*      */   public static <E> Iterable<E> boundedIterable(final Iterable<E> iterable, final long maxSize) {
/*  291 */     checkNotNull(iterable);
/*  292 */     if (maxSize < 0L) {
/*  293 */       throw new IllegalArgumentException("MaxSize parameter must not be negative.");
/*      */     }
/*      */     
/*  296 */     return new FluentIterable<E>()
/*      */       {
/*      */         public Iterator<E> iterator() {
/*  299 */           return (Iterator)IteratorUtils.boundedIterator(iterable.iterator(), maxSize);
/*      */         }
/*      */       };
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
/*      */   public static <E> Iterable<E> loopingIterable(final Iterable<E> iterable) {
/*  323 */     checkNotNull(iterable);
/*  324 */     return new FluentIterable<E>()
/*      */       {
/*      */         public Iterator<E> iterator() {
/*  327 */           return (Iterator<E>)new LazyIteratorChain<E>()
/*      */             {
/*      */               protected Iterator<? extends E> nextIterator(int count) {
/*  330 */                 if (IterableUtils.isEmpty(iterable)) {
/*  331 */                   return null;
/*      */                 }
/*  333 */                 return iterable.iterator();
/*      */               }
/*      */             };
/*      */         }
/*      */       };
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
/*      */   public static <E> Iterable<E> reversedIterable(final Iterable<E> iterable) {
/*  361 */     checkNotNull(iterable);
/*  362 */     return new FluentIterable<E>()
/*      */       {
/*      */         public Iterator<E> iterator() {
/*  365 */           List<E> list = (iterable instanceof List) ? (List<E>)iterable : IteratorUtils.<E>toList(iterable.iterator());
/*      */ 
/*      */           
/*  368 */           return (Iterator<E>)new ReverseListIterator(list);
/*      */         }
/*      */       };
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
/*      */   public static <E> Iterable<E> skippingIterable(final Iterable<E> iterable, final long elementsToSkip) {
/*  390 */     checkNotNull(iterable);
/*  391 */     if (elementsToSkip < 0L) {
/*  392 */       throw new IllegalArgumentException("ElementsToSkip parameter must not be negative.");
/*      */     }
/*      */     
/*  395 */     return new FluentIterable<E>()
/*      */       {
/*      */         public Iterator<E> iterator() {
/*  398 */           return (Iterator)IteratorUtils.skippingIterator(iterable.iterator(), elementsToSkip);
/*      */         }
/*      */       };
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
/*      */   public static <I, O> Iterable<O> transformedIterable(final Iterable<I> iterable, final Transformer<? super I, ? extends O> transformer) {
/*  422 */     checkNotNull(iterable);
/*  423 */     if (transformer == null) {
/*  424 */       throw new NullPointerException("Transformer must not be null.");
/*      */     }
/*  426 */     return new FluentIterable<O>()
/*      */       {
/*      */         public Iterator<O> iterator() {
/*  429 */           return IteratorUtils.transformedIterator(iterable.iterator(), transformer);
/*      */         }
/*      */       };
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
/*      */   public static <E> Iterable<E> uniqueIterable(final Iterable<E> iterable) {
/*  450 */     checkNotNull(iterable);
/*  451 */     return new FluentIterable<E>()
/*      */       {
/*      */         public Iterator<E> iterator() {
/*  454 */           return (Iterator<E>)new UniqueFilterIterator(iterable.iterator());
/*      */         }
/*      */       };
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
/*      */   public static <E> Iterable<E> unmodifiableIterable(Iterable<E> iterable) {
/*  473 */     checkNotNull(iterable);
/*  474 */     if (iterable instanceof UnmodifiableIterable) {
/*  475 */       return iterable;
/*      */     }
/*  477 */     return new UnmodifiableIterable<E>(iterable);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class UnmodifiableIterable<E>
/*      */     extends FluentIterable<E>
/*      */   {
/*      */     private final Iterable<E> unmodifiable;
/*      */ 
/*      */     
/*      */     public UnmodifiableIterable(Iterable<E> iterable) {
/*  488 */       this.unmodifiable = iterable;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/*  493 */       return IteratorUtils.unmodifiableIterator(this.unmodifiable.iterator());
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
/*      */   public static <E> Iterable<E> zippingIterable(final Iterable<? extends E> a, final Iterable<? extends E> b) {
/*  518 */     checkNotNull(a);
/*  519 */     checkNotNull(b);
/*  520 */     return new FluentIterable<E>()
/*      */       {
/*      */         public Iterator<E> iterator() {
/*  523 */           return (Iterator)IteratorUtils.zippingIterator(a.iterator(), b.iterator());
/*      */         }
/*      */       };
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
/*      */   public static <E> Iterable<E> zippingIterable(final Iterable<? extends E> first, Iterable<? extends E>... others) {
/*  546 */     checkNotNull(first);
/*  547 */     checkNotNull((Iterable<?>[])others);
/*  548 */     return new FluentIterable<E>()
/*      */       {
/*      */         public Iterator<E> iterator()
/*      */         {
/*  552 */           Iterator[] arrayOfIterator = new Iterator[others.length + 1];
/*  553 */           arrayOfIterator[0] = first.iterator();
/*  554 */           for (int i = 0; i < others.length; i++) {
/*  555 */             arrayOfIterator[i + 1] = others[i].iterator();
/*      */           }
/*  557 */           return (Iterator)IteratorUtils.zippingIterator((Iterator<?>[])arrayOfIterator);
/*      */         }
/*      */       };
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
/*      */   public static <E> Iterable<E> emptyIfNull(Iterable<E> iterable) {
/*  574 */     return (iterable == null) ? emptyIterable() : iterable;
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
/*      */   public static <E> void forEach(Iterable<E> iterable, Closure<? super E> closure) {
/*  586 */     IteratorUtils.forEach(emptyIteratorIfNull(iterable), closure);
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
/*      */   public static <E> E forEachButLast(Iterable<E> iterable, Closure<? super E> closure) {
/*  600 */     return IteratorUtils.forEachButLast(emptyIteratorIfNull(iterable), closure);
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
/*      */   public static <E> E find(Iterable<E> iterable, Predicate<? super E> predicate) {
/*  615 */     return IteratorUtils.find(emptyIteratorIfNull(iterable), predicate);
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
/*      */   public static <E> int indexOf(Iterable<E> iterable, Predicate<? super E> predicate) {
/*  631 */     return IteratorUtils.indexOf(emptyIteratorIfNull(iterable), predicate);
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
/*      */   public static <E> boolean matchesAll(Iterable<E> iterable, Predicate<? super E> predicate) {
/*  647 */     return IteratorUtils.matchesAll(emptyIteratorIfNull(iterable), predicate);
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
/*      */   public static <E> boolean matchesAny(Iterable<E> iterable, Predicate<? super E> predicate) {
/*  662 */     return IteratorUtils.matchesAny(emptyIteratorIfNull(iterable), predicate);
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
/*      */   public static <E> long countMatches(Iterable<E> input, Predicate<? super E> predicate) {
/*  677 */     if (predicate == null) {
/*  678 */       throw new NullPointerException("Predicate must not be null.");
/*      */     }
/*  680 */     return size(filteredIterable(emptyIfNull(input), predicate));
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
/*      */   public static boolean isEmpty(Iterable<?> iterable) {
/*  692 */     if (iterable instanceof Collection) {
/*  693 */       return ((Collection)iterable).isEmpty();
/*      */     }
/*  695 */     return IteratorUtils.isEmpty(emptyIteratorIfNull(iterable));
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
/*      */   public static <E> boolean contains(Iterable<E> iterable, Object object) {
/*  710 */     if (iterable instanceof Collection) {
/*  711 */       return ((Collection)iterable).contains(object);
/*      */     }
/*  713 */     return IteratorUtils.contains(emptyIteratorIfNull(iterable), object);
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
/*      */   public static <E> boolean contains(Iterable<? extends E> iterable, E object, Equator<? super E> equator) {
/*  736 */     if (equator == null) {
/*  737 */       throw new NullPointerException("Equator must not be null.");
/*      */     }
/*  739 */     return matchesAny(iterable, EqualPredicate.equalPredicate(object, equator));
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
/*      */   public static <E, T extends E> int frequency(Iterable<E> iterable, T obj) {
/*  752 */     if (iterable instanceof Set) {
/*  753 */       return ((Set)iterable).contains(obj) ? 1 : 0;
/*      */     }
/*  755 */     if (iterable instanceof Bag) {
/*  756 */       return ((Bag)iterable).getCount(obj);
/*      */     }
/*  758 */     return size(filteredIterable(emptyIfNull(iterable), EqualPredicate.equalPredicate(obj)));
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
/*      */   public static <T> T get(Iterable<T> iterable, int index) {
/*  774 */     CollectionUtils.checkIndexBounds(index);
/*  775 */     if (iterable instanceof List) {
/*  776 */       return ((List<T>)iterable).get(index);
/*      */     }
/*  778 */     return IteratorUtils.get(emptyIteratorIfNull(iterable), index);
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
/*      */   public static int size(Iterable<?> iterable) {
/*  790 */     if (iterable instanceof Collection) {
/*  791 */       return ((Collection)iterable).size();
/*      */     }
/*  793 */     return IteratorUtils.size(emptyIteratorIfNull(iterable));
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
/*      */   public static <O> List<List<O>> partition(Iterable<? extends O> iterable, Predicate<? super O> predicate) {
/*  826 */     if (predicate == null) {
/*  827 */       throw new NullPointerException("Predicate must not be null.");
/*      */     }
/*      */     
/*  830 */     Factory<List<O>> factory = FactoryUtils.instantiateFactory((Class)ArrayList.class);
/*      */     
/*  832 */     Predicate[] arrayOfPredicate = { predicate };
/*  833 */     return partition(iterable, factory, (Predicate<? super O>[])arrayOfPredicate);
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
/*      */   public static <O> List<List<O>> partition(Iterable<? extends O> iterable, Predicate<? super O>... predicates) {
/*  872 */     Factory<List<O>> factory = FactoryUtils.instantiateFactory((Class)ArrayList.class);
/*  873 */     return partition(iterable, factory, predicates);
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
/*      */   public static <O, R extends Collection<O>> List<R> partition(Iterable<? extends O> iterable, Factory<R> partitionFactory, Predicate<? super O>... predicates) {
/*  915 */     if (iterable == null) {
/*  916 */       Iterable<O> empty = emptyIterable();
/*  917 */       return partition(empty, partitionFactory, predicates);
/*      */     } 
/*      */     
/*  920 */     if (predicates == null) {
/*  921 */       throw new NullPointerException("Predicates must not be null.");
/*      */     }
/*      */     
/*  924 */     for (Predicate<?> p : predicates) {
/*  925 */       if (p == null) {
/*  926 */         throw new NullPointerException("Predicate must not be null.");
/*      */       }
/*      */     } 
/*      */     
/*  930 */     if (predicates.length < 1) {
/*      */       
/*  932 */       Collection<O> collection = (Collection)partitionFactory.create();
/*  933 */       CollectionUtils.addAll(collection, iterable);
/*  934 */       return Collections.singletonList((R)collection);
/*      */     } 
/*      */ 
/*      */     
/*  938 */     int numberOfPredicates = predicates.length;
/*  939 */     int numberOfPartitions = numberOfPredicates + 1;
/*  940 */     List<R> partitions = new ArrayList<R>(numberOfPartitions);
/*  941 */     for (int i = 0; i < numberOfPartitions; i++) {
/*  942 */       partitions.add(partitionFactory.create());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  949 */     for (O element : iterable) {
/*  950 */       boolean elementAssigned = false;
/*  951 */       for (int j = 0; j < numberOfPredicates; j++) {
/*  952 */         if (predicates[j].evaluate(element)) {
/*  953 */           ((Collection<O>)partitions.get(j)).add(element);
/*  954 */           elementAssigned = true;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  959 */       if (!elementAssigned)
/*      */       {
/*      */         
/*  962 */         ((Collection<O>)partitions.get(numberOfPredicates)).add(element);
/*      */       }
/*      */     } 
/*      */     
/*  966 */     return partitions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> List<E> toList(Iterable<E> iterable) {
/*  977 */     return IteratorUtils.toList(emptyIteratorIfNull(iterable));
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
/*      */   public static <E> String toString(Iterable<E> iterable) {
/*  993 */     return IteratorUtils.toString(emptyIteratorIfNull(iterable));
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
/*      */   public static <E> String toString(Iterable<E> iterable, Transformer<? super E, String> transformer) {
/* 1012 */     if (transformer == null) {
/* 1013 */       throw new NullPointerException("Transformer must not be null.");
/*      */     }
/* 1015 */     return IteratorUtils.toString(emptyIteratorIfNull(iterable), transformer);
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
/*      */   public static <E> String toString(Iterable<E> iterable, Transformer<? super E, String> transformer, String delimiter, String prefix, String suffix) {
/* 1040 */     return IteratorUtils.toString(emptyIteratorIfNull(iterable), transformer, delimiter, prefix, suffix);
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
/*      */   static void checkNotNull(Iterable<?> iterable) {
/* 1054 */     if (iterable == null) {
/* 1055 */       throw new NullPointerException("Iterable must not be null.");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void checkNotNull(Iterable<?>... iterables) {
/* 1066 */     if (iterables == null) {
/* 1067 */       throw new NullPointerException("Iterables must not be null.");
/*      */     }
/* 1069 */     for (Iterable<?> iterable : iterables) {
/* 1070 */       checkNotNull(iterable);
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
/*      */   private static <E> Iterator<E> emptyIteratorIfNull(Iterable<E> iterable) {
/* 1083 */     return (iterable != null) ? iterable.iterator() : IteratorUtils.<E>emptyIterator();
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\IterableUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */