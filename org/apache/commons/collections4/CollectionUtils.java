/*      */ package org.apache.commons.collections4;
/*      */ 
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.collections4.bag.HashBag;
/*      */ import org.apache.commons.collections4.collection.PredicatedCollection;
/*      */ import org.apache.commons.collections4.collection.SynchronizedCollection;
/*      */ import org.apache.commons.collections4.collection.TransformedCollection;
/*      */ import org.apache.commons.collections4.collection.UnmodifiableBoundedCollection;
/*      */ import org.apache.commons.collections4.collection.UnmodifiableCollection;
/*      */ import org.apache.commons.collections4.functors.TruePredicate;
/*      */ import org.apache.commons.collections4.iterators.CollatingIterator;
/*      */ import org.apache.commons.collections4.iterators.PermutationIterator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CollectionUtils
/*      */ {
/*      */   private static class CardinalityHelper<O>
/*      */   {
/*      */     final Map<O, Integer> cardinalityA;
/*      */     final Map<O, Integer> cardinalityB;
/*      */     
/*      */     public CardinalityHelper(Iterable<? extends O> a, Iterable<? extends O> b) {
/*   75 */       this.cardinalityA = CollectionUtils.getCardinalityMap(a);
/*   76 */       this.cardinalityB = CollectionUtils.getCardinalityMap(b);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final int max(Object obj) {
/*   85 */       return Math.max(freqA(obj), freqB(obj));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final int min(Object obj) {
/*   94 */       return Math.min(freqA(obj), freqB(obj));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int freqA(Object obj) {
/*  103 */       return getFreq(obj, this.cardinalityA);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int freqB(Object obj) {
/*  112 */       return getFreq(obj, this.cardinalityB);
/*      */     }
/*      */     
/*      */     private final int getFreq(Object obj, Map<?, Integer> freqMap) {
/*  116 */       Integer count = freqMap.get(obj);
/*  117 */       if (count != null) {
/*  118 */         return count.intValue();
/*      */       }
/*  120 */       return 0;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class SetOperationCardinalityHelper<O>
/*      */     extends CardinalityHelper<O>
/*      */     implements Iterable<O>
/*      */   {
/*      */     private final Set<O> elements;
/*      */ 
/*      */ 
/*      */     
/*      */     private final List<O> newList;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SetOperationCardinalityHelper(Iterable<? extends O> a, Iterable<? extends O> b) {
/*  142 */       super(a, b);
/*  143 */       this.elements = new HashSet<O>();
/*  144 */       CollectionUtils.addAll(this.elements, a);
/*  145 */       CollectionUtils.addAll(this.elements, b);
/*      */       
/*  147 */       this.newList = new ArrayList<O>(this.elements.size());
/*      */     }
/*      */     
/*      */     public Iterator<O> iterator() {
/*  151 */       return this.elements.iterator();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setCardinality(O obj, int count) {
/*  160 */       for (int i = 0; i < count; i++) {
/*  161 */         this.newList.add(obj);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Collection<O> list() {
/*  170 */       return this.newList;
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
/*  182 */   public static final Collection EMPTY_COLLECTION = UnmodifiableCollection.unmodifiableCollection(new ArrayList());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Collection<T> emptyCollection() {
/*  200 */     return EMPTY_COLLECTION;
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
/*      */   public static <T> Collection<T> emptyIfNull(Collection<T> collection) {
/*  213 */     return (collection == null) ? EMPTY_COLLECTION : collection;
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
/*      */   public static <O> Collection<O> union(Iterable<? extends O> a, Iterable<? extends O> b) {
/*  232 */     SetOperationCardinalityHelper<O> helper = new SetOperationCardinalityHelper<O>(a, b);
/*  233 */     for (O obj : helper) {
/*  234 */       helper.setCardinality(obj, helper.max(obj));
/*      */     }
/*  236 */     return helper.list();
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
/*      */   public static <O> Collection<O> intersection(Iterable<? extends O> a, Iterable<? extends O> b) {
/*  256 */     SetOperationCardinalityHelper<O> helper = new SetOperationCardinalityHelper<O>(a, b);
/*  257 */     for (O obj : helper) {
/*  258 */       helper.setCardinality(obj, helper.min(obj));
/*      */     }
/*  260 */     return helper.list();
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
/*      */   public static <O> Collection<O> disjunction(Iterable<? extends O> a, Iterable<? extends O> b) {
/*  284 */     SetOperationCardinalityHelper<O> helper = new SetOperationCardinalityHelper<O>(a, b);
/*  285 */     for (O obj : helper) {
/*  286 */       helper.setCardinality(obj, helper.max(obj) - helper.min(obj));
/*      */     }
/*  288 */     return helper.list();
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
/*      */   public static <O> Collection<O> subtract(Iterable<? extends O> a, Iterable<? extends O> b) {
/*  305 */     Predicate<O> p = TruePredicate.truePredicate();
/*  306 */     return subtract(a, b, p);
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
/*      */   public static <O> Collection<O> subtract(Iterable<? extends O> a, Iterable<? extends O> b, Predicate<O> p) {
/*  333 */     ArrayList<O> list = new ArrayList<O>();
/*  334 */     HashBag<O> bag = new HashBag();
/*  335 */     for (O element : b) {
/*  336 */       if (p.evaluate(element)) {
/*  337 */         bag.add(element);
/*      */       }
/*      */     } 
/*  340 */     for (O element : a) {
/*  341 */       if (!bag.remove(element, 1)) {
/*  342 */         list.add(element);
/*      */       }
/*      */     } 
/*  345 */     return list;
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
/*      */   public static boolean containsAll(Collection<?> coll1, Collection<?> coll2) {
/*  371 */     if (coll2.isEmpty()) {
/*  372 */       return true;
/*      */     }
/*  374 */     Iterator<?> it = coll1.iterator();
/*  375 */     Set<Object> elementsAlreadySeen = new HashSet();
/*  376 */     for (Object nextElement : coll2) {
/*  377 */       if (elementsAlreadySeen.contains(nextElement)) {
/*      */         continue;
/*      */       }
/*      */       
/*  381 */       boolean foundCurrentElement = false;
/*  382 */       while (it.hasNext()) {
/*  383 */         Object p = it.next();
/*  384 */         elementsAlreadySeen.add(p);
/*  385 */         if ((nextElement == null) ? (p == null) : nextElement.equals(p)) {
/*  386 */           foundCurrentElement = true;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  391 */       if (foundCurrentElement) {
/*      */         continue;
/*      */       }
/*  394 */       return false;
/*      */     } 
/*      */     
/*  397 */     return true;
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
/*      */   public static boolean containsAny(Collection<?> coll1, Collection<?> coll2) {
/*  414 */     if (coll1.size() < coll2.size()) {
/*  415 */       for (Object aColl1 : coll1) {
/*  416 */         if (coll2.contains(aColl1)) {
/*  417 */           return true;
/*      */         }
/*      */       } 
/*      */     } else {
/*  421 */       for (Object aColl2 : coll2) {
/*  422 */         if (coll1.contains(aColl2)) {
/*  423 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*  427 */     return false;
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
/*      */   public static <O> Map<O, Integer> getCardinalityMap(Iterable<? extends O> coll) {
/*  443 */     Map<O, Integer> count = new HashMap<O, Integer>();
/*  444 */     for (O obj : coll) {
/*  445 */       Integer c = count.get(obj);
/*  446 */       if (c == null) {
/*  447 */         count.put(obj, Integer.valueOf(1)); continue;
/*      */       } 
/*  449 */       count.put(obj, Integer.valueOf(c.intValue() + 1));
/*      */     } 
/*      */     
/*  452 */     return count;
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
/*      */   public static boolean isSubCollection(Collection<?> a, Collection<?> b) {
/*  468 */     CardinalityHelper<Object> helper = new CardinalityHelper(a, b);
/*  469 */     for (Object obj : a) {
/*  470 */       if (helper.freqA(obj) > helper.freqB(obj)) {
/*  471 */         return false;
/*      */       }
/*      */     } 
/*  474 */     return true;
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
/*      */   public static boolean isProperSubCollection(Collection<?> a, Collection<?> b) {
/*  499 */     return (a.size() < b.size() && isSubCollection(a, b));
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
/*      */   public static boolean isEqualCollection(Collection<?> a, Collection<?> b) {
/*  515 */     if (a.size() != b.size()) {
/*  516 */       return false;
/*      */     }
/*  518 */     CardinalityHelper<Object> helper = new CardinalityHelper(a, b);
/*  519 */     if (helper.cardinalityA.size() != helper.cardinalityB.size()) {
/*  520 */       return false;
/*      */     }
/*  522 */     for (Object obj : helper.cardinalityA.keySet()) {
/*  523 */       if (helper.freqA(obj) != helper.freqB(obj)) {
/*  524 */         return false;
/*      */       }
/*      */     } 
/*  527 */     return true;
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
/*      */   public static <E> boolean isEqualCollection(Collection<? extends E> a, Collection<? extends E> b, final Equator<? super E> equator) {
/*  554 */     if (equator == null) {
/*  555 */       throw new NullPointerException("Equator must not be null.");
/*      */     }
/*      */     
/*  558 */     if (a.size() != b.size()) {
/*  559 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  563 */     Transformer<E, ?> transformer = new Transformer<E, Object>() {
/*      */         public CollectionUtils.EquatorWrapper<?> transform(Object input) {
/*  565 */           return new CollectionUtils.EquatorWrapper(equator, input);
/*      */         }
/*      */       };
/*      */     
/*  569 */     return isEqualCollection(collect(a, transformer), collect(b, transformer));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class EquatorWrapper<O>
/*      */   {
/*      */     private final Equator<? super O> equator;
/*      */ 
/*      */ 
/*      */     
/*      */     private final O object;
/*      */ 
/*      */ 
/*      */     
/*      */     public EquatorWrapper(Equator<? super O> equator, O object) {
/*  586 */       this.equator = equator;
/*  587 */       this.object = object;
/*      */     }
/*      */     
/*      */     public O getObject() {
/*  591 */       return this.object;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/*  596 */       if (!(obj instanceof EquatorWrapper)) {
/*  597 */         return false;
/*      */       }
/*      */       
/*  600 */       EquatorWrapper<O> otherObj = (EquatorWrapper<O>)obj;
/*  601 */       return this.equator.equate(this.object, otherObj.getObject());
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  606 */       return this.equator.hash(this.object);
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
/*      */   @Deprecated
/*      */   public static <O> int cardinality(O obj, Iterable<? super O> coll) {
/*  623 */     if (coll == null) {
/*  624 */       throw new NullPointerException("coll must not be null.");
/*      */     }
/*  626 */     return IterableUtils.frequency(coll, obj);
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
/*      */   @Deprecated
/*      */   public static <T> T find(Iterable<T> collection, Predicate<? super T> predicate) {
/*  643 */     return (predicate != null) ? IterableUtils.<T>find(collection, predicate) : null;
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
/*      */   @Deprecated
/*      */   public static <T, C extends Closure<? super T>> C forAllDo(Iterable<T> collection, C closure) {
/*  660 */     if (closure != null) {
/*  661 */       IterableUtils.forEach(collection, (Closure<? super T>)closure);
/*      */     }
/*  663 */     return closure;
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
/*      */   @Deprecated
/*      */   public static <T, C extends Closure<? super T>> C forAllDo(Iterator<T> iterator, C closure) {
/*  681 */     if (closure != null) {
/*  682 */       IteratorUtils.forEach(iterator, (Closure<? super T>)closure);
/*      */     }
/*  684 */     return closure;
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
/*      */   @Deprecated
/*      */   public static <T, C extends Closure<? super T>> T forAllButLastDo(Iterable<T> collection, C closure) {
/*  703 */     return (closure != null) ? IterableUtils.<T>forEachButLast(collection, (Closure<? super T>)closure) : null;
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
/*      */   @Deprecated
/*      */   public static <T, C extends Closure<? super T>> T forAllButLastDo(Iterator<T> iterator, C closure) {
/*  721 */     return (closure != null) ? IteratorUtils.<T>forEachButLast(iterator, (Closure<? super T>)closure) : null;
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
/*      */   public static <T> boolean filter(Iterable<T> collection, Predicate<? super T> predicate) {
/*  736 */     boolean result = false;
/*  737 */     if (collection != null && predicate != null) {
/*  738 */       for (Iterator<T> it = collection.iterator(); it.hasNext();) {
/*  739 */         if (!predicate.evaluate(it.next())) {
/*  740 */           it.remove();
/*  741 */           result = true;
/*      */         } 
/*      */       } 
/*      */     }
/*  745 */     return result;
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
/*      */   public static <T> boolean filterInverse(Iterable<T> collection, Predicate<? super T> predicate) {
/*  763 */     return filter(collection, (predicate == null) ? null : PredicateUtils.<T>notPredicate(predicate));
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
/*      */   public static <C> void transform(Collection<C> collection, Transformer<? super C, ? extends C> transformer) {
/*  786 */     if (collection != null && transformer != null) {
/*  787 */       if (collection instanceof List) {
/*  788 */         List<C> list = (List<C>)collection;
/*  789 */         for (ListIterator<C> it = list.listIterator(); it.hasNext();) {
/*  790 */           it.set(transformer.transform(it.next()));
/*      */         }
/*      */       } else {
/*  793 */         Collection<C> resultCollection = collect(collection, transformer);
/*  794 */         collection.clear();
/*  795 */         collection.addAll(resultCollection);
/*      */       } 
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
/*      */   @Deprecated
/*      */   public static <C> int countMatches(Iterable<C> input, Predicate<? super C> predicate) {
/*  814 */     return (predicate == null) ? 0 : (int)IterableUtils.<C>countMatches(input, predicate);
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
/*      */   @Deprecated
/*      */   public static <C> boolean exists(Iterable<C> input, Predicate<? super C> predicate) {
/*  831 */     return (predicate == null) ? false : IterableUtils.<C>matchesAny(input, predicate);
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
/*      */   @Deprecated
/*      */   public static <C> boolean matchesAll(Iterable<C> input, Predicate<? super C> predicate) {
/*  851 */     return (predicate == null) ? false : IterableUtils.<C>matchesAll(input, predicate);
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
/*      */   public static <O> Collection<O> select(Iterable<? extends O> inputCollection, Predicate<? super O> predicate) {
/*  868 */     Collection<O> answer = (inputCollection instanceof Collection) ? new ArrayList<O>(((Collection)inputCollection).size()) : new ArrayList<O>();
/*      */     
/*  870 */     return select(inputCollection, predicate, answer);
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
/*      */   public static <O, R extends Collection<? super O>> R select(Iterable<? extends O> inputCollection, Predicate<? super O> predicate, R outputCollection) {
/*  891 */     if (inputCollection != null && predicate != null) {
/*  892 */       for (O item : inputCollection) {
/*  893 */         if (predicate.evaluate(item)) {
/*  894 */           outputCollection.add(item);
/*      */         }
/*      */       } 
/*      */     }
/*  898 */     return outputCollection;
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
/*      */   public static <O, R extends Collection<? super O>> R select(Iterable<? extends O> inputCollection, Predicate<? super O> predicate, R outputCollection, R rejectedCollection) {
/*  931 */     if (inputCollection != null && predicate != null) {
/*  932 */       for (O element : inputCollection) {
/*  933 */         if (predicate.evaluate(element)) {
/*  934 */           outputCollection.add(element); continue;
/*      */         } 
/*  936 */         rejectedCollection.add(element);
/*      */       } 
/*      */     }
/*      */     
/*  940 */     return outputCollection;
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
/*      */   public static <O> Collection<O> selectRejected(Iterable<? extends O> inputCollection, Predicate<? super O> predicate) {
/*  958 */     Collection<O> answer = (inputCollection instanceof Collection) ? new ArrayList<O>(((Collection)inputCollection).size()) : new ArrayList<O>();
/*      */     
/*  960 */     return selectRejected(inputCollection, predicate, answer);
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
/*      */   public static <O, R extends Collection<? super O>> R selectRejected(Iterable<? extends O> inputCollection, Predicate<? super O> predicate, R outputCollection) {
/*  981 */     if (inputCollection != null && predicate != null) {
/*  982 */       for (O item : inputCollection) {
/*  983 */         if (!predicate.evaluate(item)) {
/*  984 */           outputCollection.add(item);
/*      */         }
/*      */       } 
/*      */     }
/*  988 */     return outputCollection;
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
/*      */   public static <I, O> Collection<O> collect(Iterable<I> inputCollection, Transformer<? super I, ? extends O> transformer) {
/* 1006 */     Collection<O> answer = (inputCollection instanceof Collection) ? new ArrayList<O>(((Collection)inputCollection).size()) : new ArrayList<O>();
/*      */     
/* 1008 */     return collect(inputCollection, transformer, answer);
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
/*      */   public static <I, O> Collection<O> collect(Iterator<I> inputIterator, Transformer<? super I, ? extends O> transformer) {
/* 1025 */     return collect(inputIterator, transformer, new ArrayList<O>());
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
/*      */   public static <I, O, R extends Collection<? super O>> R collect(Iterable<? extends I> inputCollection, Transformer<? super I, ? extends O> transformer, R outputCollection) {
/* 1048 */     if (inputCollection != null) {
/* 1049 */       return collect(inputCollection.iterator(), transformer, outputCollection);
/*      */     }
/* 1051 */     return outputCollection;
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
/*      */   public static <I, O, R extends Collection<? super O>> R collect(Iterator<? extends I> inputIterator, Transformer<? super I, ? extends O> transformer, R outputCollection) {
/* 1074 */     if (inputIterator != null && transformer != null) {
/* 1075 */       while (inputIterator.hasNext()) {
/* 1076 */         I item = inputIterator.next();
/* 1077 */         O value = transformer.transform(item);
/* 1078 */         outputCollection.add(value);
/*      */       } 
/*      */     }
/* 1081 */     return outputCollection;
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
/*      */   public static <T> boolean addIgnoreNull(Collection<T> collection, T object) {
/* 1096 */     if (collection == null) {
/* 1097 */       throw new NullPointerException("The collection must not be null");
/*      */     }
/* 1099 */     return (object != null && collection.add(object));
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
/*      */   public static <C> boolean addAll(Collection<C> collection, Iterable<? extends C> iterable) {
/* 1114 */     if (iterable instanceof Collection) {
/* 1115 */       return collection.addAll((Collection<? extends C>)iterable);
/*      */     }
/* 1117 */     return addAll(collection, iterable.iterator());
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
/*      */   public static <C> boolean addAll(Collection<C> collection, Iterator<? extends C> iterator) {
/* 1130 */     boolean changed = false;
/* 1131 */     while (iterator.hasNext()) {
/* 1132 */       changed |= collection.add(iterator.next());
/*      */     }
/* 1134 */     return changed;
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
/*      */   public static <C> boolean addAll(Collection<C> collection, Enumeration<? extends C> enumeration) {
/* 1147 */     boolean changed = false;
/* 1148 */     while (enumeration.hasMoreElements()) {
/* 1149 */       changed |= collection.add(enumeration.nextElement());
/*      */     }
/* 1151 */     return changed;
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
/*      */   public static <C> boolean addAll(Collection<C> collection, C[] elements) {
/* 1164 */     boolean changed = false;
/* 1165 */     for (C element : elements) {
/* 1166 */       changed |= collection.add(element);
/*      */     }
/* 1168 */     return changed;
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
/*      */   @Deprecated
/*      */   public static <T> T get(Iterator<T> iterator, int index) {
/* 1188 */     return IteratorUtils.get(iterator, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void checkIndexBounds(int index) {
/* 1197 */     if (index < 0) {
/* 1198 */       throw new IndexOutOfBoundsException("Index cannot be negative: " + index);
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
/*      */   @Deprecated
/*      */   public static <T> T get(Iterable<T> iterable, int index) {
/* 1217 */     return IterableUtils.get(iterable, index);
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
/*      */   public static Object get(Object object, int index) {
/* 1251 */     int i = index;
/* 1252 */     if (i < 0) {
/* 1253 */       throw new IndexOutOfBoundsException("Index cannot be negative: " + i);
/*      */     }
/* 1255 */     if (object instanceof Map) {
/* 1256 */       Map<?, ?> map = (Map<?, ?>)object;
/* 1257 */       Iterator<?> iterator = map.entrySet().iterator();
/* 1258 */       return IteratorUtils.get(iterator, i);
/* 1259 */     }  if (object instanceof Object[])
/* 1260 */       return ((Object[])object)[i]; 
/* 1261 */     if (object instanceof Iterator) {
/* 1262 */       Iterator<?> it = (Iterator)object;
/* 1263 */       return IteratorUtils.get(it, i);
/* 1264 */     }  if (object instanceof Iterable) {
/* 1265 */       Iterable<?> iterable = (Iterable)object;
/* 1266 */       return IterableUtils.get(iterable, i);
/* 1267 */     }  if (object instanceof Collection) {
/* 1268 */       Iterator<?> iterator = ((Collection)object).iterator();
/* 1269 */       return IteratorUtils.get(iterator, i);
/* 1270 */     }  if (object instanceof Enumeration) {
/* 1271 */       Enumeration<?> it = (Enumeration)object;
/* 1272 */       return EnumerationUtils.get(it, i);
/* 1273 */     }  if (object == null) {
/* 1274 */       throw new IllegalArgumentException("Unsupported object type: null");
/*      */     }
/*      */     try {
/* 1277 */       return Array.get(object, i);
/* 1278 */     } catch (IllegalArgumentException ex) {
/* 1279 */       throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
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
/*      */   public static <K, V> Map.Entry<K, V> get(Map<K, V> map, int index) {
/* 1296 */     checkIndexBounds(index);
/* 1297 */     return get(map.entrySet(), index);
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
/*      */   public static int size(Object object) {
/* 1318 */     if (object == null) {
/* 1319 */       return 0;
/*      */     }
/* 1321 */     int total = 0;
/* 1322 */     if (object instanceof Map) {
/* 1323 */       total = ((Map)object).size();
/* 1324 */     } else if (object instanceof Collection) {
/* 1325 */       total = ((Collection)object).size();
/* 1326 */     } else if (object instanceof Iterable) {
/* 1327 */       total = IterableUtils.size((Iterable)object);
/* 1328 */     } else if (object instanceof Object[]) {
/* 1329 */       total = ((Object[])object).length;
/* 1330 */     } else if (object instanceof Iterator) {
/* 1331 */       total = IteratorUtils.size((Iterator)object);
/* 1332 */     } else if (object instanceof Enumeration) {
/* 1333 */       Enumeration<?> it = (Enumeration)object;
/* 1334 */       while (it.hasMoreElements()) {
/* 1335 */         total++;
/* 1336 */         it.nextElement();
/*      */       } 
/*      */     } else {
/*      */       try {
/* 1340 */         total = Array.getLength(object);
/* 1341 */       } catch (IllegalArgumentException ex) {
/* 1342 */         throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
/*      */       } 
/*      */     } 
/* 1345 */     return total;
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
/*      */   public static boolean sizeIsEmpty(Object object) {
/* 1369 */     if (object == null)
/* 1370 */       return true; 
/* 1371 */     if (object instanceof Collection)
/* 1372 */       return ((Collection)object).isEmpty(); 
/* 1373 */     if (object instanceof Iterable)
/* 1374 */       return IterableUtils.isEmpty((Iterable)object); 
/* 1375 */     if (object instanceof Map)
/* 1376 */       return ((Map)object).isEmpty(); 
/* 1377 */     if (object instanceof Object[])
/* 1378 */       return (((Object[])object).length == 0); 
/* 1379 */     if (object instanceof Iterator)
/* 1380 */       return !((Iterator)object).hasNext(); 
/* 1381 */     if (object instanceof Enumeration) {
/* 1382 */       return !((Enumeration)object).hasMoreElements();
/*      */     }
/*      */     try {
/* 1385 */       return (Array.getLength(object) == 0);
/* 1386 */     } catch (IllegalArgumentException ex) {
/* 1387 */       throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
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
/*      */   public static boolean isEmpty(Collection<?> coll) {
/* 1403 */     return (coll == null || coll.isEmpty());
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
/*      */   public static boolean isNotEmpty(Collection<?> coll) {
/* 1416 */     return !isEmpty(coll);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverseArray(Object[] array) {
/* 1426 */     int i = 0;
/* 1427 */     int j = array.length - 1;
/*      */ 
/*      */     
/* 1430 */     while (j > i) {
/* 1431 */       Object tmp = array[j];
/* 1432 */       array[j] = array[i];
/* 1433 */       array[i] = tmp;
/* 1434 */       j--;
/* 1435 */       i++;
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
/*      */   public static boolean isFull(Collection<? extends Object> coll) {
/* 1455 */     if (coll == null) {
/* 1456 */       throw new NullPointerException("The collection must not be null");
/*      */     }
/* 1458 */     if (coll instanceof BoundedCollection) {
/* 1459 */       return ((BoundedCollection)coll).isFull();
/*      */     }
/*      */     try {
/* 1462 */       BoundedCollection<?> bcoll = UnmodifiableBoundedCollection.unmodifiableBoundedCollection(coll);
/*      */       
/* 1464 */       return bcoll.isFull();
/* 1465 */     } catch (IllegalArgumentException ex) {
/* 1466 */       return false;
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
/*      */   public static int maxSize(Collection<? extends Object> coll) {
/* 1486 */     if (coll == null) {
/* 1487 */       throw new NullPointerException("The collection must not be null");
/*      */     }
/* 1489 */     if (coll instanceof BoundedCollection) {
/* 1490 */       return ((BoundedCollection)coll).maxSize();
/*      */     }
/*      */     try {
/* 1493 */       BoundedCollection<?> bcoll = UnmodifiableBoundedCollection.unmodifiableBoundedCollection(coll);
/*      */       
/* 1495 */       return bcoll.maxSize();
/* 1496 */     } catch (IllegalArgumentException ex) {
/* 1497 */       return -1;
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
/*      */   public static <O extends Comparable<? super O>> List<O> collate(Iterable<? extends O> a, Iterable<? extends O> b) {
/* 1517 */     return collate(a, b, ComparatorUtils.naturalComparator(), true);
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
/*      */   public static <O extends Comparable<? super O>> List<O> collate(Iterable<? extends O> a, Iterable<? extends O> b, boolean includeDuplicates) {
/* 1538 */     return collate(a, b, ComparatorUtils.naturalComparator(), includeDuplicates);
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
/*      */   public static <O> List<O> collate(Iterable<? extends O> a, Iterable<? extends O> b, Comparator<? super O> c) {
/* 1557 */     return collate(a, b, c, true);
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
/*      */   public static <O> List<O> collate(Iterable<? extends O> a, Iterable<? extends O> b, Comparator<? super O> c, boolean includeDuplicates) {
/* 1579 */     if (a == null || b == null) {
/* 1580 */       throw new NullPointerException("The collections must not be null");
/*      */     }
/* 1582 */     if (c == null) {
/* 1583 */       throw new NullPointerException("The comparator must not be null");
/*      */     }
/*      */ 
/*      */     
/* 1587 */     int totalSize = (a instanceof Collection && b instanceof Collection) ? Math.max(1, ((Collection)a).size() + ((Collection)b).size()) : 10;
/*      */ 
/*      */     
/* 1590 */     CollatingIterator<O> collatingIterator = new CollatingIterator(c, a.iterator(), b.iterator());
/* 1591 */     if (includeDuplicates) {
/* 1592 */       return IteratorUtils.toList((Iterator<? extends O>)collatingIterator, totalSize);
/*      */     }
/* 1594 */     ArrayList<O> mergedList = new ArrayList<O>(totalSize);
/*      */     
/* 1596 */     O lastItem = null;
/* 1597 */     while (collatingIterator.hasNext()) {
/* 1598 */       O item = collatingIterator.next();
/* 1599 */       if (lastItem == null || !lastItem.equals(item)) {
/* 1600 */         mergedList.add(item);
/*      */       }
/* 1602 */       lastItem = item;
/*      */     } 
/*      */     
/* 1605 */     mergedList.trimToSize();
/* 1606 */     return mergedList;
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
/*      */   public static <E> Collection<List<E>> permutations(Collection<E> collection) {
/* 1631 */     PermutationIterator<E> it = new PermutationIterator(collection);
/* 1632 */     Collection<List<E>> result = new LinkedList<List<E>>();
/* 1633 */     while (it.hasNext()) {
/* 1634 */       result.add(it.next());
/*      */     }
/* 1636 */     return result;
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
/*      */   public static <C> Collection<C> retainAll(Collection<C> collection, Collection<?> retain) {
/* 1663 */     return ListUtils.retainAll(collection, retain);
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
/*      */   public static <E> Collection<E> retainAll(Iterable<E> collection, Iterable<? extends E> retain, final Equator<? super E> equator) {
/* 1695 */     Transformer<E, EquatorWrapper<E>> transformer = new Transformer<E, EquatorWrapper<E>>() {
/*      */         public CollectionUtils.EquatorWrapper<E> transform(E input) {
/* 1697 */           return new CollectionUtils.EquatorWrapper<E>(equator, input);
/*      */         }
/*      */       };
/*      */     
/* 1701 */     Set<EquatorWrapper<E>> retainSet = collect(retain, transformer, new HashSet<EquatorWrapper<E>>());
/*      */ 
/*      */     
/* 1704 */     List<E> list = new ArrayList<E>();
/* 1705 */     for (E element : collection) {
/* 1706 */       if (retainSet.contains(new EquatorWrapper<E>(equator, element))) {
/* 1707 */         list.add(element);
/*      */       }
/*      */     } 
/* 1710 */     return list;
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
/*      */   public static <E> Collection<E> removeAll(Collection<E> collection, Collection<?> remove) {
/* 1737 */     return ListUtils.removeAll(collection, remove);
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
/*      */   public static <E> Collection<E> removeAll(Iterable<E> collection, Iterable<? extends E> remove, final Equator<? super E> equator) {
/* 1770 */     Transformer<E, EquatorWrapper<E>> transformer = new Transformer<E, EquatorWrapper<E>>() {
/*      */         public CollectionUtils.EquatorWrapper<E> transform(E input) {
/* 1772 */           return new CollectionUtils.EquatorWrapper<E>(equator, input);
/*      */         }
/*      */       };
/*      */     
/* 1776 */     Set<EquatorWrapper<E>> removeSet = collect(remove, transformer, new HashSet<EquatorWrapper<E>>());
/*      */ 
/*      */     
/* 1779 */     List<E> list = new ArrayList<E>();
/* 1780 */     for (E element : collection) {
/* 1781 */       if (!removeSet.contains(new EquatorWrapper<E>(equator, element))) {
/* 1782 */         list.add(element);
/*      */       }
/*      */     } 
/* 1785 */     return list;
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
/*      */   @Deprecated
/*      */   public static <C> Collection<C> synchronizedCollection(Collection<C> collection) {
/* 1815 */     return (Collection<C>)SynchronizedCollection.synchronizedCollection(collection);
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
/*      */   @Deprecated
/*      */   public static <C> Collection<C> unmodifiableCollection(Collection<? extends C> collection) {
/* 1831 */     return UnmodifiableCollection.unmodifiableCollection(collection);
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
/*      */   public static <C> Collection<C> predicatedCollection(Collection<C> collection, Predicate<? super C> predicate) {
/* 1850 */     return (Collection<C>)PredicatedCollection.predicatedCollection(collection, predicate);
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
/*      */   public static <E> Collection<E> transformingCollection(Collection<E> collection, Transformer<? super E, ? extends E> transformer) {
/* 1871 */     return (Collection<E>)TransformedCollection.transformingCollection(collection, transformer);
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
/*      */   public static <E> E extractSingleton(Collection<E> collection) {
/* 1884 */     if (collection == null) {
/* 1885 */       throw new NullPointerException("Collection must not be null.");
/*      */     }
/* 1887 */     if (collection.size() != 1) {
/* 1888 */       throw new IllegalArgumentException("Can extract singleton only when collection size == 1");
/*      */     }
/* 1890 */     return collection.iterator().next();
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\CollectionUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */