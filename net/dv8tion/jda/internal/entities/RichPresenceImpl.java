/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.Activity;
/*     */ import net.dv8tion.jda.api.entities.ActivityFlag;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RichPresenceImpl
/*     */   extends ActivityImpl
/*     */   implements RichPresence
/*     */ {
/*     */   protected final long applicationId;
/*     */   protected final RichPresence.Party party;
/*     */   protected final String details;
/*     */   protected final String state;
/*     */   protected final RichPresence.Image largeImage;
/*     */   protected final RichPresence.Image smallImage;
/*     */   protected final String sessionId;
/*     */   protected final String syncId;
/*     */   protected final int flags;
/*     */   
/*     */   protected RichPresenceImpl(Activity.ActivityType type, String name, String url, long applicationId, Activity.Emoji emoji, RichPresence.Party party, String details, String state, Activity.Timestamps timestamps, String syncId, String sessionId, int flags, String largeImageKey, String largeImageText, String smallImageKey, String smallImageText) {
/*  45 */     super(name, url, type, timestamps, emoji);
/*  46 */     this.applicationId = applicationId;
/*  47 */     this.party = party;
/*  48 */     this.details = details;
/*  49 */     this.state = state;
/*  50 */     this.sessionId = sessionId;
/*  51 */     this.syncId = syncId;
/*  52 */     this.flags = flags;
/*  53 */     this.largeImage = (largeImageKey != null) ? new RichPresence.Image(applicationId, largeImageKey, largeImageText) : null;
/*  54 */     this.smallImage = (smallImageKey != null) ? new RichPresence.Image(applicationId, smallImageKey, smallImageText) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRich() {
/*  60 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RichPresence asRichPresence() {
/*  66 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getApplicationIdLong() {
/*  72 */     return this.applicationId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getApplicationId() {
/*  79 */     return Long.toUnsignedString(this.applicationId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getSessionId() {
/*  86 */     return this.sessionId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getSyncId() {
/*  93 */     return this.syncId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFlags() {
/*  99 */     return this.flags;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EnumSet<ActivityFlag> getFlagSet() {
/* 105 */     return ActivityFlag.getFlags(getFlags());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getState() {
/* 112 */     return this.state;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getDetails() {
/* 119 */     return this.details;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public RichPresence.Party getParty() {
/* 126 */     return this.party;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public RichPresence.Image getLargeImage() {
/* 133 */     return this.largeImage;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public RichPresence.Image getSmallImage() {
/* 140 */     return this.smallImage;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 146 */     return String.format("RichPresence(%s / %s)", new Object[] { this.name, getApplicationId() });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 152 */     return Objects.hash(new Object[] { Long.valueOf(this.applicationId), this.state, this.details, this.party, this.sessionId, this.syncId, Integer.valueOf(this.flags), this.timestamps, this.largeImage, this.smallImage });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 158 */     if (this == o)
/* 159 */       return true; 
/* 160 */     if (!(o instanceof RichPresenceImpl))
/* 161 */       return false; 
/* 162 */     RichPresenceImpl p = (RichPresenceImpl)o;
/* 163 */     return (this.applicationId == p.applicationId && 
/* 164 */       Objects.equals(this.name, p.name) && 
/* 165 */       Objects.equals(this.url, p.url) && 
/* 166 */       Objects.equals(this.type, p.type) && 
/* 167 */       Objects.equals(this.state, p.state) && 
/* 168 */       Objects.equals(this.details, p.details) && 
/* 169 */       Objects.equals(this.party, p.party) && 
/* 170 */       Objects.equals(this.sessionId, p.sessionId) && 
/* 171 */       Objects.equals(this.syncId, p.syncId) && 
/* 172 */       Objects.equals(Integer.valueOf(this.flags), Integer.valueOf(p.flags)) && 
/* 173 */       Objects.equals(this.timestamps, p.timestamps) && 
/* 174 */       Objects.equals(this.largeImage, p.largeImage) && 
/* 175 */       Objects.equals(this.smallImage, p.smallImage));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\RichPresenceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */