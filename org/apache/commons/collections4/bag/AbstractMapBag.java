/*     */ package org.apache.commons.collections4.bag;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.Bag;
/*     */ import org.apache.commons.collections4.set.UnmodifiableSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMapBag<E>
/*     */   implements Bag<E>
/*     */ {
/*     */   private transient Map<E, MutableInteger> map;
/*     */   private int size;
/*     */   private transient int modCount;
/*     */   private transient Set<E> uniqueSet;
/*     */   
/*     */   protected AbstractMapBag() {}
/*     */   
/*     */   protected AbstractMapBag(Map<E, MutableInteger> map) {
/*  70 */     this.map = map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<E, MutableInteger> getMap() {
/*  80 */     return this.map;
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
/*  91 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 101 */     return this.map.isEmpty();
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
/*     */   public int getCount(Object object) {
/* 113 */     MutableInteger count = this.map.get(object);
/* 114 */     if (count != null) {
/* 115 */       return count.value;
/*     */     }
/* 117 */     return 0;
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
/*     */   public boolean contains(Object object) {
/* 130 */     return this.map.containsKey(object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> coll) {
/* 141 */     if (coll instanceof Bag) {
/* 142 */       return containsAll((Bag)coll);
/*     */     }
/* 144 */     return containsAll(new HashBag(coll));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean containsAll(Bag<?> other) {
/* 155 */     Iterator<?> it = other.uniqueSet().iterator();
/* 156 */     while (it.hasNext()) {
/* 157 */       Object current = it.next();
/* 158 */       if (getCount(current) < other.getCount(current)) {
/* 159 */         return false;
/*     */       }
/*     */     } 
/* 162 */     return true;
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
/*     */   public Iterator<E> iterator() {
/* 174 */     return new BagIterator<E>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   static class BagIterator<E>
/*     */     implements Iterator<E>
/*     */   {
/*     */     private final AbstractMapBag<E> parent;
/*     */     
/*     */     private final Iterator<Map.Entry<E, AbstractMapBag.MutableInteger>> entryIterator;
/*     */     
/*     */     private Map.Entry<E, AbstractMapBag.MutableInteger> current;
/*     */     
/*     */     private int itemCount;
/*     */     
/*     */     private final int mods;
/*     */     
/*     */     private boolean canRemove;
/*     */     
/*     */     public BagIterator(AbstractMapBag<E> parent) {
/* 194 */       this.parent = parent;
/* 195 */       this.entryIterator = parent.map.entrySet().iterator();
/* 196 */       this.current = null;
/* 197 */       this.mods = parent.modCount;
/* 198 */       this.canRemove = false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 204 */       return (this.itemCount > 0 || this.entryIterator.hasNext());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public E next() {
/* 210 */       if (this.parent.modCount != this.mods) {
/* 211 */         throw new ConcurrentModificationException();
/*     */       }
/* 213 */       if (this.itemCount == 0) {
/* 214 */         this.current = this.entryIterator.next();
/* 215 */         this.itemCount = ((AbstractMapBag.MutableInteger)this.current.getValue()).value;
/*     */       } 
/* 217 */       this.canRemove = true;
/* 218 */       this.itemCount--;
/* 219 */       return this.current.getKey();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void remove() {
/* 225 */       if (this.parent.modCount != this.mods) {
/* 226 */         throw new ConcurrentModificationException();
/*     */       }
/* 228 */       if (!this.canRemove) {
/* 229 */         throw new IllegalStateException();
/*     */       }
/* 231 */       AbstractMapBag.MutableInteger mut = this.current.getValue();
/* 232 */       if (mut.value > 1) {
/* 233 */         mut.value--;
/*     */       } else {
/* 235 */         this.entryIterator.remove();
/*     */       } 
/* 237 */       this.parent.size--;
/* 238 */       this.canRemove = false;
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
/*     */   public boolean add(E object) {
/* 251 */     return add(object, 1);
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
/*     */   public boolean add(E object, int nCopies) {
/* 263 */     this.modCount++;
/* 264 */     if (nCopies > 0) {
/* 265 */       MutableInteger mut = this.map.get(object);
/* 266 */       this.size += nCopies;
/* 267 */       if (mut == null) {
/* 268 */         this.map.put(object, new MutableInteger(nCopies));
/* 269 */         return true;
/*     */       } 
/* 271 */       mut.value += nCopies;
/* 272 */       return false;
/*     */     } 
/* 274 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends E> coll) {
/* 285 */     boolean changed = false;
/* 286 */     Iterator<? extends E> i = coll.iterator();
/* 287 */     while (i.hasNext()) {
/* 288 */       boolean added = add(i.next());
/* 289 */       changed = (changed || added);
/*     */     } 
/* 291 */     return changed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 300 */     this.modCount++;
/* 301 */     this.map.clear();
/* 302 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(Object object) {
/* 313 */     MutableInteger mut = this.map.get(object);
/* 314 */     if (mut == null) {
/* 315 */       return false;
/*     */     }
/* 317 */     this.modCount++;
/* 318 */     this.map.remove(object);
/* 319 */     this.size -= mut.value;
/* 320 */     return true;
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
/*     */   public boolean remove(Object object, int nCopies) {
/* 332 */     MutableInteger mut = this.map.get(object);
/* 333 */     if (mut == null) {
/* 334 */       return false;
/*     */     }
/* 336 */     if (nCopies <= 0) {
/* 337 */       return false;
/*     */     }
/* 339 */     this.modCount++;
/* 340 */     if (nCopies < mut.value) {
/* 341 */       mut.value -= nCopies;
/* 342 */       this.size -= nCopies;
/*     */     } else {
/* 344 */       this.map.remove(object);
/* 345 */       this.size -= mut.value;
/*     */     } 
/* 347 */     return true;
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
/*     */   public boolean removeAll(Collection<?> coll) {
/* 359 */     boolean result = false;
/* 360 */     if (coll != null) {
/* 361 */       Iterator<?> i = coll.iterator();
/* 362 */       while (i.hasNext()) {
/* 363 */         boolean changed = remove(i.next(), 1);
/* 364 */         result = (result || changed);
/*     */       } 
/*     */     } 
/* 367 */     return result;
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
/*     */   public boolean retainAll(Collection<?> coll) {
/* 379 */     if (coll instanceof Bag) {
/* 380 */       return retainAll((Bag)coll);
/*     */     }
/* 382 */     return retainAll(new HashBag(coll));
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
/*     */   boolean retainAll(Bag<?> other) {
/* 394 */     boolean result = false;
/* 395 */     Bag<E> excess = new HashBag<E>();
/* 396 */     Iterator<E> i = uniqueSet().iterator();
/* 397 */     while (i.hasNext()) {
/* 398 */       E current = i.next();
/* 399 */       int myCount = getCount(current);
/* 400 */       int otherCount = other.getCount(current);
/* 401 */       if (1 <= otherCount && otherCount <= myCount) {
/* 402 */         excess.add(current, myCount - otherCount); continue;
/*     */       } 
/* 404 */       excess.add(current, myCount);
/*     */     } 
/*     */     
/* 407 */     if (!excess.isEmpty()) {
/* 408 */       result = removeAll((Collection<?>)excess);
/*     */     }
/* 410 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class MutableInteger
/*     */   {
/*     */     protected int value;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     MutableInteger(int value) {
/* 426 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 431 */       if (!(obj instanceof MutableInteger)) {
/* 432 */         return false;
/*     */       }
/* 434 */       return (((MutableInteger)obj).value == this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 439 */       return this.value;
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
/*     */   public Object[] toArray() {
/* 451 */     Object[] result = new Object[size()];
/* 452 */     int i = 0;
/* 453 */     Iterator<E> it = this.map.keySet().iterator();
/* 454 */     while (it.hasNext()) {
/* 455 */       E current = it.next();
/* 456 */       for (int index = getCount(current); index > 0; index--) {
/* 457 */         result[i++] = current;
/*     */       }
/*     */     } 
/* 460 */     return result;
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
/*     */   public <T> T[] toArray(T[] array) {
/* 477 */     int size = size();
/* 478 */     if (array.length < size) {
/*     */       
/* 480 */       T[] unchecked = (T[])Array.newInstance(array.getClass().getComponentType(), size);
/* 481 */       array = unchecked;
/*     */     } 
/*     */     
/* 484 */     int i = 0;
/* 485 */     Iterator<E> it = this.map.keySet().iterator();
/* 486 */     while (it.hasNext()) {
/* 487 */       E current = it.next();
/* 488 */       for (int index = getCount(current); index > 0; index--) {
/*     */ 
/*     */         
/* 491 */         E e = current;
/* 492 */         array[i++] = (T)e;
/*     */       } 
/*     */     } 
/* 495 */     while (i < array.length) {
/* 496 */       array[i++] = null;
/*     */     }
/* 498 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<E> uniqueSet() {
/* 508 */     if (this.uniqueSet == null) {
/* 509 */       this.uniqueSet = UnmodifiableSet.unmodifiableSet(this.map.keySet());
/*     */     }
/* 511 */     return this.uniqueSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doWriteObject(ObjectOutputStream out) throws IOException {
/* 521 */     out.writeInt(this.map.size());
/* 522 */     for (Map.Entry<E, MutableInteger> entry : this.map.entrySet()) {
/* 523 */       out.writeObject(entry.getKey());
/* 524 */       out.writeInt(((MutableInteger)entry.getValue()).value);
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
/*     */   protected void doReadObject(Map<E, MutableInteger> map, ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 538 */     this.map = map;
/* 539 */     int entrySize = in.readInt();
/* 540 */     for (int i = 0; i < entrySize; i++) {
/*     */       
/* 542 */       E obj = (E)in.readObject();
/* 543 */       int count = in.readInt();
/* 544 */       map.put(obj, new MutableInteger(count));
/* 545 */       this.size += count;
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
/*     */   public boolean equals(Object object) {
/* 559 */     if (object == this) {
/* 560 */       return true;
/*     */     }
/* 562 */     if (!(object instanceof Bag)) {
/* 563 */       return false;
/*     */     }
/* 565 */     Bag<?> other = (Bag)object;
/* 566 */     if (other.size() != size()) {
/* 567 */       return false;
/*     */     }
/* 569 */     for (E element : this.map.keySet()) {
/* 570 */       if (other.getCount(element) != getCount(element)) {
/* 571 */         return false;
/*     */       }
/*     */     } 
/* 574 */     return true;
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
/*     */   public int hashCode() {
/* 588 */     int total = 0;
/* 589 */     for (Map.Entry<E, MutableInteger> entry : this.map.entrySet()) {
/* 590 */       E element = entry.getKey();
/* 591 */       MutableInteger count = entry.getValue();
/* 592 */       total += ((element == null) ? 0 : element.hashCode()) ^ count.value;
/*     */     } 
/* 594 */     return total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 604 */     if (size() == 0) {
/* 605 */       return "[]";
/*     */     }
/* 607 */     StringBuilder buf = new StringBuilder();
/* 608 */     buf.append('[');
/* 609 */     Iterator<E> it = uniqueSet().iterator();
/* 610 */     while (it.hasNext()) {
/* 611 */       Object current = it.next();
/* 612 */       int count = getCount(current);
/* 613 */       buf.append(count);
/* 614 */       buf.append(':');
/* 615 */       buf.append(current);
/* 616 */       if (it.hasNext()) {
/* 617 */         buf.append(',');
/*     */       }
/*     */     } 
/* 620 */     buf.append(']');
/* 621 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\bag\AbstractMapBag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */