/*     */ package org.apache.commons.collections4.collection;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import org.apache.commons.collections4.MultiMap;
/*     */ import org.apache.commons.collections4.Transformer;
/*     */ import org.apache.commons.collections4.map.MultiValueMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IndexedCollection<K, C>
/*     */   extends AbstractCollectionDecorator<C>
/*     */ {
/*     */   private static final long serialVersionUID = -5512610452568370038L;
/*     */   private final Transformer<C, K> keyTransformer;
/*     */   private final MultiMap<K, C> index;
/*     */   private final boolean uniqueIndex;
/*     */   
/*     */   public static <K, C> IndexedCollection<K, C> uniqueIndexedCollection(Collection<C> coll, Transformer<C, K> keyTransformer) {
/*  74 */     return new IndexedCollection<K, C>(coll, keyTransformer, (MultiMap<K, C>)MultiValueMap.multiValueMap(new HashMap<Object, Object>()), true);
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
/*     */   public static <K, C> IndexedCollection<K, C> nonUniqueIndexedCollection(Collection<C> coll, Transformer<C, K> keyTransformer) {
/*  90 */     return new IndexedCollection<K, C>(coll, keyTransformer, (MultiMap<K, C>)MultiValueMap.multiValueMap(new HashMap<Object, Object>()), false);
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
/*     */   public IndexedCollection(Collection<C> coll, Transformer<C, K> keyTransformer, MultiMap<K, C> map, boolean uniqueIndex) {
/* 105 */     super(coll);
/* 106 */     this.keyTransformer = keyTransformer;
/* 107 */     this.index = map;
/* 108 */     this.uniqueIndex = uniqueIndex;
/* 109 */     reindex();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(C object) {
/* 120 */     boolean added = super.add(object);
/* 121 */     if (added) {
/* 122 */       addToIndex(object);
/*     */     }
/* 124 */     return added;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends C> coll) {
/* 129 */     boolean changed = false;
/* 130 */     for (C c : coll) {
/* 131 */       changed |= add(c);
/*     */     }
/* 133 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 138 */     super.clear();
/* 139 */     this.index.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/* 150 */     return this.index.containsKey(this.keyTransformer.transform(object));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> coll) {
/* 160 */     for (Object o : coll) {
/* 161 */       if (!contains(o)) {
/* 162 */         return false;
/*     */       }
/*     */     } 
/* 165 */     return true;
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
/*     */   public C get(K key) {
/* 181 */     Collection<C> coll = (Collection<C>)this.index.get(key);
/* 182 */     return (coll == null) ? null : coll.iterator().next();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<C> values(K key) {
/* 193 */     return (Collection<C>)this.index.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reindex() {
/* 200 */     this.index.clear();
/* 201 */     for (C c : decorated()) {
/* 202 */       addToIndex(c);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(Object object) {
/* 209 */     boolean removed = super.remove(object);
/* 210 */     if (removed) {
/* 211 */       removeFromIndex((C)object);
/*     */     }
/* 213 */     return removed;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> coll) {
/* 218 */     boolean changed = false;
/* 219 */     for (Object o : coll) {
/* 220 */       changed |= remove(o);
/*     */     }
/* 222 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> coll) {
/* 227 */     boolean changed = super.retainAll(coll);
/* 228 */     if (changed) {
/* 229 */       reindex();
/*     */     }
/* 231 */     return changed;
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
/*     */   private void addToIndex(C object) {
/* 244 */     K key = (K)this.keyTransformer.transform(object);
/* 245 */     if (this.uniqueIndex && this.index.containsKey(key)) {
/* 246 */       throw new IllegalArgumentException("Duplicate key in uniquely indexed collection.");
/*     */     }
/* 248 */     this.index.put(key, object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeFromIndex(C object) {
/* 257 */     this.index.remove(this.keyTransformer.transform(object));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\collection\IndexedCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */