/*     */ package org.apache.commons.collections4.bag;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import org.apache.commons.collections4.SortedBag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CollectionSortedBag<E>
/*     */   extends AbstractSortedBagDecorator<E>
/*     */ {
/*     */   private static final long serialVersionUID = -2560033712679053143L;
/*     */   
/*     */   public static <E> SortedBag<E> collectionSortedBag(SortedBag<E> bag) {
/*  47 */     return new CollectionSortedBag<E>(bag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CollectionSortedBag(SortedBag<E> bag) {
/*  58 */     super(bag);
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
/*  69 */     out.defaultWriteObject();
/*  70 */     out.writeObject(decorated());
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
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*  83 */     in.defaultReadObject();
/*  84 */     setCollection((Collection)in.readObject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> coll) {
/*  93 */     Iterator<?> e = coll.iterator();
/*  94 */     while (e.hasNext()) {
/*  95 */       if (!contains(e.next())) {
/*  96 */         return false;
/*     */       }
/*     */     } 
/*  99 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(E object) {
/* 104 */     return add(object, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends E> coll) {
/* 109 */     boolean changed = false;
/* 110 */     Iterator<? extends E> i = coll.iterator();
/* 111 */     while (i.hasNext()) {
/* 112 */       boolean added = add(i.next(), 1);
/* 113 */       changed = (changed || added);
/*     */     } 
/* 115 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object object) {
/* 120 */     return remove(object, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> coll) {
/* 125 */     if (coll != null) {
/* 126 */       boolean result = false;
/* 127 */       Iterator<?> i = coll.iterator();
/* 128 */       while (i.hasNext()) {
/* 129 */         Object obj = i.next();
/* 130 */         boolean changed = remove(obj, getCount(obj));
/* 131 */         result = (result || changed);
/*     */       } 
/* 133 */       return result;
/*     */     } 
/*     */     
/* 136 */     return decorated().removeAll(null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> coll) {
/* 142 */     if (coll != null) {
/* 143 */       boolean modified = false;
/* 144 */       Iterator<E> e = iterator();
/* 145 */       while (e.hasNext()) {
/* 146 */         if (!coll.contains(e.next())) {
/* 147 */           e.remove();
/* 148 */           modified = true;
/*     */         } 
/*     */       } 
/* 151 */       return modified;
/*     */     } 
/*     */     
/* 154 */     return decorated().retainAll(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(E object, int count) {
/* 164 */     decorated().add(object, count);
/* 165 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\bag\CollectionSortedBag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */