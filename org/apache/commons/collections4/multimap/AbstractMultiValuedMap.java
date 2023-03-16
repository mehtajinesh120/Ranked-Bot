/*     */ package org.apache.commons.collections4.multimap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.CollectionUtils;
/*     */ import org.apache.commons.collections4.IteratorUtils;
/*     */ import org.apache.commons.collections4.MapIterator;
/*     */ import org.apache.commons.collections4.MultiSet;
/*     */ import org.apache.commons.collections4.MultiValuedMap;
/*     */ import org.apache.commons.collections4.Transformer;
/*     */ import org.apache.commons.collections4.iterators.AbstractIteratorDecorator;
/*     */ import org.apache.commons.collections4.iterators.EmptyMapIterator;
/*     */ import org.apache.commons.collections4.iterators.IteratorChain;
/*     */ import org.apache.commons.collections4.iterators.LazyIteratorChain;
/*     */ import org.apache.commons.collections4.iterators.TransformIterator;
/*     */ import org.apache.commons.collections4.keyvalue.AbstractMapEntry;
/*     */ import org.apache.commons.collections4.keyvalue.UnmodifiableMapEntry;
/*     */ import org.apache.commons.collections4.multiset.AbstractMultiSet;
/*     */ import org.apache.commons.collections4.multiset.UnmodifiableMultiSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMultiValuedMap<K, V>
/*     */   implements MultiValuedMap<K, V>
/*     */ {
/*     */   private transient Collection<V> valuesView;
/*     */   private transient EntryValues entryValuesView;
/*     */   private transient MultiSet<K> keysMultiSetView;
/*     */   private transient AsMap asMapView;
/*     */   private transient Map<K, Collection<V>> map;
/*     */   
/*     */   protected AbstractMultiValuedMap() {}
/*     */   
/*     */   protected AbstractMultiValuedMap(Map<K, ? extends Collection<V>> map) {
/*  89 */     if (map == null) {
/*  90 */       throw new NullPointerException("Map must not be null.");
/*     */     }
/*  92 */     this.map = (Map)map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<K, ? extends Collection<V>> getMap() {
/* 102 */     return this.map;
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
/*     */   protected void setMap(Map<K, ? extends Collection<V>> map) {
/* 114 */     this.map = (Map)map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 122 */     return getMap().containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 127 */     return values().contains(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsMapping(Object key, Object value) {
/* 132 */     Collection<V> coll = getMap().get(key);
/* 133 */     return (coll != null && coll.contains(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<Map.Entry<K, V>> entries() {
/* 138 */     return (this.entryValuesView != null) ? this.entryValuesView : (this.entryValuesView = new EntryValues());
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
/*     */   public Collection<V> get(K key) {
/* 150 */     return wrappedCollection(key);
/*     */   }
/*     */   
/*     */   Collection<V> wrappedCollection(K key) {
/* 154 */     return new WrappedCollection(key);
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
/*     */   public Collection<V> remove(Object key) {
/* 168 */     return CollectionUtils.emptyIfNull(getMap().remove(key));
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
/*     */   public boolean removeMapping(Object key, Object value) {
/* 186 */     Collection<V> coll = getMap().get(key);
/* 187 */     if (coll == null) {
/* 188 */       return false;
/*     */     }
/* 190 */     boolean changed = coll.remove(value);
/* 191 */     if (coll.isEmpty()) {
/* 192 */       getMap().remove(key);
/*     */     }
/* 194 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 199 */     return getMap().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 204 */     return getMap().keySet();
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
/*     */   public int size() {
/* 220 */     int size = 0;
/* 221 */     for (Collection<V> col : getMap().values()) {
/* 222 */       size += col.size();
/*     */     }
/* 224 */     return size;
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
/*     */   public Collection<V> values() {
/* 236 */     Collection<V> vs = this.valuesView;
/* 237 */     return (vs != null) ? vs : (this.valuesView = new Values());
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 242 */     getMap().clear();
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
/*     */   public boolean put(K key, V value) {
/* 257 */     Collection<V> coll = getMap().get(key);
/* 258 */     if (coll == null) {
/* 259 */       coll = createCollection();
/* 260 */       if (coll.add(value)) {
/* 261 */         this.map.put(key, coll);
/* 262 */         return true;
/*     */       } 
/* 264 */       return false;
/*     */     } 
/*     */     
/* 267 */     return coll.add(value);
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
/*     */   public boolean putAll(Map<? extends K, ? extends V> map) {
/* 285 */     if (map == null) {
/* 286 */       throw new NullPointerException("Map must not be null.");
/*     */     }
/* 288 */     boolean changed = false;
/* 289 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 290 */       changed |= put(entry.getKey(), entry.getValue());
/*     */     }
/* 292 */     return changed;
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
/*     */   public boolean putAll(MultiValuedMap<? extends K, ? extends V> map) {
/* 309 */     if (map == null) {
/* 310 */       throw new NullPointerException("Map must not be null.");
/*     */     }
/* 312 */     boolean changed = false;
/* 313 */     for (Map.Entry<? extends K, ? extends V> entry : (Iterable<Map.Entry<? extends K, ? extends V>>)map.entries()) {
/* 314 */       changed |= put(entry.getKey(), entry.getValue());
/*     */     }
/* 316 */     return changed;
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
/*     */   public MultiSet<K> keys() {
/* 331 */     if (this.keysMultiSetView == null) {
/* 332 */       this.keysMultiSetView = UnmodifiableMultiSet.unmodifiableMultiSet((MultiSet)new KeysMultiSet());
/*     */     }
/* 334 */     return this.keysMultiSetView;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<K, Collection<V>> asMap() {
/* 339 */     return (this.asMapView != null) ? this.asMapView : (this.asMapView = new AsMap(this.map));
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
/*     */   public boolean putAll(K key, Iterable<? extends V> values) {
/* 352 */     if (values == null) {
/* 353 */       throw new NullPointerException("Values must not be null.");
/*     */     }
/*     */     
/* 356 */     if (values instanceof Collection) {
/* 357 */       Collection<? extends V> valueCollection = (Collection<? extends V>)values;
/* 358 */       return (!valueCollection.isEmpty() && get(key).addAll(valueCollection));
/*     */     } 
/* 360 */     Iterator<? extends V> it = values.iterator();
/* 361 */     return (it.hasNext() && CollectionUtils.addAll(get(key), it));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MapIterator<K, V> mapIterator() {
/* 367 */     if (size() == 0) {
/* 368 */       return EmptyMapIterator.emptyMapIterator();
/*     */     }
/* 370 */     return new MultiValuedMapIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 375 */     if (this == obj) {
/* 376 */       return true;
/*     */     }
/* 378 */     if (obj instanceof MultiValuedMap) {
/* 379 */       return asMap().equals(((MultiValuedMap)obj).asMap());
/*     */     }
/* 381 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 386 */     return getMap().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 391 */     return getMap().toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class WrappedCollection
/*     */     implements Collection<V>
/*     */   {
/*     */     protected final K key;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public WrappedCollection(K key) {
/* 411 */       this.key = key;
/*     */     }
/*     */     
/*     */     protected Collection<V> getMapping() {
/* 415 */       return (Collection<V>)AbstractMultiValuedMap.this.getMap().get(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean add(V value) {
/* 420 */       Collection<V> coll = getMapping();
/* 421 */       if (coll == null) {
/* 422 */         coll = AbstractMultiValuedMap.this.createCollection();
/* 423 */         AbstractMultiValuedMap.this.map.put(this.key, coll);
/*     */       } 
/* 425 */       return coll.add(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection<? extends V> other) {
/* 430 */       Collection<V> coll = getMapping();
/* 431 */       if (coll == null) {
/* 432 */         coll = AbstractMultiValuedMap.this.createCollection();
/* 433 */         AbstractMultiValuedMap.this.map.put(this.key, coll);
/*     */       } 
/* 435 */       return coll.addAll(other);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 440 */       Collection<V> coll = getMapping();
/* 441 */       if (coll != null) {
/* 442 */         coll.clear();
/* 443 */         AbstractMultiValuedMap.this.remove(this.key);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterator<V> iterator() {
/* 450 */       Collection<V> coll = getMapping();
/* 451 */       if (coll == null) {
/* 452 */         return (Iterator<V>)IteratorUtils.EMPTY_ITERATOR;
/*     */       }
/* 454 */       return new AbstractMultiValuedMap.ValuesIterator(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 459 */       Collection<V> coll = getMapping();
/* 460 */       return (coll == null) ? 0 : coll.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object obj) {
/* 465 */       Collection<V> coll = getMapping();
/* 466 */       return (coll == null) ? false : coll.contains(obj);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsAll(Collection<?> other) {
/* 471 */       Collection<V> coll = getMapping();
/* 472 */       return (coll == null) ? false : coll.containsAll(other);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 477 */       Collection<V> coll = getMapping();
/* 478 */       return (coll == null) ? true : coll.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object item) {
/* 483 */       Collection<V> coll = getMapping();
/* 484 */       if (coll == null) {
/* 485 */         return false;
/*     */       }
/*     */       
/* 488 */       boolean result = coll.remove(item);
/* 489 */       if (coll.isEmpty()) {
/* 490 */         AbstractMultiValuedMap.this.remove(this.key);
/*     */       }
/* 492 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 497 */       Collection<V> coll = getMapping();
/* 498 */       if (coll == null) {
/* 499 */         return false;
/*     */       }
/*     */       
/* 502 */       boolean result = coll.removeAll(c);
/* 503 */       if (coll.isEmpty()) {
/* 504 */         AbstractMultiValuedMap.this.remove(this.key);
/*     */       }
/* 506 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 511 */       Collection<V> coll = getMapping();
/* 512 */       if (coll == null) {
/* 513 */         return false;
/*     */       }
/*     */       
/* 516 */       boolean result = coll.retainAll(c);
/* 517 */       if (coll.isEmpty()) {
/* 518 */         AbstractMultiValuedMap.this.remove(this.key);
/*     */       }
/* 520 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 525 */       Collection<V> coll = getMapping();
/* 526 */       if (coll == null) {
/* 527 */         return CollectionUtils.EMPTY_COLLECTION.toArray();
/*     */       }
/* 529 */       return coll.toArray();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] a) {
/* 535 */       Collection<V> coll = getMapping();
/* 536 */       if (coll == null) {
/* 537 */         return (T[])CollectionUtils.EMPTY_COLLECTION.toArray((Object[])a);
/*     */       }
/* 539 */       return coll.toArray(a);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 544 */       Collection<V> coll = getMapping();
/* 545 */       if (coll == null) {
/* 546 */         return CollectionUtils.EMPTY_COLLECTION.toString();
/*     */       }
/* 548 */       return coll.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class KeysMultiSet
/*     */     extends AbstractMultiSet<K>
/*     */   {
/*     */     private KeysMultiSet() {}
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 560 */       return AbstractMultiValuedMap.this.getMap().containsKey(o);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 565 */       return AbstractMultiValuedMap.this.getMap().isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 570 */       return AbstractMultiValuedMap.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     protected int uniqueElements() {
/* 575 */       return AbstractMultiValuedMap.this.getMap().size();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getCount(Object object) {
/* 580 */       int count = 0;
/* 581 */       Collection<V> col = (Collection<V>)AbstractMultiValuedMap.this.getMap().get(object);
/* 582 */       if (col != null) {
/* 583 */         count = col.size();
/*     */       }
/* 585 */       return count;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Iterator<MultiSet.Entry<K>> createEntrySetIterator() {
/* 590 */       MapEntryTransformer transformer = new MapEntryTransformer();
/* 591 */       return IteratorUtils.transformedIterator(AbstractMultiValuedMap.this.map.entrySet().iterator(), transformer);
/*     */     }
/*     */     
/*     */     private final class MapEntryTransformer implements Transformer<Map.Entry<K, Collection<V>>, MultiSet.Entry<K>> {
/*     */       private MapEntryTransformer() {}
/*     */       
/*     */       public MultiSet.Entry<K> transform(final Map.Entry<K, Collection<V>> mapEntry) {
/* 598 */         return (MultiSet.Entry<K>)new AbstractMultiSet.AbstractEntry<K>()
/*     */           {
/*     */             public K getElement() {
/* 601 */               return (K)mapEntry.getKey();
/*     */             }
/*     */ 
/*     */             
/*     */             public int getCount() {
/* 606 */               return ((Collection)mapEntry.getValue()).size();
/*     */             }
/*     */           };
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class EntryValues
/*     */     extends AbstractCollection<Map.Entry<K, V>>
/*     */   {
/*     */     private EntryValues() {}
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 620 */       return (Iterator<Map.Entry<K, V>>)new LazyIteratorChain<Map.Entry<K, V>>()
/*     */         {
/* 622 */           final Collection<K> keysCol = new ArrayList<K>(AbstractMultiValuedMap.this.getMap().keySet());
/* 623 */           final Iterator<K> keyIterator = this.keysCol.iterator();
/*     */ 
/*     */           
/*     */           protected Iterator<? extends Map.Entry<K, V>> nextIterator(int count) {
/* 627 */             if (!this.keyIterator.hasNext()) {
/* 628 */               return null;
/*     */             }
/* 630 */             final K key = this.keyIterator.next();
/* 631 */             Transformer<V, Map.Entry<K, V>> entryTransformer = new Transformer<V, Map.Entry<K, V>>()
/*     */               {
/*     */                 public Map.Entry<K, V> transform(V input)
/*     */                 {
/* 635 */                   return (Map.Entry<K, V>)new AbstractMultiValuedMap.MultiValuedMapEntry((K)key, input);
/*     */                 }
/*     */               };
/*     */             
/* 639 */             return (Iterator<? extends Map.Entry<K, V>>)new TransformIterator(new AbstractMultiValuedMap.ValuesIterator(key), entryTransformer);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 646 */       return AbstractMultiValuedMap.this.size();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class MultiValuedMapEntry
/*     */     extends AbstractMapEntry<K, V>
/*     */   {
/*     */     public MultiValuedMapEntry(K key, V value) {
/* 657 */       super(key, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V setValue(V value) {
/* 662 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class MultiValuedMapIterator
/*     */     implements MapIterator<K, V>
/*     */   {
/*     */     private final Iterator<Map.Entry<K, V>> it;
/*     */ 
/*     */     
/* 674 */     private Map.Entry<K, V> current = null;
/*     */     
/*     */     public MultiValuedMapIterator() {
/* 677 */       this.it = AbstractMultiValuedMap.this.entries().iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 682 */       return this.it.hasNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public K next() {
/* 687 */       this.current = this.it.next();
/* 688 */       return this.current.getKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public K getKey() {
/* 693 */       if (this.current == null) {
/* 694 */         throw new IllegalStateException();
/*     */       }
/* 696 */       return this.current.getKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/* 701 */       if (this.current == null) {
/* 702 */         throw new IllegalStateException();
/*     */       }
/* 704 */       return this.current.getValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 709 */       this.it.remove();
/*     */     }
/*     */ 
/*     */     
/*     */     public V setValue(V value) {
/* 714 */       if (this.current == null) {
/* 715 */         throw new IllegalStateException();
/*     */       }
/* 717 */       return this.current.setValue(value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class Values
/*     */     extends AbstractCollection<V>
/*     */   {
/*     */     private Values() {}
/*     */     
/*     */     public Iterator<V> iterator() {
/* 728 */       IteratorChain<V> chain = new IteratorChain();
/* 729 */       for (K k : AbstractMultiValuedMap.this.keySet()) {
/* 730 */         chain.addIterator(new AbstractMultiValuedMap.ValuesIterator(k));
/*     */       }
/* 732 */       return (Iterator<V>)chain;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 737 */       return AbstractMultiValuedMap.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 742 */       AbstractMultiValuedMap.this.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class ValuesIterator
/*     */     implements Iterator<V>
/*     */   {
/*     */     private final Object key;
/*     */     private final Collection<V> values;
/*     */     private final Iterator<V> iterator;
/*     */     
/*     */     public ValuesIterator(Object key) {
/* 755 */       this.key = key;
/* 756 */       this.values = (Collection<V>)AbstractMultiValuedMap.this.getMap().get(key);
/* 757 */       this.iterator = this.values.iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 762 */       this.iterator.remove();
/* 763 */       if (this.values.isEmpty()) {
/* 764 */         AbstractMultiValuedMap.this.remove(this.key);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 770 */       return this.iterator.hasNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public V next() {
/* 775 */       return this.iterator.next();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class AsMap
/*     */     extends AbstractMap<K, Collection<V>>
/*     */   {
/*     */     final transient Map<K, Collection<V>> decoratedMap;
/*     */     
/*     */     AsMap(Map<K, Collection<V>> map) {
/* 786 */       this.decoratedMap = map;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Map.Entry<K, Collection<V>>> entrySet() {
/* 791 */       return new AsMapEntrySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 796 */       return this.decoratedMap.containsKey(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<V> get(Object key) {
/* 801 */       Collection<V> collection = this.decoratedMap.get(key);
/* 802 */       if (collection == null) {
/* 803 */         return null;
/*     */       }
/*     */       
/* 806 */       K k = (K)key;
/* 807 */       return AbstractMultiValuedMap.this.wrappedCollection(k);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<K> keySet() {
/* 812 */       return AbstractMultiValuedMap.this.keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 817 */       return this.decoratedMap.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<V> remove(Object key) {
/* 822 */       Collection<V> collection = this.decoratedMap.remove(key);
/* 823 */       if (collection == null) {
/* 824 */         return null;
/*     */       }
/*     */       
/* 827 */       Collection<V> output = AbstractMultiValuedMap.this.createCollection();
/* 828 */       output.addAll(collection);
/* 829 */       collection.clear();
/* 830 */       return output;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 835 */       return (this == object || this.decoratedMap.equals(object));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 840 */       return this.decoratedMap.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 845 */       return this.decoratedMap.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 850 */       AbstractMultiValuedMap.this.clear();
/*     */     }
/*     */     
/*     */     class AsMapEntrySet
/*     */       extends AbstractSet<Map.Entry<K, Collection<V>>>
/*     */     {
/*     */       public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 857 */         return (Iterator<Map.Entry<K, Collection<V>>>)new AbstractMultiValuedMap.AsMap.AsMapEntrySetIterator(AbstractMultiValuedMap.AsMap.this.decoratedMap.entrySet().iterator());
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/* 862 */         return AbstractMultiValuedMap.AsMap.this.size();
/*     */       }
/*     */ 
/*     */       
/*     */       public void clear() {
/* 867 */         AbstractMultiValuedMap.AsMap.this.clear();
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean contains(Object o) {
/* 872 */         return AbstractMultiValuedMap.AsMap.this.decoratedMap.entrySet().contains(o);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object o) {
/* 877 */         if (!contains(o)) {
/* 878 */           return false;
/*     */         }
/* 880 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 881 */         AbstractMultiValuedMap.this.remove(entry.getKey());
/* 882 */         return true;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     class AsMapEntrySetIterator
/*     */       extends AbstractIteratorDecorator<Map.Entry<K, Collection<V>>>
/*     */     {
/*     */       AsMapEntrySetIterator(Iterator<Map.Entry<K, Collection<V>>> iterator) {
/* 892 */         super(iterator);
/*     */       }
/*     */ 
/*     */       
/*     */       public Map.Entry<K, Collection<V>> next() {
/* 897 */         Map.Entry<K, Collection<V>> entry = (Map.Entry<K, Collection<V>>)super.next();
/* 898 */         K key = entry.getKey();
/* 899 */         return (Map.Entry<K, Collection<V>>)new UnmodifiableMapEntry(key, AbstractMultiValuedMap.this.wrappedCollection(key));
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
/*     */   protected void doWriteObject(ObjectOutputStream out) throws IOException {
/* 911 */     out.writeInt(this.map.size());
/* 912 */     for (Map.Entry<K, Collection<V>> entry : this.map.entrySet()) {
/* 913 */       out.writeObject(entry.getKey());
/* 914 */       out.writeInt(((Collection)entry.getValue()).size());
/* 915 */       for (V value : entry.getValue()) {
/* 916 */         out.writeObject(value);
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
/*     */   protected void doReadObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 930 */     int entrySize = in.readInt();
/* 931 */     for (int i = 0; i < entrySize; i++) {
/*     */       
/* 933 */       K key = (K)in.readObject();
/* 934 */       Collection<V> values = get(key);
/* 935 */       int valueSize = in.readInt();
/* 936 */       for (int j = 0; j < valueSize; j++) {
/*     */         
/* 938 */         V value = (V)in.readObject();
/* 939 */         values.add(value);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract Collection<V> createCollection();
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\multimap\AbstractMultiValuedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */