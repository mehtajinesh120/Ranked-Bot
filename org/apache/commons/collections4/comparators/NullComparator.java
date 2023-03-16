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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NullComparator<E>
/*     */   implements Comparator<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -5820772575483504339L;
/*     */   private final Comparator<? super E> nonNullComparator;
/*     */   private final boolean nullsAreHigh;
/*     */   
/*     */   public NullComparator() {
/*  56 */     this(ComparatorUtils.NATURAL_COMPARATOR, true);
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
/*     */   public NullComparator(Comparator<? super E> nonNullComparator) {
/*  73 */     this(nonNullComparator, true);
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
/*     */   public NullComparator(boolean nullsAreHigh) {
/*  90 */     this(ComparatorUtils.NATURAL_COMPARATOR, nullsAreHigh);
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
/*     */   public NullComparator(Comparator<? super E> nonNullComparator, boolean nullsAreHigh) {
/* 113 */     this.nonNullComparator = nonNullComparator;
/* 114 */     this.nullsAreHigh = nullsAreHigh;
/*     */     
/* 116 */     if (nonNullComparator == null) {
/* 117 */       throw new NullPointerException("null nonNullComparator");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(E o1, E o2) {
/* 140 */     if (o1 == o2) return 0; 
/* 141 */     if (o1 == null) return this.nullsAreHigh ? 1 : -1; 
/* 142 */     if (o2 == null) return this.nullsAreHigh ? -1 : 1; 
/* 143 */     return this.nonNullComparator.compare(o1, o2);
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
/* 155 */     return (this.nullsAreHigh ? -1 : 1) * this.nonNullComparator.hashCode();
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
/*     */   public boolean equals(Object obj) {
/* 171 */     if (obj == null) return false; 
/* 172 */     if (obj == this) return true; 
/* 173 */     if (!obj.getClass().equals(getClass())) return false;
/*     */     
/* 175 */     NullComparator<?> other = (NullComparator)obj;
/*     */     
/* 177 */     return (this.nullsAreHigh == other.nullsAreHigh && this.nonNullComparator.equals(other.nonNullComparator));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\comparators\NullComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */