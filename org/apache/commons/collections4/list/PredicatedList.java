/*     */ package org.apache.commons.collections4.list;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import org.apache.commons.collections4.Predicate;
/*     */ import org.apache.commons.collections4.collection.PredicatedCollection;
/*     */ import org.apache.commons.collections4.iterators.AbstractListIteratorDecorator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PredicatedList<E>
/*     */   extends PredicatedCollection<E>
/*     */   implements List<E>
/*     */ {
/*     */   private static final long serialVersionUID = -5722039223898659102L;
/*     */   
/*     */   public static <T> PredicatedList<T> predicatedList(List<T> list, Predicate<? super T> predicate) {
/*  68 */     return new PredicatedList<T>(list, predicate);
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
/*     */   protected PredicatedList(List<E> list, Predicate<? super E> predicate) {
/*  84 */     super(list, predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<E> decorated() {
/*  94 */     return (List<E>)super.decorated();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/*  99 */     return (object == this || decorated().equals(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 104 */     return decorated().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E get(int index) {
/* 110 */     return decorated().get(index);
/*     */   }
/*     */   
/*     */   public int indexOf(Object object) {
/* 114 */     return decorated().indexOf(object);
/*     */   }
/*     */   
/*     */   public int lastIndexOf(Object object) {
/* 118 */     return decorated().lastIndexOf(object);
/*     */   }
/*     */   
/*     */   public E remove(int index) {
/* 122 */     return decorated().remove(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(int index, E object) {
/* 128 */     validate(object);
/* 129 */     decorated().add(index, object);
/*     */   }
/*     */   
/*     */   public boolean addAll(int index, Collection<? extends E> coll) {
/* 133 */     for (E aColl : coll) {
/* 134 */       validate(aColl);
/*     */     }
/* 136 */     return decorated().addAll(index, coll);
/*     */   }
/*     */   
/*     */   public ListIterator<E> listIterator() {
/* 140 */     return listIterator(0);
/*     */   }
/*     */   
/*     */   public ListIterator<E> listIterator(int i) {
/* 144 */     return (ListIterator<E>)new PredicatedListIterator(decorated().listIterator(i));
/*     */   }
/*     */   
/*     */   public E set(int index, E object) {
/* 148 */     validate(object);
/* 149 */     return decorated().set(index, object);
/*     */   }
/*     */   
/*     */   public List<E> subList(int fromIndex, int toIndex) {
/* 153 */     List<E> sub = decorated().subList(fromIndex, toIndex);
/* 154 */     return new PredicatedList(sub, this.predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class PredicatedListIterator
/*     */     extends AbstractListIteratorDecorator<E>
/*     */   {
/*     */     protected PredicatedListIterator(ListIterator<E> iterator) {
/* 168 */       super(iterator);
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(E object) {
/* 173 */       PredicatedList.this.validate(object);
/* 174 */       getListIterator().add(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(E object) {
/* 179 */       PredicatedList.this.validate(object);
/* 180 */       getListIterator().set(object);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\list\PredicatedList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */