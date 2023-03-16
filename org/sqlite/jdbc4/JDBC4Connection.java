/*     */ package org.sqlite.jdbc4;
/*     */ 
/*     */ import java.sql.Array;
/*     */ import java.sql.Blob;
/*     */ import java.sql.Clob;
/*     */ import java.sql.NClob;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLClientInfoException;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.sql.SQLXML;
/*     */ import java.sql.Statement;
/*     */ import java.util.Properties;
/*     */ import org.sqlite.SQLiteConnection;
/*     */ import org.sqlite.jdbc3.JDBC3Connection;
/*     */ 
/*     */ public class JDBC4Connection extends JDBC3Connection {
/*     */   public JDBC4Connection(String url, String fileName, Properties prop) throws SQLException {
/*  19 */     super(url, fileName, prop);
/*     */   }
/*     */   
/*     */   public Statement createStatement(int rst, int rsc, int rsh) throws SQLException {
/*  23 */     checkOpen();
/*  24 */     checkCursor(rst, rsc, rsh);
/*     */     
/*  26 */     return new JDBC4Statement((SQLiteConnection)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int rst, int rsc, int rsh) throws SQLException {
/*  31 */     checkOpen();
/*  32 */     checkCursor(rst, rsc, rsh);
/*     */     
/*  34 */     return new JDBC4PreparedStatement((SQLiteConnection)this, sql);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClosed() throws SQLException {
/*  40 */     return super.isClosed();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T unwrap(Class<T> iface) throws ClassCastException {
/*  45 */     return iface.cast(this);
/*     */   }
/*     */   
/*     */   public boolean isWrapperFor(Class<?> iface) {
/*  49 */     return iface.isInstance(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Clob createClob() throws SQLException {
/*  54 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Blob createBlob() throws SQLException {
/*  59 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public NClob createNClob() throws SQLException {
/*  64 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLXML createSQLXML() throws SQLException {
/*  69 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */   
/*     */   public boolean isValid(int timeout) throws SQLException {
/*  73 */     if (isClosed()) {
/*  74 */       return false;
/*     */     }
/*  76 */     Statement statement = createStatement();
/*     */     try {
/*  78 */       return statement.execute("select 1");
/*     */     } finally {
/*  80 */       statement.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClientInfo(String name, String value) throws SQLClientInfoException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClientInfo(Properties properties) throws SQLClientInfoException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClientInfo(String name) throws SQLException {
/*  96 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Properties getClientInfo() throws SQLException {
/* 101 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
/* 106 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\jdbc4\JDBC4Connection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */