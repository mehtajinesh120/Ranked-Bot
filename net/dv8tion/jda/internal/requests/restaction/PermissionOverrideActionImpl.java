/*     */ package net.dv8tion.jda.internal.requests.restaction;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.IPermissionHolder;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.PermissionOverride;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.exceptions.MissingAccessException;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.entities.AbstractChannelImpl;
/*     */ import net.dv8tion.jda.internal.entities.PermissionOverrideImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PermissionOverrideActionImpl
/*     */   extends AuditableRestActionImpl<PermissionOverride>
/*     */   implements PermissionOverrideAction
/*     */ {
/*     */   private boolean isOverride = true;
/*     */   private boolean allowSet = false;
/*     */   private boolean denySet = false;
/*  48 */   private long allow = 0L;
/*  49 */   private long deny = 0L;
/*     */   
/*     */   private final AbstractChannelImpl<?, ?> channel;
/*     */   private final IPermissionHolder permissionHolder;
/*     */   private final boolean isRole;
/*     */   private final long id;
/*     */   
/*     */   public PermissionOverrideActionImpl(PermissionOverride override) {
/*  57 */     super(override.getJDA(), Route.Channels.MODIFY_PERM_OVERRIDE.compile(new String[] { override.getChannel().getId(), override.getId() }));
/*  58 */     this.channel = (AbstractChannelImpl<?, ?>)override.getChannel();
/*  59 */     this.permissionHolder = override.getPermissionHolder();
/*  60 */     this.isRole = override.isRoleOverride();
/*  61 */     this.id = override.getIdLong();
/*     */   }
/*     */ 
/*     */   
/*     */   public PermissionOverrideActionImpl(JDA api, GuildChannel channel, IPermissionHolder permissionHolder) {
/*  66 */     super(api, Route.Channels.CREATE_PERM_OVERRIDE.compile(new String[] { channel.getId(), permissionHolder.getId() }));
/*  67 */     this.channel = (AbstractChannelImpl<?, ?>)channel;
/*  68 */     this.permissionHolder = permissionHolder;
/*  69 */     this.isRole = permissionHolder instanceof Role;
/*  70 */     this.id = permissionHolder.getIdLong();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PermissionOverrideActionImpl setOverride(boolean override) {
/*  76 */     this.isOverride = override;
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BooleanSupplier finalizeChecks() {
/*  83 */     return () -> {
/*     */         Member selfMember = getGuild().getSelfMember();
/*     */         if (!selfMember.hasPermission((GuildChannel)this.channel, new Permission[] { Permission.VIEW_CHANNEL })) {
/*     */           throw new MissingAccessException(this.channel, Permission.VIEW_CHANNEL);
/*     */         }
/*     */         if (!selfMember.hasAccess((GuildChannel)this.channel)) {
/*     */           throw new MissingAccessException(this.channel, Permission.VOICE_CONNECT);
/*     */         }
/*     */         if (!selfMember.hasPermission((GuildChannel)this.channel, new Permission[] { Permission.MANAGE_PERMISSIONS })) {
/*     */           throw new InsufficientPermissionException(this.channel, Permission.MANAGE_PERMISSIONS);
/*     */         }
/*     */         return true;
/*     */       };
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   public PermissionOverrideActionImpl setCheck(BooleanSupplier checks) {
/* 100 */     return (PermissionOverrideActionImpl)super.setCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public PermissionOverrideActionImpl timeout(long timeout, @Nonnull TimeUnit unit) {
/* 107 */     return (PermissionOverrideActionImpl)super.timeout(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public PermissionOverrideActionImpl deadline(long timestamp) {
/* 114 */     return (PermissionOverrideActionImpl)super.deadline(timestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public PermissionOverrideAction resetAllow() {
/* 121 */     this.allow = getOriginalAllow();
/* 122 */     this.allowSet = false;
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public PermissionOverrideAction resetDeny() {
/* 130 */     this.deny = getOriginalDeny();
/* 131 */     this.denySet = false;
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public GuildChannel getChannel() {
/* 139 */     return (GuildChannel)this.channel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Role getRole() {
/* 145 */     return isRole() ? (Role)this.permissionHolder : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Member getMember() {
/* 151 */     return isMember() ? (Member)this.permissionHolder : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getAllow() {
/* 157 */     return getCurrentAllow();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDeny() {
/* 163 */     return getCurrentDeny();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getInherited() {
/* 169 */     return (getAllow() ^ 0xFFFFFFFFFFFFFFFFL) & (getDeny() ^ 0xFFFFFFFFFFFFFFFFL);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMember() {
/* 175 */     return !this.isRole;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRole() {
/* 181 */     return this.isRole;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public PermissionOverrideActionImpl setAllow(long allowBits) {
/* 189 */     checkPermissions(getOriginalAllow() ^ allowBits);
/* 190 */     this.allow = allowBits;
/* 191 */     this.deny = getCurrentDeny() & (allowBits ^ 0xFFFFFFFFFFFFFFFFL);
/* 192 */     this.allowSet = this.denySet = true;
/* 193 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public PermissionOverrideAction grant(long allowBits) {
/* 200 */     return setAllow(getCurrentAllow() | allowBits);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public PermissionOverrideActionImpl setDeny(long denyBits) {
/* 208 */     checkPermissions(getOriginalDeny() ^ denyBits);
/* 209 */     this.deny = denyBits;
/* 210 */     this.allow = getCurrentAllow() & (denyBits ^ 0xFFFFFFFFFFFFFFFFL);
/* 211 */     this.allowSet = this.denySet = true;
/* 212 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public PermissionOverrideAction deny(long denyBits) {
/* 219 */     return setDeny(getCurrentDeny() | denyBits);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public PermissionOverrideAction clear(long inheritedBits) {
/* 226 */     return setAllow(getCurrentAllow() & (inheritedBits ^ 0xFFFFFFFFFFFFFFFFL)).setDeny(getCurrentDeny() & (inheritedBits ^ 0xFFFFFFFFFFFFFFFFL));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkPermissions(long changed) {
/* 231 */     Member selfMember = getGuild().getSelfMember();
/* 232 */     if (changed != 0L && !selfMember.hasPermission(new Permission[] { Permission.ADMINISTRATOR })) {
/*     */       
/* 234 */       long channelPermissions = PermissionUtil.getExplicitPermission((GuildChannel)this.channel, selfMember, false);
/* 235 */       if ((channelPermissions & Permission.MANAGE_PERMISSIONS.getRawValue()) == 0L) {
/*     */ 
/*     */         
/* 238 */         long botPerms = PermissionUtil.getEffectivePermission((GuildChannel)this.channel, selfMember);
/* 239 */         EnumSet<Permission> missing = Permission.getPermissions(changed & (botPerms ^ 0xFFFFFFFFFFFFFFFFL));
/* 240 */         if (!missing.isEmpty()) {
/* 241 */           throw new InsufficientPermissionException(this.channel, Permission.MANAGE_PERMISSIONS, "You must have Permission.MANAGE_PERMISSIONS on the channel explicitly in order to set permissions you don't already have!");
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public PermissionOverrideActionImpl setPermissions(long allowBits, long denyBits) {
/* 251 */     return setAllow(allowBits).setDeny(denyBits);
/*     */   }
/*     */ 
/*     */   
/*     */   private long getCurrentAllow() {
/* 256 */     if (this.allowSet)
/* 257 */       return this.allow; 
/* 258 */     return this.isOverride ? 0L : getOriginalAllow();
/*     */   }
/*     */ 
/*     */   
/*     */   private long getCurrentDeny() {
/* 263 */     if (this.denySet)
/* 264 */       return this.deny; 
/* 265 */     return this.isOverride ? 0L : getOriginalDeny();
/*     */   }
/*     */ 
/*     */   
/*     */   private long getOriginalDeny() {
/* 270 */     PermissionOverride override = (PermissionOverride)this.channel.getOverrideMap().get(this.id);
/* 271 */     return (override == null) ? 0L : override.getDeniedRaw();
/*     */   }
/*     */ 
/*     */   
/*     */   private long getOriginalAllow() {
/* 276 */     PermissionOverride override = (PermissionOverride)this.channel.getOverrideMap().get(this.id);
/* 277 */     return (override == null) ? 0L : override.getAllowedRaw();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 283 */     DataObject object = DataObject.empty();
/* 284 */     object.put("type", Integer.valueOf(isRole() ? 0 : 1));
/* 285 */     object.put("allow", Long.valueOf(getCurrentAllow()));
/* 286 */     object.put("deny", Long.valueOf(getCurrentDeny()));
/* 287 */     reset();
/* 288 */     return getRequestBody(object);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSuccess(Response response, Request<PermissionOverride> request) {
/* 294 */     DataObject object = (DataObject)request.getRawBody();
/* 295 */     PermissionOverrideImpl override = new PermissionOverrideImpl((GuildChannel)this.channel, this.id, isRole());
/* 296 */     override.setAllow(object.getLong("allow"));
/* 297 */     override.setDeny(object.getLong("deny"));
/*     */     
/* 299 */     request.onSuccess(override);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\PermissionOverrideActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */