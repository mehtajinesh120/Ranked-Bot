/*     */ package net.dv8tion.jda.api.entities.templates;
/*     */ 
/*     */ import java.time.OffsetDateTime;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.ISnowflake;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TemplateChannel
/*     */   implements ISnowflake
/*     */ {
/*     */   private final long id;
/*     */   private final ChannelType channelType;
/*     */   private final String name;
/*     */   private final String topic;
/*     */   private final int rawPosition;
/*     */   private final long parentId;
/*     */   private final boolean isNews;
/*     */   private final List<PermissionOverride> permissionOverrides;
/*     */   private final boolean nsfw;
/*     */   private final int slowmode;
/*     */   private final int bitrate;
/*     */   private final int userLimit;
/*     */   
/*     */   public TemplateChannel(long id, ChannelType channelType, String name, String topic, int rawPosition, long parentId, boolean news, List<PermissionOverride> permissionOverrides, boolean nsfw, int slowmode, int bitrate, int userLimit) {
/*  58 */     this.id = id;
/*  59 */     this.channelType = channelType;
/*  60 */     this.name = name;
/*  61 */     this.topic = topic;
/*  62 */     this.rawPosition = rawPosition;
/*  63 */     this.parentId = parentId;
/*  64 */     this.isNews = news;
/*  65 */     this.permissionOverrides = Collections.unmodifiableList(permissionOverrides);
/*     */     
/*  67 */     this.nsfw = nsfw;
/*  68 */     this.slowmode = slowmode;
/*     */     
/*  70 */     this.bitrate = bitrate;
/*  71 */     this.userLimit = userLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/*  82 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OffsetDateTime getTimeCreated() {
/*  94 */     throw new UnsupportedOperationException("The date of creation cannot be calculated");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelType getType() {
/* 105 */     return this.channelType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getName() {
/* 117 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getTopic() {
/* 130 */     return this.topic;
/*     */   }
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
/*     */   public int getPositionRaw() {
/* 143 */     return this.rawPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getParentId() {
/* 155 */     return this.parentId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNSFW() {
/* 167 */     return this.nsfw;
/*     */   }
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
/*     */   public int getSlowmode() {
/* 184 */     return this.slowmode;
/*     */   }
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
/*     */   public int getBitrate() {
/* 197 */     return this.bitrate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUserLimit() {
/* 209 */     return this.userLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNews() {
/* 220 */     return this.isNews;
/*     */   }
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
/*     */   @Nonnull
/*     */   public List<PermissionOverride> getPermissionOverrides() {
/* 234 */     return this.permissionOverrides;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class PermissionOverride
/*     */     implements ISnowflake
/*     */   {
/*     */     private final long id;
/*     */ 
/*     */     
/*     */     private final long allow;
/*     */     
/*     */     private final long deny;
/*     */ 
/*     */     
/*     */     public PermissionOverride(long id, long allow, long deny) {
/* 251 */       this.id = id;
/* 252 */       this.allow = allow;
/* 253 */       this.deny = deny;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getAllowedRaw() {
/* 264 */       return this.allow;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getInheritRaw() {
/* 275 */       return (this.allow | this.deny) ^ 0xFFFFFFFFFFFFFFFFL;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getDeniedRaw() {
/* 286 */       return this.deny;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public EnumSet<Permission> getAllowed() {
/* 298 */       return Permission.getPermissions(this.allow);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public EnumSet<Permission> getInherit() {
/* 310 */       return Permission.getPermissions(getInheritRaw());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public EnumSet<Permission> getDenied() {
/* 322 */       return Permission.getPermissions(this.deny);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getIdLong() {
/* 333 */       return this.id;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OffsetDateTime getTimeCreated() {
/* 345 */       throw new UnsupportedOperationException("The date of creation cannot be calculated");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\templates\TemplateChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */