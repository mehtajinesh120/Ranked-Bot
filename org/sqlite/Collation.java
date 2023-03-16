/*    */ package org.sqlite;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
/*    */ import org.sqlite.core.DB;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Collation
/*    */ {
/*    */   private SQLiteConnection conn;
/*    */   private DB db;
/*    */   
/*    */   public static final void create(Connection conn, String name, Collation f) throws SQLException {
/* 55 */     if (conn == null || !(conn instanceof SQLiteConnection)) {
/* 56 */       throw new SQLException("connection must be to an SQLite db");
/*    */     }
/* 58 */     if (conn.isClosed()) {
/* 59 */       throw new SQLException("connection closed");
/*    */     }
/*    */     
/* 62 */     f.conn = (SQLiteConnection)conn;
/* 63 */     f.db = f.conn.getDatabase();
/*    */     
/* 65 */     if (f.db.create_collation(name, f) != 0) {
/* 66 */       throw new SQLException("error creating collation");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final void destroy(Connection conn, String name) throws SQLException {
/* 78 */     if (conn == null || !(conn instanceof SQLiteConnection)) {
/* 79 */       throw new SQLException("connection must be to an SQLite db");
/*    */     }
/* 81 */     ((SQLiteConnection)conn).getDatabase().destroy_collation(name);
/*    */   }
/*    */   
/*    */   protected abstract int xCompare(String paramString1, String paramString2);
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\Collation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */