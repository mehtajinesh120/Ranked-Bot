/*     */ package org.apache.commons.collections4.set;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MapBackedSet<E, V>
/*     */   implements Set<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6723912213766056587L;
/*     */   private final Map<E, ? super V> map;
/*     */   private final V dummyValue;
/*     */   
/*     */   public static <E, V> MapBackedSet<E, V> mapBackedSet(Map<E, ? super V> map) {
/*  60 */     return mapBackedSet(map, null);
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
/*     */   public static <E, V> MapBackedSet<E, V> mapBackedSet(Map<E, ? super V> map, V dummyValue) {
/*  75 */     return new MapBackedSet<E, V>(map, dummyValue);
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
/*     */   private MapBackedSet(Map<E, ? super V> map, V dummyValue) {
/*  88 */     if (map == null) {
/*  89 */       throw new NullPointerException("The map must not be null");
/*     */     }
/*  91 */     this.map = map;
/*  92 */     this.dummyValue = dummyValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  97 */     return this.map.size();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 101 */     return this.map.isEmpty();
/*     */   }
/*     */   
/*     */   public Iterator<E> iterator() {
/* 105 */     return this.map.keySet().iterator();
/*     */   }
/*     */   
/*     */   public boolean contains(Object obj) {
/* 109 */     return this.map.containsKey(obj);
/*     */   }
/*     */   
/*     */   public boolean containsAll(Collection<?> coll) {
/* 113 */     return this.map.keySet().containsAll(coll);
/*     */   }
/*     */   
/*     */   public boolean add(E obj) {
/* 117 */     int size = this.map.size();
/* 118 */     this.map.put(obj, this.dummyValue);
/* 119 */     return (this.map.size() != size);
/*     */   }
/*     */   
/*     */   public boolean addAll(Collection<? extends E> coll) {
/* 123 */     int size = this.map.size();
/* 124 */     for (E e : coll) {
/* 125 */       this.map.put(e, this.dummyValue);
/*     */     }
/* 127 */     return (this.map.size() != size);
/*     */   }
/*     */   
/*     */   public boolean remove(Object obj) {
/* 131 */     int size = this.map.size();
/* 132 */     this.map.remove(obj);
/* 133 */     return (this.map.size() != size);
/*     */   }
/*     */   
/*     */   public boolean removeAll(Collection<?> coll) {
/* 137 */     return this.map.keySet().removeAll(coll);
/*     */   }
/*     */   
/*     */   public boolean retainAll(Collection<?> coll) {
/* 141 */     return this.map.keySet().retainAll(coll);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 145 */     this.map.clear();
/*     */   }
/*     */   
/*     */   public Object[] toArray() {
/* 149 */     return this.map.keySet().toArray();
/*     */   }
/*     */   
/*     */   public <T> T[] toArray(T[] array) {
/* 153 */     return (T[])this.map.keySet().toArray((Object[])array);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 158 */     return this.map.keySet().equals(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 163 */     return this.map.keySet().hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\set\MapBackedSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */