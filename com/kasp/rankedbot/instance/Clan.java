/*     */ package com.kasp.rankedbot.instance;
/*     */ 
/*     */ import com.kasp.rankedbot.config.Config;
/*     */ import com.kasp.rankedbot.database.SQLClanManager;
/*     */ import com.kasp.rankedbot.database.SQLite;
/*     */ import com.kasp.rankedbot.instance.cache.ClanCache;
/*     */ import com.kasp.rankedbot.instance.cache.ClanLevelCache;
/*     */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Clan
/*     */ {
/*     */   private String name;
/*     */   private Player leader;
/*     */   private List<Player> members;
/*     */   private int reputation;
/*     */   private int xp;
/*     */   private ClanLevel level;
/*     */   private int wins;
/*     */   private int losses;
/*     */   private boolean isPrivate;
/*     */   private int eloJoinReq;
/*     */   private String description;
/*     */   private List<Player> invitedPlayers;
/*     */   
/*     */   public Clan(String name, Player leader) {
/*  42 */     this.members = new ArrayList<>();
/*  43 */     this.invitedPlayers = new ArrayList<>();
/*  44 */     this.members.add(leader);
/*     */     
/*  46 */     this.name = name;
/*  47 */     this.leader = leader;
/*  48 */     this.reputation = Integer.parseInt(Config.getValue("clan-starting-rep"));
/*  49 */     this.level = ClanLevelCache.getLevel(0);
/*     */     
/*  51 */     this.isPrivate = true;
/*  52 */     this.description = "A newly created clan";
/*  53 */     ClanCache.initializeClan(name, this);
/*  54 */     create();
/*     */   }
/*     */ 
/*     */   
/*     */   public Clan(String name) {
/*  59 */     this.invitedPlayers = new ArrayList<>();
/*  60 */     this.name = name;
/*     */     
/*  62 */     ResultSet resultSet = SQLite.queryData("SELECT * FROM clans WHERE name='" + name + "';");
/*     */     
/*     */     try {
/*  65 */       this.leader = PlayerCache.getPlayer(resultSet.getString(3));
/*  66 */       this.members = new ArrayList<>();
/*  67 */       for (String ID : resultSet.getString(4).split(",")) {
/*  68 */         this.members.add(PlayerCache.getPlayer(ID));
/*     */       }
/*  70 */       this.reputation = resultSet.getInt(5);
/*  71 */       this.xp = resultSet.getInt(6);
/*  72 */       this.level = ClanLevelCache.getLevel(resultSet.getInt(7));
/*  73 */       this.wins = resultSet.getInt(8);
/*  74 */       this.losses = resultSet.getInt(9);
/*  75 */       this.isPrivate = Boolean.parseBoolean(resultSet.getString(10));
/*  76 */       this.eloJoinReq = resultSet.getInt(11);
/*  77 */       this.description = resultSet.getString(12);
/*  78 */     } catch (SQLException e) {
/*  79 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/*  82 */     ClanCache.initializeClan(name, this);
/*     */   }
/*     */   
/*     */   public void disband() {
/*  86 */     ClanCache.removeClan(this.name);
/*     */     
/*     */     try {
/*  89 */       Files.deleteIfExists(Path.of("RankedBot/clans/" + this.name + "/theme.png", new String[0]));
/*  90 */       Files.deleteIfExists(Path.of("RankedBot/clans/" + this.name + "/icon.png", new String[0]));
/*  91 */       Files.deleteIfExists(Path.of("RankedBot/clans/" + this.name, new String[0]));
/*  92 */     } catch (IOException e) {
/*  93 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void create() {
/*  99 */     (new File("RankedBot/clans/" + this.name)).mkdirs();
/*     */     
/* 101 */     if (!(new File("RankedBot/clans/" + this.name + "/data.yml")).exists()) {
/* 102 */       this.description = "A newly created PRBW clan";
/*     */     }
/*     */     
/* 105 */     SQLClanManager.createClan(this.name, this.leader.getID());
/*     */   }
/*     */   
/*     */   public static void deleteIcon(String name) {
/*     */     try {
/* 110 */       Files.deleteIfExists(Path.of("RankedBot/clans/" + name + "/icon.png", new String[0]));
/* 111 */     } catch (IOException e) {
/* 112 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void deleteTheme(String name) {
/*     */     try {
/* 118 */       Files.deleteIfExists(Path.of("RankedBot/clans/" + name + "/theme.png", new String[0]));
/* 119 */     } catch (IOException e) {
/* 120 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getReputation() {
/* 125 */     return this.reputation;
/*     */   }
/*     */   
/*     */   public int getXp() {
/* 129 */     return this.xp;
/*     */   }
/*     */   
/*     */   public boolean isPrivate() {
/* 133 */     return this.isPrivate;
/*     */   }
/*     */   
/*     */   public int getEloJoinReq() {
/* 137 */     return this.eloJoinReq;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 141 */     return this.description;
/*     */   }
/*     */   
/*     */   public List<Player> getInvitedPlayers() {
/* 145 */     return this.invitedPlayers;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 149 */     return this.name;
/*     */   }
/*     */   
/*     */   public Player getLeader() {
/* 153 */     return this.leader;
/*     */   }
/*     */   
/*     */   public List<Player> getMembers() {
/* 157 */     return this.members;
/*     */   }
/*     */   
/*     */   public ClanLevel getLevel() {
/* 161 */     return this.level;
/*     */   }
/*     */   
/*     */   public int getWins() {
/* 165 */     return this.wins;
/*     */   }
/*     */   
/*     */   public int getLosses() {
/* 169 */     return this.losses;
/*     */   }
/*     */   
/*     */   public void setPrivate(boolean aPrivate) {
/* 173 */     this.isPrivate = aPrivate;
/* 174 */     SQLClanManager.updatePrivate(this.name);
/*     */   }
/*     */   
/*     */   public void setEloJoinReq(int eloJoinReq) {
/* 178 */     this.eloJoinReq = eloJoinReq;
/* 179 */     SQLClanManager.updateEloJoinReq(this.name);
/*     */   }
/*     */   
/*     */   public void setDescription(String description) {
/* 183 */     this.description = description;
/* 184 */     SQLClanManager.updateDescription(this.name);
/*     */   }
/*     */   
/*     */   public void setXp(int xp) {
/* 188 */     this.xp = xp;
/* 189 */     SQLClanManager.updateXP(this.name);
/*     */   }
/*     */   
/*     */   public void setLevel(ClanLevel level) {
/* 193 */     this.level = level;
/* 194 */     SQLClanManager.updateLevel(this.name);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\Clan.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */