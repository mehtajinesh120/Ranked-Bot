/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.KeyValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StaticBucketMap<K, V>
/*     */   extends AbstractIterableMap<K, V>
/*     */ {
/*     */   private static final int DEFAULT_BUCKETS = 255;
/*     */   private final Node<K, V>[] buckets;
/*     */   private final Lock[] locks;
/*     */   
/*     */   public StaticBucketMap() {
/* 109 */     this(255);
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
/*     */   public StaticBucketMap(int numBuckets) {
/* 124 */     int size = Math.max(17, numBuckets);
/*     */ 
/*     */     
/* 127 */     if (size % 2 == 0) {
/* 128 */       size--;
/*     */     }
/*     */     
/* 131 */     this.buckets = (Node<K, V>[])new Node[size];
/* 132 */     this.locks = new Lock[size];
/*     */     
/* 134 */     for (int i = 0; i < size; i++) {
/* 135 */       this.locks[i] = new Lock();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getHash(Object key) {
/* 154 */     if (key == null) {
/* 155 */       return 0;
/*     */     }
/* 157 */     int hash = key.hashCode();
/* 158 */     hash += hash << 15 ^ 0xFFFFFFFF;
/* 159 */     hash ^= hash >>> 10;
/* 160 */     hash += hash << 3;
/* 161 */     hash ^= hash >>> 6;
/* 162 */     hash += hash << 11 ^ 0xFFFFFFFF;
/* 163 */     hash ^= hash >>> 16;
/* 164 */     hash %= this.buckets.length;
/* 165 */     return (hash < 0) ? (hash * -1) : hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 175 */     int cnt = 0;
/*     */     
/* 177 */     for (int i = 0; i < this.buckets.length; i++) {
/* 178 */       synchronized (this.locks[i]) {
/* 179 */         cnt += (this.locks[i]).size;
/*     */       } 
/*     */     } 
/* 182 */     return cnt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 191 */     return (size() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/* 201 */     int hash = getHash(key);
/*     */     
/* 203 */     synchronized (this.locks[hash]) {
/* 204 */       Node<K, V> n = this.buckets[hash];
/*     */       
/* 206 */       while (n != null) {
/* 207 */         if (n.key == key || (n.key != null && n.key.equals(key))) {
/* 208 */           return n.value;
/*     */         }
/*     */         
/* 211 */         n = n.next;
/*     */       } 
/*     */     } 
/* 214 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 224 */     int hash = getHash(key);
/*     */     
/* 226 */     synchronized (this.locks[hash]) {
/* 227 */       Node<K, V> n = this.buckets[hash];
/*     */       
/* 229 */       while (n != null) {
/* 230 */         if (n.key == key || (n.key != null && n.key.equals(key))) {
/* 231 */           return true;
/*     */         }
/*     */         
/* 234 */         n = n.next;
/*     */       } 
/*     */     } 
/* 237 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 247 */     for (int i = 0; i < this.buckets.length; i++) {
/* 248 */       synchronized (this.locks[i]) {
/* 249 */         Node<K, V> n = this.buckets[i];
/*     */         
/* 251 */         while (n != null) {
/* 252 */           if (n.value == value || (n.value != null && n.value.equals(value))) {
/* 253 */             return true;
/*     */           }
/*     */           
/* 256 */           n = n.next;
/*     */         } 
/*     */       } 
/*     */     } 
/* 260 */     return false;
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
/*     */   public V put(K key, V value) {
/* 272 */     int hash = getHash(key);
/*     */     
/* 274 */     synchronized (this.locks[hash]) {
/* 275 */       Node<K, V> n = this.buckets[hash];
/*     */       
/* 277 */       if (n == null) {
/* 278 */         n = new Node<K, V>();
/* 279 */         n.key = key;
/* 280 */         n.value = value;
/* 281 */         this.buckets[hash] = n;
/* 282 */         (this.locks[hash]).size++;
/* 283 */         return null;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 289 */       for (Node<K, V> next = n; next != null; next = next.next) {
/* 290 */         n = next;
/*     */         
/* 292 */         if (n.key == key || (n.key != null && n.key.equals(key))) {
/* 293 */           V returnVal = n.value;
/* 294 */           n.value = value;
/* 295 */           return returnVal;
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 301 */       Node<K, V> newNode = new Node<K, V>();
/* 302 */       newNode.key = key;
/* 303 */       newNode.value = value;
/* 304 */       n.next = newNode;
/* 305 */       (this.locks[hash]).size++;
/*     */     } 
/* 307 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/* 317 */     int hash = getHash(key);
/*     */     
/* 319 */     synchronized (this.locks[hash]) {
/* 320 */       Node<K, V> n = this.buckets[hash];
/* 321 */       Node<K, V> prev = null;
/*     */       
/* 323 */       while (n != null) {
/* 324 */         if (n.key == key || (n.key != null && n.key.equals(key))) {
/*     */           
/* 326 */           if (null == prev) {
/*     */             
/* 328 */             this.buckets[hash] = n.next;
/*     */           } else {
/*     */             
/* 331 */             prev.next = n.next;
/*     */           } 
/* 333 */           (this.locks[hash]).size--;
/* 334 */           return n.value;
/*     */         } 
/*     */         
/* 337 */         prev = n;
/* 338 */         n = n.next;
/*     */       } 
/*     */     } 
/* 341 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 351 */     return new KeySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 360 */     return new Values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 369 */     return new EntrySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/* 380 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 381 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 389 */     for (int i = 0; i < this.buckets.length; i++) {
/* 390 */       Lock lock = this.locks[i];
/* 391 */       synchronized (lock) {
/* 392 */         this.buckets[i] = null;
/* 393 */         lock.size = 0;
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
/*     */   public boolean equals(Object obj) {
/* 406 */     if (obj == this) {
/* 407 */       return true;
/*     */     }
/* 409 */     if (!(obj instanceof Map)) {
/* 410 */       return false;
/*     */     }
/* 412 */     Map<?, ?> other = (Map<?, ?>)obj;
/* 413 */     return entrySet().equals(other.entrySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 423 */     int hashCode = 0;
/*     */     
/* 425 */     for (int i = 0; i < this.buckets.length; i++) {
/* 426 */       synchronized (this.locks[i]) {
/* 427 */         Node<K, V> n = this.buckets[i];
/*     */         
/* 429 */         while (n != null) {
/* 430 */           hashCode += n.hashCode();
/* 431 */           n = n.next;
/*     */         } 
/*     */       } 
/*     */     } 
/* 435 */     return hashCode;
/*     */   }
/*     */   
/*     */   private static final class Node<K, V>
/*     */     implements Map.Entry<K, V>, KeyValue<K, V>
/*     */   {
/*     */     protected K key;
/*     */     protected V value;
/*     */     protected Node<K, V> next;
/*     */     
/*     */     private Node() {}
/*     */     
/*     */     public K getKey() {
/* 448 */       return this.key;
/*     */     }
/*     */     
/*     */     public V getValue() {
/* 452 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 457 */       return ((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : this.value.hashCode());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 463 */       if (obj == this) {
/* 464 */         return true;
/*     */       }
/* 466 */       if (!(obj instanceof Map.Entry)) {
/* 467 */         return false;
/*     */       }
/*     */       
/* 470 */       Map.Entry<?, ?> e2 = (Map.Entry<?, ?>)obj;
/* 471 */       return (((this.key == null) ? (e2.getKey() == null) : this.key.equals(e2.getKey())) && ((this.value == null) ? (e2.getValue() == null) : this.value.equals(e2.getValue())));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public V setValue(V obj) {
/* 477 */       V retVal = this.value;
/* 478 */       this.value = obj;
/* 479 */       return retVal;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Lock
/*     */   {
/*     */     public int size;
/*     */     
/*     */     private Lock() {}
/*     */   }
/*     */   
/*     */   private class BaseIterator
/*     */   {
/* 492 */     private final ArrayList<Map.Entry<K, V>> current = new ArrayList<Map.Entry<K, V>>();
/*     */     private int bucket;
/*     */     private Map.Entry<K, V> last;
/*     */     
/*     */     public boolean hasNext() {
/* 497 */       if (this.current.size() > 0) {
/* 498 */         return true;
/*     */       }
/* 500 */       while (this.bucket < StaticBucketMap.this.buckets.length) {
/* 501 */         synchronized (StaticBucketMap.this.locks[this.bucket]) {
/* 502 */           StaticBucketMap.Node<K, V> n = StaticBucketMap.this.buckets[this.bucket];
/* 503 */           while (n != null) {
/* 504 */             this.current.add(n);
/* 505 */             n = n.next;
/*     */           } 
/* 507 */           this.bucket++;
/* 508 */           if (this.current.size() > 0) {
/* 509 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/* 513 */       return false;
/*     */     }
/*     */     
/*     */     protected Map.Entry<K, V> nextEntry() {
/* 517 */       if (!hasNext()) {
/* 518 */         throw new NoSuchElementException();
/*     */       }
/* 520 */       this.last = this.current.remove(this.current.size() - 1);
/* 521 */       return this.last;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 525 */       if (this.last == null) {
/* 526 */         throw new IllegalStateException();
/*     */       }
/* 528 */       StaticBucketMap.this.remove(this.last.getKey());
/* 529 */       this.last = null;
/*     */     }
/*     */     
/*     */     private BaseIterator() {}
/*     */   }
/*     */   
/*     */   private class EntryIterator extends BaseIterator implements Iterator<Map.Entry<K, V>> { public Map.Entry<K, V> next() {
/* 536 */       return nextEntry();
/*     */     }
/*     */     
/*     */     private EntryIterator() {} }
/*     */ 
/*     */   
/*     */   private class ValueIterator extends BaseIterator implements Iterator<V> {
/*     */     public V next() {
/* 544 */       return nextEntry().getValue();
/*     */     }
/*     */     
/*     */     private ValueIterator() {} }
/*     */   
/*     */   private class KeyIterator extends BaseIterator implements Iterator<K> { private KeyIterator() {}
/*     */     
/*     */     public K next() {
/* 552 */       return nextEntry().getKey();
/*     */     } }
/*     */ 
/*     */   
/*     */   private class EntrySet
/*     */     extends AbstractSet<Map.Entry<K, V>> {
/*     */     private EntrySet() {}
/*     */     
/*     */     public int size() {
/* 561 */       return StaticBucketMap.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 566 */       StaticBucketMap.this.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 571 */       return new StaticBucketMap.EntryIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object obj) {
/* 576 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 577 */       int hash = StaticBucketMap.this.getHash(entry.getKey());
/* 578 */       synchronized (StaticBucketMap.this.locks[hash]) {
/* 579 */         for (StaticBucketMap.Node<K, V> n = StaticBucketMap.this.buckets[hash]; n != null; n = n.next) {
/* 580 */           if (n.equals(entry)) {
/* 581 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/* 585 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object obj) {
/* 590 */       if (!(obj instanceof Map.Entry)) {
/* 591 */         return false;
/*     */       }
/* 593 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 594 */       int hash = StaticBucketMap.this.getHash(entry.getKey());
/* 595 */       synchronized (StaticBucketMap.this.locks[hash]) {
/* 596 */         for (StaticBucketMap.Node<K, V> n = StaticBucketMap.this.buckets[hash]; n != null; n = n.next) {
/* 597 */           if (n.equals(entry)) {
/* 598 */             StaticBucketMap.this.remove(n.getKey());
/* 599 */             return true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 603 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private class KeySet
/*     */     extends AbstractSet<K> {
/*     */     private KeySet() {}
/*     */     
/*     */     public int size() {
/* 612 */       return StaticBucketMap.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 617 */       StaticBucketMap.this.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<K> iterator() {
/* 622 */       return new StaticBucketMap.KeyIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object obj) {
/* 627 */       return StaticBucketMap.this.containsKey(obj);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object obj) {
/* 632 */       int hash = StaticBucketMap.this.getHash(obj);
/* 633 */       synchronized (StaticBucketMap.this.locks[hash]) {
/* 634 */         for (StaticBucketMap.Node<K, V> n = StaticBucketMap.this.buckets[hash]; n != null; n = n.next) {
/* 635 */           Object k = n.getKey();
/* 636 */           if (k == obj || (k != null && k.equals(obj))) {
/* 637 */             StaticBucketMap.this.remove(k);
/* 638 */             return true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 642 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private class Values
/*     */     extends AbstractCollection<V>
/*     */   {
/*     */     private Values() {}
/*     */     
/*     */     public int size() {
/* 652 */       return StaticBucketMap.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 657 */       StaticBucketMap.this.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<V> iterator() {
/* 662 */       return new StaticBucketMap.ValueIterator();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void atomic(Runnable r) {
/* 702 */     if (r == null) {
/* 703 */       throw new NullPointerException();
/*     */     }
/* 705 */     atomic(r, 0);
/*     */   }
/*     */   
/*     */   private void atomic(Runnable r, int bucket) {
/* 709 */     if (bucket >= this.buckets.length) {
/* 710 */       r.run();
/*     */       return;
/*     */     } 
/* 713 */     synchronized (this.locks[bucket]) {
/* 714 */       atomic(r, bucket + 1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\StaticBucketMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */