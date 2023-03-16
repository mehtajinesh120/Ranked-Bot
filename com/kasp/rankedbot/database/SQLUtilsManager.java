/*    */ package com.kasp.rankedbot.database;
/*    */ 
/*    */ import com.kasp.rankedbot.PickingMode;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ 
/*    */ public class SQLUtilsManager
/*    */ {
/*    */   public static void createRank(String ID, String startingElo, String endingElo, String winElo, String loseElo, String mvpElo) {
/* 11 */     SQLite.updateData("INSERT INTO ranks(discordID, startingElo, endingElo, winElo, loseElo, mvpElo) VALUES('" + ID + "'," + startingElo + "," + endingElo + "," + winElo + "," + loseElo + "," + mvpElo + ");");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int getRankSize() {
/* 21 */     ResultSet resultSet = SQLite.queryData("SELECT COUNT(_ID) FROM ranks");
/*    */     try {
/* 23 */       return resultSet.getInt(1);
/* 24 */     } catch (SQLException e) {
/* 25 */       e.printStackTrace();
/*    */ 
/*    */       
/* 28 */       return 0;
/*    */     } 
/*    */   }
/*    */   public static void createQueue(String ID, int playersEachTeam, PickingMode pickingMode, boolean casual) {
/* 32 */     SQLite.updateData("INSERT INTO queues(discordID, playersEachTeam, pickingMode, casual, eloMultiplier) VALUES('" + ID + "'," + playersEachTeam + ",'" + pickingMode
/*    */ 
/*    */         
/* 35 */         .toString().toUpperCase() + "','" + casual + "',1.0);");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static int getQueueSize() {
/* 41 */     ResultSet resultSet = SQLite.queryData("SELECT COUNT(_ID) FROM queues");
/*    */     try {
/* 43 */       return resultSet.getInt(1);
/* 44 */     } catch (SQLException e) {
/* 45 */       e.printStackTrace();
/*    */ 
/*    */       
/* 48 */       return 0;
/*    */     } 
/*    */   }
/*    */   public static void createMap(String name, String height, String team1, String team2) {
/* 52 */     SQLite.updateData("INSERT INTO maps(name, heightLimit, team1, team2) VALUES('" + name + "'," + height + ",'" + team1 + "','" + team2 + "');");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int getMapSize() {
/* 60 */     ResultSet resultSet = SQLite.queryData("SELECT COUNT(_ID) FROM maps");
/*    */     try {
/* 62 */       return resultSet.getInt(1);
/* 63 */     } catch (SQLException e) {
/* 64 */       e.printStackTrace();
/*    */ 
/*    */       
/* 67 */       return 0;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\database\SQLUtilsManager.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */