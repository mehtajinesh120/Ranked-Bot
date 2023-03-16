/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.IterableMap;
/*     */ import org.apache.commons.collections4.MapIterator;
/*     */ import org.apache.commons.collections4.Unmodifiable;
/*     */ import org.apache.commons.collections4.collection.UnmodifiableCollection;
/*     */ import org.apache.commons.collections4.iterators.EntrySetMapIterator;
/*     */ import org.apache.commons.collections4.iterators.UnmodifiableMapIterator;
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
/*     */ public final class UnmodifiableMap<K, V>
/*     */   extends AbstractMapDecorator<K, V>
/*     */   implements Unmodifiable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2737023427269031941L;
/*     */   
/*     */   public static <K, V> Map<K, V> unmodifiableMap(Map<? extends K, ? extends V> map) {
/*  63 */     if (map instanceof Unmodifiable)
/*     */     {
/*  65 */       return (Map)map;
/*     */     }
/*     */     
/*  68 */     return (Map<K, V>)new UnmodifiableMap<K, V>(map);
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
/*     */   private UnmodifiableMap(Map<? extends K, ? extends V> map) {
/*  80 */     super((Map)map);
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
/*  92 */     out.defaultWriteObject();
/*  93 */     out.writeObject(this.map);
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
/* 106 */     in.defaultReadObject();
/* 107 */     this.map = (Map<K, V>)in.readObject();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 113 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/* 118 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> mapToCopy) {
/* 123 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/* 128 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public MapIterator<K, V> mapIterator() {
/* 133 */     if (this.map instanceof IterableMap) {
/* 134 */       MapIterator<K, V> it = ((IterableMap)this.map).mapIterator();
/* 135 */       return UnmodifiableMapIterator.unmodifiableMapIterator(it);
/*     */     } 
/* 137 */     EntrySetMapIterator entrySetMapIterator = new EntrySetMapIterator(this.map);
/* 138 */     return UnmodifiableMapIterator.unmodifiableMapIterator((MapIterator)entrySetMapIterator);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 143 */     Set<Map.Entry<K, V>> set = super.entrySet();
/* 144 */     return UnmodifiableEntrySet.unmodifiableEntrySet(set);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 149 */     Set<K> set = super.keySet();
/* 150 */     return UnmodifiableSet.unmodifiableSet(set);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 155 */     Collection<V> coll = super.values();
/* 156 */     return UnmodifiableCollection.unmodifiableCollection(coll);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\UnmodifiableMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */