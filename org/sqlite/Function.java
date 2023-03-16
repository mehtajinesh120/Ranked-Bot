/*     */ package org.sqlite;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import org.sqlite.core.DB;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Function
/*     */ {
/*     */   public static final int FLAG_DETERMINISTIC = 2048;
/*     */   private SQLiteConnection conn;
/*     */   private DB db;
/*  58 */   long context = 0L;
/*  59 */   long value = 0L;
/*  60 */   int args = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void create(Connection conn, String name, Function f) throws SQLException {
/*  70 */     create(conn, name, f, 0);
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
/*     */   public static void create(Connection conn, String name, Function f, int flags) throws SQLException {
/*  83 */     create(conn, name, f, -1, flags);
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
/*     */   public static void create(Connection conn, String name, Function f, int nArgs, int flags) throws SQLException {
/*  97 */     if (!(conn instanceof SQLiteConnection)) {
/*  98 */       throw new SQLException("connection must be to an SQLite db");
/*     */     }
/* 100 */     if (conn.isClosed()) {
/* 101 */       throw new SQLException("connection closed");
/*     */     }
/*     */     
/* 104 */     f.conn = (SQLiteConnection)conn;
/* 105 */     f.db = f.conn.getDatabase();
/*     */     
/* 107 */     if (nArgs < -1 || nArgs > 127) {
/* 108 */       throw new SQLException("invalid args provided: " + nArgs);
/*     */     }
/*     */     
/* 111 */     if (f.db.create_function(name, f, nArgs, flags) != 0) {
/* 112 */       throw new SQLException("error creating function");
/*     */     }
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
/*     */   public static void destroy(Connection conn, String name, int nArgs) throws SQLException {
/* 125 */     if (!(conn instanceof SQLiteConnection)) {
/* 126 */       throw new SQLException("connection must be to an SQLite db");
/*     */     }
/* 128 */     ((SQLiteConnection)conn).getDatabase().destroy_function(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void destroy(Connection conn, String name) throws SQLException {
/* 139 */     destroy(conn, name, -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void xFunc() throws SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized int args() throws SQLException {
/* 153 */     checkContext();
/* 154 */     return this.args;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized void result(byte[] value) throws SQLException {
/* 163 */     checkContext();
/* 164 */     this.db.result_blob(this.context, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized void result(double value) throws SQLException {
/* 173 */     checkContext();
/* 174 */     this.db.result_double(this.context, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized void result(int value) throws SQLException {
/* 183 */     checkContext();
/* 184 */     this.db.result_int(this.context, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized void result(long value) throws SQLException {
/* 193 */     checkContext();
/* 194 */     this.db.result_long(this.context, value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final synchronized void result() throws SQLException {
/* 199 */     checkContext();
/* 200 */     this.db.result_null(this.context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized void result(String value) throws SQLException {
/* 209 */     checkContext();
/* 210 */     this.db.result_text(this.context, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized void error(String err) throws SQLException {
/* 219 */     checkContext();
/* 220 */     this.db.result_error(this.context, err);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized String value_text(int arg) throws SQLException {
/* 229 */     checkValue(arg);
/* 230 */     return this.db.value_text(this, arg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized byte[] value_blob(int arg) throws SQLException {
/* 239 */     checkValue(arg);
/* 240 */     return this.db.value_blob(this, arg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized double value_double(int arg) throws SQLException {
/* 249 */     checkValue(arg);
/* 250 */     return this.db.value_double(this, arg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized int value_int(int arg) throws SQLException {
/* 259 */     checkValue(arg);
/* 260 */     return this.db.value_int(this, arg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized long value_long(int arg) throws SQLException {
/* 269 */     checkValue(arg);
/* 270 */     return this.db.value_long(this, arg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized int value_type(int arg) throws SQLException {
/* 279 */     checkValue(arg);
/* 280 */     return this.db.value_type(this, arg);
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkContext() throws SQLException {
/* 285 */     if (this.conn == null || this.conn.getDatabase() == null || this.context == 0L) {
/* 286 */       throw new SQLException("no context, not allowed to read value");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkValue(int arg) throws SQLException {
/* 295 */     if (this.conn == null || this.conn.getDatabase() == null || this.value == 0L) {
/* 296 */       throw new SQLException("not in value access state");
/*     */     }
/* 298 */     if (arg >= this.args) {
/* 299 */       throw new SQLException("arg " + arg + " out bounds [0," + this.args + ")");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class Aggregate
/*     */     extends Function
/*     */     implements Cloneable
/*     */   {
/*     */     protected final void xFunc() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract void xStep() throws SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract void xFinal() throws SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object clone() throws CloneNotSupportedException {
/* 332 */       return super.clone();
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract class Window extends Aggregate {
/*     */     protected abstract void xInverse() throws SQLException;
/*     */     
/*     */     protected abstract void xValue() throws SQLException;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\Function.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */