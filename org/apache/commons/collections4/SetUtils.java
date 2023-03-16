/*     */ package org.apache.commons.collections4;
/*     */ 
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import org.apache.commons.collections4.set.ListOrderedSet;
/*     */ import org.apache.commons.collections4.set.PredicatedNavigableSet;
/*     */ import org.apache.commons.collections4.set.PredicatedSet;
/*     */ import org.apache.commons.collections4.set.PredicatedSortedSet;
/*     */ import org.apache.commons.collections4.set.TransformedNavigableSet;
/*     */ import org.apache.commons.collections4.set.TransformedSet;
/*     */ import org.apache.commons.collections4.set.TransformedSortedSet;
/*     */ import org.apache.commons.collections4.set.UnmodifiableNavigableSet;
/*     */ import org.apache.commons.collections4.set.UnmodifiableSet;
/*     */ import org.apache.commons.collections4.set.UnmodifiableSortedSet;
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
/*     */ public class SetUtils
/*     */ {
/*     */   public static <E> Set<E> emptySet() {
/*  56 */     return Collections.emptySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   public static final SortedSet EMPTY_SORTED_SET = UnmodifiableSortedSet.unmodifiableSortedSet(new TreeSet());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> SortedSet<E> emptySortedSet() {
/*  74 */     return EMPTY_SORTED_SET;
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
/*     */   public static <T> Set<T> emptyIfNull(Set<T> set) {
/*  93 */     return (set == null) ? Collections.<T>emptySet() : set;
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
/*     */   public static boolean isEqualSet(Collection<?> set1, Collection<?> set2) {
/* 125 */     if (set1 == set2) {
/* 126 */       return true;
/*     */     }
/* 128 */     if (set1 == null || set2 == null || set1.size() != set2.size()) {
/* 129 */       return false;
/*     */     }
/*     */     
/* 132 */     return set1.containsAll(set2);
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
/*     */   public static <T> int hashCodeForSet(Collection<T> set) {
/* 149 */     if (set == null) {
/* 150 */       return 0;
/*     */     }
/*     */     
/* 153 */     int hashCode = 0;
/* 154 */     for (T obj : set) {
/* 155 */       if (obj != null) {
/* 156 */         hashCode += obj.hashCode();
/*     */       }
/*     */     } 
/* 159 */     return hashCode;
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
/*     */   public static <E> Set<E> newIdentityHashSet() {
/* 182 */     return Collections.newSetFromMap(new IdentityHashMap<E, Boolean>());
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
/*     */   public static <E> Set<E> synchronizedSet(Set<E> set) {
/* 211 */     return Collections.synchronizedSet(set);
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
/*     */   public static <E> Set<E> unmodifiableSet(Set<? extends E> set) {
/* 225 */     return UnmodifiableSet.unmodifiableSet(set);
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
/*     */   public static <E> Set<E> predicatedSet(Set<E> set, Predicate<? super E> predicate) {
/* 243 */     return (Set<E>)PredicatedSet.predicatedSet(set, predicate);
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
/*     */   public static <E> Set<E> transformedSet(Set<E> set, Transformer<? super E, ? extends E> transformer) {
/* 264 */     return (Set<E>)TransformedSet.transformingSet(set, transformer);
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
/*     */   public static <E> Set<E> orderedSet(Set<E> set) {
/* 280 */     return (Set<E>)ListOrderedSet.listOrderedSet(set);
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
/*     */   public static <E> SortedSet<E> synchronizedSortedSet(SortedSet<E> set) {
/* 309 */     return Collections.synchronizedSortedSet(set);
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
/*     */   public static <E> SortedSet<E> unmodifiableSortedSet(SortedSet<E> set) {
/* 323 */     return UnmodifiableSortedSet.unmodifiableSortedSet(set);
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
/*     */   public static <E> SortedSet<E> predicatedSortedSet(SortedSet<E> set, Predicate<? super E> predicate) {
/* 342 */     return (SortedSet<E>)PredicatedSortedSet.predicatedSortedSet(set, predicate);
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
/*     */   public static <E> SortedSet<E> transformedSortedSet(SortedSet<E> set, Transformer<? super E, ? extends E> transformer) {
/* 363 */     return (SortedSet<E>)TransformedSortedSet.transformingSortedSet(set, transformer);
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
/*     */   public static <E> SortedSet<E> unmodifiableNavigableSet(NavigableSet<E> set) {
/* 380 */     return UnmodifiableNavigableSet.unmodifiableNavigableSet(set);
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
/*     */   public static <E> SortedSet<E> predicatedNavigableSet(NavigableSet<E> set, Predicate<? super E> predicate) {
/* 400 */     return (SortedSet<E>)PredicatedNavigableSet.predicatedNavigableSet(set, predicate);
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
/*     */   public static <E> SortedSet<E> transformedNavigableSet(NavigableSet<E> set, Transformer<? super E, ? extends E> transformer) {
/* 422 */     return (SortedSet<E>)TransformedNavigableSet.transformingNavigableSet(set, transformer);
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
/*     */   public static <E> SetView<E> union(final Set<? extends E> a, final Set<? extends E> b) {
/* 442 */     if (a == null || b == null) {
/* 443 */       throw new NullPointerException("Sets must not be null.");
/*     */     }
/*     */     
/* 446 */     final SetView<E> bMinusA = difference(b, a);
/*     */     
/* 448 */     return new SetView<E>()
/*     */       {
/*     */         public boolean contains(Object o) {
/* 451 */           return (a.contains(o) || b.contains(o));
/*     */         }
/*     */ 
/*     */         
/*     */         public Iterator<E> createIterator() {
/* 456 */           return IteratorUtils.chainedIterator(a.iterator(), bMinusA.iterator());
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isEmpty() {
/* 461 */           return (a.isEmpty() && b.isEmpty());
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 466 */           return a.size() + bMinusA.size();
/*     */         }
/*     */       };
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
/*     */   public static <E> SetView<E> difference(final Set<? extends E> a, final Set<? extends E> b) {
/* 486 */     if (a == null || b == null) {
/* 487 */       throw new NullPointerException("Sets must not be null.");
/*     */     }
/*     */     
/* 490 */     final Predicate<E> notContainedInB = new Predicate<E>()
/*     */       {
/*     */         public boolean evaluate(E object) {
/* 493 */           return !b.contains(object);
/*     */         }
/*     */       };
/*     */     
/* 497 */     return new SetView<E>()
/*     */       {
/*     */         public boolean contains(Object o) {
/* 500 */           return (a.contains(o) && !b.contains(o));
/*     */         }
/*     */ 
/*     */         
/*     */         public Iterator<E> createIterator() {
/* 505 */           return IteratorUtils.filteredIterator(a.iterator(), notContainedInB);
/*     */         }
/*     */       };
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
/*     */   public static <E> SetView<E> intersection(final Set<? extends E> a, final Set<? extends E> b) {
/* 524 */     if (a == null || b == null) {
/* 525 */       throw new NullPointerException("Sets must not be null.");
/*     */     }
/*     */     
/* 528 */     final Predicate<E> containedInB = new Predicate<E>()
/*     */       {
/*     */         public boolean evaluate(E object) {
/* 531 */           return b.contains(object);
/*     */         }
/*     */       };
/*     */     
/* 535 */     return new SetView<E>()
/*     */       {
/*     */         public boolean contains(Object o) {
/* 538 */           return (a.contains(o) && b.contains(o));
/*     */         }
/*     */ 
/*     */         
/*     */         public Iterator<E> createIterator() {
/* 543 */           return IteratorUtils.filteredIterator(a.iterator(), containedInB);
/*     */         }
/*     */       };
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
/*     */   public static <E> SetView<E> disjunction(final Set<? extends E> a, final Set<? extends E> b) {
/* 565 */     if (a == null || b == null) {
/* 566 */       throw new NullPointerException("Sets must not be null.");
/*     */     }
/*     */     
/* 569 */     final SetView<E> aMinusB = difference(a, b);
/* 570 */     final SetView<E> bMinusA = difference(b, a);
/*     */     
/* 572 */     return new SetView<E>()
/*     */       {
/*     */         public boolean contains(Object o) {
/* 575 */           return a.contains(o) ^ b.contains(o);
/*     */         }
/*     */ 
/*     */         
/*     */         public Iterator<E> createIterator() {
/* 580 */           return IteratorUtils.chainedIterator(aMinusB.iterator(), bMinusA.iterator());
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isEmpty() {
/* 585 */           return (aMinusB.isEmpty() && bMinusA.isEmpty());
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 590 */           return aMinusB.size() + bMinusA.size();
/*     */         }
/*     */       };
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
/*     */   public static abstract class SetView<E>
/*     */     extends AbstractSet<E>
/*     */   {
/*     */     public Iterator<E> iterator() {
/* 609 */       return IteratorUtils.unmodifiableIterator(createIterator());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract Iterator<E> createIterator();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 621 */       return IteratorUtils.size(iterator());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <S extends Set<E>> void copyInto(S set) {
/* 631 */       CollectionUtils.addAll((Collection<E>)set, this);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Set<E> toSet() {
/* 640 */       Set<E> set = new HashSet<E>(size());
/* 641 */       copyInto(set);
/* 642 */       return set;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\SetUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */