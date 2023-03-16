/*      */ package org.sqlite;
/*      */ 
/*      */ import java.sql.Connection;
/*      */ import java.sql.DriverPropertyInfo;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Statement;
/*      */ import java.util.HashSet;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SQLiteConfig
/*      */ {
/*      */   public static final String DEFAULT_DATE_STRING_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
/*      */   private static final int DEFAULT_MAX_LENGTH = 1000000000;
/*      */   private static final int DEFAULT_MAX_COLUMN = 2000;
/*      */   private static final int DEFAULT_MAX_SQL_LENGTH = 1000000;
/*      */   private static final int DEFAULT_MAX_FUNCTION_ARG = 100;
/*      */   private static final int DEFAULT_MAX_ATTACHED = 10;
/*      */   private static final int DEFAULT_MAX_PAGE_COUNT = 1073741823;
/*      */   private final Properties pragmaTable;
/*   55 */   private int openModeFlag = 0;
/*      */   
/*      */   private final int busyTimeout;
/*      */   
/*      */   private boolean explicitReadOnly;
/*      */   
/*      */   private final SQLiteConnectionConfig defaultConnectionConfig;
/*      */   
/*      */   public SQLiteConfig() {
/*   64 */     this(new Properties());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SQLiteConfig(Properties prop) {
/*   73 */     this.pragmaTable = prop;
/*      */     
/*   75 */     String openMode = this.pragmaTable.getProperty(Pragma.OPEN_MODE.pragmaName);
/*   76 */     if (openMode != null) {
/*   77 */       this.openModeFlag = Integer.parseInt(openMode);
/*      */     } else {
/*      */       
/*   80 */       setOpenMode(SQLiteOpenMode.READWRITE);
/*   81 */       setOpenMode(SQLiteOpenMode.CREATE);
/*      */     } 
/*      */     
/*   84 */     setSharedCache(
/*   85 */         Boolean.parseBoolean(this.pragmaTable
/*   86 */           .getProperty(Pragma.SHARED_CACHE.pragmaName, "false")));
/*      */     
/*   88 */     setOpenMode(SQLiteOpenMode.OPEN_URI);
/*      */     
/*   90 */     this
/*   91 */       .busyTimeout = Integer.parseInt(this.pragmaTable.getProperty(Pragma.BUSY_TIMEOUT.pragmaName, "3000"));
/*   92 */     this.defaultConnectionConfig = SQLiteConnectionConfig.fromPragmaTable(this.pragmaTable);
/*   93 */     this
/*   94 */       .explicitReadOnly = Boolean.parseBoolean(this.pragmaTable
/*   95 */         .getProperty(Pragma.JDBC_EXPLICIT_READONLY.pragmaName, "false"));
/*      */   }
/*      */   
/*      */   public SQLiteConnectionConfig newConnectionConfig() {
/*   99 */     return this.defaultConnectionConfig.copyConfig();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Connection createConnection(String url) throws SQLException {
/*  109 */     return JDBC.createConnection(url, toProperties());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void apply(Connection conn) throws SQLException {
/*  120 */     HashSet<String> pragmaParams = new HashSet<>();
/*  121 */     for (Pragma each : Pragma.values()) {
/*  122 */       pragmaParams.add(each.pragmaName);
/*      */     }
/*      */     
/*  125 */     if (conn instanceof SQLiteConnection) {
/*  126 */       SQLiteConnection sqliteConn = (SQLiteConnection)conn;
/*  127 */       sqliteConn.setLimit(SQLiteLimits.SQLITE_LIMIT_ATTACHED, 
/*      */           
/*  129 */           parseLimitPragma(Pragma.LIMIT_ATTACHED, 10));
/*  130 */       sqliteConn.setLimit(SQLiteLimits.SQLITE_LIMIT_COLUMN, 
/*      */           
/*  132 */           parseLimitPragma(Pragma.LIMIT_COLUMN, 2000));
/*  133 */       sqliteConn.setLimit(SQLiteLimits.SQLITE_LIMIT_COMPOUND_SELECT, 
/*      */           
/*  135 */           parseLimitPragma(Pragma.LIMIT_COMPOUND_SELECT, -1));
/*  136 */       sqliteConn.setLimit(SQLiteLimits.SQLITE_LIMIT_EXPR_DEPTH, 
/*      */           
/*  138 */           parseLimitPragma(Pragma.LIMIT_EXPR_DEPTH, -1));
/*  139 */       sqliteConn.setLimit(SQLiteLimits.SQLITE_LIMIT_FUNCTION_ARG, 
/*      */           
/*  141 */           parseLimitPragma(Pragma.LIMIT_FUNCTION_ARG, 100));
/*  142 */       sqliteConn.setLimit(SQLiteLimits.SQLITE_LIMIT_LENGTH, 
/*      */           
/*  144 */           parseLimitPragma(Pragma.LIMIT_LENGTH, 1000000000));
/*  145 */       sqliteConn.setLimit(SQLiteLimits.SQLITE_LIMIT_LIKE_PATTERN_LENGTH, 
/*      */           
/*  147 */           parseLimitPragma(Pragma.LIMIT_LIKE_PATTERN_LENGTH, -1));
/*  148 */       sqliteConn.setLimit(SQLiteLimits.SQLITE_LIMIT_SQL_LENGTH, 
/*      */           
/*  150 */           parseLimitPragma(Pragma.LIMIT_SQL_LENGTH, 1000000));
/*  151 */       sqliteConn.setLimit(SQLiteLimits.SQLITE_LIMIT_TRIGGER_DEPTH, 
/*      */           
/*  153 */           parseLimitPragma(Pragma.LIMIT_TRIGGER_DEPTH, -1));
/*  154 */       sqliteConn.setLimit(SQLiteLimits.SQLITE_LIMIT_VARIABLE_NUMBER, 
/*      */           
/*  156 */           parseLimitPragma(Pragma.LIMIT_VARIABLE_NUMBER, -1));
/*  157 */       sqliteConn.setLimit(SQLiteLimits.SQLITE_LIMIT_VDBE_OP, 
/*  158 */           parseLimitPragma(Pragma.LIMIT_VDBE_OP, -1));
/*  159 */       sqliteConn.setLimit(SQLiteLimits.SQLITE_LIMIT_WORKER_THREADS, 
/*      */           
/*  161 */           parseLimitPragma(Pragma.LIMIT_WORKER_THREADS, -1));
/*  162 */       sqliteConn.setLimit(SQLiteLimits.SQLITE_LIMIT_PAGE_COUNT, 
/*      */           
/*  164 */           parseLimitPragma(Pragma.LIMIT_PAGE_COUNT, 1073741823));
/*      */     } 
/*      */     
/*  167 */     pragmaParams.remove(Pragma.OPEN_MODE.pragmaName);
/*  168 */     pragmaParams.remove(Pragma.SHARED_CACHE.pragmaName);
/*  169 */     pragmaParams.remove(Pragma.LOAD_EXTENSION.pragmaName);
/*  170 */     pragmaParams.remove(Pragma.DATE_PRECISION.pragmaName);
/*  171 */     pragmaParams.remove(Pragma.DATE_CLASS.pragmaName);
/*  172 */     pragmaParams.remove(Pragma.DATE_STRING_FORMAT.pragmaName);
/*  173 */     pragmaParams.remove(Pragma.PASSWORD.pragmaName);
/*  174 */     pragmaParams.remove(Pragma.HEXKEY_MODE.pragmaName);
/*  175 */     pragmaParams.remove(Pragma.LIMIT_ATTACHED.pragmaName);
/*  176 */     pragmaParams.remove(Pragma.LIMIT_COLUMN.pragmaName);
/*  177 */     pragmaParams.remove(Pragma.LIMIT_COMPOUND_SELECT.pragmaName);
/*  178 */     pragmaParams.remove(Pragma.LIMIT_EXPR_DEPTH.pragmaName);
/*  179 */     pragmaParams.remove(Pragma.LIMIT_FUNCTION_ARG.pragmaName);
/*  180 */     pragmaParams.remove(Pragma.LIMIT_LENGTH.pragmaName);
/*  181 */     pragmaParams.remove(Pragma.LIMIT_LIKE_PATTERN_LENGTH.pragmaName);
/*  182 */     pragmaParams.remove(Pragma.LIMIT_SQL_LENGTH.pragmaName);
/*  183 */     pragmaParams.remove(Pragma.LIMIT_TRIGGER_DEPTH.pragmaName);
/*  184 */     pragmaParams.remove(Pragma.LIMIT_VARIABLE_NUMBER.pragmaName);
/*  185 */     pragmaParams.remove(Pragma.LIMIT_VDBE_OP.pragmaName);
/*  186 */     pragmaParams.remove(Pragma.LIMIT_WORKER_THREADS.pragmaName);
/*  187 */     pragmaParams.remove(Pragma.LIMIT_PAGE_COUNT.pragmaName);
/*      */ 
/*      */     
/*  190 */     pragmaParams.remove(Pragma.JDBC_EXPLICIT_READONLY.pragmaName);
/*      */     
/*  192 */     Statement stat = conn.createStatement();
/*      */     try {
/*  194 */       if (this.pragmaTable.containsKey(Pragma.PASSWORD.pragmaName)) {
/*  195 */         String password = this.pragmaTable.getProperty(Pragma.PASSWORD.pragmaName);
/*  196 */         if (password != null && !password.isEmpty()) {
/*  197 */           String passwordPragma, hexkeyMode = this.pragmaTable.getProperty(Pragma.HEXKEY_MODE.pragmaName);
/*      */           
/*  199 */           if (HexKeyMode.SSE.name().equalsIgnoreCase(hexkeyMode)) {
/*  200 */             passwordPragma = "pragma hexkey = '%s'";
/*  201 */           } else if (HexKeyMode.SQLCIPHER.name().equalsIgnoreCase(hexkeyMode)) {
/*  202 */             passwordPragma = "pragma key = \"x'%s'\"";
/*      */           } else {
/*  204 */             passwordPragma = "pragma key = '%s'";
/*      */           } 
/*  206 */           stat.execute(String.format(passwordPragma, new Object[] { password.replace("'", "''") }));
/*  207 */           stat.execute("select 1 from sqlite_master");
/*      */         } 
/*      */       } 
/*      */       
/*  211 */       for (Object each : this.pragmaTable.keySet()) {
/*  212 */         String key = each.toString();
/*  213 */         if (!pragmaParams.contains(key)) {
/*      */           continue;
/*      */         }
/*      */         
/*  217 */         String value = this.pragmaTable.getProperty(key);
/*  218 */         if (value != null) {
/*  219 */           stat.execute(String.format("pragma %s=%s", new Object[] { key, value }));
/*      */         }
/*      */       } 
/*      */     } finally {
/*  223 */       if (stat != null) {
/*  224 */         stat.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void set(Pragma pragma, boolean flag) {
/*  236 */     setPragma(pragma, Boolean.toString(flag));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void set(Pragma pragma, int num) {
/*  246 */     setPragma(pragma, Integer.toString(num));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean getBoolean(Pragma pragma, String defaultValue) {
/*  257 */     return Boolean.parseBoolean(this.pragmaTable.getProperty(pragma.pragmaName, defaultValue));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int parseLimitPragma(Pragma pragma, int defaultValue) {
/*  268 */     if (!this.pragmaTable.containsKey(pragma.pragmaName)) {
/*  269 */       return defaultValue;
/*      */     }
/*  271 */     String valueString = this.pragmaTable.getProperty(pragma.pragmaName);
/*      */     try {
/*  273 */       return Integer.parseInt(valueString);
/*  274 */     } catch (NumberFormatException ex) {
/*  275 */       return defaultValue;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnabledSharedCache() {
/*  285 */     return getBoolean(Pragma.SHARED_CACHE, "false");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnabledLoadExtension() {
/*  294 */     return getBoolean(Pragma.LOAD_EXTENSION, "false");
/*      */   }
/*      */ 
/*      */   
/*      */   public int getOpenModeFlags() {
/*  299 */     return this.openModeFlag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPragma(Pragma pragma, String value) {
/*  309 */     this.pragmaTable.put(pragma.pragmaName, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Properties toProperties() {
/*  319 */     this.pragmaTable.setProperty(Pragma.OPEN_MODE.pragmaName, Integer.toString(this.openModeFlag));
/*  320 */     this.pragmaTable.setProperty(Pragma.TRANSACTION_MODE.pragmaName, this.defaultConnectionConfig
/*      */         
/*  322 */         .getTransactionMode().getValue());
/*  323 */     this.pragmaTable.setProperty(Pragma.DATE_CLASS.pragmaName, this.defaultConnectionConfig
/*  324 */         .getDateClass().getValue());
/*  325 */     this.pragmaTable.setProperty(Pragma.DATE_PRECISION.pragmaName, this.defaultConnectionConfig
/*      */         
/*  327 */         .getDatePrecision().getValue());
/*  328 */     this.pragmaTable.setProperty(Pragma.DATE_STRING_FORMAT.pragmaName, this.defaultConnectionConfig
/*      */         
/*  330 */         .getDateStringFormat());
/*  331 */     this.pragmaTable.setProperty(Pragma.JDBC_EXPLICIT_READONLY.pragmaName, this.explicitReadOnly ? "true" : "false");
/*      */     
/*  333 */     return this.pragmaTable;
/*      */   }
/*      */ 
/*      */   
/*      */   static DriverPropertyInfo[] getDriverPropertyInfo() {
/*  338 */     Pragma[] pragma = Pragma.values();
/*  339 */     DriverPropertyInfo[] result = new DriverPropertyInfo[pragma.length];
/*  340 */     int index = 0;
/*  341 */     for (Pragma p : Pragma.values()) {
/*  342 */       DriverPropertyInfo di = new DriverPropertyInfo(p.pragmaName, null);
/*  343 */       di.choices = p.choices;
/*  344 */       di.description = p.description;
/*  345 */       di.required = false;
/*  346 */       result[index++] = di;
/*      */     } 
/*      */     
/*  349 */     return result;
/*      */   }
/*      */   
/*  352 */   private static final String[] OnOff = new String[] { "true", "false" };
/*      */   
/*  354 */   static final Set<String> pragmaSet = new TreeSet<>();
/*      */   
/*      */   static {
/*  357 */     for (Pragma pragma : Pragma.values()) {
/*  358 */       pragmaSet.add(pragma.pragmaName);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isExplicitReadOnly() {
/*  364 */     return this.explicitReadOnly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExplicitReadOnly(boolean readOnly) {
/*  373 */     this.explicitReadOnly = readOnly;
/*      */   }
/*      */ 
/*      */   
/*      */   public enum Pragma
/*      */   {
/*  379 */     OPEN_MODE("open_mode", "Database open-mode flag", null),
/*  380 */     SHARED_CACHE("shared_cache", "Enable SQLite Shared-Cache mode, native driver only", (String)SQLiteConfig.OnOff),
/*  381 */     LOAD_EXTENSION("enable_load_extension", "Enable SQLite load_extension() function, native driver only", 
/*      */ 
/*      */       
/*  384 */       (String)SQLiteConfig.OnOff),
/*      */ 
/*      */     
/*  387 */     CACHE_SIZE("cache_size"),
/*  388 */     MMAP_SIZE("mmap_size"),
/*  389 */     CASE_SENSITIVE_LIKE("case_sensitive_like", SQLiteConfig.OnOff),
/*  390 */     COUNT_CHANGES("count_changes", SQLiteConfig.OnOff),
/*  391 */     DEFAULT_CACHE_SIZE("default_cache_size"),
/*  392 */     DEFER_FOREIGN_KEYS("defer_foreign_keys", SQLiteConfig.OnOff),
/*  393 */     EMPTY_RESULT_CALLBACKS("empty_result_callback", SQLiteConfig.OnOff),
/*  394 */     ENCODING("encoding", SQLiteConfig.toStringArray((SQLiteConfig.PragmaValue[])SQLiteConfig.Encoding.values())),
/*  395 */     FOREIGN_KEYS("foreign_keys", SQLiteConfig.OnOff),
/*  396 */     FULL_COLUMN_NAMES("full_column_names", SQLiteConfig.OnOff),
/*  397 */     FULL_SYNC("fullsync", SQLiteConfig.OnOff),
/*  398 */     INCREMENTAL_VACUUM("incremental_vacuum"),
/*  399 */     JOURNAL_MODE("journal_mode", SQLiteConfig.toStringArray((SQLiteConfig.PragmaValue[])SQLiteConfig.JournalMode.values())),
/*  400 */     JOURNAL_SIZE_LIMIT("journal_size_limit"),
/*  401 */     LEGACY_FILE_FORMAT("legacy_file_format", SQLiteConfig.OnOff),
/*  402 */     LOCKING_MODE("locking_mode", SQLiteConfig.toStringArray((SQLiteConfig.PragmaValue[])SQLiteConfig.LockingMode.values())),
/*  403 */     PAGE_SIZE("page_size"),
/*  404 */     MAX_PAGE_COUNT("max_page_count"),
/*  405 */     READ_UNCOMMITTED("read_uncommitted", SQLiteConfig.OnOff),
/*  406 */     RECURSIVE_TRIGGERS("recursive_triggers", SQLiteConfig.OnOff),
/*  407 */     REVERSE_UNORDERED_SELECTS("reverse_unordered_selects", SQLiteConfig.OnOff),
/*  408 */     SECURE_DELETE("secure_delete", new String[] { "true", "false", "fast" }),
/*  409 */     SHORT_COLUMN_NAMES("short_column_names", SQLiteConfig.OnOff),
/*  410 */     SYNCHRONOUS("synchronous", SQLiteConfig.toStringArray((SQLiteConfig.PragmaValue[])SQLiteConfig.SynchronousMode.values())),
/*  411 */     TEMP_STORE("temp_store", SQLiteConfig.toStringArray((SQLiteConfig.PragmaValue[])SQLiteConfig.TempStore.values())),
/*  412 */     TEMP_STORE_DIRECTORY("temp_store_directory"),
/*  413 */     USER_VERSION("user_version"),
/*  414 */     APPLICATION_ID("application_id"),
/*      */ 
/*      */     
/*  417 */     LIMIT_LENGTH("limit_length", "The maximum size of any string or BLOB or table row, in bytes.", null),
/*      */ 
/*      */ 
/*      */     
/*  421 */     LIMIT_SQL_LENGTH("limit_sql_length", "The maximum length of an SQL statement, in bytes.", null),
/*      */     
/*  423 */     LIMIT_COLUMN("limit_column", "The maximum number of columns in a table definition or in the result set of a SELECT or the maximum number of columns in an index or in an ORDER BY or GROUP BY clause.", null),
/*      */ 
/*      */ 
/*      */     
/*  427 */     LIMIT_EXPR_DEPTH("limit_expr_depth", "The maximum depth of the parse tree on any expression.", null),
/*      */     
/*  429 */     LIMIT_COMPOUND_SELECT("limit_compound_select", "The maximum number of terms in a compound SELECT statement.", null),
/*      */ 
/*      */ 
/*      */     
/*  433 */     LIMIT_VDBE_OP("limit_vdbe_op", "The maximum number of instructions in a virtual machine program used to implement an SQL statement. If sqlite3_prepare_v2() or the equivalent tries to allocate space for more than this many opcodes in a single prepared statement, an SQLITE_NOMEM error is returned.", null),
/*      */ 
/*      */ 
/*      */     
/*  437 */     LIMIT_FUNCTION_ARG("limit_function_arg", "The maximum number of arguments on a function.", null),
/*      */     
/*  439 */     LIMIT_ATTACHED("limit_attached", "The maximum number of attached databases.", null),
/*  440 */     LIMIT_LIKE_PATTERN_LENGTH("limit_like_pattern_length", "The maximum length of the pattern argument to the LIKE or GLOB operators.", null),
/*      */ 
/*      */ 
/*      */     
/*  444 */     LIMIT_VARIABLE_NUMBER("limit_variable_number", "The maximum index number of any parameter in an SQL statement.", null),
/*      */ 
/*      */ 
/*      */     
/*  448 */     LIMIT_TRIGGER_DEPTH("limit_trigger_depth", "The maximum depth of recursion for triggers.", null),
/*      */     
/*  450 */     LIMIT_WORKER_THREADS("limit_worker_threads", "The maximum number of auxiliary worker threads that a single prepared statement may start.", null),
/*      */ 
/*      */ 
/*      */     
/*  454 */     LIMIT_PAGE_COUNT("limit_page_count", "The maximum number of pages allowed in a single database file.", null),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  460 */     TRANSACTION_MODE("transaction_mode", SQLiteConfig.toStringArray((SQLiteConfig.PragmaValue[])SQLiteConfig.TransactionMode.values())),
/*  461 */     DATE_PRECISION("date_precision", "\"seconds\": Read and store integer dates as seconds from the Unix Epoch (SQLite standard).\n\"milliseconds\": (DEFAULT) Read and store integer dates as milliseconds from the Unix Epoch (Java standard).", (String)SQLiteConfig
/*      */ 
/*      */       
/*  464 */       .toStringArray((SQLiteConfig.PragmaValue[])SQLiteConfig.DatePrecision.values())),
/*  465 */     DATE_CLASS("date_class", "\"integer\": (Default) store dates as number of seconds or milliseconds from the Unix Epoch\n\"text\": store dates as a string of text\n\"real\": store dates as Julian Dates", (String)SQLiteConfig
/*      */ 
/*      */       
/*  468 */       .toStringArray((SQLiteConfig.PragmaValue[])SQLiteConfig.DateClass.values())),
/*  469 */     DATE_STRING_FORMAT("date_string_format", "Format to store and retrieve dates stored as text. Defaults to \"yyyy-MM-dd HH:mm:ss.SSS\"", null),
/*      */ 
/*      */ 
/*      */     
/*  473 */     BUSY_TIMEOUT("busy_timeout", null),
/*  474 */     HEXKEY_MODE("hexkey_mode", SQLiteConfig.toStringArray((SQLiteConfig.PragmaValue[])SQLiteConfig.HexKeyMode.values())),
/*  475 */     PASSWORD("password", null),
/*      */ 
/*      */     
/*  478 */     JDBC_EXPLICIT_READONLY("jdbc.explicit_readonly");
/*      */ 
/*      */ 
/*      */     
/*      */     public final String pragmaName;
/*      */ 
/*      */     
/*      */     public final String[] choices;
/*      */ 
/*      */     
/*      */     public final String description;
/*      */ 
/*      */ 
/*      */     
/*      */     Pragma(String pragmaName, String description, String[] choices) {
/*  493 */       this.pragmaName = pragmaName;
/*  494 */       this.description = description;
/*  495 */       this.choices = choices;
/*      */     }
/*      */     
/*      */     public final String getPragmaName() {
/*  499 */       return this.pragmaName;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOpenMode(SQLiteOpenMode mode) {
/*  511 */     this.openModeFlag |= mode.flag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetOpenMode(SQLiteOpenMode mode) {
/*  522 */     this.openModeFlag &= mode.flag ^ 0xFFFFFFFF;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSharedCache(boolean enable) {
/*  534 */     set(Pragma.SHARED_CACHE, enable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enableLoadExtension(boolean enable) {
/*  545 */     set(Pragma.LOAD_EXTENSION, enable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReadOnly(boolean readOnly) {
/*  554 */     if (readOnly) {
/*  555 */       setOpenMode(SQLiteOpenMode.READONLY);
/*  556 */       resetOpenMode(SQLiteOpenMode.CREATE);
/*  557 */       resetOpenMode(SQLiteOpenMode.READWRITE);
/*      */     } else {
/*  559 */       setOpenMode(SQLiteOpenMode.READWRITE);
/*  560 */       setOpenMode(SQLiteOpenMode.CREATE);
/*  561 */       resetOpenMode(SQLiteOpenMode.READONLY);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCacheSize(int numberOfPages) {
/*  574 */     set(Pragma.CACHE_SIZE, numberOfPages);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enableCaseSensitiveLike(boolean enable) {
/*  585 */     set(Pragma.CASE_SENSITIVE_LIKE, enable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void enableCountChanges(boolean enable) {
/*  597 */     set(Pragma.COUNT_CHANGES, enable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDefaultCacheSize(int numberOfPages) {
/*  609 */     set(Pragma.DEFAULT_CACHE_SIZE, numberOfPages);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void deferForeignKeys(boolean enable) {
/*  620 */     set(Pragma.DEFER_FOREIGN_KEYS, enable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void enableEmptyResultCallBacks(boolean enable) {
/*  631 */     set(Pragma.EMPTY_RESULT_CALLBACKS, enable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static interface PragmaValue
/*      */   {
/*      */     String getValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String[] toStringArray(PragmaValue[] list) {
/*  650 */     String[] result = new String[list.length];
/*  651 */     for (int i = 0; i < list.length; i++) {
/*  652 */       result[i] = list[i].getValue();
/*      */     }
/*  654 */     return result;
/*      */   }
/*      */   
/*      */   public enum Encoding implements PragmaValue {
/*  658 */     UTF8("'UTF-8'"),
/*  659 */     UTF16("'UTF-16'"),
/*  660 */     UTF16_LITTLE_ENDIAN("'UTF-16le'"),
/*  661 */     UTF16_BIG_ENDIAN("'UTF-16be'"),
/*  662 */     UTF_8((String)UTF8),
/*  663 */     UTF_16((String)UTF16),
/*  664 */     UTF_16LE((String)UTF16_LITTLE_ENDIAN),
/*  665 */     UTF_16BE((String)UTF16_BIG_ENDIAN);
/*      */     
/*      */     public final String typeName;
/*      */     
/*      */     Encoding(String typeName) {
/*  670 */       this.typeName = typeName;
/*      */     }
/*      */     
/*      */     Encoding(Encoding encoding) {
/*  674 */       this.typeName = encoding.getValue();
/*      */     }
/*      */     
/*      */     public String getValue() {
/*  678 */       return this.typeName;
/*      */     }
/*      */     
/*      */     public static Encoding getEncoding(String value) {
/*  682 */       return valueOf(value.replaceAll("-", "_").toUpperCase());
/*      */     }
/*      */   }
/*      */   
/*      */   public enum JournalMode implements PragmaValue {
/*  687 */     DELETE,
/*  688 */     TRUNCATE,
/*  689 */     PERSIST,
/*  690 */     MEMORY,
/*  691 */     WAL,
/*  692 */     OFF;
/*      */     
/*      */     public String getValue() {
/*  695 */       return name();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEncoding(Encoding encoding) {
/*  707 */     setPragma(Pragma.ENCODING, encoding.typeName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enforceForeignKeys(boolean enforce) {
/*  720 */     set(Pragma.FOREIGN_KEYS, enforce);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void enableFullColumnNames(boolean enable) {
/*  733 */     set(Pragma.FULL_COLUMN_NAMES, enable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enableFullSync(boolean enable) {
/*  746 */     set(Pragma.FULL_SYNC, enable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void incrementalVacuum(int numberOfPagesToBeRemoved) {
/*  759 */     set(Pragma.INCREMENTAL_VACUUM, numberOfPagesToBeRemoved);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJournalMode(JournalMode mode) {
/*  770 */     setPragma(Pragma.JOURNAL_MODE, mode.name());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJounalSizeLimit(int limit) {
/*  782 */     set(Pragma.JOURNAL_SIZE_LIMIT, limit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void useLegacyFileFormat(boolean use) {
/*  796 */     set(Pragma.LEGACY_FILE_FORMAT, use);
/*      */   }
/*      */   
/*      */   public enum LockingMode implements PragmaValue {
/*  800 */     NORMAL,
/*  801 */     EXCLUSIVE;
/*      */     
/*      */     public String getValue() {
/*  804 */       return name();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLockingMode(LockingMode mode) {
/*  816 */     setPragma(Pragma.LOCKING_MODE, mode.name());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPageSize(int numBytes) {
/*  832 */     set(Pragma.PAGE_SIZE, numBytes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxPageCount(int numPages) {
/*  843 */     set(Pragma.MAX_PAGE_COUNT, numPages);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReadUncommited(boolean useReadUncommitedIsolationMode) {
/*  854 */     set(Pragma.READ_UNCOMMITTED, useReadUncommitedIsolationMode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enableRecursiveTriggers(boolean enable) {
/*  865 */     set(Pragma.RECURSIVE_TRIGGERS, enable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enableReverseUnorderedSelects(boolean enable) {
/*  879 */     set(Pragma.REVERSE_UNORDERED_SELECTS, enable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enableShortColumnNames(boolean enable) {
/*  891 */     set(Pragma.SHORT_COLUMN_NAMES, enable);
/*      */   }
/*      */   
/*      */   public enum SynchronousMode implements PragmaValue {
/*  895 */     OFF,
/*  896 */     NORMAL,
/*  897 */     FULL;
/*      */     
/*      */     public String getValue() {
/*  900 */       return name();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSynchronous(SynchronousMode mode) {
/*  923 */     setPragma(Pragma.SYNCHRONOUS, mode.name());
/*      */   }
/*      */   
/*      */   public enum TempStore implements PragmaValue {
/*  927 */     DEFAULT,
/*  928 */     FILE,
/*  929 */     MEMORY;
/*      */     
/*      */     public String getValue() {
/*  932 */       return name();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHexKeyMode(HexKeyMode mode) {
/*  947 */     setPragma(Pragma.HEXKEY_MODE, mode.name());
/*      */   }
/*      */   
/*      */   public enum HexKeyMode implements PragmaValue {
/*  951 */     NONE,
/*  952 */     SSE,
/*  953 */     SQLCIPHER;
/*      */     
/*      */     public String getValue() {
/*  956 */       return name();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTempStore(TempStore storeType) {
/*  975 */     setPragma(Pragma.TEMP_STORE, storeType.name());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTempStoreDirectory(String directoryName) {
/*  987 */     setPragma(Pragma.TEMP_STORE_DIRECTORY, String.format("'%s'", new Object[] { directoryName }));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUserVersion(int version) {
/* 1000 */     set(Pragma.USER_VERSION, version);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setApplicationId(int id) {
/* 1014 */     set(Pragma.APPLICATION_ID, id);
/*      */   }
/*      */   
/*      */   public enum TransactionMode
/*      */     implements PragmaValue {
/* 1019 */     DEFFERED,
/*      */     
/* 1021 */     DEFERRED,
/* 1022 */     IMMEDIATE,
/* 1023 */     EXCLUSIVE;
/*      */     
/*      */     public String getValue() {
/* 1026 */       return name();
/*      */     }
/*      */     
/*      */     public static TransactionMode getMode(String mode) {
/* 1030 */       if ("DEFFERED".equalsIgnoreCase(mode)) {
/* 1031 */         return DEFERRED;
/*      */       }
/* 1033 */       return valueOf(mode.toUpperCase());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTransactionMode(TransactionMode transactionMode) {
/* 1045 */     this.defaultConnectionConfig.setTransactionMode(transactionMode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTransactionMode(String transactionMode) {
/* 1056 */     setTransactionMode(TransactionMode.getMode(transactionMode));
/*      */   }
/*      */ 
/*      */   
/*      */   public TransactionMode getTransactionMode() {
/* 1061 */     return this.defaultConnectionConfig.getTransactionMode();
/*      */   }
/*      */   
/*      */   public enum DatePrecision implements PragmaValue {
/* 1065 */     SECONDS,
/* 1066 */     MILLISECONDS;
/*      */     
/*      */     public String getValue() {
/* 1069 */       return name();
/*      */     }
/*      */     
/*      */     public static DatePrecision getPrecision(String precision) {
/* 1073 */       return valueOf(precision.toUpperCase());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDatePrecision(String datePrecision) {
/* 1079 */     this.defaultConnectionConfig.setDatePrecision(DatePrecision.getPrecision(datePrecision));
/*      */   }
/*      */   
/*      */   public enum DateClass implements PragmaValue {
/* 1083 */     INTEGER,
/* 1084 */     TEXT,
/* 1085 */     REAL;
/*      */     
/*      */     public String getValue() {
/* 1088 */       return name();
/*      */     }
/*      */     
/*      */     public static DateClass getDateClass(String dateClass) {
/* 1092 */       return valueOf(dateClass.toUpperCase());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDateClass(String dateClass) {
/* 1098 */     this.defaultConnectionConfig.setDateClass(DateClass.getDateClass(dateClass));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDateStringFormat(String dateStringFormat) {
/* 1104 */     this.defaultConnectionConfig.setDateStringFormat(dateStringFormat);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBusyTimeout(int milliseconds) {
/* 1109 */     setPragma(Pragma.BUSY_TIMEOUT, Integer.toString(milliseconds));
/*      */   }
/*      */   
/*      */   public int getBusyTimeout() {
/* 1113 */     return this.busyTimeout;
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\SQLiteConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */