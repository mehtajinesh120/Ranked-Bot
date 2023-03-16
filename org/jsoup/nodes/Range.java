/*     */ package org.jsoup.nodes;
/*     */ 
/*     */ import org.jsoup.helper.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Range
/*     */ {
/*     */   private final Position start;
/*     */   private final Position end;
/*  15 */   private static final String RangeKey = Attributes.internalKey("jsoup.sourceRange");
/*  16 */   private static final String EndRangeKey = Attributes.internalKey("jsoup.endSourceRange");
/*  17 */   private static final Position UntrackedPos = new Position(-1, -1, -1);
/*  18 */   private static final Range Untracked = new Range(UntrackedPos, UntrackedPos);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Range(Position start, Position end) {
/*  26 */     this.start = start;
/*  27 */     this.end = end;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Position start() {
/*  35 */     return this.start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Position end() {
/*  43 */     return this.end;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTracked() {
/*  51 */     return (this != Untracked);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Range of(Node node, boolean start) {
/*  61 */     String key = start ? RangeKey : EndRangeKey;
/*  62 */     if (!node.hasAttr(key)) {
/*  63 */       return Untracked;
/*     */     }
/*  65 */     return (Range)Validate.ensureNotNull(node.attributes().getUserData(key));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void track(Node node, boolean start) {
/*  74 */     node.attributes().putUserData(start ? RangeKey : EndRangeKey, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  79 */     if (this == o) return true; 
/*  80 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/*  82 */     Range range = (Range)o;
/*     */     
/*  84 */     if (!this.start.equals(range.start)) return false; 
/*  85 */     return this.end.equals(range.end);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  90 */     int result = this.start.hashCode();
/*  91 */     result = 31 * result + this.end.hashCode();
/*  92 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 101 */     return this.start + "-" + this.end;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Position
/*     */   {
/*     */     private final int pos;
/*     */ 
/*     */     
/*     */     private final int lineNumber;
/*     */ 
/*     */     
/*     */     private final int columnNumber;
/*     */ 
/*     */ 
/*     */     
/*     */     public Position(int pos, int lineNumber, int columnNumber) {
/* 120 */       this.pos = pos;
/* 121 */       this.lineNumber = lineNumber;
/* 122 */       this.columnNumber = columnNumber;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int pos() {
/* 131 */       return this.pos;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int lineNumber() {
/* 139 */       return this.lineNumber;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int columnNumber() {
/* 148 */       return this.columnNumber;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isTracked() {
/* 156 */       return (this != Range.UntrackedPos);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 165 */       return this.lineNumber + "," + this.columnNumber + ":" + this.pos;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 170 */       if (this == o) return true; 
/* 171 */       if (o == null || getClass() != o.getClass()) return false; 
/* 172 */       Position position = (Position)o;
/* 173 */       if (this.pos != position.pos) return false; 
/* 174 */       if (this.lineNumber != position.lineNumber) return false; 
/* 175 */       return (this.columnNumber == position.columnNumber);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 180 */       int result = this.pos;
/* 181 */       result = 31 * result + this.lineNumber;
/* 182 */       result = 31 * result + this.columnNumber;
/* 183 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\nodes\Range.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */