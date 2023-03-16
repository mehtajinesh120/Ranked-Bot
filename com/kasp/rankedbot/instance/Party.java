/*    */ package com.kasp.rankedbot.instance;
/*    */ 
/*    */ import com.kasp.rankedbot.config.Config;
/*    */ import com.kasp.rankedbot.instance.cache.PartyCache;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Timer;
/*    */ import java.util.TimerTask;
/*    */ 
/*    */ 
/*    */ public class Party
/*    */ {
/*    */   private Player leader;
/*    */   private List<Player> members;
/*    */   private List<Player> invitedPlayers;
/*    */   
/*    */   public Party(Player leader) {
/* 18 */     this.leader = leader;
/* 19 */     this.members = new ArrayList<>();
/* 20 */     this.invitedPlayers = new ArrayList<>();
/* 21 */     this.members.add(leader);
/*    */     
/* 23 */     PartyCache.initializeParty(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void invite(final Player invited) {
/* 28 */     this.invitedPlayers.add(invited);
/*    */     
/* 30 */     TimerTask inviteExpiration = new TimerTask()
/*    */       {
/*    */         public void run() {
/* 33 */           if (Party.this.invitedPlayers.contains(invited)) {
/* 34 */             Party.this.invitedPlayers.remove(invited);
/*    */           }
/*    */         }
/*    */       };
/* 38 */     (new Timer()).schedule(inviteExpiration, (Integer.parseInt(Config.getValue("invite-expiration")) * 60) * 1000L);
/*    */   }
/*    */   
/*    */   public void disband() {
/* 42 */     PartyCache.removeParty(this);
/*    */   }
/*    */   
/*    */   public void promote(Player player) {
/* 46 */     this.leader = player;
/*    */   }
/*    */   
/*    */   public Player getLeader() {
/* 50 */     return this.leader;
/*    */   }
/*    */   public void setLeader(Player leader) {
/* 53 */     this.leader = leader;
/*    */   }
/*    */   public List<Player> getMembers() {
/* 56 */     return this.members;
/*    */   }
/*    */   public void setMembers(List<Player> members) {
/* 59 */     this.members = members;
/*    */   }
/*    */   public List<Player> getInvitedPlayers() {
/* 62 */     return this.invitedPlayers;
/*    */   }
/*    */   public void setInvitedPlayers(List<Player> invitedPlayers) {
/* 65 */     this.invitedPlayers = invitedPlayers;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\Party.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */