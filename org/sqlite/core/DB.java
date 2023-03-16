/*      */ package org.sqlite.core;
/*      */ 
/*      */ import java.sql.BatchUpdateException;
/*      */ import java.sql.SQLException;
/*      */ import java.util.HashSet;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import org.sqlite.BusyHandler;
/*      */ import org.sqlite.Collation;
/*      */ import org.sqlite.Function;
/*      */ import org.sqlite.ProgressHandler;
/*      */ import org.sqlite.SQLiteCommitListener;
/*      */ import org.sqlite.SQLiteConfig;
/*      */ import org.sqlite.SQLiteErrorCode;
/*      */ import org.sqlite.SQLiteException;
/*      */ import org.sqlite.SQLiteUpdateListener;
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
/*      */ public abstract class DB
/*      */   implements Codes
/*      */ {
/*      */   private final String url;
/*      */   private final String fileName;
/*      */   private final SQLiteConfig config;
/*   50 */   private final AtomicBoolean closed = new AtomicBoolean(true);
/*      */ 
/*      */   
/*      */   volatile SafeStmtPtr begin;
/*      */ 
/*      */   
/*      */   volatile SafeStmtPtr commit;
/*      */   
/*   58 */   private final Set<SafeStmtPtr> stmts = ConcurrentHashMap.newKeySet();
/*      */   
/*   60 */   private final Set<SQLiteUpdateListener> updateListeners = new HashSet<>();
/*   61 */   private final Set<SQLiteCommitListener> commitListeners = new HashSet<>();
/*      */   
/*      */   public DB(String url, String fileName, SQLiteConfig config) throws SQLException {
/*   64 */     this.url = url;
/*   65 */     this.fileName = fileName;
/*   66 */     this.config = config;
/*      */   }
/*      */   
/*      */   public String getUrl() {
/*   70 */     return this.url;
/*      */   }
/*      */   
/*      */   public boolean isClosed() {
/*   74 */     return this.closed.get();
/*      */   }
/*      */   
/*      */   public SQLiteConfig getConfig() {
/*   78 */     return this.config;
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
/*      */   public final synchronized void exec(String sql, boolean autoCommit) throws SQLException {
/*  188 */     SafeStmtPtr pointer = prepare(sql);
/*      */     try {
/*  190 */       int rc = pointer.safeRunInt(DB::step);
/*  191 */       switch (rc) {
/*      */         case 101:
/*  193 */           ensureAutoCommit(autoCommit);
/*      */           return;
/*      */         case 100:
/*      */           return;
/*      */       } 
/*  198 */       throwex(rc);
/*      */     } finally {
/*      */       
/*  201 */       pointer.close();
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
/*      */   public final synchronized void open(String file, int openFlags) throws SQLException {
/*  216 */     _open(file, openFlags);
/*  217 */     this.closed.set(false);
/*      */     
/*  219 */     if (this.fileName.startsWith("file:") && !this.fileName.contains("cache="))
/*      */     {
/*  221 */       shared_cache(this.config.isEnabledSharedCache());
/*      */     }
/*  223 */     enable_load_extension(this.config.isEnabledLoadExtension());
/*  224 */     busy_timeout(this.config.getBusyTimeout());
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
/*      */   public final synchronized void close() throws SQLException {
/*  237 */     for (SafeStmtPtr element : this.stmts) {
/*  238 */       element.close();
/*      */     }
/*      */ 
/*      */     
/*  242 */     if (this.begin != null) this.begin.close(); 
/*  243 */     if (this.commit != null) this.commit.close();
/*      */     
/*  245 */     this.closed.set(true);
/*  246 */     _close();
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
/*      */   public final synchronized void prepare(CoreStatement stmt) throws SQLException {
/*  258 */     if (stmt.sql == null) {
/*  259 */       throw new NullPointerException();
/*      */     }
/*  261 */     if (stmt.pointer != null) {
/*  262 */       stmt.pointer.close();
/*      */     }
/*  264 */     stmt.pointer = prepare(stmt.sql);
/*  265 */     boolean added = this.stmts.add(stmt.pointer);
/*  266 */     if (!added) {
/*  267 */       throw new IllegalStateException("Already added pointer to statements set");
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
/*      */   public synchronized int finalize(SafeStmtPtr safePtr, long ptr) throws SQLException {
/*      */     try {
/*  283 */       return finalize(ptr);
/*      */     } finally {
/*  285 */       this.stmts.remove(safePtr);
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
/*      */   public final synchronized String[] column_names(long stmt) throws SQLException {
/*  818 */     String[] names = new String[column_count(stmt)];
/*  819 */     for (int i = 0; i < names.length; i++) {
/*  820 */       names[i] = column_name(stmt, i);
/*      */     }
/*  822 */     return names;
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
/*      */   final synchronized int sqlbind(long stmt, int pos, Object v) throws SQLException {
/*  837 */     pos++;
/*  838 */     if (v == null)
/*  839 */       return bind_null(stmt, pos); 
/*  840 */     if (v instanceof Integer)
/*  841 */       return bind_int(stmt, pos, ((Integer)v).intValue()); 
/*  842 */     if (v instanceof Short)
/*  843 */       return bind_int(stmt, pos, ((Short)v).intValue()); 
/*  844 */     if (v instanceof Long)
/*  845 */       return bind_long(stmt, pos, ((Long)v).longValue()); 
/*  846 */     if (v instanceof Float)
/*  847 */       return bind_double(stmt, pos, ((Float)v).doubleValue()); 
/*  848 */     if (v instanceof Double)
/*  849 */       return bind_double(stmt, pos, ((Double)v).doubleValue()); 
/*  850 */     if (v instanceof String)
/*  851 */       return bind_text(stmt, pos, (String)v); 
/*  852 */     if (v instanceof byte[]) {
/*  853 */       return bind_blob(stmt, pos, (byte[])v);
/*      */     }
/*  855 */     throw new SQLException("unexpected param type: " + v.getClass());
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
/*      */   final synchronized long[] executeBatch(SafeStmtPtr stmt, int count, Object[] vals, boolean autoCommit) throws SQLException {
/*  872 */     return stmt.<long[], Throwable>safeRun((db, ptr) -> executeBatch(ptr, count, vals, autoCommit));
/*      */   }
/*      */ 
/*      */   
/*      */   private synchronized long[] executeBatch(long stmt, int count, Object[] vals, boolean autoCommit) throws SQLException {
/*  877 */     if (count < 1) {
/*  878 */       throw new SQLException("count (" + count + ") < 1");
/*      */     }
/*      */     
/*  881 */     int params = bind_parameter_count(stmt);
/*      */ 
/*      */     
/*  884 */     long[] changes = new long[count];
/*      */     
/*      */     try {
/*  887 */       for (int i = 0; i < count; i++) {
/*  888 */         reset(stmt);
/*  889 */         for (int j = 0; j < params; j++) {
/*  890 */           int k = sqlbind(stmt, j, vals[i * params + j]);
/*  891 */           if (k != 0) {
/*  892 */             throwex(k);
/*      */           }
/*      */         } 
/*      */         
/*  896 */         int rc = step(stmt);
/*  897 */         if (rc != 101) {
/*  898 */           reset(stmt);
/*  899 */           if (rc == 100) {
/*  900 */             throw new BatchUpdateException("batch entry " + i + ": query returns results", null, 0, changes, null);
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  907 */           throwex(rc);
/*      */         } 
/*      */         
/*  910 */         changes[i] = changes();
/*      */       } 
/*      */     } finally {
/*  913 */       ensureAutoCommit(autoCommit);
/*      */     } 
/*      */     
/*  916 */     reset(stmt);
/*  917 */     return changes;
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
/*      */   public final synchronized boolean execute(CoreStatement stmt, Object[] vals) throws SQLException {
/*  930 */     int statusCode = stmt.pointer.safeRunInt((db, ptr) -> execute(ptr, vals));
/*  931 */     switch (statusCode & 0xFF) {
/*      */       case 101:
/*  933 */         ensureAutoCommit(stmt.conn.getAutoCommit());
/*  934 */         return false;
/*      */       case 100:
/*  936 */         return true;
/*      */       case 5:
/*      */       case 6:
/*      */       case 19:
/*      */       case 21:
/*  941 */         throw newSQLException(statusCode);
/*      */     } 
/*  943 */     stmt.pointer.close();
/*  944 */     throw newSQLException(statusCode);
/*      */   }
/*      */ 
/*      */   
/*      */   private synchronized int execute(long ptr, Object[] vals) throws SQLException {
/*  949 */     if (vals != null) {
/*  950 */       int params = bind_parameter_count(ptr);
/*  951 */       if (params > vals.length) {
/*  952 */         throw new SQLException("assertion failure: param count (" + params + ") > value count (" + vals.length + ")");
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  960 */       for (int i = 0; i < params; i++) {
/*  961 */         int rc = sqlbind(ptr, i, vals[i]);
/*  962 */         if (rc != 0) {
/*  963 */           throwex(rc);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  968 */     int statusCode = step(ptr);
/*  969 */     if ((statusCode & 0xFF) == 101) reset(ptr); 
/*  970 */     return statusCode;
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
/*      */   final synchronized boolean execute(String sql, boolean autoCommit) throws SQLException {
/*  983 */     int statusCode = _exec(sql);
/*  984 */     switch (statusCode) {
/*      */       case 0:
/*  986 */         return false;
/*      */       case 101:
/*  988 */         ensureAutoCommit(autoCommit);
/*  989 */         return false;
/*      */       case 100:
/*  991 */         return true;
/*      */     } 
/*  993 */     throw newSQLException(statusCode);
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
/*      */   public final synchronized long executeUpdate(CoreStatement stmt, Object[] vals) throws SQLException {
/*      */     try {
/* 1010 */       if (execute(stmt, vals)) {
/* 1011 */         throw new SQLException("query returns results");
/*      */       }
/*      */     } finally {
/* 1014 */       if (!stmt.pointer.isClosed()) {
/* 1015 */         stmt.pointer.safeRunInt(DB::reset);
/*      */       }
/*      */     } 
/* 1018 */     return changes();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void addUpdateListener(SQLiteUpdateListener listener) {
/* 1026 */     if (this.updateListeners.add(listener) && this.updateListeners.size() == 1) {
/* 1027 */       set_update_listener(true);
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized void addCommitListener(SQLiteCommitListener listener) {
/* 1032 */     if (this.commitListeners.add(listener) && this.commitListeners.size() == 1) {
/* 1033 */       set_commit_listener(true);
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized void removeUpdateListener(SQLiteUpdateListener listener) {
/* 1038 */     if (this.updateListeners.remove(listener) && this.updateListeners.isEmpty()) {
/* 1039 */       set_update_listener(false);
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized void removeCommitListener(SQLiteCommitListener listener) {
/* 1044 */     if (this.commitListeners.remove(listener) && this.commitListeners.isEmpty()) {
/* 1045 */       set_commit_listener(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void onUpdate(int type, String database, String table, long rowId) {
/*      */     Set<SQLiteUpdateListener> listeners;
/* 1052 */     synchronized (this) {
/* 1053 */       listeners = new HashSet<>(this.updateListeners);
/*      */     } 
/*      */     
/* 1056 */     for (SQLiteUpdateListener listener : listeners) {
/*      */       SQLiteUpdateListener.Type operationType;
/*      */       
/* 1059 */       switch (type) {
/*      */         case 18:
/* 1061 */           operationType = SQLiteUpdateListener.Type.INSERT;
/*      */           break;
/*      */         case 9:
/* 1064 */           operationType = SQLiteUpdateListener.Type.DELETE;
/*      */           break;
/*      */         case 23:
/* 1067 */           operationType = SQLiteUpdateListener.Type.UPDATE;
/*      */           break;
/*      */         default:
/* 1070 */           throw new AssertionError("Unknown type: " + type);
/*      */       } 
/*      */       
/* 1073 */       listener.onUpdate(operationType, database, table, rowId);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void onCommit(boolean commit) {
/*      */     Set<SQLiteCommitListener> listeners;
/* 1080 */     synchronized (this) {
/* 1081 */       listeners = new HashSet<>(this.commitListeners);
/*      */     } 
/*      */     
/* 1084 */     for (SQLiteCommitListener listener : listeners) {
/* 1085 */       if (commit) { listener.onCommit(); continue; }
/* 1086 */        listener.onRollback();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void throwex() throws SQLException {
/* 1096 */     throw new SQLException(errmsg());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void throwex(int errorCode) throws SQLException {
/* 1106 */     throw newSQLException(errorCode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void throwex(int errorCode, String errorMessage) throws SQLException {
/* 1117 */     throw newSQLException(errorCode, errorMessage);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static SQLiteException newSQLException(int errorCode, String errorMessage) {
/*      */     String msg;
/* 1128 */     SQLiteErrorCode code = SQLiteErrorCode.getErrorCode(errorCode);
/*      */     
/* 1130 */     if (code == SQLiteErrorCode.UNKNOWN_ERROR) {
/* 1131 */       msg = String.format("%s:%s (%s)", new Object[] { code, Integer.valueOf(errorCode), errorMessage });
/*      */     } else {
/* 1133 */       msg = String.format("%s (%s)", new Object[] { code, errorMessage });
/*      */     } 
/* 1135 */     return new SQLiteException(msg, code);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private SQLiteException newSQLException(int errorCode) throws SQLException {
/* 1146 */     return newSQLException(errorCode, errmsg());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void ensureAutoCommit(boolean autoCommit) throws SQLException {
/* 1173 */     if (!autoCommit) {
/*      */       return;
/*      */     }
/*      */     
/* 1177 */     ensureBeginAndCommit();
/*      */     
/* 1179 */     this.begin.safeRunConsume((db, beginPtr) -> this.commit.safeRunConsume(()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void ensureBeginAndCommit() throws SQLException {
/* 1187 */     if (this.begin == null) {
/* 1188 */       synchronized (this) {
/* 1189 */         if (this.begin == null) {
/* 1190 */           this.begin = prepare("begin;");
/*      */         }
/*      */       } 
/*      */     }
/* 1194 */     if (this.commit == null) {
/* 1195 */       synchronized (this) {
/* 1196 */         if (this.commit == null) {
/* 1197 */           this.commit = prepare("commit;");
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private void ensureAutocommit(long beginPtr, long commitPtr) throws SQLException {
/*      */     try {
/* 1205 */       if (step(beginPtr) != 101) {
/*      */         return;
/*      */       }
/* 1208 */       int rc = step(commitPtr);
/* 1209 */       if (rc != 101) {
/* 1210 */         reset(commitPtr);
/* 1211 */         throwex(rc);
/*      */       } 
/*      */     } finally {
/*      */       
/* 1215 */       reset(beginPtr);
/* 1216 */       reset(commitPtr);
/*      */     } 
/*      */   }
/*      */   
/*      */   public abstract void interrupt() throws SQLException;
/*      */   
/*      */   public abstract void busy_timeout(int paramInt) throws SQLException;
/*      */   
/*      */   public abstract void busy_handler(BusyHandler paramBusyHandler) throws SQLException;
/*      */   
/*      */   abstract String errmsg() throws SQLException;
/*      */   
/*      */   public abstract String libversion() throws SQLException;
/*      */   
/*      */   public abstract long changes() throws SQLException;
/*      */   
/*      */   public abstract long total_changes() throws SQLException;
/*      */   
/*      */   public abstract int shared_cache(boolean paramBoolean) throws SQLException;
/*      */   
/*      */   public abstract int enable_load_extension(boolean paramBoolean) throws SQLException;
/*      */   
/*      */   protected abstract void _open(String paramString, int paramInt) throws SQLException;
/*      */   
/*      */   protected abstract void _close() throws SQLException;
/*      */   
/*      */   public abstract int _exec(String paramString) throws SQLException;
/*      */   
/*      */   protected abstract SafeStmtPtr prepare(String paramString) throws SQLException;
/*      */   
/*      */   protected abstract int finalize(long paramLong) throws SQLException;
/*      */   
/*      */   public abstract int step(long paramLong) throws SQLException;
/*      */   
/*      */   public abstract int reset(long paramLong) throws SQLException;
/*      */   
/*      */   public abstract int clear_bindings(long paramLong) throws SQLException;
/*      */   
/*      */   abstract int bind_parameter_count(long paramLong) throws SQLException;
/*      */   
/*      */   public abstract int column_count(long paramLong) throws SQLException;
/*      */   
/*      */   public abstract int column_type(long paramLong, int paramInt) throws SQLException;
/*      */   
/*      */   public abstract String column_decltype(long paramLong, int paramInt) throws SQLException;
/*      */   
/*      */   public abstract String column_table_name(long paramLong, int paramInt) throws SQLException;
/*      */   
/*      */   public abstract String column_name(long paramLong, int paramInt) throws SQLException;
/*      */   
/*      */   public abstract String column_text(long paramLong, int paramInt) throws SQLException;
/*      */   
/*      */   public abstract byte[] column_blob(long paramLong, int paramInt) throws SQLException;
/*      */   
/*      */   public abstract double column_double(long paramLong, int paramInt) throws SQLException;
/*      */   
/*      */   public abstract long column_long(long paramLong, int paramInt) throws SQLException;
/*      */   
/*      */   public abstract int column_int(long paramLong, int paramInt) throws SQLException;
/*      */   
/*      */   abstract int bind_null(long paramLong, int paramInt) throws SQLException;
/*      */   
/*      */   abstract int bind_int(long paramLong, int paramInt1, int paramInt2) throws SQLException;
/*      */   
/*      */   abstract int bind_long(long paramLong1, int paramInt, long paramLong2) throws SQLException;
/*      */   
/*      */   abstract int bind_double(long paramLong, int paramInt, double paramDouble) throws SQLException;
/*      */   
/*      */   abstract int bind_text(long paramLong, int paramInt, String paramString) throws SQLException;
/*      */   
/*      */   abstract int bind_blob(long paramLong, int paramInt, byte[] paramArrayOfbyte) throws SQLException;
/*      */   
/*      */   public abstract void result_null(long paramLong) throws SQLException;
/*      */   
/*      */   public abstract void result_text(long paramLong, String paramString) throws SQLException;
/*      */   
/*      */   public abstract void result_blob(long paramLong, byte[] paramArrayOfbyte) throws SQLException;
/*      */   
/*      */   public abstract void result_double(long paramLong, double paramDouble) throws SQLException;
/*      */   
/*      */   public abstract void result_long(long paramLong1, long paramLong2) throws SQLException;
/*      */   
/*      */   public abstract void result_int(long paramLong, int paramInt) throws SQLException;
/*      */   
/*      */   public abstract void result_error(long paramLong, String paramString) throws SQLException;
/*      */   
/*      */   public abstract String value_text(Function paramFunction, int paramInt) throws SQLException;
/*      */   
/*      */   public abstract byte[] value_blob(Function paramFunction, int paramInt) throws SQLException;
/*      */   
/*      */   public abstract double value_double(Function paramFunction, int paramInt) throws SQLException;
/*      */   
/*      */   public abstract long value_long(Function paramFunction, int paramInt) throws SQLException;
/*      */   
/*      */   public abstract int value_int(Function paramFunction, int paramInt) throws SQLException;
/*      */   
/*      */   public abstract int value_type(Function paramFunction, int paramInt) throws SQLException;
/*      */   
/*      */   public abstract int create_function(String paramString, Function paramFunction, int paramInt1, int paramInt2) throws SQLException;
/*      */   
/*      */   public abstract int destroy_function(String paramString) throws SQLException;
/*      */   
/*      */   public abstract int create_collation(String paramString, Collation paramCollation) throws SQLException;
/*      */   
/*      */   public abstract int destroy_collation(String paramString) throws SQLException;
/*      */   
/*      */   public abstract int backup(String paramString1, String paramString2, ProgressObserver paramProgressObserver) throws SQLException;
/*      */   
/*      */   public abstract int restore(String paramString1, String paramString2, ProgressObserver paramProgressObserver) throws SQLException;
/*      */   
/*      */   public abstract int limit(int paramInt1, int paramInt2) throws SQLException;
/*      */   
/*      */   public abstract void register_progress_handler(int paramInt, ProgressHandler paramProgressHandler) throws SQLException;
/*      */   
/*      */   public abstract void clear_progress_handler() throws SQLException;
/*      */   
/*      */   abstract boolean[][] column_metadata(long paramLong) throws SQLException;
/*      */   
/*      */   abstract void set_commit_listener(boolean paramBoolean);
/*      */   
/*      */   abstract void set_update_listener(boolean paramBoolean);
/*      */   
/*      */   public static interface ProgressObserver {
/*      */     void progress(int param1Int1, int param1Int2);
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\core\DB.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */