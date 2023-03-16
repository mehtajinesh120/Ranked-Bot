/*    */ package com.kasp.rankedbot.database;
/*    */ 
/*    */ public class SQLTableManager
/*    */ {
/*    */   public static void createPlayersTable() {
/*  6 */     SQLite.updateData("CREATE TABLE IF NOT EXISTS players(_ID INTEGER PRIMARY KEY,discordID TEXT,ign TEXT,elo INTEGER,peakElo INTEGER,wins INTEGER,losses INTEGER,winStreak INTEGER,lossStreak INTEGER,highestWS INTEGER,highestLS INTEGER,mvp INTEGER,kills INTEGER,deaths INTEGER,strikes INTEGER,scored INTEGER,gold INTEGER,level INTEGER,xp INTEGER,theme TEXT,ownedThemes TEXT,isBanned TEXT,bannedTill TEXT,banReason TEXT);");
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
/*    */   public static void createClansTable() {
/* 34 */     SQLite.updateData("CREATE TABLE IF NOT EXISTS clans(_ID INTEGER PRIMARY KEY,name TEXT,leader TEXT,members TEXT,reputation INTEGER,clanXP INTEGER,clanLevel INTEGER,wins INTEGER,losses INTEGER,private TEXT,eloJoinReq INTEGER,description TEXT);");
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
/*    */   
/*    */   public static void createGamesTable() {
/* 50 */     SQLite.updateData("CREATE TABLE IF NOT EXISTS games(_ID INTEGER PRIMARY KEY,number INTEGER,state TEXT,casual TEXT,map TEXT,channelID TEXT,vc1ID TEXT,vc2ID TEXT,queueID TEXT,team1 TEXT,team2 TEXT,mvp TEXT,scoredBy TEXT);");
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
/*    */ 
/*    */   
/*    */   public static void createMapsTable() {
/* 67 */     SQLite.updateData("CREATE TABLE IF NOT EXISTS maps(_ID INTEGER PRIMARY KEY,name TEXT,heightLimit INTEGER,team1 TEXT,team2 TEXT);");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void createQueuesTable() {
/* 76 */     SQLite.updateData("CREATE TABLE IF NOT EXISTS queues(_ID INTEGER PRIMARY KEY,discordID TEXT,playersEachTeam INTEGER,pickingMode TEXT,casual TEXT,eloMultiplier REAL);");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void createRanksTable() {
/* 86 */     SQLite.updateData("CREATE TABLE IF NOT EXISTS ranks(_ID INTEGER PRIMARY KEY,discordID TEXT,startingElo INTEGER,endingElo INTEGER,winElo INTEGER,loseElo INTEGER,mvpElo INTEGER);");
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\database\SQLTableManager.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */