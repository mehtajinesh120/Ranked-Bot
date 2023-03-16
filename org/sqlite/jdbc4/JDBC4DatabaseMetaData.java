/*    */ package org.sqlite.jdbc4;
/*    */ 
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.RowIdLifetime;
/*    */ import java.sql.SQLException;
/*    */ import java.sql.SQLFeatureNotSupportedException;
/*    */ import org.sqlite.SQLiteConnection;
/*    */ import org.sqlite.jdbc3.JDBC3DatabaseMetaData;
/*    */ 
/*    */ public class JDBC4DatabaseMetaData extends JDBC3DatabaseMetaData {
/*    */   public JDBC4DatabaseMetaData(SQLiteConnection conn) {
/* 12 */     super(conn);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T unwrap(Class<T> iface) throws ClassCastException {
/* 17 */     return iface.cast(this);
/*    */   }
/*    */   
/*    */   public boolean isWrapperFor(Class<?> iface) {
/* 21 */     return iface.isInstance(this);
/*    */   }
/*    */   
/*    */   public RowIdLifetime getRowIdLifetime() throws SQLException {
/* 25 */     throw new SQLFeatureNotSupportedException();
/*    */   }
/*    */   
/*    */   public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
/* 29 */     throw new SQLFeatureNotSupportedException();
/*    */   }
/*    */   
/*    */   public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
/* 33 */     throw new SQLFeatureNotSupportedException();
/*    */   }
/*    */   
/*    */   public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
/* 37 */     throw new SQLFeatureNotSupportedException();
/*    */   }
/*    */   
/*    */   public ResultSet getClientInfoProperties() throws SQLException {
/* 41 */     throw new SQLFeatureNotSupportedException();
/*    */   }
/*    */ 
/*    */   
/*    */   public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
/* 46 */     throw new SQLFeatureNotSupportedException();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
/* 52 */     throw new SQLFeatureNotSupportedException();
/*    */   }
/*    */   
/*    */   public boolean generatedKeyAlwaysReturned() throws SQLException {
/* 56 */     throw new SQLFeatureNotSupportedException();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\jdbc4\JDBC4DatabaseMetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */