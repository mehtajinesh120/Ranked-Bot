/*     */ package org.apache.commons.collections4.bidimap;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.BidiMap;
/*     */ import org.apache.commons.collections4.MapIterator;
/*     */ import org.apache.commons.collections4.Unmodifiable;
/*     */ import org.apache.commons.collections4.iterators.UnmodifiableMapIterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UnmodifiableBidiMap<K, V>
/*     */   extends AbstractBidiMapDecorator<K, V>
/*     */   implements Unmodifiable
/*     */ {
/*     */   private UnmodifiableBidiMap<V, K> inverse;
/*     */   
/*     */   public static <K, V> BidiMap<K, V> unmodifiableBidiMap(BidiMap<? extends K, ? extends V> map) {
/*  56 */     if (map instanceof Unmodifiable)
/*     */     {
/*  58 */       return (BidiMap)map;
/*     */     }
/*     */     
/*  61 */     return new UnmodifiableBidiMap<K, V>(map);
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
/*     */   private UnmodifiableBidiMap(BidiMap<? extends K, ? extends V> map) {
/*  73 */     super((BidiMap)map);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  79 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/*  84 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> mapToCopy) {
/*  89 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/*  94 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/*  99 */     Set<Map.Entry<K, V>> set = super.entrySet();
/* 100 */     return UnmodifiableEntrySet.unmodifiableEntrySet(set);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 105 */     Set<K> set = super.keySet();
/* 106 */     return UnmodifiableSet.unmodifiableSet(set);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<V> values() {
/* 111 */     Set<V> set = super.values();
/* 112 */     return UnmodifiableSet.unmodifiableSet(set);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public K removeValue(Object value) {
/* 118 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public MapIterator<K, V> mapIterator() {
/* 123 */     MapIterator<K, V> it = decorated().mapIterator();
/* 124 */     return UnmodifiableMapIterator.unmodifiableMapIterator(it);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized BidiMap<V, K> inverseBidiMap() {
/* 129 */     if (this.inverse == null) {
/* 130 */       this.inverse = new UnmodifiableBidiMap(decorated().inverseBidiMap());
/* 131 */       this.inverse.inverse = this;
/*     */     } 
/* 133 */     return this.inverse;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\bidimap\UnmodifiableBidiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */