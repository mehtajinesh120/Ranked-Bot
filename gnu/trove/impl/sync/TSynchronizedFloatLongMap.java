/*     */ package gnu.trove.impl.sync;
/*     */ 
/*     */ import gnu.trove.TLongCollection;
/*     */ import gnu.trove.function.TLongFunction;
/*     */ import gnu.trove.iterator.TFloatLongIterator;
/*     */ import gnu.trove.map.TFloatLongMap;
/*     */ import gnu.trove.procedure.TFloatLongProcedure;
/*     */ import gnu.trove.procedure.TFloatProcedure;
/*     */ import gnu.trove.procedure.TLongProcedure;
/*     */ import gnu.trove.set.TFloatSet;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
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
/*     */ public class TSynchronizedFloatLongMap
/*     */   implements TFloatLongMap, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1978198479659022715L;
/*     */   private final TFloatLongMap m;
/*     */   final Object mutex;
/*     */   
/*     */   public TSynchronizedFloatLongMap(TFloatLongMap m) {
/*  59 */     if (m == null)
/*  60 */       throw new NullPointerException(); 
/*  61 */     this.m = m;
/*  62 */     this.mutex = this;
/*     */   }
/*     */   
/*     */   public TSynchronizedFloatLongMap(TFloatLongMap m, Object mutex) {
/*  66 */     this.m = m;
/*  67 */     this.mutex = mutex;
/*     */   }
/*     */   
/*     */   public int size() {
/*  71 */     synchronized (this.mutex) { return this.m.size(); }
/*     */   
/*     */   } public boolean isEmpty() {
/*  74 */     synchronized (this.mutex) { return this.m.isEmpty(); }
/*     */   
/*     */   } public boolean containsKey(float key) {
/*  77 */     synchronized (this.mutex) { return this.m.containsKey(key); }
/*     */   
/*     */   } public boolean containsValue(long value) {
/*  80 */     synchronized (this.mutex) { return this.m.containsValue(value); }
/*     */   
/*     */   } public long get(float key) {
/*  83 */     synchronized (this.mutex) { return this.m.get(key); }
/*     */   
/*     */   }
/*     */   public long put(float key, long value) {
/*  87 */     synchronized (this.mutex) { return this.m.put(key, value); }
/*     */   
/*     */   } public long remove(float key) {
/*  90 */     synchronized (this.mutex) { return this.m.remove(key); }
/*     */   
/*     */   } public void putAll(Map<? extends Float, ? extends Long> map) {
/*  93 */     synchronized (this.mutex) { this.m.putAll(map); }
/*     */   
/*     */   } public void putAll(TFloatLongMap map) {
/*  96 */     synchronized (this.mutex) { this.m.putAll(map); }
/*     */   
/*     */   } public void clear() {
/*  99 */     synchronized (this.mutex) { this.m.clear(); }
/*     */   
/*     */   }
/* 102 */   private transient TFloatSet keySet = null;
/* 103 */   private transient TLongCollection values = null;
/*     */   
/*     */   public TFloatSet keySet() {
/* 106 */     synchronized (this.mutex) {
/* 107 */       if (this.keySet == null)
/* 108 */         this.keySet = new TSynchronizedFloatSet(this.m.keySet(), this.mutex); 
/* 109 */       return this.keySet;
/*     */     } 
/*     */   }
/*     */   public float[] keys() {
/* 113 */     synchronized (this.mutex) { return this.m.keys(); }
/*     */   
/*     */   } public float[] keys(float[] array) {
/* 116 */     synchronized (this.mutex) { return this.m.keys(array); }
/*     */   
/*     */   }
/*     */   public TLongCollection valueCollection() {
/* 120 */     synchronized (this.mutex) {
/* 121 */       if (this.values == null)
/* 122 */         this.values = new TSynchronizedLongCollection(this.m.valueCollection(), this.mutex); 
/* 123 */       return this.values;
/*     */     } 
/*     */   }
/*     */   public long[] values() {
/* 127 */     synchronized (this.mutex) { return this.m.values(); }
/*     */   
/*     */   } public long[] values(long[] array) {
/* 130 */     synchronized (this.mutex) { return this.m.values(array); }
/*     */   
/*     */   }
/*     */   public TFloatLongIterator iterator() {
/* 134 */     return this.m.iterator();
/*     */   }
/*     */   
/*     */   public float getNoEntryKey() {
/* 138 */     return this.m.getNoEntryKey(); } public long getNoEntryValue() {
/* 139 */     return this.m.getNoEntryValue();
/*     */   }
/*     */   public long putIfAbsent(float key, long value) {
/* 142 */     synchronized (this.mutex) { return this.m.putIfAbsent(key, value); }
/*     */   
/*     */   } public boolean forEachKey(TFloatProcedure procedure) {
/* 145 */     synchronized (this.mutex) { return this.m.forEachKey(procedure); }
/*     */   
/*     */   } public boolean forEachValue(TLongProcedure procedure) {
/* 148 */     synchronized (this.mutex) { return this.m.forEachValue(procedure); }
/*     */   
/*     */   } public boolean forEachEntry(TFloatLongProcedure procedure) {
/* 151 */     synchronized (this.mutex) { return this.m.forEachEntry(procedure); }
/*     */   
/*     */   } public void transformValues(TLongFunction function) {
/* 154 */     synchronized (this.mutex) { this.m.transformValues(function); }
/*     */   
/*     */   } public boolean retainEntries(TFloatLongProcedure procedure) {
/* 157 */     synchronized (this.mutex) { return this.m.retainEntries(procedure); }
/*     */   
/*     */   } public boolean increment(float key) {
/* 160 */     synchronized (this.mutex) { return this.m.increment(key); }
/*     */   
/*     */   } public boolean adjustValue(float key, long amount) {
/* 163 */     synchronized (this.mutex) { return this.m.adjustValue(key, amount); }
/*     */   
/*     */   } public long adjustOrPutValue(float key, long adjust_amount, long put_amount) {
/* 166 */     synchronized (this.mutex) { return this.m.adjustOrPutValue(key, adjust_amount, put_amount); }
/*     */   
/*     */   }
/*     */   public boolean equals(Object o) {
/* 170 */     synchronized (this.mutex) { return this.m.equals(o); }
/*     */   
/*     */   } public int hashCode() {
/* 173 */     synchronized (this.mutex) { return this.m.hashCode(); }
/*     */   
/*     */   } public String toString() {
/* 176 */     synchronized (this.mutex) { return this.m.toString(); }
/*     */   
/*     */   } private void writeObject(ObjectOutputStream s) throws IOException {
/* 179 */     synchronized (this.mutex) { s.defaultWriteObject(); }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\impl\sync\TSynchronizedFloatLongMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */