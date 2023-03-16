/*     */ package net.dv8tion.jda.internal.managers;
/*     */ 
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.Region;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Icon;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.managers.GuildManager;
/*     */ import net.dv8tion.jda.api.managers.Manager;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import okhttp3.RequestBody;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuildManagerImpl
/*     */   extends ManagerBase<GuildManager>
/*     */   implements GuildManager
/*     */ {
/*     */   protected Guild guild;
/*     */   protected String name;
/*     */   protected String region;
/*     */   protected Icon icon;
/*     */   protected Icon splash;
/*     */   protected Icon banner;
/*     */   protected String afkChannel;
/*     */   protected String systemChannel;
/*     */   protected String rulesChannel;
/*     */   protected String communityUpdatesChannel;
/*     */   protected String description;
/*     */   protected String vanityCode;
/*     */   protected int afkTimeout;
/*     */   protected int mfaLevel;
/*     */   protected int notificationLevel;
/*     */   protected int explicitContentLevel;
/*     */   protected int verificationLevel;
/*     */   
/*     */   public GuildManagerImpl(Guild guild) {
/*  54 */     super(guild.getJDA(), Route.Guilds.MODIFY_GUILD.compile(new String[] { guild.getId() }));
/*  55 */     JDA api = guild.getJDA();
/*  56 */     this.guild = guild;
/*  57 */     if (isPermissionChecksEnabled()) {
/*  58 */       checkPermissions();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild getGuild() {
/*  65 */     Guild realGuild = this.api.getGuildById(this.guild.getIdLong());
/*  66 */     if (realGuild != null)
/*  67 */       this.guild = realGuild; 
/*  68 */     return this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildManagerImpl reset(long fields) {
/*  76 */     super.reset(fields);
/*  77 */     if ((fields & 0x1L) == 1L)
/*  78 */       this.name = null; 
/*  79 */     if ((fields & 0x2L) == 2L)
/*  80 */       this.region = null; 
/*  81 */     if ((fields & 0x4L) == 4L)
/*  82 */       this.icon = null; 
/*  83 */     if ((fields & 0x8L) == 8L)
/*  84 */       this.splash = null; 
/*  85 */     if ((fields & 0x10L) == 16L)
/*  86 */       this.afkChannel = null; 
/*  87 */     if ((fields & 0x40L) == 64L)
/*  88 */       this.systemChannel = null; 
/*  89 */     if ((fields & 0x4000L) == 16384L)
/*  90 */       this.rulesChannel = null; 
/*  91 */     if ((fields & 0x8000L) == 32768L)
/*  92 */       this.communityUpdatesChannel = null; 
/*  93 */     if ((fields & 0x2000L) == 8192L)
/*  94 */       this.description = null; 
/*  95 */     if ((fields & 0x800L) == 2048L)
/*  96 */       this.banner = null; 
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildManagerImpl reset(long... fields) {
/* 105 */     super.reset(fields);
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildManagerImpl reset() {
/* 114 */     super.reset();
/* 115 */     this.name = null;
/* 116 */     this.region = null;
/* 117 */     this.icon = null;
/* 118 */     this.splash = null;
/* 119 */     this.vanityCode = null;
/* 120 */     this.description = null;
/* 121 */     this.banner = null;
/* 122 */     this.afkChannel = null;
/* 123 */     this.systemChannel = null;
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildManagerImpl setName(@Nonnull String name) {
/* 132 */     Checks.notEmpty(name, "Name");
/* 133 */     Checks.notLonger(name, 100, "Name");
/* 134 */     this.name = name;
/* 135 */     this.set |= 0x1L;
/* 136 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildManagerImpl setRegion(@Nonnull Region region) {
/* 144 */     Checks.notNull(region, "Region");
/* 145 */     Checks.check((region != Region.UNKNOWN), "Region must not be UNKNOWN");
/* 146 */     Checks.check((!region.isVip() || getGuild().getFeatures().contains("VIP_REGIONS")), "Cannot set a VIP voice region on this guild");
/* 147 */     this.region = region.getKey();
/* 148 */     this.set |= 0x2L;
/* 149 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildManagerImpl setIcon(Icon icon) {
/* 157 */     this.icon = icon;
/* 158 */     this.set |= 0x4L;
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildManagerImpl setSplash(Icon splash) {
/* 167 */     checkFeature("INVITE_SPLASH");
/* 168 */     this.splash = splash;
/* 169 */     this.set |= 0x8L;
/* 170 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildManagerImpl setAfkChannel(VoiceChannel afkChannel) {
/* 178 */     Checks.check((afkChannel == null || afkChannel.getGuild().equals(getGuild())), "Channel must be from the same guild");
/* 179 */     this.afkChannel = (afkChannel == null) ? null : afkChannel.getId();
/* 180 */     this.set |= 0x10L;
/* 181 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildManagerImpl setSystemChannel(TextChannel systemChannel) {
/* 189 */     Checks.check((systemChannel == null || systemChannel.getGuild().equals(getGuild())), "Channel must be from the same guild");
/* 190 */     this.systemChannel = (systemChannel == null) ? null : systemChannel.getId();
/* 191 */     this.set |= 0x40L;
/* 192 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildManagerImpl setRulesChannel(TextChannel rulesChannel) {
/* 200 */     Checks.check((rulesChannel == null || rulesChannel.getGuild().equals(getGuild())), "Channel must be from the same guild");
/* 201 */     this.rulesChannel = (rulesChannel == null) ? null : rulesChannel.getId();
/* 202 */     this.set |= 0x4000L;
/* 203 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildManagerImpl setCommunityUpdatesChannel(TextChannel communityUpdatesChannel) {
/* 211 */     Checks.check((communityUpdatesChannel == null || communityUpdatesChannel.getGuild().equals(getGuild())), "Channel must be from the same guild");
/* 212 */     this.communityUpdatesChannel = (communityUpdatesChannel == null) ? null : communityUpdatesChannel.getId();
/* 213 */     this.set |= 0x8000L;
/* 214 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildManagerImpl setAfkTimeout(@Nonnull Guild.Timeout timeout) {
/* 222 */     Checks.notNull(timeout, "Timeout");
/* 223 */     this.afkTimeout = timeout.getSeconds();
/* 224 */     this.set |= 0x20L;
/* 225 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildManagerImpl setVerificationLevel(@Nonnull Guild.VerificationLevel level) {
/* 233 */     Checks.notNull(level, "Level");
/* 234 */     Checks.check((level != Guild.VerificationLevel.UNKNOWN), "Level must not be UNKNOWN");
/* 235 */     this.verificationLevel = level.getKey();
/* 236 */     this.set |= 0x400L;
/* 237 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildManagerImpl setDefaultNotificationLevel(@Nonnull Guild.NotificationLevel level) {
/* 245 */     Checks.notNull(level, "Level");
/* 246 */     Checks.check((level != Guild.NotificationLevel.UNKNOWN), "Level must not be UNKNOWN");
/* 247 */     this.notificationLevel = level.getKey();
/* 248 */     this.set |= 0x100L;
/* 249 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildManagerImpl setRequiredMFALevel(@Nonnull Guild.MFALevel level) {
/* 257 */     Checks.notNull(level, "Level");
/* 258 */     Checks.check((level != Guild.MFALevel.UNKNOWN), "Level must not be UNKNOWN");
/* 259 */     this.mfaLevel = level.getKey();
/* 260 */     this.set |= 0x80L;
/* 261 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildManagerImpl setExplicitContentLevel(@Nonnull Guild.ExplicitContentLevel level) {
/* 269 */     Checks.notNull(level, "Level");
/* 270 */     Checks.check((level != Guild.ExplicitContentLevel.UNKNOWN), "Level must not be UNKNOWN");
/* 271 */     this.explicitContentLevel = level.getKey();
/* 272 */     this.set |= 0x200L;
/* 273 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public GuildManager setBanner(@Nullable Icon banner) {
/* 280 */     checkFeature("BANNER");
/* 281 */     this.banner = banner;
/* 282 */     this.set |= 0x800L;
/* 283 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public GuildManager setVanityCode(@Nullable String code) {
/* 290 */     checkFeature("VANITY_URL");
/* 291 */     this.vanityCode = code;
/* 292 */     this.set |= 0x1000L;
/* 293 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public GuildManager setDescription(@Nullable String description) {
/* 300 */     checkFeature("VERIFIED");
/* 301 */     this.description = description;
/* 302 */     this.set |= 0x2000L;
/* 303 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 309 */     DataObject body = DataObject.empty().put("name", getGuild().getName());
/* 310 */     if (shouldUpdate(1L))
/* 311 */       body.put("name", this.name); 
/* 312 */     if (shouldUpdate(2L))
/* 313 */       body.put("region", this.region); 
/* 314 */     if (shouldUpdate(32L))
/* 315 */       body.put("afk_timeout", Integer.valueOf(this.afkTimeout)); 
/* 316 */     if (shouldUpdate(4L))
/* 317 */       body.put("icon", (this.icon == null) ? null : this.icon.getEncoding()); 
/* 318 */     if (shouldUpdate(8L))
/* 319 */       body.put("splash", (this.splash == null) ? null : this.splash.getEncoding()); 
/* 320 */     if (shouldUpdate(16L))
/* 321 */       body.put("afk_channel_id", this.afkChannel); 
/* 322 */     if (shouldUpdate(64L))
/* 323 */       body.put("system_channel_id", this.systemChannel); 
/* 324 */     if (shouldUpdate(16384L))
/* 325 */       body.put("rules_channel_id", this.rulesChannel); 
/* 326 */     if (shouldUpdate(32768L))
/* 327 */       body.put("public_updates_channel_id", this.communityUpdatesChannel); 
/* 328 */     if (shouldUpdate(1024L))
/* 329 */       body.put("verification_level", Integer.valueOf(this.verificationLevel)); 
/* 330 */     if (shouldUpdate(256L))
/* 331 */       body.put("default_message_notifications", Integer.valueOf(this.notificationLevel)); 
/* 332 */     if (shouldUpdate(128L))
/* 333 */       body.put("mfa_level", Integer.valueOf(this.mfaLevel)); 
/* 334 */     if (shouldUpdate(512L))
/* 335 */       body.put("explicit_content_filter", Integer.valueOf(this.explicitContentLevel)); 
/* 336 */     if (shouldUpdate(2048L))
/* 337 */       body.put("banner", (this.banner == null) ? null : this.banner.getEncoding()); 
/* 338 */     if (shouldUpdate(4096L))
/* 339 */       body.put("vanity_code", this.vanityCode); 
/* 340 */     if (shouldUpdate(8192L)) {
/* 341 */       body.put("description", this.description);
/*     */     }
/* 343 */     reset();
/* 344 */     return getRequestBody(body);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkPermissions() {
/* 350 */     if (!getGuild().getSelfMember().hasPermission(new Permission[] { Permission.MANAGE_SERVER }))
/* 351 */       throw new InsufficientPermissionException(getGuild(), Permission.MANAGE_SERVER); 
/* 352 */     return super.checkPermissions();
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkFeature(String feature) {
/* 357 */     if (!getGuild().getFeatures().contains(feature))
/* 358 */       throw new IllegalStateException("This guild doesn't have the " + feature + " feature enabled"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\managers\GuildManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */