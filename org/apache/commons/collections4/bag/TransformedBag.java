/*     */ package org.apache.commons.collections4.bag;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.Bag;
/*     */ import org.apache.commons.collections4.Transformer;
/*     */ import org.apache.commons.collections4.collection.TransformedCollection;
/*     */ import org.apache.commons.collections4.set.TransformedSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TransformedBag<E>
/*     */   extends TransformedCollection<E>
/*     */   implements Bag<E>
/*     */ {
/*     */   private static final long serialVersionUID = 5421170911299074185L;
/*     */   
/*     */   public static <E> Bag<E> transformingBag(Bag<E> bag, Transformer<? super E, ? extends E> transformer) {
/*  58 */     return new TransformedBag<E>(bag, transformer);
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
/*     */   public static <E> Bag<E> transformedBag(Bag<E> bag, Transformer<? super E, ? extends E> transformer) {
/*  77 */     TransformedBag<E> decorated = new TransformedBag<E>(bag, transformer);
/*  78 */     if (bag.size() > 0) {
/*     */       
/*  80 */       E[] values = (E[])bag.toArray();
/*  81 */       bag.clear();
/*  82 */       for (E value : values) {
/*  83 */         decorated.decorated().add(transformer.transform(value));
/*     */       }
/*     */     } 
/*  86 */     return decorated;
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
/*     */   protected TransformedBag(Bag<E> bag, Transformer<? super E, ? extends E> transformer) {
/* 101 */     super((Collection)bag, transformer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Bag<E> getBag() {
/* 110 */     return (Bag<E>)decorated();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 115 */     return (object == this || decorated().equals(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 120 */     return decorated().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCount(Object object) {
/* 127 */     return getBag().getCount(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object object, int nCopies) {
/* 132 */     return getBag().remove(object, nCopies);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(E object, int nCopies) {
/* 139 */     return getBag().add(transform(object), nCopies);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> uniqueSet() {
/* 144 */     Set<E> set = getBag().uniqueSet();
/* 145 */     return (Set<E>)TransformedSet.transformingSet(set, this.transformer);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\bag\TransformedBag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */