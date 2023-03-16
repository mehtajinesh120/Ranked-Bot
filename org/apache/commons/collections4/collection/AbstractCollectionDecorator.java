/*     */ package org.apache.commons.collections4.collection;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractCollectionDecorator<E>
/*     */   implements Collection<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6249888059822088500L;
/*     */   private Collection<E> collection;
/*     */   
/*     */   protected AbstractCollectionDecorator() {}
/*     */   
/*     */   protected AbstractCollectionDecorator(Collection<E> coll) {
/*  79 */     if (coll == null) {
/*  80 */       throw new NullPointerException("Collection must not be null.");
/*     */     }
/*  82 */     this.collection = coll;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Collection<E> decorated() {
/*  92 */     return this.collection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setCollection(Collection<E> coll) {
/* 103 */     this.collection = coll;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(E object) {
/* 110 */     return decorated().add(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends E> coll) {
/* 115 */     return decorated().addAll(coll);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 120 */     decorated().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/* 125 */     return decorated().contains(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 130 */     return decorated().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 135 */     return decorated().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object object) {
/* 140 */     return decorated().remove(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 145 */     return decorated().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 150 */     return decorated().toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] object) {
/* 155 */     return decorated().toArray(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> coll) {
/* 160 */     return decorated().containsAll(coll);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> coll) {
/* 165 */     return decorated().removeAll(coll);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> coll) {
/* 170 */     return decorated().retainAll(coll);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 175 */     return decorated().toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\collection\AbstractCollectionDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */