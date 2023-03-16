/*    */ package com.kasp.rankedbot.instance.cache;
/*    */ 
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class PlayerCache
/*    */ {
/* 10 */   private static HashMap<String, Player> players = new HashMap<>();
/*    */   
/*    */   public static Player getPlayer(String ID) {
/* 13 */     return players.get(ID);
/*    */   }
/*    */   
/*    */   public static void addPlayer(Player player) {
/* 17 */     players.put(player.getID(), player);
/*    */     
/* 19 */     System.out.println("Player " + player.getIgn() + " (" + player.getID() + ") has been loaded into memory");
/*    */   }
/*    */   
/*    */   public static void removePlayer(Player player) {
/* 23 */     players.remove(player.getID());
/*    */   }
/*    */   
/*    */   public static boolean containsPlayer(Player player) {
/* 27 */     return players.containsValue(player);
/*    */   }
/*    */   
/*    */   public static void initializePlayer(Player player) {
/* 31 */     if (!containsPlayer(player))
/* 32 */       addPlayer(player); 
/*    */   }
/*    */   
/*    */   public static Map<String, Player> getPlayers() {
/* 36 */     return players;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\cache\PlayerCache.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */