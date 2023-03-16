/*     */ package org.apache.commons.collections4.bidimap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.commons.collections4.BidiMap;
/*     */ import org.apache.commons.collections4.MapIterator;
/*     */ import org.apache.commons.collections4.OrderedBidiMap;
/*     */ import org.apache.commons.collections4.OrderedMap;
/*     */ import org.apache.commons.collections4.OrderedMapIterator;
/*     */ import org.apache.commons.collections4.ResettableIterator;
/*     */ import org.apache.commons.collections4.SortedBidiMap;
/*     */ import org.apache.commons.collections4.map.AbstractSortedMapDecorator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DualTreeBidiMap<K, V>
/*     */   extends AbstractDualBidiMap<K, V>
/*     */   implements SortedBidiMap<K, V>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 721969328361809L;
/*     */   private final Comparator<? super K> comparator;
/*     */   private final Comparator<? super V> valueComparator;
/*     */   
/*     */   public DualTreeBidiMap() {
/*  71 */     super(new TreeMap<K, V>(), new TreeMap<V, K>());
/*  72 */     this.comparator = null;
/*  73 */     this.valueComparator = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DualTreeBidiMap(Map<? extends K, ? extends V> map) {
/*  83 */     super(new TreeMap<K, V>(), new TreeMap<V, K>());
/*  84 */     putAll(map);
/*  85 */     this.comparator = null;
/*  86 */     this.valueComparator = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DualTreeBidiMap(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator) {
/*  96 */     super(new TreeMap<K, V>(keyComparator), new TreeMap<V, K>(valueComparator));
/*  97 */     this.comparator = keyComparator;
/*  98 */     this.valueComparator = valueComparator;
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
/*     */   protected DualTreeBidiMap(Map<K, V> normalMap, Map<V, K> reverseMap, BidiMap<V, K> inverseBidiMap) {
/* 110 */     super(normalMap, reverseMap, inverseBidiMap);
/* 111 */     this.comparator = ((SortedMap)normalMap).comparator();
/* 112 */     this.valueComparator = ((SortedMap)reverseMap).comparator();
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
/*     */   protected DualTreeBidiMap<V, K> createBidiMap(Map<V, K> normalMap, Map<K, V> reverseMap, BidiMap<K, V> inverseMap) {
/* 126 */     return new DualTreeBidiMap(normalMap, reverseMap, inverseMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super K> comparator() {
/* 133 */     return ((SortedMap)this.normalMap).comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super V> valueComparator() {
/* 138 */     return ((SortedMap)this.reverseMap).comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public K firstKey() {
/* 143 */     return (K)((SortedMap)this.normalMap).firstKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public K lastKey() {
/* 148 */     return (K)((SortedMap)this.normalMap).lastKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public K nextKey(K key) {
/* 153 */     if (isEmpty()) {
/* 154 */       return null;
/*     */     }
/* 156 */     if (this.normalMap instanceof OrderedMap) {
/* 157 */       return (K)((OrderedMap)this.normalMap).nextKey(key);
/*     */     }
/* 159 */     SortedMap<K, V> sm = (SortedMap<K, V>)this.normalMap;
/* 160 */     Iterator<K> it = sm.tailMap(key).keySet().iterator();
/* 161 */     it.next();
/* 162 */     if (it.hasNext()) {
/* 163 */       return it.next();
/*     */     }
/* 165 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public K previousKey(K key) {
/* 170 */     if (isEmpty()) {
/* 171 */       return null;
/*     */     }
/* 173 */     if (this.normalMap instanceof OrderedMap) {
/* 174 */       return (K)((OrderedMap)this.normalMap).previousKey(key);
/*     */     }
/* 176 */     SortedMap<K, V> sm = (SortedMap<K, V>)this.normalMap;
/* 177 */     SortedMap<K, V> hm = sm.headMap(key);
/* 178 */     if (hm.isEmpty()) {
/* 179 */       return null;
/*     */     }
/* 181 */     return hm.lastKey();
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
/*     */   public OrderedMapIterator<K, V> mapIterator() {
/* 195 */     return new BidiOrderedMapIterator<K, V>(this);
/*     */   }
/*     */   
/*     */   public SortedBidiMap<V, K> inverseSortedBidiMap() {
/* 199 */     return inverseBidiMap();
/*     */   }
/*     */   
/*     */   public OrderedBidiMap<V, K> inverseOrderedBidiMap() {
/* 203 */     return (OrderedBidiMap<V, K>)inverseBidiMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> headMap(K toKey) {
/* 210 */     SortedMap<K, V> sub = ((SortedMap<K, V>)this.normalMap).headMap(toKey);
/* 211 */     return (SortedMap<K, V>)new ViewMap<K, V>(this, sub);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> tailMap(K fromKey) {
/* 216 */     SortedMap<K, V> sub = ((SortedMap<K, V>)this.normalMap).tailMap(fromKey);
/* 217 */     return (SortedMap<K, V>)new ViewMap<K, V>(this, sub);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 222 */     SortedMap<K, V> sub = ((SortedMap<K, V>)this.normalMap).subMap(fromKey, toKey);
/* 223 */     return (SortedMap<K, V>)new ViewMap<K, V>(this, sub);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedBidiMap<V, K> inverseBidiMap() {
/* 228 */     return (SortedBidiMap<V, K>)super.inverseBidiMap();
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
/*     */   protected static class ViewMap<K, V>
/*     */     extends AbstractSortedMapDecorator<K, V>
/*     */   {
/*     */     protected ViewMap(DualTreeBidiMap<K, V> bidi, SortedMap<K, V> sm) {
/* 245 */       super((SortedMap)new DualTreeBidiMap<K, V>(sm, bidi.reverseMap, bidi.inverseBidiMap));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean containsValue(Object value) {
/* 251 */       return (decorated()).normalMap.containsValue(value);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void clear() {
/* 257 */       for (Iterator<K> it = keySet().iterator(); it.hasNext(); ) {
/* 258 */         it.next();
/* 259 */         it.remove();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<K, V> headMap(K toKey) {
/* 265 */       return (SortedMap<K, V>)new ViewMap(decorated(), super.headMap(toKey));
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<K, V> tailMap(K fromKey) {
/* 270 */       return (SortedMap<K, V>)new ViewMap(decorated(), super.tailMap(fromKey));
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 275 */       return (SortedMap<K, V>)new ViewMap(decorated(), super.subMap(fromKey, toKey));
/*     */     }
/*     */ 
/*     */     
/*     */     protected DualTreeBidiMap<K, V> decorated() {
/* 280 */       return (DualTreeBidiMap<K, V>)super.decorated();
/*     */     }
/*     */ 
/*     */     
/*     */     public K previousKey(K key) {
/* 285 */       return decorated().previousKey(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public K nextKey(K key) {
/* 290 */       return decorated().nextKey(key);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class BidiOrderedMapIterator<K, V>
/*     */     implements OrderedMapIterator<K, V>, ResettableIterator<K>
/*     */   {
/*     */     private final AbstractDualBidiMap<K, V> parent;
/*     */ 
/*     */ 
/*     */     
/*     */     private ListIterator<Map.Entry<K, V>> iterator;
/*     */ 
/*     */     
/* 307 */     private Map.Entry<K, V> last = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected BidiOrderedMapIterator(AbstractDualBidiMap<K, V> parent) {
/* 315 */       this.parent = parent;
/* 316 */       this.iterator = (new ArrayList<Map.Entry<K, V>>(parent.entrySet())).listIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 321 */       return this.iterator.hasNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public K next() {
/* 326 */       this.last = this.iterator.next();
/* 327 */       return this.last.getKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasPrevious() {
/* 332 */       return this.iterator.hasPrevious();
/*     */     }
/*     */ 
/*     */     
/*     */     public K previous() {
/* 337 */       this.last = this.iterator.previous();
/* 338 */       return this.last.getKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 343 */       this.iterator.remove();
/* 344 */       this.parent.remove(this.last.getKey());
/* 345 */       this.last = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public K getKey() {
/* 350 */       if (this.last == null) {
/* 351 */         throw new IllegalStateException("Iterator getKey() can only be called after next() and before remove()");
/*     */       }
/*     */       
/* 354 */       return this.last.getKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/* 359 */       if (this.last == null) {
/* 360 */         throw new IllegalStateException("Iterator getValue() can only be called after next() and before remove()");
/*     */       }
/*     */       
/* 363 */       return this.last.getValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public V setValue(V value) {
/* 368 */       if (this.last == null) {
/* 369 */         throw new IllegalStateException("Iterator setValue() can only be called after next() and before remove()");
/*     */       }
/*     */       
/* 372 */       if (this.parent.reverseMap.containsKey(value) && this.parent.reverseMap.get(value) != this.last.getKey())
/*     */       {
/* 374 */         throw new IllegalArgumentException("Cannot use setValue() when the object being set is already in the map");
/*     */       }
/*     */       
/* 377 */       V oldValue = this.parent.put(this.last.getKey(), value);
/*     */ 
/*     */ 
/*     */       
/* 381 */       this.last.setValue(value);
/* 382 */       return oldValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public void reset() {
/* 387 */       this.iterator = (new ArrayList<Map.Entry<K, V>>(this.parent.entrySet())).listIterator();
/* 388 */       this.last = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 393 */       if (this.last != null) {
/* 394 */         return "MapIterator[" + getKey() + "=" + getValue() + "]";
/*     */       }
/* 396 */       return "MapIterator[]";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 403 */     out.defaultWriteObject();
/* 404 */     out.writeObject(this.normalMap);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 408 */     in.defaultReadObject();
/* 409 */     this.normalMap = new TreeMap<K, V>(this.comparator);
/* 410 */     this.reverseMap = new TreeMap<V, K>(this.valueComparator);
/*     */     
/* 412 */     Map<K, V> map = (Map<K, V>)in.readObject();
/* 413 */     putAll(map);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\bidimap\DualTreeBidiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */