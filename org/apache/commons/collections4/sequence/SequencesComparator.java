/*     */ package org.apache.commons.collections4.sequence;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.apache.commons.collections4.Equator;
/*     */ import org.apache.commons.collections4.functors.DefaultEquator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SequencesComparator<T>
/*     */ {
/*     */   private final List<T> sequence1;
/*     */   private final List<T> sequence2;
/*     */   private final Equator<? super T> equator;
/*     */   private final int[] vDown;
/*     */   private final int[] vUp;
/*     */   
/*     */   public SequencesComparator(List<T> sequence1, List<T> sequence2) {
/*  89 */     this(sequence1, sequence2, (Equator<? super T>)DefaultEquator.defaultEquator());
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
/*     */   public SequencesComparator(List<T> sequence1, List<T> sequence2, Equator<? super T> equator) {
/* 106 */     this.sequence1 = sequence1;
/* 107 */     this.sequence2 = sequence2;
/* 108 */     this.equator = equator;
/*     */     
/* 110 */     int size = sequence1.size() + sequence2.size() + 2;
/* 111 */     this.vDown = new int[size];
/* 112 */     this.vUp = new int[size];
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
/*     */   public EditScript<T> getScript() {
/* 129 */     EditScript<T> script = new EditScript<T>();
/* 130 */     buildScript(0, this.sequence1.size(), 0, this.sequence2.size(), script);
/* 131 */     return script;
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
/*     */   private Snake buildSnake(int start, int diag, int end1, int end2) {
/* 144 */     int end = start;
/* 145 */     while (end - diag < end2 && end < end1 && this.equator.equate(this.sequence1.get(end), this.sequence2.get(end - diag)))
/*     */     {
/*     */       
/* 148 */       end++;
/*     */     }
/* 150 */     return new Snake(start, end, diag);
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
/*     */   private Snake getMiddleSnake(int start1, int end1, int start2, int end2) {
/* 172 */     int m = end1 - start1;
/* 173 */     int n = end2 - start2;
/* 174 */     if (m == 0 || n == 0) {
/* 175 */       return null;
/*     */     }
/*     */     
/* 178 */     int delta = m - n;
/* 179 */     int sum = n + m;
/* 180 */     int offset = ((sum % 2 == 0) ? sum : (sum + 1)) / 2;
/* 181 */     this.vDown[1 + offset] = start1;
/* 182 */     this.vUp[1 + offset] = end1 + 1;
/*     */     
/* 184 */     for (int d = 0; d <= offset; d++) {
/*     */       int k;
/* 186 */       for (k = -d; k <= d; k += 2) {
/*     */ 
/*     */         
/* 189 */         int i = k + offset;
/* 190 */         if (k == -d || (k != d && this.vDown[i - 1] < this.vDown[i + 1])) {
/* 191 */           this.vDown[i] = this.vDown[i + 1];
/*     */         } else {
/* 193 */           this.vDown[i] = this.vDown[i - 1] + 1;
/*     */         } 
/*     */         
/* 196 */         int x = this.vDown[i];
/* 197 */         int y = x - start1 + start2 - k;
/*     */         
/* 199 */         while (x < end1 && y < end2 && this.equator.equate(this.sequence1.get(x), this.sequence2.get(y))) {
/* 200 */           this.vDown[i] = ++x;
/* 201 */           y++;
/*     */         } 
/*     */         
/* 204 */         if (delta % 2 != 0 && delta - d <= k && k <= delta + d && 
/* 205 */           this.vUp[i - delta] <= this.vDown[i]) {
/* 206 */           return buildSnake(this.vUp[i - delta], k + start1 - start2, end1, end2);
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 212 */       for (k = delta - d; k <= delta + d; k += 2) {
/*     */         
/* 214 */         int i = k + offset - delta;
/* 215 */         if (k == delta - d || (k != delta + d && this.vUp[i + 1] <= this.vUp[i - 1])) {
/*     */           
/* 217 */           this.vUp[i] = this.vUp[i + 1] - 1;
/*     */         } else {
/* 219 */           this.vUp[i] = this.vUp[i - 1];
/*     */         } 
/*     */         
/* 222 */         int x = this.vUp[i] - 1;
/* 223 */         int y = x - start1 + start2 - k;
/* 224 */         while (x >= start1 && y >= start2 && this.equator.equate(this.sequence1.get(x), this.sequence2.get(y))) {
/*     */           
/* 226 */           this.vUp[i] = x--;
/* 227 */           y--;
/*     */         } 
/*     */         
/* 230 */         if (delta % 2 == 0 && -d <= k && k <= d && 
/* 231 */           this.vUp[i] <= this.vDown[i + delta]) {
/* 232 */           return buildSnake(this.vUp[i], k + start1 - start2, end1, end2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 239 */     throw new RuntimeException("Internal Error");
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
/*     */   private void buildScript(int start1, int end1, int start2, int end2, EditScript<T> script) {
/* 255 */     Snake middle = getMiddleSnake(start1, end1, start2, end2);
/*     */     
/* 257 */     if (middle == null || (middle.getStart() == end1 && middle.getDiag() == end1 - end2) || (middle.getEnd() == start1 && middle.getDiag() == start1 - start2)) {
/*     */ 
/*     */ 
/*     */       
/* 261 */       int i = start1;
/* 262 */       int j = start2;
/* 263 */       while (i < end1 || j < end2) {
/* 264 */         if (i < end1 && j < end2 && this.equator.equate(this.sequence1.get(i), this.sequence2.get(j))) {
/* 265 */           script.append(new KeepCommand<T>(this.sequence1.get(i)));
/* 266 */           i++;
/* 267 */           j++; continue;
/*     */         } 
/* 269 */         if (end1 - start1 > end2 - start2) {
/* 270 */           script.append(new DeleteCommand<T>(this.sequence1.get(i)));
/* 271 */           i++; continue;
/*     */         } 
/* 273 */         script.append(new InsertCommand<T>(this.sequence2.get(j)));
/* 274 */         j++;
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 281 */       buildScript(start1, middle.getStart(), start2, middle.getStart() - middle.getDiag(), script);
/*     */ 
/*     */       
/* 284 */       for (int i = middle.getStart(); i < middle.getEnd(); i++) {
/* 285 */         script.append(new KeepCommand<T>(this.sequence1.get(i)));
/*     */       }
/* 287 */       buildScript(middle.getEnd(), end1, middle.getEnd() - middle.getDiag(), end2, script);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Snake
/*     */   {
/*     */     private final int start;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int end;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int diag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Snake(int start, int end, int diag) {
/* 316 */       this.start = start;
/* 317 */       this.end = end;
/* 318 */       this.diag = diag;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getStart() {
/* 327 */       return this.start;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getEnd() {
/* 336 */       return this.end;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getDiag() {
/* 345 */       return this.diag;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\sequence\SequencesComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */