/*    */ package com.kasp.rankedbot.instance.cache;
/*    */ 
/*    */ import com.kasp.rankedbot.instance.Theme;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class ThemeCache
/*    */ {
/* 10 */   private static HashMap<String, Theme> themes = new HashMap<>();
/*    */   
/*    */   public static Theme getTheme(String name) {
/* 13 */     return themes.get(name);
/*    */   }
/*    */   
/*    */   public static void addTheme(Theme theme) {
/* 17 */     themes.put(theme.getName(), theme);
/*    */     
/* 19 */     System.out.println("Theme " + theme.getName() + " has been loaded into memory");
/*    */   }
/*    */   
/*    */   public static void removeTheme(Theme theme) {
/* 23 */     themes.remove(theme.getName());
/*    */   }
/*    */   
/*    */   public static boolean containsTheme(String name) {
/* 27 */     return themes.containsKey(name);
/*    */   }
/*    */   
/*    */   public static void initializeTheme(String name, Theme theme) {
/* 31 */     if (!containsTheme(name))
/* 32 */       addTheme(theme); 
/*    */   }
/*    */   
/*    */   public static Map<String, Theme> getThemes() {
/* 36 */     return themes;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\cache\ThemeCache.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */