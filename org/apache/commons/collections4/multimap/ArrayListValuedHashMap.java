/*     */ package org.apache.commons.collections4.multimap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ public class ArrayListValuedHashMap<K, V>
/*     */   extends AbstractListValuedMap<K, V>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 20151118L;
/*     */   private static final int DEFAULT_INITIAL_MAP_CAPACITY = 16;
/*     */   private static final int DEFAULT_INITIAL_LIST_CAPACITY = 3;
/*     */   private final int initialListCapacity;
/*     */   
/*     */   public ArrayListValuedHashMap() {
/*  69 */     this(16, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayListValuedHashMap(int initialListCapacity) {
/*  79 */     this(16, initialListCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayListValuedHashMap(int initialMapCapacity, int initialListCapacity) {
/*  90 */     super(new HashMap<K, List<V>>(initialMapCapacity));
/*  91 */     this.initialListCapacity = initialListCapacity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayListValuedHashMap(MultiValuedMap<? extends K, ? extends V> map) {
/* 100 */     this(map.size(), 3);
/* 101 */     putAll(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayListValuedHashMap(Map<? extends K, ? extends V> map) {
/* 110 */     this(map.size(), 3);
/* 111 */     putAll(map);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ArrayList<V> createCollection() {
/* 117 */     return new ArrayList<V>(this.initialListCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trimToSize() {
/* 125 */     for (Collection<V> coll : getMap().values()) {
/* 126 */       ArrayList<V> list = (ArrayList<V>)coll;
/* 127 */       list.trimToSize();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream oos) throws IOException {
/* 133 */     oos.defaultWriteObject();
/* 134 */     doWriteObject(oos);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 138 */     ois.defaultReadObject();
/* 139 */     setMap(new HashMap<K, Collection<V>>());
/* 140 */     doReadObject(ois);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\multimap\ArrayListValuedHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */