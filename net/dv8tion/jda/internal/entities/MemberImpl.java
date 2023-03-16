/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import gnu.trove.map.TLongObjectMap;
/*     */ import java.awt.Color;
/*     */ import java.time.OffsetDateTime;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.OnlineStatus;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Activity;
/*     */ import net.dv8tion.jda.api.entities.ClientType;
/*     */ import net.dv8tion.jda.api.entities.Emote;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.GuildVoiceState;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.PermissionOverride;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.utils.cache.CacheFlag;
/*     */ import net.dv8tion.jda.api.utils.cache.CacheView;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.Helpers;
/*     */ import net.dv8tion.jda.internal.utils.PermissionUtil;
/*     */ 
/*     */ 
/*     */ public class MemberImpl
/*     */   implements Member
/*     */ {
/*     */   private final JDAImpl api;
/*  42 */   private final Set<Role> roles = ConcurrentHashMap.newKeySet();
/*     */   
/*     */   private final GuildVoiceState voiceState;
/*     */   private GuildImpl guild;
/*     */   private User user;
/*     */   private String nickname;
/*     */   private String avatarId;
/*     */   private long joinDate;
/*     */   private long boostDate;
/*     */   private boolean pending = false;
/*     */   
/*     */   public MemberImpl(GuildImpl guild, User user) {
/*  54 */     this.api = (JDAImpl)user.getJDA();
/*  55 */     this.guild = guild;
/*  56 */     this.user = user;
/*  57 */     this.joinDate = 0L;
/*  58 */     boolean cacheState = (this.api.isCacheFlagSet(CacheFlag.VOICE_STATE) || user.equals(this.api.getSelfUser()));
/*  59 */     this.voiceState = cacheState ? new GuildVoiceStateImpl(this) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public MemberPresenceImpl getPresence() {
/*  64 */     CacheView.SimpleCacheView<MemberPresenceImpl> presences = this.guild.getPresenceView();
/*  65 */     return (presences == null) ? null : (MemberPresenceImpl)presences.get(getIdLong());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public User getUser() {
/*  73 */     User realUser = getJDA().getUserById(this.user.getIdLong());
/*  74 */     if (realUser != null)
/*  75 */       this.user = realUser; 
/*  76 */     return this.user;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public GuildImpl getGuild() {
/*  83 */     GuildImpl realGuild = (GuildImpl)this.api.getGuildById(this.guild.getIdLong());
/*  84 */     if (realGuild != null)
/*  85 */       this.guild = realGuild; 
/*  86 */     return this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/*  93 */     return (JDA)this.api;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public OffsetDateTime getTimeJoined() {
/* 100 */     if (hasTimeJoined())
/* 101 */       return Helpers.toOffset(this.joinDate); 
/* 102 */     return getGuild().getTimeCreated();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasTimeJoined() {
/* 108 */     return (this.joinDate != 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public OffsetDateTime getTimeBoosted() {
/* 115 */     return (this.boostDate != 0L) ? Helpers.toOffset(this.boostDate) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public GuildVoiceState getVoiceState() {
/* 121 */     return this.voiceState;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Activity> getActivities() {
/* 128 */     MemberPresenceImpl presence = getPresence();
/* 129 */     return (presence == null) ? Collections.<Activity>emptyList() : presence.getActivities();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public OnlineStatus getOnlineStatus() {
/* 136 */     MemberPresenceImpl presence = getPresence();
/* 137 */     return (presence == null) ? OnlineStatus.OFFLINE : presence.getOnlineStatus();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public OnlineStatus getOnlineStatus(@Nonnull ClientType type) {
/* 144 */     Checks.notNull(type, "Type");
/* 145 */     MemberPresenceImpl presence = getPresence();
/* 146 */     if (presence == null)
/* 147 */       return OnlineStatus.OFFLINE; 
/* 148 */     OnlineStatus status = presence.getClientStatus().get(type);
/* 149 */     return (status == null) ? OnlineStatus.OFFLINE : status;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<ClientType> getActiveClients() {
/* 156 */     MemberPresenceImpl presence = getPresence();
/* 157 */     return (presence == null) ? EnumSet.<ClientType>noneOf(ClientType.class) : Helpers.copyEnumSet(ClientType.class, presence.getClientStatus().keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNickname() {
/* 163 */     return this.nickname;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAvatarId() {
/* 169 */     return this.avatarId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getEffectiveName() {
/* 176 */     return (this.nickname != null) ? this.nickname : getUser().getName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Role> getRoles() {
/* 183 */     List<Role> roleList = new ArrayList<>(this.roles);
/* 184 */     roleList.sort(Comparator.reverseOrder());
/*     */     
/* 186 */     return Collections.unmodifiableList(roleList);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getColor() {
/* 192 */     int raw = getColorRaw();
/* 193 */     return (raw != 536870911) ? new Color(raw) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColorRaw() {
/* 199 */     for (Role r : getRoles()) {
/*     */       
/* 201 */       int colorRaw = r.getColorRaw();
/* 202 */       if (colorRaw != 536870911)
/* 203 */         return colorRaw; 
/*     */     } 
/* 205 */     return 536870911;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getPermissions() {
/* 212 */     return Permission.getPermissions(PermissionUtil.getEffectivePermission(this));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getPermissions(@Nonnull GuildChannel channel) {
/* 219 */     Checks.notNull(channel, "Channel");
/* 220 */     if (!getGuild().equals(channel.getGuild())) {
/* 221 */       throw new IllegalArgumentException("Provided channel is not in the same guild as this member!");
/*     */     }
/* 223 */     return Permission.getPermissions(PermissionUtil.getEffectivePermission(channel, this));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getPermissionsExplicit() {
/* 230 */     return Permission.getPermissions(PermissionUtil.getExplicitPermission(this));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getPermissionsExplicit(@Nonnull GuildChannel channel) {
/* 237 */     return Permission.getPermissions(PermissionUtil.getExplicitPermission(channel, this));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPermission(@Nonnull Permission... permissions) {
/* 243 */     return PermissionUtil.checkPermission(this, permissions);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPermission(@Nonnull Collection<Permission> permissions) {
/* 249 */     Checks.notNull(permissions, "Permission Collection");
/*     */     
/* 251 */     return hasPermission(permissions.<Permission>toArray(Permission.EMPTY_PERMISSIONS));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPermission(@Nonnull GuildChannel channel, @Nonnull Permission... permissions) {
/* 257 */     return PermissionUtil.checkPermission(channel, this, permissions);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPermission(@Nonnull GuildChannel channel, @Nonnull Collection<Permission> permissions) {
/* 263 */     Checks.notNull(permissions, "Permission Collection");
/*     */     
/* 265 */     return hasPermission(channel, permissions.<Permission>toArray(Permission.EMPTY_PERMISSIONS));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canSync(@Nonnull GuildChannel targetChannel, @Nonnull GuildChannel syncSource) {
/* 271 */     Checks.notNull(targetChannel, "Channel");
/* 272 */     Checks.notNull(syncSource, "Channel");
/* 273 */     Checks.check(targetChannel.getGuild().equals(getGuild()), "Channels must be from the same guild!");
/* 274 */     Checks.check(syncSource.getGuild().equals(getGuild()), "Channels must be from the same guild!");
/* 275 */     long userPerms = PermissionUtil.getEffectivePermission(targetChannel, this);
/* 276 */     if ((userPerms & Permission.MANAGE_PERMISSIONS.getRawValue()) == 0L) {
/* 277 */       return false;
/*     */     }
/* 279 */     long channelPermissions = PermissionUtil.getExplicitPermission(targetChannel, this, false);
/*     */     
/* 281 */     boolean hasLocalAdmin = ((userPerms & Permission.ADMINISTRATOR.getRawValue() | channelPermissions & Permission.MANAGE_PERMISSIONS.getRawValue()) != 0L);
/* 282 */     if (hasLocalAdmin) {
/* 283 */       return true;
/*     */     }
/* 285 */     TLongObjectMap<PermissionOverride> existingOverrides = ((AbstractChannelImpl)targetChannel).getOverrideMap();
/* 286 */     for (PermissionOverride override : syncSource.getPermissionOverrides()) {
/*     */       
/* 288 */       PermissionOverride existing = (PermissionOverride)existingOverrides.get(override.getIdLong());
/* 289 */       long allow = override.getAllowedRaw();
/* 290 */       long deny = override.getDeniedRaw();
/* 291 */       if (existing != null) {
/*     */         
/* 293 */         allow ^= existing.getAllowedRaw();
/* 294 */         deny ^= existing.getDeniedRaw();
/*     */       } 
/*     */       
/* 297 */       if (((allow | deny) & (userPerms ^ 0xFFFFFFFFFFFFFFFFL)) != 0L)
/* 298 */         return false; 
/*     */     } 
/* 300 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canSync(@Nonnull GuildChannel channel) {
/* 306 */     Checks.notNull(channel, "Channel");
/* 307 */     Checks.check(channel.getGuild().equals(getGuild()), "Channels must be from the same guild!");
/* 308 */     long userPerms = PermissionUtil.getEffectivePermission(channel, this);
/* 309 */     if ((userPerms & Permission.MANAGE_PERMISSIONS.getRawValue()) == 0L) {
/* 310 */       return false;
/*     */     }
/* 312 */     long channelPermissions = PermissionUtil.getExplicitPermission(channel, this, false);
/*     */     
/* 314 */     return ((userPerms & Permission.ADMINISTRATOR.getRawValue() | channelPermissions & Permission.MANAGE_PERMISSIONS.getRawValue()) != 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canInteract(@Nonnull Member member) {
/* 320 */     return PermissionUtil.canInteract(this, member);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canInteract(@Nonnull Role role) {
/* 326 */     return PermissionUtil.canInteract(this, role);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canInteract(@Nonnull Emote emote) {
/* 332 */     return PermissionUtil.canInteract(this, emote);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOwner() {
/* 338 */     return (this.user.getIdLong() == getGuild().getOwnerIdLong());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPending() {
/* 344 */     return this.pending;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/* 350 */     return this.user.getIdLong();
/*     */   }
/*     */ 
/*     */   
/*     */   public MemberImpl setNickname(String nickname) {
/* 355 */     this.nickname = nickname;
/* 356 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public MemberImpl setAvatarId(String avatarId) {
/* 361 */     this.avatarId = avatarId;
/* 362 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public MemberImpl setJoinDate(long joinDate) {
/* 367 */     this.joinDate = joinDate;
/* 368 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public MemberImpl setBoostDate(long boostDate) {
/* 373 */     this.boostDate = boostDate;
/* 374 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public MemberImpl setPending(boolean pending) {
/* 379 */     this.pending = pending;
/* 380 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Role> getRoleSet() {
/* 385 */     return this.roles;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getBoostDateRaw() {
/* 390 */     return this.boostDate;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 396 */     if (o == this)
/* 397 */       return true; 
/* 398 */     if (!(o instanceof MemberImpl)) {
/* 399 */       return false;
/*     */     }
/* 401 */     MemberImpl oMember = (MemberImpl)o;
/* 402 */     return (oMember.user.getIdLong() == this.user.getIdLong() && oMember.guild
/* 403 */       .getIdLong() == this.guild.getIdLong());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 409 */     return (this.guild.getIdLong() + this.user.getId()).hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 415 */     return "MB:" + getEffectiveName() + '(' + getUser().toString() + " / " + getGuild().toString() + ')';
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getAsMention() {
/* 422 */     return ((this.nickname == null) ? "<@" : "<@!") + this.user.getId() + '>';
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public TextChannel getDefaultChannel() {
/* 429 */     return getGuild().getTextChannelsView().stream()
/* 430 */       .sorted(Comparator.reverseOrder())
/* 431 */       .filter(c -> hasPermission((GuildChannel)c, new Permission[] { Permission.MESSAGE_READ
/* 432 */           })).findFirst().orElse(null);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\MemberImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */