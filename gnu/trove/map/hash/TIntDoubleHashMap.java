/*      */ package gnu.trove.map.hash;
/*      */ 
/*      */ import gnu.trove.TDoubleCollection;
/*      */ import gnu.trove.TIntCollection;
/*      */ import gnu.trove.function.TDoubleFunction;
/*      */ import gnu.trove.impl.HashFunctions;
/*      */ import gnu.trove.impl.hash.THashPrimitiveIterator;
/*      */ import gnu.trove.impl.hash.TIntDoubleHash;
/*      */ import gnu.trove.impl.hash.TPrimitiveHash;
/*      */ import gnu.trove.iterator.TDoubleIterator;
/*      */ import gnu.trove.iterator.TIntDoubleIterator;
/*      */ import gnu.trove.iterator.TIntIterator;
/*      */ import gnu.trove.map.TIntDoubleMap;
/*      */ import gnu.trove.procedure.TDoubleProcedure;
/*      */ import gnu.trove.procedure.TIntDoubleProcedure;
/*      */ import gnu.trove.procedure.TIntProcedure;
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
/*      */ public class TIntDoubleHashMap
/*      */   extends TIntDoubleHash
/*      */   implements TIntDoubleMap, Externalizable
/*      */ {
/*      */   static final long serialVersionUID = 1L;
/*      */   protected transient double[] _values;
/*      */   
/*      */   public TIntDoubleHashMap() {}
/*      */   
/*      */   public TIntDoubleHashMap(int initialCapacity) {
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
/*      */   public TIntDoubleHashMap(int initialCapacity, float loadFactor) {
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
/*      */   public TIntDoubleHashMap(int initialCapacity, float loadFactor, int noEntryKey, double noEntryValue) {
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
/*      */   public TIntDoubleHashMap(int[] keys, double[] values) {
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
/*      */   public TIntDoubleHashMap(TIntDoubleMap map) {
/*  132 */     super(map.size());
/*  133 */     if (map instanceof TIntDoubleHashMap) {
/*  134 */       TIntDoubleHashMap hashmap = (TIntDoubleHashMap)map;
/*  135 */       this._loadFactor = hashmap._loadFactor;
/*  136 */       this.no_entry_key = hashmap.no_entry_key;
/*  137 */       this.no_entry_value = hashmap.no_entry_value;
/*      */       
/*  139 */       if (this.no_entry_key != 0) {
/*  140 */         Arrays.fill(this._set, this.no_entry_key);
/*      */       }
/*      */       
/*  143 */       if (this.no_entry_value != 0.0D) {
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
/*  163 */     this._values = new double[capacity];
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
/*  178 */     double[] oldVals = this._values;
/*  179 */     byte[] oldStates = this._states;
/*      */     
/*  181 */     this._set = new int[newCapacity];
/*  182 */     this._values = new double[newCapacity];
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
/*      */   public double put(int key, double value) {
/*  197 */     int index = insertKey(key);
/*  198 */     return doPut(key, value, index);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double putIfAbsent(int key, double value) {
/*  204 */     int index = insertKey(key);
/*  205 */     if (index < 0)
/*  206 */       return this._values[-index - 1]; 
/*  207 */     return doPut(key, value, index);
/*      */   }
/*      */ 
/*      */   
/*      */   private double doPut(int key, double value, int index) {
/*  212 */     double previous = this.no_entry_value;
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
/*      */   public void putAll(Map<? extends Integer, ? extends Double> map) {
/*  231 */     ensureCapacity(map.size());
/*      */     
/*  233 */     for (Map.Entry<? extends Integer, ? extends Double> entry : map.entrySet()) {
/*  234 */       put(((Integer)entry.getKey()).intValue(), ((Double)entry.getValue()).doubleValue());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void putAll(TIntDoubleMap map) {
/*  241 */     ensureCapacity(map.size());
/*  242 */     TIntDoubleIterator iter = map.iterator();
/*  243 */     while (iter.hasNext()) {
/*  244 */       iter.advance();
/*  245 */       put(iter.key(), iter.value());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double get(int key) {
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
/*      */   public double remove(int key) {
/*  274 */     double prev = this.no_entry_value;
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
/*      */   public TDoubleCollection valueCollection() {
/*  333 */     return new TValueView();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double[] values() {
/*  339 */     double[] vals = new double[size()];
/*  340 */     double[] v = this._values;
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
/*      */   public double[] values(double[] array) {
/*  354 */     int size = size();
/*  355 */     if (array.length < size) {
/*  356 */       array = new double[size];
/*      */     }
/*      */     
/*  359 */     double[] v = this._values;
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
/*      */   public boolean containsValue(double val) {
/*  373 */     byte[] states = this._states;
/*  374 */     double[] vals = this._values;
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
/*      */   public TIntDoubleIterator iterator() {
/*  393 */     return new TIntDoubleHashIterator(this);
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
/*      */   public boolean forEachValue(TDoubleProcedure procedure) {
/*  405 */     byte[] states = this._states;
/*  406 */     double[] values = this._values;
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
/*      */   public boolean forEachEntry(TIntDoubleProcedure procedure) {
/*  418 */     byte[] states = this._states;
/*  419 */     int[] keys = this._set;
/*  420 */     double[] values = this._values;
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
/*      */   public void transformValues(TDoubleFunction function) {
/*  432 */     byte[] states = this._states;
/*  433 */     double[] values = this._values;
/*  434 */     for (int i = values.length; i-- > 0;) {
/*  435 */       if (states[i] == 1) {
/*  436 */         values[i] = function.execute(values[i]);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean retainEntries(TIntDoubleProcedure procedure) {
/*  444 */     boolean modified = false;
/*  445 */     byte[] states = this._states;
/*  446 */     int[] keys = this._set;
/*  447 */     double[] values = this._values;
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
/*  470 */     return adjustValue(key, 1.0D);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean adjustValue(int key, double amount) {
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
/*      */   public double adjustOrPutValue(int key, double adjust_amount, double put_amount) {
/*  488 */     int index = insertKey(key);
/*      */ 
/*      */ 
/*      */     
/*  492 */     index = -index - 1;
/*  493 */     double newValue = this._values[index] = this._values[index] + adjust_amount;
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
/*  515 */       return new TIntDoubleHashMap.TIntDoubleKeyHashIterator((TPrimitiveHash)TIntDoubleHashMap.this);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int getNoEntryValue() {
/*  521 */       return TIntDoubleHashMap.this.no_entry_key;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int size() {
/*  527 */       return TIntDoubleHashMap.this._size;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  533 */       return (0 == TIntDoubleHashMap.this._size);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(int entry) {
/*  539 */       return TIntDoubleHashMap.this.contains(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int[] toArray() {
/*  545 */       return TIntDoubleHashMap.this.keys();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int[] toArray(int[] dest) {
/*  551 */       return TIntDoubleHashMap.this.keys(dest);
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
/*  567 */       return (TIntDoubleHashMap.this.no_entry_value != TIntDoubleHashMap.this.remove(entry));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> collection) {
/*  573 */       for (Object element : collection) {
/*  574 */         if (element instanceof Integer) {
/*  575 */           int ele = ((Integer)element).intValue();
/*  576 */           if (!TIntDoubleHashMap.this.containsKey(ele))
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
/*  591 */         if (!TIntDoubleHashMap.this.containsKey(iter.next())) {
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
/*  602 */         if (!TIntDoubleHashMap.this.contains(element)) {
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
/*  676 */       int[] set = TIntDoubleHashMap.this._set;
/*  677 */       byte[] states = TIntDoubleHashMap.this._states;
/*      */       
/*  679 */       for (int i = set.length; i-- > 0;) {
/*  680 */         if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
/*  681 */           TIntDoubleHashMap.this.removeAt(i);
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
/*  736 */       TIntDoubleHashMap.this.clear();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean forEach(TIntProcedure procedure) {
/*  742 */       return TIntDoubleHashMap.this.forEachKey(procedure);
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
/*  755 */       for (int i = TIntDoubleHashMap.this._states.length; i-- > 0;) {
/*  756 */         if (TIntDoubleHashMap.this._states[i] == 1 && 
/*  757 */           !that.contains(TIntDoubleHashMap.this._set[i])) {
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
/*  769 */       for (int i = TIntDoubleHashMap.this._states.length; i-- > 0;) {
/*  770 */         if (TIntDoubleHashMap.this._states[i] == 1) {
/*  771 */           hashcode += HashFunctions.hash(TIntDoubleHashMap.this._set[i]);
/*      */         }
/*      */       } 
/*  774 */       return hashcode;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  780 */       final StringBuilder buf = new StringBuilder("{");
/*  781 */       TIntDoubleHashMap.this.forEachKey(new TIntProcedure()
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
/*      */     implements TDoubleCollection
/*      */   {
/*      */     public TDoubleIterator iterator() {
/*  807 */       return new TIntDoubleHashMap.TIntDoubleValueHashIterator((TPrimitiveHash)TIntDoubleHashMap.this);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public double getNoEntryValue() {
/*  813 */       return TIntDoubleHashMap.this.no_entry_value;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int size() {
/*  819 */       return TIntDoubleHashMap.this._size;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  825 */       return (0 == TIntDoubleHashMap.this._size);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(double entry) {
/*  831 */       return TIntDoubleHashMap.this.containsValue(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public double[] toArray() {
/*  837 */       return TIntDoubleHashMap.this.values();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public double[] toArray(double[] dest) {
/*  843 */       return TIntDoubleHashMap.this.values(dest);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean add(double entry) {
/*  849 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(double entry) {
/*  855 */       double[] values = TIntDoubleHashMap.this._values;
/*  856 */       int[] set = TIntDoubleHashMap.this._set;
/*      */       
/*  858 */       for (int i = values.length; i-- > 0;) {
/*  859 */         if (set[i] != 0 && set[i] != 2 && entry == values[i]) {
/*  860 */           TIntDoubleHashMap.this.removeAt(i);
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
/*  871 */         if (element instanceof Double) {
/*  872 */           double ele = ((Double)element).doubleValue();
/*  873 */           if (!TIntDoubleHashMap.this.containsValue(ele))
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
/*      */     public boolean containsAll(TDoubleCollection collection) {
/*  886 */       TDoubleIterator iter = collection.iterator();
/*  887 */       while (iter.hasNext()) {
/*  888 */         if (!TIntDoubleHashMap.this.containsValue(iter.next())) {
/*  889 */           return false;
/*      */         }
/*      */       } 
/*  892 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsAll(double[] array) {
/*  898 */       for (double element : array) {
/*  899 */         if (!TIntDoubleHashMap.this.containsValue(element)) {
/*  900 */           return false;
/*      */         }
/*      */       } 
/*  903 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends Double> collection) {
/*  909 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addAll(TDoubleCollection collection) {
/*  915 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addAll(double[] array) {
/*  921 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> collection) {
/*  928 */       boolean modified = false;
/*  929 */       TDoubleIterator iter = iterator();
/*  930 */       while (iter.hasNext()) {
/*  931 */         if (!collection.contains(Double.valueOf(iter.next()))) {
/*  932 */           iter.remove();
/*  933 */           modified = true;
/*      */         } 
/*      */       } 
/*  936 */       return modified;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean retainAll(TDoubleCollection collection) {
/*  942 */       if (this == collection) {
/*  943 */         return false;
/*      */       }
/*  945 */       boolean modified = false;
/*  946 */       TDoubleIterator iter = iterator();
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
/*      */     public boolean retainAll(double[] array) {
/*  959 */       boolean changed = false;
/*  960 */       Arrays.sort(array);
/*  961 */       double[] values = TIntDoubleHashMap.this._values;
/*  962 */       byte[] states = TIntDoubleHashMap.this._states;
/*      */       
/*  964 */       for (int i = values.length; i-- > 0;) {
/*  965 */         if (states[i] == 1 && Arrays.binarySearch(array, values[i]) < 0) {
/*  966 */           TIntDoubleHashMap.this.removeAt(i);
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
/*  978 */         if (element instanceof Double) {
/*  979 */           double c = ((Double)element).doubleValue();
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
/*      */     public boolean removeAll(TDoubleCollection collection) {
/*  991 */       if (this == collection) {
/*  992 */         clear();
/*  993 */         return true;
/*      */       } 
/*  995 */       boolean changed = false;
/*  996 */       TDoubleIterator iter = collection.iterator();
/*  997 */       while (iter.hasNext()) {
/*  998 */         double element = iter.next();
/*  999 */         if (remove(element)) {
/* 1000 */           changed = true;
/*      */         }
/*      */       } 
/* 1003 */       return changed;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean removeAll(double[] array) {
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
/* 1021 */       TIntDoubleHashMap.this.clear();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean forEach(TDoubleProcedure procedure) {
/* 1027 */       return TIntDoubleHashMap.this.forEachValue(procedure);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1034 */       final StringBuilder buf = new StringBuilder("{");
/* 1035 */       TIntDoubleHashMap.this.forEachValue(new TDoubleProcedure() {
/*      */             private boolean first = true;
/*      */             
/*      */             public boolean execute(double value) {
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
/*      */   class TIntDoubleKeyHashIterator
/*      */     extends THashPrimitiveIterator
/*      */     implements TIntIterator
/*      */   {
/*      */     TIntDoubleKeyHashIterator(TPrimitiveHash hash) {
/* 1063 */       super(hash);
/*      */     }
/*      */ 
/*      */     
/*      */     public int next() {
/* 1068 */       moveToNextIndex();
/* 1069 */       return TIntDoubleHashMap.this._set[this._index];
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
/* 1081 */         TIntDoubleHashMap.this.removeAt(this._index);
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
/*      */   class TIntDoubleValueHashIterator
/*      */     extends THashPrimitiveIterator
/*      */     implements TDoubleIterator
/*      */   {
/*      */     TIntDoubleValueHashIterator(TPrimitiveHash hash) {
/* 1101 */       super(hash);
/*      */     }
/*      */ 
/*      */     
/*      */     public double next() {
/* 1106 */       moveToNextIndex();
/* 1107 */       return TIntDoubleHashMap.this._values[this._index];
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
/* 1119 */         TIntDoubleHashMap.this.removeAt(this._index);
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
/*      */   class TIntDoubleHashIterator
/*      */     extends THashPrimitiveIterator
/*      */     implements TIntDoubleIterator
/*      */   {
/*      */     TIntDoubleHashIterator(TIntDoubleHashMap map) {
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
/* 1148 */       return TIntDoubleHashMap.this._set[this._index];
/*      */     }
/*      */ 
/*      */     
/*      */     public double value() {
/* 1153 */       return TIntDoubleHashMap.this._values[this._index];
/*      */     }
/*      */ 
/*      */     
/*      */     public double setValue(double val) {
/* 1158 */       double old = value();
/* 1159 */       TIntDoubleHashMap.this._values[this._index] = val;
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
/* 1171 */         TIntDoubleHashMap.this.removeAt(this._index);
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
/* 1184 */     if (!(other instanceof TIntDoubleMap)) {
/* 1185 */       return false;
/*      */     }
/* 1187 */     TIntDoubleMap that = (TIntDoubleMap)other;
/* 1188 */     if (that.size() != size()) {
/* 1189 */       return false;
/*      */     }
/* 1191 */     double[] values = this._values;
/* 1192 */     byte[] states = this._states;
/* 1193 */     double this_no_entry_value = getNoEntryValue();
/* 1194 */     double that_no_entry_value = that.getNoEntryValue();
/* 1195 */     for (int i = values.length; i-- > 0;) {
/* 1196 */       if (states[i] == 1) {
/* 1197 */         int key = this._set[i];
/* 1198 */         double that_value = that.get(key);
/* 1199 */         double this_value = values[i];
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
/* 1230 */     forEachEntry(new TIntDoubleProcedure()
/*      */         {
/*      */           public boolean execute(int key, double value) {
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
/* 1262 */         out.writeDouble(this._values[i]);
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
/* 1283 */       double val = in.readDouble();
/* 1284 */       put(key, val);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\map\hash\TIntDoubleHashMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */