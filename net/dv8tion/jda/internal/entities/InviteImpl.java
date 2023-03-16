/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import java.time.OffsetDateTime;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.annotations.DeprecatedSince;
/*     */ import net.dv8tion.jda.annotations.ReplaceWith;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.ISnowflake;
/*     */ import net.dv8tion.jda.api.entities.Invite;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.requests.CompletedRestAction;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.requests.restaction.AuditableRestActionImpl;
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
/*     */ public class InviteImpl
/*     */   implements Invite
/*     */ {
/*     */   private final JDAImpl api;
/*     */   private final Invite.Channel channel;
/*     */   private final String code;
/*     */   private final boolean expanded;
/*     */   private final Invite.Guild guild;
/*     */   private final Invite.Group group;
/*     */   private final Invite.InviteTarget target;
/*     */   private final User inviter;
/*     */   private final int maxAge;
/*     */   private final int maxUses;
/*     */   private final boolean temporary;
/*     */   private final OffsetDateTime timeCreated;
/*     */   private final int uses;
/*     */   private final Invite.InviteType type;
/*     */   
/*     */   public InviteImpl(JDAImpl api, String code, boolean expanded, User inviter, int maxAge, int maxUses, boolean temporary, OffsetDateTime timeCreated, int uses, Invite.Channel channel, Invite.Guild guild, Invite.Group group, Invite.InviteTarget target, Invite.InviteType type) {
/*  64 */     this.api = api;
/*  65 */     this.code = code;
/*  66 */     this.expanded = expanded;
/*  67 */     this.inviter = inviter;
/*  68 */     this.maxAge = maxAge;
/*  69 */     this.maxUses = maxUses;
/*  70 */     this.temporary = temporary;
/*  71 */     this.timeCreated = timeCreated;
/*  72 */     this.uses = uses;
/*  73 */     this.channel = channel;
/*  74 */     this.guild = guild;
/*  75 */     this.group = group;
/*  76 */     this.target = target;
/*  77 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public static RestAction<Invite> resolve(JDA api, String code, boolean withCounts) {
/*  82 */     Checks.notNull(code, "code");
/*  83 */     Checks.notNull(api, "api");
/*     */     
/*  85 */     Route.CompiledRoute route = Route.Invites.GET_INVITE.compile(new String[] { code });
/*     */     
/*  87 */     if (withCounts) {
/*  88 */       route = route.withQueryParams(new String[] { "with_counts", "true" });
/*     */     }
/*  90 */     JDAImpl jda = (JDAImpl)api;
/*  91 */     return (RestAction<Invite>)new RestActionImpl(api, route, (response, request) -> jda.getEntityBuilder().createInvite(response.getObject()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<Void> delete() {
/*  99 */     Route.CompiledRoute route = Route.Invites.DELETE_INVITE.compile(new String[] { this.code });
/*     */     
/* 101 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl((JDA)this.api, route);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Invite> expand() {
/*     */     Route.CompiledRoute route;
/* 108 */     if (this.expanded) {
/* 109 */       return (RestAction<Invite>)new CompletedRestAction((JDA)getJDA(), this);
/*     */     }
/* 111 */     if (this.type != Invite.InviteType.GUILD) {
/* 112 */       throw new IllegalStateException("Only guild invites can be expanded");
/*     */     }
/* 114 */     Guild guild = this.api.getGuildById(this.guild.getIdLong());
/*     */     
/* 116 */     if (guild == null) {
/* 117 */       throw new UnsupportedOperationException("You're not in the guild this invite points to");
/*     */     }
/* 119 */     Member member = guild.getSelfMember();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     GuildChannel channel = (this.channel.getType() == ChannelType.TEXT) ? (GuildChannel)guild.getTextChannelById(this.channel.getIdLong()) : (GuildChannel)guild.getVoiceChannelById(this.channel.getIdLong());
/*     */     
/* 127 */     if (member.hasPermission(channel, new Permission[] { Permission.MANAGE_CHANNEL })) {
/*     */       
/* 129 */       route = Route.Invites.GET_CHANNEL_INVITES.compile(new String[] { channel.getId() });
/*     */     }
/* 131 */     else if (member.hasPermission(new Permission[] { Permission.MANAGE_SERVER })) {
/*     */       
/* 133 */       route = Route.Invites.GET_GUILD_INVITES.compile(new String[] { guild.getId() });
/*     */     }
/*     */     else {
/*     */       
/* 137 */       throw new InsufficientPermissionException(channel, Permission.MANAGE_CHANNEL, "You don't have the permission to view the full invite info");
/*     */     } 
/*     */     
/* 140 */     return (RestAction<Invite>)new RestActionImpl((JDA)this.api, route, (response, request) -> {
/*     */           EntityBuilder entityBuilder = this.api.getEntityBuilder();
/*     */           DataArray array = response.getArray();
/*     */           for (int i = 0; i < array.length(); i++) {
/*     */             DataObject object = array.getObject(i);
/*     */             if (this.code.equals(object.getString("code"))) {
/*     */               return entityBuilder.createInvite(object);
/*     */             }
/*     */           } 
/*     */           throw new IllegalStateException("Missing the invite in the channel/guild invite list");
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Invite.InviteType getType() {
/* 160 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Invite.TargetType getTargetType() {
/* 167 */     return (this.target == null) ? Invite.TargetType.NONE : this.target.getType();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Invite.Channel getChannel() {
/* 173 */     return this.channel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getCode() {
/* 180 */     return this.code;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @Deprecated
/*     */   @DeprecatedSince("4.BETA.0")
/*     */   @ReplaceWith("getTimeCreated()")
/*     */   public OffsetDateTime getCreationTime() {
/* 190 */     return getTimeCreated();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Invite.Guild getGuild() {
/* 196 */     return this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Invite.Group getGroup() {
/* 202 */     return this.group;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Invite.InviteTarget getTarget() {
/* 209 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public User getInviter() {
/* 215 */     return this.inviter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDAImpl getJDA() {
/* 222 */     return this.api;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxAge() {
/* 228 */     if (!this.expanded)
/* 229 */       throw new IllegalStateException("Only valid for expanded invites"); 
/* 230 */     return this.maxAge;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxUses() {
/* 236 */     if (!this.expanded)
/* 237 */       throw new IllegalStateException("Only valid for expanded invites"); 
/* 238 */     return this.maxUses;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public OffsetDateTime getTimeCreated() {
/* 245 */     if (!this.expanded)
/* 246 */       throw new IllegalStateException("Only valid for expanded invites"); 
/* 247 */     return this.timeCreated;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUses() {
/* 253 */     if (!this.expanded)
/* 254 */       throw new IllegalStateException("Only valid for expanded invites"); 
/* 255 */     return this.uses;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExpanded() {
/* 261 */     return this.expanded;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTemporary() {
/* 267 */     if (!this.expanded)
/* 268 */       throw new IllegalStateException("Only valid for expanded invites"); 
/* 269 */     return this.temporary;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 275 */     return this.code.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 281 */     if (obj == this)
/* 282 */       return true; 
/* 283 */     if (!(obj instanceof InviteImpl))
/* 284 */       return false; 
/* 285 */     InviteImpl impl = (InviteImpl)obj;
/* 286 */     return impl.code.equals(this.code);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 292 */     return "Invite(" + this.code + ")";
/*     */   }
/*     */   
/*     */   public static class ChannelImpl
/*     */     implements Invite.Channel
/*     */   {
/*     */     private final long id;
/*     */     private final String name;
/*     */     private final ChannelType type;
/*     */     
/*     */     public ChannelImpl(long id, String name, ChannelType type) {
/* 303 */       this.id = id;
/* 304 */       this.name = name;
/* 305 */       this.type = type;
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelImpl(GuildChannel channel) {
/* 310 */       this(channel.getIdLong(), channel.getName(), channel.getType());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public long getIdLong() {
/* 316 */       return this.id;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getName() {
/* 323 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public ChannelType getType() {
/* 330 */       return this.type;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class GuildImpl implements Invite.Guild {
/*     */     private final String iconId;
/*     */     private final String name;
/*     */     private final String splashId;
/*     */     private final int presenceCount;
/*     */     private final int memberCount;
/*     */     private final long id;
/*     */     private final Guild.VerificationLevel verificationLevel;
/*     */     private final Set<String> features;
/*     */     
/*     */     public GuildImpl(long id, String iconId, String name, String splashId, Guild.VerificationLevel verificationLevel, int presenceCount, int memberCount, Set<String> features) {
/* 345 */       this.id = id;
/* 346 */       this.iconId = iconId;
/* 347 */       this.name = name;
/* 348 */       this.splashId = splashId;
/* 349 */       this.verificationLevel = verificationLevel;
/* 350 */       this.presenceCount = presenceCount;
/* 351 */       this.memberCount = memberCount;
/* 352 */       this.features = features;
/*     */     }
/*     */ 
/*     */     
/*     */     public GuildImpl(Guild guild) {
/* 357 */       this(guild.getIdLong(), guild.getIconId(), guild.getName(), guild.getSplashId(), guild
/* 358 */           .getVerificationLevel(), -1, -1, guild.getFeatures());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getIconId() {
/* 364 */       return this.iconId;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getIconUrl() {
/* 370 */       return (this.iconId == null) ? null : (
/* 371 */         "https://cdn.discordapp.com/icons/" + this.id + "/" + this.iconId + ".png");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public long getIdLong() {
/* 377 */       return this.id;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getName() {
/* 384 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getSplashId() {
/* 390 */       return this.splashId;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getSplashUrl() {
/* 396 */       return (this.splashId == null) ? null : (
/* 397 */         "https://cdn.discordapp.com/splashes/" + this.id + "/" + this.splashId + ".png");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Guild.VerificationLevel getVerificationLevel() {
/* 404 */       return this.verificationLevel;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int getOnlineCount() {
/* 410 */       return this.presenceCount;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int getMemberCount() {
/* 416 */       return this.memberCount;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Set<String> getFeatures() {
/* 423 */       return this.features;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class GroupImpl
/*     */     implements Invite.Group {
/*     */     private final String iconId;
/*     */     private final String name;
/*     */     private final long id;
/*     */     private final List<String> users;
/*     */     
/*     */     public GroupImpl(String iconId, String name, long id, List<String> users) {
/* 435 */       this.iconId = iconId;
/* 436 */       this.name = name;
/* 437 */       this.id = id;
/* 438 */       this.users = users;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getIconId() {
/* 444 */       return this.iconId;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getIconUrl() {
/* 450 */       return (this.iconId == null) ? null : (
/* 451 */         "https://cdn.discordapp.com/channel-icons/" + this.id + "/" + this.iconId + ".png");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 457 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public long getIdLong() {
/* 463 */       return this.id;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public List<String> getUsers() {
/* 469 */       return this.users;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class InviteTargetImpl
/*     */     implements Invite.InviteTarget
/*     */   {
/*     */     private final Invite.TargetType type;
/*     */     private final Invite.EmbeddedApplication targetApplication;
/*     */     private final User targetUser;
/*     */     
/*     */     public InviteTargetImpl(Invite.TargetType type, Invite.EmbeddedApplication targetApplication, User targetUser) {
/* 481 */       this.type = type;
/* 482 */       this.targetApplication = targetApplication;
/* 483 */       this.targetUser = targetUser;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Invite.TargetType getType() {
/* 490 */       return this.type;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getId() {
/* 497 */       return getTargetEntity().getId();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public long getIdLong() {
/* 503 */       return getTargetEntity().getIdLong();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public User getUser() {
/* 510 */       return this.targetUser;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Invite.EmbeddedApplication getApplication() {
/* 517 */       return this.targetApplication;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     private ISnowflake getTargetEntity() {
/* 523 */       if (this.targetUser != null) return (ISnowflake)this.targetUser; 
/* 524 */       if (this.targetApplication != null) return (ISnowflake)this.targetApplication; 
/* 525 */       throw new IllegalStateException("No target entity");
/*     */     }
/*     */   }
/*     */   
/*     */   public static class EmbeddedApplicationImpl implements Invite.EmbeddedApplication {
/*     */     private final String iconId;
/*     */     private final String name;
/*     */     private final String description;
/*     */     private final String summary;
/*     */     private final long id;
/*     */     private final int maxParticipants;
/*     */     
/*     */     public EmbeddedApplicationImpl(String iconId, String name, String description, String summary, long id, int maxParticipants) {
/* 538 */       this.iconId = iconId;
/* 539 */       this.name = name;
/* 540 */       this.description = description;
/* 541 */       this.summary = summary;
/* 542 */       this.id = id;
/* 543 */       this.maxParticipants = maxParticipants;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public long getIdLong() {
/* 549 */       return this.id;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getName() {
/* 556 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getDescription() {
/* 563 */       return this.description;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getSummary() {
/* 570 */       return this.summary;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getIconId() {
/* 577 */       return this.iconId;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getIconUrl() {
/* 584 */       return (this.iconId == null) ? null : (
/* 585 */         "https://cdn.discordapp.com/app-icons/" + this.id + '/' + this.iconId + ".png");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int getMaxParticipants() {
/* 591 */       return this.maxParticipants;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\InviteImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */