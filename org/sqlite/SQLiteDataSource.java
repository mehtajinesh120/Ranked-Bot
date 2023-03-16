/*     */ package org.sqlite;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Logger;
/*     */ import javax.sql.DataSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SQLiteDataSource
/*     */   implements DataSource
/*     */ {
/*     */   private SQLiteConfig config;
/*     */   private transient PrintWriter logger;
/*  44 */   private int loginTimeout = 1;
/*     */   
/*  46 */   private String url = "jdbc:sqlite:";
/*  47 */   private String databaseName = "";
/*     */ 
/*     */   
/*     */   public SQLiteDataSource() {
/*  51 */     this.config = new SQLiteConfig();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SQLiteDataSource(SQLiteConfig config) {
/*  60 */     this.config = config;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfig(SQLiteConfig config) {
/*  69 */     this.config = config;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLiteConfig getConfig() {
/*  74 */     return this.config;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrl(String url) {
/*  83 */     this.url = url;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUrl() {
/*  88 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDatabaseName(String databaseName) {
/*  97 */     this.databaseName = databaseName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDatabaseName() {
/* 105 */     return this.databaseName;
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
/*     */   public void setSharedCache(boolean enable) {
/* 117 */     this.config.setSharedCache(enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLoadExtension(boolean enable) {
/* 128 */     this.config.enableLoadExtension(enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadOnly(boolean readOnly) {
/* 139 */     this.config.setReadOnly(readOnly);
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
/*     */   public void setCacheSize(int numberOfPages) {
/* 151 */     this.config.setCacheSize(numberOfPages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCaseSensitiveLike(boolean enable) {
/* 162 */     this.config.enableCaseSensitiveLike(enable);
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
/*     */   public void setCountChanges(boolean enable) {
/* 174 */     this.config.enableCountChanges(enable);
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
/*     */   public void setDefaultCacheSize(int numberOfPages) {
/* 186 */     this.config.setDefaultCacheSize(numberOfPages);
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
/*     */   public void setEncoding(String encoding) {
/* 198 */     this.config.setEncoding(SQLiteConfig.Encoding.getEncoding(encoding));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnforceForeignKeys(boolean enforce) {
/* 209 */     this.config.enforceForeignKeys(enforce);
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
/*     */   public void setFullColumnNames(boolean enable) {
/* 222 */     this.config.enableFullColumnNames(enable);
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
/*     */   public void setFullSync(boolean enable) {
/* 234 */     this.config.enableFullSync(enable);
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
/*     */   public void setIncrementalVacuum(int numberOfPagesToBeRemoved) {
/* 246 */     this.config.incrementalVacuum(numberOfPagesToBeRemoved);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJournalMode(String mode) {
/* 257 */     this.config.setJournalMode(SQLiteConfig.JournalMode.valueOf(mode));
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
/*     */   public void setJournalSizeLimit(int limit) {
/* 269 */     this.config.setJounalSizeLimit(limit);
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
/*     */   public void setLegacyFileFormat(boolean use) {
/* 283 */     this.config.useLegacyFileFormat(use);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLockingMode(String mode) {
/* 294 */     this.config.setLockingMode(SQLiteConfig.LockingMode.valueOf(mode));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPageSize(int numBytes) {
/* 305 */     this.config.setPageSize(numBytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxPageCount(int numPages) {
/* 316 */     this.config.setMaxPageCount(numPages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadUncommited(boolean useReadUncommitedIsolationMode) {
/* 327 */     this.config.setReadUncommited(useReadUncommitedIsolationMode);
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
/*     */   public void setRecursiveTriggers(boolean enable) {
/* 340 */     this.config.enableRecursiveTriggers(enable);
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
/*     */   public void setReverseUnorderedSelects(boolean enable) {
/* 353 */     this.config.enableReverseUnorderedSelects(enable);
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
/*     */   public void setShortColumnNames(boolean enable) {
/* 367 */     this.config.enableShortColumnNames(enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSynchronous(String mode) {
/* 378 */     this.config.setSynchronous(SQLiteConfig.SynchronousMode.valueOf(mode));
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
/*     */   public void setTempStore(String storeType) {
/* 390 */     this.config.setTempStore(SQLiteConfig.TempStore.valueOf(storeType));
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
/*     */   public void setTempStoreDirectory(String directoryName) {
/* 402 */     this.config.setTempStoreDirectory(directoryName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransactionMode(String transactionMode) {
/* 413 */     this.config.setTransactionMode(transactionMode);
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
/*     */   public void setUserVersion(int version) {
/* 425 */     this.config.setUserVersion(version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/* 432 */     return getConnection((String)null, (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLiteConnection getConnection(String username, String password) throws SQLException {
/* 437 */     Properties p = this.config.toProperties();
/* 438 */     if (username != null) p.put("user", username); 
/* 439 */     if (password != null) p.put("pass", password); 
/* 440 */     return JDBC.createConnection(this.url, p);
/*     */   }
/*     */ 
/*     */   
/*     */   public PrintWriter getLogWriter() throws SQLException {
/* 445 */     return this.logger;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLoginTimeout() throws SQLException {
/* 450 */     return this.loginTimeout;
/*     */   }
/*     */   
/*     */   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
/* 454 */     throw new SQLFeatureNotSupportedException("getParentLogger");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLogWriter(PrintWriter out) throws SQLException {
/* 459 */     this.logger = out;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLoginTimeout(int seconds) throws SQLException {
/* 464 */     this.loginTimeout = seconds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 475 */     return iface.isInstance(this);
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
/*     */   public <T> T unwrap(Class<T> iface) throws SQLException {
/* 487 */     return (T)this;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\SQLiteDataSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */