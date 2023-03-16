/*    */ package com.kasp.rankedbot.instance.cache;
/*    */ 
/*    */ import com.kasp.rankedbot.instance.Clan;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class ClanCache
/*    */ {
/* 11 */   private static Map<String, Clan> clans = new HashMap<>();
/*    */   
/*    */   public static Clan getClan(String name) {
/* 14 */     for (Clan c : clans.values()) {
/* 15 */       if (c.getName().equalsIgnoreCase(name)) {
/* 16 */         return c;
/*    */       }
/*    */     } 
/*    */     
/* 20 */     return null;
/*    */   }
/*    */   
/*    */   public static Clan getClan(Player player) {
/* 24 */     for (Clan c : clans.values()) {
/* 25 */       if (c.getMembers().contains(player)) {
/* 26 */         return c;
/*    */       }
/*    */     } 
/*    */     
/* 30 */     return null;
/*    */   }
/*    */   
/*    */   public static void addClan(Clan clan) {
/* 34 */     clans.put(clan.getName(), clan);
/*    */     
/* 36 */     System.out.println("Clan " + clan.getName() + " has been loaded into memory");
/*    */   }
/*    */   
/*    */   public static void removeClan(String name) {
/* 40 */     clans.remove(name);
/*    */   }
/*    */   
/*    */   public static boolean containsClan(String name) {
/* 44 */     return clans.containsKey(name);
/*    */   }
/*    */   
/*    */   public static Clan initializeClan(String name, Clan clan) {
/* 48 */     if (!containsClan(name)) {
/* 49 */       addClan(clan);
/*    */     }
/* 51 */     return getClan(name);
/*    */   }
/*    */   
/*    */   public static Map<String, Clan> getClans() {
/* 55 */     return clans;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\cache\ClanCache.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */