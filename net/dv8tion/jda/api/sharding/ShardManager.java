/*      */ package net.dv8tion.jda.api.sharding;
/*      */ 
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.IntFunction;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import javax.annotation.CheckReturnValue;
/*      */ import javax.annotation.Nonnull;
/*      */ import javax.annotation.Nullable;
/*      */ import javax.security.auth.login.LoginException;
/*      */ import net.dv8tion.jda.annotations.DeprecatedSince;
/*      */ import net.dv8tion.jda.annotations.ForRemoval;
/*      */ import net.dv8tion.jda.annotations.ReplaceWith;
/*      */ import net.dv8tion.jda.api.JDA;
/*      */ import net.dv8tion.jda.api.OnlineStatus;
/*      */ import net.dv8tion.jda.api.entities.Activity;
/*      */ import net.dv8tion.jda.api.entities.ApplicationInfo;
/*      */ import net.dv8tion.jda.api.entities.Category;
/*      */ import net.dv8tion.jda.api.entities.ChannelType;
/*      */ import net.dv8tion.jda.api.entities.Emote;
/*      */ import net.dv8tion.jda.api.entities.Guild;
/*      */ import net.dv8tion.jda.api.entities.GuildChannel;
/*      */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*      */ import net.dv8tion.jda.api.entities.Role;
/*      */ import net.dv8tion.jda.api.entities.StoreChannel;
/*      */ import net.dv8tion.jda.api.entities.TextChannel;
/*      */ import net.dv8tion.jda.api.entities.User;
/*      */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*      */ import net.dv8tion.jda.api.requests.GatewayIntent;
/*      */ import net.dv8tion.jda.api.requests.Request;
/*      */ import net.dv8tion.jda.api.requests.Response;
/*      */ import net.dv8tion.jda.api.requests.RestAction;
/*      */ import net.dv8tion.jda.api.utils.MiscUtil;
/*      */ import net.dv8tion.jda.api.utils.cache.CacheView;
/*      */ import net.dv8tion.jda.api.utils.cache.ShardCacheView;
/*      */ import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
/*      */ import net.dv8tion.jda.internal.JDAImpl;
/*      */ import net.dv8tion.jda.internal.requests.CompletedRestAction;
/*      */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*      */ import net.dv8tion.jda.internal.requests.Route;
/*      */ import net.dv8tion.jda.internal.utils.Checks;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public interface ShardManager
/*      */ {
/*      */   void addEventListener(@Nonnull Object... listeners) {
/*   72 */     Checks.noneNull(listeners, "listeners");
/*   73 */     getShardCache().forEach(jda -> jda.addEventListener(listeners));
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
/*      */ 
/*      */   
/*      */   void removeEventListener(@Nonnull Object... listeners) {
/*   87 */     Checks.noneNull(listeners, "listeners");
/*   88 */     getShardCache().forEach(jda -> jda.removeEventListener(listeners));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default void addEventListeners(@Nonnull IntFunction<Object> eventListenerProvider) {
/*  106 */     Checks.notNull(eventListenerProvider, "event listener provider");
/*  107 */     getShardCache().forEach(jda -> {
/*      */           Object listener = eventListenerProvider.apply(jda.getShardInfo().getShardId());
/*      */           if (listener != null) {
/*      */             jda.addEventListener(new Object[] { listener });
/*      */           }
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default void removeEventListeners(@Nonnull IntFunction<Collection<Object>> eventListenerProvider) {
/*  128 */     Checks.notNull(eventListenerProvider, "event listener provider");
/*  129 */     getShardCache().forEach(jda -> jda.removeEventListener(new Object[] { eventListenerProvider.apply(jda.getShardInfo().getShardId()) }));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default void removeEventListenerProvider(@Nonnull IntFunction<Object> eventListenerProvider) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default int getShardsRunning() {
/*  165 */     return (int)getShardCache().size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default int getShardsTotal() {
/*  176 */     return getShardsQueued() + getShardsRunning();
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
/*      */   default EnumSet<GatewayIntent> getGatewayIntents() {
/*  188 */     return (EnumSet<GatewayIntent>)getShardCache().applyStream(stream -> (EnumSet)stream.map(JDA::getGatewayIntents).findAny().orElse(EnumSet.noneOf(GatewayIntent.class)));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default RestAction<ApplicationInfo> retrieveApplicationInfo() {
/*  206 */     return ((JDA)getShardCache().stream()
/*  207 */       .findAny()
/*  208 */       .orElseThrow(() -> new IllegalStateException("no active shards")))
/*  209 */       .retrieveApplicationInfo();
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
/*      */ 
/*      */   
/*      */   default double getAverageGatewayPing() {
/*  223 */     return getShardCache()
/*  224 */       .stream()
/*  225 */       .mapToLong(JDA::getGatewayPing)
/*  226 */       .filter(ping -> (ping != -1L))
/*  227 */       .average()
/*  228 */       .orElse(-1.0D);
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
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default List<Category> getCategories() {
/*  244 */     return getCategoryCache().asList();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default List<Category> getCategoriesByName(@Nonnull String name, boolean ignoreCase) {
/*  264 */     return getCategoryCache().getElementsByName(name, ignoreCase);
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
/*      */   
/*      */   @Nullable
/*      */   default Category getCategoryById(long id) {
/*  278 */     return (Category)getCategoryCache().getElementById(id);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default Category getCategoryById(@Nonnull String id) {
/*  296 */     return (Category)getCategoryCache().getElementById(id);
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
/*      */   default SnowflakeCacheView<Category> getCategoryCache() {
/*  308 */     return CacheView.allSnowflakes(() -> getShardCache().stream().map(JDA::getCategoryCache));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default Emote getEmoteById(long id) {
/*  325 */     return (Emote)getEmoteCache().getElementById(id);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default Emote getEmoteById(@Nonnull String id) {
/*  345 */     return (Emote)getEmoteCache().getElementById(id);
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
/*      */   default SnowflakeCacheView<Emote> getEmoteCache() {
/*  358 */     return CacheView.allSnowflakes(() -> getShardCache().stream().map(JDA::getEmoteCache));
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
/*      */   default List<Emote> getEmotes() {
/*  380 */     return getEmoteCache().asList();
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
/*      */   default List<Emote> getEmotesByName(@Nonnull String name, boolean ignoreCase) {
/*  402 */     return getEmoteCache().getElementsByName(name, ignoreCase);
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
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default Guild getGuildById(long id) {
/*  417 */     return (Guild)getGuildCache().getElementById(id);
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
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default Guild getGuildById(@Nonnull String id) {
/*  432 */     return getGuildById(MiscUtil.parseSnowflake(id));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default List<Guild> getGuildsByName(@Nonnull String name, boolean ignoreCase) {
/*  449 */     return getGuildCache().getElementsByName(name, ignoreCase);
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
/*      */   default SnowflakeCacheView<Guild> getGuildCache() {
/*  461 */     return CacheView.allSnowflakes(() -> getShardCache().stream().map(JDA::getGuildCache));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default List<Guild> getGuilds() {
/*  479 */     return getGuildCache().asList();
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
/*      */   
/*      */   @Nonnull
/*      */   default List<Guild> getMutualGuilds(@Nonnull Collection<User> users) {
/*  493 */     Checks.noneNull(users, "users");
/*  494 */     return Collections.unmodifiableList((List<? extends Guild>)
/*  495 */         getGuildCache().stream()
/*  496 */         .filter(guild -> {
/*      */             Objects.requireNonNull(guild); return users.stream().allMatch(guild::isMember);
/*  498 */           }).collect(Collectors.toList()));
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
/*      */   
/*      */   @Nonnull
/*      */   List<Guild> getMutualGuilds(@Nonnull User... users) {
/*  512 */     Checks.notNull(users, "users");
/*  513 */     return getMutualGuilds(Arrays.asList(users));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   @CheckReturnValue
/*      */   default RestAction<User> retrieveUserById(@Nonnull String id) {
/*  543 */     return retrieveUserById(MiscUtil.parseSnowflake(id));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   @CheckReturnValue
/*      */   default RestAction<User> retrieveUserById(long id) {
/*  571 */     JDA api = null;
/*  572 */     for (JDA shard : getShardCache()) {
/*      */       
/*  574 */       api = shard;
/*  575 */       EnumSet<GatewayIntent> intents = shard.getGatewayIntents();
/*  576 */       User user = shard.getUserById(id);
/*  577 */       boolean isUpdated = (intents.contains(GatewayIntent.GUILD_PRESENCES) || intents.contains(GatewayIntent.GUILD_MEMBERS));
/*  578 */       if (user != null && isUpdated) {
/*  579 */         return (RestAction<User>)new CompletedRestAction(shard, user);
/*      */       }
/*      */     } 
/*  582 */     if (api == null) {
/*  583 */       throw new IllegalStateException("no shards active");
/*      */     }
/*  585 */     JDAImpl jda = (JDAImpl)api;
/*  586 */     Route.CompiledRoute route = Route.Users.GET_USER.compile(new String[] { Long.toUnsignedString(id) });
/*  587 */     return (RestAction<User>)new RestActionImpl((JDA)jda, route, (response, request) -> jda.getEntityBuilder().createUser(response.getObject()));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default User getUserByTag(@Nonnull String tag) {
/*  613 */     return (User)getShardCache().applyStream(stream -> (User)stream.map(()).filter(Objects::nonNull).findFirst().orElse(null));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default User getUserByTag(@Nonnull String username, @Nonnull String discriminator) {
/*  646 */     return (User)getShardCache().applyStream(stream -> (User)stream.map(()).filter(Objects::nonNull).findFirst().orElse(null));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default PrivateChannel getPrivateChannelById(long id) {
/*  667 */     return (PrivateChannel)getPrivateChannelCache().getElementById(id);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default PrivateChannel getPrivateChannelById(@Nonnull String id) {
/*  686 */     return (PrivateChannel)getPrivateChannelCache().getElementById(id);
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
/*      */   default SnowflakeCacheView<PrivateChannel> getPrivateChannelCache() {
/*  698 */     return CacheView.allSnowflakes(() -> getShardCache().stream().map(JDA::getPrivateChannelCache));
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
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default List<PrivateChannel> getPrivateChannels() {
/*  714 */     return getPrivateChannelCache().asList();
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
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default Role getRoleById(long id) {
/*  730 */     return (Role)getRoleCache().getElementById(id);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default Role getRoleById(@Nonnull String id) {
/*  749 */     return (Role)getRoleCache().getElementById(id);
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
/*      */   default SnowflakeCacheView<Role> getRoleCache() {
/*  761 */     return CacheView.allSnowflakes(() -> getShardCache().stream().map(JDA::getRoleCache));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default List<Role> getRoles() {
/*  779 */     return getRoleCache().asList();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default List<Role> getRolesByName(@Nonnull String name, boolean ignoreCase) {
/*  797 */     return getRoleCache().getElementsByName(name, ignoreCase);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default GuildChannel getGuildChannelById(@Nonnull String id) {
/*  825 */     return getGuildChannelById(MiscUtil.parseSnowflake(id));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default GuildChannel getGuildChannelById(long id) {
/*  849 */     for (JDA shard : getShards()) {
/*      */       
/*  851 */       GuildChannel channel = shard.getGuildChannelById(id);
/*  852 */       if (channel != null) {
/*  853 */         return channel;
/*      */       }
/*      */     } 
/*  856 */     return null;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default GuildChannel getGuildChannelById(@Nonnull ChannelType type, @Nonnull String id) {
/*  889 */     return getGuildChannelById(type, MiscUtil.parseSnowflake(id));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default GuildChannel getGuildChannelById(@Nonnull ChannelType type, long id) {
/*  918 */     Checks.notNull(type, "ChannelType");
/*      */     
/*  920 */     for (JDA shard : getShards()) {
/*      */       
/*  922 */       GuildChannel channel = shard.getGuildChannelById(type, id);
/*  923 */       if (channel != null) {
/*  924 */         return channel;
/*      */       }
/*      */     } 
/*  927 */     return null;
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
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default JDA getShardById(int id) {
/*  943 */     return getShardCache().getElementById(id);
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
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default JDA getShardById(@Nonnull String id) {
/*  959 */     return getShardCache().getElementById(id);
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
/*      */ 
/*      */ 
/*      */ 
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
/*      */   default List<JDA> getShards() {
/*  984 */     return getShardCache().asList();
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
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default JDA.Status getStatus(int shardId) {
/* 1000 */     JDA jda = getShardCache().getElementById(shardId);
/* 1001 */     return (jda == null) ? null : jda.getStatus();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default Map<JDA, JDA.Status> getStatuses() {
/* 1012 */     return Collections.unmodifiableMap((Map<? extends JDA, ? extends JDA.Status>)getShardCache().stream()
/* 1013 */         .collect(Collectors.toMap(Function.identity(), JDA::getStatus)));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default TextChannel getTextChannelById(long id) {
/* 1035 */     return (TextChannel)getTextChannelCache().getElementById(id);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default TextChannel getTextChannelById(@Nonnull String id) {
/* 1057 */     return (TextChannel)getTextChannelCache().getElementById(id);
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
/*      */   default SnowflakeCacheView<TextChannel> getTextChannelCache() {
/* 1069 */     return CacheView.allSnowflakes(() -> getShardCache().stream().map(JDA::getTextChannelCache));
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
/*      */ 
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
/*      */   default List<TextChannel> getTextChannels() {
/* 1092 */     return getTextChannelCache().asList();
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
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default StoreChannel getStoreChannelById(long id) {
/* 1108 */     return (StoreChannel)getStoreChannelCache().getElementById(id);
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
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default StoreChannel getStoreChannelById(@Nonnull String id) {
/* 1124 */     return (StoreChannel)getStoreChannelCache().getElementById(id);
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
/*      */   default SnowflakeCacheView<StoreChannel> getStoreChannelCache() {
/* 1136 */     return CacheView.allSnowflakes(() -> getShardCache().stream().map(JDA::getStoreChannelCache));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default List<StoreChannel> getStoreChannels() {
/* 1153 */     return getStoreChannelCache().asList();
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
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default User getUserById(long id) {
/* 1168 */     return (User)getUserCache().getElementById(id);
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
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default User getUserById(@Nonnull String id) {
/* 1183 */     return (User)getUserCache().getElementById(id);
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
/*      */   default SnowflakeCacheView<User> getUserCache() {
/* 1195 */     return CacheView.allSnowflakes(() -> getShardCache().stream().map(JDA::getUserCache));
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
/*      */   default List<User> getUsers() {
/* 1217 */     return getUserCache().asList();
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
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default VoiceChannel getVoiceChannelById(long id) {
/* 1233 */     return (VoiceChannel)getVoiceChannelCache().getElementById(id);
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
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   default VoiceChannel getVoiceChannelById(@Nonnull String id) {
/* 1248 */     return (VoiceChannel)getVoiceChannelCache().getElementById(id);
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
/*      */   default SnowflakeCacheView<VoiceChannel> getVoiceChannelCache() {
/* 1260 */     return CacheView.allSnowflakes(() -> getShardCache().stream().map(JDA::getVoiceChannelCache));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default List<VoiceChannel> getVoiceChannels() {
/* 1277 */     return getVoiceChannelCache().asList();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @ForRemoval(deadline = "5.0.0")
/*      */   @DeprecatedSince("4.0.0")
/*      */   @ReplaceWith("setActivity()")
/*      */   default void setGame(@Nullable Activity game) {
/* 1327 */     setActivityProvider(id -> game);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default void setActivity(@Nullable Activity activity) {
/* 1346 */     setActivityProvider(id -> activity);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default void setActivityProvider(@Nullable IntFunction<? extends Activity> activityProvider) {
/* 1364 */     getShardCache().forEach(jda -> jda.getPresence().setActivity((activityProvider == null) ? null : activityProvider.apply(jda.getShardInfo().getShardId())));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default void setIdle(boolean idle) {
/* 1380 */     setIdleProvider(id -> Boolean.valueOf(idle));
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
/*      */   
/*      */   default void setIdleProvider(@Nonnull IntFunction<Boolean> idleProvider) {
/* 1393 */     getShardCache().forEach(jda -> jda.getPresence().setIdle(((Boolean)idleProvider.apply(jda.getShardInfo().getShardId())).booleanValue()));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default void setPresence(@Nullable OnlineStatus status, @Nullable Activity activity) {
/* 1415 */     setPresenceProvider(id -> status, id -> activity);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default void setPresenceProvider(@Nullable IntFunction<OnlineStatus> statusProvider, @Nullable IntFunction<? extends Activity> activityProvider) {
/* 1438 */     getShardCache().forEach(jda -> jda.getPresence().setPresence((statusProvider == null) ? null : statusProvider.apply(jda.getShardInfo().getShardId()), (activityProvider == null) ? null : activityProvider.apply(jda.getShardInfo().getShardId())));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default void setStatus(@Nullable OnlineStatus status) {
/* 1455 */     setStatusProvider(id -> status);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default void setStatusProvider(@Nullable IntFunction<OnlineStatus> statusProvider) {
/* 1472 */     getShardCache().forEach(jda -> jda.getPresence().setStatus((statusProvider == null) ? null : statusProvider.apply(jda.getShardInfo().getShardId())));
/*      */   }
/*      */   
/*      */   int getShardsQueued();
/*      */   
/*      */   @Nonnull
/*      */   ShardCacheView getShardCache();
/*      */   
/*      */   void restart();
/*      */   
/*      */   void restart(int paramInt);
/*      */   
/*      */   void shutdown();
/*      */   
/*      */   void shutdown(int paramInt);
/*      */   
/*      */   void start(int paramInt);
/*      */   
/*      */   void login() throws LoginException;
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\sharding\ShardManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */