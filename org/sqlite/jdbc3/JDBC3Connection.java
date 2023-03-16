/*     */ package org.sqlite.jdbc3;
/*     */ 
/*     */ import java.sql.CallableStatement;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.sql.SQLWarning;
/*     */ import java.sql.Savepoint;
/*     */ import java.sql.Statement;
/*     */ import java.sql.Struct;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.sqlite.SQLiteConfig;
/*     */ import org.sqlite.SQLiteConnection;
/*     */ import org.sqlite.SQLiteOpenMode;
/*     */ 
/*     */ public abstract class JDBC3Connection
/*     */   extends SQLiteConnection
/*     */ {
/*  22 */   private final AtomicInteger savePoint = new AtomicInteger(0);
/*     */   
/*     */   private Map<String, Class<?>> typeMap;
/*     */   private boolean readOnly = false;
/*     */   
/*     */   protected JDBC3Connection(String url, String fileName, Properties prop) throws SQLException {
/*  28 */     super(url, fileName, prop);
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
/*     */   public void tryEnforceTransactionMode() throws SQLException {
/*  51 */     if (getDatabase().getConfig().isExplicitReadOnly() && 
/*  52 */       !getAutoCommit() && 
/*  53 */       getCurrentTransactionMode() != null) {
/*  54 */       if (isReadOnly()) {
/*     */ 
/*     */ 
/*     */         
/*  58 */         getDatabase()._exec("PRAGMA query_only = true;");
/*     */       }
/*  60 */       else if (getCurrentTransactionMode() == SQLiteConfig.TransactionMode.DEFERRED || 
/*  61 */         getCurrentTransactionMode() == SQLiteConfig.TransactionMode.DEFFERED) {
/*  62 */         if (isFirstStatementExecuted())
/*     */         {
/*     */           
/*  65 */           throw new SQLException("A statement has already been executed on this connection; cannot upgrade to write transaction");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  70 */         getDatabase()._exec("commit; /* need to explicitly upgrade transaction */");
/*     */ 
/*     */         
/*  73 */         getDatabase()._exec("PRAGMA query_only = false;");
/*  74 */         getDatabase()
/*  75 */           ._exec("BEGIN IMMEDIATE; /* explicitly upgrade transaction */");
/*  76 */         setCurrentTransactionMode(SQLiteConfig.TransactionMode.IMMEDIATE);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCatalog() throws SQLException {
/*  85 */     checkOpen();
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCatalog(String catalog) throws SQLException {
/*  91 */     checkOpen();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHoldability() throws SQLException {
/*  96 */     checkOpen();
/*  97 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHoldability(int h) throws SQLException {
/* 102 */     checkOpen();
/* 103 */     if (h != 2) {
/* 104 */       throw new SQLException("SQLite only supports CLOSE_CURSORS_AT_COMMIT");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Class<?>> getTypeMap() throws SQLException {
/* 110 */     synchronized (this) {
/* 111 */       if (this.typeMap == null) {
/* 112 */         this.typeMap = new HashMap<>();
/*     */       }
/*     */       
/* 115 */       return this.typeMap;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
/* 121 */     synchronized (this) {
/* 122 */       this.typeMap = map;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadOnly() {
/* 128 */     SQLiteConfig config = getDatabase().getConfig();
/* 129 */     return ((config
/*     */       
/* 131 */       .getOpenModeFlags() & SQLiteOpenMode.READONLY.flag) != 0 || (config
/*     */       
/* 133 */       .isExplicitReadOnly() && this.readOnly));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReadOnly(boolean ro) throws SQLException {
/* 138 */     if (getDatabase().getConfig().isExplicitReadOnly()) {
/* 139 */       if (ro != this.readOnly && isFirstStatementExecuted()) {
/* 140 */         throw new SQLException("Cannot change Read-Only status of this connection: the first statement was already executed and the transaction is open.");
/*     */ 
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 146 */     else if (ro != isReadOnly()) {
/* 147 */       throw new SQLException("Cannot change read-only flag after establishing a connection. Use SQLiteConfig#setReadOnly and SQLiteConfig.createConnection().");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 152 */     this.readOnly = ro;
/*     */   }
/*     */ 
/*     */   
/*     */   public String nativeSQL(String sql) {
/* 157 */     return sql;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearWarnings() throws SQLException {}
/*     */ 
/*     */   
/*     */   public SQLWarning getWarnings() throws SQLException {
/* 165 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Statement createStatement() throws SQLException {
/* 170 */     return createStatement(1003, 1007, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement createStatement(int rsType, int rsConcurr) throws SQLException {
/* 178 */     return createStatement(rsType, rsConcurr, 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract Statement createStatement(int paramInt1, int paramInt2, int paramInt3) throws SQLException;
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String sql) throws SQLException {
/* 186 */     return prepareCall(sql, 1003, 1007, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String sql, int rst, int rsc) throws SQLException {
/* 195 */     return prepareCall(sql, rst, rsc, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String sql, int rst, int rsc, int rsh) throws SQLException {
/* 201 */     throw new SQLException("SQLite does not support Stored Procedures");
/*     */   }
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql) throws SQLException {
/* 206 */     return prepareStatement(sql, 1003, 1007);
/*     */   }
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int autoC) throws SQLException {
/* 211 */     return prepareStatement(sql);
/*     */   }
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int[] colInds) throws SQLException {
/* 216 */     return prepareStatement(sql);
/*     */   }
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, String[] colNames) throws SQLException {
/* 221 */     return prepareStatement(sql);
/*     */   }
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int rst, int rsc) throws SQLException {
/* 226 */     return prepareStatement(sql, rst, rsc, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException;
/*     */ 
/*     */   
/*     */   public Savepoint setSavepoint() throws SQLException {
/* 235 */     checkOpen();
/* 236 */     if (getAutoCommit())
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 241 */       getConnectionConfig().setAutoCommit(false);
/*     */     }
/* 243 */     Savepoint sp = new JDBC3Savepoint(this.savePoint.incrementAndGet());
/* 244 */     getDatabase().exec(String.format("SAVEPOINT %s", new Object[] { sp.getSavepointName() }), false);
/* 245 */     return sp;
/*     */   }
/*     */ 
/*     */   
/*     */   public Savepoint setSavepoint(String name) throws SQLException {
/* 250 */     checkOpen();
/* 251 */     if (getAutoCommit())
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 256 */       getConnectionConfig().setAutoCommit(false);
/*     */     }
/* 258 */     Savepoint sp = new JDBC3Savepoint(this.savePoint.incrementAndGet(), name);
/* 259 */     getDatabase().exec(String.format("SAVEPOINT %s", new Object[] { sp.getSavepointName() }), false);
/* 260 */     return sp;
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseSavepoint(Savepoint savepoint) throws SQLException {
/* 265 */     checkOpen();
/* 266 */     if (getAutoCommit()) {
/* 267 */       throw new SQLException("database in auto-commit mode");
/*     */     }
/* 269 */     getDatabase()
/* 270 */       .exec(String.format("RELEASE SAVEPOINT %s", new Object[] { savepoint.getSavepointName() }), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rollback(Savepoint savepoint) throws SQLException {
/* 275 */     checkOpen();
/* 276 */     if (getAutoCommit()) {
/* 277 */       throw new SQLException("database in auto-commit mode");
/*     */     }
/* 279 */     getDatabase()
/* 280 */       .exec(
/* 281 */         String.format("ROLLBACK TO SAVEPOINT %s", new Object[] { savepoint.getSavepointName()
/* 282 */           }), getAutoCommit());
/*     */   }
/*     */   
/*     */   public Struct createStruct(String t, Object[] attr) throws SQLException {
/* 286 */     throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\jdbc3\JDBC3Connection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */