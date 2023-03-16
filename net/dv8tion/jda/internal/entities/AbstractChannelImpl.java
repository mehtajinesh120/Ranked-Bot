/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import gnu.trove.map.TLongObjectMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Category;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.IPermissionHolder;
/*     */ import net.dv8tion.jda.api.entities.Invite;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.PermissionOverride;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.exceptions.MissingAccessException;
/*     */ import net.dv8tion.jda.api.managers.ChannelManager;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.ChannelAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.InviteAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.managers.ChannelManagerImpl;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.requests.restaction.AuditableRestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.restaction.InviteActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.restaction.PermissionOverrideActionImpl;
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
/*     */ public abstract class AbstractChannelImpl<T extends GuildChannel, M extends AbstractChannelImpl<T, M>>
/*     */   implements GuildChannel
/*     */ {
/*     */   protected final long id;
/*     */   protected final JDAImpl api;
/*  54 */   protected final TLongObjectMap<PermissionOverride> overrides = MiscUtil.newLongMap();
/*     */   
/*     */   protected ChannelManager manager;
/*     */   
/*     */   protected GuildImpl guild;
/*     */   
/*     */   protected long parentId;
/*     */   protected String name;
/*     */   protected int rawPosition;
/*     */   
/*     */   public AbstractChannelImpl(long id, GuildImpl guild) {
/*  65 */     this.id = id;
/*  66 */     this.api = guild.getJDA();
/*  67 */     this.guild = guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(@Nonnull GuildChannel o) {
/*  73 */     Checks.notNull(o, "Channel");
/*  74 */     if (getType().getSortBucket() != o.getType().getSortBucket())
/*  75 */       return Integer.compare(getType().getSortBucket(), o.getType().getSortBucket()); 
/*  76 */     if (getPositionRaw() != o.getPositionRaw())
/*  77 */       return Integer.compare(getPositionRaw(), o.getPositionRaw()); 
/*  78 */     return Long.compareUnsigned(this.id, o.getIdLong());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getAsMention() {
/*  85 */     return "<#" + this.id + '>';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelAction<T> createCopy() {
/*  96 */     return createCopy(getGuild());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getName() {
/* 103 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public GuildImpl getGuild() {
/* 110 */     GuildImpl realGuild = (GuildImpl)this.api.getGuildById(this.guild.getIdLong());
/* 111 */     if (realGuild != null)
/* 112 */       this.guild = realGuild; 
/* 113 */     return this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Category getParent() {
/* 119 */     return (Category)getGuild().getCategoriesView().get(this.parentId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPositionRaw() {
/* 125 */     return this.rawPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/* 132 */     return (JDA)this.api;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PermissionOverride getPermissionOverride(@Nonnull IPermissionHolder permissionHolder) {
/* 138 */     Checks.notNull(permissionHolder, "Permission Holder");
/* 139 */     Checks.check(permissionHolder.getGuild().equals(getGuild()), "Provided permission holder is not from the same guild as this channel!");
/* 140 */     return (PermissionOverride)this.overrides.get(permissionHolder.getIdLong());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<PermissionOverride> getPermissionOverrides() {
/* 147 */     return Arrays.asList((PermissionOverride[])this.overrides.values((Object[])new PermissionOverride[this.overrides.size()]));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<PermissionOverride> getMemberPermissionOverrides() {
/* 154 */     return Collections.unmodifiableList((List<? extends PermissionOverride>)getPermissionOverrides().stream()
/* 155 */         .filter(PermissionOverride::isMemberOverride)
/* 156 */         .collect(Collectors.toList()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<PermissionOverride> getRolePermissionOverrides() {
/* 163 */     return Collections.unmodifiableList((List<? extends PermissionOverride>)getPermissionOverrides().stream()
/* 164 */         .filter(PermissionOverride::isRoleOverride)
/* 165 */         .collect(Collectors.toList()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSynced() {
/* 171 */     AbstractChannelImpl<?, ?> parent = (AbstractChannelImpl<?, ?>)getParent();
/* 172 */     if (parent == null)
/* 173 */       return true; 
/* 174 */     TLongObjectMap<PermissionOverride> parentOverrides = parent.getOverrideMap();
/* 175 */     if (parentOverrides.size() != this.overrides.size()) {
/* 176 */       return false;
/*     */     }
/* 178 */     for (PermissionOverride override : parentOverrides.valueCollection()) {
/*     */       
/* 180 */       PermissionOverride ourOverride = (PermissionOverride)this.overrides.get(override.getIdLong());
/* 181 */       if (ourOverride == null) {
/* 182 */         return false;
/*     */       }
/* 184 */       if (ourOverride.getAllowedRaw() != override.getAllowedRaw() || ourOverride.getDeniedRaw() != override.getDeniedRaw()) {
/* 185 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 189 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelManager getManager() {
/* 196 */     if (this.manager == null)
/* 197 */       return this.manager = (ChannelManager)new ChannelManagerImpl(this); 
/* 198 */     return this.manager;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<Void> delete() {
/* 205 */     checkPermission(Permission.MANAGE_CHANNEL);
/*     */     
/* 207 */     Route.CompiledRoute route = Route.Channels.DELETE_CHANNEL.compile(new String[] { getId() });
/* 208 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl(getJDA(), route);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public PermissionOverrideAction createPermissionOverride(@Nonnull IPermissionHolder permissionHolder) {
/* 215 */     Checks.notNull(permissionHolder, "PermissionHolder");
/* 216 */     if (getPermissionOverride(permissionHolder) != null) {
/* 217 */       throw new IllegalStateException("Provided member already has a PermissionOverride in this channel!");
/*     */     }
/* 219 */     return putPermissionOverride(permissionHolder);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public PermissionOverrideAction putPermissionOverride(@Nonnull IPermissionHolder permissionHolder) {
/* 226 */     checkPermission(Permission.MANAGE_PERMISSIONS);
/* 227 */     Checks.notNull(permissionHolder, "PermissionHolder");
/* 228 */     Checks.check(permissionHolder.getGuild().equals(getGuild()), "Provided permission holder is not from the same guild as this channel!");
/* 229 */     return (PermissionOverrideAction)new PermissionOverrideActionImpl(getJDA(), this, permissionHolder);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public InviteAction createInvite() {
/* 236 */     checkPermission(Permission.CREATE_INSTANT_INVITE);
/*     */     
/* 238 */     return (InviteAction)new InviteActionImpl(getJDA(), getId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<List<Invite>> retrieveInvites() {
/* 245 */     checkPermission(Permission.MANAGE_CHANNEL);
/*     */     
/* 247 */     Route.CompiledRoute route = Route.Invites.GET_CHANNEL_INVITES.compile(new String[] { getId() });
/*     */     
/* 249 */     JDAImpl jda = (JDAImpl)getJDA();
/* 250 */     return (RestAction<List<Invite>>)new RestActionImpl((JDA)jda, route, (response, request) -> {
/*     */           EntityBuilder entityBuilder = jda.getEntityBuilder();
/*     */           DataArray array = response.getArray();
/*     */           List<Invite> invites = new ArrayList<>(array.length());
/*     */           for (int i = 0; i < array.length(); i++) {
/*     */             invites.add(entityBuilder.createInvite(array.getObject(i)));
/*     */           }
/*     */           return Collections.unmodifiableList(invites);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/* 264 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 270 */     return Long.hashCode(this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 276 */     if (obj == this)
/* 277 */       return true; 
/* 278 */     if (!(obj instanceof GuildChannel))
/* 279 */       return false; 
/* 280 */     GuildChannel channel = (GuildChannel)obj;
/* 281 */     return (channel.getIdLong() == getIdLong());
/*     */   }
/*     */ 
/*     */   
/*     */   public TLongObjectMap<PermissionOverride> getOverrideMap() {
/* 286 */     return this.overrides;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public M setName(String name) {
/* 292 */     this.name = name;
/* 293 */     return (M)this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public M setParent(long parentId) {
/* 299 */     this.parentId = parentId;
/* 300 */     return (M)this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public M setPosition(int rawPosition) {
/* 306 */     this.rawPosition = rawPosition;
/* 307 */     return (M)this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkAccess() {
/* 312 */     Member selfMember = getGuild().getSelfMember();
/* 313 */     if (!selfMember.hasPermission(this, new Permission[] { Permission.VIEW_CHANNEL })) {
/* 314 */       throw new MissingAccessException(this, Permission.VIEW_CHANNEL);
/*     */     }
/* 316 */     if (!selfMember.hasAccess(this))
/* 317 */       throw new MissingAccessException(this, Permission.VOICE_CONNECT); 
/*     */   }
/*     */   protected void checkPermission(Permission permission) {
/* 320 */     checkPermission(permission, (String)null);
/*     */   }
/*     */   protected void checkPermission(Permission permission, String message) {
/* 323 */     checkAccess();
/* 324 */     if (!getGuild().getSelfMember().hasPermission(this, new Permission[] { permission })) {
/*     */       
/* 326 */       if (message != null) {
/* 327 */         throw new InsufficientPermissionException(this, permission, message);
/*     */       }
/* 329 */       throw new InsufficientPermissionException(this, permission);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   public abstract ChannelAction<T> createCopy(@Nonnull Guild paramGuild);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\AbstractChannelImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */