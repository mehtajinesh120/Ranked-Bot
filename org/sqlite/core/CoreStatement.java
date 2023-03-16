/*     */ package org.sqlite.core;
/*     */ 
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import org.sqlite.SQLiteConnection;
/*     */ import org.sqlite.SQLiteConnectionConfig;
/*     */ import org.sqlite.jdbc3.JDBC3Connection;
/*     */ import org.sqlite.jdbc4.JDBC4ResultSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CoreStatement
/*     */   implements Codes
/*     */ {
/*     */   public final SQLiteConnection conn;
/*     */   protected final CoreResultSet rs;
/*     */   public SafeStmtPtr pointer;
/*  30 */   protected String sql = null;
/*     */   
/*     */   protected int batchPos;
/*  33 */   protected Object[] batch = null;
/*     */   protected boolean resultsWaiting = false;
/*     */   
/*     */   protected CoreStatement(SQLiteConnection c) {
/*  37 */     this.conn = c;
/*  38 */     this.rs = (CoreResultSet)new JDBC4ResultSet(this);
/*     */   }
/*     */   
/*     */   public DB getDatabase() {
/*  42 */     return this.conn.getDatabase();
/*     */   }
/*     */   
/*     */   public SQLiteConnectionConfig getConnectionConfig() {
/*  46 */     return this.conn.getConnectionConfig();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void checkOpen() throws SQLException {
/*  51 */     if (this.pointer.isClosed()) throw new SQLException("statement is not executing");
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isOpen() throws SQLException {
/*  59 */     return !this.pointer.isClosed();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean exec() throws SQLException {
/*  69 */     if (this.sql == null) throw new SQLException("SQLiteJDBC internal error: sql==null"); 
/*  70 */     if (this.rs.isOpen()) throw new SQLException("SQLite JDBC internal error: rs.isOpen() on exec.");
/*     */     
/*  72 */     if (this.conn instanceof JDBC3Connection) {
/*  73 */       ((JDBC3Connection)this.conn).tryEnforceTransactionMode();
/*     */     }
/*     */     
/*  76 */     boolean success = false;
/*  77 */     boolean rc = false;
/*     */     try {
/*  79 */       rc = this.conn.getDatabase().execute(this, (Object[])null);
/*  80 */       success = true;
/*     */     } finally {
/*  82 */       notifyFirstStatementExecuted();
/*  83 */       this.resultsWaiting = rc;
/*  84 */       if (!success) {
/*  85 */         this.pointer.close();
/*     */       }
/*     */     } 
/*     */     
/*  89 */     return (this.pointer.safeRunInt(DB::column_count) != 0);
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
/*     */   protected boolean exec(String sql) throws SQLException {
/* 101 */     if (sql == null) throw new SQLException("SQLiteJDBC internal error: sql==null"); 
/* 102 */     if (this.rs.isOpen()) throw new SQLException("SQLite JDBC internal error: rs.isOpen() on exec.");
/*     */     
/* 104 */     if (this.conn instanceof JDBC3Connection) {
/* 105 */       ((JDBC3Connection)this.conn).tryEnforceTransactionMode();
/*     */     }
/*     */     
/* 108 */     boolean rc = false;
/* 109 */     boolean success = false;
/*     */     try {
/* 111 */       rc = this.conn.getDatabase().execute(sql, this.conn.getAutoCommit());
/* 112 */       success = true;
/*     */     } finally {
/* 114 */       notifyFirstStatementExecuted();
/* 115 */       this.resultsWaiting = rc;
/* 116 */       if (!success && this.pointer != null) {
/* 117 */         this.pointer.close();
/*     */       }
/*     */     } 
/*     */     
/* 121 */     return (this.pointer.safeRunInt(DB::column_count) != 0);
/*     */   }
/*     */   
/*     */   protected void internalClose() throws SQLException {
/* 125 */     if (this.pointer != null && !this.pointer.isClosed()) {
/* 126 */       if (this.conn.isClosed()) throw DB.newSQLException(1, "Connection is closed");
/*     */       
/* 128 */       this.rs.close();
/*     */       
/* 130 */       this.batch = null;
/* 131 */       this.batchPos = 0;
/* 132 */       int resp = this.pointer.close();
/*     */       
/* 134 */       if (resp != 0 && resp != 21) this.conn.getDatabase().throwex(resp); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void notifyFirstStatementExecuted() {
/* 139 */     this.conn.setFirstStatementExecuted(true);
/*     */   }
/*     */   
/*     */   public abstract ResultSet executeQuery(String paramString, boolean paramBoolean) throws SQLException;
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\core\CoreStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */