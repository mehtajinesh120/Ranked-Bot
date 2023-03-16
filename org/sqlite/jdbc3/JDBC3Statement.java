/*     */ package org.sqlite.jdbc3;
/*     */ 
/*     */ import java.sql.BatchUpdateException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.sql.SQLWarning;
/*     */ import java.util.Arrays;
/*     */ import org.sqlite.ExtendedCommand;
/*     */ import org.sqlite.SQLiteConnection;
/*     */ import org.sqlite.core.CoreStatement;
/*     */ import org.sqlite.core.DB;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JDBC3Statement
/*     */   extends CoreStatement
/*     */ {
/*     */   private int queryTimeout;
/*     */   
/*     */   protected JDBC3Statement(SQLiteConnection conn) {
/*  24 */     super(conn);
/*  25 */     this.queryTimeout = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws SQLException {
/*  30 */     internalClose();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean execute(String sql) throws SQLException {
/*  35 */     internalClose();
/*     */     
/*  37 */     return ((Boolean)withConnectionTimeout(() -> { ExtendedCommand.SQLExtension ext = ExtendedCommand.parse(sql); if (ext != null) { ext.execute(this.conn.getDatabase()); return Boolean.valueOf(false); }  this.sql = sql; this.conn.getDatabase().prepare(this); return Boolean.valueOf(exec()); })).booleanValue();
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
/*     */   public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
/*  55 */     return execute(sql);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResultSet executeQuery(String sql, boolean closeStmt) throws SQLException {
/*  63 */     this.rs.closeStmt = closeStmt;
/*     */     
/*  65 */     return executeQuery(sql);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet executeQuery(String sql) throws SQLException {
/*  70 */     internalClose();
/*  71 */     this.sql = sql;
/*     */     
/*  73 */     return withConnectionTimeout(() -> {
/*     */           this.conn.getDatabase().prepare(this);
/*     */           if (!exec()) {
/*     */             internalClose();
/*     */             throw new SQLException("query does not return ResultSet", "SQLITE_DONE", 101);
/*     */           } 
/*     */           return getResultSet();
/*     */         });
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   protected static interface SQLCallable<T> {
/*     */     T call() throws SQLException; }
/*     */   
/*     */   static class BackupObserver implements DB.ProgressObserver {
/*     */     public void progress(int remaining, int pageCount) {
/*  89 */       System.out.printf("remaining:%d, page count:%d%n", new Object[] { Integer.valueOf(remaining), Integer.valueOf(pageCount) });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int executeUpdate(String sql) throws SQLException {
/*  95 */     return (int)executeLargeUpdate(sql);
/*     */   }
/*     */ 
/*     */   
/*     */   public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
/* 100 */     return executeUpdate(sql);
/*     */   }
/*     */ 
/*     */   
/*     */   public long executeLargeUpdate(String sql) throws SQLException {
/* 105 */     internalClose();
/* 106 */     this.sql = sql;
/*     */     
/* 108 */     return ((Long)withConnectionTimeout(() -> { DB db = this.conn.getDatabase(); long changes = 0L; ExtendedCommand.SQLExtension ext = ExtendedCommand.parse(sql); if (ext != null) { ext.execute(db); } else { try { changes = db.total_changes(); int statusCode = db._exec(sql); if (statusCode != 0) throw DB.newSQLException(statusCode, "");  changes = db.total_changes() - changes; } finally { internalClose(); }  }  return Long.valueOf(changes); })).longValue();
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
/*     */   public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
/* 135 */     return executeLargeUpdate(sql);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getResultSet() throws SQLException {
/* 140 */     checkOpen();
/*     */     
/* 142 */     if (this.rs.isOpen()) {
/* 143 */       throw new SQLException("ResultSet already requested");
/*     */     }
/*     */     
/* 146 */     if (this.pointer.safeRunInt(DB::column_count) == 0) {
/* 147 */       return null;
/*     */     }
/*     */     
/* 150 */     if (this.rs.colsMeta == null) {
/* 151 */       this.rs.colsMeta = (String[])this.pointer.safeRun(DB::column_names);
/*     */     }
/*     */     
/* 154 */     this.rs.cols = this.rs.colsMeta;
/* 155 */     this.rs.emptyResultSet = !this.resultsWaiting;
/* 156 */     this.rs.open = true;
/* 157 */     this.resultsWaiting = false;
/*     */     
/* 159 */     return (ResultSet)this.rs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUpdateCount() throws SQLException {
/* 169 */     return (int)getLargeUpdateCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLargeUpdateCount() throws SQLException {
/* 179 */     DB db = this.conn.getDatabase();
/* 180 */     if (!this.pointer.isClosed() && 
/* 181 */       !this.rs.isOpen() && !this.resultsWaiting && this.pointer
/*     */       
/* 183 */       .safeRunInt(DB::column_count) == 0) return db.changes(); 
/* 184 */     return -1L;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addBatch(String sql) throws SQLException {
/* 189 */     internalClose();
/* 190 */     if (this.batch == null || this.batchPos + 1 >= this.batch.length) {
/* 191 */       Object[] nb = new Object[Math.max(10, this.batchPos * 2)];
/* 192 */       if (this.batch != null) System.arraycopy(this.batch, 0, nb, 0, this.batch.length); 
/* 193 */       this.batch = nb;
/*     */     } 
/* 195 */     this.batch[this.batchPos++] = sql;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearBatch() throws SQLException {
/* 200 */     this.batchPos = 0;
/* 201 */     if (this.batch != null) for (int i = 0; i < this.batch.length; ) { this.batch[i] = null; i++; }
/*     */        
/*     */   }
/*     */   
/*     */   public int[] executeBatch() throws SQLException {
/* 206 */     return Arrays.stream(executeLargeBatch()).mapToInt(l -> (int)l).toArray();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long[] executeLargeBatch() throws SQLException {
/* 212 */     internalClose();
/* 213 */     if (this.batch == null || this.batchPos == 0) return new long[0];
/*     */     
/* 215 */     long[] changes = new long[this.batchPos];
/* 216 */     DB db = this.conn.getDatabase();
/* 217 */     synchronized (db) {
/*     */       try {
/* 219 */         for (int i = 0; i < changes.length; i++) {
/*     */           
/* 221 */           try { this.sql = (String)this.batch[i];
/* 222 */             db.prepare(this);
/* 223 */             changes[i] = db.executeUpdate(this, null);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 228 */             if (this.pointer != null) this.pointer.close();  } catch (SQLException e) { throw new BatchUpdateException("batch entry " + i + ": " + e.getMessage(), null, 0, changes, e); } finally { if (this.pointer != null) this.pointer.close();  }
/*     */         
/*     */         } 
/*     */       } finally {
/* 232 */         clearBatch();
/*     */       } 
/*     */     } 
/*     */     
/* 236 */     return changes;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCursorName(String name) {}
/*     */ 
/*     */   
/*     */   public SQLWarning getWarnings() throws SQLException {
/* 244 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearWarnings() throws SQLException {}
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/* 252 */     return (Connection)this.conn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() throws SQLException {
/* 257 */     this.conn.getDatabase().interrupt();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getQueryTimeout() throws SQLException {
/* 262 */     return this.queryTimeout;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setQueryTimeout(int seconds) throws SQLException {
/* 267 */     if (seconds < 0) {
/* 268 */       throw new SQLException("query timeout must be >= 0");
/*     */     }
/* 270 */     this.queryTimeout = seconds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxRows() throws SQLException {
/* 277 */     return (int)this.rs.maxRows;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLargeMaxRows() throws SQLException {
/* 283 */     return this.rs.maxRows;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxRows(int max) throws SQLException {
/* 288 */     setLargeMaxRows(max);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLargeMaxRows(long max) throws SQLException {
/* 294 */     if (max < 0L) throw new SQLException("max row count must be >= 0"); 
/* 295 */     this.rs.maxRows = max;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxFieldSize() throws SQLException {
/* 300 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxFieldSize(int max) throws SQLException {
/* 305 */     if (max < 0) throw new SQLException("max field size " + max + " cannot be negative");
/*     */   
/*     */   }
/*     */   
/*     */   public int getFetchSize() throws SQLException {
/* 310 */     return ((ResultSet)this.rs).getFetchSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFetchSize(int r) throws SQLException {
/* 315 */     ((ResultSet)this.rs).setFetchSize(r);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFetchDirection() throws SQLException {
/* 320 */     return 1000;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFetchDirection(int direction) throws SQLException {
/* 325 */     switch (direction) {
/*     */       case 1000:
/*     */       case 1001:
/*     */       case 1002:
/*     */         return;
/*     */     } 
/*     */     
/* 332 */     throw new SQLException("Unknown fetch direction " + direction + ". Must be one of FETCH_FORWARD, FETCH_REVERSE, or FETCH_UNKNOWN in java.sql.ResultSet");
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
/*     */   public ResultSet getGeneratedKeys() throws SQLException {
/* 347 */     return this.conn.getSQLiteDatabaseMetaData().getGeneratedKeys();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getMoreResults() throws SQLException {
/* 356 */     return getMoreResults(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getMoreResults(int c) throws SQLException {
/* 361 */     checkOpen();
/* 362 */     internalClose();
/* 363 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getResultSetConcurrency() throws SQLException {
/* 368 */     return 1007;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getResultSetHoldability() throws SQLException {
/* 373 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getResultSetType() throws SQLException {
/* 378 */     return 1003;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEscapeProcessing(boolean enable) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SQLException unsupported() {
/* 393 */     return new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute(String sql, int[] colinds) throws SQLException {
/* 399 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public boolean execute(String sql, String[] colnames) throws SQLException {
/* 403 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public int executeUpdate(String sql, int[] colinds) throws SQLException {
/* 407 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public int executeUpdate(String sql, String[] cols) throws SQLException {
/* 411 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public long executeLargeUpdate(String sql, int[] colinds) throws SQLException {
/* 415 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public long executeLargeUpdate(String sql, String[] cols) throws SQLException {
/* 419 */     throw unsupported();
/*     */   }
/*     */   
/*     */   protected <T> T withConnectionTimeout(SQLCallable<T> callable) throws SQLException {
/* 423 */     int origBusyTimeout = this.conn.getBusyTimeout();
/* 424 */     if (this.queryTimeout > 0)
/*     */     {
/* 426 */       this.conn.setBusyTimeout(1000 * this.queryTimeout);
/*     */     }
/*     */     try {
/* 429 */       return callable.call();
/*     */     } finally {
/* 431 */       if (this.queryTimeout > 0)
/*     */       {
/* 433 */         this.conn.setBusyTimeout(origBusyTimeout);
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\jdbc3\JDBC3Statement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */