/*      */ package gnu.trove.map.hash;
/*      */ 
/*      */ import gnu.trove.TIntCollection;
/*      */ import gnu.trove.TLongCollection;
/*      */ import gnu.trove.function.TLongFunction;
/*      */ import gnu.trove.impl.HashFunctions;
/*      */ import gnu.trove.impl.hash.THashPrimitiveIterator;
/*      */ import gnu.trove.impl.hash.TIntLongHash;
/*      */ import gnu.trove.impl.hash.TPrimitiveHash;
/*      */ import gnu.trove.iterator.TIntIterator;
/*      */ import gnu.trove.iterator.TIntLongIterator;
/*      */ import gnu.trove.iterator.TLongIterator;
/*      */ import gnu.trove.map.TIntLongMap;
/*      */ import gnu.trove.procedure.TIntLongProcedure;
/*      */ import gnu.trove.procedure.TIntProcedure;
/*      */ import gnu.trove.procedure.TLongProcedure;
/*      */ import gnu.trove.set.TIntSet;
/*      */ import java.io.Externalizable;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInput;
/*      */ import java.io.ObjectOutput;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Map;
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
/*      */ public class TIntLongHashMap
/*      */   extends TIntLongHash
/*      */   implements TIntLongMap, Externalizable
/*      */ {
/*      */   static final long serialVersionUID = 1L;
/*      */   protected transient long[] _values;
/*      */   
/*      */   public TIntLongHashMap() {}
/*      */   
/*      */   public TIntLongHashMap(int initialCapacity) {
/*   73 */     super(initialCapacity);
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
/*      */   public TIntLongHashMap(int initialCapacity, float loadFactor) {
/*   86 */     super(initialCapacity, loadFactor);
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
/*      */   public TIntLongHashMap(int initialCapacity, float loadFactor, int noEntryKey, long noEntryValue) {
/*  104 */     super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
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
/*      */   public TIntLongHashMap(int[] keys, long[] values) {
/*  116 */     super(Math.max(keys.length, values.length));
/*      */     
/*  118 */     int size = Math.min(keys.length, values.length);
/*  119 */     for (int i = 0; i < size; i++) {
/*  120 */       put(keys[i], values[i]);
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
/*      */   public TIntLongHashMap(TIntLongMap map) {
/*  132 */     super(map.size());
/*  133 */     if (map instanceof TIntLongHashMap) {
/*  134 */       TIntLongHashMap hashmap = (TIntLongHashMap)map;
/*  135 */       this._loadFactor = hashmap._loadFactor;
/*  136 */       this.no_entry_key = hashmap.no_entry_key;
/*  137 */       this.no_entry_value = hashmap.no_entry_value;
/*      */       
/*  139 */       if (this.no_entry_key != 0) {
/*  140 */         Arrays.fill(this._set, this.no_entry_key);
/*      */       }
/*      */       
/*  143 */       if (this.no_entry_value != 0L) {
/*  144 */         Arrays.fill(this._values, this.no_entry_value);
/*      */       }
/*  146 */       setUp((int)Math.ceil((10.0F / this._loadFactor)));
/*      */     } 
/*  148 */     putAll(map);
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
/*      */   protected int setUp(int initialCapacity) {
/*  162 */     int capacity = super.setUp(initialCapacity);
/*  163 */     this._values = new long[capacity];
/*  164 */     return capacity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void rehash(int newCapacity) {
/*  175 */     int oldCapacity = this._set.length;
/*      */     
/*  177 */     int[] oldKeys = this._set;
/*  178 */     long[] oldVals = this._values;
/*  179 */     byte[] oldStates = this._states;
/*      */     
/*  181 */     this._set = new int[newCapacity];
/*  182 */     this._values = new long[newCapacity];
/*  183 */     this._states = new byte[newCapacity];
/*      */     
/*  185 */     for (int i = oldCapacity; i-- > 0;) {
/*  186 */       if (oldStates[i] == 1) {
/*  187 */         int o = oldKeys[i];
/*  188 */         int index = insertKey(o);
/*  189 */         this._values[index] = oldVals[i];
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long put(int key, long value) {
/*  197 */     int index = insertKey(key);
/*  198 */     return doPut(key, value, index);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long putIfAbsent(int key, long value) {
/*  204 */     int index = insertKey(key);
/*  205 */     if (index < 0)
/*  206 */       return this._values[-index - 1]; 
/*  207 */     return doPut(key, value, index);
/*      */   }
/*      */ 
/*      */   
/*      */   private long doPut(int key, long value, int index) {
/*  212 */     long previous = this.no_entry_value;
/*  213 */     boolean isNewMapping = true;
/*  214 */     if (index < 0) {
/*  215 */       index = -index - 1;
/*  216 */       previous = this._values[index];
/*  217 */       isNewMapping = false;
/*      */     } 
/*  219 */     this._values[index] = value;
/*      */     
/*  221 */     if (isNewMapping) {
/*  222 */       postInsertHook(this.consumeFreeSlot);
/*      */     }
/*      */     
/*  225 */     return previous;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void putAll(Map<? extends Integer, ? extends Long> map) {
/*  231 */     ensureCapacity(map.size());
/*      */     
/*  233 */     for (Map.Entry<? extends Integer, ? extends Long> entry : map.entrySet()) {
/*  234 */       put(((Integer)entry.getKey()).intValue(), ((Long)entry.getValue()).longValue());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void putAll(TIntLongMap map) {
/*  241 */     ensureCapacity(map.size());
/*  242 */     TIntLongIterator iter = map.iterator();
/*  243 */     while (iter.hasNext()) {
/*  244 */       iter.advance();
/*  245 */       put(iter.key(), iter.value());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long get(int key) {
/*  252 */     int index = index(key);
/*  253 */     return (index < 0) ? this.no_entry_value : this._values[index];
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  259 */     super.clear();
/*  260 */     Arrays.fill(this._set, 0, this._set.length, this.no_entry_key);
/*  261 */     Arrays.fill(this._values, 0, this._values.length, this.no_entry_value);
/*  262 */     Arrays.fill(this._states, 0, this._states.length, (byte)0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/*  268 */     return (0 == this._size);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long remove(int key) {
/*  274 */     long prev = this.no_entry_value;
/*  275 */     int index = index(key);
/*  276 */     if (index >= 0) {
/*  277 */       prev = this._values[index];
/*  278 */       removeAt(index);
/*      */     } 
/*  280 */     return prev;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeAt(int index) {
/*  286 */     this._values[index] = this.no_entry_value;
/*  287 */     super.removeAt(index);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public TIntSet keySet() {
/*  293 */     return new TKeyView();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] keys() {
/*  299 */     int[] keys = new int[size()];
/*  300 */     int[] k = this._set;
/*  301 */     byte[] states = this._states;
/*      */     
/*  303 */     for (int i = k.length, j = 0; i-- > 0;) {
/*  304 */       if (states[i] == 1) {
/*  305 */         keys[j++] = k[i];
/*      */       }
/*      */     } 
/*  308 */     return keys;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] keys(int[] array) {
/*  314 */     int size = size();
/*  315 */     if (array.length < size) {
/*  316 */       array = new int[size];
/*      */     }
/*      */     
/*  319 */     int[] keys = this._set;
/*  320 */     byte[] states = this._states;
/*      */     
/*  322 */     for (int i = keys.length, j = 0; i-- > 0;) {
/*  323 */       if (states[i] == 1) {
/*  324 */         array[j++] = keys[i];
/*      */       }
/*      */     } 
/*  327 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public TLongCollection valueCollection() {
/*  333 */     return new TValueView();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long[] values() {
/*  339 */     long[] vals = new long[size()];
/*  340 */     long[] v = this._values;
/*  341 */     byte[] states = this._states;
/*      */     
/*  343 */     for (int i = v.length, j = 0; i-- > 0;) {
/*  344 */       if (states[i] == 1) {
/*  345 */         vals[j++] = v[i];
/*      */       }
/*      */     } 
/*  348 */     return vals;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long[] values(long[] array) {
/*  354 */     int size = size();
/*  355 */     if (array.length < size) {
/*  356 */       array = new long[size];
/*      */     }
/*      */     
/*  359 */     long[] v = this._values;
/*  360 */     byte[] states = this._states;
/*      */     
/*  362 */     for (int i = v.length, j = 0; i-- > 0;) {
/*  363 */       if (states[i] == 1) {
/*  364 */         array[j++] = v[i];
/*      */       }
/*      */     } 
/*  367 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsValue(long val) {
/*  373 */     byte[] states = this._states;
/*  374 */     long[] vals = this._values;
/*      */     
/*  376 */     for (int i = vals.length; i-- > 0;) {
/*  377 */       if (states[i] == 1 && val == vals[i]) {
/*  378 */         return true;
/*      */       }
/*      */     } 
/*  381 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsKey(int key) {
/*  387 */     return contains(key);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public TIntLongIterator iterator() {
/*  393 */     return new TIntLongHashIterator(this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean forEachKey(TIntProcedure procedure) {
/*  399 */     return forEach(procedure);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean forEachValue(TLongProcedure procedure) {
/*  405 */     byte[] states = this._states;
/*  406 */     long[] values = this._values;
/*  407 */     for (int i = values.length; i-- > 0;) {
/*  408 */       if (states[i] == 1 && !procedure.execute(values[i])) {
/*  409 */         return false;
/*      */       }
/*      */     } 
/*  412 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean forEachEntry(TIntLongProcedure procedure) {
/*  418 */     byte[] states = this._states;
/*  419 */     int[] keys = this._set;
/*  420 */     long[] values = this._values;
/*  421 */     for (int i = keys.length; i-- > 0;) {
/*  422 */       if (states[i] == 1 && !procedure.execute(keys[i], values[i])) {
/*  423 */         return false;
/*      */       }
/*      */     } 
/*  426 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void transformValues(TLongFunction function) {
/*  432 */     byte[] states = this._states;
/*  433 */     long[] values = this._values;
/*  434 */     for (int i = values.length; i-- > 0;) {
/*  435 */       if (states[i] == 1) {
/*  436 */         values[i] = function.execute(values[i]);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean retainEntries(TIntLongProcedure procedure) {
/*  444 */     boolean modified = false;
/*  445 */     byte[] states = this._states;
/*  446 */     int[] keys = this._set;
/*  447 */     long[] values = this._values;
/*      */ 
/*      */ 
/*      */     
/*  451 */     tempDisableAutoCompaction();
/*      */     try {
/*  453 */       for (int i = keys.length; i-- > 0;) {
/*  454 */         if (states[i] == 1 && !procedure.execute(keys[i], values[i])) {
/*  455 */           removeAt(i);
/*  456 */           modified = true;
/*      */         } 
/*      */       } 
/*      */     } finally {
/*      */       
/*  461 */       reenableAutoCompaction(true);
/*      */     } 
/*      */     
/*  464 */     return modified;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean increment(int key) {
/*  470 */     return adjustValue(key, 1L);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean adjustValue(int key, long amount) {
/*  476 */     int index = index(key);
/*  477 */     if (index < 0) {
/*  478 */       return false;
/*      */     }
/*  480 */     this._values[index] = this._values[index] + amount;
/*  481 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long adjustOrPutValue(int key, long adjust_amount, long put_amount) {
/*  488 */     int index = insertKey(key);
/*      */ 
/*      */ 
/*      */     
/*  492 */     index = -index - 1;
/*  493 */     long newValue = this._values[index] = this._values[index] + adjust_amount;
/*  494 */     boolean isNewMapping = false;
/*      */     
/*  496 */     newValue = this._values[index] = put_amount;
/*  497 */     isNewMapping = true;
/*      */ 
/*      */     
/*  500 */     byte previousState = this._states[index];
/*      */     
/*  502 */     if (isNewMapping) {
/*  503 */       postInsertHook(this.consumeFreeSlot);
/*      */     }
/*      */     
/*  506 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected class TKeyView
/*      */     implements TIntSet
/*      */   {
/*      */     public TIntIterator iterator() {
/*  515 */       return new TIntLongHashMap.TIntLongKeyHashIterator((TPrimitiveHash)TIntLongHashMap.this);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int getNoEntryValue() {
/*  521 */       return TIntLongHashMap.this.no_entry_key;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int size() {
/*  527 */       return TIntLongHashMap.this._size;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  533 */       return (0 == TIntLongHashMap.this._size);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(int entry) {
/*  539 */       return TIntLongHashMap.this.contains(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int[] toArray() {
/*  545 */       return TIntLongHashMap.this.keys();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int[] toArray(int[] dest) {
/*  551 */       return TIntLongHashMap.this.keys(dest);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean add(int entry) {
/*  561 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(int entry) {
/*  567 */       return (TIntLongHashMap.this.no_entry_value != TIntLongHashMap.this.remove(entry));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> collection) {
/*  573 */       for (Object element : collection) {
/*  574 */         if (element instanceof Integer) {
/*  575 */           int ele = ((Integer)element).intValue();
/*  576 */           if (!TIntLongHashMap.this.containsKey(ele))
/*  577 */             return false; 
/*      */           continue;
/*      */         } 
/*  580 */         return false;
/*      */       } 
/*      */       
/*  583 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsAll(TIntCollection collection) {
/*  589 */       TIntIterator iter = collection.iterator();
/*  590 */       while (iter.hasNext()) {
/*  591 */         if (!TIntLongHashMap.this.containsKey(iter.next())) {
/*  592 */           return false;
/*      */         }
/*      */       } 
/*  595 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsAll(int[] array) {
/*  601 */       for (int element : array) {
/*  602 */         if (!TIntLongHashMap.this.contains(element)) {
/*  603 */           return false;
/*      */         }
/*      */       } 
/*  606 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends Integer> collection) {
/*  616 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addAll(TIntCollection collection) {
/*  626 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addAll(int[] array) {
/*  636 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> collection) {
/*  643 */       boolean modified = false;
/*  644 */       TIntIterator iter = iterator();
/*  645 */       while (iter.hasNext()) {
/*  646 */         if (!collection.contains(Integer.valueOf(iter.next()))) {
/*  647 */           iter.remove();
/*  648 */           modified = true;
/*      */         } 
/*      */       } 
/*  651 */       return modified;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean retainAll(TIntCollection collection) {
/*  657 */       if (this == collection) {
/*  658 */         return false;
/*      */       }
/*  660 */       boolean modified = false;
/*  661 */       TIntIterator iter = iterator();
/*  662 */       while (iter.hasNext()) {
/*  663 */         if (!collection.contains(iter.next())) {
/*  664 */           iter.remove();
/*  665 */           modified = true;
/*      */         } 
/*      */       } 
/*  668 */       return modified;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean retainAll(int[] array) {
/*  674 */       boolean changed = false;
/*  675 */       Arrays.sort(array);
/*  676 */       int[] set = TIntLongHashMap.this._set;
/*  677 */       byte[] states = TIntLongHashMap.this._states;
/*      */       
/*  679 */       for (int i = set.length; i-- > 0;) {
/*  680 */         if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
/*  681 */           TIntLongHashMap.this.removeAt(i);
/*  682 */           changed = true;
/*      */         } 
/*      */       } 
/*  685 */       return changed;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> collection) {
/*  691 */       boolean changed = false;
/*  692 */       for (Object element : collection) {
/*  693 */         if (element instanceof Integer) {
/*  694 */           int c = ((Integer)element).intValue();
/*  695 */           if (remove(c)) {
/*  696 */             changed = true;
/*      */           }
/*      */         } 
/*      */       } 
/*  700 */       return changed;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean removeAll(TIntCollection collection) {
/*  706 */       if (this == collection) {
/*  707 */         clear();
/*  708 */         return true;
/*      */       } 
/*  710 */       boolean changed = false;
/*  711 */       TIntIterator iter = collection.iterator();
/*  712 */       while (iter.hasNext()) {
/*  713 */         int element = iter.next();
/*  714 */         if (remove(element)) {
/*  715 */           changed = true;
/*      */         }
/*      */       } 
/*  718 */       return changed;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean removeAll(int[] array) {
/*  724 */       boolean changed = false;
/*  725 */       for (int i = array.length; i-- > 0;) {
/*  726 */         if (remove(array[i])) {
/*  727 */           changed = true;
/*      */         }
/*      */       } 
/*  730 */       return changed;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/*  736 */       TIntLongHashMap.this.clear();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean forEach(TIntProcedure procedure) {
/*  742 */       return TIntLongHashMap.this.forEachKey(procedure);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/*  748 */       if (!(other instanceof TIntSet)) {
/*  749 */         return false;
/*      */       }
/*  751 */       TIntSet that = (TIntSet)other;
/*  752 */       if (that.size() != size()) {
/*  753 */         return false;
/*      */       }
/*  755 */       for (int i = TIntLongHashMap.this._states.length; i-- > 0;) {
/*  756 */         if (TIntLongHashMap.this._states[i] == 1 && 
/*  757 */           !that.contains(TIntLongHashMap.this._set[i])) {
/*  758 */           return false;
/*      */         }
/*      */       } 
/*      */       
/*  762 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  768 */       int hashcode = 0;
/*  769 */       for (int i = TIntLongHashMap.this._states.length; i-- > 0;) {
/*  770 */         if (TIntLongHashMap.this._states[i] == 1) {
/*  771 */           hashcode += HashFunctions.hash(TIntLongHashMap.this._set[i]);
/*      */         }
/*      */       } 
/*  774 */       return hashcode;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  780 */       final StringBuilder buf = new StringBuilder("{");
/*  781 */       TIntLongHashMap.this.forEachKey(new TIntProcedure()
/*      */           {
/*      */             private boolean first = true;
/*      */             
/*      */             public boolean execute(int key) {
/*  786 */               if (this.first) {
/*  787 */                 this.first = false;
/*      */               } else {
/*  789 */                 buf.append(", ");
/*      */               } 
/*      */               
/*  792 */               buf.append(key);
/*  793 */               return true;
/*      */             }
/*      */           });
/*  796 */       buf.append("}");
/*  797 */       return buf.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected class TValueView
/*      */     implements TLongCollection
/*      */   {
/*      */     public TLongIterator iterator() {
/*  807 */       return new TIntLongHashMap.TIntLongValueHashIterator((TPrimitiveHash)TIntLongHashMap.this);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public long getNoEntryValue() {
/*  813 */       return TIntLongHashMap.this.no_entry_value;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int size() {
/*  819 */       return TIntLongHashMap.this._size;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  825 */       return (0 == TIntLongHashMap.this._size);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(long entry) {
/*  831 */       return TIntLongHashMap.this.containsValue(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public long[] toArray() {
/*  837 */       return TIntLongHashMap.this.values();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public long[] toArray(long[] dest) {
/*  843 */       return TIntLongHashMap.this.values(dest);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean add(long entry) {
/*  849 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(long entry) {
/*  855 */       long[] values = TIntLongHashMap.this._values;
/*  856 */       int[] set = TIntLongHashMap.this._set;
/*      */       
/*  858 */       for (int i = values.length; i-- > 0;) {
/*  859 */         if (set[i] != 0 && set[i] != 2 && entry == values[i]) {
/*  860 */           TIntLongHashMap.this.removeAt(i);
/*  861 */           return true;
/*      */         } 
/*      */       } 
/*  864 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> collection) {
/*  870 */       for (Object element : collection) {
/*  871 */         if (element instanceof Long) {
/*  872 */           long ele = ((Long)element).longValue();
/*  873 */           if (!TIntLongHashMap.this.containsValue(ele))
/*  874 */             return false; 
/*      */           continue;
/*      */         } 
/*  877 */         return false;
/*      */       } 
/*      */       
/*  880 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsAll(TLongCollection collection) {
/*  886 */       TLongIterator iter = collection.iterator();
/*  887 */       while (iter.hasNext()) {
/*  888 */         if (!TIntLongHashMap.this.containsValue(iter.next())) {
/*  889 */           return false;
/*      */         }
/*      */       } 
/*  892 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsAll(long[] array) {
/*  898 */       for (long element : array) {
/*  899 */         if (!TIntLongHashMap.this.containsValue(element)) {
/*  900 */           return false;
/*      */         }
/*      */       } 
/*  903 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends Long> collection) {
/*  909 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addAll(TLongCollection collection) {
/*  915 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addAll(long[] array) {
/*  921 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> collection) {
/*  928 */       boolean modified = false;
/*  929 */       TLongIterator iter = iterator();
/*  930 */       while (iter.hasNext()) {
/*  931 */         if (!collection.contains(Long.valueOf(iter.next()))) {
/*  932 */           iter.remove();
/*  933 */           modified = true;
/*      */         } 
/*      */       } 
/*  936 */       return modified;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean retainAll(TLongCollection collection) {
/*  942 */       if (this == collection) {
/*  943 */         return false;
/*      */       }
/*  945 */       boolean modified = false;
/*  946 */       TLongIterator iter = iterator();
/*  947 */       while (iter.hasNext()) {
/*  948 */         if (!collection.contains(iter.next())) {
/*  949 */           iter.remove();
/*  950 */           modified = true;
/*      */         } 
/*      */       } 
/*  953 */       return modified;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean retainAll(long[] array) {
/*  959 */       boolean changed = false;
/*  960 */       Arrays.sort(array);
/*  961 */       long[] values = TIntLongHashMap.this._values;
/*  962 */       byte[] states = TIntLongHashMap.this._states;
/*      */       
/*  964 */       for (int i = values.length; i-- > 0;) {
/*  965 */         if (states[i] == 1 && Arrays.binarySearch(array, values[i]) < 0) {
/*  966 */           TIntLongHashMap.this.removeAt(i);
/*  967 */           changed = true;
/*      */         } 
/*      */       } 
/*  970 */       return changed;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> collection) {
/*  976 */       boolean changed = false;
/*  977 */       for (Object element : collection) {
/*  978 */         if (element instanceof Long) {
/*  979 */           long c = ((Long)element).longValue();
/*  980 */           if (remove(c)) {
/*  981 */             changed = true;
/*      */           }
/*      */         } 
/*      */       } 
/*  985 */       return changed;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean removeAll(TLongCollection collection) {
/*  991 */       if (this == collection) {
/*  992 */         clear();
/*  993 */         return true;
/*      */       } 
/*  995 */       boolean changed = false;
/*  996 */       TLongIterator iter = collection.iterator();
/*  997 */       while (iter.hasNext()) {
/*  998 */         long element = iter.next();
/*  999 */         if (remove(element)) {
/* 1000 */           changed = true;
/*      */         }
/*      */       } 
/* 1003 */       return changed;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean removeAll(long[] array) {
/* 1009 */       boolean changed = false;
/* 1010 */       for (int i = array.length; i-- > 0;) {
/* 1011 */         if (remove(array[i])) {
/* 1012 */           changed = true;
/*      */         }
/*      */       } 
/* 1015 */       return changed;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1021 */       TIntLongHashMap.this.clear();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean forEach(TLongProcedure procedure) {
/* 1027 */       return TIntLongHashMap.this.forEachValue(procedure);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1034 */       final StringBuilder buf = new StringBuilder("{");
/* 1035 */       TIntLongHashMap.this.forEachValue(new TLongProcedure() {
/*      */             private boolean first = true;
/*      */             
/*      */             public boolean execute(long value) {
/* 1039 */               if (this.first) {
/* 1040 */                 this.first = false;
/*      */               } else {
/* 1042 */                 buf.append(", ");
/*      */               } 
/*      */               
/* 1045 */               buf.append(value);
/* 1046 */               return true;
/*      */             }
/*      */           });
/* 1049 */       buf.append("}");
/* 1050 */       return buf.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class TIntLongKeyHashIterator
/*      */     extends THashPrimitiveIterator
/*      */     implements TIntIterator
/*      */   {
/*      */     TIntLongKeyHashIterator(TPrimitiveHash hash) {
/* 1063 */       super(hash);
/*      */     }
/*      */ 
/*      */     
/*      */     public int next() {
/* 1068 */       moveToNextIndex();
/* 1069 */       return TIntLongHashMap.this._set[this._index];
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1074 */       if (this._expectedSize != this._hash.size()) {
/* 1075 */         throw new ConcurrentModificationException();
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/* 1080 */         this._hash.tempDisableAutoCompaction();
/* 1081 */         TIntLongHashMap.this.removeAt(this._index);
/*      */       } finally {
/*      */         
/* 1084 */         this._hash.reenableAutoCompaction(false);
/*      */       } 
/*      */       
/* 1087 */       this._expectedSize--;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class TIntLongValueHashIterator
/*      */     extends THashPrimitiveIterator
/*      */     implements TLongIterator
/*      */   {
/*      */     TIntLongValueHashIterator(TPrimitiveHash hash) {
/* 1101 */       super(hash);
/*      */     }
/*      */ 
/*      */     
/*      */     public long next() {
/* 1106 */       moveToNextIndex();
/* 1107 */       return TIntLongHashMap.this._values[this._index];
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1112 */       if (this._expectedSize != this._hash.size()) {
/* 1113 */         throw new ConcurrentModificationException();
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/* 1118 */         this._hash.tempDisableAutoCompaction();
/* 1119 */         TIntLongHashMap.this.removeAt(this._index);
/*      */       } finally {
/*      */         
/* 1122 */         this._hash.reenableAutoCompaction(false);
/*      */       } 
/*      */       
/* 1125 */       this._expectedSize--;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class TIntLongHashIterator
/*      */     extends THashPrimitiveIterator
/*      */     implements TIntLongIterator
/*      */   {
/*      */     TIntLongHashIterator(TIntLongHashMap map) {
/* 1138 */       super((TPrimitiveHash)map);
/*      */     }
/*      */ 
/*      */     
/*      */     public void advance() {
/* 1143 */       moveToNextIndex();
/*      */     }
/*      */ 
/*      */     
/*      */     public int key() {
/* 1148 */       return TIntLongHashMap.this._set[this._index];
/*      */     }
/*      */ 
/*      */     
/*      */     public long value() {
/* 1153 */       return TIntLongHashMap.this._values[this._index];
/*      */     }
/*      */ 
/*      */     
/*      */     public long setValue(long val) {
/* 1158 */       long old = value();
/* 1159 */       TIntLongHashMap.this._values[this._index] = val;
/* 1160 */       return old;
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1165 */       if (this._expectedSize != this._hash.size()) {
/* 1166 */         throw new ConcurrentModificationException();
/*      */       }
/*      */       
/*      */       try {
/* 1170 */         this._hash.tempDisableAutoCompaction();
/* 1171 */         TIntLongHashMap.this.removeAt(this._index);
/*      */       } finally {
/*      */         
/* 1174 */         this._hash.reenableAutoCompaction(false);
/*      */       } 
/* 1176 */       this._expectedSize--;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object other) {
/* 1184 */     if (!(other instanceof TIntLongMap)) {
/* 1185 */       return false;
/*      */     }
/* 1187 */     TIntLongMap that = (TIntLongMap)other;
/* 1188 */     if (that.size() != size()) {
/* 1189 */       return false;
/*      */     }
/* 1191 */     long[] values = this._values;
/* 1192 */     byte[] states = this._states;
/* 1193 */     long this_no_entry_value = getNoEntryValue();
/* 1194 */     long that_no_entry_value = that.getNoEntryValue();
/* 1195 */     for (int i = values.length; i-- > 0;) {
/* 1196 */       if (states[i] == 1) {
/* 1197 */         int key = this._set[i];
/* 1198 */         long that_value = that.get(key);
/* 1199 */         long this_value = values[i];
/* 1200 */         if (this_value != that_value && this_value != this_no_entry_value && that_value != that_no_entry_value)
/*      */         {
/*      */           
/* 1203 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/* 1207 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1214 */     int hashcode = 0;
/* 1215 */     byte[] states = this._states;
/* 1216 */     for (int i = this._values.length; i-- > 0;) {
/* 1217 */       if (states[i] == 1) {
/* 1218 */         hashcode += HashFunctions.hash(this._set[i]) ^ HashFunctions.hash(this._values[i]);
/*      */       }
/*      */     } 
/*      */     
/* 1222 */     return hashcode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1229 */     final StringBuilder buf = new StringBuilder("{");
/* 1230 */     forEachEntry(new TIntLongProcedure()
/*      */         {
/*      */           public boolean execute(int key, long value) {
/* 1233 */             if (this.first) { this.first = false; }
/* 1234 */             else { buf.append(", "); }
/*      */             
/* 1236 */             buf.append(key);
/* 1237 */             buf.append("=");
/* 1238 */             buf.append(value);
/* 1239 */             return true;
/*      */           } private boolean first = true;
/*      */         });
/* 1242 */     buf.append("}");
/* 1243 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeExternal(ObjectOutput out) throws IOException {
/* 1250 */     out.writeByte(0);
/*      */ 
/*      */     
/* 1253 */     super.writeExternal(out);
/*      */ 
/*      */     
/* 1256 */     out.writeInt(this._size);
/*      */ 
/*      */     
/* 1259 */     for (int i = this._states.length; i-- > 0;) {
/* 1260 */       if (this._states[i] == 1) {
/* 1261 */         out.writeInt(this._set[i]);
/* 1262 */         out.writeLong(this._values[i]);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/* 1271 */     in.readByte();
/*      */ 
/*      */     
/* 1274 */     super.readExternal(in);
/*      */ 
/*      */     
/* 1277 */     int size = in.readInt();
/* 1278 */     setUp(size);
/*      */ 
/*      */     
/* 1281 */     while (size-- > 0) {
/* 1282 */       int key = in.readInt();
/* 1283 */       long val = in.readLong();
/* 1284 */       put(key, val);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\map\hash\TIntLongHashMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */