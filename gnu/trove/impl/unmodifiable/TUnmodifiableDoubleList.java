/*     */ package gnu.trove.impl.unmodifiable;
/*     */ 
/*     */ import gnu.trove.TDoubleCollection;
/*     */ import gnu.trove.function.TDoubleFunction;
/*     */ import gnu.trove.list.TDoubleList;
/*     */ import gnu.trove.procedure.TDoubleProcedure;
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
/*     */ public class TUnmodifiableDoubleList
/*     */   extends TUnmodifiableDoubleCollection
/*     */   implements TDoubleList
/*     */ {
/*     */   static final long serialVersionUID = -283967356065247728L;
/*     */   final TDoubleList list;
/*     */   
/*     */   public TUnmodifiableDoubleList(TDoubleList list) {
/*  58 */     super((TDoubleCollection)list);
/*  59 */     this.list = list;
/*     */   }
/*     */   
/*  62 */   public boolean equals(Object o) { return (o == this || this.list.equals(o)); } public int hashCode() {
/*  63 */     return this.list.hashCode();
/*     */   }
/*  65 */   public double get(int index) { return this.list.get(index); }
/*  66 */   public int indexOf(double o) { return this.list.indexOf(o); } public int lastIndexOf(double o) {
/*  67 */     return this.list.lastIndexOf(o);
/*     */   }
/*     */   public double[] toArray(int offset, int len) {
/*  70 */     return this.list.toArray(offset, len);
/*     */   }
/*     */   public double[] toArray(double[] dest, int offset, int len) {
/*  73 */     return this.list.toArray(dest, offset, len);
/*     */   }
/*     */   public double[] toArray(double[] dest, int source_pos, int dest_pos, int len) {
/*  76 */     return this.list.toArray(dest, source_pos, dest_pos, len);
/*     */   }
/*     */   
/*     */   public boolean forEachDescending(TDoubleProcedure procedure) {
/*  80 */     return this.list.forEachDescending(procedure);
/*     */   }
/*     */   public int binarySearch(double value) {
/*  83 */     return this.list.binarySearch(value);
/*     */   } public int binarySearch(double value, int fromIndex, int toIndex) {
/*  85 */     return this.list.binarySearch(value, fromIndex, toIndex);
/*     */   }
/*     */   
/*  88 */   public int indexOf(int offset, double value) { return this.list.indexOf(offset, value); }
/*  89 */   public int lastIndexOf(int offset, double value) { return this.list.lastIndexOf(offset, value); }
/*  90 */   public TDoubleList grep(TDoubleProcedure condition) { return this.list.grep(condition); } public TDoubleList inverseGrep(TDoubleProcedure condition) {
/*  91 */     return this.list.inverseGrep(condition);
/*     */   }
/*  93 */   public double max() { return this.list.max(); }
/*  94 */   public double min() { return this.list.min(); } public double sum() {
/*  95 */     return this.list.sum();
/*     */   }
/*     */   public TDoubleList subList(int fromIndex, int toIndex) {
/*  98 */     return new TUnmodifiableDoubleList(this.list.subList(fromIndex, toIndex));
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
/* 140 */     return (this.list instanceof java.util.RandomAccess) ? new TUnmodifiableRandomAccessDoubleList(this.list) : this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(double[] vals) {
/* 145 */     throw new UnsupportedOperationException(); } public void add(double[] vals, int offset, int length) {
/* 146 */     throw new UnsupportedOperationException();
/*     */   }
/* 148 */   public double removeAt(int offset) { throw new UnsupportedOperationException(); } public void remove(int offset, int length) {
/* 149 */     throw new UnsupportedOperationException();
/*     */   }
/* 151 */   public void insert(int offset, double value) { throw new UnsupportedOperationException(); }
/* 152 */   public void insert(int offset, double[] values) { throw new UnsupportedOperationException(); } public void insert(int offset, double[] values, int valOffset, int len) {
/* 153 */     throw new UnsupportedOperationException();
/*     */   }
/* 155 */   public double set(int offset, double val) { throw new UnsupportedOperationException(); }
/* 156 */   public void set(int offset, double[] values) { throw new UnsupportedOperationException(); } public void set(int offset, double[] values, int valOffset, int length) {
/* 157 */     throw new UnsupportedOperationException();
/*     */   } public double replace(int offset, double val) {
/* 159 */     throw new UnsupportedOperationException();
/*     */   } public void transformValues(TDoubleFunction function) {
/* 161 */     throw new UnsupportedOperationException();
/*     */   }
/* 163 */   public void reverse() { throw new UnsupportedOperationException(); }
/* 164 */   public void reverse(int from, int to) { throw new UnsupportedOperationException(); } public void shuffle(Random rand) {
/* 165 */     throw new UnsupportedOperationException();
/*     */   }
/* 167 */   public void sort() { throw new UnsupportedOperationException(); }
/* 168 */   public void sort(int fromIndex, int toIndex) { throw new UnsupportedOperationException(); }
/* 169 */   public void fill(double val) { throw new UnsupportedOperationException(); } public void fill(int fromIndex, int toIndex, double val) {
/* 170 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\imp\\unmodifiable\TUnmodifiableDoubleList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */