/*     */ package org.apache.commons.collections4;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.bag.HashBag;
/*     */ import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
/*     */ import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
/*     */ import org.apache.commons.collections4.multimap.TransformedMultiValuedMap;
/*     */ import org.apache.commons.collections4.multimap.UnmodifiableMultiValuedMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultiMapUtils
/*     */ {
/*  55 */   public static final MultiValuedMap EMPTY_MULTI_VALUED_MAP = (MultiValuedMap)UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap((MultiValuedMap)new ArrayListValuedHashMap(0, 0));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> MultiValuedMap<K, V> emptyMultiValuedMap() {
/*  67 */     return EMPTY_MULTI_VALUED_MAP;
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
/*     */   public static <K, V> MultiValuedMap<K, V> emptyIfNull(MultiValuedMap<K, V> map) {
/*  83 */     return (map == null) ? EMPTY_MULTI_VALUED_MAP : map;
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
/*     */   public static boolean isEmpty(MultiValuedMap<?, ?> map) {
/*  95 */     return (map == null || map.isEmpty());
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
/*     */   public static <K, V> Collection<V> getCollection(MultiValuedMap<K, V> map, K key) {
/* 111 */     if (map != null) {
/* 112 */       return map.get(key);
/*     */     }
/* 114 */     return null;
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
/*     */   public static <K, V> List<V> getValuesAsList(MultiValuedMap<K, V> map, K key) {
/* 130 */     if (map != null) {
/* 131 */       Collection<V> col = map.get(key);
/* 132 */       if (col instanceof List) {
/* 133 */         return (List<V>)col;
/*     */       }
/* 135 */       return new ArrayList<V>(col);
/*     */     } 
/* 137 */     return null;
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
/*     */   public static <K, V> Set<V> getValuesAsSet(MultiValuedMap<K, V> map, K key) {
/* 150 */     if (map != null) {
/* 151 */       Collection<V> col = map.get(key);
/* 152 */       if (col instanceof Set) {
/* 153 */         return (Set<V>)col;
/*     */       }
/* 155 */       return new HashSet<V>(col);
/*     */     } 
/* 157 */     return null;
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
/*     */   public static <K, V> Bag<V> getValuesAsBag(MultiValuedMap<K, V> map, K key) {
/* 170 */     if (map != null) {
/* 171 */       Collection<V> col = map.get(key);
/* 172 */       if (col instanceof Bag) {
/* 173 */         return (Bag<V>)col;
/*     */       }
/* 175 */       return (Bag<V>)new HashBag(col);
/*     */     } 
/* 177 */     return null;
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
/*     */   public static <K, V> ListValuedMap<K, V> newListValuedHashMap() {
/* 192 */     return (ListValuedMap<K, V>)new ArrayListValuedHashMap();
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
/*     */   public static <K, V> SetValuedMap<K, V> newSetValuedHashMap() {
/* 204 */     return (SetValuedMap<K, V>)new HashSetValuedHashMap();
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
/*     */   public static <K, V> MultiValuedMap<K, V> unmodifiableMultiValuedMap(MultiValuedMap<? extends K, ? extends V> map) {
/* 222 */     return (MultiValuedMap<K, V>)UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap(map);
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
/*     */   public static <K, V> MultiValuedMap<K, V> transformedMultiValuedMap(MultiValuedMap<K, V> map, Transformer<? super K, ? extends K> keyTransformer, Transformer<? super V, ? extends V> valueTransformer) {
/* 251 */     return (MultiValuedMap<K, V>)TransformedMultiValuedMap.transformingMap(map, keyTransformer, valueTransformer);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\MultiMapUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */