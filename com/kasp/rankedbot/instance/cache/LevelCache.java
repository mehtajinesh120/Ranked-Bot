/*    */ package com.kasp.rankedbot.instance.cache;
/*    */ 
/*    */ import com.kasp.rankedbot.instance.Level;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class LevelCache
/*    */ {
/* 10 */   private static Map<Integer, Level> levels = new HashMap<>();
/*    */   
/*    */   public static Level getLevel(int level) {
/* 13 */     return levels.get(Integer.valueOf(level));
/*    */   }
/*    */   
/*    */   public static void addLevel(Level level) {
/* 17 */     levels.put(Integer.valueOf(level.getLevel()), level);
/*    */     
/* 19 */     System.out.println("Level " + level.getLevel() + " has been loaded into memory");
/*    */   }
/*    */   
/*    */   public static void removeLevel(int level) {
/* 23 */     levels.remove(Integer.valueOf(level));
/*    */   }
/*    */   
/*    */   public static boolean containsLevel(int level) {
/* 27 */     return levels.containsKey(Integer.valueOf(level));
/*    */   }
/*    */   
/*    */   public static Level initializeLevel(int level, Level lvlobj) {
/* 31 */     if (!containsLevel(level)) {
/* 32 */       addLevel(lvlobj);
/*    */     }
/* 34 */     return getLevel(level);
/*    */   }
/*    */   
/*    */   public static Map<Integer, Level> getLevels() {
/* 38 */     return levels;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\cache\LevelCache.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */