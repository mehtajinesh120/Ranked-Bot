/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
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
/*     */ public class MessageActivity
/*     */ {
/*     */   private final ActivityType type;
/*     */   private final String partyId;
/*     */   private final Application application;
/*     */   
/*     */   public MessageActivity(ActivityType type, String partyId, Application application) {
/*  34 */     this.type = type;
/*  35 */     this.partyId = partyId;
/*  36 */     this.application = application;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ActivityType getType() {
/*  47 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getPartyId() {
/*  58 */     return this.partyId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Application getApplication() {
/*  69 */     return this.application;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Application
/*     */     implements ISnowflake
/*     */   {
/*     */     private final String name;
/*     */     
/*     */     private final String description;
/*     */     
/*     */     private final String iconId;
/*     */     private final String coverId;
/*     */     private final long id;
/*     */     
/*     */     public Application(String name, String description, String iconId, String coverId, long id) {
/*  85 */       this.name = name;
/*  86 */       this.description = description;
/*  87 */       this.iconId = iconId;
/*  88 */       this.coverId = coverId;
/*  89 */       this.id = id;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getName() {
/* 100 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getDescription() {
/* 111 */       return this.description;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getIconId() {
/* 122 */       return this.iconId;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getIconUrl() {
/* 133 */       return (this.iconId == null) ? null : ("https://cdn.discordapp.com/application/" + getId() + "/" + this.iconId + ".png");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getCoverId() {
/* 144 */       return this.coverId;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getCoverUrl() {
/* 155 */       return (this.coverId == null) ? null : ("https://cdn.discordapp.com/application/" + getId() + "/" + this.coverId + ".png");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public long getIdLong() {
/* 161 */       return this.id;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum ActivityType
/*     */   {
/* 173 */     JOIN(1),
/*     */ 
/*     */ 
/*     */     
/* 177 */     SPECTATE(2),
/*     */ 
/*     */ 
/*     */     
/* 181 */     LISTENING(3),
/*     */ 
/*     */ 
/*     */     
/* 185 */     JOIN_REQUEST(5),
/*     */ 
/*     */ 
/*     */     
/* 189 */     UNKNOWN(-1);
/*     */     
/*     */     private final int id;
/*     */ 
/*     */     
/*     */     ActivityType(int id) {
/* 195 */       this.id = id;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getId() {
/* 205 */       return this.id;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public static ActivityType fromId(int id) {
/* 211 */       for (ActivityType activityType : values()) {
/*     */         
/* 213 */         if (activityType.id == id)
/* 214 */           return activityType; 
/*     */       } 
/* 216 */       return UNKNOWN;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\MessageActivity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */