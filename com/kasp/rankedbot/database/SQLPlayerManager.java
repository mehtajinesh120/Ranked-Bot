/*     */ package com.kasp.rankedbot.database;
/*     */ 
/*     */ import com.kasp.rankedbot.config.Config;
/*     */ import com.kasp.rankedbot.instance.Player;
/*     */ import com.kasp.rankedbot.instance.Theme;
/*     */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ 
/*     */ 
/*     */ public class SQLPlayerManager
/*     */ {
/*     */   public static void createPlayer(String ID, String ign) {
/*  15 */     SQLite.updateData("INSERT INTO players(discordID, ign, elo, peakElo, wins, losses, winStreak, lossStreak, highestWS, highestLS,mvp, kills, deaths, strikes, scored, gold, level, xp, theme, ownedThemes, isBanned, bannedTill) VALUES('" + ID + "','" + ign + "'," + 
/*     */ 
/*     */ 
/*     */         
/*  19 */         Config.getValue("starting-elo") + "," + 
/*  20 */         Config.getValue("starting-elo") + ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,'default','default','false','');");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getPlayerSize() {
/*  42 */     ResultSet resultSet = SQLite.queryData("SELECT COUNT(_ID) FROM players");
/*     */     try {
/*  44 */       return resultSet.getInt(1);
/*  45 */     } catch (SQLException e) {
/*  46 */       e.printStackTrace();
/*     */ 
/*     */       
/*  49 */       return 0;
/*     */     } 
/*     */   }
/*     */   public static boolean isRegistered(String ID) {
/*  53 */     ResultSet resultSet = SQLite.queryData("SELECT EXISTS(SELECT 1 FROM players WHERE discordID='" + ID + "');");
/*     */     
/*     */     try {
/*  56 */       if (resultSet.getInt(1) == 1)
/*  57 */         return true; 
/*  58 */     } catch (SQLException e) {
/*  59 */       e.printStackTrace();
/*     */     } 
/*     */     
/*  62 */     return false;
/*     */   }
/*     */   
/*     */   public static void updateIgn(String ID) {
/*  66 */     SQLite.updateData("UPDATE players SET ign = '" + PlayerCache.getPlayer(ID).getIgn() + "' WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateElo(String ID) {
/*  70 */     SQLite.updateData("UPDATE players SET elo = " + PlayerCache.getPlayer(ID).getElo() + " WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updatePeakElo(String ID) {
/*  74 */     SQLite.updateData("UPDATE players SET peakElo = " + PlayerCache.getPlayer(ID).getPeakElo() + " WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateWins(String ID) {
/*  78 */     SQLite.updateData("UPDATE players SET wins = " + PlayerCache.getPlayer(ID).getWins() + " WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateLosses(String ID) {
/*  82 */     SQLite.updateData("UPDATE players SET losses = " + PlayerCache.getPlayer(ID).getLosses() + " WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateWinStreak(String ID) {
/*  86 */     SQLite.updateData("UPDATE players SET winStreak = " + PlayerCache.getPlayer(ID).getWinStreak() + " WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateLossStreak(String ID) {
/*  90 */     SQLite.updateData("UPDATE players SET lossStreak = " + PlayerCache.getPlayer(ID).getLossStreak() + " WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateHighestWS(String ID) {
/*  94 */     SQLite.updateData("UPDATE players SET highestWS = " + PlayerCache.getPlayer(ID).getHighestWS() + " WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateHighestLS(String ID) {
/*  98 */     SQLite.updateData("UPDATE players SET highestLS = " + PlayerCache.getPlayer(ID).getHighestLS() + " WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateMvp(String ID) {
/* 102 */     SQLite.updateData("UPDATE players SET mvp = " + PlayerCache.getPlayer(ID).getMvp() + " WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateKills(String ID) {
/* 106 */     SQLite.updateData("UPDATE players SET kills = " + PlayerCache.getPlayer(ID).getKills() + " WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateDeaths(String ID) {
/* 110 */     SQLite.updateData("UPDATE players SET deaths = " + PlayerCache.getPlayer(ID).getDeaths() + " WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateStrikes(String ID) {
/* 114 */     SQLite.updateData("UPDATE players SET strikes = " + PlayerCache.getPlayer(ID).getStrikes() + " WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateScored(String ID) {
/* 118 */     SQLite.updateData("UPDATE players SET scored = " + PlayerCache.getPlayer(ID).getScored() + " WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateGold(String ID) {
/* 122 */     SQLite.updateData("UPDATE players SET gold = " + PlayerCache.getPlayer(ID).getGold() + " WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateLevel(String ID) {
/* 126 */     SQLite.updateData("UPDATE players SET level = " + PlayerCache.getPlayer(ID).getLevel().getLevel() + " WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateXP(String ID) {
/* 130 */     SQLite.updateData("UPDATE players SET xp = " + PlayerCache.getPlayer(ID).getXp() + " WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateTheme(String ID) {
/* 134 */     SQLite.updateData("UPDATE players SET theme = '" + PlayerCache.getPlayer(ID).getTheme().getName() + "' WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateOwnedThemes(String ID) {
/* 138 */     Player player = PlayerCache.getPlayer(ID);
/*     */     
/* 140 */     StringBuilder themes = new StringBuilder();
/* 141 */     for (Theme t : player.getOwnedThemes()) {
/* 142 */       themes.append(t.getName());
/* 143 */       if (player.getOwnedThemes().indexOf(t) != player.getOwnedThemes().size() - 1) {
/* 144 */         themes.append(",");
/*     */       }
/*     */     } 
/*     */     
/* 148 */     SQLite.updateData("UPDATE players SET ownedThemes = '" + themes + "' WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateIsBanned(String ID) {
/* 152 */     SQLite.updateData("UPDATE players SET isBanned = '" + PlayerCache.getPlayer(ID).isBanned() + "' WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public static void updateBannedTill(String ID) {
/* 156 */     Player player = PlayerCache.getPlayer(ID);
/*     */     
/* 158 */     if (player.isBanned()) {
/* 159 */       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
/* 160 */       String bannedTill = formatter.format(player.getBannedTill());
/*     */       
/* 162 */       SQLite.updateData("UPDATE players SET bannedTill = '" + bannedTill + "' WHERE discordID='" + ID + "';");
/*     */     } else {
/*     */       
/* 165 */       SQLite.updateData("UPDATE players SET bannedTill = '' WHERE discordID='" + ID + "';");
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void updateBanReason(String ID) {
/* 170 */     Player player = PlayerCache.getPlayer(ID);
/*     */     
/* 172 */     if (player.isBanned()) {
/* 173 */       SQLite.updateData("UPDATE players SET banReason = '" + PlayerCache.getPlayer(ID).getBanReason() + "' WHERE discordID='" + ID + "';");
/*     */     } else {
/*     */       
/* 176 */       SQLite.updateData("UPDATE players SET banReason = '' WHERE discordID='" + ID + "';");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\database\SQLPlayerManager.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */