/*     */ package org.apache.commons.collections4;
/*     */ 
/*     */ import java.util.AbstractList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.commons.collections4.bag.HashBag;
/*     */ import org.apache.commons.collections4.functors.DefaultEquator;
/*     */ import org.apache.commons.collections4.list.FixedSizeList;
/*     */ import org.apache.commons.collections4.list.LazyList;
/*     */ import org.apache.commons.collections4.list.PredicatedList;
/*     */ import org.apache.commons.collections4.list.TransformedList;
/*     */ import org.apache.commons.collections4.list.UnmodifiableList;
/*     */ import org.apache.commons.collections4.sequence.CommandVisitor;
/*     */ import org.apache.commons.collections4.sequence.EditScript;
/*     */ import org.apache.commons.collections4.sequence.SequencesComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ListUtils
/*     */ {
/*     */   public static <T> List<T> emptyIfNull(List<T> list) {
/*  62 */     return (list == null) ? Collections.<T>emptyList() : list;
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
/*     */   public static <T> List<T> defaultIfNull(List<T> list, List<T> defaultList) {
/*  76 */     return (list == null) ? defaultList : list;
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
/*     */   public static <E> List<E> intersection(List<? extends E> list1, List<? extends E> list2) {
/*  90 */     List<E> result = new ArrayList<E>();
/*     */     
/*  92 */     List<? extends E> smaller = list1;
/*  93 */     List<? extends E> larger = list2;
/*  94 */     if (list1.size() > list2.size()) {
/*  95 */       smaller = list2;
/*  96 */       larger = list1;
/*     */     } 
/*     */     
/*  99 */     HashSet<E> hashSet = new HashSet<E>(smaller);
/*     */     
/* 101 */     for (E e : larger) {
/* 102 */       if (hashSet.contains(e)) {
/* 103 */         result.add(e);
/* 104 */         hashSet.remove(e);
/*     */       } 
/*     */     } 
/* 107 */     return result;
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
/*     */   public static <E> List<E> subtract(List<E> list1, List<? extends E> list2) {
/* 127 */     ArrayList<E> result = new ArrayList<E>();
/* 128 */     HashBag<E> bag = new HashBag(list2);
/* 129 */     for (E e : list1) {
/* 130 */       if (!bag.remove(e, 1)) {
/* 131 */         result.add(e);
/*     */       }
/*     */     } 
/* 134 */     return result;
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
/*     */   public static <E> List<E> sum(List<? extends E> list1, List<? extends E> list2) {
/* 148 */     return subtract(union(list1, list2), intersection(list1, list2));
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
/*     */   public static <E> List<E> union(List<? extends E> list1, List<? extends E> list2) {
/* 163 */     ArrayList<E> result = new ArrayList<E>(list1);
/* 164 */     result.addAll(list2);
/* 165 */     return result;
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
/*     */   public static <E> List<E> select(Collection<? extends E> inputCollection, Predicate<? super E> predicate) {
/* 185 */     return CollectionUtils.<E, List<E>>select(inputCollection, predicate, new ArrayList<E>(inputCollection.size()));
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
/*     */   public static <E> List<E> selectRejected(Collection<? extends E> inputCollection, Predicate<? super E> predicate) {
/* 205 */     return CollectionUtils.<E, List<E>>selectRejected(inputCollection, predicate, new ArrayList<E>(inputCollection.size()));
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
/*     */   
/*     */   public static boolean isEqualList(Collection<?> list1, Collection<?> list2) {
/* 238 */     if (list1 == list2) {
/* 239 */       return true;
/*     */     }
/* 241 */     if (list1 == null || list2 == null || list1.size() != list2.size()) {
/* 242 */       return false;
/*     */     }
/*     */     
/* 245 */     Iterator<?> it1 = list1.iterator();
/* 246 */     Iterator<?> it2 = list2.iterator();
/* 247 */     Object obj1 = null;
/* 248 */     Object obj2 = null;
/*     */     
/* 250 */     while (it1.hasNext() && it2.hasNext()) {
/* 251 */       obj1 = it1.next();
/* 252 */       obj2 = it2.next();
/*     */       
/* 254 */       if ((obj1 == null) ? (obj2 == null) : obj1.equals(obj2))
/* 255 */         continue;  return false;
/*     */     } 
/*     */ 
/*     */     
/* 259 */     return (!it1.hasNext() && !it2.hasNext());
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
/*     */   public static int hashCodeForList(Collection<?> list) {
/* 275 */     if (list == null) {
/* 276 */       return 0;
/*     */     }
/* 278 */     int hashCode = 1;
/* 279 */     Iterator<?> it = list.iterator();
/*     */     
/* 281 */     while (it.hasNext()) {
/* 282 */       Object obj = it.next();
/* 283 */       hashCode = 31 * hashCode + ((obj == null) ? 0 : obj.hashCode());
/*     */     } 
/* 285 */     return hashCode;
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
/*     */   public static <E> List<E> retainAll(Collection<E> collection, Collection<?> retain) {
/* 312 */     List<E> list = new ArrayList<E>(Math.min(collection.size(), retain.size()));
/*     */     
/* 314 */     for (E obj : collection) {
/* 315 */       if (retain.contains(obj)) {
/* 316 */         list.add(obj);
/*     */       }
/*     */     } 
/* 319 */     return list;
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
/*     */   public static <E> List<E> removeAll(Collection<E> collection, Collection<?> remove) {
/* 346 */     List<E> list = new ArrayList<E>();
/* 347 */     for (E obj : collection) {
/* 348 */       if (!remove.contains(obj)) {
/* 349 */         list.add(obj);
/*     */       }
/*     */     } 
/* 352 */     return list;
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
/*     */   public static <E> List<E> synchronizedList(List<E> list) {
/* 380 */     return Collections.synchronizedList(list);
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
/*     */   public static <E> List<E> unmodifiableList(List<? extends E> list) {
/* 394 */     return UnmodifiableList.unmodifiableList(list);
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
/*     */   public static <E> List<E> predicatedList(List<E> list, Predicate<E> predicate) {
/* 412 */     return (List<E>)PredicatedList.predicatedList(list, predicate);
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
/*     */   public static <E> List<E> transformedList(List<E> list, Transformer<? super E, ? extends E> transformer) {
/* 437 */     return (List<E>)TransformedList.transformingList(list, transformer);
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
/*     */ 
/*     */   
/*     */   public static <E> List<E> lazyList(List<E> list, Factory<? extends E> factory) {
/* 471 */     return (List<E>)LazyList.lazyList(list, factory);
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
/*     */   public static <E> List<E> fixedSizeList(List<E> list) {
/* 486 */     return (List<E>)FixedSizeList.fixedSizeList(list);
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
/*     */   public static <E> int indexOf(List<E> list, Predicate<E> predicate) {
/* 502 */     if (list != null && predicate != null) {
/* 503 */       for (int i = 0; i < list.size(); i++) {
/* 504 */         E item = list.get(i);
/* 505 */         if (predicate.evaluate(item)) {
/* 506 */           return i;
/*     */         }
/*     */       } 
/*     */     }
/* 510 */     return -1;
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
/*     */   public static <E> List<E> longestCommonSubsequence(List<E> a, List<E> b) {
/* 525 */     return longestCommonSubsequence(a, b, (Equator<? super E>)DefaultEquator.defaultEquator());
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
/*     */   public static <E> List<E> longestCommonSubsequence(List<E> a, List<E> b, Equator<? super E> equator) {
/* 541 */     if (a == null || b == null) {
/* 542 */       throw new NullPointerException("List must not be null");
/*     */     }
/* 544 */     if (equator == null) {
/* 545 */       throw new NullPointerException("Equator must not be null");
/*     */     }
/*     */     
/* 548 */     SequencesComparator<E> comparator = new SequencesComparator(a, b, equator);
/* 549 */     EditScript<E> script = comparator.getScript();
/* 550 */     LcsVisitor<E> visitor = new LcsVisitor<E>();
/* 551 */     script.visit(visitor);
/* 552 */     return visitor.getSubSequence();
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
/*     */   public static String longestCommonSubsequence(CharSequence a, CharSequence b) {
/* 568 */     if (a == null || b == null) {
/* 569 */       throw new NullPointerException("CharSequence must not be null");
/*     */     }
/* 571 */     List<Character> lcs = longestCommonSubsequence(new CharSequenceAsList(a), new CharSequenceAsList(b));
/* 572 */     StringBuilder sb = new StringBuilder();
/* 573 */     for (Character ch : lcs) {
/* 574 */       sb.append(ch);
/*     */     }
/* 576 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class LcsVisitor<E>
/*     */     implements CommandVisitor<E>
/*     */   {
/* 586 */     private ArrayList<E> sequence = new ArrayList<E>();
/*     */ 
/*     */     
/*     */     public void visitInsertCommand(E object) {}
/*     */     
/*     */     public void visitDeleteCommand(E object) {}
/*     */     
/*     */     public void visitKeepCommand(E object) {
/* 594 */       this.sequence.add(object);
/*     */     }
/*     */     
/*     */     public List<E> getSubSequence() {
/* 598 */       return this.sequence;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class CharSequenceAsList
/*     */     extends AbstractList<Character>
/*     */   {
/*     */     private final CharSequence sequence;
/*     */ 
/*     */     
/*     */     public CharSequenceAsList(CharSequence sequence) {
/* 610 */       this.sequence = sequence;
/*     */     }
/*     */ 
/*     */     
/*     */     public Character get(int index) {
/* 615 */       return Character.valueOf(this.sequence.charAt(index));
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 620 */       return this.sequence.length();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> List<List<T>> partition(List<T> list, int size) {
/* 649 */     if (list == null) {
/* 650 */       throw new NullPointerException("List must not be null");
/*     */     }
/* 652 */     if (size <= 0) {
/* 653 */       throw new IllegalArgumentException("Size must be greater than 0");
/*     */     }
/* 655 */     return new Partition<T>(list, size);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Partition<T>
/*     */     extends AbstractList<List<T>>
/*     */   {
/*     */     private final List<T> list;
/*     */     
/*     */     private final int size;
/*     */     
/*     */     private Partition(List<T> list, int size) {
/* 667 */       this.list = list;
/* 668 */       this.size = size;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<T> get(int index) {
/* 673 */       int listSize = size();
/* 674 */       if (listSize < 0) {
/* 675 */         throw new IllegalArgumentException("negative size: " + listSize);
/*     */       }
/* 677 */       if (index < 0) {
/* 678 */         throw new IndexOutOfBoundsException("Index " + index + " must not be negative");
/*     */       }
/* 680 */       if (index >= listSize) {
/* 681 */         throw new IndexOutOfBoundsException("Index " + index + " must be less than size " + listSize);
/*     */       }
/*     */       
/* 684 */       int start = index * this.size;
/* 685 */       int end = Math.min(start + this.size, this.list.size());
/* 686 */       return this.list.subList(start, end);
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 691 */       return (this.list.size() + this.size - 1) / this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 696 */       return this.list.isEmpty();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\ListUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */