/*     */ package org.apache.commons.collections4.queue;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Queue;
/*     */ import org.apache.commons.collections4.BoundedCollection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CircularFifoQueue<E>
/*     */   extends AbstractCollection<E>
/*     */   implements Queue<E>, BoundedCollection<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -8423413834657610406L;
/*     */   private transient E[] elements;
/*  59 */   private transient int start = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   private transient int end = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   private transient boolean full = false;
/*     */ 
/*     */   
/*     */   private final int maxElements;
/*     */ 
/*     */ 
/*     */   
/*     */   public CircularFifoQueue() {
/*  80 */     this(32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CircularFifoQueue(int size) {
/*  91 */     if (size <= 0) {
/*  92 */       throw new IllegalArgumentException("The size must be greater than 0");
/*     */     }
/*  94 */     this.elements = (E[])new Object[size];
/*  95 */     this.maxElements = this.elements.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CircularFifoQueue(Collection<? extends E> coll) {
/* 106 */     this(coll.size());
/* 107 */     addAll(coll);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 118 */     out.defaultWriteObject();
/* 119 */     out.writeInt(size());
/* 120 */     for (E e : this) {
/* 121 */       out.writeObject(e);
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
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 134 */     in.defaultReadObject();
/* 135 */     this.elements = (E[])new Object[this.maxElements];
/* 136 */     int size = in.readInt();
/* 137 */     for (int i = 0; i < size; i++) {
/* 138 */       this.elements[i] = (E)in.readObject();
/*     */     }
/* 140 */     this.start = 0;
/* 141 */     this.full = (size == this.maxElements);
/* 142 */     if (this.full) {
/* 143 */       this.end = 0;
/*     */     } else {
/* 145 */       this.end = size;
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
/*     */   public int size() {
/* 157 */     int size = 0;
/*     */     
/* 159 */     if (this.end < this.start) {
/* 160 */       size = this.maxElements - this.start + this.end;
/* 161 */     } else if (this.end == this.start) {
/* 162 */       size = this.full ? this.maxElements : 0;
/*     */     } else {
/* 164 */       size = this.end - this.start;
/*     */     } 
/*     */     
/* 167 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 177 */     return (size() == 0);
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
/*     */   public boolean isFull() {
/* 189 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAtFullCapacity() {
/* 200 */     return (size() == this.maxElements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int maxSize() {
/* 209 */     return this.maxElements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 217 */     this.full = false;
/* 218 */     this.start = 0;
/* 219 */     this.end = 0;
/* 220 */     Arrays.fill((Object[])this.elements, (Object)null);
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
/*     */   public boolean add(E element) {
/* 233 */     if (null == element) {
/* 234 */       throw new NullPointerException("Attempted to add null object to queue");
/*     */     }
/*     */     
/* 237 */     if (isAtFullCapacity()) {
/* 238 */       remove();
/*     */     }
/*     */     
/* 241 */     this.elements[this.end++] = element;
/*     */     
/* 243 */     if (this.end >= this.maxElements) {
/* 244 */       this.end = 0;
/*     */     }
/*     */     
/* 247 */     if (this.end == this.start) {
/* 248 */       this.full = true;
/*     */     }
/*     */     
/* 251 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E get(int index) {
/* 262 */     int sz = size();
/* 263 */     if (index < 0 || index >= sz) {
/* 264 */       throw new NoSuchElementException(String.format("The specified index (%1$d) is outside the available range [0, %2$d)", new Object[] { Integer.valueOf(index), Integer.valueOf(sz) }));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 269 */     int idx = (this.start + index) % this.maxElements;
/* 270 */     return this.elements[idx];
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
/*     */   public boolean offer(E element) {
/* 284 */     return add(element);
/*     */   }
/*     */   
/*     */   public E poll() {
/* 288 */     if (isEmpty()) {
/* 289 */       return null;
/*     */     }
/* 291 */     return remove();
/*     */   }
/*     */   
/*     */   public E element() {
/* 295 */     if (isEmpty()) {
/* 296 */       throw new NoSuchElementException("queue is empty");
/*     */     }
/* 298 */     return peek();
/*     */   }
/*     */   
/*     */   public E peek() {
/* 302 */     if (isEmpty()) {
/* 303 */       return null;
/*     */     }
/* 305 */     return this.elements[this.start];
/*     */   }
/*     */   
/*     */   public E remove() {
/* 309 */     if (isEmpty()) {
/* 310 */       throw new NoSuchElementException("queue is empty");
/*     */     }
/*     */     
/* 313 */     E element = this.elements[this.start];
/* 314 */     if (null != element) {
/* 315 */       this.elements[this.start++] = null;
/*     */       
/* 317 */       if (this.start >= this.maxElements) {
/* 318 */         this.start = 0;
/*     */       }
/* 320 */       this.full = false;
/*     */     } 
/* 322 */     return element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int increment(int index) {
/* 333 */     index++;
/* 334 */     if (index >= this.maxElements) {
/* 335 */       index = 0;
/*     */     }
/* 337 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int decrement(int index) {
/* 347 */     index--;
/* 348 */     if (index < 0) {
/* 349 */       index = this.maxElements - 1;
/*     */     }
/* 351 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 361 */     return new Iterator<E>()
/*     */       {
/* 363 */         private int index = CircularFifoQueue.this.start;
/* 364 */         private int lastReturnedIndex = -1;
/* 365 */         private boolean isFirst = CircularFifoQueue.this.full;
/*     */         
/*     */         public boolean hasNext() {
/* 368 */           return (this.isFirst || this.index != CircularFifoQueue.this.end);
/*     */         }
/*     */         
/*     */         public E next() {
/* 372 */           if (!hasNext()) {
/* 373 */             throw new NoSuchElementException();
/*     */           }
/* 375 */           this.isFirst = false;
/* 376 */           this.lastReturnedIndex = this.index;
/* 377 */           this.index = CircularFifoQueue.this.increment(this.index);
/* 378 */           return (E)CircularFifoQueue.this.elements[this.lastReturnedIndex];
/*     */         }
/*     */         
/*     */         public void remove() {
/* 382 */           if (this.lastReturnedIndex == -1) {
/* 383 */             throw new IllegalStateException();
/*     */           }
/*     */ 
/*     */           
/* 387 */           if (this.lastReturnedIndex == CircularFifoQueue.this.start) {
/* 388 */             CircularFifoQueue.this.remove();
/* 389 */             this.lastReturnedIndex = -1;
/*     */             
/*     */             return;
/*     */           } 
/* 393 */           int pos = this.lastReturnedIndex + 1;
/* 394 */           if (CircularFifoQueue.this.start < this.lastReturnedIndex && pos < CircularFifoQueue.this.end) {
/*     */             
/* 396 */             System.arraycopy(CircularFifoQueue.this.elements, pos, CircularFifoQueue.this.elements, this.lastReturnedIndex, CircularFifoQueue.this.end - pos);
/*     */           } else {
/*     */             
/* 399 */             while (pos != CircularFifoQueue.this.end) {
/* 400 */               if (pos >= CircularFifoQueue.this.maxElements) {
/* 401 */                 CircularFifoQueue.this.elements[pos - 1] = CircularFifoQueue.this.elements[0];
/* 402 */                 pos = 0; continue;
/*     */               } 
/* 404 */               CircularFifoQueue.this.elements[CircularFifoQueue.this.decrement(pos)] = CircularFifoQueue.this.elements[pos];
/* 405 */               pos = CircularFifoQueue.this.increment(pos);
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 410 */           this.lastReturnedIndex = -1;
/* 411 */           CircularFifoQueue.this.end = CircularFifoQueue.this.decrement(CircularFifoQueue.this.end);
/* 412 */           CircularFifoQueue.this.elements[CircularFifoQueue.this.end] = null;
/* 413 */           CircularFifoQueue.this.full = false;
/* 414 */           this.index = CircularFifoQueue.this.decrement(this.index);
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\queue\CircularFifoQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */