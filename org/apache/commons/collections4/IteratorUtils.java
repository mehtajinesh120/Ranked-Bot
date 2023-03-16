/*      */ package org.apache.commons.collections4;
/*      */ 
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Dictionary;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import org.apache.commons.collections4.functors.EqualPredicate;
/*      */ import org.apache.commons.collections4.iterators.ArrayIterator;
/*      */ import org.apache.commons.collections4.iterators.ArrayListIterator;
/*      */ import org.apache.commons.collections4.iterators.BoundedIterator;
/*      */ import org.apache.commons.collections4.iterators.CollatingIterator;
/*      */ import org.apache.commons.collections4.iterators.EmptyIterator;
/*      */ import org.apache.commons.collections4.iterators.EmptyListIterator;
/*      */ import org.apache.commons.collections4.iterators.EmptyMapIterator;
/*      */ import org.apache.commons.collections4.iterators.EmptyOrderedIterator;
/*      */ import org.apache.commons.collections4.iterators.EmptyOrderedMapIterator;
/*      */ import org.apache.commons.collections4.iterators.EnumerationIterator;
/*      */ import org.apache.commons.collections4.iterators.FilterIterator;
/*      */ import org.apache.commons.collections4.iterators.FilterListIterator;
/*      */ import org.apache.commons.collections4.iterators.IteratorChain;
/*      */ import org.apache.commons.collections4.iterators.IteratorEnumeration;
/*      */ import org.apache.commons.collections4.iterators.IteratorIterable;
/*      */ import org.apache.commons.collections4.iterators.ListIteratorWrapper;
/*      */ import org.apache.commons.collections4.iterators.LoopingIterator;
/*      */ import org.apache.commons.collections4.iterators.LoopingListIterator;
/*      */ import org.apache.commons.collections4.iterators.NodeListIterator;
/*      */ import org.apache.commons.collections4.iterators.ObjectArrayIterator;
/*      */ import org.apache.commons.collections4.iterators.ObjectArrayListIterator;
/*      */ import org.apache.commons.collections4.iterators.ObjectGraphIterator;
/*      */ import org.apache.commons.collections4.iterators.PeekingIterator;
/*      */ import org.apache.commons.collections4.iterators.PushbackIterator;
/*      */ import org.apache.commons.collections4.iterators.SingletonIterator;
/*      */ import org.apache.commons.collections4.iterators.SingletonListIterator;
/*      */ import org.apache.commons.collections4.iterators.SkippingIterator;
/*      */ import org.apache.commons.collections4.iterators.TransformIterator;
/*      */ import org.apache.commons.collections4.iterators.UnmodifiableIterator;
/*      */ import org.apache.commons.collections4.iterators.UnmodifiableListIterator;
/*      */ import org.apache.commons.collections4.iterators.UnmodifiableMapIterator;
/*      */ import org.apache.commons.collections4.iterators.ZippingIterator;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class IteratorUtils
/*      */ {
/*   83 */   public static final ResettableIterator EMPTY_ITERATOR = EmptyIterator.RESETTABLE_INSTANCE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   89 */   public static final ResettableListIterator EMPTY_LIST_ITERATOR = EmptyListIterator.RESETTABLE_INSTANCE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   95 */   public static final OrderedIterator EMPTY_ORDERED_ITERATOR = EmptyOrderedIterator.INSTANCE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  101 */   public static final MapIterator EMPTY_MAP_ITERATOR = EmptyMapIterator.INSTANCE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  107 */   public static final OrderedMapIterator EMPTY_ORDERED_MAP_ITERATOR = EmptyOrderedMapIterator.INSTANCE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String DEFAULT_TOSTRING_PREFIX = "[";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String DEFAULT_TOSTRING_SUFFIX = "]";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String DEFAULT_TOSTRING_DELIMITER = ", ";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> ResettableIterator<E> emptyIterator() {
/*  141 */     return EmptyIterator.resettableEmptyIterator();
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
/*      */   public static <E> ResettableListIterator<E> emptyListIterator() {
/*  154 */     return EmptyListIterator.resettableEmptyListIterator();
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
/*      */   public static <E> OrderedIterator<E> emptyOrderedIterator() {
/*  167 */     return EmptyOrderedIterator.emptyOrderedIterator();
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
/*      */   public static <K, V> MapIterator<K, V> emptyMapIterator() {
/*  181 */     return EmptyMapIterator.emptyMapIterator();
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
/*      */   public static <K, V> OrderedMapIterator<K, V> emptyOrderedMapIterator() {
/*  195 */     return EmptyOrderedMapIterator.emptyOrderedMapIterator();
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
/*      */   public static <E> ResettableIterator<E> singletonIterator(E object) {
/*  211 */     return (ResettableIterator<E>)new SingletonIterator(object);
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
/*      */   public static <E> ListIterator<E> singletonListIterator(E object) {
/*  225 */     return (ListIterator<E>)new SingletonListIterator(object);
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
/*      */   public static <E> ResettableIterator<E> arrayIterator(E... array) {
/*  239 */     return (ResettableIterator<E>)new ObjectArrayIterator((Object[])array);
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
/*      */   public static <E> ResettableIterator<E> arrayIterator(Object array) {
/*  255 */     return (ResettableIterator<E>)new ArrayIterator(array);
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
/*      */   public static <E> ResettableIterator<E> arrayIterator(E[] array, int start) {
/*  270 */     return (ResettableIterator<E>)new ObjectArrayIterator((Object[])array, start);
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
/*      */   public static <E> ResettableIterator<E> arrayIterator(Object array, int start) {
/*  289 */     return (ResettableIterator<E>)new ArrayIterator(array, start);
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
/*      */   public static <E> ResettableIterator<E> arrayIterator(E[] array, int start, int end) {
/*  305 */     return (ResettableIterator<E>)new ObjectArrayIterator((Object[])array, start, end);
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
/*      */   public static <E> ResettableIterator<E> arrayIterator(Object array, int start, int end) {
/*  324 */     return (ResettableIterator<E>)new ArrayIterator(array, start, end);
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
/*      */   public static <E> ResettableListIterator<E> arrayListIterator(E... array) {
/*  337 */     return (ResettableListIterator<E>)new ObjectArrayListIterator((Object[])array);
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
/*      */   public static <E> ResettableListIterator<E> arrayListIterator(Object array) {
/*  353 */     return (ResettableListIterator<E>)new ArrayListIterator(array);
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
/*      */   public static <E> ResettableListIterator<E> arrayListIterator(E[] array, int start) {
/*  367 */     return (ResettableListIterator<E>)new ObjectArrayListIterator((Object[])array, start);
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
/*      */   public static <E> ResettableListIterator<E> arrayListIterator(Object array, int start) {
/*  385 */     return (ResettableListIterator<E>)new ArrayListIterator(array, start);
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
/*      */   public static <E> ResettableListIterator<E> arrayListIterator(E[] array, int start, int end) {
/*  401 */     return (ResettableListIterator<E>)new ObjectArrayListIterator((Object[])array, start, end);
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
/*      */   public static <E> ResettableListIterator<E> arrayListIterator(Object array, int start, int end) {
/*  420 */     return (ResettableListIterator<E>)new ArrayListIterator(array, start, end);
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
/*      */   public static <E> BoundedIterator<E> boundedIterator(Iterator<? extends E> iterator, long max) {
/*  438 */     return boundedIterator(iterator, 0L, max);
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
/*      */   public static <E> BoundedIterator<E> boundedIterator(Iterator<? extends E> iterator, long offset, long max) {
/*  460 */     return new BoundedIterator(iterator, offset, max);
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
/*      */   public static <E> Iterator<E> unmodifiableIterator(Iterator<E> iterator) {
/*  475 */     return UnmodifiableIterator.unmodifiableIterator(iterator);
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
/*      */   public static <E> ListIterator<E> unmodifiableListIterator(ListIterator<E> listIterator) {
/*  489 */     return UnmodifiableListIterator.umodifiableListIterator(listIterator);
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
/*      */   public static <K, V> MapIterator<K, V> unmodifiableMapIterator(MapIterator<K, V> mapIterator) {
/*  503 */     return UnmodifiableMapIterator.unmodifiableMapIterator(mapIterator);
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
/*      */   public static <E> Iterator<E> chainedIterator(Iterator<? extends E> iterator1, Iterator<? extends E> iterator2) {
/*  523 */     return (Iterator<E>)new IteratorChain(iterator1, iterator2);
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
/*      */   public static <E> Iterator<E> chainedIterator(Iterator<? extends E>... iterators) {
/*  536 */     return (Iterator<E>)new IteratorChain((Iterator[])iterators);
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
/*      */   public static <E> Iterator<E> chainedIterator(Collection<Iterator<? extends E>> iterators) {
/*  550 */     return (Iterator<E>)new IteratorChain(iterators);
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
/*      */   public static <E> Iterator<E> collatedIterator(Comparator<? super E> comparator, Iterator<? extends E> iterator1, Iterator<? extends E> iterator2) {
/*  576 */     Comparator<E> comp = (comparator == null) ? ComparatorUtils.NATURAL_COMPARATOR : (Comparator)comparator;
/*      */     
/*  578 */     return (Iterator<E>)new CollatingIterator(comp, iterator1, iterator2);
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
/*      */   public static <E> Iterator<E> collatedIterator(Comparator<? super E> comparator, Iterator<? extends E>... iterators) {
/*  600 */     Comparator<E> comp = (comparator == null) ? ComparatorUtils.NATURAL_COMPARATOR : (Comparator)comparator;
/*      */     
/*  602 */     return (Iterator<E>)new CollatingIterator(comp, (Iterator[])iterators);
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
/*      */   public static <E> Iterator<E> collatedIterator(Comparator<? super E> comparator, Collection<Iterator<? extends E>> iterators) {
/*  625 */     Comparator<E> comp = (comparator == null) ? ComparatorUtils.NATURAL_COMPARATOR : (Comparator)comparator;
/*      */     
/*  627 */     return (Iterator<E>)new CollatingIterator(comp, iterators);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> Iterator<E> objectGraphIterator(E root, Transformer<? super E, ? extends E> transformer) {
/*  688 */     return (Iterator<E>)new ObjectGraphIterator(root, transformer);
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
/*      */   public static <I, O> Iterator<O> transformedIterator(Iterator<? extends I> iterator, Transformer<? super I, ? extends O> transform) {
/*  709 */     if (iterator == null) {
/*  710 */       throw new NullPointerException("Iterator must not be null");
/*      */     }
/*  712 */     if (transform == null) {
/*  713 */       throw new NullPointerException("Transformer must not be null");
/*      */     }
/*  715 */     return (Iterator<O>)new TransformIterator(iterator, transform);
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
/*      */   public static <E> Iterator<E> filteredIterator(Iterator<? extends E> iterator, Predicate<? super E> predicate) {
/*  734 */     if (iterator == null) {
/*  735 */       throw new NullPointerException("Iterator must not be null");
/*      */     }
/*  737 */     if (predicate == null) {
/*  738 */       throw new NullPointerException("Predicate must not be null");
/*      */     }
/*  740 */     return (Iterator<E>)new FilterIterator(iterator, predicate);
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
/*      */   public static <E> ListIterator<E> filteredListIterator(ListIterator<? extends E> listIterator, Predicate<? super E> predicate) {
/*  758 */     if (listIterator == null) {
/*  759 */       throw new NullPointerException("ListIterator must not be null");
/*      */     }
/*  761 */     if (predicate == null) {
/*  762 */       throw new NullPointerException("Predicate must not be null");
/*      */     }
/*  764 */     return (ListIterator<E>)new FilterListIterator(listIterator, predicate);
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
/*      */   public static <E> ResettableIterator<E> loopingIterator(Collection<? extends E> coll) {
/*  782 */     if (coll == null) {
/*  783 */       throw new NullPointerException("Collection must not be null");
/*      */     }
/*  785 */     return (ResettableIterator<E>)new LoopingIterator(coll);
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
/*      */   public static <E> ResettableListIterator<E> loopingListIterator(List<E> list) {
/*  801 */     if (list == null) {
/*  802 */       throw new NullPointerException("List must not be null");
/*      */     }
/*  804 */     return (ResettableListIterator<E>)new LoopingListIterator(list);
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
/*      */   public static NodeListIterator nodeListIterator(NodeList nodeList) {
/*  819 */     if (nodeList == null) {
/*  820 */       throw new NullPointerException("NodeList must not be null");
/*      */     }
/*  822 */     return new NodeListIterator(nodeList);
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
/*      */   public static NodeListIterator nodeListIterator(Node node) {
/*  843 */     if (node == null) {
/*  844 */       throw new NullPointerException("Node must not be null");
/*      */     }
/*  846 */     return new NodeListIterator(node);
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
/*      */   public static <E> Iterator<E> peekingIterator(Iterator<? extends E> iterator) {
/*  862 */     return (Iterator<E>)PeekingIterator.peekingIterator(iterator);
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
/*      */   public static <E> Iterator<E> pushbackIterator(Iterator<? extends E> iterator) {
/*  878 */     return (Iterator<E>)PushbackIterator.pushbackIterator(iterator);
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
/*      */   public static <E> SkippingIterator<E> skippingIterator(Iterator<E> iterator, long offset) {
/*  895 */     return new SkippingIterator(iterator, offset);
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
/*      */   public static <E> ZippingIterator<E> zippingIterator(Iterator<? extends E> a, Iterator<? extends E> b) {
/*  912 */     return new ZippingIterator(a, b);
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
/*      */   public static <E> ZippingIterator<E> zippingIterator(Iterator<? extends E> a, Iterator<? extends E> b, Iterator<? extends E> c) {
/*  929 */     return new ZippingIterator(a, b, c);
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
/*      */   public static <E> ZippingIterator<E> zippingIterator(Iterator<? extends E>... iterators) {
/*  942 */     return new ZippingIterator((Iterator[])iterators);
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
/*      */   public static <E> Iterator<E> asIterator(Enumeration<? extends E> enumeration) {
/*  956 */     if (enumeration == null) {
/*  957 */       throw new NullPointerException("Enumeration must not be null");
/*      */     }
/*  959 */     return (Iterator<E>)new EnumerationIterator(enumeration);
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
/*      */   public static <E> Iterator<E> asIterator(Enumeration<? extends E> enumeration, Collection<? super E> removeCollection) {
/*  974 */     if (enumeration == null) {
/*  975 */       throw new NullPointerException("Enumeration must not be null");
/*      */     }
/*  977 */     if (removeCollection == null) {
/*  978 */       throw new NullPointerException("Collection must not be null");
/*      */     }
/*  980 */     return (Iterator<E>)new EnumerationIterator(enumeration, removeCollection);
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
/*      */   public static <E> Enumeration<E> asEnumeration(Iterator<? extends E> iterator) {
/*  992 */     if (iterator == null) {
/*  993 */       throw new NullPointerException("Iterator must not be null");
/*      */     }
/*  995 */     return (Enumeration<E>)new IteratorEnumeration(iterator);
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
/*      */   public static <E> Iterable<E> asIterable(Iterator<? extends E> iterator) {
/* 1008 */     if (iterator == null) {
/* 1009 */       throw new NullPointerException("Iterator must not be null");
/*      */     }
/* 1011 */     return (Iterable<E>)new IteratorIterable(iterator, false);
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
/*      */   public static <E> Iterable<E> asMultipleUseIterable(Iterator<? extends E> iterator) {
/* 1024 */     if (iterator == null) {
/* 1025 */       throw new NullPointerException("Iterator must not be null");
/*      */     }
/* 1027 */     return (Iterable<E>)new IteratorIterable(iterator, true);
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
/*      */   public static <E> ListIterator<E> toListIterator(Iterator<? extends E> iterator) {
/* 1042 */     if (iterator == null) {
/* 1043 */       throw new NullPointerException("Iterator must not be null");
/*      */     }
/* 1045 */     return (ListIterator<E>)new ListIteratorWrapper(iterator);
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
/*      */   public static Object[] toArray(Iterator<?> iterator) {
/* 1059 */     if (iterator == null) {
/* 1060 */       throw new NullPointerException("Iterator must not be null");
/*      */     }
/* 1062 */     List<?> list = toList(iterator, 100);
/* 1063 */     return list.toArray();
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
/*      */   public static <E> E[] toArray(Iterator<? extends E> iterator, Class<E> arrayClass) {
/* 1080 */     if (iterator == null) {
/* 1081 */       throw new NullPointerException("Iterator must not be null");
/*      */     }
/* 1083 */     if (arrayClass == null) {
/* 1084 */       throw new NullPointerException("Array class must not be null");
/*      */     }
/* 1086 */     List<E> list = toList(iterator, 100);
/*      */     
/* 1088 */     E[] array = (E[])Array.newInstance(arrayClass, list.size());
/* 1089 */     return list.toArray(array);
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
/*      */   public static <E> List<E> toList(Iterator<? extends E> iterator) {
/* 1104 */     return toList(iterator, 10);
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
/*      */   public static <E> List<E> toList(Iterator<? extends E> iterator, int estimatedSize) {
/* 1121 */     if (iterator == null) {
/* 1122 */       throw new NullPointerException("Iterator must not be null");
/*      */     }
/* 1124 */     if (estimatedSize < 1) {
/* 1125 */       throw new IllegalArgumentException("Estimated size must be greater than 0");
/*      */     }
/* 1127 */     List<E> list = new ArrayList<E>(estimatedSize);
/* 1128 */     while (iterator.hasNext()) {
/* 1129 */       list.add(iterator.next());
/*      */     }
/* 1131 */     return list;
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
/*      */   public static Iterator<?> getIterator(Object obj) {
/* 1156 */     if (obj == null) {
/* 1157 */       return emptyIterator();
/*      */     }
/* 1159 */     if (obj instanceof Iterator) {
/* 1160 */       return (Iterator)obj;
/*      */     }
/* 1162 */     if (obj instanceof Iterable) {
/* 1163 */       return ((Iterable)obj).iterator();
/*      */     }
/* 1165 */     if (obj instanceof Object[]) {
/* 1166 */       return (Iterator<?>)new ObjectArrayIterator((Object[])obj);
/*      */     }
/* 1168 */     if (obj instanceof Enumeration) {
/* 1169 */       return (Iterator<?>)new EnumerationIterator((Enumeration)obj);
/*      */     }
/* 1171 */     if (obj instanceof Map) {
/* 1172 */       return ((Map)obj).values().iterator();
/*      */     }
/* 1174 */     if (obj instanceof NodeList) {
/* 1175 */       return (Iterator<?>)new NodeListIterator((NodeList)obj);
/*      */     }
/* 1177 */     if (obj instanceof Node) {
/* 1178 */       return (Iterator<?>)new NodeListIterator((Node)obj);
/*      */     }
/* 1180 */     if (obj instanceof Dictionary)
/* 1181 */       return (Iterator<?>)new EnumerationIterator(((Dictionary)obj).elements()); 
/* 1182 */     if (obj.getClass().isArray()) {
/* 1183 */       return (Iterator<?>)new ArrayIterator(obj);
/*      */     }
/*      */     try {
/* 1186 */       Method method = obj.getClass().getMethod("iterator", (Class[])null);
/* 1187 */       if (Iterator.class.isAssignableFrom(method.getReturnType())) {
/* 1188 */         Iterator<?> it = (Iterator)method.invoke(obj, (Object[])null);
/* 1189 */         if (it != null) {
/* 1190 */           return it;
/*      */         }
/*      */       } 
/* 1193 */     } catch (RuntimeException e) {
/*      */     
/* 1195 */     } catch (NoSuchMethodException e) {
/*      */     
/* 1197 */     } catch (IllegalAccessException e) {
/*      */     
/* 1199 */     } catch (InvocationTargetException e) {}
/*      */ 
/*      */     
/* 1202 */     return singletonIterator(obj);
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
/*      */   public static <E> void forEach(Iterator<E> iterator, Closure<? super E> closure) {
/* 1218 */     if (closure == null) {
/* 1219 */       throw new NullPointerException("Closure must not be null");
/*      */     }
/*      */     
/* 1222 */     if (iterator != null) {
/* 1223 */       while (iterator.hasNext()) {
/* 1224 */         E element = iterator.next();
/* 1225 */         closure.execute(element);
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
/*      */   public static <E> E forEachButLast(Iterator<E> iterator, Closure<? super E> closure) {
/* 1243 */     if (closure == null) {
/* 1244 */       throw new NullPointerException("Closure must not be null.");
/*      */     }
/* 1246 */     if (iterator != null) {
/* 1247 */       while (iterator.hasNext()) {
/* 1248 */         E element = iterator.next();
/* 1249 */         if (iterator.hasNext()) {
/* 1250 */           closure.execute(element); continue;
/*      */         } 
/* 1252 */         return element;
/*      */       } 
/*      */     }
/*      */     
/* 1256 */     return null;
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
/*      */   public static <E> E find(Iterator<E> iterator, Predicate<? super E> predicate) {
/* 1272 */     if (predicate == null) {
/* 1273 */       throw new NullPointerException("Predicate must not be null");
/*      */     }
/*      */     
/* 1276 */     if (iterator != null) {
/* 1277 */       while (iterator.hasNext()) {
/* 1278 */         E element = iterator.next();
/* 1279 */         if (predicate.evaluate(element)) {
/* 1280 */           return element;
/*      */         }
/*      */       } 
/*      */     }
/* 1284 */     return null;
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
/*      */   public static <E> int indexOf(Iterator<E> iterator, Predicate<? super E> predicate) {
/* 1301 */     if (predicate == null) {
/* 1302 */       throw new NullPointerException("Predicate must not be null");
/*      */     }
/*      */     
/* 1305 */     if (iterator != null) {
/* 1306 */       for (int index = 0; iterator.hasNext(); index++) {
/* 1307 */         E element = iterator.next();
/* 1308 */         if (predicate.evaluate(element)) {
/* 1309 */           return index;
/*      */         }
/*      */       } 
/*      */     }
/* 1313 */     return -1;
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
/*      */   public static <E> boolean matchesAny(Iterator<E> iterator, Predicate<? super E> predicate) {
/* 1329 */     return (indexOf(iterator, predicate) != -1);
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
/*      */   public static <E> boolean matchesAll(Iterator<E> iterator, Predicate<? super E> predicate) {
/* 1346 */     if (predicate == null) {
/* 1347 */       throw new NullPointerException("Predicate must not be null");
/*      */     }
/*      */     
/* 1350 */     if (iterator != null) {
/* 1351 */       while (iterator.hasNext()) {
/* 1352 */         E element = iterator.next();
/* 1353 */         if (!predicate.evaluate(element)) {
/* 1354 */           return false;
/*      */         }
/*      */       } 
/*      */     }
/* 1358 */     return true;
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
/*      */   public static boolean isEmpty(Iterator<?> iterator) {
/* 1371 */     return (iterator == null || !iterator.hasNext());
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
/*      */   public static <E> boolean contains(Iterator<E> iterator, Object object) {
/* 1386 */     return matchesAny(iterator, EqualPredicate.equalPredicate(object));
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
/*      */   public static <E> E get(Iterator<E> iterator, int index) {
/* 1404 */     int i = index;
/* 1405 */     CollectionUtils.checkIndexBounds(i);
/* 1406 */     while (iterator.hasNext()) {
/* 1407 */       i--;
/* 1408 */       if (i == -1) {
/* 1409 */         return iterator.next();
/*      */       }
/* 1411 */       iterator.next();
/*      */     } 
/* 1413 */     throw new IndexOutOfBoundsException("Entry does not exist: " + i);
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
/*      */   public static int size(Iterator<?> iterator) {
/* 1426 */     int size = 0;
/* 1427 */     if (iterator != null) {
/* 1428 */       while (iterator.hasNext()) {
/* 1429 */         iterator.next();
/* 1430 */         size++;
/*      */       } 
/*      */     }
/* 1433 */     return size;
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
/*      */   public static <E> String toString(Iterator<E> iterator) {
/* 1450 */     return toString(iterator, TransformerUtils.stringValueTransformer(), ", ", "[", "]");
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
/*      */   public static <E> String toString(Iterator<E> iterator, Transformer<? super E, String> transformer) {
/* 1472 */     return toString(iterator, transformer, ", ", "[", "]");
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
/*      */   public static <E> String toString(Iterator<E> iterator, Transformer<? super E, String> transformer, String delimiter, String prefix, String suffix) {
/* 1499 */     if (transformer == null) {
/* 1500 */       throw new NullPointerException("transformer may not be null");
/*      */     }
/* 1502 */     if (delimiter == null) {
/* 1503 */       throw new NullPointerException("delimiter may not be null");
/*      */     }
/* 1505 */     if (prefix == null) {
/* 1506 */       throw new NullPointerException("prefix may not be null");
/*      */     }
/* 1508 */     if (suffix == null) {
/* 1509 */       throw new NullPointerException("suffix may not be null");
/*      */     }
/* 1511 */     StringBuilder stringBuilder = new StringBuilder(prefix);
/* 1512 */     if (iterator != null) {
/* 1513 */       while (iterator.hasNext()) {
/* 1514 */         E element = iterator.next();
/* 1515 */         stringBuilder.append(transformer.transform(element));
/* 1516 */         stringBuilder.append(delimiter);
/*      */       } 
/* 1518 */       if (stringBuilder.length() > prefix.length()) {
/* 1519 */         stringBuilder.setLength(stringBuilder.length() - delimiter.length());
/*      */       }
/*      */     } 
/* 1522 */     stringBuilder.append(suffix);
/* 1523 */     return stringBuilder.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\IteratorUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */