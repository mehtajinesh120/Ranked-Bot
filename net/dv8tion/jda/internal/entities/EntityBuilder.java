/*      */ package net.dv8tion.jda.internal.entities;
/*      */ import gnu.trove.map.TLongObjectMap;
/*      */ import gnu.trove.map.hash.TLongObjectHashMap;
/*      */ import gnu.trove.set.TLongSet;
/*      */ import gnu.trove.set.hash.TLongHashSet;
/*      */ import java.time.Instant;
/*      */ import java.time.OffsetDateTime;
/*      */ import java.time.format.DateTimeFormatter;
/*      */ import java.time.temporal.TemporalAccessor;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.function.ToLongFunction;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import java.util.stream.StreamSupport;
/*      */ import javax.annotation.Nullable;
/*      */ import net.dv8tion.jda.api.JDA;
/*      */ import net.dv8tion.jda.api.OnlineStatus;
/*      */ import net.dv8tion.jda.api.audit.ActionType;
/*      */ import net.dv8tion.jda.api.audit.AuditLogChange;
/*      */ import net.dv8tion.jda.api.entities.Activity;
/*      */ import net.dv8tion.jda.api.entities.ApplicationTeam;
/*      */ import net.dv8tion.jda.api.entities.Category;
/*      */ import net.dv8tion.jda.api.entities.ChannelType;
/*      */ import net.dv8tion.jda.api.entities.ClientType;
/*      */ import net.dv8tion.jda.api.entities.EmbedType;
/*      */ import net.dv8tion.jda.api.entities.Emote;
/*      */ import net.dv8tion.jda.api.entities.Guild;
/*      */ import net.dv8tion.jda.api.entities.GuildChannel;
/*      */ import net.dv8tion.jda.api.entities.Invite;
/*      */ import net.dv8tion.jda.api.entities.Member;
/*      */ import net.dv8tion.jda.api.entities.Message;
/*      */ import net.dv8tion.jda.api.entities.MessageActivity;
/*      */ import net.dv8tion.jda.api.entities.MessageChannel;
/*      */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*      */ import net.dv8tion.jda.api.entities.MessageReaction;
/*      */ import net.dv8tion.jda.api.entities.MessageReference;
/*      */ import net.dv8tion.jda.api.entities.MessageSticker;
/*      */ import net.dv8tion.jda.api.entities.MessageType;
/*      */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*      */ import net.dv8tion.jda.api.entities.RichPresence;
/*      */ import net.dv8tion.jda.api.entities.Role;
/*      */ import net.dv8tion.jda.api.entities.StageInstance;
/*      */ import net.dv8tion.jda.api.entities.StoreChannel;
/*      */ import net.dv8tion.jda.api.entities.TeamMember;
/*      */ import net.dv8tion.jda.api.entities.TextChannel;
/*      */ import net.dv8tion.jda.api.entities.User;
/*      */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*      */ import net.dv8tion.jda.api.entities.WebhookType;
/*      */ import net.dv8tion.jda.api.entities.templates.TemplateChannel;
/*      */ import net.dv8tion.jda.api.entities.templates.TemplateGuild;
/*      */ import net.dv8tion.jda.api.entities.templates.TemplateRole;
/*      */ import net.dv8tion.jda.api.events.GenericEvent;
/*      */ import net.dv8tion.jda.api.interactions.components.ActionRow;
/*      */ import net.dv8tion.jda.api.utils.cache.CacheFlag;
/*      */ import net.dv8tion.jda.api.utils.cache.CacheView;
/*      */ import net.dv8tion.jda.api.utils.data.DataArray;
/*      */ import net.dv8tion.jda.api.utils.data.DataObject;
/*      */ import net.dv8tion.jda.internal.JDAImpl;
/*      */ import net.dv8tion.jda.internal.handle.EventCache;
/*      */ import net.dv8tion.jda.internal.utils.UnlockHook;
/*      */ import net.dv8tion.jda.internal.utils.cache.MemberCacheViewImpl;
/*      */ import net.dv8tion.jda.internal.utils.cache.SnowflakeCacheViewImpl;
/*      */ import net.dv8tion.jda.internal.utils.cache.SortedSnowflakeCacheViewImpl;
/*      */ import org.apache.commons.collections4.map.CaseInsensitiveMap;
/*      */ 
/*      */ public class EntityBuilder {
/*   77 */   public static final Logger LOG = JDALogger.getLog(EntityBuilder.class); public static final String MISSING_CHANNEL = "MISSING_CHANNEL";
/*      */   public static final String MISSING_USER = "MISSING_USER";
/*      */   public static final String UNKNOWN_MESSAGE_TYPE = "UNKNOWN_MESSAGE_TYPE";
/*      */   private static final Set<String> richGameFields;
/*      */   protected final JDAImpl api;
/*      */   
/*      */   static {
/*   84 */     Set<String> tmp = new HashSet<>();
/*   85 */     tmp.add("application_id");
/*   86 */     tmp.add("assets");
/*   87 */     tmp.add("details");
/*   88 */     tmp.add("flags");
/*   89 */     tmp.add("party");
/*   90 */     tmp.add("session_id");
/*   91 */     tmp.add("state");
/*   92 */     tmp.add("sync_id");
/*   93 */     richGameFields = Collections.unmodifiableSet(tmp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EntityBuilder(JDA api) {
/*  100 */     this.api = (JDAImpl)api;
/*      */   }
/*      */ 
/*      */   
/*      */   public JDAImpl getJDA() {
/*  105 */     return this.api;
/*      */   }
/*      */ 
/*      */   
/*      */   public SelfUser createSelfUser(DataObject self) {
/*  110 */     SelfUserImpl selfUser = getJDA().hasSelfUser() ? (SelfUserImpl)getJDA().getSelfUser() : null;
/*  111 */     if (selfUser == null) {
/*      */       
/*  113 */       long id = self.getLong("id");
/*  114 */       selfUser = new SelfUserImpl(id, getJDA());
/*  115 */       getJDA().setSelfUser(selfUser);
/*      */     } 
/*      */     
/*  118 */     SnowflakeCacheViewImpl<User> userView = getJDA().getUsersView();
/*  119 */     UnlockHook hook = userView.writeLock();
/*      */     
/*  121 */     try { if (userView.getElementById(selfUser.getIdLong()) == null)
/*  122 */         userView.getMap().put(selfUser.getIdLong(), selfUser); 
/*  123 */       if (hook != null) hook.close();  } catch (Throwable throwable) { if (hook != null)
/*      */         try { hook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*  125 */      if (!self.isNull("application_id"))
/*  126 */       selfUser.setApplicationId(self.getUnsignedLong("application_id")); 
/*  127 */     selfUser.setVerified(self.getBoolean("verified"))
/*  128 */       .setMfaEnabled(self.getBoolean("mfa_enabled"))
/*  129 */       .setName(self.getString("username"))
/*  130 */       .setDiscriminator(self.getString("discriminator"))
/*  131 */       .setAvatarId(self.getString("avatar", null))
/*  132 */       .setBot(self.getBoolean("bot"))
/*  133 */       .setSystem(false);
/*      */     
/*  135 */     return selfUser;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Activity createActivity(String name, String url, Activity.ActivityType type) {
/*  140 */     return new ActivityImpl(name, url, type);
/*      */   }
/*      */ 
/*      */   
/*      */   private void createGuildEmotePass(GuildImpl guildObj, DataArray array) {
/*  145 */     if (!getJDA().isCacheFlagSet(CacheFlag.EMOTE))
/*      */       return; 
/*  147 */     SnowflakeCacheViewImpl<Emote> emoteView = guildObj.getEmotesView();
/*  148 */     UnlockHook hook = emoteView.writeLock();
/*      */     
/*  150 */     try { TLongObjectMap<Emote> emoteMap = emoteView.getMap();
/*  151 */       for (int i = 0; i < array.length(); i++) {
/*      */         
/*  153 */         DataObject object = array.getObject(i);
/*  154 */         if (object.isNull("id")) {
/*      */           
/*  156 */           LOG.error("Received GUILD_CREATE with an emoji with a null ID. JSON: {}", object);
/*      */         } else {
/*      */           
/*  159 */           long emoteId = object.getLong("id");
/*  160 */           emoteMap.put(emoteId, createEmote(guildObj, object));
/*      */         } 
/*  162 */       }  if (hook != null) hook.close();  }
/*      */     catch (Throwable throwable) { if (hook != null)
/*      */         try { hook.close(); }
/*      */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*      */           throw throwable; }
/*  167 */      } public TLongObjectMap<DataObject> convertToUserMap(ToLongFunction<DataObject> getId, DataArray array) { TLongObjectHashMap tLongObjectHashMap = new TLongObjectHashMap();
/*  168 */     for (int i = 0; i < array.length(); i++) {
/*      */       
/*  170 */       DataObject obj = array.getObject(i);
/*  171 */       long userId = getId.applyAsLong(obj);
/*  172 */       tLongObjectHashMap.put(userId, obj);
/*      */     } 
/*  174 */     return (TLongObjectMap<DataObject>)tLongObjectHashMap; }
/*      */ 
/*      */ 
/*      */   
/*      */   public GuildImpl createGuild(long guildId, DataObject guildJson, TLongObjectMap<DataObject> members, int memberCount) {
/*  179 */     GuildImpl guildObj = new GuildImpl(getJDA(), guildId);
/*  180 */     String name = guildJson.getString("name", "");
/*  181 */     String iconId = guildJson.getString("icon", null);
/*  182 */     String splashId = guildJson.getString("splash", null);
/*  183 */     String region = guildJson.getString("region", null);
/*  184 */     String description = guildJson.getString("description", null);
/*  185 */     String vanityCode = guildJson.getString("vanity_url_code", null);
/*  186 */     String bannerId = guildJson.getString("banner", null);
/*  187 */     String locale = guildJson.getString("preferred_locale", "en");
/*  188 */     DataArray roleArray = guildJson.getArray("roles");
/*  189 */     DataArray channelArray = guildJson.getArray("channels");
/*  190 */     DataArray emotesArray = guildJson.getArray("emojis");
/*  191 */     DataArray voiceStateArray = guildJson.getArray("voice_states");
/*  192 */     Optional<DataArray> featuresArray = guildJson.optArray("features");
/*  193 */     Optional<DataArray> presencesArray = guildJson.optArray("presences");
/*  194 */     long ownerId = guildJson.getUnsignedLong("owner_id", 0L);
/*  195 */     long afkChannelId = guildJson.getUnsignedLong("afk_channel_id", 0L);
/*  196 */     long systemChannelId = guildJson.getUnsignedLong("system_channel_id", 0L);
/*  197 */     long rulesChannelId = guildJson.getUnsignedLong("rules_channel_id", 0L);
/*  198 */     long communityUpdatesChannelId = guildJson.getUnsignedLong("public_updates_channel_id", 0L);
/*  199 */     int boostCount = guildJson.getInt("premium_subscription_count", 0);
/*  200 */     int boostTier = guildJson.getInt("premium_tier", 0);
/*  201 */     int maxMembers = guildJson.getInt("max_members", 0);
/*  202 */     int maxPresences = guildJson.getInt("max_presences", 5000);
/*  203 */     int mfaLevel = guildJson.getInt("mfa_level", 0);
/*  204 */     int afkTimeout = guildJson.getInt("afk_timeout", 0);
/*  205 */     int verificationLevel = guildJson.getInt("verification_level", 0);
/*  206 */     int notificationLevel = guildJson.getInt("default_message_notifications", 0);
/*  207 */     int explicitContentLevel = guildJson.getInt("explicit_content_filter", 0);
/*  208 */     int nsfwLevel = guildJson.getInt("nsfw_level", -1);
/*      */     
/*  210 */     guildObj.setAvailable(true)
/*  211 */       .setName(name)
/*  212 */       .setIconId(iconId)
/*  213 */       .setSplashId(splashId)
/*  214 */       .setRegion(region)
/*  215 */       .setDescription(description)
/*  216 */       .setBannerId(bannerId)
/*  217 */       .setVanityCode(vanityCode)
/*  218 */       .setMaxMembers(maxMembers)
/*  219 */       .setMaxPresences(maxPresences)
/*  220 */       .setOwnerId(ownerId)
/*  221 */       .setAfkTimeout(Guild.Timeout.fromKey(afkTimeout))
/*  222 */       .setVerificationLevel(Guild.VerificationLevel.fromKey(verificationLevel))
/*  223 */       .setDefaultNotificationLevel(Guild.NotificationLevel.fromKey(notificationLevel))
/*  224 */       .setExplicitContentLevel(Guild.ExplicitContentLevel.fromKey(explicitContentLevel))
/*  225 */       .setRequiredMFALevel(Guild.MFALevel.fromKey(mfaLevel))
/*  226 */       .setLocale(locale)
/*  227 */       .setBoostCount(boostCount)
/*  228 */       .setBoostTier(boostTier)
/*  229 */       .setMemberCount(memberCount)
/*  230 */       .setNSFWLevel(Guild.NSFWLevel.fromKey(nsfwLevel));
/*      */     
/*  232 */     SnowflakeCacheViewImpl<Guild> guildView = getJDA().getGuildsView();
/*  233 */     UnlockHook hook = guildView.writeLock();
/*      */     
/*  235 */     try { guildView.getMap().put(guildId, guildObj);
/*  236 */       if (hook != null) hook.close();  } catch (Throwable throwable) { if (hook != null)
/*      */         try { hook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*  238 */      guildObj.setFeatures(featuresArray.<Set<String>>map(it -> (Set)StreamSupport.stream(it.spliterator(), false).map(String::valueOf).collect(Collectors.toSet()))
/*      */ 
/*      */ 
/*      */         
/*  242 */         .orElse(Collections.emptySet()));
/*      */     
/*  244 */     SortedSnowflakeCacheViewImpl<Role> sortedSnowflakeCacheViewImpl = guildObj.getRolesView();
/*  245 */     UnlockHook unlockHook1 = sortedSnowflakeCacheViewImpl.writeLock();
/*      */     
/*  247 */     try { TLongObjectMap<Role> map = sortedSnowflakeCacheViewImpl.getMap();
/*  248 */       for (int j = 0; j < roleArray.length(); j++) {
/*      */         
/*  250 */         DataObject obj = roleArray.getObject(j);
/*  251 */         Role role = createRole(guildObj, obj, guildId);
/*  252 */         map.put(role.getIdLong(), role);
/*  253 */         if (role.getIdLong() == guildObj.getIdLong())
/*  254 */           guildObj.setPublicRole(role); 
/*      */       } 
/*  256 */       if (unlockHook1 != null) unlockHook1.close();  } catch (Throwable throwable) { if (unlockHook1 != null)
/*      */         try { unlockHook1.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*  258 */      for (int i = 0; i < channelArray.length(); i++) {
/*      */       
/*  260 */       DataObject channelJson = channelArray.getObject(i);
/*  261 */       createGuildChannel(guildObj, channelJson);
/*      */     } 
/*      */     
/*  264 */     TLongObjectMap<DataObject> voiceStates = convertToUserMap(o -> o.getUnsignedLong("user_id", 0L), voiceStateArray);
/*  265 */     TLongObjectMap<DataObject> presences = presencesArray.<TLongObjectMap<DataObject>>map(o1 -> convertToUserMap((), o1)).orElseGet(TLongObjectHashMap::new);
/*  266 */     UnlockHook h1 = guildObj.getMembersView().writeLock(); 
/*  267 */     try { UnlockHook h2 = getJDA().getUsersView().writeLock();
/*      */ 
/*      */ 
/*      */       
/*  271 */       try { for (DataObject memberJson : members.valueCollection()) {
/*      */           
/*  273 */           long userId = memberJson.getObject("user").getUnsignedLong("id");
/*  274 */           DataObject voiceState = (DataObject)voiceStates.get(userId);
/*  275 */           DataObject presence = (DataObject)presences.get(userId);
/*  276 */           updateMemberCache(createMember(guildObj, memberJson, voiceState, presence));
/*      */         } 
/*  278 */         if (h2 != null) h2.close();  } catch (Throwable throwable) { if (h2 != null) try { h2.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (h1 != null) h1.close();  } catch (Throwable throwable) { if (h1 != null)
/*      */         try { h1.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*  280 */      if (guildObj.getOwner() == null)
/*  281 */       LOG.debug("Finished setup for guild with a null owner. GuildId: {} OwnerId: {}", Long.valueOf(guildId), guildJson.opt("owner_id").orElse(null)); 
/*  282 */     if (guildObj.getMember((User)this.api.getSelfUser()) == null) {
/*      */       
/*  284 */       LOG.error("Guild is missing a SelfMember. GuildId: {}", Long.valueOf(guildId));
/*  285 */       LOG.debug("Guild is missing a SelfMember. GuildId: {} JSON: \n{}", Long.valueOf(guildId), guildJson);
/*      */       
/*  287 */       guildObj.retrieveMembersByIds(new long[] { this.api.getSelfUser().getIdLong() }).onSuccess(m -> {
/*      */             if (m.isEmpty()) {
/*      */               LOG.warn("Was unable to recover SelfMember for guild with id {}. This guild might be corrupted!", Long.valueOf(guildId));
/*      */             } else {
/*      */               LOG.debug("Successfully recovered SelfMember for guild with id {}.", Long.valueOf(guildId));
/*      */             } 
/*      */           });
/*      */     } 
/*      */     
/*  296 */     createGuildEmotePass(guildObj, emotesArray);
/*  297 */     guildJson.optArray("stage_instances")
/*  298 */       .map(arr -> arr.stream(DataArray::getObject))
/*  299 */       .ifPresent(list -> list.forEach(()));
/*      */     
/*  301 */     guildObj.setAfkChannel(guildObj.getVoiceChannelById(afkChannelId))
/*  302 */       .setSystemChannel(guildObj.getTextChannelById(systemChannelId))
/*  303 */       .setRulesChannel(guildObj.getTextChannelById(rulesChannelId))
/*  304 */       .setCommunityUpdatesChannel(guildObj.getTextChannelById(communityUpdatesChannelId));
/*      */     
/*  306 */     return guildObj;
/*      */   }
/*      */ 
/*      */   
/*      */   private void createGuildChannel(GuildImpl guildObj, DataObject channelData) {
/*  311 */     ChannelType channelType = ChannelType.fromId(channelData.getInt("type"));
/*  312 */     switch (channelType) {
/*      */       
/*      */       case EMBEDDED_APPLICATION:
/*  315 */         createTextChannel(guildObj, channelData, guildObj.getIdLong());
/*      */         return;
/*      */       case STREAM:
/*      */       case NONE:
/*  319 */         createVoiceChannel(guildObj, channelData, guildObj.getIdLong());
/*      */         return;
/*      */       case null:
/*  322 */         createCategory(guildObj, channelData, guildObj.getIdLong());
/*      */         return;
/*      */       case null:
/*  325 */         createStoreChannel(guildObj, channelData, guildObj.getIdLong());
/*      */         return;
/*      */     } 
/*  328 */     LOG.debug("Cannot create channel for type " + channelData.getInt("type"));
/*      */   }
/*      */ 
/*      */   
/*      */   public UserImpl createUser(DataObject user) {
/*      */     UserImpl userObj;
/*  334 */     boolean newUser = false;
/*  335 */     long id = user.getLong("id");
/*      */ 
/*      */     
/*  338 */     SnowflakeCacheViewImpl<User> userView = getJDA().getUsersView();
/*  339 */     UnlockHook hook = userView.readLock();
/*      */     
/*  341 */     try { userObj = (UserImpl)userView.getElementById(id);
/*  342 */       if (userObj == null) {
/*      */         
/*  344 */         userObj = new UserImpl(id, getJDA());
/*  345 */         newUser = true;
/*      */       } 
/*  347 */       if (hook != null) hook.close();  } catch (Throwable throwable) { if (hook != null)
/*      */         try { hook.close(); }
/*      */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*      */           throw throwable; }
/*  351 */      User.Profile profile = user.hasKey("banner") ? new User.Profile(id, user.getString("banner", null), user.getInt("accent_color", 536870911)) : null;
/*      */     
/*  353 */     if (newUser) {
/*      */ 
/*      */       
/*  356 */       userObj.setName(user.getString("username"))
/*  357 */         .setDiscriminator(user.get("discriminator").toString())
/*  358 */         .setAvatarId(user.getString("avatar", null))
/*  359 */         .setBot(user.getBoolean("bot"))
/*  360 */         .setSystem(user.getBoolean("system"))
/*  361 */         .setFlags(user.getInt("public_flags", 0))
/*  362 */         .setProfile(profile);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  367 */       updateUser(userObj, user);
/*      */     } 
/*      */     
/*  370 */     return userObj;
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateUser(UserImpl userObj, DataObject user) {
/*  375 */     String oldName = userObj.getName();
/*  376 */     String newName = user.getString("username");
/*  377 */     String oldDiscriminator = userObj.getDiscriminator();
/*  378 */     String newDiscriminator = user.get("discriminator").toString();
/*  379 */     String oldAvatar = userObj.getAvatarId();
/*  380 */     String newAvatar = user.getString("avatar", null);
/*  381 */     int oldFlags = userObj.getFlagsRaw();
/*  382 */     int newFlags = user.getInt("public_flags", 0);
/*      */     
/*  384 */     JDAImpl jda = getJDA();
/*  385 */     long responseNumber = jda.getResponseTotal();
/*  386 */     if (!oldName.equals(newName)) {
/*      */       
/*  388 */       userObj.setName(newName);
/*  389 */       jda.handleEvent((GenericEvent)new UserUpdateNameEvent((JDA)jda, responseNumber, userObj, oldName));
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  395 */     if (!oldDiscriminator.equals(newDiscriminator)) {
/*      */       
/*  397 */       userObj.setDiscriminator(newDiscriminator);
/*  398 */       jda.handleEvent((GenericEvent)new UserUpdateDiscriminatorEvent((JDA)jda, responseNumber, userObj, oldDiscriminator));
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  404 */     if (!Objects.equals(oldAvatar, newAvatar)) {
/*      */       
/*  406 */       userObj.setAvatarId(newAvatar);
/*  407 */       jda.handleEvent((GenericEvent)new UserUpdateAvatarEvent((JDA)jda, responseNumber, userObj, oldAvatar));
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  413 */     if (oldFlags != newFlags) {
/*      */       
/*  415 */       userObj.setFlags(newFlags);
/*  416 */       jda.handleEvent((GenericEvent)new UserUpdateFlagsEvent((JDA)jda, responseNumber, userObj, 
/*      */ 
/*      */             
/*  419 */             User.UserFlag.getFlags(oldFlags)));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean updateMemberCache(MemberImpl member) {
/*  425 */     return updateMemberCache(member, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean updateMemberCache(MemberImpl member, boolean forceRemove) {
/*  430 */     GuildImpl guild = member.getGuild();
/*  431 */     UserImpl user = (UserImpl)member.getUser();
/*  432 */     MemberCacheViewImpl membersView = guild.getMembersView();
/*  433 */     if (forceRemove || !getJDA().cacheMember(member)) {
/*      */       
/*  435 */       if (membersView.remove(member.getIdLong()) == null)
/*  436 */         return false; 
/*  437 */       LOG.trace("Unloading member {}", member);
/*  438 */       if (user.getMutualGuilds().isEmpty()) {
/*      */ 
/*      */         
/*  441 */         user.setFake(true);
/*  442 */         getJDA().getUsersView().remove(user.getIdLong());
/*      */       } 
/*      */       
/*  445 */       GuildVoiceStateImpl voiceState = (GuildVoiceStateImpl)member.getVoiceState();
/*  446 */       if (voiceState != null) {
/*      */         
/*  448 */         VoiceChannelImpl connectedChannel = (VoiceChannelImpl)voiceState.getChannel();
/*  449 */         if (connectedChannel != null)
/*  450 */           connectedChannel.getConnectedMembersMap().remove(member.getIdLong()); 
/*  451 */         voiceState.setConnectedChannel(null);
/*      */       } 
/*      */       
/*  454 */       return false;
/*      */     } 
/*  456 */     if (guild.getMemberById(member.getIdLong()) != null)
/*      */     {
/*      */       
/*  459 */       return true;
/*      */     }
/*      */     
/*  462 */     LOG.trace("Loading member {}", member);
/*      */     
/*  464 */     if (getJDA().getUserById(user.getIdLong()) == null) {
/*      */       
/*  466 */       SnowflakeCacheViewImpl<User> usersView = getJDA().getUsersView();
/*  467 */       UnlockHook hook1 = usersView.writeLock();
/*      */       
/*  469 */       try { usersView.getMap().put(user.getIdLong(), user);
/*  470 */         if (hook1 != null) hook1.close();  } catch (Throwable throwable) { if (hook1 != null)
/*      */           try { hook1.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*      */     
/*  473 */     }  UnlockHook hook = membersView.writeLock();
/*      */     
/*  475 */     try { membersView.getMap().put(member.getIdLong(), member);
/*  476 */       if (member.isOwner())
/*  477 */         guild.setOwner(member); 
/*  478 */       if (hook != null) hook.close();  } catch (Throwable throwable) { if (hook != null)
/*      */         try { hook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*  480 */      long hashId = guild.getIdLong() ^ user.getIdLong();
/*  481 */     getJDA().getEventCache().playbackCache(EventCache.Type.USER, member.getIdLong());
/*  482 */     getJDA().getEventCache().playbackCache(EventCache.Type.MEMBER, hashId);
/*  483 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public MemberImpl createMember(GuildImpl guild, DataObject memberJson) {
/*  488 */     return createMember(guild, memberJson, null, null);
/*      */   }
/*      */ 
/*      */   
/*      */   public MemberImpl createMember(GuildImpl guild, DataObject memberJson, DataObject voiceStateJson, DataObject presence) {
/*  493 */     boolean playbackCache = false;
/*  494 */     User user = createUser(memberJson.getObject("user"));
/*  495 */     DataArray roleArray = memberJson.getArray("roles");
/*  496 */     MemberImpl member = (MemberImpl)guild.getMember(user);
/*  497 */     if (member == null) {
/*      */ 
/*      */       
/*  500 */       member = new MemberImpl(guild, user);
/*  501 */       member.setNickname(memberJson.getString("nick", null));
/*  502 */       member.setAvatarId(memberJson.getString("avatar", null));
/*      */       
/*  504 */       long epoch = 0L;
/*  505 */       if (!memberJson.isNull("premium_since")) {
/*      */         
/*  507 */         TemporalAccessor date = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(memberJson.getString("premium_since"));
/*  508 */         epoch = Instant.from(date).toEpochMilli();
/*      */       } 
/*  510 */       member.setBoostDate(epoch);
/*  511 */       if (!memberJson.isNull("pending"))
/*  512 */         member.setPending(memberJson.getBoolean("pending")); 
/*  513 */       Set<Role> roles = member.getRoleSet();
/*  514 */       for (int i = 0; i < roleArray.length(); i++)
/*      */       {
/*  516 */         long roleId = roleArray.getUnsignedLong(i);
/*  517 */         Role role = guild.getRoleById(roleId);
/*  518 */         if (role != null) {
/*  519 */           roles.add(role);
/*      */         }
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  525 */       List<Role> roles = new ArrayList<>(roleArray.length());
/*  526 */       for (int i = 0; i < roleArray.length(); i++) {
/*      */         
/*  528 */         long roleId = roleArray.getUnsignedLong(i);
/*  529 */         Role role = guild.getRoleById(roleId);
/*  530 */         if (role != null)
/*  531 */           roles.add(role); 
/*      */       } 
/*  533 */       updateMember(guild, member, memberJson, roles);
/*      */     } 
/*      */ 
/*      */     
/*  537 */     if (!memberJson.isNull("joined_at") && !member.hasTimeJoined()) {
/*      */       
/*  539 */       String joinedAtRaw = memberJson.getString("joined_at");
/*  540 */       TemporalAccessor joinedAt = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(joinedAtRaw);
/*  541 */       long joinEpoch = Instant.from(joinedAt).toEpochMilli();
/*  542 */       member.setJoinDate(joinEpoch);
/*      */     } 
/*      */ 
/*      */     
/*  546 */     if (voiceStateJson != null && member.getVoiceState() != null)
/*  547 */       createVoiceState(guild, voiceStateJson, user, member); 
/*  548 */     if (presence != null)
/*  549 */       createPresence(member, presence); 
/*  550 */     return member;
/*      */   }
/*      */ 
/*      */   
/*      */   private void createVoiceState(GuildImpl guild, DataObject voiceStateJson, User user, MemberImpl member) {
/*  555 */     GuildVoiceStateImpl voiceState = (GuildVoiceStateImpl)member.getVoiceState();
/*      */     
/*  557 */     long channelId = voiceStateJson.getLong("channel_id");
/*  558 */     VoiceChannelImpl voiceChannel = (VoiceChannelImpl)guild.getVoiceChannelsView().get(channelId);
/*  559 */     if (voiceChannel != null) {
/*  560 */       voiceChannel.getConnectedMembersMap().put(member.getIdLong(), member);
/*      */     } else {
/*  562 */       LOG.error("Received a GuildVoiceState with a channel ID for a non-existent channel! ChannelId: {} GuildId: {} UserId: {}", new Object[] {
/*  563 */             Long.valueOf(channelId), guild.getId(), user.getId() });
/*      */     } 
/*  565 */     String requestToSpeak = voiceStateJson.getString("request_to_speak_timestamp", null);
/*  566 */     OffsetDateTime timestamp = null;
/*  567 */     if (requestToSpeak != null) {
/*  568 */       timestamp = OffsetDateTime.parse(requestToSpeak);
/*      */     }
/*      */     
/*  571 */     voiceState.setSelfMuted(voiceStateJson.getBoolean("self_mute"))
/*  572 */       .setSelfDeafened(voiceStateJson.getBoolean("self_deaf"))
/*  573 */       .setGuildMuted(voiceStateJson.getBoolean("mute"))
/*  574 */       .setGuildDeafened(voiceStateJson.getBoolean("deaf"))
/*  575 */       .setSuppressed(voiceStateJson.getBoolean("suppress"))
/*  576 */       .setSessionId(voiceStateJson.getString("session_id"))
/*  577 */       .setStream(voiceStateJson.getBoolean("self_stream"))
/*  578 */       .setRequestToSpeak(timestamp)
/*  579 */       .setConnectedChannel(voiceChannel);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateMember(GuildImpl guild, MemberImpl member, DataObject content, List<Role> newRoles) {
/*  585 */     long responseNumber = getJDA().getResponseTotal();
/*  586 */     if (newRoles != null)
/*      */     {
/*  588 */       updateMemberRoles(member, newRoles, responseNumber);
/*      */     }
/*      */     
/*  591 */     if (content.hasKey("nick")) {
/*      */       
/*  593 */       String oldNick = member.getNickname();
/*  594 */       String newNick = content.getString("nick", null);
/*  595 */       if (!Objects.equals(oldNick, newNick)) {
/*      */         
/*  597 */         member.setNickname(newNick);
/*  598 */         getJDA().handleEvent((GenericEvent)new GuildMemberUpdateNicknameEvent((JDA)
/*      */               
/*  600 */               getJDA(), responseNumber, member, oldNick));
/*      */       } 
/*      */     } 
/*      */     
/*  604 */     if (content.hasKey("avatar")) {
/*      */       
/*  606 */       String oldAvatarId = member.getAvatarId();
/*  607 */       String newAvatarId = content.getString("avatar", null);
/*  608 */       if (!Objects.equals(oldAvatarId, newAvatarId)) {
/*      */         
/*  610 */         member.setAvatarId(newAvatarId);
/*  611 */         getJDA().handleEvent((GenericEvent)new GuildMemberUpdateAvatarEvent((JDA)
/*      */               
/*  613 */               getJDA(), responseNumber, member, oldAvatarId));
/*      */       } 
/*      */     } 
/*      */     
/*  617 */     if (content.hasKey("premium_since")) {
/*      */       
/*  619 */       long epoch = 0L;
/*  620 */       if (!content.isNull("premium_since")) {
/*      */         
/*  622 */         TemporalAccessor date = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(content.getString("premium_since"));
/*  623 */         epoch = Instant.from(date).toEpochMilli();
/*      */       } 
/*  625 */       if (epoch != member.getBoostDateRaw()) {
/*      */         
/*  627 */         OffsetDateTime oldTime = member.getTimeBoosted();
/*  628 */         member.setBoostDate(epoch);
/*  629 */         getJDA().handleEvent((GenericEvent)new GuildMemberUpdateBoostTimeEvent((JDA)
/*      */               
/*  631 */               getJDA(), responseNumber, member, oldTime));
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  636 */     if (!content.isNull("joined_at") && !member.hasTimeJoined()) {
/*      */       
/*  638 */       String joinedAtRaw = content.getString("joined_at");
/*  639 */       TemporalAccessor joinedAt = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(joinedAtRaw);
/*  640 */       long joinEpoch = Instant.from(joinedAt).toEpochMilli();
/*  641 */       member.setJoinDate(joinEpoch);
/*      */     } 
/*      */     
/*  644 */     if (!content.isNull("pending")) {
/*      */       
/*  646 */       boolean pending = content.getBoolean("pending");
/*  647 */       boolean oldPending = member.isPending();
/*  648 */       if (pending != oldPending) {
/*      */         
/*  650 */         member.setPending(pending);
/*  651 */         getJDA().handleEvent((GenericEvent)new GuildMemberUpdatePendingEvent((JDA)
/*      */               
/*  653 */               getJDA(), responseNumber, member, oldPending));
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  658 */     updateUser((UserImpl)member.getUser(), content.getObject("user"));
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateMemberRoles(MemberImpl member, List<Role> newRoles, long responseNumber) {
/*  663 */     Set<Role> currentRoles = member.getRoleSet();
/*      */     
/*  665 */     List<Role> removedRoles = new LinkedList<>();
/*      */     
/*  667 */     for (Role role : currentRoles) {
/*      */       Iterator<Role> it;
/*  669 */       for (it = newRoles.iterator(); it.hasNext(); ) {
/*      */         
/*  671 */         Role r = it.next();
/*  672 */         if (role.equals(r))
/*      */         {
/*  674 */           it.remove();
/*      */         }
/*      */       } 
/*      */       
/*  678 */       removedRoles.add(role);
/*      */     } 
/*      */     
/*  681 */     if (removedRoles.size() > 0)
/*  682 */       currentRoles.removeAll(removedRoles); 
/*  683 */     if (newRoles.size() > 0) {
/*  684 */       currentRoles.addAll(newRoles);
/*      */     }
/*  686 */     if (removedRoles.size() > 0)
/*      */     {
/*  688 */       getJDA().handleEvent((GenericEvent)new GuildMemberRoleRemoveEvent((JDA)
/*      */             
/*  690 */             getJDA(), responseNumber, member, removedRoles));
/*      */     }
/*      */     
/*  693 */     if (newRoles.size() > 0)
/*      */     {
/*  695 */       getJDA().handleEvent((GenericEvent)new GuildMemberRoleAddEvent((JDA)
/*      */             
/*  697 */             getJDA(), responseNumber, member, newRoles));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void createPresence(MemberImpl member, DataObject presenceJson) {
/*  704 */     if (member == null)
/*  705 */       throw new NullPointerException("Provided member was null!"); 
/*  706 */     OnlineStatus onlineStatus = OnlineStatus.fromKey(presenceJson.getString("status"));
/*  707 */     if (onlineStatus == OnlineStatus.OFFLINE)
/*      */       return; 
/*  709 */     MemberPresenceImpl presence = member.getPresence();
/*  710 */     if (presence == null) {
/*      */       
/*  712 */       CacheView.SimpleCacheView<MemberPresenceImpl> view = member.getGuild().getPresenceView();
/*  713 */       if (view == null)
/*      */         return; 
/*  715 */       presence = new MemberPresenceImpl();
/*  716 */       UnlockHook lock = view.writeLock();
/*      */       
/*  718 */       try { view.getMap().put(member.getIdLong(), presence);
/*  719 */         if (lock != null) lock.close();  } catch (Throwable throwable) { if (lock != null)
/*      */           try { lock.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*      */     
/*  722 */     }  boolean cacheGame = getJDA().isCacheFlagSet(CacheFlag.ACTIVITY);
/*  723 */     boolean cacheStatus = getJDA().isCacheFlagSet(CacheFlag.CLIENT_STATUS);
/*      */     
/*  725 */     DataArray activityArray = (!cacheGame || presenceJson.isNull("activities")) ? null : presenceJson.getArray("activities");
/*  726 */     DataObject clientStatusJson = (!cacheStatus || presenceJson.isNull("client_status")) ? null : presenceJson.getObject("client_status");
/*  727 */     List<Activity> activities = new ArrayList<>();
/*  728 */     boolean parsedActivity = false;
/*      */     
/*  730 */     if (cacheGame && activityArray != null)
/*      */     {
/*  732 */       for (int i = 0; i < activityArray.length(); i++) {
/*      */ 
/*      */         
/*      */         try {
/*  736 */           activities.add(createActivity(activityArray.getObject(i)));
/*  737 */           parsedActivity = true;
/*      */         }
/*  739 */         catch (Exception ex) {
/*      */           
/*  741 */           String userId = member.getId();
/*  742 */           if (LOG.isDebugEnabled()) {
/*  743 */             LOG.warn("Encountered exception trying to parse a presence! UserId: {} JSON: {}", new Object[] { userId, activityArray, ex });
/*      */           } else {
/*  745 */             LOG.warn("Encountered exception trying to parse a presence! UserId: {} Message: {} Enable debug for details", userId, ex.getMessage());
/*      */           } 
/*      */         } 
/*      */       }  } 
/*  749 */     if (cacheGame && parsedActivity)
/*  750 */       presence.setActivities(activities); 
/*  751 */     presence.setOnlineStatus(onlineStatus);
/*  752 */     if (clientStatusJson != null)
/*      */     {
/*  754 */       for (String key : clientStatusJson.keys()) {
/*      */         
/*  756 */         ClientType type = ClientType.fromKey(key);
/*  757 */         OnlineStatus status = OnlineStatus.fromKey(clientStatusJson.getString(key));
/*  758 */         presence.setOnlineStatus(type, status);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static Activity createActivity(DataObject gameJson) {
/*      */     Activity.ActivityType type;
/*  765 */     String name = String.valueOf(gameJson.get("name"));
/*  766 */     String url = gameJson.isNull("url") ? null : String.valueOf(gameJson.get("url"));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  772 */       type = gameJson.isNull("type") ? Activity.ActivityType.DEFAULT : Activity.ActivityType.fromKey(Integer.parseInt(gameJson.get("type").toString()));
/*      */     }
/*  774 */     catch (NumberFormatException e) {
/*      */       
/*  776 */       type = Activity.ActivityType.DEFAULT;
/*      */     } 
/*      */     
/*  779 */     Activity.Timestamps timestamps = null;
/*  780 */     if (!gameJson.isNull("timestamps")) {
/*      */       
/*  782 */       DataObject obj = gameJson.getObject("timestamps");
/*      */       
/*  784 */       long start = obj.getLong("start", 0L);
/*  785 */       long end = obj.getLong("end", 0L);
/*  786 */       timestamps = new Activity.Timestamps(start, end);
/*      */     } 
/*      */     
/*  789 */     Activity.Emoji emoji = null;
/*  790 */     if (!gameJson.isNull("emoji")) {
/*      */       
/*  792 */       DataObject emojiJson = gameJson.getObject("emoji");
/*  793 */       String emojiName = emojiJson.getString("name");
/*  794 */       long emojiId = emojiJson.getUnsignedLong("id", 0L);
/*  795 */       boolean emojiAnimated = emojiJson.getBoolean("animated");
/*  796 */       emoji = new Activity.Emoji(emojiName, emojiId, emojiAnimated);
/*      */     } 
/*      */     
/*  799 */     if (type == Activity.ActivityType.CUSTOM_STATUS)
/*      */     {
/*  801 */       if (gameJson.hasKey("state") && name.equalsIgnoreCase("Custom Status")) {
/*      */         
/*  803 */         name = gameJson.getString("state", "");
/*  804 */         gameJson = gameJson.remove("state");
/*      */       } 
/*      */     }
/*      */     
/*  808 */     if (!CollectionUtils.containsAny(gameJson.keys(), richGameFields)) {
/*  809 */       return new ActivityImpl(name, url, type, timestamps, emoji);
/*      */     }
/*      */     
/*  812 */     long id = gameJson.getLong("application_id", 0L);
/*  813 */     String sessionId = gameJson.getString("session_id", null);
/*  814 */     String syncId = gameJson.getString("sync_id", null);
/*  815 */     int flags = gameJson.getInt("flags", 0);
/*  816 */     String details = gameJson.isNull("details") ? null : String.valueOf(gameJson.get("details"));
/*  817 */     String state = gameJson.isNull("state") ? null : String.valueOf(gameJson.get("state"));
/*      */     
/*  819 */     RichPresence.Party party = null;
/*  820 */     if (!gameJson.isNull("party")) {
/*      */       
/*  822 */       DataObject obj = gameJson.getObject("party");
/*  823 */       String partyId = obj.isNull("id") ? null : obj.getString("id");
/*  824 */       DataArray sizeArr = obj.isNull("size") ? null : obj.getArray("size");
/*  825 */       long size = 0L, max = 0L;
/*  826 */       if (sizeArr != null && sizeArr.length() > 0) {
/*      */         
/*  828 */         size = sizeArr.getLong(0);
/*  829 */         max = (sizeArr.length() < 2) ? 0L : sizeArr.getLong(1);
/*      */       } 
/*  831 */       party = new RichPresence.Party(partyId, size, max);
/*      */     } 
/*      */     
/*  834 */     String smallImageKey = null, smallImageText = null;
/*  835 */     String largeImageKey = null, largeImageText = null;
/*  836 */     if (!gameJson.isNull("assets")) {
/*      */       
/*  838 */       DataObject assets = gameJson.getObject("assets");
/*  839 */       if (!assets.isNull("small_image")) {
/*      */         
/*  841 */         smallImageKey = String.valueOf(assets.get("small_image"));
/*  842 */         smallImageText = assets.isNull("small_text") ? null : String.valueOf(assets.get("small_text"));
/*      */       } 
/*  844 */       if (!assets.isNull("large_image")) {
/*      */         
/*  846 */         largeImageKey = String.valueOf(assets.get("large_image"));
/*  847 */         largeImageText = assets.isNull("large_text") ? null : String.valueOf(assets.get("large_text"));
/*      */       } 
/*      */     } 
/*      */     
/*  851 */     return new RichPresenceImpl(type, name, url, id, emoji, party, details, state, timestamps, syncId, sessionId, flags, largeImageKey, largeImageText, smallImageKey, smallImageText);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EmoteImpl createEmote(GuildImpl guildObj, DataObject json) {
/*  858 */     DataArray emoteRoles = json.optArray("roles").orElseGet(DataArray::empty);
/*  859 */     long emoteId = json.getLong("id");
/*  860 */     User user = json.isNull("user") ? null : createUser(json.getObject("user"));
/*  861 */     EmoteImpl emoteObj = (EmoteImpl)guildObj.getEmoteById(emoteId);
/*  862 */     if (emoteObj == null)
/*  863 */       emoteObj = new EmoteImpl(emoteId, guildObj); 
/*  864 */     Set<Role> roleSet = emoteObj.getRoleSet();
/*      */     
/*  866 */     roleSet.clear();
/*  867 */     for (int j = 0; j < emoteRoles.length(); j++) {
/*      */       
/*  869 */       Role role = guildObj.getRoleById(emoteRoles.getString(j));
/*  870 */       if (role != null)
/*  871 */         roleSet.add(role); 
/*      */     } 
/*  873 */     if (user != null)
/*  874 */       emoteObj.setUser(user); 
/*  875 */     return emoteObj
/*  876 */       .setName(json.getString("name", ""))
/*  877 */       .setAnimated(json.getBoolean("animated"))
/*  878 */       .setManaged(json.getBoolean("managed"))
/*  879 */       .setAvailable(json.getBoolean("available", true));
/*      */   }
/*      */ 
/*      */   
/*      */   public Category createCategory(DataObject json, long guildId) {
/*  884 */     return createCategory(null, json, guildId);
/*      */   }
/*      */ 
/*      */   
/*      */   public Category createCategory(GuildImpl guild, DataObject json, long guildId) {
/*  889 */     boolean playbackCache = false;
/*  890 */     long id = json.getLong("id");
/*  891 */     CategoryImpl channel = (CategoryImpl)getJDA().getCategoriesView().get(id);
/*  892 */     if (channel == null) {
/*      */       
/*  894 */       if (guild == null) {
/*  895 */         guild = (GuildImpl)getJDA().getGuildsView().get(guildId);
/*      */       }
/*  897 */       SortedSnowflakeCacheViewImpl<Category> sortedSnowflakeCacheViewImpl = guild.getCategoriesView();
/*  898 */       SnowflakeCacheViewImpl<Category> categoryView = getJDA().getCategoriesView();
/*      */       
/*  900 */       UnlockHook glock = sortedSnowflakeCacheViewImpl.writeLock(); 
/*  901 */       try { UnlockHook jlock = categoryView.writeLock();
/*      */         
/*  903 */         try { channel = new CategoryImpl(id, guild);
/*  904 */           sortedSnowflakeCacheViewImpl.getMap().put(id, channel);
/*  905 */           playbackCache = (categoryView.getMap().put(id, channel) == null);
/*  906 */           if (jlock != null) jlock.close();  } catch (Throwable throwable) { if (jlock != null) try { jlock.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (glock != null) glock.close();  } catch (Throwable throwable) { if (glock != null)
/*      */           try { glock.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*      */     
/*  909 */     }  channel
/*  910 */       .setName(json.getString("name"))
/*  911 */       .setPosition(json.getInt("position"));
/*      */     
/*  913 */     createOverridesPass(channel, json.getArray("permission_overwrites"));
/*  914 */     if (playbackCache)
/*  915 */       getJDA().getEventCache().playbackCache(EventCache.Type.CHANNEL, id); 
/*  916 */     return channel;
/*      */   }
/*      */ 
/*      */   
/*      */   public StoreChannel createStoreChannel(DataObject json, long guildId) {
/*  921 */     return createStoreChannel(null, json, guildId);
/*      */   }
/*      */ 
/*      */   
/*      */   public StoreChannel createStoreChannel(GuildImpl guild, DataObject json, long guildId) {
/*  926 */     boolean playbackCache = false;
/*  927 */     long id = json.getLong("id");
/*  928 */     StoreChannelImpl channel = (StoreChannelImpl)getJDA().getStoreChannelsView().get(id);
/*  929 */     if (channel == null) {
/*      */       
/*  931 */       if (guild == null) {
/*  932 */         guild = (GuildImpl)getJDA().getGuildById(guildId);
/*      */       }
/*  934 */       SortedSnowflakeCacheViewImpl<StoreChannel> sortedSnowflakeCacheViewImpl = guild.getStoreChannelView();
/*  935 */       SnowflakeCacheViewImpl<StoreChannel> storeView = getJDA().getStoreChannelsView();
/*      */       
/*  937 */       UnlockHook glock = sortedSnowflakeCacheViewImpl.writeLock(); 
/*  938 */       try { UnlockHook jlock = storeView.writeLock();
/*      */         
/*  940 */         try { channel = new StoreChannelImpl(id, guild);
/*  941 */           sortedSnowflakeCacheViewImpl.getMap().put(id, channel);
/*  942 */           playbackCache = (storeView.getMap().put(id, channel) == null);
/*  943 */           if (jlock != null) jlock.close();  } catch (Throwable throwable) { if (jlock != null) try { jlock.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (glock != null) glock.close();  } catch (Throwable throwable) { if (glock != null)
/*      */           try { glock.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*      */     
/*  946 */     }  channel
/*  947 */       .setParent(json.getLong("parent_id", 0L))
/*  948 */       .setName(json.getString("name"))
/*  949 */       .setPosition(json.getInt("position"));
/*      */     
/*  951 */     createOverridesPass(channel, json.getArray("permission_overwrites"));
/*  952 */     if (playbackCache)
/*  953 */       getJDA().getEventCache().playbackCache(EventCache.Type.CHANNEL, id); 
/*  954 */     return channel;
/*      */   }
/*      */ 
/*      */   
/*      */   public TextChannel createTextChannel(DataObject json, long guildId) {
/*  959 */     return createTextChannel(null, json, guildId);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public TextChannel createTextChannel(GuildImpl guildObj, DataObject json, long guildId) {
/*  965 */     boolean playbackCache = false;
/*  966 */     long id = json.getLong("id");
/*  967 */     TextChannelImpl channel = (TextChannelImpl)getJDA().getTextChannelsView().get(id);
/*  968 */     if (channel == null) {
/*      */       
/*  970 */       if (guildObj == null) {
/*  971 */         guildObj = (GuildImpl)getJDA().getGuildsView().get(guildId);
/*      */       }
/*  973 */       SortedSnowflakeCacheViewImpl<TextChannel> sortedSnowflakeCacheViewImpl = guildObj.getTextChannelsView();
/*  974 */       SnowflakeCacheViewImpl<TextChannel> textView = getJDA().getTextChannelsView();
/*      */       
/*  976 */       UnlockHook glock = sortedSnowflakeCacheViewImpl.writeLock(); 
/*  977 */       try { UnlockHook jlock = textView.writeLock();
/*      */         
/*  979 */         try { channel = new TextChannelImpl(id, guildObj);
/*  980 */           sortedSnowflakeCacheViewImpl.getMap().put(id, channel);
/*  981 */           playbackCache = (textView.getMap().put(id, channel) == null);
/*  982 */           if (jlock != null) jlock.close();  } catch (Throwable throwable) { if (jlock != null) try { jlock.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (glock != null) glock.close();  } catch (Throwable throwable) { if (glock != null)
/*      */           try { glock.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*      */     
/*  985 */     }  channel
/*  986 */       .setParent(json.getLong("parent_id", 0L))
/*  987 */       .setLastMessageId(json.getLong("last_message_id", 0L))
/*  988 */       .setName(json.getString("name"))
/*  989 */       .setTopic(json.getString("topic", null))
/*  990 */       .setPosition(json.getInt("position"))
/*  991 */       .setNSFW(json.getBoolean("nsfw"))
/*  992 */       .setNews((json.getInt("type") == 5))
/*  993 */       .setSlowmode(json.getInt("rate_limit_per_user", 0));
/*      */     
/*  995 */     createOverridesPass(channel, json.getArray("permission_overwrites"));
/*  996 */     if (playbackCache)
/*  997 */       getJDA().getEventCache().playbackCache(EventCache.Type.CHANNEL, id); 
/*  998 */     return channel;
/*      */   }
/*      */ 
/*      */   
/*      */   public VoiceChannel createVoiceChannel(DataObject json, long guildId) {
/* 1003 */     return createVoiceChannel(null, json, guildId);
/*      */   }
/*      */ 
/*      */   
/*      */   public VoiceChannel createVoiceChannel(GuildImpl guild, DataObject json, long guildId) {
/* 1008 */     boolean playbackCache = false;
/* 1009 */     long id = json.getLong("id");
/* 1010 */     VoiceChannelImpl channel = (VoiceChannelImpl)getJDA().getVoiceChannelsView().get(id);
/* 1011 */     if (channel == null) {
/*      */       
/* 1013 */       if (guild == null) {
/* 1014 */         guild = (GuildImpl)getJDA().getGuildsView().get(guildId);
/*      */       }
/* 1016 */       SortedSnowflakeCacheViewImpl<VoiceChannel> sortedSnowflakeCacheViewImpl = guild.getVoiceChannelsView();
/* 1017 */       SnowflakeCacheViewImpl<VoiceChannel> voiceView = getJDA().getVoiceChannelsView();
/*      */       
/* 1019 */       UnlockHook vlock = sortedSnowflakeCacheViewImpl.writeLock(); 
/* 1020 */       try { UnlockHook jlock = voiceView.writeLock();
/*      */         
/* 1022 */         try { if (json.getInt("type") == ChannelType.STAGE.getId()) {
/* 1023 */             channel = new StageChannelImpl(id, guild);
/*      */           } else {
/* 1025 */             channel = new VoiceChannelImpl(id, guild);
/* 1026 */           }  sortedSnowflakeCacheViewImpl.getMap().put(id, channel);
/* 1027 */           playbackCache = (voiceView.getMap().put(id, channel) == null);
/* 1028 */           if (jlock != null) jlock.close();  } catch (Throwable throwable) { if (jlock != null) try { jlock.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (vlock != null) vlock.close();  } catch (Throwable throwable) { if (vlock != null)
/*      */           try { vlock.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*      */     
/* 1031 */     }  channel
/* 1032 */       .setParent(json.getLong("parent_id", 0L))
/* 1033 */       .setName(json.getString("name"))
/* 1034 */       .setPosition(json.getInt("position"))
/* 1035 */       .setUserLimit(json.getInt("user_limit"))
/* 1036 */       .setBitrate(json.getInt("bitrate"))
/* 1037 */       .setRegion(json.getString("rtc_region", null));
/*      */     
/* 1039 */     createOverridesPass(channel, json.getArray("permission_overwrites"));
/* 1040 */     if (playbackCache)
/* 1041 */       getJDA().getEventCache().playbackCache(EventCache.Type.CHANNEL, id); 
/* 1042 */     return channel;
/*      */   }
/*      */ 
/*      */   
/*      */   public PrivateChannel createPrivateChannel(DataObject json) {
/* 1047 */     long channelId = json.getUnsignedLong("id");
/* 1048 */     PrivateChannel channel = this.api.getPrivateChannelById(channelId);
/* 1049 */     this.api.usedPrivateChannel(channelId);
/* 1050 */     if (channel != null) {
/* 1051 */       return channel;
/*      */     }
/*      */ 
/*      */     
/* 1055 */     DataObject recipient = json.hasKey("recipients") ? json.getArray("recipients").getObject(0) : json.getObject("recipient");
/* 1056 */     long userId = recipient.getLong("id");
/* 1057 */     UserImpl user = (UserImpl)getJDA().getUserById(userId);
/* 1058 */     if (user == null)
/*      */     {
/*      */       
/* 1061 */       user = createUser(recipient);
/*      */     }
/*      */     
/* 1064 */     return createPrivateChannel(json, user);
/*      */   }
/*      */ 
/*      */   
/*      */   public PrivateChannel createPrivateChannel(DataObject json, UserImpl user) {
/* 1069 */     long channelId = json.getLong("id");
/*      */     
/* 1071 */     PrivateChannelImpl priv = (new PrivateChannelImpl(channelId, user)).setLastMessageId(json.getLong("last_message_id", 0L));
/* 1072 */     user.setPrivateChannel(priv);
/*      */ 
/*      */     
/* 1075 */     SnowflakeCacheViewImpl<PrivateChannel> privateView = getJDA().getPrivateChannelsView();
/* 1076 */     UnlockHook hook = privateView.writeLock();
/*      */     
/* 1078 */     try { privateView.getMap().put(channelId, priv);
/* 1079 */       if (hook != null) hook.close();  } catch (Throwable throwable) { if (hook != null)
/* 1080 */         try { hook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  this.api.usedPrivateChannel(channelId);
/* 1081 */     getJDA().getEventCache().playbackCache(EventCache.Type.CHANNEL, channelId);
/* 1082 */     return priv;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public StageInstance createStageInstance(GuildImpl guild, DataObject json) {
/* 1088 */     long channelId = json.getUnsignedLong("channel_id");
/* 1089 */     StageChannelImpl channel = (StageChannelImpl)guild.getStageChannelById(channelId);
/* 1090 */     if (channel == null) {
/* 1091 */       return null;
/*      */     }
/* 1093 */     long id = json.getUnsignedLong("id");
/* 1094 */     String topic = json.getString("topic");
/* 1095 */     boolean discoverable = !json.getBoolean("discoverable_disabled");
/* 1096 */     StageInstance.PrivacyLevel level = StageInstance.PrivacyLevel.fromKey(json.getInt("privacy_level", -1));
/*      */ 
/*      */     
/* 1099 */     StageInstanceImpl instance = (StageInstanceImpl)channel.getStageInstance();
/* 1100 */     if (instance == null) {
/*      */       
/* 1102 */       instance = new StageInstanceImpl(id, channel);
/* 1103 */       channel.setStageInstance(instance);
/*      */     } 
/*      */     
/* 1106 */     return instance
/* 1107 */       .setPrivacyLevel(level)
/* 1108 */       .setDiscoverable(discoverable)
/* 1109 */       .setTopic(topic);
/*      */   }
/*      */ 
/*      */   
/*      */   public void createOverridesPass(AbstractChannelImpl<?, ?> channel, DataArray overrides) {
/* 1114 */     for (int i = 0; i < overrides.length(); i++) {
/*      */ 
/*      */       
/*      */       try {
/* 1118 */         createPermissionOverride(overrides.getObject(i), channel);
/*      */       }
/* 1120 */       catch (NoSuchElementException e) {
/*      */ 
/*      */         
/* 1123 */         LOG.debug("{}. Ignoring PermissionOverride.", e.getMessage());
/*      */       }
/* 1125 */       catch (IllegalArgumentException e) {
/*      */ 
/*      */         
/* 1128 */         LOG.warn("{}. Ignoring PermissionOverride.", e.getMessage());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Role createRole(GuildImpl guild, DataObject roleJson, long guildId) {
/* 1135 */     boolean playbackCache = false;
/* 1136 */     long id = roleJson.getLong("id");
/* 1137 */     if (guild == null)
/* 1138 */       guild = (GuildImpl)getJDA().getGuildsView().get(guildId); 
/* 1139 */     RoleImpl role = (RoleImpl)guild.getRolesView().get(id);
/* 1140 */     if (role == null) {
/*      */       
/* 1142 */       SortedSnowflakeCacheViewImpl<Role> sortedSnowflakeCacheViewImpl = guild.getRolesView();
/* 1143 */       UnlockHook hook = sortedSnowflakeCacheViewImpl.writeLock();
/*      */       
/* 1145 */       try { role = new RoleImpl(id, guild);
/* 1146 */         playbackCache = (sortedSnowflakeCacheViewImpl.getMap().put(id, role) == null);
/* 1147 */         if (hook != null) hook.close();  } catch (Throwable throwable) { if (hook != null)
/*      */           try { hook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; } 
/* 1149 */     }  int color = roleJson.getInt("color");
/* 1150 */     role.setName(roleJson.getString("name"))
/* 1151 */       .setRawPosition(roleJson.getInt("position"))
/* 1152 */       .setRawPermissions(roleJson.getLong("permissions"))
/* 1153 */       .setManaged(roleJson.getBoolean("managed"))
/* 1154 */       .setHoisted(roleJson.getBoolean("hoist"))
/* 1155 */       .setColor((color == 0) ? 536870911 : color)
/* 1156 */       .setMentionable(roleJson.getBoolean("mentionable"))
/* 1157 */       .setTags(roleJson.optObject("tags").orElseGet(DataObject::empty));
/*      */     
/* 1159 */     String iconId = roleJson.getString("icon", null);
/* 1160 */     String emoji = roleJson.getString("unicode_emoji", null);
/* 1161 */     if (iconId == null && emoji == null) {
/* 1162 */       role.setIcon(null);
/*      */     } else {
/* 1164 */       role.setIcon(new RoleIcon(iconId, emoji, id));
/*      */     } 
/* 1166 */     if (playbackCache)
/* 1167 */       getJDA().getEventCache().playbackCache(EventCache.Type.ROLE, id); 
/* 1168 */     return role;
/*      */   }
/*      */   public Message createMessage(DataObject jsonObject) {
/* 1171 */     return createMessage(jsonObject, false);
/*      */   }
/*      */   public Message createMessage(DataObject jsonObject, boolean modifyCache) { PrivateChannel privateChannel;
/* 1174 */     long channelId = jsonObject.getLong("channel_id");
/*      */     
/* 1176 */     TextChannel textChannel = getJDA().getTextChannelById(channelId);
/* 1177 */     if (textChannel == null)
/* 1178 */       privateChannel = getJDA().getPrivateChannelById(channelId); 
/* 1179 */     if (privateChannel == null && !jsonObject.isNull("guild_id")) {
/* 1180 */       throw new IllegalArgumentException("MISSING_CHANNEL");
/*      */     }
/* 1182 */     return createMessage(jsonObject, (MessageChannel)privateChannel, modifyCache); } public ReceivedMessage createMessage(DataObject jsonObject, @Nullable MessageChannel channel, boolean modifyCache) { PrivateChannel privateChannel1;
/*      */     User user;
/*      */     Guild guild;
/*      */     ReceivedMessage message;
/* 1186 */     long channelId = jsonObject.getUnsignedLong("channel_id");
/* 1187 */     if (channel != null && channelId != channel.getIdLong()) {
/*      */       
/* 1189 */       TextChannel textChannel = this.api.getTextChannelById(channelId);
/* 1190 */       if (textChannel == null) {
/* 1191 */         privateChannel1 = this.api.getPrivateChannelById(channelId);
/*      */       }
/*      */     } 
/* 1194 */     long id = jsonObject.getLong("id");
/* 1195 */     DataObject author = jsonObject.getObject("author");
/* 1196 */     long authorId = author.getLong("id");
/* 1197 */     MemberImpl member = null;
/*      */     
/* 1199 */     if (privateChannel1 == null && jsonObject.isNull("guild_id") && authorId != getJDA().getSelfUser().getIdLong()) {
/*      */ 
/*      */ 
/*      */       
/* 1203 */       DataObject channelData = DataObject.empty().put("id", Long.valueOf(channelId)).put("recipient", author);
/* 1204 */       privateChannel1 = createPrivateChannel(channelData);
/*      */     }
/* 1206 */     else if (privateChannel1 == null) {
/* 1207 */       throw new IllegalArgumentException("MISSING_CHANNEL");
/*      */     } 
/* 1209 */     if (privateChannel1.getType().isGuild() && !jsonObject.isNull("member")) {
/*      */       
/* 1211 */       DataObject memberJson = jsonObject.getObject("member");
/* 1212 */       memberJson.put("user", author);
/* 1213 */       GuildChannel guildChannel = (GuildChannel)privateChannel1;
/* 1214 */       Guild guild1 = guildChannel.getGuild();
/* 1215 */       member = createMember((GuildImpl)guild1, memberJson);
/* 1216 */       if (modifyCache)
/*      */       {
/*      */         
/* 1219 */         updateMemberCache(member);
/*      */       }
/*      */     } 
/*      */     
/* 1223 */     String content = jsonObject.getString("content", "");
/* 1224 */     boolean fromWebhook = jsonObject.hasKey("webhook_id");
/* 1225 */     boolean pinned = jsonObject.getBoolean("pinned");
/* 1226 */     boolean tts = jsonObject.getBoolean("tts");
/* 1227 */     boolean mentionsEveryone = jsonObject.getBoolean("mention_everyone");
/* 1228 */     OffsetDateTime editTime = jsonObject.isNull("edited_timestamp") ? null : OffsetDateTime.parse(jsonObject.getString("edited_timestamp"));
/* 1229 */     String nonce = jsonObject.isNull("nonce") ? null : jsonObject.get("nonce").toString();
/* 1230 */     int flags = jsonObject.getInt("flags", 0);
/*      */     
/* 1232 */     PrivateChannel privateChannel2 = privateChannel1;
/* 1233 */     List<Message.Attachment> attachments = map(jsonObject, "attachments", this::createMessageAttachment);
/* 1234 */     List<MessageEmbed> embeds = map(jsonObject, "embeds", this::createMessageEmbed);
/* 1235 */     List<MessageReaction> reactions = map(jsonObject, "reactions", obj -> createMessageReaction(tmpChannel, id, obj));
/* 1236 */     List<MessageSticker> stickers = map(jsonObject, "sticker_items", this::createSticker);
/*      */     
/* 1238 */     MessageActivity activity = null;
/*      */     
/* 1240 */     if (!jsonObject.isNull("activity")) {
/* 1241 */       activity = createMessageActivity(jsonObject);
/*      */     }
/*      */     
/* 1244 */     switch (privateChannel1.getType()) {
/*      */       
/*      */       case null:
/* 1247 */         if (authorId == getJDA().getSelfUser().getIdLong()) {
/* 1248 */           SelfUser selfUser = getJDA().getSelfUser(); break;
/*      */         } 
/* 1250 */         user = privateChannel1.getUser();
/*      */         break;
/*      */       case null:
/* 1253 */         throw new IllegalStateException("Cannot build a message for a group channel, how did this even get here?");
/*      */       case EMBEDDED_APPLICATION:
/* 1255 */         guild = ((TextChannel)privateChannel1).getGuild();
/* 1256 */         if (member == null)
/* 1257 */           member = (MemberImpl)guild.getMemberById(authorId); 
/* 1258 */         user = (member != null) ? member.getUser() : null;
/* 1259 */         if (user == null) {
/*      */           
/* 1261 */           if (fromWebhook || !modifyCache) {
/* 1262 */             user = createUser(author); break;
/*      */           } 
/* 1264 */           throw new IllegalArgumentException("MISSING_USER");
/*      */         }  break;
/*      */       default:
/* 1267 */         throw new IllegalArgumentException("Invalid Channel for creating a Message [" + privateChannel1.getType() + ']');
/*      */     } 
/*      */     
/* 1270 */     if (modifyCache && !fromWebhook) {
/* 1271 */       updateUser((UserImpl)user, author);
/*      */     }
/* 1273 */     TLongHashSet tLongHashSet1 = new TLongHashSet();
/* 1274 */     TLongHashSet tLongHashSet2 = new TLongHashSet(map(jsonObject, "mentions", o -> Long.valueOf(o.getLong("id"))));
/* 1275 */     Optional<DataArray> roleMentionArr = jsonObject.optArray("mention_roles");
/* 1276 */     roleMentionArr.ifPresent(arr -> {
/*      */           for (int i = 0; i < arr.length(); i++) {
/*      */             mentionedRoles.add(arr.getLong(i));
/*      */           }
/*      */         });
/*      */     
/* 1282 */     MessageType type = MessageType.fromId(jsonObject.getInt("type"));
/*      */     
/* 1284 */     Message referencedMessage = null;
/* 1285 */     if (!jsonObject.isNull("referenced_message")) {
/*      */       
/* 1287 */       DataObject referenceJson = jsonObject.getObject("referenced_message");
/*      */       
/*      */       try {
/* 1290 */         referencedMessage = createMessage(referenceJson, (MessageChannel)privateChannel1, false);
/*      */       }
/* 1292 */       catch (IllegalArgumentException ex) {
/*      */ 
/*      */         
/* 1295 */         if ("UNKNOWN_MESSAGE_TYPE".equals(ex.getMessage())) {
/* 1296 */           LOG.debug("Received referenced message with unknown type. Type: {}", Integer.valueOf(referenceJson.getInt("type", -1)));
/* 1297 */         } else if ("MISSING_CHANNEL".equals(ex.getMessage())) {
/* 1298 */           LOG.debug("Received referenced message with unknown channel. channel_id: {} Type: {}", 
/* 1299 */               Long.valueOf(referenceJson.getUnsignedLong("channel_id", 0L)), Integer.valueOf(referenceJson.getInt("type", -1)));
/*      */         } else {
/* 1301 */           throw ex;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1305 */     MessageReference messageReference = null;
/*      */     
/* 1307 */     if (!jsonObject.isNull("message_reference")) {
/*      */       
/* 1309 */       DataObject messageReferenceJson = jsonObject.getObject("message_reference");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1314 */       messageReference = new MessageReference(messageReferenceJson.getLong("message_id", 0L), messageReferenceJson.getLong("channel_id", 0L), messageReferenceJson.getLong("guild_id", 0L), referencedMessage, (JDA)this.api);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1320 */     List<ActionRow> components = Collections.emptyList();
/* 1321 */     Optional<DataArray> componentsArrayOpt = jsonObject.optArray("components");
/* 1322 */     if (componentsArrayOpt.isPresent()) {
/*      */       
/* 1324 */       DataArray array = componentsArrayOpt.get();
/*      */ 
/*      */ 
/*      */       
/* 1328 */       components = (List<ActionRow>)array.stream(DataArray::getObject).filter(it -> (it.getInt("type", 0) == 1)).map(ActionRow::fromData).collect(Collectors.toList());
/*      */     } 
/*      */     
/* 1331 */     Message.Interaction messageInteraction = null;
/* 1332 */     if (!jsonObject.isNull("interaction")) {
/*      */       
/* 1334 */       GuildImpl guildImpl1 = null;
/* 1335 */       if (privateChannel1 instanceof GuildChannel)
/*      */       {
/* 1337 */         guildImpl1 = (GuildImpl)((GuildChannel)privateChannel1).getGuild();
/*      */       }
/* 1339 */       messageInteraction = createMessageInteraction(guildImpl1, jsonObject.getObject("interaction"));
/*      */     } 
/*      */     
/* 1342 */     if (type == MessageType.UNKNOWN)
/* 1343 */       throw new IllegalArgumentException("UNKNOWN_MESSAGE_TYPE"); 
/* 1344 */     if (!type.isSystem()) {
/*      */       
/* 1346 */       message = new ReceivedMessage(id, (MessageChannel)privateChannel1, type, messageReference, fromWebhook, mentionsEveryone, (TLongSet)tLongHashSet2, (TLongSet)tLongHashSet1, tts, pinned, content, nonce, user, member, activity, editTime, reactions, attachments, embeds, stickers, components, flags, messageInteraction);
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 1352 */       message = new SystemMessage(id, (MessageChannel)privateChannel1, type, messageReference, fromWebhook, mentionsEveryone, (TLongSet)tLongHashSet2, (TLongSet)tLongHashSet1, tts, pinned, content, nonce, user, member, activity, editTime, reactions, attachments, embeds, stickers, flags);
/*      */ 
/*      */       
/* 1355 */       return message;
/*      */     } 
/*      */     
/* 1358 */     GuildImpl guildImpl = message.isFromGuild() ? (GuildImpl)message.getGuild() : null;
/*      */ 
/*      */     
/* 1361 */     List<User> mentionedUsersList = new ArrayList<>();
/* 1362 */     List<Member> mentionedMembersList = new ArrayList<>();
/* 1363 */     DataArray userMentions = jsonObject.getArray("mentions");
/*      */     
/* 1365 */     for (int i = 0; i < userMentions.length(); i++) {
/*      */       
/* 1367 */       DataObject mentionJson = userMentions.getObject(i);
/* 1368 */       if (guildImpl == null || mentionJson.isNull("member")) {
/*      */ 
/*      */         
/* 1371 */         User mentionedUser = createUser(mentionJson);
/* 1372 */         mentionedUsersList.add(mentionedUser);
/* 1373 */         if (guildImpl != null)
/*      */         {
/* 1375 */           Member mentionedMember = guildImpl.getMember(mentionedUser);
/* 1376 */           if (mentionedMember != null) {
/* 1377 */             mentionedMembersList.add(mentionedMember);
/*      */           }
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1383 */         DataObject memberJson = mentionJson.getObject("member");
/* 1384 */         mentionJson.remove("member");
/* 1385 */         memberJson.put("user", mentionJson);
/* 1386 */         Member mentionedMember = createMember(guildImpl, memberJson);
/* 1387 */         mentionedMembersList.add(mentionedMember);
/* 1388 */         mentionedUsersList.add(mentionedMember.getUser());
/*      */       } 
/*      */     } 
/* 1391 */     message.setMentions(mentionedUsersList, mentionedMembersList);
/* 1392 */     return message; }
/*      */ 
/*      */ 
/*      */   
/*      */   private static MessageActivity createMessageActivity(DataObject jsonObject) {
/* 1397 */     DataObject activityData = jsonObject.getObject("activity");
/* 1398 */     MessageActivity.ActivityType activityType = MessageActivity.ActivityType.fromId(activityData.getInt("type"));
/* 1399 */     String partyId = activityData.getString("party_id", null);
/* 1400 */     MessageActivity.Application application = null;
/*      */     
/* 1402 */     if (!jsonObject.isNull("application")) {
/*      */       
/* 1404 */       DataObject applicationData = jsonObject.getObject("application");
/*      */       
/* 1406 */       String name = applicationData.getString("name");
/* 1407 */       String description = applicationData.getString("description", "");
/* 1408 */       String iconId = applicationData.getString("icon", null);
/* 1409 */       String coverId = applicationData.getString("cover_image", null);
/* 1410 */       long applicationId = applicationData.getLong("id");
/*      */       
/* 1412 */       application = new MessageActivity.Application(name, description, iconId, coverId, applicationId);
/*      */     } 
/* 1414 */     if (activityType == MessageActivity.ActivityType.UNKNOWN)
/*      */     {
/* 1416 */       LOG.debug("Received an unknown ActivityType, Activity: {}", activityData);
/*      */     }
/*      */     
/* 1419 */     return new MessageActivity(activityType, partyId, application);
/*      */   }
/*      */   
/*      */   public MessageReaction createMessageReaction(MessageChannel chan, long id, DataObject obj) {
/*      */     MessageReaction.ReactionEmote reactionEmote;
/* 1424 */     DataObject emoji = obj.getObject("emoji");
/* 1425 */     Long emojiID = emoji.isNull("id") ? null : Long.valueOf(emoji.getLong("id"));
/* 1426 */     String name = emoji.getString("name", "");
/* 1427 */     boolean animated = emoji.getBoolean("animated");
/* 1428 */     int count = obj.getInt("count", -1);
/* 1429 */     boolean me = obj.getBoolean("me");
/*      */ 
/*      */     
/* 1432 */     if (emojiID != null) {
/*      */       EmoteImpl emoteImpl;
/* 1434 */       Emote emote = getJDA().getEmoteById(emojiID.longValue());
/*      */       
/* 1436 */       if (emote == null)
/* 1437 */         emoteImpl = (new EmoteImpl(emojiID.longValue(), getJDA())).setAnimated(animated).setName(name); 
/* 1438 */       reactionEmote = MessageReaction.ReactionEmote.fromCustom((Emote)emoteImpl);
/*      */     }
/*      */     else {
/*      */       
/* 1442 */       reactionEmote = MessageReaction.ReactionEmote.fromUnicode(name, (JDA)getJDA());
/*      */     } 
/*      */     
/* 1445 */     return new MessageReaction(chan, reactionEmote, id, me, count);
/*      */   }
/*      */ 
/*      */   
/*      */   public Message.Attachment createMessageAttachment(DataObject jsonObject) {
/* 1450 */     boolean ephemeral = jsonObject.getBoolean("ephemeral", false);
/* 1451 */     int width = jsonObject.getInt("width", -1);
/* 1452 */     int height = jsonObject.getInt("height", -1);
/* 1453 */     int size = jsonObject.getInt("size");
/* 1454 */     String url = jsonObject.getString("url");
/* 1455 */     String proxyUrl = jsonObject.getString("proxy_url");
/* 1456 */     String filename = jsonObject.getString("filename");
/* 1457 */     String contentType = jsonObject.getString("content_type", null);
/* 1458 */     long id = jsonObject.getLong("id");
/* 1459 */     return new Message.Attachment(id, url, proxyUrl, filename, contentType, size, height, width, ephemeral, getJDA()); } public MessageEmbed createMessageEmbed(DataObject content) { MessageEmbed.Thumbnail thumbnail; MessageEmbed.Provider provider;
/*      */     MessageEmbed.AuthorInfo author;
/*      */     MessageEmbed.VideoInfo video;
/*      */     MessageEmbed.Footer footer;
/*      */     MessageEmbed.ImageInfo image;
/* 1464 */     if (content.isNull("type"))
/* 1465 */       throw new IllegalStateException("Encountered embed object with missing/null type field for Json: " + content); 
/* 1466 */     EmbedType type = EmbedType.fromKey(content.getString("type"));
/* 1467 */     String url = content.getString("url", null);
/* 1468 */     String title = content.getString("title", null);
/* 1469 */     String description = content.getString("description", null);
/* 1470 */     OffsetDateTime timestamp = content.isNull("timestamp") ? null : OffsetDateTime.parse(content.getString("timestamp"));
/* 1471 */     int color = content.isNull("color") ? 536870911 : content.getInt("color");
/*      */ 
/*      */     
/* 1474 */     if (content.isNull("thumbnail")) {
/*      */       
/* 1476 */       thumbnail = null;
/*      */     }
/*      */     else {
/*      */       
/* 1480 */       DataObject obj = content.getObject("thumbnail");
/*      */ 
/*      */ 
/*      */       
/* 1484 */       thumbnail = new MessageEmbed.Thumbnail(obj.getString("url", null), obj.getString("proxy_url", null), obj.getInt("width", -1), obj.getInt("height", -1));
/*      */     } 
/*      */ 
/*      */     
/* 1488 */     if (content.isNull("provider")) {
/*      */       
/* 1490 */       provider = null;
/*      */     }
/*      */     else {
/*      */       
/* 1494 */       DataObject obj = content.getObject("provider");
/*      */       
/* 1496 */       provider = new MessageEmbed.Provider(obj.getString("name", null), obj.getString("url", null));
/*      */     } 
/*      */ 
/*      */     
/* 1500 */     if (content.isNull("author")) {
/*      */       
/* 1502 */       author = null;
/*      */     }
/*      */     else {
/*      */       
/* 1506 */       DataObject obj = content.getObject("author");
/*      */ 
/*      */ 
/*      */       
/* 1510 */       author = new MessageEmbed.AuthorInfo(obj.getString("name", null), obj.getString("url", null), obj.getString("icon_url", null), obj.getString("proxy_icon_url", null));
/*      */     } 
/*      */ 
/*      */     
/* 1514 */     if (content.isNull("video")) {
/*      */       
/* 1516 */       video = null;
/*      */     }
/*      */     else {
/*      */       
/* 1520 */       DataObject obj = content.getObject("video");
/*      */ 
/*      */       
/* 1523 */       video = new MessageEmbed.VideoInfo(obj.getString("url", null), obj.getInt("width", -1), obj.getInt("height", -1));
/*      */     } 
/*      */ 
/*      */     
/* 1527 */     if (content.isNull("footer")) {
/*      */       
/* 1529 */       footer = null;
/*      */     }
/*      */     else {
/*      */       
/* 1533 */       DataObject obj = content.getObject("footer");
/*      */ 
/*      */       
/* 1536 */       footer = new MessageEmbed.Footer(obj.getString("text", null), obj.getString("icon_url", null), obj.getString("proxy_icon_url", null));
/*      */     } 
/*      */ 
/*      */     
/* 1540 */     if (content.isNull("image")) {
/*      */       
/* 1542 */       image = null;
/*      */     }
/*      */     else {
/*      */       
/* 1546 */       DataObject obj = content.getObject("image");
/*      */ 
/*      */ 
/*      */       
/* 1550 */       image = new MessageEmbed.ImageInfo(obj.getString("url", null), obj.getString("proxy_url", null), obj.getInt("width", -1), obj.getInt("height", -1));
/*      */     } 
/*      */     
/* 1553 */     List<MessageEmbed.Field> fields = map(content, "fields", obj -> new MessageEmbed.Field(obj.getString("name", null), obj.getString("value", null), obj.getBoolean("inline"), false));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1560 */     return createMessageEmbed(url, title, description, type, timestamp, color, thumbnail, provider, author, video, footer, image, fields); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MessageEmbed createMessageEmbed(String url, String title, String description, EmbedType type, OffsetDateTime timestamp, int color, MessageEmbed.Thumbnail thumbnail, MessageEmbed.Provider siteProvider, MessageEmbed.AuthorInfo author, MessageEmbed.VideoInfo videoInfo, MessageEmbed.Footer footer, MessageEmbed.ImageInfo image, List<MessageEmbed.Field> fields) {
/* 1568 */     return new MessageEmbed(url, title, description, type, timestamp, color, thumbnail, siteProvider, author, videoInfo, footer, image, fields);
/*      */   }
/*      */ 
/*      */   
/*      */   public MessageSticker createSticker(DataObject content) {
/*      */     Set<String> tags;
/* 1574 */     long id = content.getLong("id");
/* 1575 */     String name = content.getString("name");
/* 1576 */     String description = content.getString("description", "");
/* 1577 */     long packId = content.getLong("pack_id", content.getLong("guild_id", 0L));
/* 1578 */     String asset = content.getString("asset", "");
/* 1579 */     MessageSticker.StickerFormat format = MessageSticker.StickerFormat.fromId(content.getInt("format_type"));
/*      */     
/* 1581 */     if (content.isNull("tags")) {
/*      */       
/* 1583 */       tags = Collections.emptySet();
/*      */     }
/*      */     else {
/*      */       
/* 1587 */       String[] split = content.getString("tags").split(", ");
/* 1588 */       Set<String> tmp = new HashSet<>(Arrays.asList(split));
/* 1589 */       tags = Collections.unmodifiableSet(tmp);
/*      */     } 
/* 1591 */     return new MessageSticker(id, name, description, packId, asset, format, tags);
/*      */   }
/*      */ 
/*      */   
/*      */   public Message.Interaction createMessageInteraction(GuildImpl guildImpl, DataObject content) {
/* 1596 */     long id = content.getLong("id");
/* 1597 */     int type = content.getInt("type");
/* 1598 */     String name = content.getString("name");
/* 1599 */     DataObject userJson = content.getObject("user");
/* 1600 */     User user = null;
/* 1601 */     MemberImpl member = null;
/* 1602 */     if (!content.isNull("member") && guildImpl != null) {
/*      */       
/* 1604 */       DataObject memberJson = content.getObject("member");
/* 1605 */       memberJson.put("user", userJson);
/* 1606 */       member = createMember(guildImpl, memberJson);
/* 1607 */       user = member.getUser();
/*      */     }
/*      */     else {
/*      */       
/* 1611 */       user = createUser(userJson);
/*      */     } 
/*      */     
/* 1614 */     return new Message.Interaction(id, type, name, user, member);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public PermissionOverride createPermissionOverride(DataObject override, AbstractChannelImpl<?, ?> chan) {
/* 1620 */     int type = override.getInt("type");
/* 1621 */     long id = override.getLong("id");
/* 1622 */     boolean role = (type == 0);
/* 1623 */     if (role && chan.getGuild().getRoleById(id) == null)
/* 1624 */       throw new NoSuchElementException("Attempted to create a PermissionOverride for a non-existent role! JSON: " + override); 
/* 1625 */     if (!role && type != 1)
/* 1626 */       throw new IllegalArgumentException("Provided with an unknown PermissionOverride type! JSON: " + override); 
/* 1627 */     if (!role && id != this.api.getSelfUser().getIdLong() && !this.api.isCacheFlagSet(CacheFlag.MEMBER_OVERRIDES)) {
/* 1628 */       return null;
/*      */     }
/* 1630 */     long allow = override.getLong("allow");
/* 1631 */     long deny = override.getLong("deny");
/*      */     
/* 1633 */     if (id == chan.getGuild().getIdLong() && (allow | deny) == 0L) {
/* 1634 */       return null;
/*      */     }
/* 1636 */     PermissionOverrideImpl permOverride = (PermissionOverrideImpl)chan.getOverrideMap().get(id);
/* 1637 */     if (permOverride == null) {
/*      */       
/* 1639 */       permOverride = new PermissionOverrideImpl(chan, id, role);
/* 1640 */       chan.getOverrideMap().put(id, permOverride);
/*      */     } 
/*      */     
/* 1643 */     return permOverride.setAllow(allow).setDeny(deny);
/*      */   }
/*      */ 
/*      */   
/*      */   public WebhookImpl createWebhook(DataObject object) {
/* 1648 */     return createWebhook(object, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public WebhookImpl createWebhook(DataObject object, boolean allowMissingChannel) {
/* 1653 */     long id = object.getLong("id");
/* 1654 */     long guildId = object.getUnsignedLong("guild_id");
/* 1655 */     long channelId = object.getUnsignedLong("channel_id");
/* 1656 */     String token = object.getString("token", null);
/* 1657 */     WebhookType type = WebhookType.fromKey(object.getInt("type", -1));
/*      */     
/* 1659 */     TextChannel channel = getJDA().getTextChannelById(channelId);
/* 1660 */     if (channel == null && !allowMissingChannel)
/* 1661 */       throw new NullPointerException(String.format("Tried to create Webhook for an un-cached TextChannel! WebhookId: %s ChannelId: %s GuildId: %s", new Object[] {
/* 1662 */               Long.valueOf(id), Long.valueOf(channelId), Long.valueOf(guildId)
/*      */             })); 
/* 1664 */     Object name = !object.isNull("name") ? object.get("name") : null;
/* 1665 */     Object avatar = !object.isNull("avatar") ? object.get("avatar") : null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1671 */     DataObject fakeUser = DataObject.empty().put("username", name).put("discriminator", "0000").put("id", Long.valueOf(id)).put("avatar", avatar);
/* 1672 */     User defaultUser = createUser(fakeUser);
/*      */     
/* 1674 */     Optional<DataObject> ownerJson = object.optObject("user");
/* 1675 */     User owner = null;
/*      */     
/* 1677 */     if (ownerJson.isPresent()) {
/*      */       
/* 1679 */       DataObject json = ownerJson.get();
/* 1680 */       long userId = json.getLong("id");
/*      */       
/* 1682 */       owner = getJDA().getUserById(userId);
/* 1683 */       if (owner == null) {
/*      */         
/* 1685 */         json.put("id", Long.valueOf(userId));
/* 1686 */         owner = createUser(json);
/*      */       } 
/*      */     } 
/*      */     
/* 1690 */     Member ownerMember = (owner == null || channel == null) ? null : channel.getGuild().getMember(owner);
/*      */ 
/*      */ 
/*      */     
/* 1694 */     WebhookImpl webhook = (new WebhookImpl(channel, (JDA)getJDA(), id, type)).setToken(token).setOwner(ownerMember, owner).setUser(defaultUser);
/*      */     
/* 1696 */     if (!object.isNull("source_channel")) {
/*      */       
/* 1698 */       DataObject source = object.getObject("source_channel");
/* 1699 */       webhook.setSourceChannel(new Webhook.ChannelReference(source.getUnsignedLong("id"), source.getString("name")));
/*      */     } 
/* 1701 */     if (!object.isNull("source_guild")) {
/*      */       
/* 1703 */       DataObject source = object.getObject("source_guild");
/* 1704 */       webhook.setSourceGuild(new Webhook.GuildReference(source.getUnsignedLong("id"), source.getString("name")));
/*      */     } 
/*      */     
/* 1707 */     return webhook; } public Invite createInvite(DataObject object) { Invite.InviteType type; Invite.Guild guild; Invite.Channel channel; Invite.Group group; Invite.InviteTarget target; DataObject applicationObject; int maxAge; Invite.EmbeddedApplication application; int maxUses; DataObject targetUserObject;
/*      */     boolean temporary;
/*      */     OffsetDateTime timeCreated;
/*      */     int uses;
/*      */     boolean expanded;
/* 1712 */     String code = object.getString("code");
/* 1713 */     User inviter = object.hasKey("inviter") ? createUser(object.getObject("inviter")) : null;
/*      */     
/* 1715 */     DataObject channelObject = object.getObject("channel");
/* 1716 */     ChannelType channelType = ChannelType.fromId(channelObject.getInt("type"));
/* 1717 */     Invite.TargetType targetType = Invite.TargetType.fromId(object.getInt("target_type", 0));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1725 */     if (channelType == ChannelType.GROUP) {
/*      */       List<String> usernames;
/* 1727 */       type = Invite.InviteType.GROUP;
/* 1728 */       guild = null;
/* 1729 */       channel = null;
/*      */       
/* 1731 */       String groupName = channelObject.getString("name", "");
/* 1732 */       long groupId = channelObject.getLong("id");
/* 1733 */       String groupIconId = channelObject.getString("icon", null);
/*      */ 
/*      */       
/* 1736 */       if (channelObject.isNull("recipients")) {
/* 1737 */         usernames = null;
/*      */       } else {
/* 1739 */         usernames = map(channelObject, "recipients", json -> json.getString("username"));
/*      */       } 
/* 1741 */       group = new InviteImpl.GroupImpl(groupIconId, groupName, groupId, usernames);
/*      */     }
/* 1743 */     else if (channelType.isGuild()) {
/*      */       Set<String> guildFeatures;
/* 1745 */       type = Invite.InviteType.GUILD;
/*      */       
/* 1747 */       DataObject guildObject = object.getObject("guild");
/*      */       
/* 1749 */       String guildIconId = guildObject.getString("icon", null);
/* 1750 */       long guildId = guildObject.getLong("id");
/* 1751 */       String guildName = guildObject.getString("name");
/* 1752 */       String guildSplashId = guildObject.getString("splash", null);
/* 1753 */       Guild.VerificationLevel guildVerificationLevel = Guild.VerificationLevel.fromKey(guildObject.getInt("verification_level", -1));
/* 1754 */       int presenceCount = object.getInt("approximate_presence_count", -1);
/* 1755 */       int memberCount = object.getInt("approximate_member_count", -1);
/*      */ 
/*      */       
/* 1758 */       if (guildObject.isNull("features")) {
/* 1759 */         guildFeatures = Collections.emptySet();
/*      */       } else {
/* 1761 */         guildFeatures = Collections.unmodifiableSet((Set<? extends String>)StreamSupport.stream(guildObject.getArray("features").spliterator(), false).map(String::valueOf).collect(Collectors.toSet()));
/*      */       } 
/* 1763 */       guild = new InviteImpl.GuildImpl(guildId, guildIconId, guildName, guildSplashId, guildVerificationLevel, presenceCount, memberCount, guildFeatures);
/*      */       
/* 1765 */       String channelName = channelObject.getString("name");
/* 1766 */       long channelId = channelObject.getLong("id");
/*      */       
/* 1768 */       channel = new InviteImpl.ChannelImpl(channelId, channelName, channelType);
/* 1769 */       group = null;
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 1775 */       type = Invite.InviteType.UNKNOWN;
/* 1776 */       guild = null;
/* 1777 */       channel = null;
/* 1778 */       group = null;
/*      */     } 
/*      */     
/* 1781 */     switch (targetType) {
/*      */       
/*      */       case EMBEDDED_APPLICATION:
/* 1784 */         applicationObject = object.getObject("target_application");
/*      */ 
/*      */ 
/*      */         
/* 1788 */         application = new InviteImpl.EmbeddedApplicationImpl(applicationObject.getString("icon", null), applicationObject.getString("name"), applicationObject.getString("description"), applicationObject.getString("summary"), applicationObject.getLong("id"), applicationObject.getInt("max_participants", -1));
/*      */         
/* 1790 */         target = new InviteImpl.InviteTargetImpl(targetType, application, null);
/*      */         break;
/*      */       case STREAM:
/* 1793 */         targetUserObject = object.getObject("target_user");
/* 1794 */         target = new InviteImpl.InviteTargetImpl(targetType, null, createUser(targetUserObject));
/*      */         break;
/*      */       case NONE:
/* 1797 */         target = null;
/*      */         break;
/*      */       default:
/* 1800 */         target = new InviteImpl.InviteTargetImpl(targetType, null, null);
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1810 */     if (object.hasKey("max_uses")) {
/*      */       
/* 1812 */       expanded = true;
/* 1813 */       maxAge = object.getInt("max_age");
/* 1814 */       maxUses = object.getInt("max_uses");
/* 1815 */       uses = object.getInt("uses");
/* 1816 */       temporary = object.getBoolean("temporary");
/* 1817 */       timeCreated = OffsetDateTime.parse(object.getString("created_at"));
/*      */     }
/*      */     else {
/*      */       
/* 1821 */       expanded = false;
/* 1822 */       maxAge = -1;
/* 1823 */       maxUses = -1;
/* 1824 */       uses = -1;
/* 1825 */       temporary = false;
/* 1826 */       timeCreated = null;
/*      */     } 
/*      */     
/* 1829 */     return new InviteImpl(getJDA(), code, expanded, inviter, maxAge, maxUses, temporary, timeCreated, uses, channel, guild, group, target, type); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Template createTemplate(DataObject object) {
/* 1836 */     String code = object.getString("code");
/* 1837 */     String name = object.getString("name");
/* 1838 */     String description = object.getString("description", null);
/* 1839 */     int uses = object.getInt("usage_count");
/* 1840 */     User creator = createUser(object.getObject("creator"));
/* 1841 */     OffsetDateTime createdAt = OffsetDateTime.parse(object.getString("created_at"));
/* 1842 */     OffsetDateTime updatedAt = OffsetDateTime.parse(object.getString("updated_at"));
/* 1843 */     long guildId = object.getLong("source_guild_id");
/* 1844 */     DataObject guildObject = object.getObject("serialized_source_guild");
/* 1845 */     String guildName = guildObject.getString("name");
/* 1846 */     String guildDescription = guildObject.getString("description", null);
/* 1847 */     String region = guildObject.getString("region", null);
/* 1848 */     String guildIconId = guildObject.getString("icon_hash", null);
/* 1849 */     Guild.VerificationLevel guildVerificationLevel = Guild.VerificationLevel.fromKey(guildObject.getInt("verification_level", -1));
/* 1850 */     Guild.NotificationLevel notificationLevel = Guild.NotificationLevel.fromKey(guildObject.getInt("default_message_notifications", 0));
/* 1851 */     Guild.ExplicitContentLevel explicitContentLevel = Guild.ExplicitContentLevel.fromKey(guildObject.getInt("explicit_content_filter", 0));
/* 1852 */     Locale locale = Locale.forLanguageTag(guildObject.getString("preferred_locale", "en"));
/* 1853 */     Guild.Timeout afkTimeout = Guild.Timeout.fromKey(guildObject.getInt("afk_timeout", 0));
/* 1854 */     DataArray roleArray = guildObject.getArray("roles");
/* 1855 */     DataArray channelsArray = guildObject.getArray("channels");
/* 1856 */     long afkChannelId = guildObject.getLong("afk_channel_id", -1L);
/* 1857 */     long systemChannelId = guildObject.getLong("system_channel_id", -1L);
/*      */     
/* 1859 */     List<TemplateRole> roles = new ArrayList<>();
/* 1860 */     for (int i = 0; i < roleArray.length(); i++) {
/*      */       
/* 1862 */       DataObject obj = roleArray.getObject(i);
/* 1863 */       long roleId = obj.getLong("id");
/* 1864 */       String roleName = obj.getString("name");
/* 1865 */       int roleColor = obj.getInt("color");
/* 1866 */       boolean hoisted = obj.getBoolean("hoist");
/* 1867 */       boolean mentionable = obj.getBoolean("mentionable");
/* 1868 */       long rawPermissions = obj.getLong("permissions");
/* 1869 */       roles.add(new TemplateRole(roleId, roleName, (roleColor == 0) ? 536870911 : roleColor, hoisted, mentionable, rawPermissions));
/*      */     } 
/*      */     
/* 1872 */     List<TemplateChannel> channels = new ArrayList<>();
/* 1873 */     for (int j = 0; j < channelsArray.length(); j++) {
/*      */       
/* 1875 */       DataObject obj = channelsArray.getObject(j);
/* 1876 */       long channelId = obj.getLong("id");
/* 1877 */       int type = obj.getInt("type");
/* 1878 */       ChannelType channelType = ChannelType.fromId(type);
/* 1879 */       String channelName = obj.getString("name");
/* 1880 */       String topic = obj.getString("topic", null);
/* 1881 */       int rawPosition = obj.getInt("position");
/* 1882 */       long parentId = obj.getLong("parent_id", -1L);
/*      */       
/* 1884 */       boolean nsfw = obj.getBoolean("nsfw");
/* 1885 */       int slowmode = obj.getInt("rate_limit_per_user");
/*      */       
/* 1887 */       int bitrate = obj.getInt("bitrate");
/* 1888 */       int userLimit = obj.getInt("user_limit");
/*      */       
/* 1890 */       List<TemplateChannel.PermissionOverride> permissionOverrides = new ArrayList<>();
/* 1891 */       DataArray overrides = obj.getArray("permission_overwrites");
/*      */       
/* 1893 */       for (int k = 0; k < overrides.length(); k++) {
/*      */         
/* 1895 */         DataObject overrideObj = overrides.getObject(k);
/* 1896 */         long overrideId = overrideObj.getLong("id");
/* 1897 */         long allow = overrideObj.getLong("allow");
/* 1898 */         long deny = overrideObj.getLong("deny");
/* 1899 */         permissionOverrides.add(new TemplateChannel.PermissionOverride(overrideId, allow, deny));
/*      */       } 
/* 1901 */       channels.add(new TemplateChannel(channelId, channelType, channelName, topic, rawPosition, parentId, (type == 5), permissionOverrides, nsfw, slowmode, bitrate, userLimit));
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1906 */     TemplateChannel afkChannel = channels.stream().filter(templateChannel -> (templateChannel.getIdLong() == afkChannelId)).findFirst().orElse(null);
/*      */     
/* 1908 */     TemplateChannel systemChannel = channels.stream().filter(templateChannel -> (templateChannel.getIdLong() == systemChannelId)).findFirst().orElse(null);
/*      */     
/* 1910 */     TemplateGuild guild = new TemplateGuild(guildId, guildName, guildDescription, region, guildIconId, guildVerificationLevel, notificationLevel, explicitContentLevel, locale, afkTimeout, afkChannel, systemChannel, roles, channels);
/*      */ 
/*      */     
/* 1913 */     boolean synced = !object.getBoolean("is_dirty", false);
/*      */     
/* 1915 */     return new Template(getJDA(), code, name, description, uses, creator, createdAt, updatedAt, guild, synced);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ApplicationInfo createApplicationInfo(DataObject object) {
/* 1922 */     String description = object.getString("description");
/* 1923 */     String termsOfServiceUrl = object.getString("terms_of_service_url", null);
/* 1924 */     String privacyPolicyUrl = object.getString("privacy_policy_url", null);
/* 1925 */     boolean doesBotRequireCodeGrant = object.getBoolean("bot_require_code_grant");
/* 1926 */     String iconId = object.getString("icon", null);
/* 1927 */     long id = object.getLong("id");
/* 1928 */     String name = object.getString("name");
/* 1929 */     boolean isBotPublic = object.getBoolean("bot_public");
/* 1930 */     User owner = createUser(object.getObject("owner"));
/* 1931 */     ApplicationTeam team = !object.isNull("team") ? createApplicationTeam(object.getObject("team")) : null;
/*      */     
/* 1933 */     return new ApplicationInfoImpl((JDA)getJDA(), description, doesBotRequireCodeGrant, iconId, id, isBotPublic, name, termsOfServiceUrl, privacyPolicyUrl, owner, team);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ApplicationTeam createApplicationTeam(DataObject object) {
/* 1939 */     String iconId = object.getString("icon", null);
/* 1940 */     long id = object.getUnsignedLong("id");
/* 1941 */     long ownerId = object.getUnsignedLong("owner_user_id", 0L);
/* 1942 */     List<TeamMember> members = map(object, "members", o -> {
/*      */           DataObject userJson = o.getObject("user");
/*      */           TeamMember.MembershipState state = TeamMember.MembershipState.fromKey(o.getInt("membership_state"));
/*      */           User user = createUser(userJson);
/*      */           return new TeamMemberImpl(user, state, id);
/*      */         });
/* 1948 */     return new ApplicationTeamImpl(iconId, members, id, ownerId);
/*      */   }
/*      */   
/*      */   public AuditLogEntry createAuditLogEntry(GuildImpl guild, DataObject entryJson, DataObject userJson, DataObject webhookJson) {
/*      */     Set<AuditLogChange> changesList;
/* 1953 */     long targetId = entryJson.getLong("target_id", 0L);
/* 1954 */     long id = entryJson.getLong("id");
/* 1955 */     int typeKey = entryJson.getInt("action_type");
/* 1956 */     DataArray changes = entryJson.isNull("changes") ? null : entryJson.getArray("changes");
/* 1957 */     DataObject options = entryJson.isNull("options") ? null : entryJson.getObject("options");
/* 1958 */     String reason = entryJson.getString("reason", null);
/*      */     
/* 1960 */     UserImpl user = (userJson == null) ? null : createUser(userJson);
/* 1961 */     WebhookImpl webhook = (webhookJson == null) ? null : createWebhook(webhookJson);
/*      */     
/* 1963 */     ActionType type = ActionType.from(typeKey);
/*      */     
/* 1965 */     if (changes != null) {
/*      */       
/* 1967 */       changesList = new HashSet<>(changes.length());
/* 1968 */       for (int i = 0; i < changes.length(); i++)
/*      */       {
/* 1970 */         DataObject object = changes.getObject(i);
/* 1971 */         AuditLogChange change = createAuditLogChange(object);
/* 1972 */         changesList.add(change);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1977 */       changesList = Collections.emptySet();
/*      */     } 
/*      */     
/* 1980 */     CaseInsensitiveMap<String, AuditLogChange> changeMap = new CaseInsensitiveMap(changeToMap(changesList));
/*      */     
/* 1982 */     CaseInsensitiveMap<String, Object> optionMap = (options != null) ? new CaseInsensitiveMap(options.toMap()) : null;
/*      */     
/* 1984 */     return new AuditLogEntry(type, typeKey, id, targetId, guild, user, webhook, reason, (Map)changeMap, (Map)optionMap);
/*      */   }
/*      */ 
/*      */   
/*      */   public AuditLogChange createAuditLogChange(DataObject change) {
/* 1989 */     String key = change.getString("key");
/* 1990 */     Object oldValue = change.isNull("old_value") ? null : change.get("old_value");
/* 1991 */     Object newValue = change.isNull("new_value") ? null : change.get("new_value");
/* 1992 */     return new AuditLogChange(oldValue, newValue, key);
/*      */   }
/*      */ 
/*      */   
/*      */   private Map<String, AuditLogChange> changeToMap(Set<AuditLogChange> changesList) {
/* 1997 */     return (Map<String, AuditLogChange>)changesList.stream().collect(Collectors.toMap(AuditLogChange::getKey, UnaryOperator.identity()));
/*      */   }
/*      */ 
/*      */   
/*      */   private <T> List<T> map(DataObject jsonObject, String key, Function<DataObject, T> convert) {
/* 2002 */     if (jsonObject.isNull(key)) {
/* 2003 */       return Collections.emptyList();
/*      */     }
/* 2005 */     DataArray arr = jsonObject.getArray(key);
/* 2006 */     List<T> mappedObjects = new ArrayList<>(arr.length());
/* 2007 */     for (int i = 0; i < arr.length(); i++) {
/*      */       
/* 2009 */       DataObject obj = arr.getObject(i);
/* 2010 */       T result = convert.apply(obj);
/* 2011 */       if (result != null) {
/* 2012 */         mappedObjects.add(result);
/*      */       }
/*      */     } 
/* 2015 */     return mappedObjects;
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\EntityBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */