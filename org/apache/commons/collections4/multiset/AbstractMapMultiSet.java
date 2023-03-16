/*     */ package org.apache.commons.collections4.multiset;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.collections4.MultiSet;
/*     */ import org.apache.commons.collections4.iterators.AbstractIteratorDecorator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMapMultiSet<E>
/*     */   extends AbstractMultiSet<E>
/*     */ {
/*     */   private transient Map<E, MutableInteger> map;
/*     */   private transient int size;
/*     */   private transient int modCount;
/*     */   
/*     */   protected AbstractMapMultiSet() {}
/*     */   
/*     */   protected AbstractMapMultiSet(Map<E, MutableInteger> map) {
/*  65 */     this.map = map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<E, MutableInteger> getMap() {
/*  75 */     return this.map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setMap(Map<E, MutableInteger> map) {
/*  86 */     this.map = map;
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
/*  97 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 107 */     return this.map.isEmpty();
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
/* 119 */     MutableInteger count = this.map.get(object);
/* 120 */     if (count != null) {
/* 121 */       return count.value;
/*     */     }
/* 123 */     return 0;
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
/* 136 */     return this.map.containsKey(object);
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
/* 148 */     return new MapBasedMultiSetIterator<E>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class MapBasedMultiSetIterator<E>
/*     */     implements Iterator<E>
/*     */   {
/*     */     private final AbstractMapMultiSet<E> parent;
/*     */     
/*     */     private final Iterator<Map.Entry<E, AbstractMapMultiSet.MutableInteger>> entryIterator;
/*     */     
/*     */     private Map.Entry<E, AbstractMapMultiSet.MutableInteger> current;
/*     */     
/*     */     private int itemCount;
/*     */     
/*     */     private final int mods;
/*     */     
/*     */     private boolean canRemove;
/*     */     
/*     */     public MapBasedMultiSetIterator(AbstractMapMultiSet<E> parent) {
/* 168 */       this.parent = parent;
/* 169 */       this.entryIterator = parent.map.entrySet().iterator();
/* 170 */       this.current = null;
/* 171 */       this.mods = parent.modCount;
/* 172 */       this.canRemove = false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 178 */       return (this.itemCount > 0 || this.entryIterator.hasNext());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public E next() {
/* 184 */       if (this.parent.modCount != this.mods) {
/* 185 */         throw new ConcurrentModificationException();
/*     */       }
/* 187 */       if (this.itemCount == 0) {
/* 188 */         this.current = this.entryIterator.next();
/* 189 */         this.itemCount = ((AbstractMapMultiSet.MutableInteger)this.current.getValue()).value;
/*     */       } 
/* 191 */       this.canRemove = true;
/* 192 */       this.itemCount--;
/* 193 */       return this.current.getKey();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void remove() {
/* 199 */       if (this.parent.modCount != this.mods) {
/* 200 */         throw new ConcurrentModificationException();
/*     */       }
/* 202 */       if (!this.canRemove) {
/* 203 */         throw new IllegalStateException();
/*     */       }
/* 205 */       AbstractMapMultiSet.MutableInteger mut = this.current.getValue();
/* 206 */       if (mut.value > 1) {
/* 207 */         mut.value--;
/*     */       } else {
/* 209 */         this.entryIterator.remove();
/*     */       } 
/* 211 */       this.parent.size--;
/* 212 */       this.canRemove = false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int add(E object, int occurrences) {
/* 219 */     if (occurrences < 0) {
/* 220 */       throw new IllegalArgumentException("Occurrences must not be negative.");
/*     */     }
/*     */     
/* 223 */     MutableInteger mut = this.map.get(object);
/* 224 */     int oldCount = (mut != null) ? mut.value : 0;
/*     */     
/* 226 */     if (occurrences > 0) {
/* 227 */       this.modCount++;
/* 228 */       this.size += occurrences;
/* 229 */       if (mut == null) {
/* 230 */         this.map.put(object, new MutableInteger(occurrences));
/*     */       } else {
/* 232 */         mut.value += occurrences;
/*     */       } 
/*     */     } 
/* 235 */     return oldCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 244 */     this.modCount++;
/* 245 */     this.map.clear();
/* 246 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int remove(Object object, int occurrences) {
/* 251 */     if (occurrences < 0) {
/* 252 */       throw new IllegalArgumentException("Occurrences must not be negative.");
/*     */     }
/*     */     
/* 255 */     MutableInteger mut = this.map.get(object);
/* 256 */     if (mut == null) {
/* 257 */       return 0;
/*     */     }
/* 259 */     int oldCount = mut.value;
/* 260 */     if (occurrences > 0) {
/* 261 */       this.modCount++;
/* 262 */       if (occurrences < mut.value) {
/* 263 */         mut.value -= occurrences;
/* 264 */         this.size -= occurrences;
/*     */       } else {
/* 266 */         this.map.remove(object);
/* 267 */         this.size -= mut.value;
/*     */       } 
/*     */     } 
/* 270 */     return oldCount;
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
/* 286 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 291 */       if (!(obj instanceof MutableInteger)) {
/* 292 */         return false;
/*     */       }
/* 294 */       return (((MutableInteger)obj).value == this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 299 */       return this.value;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Iterator<E> createUniqueSetIterator() {
/* 306 */     return (Iterator<E>)new UniqueSetIterator<E>(getMap().keySet().iterator(), this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int uniqueElements() {
/* 311 */     return this.map.size();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Iterator<MultiSet.Entry<E>> createEntrySetIterator() {
/* 316 */     return new EntrySetIterator<E>(this.map.entrySet().iterator(), this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class UniqueSetIterator<E>
/*     */     extends AbstractIteratorDecorator<E>
/*     */   {
/*     */     protected final AbstractMapMultiSet<E> parent;
/*     */ 
/*     */ 
/*     */     
/* 329 */     protected E lastElement = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean canRemove = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected UniqueSetIterator(Iterator<E> iterator, AbstractMapMultiSet<E> parent) {
/* 340 */       super(iterator);
/* 341 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/* 346 */       this.lastElement = (E)super.next();
/* 347 */       this.canRemove = true;
/* 348 */       return this.lastElement;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 353 */       if (!this.canRemove) {
/* 354 */         throw new IllegalStateException("Iterator remove() can only be called once after next()");
/*     */       }
/* 356 */       int count = this.parent.getCount(this.lastElement);
/* 357 */       super.remove();
/* 358 */       this.parent.remove(this.lastElement, count);
/* 359 */       this.lastElement = null;
/* 360 */       this.canRemove = false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class EntrySetIterator<E>
/*     */     implements Iterator<MultiSet.Entry<E>>
/*     */   {
/*     */     protected final AbstractMapMultiSet<E> parent;
/*     */ 
/*     */     
/*     */     protected final Iterator<Map.Entry<E, AbstractMapMultiSet.MutableInteger>> decorated;
/*     */ 
/*     */     
/* 375 */     protected MultiSet.Entry<E> last = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean canRemove = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected EntrySetIterator(Iterator<Map.Entry<E, AbstractMapMultiSet.MutableInteger>> iterator, AbstractMapMultiSet<E> parent) {
/* 387 */       this.decorated = iterator;
/* 388 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 393 */       return this.decorated.hasNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public MultiSet.Entry<E> next() {
/* 398 */       this.last = new AbstractMapMultiSet.MultiSetEntry<E>(this.decorated.next());
/* 399 */       this.canRemove = true;
/* 400 */       return this.last;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 405 */       if (!this.canRemove) {
/* 406 */         throw new IllegalStateException("Iterator remove() can only be called once after next()");
/*     */       }
/* 408 */       this.decorated.remove();
/* 409 */       this.last = null;
/* 410 */       this.canRemove = false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class MultiSetEntry<E>
/*     */     extends AbstractMultiSet.AbstractEntry<E>
/*     */   {
/*     */     protected final Map.Entry<E, AbstractMapMultiSet.MutableInteger> parentEntry;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected MultiSetEntry(Map.Entry<E, AbstractMapMultiSet.MutableInteger> parentEntry) {
/* 426 */       this.parentEntry = parentEntry;
/*     */     }
/*     */ 
/*     */     
/*     */     public E getElement() {
/* 431 */       return this.parentEntry.getKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getCount() {
/* 436 */       return ((AbstractMapMultiSet.MutableInteger)this.parentEntry.getValue()).value;
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
/*     */   protected void doWriteObject(ObjectOutputStream out) throws IOException {
/* 448 */     out.writeInt(this.map.size());
/* 449 */     for (Map.Entry<E, MutableInteger> entry : this.map.entrySet()) {
/* 450 */       out.writeObject(entry.getKey());
/* 451 */       out.writeInt(((MutableInteger)entry.getValue()).value);
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
/*     */   protected void doReadObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 465 */     int entrySize = in.readInt();
/* 466 */     for (int i = 0; i < entrySize; i++) {
/*     */       
/* 468 */       E obj = (E)in.readObject();
/* 469 */       int count = in.readInt();
/* 470 */       this.map.put(obj, new MutableInteger(count));
/* 471 */       this.size += count;
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
/* 483 */     Object[] result = new Object[size()];
/* 484 */     int i = 0;
/* 485 */     for (Map.Entry<E, MutableInteger> entry : this.map.entrySet()) {
/* 486 */       E current = entry.getKey();
/* 487 */       MutableInteger count = entry.getValue();
/* 488 */       for (int index = count.value; index > 0; index--) {
/* 489 */         result[i++] = current;
/*     */       }
/*     */     } 
/* 492 */     return result;
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
/* 509 */     int size = size();
/* 510 */     if (array.length < size) {
/*     */       
/* 512 */       T[] unchecked = (T[])Array.newInstance(array.getClass().getComponentType(), size);
/* 513 */       array = unchecked;
/*     */     } 
/*     */     
/* 516 */     int i = 0;
/* 517 */     for (Map.Entry<E, MutableInteger> entry : this.map.entrySet()) {
/* 518 */       E current = entry.getKey();
/* 519 */       MutableInteger count = entry.getValue();
/* 520 */       for (int index = count.value; index > 0; index--) {
/*     */ 
/*     */         
/* 523 */         E e = current;
/* 524 */         array[i++] = (T)e;
/*     */       } 
/*     */     } 
/* 527 */     while (i < array.length) {
/* 528 */       array[i++] = null;
/*     */     }
/* 530 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 536 */     if (object == this) {
/* 537 */       return true;
/*     */     }
/* 539 */     if (!(object instanceof MultiSet)) {
/* 540 */       return false;
/*     */     }
/* 542 */     MultiSet<?> other = (MultiSet)object;
/* 543 */     if (other.size() != size()) {
/* 544 */       return false;
/*     */     }
/* 546 */     for (E element : this.map.keySet()) {
/* 547 */       if (other.getCount(element) != getCount(element)) {
/* 548 */         return false;
/*     */       }
/*     */     } 
/* 551 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 556 */     int total = 0;
/* 557 */     for (Map.Entry<E, MutableInteger> entry : this.map.entrySet()) {
/* 558 */       E element = entry.getKey();
/* 559 */       MutableInteger count = entry.getValue();
/* 560 */       total += ((element == null) ? 0 : element.hashCode()) ^ count.value;
/*     */     } 
/* 562 */     return total;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\multiset\AbstractMapMultiSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */