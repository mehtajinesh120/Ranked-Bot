/*    */ package com.kasp.rankedbot.instance;
/*    */ 
/*    */ import com.kasp.rankedbot.database.SQLite;
/*    */ import com.kasp.rankedbot.instance.cache.MapCache;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ 
/*    */ public class GameMap
/*    */ {
/*    */   private String name;
/*    */   private int height;
/*    */   private String team1;
/*    */   private String team2;
/*    */   
/*    */   public GameMap(String name) {
/* 17 */     this.name = name;
/*    */     
/* 19 */     ResultSet resultSet = SQLite.queryData("SELECT * FROM maps WHERE name='" + name + "';");
/*    */     
/*    */     try {
/* 22 */       this.height = resultSet.getInt(3);
/* 23 */       this.team1 = resultSet.getString(4);
/* 24 */       this.team2 = resultSet.getString(5);
/* 25 */     } catch (SQLException e) {
/* 26 */       throw new RuntimeException(e);
/*    */     } 
/*    */     
/* 29 */     MapCache.initializeMap(name, this);
/*    */   }
/*    */   
/*    */   public static void delete(String name) {
/* 33 */     MapCache.removeMap(MapCache.getMap(name));
/*    */     
/* 35 */     SQLite.updateData("DELETE FROM maps WHERE name='" + name + "';");
/*    */   }
/*    */   
/*    */   public String getName() {
/* 39 */     return this.name;
/*    */   }
/*    */   public void setName(String name) {
/* 42 */     this.name = name;
/*    */   }
/*    */   public int getHeight() {
/* 45 */     return this.height;
/*    */   }
/*    */   public void setHeight(int height) {
/* 48 */     this.height = height;
/*    */   }
/*    */   public String getTeam1() {
/* 51 */     return this.team1;
/*    */   }
/*    */   public void setTeam1(String team1) {
/* 54 */     this.team1 = team1;
/*    */   }
/*    */   public String getTeam2() {
/* 57 */     return this.team2;
/*    */   }
/*    */   public void setTeam2(String team2) {
/* 60 */     this.team2 = team2;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\GameMap.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */