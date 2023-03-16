/*     */ package org.apache.commons.collections4.bag;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.Bag;
/*     */ import org.apache.commons.collections4.Predicate;
/*     */ import org.apache.commons.collections4.collection.PredicatedCollection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PredicatedBag<E>
/*     */   extends PredicatedCollection<E>
/*     */   implements Bag<E>
/*     */ {
/*     */   private static final long serialVersionUID = -2575833140344736876L;
/*     */   
/*     */   public static <E> PredicatedBag<E> predicatedBag(Bag<E> bag, Predicate<? super E> predicate) {
/*  63 */     return new PredicatedBag<E>(bag, predicate);
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
/*     */   protected PredicatedBag(Bag<E> bag, Predicate<? super E> predicate) {
/*  79 */     super((Collection)bag, predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Bag<E> decorated() {
/*  89 */     return (Bag<E>)super.decorated();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/*  94 */     return (object == this || decorated().equals(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  99 */     return decorated().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(E object, int count) {
/* 106 */     validate(object);
/* 107 */     return decorated().add(object, count);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object object, int count) {
/* 112 */     return decorated().remove(object, count);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> uniqueSet() {
/* 117 */     return decorated().uniqueSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCount(Object object) {
/* 122 */     return decorated().getCount(object);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\bag\PredicatedBag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */