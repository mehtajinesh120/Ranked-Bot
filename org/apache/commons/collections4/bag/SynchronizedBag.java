/*     */ package org.apache.commons.collections4.bag;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.Bag;
/*     */ import org.apache.commons.collections4.collection.SynchronizedCollection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SynchronizedBag<E>
/*     */   extends SynchronizedCollection<E>
/*     */   implements Bag<E>
/*     */ {
/*     */   private static final long serialVersionUID = 8084674570753837109L;
/*     */   
/*     */   public static <E> SynchronizedBag<E> synchronizedBag(Bag<E> bag) {
/*  51 */     return new SynchronizedBag<E>(bag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SynchronizedBag(Bag<E> bag) {
/*  62 */     super((Collection)bag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SynchronizedBag(Bag<E> bag, Object lock) {
/*  73 */     super((Collection)bag, lock);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Bag<E> getBag() {
/*  82 */     return (Bag<E>)decorated();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/*  87 */     if (object == this) {
/*  88 */       return true;
/*     */     }
/*  90 */     synchronized (this.lock) {
/*  91 */       return getBag().equals(object);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  97 */     synchronized (this.lock) {
/*  98 */       return getBag().hashCode();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(E object, int count) {
/* 106 */     synchronized (this.lock) {
/* 107 */       return getBag().add(object, count);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object object, int count) {
/* 113 */     synchronized (this.lock) {
/* 114 */       return getBag().remove(object, count);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> uniqueSet() {
/* 120 */     synchronized (this.lock) {
/* 121 */       Set<E> set = getBag().uniqueSet();
/* 122 */       return new SynchronizedBagSet(set, this.lock);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCount(Object object) {
/* 128 */     synchronized (this.lock) {
/* 129 */       return getBag().getCount(object);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class SynchronizedBagSet
/*     */     extends SynchronizedCollection<E>
/*     */     implements Set<E>
/*     */   {
/*     */     private static final long serialVersionUID = 2990565892366827855L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     SynchronizedBagSet(Set<E> set, Object lock) {
/* 147 */       super(set, lock);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\bag\SynchronizedBag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */