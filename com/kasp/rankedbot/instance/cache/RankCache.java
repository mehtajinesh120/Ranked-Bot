/*    */ package com.kasp.rankedbot.instance.cache;
/*    */ 
/*    */ import com.kasp.rankedbot.instance.Rank;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class RankCache
/*    */ {
/* 10 */   private static HashMap<String, Rank> ranks = new HashMap<>();
/*    */   
/*    */   public static Rank getRank(String ID) {
/* 13 */     return ranks.get(ID);
/*    */   }
/*    */   
/*    */   public static void addRank(Rank rank) {
/* 17 */     ranks.put(rank.getID(), rank);
/*    */     
/* 19 */     System.out.println("Rank " + rank.getID() + " has been loaded into memory");
/*    */   }
/*    */   
/*    */   public static void removeRank(Rank rank) {
/* 23 */     ranks.remove(rank.getID());
/*    */   }
/*    */   
/*    */   public static boolean containsRank(String ID) {
/* 27 */     return ranks.containsKey(ID);
/*    */   }
/*    */   
/*    */   public static Rank initializeRank(String ID, Rank rank) {
/* 31 */     if (!containsRank(ID)) {
/* 32 */       addRank(rank);
/*    */     }
/* 34 */     return getRank(ID);
/*    */   }
/*    */   
/*    */   public static Map<String, Rank> getRanks() {
/* 38 */     return ranks;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\cache\RankCache.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */