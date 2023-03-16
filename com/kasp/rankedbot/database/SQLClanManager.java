/*    */ package com.kasp.rankedbot.database;
/*    */ 
/*    */ import com.kasp.rankedbot.instance.cache.ClanCache;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ 
/*    */ public class SQLClanManager
/*    */ {
/*    */   public static void createClan(String name, String leader) {
/* 11 */     SQLite.updateData("INSERT INTO clans(name, leader, members, reputation, xp, level, wins, losses, private, eloJoinReq, description) VALUES('" + name + "','" + leader + "','" + leader + "',0,0,0,0,0,'true',0,'A newly created clan');");
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
/*    */ 
/*    */ 
/*    */   
/*    */   public static int getClanSize() {
/* 26 */     ResultSet resultSet = SQLite.queryData("SELECT COUNT(_ID) FROM clans");
/*    */     try {
/* 28 */       return resultSet.getInt(1);
/* 29 */     } catch (SQLException e) {
/* 30 */       e.printStackTrace();
/*    */ 
/*    */       
/* 33 */       return 0;
/*    */     } 
/*    */   }
/*    */   public static void updateXP(String name) {
/* 37 */     SQLite.updateData("UPDATE clans SET xp = '" + ClanCache.getClan(name).getXp() + "' WHERE name='" + name + "';");
/*    */   }
/*    */   
/*    */   public static void updateLevel(String name) {
/* 41 */     SQLite.updateData("UPDATE clans SET level = '" + ClanCache.getClan(name).getLevel().getLevel() + "' WHERE name='" + name + "';");
/*    */   }
/*    */   
/*    */   public static void updatePrivate(String name) {
/* 45 */     SQLite.updateData("UPDATE clans SET private = '" + ClanCache.getClan(name).isPrivate() + "' WHERE name='" + name + "';");
/*    */   }
/*    */   
/*    */   public static void updateEloJoinReq(String name) {
/* 49 */     SQLite.updateData("UPDATE clans SET eloJoinReq = '" + ClanCache.getClan(name).getEloJoinReq() + "' WHERE name='" + name + "';");
/*    */   }
/*    */   
/*    */   public static void updateDescription(String name) {
/* 53 */     SQLite.updateData("UPDATE clans SET description = '" + ClanCache.getClan(name).getDescription() + "' WHERE name='" + name + "';");
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\database\SQLClanManager.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */