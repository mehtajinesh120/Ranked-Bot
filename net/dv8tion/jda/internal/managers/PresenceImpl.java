/*     */ package net.dv8tion.jda.internal.managers;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.OnlineStatus;
/*     */ import net.dv8tion.jda.api.entities.Activity;
/*     */ import net.dv8tion.jda.api.managers.Presence;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PresenceImpl
/*     */   implements Presence
/*     */ {
/*     */   private final JDAImpl api;
/*     */   private boolean idle = false;
/*  42 */   private Activity activity = null;
/*  43 */   private OnlineStatus status = OnlineStatus.ONLINE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PresenceImpl(JDAImpl jda) {
/*  53 */     this.api = jda;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/*  64 */     return (JDA)this.api;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public OnlineStatus getStatus() {
/*  71 */     return this.status;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Activity getActivity() {
/*  77 */     return this.activity;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIdle() {
/*  83 */     return this.idle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatus(OnlineStatus status) {
/*  93 */     setPresence(status, this.activity, this.idle);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setActivity(Activity game) {
/*  99 */     setPresence(this.status, game);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIdle(boolean idle) {
/* 105 */     setPresence(this.status, idle);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPresence(OnlineStatus status, Activity activity, boolean idle) {
/* 111 */     Checks.check((status != OnlineStatus.UNKNOWN), "Cannot set the presence status to an unknown OnlineStatus!");
/*     */     
/* 113 */     if (status == OnlineStatus.OFFLINE || status == null) {
/* 114 */       status = OnlineStatus.INVISIBLE;
/*     */     }
/* 116 */     this.idle = idle;
/* 117 */     this.status = status;
/* 118 */     this.activity = activity;
/* 119 */     update();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPresence(OnlineStatus status, Activity activity) {
/* 125 */     setPresence(status, activity, this.idle);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPresence(OnlineStatus status, boolean idle) {
/* 131 */     setPresence(status, this.activity, idle);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPresence(Activity game, boolean idle) {
/* 137 */     setPresence(this.status, game, idle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PresenceImpl setCacheStatus(OnlineStatus status) {
/* 146 */     if (status == null)
/* 147 */       throw new NullPointerException("Null OnlineStatus is not allowed."); 
/* 148 */     if (status == OnlineStatus.OFFLINE)
/* 149 */       status = OnlineStatus.INVISIBLE; 
/* 150 */     this.status = status;
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public PresenceImpl setCacheActivity(Activity game) {
/* 156 */     this.activity = game;
/* 157 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public PresenceImpl setCacheIdle(boolean idle) {
/* 162 */     this.idle = idle;
/* 163 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DataObject getFullPresence() {
/* 172 */     DataObject activity = getGameJson(this.activity);
/* 173 */     return DataObject.empty()
/* 174 */       .put("afk", Boolean.valueOf(this.idle))
/* 175 */       .put("since", Long.valueOf(System.currentTimeMillis()))
/* 176 */       .put("activities", DataArray.fromCollection((activity == null) ? 
/* 177 */           Collections.emptyList() : 
/* 178 */           Collections.<DataObject>singletonList(activity)))
/* 179 */       .put("status", getStatus().getKey());
/*     */   }
/*     */ 
/*     */   
/*     */   private DataObject getGameJson(Activity activity) {
/* 184 */     if (activity == null || activity.getName() == null || activity.getType() == null)
/* 185 */       return null; 
/* 186 */     DataObject gameObj = DataObject.empty();
/* 187 */     gameObj.put("name", activity.getName());
/* 188 */     gameObj.put("type", Integer.valueOf(activity.getType().getKey()));
/* 189 */     if (activity.getUrl() != null) {
/* 190 */       gameObj.put("url", activity.getUrl());
/*     */     }
/* 192 */     return gameObj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void update() {
/* 201 */     DataObject data = getFullPresence();
/* 202 */     JDA.Status status = this.api.getStatus();
/* 203 */     if (status == JDA.Status.RECONNECT_QUEUED || status == JDA.Status.SHUTDOWN || status == JDA.Status.SHUTTING_DOWN)
/*     */       return; 
/* 205 */     this.api.getClient().send(DataObject.empty()
/* 206 */         .put("d", data)
/* 207 */         .put("op", Integer.valueOf(3)));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\managers\PresenceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */