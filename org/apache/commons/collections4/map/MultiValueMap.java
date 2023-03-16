/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.CollectionUtils;
/*     */ import org.apache.commons.collections4.Factory;
/*     */ import org.apache.commons.collections4.FunctorException;
/*     */ import org.apache.commons.collections4.MultiMap;
/*     */ import org.apache.commons.collections4.Transformer;
/*     */ import org.apache.commons.collections4.iterators.EmptyIterator;
/*     */ import org.apache.commons.collections4.iterators.IteratorChain;
/*     */ import org.apache.commons.collections4.iterators.LazyIteratorChain;
/*     */ import org.apache.commons.collections4.iterators.TransformIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class MultiValueMap<K, V>
/*     */   extends AbstractMapDecorator<K, Object>
/*     */   implements MultiMap<K, V>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2214159910087182007L;
/*     */   private final Factory<? extends Collection<V>> collectionFactory;
/*     */   private transient Collection<V> valuesView;
/*     */   
/*     */   public static <K, V> MultiValueMap<K, V> multiValueMap(Map<K, ? super Collection<V>> map) {
/*  89 */     return multiValueMap(map, (Class)ArrayList.class);
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
/*     */   public static <K, V, C extends Collection<V>> MultiValueMap<K, V> multiValueMap(Map<K, ? super C> map, Class<C> collectionClass) {
/* 106 */     return new MultiValueMap<K, V>(map, new ReflectionFactory<C>(collectionClass));
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
/*     */   public static <K, V, C extends Collection<V>> MultiValueMap<K, V> multiValueMap(Map<K, ? super C> map, Factory<C> collectionFactory) {
/* 123 */     return new MultiValueMap<K, V>(map, collectionFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiValueMap() {
/* 133 */     this(new HashMap<K, Collection<V>>(), new ReflectionFactory<Collection<V>>((Class)ArrayList.class));
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
/*     */   protected <C extends Collection<V>> MultiValueMap(Map<K, ? super C> map, Factory<C> collectionFactory) {
/* 147 */     super((Map)map);
/* 148 */     if (collectionFactory == null) {
/* 149 */       throw new IllegalArgumentException("The factory must not be null");
/*     */     }
/* 151 */     this.collectionFactory = collectionFactory;
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
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 163 */     out.defaultWriteObject();
/* 164 */     out.writeObject(this.map);
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
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 177 */     in.defaultReadObject();
/* 178 */     this.map = (Map<K, Object>)in.readObject();
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
/*     */   public void clear() {
/* 195 */     decorated().clear();
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
/* 213 */     Collection<V> valuesForKey = getCollection(key);
/* 214 */     if (valuesForKey == null) {
/* 215 */       return false;
/*     */     }
/* 217 */     boolean removed = valuesForKey.remove(value);
/* 218 */     if (!removed) {
/* 219 */       return false;
/*     */     }
/* 221 */     if (valuesForKey.isEmpty()) {
/* 222 */       remove(key);
/*     */     }
/* 224 */     return true;
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
/*     */   public boolean containsValue(Object value) {
/* 238 */     Set<Map.Entry<K, Object>> pairs = decorated().entrySet();
/* 239 */     if (pairs != null) {
/* 240 */       for (Map.Entry<K, Object> entry : pairs) {
/* 241 */         if (((Collection)entry.getValue()).contains(value)) {
/* 242 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 246 */     return false;
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
/*     */   public Object put(K key, Object value) {
/* 262 */     boolean result = false;
/* 263 */     Collection<V> coll = getCollection(key);
/* 264 */     if (coll == null) {
/* 265 */       coll = createCollection(1);
/* 266 */       coll.add((V)value);
/* 267 */       if (coll.size() > 0) {
/*     */         
/* 269 */         decorated().put(key, coll);
/* 270 */         result = true;
/*     */       } 
/*     */     } else {
/* 273 */       result = coll.add((V)value);
/*     */     } 
/* 275 */     return result ? value : null;
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
/*     */   public void putAll(Map<? extends K, ?> map) {
/* 292 */     if (map instanceof MultiMap) {
/* 293 */       for (Map.Entry<? extends K, Object> entry : (Iterable<Map.Entry<? extends K, Object>>)((MultiMap)map).entrySet()) {
/* 294 */         putAll(entry.getKey(), (Collection<V>)entry.getValue());
/*     */       }
/*     */     } else {
/* 297 */       for (Map.Entry<? extends K, ?> entry : map.entrySet()) {
/* 298 */         put(entry.getKey(), entry.getValue());
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
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, Object>> entrySet() {
/* 314 */     return super.entrySet();
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
/*     */   public Collection<Object> values() {
/* 327 */     Collection<V> vs = this.valuesView;
/* 328 */     return (vs != null) ? (Collection)vs : (Collection)(this.valuesView = new Values());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object key, Object value) {
/* 339 */     Collection<V> coll = getCollection(key);
/* 340 */     if (coll == null) {
/* 341 */       return false;
/*     */     }
/* 343 */     return coll.contains(value);
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
/*     */   public Collection<V> getCollection(Object key) {
/* 355 */     return (Collection<V>)decorated().get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size(Object key) {
/* 365 */     Collection<V> coll = getCollection(key);
/* 366 */     if (coll == null) {
/* 367 */       return 0;
/*     */     }
/* 369 */     return coll.size();
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
/*     */   public boolean putAll(K key, Collection<V> values) {
/* 381 */     if (values == null || values.size() == 0) {
/* 382 */       return false;
/*     */     }
/* 384 */     boolean result = false;
/* 385 */     Collection<V> coll = getCollection(key);
/* 386 */     if (coll == null) {
/* 387 */       coll = createCollection(values.size());
/* 388 */       coll.addAll(values);
/* 389 */       if (coll.size() > 0) {
/*     */         
/* 391 */         decorated().put(key, coll);
/* 392 */         result = true;
/*     */       } 
/*     */     } else {
/* 395 */       result = coll.addAll(values);
/*     */     } 
/* 397 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<V> iterator(Object key) {
/* 407 */     if (!containsKey(key)) {
/* 408 */       return EmptyIterator.emptyIterator();
/*     */     }
/* 410 */     return new ValuesIterator(key);
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
/*     */   public Iterator<Map.Entry<K, V>> iterator() {
/* 426 */     Collection<K> allKeys = new ArrayList<K>(keySet());
/* 427 */     final Iterator<K> keyIterator = allKeys.iterator();
/*     */     
/* 429 */     return (Iterator<Map.Entry<K, V>>)new LazyIteratorChain<Map.Entry<K, V>>()
/*     */       {
/*     */         protected Iterator<? extends Map.Entry<K, V>> nextIterator(int count) {
/* 432 */           if (!keyIterator.hasNext()) {
/* 433 */             return null;
/*     */           }
/* 435 */           final K key = keyIterator.next();
/* 436 */           Transformer<V, Map.Entry<K, V>> transformer = new Transformer<V, Map.Entry<K, V>>()
/*     */             {
/*     */               public Map.Entry<K, V> transform(final V input) {
/* 439 */                 return new Map.Entry<K, V>()
/*     */                   {
/*     */                     public K getKey() {
/* 442 */                       return (K)key;
/*     */                     }
/*     */                     
/*     */                     public V getValue() {
/* 446 */                       return (V)input;
/*     */                     }
/*     */                     
/*     */                     public V setValue(V value) {
/* 450 */                       throw new UnsupportedOperationException();
/*     */                     }
/*     */                   };
/*     */               }
/*     */             };
/* 455 */           return (Iterator<? extends Map.Entry<K, V>>)new TransformIterator(new MultiValueMap.ValuesIterator(key), transformer);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int totalSize() {
/* 466 */     int total = 0;
/* 467 */     for (Object v : decorated().values()) {
/* 468 */       total += CollectionUtils.size(v);
/*     */     }
/* 470 */     return total;
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
/*     */   protected Collection<V> createCollection(int size) {
/* 484 */     return (Collection<V>)this.collectionFactory.create();
/*     */   }
/*     */ 
/*     */   
/*     */   private class Values
/*     */     extends AbstractCollection<V>
/*     */   {
/*     */     private Values() {}
/*     */     
/*     */     public Iterator<V> iterator() {
/* 494 */       IteratorChain<V> chain = new IteratorChain();
/* 495 */       for (K k : MultiValueMap.this.keySet()) {
/* 496 */         chain.addIterator(new MultiValueMap.ValuesIterator(k));
/*     */       }
/* 498 */       return (Iterator<V>)chain;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 503 */       return MultiValueMap.this.totalSize();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 508 */       MultiValueMap.this.clear();
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
/* 521 */       this.key = key;
/* 522 */       this.values = MultiValueMap.this.getCollection(key);
/* 523 */       this.iterator = this.values.iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 528 */       this.iterator.remove();
/* 529 */       if (this.values.isEmpty()) {
/* 530 */         MultiValueMap.this.remove(this.key);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 536 */       return this.iterator.hasNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public V next() {
/* 541 */       return this.iterator.next();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ReflectionFactory<T extends Collection<?>>
/*     */     implements Factory<T>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 2986114157496788874L;
/*     */     
/*     */     private final Class<T> clazz;
/*     */ 
/*     */     
/*     */     public ReflectionFactory(Class<T> clazz) {
/* 556 */       this.clazz = clazz;
/*     */     }
/*     */ 
/*     */     
/*     */     public T create() {
/*     */       try {
/* 562 */         return this.clazz.newInstance();
/* 563 */       } catch (Exception ex) {
/* 564 */         throw new FunctorException("Cannot instantiate class: " + this.clazz, ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void readObject(ObjectInputStream is) throws IOException, ClassNotFoundException {
/* 569 */       is.defaultReadObject();
/*     */       
/* 571 */       if (this.clazz != null && !Collection.class.isAssignableFrom(this.clazz))
/* 572 */         throw new UnsupportedOperationException(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\MultiValueMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */