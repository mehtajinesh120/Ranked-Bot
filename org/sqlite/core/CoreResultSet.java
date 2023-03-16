/*     */ package org.sqlite.core;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.sqlite.SQLiteConnectionConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CoreResultSet
/*     */   implements Codes
/*     */ {
/*     */   protected final CoreStatement stmt;
/*     */   public boolean emptyResultSet = false;
/*     */   public boolean open = false;
/*     */   public long maxRows;
/*  35 */   public String[] cols = null;
/*     */   
/*  37 */   public String[] colsMeta = null;
/*     */   
/*  39 */   protected boolean[][] meta = (boolean[][])null;
/*     */ 
/*     */   
/*     */   protected int limitRows;
/*     */   
/*  44 */   protected int row = 0;
/*     */   
/*     */   protected int lastCol;
/*     */   
/*     */   public boolean closeStmt;
/*  49 */   protected Map<String, Integer> columnNameToIndex = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CoreResultSet(CoreStatement stmt) {
/*  57 */     this.stmt = stmt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected DB getDatabase() {
/*  63 */     return this.stmt.getDatabase();
/*     */   }
/*     */   
/*     */   protected SQLiteConnectionConfig getConnectionConfig() {
/*  67 */     return this.stmt.getConnectionConfig();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/*  76 */     return this.open;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkOpen() throws SQLException {
/*  81 */     if (!this.open) {
/*  82 */       throw new SQLException("ResultSet closed");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int checkCol(int col) throws SQLException {
/*  94 */     if (this.colsMeta == null) {
/*  95 */       throw new IllegalStateException("SQLite JDBC: inconsistent internal state");
/*     */     }
/*  97 */     if (col < 1 || col > this.colsMeta.length) {
/*  98 */       throw new SQLException("column " + col + " out of bounds [1," + this.colsMeta.length + "]");
/*     */     }
/* 100 */     return --col;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int markCol(int col) throws SQLException {
/* 111 */     checkOpen();
/* 112 */     checkCol(col);
/* 113 */     this.lastCol = col;
/* 114 */     return --col;
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkMeta() throws SQLException {
/* 119 */     checkCol(1);
/* 120 */     if (this.meta == null) {
/* 121 */       this.meta = this.stmt.pointer.<boolean[][], Throwable>safeRun(DB::column_metadata);
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() throws SQLException {
/* 126 */     this.cols = null;
/* 127 */     this.colsMeta = null;
/* 128 */     this.meta = (boolean[][])null;
/* 129 */     this.limitRows = 0;
/* 130 */     this.row = 0;
/* 131 */     this.lastCol = -1;
/* 132 */     this.columnNameToIndex = null;
/* 133 */     this.emptyResultSet = false;
/*     */     
/* 135 */     if (this.stmt.pointer.isClosed() || (!this.open && !this.closeStmt)) {
/*     */       return;
/*     */     }
/*     */     
/* 139 */     DB db = this.stmt.getDatabase();
/* 140 */     synchronized (db) {
/* 141 */       if (!this.stmt.pointer.isClosed()) {
/* 142 */         this.stmt.pointer.safeRunInt(DB::reset);
/*     */         
/* 144 */         if (this.closeStmt) {
/* 145 */           this.closeStmt = false;
/* 146 */           ((Statement)this.stmt).close();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 151 */     this.open = false;
/*     */   }
/*     */   
/*     */   protected Integer findColumnIndexInCache(String col) {
/* 155 */     if (this.columnNameToIndex == null) {
/* 156 */       return null;
/*     */     }
/* 158 */     return this.columnNameToIndex.get(col);
/*     */   }
/*     */   
/*     */   protected int addColumnIndexInCache(String col, int index) {
/* 162 */     if (this.columnNameToIndex == null) {
/* 163 */       this.columnNameToIndex = new HashMap<>(this.cols.length);
/*     */     }
/* 165 */     this.columnNameToIndex.put(col, Integer.valueOf(index));
/* 166 */     return index;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\core\CoreResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */