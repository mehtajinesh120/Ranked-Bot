/*     */ package net.dv8tion.jda.internal.managers;
/*     */ 
/*     */ import gnu.trove.map.TLongObjectMap;
/*     */ import gnu.trove.map.hash.TLongObjectHashMap;
/*     */ import gnu.trove.set.TLongSet;
/*     */ import gnu.trove.set.hash.TLongHashSet;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Objects;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.Region;
/*     */ import net.dv8tion.jda.api.entities.Category;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.IPermissionHolder;
/*     */ import net.dv8tion.jda.api.entities.ISnowflake;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.PermissionOverride;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.exceptions.MissingAccessException;
/*     */ import net.dv8tion.jda.api.managers.ChannelManager;
/*     */ import net.dv8tion.jda.api.managers.Manager;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.entities.AbstractChannelImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.requests.restaction.PermOverrideData;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.PermissionUtil;
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
/*     */ public class ChannelManagerImpl
/*     */   extends ManagerBase<ChannelManager>
/*     */   implements ChannelManager
/*     */ {
/*     */   protected GuildChannel channel;
/*     */   protected String name;
/*     */   protected String parent;
/*     */   protected String topic;
/*     */   protected String region;
/*     */   protected int position;
/*     */   protected boolean nsfw;
/*     */   protected int slowmode;
/*     */   protected int userlimit;
/*     */   protected int bitrate;
/*     */   protected boolean news;
/*  57 */   protected final Object lock = new Object();
/*     */ 
/*     */ 
/*     */   
/*     */   protected final TLongObjectHashMap<PermOverrideData> overridesAdd;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final TLongSet overridesRem;
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelManagerImpl(GuildChannel channel) {
/*  70 */     super(channel.getJDA(), Route.Channels.MODIFY_CHANNEL
/*  71 */         .compile(new String[] { channel.getId() }));
/*  72 */     JDA jda = channel.getJDA();
/*  73 */     ChannelType type = channel.getType();
/*  74 */     this.channel = channel;
/*  75 */     if (isPermissionChecksEnabled())
/*  76 */       checkPermissions(); 
/*  77 */     this.overridesAdd = new TLongObjectHashMap();
/*  78 */     this.overridesRem = (TLongSet)new TLongHashSet();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public GuildChannel getChannel() {
/*  85 */     GuildChannel realChannel = this.api.getGuildChannelById(this.channel.getType(), this.channel.getIdLong());
/*  86 */     if (realChannel != null)
/*  87 */       this.channel = realChannel; 
/*  88 */     return this.channel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl reset(long fields) {
/*  96 */     super.reset(fields);
/*  97 */     if ((fields & 0x1L) == 1L)
/*  98 */       this.name = null; 
/*  99 */     if ((fields & 0x2L) == 2L)
/* 100 */       this.parent = null; 
/* 101 */     if ((fields & 0x4L) == 4L)
/* 102 */       this.topic = null; 
/* 103 */     if ((fields & 0x200L) == 512L)
/* 104 */       this.news = false; 
/* 105 */     if ((fields & 0x400L) == 1024L)
/* 106 */       this.region = null; 
/* 107 */     if ((fields & 0x80L) == 128L)
/*     */     {
/* 109 */       withLock(this.lock, lock -> {
/*     */             this.overridesRem.clear();
/*     */             
/*     */             this.overridesAdd.clear();
/*     */           });
/*     */     }
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl reset(long... fields) {
/* 123 */     super.reset(fields);
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl reset() {
/* 132 */     super.reset();
/* 133 */     this.name = null;
/* 134 */     this.parent = null;
/* 135 */     this.topic = null;
/* 136 */     this.region = null;
/* 137 */     this.news = false;
/* 138 */     withLock(this.lock, lock -> {
/*     */           this.overridesRem.clear();
/*     */           
/*     */           this.overridesAdd.clear();
/*     */         });
/* 143 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl clearOverridesAdded() {
/* 151 */     withLock(this.lock, lock -> {
/*     */           this.overridesAdd.clear();
/*     */           if (this.overridesRem.isEmpty()) {
/*     */             this.set &= 0xFFFFFFFFFFFFFF7FL;
/*     */           }
/*     */         });
/* 157 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl clearOverridesRemoved() {
/* 165 */     withLock(this.lock, lock -> {
/*     */           this.overridesRem.clear();
/*     */           if (this.overridesAdd.isEmpty()) {
/*     */             this.set &= 0xFFFFFFFFFFFFFF7FL;
/*     */           }
/*     */         });
/* 171 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl putPermissionOverride(@Nonnull IPermissionHolder permHolder, long allow, long deny) {
/* 179 */     Checks.notNull(permHolder, "PermissionHolder");
/* 180 */     Checks.check(permHolder.getGuild().equals(getGuild()), "PermissionHolder is not from the same Guild!");
/* 181 */     long id = permHolder.getIdLong();
/* 182 */     int type = (permHolder instanceof net.dv8tion.jda.api.entities.Role) ? 0 : 1;
/* 183 */     putPermissionOverride(new PermOverrideData(type, id, allow, deny));
/* 184 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl putMemberPermissionOverride(long memberId, long allow, long deny) {
/* 192 */     putPermissionOverride(new PermOverrideData(1, memberId, allow, deny));
/* 193 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl putRolePermissionOverride(long roleId, long allow, long deny) {
/* 201 */     putPermissionOverride(new PermOverrideData(0, roleId, allow, deny));
/* 202 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkCanPutPermissions(long allow, long deny) {
/* 207 */     Member selfMember = getGuild().getSelfMember();
/* 208 */     if (isPermissionChecksEnabled() && !selfMember.hasPermission(new Permission[] { Permission.ADMINISTRATOR })) {
/*     */       
/* 210 */       if (!selfMember.hasPermission(this.channel, new Permission[] { Permission.MANAGE_ROLES })) {
/* 211 */         throw new InsufficientPermissionException(this.channel, Permission.MANAGE_PERMISSIONS);
/*     */       }
/*     */       
/* 214 */       long channelPermissions = PermissionUtil.getExplicitPermission(this.channel, selfMember, false);
/* 215 */       if ((channelPermissions & Permission.MANAGE_PERMISSIONS.getRawValue()) == 0L) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 220 */         long botPerms = PermissionUtil.getEffectivePermission(this.channel, selfMember) & (Permission.MANAGE_ROLES.getRawValue() ^ 0xFFFFFFFFFFFFFFFFL);
/* 221 */         EnumSet<Permission> missing = Permission.getPermissions((allow | deny) & (botPerms ^ 0xFFFFFFFFFFFFFFFFL));
/* 222 */         if (!missing.isEmpty()) {
/* 223 */           throw new InsufficientPermissionException(this.channel, Permission.MANAGE_PERMISSIONS, "You must have Permission.MANAGE_PERMISSIONS on the channel explicitly in order to set permissions you don't already have!");
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void putPermissionOverride(@Nonnull PermOverrideData overrideData) {
/* 230 */     checkCanPutPermissions(overrideData.allow, overrideData.deny);
/* 231 */     withLock(this.lock, lock -> {
/*     */           this.overridesRem.remove(overrideData.id);
/*     */           this.overridesAdd.put(overrideData.id, overrideData);
/*     */           this.set |= 0x80L;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl removePermissionOverride(@Nonnull IPermissionHolder permHolder) {
/* 244 */     Checks.notNull(permHolder, "PermissionHolder");
/* 245 */     Checks.check(permHolder.getGuild().equals(getGuild()), "PermissionHolder is not from the same Guild!");
/* 246 */     return removePermissionOverride(permHolder.getIdLong());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl removePermissionOverride(long id) {
/* 254 */     if (isPermissionChecksEnabled() && !getGuild().getSelfMember().hasPermission(getChannel(), new Permission[] { Permission.MANAGE_PERMISSIONS }))
/* 255 */       throw new InsufficientPermissionException(getChannel(), Permission.MANAGE_PERMISSIONS); 
/* 256 */     withLock(this.lock, lock -> {
/*     */           this.overridesRem.add(id);
/*     */           
/*     */           this.overridesAdd.remove(id);
/*     */           this.set |= 0x80L;
/*     */         });
/* 262 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl sync(@Nonnull GuildChannel syncSource) {
/* 270 */     Checks.notNull(syncSource, "SyncSource");
/* 271 */     Checks.check(getGuild().equals(syncSource.getGuild()), "Sync only works for channels of same guild");
/*     */     
/* 273 */     if (syncSource.equals(getChannel())) {
/* 274 */       return this;
/*     */     }
/* 276 */     if (isPermissionChecksEnabled()) {
/*     */       
/* 278 */       Member selfMember = getGuild().getSelfMember();
/* 279 */       if (!selfMember.hasPermission(getChannel(), new Permission[] { Permission.MANAGE_PERMISSIONS })) {
/* 280 */         throw new InsufficientPermissionException(getChannel(), Permission.MANAGE_PERMISSIONS);
/*     */       }
/* 282 */       if (!selfMember.canSync(this.channel, syncSource)) {
/* 283 */         throw new InsufficientPermissionException(getChannel(), Permission.MANAGE_PERMISSIONS, "Cannot sync channel with parent due to permission escalation issues. One of the overrides would set MANAGE_PERMISSIONS or a permission that the bot does not have. This is not possible without explicitly having MANAGE_PERMISSIONS on this channel or ADMINISTRATOR on a role.");
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 290 */     withLock(this.lock, lock -> {
/*     */           this.overridesRem.clear();
/*     */ 
/*     */ 
/*     */           
/*     */           this.overridesAdd.clear();
/*     */ 
/*     */ 
/*     */           
/*     */           Objects.requireNonNull(this.overridesRem);
/*     */ 
/*     */           
/*     */           getChannel().getPermissionOverrides().stream().mapToLong(ISnowflake::getIdLong).forEach(this.overridesRem::add);
/*     */ 
/*     */           
/*     */           syncSource.getPermissionOverrides().forEach(());
/*     */ 
/*     */           
/*     */           this.set |= 0x80L;
/*     */         });
/*     */ 
/*     */     
/* 312 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl setName(@Nonnull String name) {
/* 320 */     Checks.notBlank(name, "Name");
/* 321 */     name = name.trim();
/* 322 */     Checks.notEmpty(name, "Name");
/* 323 */     Checks.notLonger(name, 100, "Name");
/* 324 */     this.name = name;
/* 325 */     this.set |= 0x1L;
/* 326 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl setRegion(@Nonnull Region region) {
/* 334 */     Checks.notNull(region, "Region");
/* 335 */     if (!getType().isAudio())
/* 336 */       throw new IllegalStateException("Can only change region on voice channels!"); 
/* 337 */     Checks.check(Region.VOICE_CHANNEL_REGIONS.contains(region), "Region is not usable for VoiceChannel region overrides!");
/* 338 */     this.region = (region == Region.AUTOMATIC) ? null : region.getKey();
/* 339 */     this.set |= 0x400L;
/* 340 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl setParent(Category category) {
/* 348 */     if (category != null) {
/*     */       
/* 350 */       if (getType() == ChannelType.CATEGORY)
/* 351 */         throw new IllegalStateException("Cannot set the parent of a category"); 
/* 352 */       Checks.check(category.getGuild().equals(getGuild()), "Category is not from the same guild");
/*     */     } 
/* 354 */     this.parent = (category == null) ? null : category.getId();
/* 355 */     this.set |= 0x2L;
/* 356 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl setPosition(int position) {
/* 364 */     this.position = position;
/* 365 */     this.set |= 0x8L;
/* 366 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl setTopic(String topic) {
/* 374 */     if (getType() != ChannelType.TEXT)
/* 375 */       throw new IllegalStateException("Can only set topic on text channels"); 
/* 376 */     if (topic != null)
/* 377 */       Checks.notLonger(topic, 1024, "Topic"); 
/* 378 */     this.topic = topic;
/* 379 */     this.set |= 0x4L;
/* 380 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl setNSFW(boolean nsfw) {
/* 388 */     if (getType() != ChannelType.TEXT)
/* 389 */       throw new IllegalStateException("Can only set nsfw on text channels"); 
/* 390 */     this.nsfw = nsfw;
/* 391 */     this.set |= 0x10L;
/* 392 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl setSlowmode(int slowmode) {
/* 400 */     if (getType() != ChannelType.TEXT)
/* 401 */       throw new IllegalStateException("Can only set slowmode on text channels"); 
/* 402 */     Checks.check((slowmode <= 21600 && slowmode >= 0), "Slowmode per user must be between 0 and %d (seconds)!", Integer.valueOf(21600));
/* 403 */     this.slowmode = slowmode;
/* 404 */     this.set |= 0x100L;
/* 405 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl setUserLimit(int userLimit) {
/* 413 */     if (getType() != ChannelType.VOICE)
/* 414 */       throw new IllegalStateException("Can only set userlimit on voice channels"); 
/* 415 */     Checks.notNegative(userLimit, "Userlimit");
/* 416 */     Checks.check((userLimit <= 99), "Userlimit may not be greater than 99");
/* 417 */     this.userlimit = userLimit;
/* 418 */     this.set |= 0x20L;
/* 419 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl setBitrate(int bitrate) {
/* 427 */     if (!getType().isAudio())
/* 428 */       throw new IllegalStateException("Can only set bitrate on voice channels"); 
/* 429 */     int maxBitrate = getGuild().getMaxBitrate();
/* 430 */     Checks.check((bitrate >= 8000), "Bitrate must be greater or equal to 8000");
/* 431 */     Checks.check((bitrate <= maxBitrate), "Bitrate must be less or equal to %s", Integer.valueOf(maxBitrate));
/* 432 */     this.bitrate = bitrate;
/* 433 */     this.set |= 0x40L;
/* 434 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelManagerImpl setNews(boolean news) {
/* 442 */     if (getType() != ChannelType.TEXT)
/* 443 */       throw new IllegalStateException("Can only set channel as news on text channels"); 
/* 444 */     if (news && !getGuild().getFeatures().contains("NEWS"))
/* 445 */       throw new IllegalStateException("Can only set channel as news for guilds with NEWS feature"); 
/* 446 */     this.news = news;
/* 447 */     this.set |= 0x200L;
/* 448 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 454 */     DataObject frame = DataObject.empty().put("name", getChannel().getName());
/* 455 */     if (shouldUpdate(1L))
/* 456 */       frame.put("name", this.name); 
/* 457 */     if (shouldUpdate(8L))
/* 458 */       frame.put("position", Integer.valueOf(this.position)); 
/* 459 */     if (shouldUpdate(4L))
/* 460 */       frame.put("topic", this.topic); 
/* 461 */     if (shouldUpdate(16L))
/* 462 */       frame.put("nsfw", Boolean.valueOf(this.nsfw)); 
/* 463 */     if (shouldUpdate(256L))
/* 464 */       frame.put("rate_limit_per_user", Integer.valueOf(this.slowmode)); 
/* 465 */     if (shouldUpdate(32L))
/* 466 */       frame.put("user_limit", Integer.valueOf(this.userlimit)); 
/* 467 */     if (shouldUpdate(64L))
/* 468 */       frame.put("bitrate", Integer.valueOf(this.bitrate)); 
/* 469 */     if (shouldUpdate(2L))
/* 470 */       frame.put("parent_id", this.parent); 
/* 471 */     if (shouldUpdate(512L))
/* 472 */       frame.put("type", Integer.valueOf(this.news ? 5 : 0)); 
/* 473 */     if (shouldUpdate(1024L))
/* 474 */       frame.put("rtc_region", this.region); 
/* 475 */     withLock(this.lock, lock -> {
/*     */           if (shouldUpdate(128L)) {
/*     */             frame.put("permission_overwrites", getOverrides());
/*     */           }
/*     */         });
/*     */     
/* 481 */     reset();
/* 482 */     return getRequestBody(frame);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkPermissions() {
/* 488 */     Member selfMember = getGuild().getSelfMember();
/* 489 */     GuildChannel channel = getChannel();
/* 490 */     if (!selfMember.hasPermission(channel, new Permission[] { Permission.VIEW_CHANNEL }))
/* 491 */       throw new MissingAccessException(channel, Permission.VIEW_CHANNEL); 
/* 492 */     if (!selfMember.hasAccess(channel))
/* 493 */       throw new MissingAccessException(channel, Permission.VOICE_CONNECT); 
/* 494 */     if (!selfMember.hasPermission(channel, new Permission[] { Permission.MANAGE_CHANNEL }))
/* 495 */       throw new InsufficientPermissionException(channel, Permission.MANAGE_CHANNEL); 
/* 496 */     return super.checkPermissions();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Collection<PermOverrideData> getOverrides() {
/* 502 */     TLongObjectHashMap<PermOverrideData> data = new TLongObjectHashMap((TLongObjectMap)this.overridesAdd);
/*     */     
/* 504 */     AbstractChannelImpl<?, ?> impl = (AbstractChannelImpl<?, ?>)getChannel();
/* 505 */     impl.getOverrideMap().forEachEntry((id, override) -> {
/*     */           if (!this.overridesRem.remove(id) && !data.containsKey(id)) {
/*     */             data.put(id, new PermOverrideData(override));
/*     */           }
/*     */           
/*     */           return true;
/*     */         });
/*     */     
/* 513 */     return data.valueCollection();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\managers\ChannelManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */