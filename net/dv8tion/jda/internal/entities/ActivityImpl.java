/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.Activity;
/*     */ import net.dv8tion.jda.api.entities.RichPresence;
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
/*     */ public class ActivityImpl
/*     */   implements Activity
/*     */ {
/*     */   protected final String name;
/*     */   protected final String url;
/*     */   protected final Activity.ActivityType type;
/*     */   protected final Activity.Timestamps timestamps;
/*     */   protected final Activity.Emoji emoji;
/*     */   
/*     */   protected ActivityImpl(String name) {
/*  35 */     this(name, null, Activity.ActivityType.DEFAULT);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ActivityImpl(String name, String url) {
/*  40 */     this(name, url, Activity.ActivityType.STREAMING);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ActivityImpl(String name, String url, Activity.ActivityType type) {
/*  45 */     this(name, url, type, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ActivityImpl(String name, String url, Activity.ActivityType type, Activity.Timestamps timestamps, Activity.Emoji emoji) {
/*  50 */     this.name = name;
/*  51 */     this.url = url;
/*  52 */     this.type = type;
/*  53 */     this.timestamps = timestamps;
/*  54 */     this.emoji = emoji;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRich() {
/*  60 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RichPresence asRichPresence() {
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getName() {
/*  73 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUrl() {
/*  79 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Activity.ActivityType getType() {
/*  86 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Activity.Timestamps getTimestamps() {
/*  92 */     return this.timestamps;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Activity.Emoji getEmoji() {
/*  99 */     return this.emoji;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 105 */     if (o == this)
/* 106 */       return true; 
/* 107 */     if (!(o instanceof ActivityImpl)) {
/* 108 */       return false;
/*     */     }
/* 110 */     ActivityImpl oGame = (ActivityImpl)o;
/* 111 */     return (oGame.getType() == this.type && 
/* 112 */       Objects.equals(this.name, oGame.getName()) && 
/* 113 */       Objects.equals(this.url, oGame.getUrl()) && 
/* 114 */       Objects.equals(this.timestamps, oGame.timestamps));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 120 */     return Objects.hash(new Object[] { this.name, this.type, this.url, this.timestamps });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 126 */     if (this.url != null) {
/* 127 */       return String.format("Activity(%s | %s)", new Object[] { this.name, this.url });
/*     */     }
/* 129 */     return String.format("Activity(%s)", new Object[] { this.name });
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\ActivityImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */