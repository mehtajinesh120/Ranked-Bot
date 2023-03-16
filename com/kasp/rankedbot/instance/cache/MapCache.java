/*    */ package com.kasp.rankedbot.instance.cache;
/*    */ 
/*    */ import com.kasp.rankedbot.instance.GameMap;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class MapCache
/*    */ {
/* 10 */   private static HashMap<String, GameMap> maps = new HashMap<>();
/*    */   
/*    */   public static GameMap getMap(String name) {
/* 13 */     return maps.get(name);
/*    */   }
/*    */   
/*    */   public static void addMap(GameMap map) {
/* 17 */     maps.put(map.getName(), map);
/*    */     
/* 19 */     System.out.println("Map " + map.getName() + " has been loaded into memory");
/*    */   }
/*    */   
/*    */   public static void removeMap(GameMap map) {
/* 23 */     maps.remove(map.getName());
/*    */   }
/*    */   
/*    */   public static boolean containsMap(String name) {
/* 27 */     return maps.containsKey(name);
/*    */   }
/*    */   
/*    */   public static GameMap initializeMap(String name, GameMap map) {
/* 31 */     if (!containsMap(name)) {
/* 32 */       addMap(map);
/*    */     }
/* 34 */     return getMap(name);
/*    */   }
/*    */   
/*    */   public static Map<String, GameMap> getMaps() {
/* 38 */     return maps;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\cache\MapCache.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */