/*     */ package org.apache.commons.collections4.bidimap;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.BidiMap;
/*     */ import org.apache.commons.collections4.MapIterator;
/*     */ import org.apache.commons.collections4.ResettableIterator;
/*     */ import org.apache.commons.collections4.collection.AbstractCollectionDecorator;
/*     */ import org.apache.commons.collections4.iterators.AbstractIteratorDecorator;
/*     */ import org.apache.commons.collections4.keyvalue.AbstractMapEntryDecorator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDualBidiMap<K, V>
/*     */   implements BidiMap<K, V>
/*     */ {
/*     */   transient Map<K, V> normalMap;
/*     */   transient Map<V, K> reverseMap;
/*  57 */   transient BidiMap<V, K> inverseBidiMap = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   transient Set<K> keySet = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   transient Set<V> values = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   transient Set<Map.Entry<K, V>> entrySet = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractDualBidiMap(Map<K, V> normalMap, Map<V, K> reverseMap) {
/* 101 */     this.normalMap = normalMap;
/* 102 */     this.reverseMap = reverseMap;
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
/*     */   protected AbstractDualBidiMap(Map<K, V> normalMap, Map<V, K> reverseMap, BidiMap<V, K> inverseBidiMap) {
/* 116 */     this.normalMap = normalMap;
/* 117 */     this.reverseMap = reverseMap;
/* 118 */     this.inverseBidiMap = inverseBidiMap;
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
/*     */   public V get(Object key) {
/* 136 */     return this.normalMap.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 141 */     return this.normalMap.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 146 */     return this.normalMap.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 151 */     return this.normalMap.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 156 */     return this.normalMap.equals(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 161 */     return this.normalMap.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 166 */     return this.normalMap.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/* 174 */     if (this.normalMap.containsKey(key)) {
/* 175 */       this.reverseMap.remove(this.normalMap.get(key));
/*     */     }
/* 177 */     if (this.reverseMap.containsKey(value)) {
/* 178 */       this.normalMap.remove(this.reverseMap.get(value));
/*     */     }
/* 180 */     V obj = this.normalMap.put(key, value);
/* 181 */     this.reverseMap.put(value, key);
/* 182 */     return obj;
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/* 187 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 188 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/* 194 */     V value = null;
/* 195 */     if (this.normalMap.containsKey(key)) {
/* 196 */       value = this.normalMap.remove(key);
/* 197 */       this.reverseMap.remove(value);
/*     */     } 
/* 199 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 204 */     this.normalMap.clear();
/* 205 */     this.reverseMap.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 210 */     return this.reverseMap.containsKey(value);
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
/*     */   public MapIterator<K, V> mapIterator() {
/* 228 */     return new BidiMapIterator<K, V>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public K getKey(Object value) {
/* 233 */     return this.reverseMap.get(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public K removeValue(Object value) {
/* 238 */     K key = null;
/* 239 */     if (this.reverseMap.containsKey(value)) {
/* 240 */       key = this.reverseMap.remove(value);
/* 241 */       this.normalMap.remove(key);
/*     */     } 
/* 243 */     return key;
/*     */   }
/*     */ 
/*     */   
/*     */   public BidiMap<V, K> inverseBidiMap() {
/* 248 */     if (this.inverseBidiMap == null) {
/* 249 */       this.inverseBidiMap = createBidiMap(this.reverseMap, this.normalMap, this);
/*     */     }
/* 251 */     return this.inverseBidiMap;
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
/*     */   public Set<K> keySet() {
/* 265 */     if (this.keySet == null) {
/* 266 */       this.keySet = new KeySet<K>(this);
/*     */     }
/* 268 */     return this.keySet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Iterator<K> createKeySetIterator(Iterator<K> iterator) {
/* 279 */     return (Iterator<K>)new KeySetIterator<K>(iterator, this);
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
/*     */   public Set<V> values() {
/* 291 */     if (this.values == null) {
/* 292 */       this.values = new Values<V>(this);
/*     */     }
/* 294 */     return this.values;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Iterator<V> createValuesIterator(Iterator<V> iterator) {
/* 305 */     return (Iterator<V>)new ValuesIterator<V>(iterator, this);
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
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 321 */     if (this.entrySet == null) {
/* 322 */       this.entrySet = new EntrySet<K, V>(this);
/*     */     }
/* 324 */     return this.entrySet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Iterator<Map.Entry<K, V>> createEntrySetIterator(Iterator<Map.Entry<K, V>> iterator) {
/* 335 */     return (Iterator)new EntrySetIterator<K, V>(iterator, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractDualBidiMap() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract BidiMap<V, K> createBidiMap(Map<V, K> paramMap, Map<K, V> paramMap1, BidiMap<K, V> paramBidiMap);
/*     */ 
/*     */ 
/*     */   
/*     */   protected static abstract class View<K, V, E>
/*     */     extends AbstractCollectionDecorator<E>
/*     */   {
/*     */     private static final long serialVersionUID = 4621510560119690639L;
/*     */     
/*     */     protected final AbstractDualBidiMap<K, V> parent;
/*     */ 
/*     */     
/*     */     protected View(Collection<E> coll, AbstractDualBidiMap<K, V> parent) {
/* 357 */       super(coll);
/* 358 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 363 */       return (object == this || decorated().equals(object));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 368 */       return decorated().hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> coll) {
/* 373 */       if (this.parent.isEmpty() || coll.isEmpty()) {
/* 374 */         return false;
/*     */       }
/* 376 */       boolean modified = false;
/* 377 */       Iterator<?> it = coll.iterator();
/* 378 */       while (it.hasNext()) {
/* 379 */         modified |= remove(it.next());
/*     */       }
/* 381 */       return modified;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> coll) {
/* 395 */       if (this.parent.isEmpty()) {
/* 396 */         return false;
/*     */       }
/* 398 */       if (coll.isEmpty()) {
/* 399 */         this.parent.clear();
/* 400 */         return true;
/*     */       } 
/* 402 */       boolean modified = false;
/* 403 */       Iterator<E> it = iterator();
/* 404 */       while (it.hasNext()) {
/* 405 */         if (!coll.contains(it.next())) {
/* 406 */           it.remove();
/* 407 */           modified = true;
/*     */         } 
/*     */       } 
/* 410 */       return modified;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 415 */       this.parent.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class KeySet<K>
/*     */     extends View<K, Object, K>
/*     */     implements Set<K>
/*     */   {
/*     */     private static final long serialVersionUID = -7107935777385040694L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected KeySet(AbstractDualBidiMap<K, ?> parent) {
/* 435 */       super(parent.normalMap.keySet(), (AbstractDualBidiMap)parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<K> iterator() {
/* 440 */       return this.parent.createKeySetIterator(super.iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object key) {
/* 445 */       return this.parent.normalMap.containsKey(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object key) {
/* 450 */       if (this.parent.normalMap.containsKey(key)) {
/* 451 */         Object value = this.parent.normalMap.remove(key);
/* 452 */         this.parent.reverseMap.remove(value);
/* 453 */         return true;
/*     */       } 
/* 455 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class KeySetIterator<K>
/*     */     extends AbstractIteratorDecorator<K>
/*     */   {
/*     */     protected final AbstractDualBidiMap<K, ?> parent;
/*     */ 
/*     */     
/* 468 */     protected K lastKey = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean canRemove = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected KeySetIterator(Iterator<K> iterator, AbstractDualBidiMap<K, ?> parent) {
/* 479 */       super(iterator);
/* 480 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public K next() {
/* 485 */       this.lastKey = (K)super.next();
/* 486 */       this.canRemove = true;
/* 487 */       return this.lastKey;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 492 */       if (!this.canRemove) {
/* 493 */         throw new IllegalStateException("Iterator remove() can only be called once after next()");
/*     */       }
/* 495 */       Object value = this.parent.normalMap.get(this.lastKey);
/* 496 */       super.remove();
/* 497 */       this.parent.reverseMap.remove(value);
/* 498 */       this.lastKey = null;
/* 499 */       this.canRemove = false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class Values<V>
/*     */     extends View<Object, V, V>
/*     */     implements Set<V>
/*     */   {
/*     */     private static final long serialVersionUID = 4023777119829639864L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Values(AbstractDualBidiMap<?, V> parent) {
/* 519 */       super(parent.normalMap.values(), (AbstractDualBidiMap)parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<V> iterator() {
/* 524 */       return this.parent.createValuesIterator(super.iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object value) {
/* 529 */       return this.parent.reverseMap.containsKey(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object value) {
/* 534 */       if (this.parent.reverseMap.containsKey(value)) {
/* 535 */         Object key = this.parent.reverseMap.remove(value);
/* 536 */         this.parent.normalMap.remove(key);
/* 537 */         return true;
/*     */       } 
/* 539 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class ValuesIterator<V>
/*     */     extends AbstractIteratorDecorator<V>
/*     */   {
/*     */     protected final AbstractDualBidiMap<Object, V> parent;
/*     */ 
/*     */     
/* 552 */     protected V lastValue = null;
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
/*     */     protected ValuesIterator(Iterator<V> iterator, AbstractDualBidiMap<?, V> parent) {
/* 564 */       super(iterator);
/* 565 */       this.parent = (AbstractDualBidiMap)parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public V next() {
/* 570 */       this.lastValue = (V)super.next();
/* 571 */       this.canRemove = true;
/* 572 */       return this.lastValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 577 */       if (!this.canRemove) {
/* 578 */         throw new IllegalStateException("Iterator remove() can only be called once after next()");
/*     */       }
/* 580 */       super.remove();
/* 581 */       this.parent.reverseMap.remove(this.lastValue);
/* 582 */       this.lastValue = null;
/* 583 */       this.canRemove = false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class EntrySet<K, V>
/*     */     extends View<K, V, Map.Entry<K, V>>
/*     */     implements Set<Map.Entry<K, V>>
/*     */   {
/*     */     private static final long serialVersionUID = 4040410962603292348L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected EntrySet(AbstractDualBidiMap<K, V> parent) {
/* 602 */       super(parent.normalMap.entrySet(), parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 607 */       return this.parent.createEntrySetIterator(super.iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object obj) {
/* 612 */       if (!(obj instanceof Map.Entry)) {
/* 613 */         return false;
/*     */       }
/* 615 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 616 */       Object key = entry.getKey();
/* 617 */       if (this.parent.containsKey(key)) {
/* 618 */         V value = this.parent.normalMap.get(key);
/* 619 */         if ((value == null) ? (entry.getValue() == null) : value.equals(entry.getValue())) {
/* 620 */           this.parent.normalMap.remove(key);
/* 621 */           this.parent.reverseMap.remove(value);
/* 622 */           return true;
/*     */         } 
/*     */       } 
/* 625 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class EntrySetIterator<K, V>
/*     */     extends AbstractIteratorDecorator<Map.Entry<K, V>>
/*     */   {
/*     */     protected final AbstractDualBidiMap<K, V> parent;
/*     */ 
/*     */     
/* 638 */     protected Map.Entry<K, V> last = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean canRemove = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected EntrySetIterator(Iterator<Map.Entry<K, V>> iterator, AbstractDualBidiMap<K, V> parent) {
/* 649 */       super(iterator);
/* 650 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map.Entry<K, V> next() {
/* 655 */       this.last = (Map.Entry<K, V>)new AbstractDualBidiMap.MapEntry<K, V>((Map.Entry<K, V>)super.next(), this.parent);
/* 656 */       this.canRemove = true;
/* 657 */       return this.last;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 662 */       if (!this.canRemove) {
/* 663 */         throw new IllegalStateException("Iterator remove() can only be called once after next()");
/*     */       }
/*     */       
/* 666 */       Object value = this.last.getValue();
/* 667 */       super.remove();
/* 668 */       this.parent.reverseMap.remove(value);
/* 669 */       this.last = null;
/* 670 */       this.canRemove = false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class MapEntry<K, V>
/*     */     extends AbstractMapEntryDecorator<K, V>
/*     */   {
/*     */     protected final AbstractDualBidiMap<K, V> parent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected MapEntry(Map.Entry<K, V> entry, AbstractDualBidiMap<K, V> parent) {
/* 688 */       super(entry);
/* 689 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public V setValue(V value) {
/* 694 */       K key = (K)getKey();
/* 695 */       if (this.parent.reverseMap.containsKey(value) && this.parent.reverseMap.get(value) != key)
/*     */       {
/* 697 */         throw new IllegalArgumentException("Cannot use setValue() when the object being set is already in the map");
/*     */       }
/*     */       
/* 700 */       this.parent.put(key, value);
/* 701 */       return (V)super.setValue(value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class BidiMapIterator<K, V>
/*     */     implements MapIterator<K, V>, ResettableIterator<K>
/*     */   {
/*     */     protected final AbstractDualBidiMap<K, V> parent;
/*     */ 
/*     */     
/*     */     protected Iterator<Map.Entry<K, V>> iterator;
/*     */ 
/*     */     
/* 717 */     protected Map.Entry<K, V> last = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean canRemove = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected BidiMapIterator(AbstractDualBidiMap<K, V> parent) {
/* 728 */       this.parent = parent;
/* 729 */       this.iterator = parent.normalMap.entrySet().iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 734 */       return this.iterator.hasNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public K next() {
/* 739 */       this.last = this.iterator.next();
/* 740 */       this.canRemove = true;
/* 741 */       return this.last.getKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 746 */       if (!this.canRemove) {
/* 747 */         throw new IllegalStateException("Iterator remove() can only be called once after next()");
/*     */       }
/*     */       
/* 750 */       V value = this.last.getValue();
/* 751 */       this.iterator.remove();
/* 752 */       this.parent.reverseMap.remove(value);
/* 753 */       this.last = null;
/* 754 */       this.canRemove = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public K getKey() {
/* 759 */       if (this.last == null) {
/* 760 */         throw new IllegalStateException("Iterator getKey() can only be called after next() and before remove()");
/*     */       }
/*     */       
/* 763 */       return this.last.getKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/* 768 */       if (this.last == null) {
/* 769 */         throw new IllegalStateException("Iterator getValue() can only be called after next() and before remove()");
/*     */       }
/*     */       
/* 772 */       return this.last.getValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public V setValue(V value) {
/* 777 */       if (this.last == null) {
/* 778 */         throw new IllegalStateException("Iterator setValue() can only be called after next() and before remove()");
/*     */       }
/*     */       
/* 781 */       if (this.parent.reverseMap.containsKey(value) && this.parent.reverseMap.get(value) != this.last.getKey())
/*     */       {
/* 783 */         throw new IllegalArgumentException("Cannot use setValue() when the object being set is already in the map");
/*     */       }
/*     */       
/* 786 */       return this.parent.put(this.last.getKey(), value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void reset() {
/* 791 */       this.iterator = this.parent.normalMap.entrySet().iterator();
/* 792 */       this.last = null;
/* 793 */       this.canRemove = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 798 */       if (this.last != null) {
/* 799 */         return "MapIterator[" + getKey() + "=" + getValue() + "]";
/*     */       }
/* 801 */       return "MapIterator[]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\bidimap\AbstractDualBidiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */