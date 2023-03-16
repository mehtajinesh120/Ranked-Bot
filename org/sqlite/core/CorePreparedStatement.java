/*     */ package org.sqlite.core;
/*     */ 
/*     */ import java.sql.Date;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Calendar;
/*     */ import org.sqlite.SQLiteConfig;
/*     */ import org.sqlite.SQLiteConnection;
/*     */ import org.sqlite.SQLiteConnectionConfig;
/*     */ import org.sqlite.date.FastDateFormat;
/*     */ import org.sqlite.jdbc3.JDBC3Connection;
/*     */ import org.sqlite.jdbc4.JDBC4Statement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CorePreparedStatement
/*     */   extends JDBC4Statement
/*     */ {
/*     */   protected int columnCount;
/*     */   protected int paramCount;
/*     */   protected int batchQueryCount;
/*     */   
/*     */   protected CorePreparedStatement(SQLiteConnection conn, String sql) throws SQLException {
/*  42 */     super(conn);
/*     */     
/*  44 */     this.sql = sql;
/*  45 */     DB db = conn.getDatabase();
/*  46 */     db.prepare((CoreStatement)this);
/*  47 */     this.rs.colsMeta = this.pointer.<String[], Throwable>safeRun(DB::column_names);
/*  48 */     this.columnCount = this.pointer.safeRunInt(DB::column_count);
/*  49 */     this.paramCount = this.pointer.safeRunInt(DB::bind_parameter_count);
/*  50 */     this.batchQueryCount = 0;
/*  51 */     this.batch = null;
/*  52 */     this.batchPos = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] executeBatch() throws SQLException {
/*  58 */     return Arrays.stream(executeLargeBatch()).mapToInt(l -> (int)l).toArray();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long[] executeLargeBatch() throws SQLException {
/*  64 */     if (this.batchQueryCount == 0) {
/*  65 */       return new long[0];
/*     */     }
/*     */     
/*  68 */     if (this.conn instanceof JDBC3Connection) {
/*  69 */       ((JDBC3Connection)this.conn).tryEnforceTransactionMode();
/*     */     }
/*     */     
/*  72 */     return (long[])withConnectionTimeout(() -> {
/*     */           try {
/*     */             return this.conn.getDatabase().executeBatch(this.pointer, this.batchQueryCount, this.batch, this.conn.getAutoCommit());
/*     */           } finally {
/*     */             clearBatch();
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearBatch() throws SQLException {
/*  87 */     super.clearBatch();
/*  88 */     this.batchQueryCount = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUpdateCount() throws SQLException {
/*  94 */     return (int)getLargeUpdateCount();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLargeUpdateCount() throws SQLException {
/* 100 */     if (this.pointer.isClosed() || this.resultsWaiting || this.rs.isOpen()) {
/* 101 */       return -1L;
/*     */     }
/*     */     
/* 104 */     return this.conn.getDatabase().changes();
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
/*     */   protected void batch(int pos, Object value) throws SQLException {
/* 117 */     checkOpen();
/* 118 */     if (this.batch == null) {
/* 119 */       this.batch = new Object[this.paramCount];
/*     */     }
/* 121 */     this.batch[this.batchPos + pos - 1] = value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setDateByMilliseconds(int pos, Long value, Calendar calendar) throws SQLException {
/* 127 */     SQLiteConnectionConfig config = this.conn.getConnectionConfig();
/* 128 */     switch (config.getDateClass()) {
/*     */       case TEXT:
/* 130 */         batch(pos, 
/*     */             
/* 132 */             FastDateFormat.getInstance(config
/* 133 */               .getDateStringFormat(), calendar.getTimeZone())
/* 134 */             .format(new Date(value.longValue())));
/*     */         return;
/*     */ 
/*     */       
/*     */       case REAL:
/* 139 */         batch(pos, new Double(value.longValue() / 8.64E7D + 2440587.5D));
/*     */         return;
/*     */     } 
/*     */     
/* 143 */     batch(pos, new Long(value.longValue() / config.getDateMultiplier()));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\core\CorePreparedStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */