/*     */ package org.apache.commons.collections4;
/*     */ 
/*     */ import org.apache.commons.collections4.bag.CollectionBag;
/*     */ import org.apache.commons.collections4.bag.HashBag;
/*     */ import org.apache.commons.collections4.bag.PredicatedBag;
/*     */ import org.apache.commons.collections4.bag.PredicatedSortedBag;
/*     */ import org.apache.commons.collections4.bag.SynchronizedBag;
/*     */ import org.apache.commons.collections4.bag.SynchronizedSortedBag;
/*     */ import org.apache.commons.collections4.bag.TransformedBag;
/*     */ import org.apache.commons.collections4.bag.TransformedSortedBag;
/*     */ import org.apache.commons.collections4.bag.TreeBag;
/*     */ import org.apache.commons.collections4.bag.UnmodifiableBag;
/*     */ import org.apache.commons.collections4.bag.UnmodifiableSortedBag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BagUtils
/*     */ {
/*  43 */   public static final Bag EMPTY_BAG = UnmodifiableBag.unmodifiableBag((Bag)new HashBag());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   public static final Bag EMPTY_SORTED_BAG = UnmodifiableSortedBag.unmodifiableSortedBag((SortedBag)new TreeBag());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Bag<E> synchronizedBag(Bag<E> bag) {
/*  85 */     return (Bag<E>)SynchronizedBag.synchronizedBag(bag);
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
/*     */   public static <E> Bag<E> unmodifiableBag(Bag<? extends E> bag) {
/*  98 */     return UnmodifiableBag.unmodifiableBag(bag);
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
/*     */   public static <E> Bag<E> predicatedBag(Bag<E> bag, Predicate<? super E> predicate) {
/* 117 */     return (Bag<E>)PredicatedBag.predicatedBag(bag, predicate);
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
/*     */   public static <E> Bag<E> transformingBag(Bag<E> bag, Transformer<? super E, ? extends E> transformer) {
/* 137 */     return TransformedBag.transformingBag(bag, transformer);
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
/*     */   public static <E> Bag<E> collectionBag(Bag<E> bag) {
/* 150 */     return CollectionBag.collectionBag(bag);
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
/*     */   public static <E> SortedBag<E> synchronizedSortedBag(SortedBag<E> bag) {
/* 181 */     return (SortedBag<E>)SynchronizedSortedBag.synchronizedSortedBag(bag);
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
/*     */   public static <E> SortedBag<E> unmodifiableSortedBag(SortedBag<E> bag) {
/* 195 */     return UnmodifiableSortedBag.unmodifiableSortedBag(bag);
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
/*     */   public static <E> SortedBag<E> predicatedSortedBag(SortedBag<E> bag, Predicate<? super E> predicate) {
/* 216 */     return (SortedBag<E>)PredicatedSortedBag.predicatedSortedBag(bag, predicate);
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
/*     */   public static <E> SortedBag<E> transformingSortedBag(SortedBag<E> bag, Transformer<? super E, ? extends E> transformer) {
/* 238 */     return (SortedBag<E>)TransformedSortedBag.transformingSortedBag(bag, transformer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Bag<E> emptyBag() {
/* 249 */     return EMPTY_BAG;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> SortedBag<E> emptySortedBag() {
/* 260 */     return (SortedBag<E>)EMPTY_SORTED_BAG;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\BagUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */