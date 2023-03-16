/*      */ package org.apache.commons.collections4.map;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Collection;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.collections4.IterableMap;
/*      */ import org.apache.commons.collections4.KeyValue;
/*      */ import org.apache.commons.collections4.MapIterator;
/*      */ import org.apache.commons.collections4.iterators.EmptyIterator;
/*      */ import org.apache.commons.collections4.iterators.EmptyMapIterator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class AbstractHashedMap<K, V>
/*      */   extends AbstractMap<K, V>
/*      */   implements IterableMap<K, V>
/*      */ {
/*      */   protected static final String NO_NEXT_ENTRY = "No next() entry in the iteration";
/*      */   protected static final String NO_PREVIOUS_ENTRY = "No previous() entry in the iteration";
/*      */   protected static final String REMOVE_INVALID = "remove() can only be called once after next()";
/*      */   protected static final String GETKEY_INVALID = "getKey() can only be called after next() and before remove()";
/*      */   protected static final String GETVALUE_INVALID = "getValue() can only be called after next() and before remove()";
/*      */   protected static final String SETVALUE_INVALID = "setValue() can only be called after next() and before remove()";
/*      */   protected static final int DEFAULT_CAPACITY = 16;
/*      */   protected static final int DEFAULT_THRESHOLD = 12;
/*      */   protected static final float DEFAULT_LOAD_FACTOR = 0.75F;
/*      */   protected static final int MAXIMUM_CAPACITY = 1073741824;
/*   76 */   protected static final Object NULL = new Object();
/*      */ 
/*      */ 
/*      */   
/*      */   transient float loadFactor;
/*      */ 
/*      */ 
/*      */   
/*      */   transient int size;
/*      */ 
/*      */ 
/*      */   
/*      */   transient HashEntry<K, V>[] data;
/*      */ 
/*      */   
/*      */   transient int threshold;
/*      */ 
/*      */   
/*      */   transient int modCount;
/*      */ 
/*      */   
/*      */   transient EntrySet<K, V> entrySet;
/*      */ 
/*      */   
/*      */   transient KeySet<K> keySet;
/*      */ 
/*      */   
/*      */   transient Values<V> values;
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractHashedMap() {}
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractHashedMap(int initialCapacity, float loadFactor, int threshold) {
/*  112 */     this.loadFactor = loadFactor;
/*  113 */     this.data = (HashEntry<K, V>[])new HashEntry[initialCapacity];
/*  114 */     this.threshold = threshold;
/*  115 */     init();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractHashedMap(int initialCapacity) {
/*  126 */     this(initialCapacity, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractHashedMap(int initialCapacity, float loadFactor) {
/*  141 */     if (initialCapacity < 0) {
/*  142 */       throw new IllegalArgumentException("Initial capacity must be a non negative number");
/*      */     }
/*  144 */     if (loadFactor <= 0.0F || Float.isNaN(loadFactor)) {
/*  145 */       throw new IllegalArgumentException("Load factor must be greater than 0");
/*      */     }
/*  147 */     this.loadFactor = loadFactor;
/*  148 */     initialCapacity = calculateNewCapacity(initialCapacity);
/*  149 */     this.threshold = calculateThreshold(initialCapacity, loadFactor);
/*  150 */     this.data = (HashEntry<K, V>[])new HashEntry[initialCapacity];
/*  151 */     init();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractHashedMap(Map<? extends K, ? extends V> map) {
/*  161 */     this(Math.max(2 * map.size(), 16), 0.75F);
/*  162 */     _putAll(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void init() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public V get(Object key) {
/*  180 */     key = convertKey(key);
/*  181 */     int hashCode = hash(key);
/*  182 */     HashEntry<K, V> entry = this.data[hashIndex(hashCode, this.data.length)];
/*  183 */     while (entry != null) {
/*  184 */       if (entry.hashCode == hashCode && isEqualKey(key, entry.key)) {
/*  185 */         return entry.getValue();
/*      */       }
/*  187 */       entry = entry.next;
/*      */     } 
/*  189 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  199 */     return this.size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/*  209 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsKey(Object key) {
/*  221 */     key = convertKey(key);
/*  222 */     int hashCode = hash(key);
/*  223 */     HashEntry<K, V> entry = this.data[hashIndex(hashCode, this.data.length)];
/*  224 */     while (entry != null) {
/*  225 */       if (entry.hashCode == hashCode && isEqualKey(key, entry.key)) {
/*  226 */         return true;
/*      */       }
/*  228 */       entry = entry.next;
/*      */     } 
/*  230 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsValue(Object value) {
/*  241 */     if (value == null) {
/*  242 */       for (HashEntry<K, V> element : this.data) {
/*  243 */         HashEntry<K, V> entry = element;
/*  244 */         while (entry != null) {
/*  245 */           if (entry.getValue() == null) {
/*  246 */             return true;
/*      */           }
/*  248 */           entry = entry.next;
/*      */         } 
/*      */       } 
/*      */     } else {
/*  252 */       for (HashEntry<K, V> element : this.data) {
/*  253 */         HashEntry<K, V> entry = element;
/*  254 */         while (entry != null) {
/*  255 */           if (isEqualValue(value, entry.getValue())) {
/*  256 */             return true;
/*      */           }
/*  258 */           entry = entry.next;
/*      */         } 
/*      */       } 
/*      */     } 
/*  262 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public V put(K key, V value) {
/*  275 */     Object convertedKey = convertKey(key);
/*  276 */     int hashCode = hash(convertedKey);
/*  277 */     int index = hashIndex(hashCode, this.data.length);
/*  278 */     HashEntry<K, V> entry = this.data[index];
/*  279 */     while (entry != null) {
/*  280 */       if (entry.hashCode == hashCode && isEqualKey(convertedKey, entry.key)) {
/*  281 */         V oldValue = entry.getValue();
/*  282 */         updateEntry(entry, value);
/*  283 */         return oldValue;
/*      */       } 
/*  285 */       entry = entry.next;
/*      */     } 
/*      */     
/*  288 */     addMapping(index, hashCode, key, value);
/*  289 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends V> map) {
/*  303 */     _putAll(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void _putAll(Map<? extends K, ? extends V> map) {
/*  319 */     int mapSize = map.size();
/*  320 */     if (mapSize == 0) {
/*      */       return;
/*      */     }
/*  323 */     int newSize = (int)((this.size + mapSize) / this.loadFactor + 1.0F);
/*  324 */     ensureCapacity(calculateNewCapacity(newSize));
/*  325 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/*  326 */       put(entry.getKey(), entry.getValue());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public V remove(Object key) {
/*  338 */     key = convertKey(key);
/*  339 */     int hashCode = hash(key);
/*  340 */     int index = hashIndex(hashCode, this.data.length);
/*  341 */     HashEntry<K, V> entry = this.data[index];
/*  342 */     HashEntry<K, V> previous = null;
/*  343 */     while (entry != null) {
/*  344 */       if (entry.hashCode == hashCode && isEqualKey(key, entry.key)) {
/*  345 */         V oldValue = entry.getValue();
/*  346 */         removeMapping(entry, index, previous);
/*  347 */         return oldValue;
/*      */       } 
/*  349 */       previous = entry;
/*  350 */       entry = entry.next;
/*      */     } 
/*  352 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  361 */     this.modCount++;
/*  362 */     HashEntry<K, V>[] data = this.data;
/*  363 */     for (int i = data.length - 1; i >= 0; i--) {
/*  364 */       data[i] = null;
/*      */     }
/*  366 */     this.size = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object convertKey(Object key) {
/*  382 */     return (key == null) ? NULL : key;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int hash(Object key) {
/*  395 */     int h = key.hashCode();
/*  396 */     h += h << 9 ^ 0xFFFFFFFF;
/*  397 */     h ^= h >>> 14;
/*  398 */     h += h << 4;
/*  399 */     h ^= h >>> 10;
/*  400 */     return h;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isEqualKey(Object key1, Object key2) {
/*  413 */     return (key1 == key2 || key1.equals(key2));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isEqualValue(Object value1, Object value2) {
/*  426 */     return (value1 == value2 || value1.equals(value2));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int hashIndex(int hashCode, int dataSize) {
/*  439 */     return hashCode & dataSize - 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected HashEntry<K, V> getEntry(Object key) {
/*  454 */     key = convertKey(key);
/*  455 */     int hashCode = hash(key);
/*  456 */     HashEntry<K, V> entry = this.data[hashIndex(hashCode, this.data.length)];
/*  457 */     while (entry != null) {
/*  458 */       if (entry.hashCode == hashCode && isEqualKey(key, entry.key)) {
/*  459 */         return entry;
/*      */       }
/*  461 */       entry = entry.next;
/*      */     } 
/*  463 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void updateEntry(HashEntry<K, V> entry, V newValue) {
/*  477 */     entry.setValue(newValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void reuseEntry(HashEntry<K, V> entry, int hashIndex, int hashCode, K key, V value) {
/*  494 */     entry.next = this.data[hashIndex];
/*  495 */     entry.hashCode = hashCode;
/*  496 */     entry.key = key;
/*  497 */     entry.value = value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addMapping(int hashIndex, int hashCode, K key, V value) {
/*  515 */     this.modCount++;
/*  516 */     HashEntry<K, V> entry = createEntry(this.data[hashIndex], hashCode, key, value);
/*  517 */     addEntry(entry, hashIndex);
/*  518 */     this.size++;
/*  519 */     checkCapacity();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected HashEntry<K, V> createEntry(HashEntry<K, V> next, int hashCode, K key, V value) {
/*  536 */     return new HashEntry<K, V>(next, hashCode, convertKey(key), value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addEntry(HashEntry<K, V> entry, int hashIndex) {
/*  549 */     this.data[hashIndex] = entry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeMapping(HashEntry<K, V> entry, int hashIndex, HashEntry<K, V> previous) {
/*  565 */     this.modCount++;
/*  566 */     removeEntry(entry, hashIndex, previous);
/*  567 */     this.size--;
/*  568 */     destroyEntry(entry);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeEntry(HashEntry<K, V> entry, int hashIndex, HashEntry<K, V> previous) {
/*  583 */     if (previous == null) {
/*  584 */       this.data[hashIndex] = entry.next;
/*      */     } else {
/*  586 */       previous.next = entry.next;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void destroyEntry(HashEntry<K, V> entry) {
/*  599 */     entry.next = null;
/*  600 */     entry.key = null;
/*  601 */     entry.value = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkCapacity() {
/*  611 */     if (this.size >= this.threshold) {
/*  612 */       int newCapacity = this.data.length * 2;
/*  613 */       if (newCapacity <= 1073741824) {
/*  614 */         ensureCapacity(newCapacity);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void ensureCapacity(int newCapacity) {
/*  626 */     int oldCapacity = this.data.length;
/*  627 */     if (newCapacity <= oldCapacity) {
/*      */       return;
/*      */     }
/*  630 */     if (this.size == 0) {
/*  631 */       this.threshold = calculateThreshold(newCapacity, this.loadFactor);
/*  632 */       this.data = (HashEntry<K, V>[])new HashEntry[newCapacity];
/*      */     } else {
/*  634 */       HashEntry<K, V>[] oldEntries = this.data;
/*  635 */       HashEntry[] arrayOfHashEntry = new HashEntry[newCapacity];
/*      */       
/*  637 */       this.modCount++;
/*  638 */       for (int i = oldCapacity - 1; i >= 0; i--) {
/*  639 */         HashEntry<K, V> entry = oldEntries[i];
/*  640 */         if (entry != null) {
/*  641 */           oldEntries[i] = null;
/*      */           do {
/*  643 */             HashEntry<K, V> next = entry.next;
/*  644 */             int index = hashIndex(entry.hashCode, newCapacity);
/*  645 */             entry.next = arrayOfHashEntry[index];
/*  646 */             arrayOfHashEntry[index] = entry;
/*  647 */             entry = next;
/*  648 */           } while (entry != null);
/*      */         } 
/*      */       } 
/*  651 */       this.threshold = calculateThreshold(newCapacity, this.loadFactor);
/*  652 */       this.data = (HashEntry<K, V>[])arrayOfHashEntry;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int calculateNewCapacity(int proposedCapacity) {
/*  664 */     int newCapacity = 1;
/*  665 */     if (proposedCapacity > 1073741824) {
/*  666 */       newCapacity = 1073741824;
/*      */     } else {
/*  668 */       while (newCapacity < proposedCapacity) {
/*  669 */         newCapacity <<= 1;
/*      */       }
/*  671 */       if (newCapacity > 1073741824) {
/*  672 */         newCapacity = 1073741824;
/*      */       }
/*      */     } 
/*  675 */     return newCapacity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int calculateThreshold(int newCapacity, float factor) {
/*  687 */     return (int)(newCapacity * factor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected HashEntry<K, V> entryNext(HashEntry<K, V> entry) {
/*  701 */     return entry.next;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int entryHashCode(HashEntry<K, V> entry) {
/*  714 */     return entry.hashCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected K entryKey(HashEntry<K, V> entry) {
/*  727 */     return entry.getKey();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected V entryValue(HashEntry<K, V> entry) {
/*  740 */     return entry.getValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MapIterator<K, V> mapIterator() {
/*  756 */     if (this.size == 0) {
/*  757 */       return EmptyMapIterator.emptyMapIterator();
/*      */     }
/*  759 */     return new HashMapIterator<K, V>(this);
/*      */   }
/*      */ 
/*      */   
/*      */   protected static class HashMapIterator<K, V>
/*      */     extends HashIterator<K, V>
/*      */     implements MapIterator<K, V>
/*      */   {
/*      */     protected HashMapIterator(AbstractHashedMap<K, V> parent) {
/*  768 */       super(parent);
/*      */     }
/*      */     
/*      */     public K next() {
/*  772 */       return nextEntry().getKey();
/*      */     }
/*      */     
/*      */     public K getKey() {
/*  776 */       AbstractHashedMap.HashEntry<K, V> current = currentEntry();
/*  777 */       if (current == null) {
/*  778 */         throw new IllegalStateException("getKey() can only be called after next() and before remove()");
/*      */       }
/*  780 */       return current.getKey();
/*      */     }
/*      */     
/*      */     public V getValue() {
/*  784 */       AbstractHashedMap.HashEntry<K, V> current = currentEntry();
/*  785 */       if (current == null) {
/*  786 */         throw new IllegalStateException("getValue() can only be called after next() and before remove()");
/*      */       }
/*  788 */       return current.getValue();
/*      */     }
/*      */     
/*      */     public V setValue(V value) {
/*  792 */       AbstractHashedMap.HashEntry<K, V> current = currentEntry();
/*  793 */       if (current == null) {
/*  794 */         throw new IllegalStateException("setValue() can only be called after next() and before remove()");
/*      */       }
/*  796 */       return current.setValue(value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<Map.Entry<K, V>> entrySet() {
/*  810 */     if (this.entrySet == null) {
/*  811 */       this.entrySet = new EntrySet<K, V>(this);
/*      */     }
/*  813 */     return this.entrySet;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Iterator<Map.Entry<K, V>> createEntrySetIterator() {
/*  823 */     if (size() == 0) {
/*  824 */       return EmptyIterator.emptyIterator();
/*      */     }
/*  826 */     return new EntrySetIterator<K, V>(this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class EntrySet<K, V>
/*      */     extends AbstractSet<Map.Entry<K, V>>
/*      */   {
/*      */     private final AbstractHashedMap<K, V> parent;
/*      */ 
/*      */     
/*      */     protected EntrySet(AbstractHashedMap<K, V> parent) {
/*  838 */       this.parent = parent;
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  843 */       return this.parent.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  848 */       this.parent.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object entry) {
/*  853 */       if (entry instanceof Map.Entry) {
/*  854 */         Map.Entry<?, ?> e = (Map.Entry<?, ?>)entry;
/*  855 */         Map.Entry<K, V> match = this.parent.getEntry(e.getKey());
/*  856 */         return (match != null && match.equals(e));
/*      */       } 
/*  858 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object obj) {
/*  863 */       if (!(obj instanceof Map.Entry)) {
/*  864 */         return false;
/*      */       }
/*  866 */       if (!contains(obj)) {
/*  867 */         return false;
/*      */       }
/*  869 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/*  870 */       this.parent.remove(entry.getKey());
/*  871 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/*  876 */       return this.parent.createEntrySetIterator();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected static class EntrySetIterator<K, V>
/*      */     extends HashIterator<K, V>
/*      */     implements Iterator<Map.Entry<K, V>>
/*      */   {
/*      */     protected EntrySetIterator(AbstractHashedMap<K, V> parent) {
/*  886 */       super(parent);
/*      */     }
/*      */     
/*      */     public Map.Entry<K, V> next() {
/*  890 */       return nextEntry();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<K> keySet() {
/*  904 */     if (this.keySet == null) {
/*  905 */       this.keySet = new KeySet<K>(this);
/*      */     }
/*  907 */     return this.keySet;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Iterator<K> createKeySetIterator() {
/*  917 */     if (size() == 0) {
/*  918 */       return EmptyIterator.emptyIterator();
/*      */     }
/*  920 */     return new KeySetIterator<K>(this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class KeySet<K>
/*      */     extends AbstractSet<K>
/*      */   {
/*      */     private final AbstractHashedMap<K, ?> parent;
/*      */ 
/*      */     
/*      */     protected KeySet(AbstractHashedMap<K, ?> parent) {
/*  932 */       this.parent = parent;
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  937 */       return this.parent.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  942 */       this.parent.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object key) {
/*  947 */       return this.parent.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object key) {
/*  952 */       boolean result = this.parent.containsKey(key);
/*  953 */       this.parent.remove(key);
/*  954 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/*  959 */       return this.parent.createKeySetIterator();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class KeySetIterator<K>
/*      */     extends HashIterator<K, Object>
/*      */     implements Iterator<K>
/*      */   {
/*      */     protected KeySetIterator(AbstractHashedMap<K, ?> parent) {
/*  970 */       super((AbstractHashedMap)parent);
/*      */     }
/*      */     
/*      */     public K next() {
/*  974 */       return nextEntry().getKey();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/*  988 */     if (this.values == null) {
/*  989 */       this.values = new Values<V>(this);
/*      */     }
/*  991 */     return this.values;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Iterator<V> createValuesIterator() {
/* 1001 */     if (size() == 0) {
/* 1002 */       return EmptyIterator.emptyIterator();
/*      */     }
/* 1004 */     return new ValuesIterator<V>(this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class Values<V>
/*      */     extends AbstractCollection<V>
/*      */   {
/*      */     private final AbstractHashedMap<?, V> parent;
/*      */ 
/*      */     
/*      */     protected Values(AbstractHashedMap<?, V> parent) {
/* 1016 */       this.parent = parent;
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1021 */       return this.parent.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1026 */       this.parent.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object value) {
/* 1031 */       return this.parent.containsValue(value);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> iterator() {
/* 1036 */       return this.parent.createValuesIterator();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class ValuesIterator<V>
/*      */     extends HashIterator<Object, V>
/*      */     implements Iterator<V>
/*      */   {
/*      */     protected ValuesIterator(AbstractHashedMap<?, V> parent) {
/* 1047 */       super((AbstractHashedMap)parent);
/*      */     }
/*      */     
/*      */     public V next() {
/* 1051 */       return nextEntry().getValue();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class HashEntry<K, V>
/*      */     implements Map.Entry<K, V>, KeyValue<K, V>
/*      */   {
/*      */     protected HashEntry<K, V> next;
/*      */ 
/*      */ 
/*      */     
/*      */     protected int hashCode;
/*      */ 
/*      */     
/*      */     protected Object key;
/*      */ 
/*      */     
/*      */     protected Object value;
/*      */ 
/*      */ 
/*      */     
/*      */     protected HashEntry(HashEntry<K, V> next, int hashCode, Object key, V value) {
/* 1076 */       this.next = next;
/* 1077 */       this.hashCode = hashCode;
/* 1078 */       this.key = key;
/* 1079 */       this.value = value;
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/* 1084 */       if (this.key == AbstractHashedMap.NULL) {
/* 1085 */         return null;
/*      */       }
/* 1087 */       return (K)this.key;
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/* 1092 */       return (V)this.value;
/*      */     }
/*      */ 
/*      */     
/*      */     public V setValue(V value) {
/* 1097 */       Object old = this.value;
/* 1098 */       this.value = value;
/* 1099 */       return (V)old;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1104 */       if (obj == this) {
/* 1105 */         return true;
/*      */       }
/* 1107 */       if (!(obj instanceof Map.Entry)) {
/* 1108 */         return false;
/*      */       }
/* 1110 */       Map.Entry<?, ?> other = (Map.Entry<?, ?>)obj;
/* 1111 */       return (((getKey() == null) ? (other.getKey() == null) : getKey().equals(other.getKey())) && ((getValue() == null) ? (other.getValue() == null) : getValue().equals(other.getValue())));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1118 */       return ((getKey() == null) ? 0 : getKey().hashCode()) ^ ((getValue() == null) ? 0 : getValue().hashCode());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1124 */       return (new StringBuilder()).append(getKey()).append('=').append(getValue()).toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected static abstract class HashIterator<K, V>
/*      */   {
/*      */     private final AbstractHashedMap<K, V> parent;
/*      */ 
/*      */     
/*      */     private int hashIndex;
/*      */ 
/*      */     
/*      */     private AbstractHashedMap.HashEntry<K, V> last;
/*      */     
/*      */     private AbstractHashedMap.HashEntry<K, V> next;
/*      */     
/*      */     private int expectedModCount;
/*      */ 
/*      */     
/*      */     protected HashIterator(AbstractHashedMap<K, V> parent) {
/* 1146 */       this.parent = parent;
/* 1147 */       AbstractHashedMap.HashEntry<K, V>[] data = parent.data;
/* 1148 */       int i = data.length;
/* 1149 */       AbstractHashedMap.HashEntry<K, V> next = null;
/* 1150 */       while (i > 0 && next == null) {
/* 1151 */         next = data[--i];
/*      */       }
/* 1153 */       this.next = next;
/* 1154 */       this.hashIndex = i;
/* 1155 */       this.expectedModCount = parent.modCount;
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 1159 */       return (this.next != null);
/*      */     }
/*      */     
/*      */     protected AbstractHashedMap.HashEntry<K, V> nextEntry() {
/* 1163 */       if (this.parent.modCount != this.expectedModCount) {
/* 1164 */         throw new ConcurrentModificationException();
/*      */       }
/* 1166 */       AbstractHashedMap.HashEntry<K, V> newCurrent = this.next;
/* 1167 */       if (newCurrent == null) {
/* 1168 */         throw new NoSuchElementException("No next() entry in the iteration");
/*      */       }
/* 1170 */       AbstractHashedMap.HashEntry<K, V>[] data = this.parent.data;
/* 1171 */       int i = this.hashIndex;
/* 1172 */       AbstractHashedMap.HashEntry<K, V> n = newCurrent.next;
/* 1173 */       while (n == null && i > 0) {
/* 1174 */         n = data[--i];
/*      */       }
/* 1176 */       this.next = n;
/* 1177 */       this.hashIndex = i;
/* 1178 */       this.last = newCurrent;
/* 1179 */       return newCurrent;
/*      */     }
/*      */     
/*      */     protected AbstractHashedMap.HashEntry<K, V> currentEntry() {
/* 1183 */       return this.last;
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1187 */       if (this.last == null) {
/* 1188 */         throw new IllegalStateException("remove() can only be called once after next()");
/*      */       }
/* 1190 */       if (this.parent.modCount != this.expectedModCount) {
/* 1191 */         throw new ConcurrentModificationException();
/*      */       }
/* 1193 */       this.parent.remove(this.last.getKey());
/* 1194 */       this.last = null;
/* 1195 */       this.expectedModCount = this.parent.modCount;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1200 */       if (this.last != null) {
/* 1201 */         return "Iterator[" + this.last.getKey() + "=" + this.last.getValue() + "]";
/*      */       }
/* 1203 */       return "Iterator[]";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void doWriteObject(ObjectOutputStream out) throws IOException {
/* 1229 */     out.writeFloat(this.loadFactor);
/* 1230 */     out.writeInt(this.data.length);
/* 1231 */     out.writeInt(this.size);
/* 1232 */     for (MapIterator<K, V> it = mapIterator(); it.hasNext(); ) {
/* 1233 */       out.writeObject(it.next());
/* 1234 */       out.writeObject(it.getValue());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void doReadObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 1260 */     this.loadFactor = in.readFloat();
/* 1261 */     int capacity = in.readInt();
/* 1262 */     int size = in.readInt();
/* 1263 */     init();
/* 1264 */     this.threshold = calculateThreshold(capacity, this.loadFactor);
/* 1265 */     this.data = (HashEntry<K, V>[])new HashEntry[capacity];
/* 1266 */     for (int i = 0; i < size; i++) {
/* 1267 */       K key = (K)in.readObject();
/* 1268 */       V value = (V)in.readObject();
/* 1269 */       put(key, value);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractHashedMap<K, V> clone() {
/*      */     try {
/* 1287 */       AbstractHashedMap<K, V> cloned = (AbstractHashedMap<K, V>)super.clone();
/* 1288 */       cloned.data = (HashEntry<K, V>[])new HashEntry[this.data.length];
/* 1289 */       cloned.entrySet = null;
/* 1290 */       cloned.keySet = null;
/* 1291 */       cloned.values = null;
/* 1292 */       cloned.modCount = 0;
/* 1293 */       cloned.size = 0;
/* 1294 */       cloned.init();
/* 1295 */       cloned.putAll(this);
/* 1296 */       return cloned;
/* 1297 */     } catch (CloneNotSupportedException ex) {
/* 1298 */       throw new InternalError();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object obj) {
/* 1310 */     if (obj == this) {
/* 1311 */       return true;
/*      */     }
/* 1313 */     if (!(obj instanceof Map)) {
/* 1314 */       return false;
/*      */     }
/* 1316 */     Map<?, ?> map = (Map<?, ?>)obj;
/* 1317 */     if (map.size() != size()) {
/* 1318 */       return false;
/*      */     }
/* 1320 */     MapIterator<?, ?> it = mapIterator();
/*      */     try {
/* 1322 */       while (it.hasNext()) {
/* 1323 */         Object key = it.next();
/* 1324 */         Object value = it.getValue();
/* 1325 */         if (value == null) {
/* 1326 */           if (map.get(key) != null || !map.containsKey(key))
/* 1327 */             return false; 
/*      */           continue;
/*      */         } 
/* 1330 */         if (!value.equals(map.get(key))) {
/* 1331 */           return false;
/*      */         }
/*      */       }
/*      */     
/* 1335 */     } catch (ClassCastException ignored) {
/* 1336 */       return false;
/* 1337 */     } catch (NullPointerException ignored) {
/* 1338 */       return false;
/*      */     } 
/* 1340 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1350 */     int total = 0;
/* 1351 */     Iterator<Map.Entry<K, V>> it = createEntrySetIterator();
/* 1352 */     while (it.hasNext()) {
/* 1353 */       total += ((Map.Entry)it.next()).hashCode();
/*      */     }
/* 1355 */     return total;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1365 */     if (size() == 0) {
/* 1366 */       return "{}";
/*      */     }
/* 1368 */     StringBuilder buf = new StringBuilder(32 * size());
/* 1369 */     buf.append('{');
/*      */     
/* 1371 */     MapIterator<K, V> it = mapIterator();
/* 1372 */     boolean hasNext = it.hasNext();
/* 1373 */     while (hasNext) {
/* 1374 */       K key = (K)it.next();
/* 1375 */       V value = (V)it.getValue();
/* 1376 */       buf.append((key == this) ? "(this Map)" : key).append('=').append((value == this) ? "(this Map)" : value);
/*      */ 
/*      */ 
/*      */       
/* 1380 */       hasNext = it.hasNext();
/* 1381 */       if (hasNext) {
/* 1382 */         buf.append(',').append(' ');
/*      */       }
/*      */     } 
/*      */     
/* 1386 */     buf.append('}');
/* 1387 */     return buf.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\AbstractHashedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */