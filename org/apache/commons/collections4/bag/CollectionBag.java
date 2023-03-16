/*     */ package org.apache.commons.collections4.bag;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import org.apache.commons.collections4.Bag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CollectionBag<E>
/*     */   extends AbstractBagDecorator<E>
/*     */ {
/*     */   private static final long serialVersionUID = -2560033712679053143L;
/*     */   
/*     */   public static <E> Bag<E> collectionBag(Bag<E> bag) {
/*  55 */     return new CollectionBag<E>(bag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CollectionBag(Bag<E> bag) {
/*  66 */     super(bag);
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
/*  77 */     out.defaultWriteObject();
/*  78 */     out.writeObject(decorated());
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
/*  91 */     in.defaultReadObject();
/*  92 */     setCollection((Collection)in.readObject());
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
/*     */   public boolean containsAll(Collection<?> coll) {
/* 111 */     Iterator<?> e = coll.iterator();
/* 112 */     while (e.hasNext()) {
/* 113 */       if (!contains(e.next())) {
/* 114 */         return false;
/*     */       }
/*     */     } 
/* 117 */     return true;
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
/*     */   public boolean add(E object) {
/* 132 */     return add(object, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends E> coll) {
/* 137 */     boolean changed = false;
/* 138 */     Iterator<? extends E> i = coll.iterator();
/* 139 */     while (i.hasNext()) {
/* 140 */       boolean added = add(i.next(), 1);
/* 141 */       changed = (changed || added);
/*     */     } 
/* 143 */     return changed;
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
/*     */   public boolean remove(Object object) {
/* 158 */     return remove(object, 1);
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
/*     */   public boolean removeAll(Collection<?> coll) {
/* 172 */     if (coll != null) {
/* 173 */       boolean result = false;
/* 174 */       Iterator<?> i = coll.iterator();
/* 175 */       while (i.hasNext()) {
/* 176 */         Object obj = i.next();
/* 177 */         boolean changed = remove(obj, getCount(obj));
/* 178 */         result = (result || changed);
/*     */       } 
/* 180 */       return result;
/*     */     } 
/*     */     
/* 183 */     return decorated().removeAll(null);
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
/*     */   public boolean retainAll(Collection<?> coll) {
/* 206 */     if (coll != null) {
/* 207 */       boolean modified = false;
/* 208 */       Iterator<E> e = iterator();
/* 209 */       while (e.hasNext()) {
/* 210 */         if (!coll.contains(e.next())) {
/* 211 */           e.remove();
/* 212 */           modified = true;
/*     */         } 
/*     */       } 
/* 215 */       return modified;
/*     */     } 
/*     */     
/* 218 */     return decorated().retainAll(null);
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
/*     */   public boolean add(E object, int count) {
/* 239 */     decorated().add(object, count);
/* 240 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\bag\CollectionBag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */