/*    */ package net.dv8tion.jda.internal.entities;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.EnumMap;
/*    */ import java.util.List;
/*    */ import net.dv8tion.jda.api.OnlineStatus;
/*    */ import net.dv8tion.jda.api.entities.Activity;
/*    */ import net.dv8tion.jda.api.entities.ClientType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MemberPresenceImpl
/*    */ {
/* 29 */   private List<Activity> activities = Collections.emptyList();
/*    */   private EnumMap<ClientType, OnlineStatus> clientStatus;
/* 31 */   private OnlineStatus status = OnlineStatus.OFFLINE;
/*    */ 
/*    */   
/*    */   public void setActivities(List<Activity> activities) {
/* 35 */     this.activities = activities;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setClientStatus(EnumMap<ClientType, OnlineStatus> clientStatus) {
/* 40 */     this.clientStatus = clientStatus;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setOnlineStatus(OnlineStatus status) {
/* 45 */     this.status = status;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Activity> getActivities() {
/* 50 */     return this.activities;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnumMap<ClientType, OnlineStatus> getClientStatus() {
/* 55 */     if (this.clientStatus == null)
/* 56 */       return new EnumMap<>(ClientType.class); 
/* 57 */     return this.clientStatus;
/*    */   }
/*    */ 
/*    */   
/*    */   public OnlineStatus getOnlineStatus() {
/* 62 */     return this.status;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setOnlineStatus(ClientType type, OnlineStatus clientStatus) {
/* 67 */     if (this.clientStatus == null) {
/*    */       
/* 69 */       if (clientStatus == null || clientStatus == OnlineStatus.OFFLINE)
/*    */         return; 
/* 71 */       this.clientStatus = new EnumMap<>(ClientType.class);
/*    */     } 
/* 73 */     if (clientStatus == OnlineStatus.OFFLINE) {
/* 74 */       this.clientStatus.remove(type);
/*    */     } else {
/* 76 */       this.clientStatus.put(type, clientStatus);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\MemberPresenceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */