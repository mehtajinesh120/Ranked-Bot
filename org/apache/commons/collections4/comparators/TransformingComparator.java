/*     */ package org.apache.commons.collections4.comparators;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ import org.apache.commons.collections4.ComparatorUtils;
/*     */ import org.apache.commons.collections4.Transformer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TransformingComparator<I, O>
/*     */   implements Comparator<I>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3456940356043606220L;
/*     */   private final Comparator<O> decorated;
/*     */   private final Transformer<? super I, ? extends O> transformer;
/*     */   
/*     */   public TransformingComparator(Transformer<? super I, ? extends O> transformer) {
/*  57 */     this(transformer, ComparatorUtils.NATURAL_COMPARATOR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TransformingComparator(Transformer<? super I, ? extends O> transformer, Comparator<O> decorated) {
/*  68 */     this.decorated = decorated;
/*  69 */     this.transformer = transformer;
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
/*     */   public int compare(I obj1, I obj2) {
/*  82 */     O value1 = (O)this.transformer.transform(obj1);
/*  83 */     O value2 = (O)this.transformer.transform(obj2);
/*  84 */     return this.decorated.compare(value1, value2);
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
/*     */   public int hashCode() {
/*  96 */     int total = 17;
/*  97 */     total = total * 37 + ((this.decorated == null) ? 0 : this.decorated.hashCode());
/*  98 */     total = total * 37 + ((this.transformer == null) ? 0 : this.transformer.hashCode());
/*  99 */     return total;
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
/*     */   public boolean equals(Object object) {
/* 116 */     if (this == object) {
/* 117 */       return true;
/*     */     }
/* 119 */     if (null == object) {
/* 120 */       return false;
/*     */     }
/* 122 */     if (object.getClass().equals(getClass())) {
/* 123 */       TransformingComparator<?, ?> comp = (TransformingComparator<?, ?>)object;
/* 124 */       return (((null == this.decorated) ? (null == comp.decorated) : this.decorated.equals(comp.decorated)) && ((null == this.transformer) ? (null == comp.transformer) : this.transformer.equals(comp.transformer)));
/*     */     } 
/*     */     
/* 127 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\comparators\TransformingComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */