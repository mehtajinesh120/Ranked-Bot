/*    */ package com.kasp.rankedbot.instance;
/*    */ 
/*    */ import com.kasp.rankedbot.database.SQLite;
/*    */ import com.kasp.rankedbot.instance.cache.RankCache;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ 
/*    */ public class Rank
/*    */ {
/*    */   private String ID;
/*    */   private int startingElo;
/*    */   private int endingElo;
/*    */   private int winElo;
/*    */   private int loseElo;
/*    */   private int mvpElo;
/*    */   
/*    */   public Rank(String ID) {
/* 19 */     this.ID = ID;
/*    */     
/* 21 */     ResultSet resultSet = SQLite.queryData("SELECT * FROM ranks WHERE discordID='" + ID + "';");
/*    */     
/*    */     try {
/* 24 */       this.startingElo = resultSet.getInt(3);
/* 25 */       this.endingElo = resultSet.getInt(4);
/* 26 */       this.winElo = resultSet.getInt(5);
/* 27 */       this.loseElo = resultSet.getInt(6);
/* 28 */       this.mvpElo = resultSet.getInt(7);
/* 29 */     } catch (SQLException e) {
/* 30 */       throw new RuntimeException(e);
/*    */     } 
/*    */     
/* 33 */     RankCache.initializeRank(ID, this);
/*    */   }
/*    */   
/*    */   public static void delete(String ID) {
/* 37 */     RankCache.removeRank(RankCache.getRank(ID));
/*    */     
/* 39 */     SQLite.updateData("DELETE FROM ranks WHERE discordID='" + ID + "';");
/*    */   }
/*    */   
/*    */   public String getID() {
/* 43 */     return this.ID;
/*    */   }
/*    */   public void setID(String ID) {
/* 46 */     this.ID = ID;
/*    */   }
/*    */   public int getStartingElo() {
/* 49 */     return this.startingElo;
/*    */   }
/*    */   public void setStartingElo(int startingElo) {
/* 52 */     this.startingElo = startingElo;
/*    */   }
/*    */   public int getEndingElo() {
/* 55 */     return this.endingElo;
/*    */   }
/*    */   public void setEndingElo(int endingElo) {
/* 58 */     this.endingElo = endingElo;
/*    */   }
/*    */   public int getWinElo() {
/* 61 */     return this.winElo;
/*    */   }
/*    */   public void setWinElo(int winElo) {
/* 64 */     this.winElo = winElo;
/*    */   }
/*    */   public int getLoseElo() {
/* 67 */     return this.loseElo;
/*    */   }
/*    */   public void setLoseElo(int loseElo) {
/* 70 */     this.loseElo = loseElo;
/*    */   }
/*    */   public int getMvpElo() {
/* 73 */     return this.mvpElo;
/*    */   }
/*    */   public void setMvpElo(int mvpElo) {
/* 76 */     this.mvpElo = mvpElo;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\Rank.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */