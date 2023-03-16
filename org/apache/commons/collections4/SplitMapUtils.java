/*     */ package org.apache.commons.collections4;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.collection.UnmodifiableCollection;
/*     */ import org.apache.commons.collections4.iterators.UnmodifiableMapIterator;
/*     */ import org.apache.commons.collections4.map.EntrySetToMapIteratorAdapter;
/*     */ import org.apache.commons.collections4.map.UnmodifiableEntrySet;
/*     */ import org.apache.commons.collections4.set.UnmodifiableSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SplitMapUtils
/*     */ {
/*     */   private static class WrappedGet<K, V>
/*     */     implements IterableMap<K, V>, Unmodifiable
/*     */   {
/*     */     private final Get<K, V> get;
/*     */     
/*     */     private WrappedGet(Get<K, V> get) {
/*  52 */       this.get = get;
/*     */     }
/*     */     
/*     */     public void clear() {
/*  56 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean containsKey(Object key) {
/*  60 */       return this.get.containsKey(key);
/*     */     }
/*     */     
/*     */     public boolean containsValue(Object value) {
/*  64 */       return this.get.containsValue(value);
/*     */     }
/*     */     
/*     */     public Set<Map.Entry<K, V>> entrySet() {
/*  68 */       return UnmodifiableEntrySet.unmodifiableEntrySet(this.get.entrySet());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object arg0) {
/*  73 */       if (arg0 == this) {
/*  74 */         return true;
/*     */       }
/*  76 */       return (arg0 instanceof WrappedGet && ((WrappedGet)arg0).get.equals(this.get));
/*     */     }
/*     */     
/*     */     public V get(Object key) {
/*  80 */       return this.get.get(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  85 */       return "WrappedGet".hashCode() << 4 | this.get.hashCode();
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/*  89 */       return this.get.isEmpty();
/*     */     }
/*     */     
/*     */     public Set<K> keySet() {
/*  93 */       return UnmodifiableSet.unmodifiableSet(this.get.keySet());
/*     */     }
/*     */     
/*     */     public V put(K key, V value) {
/*  97 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void putAll(Map<? extends K, ? extends V> t) {
/* 101 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public V remove(Object key) {
/* 105 */       return this.get.remove(key);
/*     */     }
/*     */     
/*     */     public int size() {
/* 109 */       return this.get.size();
/*     */     }
/*     */     
/*     */     public Collection<V> values() {
/* 113 */       return UnmodifiableCollection.unmodifiableCollection(this.get.values());
/*     */     }
/*     */     
/*     */     public MapIterator<K, V> mapIterator() {
/*     */       EntrySetToMapIteratorAdapter entrySetToMapIteratorAdapter;
/* 118 */       if (this.get instanceof IterableGet) {
/* 119 */         MapIterator<K, V> it = ((IterableGet<K, V>)this.get).mapIterator();
/*     */       } else {
/* 121 */         entrySetToMapIteratorAdapter = new EntrySetToMapIteratorAdapter(this.get.entrySet());
/*     */       } 
/* 123 */       return UnmodifiableMapIterator.unmodifiableMapIterator((MapIterator)entrySetToMapIteratorAdapter);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class WrappedPut<K, V> implements Map<K, V>, Put<K, V> {
/*     */     private final Put<K, V> put;
/*     */     
/*     */     private WrappedPut(Put<K, V> put) {
/* 131 */       this.put = put;
/*     */     }
/*     */     
/*     */     public void clear() {
/* 135 */       this.put.clear();
/*     */     }
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 139 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean containsValue(Object value) {
/* 143 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public Set<Map.Entry<K, V>> entrySet() {
/* 147 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 152 */       if (obj == this) {
/* 153 */         return true;
/*     */       }
/* 155 */       return (obj instanceof WrappedPut && ((WrappedPut)obj).put.equals(this.put));
/*     */     }
/*     */     
/*     */     public V get(Object key) {
/* 159 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 164 */       return "WrappedPut".hashCode() << 4 | this.put.hashCode();
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 168 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public Set<K> keySet() {
/* 172 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public V put(K key, V value) {
/* 177 */       return (V)this.put.put(key, value);
/*     */     }
/*     */     
/*     */     public void putAll(Map<? extends K, ? extends V> t) {
/* 181 */       this.put.putAll(t);
/*     */     }
/*     */     
/*     */     public V remove(Object key) {
/* 185 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int size() {
/* 189 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public Collection<V> values() {
/* 193 */       throw new UnsupportedOperationException();
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
/*     */   public static <K, V> IterableMap<K, V> readableMap(Get<K, V> get) {
/* 212 */     if (get == null) {
/* 213 */       throw new NullPointerException("Get must not be null");
/*     */     }
/* 215 */     if (get instanceof Map) {
/* 216 */       return (get instanceof IterableMap) ? (IterableMap<K, V>)get : MapUtils.<K, V>iterableMap((Map<K, V>)get);
/*     */     }
/*     */ 
/*     */     
/* 220 */     return new WrappedGet<K, V>(get);
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
/*     */   public static <K, V> Map<K, V> writableMap(Put<K, V> put) {
/* 238 */     if (put == null) {
/* 239 */       throw new NullPointerException("Put must not be null");
/*     */     }
/* 241 */     if (put instanceof Map) {
/* 242 */       return (Map<K, V>)put;
/*     */     }
/* 244 */     return new WrappedPut<K, V>(put);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\SplitMapUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */