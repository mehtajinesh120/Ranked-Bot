/*     */ package org.apache.commons.collections4.list;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import org.apache.commons.collections4.collection.AbstractCollectionDecorator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractListDecorator<E>
/*     */   extends AbstractCollectionDecorator<E>
/*     */   implements List<E>
/*     */ {
/*     */   private static final long serialVersionUID = 4500739654952315623L;
/*     */   
/*     */   protected AbstractListDecorator() {}
/*     */   
/*     */   protected AbstractListDecorator(List<E> list) {
/*  55 */     super(list);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<E> decorated() {
/*  65 */     return (List<E>)super.decorated();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/*  70 */     return (object == this || decorated().equals(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  75 */     return decorated().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(int index, E object) {
/*  81 */     decorated().add(index, object);
/*     */   }
/*     */   
/*     */   public boolean addAll(int index, Collection<? extends E> coll) {
/*  85 */     return decorated().addAll(index, coll);
/*     */   }
/*     */   
/*     */   public E get(int index) {
/*  89 */     return decorated().get(index);
/*     */   }
/*     */   
/*     */   public int indexOf(Object object) {
/*  93 */     return decorated().indexOf(object);
/*     */   }
/*     */   
/*     */   public int lastIndexOf(Object object) {
/*  97 */     return decorated().lastIndexOf(object);
/*     */   }
/*     */   
/*     */   public ListIterator<E> listIterator() {
/* 101 */     return decorated().listIterator();
/*     */   }
/*     */   
/*     */   public ListIterator<E> listIterator(int index) {
/* 105 */     return decorated().listIterator(index);
/*     */   }
/*     */   
/*     */   public E remove(int index) {
/* 109 */     return decorated().remove(index);
/*     */   }
/*     */   
/*     */   public E set(int index, E object) {
/* 113 */     return decorated().set(index, object);
/*     */   }
/*     */   
/*     */   public List<E> subList(int fromIndex, int toIndex) {
/* 117 */     return decorated().subList(fromIndex, toIndex);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\list\AbstractListDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */