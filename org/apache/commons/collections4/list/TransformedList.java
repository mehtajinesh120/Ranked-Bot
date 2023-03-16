/*     */ package org.apache.commons.collections4.list;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import org.apache.commons.collections4.Transformer;
/*     */ import org.apache.commons.collections4.collection.TransformedCollection;
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
/*     */ public class TransformedList<E>
/*     */   extends TransformedCollection<E>
/*     */   implements List<E>
/*     */ {
/*     */   private static final long serialVersionUID = 1077193035000013141L;
/*     */   
/*     */   public static <E> TransformedList<E> transformingList(List<E> list, Transformer<? super E, ? extends E> transformer) {
/*  61 */     return new TransformedList<E>(list, transformer);
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
/*     */   public static <E> TransformedList<E> transformedList(List<E> list, Transformer<? super E, ? extends E> transformer) {
/*  81 */     TransformedList<E> decorated = new TransformedList<E>(list, transformer);
/*  82 */     if (list.size() > 0) {
/*     */       
/*  84 */       E[] values = (E[])list.toArray();
/*  85 */       list.clear();
/*  86 */       for (E value : values) {
/*  87 */         decorated.decorated().add(transformer.transform(value));
/*     */       }
/*     */     } 
/*  90 */     return decorated;
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
/*     */   protected TransformedList(List<E> list, Transformer<? super E, ? extends E> transformer) {
/* 105 */     super(list, transformer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<E> getList() {
/* 114 */     return (List<E>)decorated();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 119 */     return (object == this || decorated().equals(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 124 */     return decorated().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E get(int index) {
/* 130 */     return getList().get(index);
/*     */   }
/*     */   
/*     */   public int indexOf(Object object) {
/* 134 */     return getList().indexOf(object);
/*     */   }
/*     */   
/*     */   public int lastIndexOf(Object object) {
/* 138 */     return getList().lastIndexOf(object);
/*     */   }
/*     */   
/*     */   public E remove(int index) {
/* 142 */     return getList().remove(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(int index, E object) {
/* 148 */     object = (E)transform(object);
/* 149 */     getList().add(index, object);
/*     */   }
/*     */   
/*     */   public boolean addAll(int index, Collection<? extends E> coll) {
/* 153 */     coll = transform(coll);
/* 154 */     return getList().addAll(index, coll);
/*     */   }
/*     */   
/*     */   public ListIterator<E> listIterator() {
/* 158 */     return listIterator(0);
/*     */   }
/*     */   
/*     */   public ListIterator<E> listIterator(int i) {
/* 162 */     return (ListIterator<E>)new TransformedListIterator(getList().listIterator(i));
/*     */   }
/*     */   
/*     */   public E set(int index, E object) {
/* 166 */     object = (E)transform(object);
/* 167 */     return getList().set(index, object);
/*     */   }
/*     */   
/*     */   public List<E> subList(int fromIndex, int toIndex) {
/* 171 */     List<E> sub = getList().subList(fromIndex, toIndex);
/* 172 */     return new TransformedList(sub, this.transformer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class TransformedListIterator
/*     */     extends AbstractListIteratorDecorator<E>
/*     */   {
/*     */     protected TransformedListIterator(ListIterator<E> iterator) {
/* 186 */       super(iterator);
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(E object) {
/* 191 */       object = (E)TransformedList.this.transform(object);
/* 192 */       getListIterator().add(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(E object) {
/* 197 */       object = (E)TransformedList.this.transform(object);
/* 198 */       getListIterator().set(object);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\list\TransformedList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */