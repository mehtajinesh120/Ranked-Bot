/*     */ package com.kasp.rankedbot.instance;
/*     */ 
/*     */ import com.kasp.rankedbot.PickingMode;
/*     */ import com.kasp.rankedbot.database.SQLite;
/*     */ import com.kasp.rankedbot.instance.cache.PartyCache;
/*     */ import com.kasp.rankedbot.instance.cache.QueueCache;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Queue
/*     */ {
/*     */   private String ID;
/*     */   private int playersEachTeam;
/*     */   private PickingMode pickingMode;
/*     */   private boolean casual;
/*     */   private List<Player> players;
/*     */   private double eloMultiplier;
/*     */   TimerTask queueTimer;
/*     */   
/*     */   public Queue(String ID) {
/*  27 */     this.ID = ID;
/*     */     
/*  29 */     ResultSet resultSet = SQLite.queryData("SELECT * FROM queues WHERE discordID='" + ID + "';");
/*     */     
/*     */     try {
/*  32 */       this.playersEachTeam = resultSet.getInt(3);
/*  33 */       this.pickingMode = PickingMode.valueOf(resultSet.getString(4).toUpperCase());
/*  34 */       this.casual = Boolean.parseBoolean(resultSet.getString(5));
/*  35 */       this.eloMultiplier = resultSet.getDouble(6);
/*  36 */     } catch (SQLException e) {
/*  37 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/*  40 */     this.players = new ArrayList<>();
/*     */     
/*  42 */     QueueCache.initializeQueue(ID, this);
/*     */     
/*  44 */     final Queue q = this;
/*     */     
/*  46 */     this.queueTimer = new TimerTask()
/*     */       {
/*     */         public void run() {
/*     */           try {
/*  50 */             if (Queue.this.players.size() >= Queue.this.playersEachTeam * 2) {
/*     */               
/*  52 */               List<Party> partiesInQ = new ArrayList<>();
/*  53 */               List<Player> soloPlayersInQ = new ArrayList<>();
/*     */ 
/*     */               
/*  56 */               for (Player p : Queue.this.players) {
/*  57 */                 if (PartyCache.getParty(p) != null) {
/*  58 */                   if (!partiesInQ.contains(PartyCache.getParty(p))) {
/*  59 */                     partiesInQ.add(PartyCache.getParty(p));
/*     */                   }
/*     */                   continue;
/*     */                 } 
/*  63 */                 soloPlayersInQ.add(p);
/*     */               } 
/*     */ 
/*     */               
/*  67 */               List<Party> partiesStillInQ = new ArrayList<>(partiesInQ);
/*     */               
/*  69 */               if (partiesInQ.size() > 0) {
/*  70 */                 for (Party p : partiesInQ) {
/*  71 */                   for (Player player : p.getMembers()) {
/*  72 */                     if (!Queue.this.players.contains(player)) {
/*  73 */                       partiesStillInQ.remove(PartyCache.getParty(player));
/*     */                     }
/*     */                   } 
/*     */                 } 
/*     */               }
/*     */ 
/*     */               
/*  80 */               List<Player> ableToQ = new ArrayList<>(soloPlayersInQ);
/*  81 */               for (Party p : partiesStillInQ) {
/*  82 */                 ableToQ.addAll(p.getMembers());
/*     */               }
/*     */               
/*  85 */               if (ableToQ.size() >= Queue.this.getPlayersEachTeam() * 2) {
/*  86 */                 List<Player> playerList = new ArrayList<>();
/*     */                 
/*  88 */                 for (Party p : partiesStillInQ) {
/*  89 */                   if (playerList.size() + p.getMembers().size() <= Queue.this.getPlayersEachTeam() * 2) {
/*  90 */                     playerList.addAll(p.getMembers());
/*  91 */                     ableToQ.removeAll(p.getMembers());
/*     */                   } 
/*     */                 } 
/*     */                 
/*  95 */                 int tempPL = playerList.size();
/*  96 */                 for (int i = 0; i < Queue.this.getPlayersEachTeam() * 2 - tempPL; i++) {
/*  97 */                   playerList.add(ableToQ.get(i));
/*     */                 }
/*     */ 
/*     */                 
/* 101 */                 if (playerList.size() == Queue.this.getPlayersEachTeam() * 2) {
/* 102 */                   (new Game(playerList, q)).pickTeams();
/*     */                 }
/*     */               } 
/*     */             } 
/* 106 */           } catch (Exception e) {
/* 107 */             e.printStackTrace();
/*     */           } 
/*     */         }
/*     */       };
/*     */     
/* 112 */     (new Timer()).schedule(this.queueTimer, 0L, 3000L);
/*     */   }
/*     */   
/*     */   public void addPlayer(Player player) {
/* 116 */     this.players.add(player);
/*     */   }
/*     */   
/*     */   public static void delete(String ID) {
/* 120 */     QueueCache.removeQueue(QueueCache.getQueue(ID));
/*     */     
/* 122 */     SQLite.updateData("DELETE FROM queues WHERE discordID='" + ID + "';");
/*     */   }
/*     */   
/*     */   public void removePlayer(Player player) {
/* 126 */     this.players.remove(player);
/*     */   }
/*     */   public int getPlayersEachTeam() {
/* 129 */     return this.playersEachTeam;
/*     */   }
/*     */   public PickingMode getPickingMode() {
/* 132 */     return this.pickingMode;
/*     */   }
/*     */   public boolean isCasual() {
/* 135 */     return this.casual;
/*     */   }
/*     */   public String getID() {
/* 138 */     return this.ID;
/*     */   }
/*     */   public List<Player> getPlayers() {
/* 141 */     return this.players;
/*     */   }
/*     */   public double getEloMultiplier() {
/* 144 */     return this.eloMultiplier;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\Queue.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */