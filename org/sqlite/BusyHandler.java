/*    */ package org.sqlite;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
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
/*    */ public abstract class BusyHandler
/*    */ {
/*    */   private static void commitHandler(Connection conn, BusyHandler busyHandler) throws SQLException {
/* 19 */     if (!(conn instanceof SQLiteConnection)) {
/* 20 */       throw new SQLException("connection must be to an SQLite db");
/*    */     }
/*    */     
/* 23 */     if (conn.isClosed()) {
/* 24 */       throw new SQLException("connection closed");
/*    */     }
/*    */     
/* 27 */     SQLiteConnection sqliteConnection = (SQLiteConnection)conn;
/* 28 */     sqliteConnection.getDatabase().busy_handler(busyHandler);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final void setHandler(Connection conn, BusyHandler busyHandler) throws SQLException {
/* 40 */     commitHandler(conn, busyHandler);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final void clearHandler(Connection conn) throws SQLException {
/* 50 */     commitHandler(conn, null);
/*    */   }
/*    */   
/*    */   protected abstract int callback(int paramInt) throws SQLException;
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\BusyHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */