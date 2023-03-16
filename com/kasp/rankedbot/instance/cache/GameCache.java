/*    */ package com.kasp.rankedbot.instance.cache;
/*    */ 
/*    */ import com.kasp.rankedbot.instance.Game;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class GameCache
/*    */ {
/* 10 */   public static HashMap<Integer, Game> games = new HashMap<>();
/*    */   
/*    */   public static Game getGame(int number) {
/* 13 */     return games.get(Integer.valueOf(number));
/*    */   }
/*    */ 
/*    */   
/*    */   public static Game getGame(String channelID) {
/* 18 */     for (Game g : games.values()) {
/* 19 */       if (g.getChannelID().equals(channelID)) {
/* 20 */         return g;
/*    */       }
/*    */     } 
/*    */     
/* 24 */     return null;
/*    */   }
/*    */   
/*    */   public static void addGame(Game game) {
/* 28 */     games.put(Integer.valueOf(game.getNumber()), game);
/*    */     
/* 30 */     System.out.println("Game " + game.getNumber() + " has been loaded into memory");
/*    */   }
/*    */   
/*    */   public static void removeGame(Game game) {
/* 34 */     games.remove(Integer.valueOf(game.getNumber()));
/*    */   }
/*    */   
/*    */   public static boolean containsGame(Game game) {
/* 38 */     return games.containsValue(game);
/*    */   }
/*    */   
/*    */   public static void initializeGame(Game game) {
/* 42 */     if (!containsGame(game))
/* 43 */       addGame(game); 
/*    */   }
/*    */   
/*    */   public static Map<Integer, Game> getGames() {
/* 47 */     return games;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\cache\GameCache.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */