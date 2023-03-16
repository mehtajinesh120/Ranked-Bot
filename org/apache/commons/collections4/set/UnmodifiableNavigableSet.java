/*     */ package org.apache.commons.collections4.set;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.SortedSet;
/*     */ import org.apache.commons.collections4.Unmodifiable;
/*     */ import org.apache.commons.collections4.iterators.UnmodifiableIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UnmodifiableNavigableSet<E>
/*     */   extends AbstractNavigableSetDecorator<E>
/*     */   implements Unmodifiable
/*     */ {
/*     */   private static final long serialVersionUID = 20150528L;
/*     */   
/*     */   public static <E> NavigableSet<E> unmodifiableNavigableSet(NavigableSet<E> set) {
/*  54 */     if (set instanceof Unmodifiable) {
/*  55 */       return set;
/*     */     }
/*  57 */     return new UnmodifiableNavigableSet<E>(set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UnmodifiableNavigableSet(NavigableSet<E> set) {
/*  68 */     super(set);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/*  74 */     return UnmodifiableIterator.unmodifiableIterator(decorated().iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(E object) {
/*  79 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends E> coll) {
/*  84 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  89 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object object) {
/*  94 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> coll) {
/*  99 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> coll) {
/* 104 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedSet<E> subSet(E fromElement, E toElement) {
/* 111 */     SortedSet<E> sub = decorated().subSet(fromElement, toElement);
/* 112 */     return UnmodifiableSortedSet.unmodifiableSortedSet(sub);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSet<E> headSet(E toElement) {
/* 117 */     SortedSet<E> head = decorated().headSet(toElement);
/* 118 */     return UnmodifiableSortedSet.unmodifiableSortedSet(head);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSet<E> tailSet(E fromElement) {
/* 123 */     SortedSet<E> tail = decorated().tailSet(fromElement);
/* 124 */     return UnmodifiableSortedSet.unmodifiableSortedSet(tail);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NavigableSet<E> descendingSet() {
/* 131 */     return unmodifiableNavigableSet(decorated().descendingSet());
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> descendingIterator() {
/* 136 */     return UnmodifiableIterator.unmodifiableIterator(decorated().descendingIterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 141 */     NavigableSet<E> sub = decorated().subSet(fromElement, fromInclusive, toElement, toInclusive);
/* 142 */     return unmodifiableNavigableSet(sub);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 147 */     NavigableSet<E> head = decorated().headSet(toElement, inclusive);
/* 148 */     return unmodifiableNavigableSet(head);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 153 */     NavigableSet<E> tail = decorated().tailSet(fromElement, inclusive);
/* 154 */     return unmodifiableNavigableSet(tail);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 165 */     out.defaultWriteObject();
/* 166 */     out.writeObject(decorated());
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
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 178 */     in.defaultReadObject();
/* 179 */     setCollection((Collection)in.readObject());
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\set\UnmodifiableNavigableSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */