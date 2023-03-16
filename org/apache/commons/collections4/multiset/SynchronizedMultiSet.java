/*     */ package org.apache.commons.collections4.multiset;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.MultiSet;
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
/*     */ public class SynchronizedMultiSet<E>
/*     */   extends SynchronizedCollection<E>
/*     */   implements MultiSet<E>
/*     */ {
/*     */   private static final long serialVersionUID = 20150629L;
/*     */   
/*     */   public static <E> SynchronizedMultiSet<E> synchronizedMultiSet(MultiSet<E> multiset) {
/*  48 */     return new SynchronizedMultiSet<E>(multiset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SynchronizedMultiSet(MultiSet<E> multiset) {
/*  59 */     super((Collection)multiset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SynchronizedMultiSet(MultiSet<E> multiset, Object lock) {
/*  70 */     super((Collection)multiset, lock);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MultiSet<E> decorated() {
/*  80 */     return (MultiSet<E>)super.decorated();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/*  85 */     if (object == this) {
/*  86 */       return true;
/*     */     }
/*  88 */     synchronized (this.lock) {
/*  89 */       return decorated().equals(object);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  95 */     synchronized (this.lock) {
/*  96 */       return decorated().hashCode();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int add(E object, int count) {
/* 104 */     synchronized (this.lock) {
/* 105 */       return decorated().add(object, count);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int remove(Object object, int count) {
/* 111 */     synchronized (this.lock) {
/* 112 */       return decorated().remove(object, count);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCount(Object object) {
/* 118 */     synchronized (this.lock) {
/* 119 */       return decorated().getCount(object);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int setCount(E object, int count) {
/* 125 */     synchronized (this.lock) {
/* 126 */       return decorated().setCount(object, count);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> uniqueSet() {
/* 132 */     synchronized (this.lock) {
/* 133 */       Set<E> set = decorated().uniqueSet();
/* 134 */       return new SynchronizedSet<E>(set, this.lock);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<MultiSet.Entry<E>> entrySet() {
/* 140 */     synchronized (this.lock) {
/* 141 */       Set<MultiSet.Entry<E>> set = decorated().entrySet();
/* 142 */       return new SynchronizedSet<MultiSet.Entry<E>>(set, this.lock);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class SynchronizedSet<T>
/*     */     extends SynchronizedCollection<T>
/*     */     implements Set<T>
/*     */   {
/*     */     private static final long serialVersionUID = 20150629L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     SynchronizedSet(Set<T> set, Object lock) {
/* 160 */       super(set, lock);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\multiset\SynchronizedMultiSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */