/*      */ package org.apache.commons.collections4.map;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.collections4.MapIterator;
/*      */ import org.apache.commons.collections4.keyvalue.DefaultMapEntry;
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
/*      */ public abstract class AbstractReferenceMap<K, V>
/*      */   extends AbstractHashedMap<K, V>
/*      */ {
/*      */   private ReferenceStrength keyType;
/*      */   private ReferenceStrength valueType;
/*      */   private boolean purgeValues;
/*      */   private transient ReferenceQueue<Object> queue;
/*      */   
/*      */   public enum ReferenceStrength
/*      */   {
/*   88 */     HARD(0), SOFT(1), WEAK(2);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final int value;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static ReferenceStrength resolve(int value) {
/*  100 */       switch (value) {
/*      */         case 0:
/*  102 */           return HARD;
/*      */         case 1:
/*  104 */           return SOFT;
/*      */         case 2:
/*  106 */           return WEAK;
/*      */       } 
/*  108 */       throw new IllegalArgumentException();
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceStrength(int value) {
/*  113 */       this.value = value;
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
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractReferenceMap() {}
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
/*      */   protected AbstractReferenceMap(ReferenceStrength keyType, ReferenceStrength valueType, int capacity, float loadFactor, boolean purgeValues) {
/*  167 */     super(capacity, loadFactor);
/*  168 */     this.keyType = keyType;
/*  169 */     this.valueType = valueType;
/*  170 */     this.purgeValues = purgeValues;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void init() {
/*  178 */     this.queue = new ReferenceQueue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  189 */     purgeBeforeRead();
/*  190 */     return super.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/*  200 */     purgeBeforeRead();
/*  201 */     return super.isEmpty();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsKey(Object key) {
/*  212 */     purgeBeforeRead();
/*  213 */     Map.Entry<K, V> entry = getEntry(key);
/*  214 */     if (entry == null) {
/*  215 */       return false;
/*      */     }
/*  217 */     return (entry.getValue() != null);
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
/*  228 */     purgeBeforeRead();
/*  229 */     if (value == null) {
/*  230 */       return false;
/*      */     }
/*  232 */     return super.containsValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public V get(Object key) {
/*  243 */     purgeBeforeRead();
/*  244 */     Map.Entry<K, V> entry = getEntry(key);
/*  245 */     if (entry == null) {
/*  246 */       return null;
/*      */     }
/*  248 */     return entry.getValue();
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
/*      */   public V put(K key, V value) {
/*  263 */     if (key == null) {
/*  264 */       throw new NullPointerException("null keys not allowed");
/*      */     }
/*  266 */     if (value == null) {
/*  267 */       throw new NullPointerException("null values not allowed");
/*      */     }
/*      */     
/*  270 */     purgeBeforeWrite();
/*  271 */     return super.put(key, value);
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
/*  282 */     if (key == null) {
/*  283 */       return null;
/*      */     }
/*  285 */     purgeBeforeWrite();
/*  286 */     return super.remove(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  294 */     super.clear();
/*  295 */     while (this.queue.poll() != null);
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
/*      */   public MapIterator<K, V> mapIterator() {
/*  307 */     return new ReferenceMapIterator<K, V>(this);
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
/*      */   public Set<Map.Entry<K, V>> entrySet() {
/*  319 */     if (this.entrySet == null) {
/*  320 */       this.entrySet = new ReferenceEntrySet<K, V>(this);
/*      */     }
/*  322 */     return this.entrySet;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<K> keySet() {
/*  332 */     if (this.keySet == null) {
/*  333 */       this.keySet = new ReferenceKeySet<K>(this);
/*      */     }
/*  335 */     return this.keySet;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/*  345 */     if (this.values == null) {
/*  346 */       this.values = new ReferenceValues<V>(this);
/*      */     }
/*  348 */     return this.values;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void purgeBeforeRead() {
/*  358 */     purge();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void purgeBeforeWrite() {
/*  367 */     purge();
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
/*      */   protected void purge() {
/*  379 */     Reference<?> ref = this.queue.poll();
/*  380 */     while (ref != null) {
/*  381 */       purge(ref);
/*  382 */       ref = this.queue.poll();
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
/*      */   protected void purge(Reference<?> ref) {
/*  395 */     int hash = ref.hashCode();
/*  396 */     int index = hashIndex(hash, this.data.length);
/*  397 */     AbstractHashedMap.HashEntry<K, V> previous = null;
/*  398 */     AbstractHashedMap.HashEntry<K, V> entry = this.data[index];
/*  399 */     while (entry != null) {
/*  400 */       if (((ReferenceEntry)entry).purge(ref)) {
/*  401 */         if (previous == null) {
/*  402 */           this.data[index] = entry.next;
/*      */         } else {
/*  404 */           previous.next = entry.next;
/*      */         } 
/*  406 */         this.size--;
/*      */         return;
/*      */       } 
/*  409 */       previous = entry;
/*  410 */       entry = entry.next;
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
/*      */   protected AbstractHashedMap.HashEntry<K, V> getEntry(Object key) {
/*  424 */     if (key == null) {
/*  425 */       return null;
/*      */     }
/*  427 */     return super.getEntry(key);
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
/*      */   protected int hashEntry(Object key, Object value) {
/*  439 */     return ((key == null) ? 0 : key.hashCode()) ^ ((value == null) ? 0 : value.hashCode());
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
/*      */   protected boolean isEqualKey(Object key1, Object key2) {
/*  456 */     key2 = (this.keyType == ReferenceStrength.HARD) ? key2 : ((Reference)key2).get();
/*  457 */     return (key1 == key2 || key1.equals(key2));
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
/*      */   protected ReferenceEntry<K, V> createEntry(AbstractHashedMap.HashEntry<K, V> next, int hashCode, K key, V value) {
/*  472 */     return new ReferenceEntry<K, V>(this, next, hashCode, key, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Iterator<Map.Entry<K, V>> createEntrySetIterator() {
/*  482 */     return new ReferenceEntrySetIterator<K, V>(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Iterator<K> createKeySetIterator() {
/*  492 */     return new ReferenceKeySetIterator<K>(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Iterator<V> createValuesIterator() {
/*  502 */     return new ReferenceValuesIterator<V>(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class ReferenceEntrySet<K, V>
/*      */     extends AbstractHashedMap.EntrySet<K, V>
/*      */   {
/*      */     protected ReferenceEntrySet(AbstractHashedMap<K, V> parent) {
/*  512 */       super(parent);
/*      */     }
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/*  517 */       return toArray(new Object[size()]);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] arr) {
/*  523 */       ArrayList<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(size());
/*  524 */       for (Map.Entry<K, V> entry : this) {
/*  525 */         list.add(new DefaultMapEntry(entry));
/*      */       }
/*  527 */       return list.toArray(arr);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class ReferenceKeySet<K>
/*      */     extends AbstractHashedMap.KeySet<K>
/*      */   {
/*      */     protected ReferenceKeySet(AbstractHashedMap<K, ?> parent) {
/*  538 */       super(parent);
/*      */     }
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/*  543 */       return toArray(new Object[size()]);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] arr) {
/*  549 */       List<K> list = new ArrayList<K>(size());
/*  550 */       for (K key : this) {
/*  551 */         list.add(key);
/*      */       }
/*  553 */       return list.toArray(arr);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class ReferenceValues<V>
/*      */     extends AbstractHashedMap.Values<V>
/*      */   {
/*      */     protected ReferenceValues(AbstractHashedMap<?, V> parent) {
/*  564 */       super(parent);
/*      */     }
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/*  569 */       return toArray(new Object[size()]);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] arr) {
/*  575 */       List<V> list = new ArrayList<V>(size());
/*  576 */       for (V value : this) {
/*  577 */         list.add(value);
/*      */       }
/*  579 */       return list.toArray(arr);
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
/*      */   protected static class ReferenceEntry<K, V>
/*      */     extends AbstractHashedMap.HashEntry<K, V>
/*      */   {
/*      */     private final AbstractReferenceMap<K, V> parent;
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
/*      */     public ReferenceEntry(AbstractReferenceMap<K, V> parent, AbstractHashedMap.HashEntry<K, V> next, int hashCode, K key, V value) {
/*  607 */       super(next, hashCode, null, null);
/*  608 */       this.parent = parent;
/*  609 */       this.key = toReference(parent.keyType, key, hashCode);
/*  610 */       this.value = toReference(parent.valueType, value, hashCode);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public K getKey() {
/*  622 */       return (this.parent.keyType == AbstractReferenceMap.ReferenceStrength.HARD) ? (K)this.key : ((Reference<K>)this.key).get();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V getValue() {
/*  634 */       return (this.parent.valueType == AbstractReferenceMap.ReferenceStrength.HARD) ? (V)this.value : ((Reference<V>)this.value).get();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V setValue(V obj) {
/*  646 */       V old = getValue();
/*  647 */       if (this.parent.valueType != AbstractReferenceMap.ReferenceStrength.HARD) {
/*  648 */         ((Reference)this.value).clear();
/*      */       }
/*  650 */       this.value = toReference(this.parent.valueType, obj, this.hashCode);
/*  651 */       return old;
/*      */     }
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
/*      */     public boolean equals(Object obj) {
/*  665 */       if (obj == this) {
/*  666 */         return true;
/*      */       }
/*  668 */       if (!(obj instanceof Map.Entry)) {
/*  669 */         return false;
/*      */       }
/*      */       
/*  672 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/*  673 */       Object entryKey = entry.getKey();
/*  674 */       Object entryValue = entry.getValue();
/*  675 */       if (entryKey == null || entryValue == null) {
/*  676 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  680 */       return (this.parent.isEqualKey(entryKey, this.key) && this.parent.isEqualValue(entryValue, getValue()));
/*      */     }
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
/*      */     public int hashCode() {
/*  693 */       return this.parent.hashEntry(getKey(), getValue());
/*      */     }
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
/*      */     protected <T> Object toReference(AbstractReferenceMap.ReferenceStrength type, T referent, int hash) {
/*  709 */       if (type == AbstractReferenceMap.ReferenceStrength.HARD) {
/*  710 */         return referent;
/*      */       }
/*  712 */       if (type == AbstractReferenceMap.ReferenceStrength.SOFT) {
/*  713 */         return new AbstractReferenceMap.SoftRef<T>(hash, referent, this.parent.queue);
/*      */       }
/*  715 */       if (type == AbstractReferenceMap.ReferenceStrength.WEAK) {
/*  716 */         return new AbstractReferenceMap.WeakRef<T>(hash, referent, this.parent.queue);
/*      */       }
/*  718 */       throw new Error();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean purge(Reference<?> ref) {
/*  727 */       boolean r = (this.parent.keyType != AbstractReferenceMap.ReferenceStrength.HARD && this.key == ref);
/*  728 */       r = (r || (this.parent.valueType != AbstractReferenceMap.ReferenceStrength.HARD && this.value == ref));
/*  729 */       if (r) {
/*  730 */         if (this.parent.keyType != AbstractReferenceMap.ReferenceStrength.HARD) {
/*  731 */           ((Reference)this.key).clear();
/*      */         }
/*  733 */         if (this.parent.valueType != AbstractReferenceMap.ReferenceStrength.HARD) {
/*  734 */           ((Reference)this.value).clear();
/*  735 */         } else if (this.parent.purgeValues) {
/*  736 */           this.value = null;
/*      */         } 
/*      */       } 
/*  739 */       return r;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected ReferenceEntry<K, V> next() {
/*  748 */       return (ReferenceEntry<K, V>)this.next;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class ReferenceBaseIterator<K, V>
/*      */   {
/*      */     final AbstractReferenceMap<K, V> parent;
/*      */     
/*      */     int index;
/*      */     
/*      */     AbstractReferenceMap.ReferenceEntry<K, V> entry;
/*      */     
/*      */     AbstractReferenceMap.ReferenceEntry<K, V> previous;
/*      */     
/*      */     K currentKey;
/*      */     
/*      */     K nextKey;
/*      */     
/*      */     V currentValue;
/*      */     
/*      */     V nextValue;
/*      */     
/*      */     int expectedModCount;
/*      */ 
/*      */     
/*      */     public ReferenceBaseIterator(AbstractReferenceMap<K, V> parent) {
/*  775 */       this.parent = parent;
/*  776 */       this.index = (parent.size() != 0) ? parent.data.length : 0;
/*      */ 
/*      */       
/*  779 */       this.expectedModCount = parent.modCount;
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/*  783 */       checkMod();
/*  784 */       while (nextNull()) {
/*  785 */         AbstractReferenceMap.ReferenceEntry<K, V> e = this.entry;
/*  786 */         int i = this.index;
/*  787 */         while (e == null && i > 0) {
/*  788 */           i--;
/*  789 */           e = (AbstractReferenceMap.ReferenceEntry<K, V>)this.parent.data[i];
/*      */         } 
/*  791 */         this.entry = e;
/*  792 */         this.index = i;
/*  793 */         if (e == null) {
/*  794 */           this.currentKey = null;
/*  795 */           this.currentValue = null;
/*  796 */           return false;
/*      */         } 
/*  798 */         this.nextKey = e.getKey();
/*  799 */         this.nextValue = e.getValue();
/*  800 */         if (nextNull()) {
/*  801 */           this.entry = this.entry.next();
/*      */         }
/*      */       } 
/*  804 */       return true;
/*      */     }
/*      */     
/*      */     private void checkMod() {
/*  808 */       if (this.parent.modCount != this.expectedModCount) {
/*  809 */         throw new ConcurrentModificationException();
/*      */       }
/*      */     }
/*      */     
/*      */     private boolean nextNull() {
/*  814 */       return (this.nextKey == null || this.nextValue == null);
/*      */     }
/*      */     
/*      */     protected AbstractReferenceMap.ReferenceEntry<K, V> nextEntry() {
/*  818 */       checkMod();
/*  819 */       if (nextNull() && !hasNext()) {
/*  820 */         throw new NoSuchElementException();
/*      */       }
/*  822 */       this.previous = this.entry;
/*  823 */       this.entry = this.entry.next();
/*  824 */       this.currentKey = this.nextKey;
/*  825 */       this.currentValue = this.nextValue;
/*  826 */       this.nextKey = null;
/*  827 */       this.nextValue = null;
/*  828 */       return this.previous;
/*      */     }
/*      */     
/*      */     protected AbstractReferenceMap.ReferenceEntry<K, V> currentEntry() {
/*  832 */       checkMod();
/*  833 */       return this.previous;
/*      */     }
/*      */     
/*      */     public void remove() {
/*  837 */       checkMod();
/*  838 */       if (this.previous == null) {
/*  839 */         throw new IllegalStateException();
/*      */       }
/*  841 */       this.parent.remove(this.currentKey);
/*  842 */       this.previous = null;
/*  843 */       this.currentKey = null;
/*  844 */       this.currentValue = null;
/*  845 */       this.expectedModCount = this.parent.modCount;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class ReferenceEntrySetIterator<K, V>
/*      */     extends ReferenceBaseIterator<K, V>
/*      */     implements Iterator<Map.Entry<K, V>>
/*      */   {
/*      */     public ReferenceEntrySetIterator(AbstractReferenceMap<K, V> parent) {
/*  856 */       super(parent);
/*      */     }
/*      */     
/*      */     public Map.Entry<K, V> next() {
/*  860 */       return nextEntry();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class ReferenceKeySetIterator<K>
/*      */     extends ReferenceBaseIterator<K, Object>
/*      */     implements Iterator<K>
/*      */   {
/*      */     ReferenceKeySetIterator(AbstractReferenceMap<K, ?> parent) {
/*  872 */       super((AbstractReferenceMap)parent);
/*      */     }
/*      */     
/*      */     public K next() {
/*  876 */       return nextEntry().getKey();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class ReferenceValuesIterator<V>
/*      */     extends ReferenceBaseIterator<Object, V>
/*      */     implements Iterator<V>
/*      */   {
/*      */     ReferenceValuesIterator(AbstractReferenceMap<?, V> parent) {
/*  887 */       super((AbstractReferenceMap)parent);
/*      */     }
/*      */     
/*      */     public V next() {
/*  891 */       return nextEntry().getValue();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class ReferenceMapIterator<K, V>
/*      */     extends ReferenceBaseIterator<K, V>
/*      */     implements MapIterator<K, V>
/*      */   {
/*      */     protected ReferenceMapIterator(AbstractReferenceMap<K, V> parent) {
/*  901 */       super(parent);
/*      */     }
/*      */     
/*      */     public K next() {
/*  905 */       return nextEntry().getKey();
/*      */     }
/*      */     
/*      */     public K getKey() {
/*  909 */       AbstractHashedMap.HashEntry<K, V> current = currentEntry();
/*  910 */       if (current == null) {
/*  911 */         throw new IllegalStateException("getKey() can only be called after next() and before remove()");
/*      */       }
/*  913 */       return current.getKey();
/*      */     }
/*      */     
/*      */     public V getValue() {
/*  917 */       AbstractHashedMap.HashEntry<K, V> current = currentEntry();
/*  918 */       if (current == null) {
/*  919 */         throw new IllegalStateException("getValue() can only be called after next() and before remove()");
/*      */       }
/*  921 */       return current.getValue();
/*      */     }
/*      */     
/*      */     public V setValue(V value) {
/*  925 */       AbstractHashedMap.HashEntry<K, V> current = currentEntry();
/*  926 */       if (current == null) {
/*  927 */         throw new IllegalStateException("setValue() can only be called after next() and before remove()");
/*      */       }
/*  929 */       return current.setValue(value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class SoftRef<T>
/*      */     extends SoftReference<T>
/*      */   {
/*      */     private final int hash;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SoftRef(int hash, T r, ReferenceQueue<? super T> q) {
/*  946 */       super(r, q);
/*  947 */       this.hash = hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  952 */       return this.hash;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class WeakRef<T>
/*      */     extends WeakReference<T>
/*      */   {
/*      */     private final int hash;
/*      */ 
/*      */     
/*      */     public WeakRef(int hash, T r, ReferenceQueue<? super T> q) {
/*  964 */       super(r, q);
/*  965 */       this.hash = hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  970 */       return this.hash;
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
/*  996 */     out.writeInt(this.keyType.value);
/*  997 */     out.writeInt(this.valueType.value);
/*  998 */     out.writeBoolean(this.purgeValues);
/*  999 */     out.writeFloat(this.loadFactor);
/* 1000 */     out.writeInt(this.data.length);
/* 1001 */     for (MapIterator<K, V> it = mapIterator(); it.hasNext(); ) {
/* 1002 */       out.writeObject(it.next());
/* 1003 */       out.writeObject(it.getValue());
/*      */     } 
/* 1005 */     out.writeObject(null);
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
/*      */   
/*      */   protected void doReadObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 1031 */     this.keyType = ReferenceStrength.resolve(in.readInt());
/* 1032 */     this.valueType = ReferenceStrength.resolve(in.readInt());
/* 1033 */     this.purgeValues = in.readBoolean();
/* 1034 */     this.loadFactor = in.readFloat();
/* 1035 */     int capacity = in.readInt();
/* 1036 */     init();
/* 1037 */     this.data = (AbstractHashedMap.HashEntry<K, V>[])new AbstractHashedMap.HashEntry[capacity];
/*      */     while (true) {
/* 1039 */       K key = (K)in.readObject();
/* 1040 */       if (key == null) {
/*      */         break;
/*      */       }
/* 1043 */       V value = (V)in.readObject();
/* 1044 */       put(key, value);
/*      */     } 
/* 1046 */     this.threshold = calculateThreshold(this.data.length, this.loadFactor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isKeyType(ReferenceStrength type) {
/* 1056 */     return (this.keyType == type);
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\AbstractReferenceMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */