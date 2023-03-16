/*    */ package com.kasp.rankedbot.instance;
/*    */ import com.kasp.rankedbot.Statistic;
/*    */ import java.util.Collections;
/*    */ import java.util.Comparator;
/*    */ import java.util.HashMap;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class Leaderboard {
/*    */   public static List<String> getLeaderboard(Statistic statistic) {
/* 12 */     Map<Player, Double> unsortedMap = new HashMap<>();
/*    */     
/* 14 */     for (Player p : PlayerCache.getPlayers().values())
/*    */     {
/* 16 */       unsortedMap.put(p, Double.valueOf(p.getStatistic(statistic)));
/*    */     }
/*    */     
/* 19 */     List<String> lb = new ArrayList<>();
/*    */     
/* 21 */     List<Map.Entry<Player, Double>> list = new LinkedList<>(unsortedMap.entrySet());
/* 22 */     list.sort((Comparator)Map.Entry.comparingByValue());
/* 23 */     Collections.reverse(list);
/*    */     
/* 25 */     Map<Player, Double> sortedMap = new LinkedHashMap<>();
/*    */     
/* 27 */     for (Map.Entry<Player, Double> entry : list) {
/* 28 */       sortedMap.put(entry.getKey(), entry.getValue());
/*    */     }
/*    */     
/* 31 */     for (Map.Entry<Player, Double> entry : sortedMap.entrySet()) {
/* 32 */       lb.add(((Player)entry.getKey()).getID() + "=" + ((Player)entry.getKey()).getID());
/*    */     }
/*    */     
/* 35 */     return lb;
/*    */   }
/*    */   
/*    */   public static List<Clan> getClansLeaderboard() {
/* 39 */     Map<Clan, Integer> unsortedMap = new HashMap<>();
/*    */     
/* 41 */     for (Clan c : ClanCache.getClans().values())
/*    */     {
/* 43 */       unsortedMap.put(c, Integer.valueOf(c.getReputation()));
/*    */     }
/*    */     
/* 46 */     List<Map.Entry<Clan, Integer>> list = new LinkedList<>(unsortedMap.entrySet());
/* 47 */     list.sort((Comparator)Map.Entry.comparingByValue());
/* 48 */     Collections.reverse(list);
/*    */     
/* 50 */     Map<Clan, Integer> sortedMap = new LinkedHashMap<>();
/*    */     
/* 52 */     for (Map.Entry<Clan, Integer> entry : list) {
/* 53 */       sortedMap.put(entry.getKey(), entry.getValue());
/*    */     }
/*    */     
/* 56 */     List<Clan> lb = new ArrayList<>();
/*    */     
/* 58 */     for (Map.Entry<Clan, Integer> entry : sortedMap.entrySet()) {
/* 59 */       lb.add(entry.getKey());
/*    */     }
/*    */     
/* 62 */     return lb;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\Leaderboard.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */