/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.BoundedMap;
/*     */ import org.apache.commons.collections4.KeyValue;
/*     */ import org.apache.commons.collections4.MapIterator;
/*     */ import org.apache.commons.collections4.OrderedMap;
/*     */ import org.apache.commons.collections4.OrderedMapIterator;
/*     */ import org.apache.commons.collections4.ResettableIterator;
/*     */ import org.apache.commons.collections4.iterators.SingletonIterator;
/*     */ import org.apache.commons.collections4.keyvalue.TiedMapEntry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SingletonMap<K, V>
/*     */   implements OrderedMap<K, V>, BoundedMap<K, V>, KeyValue<K, V>, Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = -8931271118676803261L;
/*     */   private final K key;
/*     */   private V value;
/*     */   
/*     */   public SingletonMap() {
/*  75 */     this.key = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SingletonMap(K key, V value) {
/*  86 */     this.key = key;
/*  87 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SingletonMap(KeyValue<K, V> keyValue) {
/*  97 */     this.key = (K)keyValue.getKey();
/*  98 */     this.value = (V)keyValue.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SingletonMap(Map.Entry<? extends K, ? extends V> mapEntry) {
/* 108 */     this.key = mapEntry.getKey();
/* 109 */     this.value = mapEntry.getValue();
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
/*     */   public SingletonMap(Map<? extends K, ? extends V> map) {
/* 121 */     if (map.size() != 1) {
/* 122 */       throw new IllegalArgumentException("The map size must be 1");
/*     */     }
/* 124 */     Map.Entry<? extends K, ? extends V> entry = map.entrySet().iterator().next();
/* 125 */     this.key = entry.getKey();
/* 126 */     this.value = entry.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K getKey() {
/* 137 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V getValue() {
/* 146 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V setValue(V value) {
/* 156 */     V old = this.value;
/* 157 */     this.value = value;
/* 158 */     return old;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFull() {
/* 169 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int maxSize() {
/* 178 */     return 1;
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
/*     */   public V get(Object key) {
/* 190 */     if (isEqualKey(key)) {
/* 191 */       return this.value;
/*     */     }
/* 193 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 202 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 211 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 222 */     return isEqualKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 232 */     return isEqualValue(value);
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
/*     */   public V put(K key, V value) {
/* 248 */     if (isEqualKey(key)) {
/* 249 */       return setValue(value);
/*     */     }
/* 251 */     throw new IllegalArgumentException("Cannot put new key/value pair - Map is fixed size singleton");
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
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/*     */     Map.Entry<? extends K, ? extends V> entry;
/* 266 */     switch (map.size()) {
/*     */       case 0:
/*     */         return;
/*     */       
/*     */       case 1:
/* 271 */         entry = map.entrySet().iterator().next();
/* 272 */         put(entry.getKey(), entry.getValue());
/*     */         return;
/*     */     } 
/*     */     
/* 276 */     throw new IllegalArgumentException("The map size must be 0 or 1");
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
/*     */   public V remove(Object key) {
/* 288 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 295 */     throw new UnsupportedOperationException();
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
/* 307 */     TiedMapEntry tiedMapEntry = new TiedMapEntry((Map)this, getKey());
/* 308 */     return (Set)Collections.singleton(tiedMapEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 319 */     return Collections.singleton(this.key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 330 */     return new SingletonValues<V>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OrderedMapIterator<K, V> mapIterator() {
/* 337 */     return new SingletonMapIterator<K, V>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K firstKey() {
/* 346 */     return getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K lastKey() {
/* 355 */     return getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K nextKey(K key) {
/* 365 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K previousKey(K key) {
/* 375 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEqualKey(Object key) {
/* 386 */     return (key == null) ? ((getKey() == null)) : key.equals(getKey());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEqualValue(Object value) {
/* 396 */     return (value == null) ? ((getValue() == null)) : value.equals(getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   static class SingletonMapIterator<K, V>
/*     */     implements OrderedMapIterator<K, V>, ResettableIterator<K>
/*     */   {
/*     */     private final SingletonMap<K, V> parent;
/*     */     
/*     */     private boolean hasNext = true;
/*     */     
/*     */     private boolean canGetSet = false;
/*     */     
/*     */     SingletonMapIterator(SingletonMap<K, V> parent) {
/* 410 */       this.parent = parent;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 414 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public K next() {
/* 418 */       if (!this.hasNext) {
/* 419 */         throw new NoSuchElementException("No next() entry in the iteration");
/*     */       }
/* 421 */       this.hasNext = false;
/* 422 */       this.canGetSet = true;
/* 423 */       return this.parent.getKey();
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 427 */       return !this.hasNext;
/*     */     }
/*     */     
/*     */     public K previous() {
/* 431 */       if (this.hasNext == true) {
/* 432 */         throw new NoSuchElementException("No previous() entry in the iteration");
/*     */       }
/* 434 */       this.hasNext = true;
/* 435 */       return this.parent.getKey();
/*     */     }
/*     */     
/*     */     public void remove() {
/* 439 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public K getKey() {
/* 443 */       if (!this.canGetSet) {
/* 444 */         throw new IllegalStateException("getKey() can only be called after next() and before remove()");
/*     */       }
/* 446 */       return this.parent.getKey();
/*     */     }
/*     */     
/*     */     public V getValue() {
/* 450 */       if (!this.canGetSet) {
/* 451 */         throw new IllegalStateException("getValue() can only be called after next() and before remove()");
/*     */       }
/* 453 */       return this.parent.getValue();
/*     */     }
/*     */     
/*     */     public V setValue(V value) {
/* 457 */       if (!this.canGetSet) {
/* 458 */         throw new IllegalStateException("setValue() can only be called after next() and before remove()");
/*     */       }
/* 460 */       return this.parent.setValue(value);
/*     */     }
/*     */     
/*     */     public void reset() {
/* 464 */       this.hasNext = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 469 */       if (this.hasNext) {
/* 470 */         return "Iterator[]";
/*     */       }
/* 472 */       return "Iterator[" + getKey() + "=" + getValue() + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class SingletonValues<V>
/*     */     extends AbstractSet<V>
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -3689524741863047872L;
/*     */     
/*     */     private final SingletonMap<?, V> parent;
/*     */     
/*     */     SingletonValues(SingletonMap<?, V> parent) {
/* 486 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 491 */       return 1;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 495 */       return false;
/*     */     }
/*     */     
/*     */     public boolean contains(Object object) {
/* 499 */       return this.parent.containsValue(object);
/*     */     }
/*     */     
/*     */     public void clear() {
/* 503 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public Iterator<V> iterator() {
/* 507 */       return (Iterator<V>)new SingletonIterator(this.parent.getValue(), false);
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
/*     */   public SingletonMap<K, V> clone() {
/*     */     try {
/* 521 */       return (SingletonMap<K, V>)super.clone();
/* 522 */     } catch (CloneNotSupportedException ex) {
/* 523 */       throw new InternalError();
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
/*     */   public boolean equals(Object obj) {
/* 535 */     if (obj == this) {
/* 536 */       return true;
/*     */     }
/* 538 */     if (!(obj instanceof Map)) {
/* 539 */       return false;
/*     */     }
/* 541 */     Map<?, ?> other = (Map<?, ?>)obj;
/* 542 */     if (other.size() != 1) {
/* 543 */       return false;
/*     */     }
/* 545 */     Map.Entry<?, ?> entry = other.entrySet().iterator().next();
/* 546 */     return (isEqualKey(entry.getKey()) && isEqualValue(entry.getValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 556 */     return ((getKey() == null) ? 0 : getKey().hashCode()) ^ ((getValue() == null) ? 0 : getValue().hashCode());
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
/* 567 */     return (new StringBuilder(128)).append('{').append((getKey() == this) ? "(this Map)" : getKey()).append('=').append((getValue() == this) ? "(this Map)" : getValue()).append('}').toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\SingletonMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */