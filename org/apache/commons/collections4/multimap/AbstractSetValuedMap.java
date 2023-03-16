/*     */ package org.apache.commons.collections4.multimap;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.SetUtils;
/*     */ import org.apache.commons.collections4.SetValuedMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractSetValuedMap<K, V>
/*     */   extends AbstractMultiValuedMap<K, V>
/*     */   implements SetValuedMap<K, V>
/*     */ {
/*     */   protected AbstractSetValuedMap() {}
/*     */   
/*     */   protected AbstractSetValuedMap(Map<K, ? extends Set<V>> map) {
/*  53 */     super((Map)map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<K, Set<V>> getMap() {
/*  60 */     return (Map)super.getMap();
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
/*     */   public Set<V> get(K key) {
/*  81 */     return wrappedCollection(key);
/*     */   }
/*     */ 
/*     */   
/*     */   Set<V> wrappedCollection(K key) {
/*  86 */     return new WrappedSet(key);
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
/*     */   public Set<V> remove(Object key) {
/* 100 */     return SetUtils.emptyIfNull(getMap().remove(key));
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract Set<V> createCollection();
/*     */   
/*     */   private class WrappedSet
/*     */     extends AbstractMultiValuedMap<K, V>.WrappedCollection
/*     */     implements Set<V>
/*     */   {
/*     */     public WrappedSet(K key) {
/* 111 */       super(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 116 */       Set<V> set = (Set<V>)getMapping();
/* 117 */       if (set == null) {
/* 118 */         return Collections.emptySet().equals(other);
/*     */       }
/* 120 */       if (!(other instanceof Set)) {
/* 121 */         return false;
/*     */       }
/* 123 */       Set<?> otherSet = (Set)other;
/* 124 */       return SetUtils.isEqualSet(set, otherSet);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 129 */       Set<V> set = (Set<V>)getMapping();
/* 130 */       return SetUtils.hashCodeForSet(set);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\multimap\AbstractSetValuedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */