/*     */ package org.apache.commons.collections4.iterators;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PermutationIterator<E>
/*     */   implements Iterator<List<E>>
/*     */ {
/*     */   private int[] keys;
/*     */   private Map<Integer, E> objectMap;
/*     */   private boolean[] direction;
/*     */   private List<E> nextPermutation;
/*     */   
/*     */   public PermutationIterator(Collection<? extends E> coll) {
/*  77 */     if (coll == null) {
/*  78 */       throw new NullPointerException("The collection must not be null");
/*     */     }
/*     */     
/*  81 */     this.keys = new int[coll.size()];
/*  82 */     this.direction = new boolean[coll.size()];
/*  83 */     Arrays.fill(this.direction, false);
/*  84 */     int value = 1;
/*  85 */     this.objectMap = new HashMap<Integer, E>();
/*  86 */     for (E e : coll) {
/*  87 */       this.objectMap.put(Integer.valueOf(value), e);
/*  88 */       this.keys[value - 1] = value;
/*  89 */       value++;
/*     */     } 
/*  91 */     this.nextPermutation = new ArrayList<E>(coll);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/*  99 */     return (this.nextPermutation != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<E> next() {
/* 108 */     if (!hasNext()) {
/* 109 */       throw new NoSuchElementException();
/*     */     }
/*     */ 
/*     */     
/* 113 */     int indexOfLargestMobileInteger = -1;
/* 114 */     int largestKey = -1;
/* 115 */     for (int i = 0; i < this.keys.length; i++) {
/* 116 */       if ((this.direction[i] && i < this.keys.length - 1 && this.keys[i] > this.keys[i + 1]) || (!this.direction[i] && i > 0 && this.keys[i] > this.keys[i - 1]))
/*     */       {
/* 118 */         if (this.keys[i] > largestKey) {
/* 119 */           largestKey = this.keys[i];
/* 120 */           indexOfLargestMobileInteger = i;
/*     */         } 
/*     */       }
/*     */     } 
/* 124 */     if (largestKey == -1) {
/* 125 */       List<E> toReturn = this.nextPermutation;
/* 126 */       this.nextPermutation = null;
/* 127 */       return toReturn;
/*     */     } 
/*     */ 
/*     */     
/* 131 */     int offset = this.direction[indexOfLargestMobileInteger] ? 1 : -1;
/* 132 */     int tmpKey = this.keys[indexOfLargestMobileInteger];
/* 133 */     this.keys[indexOfLargestMobileInteger] = this.keys[indexOfLargestMobileInteger + offset];
/* 134 */     this.keys[indexOfLargestMobileInteger + offset] = tmpKey;
/* 135 */     boolean tmpDirection = this.direction[indexOfLargestMobileInteger];
/* 136 */     this.direction[indexOfLargestMobileInteger] = this.direction[indexOfLargestMobileInteger + offset];
/* 137 */     this.direction[indexOfLargestMobileInteger + offset] = tmpDirection;
/*     */ 
/*     */     
/* 140 */     List<E> nextP = new ArrayList<E>();
/* 141 */     for (int j = 0; j < this.keys.length; j++) {
/* 142 */       if (this.keys[j] > largestKey) {
/* 143 */         this.direction[j] = !this.direction[j];
/*     */       }
/* 145 */       nextP.add(this.objectMap.get(Integer.valueOf(this.keys[j])));
/*     */     } 
/* 147 */     List<E> result = this.nextPermutation;
/* 148 */     this.nextPermutation = nextP;
/* 149 */     return result;
/*     */   }
/*     */   
/*     */   public void remove() {
/* 153 */     throw new UnsupportedOperationException("remove() is not supported");
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\iterators\PermutationIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */