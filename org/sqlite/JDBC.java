/*     */ package org.sqlite;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.Driver;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.DriverPropertyInfo;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Logger;
/*     */ import org.sqlite.jdbc4.JDBC4Connection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JDBC
/*     */   implements Driver
/*     */ {
/*     */   public static final String PREFIX = "jdbc:sqlite:";
/*     */   
/*     */   static {
/*     */     try {
/*  29 */       DriverManager.registerDriver(new JDBC());
/*  30 */     } catch (SQLException e) {
/*  31 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMajorVersion() {
/*  37 */     return SQLiteJDBCLoader.getMajorVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMinorVersion() {
/*  42 */     return SQLiteJDBCLoader.getMinorVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean jdbcCompliant() {
/*  47 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
/*  52 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean acceptsURL(String url) {
/*  57 */     return isValidURL(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValidURL(String url) {
/*  67 */     return (url != null && url.toLowerCase().startsWith("jdbc:sqlite:"));
/*     */   }
/*     */ 
/*     */   
/*     */   public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
/*  72 */     return SQLiteConfig.getDriverPropertyInfo();
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection connect(String url, Properties info) throws SQLException {
/*  77 */     return createConnection(url, info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String extractAddress(String url) {
/*  87 */     return url.substring("jdbc:sqlite:".length());
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
/*     */   public static SQLiteConnection createConnection(String url, Properties prop) throws SQLException {
/* 101 */     if (!isValidURL(url)) return null;
/*     */     
/* 103 */     url = url.trim();
/* 104 */     return (SQLiteConnection)new JDBC4Connection(url, extractAddress(url), prop);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\JDBC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */