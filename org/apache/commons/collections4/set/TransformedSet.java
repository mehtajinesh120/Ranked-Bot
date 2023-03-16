/*     */ package org.apache.commons.collections4.set;
/*     */ 
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.Transformer;
/*     */ import org.apache.commons.collections4.collection.TransformedCollection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TransformedSet<E>
/*     */   extends TransformedCollection<E>
/*     */   implements Set<E>
/*     */ {
/*     */   private static final long serialVersionUID = 306127383500410386L;
/*     */   
/*     */   public static <E> TransformedSet<E> transformingSet(Set<E> set, Transformer<? super E, ? extends E> transformer) {
/*  58 */     return new TransformedSet<E>(set, transformer);
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
/*     */   public static <E> Set<E> transformedSet(Set<E> set, Transformer<? super E, ? extends E> transformer) {
/*  77 */     TransformedSet<E> decorated = new TransformedSet<E>(set, transformer);
/*  78 */     if (set.size() > 0) {
/*     */       
/*  80 */       E[] values = (E[])set.toArray();
/*  81 */       set.clear();
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
/*     */   protected TransformedSet(Set<E> set, Transformer<? super E, ? extends E> transformer) {
/* 101 */     super(set, transformer);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 106 */     return (object == this || decorated().equals(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 111 */     return decorated().hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\set\TransformedSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */