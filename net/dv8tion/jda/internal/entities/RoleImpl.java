/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import gnu.trove.map.TLongObjectMap;
/*     */ import java.awt.Color;
/*     */ import java.time.OffsetDateTime;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.PermissionOverride;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.entities.RoleIcon;
/*     */ import net.dv8tion.jda.api.exceptions.HierarchyException;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.managers.RoleManager;
/*     */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.RoleAction;
/*     */ import net.dv8tion.jda.api.utils.cache.CacheFlag;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.managers.RoleManagerImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.requests.restaction.AuditableRestActionImpl;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.PermissionUtil;
/*     */ import net.dv8tion.jda.internal.utils.cache.SortedSnowflakeCacheViewImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RoleImpl
/*     */   implements Role
/*     */ {
/*     */   private final long id;
/*     */   private final JDAImpl api;
/*     */   private Guild guild;
/*     */   private RoleManager manager;
/*     */   private RoleTagsImpl tags;
/*     */   private String name;
/*     */   private boolean managed;
/*     */   private boolean hoisted;
/*     */   private boolean mentionable;
/*     */   private long rawPermissions;
/*     */   private int color;
/*     */   private int rawPosition;
/*     */   private RoleIcon icon;
/*     */   
/*     */   public RoleImpl(long id, Guild guild) {
/*  66 */     this.id = id;
/*  67 */     this.api = (JDAImpl)guild.getJDA();
/*  68 */     this.guild = guild;
/*  69 */     this.tags = this.api.isCacheFlagSet(CacheFlag.ROLE_TAGS) ? new RoleTagsImpl() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPosition() {
/*  75 */     Guild guild = getGuild();
/*  76 */     if (equals(guild.getPublicRole())) {
/*  77 */       return -1;
/*     */     }
/*     */     
/*  80 */     int i = guild.getRoles().size() - 2;
/*  81 */     for (Role r : guild.getRoles()) {
/*     */       
/*  83 */       if (equals(r))
/*  84 */         return i; 
/*  85 */       i--;
/*     */     } 
/*  87 */     throw new IllegalStateException("Somehow when determining position we never found the role in the Guild's roles? wtf?");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPositionRaw() {
/*  93 */     return this.rawPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getName() {
/* 100 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isManaged() {
/* 106 */     return this.managed;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHoisted() {
/* 112 */     return this.hoisted;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMentionable() {
/* 118 */     return this.mentionable;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getPermissionsRaw() {
/* 124 */     return this.rawPermissions;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getPermissions() {
/* 131 */     return Permission.getPermissions(this.rawPermissions);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getPermissions(@Nonnull GuildChannel channel) {
/* 138 */     return Permission.getPermissions(PermissionUtil.getEffectivePermission(channel, this));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getPermissionsExplicit() {
/* 145 */     return getPermissions();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getPermissionsExplicit(@Nonnull GuildChannel channel) {
/* 152 */     return Permission.getPermissions(PermissionUtil.getExplicitPermission(channel, this));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getColor() {
/* 158 */     return (this.color != 536870911) ? new Color(this.color) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColorRaw() {
/* 164 */     return this.color;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPublicRole() {
/* 170 */     return equals(getGuild().getPublicRole());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPermission(@Nonnull Permission... permissions) {
/* 176 */     long effectivePerms = this.rawPermissions | getGuild().getPublicRole().getPermissionsRaw();
/* 177 */     for (Permission perm : permissions) {
/*     */       
/* 179 */       long rawValue = perm.getRawValue();
/* 180 */       if ((effectivePerms & rawValue) != rawValue)
/* 181 */         return false; 
/*     */     } 
/* 183 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPermission(@Nonnull Collection<Permission> permissions) {
/* 189 */     Checks.notNull(permissions, "Permission Collection");
/*     */     
/* 191 */     return hasPermission(permissions.<Permission>toArray(Permission.EMPTY_PERMISSIONS));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPermission(@Nonnull GuildChannel channel, @Nonnull Permission... permissions) {
/* 197 */     long effectivePerms = PermissionUtil.getEffectivePermission(channel, this);
/* 198 */     for (Permission perm : permissions) {
/*     */       
/* 200 */       long rawValue = perm.getRawValue();
/* 201 */       if ((effectivePerms & rawValue) != rawValue)
/* 202 */         return false; 
/*     */     } 
/* 204 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPermission(@Nonnull GuildChannel channel, @Nonnull Collection<Permission> permissions) {
/* 210 */     Checks.notNull(permissions, "Permission Collection");
/*     */     
/* 212 */     return hasPermission(channel, permissions.<Permission>toArray(Permission.EMPTY_PERMISSIONS));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canSync(@Nonnull GuildChannel targetChannel, @Nonnull GuildChannel syncSource) {
/* 218 */     Checks.notNull(targetChannel, "Channel");
/* 219 */     Checks.notNull(syncSource, "Channel");
/* 220 */     Checks.check(targetChannel.getGuild().equals(getGuild()), "Channels must be from the same guild!");
/* 221 */     Checks.check(syncSource.getGuild().equals(getGuild()), "Channels must be from the same guild!");
/* 222 */     long rolePerms = PermissionUtil.getEffectivePermission(targetChannel, this);
/* 223 */     if ((rolePerms & Permission.MANAGE_PERMISSIONS.getRawValue()) == 0L) {
/* 224 */       return false;
/*     */     }
/* 226 */     long channelPermissions = PermissionUtil.getExplicitPermission(targetChannel, this, false);
/*     */     
/* 228 */     boolean hasLocalAdmin = ((rolePerms & Permission.ADMINISTRATOR.getRawValue() | channelPermissions & Permission.MANAGE_PERMISSIONS.getRawValue()) != 0L);
/* 229 */     if (hasLocalAdmin) {
/* 230 */       return true;
/*     */     }
/* 232 */     TLongObjectMap<PermissionOverride> existingOverrides = ((AbstractChannelImpl)targetChannel).getOverrideMap();
/* 233 */     for (PermissionOverride override : syncSource.getPermissionOverrides()) {
/*     */       
/* 235 */       PermissionOverride existing = (PermissionOverride)existingOverrides.get(override.getIdLong());
/* 236 */       long allow = override.getAllowedRaw();
/* 237 */       long deny = override.getDeniedRaw();
/* 238 */       if (existing != null) {
/*     */         
/* 240 */         allow ^= existing.getAllowedRaw();
/* 241 */         deny ^= existing.getDeniedRaw();
/*     */       } 
/*     */       
/* 244 */       if (((allow | deny) & (rolePerms ^ 0xFFFFFFFFFFFFFFFFL)) != 0L)
/* 245 */         return false; 
/*     */     } 
/* 247 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canSync(@Nonnull GuildChannel channel) {
/* 253 */     Checks.notNull(channel, "Channel");
/* 254 */     Checks.check(channel.getGuild().equals(getGuild()), "Channels must be from the same guild!");
/* 255 */     long rolePerms = PermissionUtil.getEffectivePermission(channel, this);
/* 256 */     if ((rolePerms & Permission.MANAGE_PERMISSIONS.getRawValue()) == 0L) {
/* 257 */       return false;
/*     */     }
/* 259 */     long channelPermissions = PermissionUtil.getExplicitPermission(channel, this, false);
/*     */     
/* 261 */     return ((rolePerms & Permission.ADMINISTRATOR.getRawValue() | channelPermissions & Permission.MANAGE_PERMISSIONS.getRawValue()) != 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canInteract(@Nonnull Role role) {
/* 267 */     return PermissionUtil.canInteract(this, role);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild getGuild() {
/* 274 */     Guild realGuild = this.api.getGuildById(this.guild.getIdLong());
/* 275 */     if (realGuild != null)
/* 276 */       this.guild = realGuild; 
/* 277 */     return this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RoleAction createCopy(@Nonnull Guild guild) {
/* 284 */     Checks.notNull(guild, "Guild");
/* 285 */     return guild.createRole()
/* 286 */       .setColor(Integer.valueOf(this.color))
/* 287 */       .setHoisted(Boolean.valueOf(this.hoisted))
/* 288 */       .setMentionable(Boolean.valueOf(this.mentionable))
/* 289 */       .setName(this.name)
/* 290 */       .setPermissions(Long.valueOf(this.rawPermissions))
/* 291 */       .setIcon((this.icon == null) ? null : this.icon.getEmoji());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RoleManager getManager() {
/* 298 */     if (this.manager == null)
/* 299 */       return this.manager = (RoleManager)new RoleManagerImpl(this); 
/* 300 */     return this.manager;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<Void> delete() {
/* 307 */     Guild guild = getGuild();
/* 308 */     if (!guild.getSelfMember().hasPermission(new Permission[] { Permission.MANAGE_ROLES }))
/* 309 */       throw new InsufficientPermissionException(guild, Permission.MANAGE_ROLES); 
/* 310 */     if (!PermissionUtil.canInteract(guild.getSelfMember(), this))
/* 311 */       throw new HierarchyException("Can't delete role >= highest self-role"); 
/* 312 */     if (this.managed) {
/* 313 */       throw new UnsupportedOperationException("Cannot delete a Role that is managed. ");
/*     */     }
/* 315 */     Route.CompiledRoute route = Route.Roles.DELETE_ROLE.compile(new String[] { guild.getId(), getId() });
/* 316 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl(getJDA(), route);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/* 323 */     return (JDA)this.api;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Role.RoleTags getTags() {
/* 330 */     return (this.tags == null) ? RoleTagsImpl.EMPTY : this.tags;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public RoleIcon getIcon() {
/* 337 */     return this.icon;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getAsMention() {
/* 344 */     return isPublicRole() ? "@everyone" : ("<@&" + getId() + '>');
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/* 350 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 356 */     if (o == this)
/* 357 */       return true; 
/* 358 */     if (!(o instanceof Role))
/* 359 */       return false; 
/* 360 */     Role oRole = (Role)o;
/* 361 */     return (getIdLong() == oRole.getIdLong());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 367 */     return Long.hashCode(this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 373 */     return "R:" + getName() + '(' + this.id + ')';
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(@Nonnull Role r) {
/* 379 */     if (this == r)
/* 380 */       return 0; 
/* 381 */     if (!(r instanceof RoleImpl))
/* 382 */       throw new IllegalArgumentException("Cannot compare different role implementations"); 
/* 383 */     RoleImpl impl = (RoleImpl)r;
/*     */     
/* 385 */     if (this.guild.getIdLong() != impl.guild.getIdLong()) {
/* 386 */       throw new IllegalArgumentException("Cannot compare roles that aren't from the same guild!");
/*     */     }
/* 388 */     if (getPositionRaw() != r.getPositionRaw()) {
/* 389 */       return getPositionRaw() - r.getPositionRaw();
/*     */     }
/* 391 */     OffsetDateTime thisTime = getTimeCreated();
/* 392 */     OffsetDateTime rTime = r.getTimeCreated();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 397 */     return rTime.compareTo(thisTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RoleImpl setName(String name) {
/* 404 */     this.name = name;
/* 405 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RoleImpl setColor(int color) {
/* 410 */     this.color = color;
/* 411 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RoleImpl setManaged(boolean managed) {
/* 416 */     this.managed = managed;
/* 417 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RoleImpl setHoisted(boolean hoisted) {
/* 422 */     this.hoisted = hoisted;
/* 423 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RoleImpl setMentionable(boolean mentionable) {
/* 428 */     this.mentionable = mentionable;
/* 429 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RoleImpl setRawPermissions(long rawPermissions) {
/* 434 */     this.rawPermissions = rawPermissions;
/* 435 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RoleImpl setRawPosition(int rawPosition) {
/* 440 */     SortedSnowflakeCacheViewImpl<Role> roleCache = (SortedSnowflakeCacheViewImpl<Role>)getGuild().getRoleCache();
/* 441 */     roleCache.clearCachedLists();
/* 442 */     this.rawPosition = rawPosition;
/* 443 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RoleImpl setTags(DataObject tags) {
/* 448 */     if (this.tags == null)
/* 449 */       return this; 
/* 450 */     this.tags = new RoleTagsImpl(tags);
/* 451 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RoleImpl setIcon(RoleIcon icon) {
/* 456 */     this.icon = icon;
/* 457 */     return this;
/*     */   }
/*     */   
/*     */   public static class RoleTagsImpl
/*     */     implements Role.RoleTags {
/* 462 */     public static final Role.RoleTags EMPTY = new RoleTagsImpl();
/*     */     
/*     */     private final long botId;
/*     */     private final long integrationId;
/*     */     private final boolean premiumSubscriber;
/*     */     
/*     */     public RoleTagsImpl() {
/* 469 */       this.botId = 0L;
/* 470 */       this.integrationId = 0L;
/* 471 */       this.premiumSubscriber = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public RoleTagsImpl(DataObject tags) {
/* 476 */       this.botId = tags.hasKey("bot_id") ? tags.getUnsignedLong("bot_id") : 0L;
/* 477 */       this.integrationId = tags.hasKey("integration_id") ? tags.getUnsignedLong("integration_id") : 0L;
/* 478 */       this.premiumSubscriber = tags.hasKey("premium_subscriber");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isBot() {
/* 484 */       return (this.botId != 0L);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public long getBotIdLong() {
/* 490 */       return this.botId;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isBoost() {
/* 496 */       return this.premiumSubscriber;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isIntegration() {
/* 502 */       return (this.integrationId != 0L);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public long getIntegrationIdLong() {
/* 508 */       return this.integrationId;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 514 */       return Objects.hash(new Object[] { Long.valueOf(this.botId), Long.valueOf(this.integrationId), Boolean.valueOf(this.premiumSubscriber) });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 520 */       if (obj == this)
/* 521 */         return true; 
/* 522 */       if (!(obj instanceof RoleTagsImpl))
/* 523 */         return false; 
/* 524 */       RoleTagsImpl other = (RoleTagsImpl)obj;
/* 525 */       return (this.botId == other.botId && this.integrationId == other.integrationId && this.premiumSubscriber == other.premiumSubscriber);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 533 */       return "RoleTags(bot=" + getBotId() + ",integration=" + getIntegrationId() + ",boost=" + isBoost() + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\RoleImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */