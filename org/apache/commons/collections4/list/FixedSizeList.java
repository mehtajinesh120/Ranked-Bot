/*     */ package org.apache.commons.collections4.list;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import org.apache.commons.collections4.BoundedCollection;
/*     */ import org.apache.commons.collections4.iterators.AbstractListIteratorDecorator;
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
/*     */ public class FixedSizeList<E>
/*     */   extends AbstractSerializableListDecorator<E>
/*     */   implements BoundedCollection<E>
/*     */ {
/*     */   private static final long serialVersionUID = -2218010673611160319L;
/*     */   
/*     */   public static <E> FixedSizeList<E> fixedSizeList(List<E> list) {
/*  56 */     return new FixedSizeList<E>(list);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FixedSizeList(List<E> list) {
/*  67 */     super(list);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(E object) {
/*  73 */     throw new UnsupportedOperationException("List is fixed size");
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int index, E object) {
/*  78 */     throw new UnsupportedOperationException("List is fixed size");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends E> coll) {
/*  83 */     throw new UnsupportedOperationException("List is fixed size");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(int index, Collection<? extends E> coll) {
/*  88 */     throw new UnsupportedOperationException("List is fixed size");
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  93 */     throw new UnsupportedOperationException("List is fixed size");
/*     */   }
/*     */ 
/*     */   
/*     */   public E get(int index) {
/*  98 */     return decorated().get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(Object object) {
/* 103 */     return decorated().indexOf(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 108 */     return UnmodifiableIterator.unmodifiableIterator(decorated().iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(Object object) {
/* 113 */     return decorated().lastIndexOf(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator() {
/* 118 */     return (ListIterator<E>)new FixedSizeListIterator(decorated().listIterator(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator(int index) {
/* 123 */     return (ListIterator<E>)new FixedSizeListIterator(decorated().listIterator(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public E remove(int index) {
/* 128 */     throw new UnsupportedOperationException("List is fixed size");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object object) {
/* 133 */     throw new UnsupportedOperationException("List is fixed size");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> coll) {
/* 138 */     throw new UnsupportedOperationException("List is fixed size");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> coll) {
/* 143 */     throw new UnsupportedOperationException("List is fixed size");
/*     */   }
/*     */ 
/*     */   
/*     */   public E set(int index, E object) {
/* 148 */     return decorated().set(index, object);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<E> subList(int fromIndex, int toIndex) {
/* 153 */     List<E> sub = decorated().subList(fromIndex, toIndex);
/* 154 */     return new FixedSizeList(sub);
/*     */   }
/*     */ 
/*     */   
/*     */   private class FixedSizeListIterator
/*     */     extends AbstractListIteratorDecorator<E>
/*     */   {
/*     */     protected FixedSizeListIterator(ListIterator<E> iterator) {
/* 162 */       super(iterator);
/*     */     }
/*     */     
/*     */     public void remove() {
/* 166 */       throw new UnsupportedOperationException("List is fixed size");
/*     */     }
/*     */     
/*     */     public void add(Object object) {
/* 170 */       throw new UnsupportedOperationException("List is fixed size");
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isFull() {
/* 175 */     return true;
/*     */   }
/*     */   
/*     */   public int maxSize() {
/* 179 */     return size();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\list\FixedSizeList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */