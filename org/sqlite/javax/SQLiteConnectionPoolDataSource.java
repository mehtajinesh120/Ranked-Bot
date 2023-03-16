/*    */ package org.sqlite.javax;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import javax.sql.ConnectionPoolDataSource;
/*    */ import javax.sql.PooledConnection;
/*    */ import org.sqlite.SQLiteConfig;
/*    */ import org.sqlite.SQLiteDataSource;
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
/*    */ public class SQLiteConnectionPoolDataSource
/*    */   extends SQLiteDataSource
/*    */   implements ConnectionPoolDataSource
/*    */ {
/*    */   public SQLiteConnectionPoolDataSource() {}
/*    */   
/*    */   public SQLiteConnectionPoolDataSource(SQLiteConfig config) {
/* 35 */     super(config);
/*    */   }
/*    */ 
/*    */   
/*    */   public PooledConnection getPooledConnection() throws SQLException {
/* 40 */     return getPooledConnection(null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PooledConnection getPooledConnection(String user, String password) throws SQLException {
/* 48 */     return (PooledConnection)new SQLitePooledConnection(getConnection(user, password));
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\javax\SQLiteConnectionPoolDataSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */