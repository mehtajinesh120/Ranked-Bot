/*     */ package net.dv8tion.jda.api.requests.restaction;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.annotations.DeprecatedSince;
/*     */ import net.dv8tion.jda.annotations.ForRemoval;
/*     */ import net.dv8tion.jda.annotations.ReplaceWith;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.Region;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Icon;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.api.utils.data.SerializableData;
/*     */ import net.dv8tion.jda.internal.requests.restaction.PermOverrideData;
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
/*     */ public interface GuildAction
/*     */   extends RestAction<Void>
/*     */ {
/*     */   @Nonnull
/*     */   GuildAction setCheck(@Nullable BooleanSupplier paramBooleanSupplier);
/*     */   
/*     */   @Nonnull
/*     */   GuildAction timeout(long paramLong, @Nonnull TimeUnit paramTimeUnit);
/*     */   
/*     */   @Nonnull
/*     */   GuildAction deadline(long paramLong);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   @Deprecated
/*     */   @ForRemoval(deadline = "5.0.0")
/*     */   @ReplaceWith("ChannelManager.setRegion()")
/*     */   @DeprecatedSince("4.3.0")
/*     */   GuildAction setRegion(@Nullable Region paramRegion);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   GuildAction setIcon(@Nullable Icon paramIcon);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   GuildAction setName(@Nonnull String paramString);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   GuildAction setVerificationLevel(@Nullable Guild.VerificationLevel paramVerificationLevel);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   GuildAction setNotificationLevel(@Nullable Guild.NotificationLevel paramNotificationLevel);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   GuildAction setExplicitContentLevel(@Nullable Guild.ExplicitContentLevel paramExplicitContentLevel);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   GuildAction addChannel(@Nonnull ChannelData paramChannelData);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelData getChannel(int paramInt);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelData removeChannel(int paramInt);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   GuildAction removeChannel(@Nonnull ChannelData paramChannelData);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelData newChannel(@Nonnull ChannelType paramChannelType, @Nonnull String paramString);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   RoleData getPublicRole();
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   RoleData getRole(int paramInt);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   RoleData newRole();
/*     */   
/*     */   public static class RoleData
/*     */     implements SerializableData
/*     */   {
/*     */     protected final long id;
/*     */     protected final boolean isPublicRole;
/*     */     protected Long permissions;
/*     */     protected String name;
/*     */     protected Integer color;
/*     */     protected Integer position;
/*     */     protected Boolean mentionable;
/*     */     protected Boolean hoisted;
/*     */     
/*     */     public RoleData(long id) {
/* 304 */       this.id = id;
/* 305 */       this.isPublicRole = (id == 0L);
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
/*     */     
/*     */     @Nonnull
/*     */     public RoleData setPermissionsRaw(@Nullable Long rawPermissions) {
/* 319 */       this.permissions = rawPermissions;
/* 320 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public RoleData addPermissions(@Nonnull Permission... permissions) {
/* 337 */       Checks.notNull(permissions, "Permissions");
/* 338 */       for (Permission perm : permissions)
/* 339 */         Checks.notNull(perm, "Permissions"); 
/* 340 */       if (this.permissions == null)
/* 341 */         this.permissions = Long.valueOf(0L); 
/* 342 */       this.permissions = Long.valueOf(this.permissions.longValue() | Permission.getRaw(permissions));
/* 343 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public RoleData addPermissions(@Nonnull Collection<Permission> permissions) {
/* 360 */       Checks.noneNull(permissions, "Permissions");
/* 361 */       if (this.permissions == null)
/* 362 */         this.permissions = Long.valueOf(0L); 
/* 363 */       this.permissions = Long.valueOf(this.permissions.longValue() | Permission.getRaw(permissions));
/* 364 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public RoleData setName(@Nullable String name) {
/* 381 */       checkPublic("name");
/* 382 */       this.name = name;
/* 383 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public RoleData setColor(@Nullable Color color) {
/* 400 */       checkPublic("color");
/* 401 */       this.color = (color == null) ? null : Integer.valueOf(color.getRGB());
/* 402 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public RoleData setColor(@Nullable Integer color) {
/* 419 */       checkPublic("color");
/* 420 */       this.color = color;
/* 421 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public RoleData setPosition(@Nullable Integer position) {
/* 438 */       checkPublic("position");
/* 439 */       this.position = position;
/* 440 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public RoleData setMentionable(@Nullable Boolean mentionable) {
/* 457 */       checkPublic("mentionable");
/* 458 */       this.mentionable = mentionable;
/* 459 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public RoleData setHoisted(@Nullable Boolean hoisted) {
/* 476 */       checkPublic("hoisted");
/* 477 */       this.hoisted = hoisted;
/* 478 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public DataObject toData() {
/* 485 */       DataObject o = DataObject.empty().put("id", Long.toUnsignedString(this.id));
/* 486 */       if (this.permissions != null)
/* 487 */         o.put("permissions", this.permissions); 
/* 488 */       if (this.position != null)
/* 489 */         o.put("position", this.position); 
/* 490 */       if (this.name != null)
/* 491 */         o.put("name", this.name); 
/* 492 */       if (this.color != null)
/* 493 */         o.put("color", Integer.valueOf(this.color.intValue() & 0xFFFFFF)); 
/* 494 */       if (this.mentionable != null)
/* 495 */         o.put("mentionable", this.mentionable); 
/* 496 */       if (this.hoisted != null)
/* 497 */         o.put("hoist", this.hoisted); 
/* 498 */       return o;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void checkPublic(String comment) {
/* 503 */       if (this.isPublicRole) {
/* 504 */         throw new IllegalStateException("Cannot modify " + comment + " for the public role!");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ChannelData
/*     */     implements SerializableData
/*     */   {
/*     */     protected final ChannelType type;
/*     */ 
/*     */     
/*     */     protected final String name;
/*     */     
/* 519 */     protected final Set<PermOverrideData> overrides = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Integer position;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected String topic;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Boolean nsfw;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Integer bitrate;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Integer userlimit;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ChannelData(ChannelType type, String name) {
/* 550 */       Checks.notBlank(name, "Name");
/* 551 */       Checks.check((type == ChannelType.TEXT || type == ChannelType.VOICE || type == ChannelType.STAGE), "Can only create channels of type TEXT, STAGE, or VOICE in GuildAction!");
/*     */       
/* 553 */       Checks.check((name.length() >= 2 && name.length() <= 100), "Channel name has to be between 2-100 characters long!");
/*     */       
/* 555 */       Checks.check((type == ChannelType.VOICE || type == ChannelType.STAGE || name.matches("[a-zA-Z0-9-_]+")), "Channels of type TEXT must have a name in alphanumeric with underscores!");
/*     */ 
/*     */       
/* 558 */       this.type = type;
/* 559 */       this.name = name;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public ChannelData setTopic(@Nullable String topic) {
/* 577 */       if (topic != null && topic.length() > 1024)
/* 578 */         throw new IllegalArgumentException("Channel Topic must not be greater than 1024 in length!"); 
/* 579 */       this.topic = topic;
/* 580 */       return this;
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
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public ChannelData setNSFW(@Nullable Boolean nsfw) {
/* 595 */       this.nsfw = nsfw;
/* 596 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public ChannelData setBitrate(@Nullable Integer bitrate) {
/* 614 */       if (bitrate != null) {
/*     */         
/* 616 */         Checks.check((bitrate.intValue() >= 8000), "Bitrate must be greater than 8000.");
/* 617 */         Checks.check((bitrate.intValue() <= 96000), "Bitrate must be less than 96000.");
/*     */       } 
/* 619 */       this.bitrate = bitrate;
/* 620 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public ChannelData setUserlimit(@Nullable Integer userlimit) {
/* 638 */       if (userlimit != null && (userlimit.intValue() < 0 || userlimit.intValue() > 99))
/* 639 */         throw new IllegalArgumentException("Userlimit must be between 0-99!"); 
/* 640 */       this.userlimit = userlimit;
/* 641 */       return this;
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
/*     */     
/*     */     @Nonnull
/*     */     public ChannelData setPosition(@Nullable Integer position) {
/* 655 */       this.position = position;
/* 656 */       return this;
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
/*     */     @Nonnull
/*     */     public ChannelData addPermissionOverride(@Nonnull GuildAction.RoleData role, long allow, long deny) {
/* 679 */       Checks.notNull(role, "Role");
/* 680 */       this.overrides.add(new PermOverrideData(0, role.id, allow, deny));
/* 681 */       return this;
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
/*     */     @Nonnull
/*     */     public ChannelData addPermissionOverride(@Nonnull GuildAction.RoleData role, @Nullable Collection<Permission> allow, @Nullable Collection<Permission> deny) {
/* 707 */       long allowRaw = 0L;
/* 708 */       long denyRaw = 0L;
/* 709 */       if (allow != null) {
/*     */         
/* 711 */         Checks.noneNull(allow, "Granted Permissions");
/* 712 */         allowRaw = Permission.getRaw(allow);
/*     */       } 
/* 714 */       if (deny != null) {
/*     */         
/* 716 */         Checks.noneNull(deny, "Denied Permissions");
/* 717 */         denyRaw = Permission.getRaw(deny);
/*     */       } 
/* 719 */       return addPermissionOverride(role, allowRaw, denyRaw);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public DataObject toData() {
/* 726 */       DataObject o = DataObject.empty();
/* 727 */       o.put("name", this.name);
/* 728 */       o.put("type", Integer.valueOf(this.type.getId()));
/* 729 */       if (this.topic != null)
/* 730 */         o.put("topic", this.topic); 
/* 731 */       if (this.nsfw != null)
/* 732 */         o.put("nsfw", this.nsfw); 
/* 733 */       if (this.bitrate != null)
/* 734 */         o.put("bitrate", this.bitrate); 
/* 735 */       if (this.userlimit != null)
/* 736 */         o.put("user_limit", this.userlimit); 
/* 737 */       if (this.position != null)
/* 738 */         o.put("position", this.position); 
/* 739 */       if (!this.overrides.isEmpty())
/* 740 */         o.put("permission_overwrites", this.overrides); 
/* 741 */       return o;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\GuildAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */