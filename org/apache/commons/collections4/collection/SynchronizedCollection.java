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
/*     */ public class SynchronizedCollection<E>
/*     */   implements Collection<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2412805092710877986L;
/*     */   private final Collection<E> collection;
/*     */   protected final Object lock;
/*     */   
/*     */   public static <T> SynchronizedCollection<T> synchronizedCollection(Collection<T> coll) {
/*  61 */     return new SynchronizedCollection<T>(coll);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SynchronizedCollection(Collection<E> collection) {
/*  72 */     if (collection == null) {
/*  73 */       throw new NullPointerException("Collection must not be null.");
/*     */     }
/*  75 */     this.collection = collection;
/*  76 */     this.lock = this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SynchronizedCollection(Collection<E> collection, Object lock) {
/*  87 */     if (collection == null) {
/*  88 */       throw new NullPointerException("Collection must not be null.");
/*     */     }
/*  90 */     if (lock == null) {
/*  91 */       throw new NullPointerException("Lock must not be null.");
/*     */     }
/*  93 */     this.collection = collection;
/*  94 */     this.lock = lock;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Collection<E> decorated() {
/* 103 */     return this.collection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(E object) {
/* 110 */     synchronized (this.lock) {
/* 111 */       return decorated().add(object);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends E> coll) {
/* 117 */     synchronized (this.lock) {
/* 118 */       return decorated().addAll(coll);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 124 */     synchronized (this.lock) {
/* 125 */       decorated().clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/* 131 */     synchronized (this.lock) {
/* 132 */       return decorated().contains(object);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> coll) {
/* 138 */     synchronized (this.lock) {
/* 139 */       return decorated().containsAll(coll);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 145 */     synchronized (this.lock) {
/* 146 */       return decorated().isEmpty();
/*     */     } 
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
/*     */   public Iterator<E> iterator() {
/* 163 */     return decorated().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 168 */     synchronized (this.lock) {
/* 169 */       return decorated().toArray();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] object) {
/* 175 */     synchronized (this.lock) {
/* 176 */       return decorated().toArray(object);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object object) {
/* 182 */     synchronized (this.lock) {
/* 183 */       return decorated().remove(object);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> coll) {
/* 189 */     synchronized (this.lock) {
/* 190 */       return decorated().removeAll(coll);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> coll) {
/* 196 */     synchronized (this.lock) {
/* 197 */       return decorated().retainAll(coll);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 203 */     synchronized (this.lock) {
/* 204 */       return decorated().size();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 210 */     synchronized (this.lock) {
/* 211 */       if (object == this) {
/* 212 */         return true;
/*     */       }
/* 214 */       return (object == this || decorated().equals(object));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 220 */     synchronized (this.lock) {
/* 221 */       return decorated().hashCode();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 227 */     synchronized (this.lock) {
/* 228 */       return decorated().toString();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\collection\SynchronizedCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */