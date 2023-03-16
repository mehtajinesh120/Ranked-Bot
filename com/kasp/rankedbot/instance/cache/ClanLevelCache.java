/*    */ package com.kasp.rankedbot.instance.cache;
/*    */ 
/*    */ import com.kasp.rankedbot.instance.ClanLevel;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class ClanLevelCache
/*    */ {
/* 10 */   private static Map<Integer, ClanLevel> clanLevels = new HashMap<>();
/*    */   
/*    */   public static ClanLevel getLevel(int level) {
/* 13 */     return clanLevels.get(Integer.valueOf(level));
/*    */   }
/*    */   
/*    */   public static void addLevel(ClanLevel level) {
/* 17 */     clanLevels.put(Integer.valueOf(level.getLevel()), level);
/*    */     
/* 19 */     System.out.println("ClanLevel " + level.getLevel() + " has been loaded into memory");
/*    */   }
/*    */   
/*    */   public static void removeLevel(int level) {
/* 23 */     clanLevels.remove(Integer.valueOf(level));
/*    */   }
/*    */   
/*    */   public static boolean containsLevel(int level) {
/* 27 */     return clanLevels.containsKey(Integer.valueOf(level));
/*    */   }
/*    */   
/*    */   public static ClanLevel initializeLevel(int level, ClanLevel lvlobj) {
/* 31 */     if (!containsLevel(level)) {
/* 32 */       addLevel(lvlobj);
/*    */     }
/* 34 */     return getLevel(level);
/*    */   }
/*    */   
/*    */   public static Map<Integer, ClanLevel> getClanLevels() {
/* 38 */     return clanLevels;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\cache\ClanLevelCache.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */