/*     */ package org.sqlite;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.sqlite.core.CoreDatabaseMetaData;
/*     */ import org.sqlite.core.DB;
/*     */ import org.sqlite.core.NativeDB;
/*     */ import org.sqlite.jdbc4.JDBC4DatabaseMetaData;
/*     */ 
/*     */ public abstract class SQLiteConnection
/*     */   implements Connection
/*     */ {
/*     */   private static final String RESOURCE_NAME_PREFIX = ":resource:";
/*     */   private final DB db;
/*  27 */   private CoreDatabaseMetaData meta = null;
/*     */ 
/*     */   
/*     */   private final SQLiteConnectionConfig connectionConfig;
/*     */ 
/*     */   
/*     */   private SQLiteConfig.TransactionMode currentTransactionMode;
/*     */   
/*     */   private boolean firstStatementExecuted = false;
/*     */ 
/*     */   
/*     */   public SQLiteConnection(DB db) {
/*  39 */     this.db = db;
/*  40 */     this.connectionConfig = db.getConfig().newConnectionConfig();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SQLiteConnection(String url, String fileName) throws SQLException {
/*  51 */     this(url, fileName, new Properties());
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
/*     */   public SQLiteConnection(String url, String fileName, Properties prop) throws SQLException {
/*  63 */     DB newDB = null;
/*     */     try {
/*  65 */       this.db = newDB = open(url, fileName, prop);
/*  66 */       SQLiteConfig config = this.db.getConfig();
/*  67 */       this.connectionConfig = this.db.getConfig().newConnectionConfig();
/*  68 */       config.apply(this);
/*  69 */       this.currentTransactionMode = getDatabase().getConfig().getTransactionMode();
/*     */       
/*  71 */       this.firstStatementExecuted = false;
/*  72 */     } catch (Throwable t) {
/*     */       try {
/*  74 */         if (newDB != null) {
/*  75 */           newDB.close();
/*     */         }
/*  77 */       } catch (Exception e) {
/*  78 */         t.addSuppressed(e);
/*     */       } 
/*  80 */       throw t;
/*     */     } 
/*     */   }
/*     */   
/*     */   public SQLiteConfig.TransactionMode getCurrentTransactionMode() {
/*  85 */     return this.currentTransactionMode;
/*     */   }
/*     */   
/*     */   public void setCurrentTransactionMode(SQLiteConfig.TransactionMode currentTransactionMode) {
/*  89 */     this.currentTransactionMode = currentTransactionMode;
/*     */   }
/*     */   
/*     */   public void setFirstStatementExecuted(boolean firstStatementExecuted) {
/*  93 */     this.firstStatementExecuted = firstStatementExecuted;
/*     */   }
/*     */   
/*     */   public boolean isFirstStatementExecuted() {
/*  97 */     return this.firstStatementExecuted;
/*     */   }
/*     */   
/*     */   public SQLiteConnectionConfig getConnectionConfig() {
/* 101 */     return this.connectionConfig;
/*     */   }
/*     */   
/*     */   public CoreDatabaseMetaData getSQLiteDatabaseMetaData() throws SQLException {
/* 105 */     checkOpen();
/*     */     
/* 107 */     if (this.meta == null) {
/* 108 */       this.meta = (CoreDatabaseMetaData)new JDBC4DatabaseMetaData(this);
/*     */     }
/*     */     
/* 111 */     return this.meta;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatabaseMetaData getMetaData() throws SQLException {
/* 116 */     return (DatabaseMetaData)getSQLiteDatabaseMetaData();
/*     */   }
/*     */   
/*     */   public String getUrl() {
/* 120 */     return this.db.getUrl();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSchema(String schema) throws SQLException {}
/*     */ 
/*     */   
/*     */   public String getSchema() throws SQLException {
/* 129 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void abort(Executor executor) throws SQLException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {}
/*     */ 
/*     */   
/*     */   public int getNetworkTimeout() throws SQLException {
/* 142 */     return 0;
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
/*     */   protected void checkCursor(int rst, int rsc, int rsh) throws SQLException {
/* 161 */     if (rst != 1003)
/* 162 */       throw new SQLException("SQLite only supports TYPE_FORWARD_ONLY cursors"); 
/* 163 */     if (rsc != 1007)
/* 164 */       throw new SQLException("SQLite only supports CONCUR_READ_ONLY cursors"); 
/* 165 */     if (rsh != 2) {
/* 166 */       throw new SQLException("SQLite only supports closing cursors at commit");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setTransactionMode(SQLiteConfig.TransactionMode mode) {
/* 177 */     this.connectionConfig.setTransactionMode(mode);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTransactionIsolation() {
/* 183 */     return this.connectionConfig.getTransactionIsolation();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTransactionIsolation(int level) throws SQLException {
/* 188 */     checkOpen();
/*     */     
/* 190 */     switch (level) {
/*     */       
/*     */       case 2:
/*     */       case 4:
/*     */       case 8:
/* 195 */         getDatabase().exec("PRAGMA read_uncommitted = false;", getAutoCommit());
/*     */         break;
/*     */       case 1:
/* 198 */         getDatabase().exec("PRAGMA read_uncommitted = true;", getAutoCommit());
/*     */         break;
/*     */       default:
/* 201 */         throw new SQLException("Unsupported transaction isolation level: " + level + ". Must be one of TRANSACTION_READ_UNCOMMITTED, TRANSACTION_READ_COMMITTED, TRANSACTION_REPEATABLE_READ, or TRANSACTION_SERIALIZABLE in java.sql.Connection");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     this.connectionConfig.setTransactionIsolation(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static DB open(String url, String origFileName, Properties props) throws SQLException {
/*     */     NativeDB nativeDB;
/* 219 */     Properties newProps = new Properties();
/* 220 */     newProps.putAll(props);
/*     */ 
/*     */     
/* 223 */     String fileName = extractPragmasFromFilename(url, origFileName, newProps);
/* 224 */     SQLiteConfig config = new SQLiteConfig(newProps);
/*     */ 
/*     */     
/* 227 */     if (!fileName.isEmpty() && 
/* 228 */       !":memory:".equals(fileName) && 
/* 229 */       !fileName.startsWith("file:") && 
/* 230 */       !fileName.contains("mode=memory")) {
/* 231 */       if (fileName.startsWith(":resource:")) {
/* 232 */         String resourceName = fileName.substring(":resource:".length());
/*     */ 
/*     */         
/* 235 */         ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
/* 236 */         URL resourceAddr = contextCL.getResource(resourceName);
/* 237 */         if (resourceAddr == null) {
/*     */           try {
/* 239 */             resourceAddr = new URL(resourceName);
/* 240 */           } catch (MalformedURLException e) {
/* 241 */             throw new SQLException(
/* 242 */                 String.format("resource %s not found: %s", new Object[] { resourceName, e }));
/*     */           } 
/*     */         }
/*     */         
/*     */         try {
/* 247 */           fileName = extractResource(resourceAddr).getAbsolutePath();
/* 248 */         } catch (IOException e) {
/* 249 */           throw new SQLException(String.format("failed to load %s: %s", new Object[] { resourceName, e }));
/*     */         } 
/*     */       } else {
/* 252 */         File file = (new File(fileName)).getAbsoluteFile();
/* 253 */         File parent = file.getParentFile();
/* 254 */         if (parent != null && !parent.exists()) {
/* 255 */           for (File up = parent; up != null && !up.exists(); ) {
/* 256 */             parent = up;
/* 257 */             up = up.getParentFile();
/*     */           } 
/* 259 */           throw new SQLException("path to '" + fileName + "': '" + parent + "' does not exist");
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 267 */           if (!file.exists() && file.createNewFile()) file.delete(); 
/* 268 */         } catch (Exception e) {
/* 269 */           throw new SQLException("opening db: '" + fileName + "': " + e.getMessage());
/*     */         } 
/* 271 */         fileName = file.getAbsolutePath();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 276 */     DB db = null;
/*     */     try {
/* 278 */       NativeDB.load();
/* 279 */       nativeDB = new NativeDB(url, fileName, config);
/* 280 */     } catch (Exception e) {
/* 281 */       SQLException err = new SQLException("Error opening connection");
/* 282 */       err.initCause(e);
/* 283 */       throw err;
/*     */     } 
/* 285 */     nativeDB.open(fileName, config.getOpenModeFlags());
/* 286 */     return (DB)nativeDB;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static File extractResource(URL resourceAddr) throws IOException {
/* 297 */     if (resourceAddr.getProtocol().equals("file")) {
/*     */       try {
/* 299 */         return new File(resourceAddr.toURI());
/* 300 */       } catch (URISyntaxException e) {
/* 301 */         throw new IOException(e.getMessage());
/*     */       } 
/*     */     }
/*     */     
/* 305 */     String tempFolder = (new File(System.getProperty("java.io.tmpdir"))).getAbsolutePath();
/* 306 */     String dbFileName = String.format("sqlite-jdbc-tmp-%d.db", new Object[] { Integer.valueOf(resourceAddr.hashCode()) });
/* 307 */     File dbFile = new File(tempFolder, dbFileName);
/*     */     
/* 309 */     if (dbFile.exists()) {
/* 310 */       long resourceLastModified = resourceAddr.openConnection().getLastModified();
/* 311 */       long tmpFileLastModified = dbFile.lastModified();
/* 312 */       if (resourceLastModified < tmpFileLastModified) {
/* 313 */         return dbFile;
/*     */       }
/*     */       
/* 316 */       boolean deletionSucceeded = dbFile.delete();
/* 317 */       if (!deletionSucceeded) {
/* 318 */         throw new IOException("failed to remove existing DB file: " + dbFile
/* 319 */             .getAbsolutePath());
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 333 */     try (InputStream reader = resourceAddr.openStream()) {
/* 334 */       Files.copy(reader, dbFile.toPath(), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 335 */       return dbFile;
/*     */     } 
/*     */   }
/*     */   
/*     */   public DB getDatabase() {
/* 340 */     return this.db;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getAutoCommit() throws SQLException {
/* 346 */     checkOpen();
/*     */     
/* 348 */     return this.connectionConfig.isAutoCommit();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoCommit(boolean ac) throws SQLException {
/* 354 */     checkOpen();
/* 355 */     if (this.connectionConfig.isAutoCommit() == ac)
/*     */       return; 
/* 357 */     this.connectionConfig.setAutoCommit(ac);
/*     */ 
/*     */     
/* 360 */     if (getConnectionConfig().isAutoCommit()) {
/* 361 */       this.db.exec("commit;", ac);
/* 362 */       this.currentTransactionMode = null;
/*     */     } else {
/* 364 */       this.db.exec(transactionPrefix(), ac);
/* 365 */       this.currentTransactionMode = getConnectionConfig().getTransactionMode();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBusyTimeout() {
/* 375 */     return this.db.getConfig().getBusyTimeout();
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
/*     */   public void setBusyTimeout(int timeoutMillis) throws SQLException {
/* 388 */     this.db.getConfig().setBusyTimeout(timeoutMillis);
/* 389 */     this.db.busy_timeout(timeoutMillis);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLimit(SQLiteLimits limit, int value) throws SQLException {
/* 395 */     if (value >= 0) {
/* 396 */       this.db.limit(limit.getId(), value);
/*     */     }
/*     */   }
/*     */   
/*     */   public void getLimit(SQLiteLimits limit) throws SQLException {
/* 401 */     this.db.limit(limit.getId(), -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClosed() throws SQLException {
/* 406 */     return this.db.isClosed();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws SQLException {
/* 412 */     if (isClosed())
/* 413 */       return;  if (this.meta != null) this.meta.close();
/*     */     
/* 415 */     this.db.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkOpen() throws SQLException {
/* 424 */     if (isClosed()) throw new SQLException("database connection closed");
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String libversion() throws SQLException {
/* 434 */     checkOpen();
/*     */     
/* 436 */     return this.db.libversion();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void commit() throws SQLException {
/* 442 */     checkOpen();
/* 443 */     if (this.connectionConfig.isAutoCommit()) throw new SQLException("database in auto-commit mode"); 
/* 444 */     this.db.exec("commit;", getAutoCommit());
/* 445 */     this.db.exec(transactionPrefix(), getAutoCommit());
/* 446 */     this.firstStatementExecuted = false;
/* 447 */     setCurrentTransactionMode(getConnectionConfig().getTransactionMode());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollback() throws SQLException {
/* 453 */     checkOpen();
/* 454 */     if (this.connectionConfig.isAutoCommit()) throw new SQLException("database in auto-commit mode"); 
/* 455 */     this.db.exec("rollback;", getAutoCommit());
/* 456 */     this.db.exec(transactionPrefix(), getAutoCommit());
/* 457 */     this.firstStatementExecuted = false;
/* 458 */     setCurrentTransactionMode(getConnectionConfig().getTransactionMode());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addUpdateListener(SQLiteUpdateListener listener) {
/* 467 */     this.db.addUpdateListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeUpdateListener(SQLiteUpdateListener listener) {
/* 476 */     this.db.removeUpdateListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCommitListener(SQLiteCommitListener listener) {
/* 486 */     this.db.addCommitListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeCommitListener(SQLiteCommitListener listener) {
/* 495 */     this.db.removeCommitListener(listener);
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
/*     */   protected static String extractPragmasFromFilename(String url, String filename, Properties prop) throws SQLException {
/* 509 */     int parameterDelimiter = filename.indexOf('?');
/* 510 */     if (parameterDelimiter == -1)
/*     */     {
/* 512 */       return filename;
/*     */     }
/*     */     
/* 515 */     StringBuilder sb = new StringBuilder();
/* 516 */     sb.append(filename.substring(0, parameterDelimiter));
/*     */     
/* 518 */     int nonPragmaCount = 0;
/* 519 */     String[] parameters = filename.substring(parameterDelimiter + 1).split("&");
/* 520 */     for (int i = 0; i < parameters.length; i++) {
/*     */       
/* 522 */       String parameter = parameters[parameters.length - 1 - i].trim();
/*     */       
/* 524 */       if (!parameter.isEmpty()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 529 */         String[] kvp = parameter.split("=");
/* 530 */         String key = kvp[0].trim().toLowerCase();
/* 531 */         if (SQLiteConfig.pragmaSet.contains(key)) {
/* 532 */           if (kvp.length == 1) {
/* 533 */             throw new SQLException(
/* 534 */                 String.format("Please specify a value for PRAGMA %s in URL %s", new Object[] { key, url }));
/*     */           }
/*     */           
/* 537 */           String value = kvp[1].trim();
/* 538 */           if (!value.isEmpty() && 
/* 539 */             !prop.containsKey(key))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 549 */             prop.setProperty(key, value);
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 554 */           sb.append((nonPragmaCount == 0) ? 63 : 38);
/* 555 */           sb.append(parameter);
/* 556 */           nonPragmaCount++;
/*     */         } 
/*     */       } 
/*     */     } 
/* 560 */     String newFilename = sb.toString();
/* 561 */     return newFilename;
/*     */   }
/*     */   
/*     */   protected String transactionPrefix() {
/* 565 */     return this.connectionConfig.transactionPrefix();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\SQLiteConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */