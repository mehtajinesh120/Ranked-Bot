/*    */ package com.kasp.rankedbot.database;
/*    */ import java.io.File;
/*    */ import java.sql.Connection;
/*    */ import java.sql.DriverManager;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import java.sql.Statement;
/*    */ 
/*    */ public class SQLite {
/*    */   private static Connection connection;
/*    */   
/*    */   public static void connect() {
/* 13 */     connection = null;
/*    */     try {
/* 15 */       File file = new File("data.db");
/* 16 */       if (!file.exists()) {
/* 17 */         file.createNewFile();
/*    */       }
/*    */       
/* 20 */       String link = "jdbc:sqlite:" + file.getPath();
/*    */       
/* 22 */       connection = DriverManager.getConnection(link);
/* 23 */       statement = connection.createStatement();
/* 24 */       System.out.println("Successfully connected to the database");
/* 25 */     } catch (IOException|SQLException e) {
/* 26 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */   private static Statement statement;
/*    */   public static void disconnect() {
/* 31 */     if (connection != null) {
/*    */       try {
/* 33 */         connection.close();
/* 34 */       } catch (SQLException e) {
/* 35 */         e.printStackTrace();
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   public static void updateData(String sql) {
/* 41 */     if (statement != null) {
/*    */       try {
/* 43 */         statement.execute(sql);
/* 44 */       } catch (SQLException e) {
/* 45 */         e.printStackTrace();
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   public static ResultSet queryData(String sql) {
/* 51 */     if (statement != null) {
/*    */       try {
/* 53 */         return statement.executeQuery(sql);
/* 54 */       } catch (SQLException e) {
/* 55 */         e.printStackTrace();
/*    */       } 
/*    */     }
/*    */     
/* 59 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\database\SQLite.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */