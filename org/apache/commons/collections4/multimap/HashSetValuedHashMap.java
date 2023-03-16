/*     */ package org.apache.commons.collections4.multimap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.MultiValuedMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HashSetValuedHashMap<K, V>
/*     */   extends AbstractSetValuedMap<K, V>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 20151118L;
/*     */   private static final int DEFAULT_INITIAL_MAP_CAPACITY = 16;
/*     */   private static final int DEFAULT_INITIAL_SET_CAPACITY = 3;
/*     */   private final int initialSetCapacity;
/*     */   
/*     */   public HashSetValuedHashMap() {
/*  68 */     this(16, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashSetValuedHashMap(int initialSetCapacity) {
/*  78 */     this(16, initialSetCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashSetValuedHashMap(int initialMapCapacity, int initialSetCapacity) {
/*  89 */     super(new HashMap<K, Set<V>>(initialMapCapacity));
/*  90 */     this.initialSetCapacity = initialSetCapacity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashSetValuedHashMap(MultiValuedMap<? extends K, ? extends V> map) {
/*  99 */     this(map.size(), 3);
/* 100 */     putAll(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashSetValuedHashMap(Map<? extends K, ? extends V> map) {
/* 109 */     this(map.size(), 3);
/* 110 */     putAll(map);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected HashSet<V> createCollection() {
/* 116 */     return new HashSet<V>(this.initialSetCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream oos) throws IOException {
/* 121 */     oos.defaultWriteObject();
/* 122 */     doWriteObject(oos);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 126 */     ois.defaultReadObject();
/* 127 */     setMap(new HashMap<K, Collection<V>>());
/* 128 */     doReadObject(ois);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\multimap\HashSetValuedHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */