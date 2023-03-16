/*     */ package com.kasp.rankedbot.database;
/*     */ 
/*     */ import com.kasp.rankedbot.instance.Game;
/*     */ import com.kasp.rankedbot.instance.Player;
/*     */ import com.kasp.rankedbot.instance.cache.GameCache;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ 
/*     */ 
/*     */ public class SQLGameManager
/*     */ {
/*     */   public static void createGame(Game g) {
/*  13 */     String team1 = "";
/*  14 */     String team2 = "";
/*     */     
/*  16 */     for (Player p : g.getTeam1()) {
/*  17 */       team1 = team1 + team1;
/*  18 */       if (g.getTeam1().indexOf(p) + 1 < g.getTeam1().size()) {
/*  19 */         team1 = team1 + ",";
/*     */       }
/*     */     } 
/*     */     
/*  23 */     for (Player p : g.getTeam2()) {
/*  24 */       team2 = team2 + team2;
/*  25 */       if (g.getTeam2().indexOf(p) + 1 < g.getTeam2().size()) {
/*  26 */         team2 = team2 + ",";
/*     */       }
/*     */     } 
/*     */     
/*  30 */     SQLite.updateData("INSERT INTO games(number, state, casual, map, channelID, vc1ID, vc2ID, queueID, team1, team2) VALUES(" + g
/*  31 */         .getNumber() + ",'" + g
/*  32 */         .getState().toString().toUpperCase() + "','" + g
/*  33 */         .isCasual() + "','" + g
/*  34 */         .getMap().getName() + "','" + g
/*  35 */         .getChannelID() + "','" + g
/*  36 */         .getVC1ID() + "','" + g
/*  37 */         .getVC2ID() + "','" + g
/*  38 */         .getQueue().getID() + "','" + team1 + "','" + team2 + "');");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getGameSize() {
/*  44 */     ResultSet resultSet = SQLite.queryData("SELECT COUNT(_ID) FROM games");
/*     */     try {
/*  46 */       return resultSet.getInt(1);
/*  47 */     } catch (SQLException e) {
/*  48 */       e.printStackTrace();
/*     */ 
/*     */       
/*  51 */       return 0;
/*     */     } 
/*     */   }
/*     */   public static void updateState(int number) {
/*  55 */     SQLite.updateData("UPDATE games SET state = '" + GameCache.getGame(number).getState().toString().toUpperCase() + "' WHERE number=" + number + ";");
/*     */   }
/*     */   
/*     */   public static void updateMvp(int number) {
/*  59 */     SQLite.updateData("UPDATE games SET mvp = '" + GameCache.getGame(number).getMvp().getIgn() + "' WHERE number=" + number + ";");
/*     */   }
/*     */   
/*     */   public static void updateScoredBy(int number) {
/*  63 */     SQLite.updateData("UPDATE games SET scoredBy = '" + GameCache.getGame(number).getScoredBy().getId() + "' WHERE number=" + number + ";");
/*     */   }
/*     */   
/*     */   public static void updateEloGain(int number) {
/*  67 */     Game g = GameCache.getGame(number);
/*     */     
/*  69 */     String team1 = "";
/*  70 */     String team2 = "";
/*     */     
/*  72 */     for (Player p : g.getTeam1()) {
/*  73 */       team1 = team1 + team1 + "=" + p.getID();
/*  74 */       if (g.getTeam1().indexOf(p) + 1 < g.getTeam1().size()) {
/*  75 */         team1 = team1 + ",";
/*     */       }
/*     */     } 
/*     */     
/*  79 */     for (Player p : g.getTeam2()) {
/*  80 */       team2 = team2 + team2 + "=" + p.getID();
/*  81 */       if (g.getTeam2().indexOf(p) + 1 < g.getTeam2().size()) {
/*  82 */         team2 = team2 + ",";
/*     */       }
/*     */     } 
/*     */     
/*  86 */     SQLite.updateData("UPDATE games SET team1 = '" + team1 + "' WHERE number=" + number + ";");
/*  87 */     SQLite.updateData("UPDATE games SET team2 = '" + team2 + "' WHERE number=" + number + ";");
/*     */   }
/*     */   
/*     */   public static void removeEloGain(int number) {
/*  91 */     Game g = GameCache.getGame(number);
/*     */     
/*  93 */     String team1 = "";
/*  94 */     String team2 = "";
/*     */     
/*  96 */     for (Player p : g.getTeam1()) {
/*  97 */       team1 = team1 + team1;
/*  98 */       if (g.getTeam1().indexOf(p) + 1 < g.getTeam1().size()) {
/*  99 */         team1 = team1 + ",";
/*     */       }
/*     */     } 
/*     */     
/* 103 */     for (Player p : g.getTeam2()) {
/* 104 */       team2 = team2 + team2;
/* 105 */       if (g.getTeam2().indexOf(p) + 1 < g.getTeam2().size()) {
/* 106 */         team2 = team2 + ",";
/*     */       }
/*     */     } 
/*     */     
/* 110 */     SQLite.updateData("UPDATE games SET team1 = '" + team1 + "' WHERE number=" + number + ";");
/* 111 */     SQLite.updateData("UPDATE games SET team2 = '" + team2 + "' WHERE number=" + number + ";");
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\database\SQLGameManager.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */