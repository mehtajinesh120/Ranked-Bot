/*      */ package net.dv8tion.jda.internal.entities;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import javax.annotation.Nonnull;
/*      */ import javax.annotation.Nullable;
/*      */ import net.dv8tion.jda.api.JDA;
/*      */ import net.dv8tion.jda.api.Permission;
/*      */ import net.dv8tion.jda.api.Region;
/*      */ import net.dv8tion.jda.api.entities.AbstractChannel;
/*      */ import net.dv8tion.jda.api.entities.Category;
/*      */ import net.dv8tion.jda.api.entities.ChannelType;
/*      */ import net.dv8tion.jda.api.entities.Emote;
/*      */ import net.dv8tion.jda.api.entities.Guild;
/*      */ import net.dv8tion.jda.api.entities.GuildChannel;
/*      */ import net.dv8tion.jda.api.entities.GuildVoiceState;
/*      */ import net.dv8tion.jda.api.entities.Invite;
/*      */ import net.dv8tion.jda.api.entities.ListedEmote;
/*      */ import net.dv8tion.jda.api.entities.Member;
/*      */ import net.dv8tion.jda.api.entities.Role;
/*      */ import net.dv8tion.jda.api.entities.StageChannel;
/*      */ import net.dv8tion.jda.api.entities.StageInstance;
/*      */ import net.dv8tion.jda.api.entities.StoreChannel;
/*      */ import net.dv8tion.jda.api.entities.TextChannel;
/*      */ import net.dv8tion.jda.api.entities.User;
/*      */ import net.dv8tion.jda.api.entities.VanityInvite;
/*      */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*      */ import net.dv8tion.jda.api.entities.Webhook;
/*      */ import net.dv8tion.jda.api.entities.templates.Template;
/*      */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*      */ import net.dv8tion.jda.api.interactions.commands.Command;
/*      */ import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
/*      */ import net.dv8tion.jda.api.managers.AudioManager;
/*      */ import net.dv8tion.jda.api.requests.GatewayIntent;
/*      */ import net.dv8tion.jda.api.requests.Request;
/*      */ import net.dv8tion.jda.api.requests.Response;
/*      */ import net.dv8tion.jda.api.requests.RestAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.ChannelAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.order.CategoryOrderAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.order.ChannelOrderAction;
/*      */ import net.dv8tion.jda.api.utils.cache.SortedSnowflakeCacheView;
/*      */ import net.dv8tion.jda.api.utils.concurrent.Task;
/*      */ import net.dv8tion.jda.api.utils.data.DataArray;
/*      */ import net.dv8tion.jda.api.utils.data.DataObject;
/*      */ import net.dv8tion.jda.internal.JDAImpl;
/*      */ import net.dv8tion.jda.internal.managers.AudioManagerImpl;
/*      */ import net.dv8tion.jda.internal.requests.CompletedRestAction;
/*      */ import net.dv8tion.jda.internal.requests.DeferredRestAction;
/*      */ import net.dv8tion.jda.internal.requests.MemberChunkManager;
/*      */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*      */ import net.dv8tion.jda.internal.requests.Route;
/*      */ import net.dv8tion.jda.internal.requests.WebSocketClient;
/*      */ import net.dv8tion.jda.internal.requests.restaction.AuditableRestActionImpl;
/*      */ import net.dv8tion.jda.internal.requests.restaction.ChannelActionImpl;
/*      */ import net.dv8tion.jda.internal.utils.Checks;
/*      */ import net.dv8tion.jda.internal.utils.UnlockHook;
/*      */ import net.dv8tion.jda.internal.utils.cache.AbstractCacheView;
/*      */ import net.dv8tion.jda.internal.utils.cache.SortedSnowflakeCacheViewImpl;
/*      */ import net.dv8tion.jda.internal.utils.concurrent.task.GatewayTask;
/*      */ 
/*      */ public class GuildImpl implements Guild {
/*      */   private final long id;
/*   77 */   private final SortedSnowflakeCacheViewImpl<Category> categoryCache = new SortedSnowflakeCacheViewImpl(Category.class, AbstractChannel::getName, Comparator.naturalOrder()); private final JDAImpl api;
/*   78 */   private final SortedSnowflakeCacheViewImpl<VoiceChannel> voiceChannelCache = new SortedSnowflakeCacheViewImpl(VoiceChannel.class, AbstractChannel::getName, Comparator.naturalOrder());
/*   79 */   private final SortedSnowflakeCacheViewImpl<StoreChannel> storeChannelCache = new SortedSnowflakeCacheViewImpl(StoreChannel.class, AbstractChannel::getName, Comparator.naturalOrder());
/*   80 */   private final SortedSnowflakeCacheViewImpl<TextChannel> textChannelCache = new SortedSnowflakeCacheViewImpl(TextChannel.class, AbstractChannel::getName, Comparator.naturalOrder());
/*   81 */   private final SortedSnowflakeCacheViewImpl<Role> roleCache = new SortedSnowflakeCacheViewImpl(Role.class, Role::getName, Comparator.reverseOrder());
/*   82 */   private final SnowflakeCacheViewImpl<Emote> emoteCache = new SnowflakeCacheViewImpl(Emote.class, Emote::getName);
/*   83 */   private final MemberCacheViewImpl memberCache = new MemberCacheViewImpl(); private final CacheView.SimpleCacheView<MemberPresenceImpl> memberPresences;
/*      */   private GuildManager manager;
/*      */   private CompletableFuture<Void> pendingRequestToSpeak;
/*      */   private Member owner;
/*      */   private String name;
/*      */   private String iconId;
/*      */   private String splashId;
/*      */   private String region;
/*      */   private String vanityCode;
/*      */   private String description;
/*      */   private String banner;
/*      */   private int maxPresences;
/*      */   private int maxMembers;
/*      */   private int boostCount;
/*      */   private long ownerId;
/*      */   private Set<String> features;
/*      */   private VoiceChannel afkChannel;
/*      */   private TextChannel systemChannel;
/*      */   private TextChannel rulesChannel;
/*      */   private TextChannel communityUpdatesChannel;
/*      */   private Role publicRole;
/*  104 */   private Guild.VerificationLevel verificationLevel = Guild.VerificationLevel.UNKNOWN;
/*  105 */   private Guild.NotificationLevel defaultNotificationLevel = Guild.NotificationLevel.UNKNOWN;
/*  106 */   private Guild.MFALevel mfaLevel = Guild.MFALevel.UNKNOWN;
/*  107 */   private Guild.ExplicitContentLevel explicitContentLevel = Guild.ExplicitContentLevel.UNKNOWN;
/*  108 */   private Guild.NSFWLevel nsfwLevel = Guild.NSFWLevel.UNKNOWN;
/*      */   private Guild.Timeout afkTimeout;
/*  110 */   private Guild.BoostTier boostTier = Guild.BoostTier.NONE;
/*  111 */   private Locale preferredLocale = Locale.ENGLISH;
/*      */   
/*      */   private boolean available;
/*      */   private boolean canSendVerification = false;
/*      */   private int memberCount;
/*      */   
/*      */   public GuildImpl(JDAImpl api, long id) {
/*  118 */     this.id = id;
/*  119 */     this.api = api;
/*  120 */     if (api.getCacheFlags().stream().anyMatch(CacheFlag::isPresence)) {
/*  121 */       this.memberPresences = new CacheView.SimpleCacheView(MemberPresenceImpl.class, null);
/*      */     } else {
/*  123 */       this.memberPresences = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<List<Command>> retrieveCommands() {
/*  130 */     Route.CompiledRoute route = Route.Interactions.GET_GUILD_COMMANDS.compile(new String[] { getJDA().getSelfUser().getApplicationId(), getId() });
/*  131 */     return (RestAction<List<Command>>)new RestActionImpl((JDA)getJDA(), route, (response, request) -> (List)response.getArray().stream(DataArray::getObject).map(()).collect(Collectors.toList()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Command> retrieveCommandById(@Nonnull String id) {
/*  143 */     Checks.isSnowflake(id);
/*  144 */     Route.CompiledRoute route = Route.Interactions.GET_GUILD_COMMAND.compile(new String[] { getJDA().getSelfUser().getApplicationId(), getId(), id });
/*  145 */     return (RestAction<Command>)new RestActionImpl((JDA)getJDA(), route, (response, request) -> new Command(getJDA(), this, response.getObject()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public CommandCreateAction upsertCommand(@Nonnull CommandData command) {
/*  152 */     Checks.notNull(command, "CommandData");
/*  153 */     return (CommandCreateAction)new CommandCreateActionImpl(this, command);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public CommandListUpdateAction updateCommands() {
/*  160 */     Route.CompiledRoute route = Route.Interactions.UPDATE_GUILD_COMMANDS.compile(new String[] { getJDA().getSelfUser().getApplicationId(), getId() });
/*  161 */     return (CommandListUpdateAction)new CommandListUpdateActionImpl((JDA)getJDA(), this, route);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public CommandEditAction editCommandById(@Nonnull String id) {
/*  168 */     Checks.isSnowflake(id);
/*  169 */     return (CommandEditAction)new CommandEditActionImpl(this, id);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Void> deleteCommandById(@Nonnull String commandId) {
/*  176 */     Checks.isSnowflake(commandId);
/*  177 */     Route.CompiledRoute route = Route.Interactions.DELETE_GUILD_COMMAND.compile(new String[] { getJDA().getSelfUser().getApplicationId(), getId(), commandId });
/*  178 */     return (RestAction<Void>)new RestActionImpl((JDA)getJDA(), route);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<List<CommandPrivilege>> retrieveCommandPrivilegesById(@Nonnull String commandId) {
/*  185 */     Checks.isSnowflake(commandId, "ID");
/*  186 */     Route.CompiledRoute route = Route.Interactions.GET_COMMAND_PERMISSIONS.compile(new String[] { getJDA().getSelfUser().getApplicationId(), getId(), commandId });
/*  187 */     return (RestAction<List<CommandPrivilege>>)new RestActionImpl((JDA)getJDA(), route, (response, request) -> parsePrivilegesList(response.getObject()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Map<String, List<CommandPrivilege>>> retrieveCommandPrivileges() {
/*  194 */     Route.CompiledRoute route = Route.Interactions.GET_ALL_COMMAND_PERMISSIONS.compile(new String[] { getJDA().getSelfUser().getApplicationId(), getId() });
/*  195 */     return (RestAction<Map<String, List<CommandPrivilege>>>)new RestActionImpl((JDA)getJDA(), route, (response, request) -> {
/*      */           Map<String, List<CommandPrivilege>> privileges = new HashMap<>();
/*      */           response.getArray().stream(DataArray::getObject).forEach(());
/*      */           return privileges;
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<List<CommandPrivilege>> updateCommandPrivilegesById(@Nonnull String id, @Nonnull Collection<? extends CommandPrivilege> privileges) {
/*  210 */     Checks.isSnowflake(id, "ID");
/*  211 */     Checks.noneNull(privileges, "Privileges");
/*  212 */     Checks.check((privileges.size() <= 10), "Cannot have more than 10 privileges for a command!");
/*  213 */     Route.CompiledRoute route = Route.Interactions.EDIT_COMMAND_PERMISSIONS.compile(new String[] { getJDA().getSelfUser().getApplicationId(), getId(), id });
/*  214 */     DataArray array = DataArray.fromCollection(privileges);
/*  215 */     return (RestAction<List<CommandPrivilege>>)new RestActionImpl((JDA)getJDA(), route, DataObject.empty().put("permissions", array), (response, request) -> parsePrivilegesList(response.getObject()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Map<String, List<CommandPrivilege>>> updateCommandPrivileges(@Nonnull Map<String, Collection<? extends CommandPrivilege>> privileges) {
/*  223 */     Checks.notNull(privileges, "Privileges");
/*  224 */     privileges.forEach((key, value) -> {
/*      */           Checks.isSnowflake(key, "Map Key");
/*      */           Checks.noneNull(value, "Privilege List for Command");
/*      */           Checks.check((value.size() <= 10), "Cannot have more than 10 privileges for a command!");
/*      */         });
/*  229 */     DataArray array = DataArray.empty();
/*  230 */     privileges.forEach((commandId, list) -> {
/*      */           DataObject entry = DataObject.empty();
/*      */           
/*      */           entry.put("id", commandId);
/*      */           entry.put("permissions", DataArray.fromCollection(list));
/*      */           array.add(entry);
/*      */         });
/*  237 */     Route.CompiledRoute route = Route.Interactions.EDIT_ALL_COMMAND_PERMISSIONS.compile(new String[] { getJDA().getSelfUser().getApplicationId(), getId() });
/*  238 */     return (RestAction<Map<String, List<CommandPrivilege>>>)new RestActionImpl((JDA)getJDA(), route, RequestBody.create(Requester.MEDIA_TYPE_JSON, array.toJson()), (response, request) -> {
/*      */           Map<String, List<CommandPrivilege>> map = new HashMap<>();
/*      */           response.getArray().stream(DataArray::getObject).forEach(());
/*      */           return map;
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<CommandPrivilege> parsePrivilegesList(DataObject obj) {
/*  251 */     return (List<CommandPrivilege>)obj.getArray("permissions")
/*  252 */       .stream(DataArray::getObject)
/*  253 */       .map(this::parsePrivilege)
/*  254 */       .collect(Collectors.toList());
/*      */   }
/*      */ 
/*      */   
/*      */   private CommandPrivilege parsePrivilege(DataObject data) {
/*  259 */     CommandPrivilege.Type type = CommandPrivilege.Type.fromKey(data.getInt("type", 1));
/*  260 */     boolean enabled = data.getBoolean("permission");
/*  261 */     return new CommandPrivilege(type, enabled, data.getUnsignedLong("id"));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<EnumSet<Region>> retrieveRegions(boolean includeDeprecated) {
/*  268 */     Route.CompiledRoute route = Route.Guilds.GET_VOICE_REGIONS.compile(new String[] { getId() });
/*  269 */     return (RestAction<EnumSet<Region>>)new RestActionImpl((JDA)getJDA(), route, (response, request) -> {
/*      */           EnumSet<Region> set = EnumSet.noneOf(Region.class);
/*      */           DataArray arr = response.getArray();
/*      */           for (int i = 0; i < arr.length(); i++) {
/*      */             DataObject obj = arr.getObject(i);
/*      */             if (includeDeprecated || !obj.getBoolean("deprecated")) {
/*      */               String id = obj.getString("id", "");
/*      */               Region region = Region.fromKey(id);
/*      */               if (region != Region.UNKNOWN) {
/*      */                 set.add(region);
/*      */               }
/*      */             } 
/*      */           } 
/*      */           return set;
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public MemberAction addMember(@Nonnull String accessToken, @Nonnull String userId) {
/*  291 */     Checks.notBlank(accessToken, "Access-Token");
/*  292 */     Checks.isSnowflake(userId, "User ID");
/*  293 */     Checks.check((getMemberById(userId) == null), "User is already in this guild");
/*  294 */     if (!getSelfMember().hasPermission(new Permission[] { Permission.CREATE_INSTANT_INVITE }))
/*  295 */       throw new InsufficientPermissionException(this, Permission.CREATE_INSTANT_INVITE); 
/*  296 */     return (MemberAction)new MemberActionImpl((JDA)getJDA(), this, userId, accessToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLoaded() {
/*  303 */     return (getJDA().isIntent(GatewayIntent.GUILD_MEMBERS) && 
/*  304 */       getMemberCount() <= getMemberCache().size());
/*      */   }
/*      */ 
/*      */   
/*      */   public void pruneMemberCache()
/*      */   {
/*  310 */     UnlockHook h = this.memberCache.writeLock();
/*      */     
/*  312 */     try { EntityBuilder builder = getJDA().getEntityBuilder();
/*  313 */       Set<Member> members = this.memberCache.asSet();
/*  314 */       members.forEach(m -> builder.updateMemberCache((MemberImpl)m));
/*  315 */       if (h != null) h.close();  }
/*      */     catch (Throwable throwable) { if (h != null)
/*      */         try { h.close(); }
/*      */         catch (Throwable throwable1)
/*      */         { throwable.addSuppressed(throwable1); }
/*      */           throw throwable; }
/*  321 */      } public boolean unloadMember(long userId) { if (userId == this.api.getSelfUser().getIdLong())
/*  322 */       return false; 
/*  323 */     MemberImpl member = (MemberImpl)getMemberById(userId);
/*  324 */     if (member == null)
/*  325 */       return false; 
/*  326 */     this.api.getEntityBuilder().updateMemberCache(member, true);
/*  327 */     return true; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMemberCount() {
/*  333 */     return this.memberCount;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public String getName() {
/*  340 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getIconId() {
/*  346 */     return this.iconId;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Set<String> getFeatures() {
/*  353 */     return this.features;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSplashId() {
/*  359 */     return this.splashId;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @Deprecated
/*      */   public RestAction<String> retrieveVanityUrl() {
/*  367 */     if (!getSelfMember().hasPermission(new Permission[] { Permission.MANAGE_SERVER }))
/*  368 */       throw new InsufficientPermissionException(this, Permission.MANAGE_SERVER); 
/*  369 */     if (!getFeatures().contains("VANITY_URL")) {
/*  370 */       throw new IllegalStateException("This guild doesn't have a vanity url");
/*      */     }
/*  372 */     Route.CompiledRoute route = Route.Guilds.GET_VANITY_URL.compile(new String[] { getId() });
/*      */     
/*  374 */     return (RestAction<String>)new RestActionImpl((JDA)getJDA(), route, (response, request) -> response.getObject().getString("code"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getVanityCode() {
/*  382 */     return this.vanityCode;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<VanityInvite> retrieveVanityInvite() {
/*  389 */     checkPermission(Permission.MANAGE_SERVER);
/*  390 */     JDAImpl api = getJDA();
/*  391 */     Route.CompiledRoute route = Route.Guilds.GET_VANITY_URL.compile(new String[] { getId() });
/*  392 */     return (RestAction<VanityInvite>)new RestActionImpl((JDA)api, route, (response, request) -> new VanityInvite(this.vanityCode, response.getObject().getInt("uses")));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getDescription() {
/*  400 */     return this.description;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Locale getLocale() {
/*  407 */     return this.preferredLocale;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getBannerId() {
/*  414 */     return this.banner;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Guild.BoostTier getBoostTier() {
/*  421 */     return this.boostTier;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getBoostCount() {
/*  427 */     return this.boostCount;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public List<Member> getBoosters() {
/*  435 */     return (List<Member>)this.memberCache.applyStream(members -> (List)members.filter(()).sorted(Comparator.comparing(Member::getTimeBoosted)).collect(Collectors.toList()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxMembers() {
/*  444 */     return this.maxMembers;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxPresences() {
/*  450 */     return this.maxPresences;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Guild.MetaData> retrieveMetaData() {
/*  457 */     Route.CompiledRoute route = Route.Guilds.GET_GUILD.compile(new String[] { getId() });
/*  458 */     route = route.withQueryParams(new String[] { "with_counts", "true" });
/*  459 */     return (RestAction<Guild.MetaData>)new RestActionImpl((JDA)getJDA(), route, (response, request) -> {
/*      */           DataObject json = response.getObject();
/*      */           int memberLimit = json.getInt("max_members", 0);
/*      */           int presenceLimit = json.getInt("max_presences", 5000);
/*      */           this.maxMembers = memberLimit;
/*      */           this.maxPresences = presenceLimit;
/*      */           int approxMembers = json.getInt("approximate_member_count", this.memberCount);
/*      */           int approxPresence = json.getInt("approximate_presence_count", 0);
/*      */           return new Guild.MetaData(memberLimit, presenceLimit, approxPresence, approxMembers);
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public VoiceChannel getAfkChannel() {
/*  474 */     return this.afkChannel;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public TextChannel getSystemChannel() {
/*  480 */     return this.systemChannel;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public TextChannel getRulesChannel() {
/*  486 */     return this.rulesChannel;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public TextChannel getCommunityUpdatesChannel() {
/*  492 */     return this.communityUpdatesChannel;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<List<Webhook>> retrieveWebhooks() {
/*  499 */     if (!getSelfMember().hasPermission(new Permission[] { Permission.MANAGE_WEBHOOKS })) {
/*  500 */       throw new InsufficientPermissionException(this, Permission.MANAGE_WEBHOOKS);
/*      */     }
/*  502 */     Route.CompiledRoute route = Route.Guilds.GET_WEBHOOKS.compile(new String[] { getId() });
/*      */     
/*  504 */     return (RestAction<List<Webhook>>)new RestActionImpl((JDA)getJDA(), route, (response, request) -> {
/*      */           DataArray array = response.getArray();
/*      */ 
/*      */           
/*      */           List<Webhook> webhooks = new ArrayList<>(array.length());
/*      */ 
/*      */           
/*      */           EntityBuilder builder = this.api.getEntityBuilder();
/*      */           
/*      */           for (int i = 0; i < array.length(); i++) {
/*      */             try {
/*      */               webhooks.add(builder.createWebhook(array.getObject(i)));
/*  516 */             } catch (Exception e) {
/*      */               JDAImpl.LOG.error("Error creating webhook from json", e);
/*      */             } 
/*      */           } 
/*      */           return Collections.unmodifiableList(webhooks);
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Member getOwner() {
/*  529 */     return this.owner;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getOwnerIdLong() {
/*  535 */     return this.ownerId;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Guild.Timeout getAfkTimeout() {
/*  542 */     return this.afkTimeout;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public String getRegionRaw() {
/*  549 */     return this.region;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMember(@Nonnull User user) {
/*  555 */     return (this.memberCache.get(user.getIdLong()) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Member getSelfMember() {
/*  562 */     Member member = getMember((User)getJDA().getSelfUser());
/*  563 */     if (member == null)
/*  564 */       throw new IllegalStateException("Guild does not have a self member"); 
/*  565 */     return member;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Member getMember(@Nonnull User user) {
/*  571 */     Checks.notNull(user, "User");
/*  572 */     return getMemberById(user.getIdLong());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public MemberCacheView getMemberCache() {
/*  579 */     return (MemberCacheView)this.memberCache;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public SortedSnowflakeCacheView<Category> getCategoryCache() {
/*  586 */     return (SortedSnowflakeCacheView<Category>)this.categoryCache;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public SortedSnowflakeCacheView<StoreChannel> getStoreChannelCache() {
/*  593 */     return (SortedSnowflakeCacheView<StoreChannel>)this.storeChannelCache;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public SortedSnowflakeCacheView<TextChannel> getTextChannelCache() {
/*  600 */     return (SortedSnowflakeCacheView<TextChannel>)this.textChannelCache;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public SortedSnowflakeCacheView<VoiceChannel> getVoiceChannelCache() {
/*  607 */     return (SortedSnowflakeCacheView<VoiceChannel>)this.voiceChannelCache;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public SortedSnowflakeCacheView<Role> getRoleCache() {
/*  614 */     return (SortedSnowflakeCacheView<Role>)this.roleCache;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public SnowflakeCacheView<Emote> getEmoteCache()
/*      */   {
/*  621 */     return (SnowflakeCacheView<Emote>)this.emoteCache; } @Nonnull
/*      */   public List<GuildChannel> getChannels(boolean includeHidden) {
/*      */     List<GuildChannel> channels;
/*      */     List<TextChannel> textChannels;
/*      */     List<StoreChannel> storeChannels;
/*      */     List<VoiceChannel> voiceChannels;
/*      */     List<Category> categories;
/*  628 */     Member self = getSelfMember();
/*  629 */     Predicate<GuildChannel> filterHidden = it -> self.hasPermission(it, new Permission[] { Permission.VIEW_CHANNEL });
/*      */ 
/*      */     
/*  632 */     SortedSnowflakeCacheViewImpl<Category> sortedSnowflakeCacheViewImpl = getCategoriesView();
/*  633 */     SortedSnowflakeCacheViewImpl<VoiceChannel> sortedSnowflakeCacheViewImpl1 = getVoiceChannelsView();
/*  634 */     SortedSnowflakeCacheViewImpl<TextChannel> sortedSnowflakeCacheViewImpl2 = getTextChannelsView();
/*  635 */     SortedSnowflakeCacheViewImpl<StoreChannel> sortedSnowflakeCacheViewImpl3 = getStoreChannelView();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  640 */     UnlockHook categoryHook = sortedSnowflakeCacheViewImpl.readLock(); 
/*  641 */     try { UnlockHook voiceHook = sortedSnowflakeCacheViewImpl1.readLock(); 
/*  642 */       try { UnlockHook textHook = sortedSnowflakeCacheViewImpl2.readLock(); 
/*  643 */         try { UnlockHook storeHook = sortedSnowflakeCacheViewImpl3.readLock();
/*      */           
/*  645 */           try { if (includeHidden) {
/*      */               
/*  647 */               storeChannels = sortedSnowflakeCacheViewImpl3.asList();
/*  648 */               textChannels = sortedSnowflakeCacheViewImpl2.asList();
/*  649 */               voiceChannels = sortedSnowflakeCacheViewImpl1.asList();
/*      */             }
/*      */             else {
/*      */               
/*  653 */               storeChannels = (List<StoreChannel>)sortedSnowflakeCacheViewImpl3.stream().filter(filterHidden).collect(Collectors.toList());
/*  654 */               textChannels = (List<TextChannel>)sortedSnowflakeCacheViewImpl2.stream().filter(filterHidden).collect(Collectors.toList());
/*  655 */               voiceChannels = (List<VoiceChannel>)sortedSnowflakeCacheViewImpl1.stream().filter(filterHidden).collect(Collectors.toList());
/*      */             } 
/*  657 */             categories = sortedSnowflakeCacheViewImpl.asList();
/*  658 */             channels = new ArrayList<>((int)sortedSnowflakeCacheViewImpl.size() + voiceChannels.size() + textChannels.size() + storeChannels.size());
/*  659 */             if (storeHook != null) storeHook.close();  } catch (Throwable throwable) { if (storeHook != null) try { storeHook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (textHook != null) textHook.close();  } catch (Throwable throwable) { if (textHook != null) try { textHook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (voiceHook != null) voiceHook.close();  } catch (Throwable throwable) { if (voiceHook != null) try { voiceHook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (categoryHook != null) categoryHook.close();  } catch (Throwable throwable) { if (categoryHook != null)
/*      */         try { categoryHook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*  661 */      Objects.requireNonNull(channels); storeChannels.stream().filter(it -> (it.getParent() == null)).forEach(channels::add);
/*  662 */     Objects.requireNonNull(channels); textChannels.stream().filter(it -> (it.getParent() == null)).forEach(channels::add);
/*  663 */     Collections.sort(channels);
/*  664 */     Objects.requireNonNull(channels); voiceChannels.stream().filter(it -> (it.getParent() == null)).forEach(channels::add);
/*      */     
/*  666 */     for (Category category : categories) {
/*      */       List<GuildChannel> children;
/*      */       
/*  669 */       if (includeHidden) {
/*      */         
/*  671 */         children = category.getChannels();
/*      */       }
/*      */       else {
/*      */         
/*  675 */         children = (List<GuildChannel>)category.getChannels().stream().filter(filterHidden).collect(Collectors.toList());
/*  676 */         if (children.isEmpty()) {
/*      */           continue;
/*      */         }
/*      */       } 
/*  680 */       channels.add(category);
/*  681 */       channels.addAll(children);
/*      */     } 
/*      */     
/*  684 */     return Collections.unmodifiableList(channels);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<List<ListedEmote>> retrieveEmotes() {
/*  691 */     Route.CompiledRoute route = Route.Emotes.GET_EMOTES.compile(new String[] { getId() });
/*  692 */     return (RestAction<List<ListedEmote>>)new RestActionImpl((JDA)getJDA(), route, (response, request) -> {
/*      */           EntityBuilder builder = getJDA().getEntityBuilder();
/*      */           DataArray emotes = response.getArray();
/*      */           List<ListedEmote> list = new ArrayList<>(emotes.length());
/*      */           for (int i = 0; i < emotes.length(); i++) {
/*      */             DataObject emote = emotes.getObject(i);
/*      */             list.add(builder.createEmote(this, emote));
/*      */           } 
/*      */           return Collections.unmodifiableList(list);
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<ListedEmote> retrieveEmoteById(@Nonnull String id) {
/*  712 */     Checks.isSnowflake(id, "Emote ID");
/*      */     
/*  714 */     JDAImpl jda = getJDA();
/*  715 */     return (RestAction<ListedEmote>)new DeferredRestAction((JDA)jda, ListedEmote.class, () -> {
/*      */           Emote emote = getEmoteById(id);
/*      */           if (emote != null) {
/*      */             ListedEmote listedEmote = (ListedEmote)emote;
/*      */             if (listedEmote.hasUser() || !getSelfMember().hasPermission(new Permission[] { Permission.MANAGE_EMOTES }, )) {
/*      */               return listedEmote;
/*      */             }
/*      */           } 
/*      */           return null;
/*      */         }() -> {
/*      */           Route.CompiledRoute route = Route.Emotes.GET_EMOTE.compile(new String[] { getId(), id });
/*      */           return new AuditableRestActionImpl((JDA)jda, route, ());
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestActionImpl<List<Guild.Ban>> retrieveBanList() {
/*  739 */     if (!getSelfMember().hasPermission(new Permission[] { Permission.BAN_MEMBERS })) {
/*  740 */       throw new InsufficientPermissionException(this, Permission.BAN_MEMBERS);
/*      */     }
/*  742 */     Route.CompiledRoute route = Route.Guilds.GET_BANS.compile(new String[] { getId() });
/*  743 */     return new RestActionImpl((JDA)getJDA(), route, (response, request) -> {
/*      */           EntityBuilder builder = this.api.getEntityBuilder();
/*      */           List<Guild.Ban> bans = new LinkedList<>();
/*      */           DataArray bannedArr = response.getArray();
/*      */           for (int i = 0; i < bannedArr.length(); i++) {
/*      */             DataObject object = bannedArr.getObject(i);
/*      */             DataObject user = object.getObject("user");
/*      */             bans.add(new Guild.Ban(builder.createUser(user), object.getString("reason", null)));
/*      */           } 
/*      */           return Collections.unmodifiableList(bans);
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Guild.Ban> retrieveBanById(@Nonnull String userId) {
/*  763 */     if (!getSelfMember().hasPermission(new Permission[] { Permission.BAN_MEMBERS })) {
/*  764 */       throw new InsufficientPermissionException(this, Permission.BAN_MEMBERS);
/*      */     }
/*  766 */     Checks.isSnowflake(userId, "User ID");
/*      */     
/*  768 */     Route.CompiledRoute route = Route.Guilds.GET_BAN.compile(new String[] { getId(), userId });
/*  769 */     return (RestAction<Guild.Ban>)new RestActionImpl((JDA)getJDA(), route, (response, request) -> {
/*      */           EntityBuilder builder = this.api.getEntityBuilder();
/*      */           DataObject bannedObj = response.getObject();
/*      */           DataObject user = bannedObj.getObject("user");
/*      */           return new Guild.Ban(builder.createUser(user), bannedObj.getString("reason", null));
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Integer> retrievePrunableMemberCount(int days) {
/*  783 */     if (!getSelfMember().hasPermission(new Permission[] { Permission.KICK_MEMBERS })) {
/*  784 */       throw new InsufficientPermissionException(this, Permission.KICK_MEMBERS);
/*      */     }
/*  786 */     Checks.check((days >= 1 && days <= 30), "Provided %d days must be between 1 and 30.", Integer.valueOf(days));
/*      */     
/*  788 */     Route.CompiledRoute route = Route.Guilds.PRUNABLE_COUNT.compile(new String[] { getId() }).withQueryParams(new String[] { "days", Integer.toString(days) });
/*  789 */     return (RestAction<Integer>)new RestActionImpl((JDA)getJDA(), route, (response, request) -> Integer.valueOf(response.getObject().getInt("pruned")));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Role getPublicRole() {
/*  796 */     return this.publicRole;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public TextChannel getDefaultChannel() {
/*  803 */     Role role = getPublicRole();
/*  804 */     return getTextChannelsView().stream()
/*  805 */       .filter(c -> role.hasPermission((GuildChannel)c, new Permission[] { Permission.MESSAGE_READ
/*  806 */           })).min(Comparator.naturalOrder()).orElse(null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public GuildManager getManager() {
/*  813 */     if (this.manager == null)
/*  814 */       return this.manager = (GuildManager)new GuildManagerImpl(this); 
/*  815 */     return this.manager;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AuditLogPaginationAction retrieveAuditLogs() {
/*  822 */     return (AuditLogPaginationAction)new AuditLogPaginationActionImpl(this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Void> leave() {
/*  829 */     if (getSelfMember().isOwner()) {
/*  830 */       throw new IllegalStateException("Cannot leave a guild that you are the owner of! Transfer guild ownership first!");
/*      */     }
/*  832 */     Route.CompiledRoute route = Route.Self.LEAVE_GUILD.compile(new String[] { getId() });
/*  833 */     return (RestAction<Void>)new RestActionImpl((JDA)getJDA(), route);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Void> delete() {
/*  840 */     if (!getJDA().getSelfUser().isBot() && getJDA().getSelfUser().isMfaEnabled()) {
/*  841 */       throw new IllegalStateException("Cannot delete a guild without providing MFA code. Use Guild#delete(String)");
/*      */     }
/*  843 */     return delete((String)null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Void> delete(String mfaCode) {
/*  850 */     if (!getSelfMember().isOwner()) {
/*  851 */       throw new PermissionException("Cannot delete a guild that you do not own!");
/*      */     }
/*  853 */     DataObject mfaBody = null;
/*  854 */     if (!getJDA().getSelfUser().isBot() && getJDA().getSelfUser().isMfaEnabled()) {
/*      */       
/*  856 */       Checks.notEmpty(mfaCode, "Provided MultiFactor Auth code");
/*  857 */       mfaBody = DataObject.empty().put("code", mfaCode);
/*      */     } 
/*      */     
/*  860 */     Route.CompiledRoute route = Route.Guilds.DELETE_GUILD.compile(new String[] { getId() });
/*  861 */     return (RestAction<Void>)new RestActionImpl((JDA)getJDA(), route, mfaBody);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AudioManager getAudioManager() {
/*      */     AudioManagerImpl audioManagerImpl;
/*  868 */     if (!getJDA().isIntent(GatewayIntent.GUILD_VOICE_STATES))
/*  869 */       throw new IllegalStateException("Cannot use audio features with disabled GUILD_VOICE_STATES intent!"); 
/*  870 */     AbstractCacheView<AudioManager> managerMap = getJDA().getAudioManagersView();
/*  871 */     AudioManager mng = (AudioManager)managerMap.get(this.id);
/*  872 */     if (mng == null) {
/*      */ 
/*      */       
/*  875 */       UnlockHook hook = managerMap.writeLock();
/*      */       
/*  877 */       try { GuildImpl cachedGuild = (GuildImpl)getJDA().getGuildById(this.id);
/*  878 */         if (cachedGuild == null)
/*  879 */           throw new IllegalStateException("Cannot get an AudioManager instance on an uncached Guild"); 
/*  880 */         mng = (AudioManager)managerMap.get(this.id);
/*  881 */         if (mng == null) {
/*      */           
/*  883 */           audioManagerImpl = new AudioManagerImpl(cachedGuild);
/*  884 */           managerMap.getMap().put(this.id, audioManagerImpl);
/*      */         } 
/*  886 */         if (hook != null) hook.close();  } catch (Throwable throwable) { if (hook != null)
/*      */           try { hook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; } 
/*  888 */     }  return (AudioManager)audioManagerImpl;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public synchronized Task<Void> requestToSpeak() {
/*  895 */     if (!isRequestToSpeakPending()) {
/*  896 */       this.pendingRequestToSpeak = new CompletableFuture<>();
/*      */     }
/*  898 */     GatewayTask gatewayTask = new GatewayTask(this.pendingRequestToSpeak, this::cancelRequestToSpeak);
/*  899 */     updateRequestToSpeak();
/*  900 */     return (Task<Void>)gatewayTask;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public synchronized Task<Void> cancelRequestToSpeak() {
/*  907 */     if (isRequestToSpeakPending()) {
/*      */       
/*  909 */       this.pendingRequestToSpeak.cancel(false);
/*  910 */       this.pendingRequestToSpeak = null;
/*      */     } 
/*      */     
/*  913 */     VoiceChannel channel = getSelfMember().getVoiceState().getChannel();
/*  914 */     StageInstance instance = (channel instanceof StageChannel) ? ((StageChannel)channel).getStageInstance() : null;
/*  915 */     if (instance == null)
/*  916 */       return (Task<Void>)new GatewayTask(CompletableFuture.completedFuture(null), () -> { 
/*  917 */           });  CompletableFuture<Void> future = instance.cancelRequestToSpeak().submit();
/*  918 */     return (Task<Void>)new GatewayTask(future, () -> future.cancel(false));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDAImpl getJDA() {
/*  925 */     return this.api;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public List<GuildVoiceState> getVoiceStates() {
/*  932 */     return Collections.unmodifiableList((List<? extends GuildVoiceState>)
/*  933 */         getMembersView().stream()
/*  934 */         .map(Member::getVoiceState)
/*  935 */         .filter(Objects::nonNull)
/*  936 */         .collect(Collectors.toList()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Guild.VerificationLevel getVerificationLevel() {
/*  943 */     return this.verificationLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Guild.NotificationLevel getDefaultNotificationLevel() {
/*  950 */     return this.defaultNotificationLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Guild.MFALevel getRequiredMFALevel() {
/*  957 */     return this.mfaLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Guild.ExplicitContentLevel getExplicitContentLevel() {
/*  964 */     return this.explicitContentLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean checkVerification() {
/*  971 */     if (getJDA().getAccountType() == AccountType.BOT)
/*  972 */       return true; 
/*  973 */     if (this.canSendVerification) {
/*  974 */       return true;
/*      */     }
/*  976 */     switch (this.verificationLevel) {
/*      */ 
/*      */ 
/*      */       
/*      */       case HIGH:
/*  981 */         if (ChronoUnit.MINUTES.between(getSelfMember().getTimeJoined(), OffsetDateTime.now()) < 10L)
/*      */           break; 
/*      */       case MEDIUM:
/*  984 */         if (ChronoUnit.MINUTES.between(getJDA().getSelfUser().getTimeCreated(), OffsetDateTime.now()) < 5L)
/*      */           break; 
/*      */       case LOW:
/*  987 */         if (!getJDA().getSelfUser().isVerified())
/*      */           break; 
/*      */       case NONE:
/*  990 */         this.canSendVerification = true;
/*  991 */         return true;
/*      */       case UNKNOWN:
/*  993 */         return true;
/*      */     } 
/*  995 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean isAvailable() {
/* 1002 */     return this.available;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @Deprecated
/*      */   public CompletableFuture<Void> retrieveMembers() {
/* 1010 */     if (!getJDA().isIntent(GatewayIntent.GUILD_MEMBERS)) {
/*      */       
/* 1012 */       CompletableFuture<Void> completableFuture = new CompletableFuture<>();
/* 1013 */       completableFuture.completeExceptionally(new IllegalStateException("Unable to start member chunking on a guild with disabled GUILD_MEMBERS intent!"));
/* 1014 */       return completableFuture;
/*      */     } 
/*      */     
/* 1017 */     if (isLoaded())
/* 1018 */       return CompletableFuture.completedFuture(null); 
/* 1019 */     Task<List<Member>> task = loadMembers();
/* 1020 */     CompletableFuture<Void> future = new CompletableFuture<>();
/* 1021 */     Objects.requireNonNull(future); task.onError(future::completeExceptionally);
/* 1022 */     task.onSuccess(members -> { UnlockHook hook = this.memberCache.writeLock(); try { members.forEach(()); if (hook != null)
/* 1023 */               hook.close();  } catch (Throwable throwable) { if (hook != null) try { hook.close(); } catch (Throwable throwable1)
/*      */               { throwable.addSuppressed(throwable1); }
/*      */                
/*      */             throw throwable; }
/*      */           
/*      */           future.complete(null);
/*      */         });
/* 1030 */     return future;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Task<Void> loadMembers(@Nonnull Consumer<Member> callback) {
/* 1037 */     Checks.notNull(callback, "Callback");
/* 1038 */     if (!getJDA().isIntent(GatewayIntent.GUILD_MEMBERS))
/* 1039 */       throw new IllegalStateException("Cannot use loadMembers without GatewayIntent.GUILD_MEMBERS!"); 
/* 1040 */     if (isLoaded()) {
/*      */       
/* 1042 */       this.memberCache.forEachUnordered(callback);
/* 1043 */       return (Task<Void>)new GatewayTask(CompletableFuture.completedFuture(null), () -> {
/*      */           
/*      */           });
/* 1046 */     }  MemberChunkManager chunkManager = getJDA().getClient().getChunkManager();
/* 1047 */     boolean includePresences = getJDA().isIntent(GatewayIntent.GUILD_PRESENCES);
/* 1048 */     CompletableFuture<Void> handler = chunkManager.chunkGuild(this, includePresences, (last, list) -> list.forEach(callback));
/* 1049 */     handler.exceptionally(ex -> {
/*      */           WebSocketClient.LOG.error("Encountered exception trying to handle member chunk response", ex);
/*      */           return null;
/*      */         });
/* 1053 */     return (Task<Void>)new GatewayTask(handler, () -> handler.cancel(false));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Member getMember(long id, boolean update, JDAImpl jda) {
/* 1059 */     if (!update || jda.isIntent(GatewayIntent.GUILD_MEMBERS)) {
/*      */ 
/*      */       
/* 1062 */       Member member = getMemberById(id);
/*      */       
/* 1064 */       if (!update || (member != null && member.hasTimeJoined()))
/* 1065 */         return member; 
/*      */     } 
/* 1067 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Member> retrieveMemberById(long id, boolean update) {
/* 1074 */     JDAImpl jda = getJDA();
/* 1075 */     if (id == jda.getSelfUser().getIdLong()) {
/* 1076 */       return (RestAction<Member>)new CompletedRestAction((JDA)jda, getSelfMember());
/*      */     }
/* 1078 */     return (RestAction<Member>)new DeferredRestAction((JDA)jda, Member.class, () -> getMember(id, update, jda), () -> {
/*      */           Route.CompiledRoute route = Route.Guilds.GET_MEMBER.compile(new String[] { getId(), Long.toUnsignedString(id) });
/*      */           return new RestActionImpl((JDA)jda, route, ());
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Task<List<Member>> retrieveMembersByIds(boolean includePresence, @Nonnull long... ids) {
/* 1094 */     Checks.notNull(ids, "ID Array");
/* 1095 */     Checks.check((!includePresence || this.api.isIntent(GatewayIntent.GUILD_PRESENCES)), "Cannot retrieve presences of members without GUILD_PRESENCES intent!");
/*      */ 
/*      */     
/* 1098 */     if (ids.length == 0)
/* 1099 */       return (Task<List<Member>>)new GatewayTask(CompletableFuture.completedFuture(Collections.emptyList()), () -> { 
/* 1100 */           });  Checks.check((ids.length <= 100), "You can only request 100 members at once");
/* 1101 */     MemberChunkManager chunkManager = this.api.getClient().getChunkManager();
/* 1102 */     List<Member> collect = new ArrayList<>(ids.length);
/* 1103 */     CompletableFuture<List<Member>> result = new CompletableFuture<>();
/* 1104 */     CompletableFuture<Void> handle = chunkManager.chunkGuild(this, includePresence, ids, (last, list) -> {
/*      */           collect.addAll(list);
/*      */           if (last.booleanValue()) {
/*      */             result.complete(collect);
/*      */           }
/*      */         });
/* 1110 */     handle.exceptionally(ex -> {
/*      */           WebSocketClient.LOG.error("Encountered exception trying to handle member chunk response", ex);
/*      */           
/*      */           result.completeExceptionally(ex);
/*      */           return null;
/*      */         });
/* 1116 */     return (Task<List<Member>>)new GatewayTask(result, () -> handle.cancel(false));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   public Task<List<Member>> retrieveMembersByPrefix(@Nonnull String prefix, int limit) {
/* 1124 */     Checks.notEmpty(prefix, "Prefix");
/* 1125 */     Checks.positive(limit, "Limit");
/* 1126 */     Checks.check((limit <= 100), "Limit must not be greater than 100");
/* 1127 */     MemberChunkManager chunkManager = this.api.getClient().getChunkManager();
/*      */     
/* 1129 */     List<Member> collect = new ArrayList<>(limit);
/* 1130 */     CompletableFuture<List<Member>> result = new CompletableFuture<>();
/* 1131 */     CompletableFuture<Void> handle = chunkManager.chunkGuild(this, prefix, limit, (last, list) -> {
/*      */           collect.addAll(list);
/*      */           if (last.booleanValue()) {
/*      */             result.complete(collect);
/*      */           }
/*      */         });
/* 1137 */     handle.exceptionally(ex -> {
/*      */           WebSocketClient.LOG.error("Encountered exception trying to handle member chunk response", ex);
/*      */           
/*      */           result.completeExceptionally(ex);
/*      */           return null;
/*      */         });
/* 1143 */     return (Task<List<Member>>)new GatewayTask(result, () -> handle.cancel(false));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getIdLong() {
/* 1149 */     return this.id;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<List<Invite>> retrieveInvites() {
/* 1156 */     if (!getSelfMember().hasPermission(new Permission[] { Permission.MANAGE_SERVER })) {
/* 1157 */       throw new InsufficientPermissionException(this, Permission.MANAGE_SERVER);
/*      */     }
/* 1159 */     Route.CompiledRoute route = Route.Invites.GET_GUILD_INVITES.compile(new String[] { getId() });
/* 1160 */     return (RestAction<List<Invite>>)new RestActionImpl((JDA)getJDA(), route, (response, request) -> {
/*      */           EntityBuilder entityBuilder = this.api.getEntityBuilder();
/*      */           DataArray array = response.getArray();
/*      */           List<Invite> invites = new ArrayList<>(array.length());
/*      */           for (int i = 0; i < array.length(); i++) {
/*      */             invites.add(entityBuilder.createInvite(array.getObject(i)));
/*      */           }
/*      */           return Collections.unmodifiableList(invites);
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<List<Template>> retrieveTemplates() {
/* 1175 */     if (!getSelfMember().hasPermission(new Permission[] { Permission.MANAGE_SERVER })) {
/* 1176 */       throw new InsufficientPermissionException(this, Permission.MANAGE_SERVER);
/*      */     }
/* 1178 */     Route.CompiledRoute route = Route.Templates.GET_GUILD_TEMPLATES.compile(new String[] { getId() });
/* 1179 */     return (RestAction<List<Template>>)new RestActionImpl((JDA)getJDA(), route, (response, request) -> {
/*      */           EntityBuilder entityBuilder = this.api.getEntityBuilder();
/*      */ 
/*      */           
/*      */           DataArray array = response.getArray();
/*      */           
/*      */           List<Template> templates = new ArrayList<>(array.length());
/*      */           
/*      */           for (int i = 0; i < array.length(); i++) {
/*      */             try {
/*      */               templates.add(entityBuilder.createTemplate(array.getObject(i)));
/* 1190 */             } catch (Exception e) {
/*      */               JDAImpl.LOG.error("Error creating template from json", e);
/*      */             } 
/*      */           } 
/*      */           return Collections.unmodifiableList(templates);
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Template> createTemplate(@Nonnull String name, @Nullable String description) {
/* 1203 */     checkPermission(Permission.MANAGE_SERVER);
/* 1204 */     Checks.notBlank(name, "Name");
/* 1205 */     name = name.trim();
/*      */     
/* 1207 */     Checks.notLonger(name, 100, "Name");
/* 1208 */     if (description != null) {
/* 1209 */       Checks.notLonger(description, 120, "Description");
/*      */     }
/* 1211 */     Route.CompiledRoute route = Route.Templates.CREATE_TEMPLATE.compile(new String[] { getId() });
/*      */     
/* 1213 */     DataObject object = DataObject.empty();
/* 1214 */     object.put("name", name);
/* 1215 */     object.put("description", description);
/*      */     
/* 1217 */     return (RestAction<Template>)new RestActionImpl((JDA)getJDA(), route, object, (response, request) -> {
/*      */           EntityBuilder entityBuilder = this.api.getEntityBuilder();
/*      */           return entityBuilder.createTemplate(response.getObject());
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Void> moveVoiceMember(@Nonnull Member member, @Nullable VoiceChannel voiceChannel) {
/* 1228 */     Checks.notNull(member, "Member");
/* 1229 */     checkGuild(member.getGuild(), "Member");
/* 1230 */     if (voiceChannel != null) {
/* 1231 */       checkGuild(voiceChannel.getGuild(), "VoiceChannel");
/*      */     }
/* 1233 */     GuildVoiceState vState = member.getVoiceState();
/* 1234 */     if (vState == null)
/* 1235 */       throw new IllegalStateException("Cannot move a Member with disabled CacheFlag.VOICE_STATE"); 
/* 1236 */     VoiceChannel channel = vState.getChannel();
/* 1237 */     if (channel == null) {
/* 1238 */       throw new IllegalStateException("You cannot move a Member who isn't in a VoiceChannel!");
/*      */     }
/* 1240 */     if (!PermissionUtil.checkPermission((GuildChannel)channel, getSelfMember(), new Permission[] { Permission.VOICE_MOVE_OTHERS })) {
/* 1241 */       throw new InsufficientPermissionException(channel, Permission.VOICE_MOVE_OTHERS, "This account does not have Permission to MOVE_OTHERS out of the channel that the Member is currently in.");
/*      */     }
/* 1243 */     if (voiceChannel != null && 
/* 1244 */       !PermissionUtil.checkPermission((GuildChannel)voiceChannel, getSelfMember(), new Permission[] { Permission.VOICE_CONNECT
/* 1245 */         }) && !PermissionUtil.checkPermission((GuildChannel)voiceChannel, member, new Permission[] { Permission.VOICE_CONNECT })) {
/* 1246 */       throw new InsufficientPermissionException(voiceChannel, Permission.VOICE_CONNECT, "Neither this account nor the Member that is attempting to be moved have the VOICE_CONNECT permission for the destination VoiceChannel, so the move cannot be done.");
/*      */     }
/*      */ 
/*      */     
/* 1250 */     DataObject body = DataObject.empty().put("channel_id", (voiceChannel == null) ? null : voiceChannel.getId());
/* 1251 */     Route.CompiledRoute route = Route.Guilds.MODIFY_MEMBER.compile(new String[] { getId(), member.getUser().getId() });
/* 1252 */     return (RestAction<Void>)new RestActionImpl((JDA)getJDA(), route, body);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AuditableRestAction<Void> modifyNickname(@Nonnull Member member, String nickname) {
/* 1259 */     Checks.notNull(member, "Member");
/* 1260 */     checkGuild(member.getGuild(), "Member");
/*      */     
/* 1262 */     if (member.equals(getSelfMember())) {
/*      */       
/* 1264 */       if (!member.hasPermission(new Permission[] { Permission.NICKNAME_CHANGE }) && !member.hasPermission(new Permission[] { Permission.NICKNAME_MANAGE })) {
/* 1265 */         throw new InsufficientPermissionException(this, Permission.NICKNAME_CHANGE, "You neither have NICKNAME_CHANGE nor NICKNAME_MANAGE permission!");
/*      */       }
/*      */     } else {
/*      */       
/* 1269 */       checkPermission(Permission.NICKNAME_MANAGE);
/* 1270 */       checkPosition(member);
/*      */     } 
/*      */     
/* 1273 */     JDAImpl jda = getJDA();
/* 1274 */     return (new DeferredRestAction((JDA)jda, () -> {
/*      */           Route.CompiledRoute route;
/*      */           
/*      */           DataObject body = DataObject.empty().put("nick", (nickname == null) ? "" : nickname);
/*      */           if (member.equals(getSelfMember())) {
/*      */             route = Route.Guilds.MODIFY_SELF.compile(new String[] { getId() });
/*      */           } else {
/*      */             route = Route.Guilds.MODIFY_MEMBER.compile(new String[] { getId(), member.getUser().getId() });
/*      */           } 
/*      */           return new AuditableRestActionImpl((JDA)jda, route, body);
/* 1284 */         })).setCacheCheck(() -> !Objects.equals(nickname, member.getNickname()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AuditableRestAction<Integer> prune(int days, boolean wait, @Nonnull Role... roles) {
/* 1291 */     checkPermission(Permission.KICK_MEMBERS);
/*      */     
/* 1293 */     Checks.check((days >= 1 && days <= 30), "Provided %d days must be between 1 and 30.", Integer.valueOf(days));
/* 1294 */     Checks.notNull(roles, "Roles");
/*      */     
/* 1296 */     Route.CompiledRoute route = Route.Guilds.PRUNE_MEMBERS.compile(new String[] { getId() });
/* 1297 */     DataObject body = DataObject.empty();
/* 1298 */     body.put("days", Integer.valueOf(days));
/* 1299 */     if (!wait)
/* 1300 */       body.put("compute_prune_count", Boolean.valueOf(false)); 
/* 1301 */     if (roles.length != 0) {
/*      */       
/* 1303 */       for (Role role : roles) {
/*      */         
/* 1305 */         Checks.notNull(role, "Role");
/* 1306 */         Checks.check(role.getGuild().equals(this), "Role is not from the same guild!");
/*      */       } 
/* 1308 */       body.put("include_roles", Arrays.<Role>stream(roles).map(ISnowflake::getId).collect(Collectors.toList()));
/*      */     } 
/* 1310 */     return (AuditableRestAction<Integer>)new AuditableRestActionImpl((JDA)getJDA(), route, body, (response, request) -> Integer.valueOf(response.getObject().getInt("pruned", 0)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AuditableRestAction<Void> kick(@Nonnull Member member, String reason) {
/* 1317 */     Checks.notNull(member, "member");
/* 1318 */     checkGuild(member.getGuild(), "member");
/* 1319 */     checkPermission(Permission.KICK_MEMBERS);
/* 1320 */     checkPosition(member);
/* 1321 */     return kick0(member.getUser().getId(), reason);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AuditableRestAction<Void> kick(@Nonnull String userId, @Nullable String reason) {
/* 1328 */     Member member = getMemberById(userId);
/* 1329 */     if (member != null) {
/* 1330 */       return kick(member, reason);
/*      */     }
/* 1332 */     Checks.check(!userId.equals(getOwnerId()), "Cannot kick the owner of a guild!");
/* 1333 */     checkPermission(Permission.KICK_MEMBERS);
/* 1334 */     return kick0(userId, reason);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   private AuditableRestAction<Void> kick0(@Nonnull String userId, @Nullable String reason) {
/* 1340 */     Route.CompiledRoute route = Route.Guilds.KICK_MEMBER.compile(new String[] { getId(), userId });
/* 1341 */     if (!Helpers.isBlank(reason)) {
/*      */       
/* 1343 */       Checks.check((reason.length() <= 512), "Reason cannot be longer than 512 characters.");
/* 1344 */       route = route.withQueryParams(new String[] { "reason", EncodingUtil.encodeUTF8(reason) });
/*      */     } 
/* 1346 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl((JDA)getJDA(), route);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AuditableRestAction<Void> ban(@Nonnull User user, int delDays, String reason) {
/* 1353 */     Checks.notNull(user, "User");
/* 1354 */     checkPermission(Permission.BAN_MEMBERS);
/*      */     
/* 1356 */     if (isMember(user)) {
/* 1357 */       checkPosition(getMember(user));
/*      */     }
/* 1359 */     return ban0(user.getId(), delDays, reason);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AuditableRestAction<Void> ban(@Nonnull String userId, int delDays, String reason) {
/* 1366 */     Checks.notNull(userId, "User");
/* 1367 */     checkPermission(Permission.BAN_MEMBERS);
/*      */     
/* 1369 */     User user = getJDA().getUserById(userId);
/* 1370 */     if (user != null) {
/* 1371 */       return ban(user, delDays, reason);
/*      */     }
/* 1373 */     return ban0(userId, delDays, reason);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   private AuditableRestAction<Void> ban0(@Nonnull String userId, int delDays, String reason) {
/* 1379 */     Checks.notNegative(delDays, "Deletion Days");
/* 1380 */     Checks.check((delDays <= 7), "Deletion Days must not be bigger than 7.");
/*      */     
/* 1382 */     Route.CompiledRoute route = Route.Guilds.BAN.compile(new String[] { getId(), userId });
/* 1383 */     DataObject params = DataObject.empty();
/* 1384 */     if (!Helpers.isBlank(reason)) {
/*      */       
/* 1386 */       Checks.check((reason.length() <= 512), "Reason cannot be longer than 512 characters.");
/* 1387 */       params.put("reason", reason);
/*      */     } 
/* 1389 */     if (delDays > 0) {
/* 1390 */       params.put("delete_message_days", Integer.valueOf(delDays));
/*      */     }
/* 1392 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl((JDA)getJDA(), route, params);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AuditableRestAction<Void> unban(@Nonnull String userId) {
/* 1399 */     Checks.isSnowflake(userId, "User ID");
/* 1400 */     checkPermission(Permission.BAN_MEMBERS);
/*      */     
/* 1402 */     Route.CompiledRoute route = Route.Guilds.UNBAN.compile(new String[] { getId(), userId });
/* 1403 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl((JDA)getJDA(), route);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AuditableRestAction<Void> deafen(@Nonnull Member member, boolean deafen) {
/* 1410 */     Checks.notNull(member, "Member");
/* 1411 */     checkGuild(member.getGuild(), "Member");
/* 1412 */     checkPermission(Permission.VOICE_DEAF_OTHERS);
/*      */     
/* 1414 */     GuildVoiceState voiceState = member.getVoiceState();
/* 1415 */     if (voiceState != null) {
/*      */       
/* 1417 */       if (voiceState.getChannel() == null)
/* 1418 */         throw new IllegalStateException("Can only deafen members who are currently in a voice channel"); 
/* 1419 */       if (voiceState.isGuildDeafened() == deafen) {
/* 1420 */         return (AuditableRestAction<Void>)new CompletedRestAction((JDA)getJDA(), null);
/*      */       }
/*      */     } 
/* 1423 */     DataObject body = DataObject.empty().put("deaf", Boolean.valueOf(deafen));
/* 1424 */     Route.CompiledRoute route = Route.Guilds.MODIFY_MEMBER.compile(new String[] { getId(), member.getUser().getId() });
/* 1425 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl((JDA)getJDA(), route, body);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AuditableRestAction<Void> mute(@Nonnull Member member, boolean mute) {
/* 1432 */     Checks.notNull(member, "Member");
/* 1433 */     checkGuild(member.getGuild(), "Member");
/* 1434 */     checkPermission(Permission.VOICE_MUTE_OTHERS);
/*      */     
/* 1436 */     GuildVoiceState voiceState = member.getVoiceState();
/* 1437 */     if (voiceState != null) {
/*      */       
/* 1439 */       if (voiceState.getChannel() == null)
/* 1440 */         throw new IllegalStateException("Can only mute members who are currently in a voice channel"); 
/* 1441 */       if (voiceState.isGuildMuted() == mute && (mute || !voiceState.isSuppressed())) {
/* 1442 */         return (AuditableRestAction<Void>)new CompletedRestAction((JDA)getJDA(), null);
/*      */       }
/*      */     } 
/* 1445 */     DataObject body = DataObject.empty().put("mute", Boolean.valueOf(mute));
/* 1446 */     Route.CompiledRoute route = Route.Guilds.MODIFY_MEMBER.compile(new String[] { getId(), member.getUser().getId() });
/* 1447 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl((JDA)getJDA(), route, body);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AuditableRestAction<Void> addRoleToMember(@Nonnull Member member, @Nonnull Role role) {
/* 1454 */     Checks.notNull(member, "Member");
/* 1455 */     Checks.notNull(role, "Role");
/* 1456 */     checkGuild(member.getGuild(), "Member");
/* 1457 */     checkGuild(role.getGuild(), "Role");
/* 1458 */     checkPermission(Permission.MANAGE_ROLES);
/* 1459 */     checkPosition(role);
/*      */     
/* 1461 */     Route.CompiledRoute route = Route.Guilds.ADD_MEMBER_ROLE.compile(new String[] { getId(), member.getUser().getId(), role.getId() });
/* 1462 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl((JDA)getJDA(), route);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AuditableRestAction<Void> removeRoleFromMember(@Nonnull Member member, @Nonnull Role role) {
/* 1469 */     Checks.notNull(member, "Member");
/* 1470 */     Checks.notNull(role, "Role");
/* 1471 */     checkGuild(member.getGuild(), "Member");
/* 1472 */     checkGuild(role.getGuild(), "Role");
/* 1473 */     checkPermission(Permission.MANAGE_ROLES);
/* 1474 */     checkPosition(role);
/*      */     
/* 1476 */     Route.CompiledRoute route = Route.Guilds.REMOVE_MEMBER_ROLE.compile(new String[] { getId(), member.getUser().getId(), role.getId() });
/* 1477 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl((JDA)getJDA(), route);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AuditableRestAction<Void> modifyMemberRoles(@Nonnull Member member, Collection<Role> rolesToAdd, Collection<Role> rolesToRemove) {
/* 1484 */     Checks.notNull(member, "Member");
/* 1485 */     checkGuild(member.getGuild(), "Member");
/* 1486 */     checkPermission(Permission.MANAGE_ROLES);
/* 1487 */     Set<Role> currentRoles = new HashSet<>(((MemberImpl)member).getRoleSet());
/* 1488 */     if (rolesToAdd != null) {
/*      */       
/* 1490 */       checkRoles(rolesToAdd, "add", "to");
/* 1491 */       currentRoles.addAll(rolesToAdd);
/*      */     } 
/*      */     
/* 1494 */     if (rolesToRemove != null) {
/*      */       
/* 1496 */       checkRoles(rolesToRemove, "remove", "from");
/* 1497 */       currentRoles.removeAll(rolesToRemove);
/*      */     } 
/*      */     
/* 1500 */     return modifyMemberRoles(member, currentRoles);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AuditableRestAction<Void> modifyMemberRoles(@Nonnull Member member, @Nonnull Collection<Role> roles) {
/* 1507 */     Checks.notNull(member, "Member");
/* 1508 */     Checks.notNull(roles, "Roles");
/* 1509 */     checkGuild(member.getGuild(), "Member");
/* 1510 */     roles.forEach(role -> {
/*      */           Checks.notNull(role, "Role in collection");
/*      */           
/*      */           checkGuild(role.getGuild(), "Role: " + role.toString());
/*      */         });
/*      */     
/* 1516 */     Checks.check(!roles.contains(getPublicRole()), "Cannot add the PublicRole of a Guild to a Member. All members have this role by default!");
/*      */ 
/*      */ 
/*      */     
/* 1520 */     List<Role> memberRoles = member.getRoles();
/* 1521 */     if (Helpers.deepEqualsUnordered(roles, memberRoles)) {
/* 1522 */       return (AuditableRestAction<Void>)new CompletedRestAction((JDA)getJDA(), null);
/*      */     }
/*      */     
/* 1525 */     for (Role r : memberRoles) {
/*      */       
/* 1527 */       if (!roles.contains(r)) {
/*      */         
/* 1529 */         checkPosition(r);
/* 1530 */         Checks.check(!r.isManaged(), "Cannot remove managed role from member. Role: %s", r);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1535 */     for (Role r : roles) {
/*      */       
/* 1537 */       if (!memberRoles.contains(r)) {
/*      */         
/* 1539 */         checkPosition(r);
/* 1540 */         Checks.check(!r.isManaged(), "Cannot add managed role to member. Role: %s", r);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1545 */     DataObject body = DataObject.empty().put("roles", roles.stream().map(ISnowflake::getId).collect(Collectors.toSet()));
/* 1546 */     Route.CompiledRoute route = Route.Guilds.MODIFY_MEMBER.compile(new String[] { getId(), member.getUser().getId() });
/*      */     
/* 1548 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl((JDA)getJDA(), route, body);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AuditableRestAction<Void> transferOwnership(@Nonnull Member newOwner) {
/* 1555 */     Checks.notNull(newOwner, "Member");
/* 1556 */     checkGuild(newOwner.getGuild(), "Member");
/* 1557 */     if (!getSelfMember().isOwner()) {
/* 1558 */       throw new PermissionException("The logged in account must be the owner of this Guild to be able to transfer ownership");
/*      */     }
/* 1560 */     Checks.check(!getSelfMember().equals(newOwner), "The member provided as the newOwner is the currently logged in account. Provide a different member to give ownership to.");
/*      */ 
/*      */     
/* 1563 */     Checks.check(!newOwner.getUser().isBot(), "Cannot transfer ownership of a Guild to a Bot!");
/*      */     
/* 1565 */     DataObject body = DataObject.empty().put("owner_id", newOwner.getUser().getId());
/* 1566 */     Route.CompiledRoute route = Route.Guilds.MODIFY_GUILD.compile(new String[] { getId() });
/* 1567 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl((JDA)getJDA(), route, body);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public ChannelAction<TextChannel> createTextChannel(@Nonnull String name, Category parent) {
/* 1574 */     if (parent != null) {
/*      */       
/* 1576 */       Checks.check(parent.getGuild().equals(this), "Category is not from the same guild!");
/* 1577 */       if (!getSelfMember().hasPermission((GuildChannel)parent, new Permission[] { Permission.MANAGE_CHANNEL })) {
/* 1578 */         throw new InsufficientPermissionException(parent, Permission.MANAGE_CHANNEL);
/*      */       }
/*      */     } else {
/*      */       
/* 1582 */       checkPermission(Permission.MANAGE_CHANNEL);
/*      */     } 
/*      */     
/* 1585 */     Checks.notBlank(name, "Name");
/* 1586 */     name = name.trim();
/* 1587 */     Checks.notLonger(name, 100, "Name");
/* 1588 */     return (ChannelAction<TextChannel>)(new ChannelActionImpl(TextChannel.class, name, this, ChannelType.TEXT)).setParent(parent);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public ChannelAction<VoiceChannel> createVoiceChannel(@Nonnull String name, Category parent) {
/* 1595 */     if (parent != null) {
/*      */       
/* 1597 */       Checks.check(parent.getGuild().equals(this), "Category is not from the same guild!");
/* 1598 */       if (!getSelfMember().hasPermission((GuildChannel)parent, new Permission[] { Permission.MANAGE_CHANNEL })) {
/* 1599 */         throw new InsufficientPermissionException(parent, Permission.MANAGE_CHANNEL);
/*      */       }
/*      */     } else {
/*      */       
/* 1603 */       checkPermission(Permission.MANAGE_CHANNEL);
/*      */     } 
/*      */     
/* 1606 */     Checks.notBlank(name, "Name");
/* 1607 */     name = name.trim();
/* 1608 */     Checks.notLonger(name, 100, "Name");
/* 1609 */     return (ChannelAction<VoiceChannel>)(new ChannelActionImpl(VoiceChannel.class, name, this, ChannelType.VOICE)).setParent(parent);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public ChannelAction<StageChannel> createStageChannel(@Nonnull String name, Category parent) {
/* 1616 */     if (parent != null) {
/*      */       
/* 1618 */       Checks.check(parent.getGuild().equals(this), "Category is not from the same guild!");
/* 1619 */       if (!getSelfMember().hasPermission((GuildChannel)parent, new Permission[] { Permission.MANAGE_CHANNEL })) {
/* 1620 */         throw new InsufficientPermissionException(parent, Permission.MANAGE_CHANNEL);
/*      */       }
/*      */     } else {
/*      */       
/* 1624 */       checkPermission(Permission.MANAGE_CHANNEL);
/*      */     } 
/*      */     
/* 1627 */     Checks.notBlank(name, "Name");
/* 1628 */     name = name.trim();
/* 1629 */     Checks.notLonger(name, 100, "Name");
/* 1630 */     return (ChannelAction<StageChannel>)(new ChannelActionImpl(StageChannel.class, name, this, ChannelType.STAGE)).setParent(parent);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public ChannelAction<Category> createCategory(@Nonnull String name) {
/* 1637 */     checkPermission(Permission.MANAGE_CHANNEL);
/* 1638 */     Checks.notBlank(name, "Name");
/* 1639 */     name = name.trim();
/* 1640 */     Checks.notEmpty(name, "Name");
/* 1641 */     Checks.notLonger(name, 100, "Name");
/* 1642 */     return (ChannelAction<Category>)new ChannelActionImpl(Category.class, name, this, ChannelType.CATEGORY);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RoleAction createRole() {
/* 1649 */     checkPermission(Permission.MANAGE_ROLES);
/* 1650 */     return (RoleAction)new RoleActionImpl(this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AuditableRestAction<Emote> createEmote(@Nonnull String name, @Nonnull Icon icon, @Nonnull Role... roles) {
/* 1657 */     checkPermission(Permission.MANAGE_EMOTES);
/* 1658 */     Checks.inRange(name, 2, 32, "Emote name");
/* 1659 */     Checks.notNull(icon, "Emote icon");
/* 1660 */     Checks.notNull(roles, "Roles");
/*      */     
/* 1662 */     DataObject body = DataObject.empty();
/* 1663 */     body.put("name", name);
/* 1664 */     body.put("image", icon.getEncoding());
/* 1665 */     if (roles.length > 0) {
/* 1666 */       body.put("roles", Stream.<Role>of(roles).filter(Objects::nonNull).map(ISnowflake::getId).collect(Collectors.toSet()));
/*      */     }
/* 1668 */     JDAImpl jda = getJDA();
/* 1669 */     Route.CompiledRoute route = Route.Emotes.CREATE_EMOTE.compile(new String[] { getId() });
/* 1670 */     return (AuditableRestAction<Emote>)new AuditableRestActionImpl((JDA)jda, route, body, (response, request) -> {
/*      */           DataObject obj = response.getObject();
/*      */           return (Emote)jda.getEntityBuilder().createEmote(this, obj);
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public ChannelOrderAction modifyCategoryPositions() {
/* 1681 */     return (ChannelOrderAction)new ChannelOrderActionImpl(this, ChannelType.CATEGORY.getSortBucket());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public ChannelOrderAction modifyTextChannelPositions() {
/* 1688 */     return (ChannelOrderAction)new ChannelOrderActionImpl(this, ChannelType.TEXT.getSortBucket());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public ChannelOrderAction modifyVoiceChannelPositions() {
/* 1695 */     return (ChannelOrderAction)new ChannelOrderActionImpl(this, ChannelType.VOICE.getSortBucket());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public CategoryOrderAction modifyTextChannelPositions(@Nonnull Category category) {
/* 1702 */     Checks.notNull(category, "Category");
/* 1703 */     checkGuild(category.getGuild(), "Category");
/* 1704 */     return (CategoryOrderAction)new CategoryOrderActionImpl(category, ChannelType.TEXT.getSortBucket());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public CategoryOrderAction modifyVoiceChannelPositions(@Nonnull Category category) {
/* 1711 */     Checks.notNull(category, "Category");
/* 1712 */     checkGuild(category.getGuild(), "Category");
/* 1713 */     return (CategoryOrderAction)new CategoryOrderActionImpl(category, ChannelType.VOICE.getSortBucket());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RoleOrderAction modifyRolePositions(boolean useAscendingOrder) {
/* 1720 */     return (RoleOrderAction)new RoleOrderActionImpl(this, useAscendingOrder);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void checkGuild(Guild providedGuild, String comment) {
/* 1725 */     if (!equals(providedGuild)) {
/* 1726 */       throw new IllegalArgumentException("Provided " + comment + " is not part of this Guild!");
/*      */     }
/*      */   }
/*      */   
/*      */   protected void checkPermission(Permission perm) {
/* 1731 */     if (!getSelfMember().hasPermission(new Permission[] { perm })) {
/* 1732 */       throw new InsufficientPermissionException(this, perm);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void checkPosition(Member member) {
/* 1737 */     if (!getSelfMember().canInteract(member)) {
/* 1738 */       throw new HierarchyException("Can't modify a member with higher or equal highest role than yourself!");
/*      */     }
/*      */   }
/*      */   
/*      */   protected void checkPosition(Role role) {
/* 1743 */     if (!getSelfMember().canInteract(role)) {
/* 1744 */       throw new HierarchyException("Can't modify a role with higher or equal highest role than yourself! Role: " + role.toString());
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkRoles(Collection<Role> roles, String type, String preposition) {
/* 1749 */     roles.forEach(role -> {
/*      */           Checks.notNull(role, "Role in roles to " + type);
/*      */           checkGuild(role.getGuild(), "Role: " + role.toString());
/*      */           checkPosition(role);
/*      */           Checks.check(!role.isManaged(), "Cannot %s a managed role %s a Member. Role: %s", new Object[] { type, preposition, role.toString() });
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private synchronized boolean isRequestToSpeakPending() {
/* 1760 */     return (this.pendingRequestToSpeak != null && !this.pendingRequestToSpeak.isDone());
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateRequestToSpeak() {
/* 1765 */     if (!isRequestToSpeakPending())
/*      */       return; 
/* 1767 */     VoiceChannel connectedChannel = getSelfMember().getVoiceState().getChannel();
/* 1768 */     if (!(connectedChannel instanceof StageChannel))
/*      */       return; 
/* 1770 */     StageChannel stage = (StageChannel)connectedChannel;
/* 1771 */     StageInstance instance = stage.getStageInstance();
/* 1772 */     if (instance == null) {
/*      */       return;
/*      */     }
/* 1775 */     CompletableFuture<Void> future = this.pendingRequestToSpeak;
/* 1776 */     this.pendingRequestToSpeak = null;
/*      */     
/* 1778 */     Objects.requireNonNull(future); instance.requestToSpeak().queue(v -> future.complete(null), future::completeExceptionally);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GuildImpl setAvailable(boolean available) {
/* 1785 */     this.available = available;
/* 1786 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public GuildImpl setOwner(Member owner) {
/* 1792 */     if (owner != null && getMemberById(owner.getIdLong()) != null)
/* 1793 */       this.owner = owner; 
/* 1794 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setName(String name) {
/* 1799 */     this.name = name;
/* 1800 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setIconId(String iconId) {
/* 1805 */     this.iconId = iconId;
/* 1806 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setFeatures(Set<String> features) {
/* 1811 */     this.features = Collections.unmodifiableSet(features);
/* 1812 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setSplashId(String splashId) {
/* 1817 */     this.splashId = splashId;
/* 1818 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setRegion(String region) {
/* 1823 */     this.region = region;
/* 1824 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setVanityCode(String code) {
/* 1829 */     this.vanityCode = code;
/* 1830 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setDescription(String description) {
/* 1835 */     this.description = description;
/* 1836 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setBannerId(String bannerId) {
/* 1841 */     this.banner = bannerId;
/* 1842 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setMaxPresences(int maxPresences) {
/* 1847 */     this.maxPresences = maxPresences;
/* 1848 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setMaxMembers(int maxMembers) {
/* 1853 */     this.maxMembers = maxMembers;
/* 1854 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setAfkChannel(VoiceChannel afkChannel) {
/* 1859 */     this.afkChannel = afkChannel;
/* 1860 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setSystemChannel(TextChannel systemChannel) {
/* 1865 */     this.systemChannel = systemChannel;
/* 1866 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setRulesChannel(TextChannel rulesChannel) {
/* 1871 */     this.rulesChannel = rulesChannel;
/* 1872 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setCommunityUpdatesChannel(TextChannel communityUpdatesChannel) {
/* 1877 */     this.communityUpdatesChannel = communityUpdatesChannel;
/* 1878 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setPublicRole(Role publicRole) {
/* 1883 */     this.publicRole = publicRole;
/* 1884 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setVerificationLevel(Guild.VerificationLevel level) {
/* 1889 */     this.verificationLevel = level;
/* 1890 */     this.canSendVerification = false;
/* 1891 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setDefaultNotificationLevel(Guild.NotificationLevel level) {
/* 1896 */     this.defaultNotificationLevel = level;
/* 1897 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setRequiredMFALevel(Guild.MFALevel level) {
/* 1902 */     this.mfaLevel = level;
/* 1903 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setExplicitContentLevel(Guild.ExplicitContentLevel level) {
/* 1908 */     this.explicitContentLevel = level;
/* 1909 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setAfkTimeout(Guild.Timeout afkTimeout) {
/* 1914 */     this.afkTimeout = afkTimeout;
/* 1915 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setLocale(String locale) {
/* 1920 */     this.preferredLocale = Locale.forLanguageTag(locale);
/* 1921 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setBoostTier(int tier) {
/* 1926 */     this.boostTier = Guild.BoostTier.fromKey(tier);
/* 1927 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setBoostCount(int count) {
/* 1932 */     this.boostCount = count;
/* 1933 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setOwnerId(long ownerId) {
/* 1938 */     this.ownerId = ownerId;
/* 1939 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setMemberCount(int count) {
/* 1944 */     this.memberCount = count;
/* 1945 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildImpl setNSFWLevel(Guild.NSFWLevel nsfwLevel) {
/* 1950 */     this.nsfwLevel = nsfwLevel;
/* 1951 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SortedSnowflakeCacheViewImpl<Category> getCategoriesView() {
/* 1958 */     return this.categoryCache;
/*      */   }
/*      */ 
/*      */   
/*      */   public SortedSnowflakeCacheViewImpl<StoreChannel> getStoreChannelView() {
/* 1963 */     return this.storeChannelCache;
/*      */   }
/*      */ 
/*      */   
/*      */   public SortedSnowflakeCacheViewImpl<TextChannel> getTextChannelsView() {
/* 1968 */     return this.textChannelCache;
/*      */   }
/*      */ 
/*      */   
/*      */   public SortedSnowflakeCacheViewImpl<VoiceChannel> getVoiceChannelsView() {
/* 1973 */     return this.voiceChannelCache;
/*      */   }
/*      */ 
/*      */   
/*      */   public SortedSnowflakeCacheViewImpl<Role> getRolesView() {
/* 1978 */     return this.roleCache;
/*      */   }
/*      */ 
/*      */   
/*      */   public SnowflakeCacheViewImpl<Emote> getEmotesView() {
/* 1983 */     return this.emoteCache;
/*      */   }
/*      */ 
/*      */   
/*      */   public MemberCacheViewImpl getMembersView() {
/* 1988 */     return this.memberCache;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Guild.NSFWLevel getNSFWLevel() {
/* 1995 */     return this.nsfwLevel;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public CacheView.SimpleCacheView<MemberPresenceImpl> getPresenceView() {
/* 2001 */     return this.memberPresences;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onMemberAdd() {
/* 2008 */     this.memberCount++;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onMemberRemove() {
/* 2013 */     this.memberCount--;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object o) {
/* 2021 */     if (o == this)
/* 2022 */       return true; 
/* 2023 */     if (!(o instanceof GuildImpl))
/* 2024 */       return false; 
/* 2025 */     GuildImpl oGuild = (GuildImpl)o;
/* 2026 */     return (this.id == oGuild.id);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 2032 */     return Long.hashCode(this.id);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 2038 */     return "G:" + getName() + '(' + this.id + ')';
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\GuildImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */