/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.CollectionUtils;
/*     */ import org.apache.commons.collections4.collection.CompositeCollection;
/*     */ import org.apache.commons.collections4.set.CompositeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompositeMap<K, V>
/*     */   extends AbstractIterableMap<K, V>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6096931280583808322L;
/*     */   private Map<K, V>[] composite;
/*     */   private MapMutator<K, V> mutator;
/*     */   
/*     */   public CompositeMap() {
/*  61 */     this((Map<K, V>[])new Map[0], (MapMutator<K, V>)null);
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
/*     */   public CompositeMap(Map<K, V> one, Map<K, V> two) {
/*  73 */     this((Map<K, V>[])new Map[] { one, two }, (MapMutator<K, V>)null);
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
/*     */   public CompositeMap(Map<K, V> one, Map<K, V> two, MapMutator<K, V> mutator) {
/*  85 */     this((Map<K, V>[])new Map[] { one, two }, mutator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeMap(Map<K, V>... composite) {
/*  96 */     this(composite, (MapMutator<K, V>)null);
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
/*     */   public CompositeMap(Map<K, V>[] composite, MapMutator<K, V> mutator) {
/* 108 */     this.mutator = mutator;
/* 109 */     this.composite = (Map<K, V>[])new Map[0];
/* 110 */     for (int i = composite.length - 1; i >= 0; i--) {
/* 111 */       addComposited(composite[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMutator(MapMutator<K, V> mutator) {
/* 122 */     this.mutator = mutator;
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
/*     */   public synchronized void addComposited(Map<K, V> map) throws IllegalArgumentException {
/* 134 */     for (int i = this.composite.length - 1; i >= 0; i--) {
/* 135 */       Collection<K> intersect = CollectionUtils.intersection(this.composite[i].keySet(), map.keySet());
/* 136 */       if (intersect.size() != 0) {
/* 137 */         if (this.mutator == null) {
/* 138 */           throw new IllegalArgumentException("Key collision adding Map to CompositeMap");
/*     */         }
/* 140 */         this.mutator.resolveCollision(this, this.composite[i], map, intersect);
/*     */       } 
/*     */     } 
/* 143 */     Map[] arrayOfMap = new Map[this.composite.length + 1];
/* 144 */     System.arraycopy(this.composite, 0, arrayOfMap, 0, this.composite.length);
/* 145 */     arrayOfMap[arrayOfMap.length - 1] = map;
/* 146 */     this.composite = (Map<K, V>[])arrayOfMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Map<K, V> removeComposited(Map<K, V> map) {
/* 157 */     int size = this.composite.length;
/* 158 */     for (int i = 0; i < size; i++) {
/* 159 */       if (this.composite[i].equals(map)) {
/* 160 */         Map[] arrayOfMap = new Map[size - 1];
/* 161 */         System.arraycopy(this.composite, 0, arrayOfMap, 0, i);
/* 162 */         System.arraycopy(this.composite, i + 1, arrayOfMap, i, size - i - 1);
/* 163 */         this.composite = (Map<K, V>[])arrayOfMap;
/* 164 */         return map;
/*     */       } 
/*     */     } 
/* 167 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 177 */     for (int i = this.composite.length - 1; i >= 0; i--) {
/* 178 */       this.composite[i].clear();
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
/*     */   public boolean containsKey(Object key) {
/* 199 */     for (int i = this.composite.length - 1; i >= 0; i--) {
/* 200 */       if (this.composite[i].containsKey(key)) {
/* 201 */         return true;
/*     */       }
/*     */     } 
/* 204 */     return false;
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
/*     */   public boolean containsValue(Object value) {
/* 224 */     for (int i = this.composite.length - 1; i >= 0; i--) {
/* 225 */       if (this.composite[i].containsValue(value)) {
/* 226 */         return true;
/*     */       }
/*     */     } 
/* 229 */     return false;
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
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 250 */     CompositeSet<Map.Entry<K, V>> entries = new CompositeSet();
/* 251 */     for (int i = this.composite.length - 1; i >= 0; i--) {
/* 252 */       entries.addComposited(this.composite[i].entrySet());
/*     */     }
/* 254 */     return (Set<Map.Entry<K, V>>)entries;
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
/*     */   public V get(Object key) {
/* 282 */     for (int i = this.composite.length - 1; i >= 0; i--) {
/* 283 */       if (this.composite[i].containsKey(key)) {
/* 284 */         return this.composite[i].get(key);
/*     */       }
/*     */     } 
/* 287 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 296 */     for (int i = this.composite.length - 1; i >= 0; i--) {
/* 297 */       if (!this.composite[i].isEmpty()) {
/* 298 */         return false;
/*     */       }
/*     */     } 
/* 301 */     return true;
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
/*     */   public Set<K> keySet() {
/* 320 */     CompositeSet<K> keys = new CompositeSet();
/* 321 */     for (int i = this.composite.length - 1; i >= 0; i--) {
/* 322 */       keys.addComposited(this.composite[i].keySet());
/*     */     }
/* 324 */     return (Set<K>)keys;
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
/*     */   public V put(K key, V value) {
/* 353 */     if (this.mutator == null) {
/* 354 */       throw new UnsupportedOperationException("No mutator specified");
/*     */     }
/* 356 */     return this.mutator.put(this, this.composite, key, value);
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
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/* 382 */     if (this.mutator == null) {
/* 383 */       throw new UnsupportedOperationException("No mutator specified");
/*     */     }
/* 385 */     this.mutator.putAll(this, this.composite, map);
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
/*     */   public V remove(Object key) {
/* 414 */     for (int i = this.composite.length - 1; i >= 0; i--) {
/* 415 */       if (this.composite[i].containsKey(key)) {
/* 416 */         return this.composite[i].remove(key);
/*     */       }
/*     */     } 
/* 419 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 430 */     int size = 0;
/* 431 */     for (int i = this.composite.length - 1; i >= 0; i--) {
/* 432 */       size += this.composite[i].size();
/*     */     }
/* 434 */     return size;
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
/*     */   public Collection<V> values() {
/* 451 */     CompositeCollection<V> values = new CompositeCollection();
/* 452 */     for (int i = this.composite.length - 1; i >= 0; i--) {
/* 453 */       values.addComposited(this.composite[i].values());
/*     */     }
/* 455 */     return (Collection<V>)values;
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
/* 466 */     if (obj instanceof Map) {
/* 467 */       Map<?, ?> map = (Map<?, ?>)obj;
/* 468 */       return entrySet().equals(map.entrySet());
/*     */     } 
/* 470 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 479 */     int code = 0;
/* 480 */     for (Map.Entry<K, V> entry : entrySet()) {
/* 481 */       code += entry.hashCode();
/*     */     }
/* 483 */     return code;
/*     */   }
/*     */   
/*     */   public static interface MapMutator<K, V> extends Serializable {
/*     */     void resolveCollision(CompositeMap<K, V> param1CompositeMap, Map<K, V> param1Map1, Map<K, V> param1Map2, Collection<K> param1Collection);
/*     */     
/*     */     V put(CompositeMap<K, V> param1CompositeMap, Map<K, V>[] param1ArrayOfMap, K param1K, V param1V);
/*     */     
/*     */     void putAll(CompositeMap<K, V> param1CompositeMap, Map<K, V>[] param1ArrayOfMap, Map<? extends K, ? extends V> param1Map);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\CompositeMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */