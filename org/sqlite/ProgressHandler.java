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
/*    */ public abstract class ProgressHandler
/*    */ {
/*    */   public static final void setHandler(Connection conn, int vmCalls, ProgressHandler progressHandler) throws SQLException {
/* 19 */     if (!(conn instanceof SQLiteConnection)) {
/* 20 */       throw new SQLException("connection must be to an SQLite db");
/*    */     }
/* 22 */     if (conn.isClosed()) {
/* 23 */       throw new SQLException("connection closed");
/*    */     }
/* 25 */     SQLiteConnection sqliteConnection = (SQLiteConnection)conn;
/* 26 */     sqliteConnection.getDatabase().register_progress_handler(vmCalls, progressHandler);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final void clearHandler(Connection conn) throws SQLException {
/* 36 */     SQLiteConnection sqliteConnection = (SQLiteConnection)conn;
/* 37 */     sqliteConnection.getDatabase().clear_progress_handler();
/*    */   }
/*    */   
/*    */   protected abstract int progress() throws SQLException;
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\ProgressHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */