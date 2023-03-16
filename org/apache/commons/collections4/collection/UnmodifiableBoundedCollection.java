/*     */ package org.apache.commons.collections4.collection;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import org.apache.commons.collections4.BoundedCollection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UnmodifiableBoundedCollection<E>
/*     */   extends AbstractCollectionDecorator<E>
/*     */   implements BoundedCollection<E>, Unmodifiable
/*     */ {
/*     */   private static final long serialVersionUID = -7112672385450340330L;
/*     */   
/*     */   public static <E> BoundedCollection<E> unmodifiableBoundedCollection(BoundedCollection<? extends E> coll) {
/*  59 */     if (coll instanceof Unmodifiable)
/*     */     {
/*  61 */       return (BoundedCollection)coll;
/*     */     }
/*     */     
/*  64 */     return new UnmodifiableBoundedCollection<E>(coll);
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
/*     */   public static <E> BoundedCollection<E> unmodifiableBoundedCollection(Collection<? extends E> coll) {
/*  82 */     if (coll == null) {
/*  83 */       throw new NullPointerException("Collection must not be null.");
/*     */     }
/*     */ 
/*     */     
/*  87 */     for (int i = 0; i < 1000 && 
/*  88 */       !(coll instanceof BoundedCollection); i++) {
/*     */ 
/*     */       
/*  91 */       if (coll instanceof AbstractCollectionDecorator) {
/*  92 */         coll = ((AbstractCollectionDecorator<? extends E>)coll).decorated();
/*  93 */       } else if (coll instanceof SynchronizedCollection) {
/*  94 */         coll = ((SynchronizedCollection<? extends E>)coll).decorated();
/*     */       } 
/*     */     } 
/*     */     
/*  98 */     if (!(coll instanceof BoundedCollection)) {
/*  99 */       throw new IllegalArgumentException("Collection is not a bounded collection.");
/*     */     }
/* 101 */     return new UnmodifiableBoundedCollection<E>((BoundedCollection<? extends E>)coll);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UnmodifiableBoundedCollection(BoundedCollection<? extends E> coll) {
/* 112 */     super((Collection)coll);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 118 */     return UnmodifiableIterator.unmodifiableIterator(decorated().iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(E object) {
/* 123 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends E> coll) {
/* 128 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 133 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object object) {
/* 138 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> coll) {
/* 143 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> coll) {
/* 148 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFull() {
/* 154 */     return decorated().isFull();
/*     */   }
/*     */ 
/*     */   
/*     */   public int maxSize() {
/* 159 */     return decorated().maxSize();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BoundedCollection<E> decorated() {
/* 164 */     return (BoundedCollection<E>)super.decorated();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\collection\UnmodifiableBoundedCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */