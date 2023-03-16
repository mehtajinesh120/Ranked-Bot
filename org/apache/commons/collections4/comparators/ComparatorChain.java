/*     */ package org.apache.commons.collections4.comparators;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ComparatorChain<E>
/*     */   implements Comparator<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -721644942746081630L;
/*     */   private final List<Comparator<E>> comparatorChain;
/*  58 */   private BitSet orderingBits = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isLocked = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ComparatorChain() {
/*  70 */     this(new ArrayList<Comparator<E>>(), new BitSet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ComparatorChain(Comparator<E> comparator) {
/*  80 */     this(comparator, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ComparatorChain(Comparator<E> comparator, boolean reverse) {
/*  91 */     this.comparatorChain = new ArrayList<Comparator<E>>(1);
/*  92 */     this.comparatorChain.add(comparator);
/*  93 */     this.orderingBits = new BitSet(1);
/*  94 */     if (reverse == true) {
/*  95 */       this.orderingBits.set(0);
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
/*     */   public ComparatorChain(List<Comparator<E>> list) {
/* 108 */     this(list, new BitSet(list.size()));
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
/*     */   public ComparatorChain(List<Comparator<E>> list, BitSet bits) {
/* 127 */     this.comparatorChain = list;
/* 128 */     this.orderingBits = bits;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComparator(Comparator<E> comparator) {
/* 139 */     addComparator(comparator, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComparator(Comparator<E> comparator, boolean reverse) {
/* 150 */     checkLocked();
/*     */     
/* 152 */     this.comparatorChain.add(comparator);
/* 153 */     if (reverse == true) {
/* 154 */       this.orderingBits.set(this.comparatorChain.size() - 1);
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
/*     */   public void setComparator(int index, Comparator<E> comparator) throws IndexOutOfBoundsException {
/* 168 */     setComparator(index, comparator, false);
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
/*     */   public void setComparator(int index, Comparator<E> comparator, boolean reverse) {
/* 180 */     checkLocked();
/*     */     
/* 182 */     this.comparatorChain.set(index, comparator);
/* 183 */     if (reverse == true) {
/* 184 */       this.orderingBits.set(index);
/*     */     } else {
/* 186 */       this.orderingBits.clear(index);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setForwardSort(int index) {
/* 197 */     checkLocked();
/* 198 */     this.orderingBits.clear(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReverseSort(int index) {
/* 208 */     checkLocked();
/* 209 */     this.orderingBits.set(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 218 */     return this.comparatorChain.size();
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
/*     */   public boolean isLocked() {
/* 230 */     return this.isLocked;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkLocked() {
/* 239 */     if (this.isLocked == true) {
/* 240 */       throw new UnsupportedOperationException("Comparator ordering cannot be changed after the first comparison is performed");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkChainIntegrity() {
/* 251 */     if (this.comparatorChain.size() == 0) {
/* 252 */       throw new UnsupportedOperationException("ComparatorChains must contain at least one Comparator");
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
/*     */   public int compare(E o1, E o2) throws UnsupportedOperationException {
/* 268 */     if (!this.isLocked) {
/* 269 */       checkChainIntegrity();
/* 270 */       this.isLocked = true;
/*     */     } 
/*     */ 
/*     */     
/* 274 */     Iterator<Comparator<E>> comparators = this.comparatorChain.iterator();
/* 275 */     for (int comparatorIndex = 0; comparators.hasNext(); comparatorIndex++) {
/*     */       
/* 277 */       Comparator<? super E> comparator = comparators.next();
/* 278 */       int retval = comparator.compare(o1, o2);
/* 279 */       if (retval != 0) {
/*     */         
/* 281 */         if (this.orderingBits.get(comparatorIndex) == true) {
/* 282 */           if (retval > 0) {
/* 283 */             retval = -1;
/*     */           } else {
/* 285 */             retval = 1;
/*     */           } 
/*     */         }
/* 288 */         return retval;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 293 */     return 0;
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
/* 306 */     int hash = 0;
/* 307 */     if (null != this.comparatorChain) {
/* 308 */       hash ^= this.comparatorChain.hashCode();
/*     */     }
/* 310 */     if (null != this.orderingBits) {
/* 311 */       hash ^= this.orderingBits.hashCode();
/*     */     }
/* 313 */     return hash;
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
/* 334 */     if (this == object) {
/* 335 */       return true;
/*     */     }
/* 337 */     if (null == object) {
/* 338 */       return false;
/*     */     }
/* 340 */     if (object.getClass().equals(getClass())) {
/* 341 */       ComparatorChain<?> chain = (ComparatorChain)object;
/* 342 */       return (((null == this.orderingBits) ? (null == chain.orderingBits) : this.orderingBits.equals(chain.orderingBits)) && ((null == this.comparatorChain) ? (null == chain.comparatorChain) : this.comparatorChain.equals(chain.comparatorChain)));
/*     */     } 
/*     */ 
/*     */     
/* 346 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\comparators\ComparatorChain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */