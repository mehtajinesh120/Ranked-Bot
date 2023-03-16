/*    */ package com.kasp.rankedbot.instance.cache;
/*    */ 
/*    */ import com.kasp.rankedbot.instance.Party;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class PartyCache
/*    */ {
/* 11 */   private static List<Party> parties = new ArrayList<>();
/*    */   
/*    */   public static Party getParty(Player player) {
/* 14 */     for (Party p : parties) {
/* 15 */       if (p.getMembers().contains(player)) {
/* 16 */         return p;
/*    */       }
/*    */     } 
/*    */     
/* 20 */     return null;
/*    */   }
/*    */   
/*    */   public static void addParty(Party party) {
/* 24 */     parties.add(party);
/*    */     
/* 26 */     System.out.println("Party created by " + party.getLeader().getIgn() + " has been loaded into memory");
/*    */   }
/*    */   
/*    */   public static void removeParty(Party party) {
/* 30 */     parties.remove(party);
/*    */   }
/*    */   
/*    */   public static boolean containsParty(Party party) {
/* 34 */     return parties.contains(party);
/*    */   }
/*    */   
/*    */   public static void initializeParty(Party party) {
/* 38 */     if (!containsParty(party))
/* 39 */       addParty(party); 
/*    */   }
/*    */   
/*    */   public static List<Party> getParties() {
/* 43 */     return parties;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\cache\PartyCache.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */