/*     */ package org.apache.commons.collections4.multimap;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.collections4.CollectionUtils;
/*     */ import org.apache.commons.collections4.FluentIterable;
/*     */ import org.apache.commons.collections4.MultiValuedMap;
/*     */ import org.apache.commons.collections4.Transformer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TransformedMultiValuedMap<K, V>
/*     */   extends AbstractMultiValuedMapDecorator<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 20150612L;
/*     */   private final Transformer<? super K, ? extends K> keyTransformer;
/*     */   private final Transformer<? super V, ? extends V> valueTransformer;
/*     */   
/*     */   public static <K, V> TransformedMultiValuedMap<K, V> transformingMap(MultiValuedMap<K, V> map, Transformer<? super K, ? extends K> keyTransformer, Transformer<? super V, ? extends V> valueTransformer) {
/*  69 */     return new TransformedMultiValuedMap<K, V>(map, keyTransformer, valueTransformer);
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
/*     */   public static <K, V> TransformedMultiValuedMap<K, V> transformedMap(MultiValuedMap<K, V> map, Transformer<? super K, ? extends K> keyTransformer, Transformer<? super V, ? extends V> valueTransformer) {
/*  91 */     TransformedMultiValuedMap<K, V> decorated = new TransformedMultiValuedMap<K, V>(map, keyTransformer, valueTransformer);
/*     */     
/*  93 */     if (!map.isEmpty()) {
/*  94 */       MultiValuedMap<K, V> mapCopy = new ArrayListValuedHashMap<K, V>(map);
/*  95 */       decorated.clear();
/*  96 */       decorated.putAll(mapCopy);
/*     */     } 
/*  98 */     return decorated;
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
/*     */   protected TransformedMultiValuedMap(MultiValuedMap<K, V> map, Transformer<? super K, ? extends K> keyTransformer, Transformer<? super V, ? extends V> valueTransformer) {
/* 116 */     super(map);
/* 117 */     this.keyTransformer = keyTransformer;
/* 118 */     this.valueTransformer = valueTransformer;
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
/*     */   protected K transformKey(K object) {
/* 130 */     if (this.keyTransformer == null) {
/* 131 */       return object;
/*     */     }
/* 133 */     return (K)this.keyTransformer.transform(object);
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
/*     */   protected V transformValue(V object) {
/* 145 */     if (this.valueTransformer == null) {
/* 146 */       return object;
/*     */     }
/* 148 */     return (V)this.valueTransformer.transform(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean put(K key, V value) {
/* 153 */     return decorated().put(transformKey(key), transformValue(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean putAll(K key, Iterable<? extends V> values) {
/* 158 */     if (values == null) {
/* 159 */       throw new NullPointerException("Values must not be null.");
/*     */     }
/*     */     
/* 162 */     FluentIterable<? extends V> fluentIterable = FluentIterable.of(values).transform(this.valueTransformer);
/* 163 */     Iterator<? extends V> it = fluentIterable.iterator();
/* 164 */     return (it.hasNext() && CollectionUtils.addAll(decorated().get(transformKey(key)), it));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean putAll(Map<? extends K, ? extends V> map) {
/* 169 */     if (map == null) {
/* 170 */       throw new NullPointerException("Map must not be null.");
/*     */     }
/* 172 */     boolean changed = false;
/* 173 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 174 */       changed |= put(entry.getKey(), entry.getValue());
/*     */     }
/* 176 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean putAll(MultiValuedMap<? extends K, ? extends V> map) {
/* 181 */     if (map == null) {
/* 182 */       throw new NullPointerException("Map must not be null.");
/*     */     }
/* 184 */     boolean changed = false;
/* 185 */     for (Map.Entry<? extends K, ? extends V> entry : (Iterable<Map.Entry<? extends K, ? extends V>>)map.entries()) {
/* 186 */       changed |= put(entry.getKey(), entry.getValue());
/*     */     }
/* 188 */     return changed;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\multimap\TransformedMultiValuedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */