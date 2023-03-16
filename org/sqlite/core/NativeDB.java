/*     */ package org.sqlite.core;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.SQLException;
/*     */ import org.sqlite.BusyHandler;
/*     */ import org.sqlite.Collation;
/*     */ import org.sqlite.Function;
/*     */ import org.sqlite.ProgressHandler;
/*     */ import org.sqlite.SQLiteConfig;
/*     */ import org.sqlite.SQLiteJDBCLoader;
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
/*     */ public final class NativeDB
/*     */   extends DB
/*     */ {
/*  33 */   private long pointer = 0L; private static boolean isLoaded; private static boolean loadSucceeded; private long busyHandler;
/*     */   private long commitListener;
/*     */   private long updateListener;
/*     */   private long progressHandler;
/*     */   
/*     */   static {
/*  39 */     if ("The Android Project".equals(System.getProperty("java.vm.vendor"))) {
/*  40 */       System.loadLibrary("sqlitejdbc");
/*  41 */       isLoaded = true;
/*  42 */       loadSucceeded = true;
/*     */     } else {
/*     */       
/*  45 */       isLoaded = false;
/*  46 */       loadSucceeded = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public NativeDB(String url, String fileName, SQLiteConfig config) throws SQLException {
/*  51 */     super(url, fileName, config);
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
/* 111 */     this.busyHandler = 0L;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 409 */     this.commitListener = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 415 */     this.updateListener = 0L;
/*     */   }
/*     */   public static boolean load() throws Exception { if (isLoaded)
/*     */       return loadSucceeded;  try { loadSucceeded = SQLiteJDBCLoader.initialize(); } finally { isLoaded = true; }
/*     */      return loadSucceeded; }
/*     */   protected synchronized void _open(String file, int openFlags) throws SQLException { _open_utf8(stringToUtf8ByteArray(file), openFlags); }
/*     */   public synchronized int _exec(String sql) throws SQLException { DriverManager.println("DriverManager [" + Thread.currentThread().getName() + "] [SQLite EXEC] " + sql); return _exec_utf8(stringToUtf8ByteArray(sql)); }
/*     */   protected synchronized SafeStmtPtr prepare(String sql) throws SQLException { DriverManager.println("DriverManager [" + Thread.currentThread().getName() + "] [SQLite PREP] " + sql); return new SafeStmtPtr(this, prepare_utf8(stringToUtf8ByteArray(sql))); }
/*     */   synchronized String errmsg() { return utf8ByteBufferToString(errmsg_utf8()); }
/*     */   public synchronized String libversion() { return utf8ByteBufferToString(libversion_utf8()); }
/*     */   public synchronized String column_decltype(long stmt, int col) { return utf8ByteBufferToString(column_decltype_utf8(stmt, col)); }
/*     */   public synchronized String column_table_name(long stmt, int col) { return utf8ByteBufferToString(column_table_name_utf8(stmt, col)); }
/* 427 */   public synchronized String column_name(long stmt, int col) { return utf8ByteBufferToString(column_name_utf8(stmt, col)); } public synchronized String column_text(long stmt, int col) { return utf8ByteBufferToString(column_text_utf8(stmt, col)); } static void throwex(String msg) throws SQLException { throw new SQLException(msg); }
/*     */   synchronized int bind_text(long stmt, int pos, String v) { return bind_text_utf8(stmt, pos, stringToUtf8ByteArray(v)); }
/*     */   public synchronized void result_text(long context, String val) { result_text_utf8(context, stringToUtf8ByteArray(val)); }
/*     */   public synchronized void result_error(long context, String err) { result_error_utf8(context, stringToUtf8ByteArray(err)); }
/* 431 */   public synchronized String value_text(Function f, int arg) { return utf8ByteBufferToString(value_text_utf8(f, arg)); } public synchronized int create_function(String name, Function func, int nArgs, int flags) throws SQLException { return create_function_utf8(nameToUtf8ByteArray("function", name), func, nArgs, flags); } public synchronized int destroy_function(String name) throws SQLException { return destroy_function_utf8(nameToUtf8ByteArray("function", name)); } public synchronized int create_collation(String name, Collation coll) throws SQLException { return create_collation_utf8(nameToUtf8ByteArray("collation", name), coll); } public synchronized int destroy_collation(String name) throws SQLException { return destroy_collation_utf8(nameToUtf8ByteArray("collation", name)); } private byte[] nameToUtf8ByteArray(String nameType, String name) throws SQLException { byte[] nameUtf8 = stringToUtf8ByteArray(name); if (name == null || "".equals(name) || nameUtf8.length > 255) throw new SQLException("invalid " + nameType + " name: '" + name + "'");  return nameUtf8; } public int backup(String dbName, String destFileName, DB.ProgressObserver observer) throws SQLException { return backup(stringToUtf8ByteArray(dbName), stringToUtf8ByteArray(destFileName), observer); } public synchronized int restore(String dbName, String sourceFileName, DB.ProgressObserver observer) throws SQLException { return restore(stringToUtf8ByteArray(dbName), stringToUtf8ByteArray(sourceFileName), observer); } static byte[] stringToUtf8ByteArray(String str) { if (str == null) {
/* 432 */       return null;
/*     */     }
/* 434 */     return str.getBytes(StandardCharsets.UTF_8); }
/*     */ 
/*     */   
/*     */   static String utf8ByteBufferToString(ByteBuffer buffer) {
/* 438 */     if (buffer == null) {
/* 439 */       return null;
/*     */     }
/* 441 */     byte[] buff = new byte[buffer.remaining()];
/* 442 */     buffer.get(buff);
/* 443 */     return new String(buff, StandardCharsets.UTF_8);
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
/*     */   long getBusyHandler() {
/* 460 */     return this.busyHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getCommitListener() {
/* 469 */     return this.commitListener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getUpdateListener() {
/* 478 */     return this.updateListener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getProgressHandler() {
/* 487 */     return this.progressHandler;
/*     */   }
/*     */   
/*     */   synchronized native void _open_utf8(byte[] paramArrayOfbyte, int paramInt) throws SQLException;
/*     */   
/*     */   protected synchronized native void _close() throws SQLException;
/*     */   
/*     */   synchronized native int _exec_utf8(byte[] paramArrayOfbyte) throws SQLException;
/*     */   
/*     */   public synchronized native int shared_cache(boolean paramBoolean);
/*     */   
/*     */   public synchronized native int enable_load_extension(boolean paramBoolean);
/*     */   
/*     */   public native void interrupt();
/*     */   
/*     */   public synchronized native void busy_timeout(int paramInt);
/*     */   
/*     */   public synchronized native void busy_handler(BusyHandler paramBusyHandler);
/*     */   
/*     */   synchronized native long prepare_utf8(byte[] paramArrayOfbyte) throws SQLException;
/*     */   
/*     */   synchronized native ByteBuffer errmsg_utf8();
/*     */   
/*     */   native ByteBuffer libversion_utf8();
/*     */   
/*     */   public synchronized native long changes();
/*     */   
/*     */   public synchronized native long total_changes();
/*     */   
/*     */   protected synchronized native int finalize(long paramLong);
/*     */   
/*     */   public synchronized native int step(long paramLong);
/*     */   
/*     */   public synchronized native int reset(long paramLong);
/*     */   
/*     */   public synchronized native int clear_bindings(long paramLong);
/*     */   
/*     */   synchronized native int bind_parameter_count(long paramLong);
/*     */   
/*     */   public synchronized native int column_count(long paramLong);
/*     */   
/*     */   public synchronized native int column_type(long paramLong, int paramInt);
/*     */   
/*     */   synchronized native ByteBuffer column_decltype_utf8(long paramLong, int paramInt);
/*     */   
/*     */   synchronized native ByteBuffer column_table_name_utf8(long paramLong, int paramInt);
/*     */   
/*     */   synchronized native ByteBuffer column_name_utf8(long paramLong, int paramInt);
/*     */   
/*     */   synchronized native ByteBuffer column_text_utf8(long paramLong, int paramInt);
/*     */   
/*     */   public synchronized native byte[] column_blob(long paramLong, int paramInt);
/*     */   
/*     */   public synchronized native double column_double(long paramLong, int paramInt);
/*     */   
/*     */   public synchronized native long column_long(long paramLong, int paramInt);
/*     */   
/*     */   public synchronized native int column_int(long paramLong, int paramInt);
/*     */   
/*     */   synchronized native int bind_null(long paramLong, int paramInt);
/*     */   
/*     */   synchronized native int bind_int(long paramLong, int paramInt1, int paramInt2);
/*     */   
/*     */   synchronized native int bind_long(long paramLong1, int paramInt, long paramLong2);
/*     */   
/*     */   synchronized native int bind_double(long paramLong, int paramInt, double paramDouble);
/*     */   
/*     */   synchronized native int bind_text_utf8(long paramLong, int paramInt, byte[] paramArrayOfbyte);
/*     */   
/*     */   synchronized native int bind_blob(long paramLong, int paramInt, byte[] paramArrayOfbyte);
/*     */   
/*     */   public synchronized native void result_null(long paramLong);
/*     */   
/*     */   synchronized native void result_text_utf8(long paramLong, byte[] paramArrayOfbyte);
/*     */   
/*     */   public synchronized native void result_blob(long paramLong, byte[] paramArrayOfbyte);
/*     */   
/*     */   public synchronized native void result_double(long paramLong, double paramDouble);
/*     */   
/*     */   public synchronized native void result_long(long paramLong1, long paramLong2);
/*     */   
/*     */   public synchronized native void result_int(long paramLong, int paramInt);
/*     */   
/*     */   synchronized native void result_error_utf8(long paramLong, byte[] paramArrayOfbyte);
/*     */   
/*     */   synchronized native ByteBuffer value_text_utf8(Function paramFunction, int paramInt);
/*     */   
/*     */   public synchronized native byte[] value_blob(Function paramFunction, int paramInt);
/*     */   
/*     */   public synchronized native double value_double(Function paramFunction, int paramInt);
/*     */   
/*     */   public synchronized native long value_long(Function paramFunction, int paramInt);
/*     */   
/*     */   public synchronized native int value_int(Function paramFunction, int paramInt);
/*     */   
/*     */   public synchronized native int value_type(Function paramFunction, int paramInt);
/*     */   
/*     */   synchronized native int create_function_utf8(byte[] paramArrayOfbyte, Function paramFunction, int paramInt1, int paramInt2);
/*     */   
/*     */   synchronized native int destroy_function_utf8(byte[] paramArrayOfbyte);
/*     */   
/*     */   synchronized native int create_collation_utf8(byte[] paramArrayOfbyte, Collation paramCollation);
/*     */   
/*     */   synchronized native int destroy_collation_utf8(byte[] paramArrayOfbyte);
/*     */   
/*     */   public synchronized native int limit(int paramInt1, int paramInt2) throws SQLException;
/*     */   
/*     */   synchronized native int backup(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, DB.ProgressObserver paramProgressObserver) throws SQLException;
/*     */   
/*     */   synchronized native int restore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, DB.ProgressObserver paramProgressObserver) throws SQLException;
/*     */   
/*     */   synchronized native boolean[][] column_metadata(long paramLong);
/*     */   
/*     */   synchronized native void set_commit_listener(boolean paramBoolean);
/*     */   
/*     */   synchronized native void set_update_listener(boolean paramBoolean);
/*     */   
/*     */   public synchronized native void register_progress_handler(int paramInt, ProgressHandler paramProgressHandler) throws SQLException;
/*     */   
/*     */   public synchronized native void clear_progress_handler() throws SQLException;
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\core\NativeDB.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */