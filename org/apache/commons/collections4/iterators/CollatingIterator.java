/*     */ package org.apache.commons.collections4.iterators;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.commons.collections4.list.UnmodifiableList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CollatingIterator<E>
/*     */   implements Iterator<E>
/*     */ {
/*  44 */   private Comparator<? super E> comparator = null;
/*     */ 
/*     */   
/*  47 */   private List<Iterator<? extends E>> iterators = null;
/*     */ 
/*     */   
/*  50 */   private List<E> values = null;
/*     */ 
/*     */   
/*  53 */   private BitSet valueSet = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private int lastReturned = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CollatingIterator() {
/*  71 */     this((Comparator<? super E>)null, 2);
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
/*     */   public CollatingIterator(Comparator<? super E> comp) {
/*  83 */     this(comp, 2);
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
/*     */   public CollatingIterator(Comparator<? super E> comp, int initIterCapacity) {
/*  98 */     this.iterators = new ArrayList<Iterator<? extends E>>(initIterCapacity);
/*  99 */     setComparator(comp);
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
/*     */   public CollatingIterator(Comparator<? super E> comp, Iterator<? extends E> a, Iterator<? extends E> b) {
/* 115 */     this(comp, 2);
/* 116 */     addIterator(a);
/* 117 */     addIterator(b);
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
/*     */   public CollatingIterator(Comparator<? super E> comp, Iterator<? extends E>[] iterators) {
/* 131 */     this(comp, iterators.length);
/* 132 */     for (Iterator<? extends E> iterator : iterators) {
/* 133 */       addIterator(iterator);
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
/*     */   public CollatingIterator(Comparator<? super E> comp, Collection<Iterator<? extends E>> iterators) {
/* 150 */     this(comp, iterators.size());
/* 151 */     for (Iterator<? extends E> iterator : iterators) {
/* 152 */       addIterator(iterator);
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
/*     */   public void addIterator(Iterator<? extends E> iterator) {
/* 166 */     checkNotStarted();
/* 167 */     if (iterator == null) {
/* 168 */       throw new NullPointerException("Iterator must not be null");
/*     */     }
/* 170 */     this.iterators.add(iterator);
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
/*     */   public void setIterator(int index, Iterator<? extends E> iterator) {
/* 183 */     checkNotStarted();
/* 184 */     if (iterator == null) {
/* 185 */       throw new NullPointerException("Iterator must not be null");
/*     */     }
/* 187 */     this.iterators.set(index, iterator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Iterator<? extends E>> getIterators() {
/* 196 */     return UnmodifiableList.unmodifiableList(this.iterators);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super E> getComparator() {
/* 205 */     return this.comparator;
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
/*     */   public void setComparator(Comparator<? super E> comp) {
/* 219 */     checkNotStarted();
/* 220 */     this.comparator = comp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/* 231 */     start();
/* 232 */     return (anyValueSet(this.valueSet) || anyHasNext(this.iterators));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E next() throws NoSuchElementException {
/* 242 */     if (!hasNext()) {
/* 243 */       throw new NoSuchElementException();
/*     */     }
/* 245 */     int leastIndex = least();
/* 246 */     if (leastIndex == -1) {
/* 247 */       throw new NoSuchElementException();
/*     */     }
/* 249 */     E val = this.values.get(leastIndex);
/* 250 */     clear(leastIndex);
/* 251 */     this.lastReturned = leastIndex;
/* 252 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() {
/* 262 */     if (this.lastReturned == -1) {
/* 263 */       throw new IllegalStateException("No value can be removed at present");
/*     */     }
/* 265 */     ((Iterator)this.iterators.get(this.lastReturned)).remove();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIteratorIndex() {
/* 275 */     if (this.lastReturned == -1) {
/* 276 */       throw new IllegalStateException("No value has been returned yet");
/*     */     }
/*     */     
/* 279 */     return this.lastReturned;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void start() {
/* 288 */     if (this.values == null) {
/* 289 */       this.values = new ArrayList<E>(this.iterators.size());
/* 290 */       this.valueSet = new BitSet(this.iterators.size());
/* 291 */       for (int i = 0; i < this.iterators.size(); i++) {
/* 292 */         this.values.add(null);
/* 293 */         this.valueSet.clear(i);
/*     */       } 
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
/*     */   private boolean set(int i) {
/* 307 */     Iterator<? extends E> it = this.iterators.get(i);
/* 308 */     if (it.hasNext()) {
/* 309 */       this.values.set(i, it.next());
/* 310 */       this.valueSet.set(i);
/* 311 */       return true;
/*     */     } 
/* 313 */     this.values.set(i, null);
/* 314 */     this.valueSet.clear(i);
/* 315 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clear(int i) {
/* 323 */     this.values.set(i, null);
/* 324 */     this.valueSet.clear(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkNotStarted() throws IllegalStateException {
/* 334 */     if (this.values != null) {
/* 335 */       throw new IllegalStateException("Can't do that after next or hasNext has been called.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int least() {
/* 346 */     int leastIndex = -1;
/* 347 */     E leastObject = null;
/* 348 */     for (int i = 0; i < this.values.size(); i++) {
/* 349 */       if (!this.valueSet.get(i)) {
/* 350 */         set(i);
/*     */       }
/* 352 */       if (this.valueSet.get(i)) {
/* 353 */         if (leastIndex == -1) {
/* 354 */           leastIndex = i;
/* 355 */           leastObject = this.values.get(i);
/*     */         } else {
/* 357 */           E curObject = this.values.get(i);
/* 358 */           if (this.comparator == null) {
/* 359 */             throw new NullPointerException("You must invoke setComparator() to set a comparator first.");
/*     */           }
/* 361 */           if (this.comparator.compare(curObject, leastObject) < 0) {
/* 362 */             leastObject = curObject;
/* 363 */             leastIndex = i;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 368 */     return leastIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean anyValueSet(BitSet set) {
/* 376 */     for (int i = 0; i < set.size(); i++) {
/* 377 */       if (set.get(i)) {
/* 378 */         return true;
/*     */       }
/*     */     } 
/* 381 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean anyHasNext(List<Iterator<? extends E>> iters) {
/* 389 */     for (Iterator<? extends E> iterator : iters) {
/* 390 */       if (iterator.hasNext()) {
/* 391 */         return true;
/*     */       }
/*     */     } 
/* 394 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\iterators\CollatingIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */