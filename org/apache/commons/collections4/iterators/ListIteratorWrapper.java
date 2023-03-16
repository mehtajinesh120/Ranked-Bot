/*     */ package org.apache.commons.collections4.iterators;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.commons.collections4.ResettableListIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ListIteratorWrapper<E>
/*     */   implements ResettableListIterator<E>
/*     */ {
/*     */   private static final String UNSUPPORTED_OPERATION_MESSAGE = "ListIteratorWrapper does not support optional operations of ListIterator.";
/*     */   private static final String CANNOT_REMOVE_MESSAGE = "Cannot remove element at index {0}.";
/*     */   private final Iterator<? extends E> iterator;
/*  60 */   private final List<E> list = new ArrayList<E>();
/*     */ 
/*     */   
/*  63 */   private int currentIndex = 0;
/*     */   
/*  65 */   private int wrappedIteratorIndex = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean removeState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListIteratorWrapper(Iterator<? extends E> iterator) {
/*  80 */     if (iterator == null) {
/*  81 */       throw new NullPointerException("Iterator must not be null");
/*     */     }
/*  83 */     this.iterator = iterator;
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
/*     */   public void add(E obj) throws UnsupportedOperationException {
/*  97 */     if (this.iterator instanceof ListIterator) {
/*     */       
/*  99 */       ListIterator<E> li = (ListIterator)this.iterator;
/* 100 */       li.add(obj);
/*     */       return;
/*     */     } 
/* 103 */     throw new UnsupportedOperationException("ListIteratorWrapper does not support optional operations of ListIterator.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/* 112 */     if (this.currentIndex == this.wrappedIteratorIndex || this.iterator instanceof ListIterator) {
/* 113 */       return this.iterator.hasNext();
/*     */     }
/* 115 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPrevious() {
/* 124 */     if (this.iterator instanceof ListIterator) {
/* 125 */       ListIterator<?> li = (ListIterator)this.iterator;
/* 126 */       return li.hasPrevious();
/*     */     } 
/* 128 */     return (this.currentIndex > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E next() throws NoSuchElementException {
/* 138 */     if (this.iterator instanceof ListIterator) {
/* 139 */       return this.iterator.next();
/*     */     }
/*     */     
/* 142 */     if (this.currentIndex < this.wrappedIteratorIndex) {
/* 143 */       this.currentIndex++;
/* 144 */       return this.list.get(this.currentIndex - 1);
/*     */     } 
/*     */     
/* 147 */     E retval = this.iterator.next();
/* 148 */     this.list.add(retval);
/* 149 */     this.currentIndex++;
/* 150 */     this.wrappedIteratorIndex++;
/* 151 */     this.removeState = true;
/* 152 */     return retval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int nextIndex() {
/* 161 */     if (this.iterator instanceof ListIterator) {
/* 162 */       ListIterator<?> li = (ListIterator)this.iterator;
/* 163 */       return li.nextIndex();
/*     */     } 
/* 165 */     return this.currentIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E previous() throws NoSuchElementException {
/* 175 */     if (this.iterator instanceof ListIterator) {
/*     */       
/* 177 */       ListIterator<E> li = (ListIterator)this.iterator;
/* 178 */       return li.previous();
/*     */     } 
/*     */     
/* 181 */     if (this.currentIndex == 0) {
/* 182 */       throw new NoSuchElementException();
/*     */     }
/* 184 */     this.removeState = (this.wrappedIteratorIndex == this.currentIndex);
/* 185 */     return this.list.get(--this.currentIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int previousIndex() {
/* 194 */     if (this.iterator instanceof ListIterator) {
/* 195 */       ListIterator<?> li = (ListIterator)this.iterator;
/* 196 */       return li.previousIndex();
/*     */     } 
/* 198 */     return this.currentIndex - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() throws UnsupportedOperationException {
/* 207 */     if (this.iterator instanceof ListIterator) {
/* 208 */       this.iterator.remove();
/*     */       return;
/*     */     } 
/* 211 */     int removeIndex = this.currentIndex;
/* 212 */     if (this.currentIndex == this.wrappedIteratorIndex) {
/* 213 */       removeIndex--;
/*     */     }
/* 215 */     if (!this.removeState || this.wrappedIteratorIndex - this.currentIndex > 1) {
/* 216 */       throw new IllegalStateException(MessageFormat.format("Cannot remove element at index {0}.", new Object[] { Integer.valueOf(removeIndex) }));
/*     */     }
/* 218 */     this.iterator.remove();
/* 219 */     this.list.remove(removeIndex);
/* 220 */     this.currentIndex = removeIndex;
/* 221 */     this.wrappedIteratorIndex--;
/* 222 */     this.removeState = false;
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
/*     */   public void set(E obj) throws UnsupportedOperationException {
/* 234 */     if (this.iterator instanceof ListIterator) {
/*     */       
/* 236 */       ListIterator<E> li = (ListIterator)this.iterator;
/* 237 */       li.set(obj);
/*     */       return;
/*     */     } 
/* 240 */     throw new UnsupportedOperationException("ListIteratorWrapper does not support optional operations of ListIterator.");
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
/*     */   public void reset() {
/* 252 */     if (this.iterator instanceof ListIterator) {
/* 253 */       ListIterator<?> li = (ListIterator)this.iterator;
/* 254 */       while (li.previousIndex() >= 0) {
/* 255 */         li.previous();
/*     */       }
/*     */       return;
/*     */     } 
/* 259 */     this.currentIndex = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\iterators\ListIteratorWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */