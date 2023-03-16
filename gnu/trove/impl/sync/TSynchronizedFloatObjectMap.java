/*     */ package gnu.trove.impl.sync;
/*     */ 
/*     */ import gnu.trove.function.TObjectFunction;
/*     */ import gnu.trove.iterator.TFloatObjectIterator;
/*     */ import gnu.trove.map.TFloatObjectMap;
/*     */ import gnu.trove.procedure.TFloatObjectProcedure;
/*     */ import gnu.trove.procedure.TFloatProcedure;
/*     */ import gnu.trove.procedure.TObjectProcedure;
/*     */ import gnu.trove.set.TFloatSet;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TSynchronizedFloatObjectMap<V>
/*     */   implements TFloatObjectMap<V>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1978198479659022715L;
/*     */   private final TFloatObjectMap<V> m;
/*     */   final Object mutex;
/*     */   
/*     */   public TSynchronizedFloatObjectMap(TFloatObjectMap<V> m) {
/*  61 */     if (m == null)
/*  62 */       throw new NullPointerException(); 
/*  63 */     this.m = m;
/*  64 */     this.mutex = this;
/*     */   }
/*     */   
/*     */   public TSynchronizedFloatObjectMap(TFloatObjectMap<V> m, Object mutex) {
/*  68 */     this.m = m;
/*  69 */     this.mutex = mutex;
/*     */   }
/*     */   
/*     */   public int size() {
/*  73 */     synchronized (this.mutex) { return this.m.size(); }
/*     */   
/*     */   } public boolean isEmpty() {
/*  76 */     synchronized (this.mutex) { return this.m.isEmpty(); }
/*     */   
/*     */   } public boolean containsKey(float key) {
/*  79 */     synchronized (this.mutex) { return this.m.containsKey(key); }
/*     */   
/*     */   } public boolean containsValue(Object value) {
/*  82 */     synchronized (this.mutex) { return this.m.containsValue(value); }
/*     */   
/*     */   } public V get(float key) {
/*  85 */     synchronized (this.mutex) { return (V)this.m.get(key); }
/*     */   
/*     */   }
/*     */   public V put(float key, V value) {
/*  89 */     synchronized (this.mutex) { return (V)this.m.put(key, value); }
/*     */   
/*     */   } public V remove(float key) {
/*  92 */     synchronized (this.mutex) { return (V)this.m.remove(key); }
/*     */   
/*     */   } public void putAll(Map<? extends Float, ? extends V> map) {
/*  95 */     synchronized (this.mutex) { this.m.putAll(map); }
/*     */   
/*     */   } public void putAll(TFloatObjectMap<? extends V> map) {
/*  98 */     synchronized (this.mutex) { this.m.putAll(map); }
/*     */   
/*     */   } public void clear() {
/* 101 */     synchronized (this.mutex) { this.m.clear(); }
/*     */   
/*     */   }
/* 104 */   private transient TFloatSet keySet = null;
/* 105 */   private transient Collection<V> values = null;
/*     */   
/*     */   public TFloatSet keySet() {
/* 108 */     synchronized (this.mutex) {
/* 109 */       if (this.keySet == null)
/* 110 */         this.keySet = new TSynchronizedFloatSet(this.m.keySet(), this.mutex); 
/* 111 */       return this.keySet;
/*     */     } 
/*     */   }
/*     */   public float[] keys() {
/* 115 */     synchronized (this.mutex) { return this.m.keys(); }
/*     */   
/*     */   } public float[] keys(float[] array) {
/* 118 */     synchronized (this.mutex) { return this.m.keys(array); }
/*     */   
/*     */   }
/*     */   public Collection<V> valueCollection() {
/* 122 */     synchronized (this.mutex) {
/* 123 */       if (this.values == null) {
/* 124 */         this.values = new SynchronizedCollection<V>(this.m.valueCollection(), this.mutex);
/*     */       }
/* 126 */       return this.values;
/*     */     } 
/*     */   }
/*     */   public Object[] values() {
/* 130 */     synchronized (this.mutex) { return this.m.values(); }
/*     */   
/*     */   } public V[] values(V[] array) {
/* 133 */     synchronized (this.mutex) { return (V[])this.m.values((Object[])array); }
/*     */   
/*     */   }
/*     */   public TFloatObjectIterator<V> iterator() {
/* 137 */     return this.m.iterator();
/*     */   }
/*     */   
/*     */   public float getNoEntryKey() {
/* 141 */     return this.m.getNoEntryKey();
/*     */   }
/*     */   public V putIfAbsent(float key, V value) {
/* 144 */     synchronized (this.mutex) { return (V)this.m.putIfAbsent(key, value); }
/*     */   
/*     */   } public boolean forEachKey(TFloatProcedure procedure) {
/* 147 */     synchronized (this.mutex) { return this.m.forEachKey(procedure); }
/*     */   
/*     */   } public boolean forEachValue(TObjectProcedure<? super V> procedure) {
/* 150 */     synchronized (this.mutex) { return this.m.forEachValue(procedure); }
/*     */   
/*     */   } public boolean forEachEntry(TFloatObjectProcedure<? super V> procedure) {
/* 153 */     synchronized (this.mutex) { return this.m.forEachEntry(procedure); }
/*     */   
/*     */   } public void transformValues(TObjectFunction<V, V> function) {
/* 156 */     synchronized (this.mutex) { this.m.transformValues(function); }
/*     */   
/*     */   } public boolean retainEntries(TFloatObjectProcedure<? super V> procedure) {
/* 159 */     synchronized (this.mutex) { return this.m.retainEntries(procedure); }
/*     */   
/*     */   }
/*     */   public boolean equals(Object o) {
/* 163 */     synchronized (this.mutex) { return this.m.equals(o); }
/*     */   
/*     */   } public int hashCode() {
/* 166 */     synchronized (this.mutex) { return this.m.hashCode(); }
/*     */   
/*     */   } public String toString() {
/* 169 */     synchronized (this.mutex) { return this.m.toString(); }
/*     */   
/*     */   } private void writeObject(ObjectOutputStream s) throws IOException {
/* 172 */     synchronized (this.mutex) { s.defaultWriteObject(); }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\impl\sync\TSynchronizedFloatObjectMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */