/*     */ package net.dv8tion.jda.internal.requests.restaction;
/*     */ 
/*     */ import gnu.trove.map.TLongObjectMap;
/*     */ import gnu.trove.map.hash.TLongObjectHashMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Category;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.PermissionOverride;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.ChannelAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.entities.EntityBuilder;
/*     */ import net.dv8tion.jda.internal.requests.Route;
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
/*     */ public class ChannelActionImpl<T extends GuildChannel>
/*     */   extends AuditableRestActionImpl<T>
/*     */   implements ChannelAction<T>
/*     */ {
/*  43 */   protected final TLongObjectMap<PermOverrideData> overrides = (TLongObjectMap<PermOverrideData>)new TLongObjectHashMap();
/*     */   
/*     */   protected final Guild guild;
/*     */   
/*     */   protected final ChannelType type;
/*     */   protected final Class<T> clazz;
/*     */   protected String name;
/*     */   protected Category parent;
/*     */   protected Integer position;
/*  52 */   protected String topic = null;
/*  53 */   protected Boolean nsfw = null;
/*  54 */   protected Integer slowmode = null;
/*  55 */   protected Boolean news = null;
/*     */ 
/*     */   
/*  58 */   protected Integer bitrate = null;
/*  59 */   protected Integer userlimit = null;
/*     */ 
/*     */   
/*     */   public ChannelActionImpl(Class<T> clazz, String name, Guild guild, ChannelType type) {
/*  63 */     super(guild.getJDA(), Route.Guilds.CREATE_CHANNEL.compile(new String[] { guild.getId() }));
/*  64 */     this.clazz = clazz;
/*  65 */     this.guild = guild;
/*  66 */     this.type = type;
/*  67 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelActionImpl<T> setCheck(BooleanSupplier checks) {
/*  74 */     return (ChannelActionImpl<T>)super.setCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelActionImpl<T> timeout(long timeout, @Nonnull TimeUnit unit) {
/*  81 */     return (ChannelActionImpl<T>)super.timeout(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelActionImpl<T> deadline(long timestamp) {
/*  88 */     return (ChannelActionImpl<T>)super.deadline(timestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild getGuild() {
/*  95 */     return this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelType getType() {
/* 102 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelActionImpl<T> setName(@Nonnull String name) {
/* 110 */     Checks.notEmpty(name, "Name");
/* 111 */     Checks.notLonger(name, 100, "Name");
/* 112 */     this.name = name;
/* 113 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelActionImpl<T> setParent(Category category) {
/* 121 */     Checks.check((category == null || category.getGuild().equals(this.guild)), "Category is not from same guild!");
/* 122 */     this.parent = category;
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelActionImpl<T> setPosition(Integer position) {
/* 131 */     Checks.check((position == null || position.intValue() >= 0), "Position must be >= 0!");
/* 132 */     this.position = position;
/* 133 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelActionImpl<T> setTopic(String topic) {
/* 141 */     if (this.type != ChannelType.TEXT)
/* 142 */       throw new UnsupportedOperationException("Can only set the topic for a TextChannel!"); 
/* 143 */     if (topic != null && topic.length() > 1024)
/* 144 */       throw new IllegalArgumentException("Channel Topic must not be greater than 1024 in length!"); 
/* 145 */     this.topic = topic;
/* 146 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelActionImpl<T> setNSFW(boolean nsfw) {
/* 154 */     if (this.type != ChannelType.TEXT)
/* 155 */       throw new UnsupportedOperationException("Can only set nsfw for a TextChannel!"); 
/* 156 */     this.nsfw = Boolean.valueOf(nsfw);
/* 157 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelActionImpl<T> setSlowmode(int slowmode) {
/* 165 */     if (this.type != ChannelType.TEXT)
/* 166 */       throw new UnsupportedOperationException("Can only set slowmode on text channels"); 
/* 167 */     Checks.check((slowmode <= 21600 && slowmode >= 0), "Slowmode must be between 0 and %d (seconds)!", Integer.valueOf(21600));
/* 168 */     this.slowmode = Integer.valueOf(slowmode);
/* 169 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelActionImpl<T> setNews(boolean news) {
/* 177 */     if (this.type != ChannelType.TEXT)
/* 178 */       throw new UnsupportedOperationException("Can only set news for a TextChannel!"); 
/* 179 */     if (news && !getGuild().getFeatures().contains("NEWS"))
/* 180 */       throw new IllegalStateException("Can only set channel as news for guilds with NEWS feature"); 
/* 181 */     this.news = Boolean.valueOf(news);
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelActionImpl<T> addMemberPermissionOverride(long userId, long allow, long deny) {
/* 190 */     return addOverride(userId, 1, allow, deny);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelActionImpl<T> addRolePermissionOverride(long roleId, long allow, long deny) {
/* 198 */     return addOverride(roleId, 0, allow, deny);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelAction<T> removePermissionOverride(long id) {
/* 205 */     this.overrides.remove(id);
/* 206 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelAction<T> clearPermissionOverrides() {
/* 213 */     this.overrides.clear();
/* 214 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelAction<T> syncPermissionOverrides() {
/* 222 */     if (this.parent == null)
/* 223 */       throw new IllegalStateException("Cannot sync overrides without parent category! Use setParent(category) first!"); 
/* 224 */     clearPermissionOverrides();
/* 225 */     Member selfMember = getGuild().getSelfMember();
/* 226 */     boolean canSetRoles = selfMember.hasPermission((GuildChannel)this.parent, new Permission[] { Permission.MANAGE_ROLES });
/*     */ 
/*     */ 
/*     */     
/* 230 */     long botPerms = PermissionUtil.getEffectivePermission(selfMember) & (Permission.MANAGE_PERMISSIONS.getRawValue() ^ 0xFFFFFFFFFFFFFFFFL);
/*     */     
/* 232 */     this.parent.getRolePermissionOverrides().forEach(override -> {
/*     */           long allow = override.getAllowedRaw();
/*     */           
/*     */           long deny = override.getDeniedRaw();
/*     */           
/*     */           if (!canSetRoles) {
/*     */             allow &= botPerms;
/*     */             deny &= botPerms;
/*     */           } 
/*     */           addRolePermissionOverride(override.getIdLong(), allow, deny);
/*     */         });
/* 243 */     this.parent.getMemberPermissionOverrides().forEach(override -> {
/*     */           long allow = override.getAllowedRaw();
/*     */           
/*     */           long deny = override.getDeniedRaw();
/*     */           if (!canSetRoles) {
/*     */             allow &= botPerms;
/*     */             deny &= botPerms;
/*     */           } 
/*     */           addMemberPermissionOverride(override.getIdLong(), allow, deny);
/*     */         });
/* 253 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   private ChannelActionImpl<T> addOverride(long targetId, int type, long allow, long deny) {
/* 258 */     Member selfMember = getGuild().getSelfMember();
/* 259 */     boolean canSetRoles = selfMember.hasPermission(new Permission[] { Permission.ADMINISTRATOR });
/* 260 */     if (!canSetRoles && this.parent != null)
/* 261 */       canSetRoles = selfMember.hasPermission((GuildChannel)this.parent, new Permission[] { Permission.MANAGE_ROLES }); 
/* 262 */     if (!canSetRoles) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 268 */       long botPerms = PermissionUtil.getEffectivePermission(selfMember) & (Permission.MANAGE_PERMISSIONS.getRawValue() ^ 0xFFFFFFFFFFFFFFFFL);
/*     */       
/* 270 */       EnumSet<Permission> missingPerms = Permission.getPermissions((allow | deny) & (botPerms ^ 0xFFFFFFFFFFFFFFFFL));
/* 271 */       if (!missingPerms.isEmpty()) {
/* 272 */         throw new InsufficientPermissionException(this.guild, Permission.MANAGE_PERMISSIONS, "You must have Permission.MANAGE_PERMISSIONS on the channel explicitly in order to set permissions you don't already have!");
/*     */       }
/*     */     } 
/* 275 */     this.overrides.put(targetId, new PermOverrideData(type, targetId, allow, deny));
/* 276 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelActionImpl<T> setBitrate(Integer bitrate) {
/* 285 */     if (!this.type.isAudio())
/* 286 */       throw new UnsupportedOperationException("Can only set the bitrate for an Audio Channel!"); 
/* 287 */     if (bitrate != null) {
/*     */       
/* 289 */       int maxBitrate = getGuild().getMaxBitrate();
/* 290 */       if (bitrate.intValue() < 8000)
/* 291 */         throw new IllegalArgumentException("Bitrate must be greater than 8000."); 
/* 292 */       if (bitrate.intValue() > maxBitrate) {
/* 293 */         throw new IllegalArgumentException("Bitrate must be less than " + maxBitrate);
/*     */       }
/*     */     } 
/* 296 */     this.bitrate = bitrate;
/* 297 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ChannelActionImpl<T> setUserlimit(Integer userlimit) {
/* 305 */     if (this.type != ChannelType.VOICE)
/* 306 */       throw new UnsupportedOperationException("Can only set the userlimit for a VoiceChannel!"); 
/* 307 */     if (userlimit != null && (userlimit.intValue() < 0 || userlimit.intValue() > 99))
/* 308 */       throw new IllegalArgumentException("Userlimit must be between 0-99!"); 
/* 309 */     this.userlimit = userlimit;
/* 310 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 316 */     DataObject object = DataObject.empty();
/* 317 */     object.put("name", this.name);
/* 318 */     object.put("type", Integer.valueOf(this.type.getId()));
/* 319 */     object.put("permission_overwrites", DataArray.fromCollection(this.overrides.valueCollection()));
/* 320 */     if (this.position != null)
/* 321 */       object.put("position", this.position); 
/* 322 */     switch (this.type) {
/*     */       
/*     */       case VOICE:
/* 325 */         if (this.bitrate != null)
/* 326 */           object.put("bitrate", this.bitrate); 
/* 327 */         if (this.userlimit != null)
/* 328 */           object.put("user_limit", this.userlimit); 
/*     */         break;
/*     */       case TEXT:
/* 331 */         if (this.topic != null && !this.topic.isEmpty())
/* 332 */           object.put("topic", this.topic); 
/* 333 */         if (this.nsfw != null)
/* 334 */           object.put("nsfw", this.nsfw); 
/* 335 */         if (this.slowmode != null)
/* 336 */           object.put("rate_limit_per_user", this.slowmode); 
/* 337 */         if (this.news != null)
/* 338 */           object.put("type", Integer.valueOf(this.news.booleanValue() ? 5 : 0)); 
/*     */         break;
/*     */       case STAGE:
/* 341 */         if (this.bitrate != null)
/* 342 */           object.put("bitrate", this.bitrate); 
/*     */         break;
/*     */     } 
/* 345 */     if (this.type != ChannelType.CATEGORY && this.parent != null) {
/* 346 */       object.put("parent_id", this.parent.getId());
/*     */     }
/* 348 */     return getRequestBody(object);
/*     */   }
/*     */   protected void handleSuccess(Response response, Request<T> request) {
/*     */     VoiceChannel voiceChannel;
/*     */     TextChannel textChannel;
/*     */     Category category;
/* 354 */     EntityBuilder builder = this.api.getEntityBuilder();
/*     */     
/* 356 */     switch (this.type) {
/*     */       
/*     */       case VOICE:
/*     */       case STAGE:
/* 360 */         voiceChannel = builder.createVoiceChannel(response.getObject(), this.guild.getIdLong());
/*     */         break;
/*     */       case TEXT:
/* 363 */         textChannel = builder.createTextChannel(response.getObject(), this.guild.getIdLong());
/*     */         break;
/*     */       case CATEGORY:
/* 366 */         category = builder.createCategory(response.getObject(), this.guild.getIdLong());
/*     */         break;
/*     */       default:
/* 369 */         request.onFailure(new IllegalStateException("Created channel of unknown type!"));
/*     */         return;
/*     */     } 
/* 372 */     request.onSuccess((GuildChannel)this.clazz.cast(category));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\ChannelActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */