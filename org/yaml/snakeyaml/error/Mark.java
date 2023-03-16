/*     */ package org.yaml.snakeyaml.error;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.yaml.snakeyaml.scanner.Constant;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Mark
/*     */   implements Serializable
/*     */ {
/*     */   private final String name;
/*     */   private final int index;
/*     */   private final int line;
/*     */   private final int column;
/*     */   private final int[] buffer;
/*     */   private final int pointer;
/*     */   
/*     */   private static int[] toCodePoints(char[] str) {
/*  33 */     int[] codePoints = new int[Character.codePointCount(str, 0, str.length)];
/*  34 */     for (int i = 0, c = 0; i < str.length; c++) {
/*  35 */       int cp = Character.codePointAt(str, i);
/*  36 */       codePoints[c] = cp;
/*  37 */       i += Character.charCount(cp);
/*     */     } 
/*  39 */     return codePoints;
/*     */   }
/*     */   
/*     */   public Mark(String name, int index, int line, int column, char[] str, int pointer) {
/*  43 */     this(name, index, line, column, toCodePoints(str), pointer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Mark(String name, int index, int line, int column, String buffer, int pointer) {
/*  54 */     this(name, index, line, column, buffer.toCharArray(), pointer);
/*     */   }
/*     */ 
/*     */   
/*     */   public Mark(String name, int index, int line, int column, int[] buffer, int pointer) {
/*  59 */     this.name = name;
/*  60 */     this.index = index;
/*  61 */     this.line = line;
/*  62 */     this.column = column;
/*  63 */     this.buffer = buffer;
/*  64 */     this.pointer = pointer;
/*     */   }
/*     */   
/*     */   private boolean isLineBreak(int c) {
/*  68 */     return Constant.NULL_OR_LINEBR.has(c);
/*     */   }
/*     */   
/*     */   public String get_snippet(int indent, int max_length) {
/*  72 */     float half = max_length / 2.0F - 1.0F;
/*  73 */     int start = this.pointer;
/*  74 */     String head = "";
/*  75 */     while (start > 0 && !isLineBreak(this.buffer[start - 1])) {
/*  76 */       start--;
/*  77 */       if ((this.pointer - start) > half) {
/*  78 */         head = " ... ";
/*  79 */         start += 5;
/*     */         break;
/*     */       } 
/*     */     } 
/*  83 */     String tail = "";
/*  84 */     int end = this.pointer;
/*  85 */     while (end < this.buffer.length && !isLineBreak(this.buffer[end])) {
/*  86 */       end++;
/*  87 */       if ((end - this.pointer) > half) {
/*  88 */         tail = " ... ";
/*  89 */         end -= 5;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  94 */     StringBuilder result = new StringBuilder(); int i;
/*  95 */     for (i = 0; i < indent; i++) {
/*  96 */       result.append(" ");
/*     */     }
/*  98 */     result.append(head);
/*  99 */     for (i = start; i < end; i++) {
/* 100 */       result.appendCodePoint(this.buffer[i]);
/*     */     }
/* 102 */     result.append(tail);
/* 103 */     result.append("\n");
/* 104 */     for (i = 0; i < indent + this.pointer - start + head.length(); i++) {
/* 105 */       result.append(" ");
/*     */     }
/* 107 */     result.append("^");
/* 108 */     return result.toString();
/*     */   }
/*     */   
/*     */   public String get_snippet() {
/* 112 */     return get_snippet(4, 75);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 117 */     String snippet = get_snippet();
/* 118 */     String builder = " in " + this.name + ", line " + (this.line + 1) + ", column " + (this.column + 1) + ":\n" + snippet;
/*     */     
/* 120 */     return builder;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 124 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLine() {
/* 133 */     return this.line;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumn() {
/* 142 */     return this.column;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIndex() {
/* 151 */     return this.index;
/*     */   }
/*     */   
/*     */   public int[] getBuffer() {
/* 155 */     return this.buffer;
/*     */   }
/*     */   
/*     */   public int getPointer() {
/* 159 */     return this.pointer;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\error\Mark.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */