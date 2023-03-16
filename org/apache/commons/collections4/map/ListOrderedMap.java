/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.MapIterator;
/*     */ import org.apache.commons.collections4.OrderedMap;
/*     */ import org.apache.commons.collections4.OrderedMapIterator;
/*     */ import org.apache.commons.collections4.ResettableIterator;
/*     */ import org.apache.commons.collections4.iterators.AbstractUntypedIteratorDecorator;
/*     */ import org.apache.commons.collections4.keyvalue.AbstractMapEntry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ListOrderedMap<K, V>
/*     */   extends AbstractMapDecorator<K, V>
/*     */   implements OrderedMap<K, V>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2728177751851003750L;
/*  86 */   private final List<K> insertOrder = new ArrayList<K>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ListOrderedMap<K, V> listOrderedMap(Map<K, V> map) {
/* 101 */     return new ListOrderedMap<K, V>(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListOrderedMap() {
/* 112 */     this(new HashMap<K, V>());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ListOrderedMap(Map<K, V> map) {
/* 122 */     super(map);
/* 123 */     this.insertOrder.addAll(decorated().keySet());
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
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 135 */     out.defaultWriteObject();
/* 136 */     out.writeObject(this.map);
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
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 149 */     in.defaultReadObject();
/* 150 */     this.map = (Map<K, V>)in.readObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OrderedMapIterator<K, V> mapIterator() {
/* 157 */     return new ListOrderedMapIterator<K, V>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K firstKey() {
/* 167 */     if (size() == 0) {
/* 168 */       throw new NoSuchElementException("Map is empty");
/*     */     }
/* 170 */     return this.insertOrder.get(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K lastKey() {
/* 180 */     if (size() == 0) {
/* 181 */       throw new NoSuchElementException("Map is empty");
/*     */     }
/* 183 */     return this.insertOrder.get(size() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K nextKey(Object key) {
/* 194 */     int index = this.insertOrder.indexOf(key);
/* 195 */     if (index >= 0 && index < size() - 1) {
/* 196 */       return this.insertOrder.get(index + 1);
/*     */     }
/* 198 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K previousKey(Object key) {
/* 209 */     int index = this.insertOrder.indexOf(key);
/* 210 */     if (index > 0) {
/* 211 */       return this.insertOrder.get(index - 1);
/*     */     }
/* 213 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/* 219 */     if (decorated().containsKey(key))
/*     */     {
/* 221 */       return decorated().put(key, value);
/*     */     }
/*     */     
/* 224 */     V result = decorated().put(key, value);
/* 225 */     this.insertOrder.add(key);
/* 226 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/* 231 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 232 */       put(entry.getKey(), entry.getValue());
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
/*     */   public void putAll(int index, Map<? extends K, ? extends V> map) {
/* 245 */     if (index < 0 || index > this.insertOrder.size()) {
/* 246 */       throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.insertOrder.size());
/*     */     }
/* 248 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 249 */       K key = entry.getKey();
/* 250 */       boolean contains = containsKey(key);
/*     */ 
/*     */       
/* 253 */       put(index, entry.getKey(), entry.getValue());
/* 254 */       if (!contains) {
/*     */         
/* 256 */         index++;
/*     */         continue;
/*     */       } 
/* 259 */       index = indexOf(entry.getKey()) + 1;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/* 266 */     V result = null;
/* 267 */     if (decorated().containsKey(key)) {
/* 268 */       result = decorated().remove(key);
/* 269 */       this.insertOrder.remove(key);
/*     */     } 
/* 271 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 276 */     decorated().clear();
/* 277 */     this.insertOrder.clear();
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
/* 291 */     return new KeySetView<K>(this);
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
/*     */   public List<K> keyList() {
/* 305 */     return UnmodifiableList.unmodifiableList(this.insertOrder);
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
/*     */   public Collection<V> values() {
/* 321 */     return new ValuesView<V>(this);
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
/*     */   public List<V> valueList() {
/* 335 */     return new ValuesView<V>(this);
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
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 347 */     return new EntrySetView<K, V>(this, this.insertOrder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 358 */     if (isEmpty()) {
/* 359 */       return "{}";
/*     */     }
/* 361 */     StringBuilder buf = new StringBuilder();
/* 362 */     buf.append('{');
/* 363 */     boolean first = true;
/* 364 */     for (Map.Entry<K, V> entry : entrySet()) {
/* 365 */       K key = entry.getKey();
/* 366 */       V value = entry.getValue();
/* 367 */       if (first) {
/* 368 */         first = false;
/*     */       } else {
/* 370 */         buf.append(", ");
/*     */       } 
/* 372 */       buf.append((key == this) ? "(this Map)" : key);
/* 373 */       buf.append('=');
/* 374 */       buf.append((value == this) ? "(this Map)" : value);
/*     */     } 
/* 376 */     buf.append('}');
/* 377 */     return buf.toString();
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
/*     */   public K get(int index) {
/* 389 */     return this.insertOrder.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V getValue(int index) {
/* 400 */     return get(this.insertOrder.get(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(Object key) {
/* 410 */     return this.insertOrder.indexOf(key);
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
/*     */   public V setValue(int index, V value) {
/* 423 */     K key = this.insertOrder.get(index);
/* 424 */     return put(key, value);
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
/*     */   public V put(int index, K key, V value) {
/* 447 */     if (index < 0 || index > this.insertOrder.size()) {
/* 448 */       throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.insertOrder.size());
/*     */     }
/*     */     
/* 451 */     Map<K, V> m = decorated();
/* 452 */     if (m.containsKey(key)) {
/* 453 */       V result = m.remove(key);
/* 454 */       int pos = this.insertOrder.indexOf(key);
/* 455 */       this.insertOrder.remove(pos);
/* 456 */       if (pos < index) {
/* 457 */         index--;
/*     */       }
/* 459 */       this.insertOrder.add(index, key);
/* 460 */       m.put(key, value);
/* 461 */       return result;
/*     */     } 
/* 463 */     this.insertOrder.add(index, key);
/* 464 */     m.put(key, value);
/* 465 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V remove(int index) {
/* 476 */     return remove(get(index));
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
/*     */   public List<K> asList() {
/* 497 */     return keyList();
/*     */   }
/*     */ 
/*     */   
/*     */   static class ValuesView<V>
/*     */     extends AbstractList<V>
/*     */   {
/*     */     private final ListOrderedMap<Object, V> parent;
/*     */     
/*     */     ValuesView(ListOrderedMap<?, V> parent) {
/* 507 */       this.parent = (ListOrderedMap)parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 512 */       return this.parent.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object value) {
/* 517 */       return this.parent.containsValue(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 522 */       this.parent.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<V> iterator() {
/* 527 */       return (Iterator)new AbstractUntypedIteratorDecorator<Map.Entry<Object, V>, V>(this.parent.entrySet().iterator()) {
/*     */           public V next() {
/* 529 */             return (V)((Map.Entry)getIterator().next()).getValue();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(int index) {
/* 536 */       return this.parent.getValue(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public V set(int index, V value) {
/* 541 */       return this.parent.setValue(index, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(int index) {
/* 546 */       return this.parent.remove(index);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class KeySetView<K>
/*     */     extends AbstractSet<K>
/*     */   {
/*     */     private final ListOrderedMap<K, Object> parent;
/*     */     
/*     */     KeySetView(ListOrderedMap<K, ?> parent) {
/* 557 */       this.parent = (ListOrderedMap)parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 562 */       return this.parent.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object value) {
/* 567 */       return this.parent.containsKey(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 572 */       this.parent.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<K> iterator() {
/* 577 */       return (Iterator)new AbstractUntypedIteratorDecorator<Map.Entry<K, Object>, K>(this.parent.entrySet().iterator()) {
/*     */           public K next() {
/* 579 */             return (K)((Map.Entry)getIterator().next()).getKey();
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   static class EntrySetView<K, V>
/*     */     extends AbstractSet<Map.Entry<K, V>>
/*     */   {
/*     */     private final ListOrderedMap<K, V> parent;
/*     */     private final List<K> insertOrder;
/*     */     private Set<Map.Entry<K, V>> entrySet;
/*     */     
/*     */     public EntrySetView(ListOrderedMap<K, V> parent, List<K> insertOrder) {
/* 593 */       this.parent = parent;
/* 594 */       this.insertOrder = insertOrder;
/*     */     }
/*     */     
/*     */     private Set<Map.Entry<K, V>> getEntrySet() {
/* 598 */       if (this.entrySet == null) {
/* 599 */         this.entrySet = this.parent.decorated().entrySet();
/*     */       }
/* 601 */       return this.entrySet;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 606 */       return this.parent.size();
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 610 */       return this.parent.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object obj) {
/* 615 */       return getEntrySet().contains(obj);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsAll(Collection<?> coll) {
/* 620 */       return getEntrySet().containsAll(coll);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean remove(Object obj) {
/* 626 */       if (!(obj instanceof Map.Entry)) {
/* 627 */         return false;
/*     */       }
/* 629 */       if (getEntrySet().contains(obj)) {
/* 630 */         Object key = ((Map.Entry)obj).getKey();
/* 631 */         this.parent.remove(key);
/* 632 */         return true;
/*     */       } 
/* 634 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 639 */       this.parent.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 644 */       if (obj == this) {
/* 645 */         return true;
/*     */       }
/* 647 */       return getEntrySet().equals(obj);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 652 */       return getEntrySet().hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 657 */       return getEntrySet().toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 662 */       return (Iterator)new ListOrderedMap.ListOrderedIterator<K, V>(this.parent, this.insertOrder);
/*     */     }
/*     */   }
/*     */   
/*     */   static class ListOrderedIterator<K, V>
/*     */     extends AbstractUntypedIteratorDecorator<K, Map.Entry<K, V>> {
/*     */     private final ListOrderedMap<K, V> parent;
/* 669 */     private K last = null;
/*     */     
/*     */     ListOrderedIterator(ListOrderedMap<K, V> parent, List<K> insertOrder) {
/* 672 */       super(insertOrder.iterator());
/* 673 */       this.parent = parent;
/*     */     }
/*     */     
/*     */     public Map.Entry<K, V> next() {
/* 677 */       this.last = getIterator().next();
/* 678 */       return (Map.Entry<K, V>)new ListOrderedMap.ListOrderedMapEntry<K, V>(this.parent, this.last);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 683 */       super.remove();
/* 684 */       this.parent.decorated().remove(this.last);
/*     */     }
/*     */   }
/*     */   
/*     */   static class ListOrderedMapEntry<K, V>
/*     */     extends AbstractMapEntry<K, V> {
/*     */     private final ListOrderedMap<K, V> parent;
/*     */     
/*     */     ListOrderedMapEntry(ListOrderedMap<K, V> parent, K key) {
/* 693 */       super(key, null);
/* 694 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/* 699 */       return this.parent.get(getKey());
/*     */     }
/*     */ 
/*     */     
/*     */     public V setValue(V value) {
/* 704 */       return this.parent.decorated().put(getKey(), value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class ListOrderedMapIterator<K, V>
/*     */     implements OrderedMapIterator<K, V>, ResettableIterator<K> {
/*     */     private final ListOrderedMap<K, V> parent;
/*     */     private ListIterator<K> iterator;
/* 712 */     private K last = null;
/*     */     
/*     */     private boolean readable = false;
/*     */     
/*     */     ListOrderedMapIterator(ListOrderedMap<K, V> parent) {
/* 717 */       this.parent = parent;
/* 718 */       this.iterator = parent.insertOrder.listIterator();
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 722 */       return this.iterator.hasNext();
/*     */     }
/*     */     
/*     */     public K next() {
/* 726 */       this.last = this.iterator.next();
/* 727 */       this.readable = true;
/* 728 */       return this.last;
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 732 */       return this.iterator.hasPrevious();
/*     */     }
/*     */     
/*     */     public K previous() {
/* 736 */       this.last = this.iterator.previous();
/* 737 */       this.readable = true;
/* 738 */       return this.last;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 742 */       if (!this.readable) {
/* 743 */         throw new IllegalStateException("remove() can only be called once after next()");
/*     */       }
/* 745 */       this.iterator.remove();
/* 746 */       this.parent.map.remove(this.last);
/* 747 */       this.readable = false;
/*     */     }
/*     */     
/*     */     public K getKey() {
/* 751 */       if (!this.readable) {
/* 752 */         throw new IllegalStateException("getKey() can only be called after next() and before remove()");
/*     */       }
/* 754 */       return this.last;
/*     */     }
/*     */     
/*     */     public V getValue() {
/* 758 */       if (!this.readable) {
/* 759 */         throw new IllegalStateException("getValue() can only be called after next() and before remove()");
/*     */       }
/* 761 */       return this.parent.get(this.last);
/*     */     }
/*     */     
/*     */     public V setValue(V value) {
/* 765 */       if (!this.readable) {
/* 766 */         throw new IllegalStateException("setValue() can only be called after next() and before remove()");
/*     */       }
/* 768 */       return this.parent.map.put(this.last, value);
/*     */     }
/*     */     
/*     */     public void reset() {
/* 772 */       this.iterator = this.parent.insertOrder.listIterator();
/* 773 */       this.last = null;
/* 774 */       this.readable = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 779 */       if (this.readable == true) {
/* 780 */         return "Iterator[" + getKey() + "=" + getValue() + "]";
/*     */       }
/* 782 */       return "Iterator[]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\ListOrderedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */