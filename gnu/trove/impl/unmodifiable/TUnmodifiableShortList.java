/*     */ package gnu.trove.impl.unmodifiable;
/*     */ 
/*     */ import gnu.trove.TShortCollection;
/*     */ import gnu.trove.function.TShortFunction;
/*     */ import gnu.trove.list.TShortList;
/*     */ import gnu.trove.procedure.TShortProcedure;
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TUnmodifiableShortList
/*     */   extends TUnmodifiableShortCollection
/*     */   implements TShortList
/*     */ {
/*     */   static final long serialVersionUID = -283967356065247728L;
/*     */   final TShortList list;
/*     */   
/*     */   public TUnmodifiableShortList(TShortList list) {
/*  58 */     super((TShortCollection)list);
/*  59 */     this.list = list;
/*     */   }
/*     */   
/*  62 */   public boolean equals(Object o) { return (o == this || this.list.equals(o)); } public int hashCode() {
/*  63 */     return this.list.hashCode();
/*     */   }
/*  65 */   public short get(int index) { return this.list.get(index); }
/*  66 */   public int indexOf(short o) { return this.list.indexOf(o); } public int lastIndexOf(short o) {
/*  67 */     return this.list.lastIndexOf(o);
/*     */   }
/*     */   public short[] toArray(int offset, int len) {
/*  70 */     return this.list.toArray(offset, len);
/*     */   }
/*     */   public short[] toArray(short[] dest, int offset, int len) {
/*  73 */     return this.list.toArray(dest, offset, len);
/*     */   }
/*     */   public short[] toArray(short[] dest, int source_pos, int dest_pos, int len) {
/*  76 */     return this.list.toArray(dest, source_pos, dest_pos, len);
/*     */   }
/*     */   
/*     */   public boolean forEachDescending(TShortProcedure procedure) {
/*  80 */     return this.list.forEachDescending(procedure);
/*     */   }
/*     */   public int binarySearch(short value) {
/*  83 */     return this.list.binarySearch(value);
/*     */   } public int binarySearch(short value, int fromIndex, int toIndex) {
/*  85 */     return this.list.binarySearch(value, fromIndex, toIndex);
/*     */   }
/*     */   
/*  88 */   public int indexOf(int offset, short value) { return this.list.indexOf(offset, value); }
/*  89 */   public int lastIndexOf(int offset, short value) { return this.list.lastIndexOf(offset, value); }
/*  90 */   public TShortList grep(TShortProcedure condition) { return this.list.grep(condition); } public TShortList inverseGrep(TShortProcedure condition) {
/*  91 */     return this.list.inverseGrep(condition);
/*     */   }
/*  93 */   public short max() { return this.list.max(); }
/*  94 */   public short min() { return this.list.min(); } public short sum() {
/*  95 */     return this.list.sum();
/*     */   }
/*     */   public TShortList subList(int fromIndex, int toIndex) {
/*  98 */     return new TUnmodifiableShortList(this.list.subList(fromIndex, toIndex));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object readResolve() {
/* 140 */     return (this.list instanceof java.util.RandomAccess) ? new TUnmodifiableRandomAccessShortList(this.list) : this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(short[] vals) {
/* 145 */     throw new UnsupportedOperationException(); } public void add(short[] vals, int offset, int length) {
/* 146 */     throw new UnsupportedOperationException();
/*     */   }
/* 148 */   public short removeAt(int offset) { throw new UnsupportedOperationException(); } public void remove(int offset, int length) {
/* 149 */     throw new UnsupportedOperationException();
/*     */   }
/* 151 */   public void insert(int offset, short value) { throw new UnsupportedOperationException(); }
/* 152 */   public void insert(int offset, short[] values) { throw new UnsupportedOperationException(); } public void insert(int offset, short[] values, int valOffset, int len) {
/* 153 */     throw new UnsupportedOperationException();
/*     */   }
/* 155 */   public short set(int offset, short val) { throw new UnsupportedOperationException(); }
/* 156 */   public void set(int offset, short[] values) { throw new UnsupportedOperationException(); } public void set(int offset, short[] values, int valOffset, int length) {
/* 157 */     throw new UnsupportedOperationException();
/*     */   } public short replace(int offset, short val) {
/* 159 */     throw new UnsupportedOperationException();
/*     */   } public void transformValues(TShortFunction function) {
/* 161 */     throw new UnsupportedOperationException();
/*     */   }
/* 163 */   public void reverse() { throw new UnsupportedOperationException(); }
/* 164 */   public void reverse(int from, int to) { throw new UnsupportedOperationException(); } public void shuffle(Random rand) {
/* 165 */     throw new UnsupportedOperationException();
/*     */   }
/* 167 */   public void sort() { throw new UnsupportedOperationException(); }
/* 168 */   public void sort(int fromIndex, int toIndex) { throw new UnsupportedOperationException(); }
/* 169 */   public void fill(short val) { throw new UnsupportedOperationException(); } public void fill(int fromIndex, int toIndex, short val) {
/* 170 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\imp\\unmodifiable\TUnmodifiableShortList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */