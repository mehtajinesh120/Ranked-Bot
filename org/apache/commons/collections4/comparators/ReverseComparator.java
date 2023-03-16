/*     */ package org.apache.commons.collections4.comparators;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ import org.apache.commons.collections4.ComparatorUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReverseComparator<E>
/*     */   implements Comparator<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2858887242028539265L;
/*     */   private final Comparator<? super E> comparator;
/*     */   
/*     */   public ReverseComparator() {
/*  51 */     this(null);
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
/*     */   public ReverseComparator(Comparator<? super E> comparator) {
/*  64 */     this.comparator = (comparator == null) ? ComparatorUtils.NATURAL_COMPARATOR : comparator;
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
/*     */   public int compare(E obj1, E obj2) {
/*  77 */     return this.comparator.compare(obj2, obj1);
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
/*     */   public int hashCode() {
/*  90 */     return "ReverseComparator".hashCode() ^ this.comparator.hashCode();
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
/*     */   public boolean equals(Object object) {
/* 111 */     if (this == object) {
/* 112 */       return true;
/*     */     }
/* 114 */     if (null == object) {
/* 115 */       return false;
/*     */     }
/* 117 */     if (object.getClass().equals(getClass())) {
/* 118 */       ReverseComparator<?> thatrc = (ReverseComparator)object;
/* 119 */       return this.comparator.equals(thatrc.comparator);
/*     */     } 
/* 121 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\comparators\ReverseComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */