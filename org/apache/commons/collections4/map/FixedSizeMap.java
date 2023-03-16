/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.BoundedMap;
/*     */ import org.apache.commons.collections4.collection.UnmodifiableCollection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FixedSizeMap<K, V>
/*     */   extends AbstractMapDecorator<K, V>
/*     */   implements BoundedMap<K, V>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 7450927208116179316L;
/*     */   
/*     */   public static <K, V> FixedSizeMap<K, V> fixedSizeMap(Map<K, V> map) {
/*  73 */     return new FixedSizeMap<K, V>(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FixedSizeMap(Map<K, V> map) {
/*  84 */     super(map);
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
/*  96 */     out.defaultWriteObject();
/*  97 */     out.writeObject(this.map);
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
/* 110 */     in.defaultReadObject();
/* 111 */     this.map = (Map<K, V>)in.readObject();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/* 117 */     if (!this.map.containsKey(key)) {
/* 118 */       throw new IllegalArgumentException("Cannot put new key/value pair - Map is fixed size");
/*     */     }
/* 120 */     return this.map.put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> mapToCopy) {
/* 125 */     for (K key : mapToCopy.keySet()) {
/* 126 */       if (!containsKey(key)) {
/* 127 */         throw new IllegalArgumentException("Cannot put new key/value pair - Map is fixed size");
/*     */       }
/*     */     } 
/* 130 */     this.map.putAll(mapToCopy);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 135 */     throw new UnsupportedOperationException("Map is fixed size");
/*     */   }
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/* 140 */     throw new UnsupportedOperationException("Map is fixed size");
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 145 */     Set<Map.Entry<K, V>> set = this.map.entrySet();
/*     */     
/* 147 */     return UnmodifiableSet.unmodifiableSet(set);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 152 */     Set<K> set = this.map.keySet();
/* 153 */     return UnmodifiableSet.unmodifiableSet(set);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 158 */     Collection<V> coll = this.map.values();
/* 159 */     return UnmodifiableCollection.unmodifiableCollection(coll);
/*     */   }
/*     */   
/*     */   public boolean isFull() {
/* 163 */     return true;
/*     */   }
/*     */   
/*     */   public int maxSize() {
/* 167 */     return size();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\FixedSizeMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */