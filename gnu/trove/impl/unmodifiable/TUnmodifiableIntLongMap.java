/*     */ package gnu.trove.impl.unmodifiable;
/*     */ 
/*     */ import gnu.trove.TCollections;
/*     */ import gnu.trove.TLongCollection;
/*     */ import gnu.trove.function.TLongFunction;
/*     */ import gnu.trove.iterator.TIntLongIterator;
/*     */ import gnu.trove.map.TIntLongMap;
/*     */ import gnu.trove.procedure.TIntLongProcedure;
/*     */ import gnu.trove.procedure.TIntProcedure;
/*     */ import gnu.trove.procedure.TLongProcedure;
/*     */ import gnu.trove.set.TIntSet;
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
/*     */ 
/*     */ public class TUnmodifiableIntLongMap
/*     */   implements TIntLongMap, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -1034234728574286014L;
/*     */   private final TIntLongMap m;
/*     */   
/*     */   public TUnmodifiableIntLongMap(TIntLongMap m) {
/*  58 */     if (m == null)
/*  59 */       throw new NullPointerException(); 
/*  60 */     this.m = m;
/*     */   }
/*     */   
/*  63 */   public int size() { return this.m.size(); }
/*  64 */   public boolean isEmpty() { return this.m.isEmpty(); }
/*  65 */   public boolean containsKey(int key) { return this.m.containsKey(key); }
/*  66 */   public boolean containsValue(long val) { return this.m.containsValue(val); } public long get(int key) {
/*  67 */     return this.m.get(key);
/*     */   }
/*  69 */   public long put(int key, long value) { throw new UnsupportedOperationException(); }
/*  70 */   public long remove(int key) { throw new UnsupportedOperationException(); }
/*  71 */   public void putAll(TIntLongMap m) { throw new UnsupportedOperationException(); }
/*  72 */   public void putAll(Map<? extends Integer, ? extends Long> map) { throw new UnsupportedOperationException(); } public void clear() {
/*  73 */     throw new UnsupportedOperationException();
/*     */   }
/*  75 */   private transient TIntSet keySet = null;
/*  76 */   private transient TLongCollection values = null;
/*     */   
/*     */   public TIntSet keySet() {
/*  79 */     if (this.keySet == null)
/*  80 */       this.keySet = TCollections.unmodifiableSet(this.m.keySet()); 
/*  81 */     return this.keySet;
/*     */   }
/*  83 */   public int[] keys() { return this.m.keys(); } public int[] keys(int[] array) {
/*  84 */     return this.m.keys(array);
/*     */   }
/*     */   public TLongCollection valueCollection() {
/*  87 */     if (this.values == null)
/*  88 */       this.values = TCollections.unmodifiableCollection(this.m.valueCollection()); 
/*  89 */     return this.values;
/*     */   }
/*  91 */   public long[] values() { return this.m.values(); } public long[] values(long[] array) {
/*  92 */     return this.m.values(array);
/*     */   }
/*  94 */   public boolean equals(Object o) { return (o == this || this.m.equals(o)); }
/*  95 */   public int hashCode() { return this.m.hashCode(); }
/*  96 */   public String toString() { return this.m.toString(); }
/*  97 */   public int getNoEntryKey() { return this.m.getNoEntryKey(); } public long getNoEntryValue() {
/*  98 */     return this.m.getNoEntryValue();
/*     */   }
/*     */   public boolean forEachKey(TIntProcedure procedure) {
/* 101 */     return this.m.forEachKey(procedure);
/*     */   }
/*     */   public boolean forEachValue(TLongProcedure procedure) {
/* 104 */     return this.m.forEachValue(procedure);
/*     */   }
/*     */   public boolean forEachEntry(TIntLongProcedure procedure) {
/* 107 */     return this.m.forEachEntry(procedure);
/*     */   }
/*     */   
/*     */   public TIntLongIterator iterator() {
/* 111 */     return new TIntLongIterator() {
/* 112 */         TIntLongIterator iter = TUnmodifiableIntLongMap.this.m.iterator();
/*     */         
/* 114 */         public int key() { return this.iter.key(); }
/* 115 */         public long value() { return this.iter.value(); }
/* 116 */         public void advance() { this.iter.advance(); }
/* 117 */         public boolean hasNext() { return this.iter.hasNext(); }
/* 118 */         public long setValue(long val) { throw new UnsupportedOperationException(); } public void remove() {
/* 119 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/*     */   }
/* 123 */   public long putIfAbsent(int key, long value) { throw new UnsupportedOperationException(); }
/* 124 */   public void transformValues(TLongFunction function) { throw new UnsupportedOperationException(); }
/* 125 */   public boolean retainEntries(TIntLongProcedure procedure) { throw new UnsupportedOperationException(); }
/* 126 */   public boolean increment(int key) { throw new UnsupportedOperationException(); }
/* 127 */   public boolean adjustValue(int key, long amount) { throw new UnsupportedOperationException(); } public long adjustOrPutValue(int key, long adjust_amount, long put_amount) {
/* 128 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\imp\\unmodifiable\TUnmodifiableIntLongMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */