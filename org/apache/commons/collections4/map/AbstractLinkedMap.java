/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.commons.collections4.MapIterator;
/*     */ import org.apache.commons.collections4.OrderedIterator;
/*     */ import org.apache.commons.collections4.OrderedMap;
/*     */ import org.apache.commons.collections4.OrderedMapIterator;
/*     */ import org.apache.commons.collections4.ResettableIterator;
/*     */ import org.apache.commons.collections4.iterators.EmptyOrderedIterator;
/*     */ import org.apache.commons.collections4.iterators.EmptyOrderedMapIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractLinkedMap<K, V>
/*     */   extends AbstractHashedMap<K, V>
/*     */   implements OrderedMap<K, V>
/*     */ {
/*     */   transient LinkEntry<K, V> header;
/*     */   
/*     */   protected AbstractLinkedMap() {}
/*     */   
/*     */   protected AbstractLinkedMap(int initialCapacity, float loadFactor, int threshold) {
/*  82 */     super(initialCapacity, loadFactor, threshold);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractLinkedMap(int initialCapacity) {
/*  92 */     super(initialCapacity);
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
/*     */   protected AbstractLinkedMap(int initialCapacity, float loadFactor) {
/* 105 */     super(initialCapacity, loadFactor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractLinkedMap(Map<? extends K, ? extends V> map) {
/* 115 */     super(map);
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
/*     */   protected void init() {
/* 127 */     this.header = createEntry((AbstractHashedMap.HashEntry<K, V>)null, -1, (K)null, (V)null);
/* 128 */     this.header.before = this.header.after = this.header;
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
/*     */   public boolean containsValue(Object value) {
/* 141 */     if (value == null) {
/* 142 */       for (LinkEntry<K, V> entry = this.header.after; entry != this.header; entry = entry.after) {
/* 143 */         if (entry.getValue() == null) {
/* 144 */           return true;
/*     */         }
/*     */       } 
/*     */     } else {
/* 148 */       for (LinkEntry<K, V> entry = this.header.after; entry != this.header; entry = entry.after) {
/* 149 */         if (isEqualValue(value, entry.getValue())) {
/* 150 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 154 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 164 */     super.clear();
/* 165 */     this.header.before = this.header.after = this.header;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K firstKey() {
/* 175 */     if (this.size == 0) {
/* 176 */       throw new NoSuchElementException("Map is empty");
/*     */     }
/* 178 */     return this.header.after.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K lastKey() {
/* 187 */     if (this.size == 0) {
/* 188 */       throw new NoSuchElementException("Map is empty");
/*     */     }
/* 190 */     return this.header.before.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K nextKey(Object key) {
/* 200 */     LinkEntry<K, V> entry = getEntry(key);
/* 201 */     return (entry == null || entry.after == this.header) ? null : entry.after.getKey();
/*     */   }
/*     */ 
/*     */   
/*     */   protected LinkEntry<K, V> getEntry(Object key) {
/* 206 */     return (LinkEntry<K, V>)super.getEntry(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K previousKey(Object key) {
/* 216 */     LinkEntry<K, V> entry = getEntry(key);
/* 217 */     return (entry == null || entry.before == this.header) ? null : entry.before.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected LinkEntry<K, V> getEntry(int index) {
/*     */     LinkEntry<K, V> entry;
/* 229 */     if (index < 0) {
/* 230 */       throw new IndexOutOfBoundsException("Index " + index + " is less than zero");
/*     */     }
/* 232 */     if (index >= this.size) {
/* 233 */       throw new IndexOutOfBoundsException("Index " + index + " is invalid for size " + this.size);
/*     */     }
/*     */     
/* 236 */     if (index < this.size / 2) {
/*     */       
/* 238 */       entry = this.header.after;
/* 239 */       for (int currentIndex = 0; currentIndex < index; currentIndex++) {
/* 240 */         entry = entry.after;
/*     */       }
/*     */     } else {
/*     */       
/* 244 */       entry = this.header;
/* 245 */       for (int currentIndex = this.size; currentIndex > index; currentIndex--) {
/* 246 */         entry = entry.before;
/*     */       }
/*     */     } 
/* 249 */     return entry;
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
/*     */   protected void addEntry(AbstractHashedMap.HashEntry<K, V> entry, int hashIndex) {
/* 263 */     LinkEntry<K, V> link = (LinkEntry<K, V>)entry;
/* 264 */     link.after = this.header;
/* 265 */     link.before = this.header.before;
/* 266 */     this.header.before.after = link;
/* 267 */     this.header.before = link;
/* 268 */     this.data[hashIndex] = link;
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
/*     */   protected LinkEntry<K, V> createEntry(AbstractHashedMap.HashEntry<K, V> next, int hashCode, K key, V value) {
/* 284 */     return new LinkEntry<K, V>(next, hashCode, convertKey(key), value);
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
/*     */   protected void removeEntry(AbstractHashedMap.HashEntry<K, V> entry, int hashIndex, AbstractHashedMap.HashEntry<K, V> previous) {
/* 299 */     LinkEntry<K, V> link = (LinkEntry<K, V>)entry;
/* 300 */     link.before.after = link.after;
/* 301 */     link.after.before = link.before;
/* 302 */     link.after = null;
/* 303 */     link.before = null;
/* 304 */     super.removeEntry(entry, hashIndex, previous);
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
/*     */   protected LinkEntry<K, V> entryBefore(LinkEntry<K, V> entry) {
/* 318 */     return entry.before;
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
/*     */   protected LinkEntry<K, V> entryAfter(LinkEntry<K, V> entry) {
/* 331 */     return entry.after;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OrderedMapIterator<K, V> mapIterator() {
/* 340 */     if (this.size == 0) {
/* 341 */       return EmptyOrderedMapIterator.emptyOrderedMapIterator();
/*     */     }
/* 343 */     return new LinkMapIterator<K, V>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class LinkMapIterator<K, V>
/*     */     extends LinkIterator<K, V>
/*     */     implements OrderedMapIterator<K, V>, ResettableIterator<K>
/*     */   {
/*     */     protected LinkMapIterator(AbstractLinkedMap<K, V> parent) {
/* 353 */       super(parent);
/*     */     }
/*     */     
/*     */     public K next() {
/* 357 */       return nextEntry().getKey();
/*     */     }
/*     */     
/*     */     public K previous() {
/* 361 */       return previousEntry().getKey();
/*     */     }
/*     */     
/*     */     public K getKey() {
/* 365 */       AbstractLinkedMap.LinkEntry<K, V> current = currentEntry();
/* 366 */       if (current == null) {
/* 367 */         throw new IllegalStateException("getKey() can only be called after next() and before remove()");
/*     */       }
/* 369 */       return current.getKey();
/*     */     }
/*     */     
/*     */     public V getValue() {
/* 373 */       AbstractLinkedMap.LinkEntry<K, V> current = currentEntry();
/* 374 */       if (current == null) {
/* 375 */         throw new IllegalStateException("getValue() can only be called after next() and before remove()");
/*     */       }
/* 377 */       return current.getValue();
/*     */     }
/*     */     
/*     */     public V setValue(V value) {
/* 381 */       AbstractLinkedMap.LinkEntry<K, V> current = currentEntry();
/* 382 */       if (current == null) {
/* 383 */         throw new IllegalStateException("setValue() can only be called after next() and before remove()");
/*     */       }
/* 385 */       return current.setValue(value);
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
/*     */   protected Iterator<Map.Entry<K, V>> createEntrySetIterator() {
/* 398 */     if (size() == 0) {
/* 399 */       return (Iterator<Map.Entry<K, V>>)EmptyOrderedIterator.emptyOrderedIterator();
/*     */     }
/* 401 */     return (Iterator)new EntrySetIterator<K, V>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class EntrySetIterator<K, V>
/*     */     extends LinkIterator<K, V>
/*     */     implements OrderedIterator<Map.Entry<K, V>>, ResettableIterator<Map.Entry<K, V>>
/*     */   {
/*     */     protected EntrySetIterator(AbstractLinkedMap<K, V> parent) {
/* 411 */       super(parent);
/*     */     }
/*     */     
/*     */     public Map.Entry<K, V> next() {
/* 415 */       return nextEntry();
/*     */     }
/*     */     
/*     */     public Map.Entry<K, V> previous() {
/* 419 */       return previousEntry();
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
/*     */   protected Iterator<K> createKeySetIterator() {
/* 432 */     if (size() == 0) {
/* 433 */       return (Iterator<K>)EmptyOrderedIterator.emptyOrderedIterator();
/*     */     }
/* 435 */     return (Iterator<K>)new KeySetIterator<K>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class KeySetIterator<K>
/*     */     extends LinkIterator<K, Object>
/*     */     implements OrderedIterator<K>, ResettableIterator<K>
/*     */   {
/*     */     protected KeySetIterator(AbstractLinkedMap<K, ?> parent) {
/* 446 */       super((AbstractLinkedMap)parent);
/*     */     }
/*     */     
/*     */     public K next() {
/* 450 */       return nextEntry().getKey();
/*     */     }
/*     */     
/*     */     public K previous() {
/* 454 */       return previousEntry().getKey();
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
/*     */   protected Iterator<V> createValuesIterator() {
/* 467 */     if (size() == 0) {
/* 468 */       return (Iterator<V>)EmptyOrderedIterator.emptyOrderedIterator();
/*     */     }
/* 470 */     return (Iterator<V>)new ValuesIterator<V>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class ValuesIterator<V>
/*     */     extends LinkIterator<Object, V>
/*     */     implements OrderedIterator<V>, ResettableIterator<V>
/*     */   {
/*     */     protected ValuesIterator(AbstractLinkedMap<?, V> parent) {
/* 481 */       super((AbstractLinkedMap)parent);
/*     */     }
/*     */     
/*     */     public V next() {
/* 485 */       return nextEntry().getValue();
/*     */     }
/*     */     
/*     */     public V previous() {
/* 489 */       return previousEntry().getValue();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class LinkEntry<K, V>
/*     */     extends AbstractHashedMap.HashEntry<K, V>
/*     */   {
/*     */     protected LinkEntry<K, V> before;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected LinkEntry<K, V> after;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected LinkEntry(AbstractHashedMap.HashEntry<K, V> next, int hashCode, Object key, V value) {
/* 517 */       super(next, hashCode, key, value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static abstract class LinkIterator<K, V>
/*     */   {
/*     */     protected final AbstractLinkedMap<K, V> parent;
/*     */ 
/*     */     
/*     */     protected AbstractLinkedMap.LinkEntry<K, V> last;
/*     */ 
/*     */     
/*     */     protected AbstractLinkedMap.LinkEntry<K, V> next;
/*     */     
/*     */     protected int expectedModCount;
/*     */ 
/*     */     
/*     */     protected LinkIterator(AbstractLinkedMap<K, V> parent) {
/* 537 */       this.parent = parent;
/* 538 */       this.next = parent.header.after;
/* 539 */       this.expectedModCount = parent.modCount;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 543 */       return (this.next != this.parent.header);
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 547 */       return (this.next.before != this.parent.header);
/*     */     }
/*     */     
/*     */     protected AbstractLinkedMap.LinkEntry<K, V> nextEntry() {
/* 551 */       if (this.parent.modCount != this.expectedModCount) {
/* 552 */         throw new ConcurrentModificationException();
/*     */       }
/* 554 */       if (this.next == this.parent.header) {
/* 555 */         throw new NoSuchElementException("No next() entry in the iteration");
/*     */       }
/* 557 */       this.last = this.next;
/* 558 */       this.next = this.next.after;
/* 559 */       return this.last;
/*     */     }
/*     */     
/*     */     protected AbstractLinkedMap.LinkEntry<K, V> previousEntry() {
/* 563 */       if (this.parent.modCount != this.expectedModCount) {
/* 564 */         throw new ConcurrentModificationException();
/*     */       }
/* 566 */       AbstractLinkedMap.LinkEntry<K, V> previous = this.next.before;
/* 567 */       if (previous == this.parent.header) {
/* 568 */         throw new NoSuchElementException("No previous() entry in the iteration");
/*     */       }
/* 570 */       this.next = previous;
/* 571 */       this.last = previous;
/* 572 */       return this.last;
/*     */     }
/*     */     
/*     */     protected AbstractLinkedMap.LinkEntry<K, V> currentEntry() {
/* 576 */       return this.last;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 580 */       if (this.last == null) {
/* 581 */         throw new IllegalStateException("remove() can only be called once after next()");
/*     */       }
/* 583 */       if (this.parent.modCount != this.expectedModCount) {
/* 584 */         throw new ConcurrentModificationException();
/*     */       }
/* 586 */       this.parent.remove(this.last.getKey());
/* 587 */       this.last = null;
/* 588 */       this.expectedModCount = this.parent.modCount;
/*     */     }
/*     */     
/*     */     public void reset() {
/* 592 */       this.last = null;
/* 593 */       this.next = this.parent.header.after;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 598 */       if (this.last != null) {
/* 599 */         return "Iterator[" + this.last.getKey() + "=" + this.last.getValue() + "]";
/*     */       }
/* 601 */       return "Iterator[]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\AbstractLinkedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */