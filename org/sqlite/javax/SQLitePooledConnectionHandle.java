/*     */ package org.sqlite.javax;
/*     */ 
/*     */ import java.sql.Array;
/*     */ import java.sql.Blob;
/*     */ import java.sql.CallableStatement;
/*     */ import java.sql.Clob;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.NClob;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLClientInfoException;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLWarning;
/*     */ import java.sql.SQLXML;
/*     */ import java.sql.Savepoint;
/*     */ import java.sql.Statement;
/*     */ import java.sql.Struct;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.sql.ConnectionEvent;
/*     */ import javax.sql.ConnectionEventListener;
/*     */ import javax.sql.PooledConnection;
/*     */ import org.sqlite.SQLiteConnection;
/*     */ import org.sqlite.core.DB;
/*     */ import org.sqlite.jdbc4.JDBC4PreparedStatement;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SQLitePooledConnectionHandle
/*     */   extends SQLiteConnection
/*     */ {
/*     */   private final SQLitePooledConnection parent;
/* 177 */   private final AtomicBoolean isClosed = new AtomicBoolean(false);
/*     */   
/*     */   public SQLitePooledConnectionHandle(SQLitePooledConnection parent) {
/* 180 */     super(parent.getPhysicalConn().getDatabase());
/* 181 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public Statement createStatement() throws SQLException {
/* 186 */     return (Statement)new JDBC4Statement(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql) throws SQLException {
/* 191 */     return (PreparedStatement)new JDBC4PreparedStatement(this, sql);
/*     */   }
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String sql) throws SQLException {
/* 196 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String nativeSQL(String sql) throws SQLException {
/* 201 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAutoCommit(boolean autoCommit) throws SQLException {}
/*     */ 
/*     */   
/*     */   public boolean getAutoCommit() throws SQLException {
/* 209 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void commit() throws SQLException {}
/*     */ 
/*     */   
/*     */   public void rollback() throws SQLException {}
/*     */ 
/*     */   
/*     */   public void close() throws SQLException {
/* 220 */     ConnectionEvent event = new ConnectionEvent((PooledConnection)this.parent);
/*     */     
/* 222 */     List<ConnectionEventListener> listeners = this.parent.getListeners();
/* 223 */     for (int i = listeners.size() - 1; i >= 0; i--) {
/* 224 */       ((ConnectionEventListener)listeners.get(i)).connectionClosed(event);
/*     */     }
/*     */     
/* 227 */     if (!this.parent.getPhysicalConn().getAutoCommit()) {
/* 228 */       this.parent.getPhysicalConn().rollback();
/*     */     }
/* 230 */     this.parent.getPhysicalConn().setAutoCommit(true);
/* 231 */     this.isClosed.set(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/* 236 */     return this.isClosed.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public DatabaseMetaData getMetaData() throws SQLException {
/* 241 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReadOnly(boolean readOnly) throws SQLException {}
/*     */ 
/*     */   
/*     */   public boolean isReadOnly() throws SQLException {
/* 249 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCatalog(String catalog) throws SQLException {}
/*     */ 
/*     */   
/*     */   public String getCatalog() throws SQLException {
/* 257 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTransactionIsolation(int level) throws SQLException {}
/*     */ 
/*     */   
/*     */   public int getTransactionIsolation() {
/* 265 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLWarning getWarnings() throws SQLException {
/* 270 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearWarnings() throws SQLException {}
/*     */ 
/*     */   
/*     */   public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
/* 279 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
/* 285 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
/* 291 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Class<?>> getTypeMap() throws SQLException {
/* 296 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTypeMap(Map<String, Class<?>> map) throws SQLException {}
/*     */ 
/*     */   
/*     */   public void setHoldability(int holdability) throws SQLException {}
/*     */ 
/*     */   
/*     */   public int getHoldability() throws SQLException {
/* 307 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Savepoint setSavepoint() throws SQLException {
/* 312 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Savepoint setSavepoint(String name) throws SQLException {
/* 317 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollback(Savepoint savepoint) throws SQLException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseSavepoint(Savepoint savepoint) throws SQLException {}
/*     */ 
/*     */   
/*     */   public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
/* 330 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
/* 337 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
/* 344 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
/* 350 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
/* 355 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
/* 361 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Clob createClob() throws SQLException {
/* 366 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Blob createBlob() throws SQLException {
/* 371 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public NClob createNClob() throws SQLException {
/* 376 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLXML createSQLXML() throws SQLException {
/* 381 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValid(int timeout) throws SQLException {
/* 386 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClientInfo(String name, String value) throws SQLClientInfoException {}
/*     */ 
/*     */   
/*     */   public void setClientInfo(Properties properties) throws SQLClientInfoException {}
/*     */ 
/*     */   
/*     */   public String getClientInfo(String name) throws SQLException {
/* 397 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Properties getClientInfo() throws SQLException {
/* 402 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
/* 407 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
/* 412 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSchema(String schema) throws SQLException {}
/*     */ 
/*     */   
/*     */   public String getSchema() throws SQLException {
/* 420 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void abort(Executor executor) throws SQLException {}
/*     */ 
/*     */   
/*     */   public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {}
/*     */ 
/*     */   
/*     */   public int getNetworkTimeout() throws SQLException {
/* 431 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T unwrap(Class<T> iface) throws SQLException {
/* 436 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 441 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBusyTimeout() {
/* 446 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBusyTimeout(int timeoutMillis) {}
/*     */ 
/*     */   
/*     */   public DB getDatabase() {
/* 454 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\javax\SQLitePooledConnectionHandle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */