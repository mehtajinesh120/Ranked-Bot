/*    */ package org.sqlite.jdbc4;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import org.sqlite.SQLiteConnection;
/*    */ 
/*    */ public class JDBC4Statement extends JDBC3Statement implements Statement {
/*    */   private boolean closed;
/*    */   boolean closeOnCompletion;
/*    */   
/* 10 */   public JDBC4Statement(SQLiteConnection conn) { super(conn);
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
/* 22 */     this.closed = false; }
/*    */    public <T> T unwrap(Class<T> iface) throws ClassCastException {
/*    */     return iface.cast(this);
/*    */   } public void close() throws SQLException {
/* 26 */     super.close();
/* 27 */     this.closed = true;
/*    */   } public boolean isWrapperFor(Class<?> iface) {
/*    */     return iface.isInstance(this);
/*    */   } public boolean isClosed() {
/* 31 */     return this.closed;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void closeOnCompletion() throws SQLException {
/* 37 */     if (this.closed) throw new SQLException("statement is closed"); 
/* 38 */     this.closeOnCompletion = true;
/*    */   }
/*    */   
/*    */   public boolean isCloseOnCompletion() throws SQLException {
/* 42 */     if (this.closed) throw new SQLException("statement is closed"); 
/* 43 */     return this.closeOnCompletion;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPoolable(boolean poolable) throws SQLException {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPoolable() throws SQLException {
/* 53 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\jdbc4\JDBC4Statement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */