/*     */ package gnu.trove.impl.unmodifiable;
/*     */ 
/*     */ import gnu.trove.TCollections;
/*     */ import gnu.trove.TShortCollection;
/*     */ import gnu.trove.function.TShortFunction;
/*     */ import gnu.trove.iterator.TIntShortIterator;
/*     */ import gnu.trove.map.TIntShortMap;
/*     */ import gnu.trove.procedure.TIntProcedure;
/*     */ import gnu.trove.procedure.TIntShortProcedure;
/*     */ import gnu.trove.procedure.TShortProcedure;
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
/*     */ public class TUnmodifiableIntShortMap
/*     */   implements TIntShortMap, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -1034234728574286014L;
/*     */   private final TIntShortMap m;
/*     */   
/*     */   public TUnmodifiableIntShortMap(TIntShortMap m) {
/*  58 */     if (m == null)
/*  59 */       throw new NullPointerException(); 
/*  60 */     this.m = m;
/*     */   }
/*     */   
/*  63 */   public int size() { return this.m.size(); }
/*  64 */   public boolean isEmpty() { return this.m.isEmpty(); }
/*  65 */   public boolean containsKey(int key) { return this.m.containsKey(key); }
/*  66 */   public boolean containsValue(short val) { return this.m.containsValue(val); } public short get(int key) {
/*  67 */     return this.m.get(key);
/*     */   }
/*  69 */   public short put(int key, short value) { throw new UnsupportedOperationException(); }
/*  70 */   public short remove(int key) { throw new UnsupportedOperationException(); }
/*  71 */   public void putAll(TIntShortMap m) { throw new UnsupportedOperationException(); }
/*  72 */   public void putAll(Map<? extends Integer, ? extends Short> map) { throw new UnsupportedOperationException(); } public void clear() {
/*  73 */     throw new UnsupportedOperationException();
/*     */   }
/*  75 */   private transient TIntSet keySet = null;
/*  76 */   private transient TShortCollection values = null;
/*     */   
/*     */   public TIntSet keySet() {
/*  79 */     if (this.keySet == null)
/*  80 */       this.keySet = TCollections.unmodifiableSet(this.m.keySet()); 
/*  81 */     return this.keySet;
/*     */   }
/*  83 */   public int[] keys() { return this.m.keys(); } public int[] keys(int[] array) {
/*  84 */     return this.m.keys(array);
/*     */   }
/*     */   public TShortCollection valueCollection() {
/*  87 */     if (this.values == null)
/*  88 */       this.values = TCollections.unmodifiableCollection(this.m.valueCollection()); 
/*  89 */     return this.values;
/*     */   }
/*  91 */   public short[] values() { return this.m.values(); } public short[] values(short[] array) {
/*  92 */     return this.m.values(array);
/*     */   }
/*  94 */   public boolean equals(Object o) { return (o == this || this.m.equals(o)); }
/*  95 */   public int hashCode() { return this.m.hashCode(); }
/*  96 */   public String toString() { return this.m.toString(); }
/*  97 */   public int getNoEntryKey() { return this.m.getNoEntryKey(); } public short getNoEntryValue() {
/*  98 */     return this.m.getNoEntryValue();
/*     */   }
/*     */   public boolean forEachKey(TIntProcedure procedure) {
/* 101 */     return this.m.forEachKey(procedure);
/*     */   }
/*     */   public boolean forEachValue(TShortProcedure procedure) {
/* 104 */     return this.m.forEachValue(procedure);
/*     */   }
/*     */   public boolean forEachEntry(TIntShortProcedure procedure) {
/* 107 */     return this.m.forEachEntry(procedure);
/*     */   }
/*     */   
/*     */   public TIntShortIterator iterator() {
/* 111 */     return new TIntShortIterator() {
/* 112 */         TIntShortIterator iter = TUnmodifiableIntShortMap.this.m.iterator();
/*     */         
/* 114 */         public int key() { return this.iter.key(); }
/* 115 */         public short value() { return this.iter.value(); }
/* 116 */         public void advance() { this.iter.advance(); }
/* 117 */         public boolean hasNext() { return this.iter.hasNext(); }
/* 118 */         public short setValue(short val) { throw new UnsupportedOperationException(); } public void remove() {
/* 119 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/*     */   }
/* 123 */   public short putIfAbsent(int key, short value) { throw new UnsupportedOperationException(); }
/* 124 */   public void transformValues(TShortFunction function) { throw new UnsupportedOperationException(); }
/* 125 */   public boolean retainEntries(TIntShortProcedure procedure) { throw new UnsupportedOperationException(); }
/* 126 */   public boolean increment(int key) { throw new UnsupportedOperationException(); }
/* 127 */   public boolean adjustValue(int key, short amount) { throw new UnsupportedOperationException(); } public short adjustOrPutValue(int key, short adjust_amount, short put_amount) {
/* 128 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\imp\\unmodifiable\TUnmodifiableIntShortMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */