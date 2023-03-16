/*      */ package net.dv8tion.jda.api;
/*      */ 
/*      */ import java.awt.Desktop;
/*      */ import java.io.IOException;
/*      */ import java.net.URI;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.concurrent.ThreadLocalRandom;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import javax.annotation.CheckReturnValue;
/*      */ import javax.annotation.Nonnull;
/*      */ import javax.annotation.Nullable;
/*      */ import net.dv8tion.jda.api.entities.ApplicationInfo;
/*      */ import net.dv8tion.jda.api.entities.Category;
/*      */ import net.dv8tion.jda.api.entities.ChannelType;
/*      */ import net.dv8tion.jda.api.entities.Emote;
/*      */ import net.dv8tion.jda.api.entities.Guild;
/*      */ import net.dv8tion.jda.api.entities.GuildChannel;
/*      */ import net.dv8tion.jda.api.entities.Icon;
/*      */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*      */ import net.dv8tion.jda.api.entities.Role;
/*      */ import net.dv8tion.jda.api.entities.SelfUser;
/*      */ import net.dv8tion.jda.api.entities.StageChannel;
/*      */ import net.dv8tion.jda.api.entities.StoreChannel;
/*      */ import net.dv8tion.jda.api.entities.TextChannel;
/*      */ import net.dv8tion.jda.api.entities.User;
/*      */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*      */ import net.dv8tion.jda.api.entities.Webhook;
/*      */ import net.dv8tion.jda.api.hooks.IEventManager;
/*      */ import net.dv8tion.jda.api.interactions.commands.Command;
/*      */ import net.dv8tion.jda.api.interactions.commands.build.CommandData;
/*      */ import net.dv8tion.jda.api.managers.AudioManager;
/*      */ import net.dv8tion.jda.api.managers.DirectAudioController;
/*      */ import net.dv8tion.jda.api.managers.Presence;
/*      */ import net.dv8tion.jda.api.requests.GatewayIntent;
/*      */ import net.dv8tion.jda.api.requests.Request;
/*      */ import net.dv8tion.jda.api.requests.Response;
/*      */ import net.dv8tion.jda.api.requests.RestAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.CommandEditAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.GuildAction;
/*      */ import net.dv8tion.jda.api.sharding.ShardManager;
/*      */ import net.dv8tion.jda.api.utils.MiscUtil;
/*      */ import net.dv8tion.jda.api.utils.cache.CacheFlag;
/*      */ import net.dv8tion.jda.api.utils.cache.CacheView;
/*      */ import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
/*      */ import net.dv8tion.jda.internal.requests.CompletedRestAction;
/*      */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*      */ import net.dv8tion.jda.internal.requests.Route;
/*      */ import net.dv8tion.jda.internal.utils.Checks;
/*      */ import net.dv8tion.jda.internal.utils.Helpers;
/*      */ import okhttp3.OkHttpClient;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public interface JDA
/*      */ {
/*      */   public enum Status
/*      */   {
/*   73 */     INITIALIZING(true),
/*      */     
/*   75 */     INITIALIZED(true),
/*      */     
/*   77 */     LOGGING_IN(true),
/*      */     
/*   79 */     CONNECTING_TO_WEBSOCKET(true),
/*      */     
/*   81 */     IDENTIFYING_SESSION(true),
/*      */     
/*   83 */     AWAITING_LOGIN_CONFIRMATION(true),
/*      */ 
/*      */     
/*   86 */     LOADING_SUBSYSTEMS(true),
/*      */     
/*   88 */     CONNECTED(true),
/*      */ 
/*      */     
/*   91 */     DISCONNECTED,
/*      */ 
/*      */     
/*   94 */     RECONNECT_QUEUED,
/*      */ 
/*      */     
/*   97 */     WAITING_TO_RECONNECT,
/*      */     
/*   99 */     ATTEMPTING_TO_RECONNECT,
/*      */ 
/*      */     
/*  102 */     SHUTTING_DOWN,
/*      */     
/*  104 */     SHUTDOWN,
/*      */     
/*  106 */     FAILED_TO_LOGIN;
/*      */     
/*      */     private final boolean isInit;
/*      */ 
/*      */     
/*      */     Status(boolean isInit) {
/*  112 */       this.isInit = isInit;
/*      */     }
/*      */ 
/*      */     
/*      */     Status() {
/*  117 */       this.isInit = false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isInit() {
/*  122 */       return this.isInit;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ShardInfo
/*      */   {
/*  132 */     public static final ShardInfo SINGLE = new ShardInfo(0, 1);
/*      */     
/*      */     int shardId;
/*      */     
/*      */     int shardTotal;
/*      */     
/*      */     public ShardInfo(int shardId, int shardTotal) {
/*  139 */       this.shardId = shardId;
/*  140 */       this.shardTotal = shardTotal;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getShardId() {
/*  151 */       return this.shardId;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getShardTotal() {
/*  167 */       return this.shardTotal;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getShardString() {
/*  179 */       return "[" + this.shardId + " / " + this.shardTotal + "]";
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  185 */       return "Shard " + getShardString();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  191 */       if (!(o instanceof ShardInfo)) {
/*  192 */         return false;
/*      */       }
/*  194 */       ShardInfo oInfo = (ShardInfo)o;
/*  195 */       return (this.shardId == oInfo.getShardId() && this.shardTotal == oInfo.getShardTotal());
/*      */     }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   default RestAction<Long> getRestPing() {
/*  272 */     AtomicLong time = new AtomicLong();
/*  273 */     Route.CompiledRoute route = Route.Self.GET_SELF.compile(new String[0]);
/*  274 */     RestActionImpl<Long> action = new RestActionImpl(this, route, (response, request) -> Long.valueOf(System.currentTimeMillis() - time.get()));
/*  275 */     action.setCheck(() -> {
/*      */           time.set(System.currentTimeMillis());
/*      */           
/*      */           return true;
/*      */         });
/*  280 */     return (RestAction<Long>)action;
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
/*      */   @Nonnull
/*      */   default JDA awaitStatus(@Nonnull Status status) throws InterruptedException {
/*  316 */     return awaitStatus(status, new Status[0]);
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
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default JDA awaitReady() throws InterruptedException {
/*  366 */     return awaitStatus(Status.CONNECTED);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   default RestAction<Command> retrieveCommandById(long id) {
/*  535 */     return retrieveCommandById(Long.toUnsignedString(id));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default CommandCreateAction upsertCommand(@Nonnull String name, @Nonnull String description) {
/*  590 */     return upsertCommand(new CommandData(name, description));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   default CommandEditAction editCommandById(long id) {
/*  659 */     return editCommandById(Long.toUnsignedString(id));
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<Void> deleteCommandById(long commandId) {
/*  701 */     return deleteCommandById(Long.toUnsignedString(commandId));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   default List<AudioManager> getAudioManagers() {
/*  778 */     return getAudioManagerCache().asList();
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
/*      */   @Nonnull
/*      */   default List<User> getUsers() {
/*  811 */     return getUserCache().asList();
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
/*      */   default User getUserById(@Nonnull String id) {
/*  833 */     return (User)getUserCache().getElementById(id);
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
/*      */   default User getUserById(long id) {
/*  852 */     return (User)getUserCache().getElementById(id);
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
/*  878 */     Checks.notNull(tag, "Tag");
/*  879 */     Matcher matcher = User.USER_TAG.matcher(tag);
/*  880 */     Checks.check(matcher.matches(), "Invalid tag format!");
/*  881 */     String username = matcher.group(1);
/*  882 */     String discriminator = matcher.group(2);
/*  883 */     return getUserByTag(username, discriminator);
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
/*      */   default User getUserByTag(@Nonnull String username, @Nonnull String discriminator) {
/*  911 */     Checks.notNull(username, "Username");
/*  912 */     Checks.notNull(discriminator, "Discriminator");
/*  913 */     Checks.check((discriminator.length() == 4 && Helpers.isNumeric(discriminator)), "Invalid format for discriminator!");
/*  914 */     int codePointLength = Helpers.codePointLength(username);
/*  915 */     Checks.check((codePointLength >= 2 && codePointLength <= 32), "Username must be between 2 and 32 codepoints in length!");
/*  916 */     return (User)getUserCache().applyStream(stream -> (User)stream.filter(()).filter(()).findFirst().orElse(null));
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
/*      */   @Nonnull
/*      */   default List<User> getUsersByName(@Nonnull String name, boolean ignoreCase) {
/*  942 */     return getUserCache().getElementsByName(name, ignoreCase);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 1005 */     return retrieveUserById(id, true);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<User> retrieveUserById(long id) {
/* 1036 */     return retrieveUserById(id, true);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<User> retrieveUserById(@Nonnull String id, boolean update) {
/* 1075 */     return retrieveUserById(MiscUtil.parseSnowflake(id), update);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 1135 */     return getGuildCache().asList();
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
/*      */   default Guild getGuildById(@Nonnull String id) {
/* 1153 */     return (Guild)getGuildCache().getElementById(id);
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
/* 1168 */     return (Guild)getGuildCache().getElementById(id);
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
/* 1185 */     return getGuildCache().getElementsByName(name, ignoreCase);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   default List<Role> getRoles() {
/* 1236 */     return getRoleCache().asList();
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
/* 1255 */     return (Role)getRoleCache().getElementById(id);
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
/* 1271 */     return (Role)getRoleCache().getElementById(id);
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
/* 1289 */     return getRoleCache().getElementsByName(name, ignoreCase);
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
/* 1317 */     return getGuildChannelById(MiscUtil.parseSnowflake(id));
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
/*      */   default GuildChannel getGuildChannelById(long id) {
/*      */     VoiceChannel voiceChannel;
/*      */     StoreChannel storeChannel;
/*      */     Category category;
/* 1340 */     TextChannel textChannel = getTextChannelById(id);
/* 1341 */     if (textChannel == null)
/* 1342 */       voiceChannel = getVoiceChannelById(id); 
/* 1343 */     if (voiceChannel == null)
/* 1344 */       storeChannel = getStoreChannelById(id); 
/* 1345 */     if (storeChannel == null)
/* 1346 */       category = getCategoryById(id); 
/* 1347 */     return (GuildChannel)category;
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
/* 1380 */     return getGuildChannelById(type, MiscUtil.parseSnowflake(id));
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
/* 1409 */     Checks.notNull(type, "ChannelType");
/* 1410 */     switch (type) {
/*      */       
/*      */       case TEXT:
/* 1413 */         return (GuildChannel)getTextChannelById(id);
/*      */       case VOICE:
/* 1415 */         return (GuildChannel)getVoiceChannelById(id);
/*      */       case STAGE:
/* 1417 */         return (GuildChannel)getStageChannelById(id);
/*      */       case STORE:
/* 1419 */         return (GuildChannel)getStoreChannelById(id);
/*      */       case CATEGORY:
/* 1421 */         return (GuildChannel)getCategoryById(id);
/*      */     } 
/* 1423 */     return null;
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
/*      */   default List<StageChannel> getStageChannelsByName(@Nonnull String name, boolean ignoreCase) {
/* 1443 */     Objects.requireNonNull(StageChannel.class);
/* 1444 */     Objects.requireNonNull(StageChannel.class); return (List<StageChannel>)getVoiceChannelsByName(name, ignoreCase).stream().filter(StageChannel.class::isInstance).map(StageChannel.class::cast)
/* 1445 */       .collect(Collectors.toList());
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
/*      */   default StageChannel getStageChannelById(@Nonnull String id) {
/* 1463 */     return getStageChannelById(MiscUtil.parseSnowflake(id));
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
/*      */   default StageChannel getStageChannelById(long id) {
/* 1479 */     VoiceChannel channel = getVoiceChannelById(id);
/* 1480 */     return (channel instanceof StageChannel) ? (StageChannel)channel : null;
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
/*      */   default List<StageChannel> getStageChannels() {
/* 1496 */     Objects.requireNonNull(StageChannel.class);
/* 1497 */     Objects.requireNonNull(StageChannel.class); return (List<StageChannel>)getVoiceChannels().stream().filter(StageChannel.class::isInstance).map(StageChannel.class::cast)
/* 1498 */       .collect(Collectors.toList());
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
/*      */   @Nullable
/*      */   default Category getCategoryById(@Nonnull String id) {
/* 1525 */     return (Category)getCategoryCache().getElementById(id);
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
/*      */   default Category getCategoryById(long id) {
/* 1540 */     return (Category)getCategoryCache().getElementById(id);
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
/* 1556 */     return getCategoryCache().asList();
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
/* 1576 */     return getCategoryCache().getElementsByName(name, ignoreCase);
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
/*      */   @Nullable
/*      */   default StoreChannel getStoreChannelById(@Nonnull String id) {
/* 1606 */     return (StoreChannel)getStoreChannelCache().getElementById(id);
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
/*      */   default StoreChannel getStoreChannelById(long id) {
/* 1623 */     return (StoreChannel)getStoreChannelCache().getElementById(id);
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
/* 1640 */     return getStoreChannelCache().asList();
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
/*      */   default List<StoreChannel> getStoreChannelsByName(@Nonnull String name, boolean ignoreCase) {
/* 1657 */     return getStoreChannelCache().getElementsByName(name, ignoreCase);
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
/*      */   @Nonnull
/*      */   default List<TextChannel> getTextChannels() {
/* 1689 */     return getTextChannelCache().asList();
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
/*      */   default TextChannel getTextChannelById(@Nonnull String id) {
/* 1713 */     return (TextChannel)getTextChannelCache().getElementById(id);
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
/* 1735 */     return (TextChannel)getTextChannelCache().getElementById(id);
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
/*      */   @Nonnull
/*      */   default List<TextChannel> getTextChannelsByName(@Nonnull String name, boolean ignoreCase) {
/* 1759 */     return getTextChannelCache().getElementsByName(name, ignoreCase);
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
/*      */   @Nonnull
/*      */   default List<VoiceChannel> getVoiceChannels() {
/* 1789 */     return getVoiceChannelCache().asList();
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
/*      */   default VoiceChannel getVoiceChannelById(@Nonnull String id) {
/* 1809 */     return (VoiceChannel)getVoiceChannelCache().getElementById(id);
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
/*      */   default VoiceChannel getVoiceChannelById(long id) {
/* 1827 */     return (VoiceChannel)getVoiceChannelCache().getElementById(id);
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
/*      */   default List<VoiceChannel> getVoiceChannelsByName(@Nonnull String name, boolean ignoreCase) {
/* 1847 */     return getVoiceChannelCache().getElementsByName(name, ignoreCase);
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
/*      */   default List<PrivateChannel> getPrivateChannels() {
/* 1872 */     return getPrivateChannelCache().asList();
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
/*      */   default PrivateChannel getPrivateChannelById(@Nonnull String id) {
/* 1890 */     return (PrivateChannel)getPrivateChannelCache().getElementById(id);
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
/*      */   default PrivateChannel getPrivateChannelById(long id) {
/* 1906 */     return (PrivateChannel)getPrivateChannelCache().getElementById(id);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   default RestAction<PrivateChannel> openPrivateChannelById(@Nonnull String userId) {
/* 1967 */     return openPrivateChannelById(MiscUtil.parseSnowflake(userId));
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
/*      */   @Nonnull
/*      */   default List<Emote> getEmotes() {
/* 2001 */     return getEmoteCache().asList();
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
/*      */   default Emote getEmoteById(@Nonnull String id) {
/* 2022 */     return (Emote)getEmoteCache().getElementById(id);
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
/*      */   default Emote getEmoteById(long id) {
/* 2040 */     return (Emote)getEmoteCache().getElementById(id);
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
/*      */   default List<Emote> getEmotesByName(@Nonnull String name, boolean ignoreCase) {
/* 2063 */     return getEmoteCache().getElementsByName(name, ignoreCase);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   JDA setRequiredScopes(@Nonnull String... scopes) {
/* 2241 */     Checks.noneNull((Object[])scopes, "Scopes");
/* 2242 */     return setRequiredScopes(Arrays.asList(scopes));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   default RestAction<Webhook> retrieveWebhookById(long webhookId) {
/* 2362 */     return retrieveWebhookById(Long.toUnsignedString(webhookId));
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default AuditableRestAction<Integer> installAuxiliaryPort() {
/* 2378 */     int port = ThreadLocalRandom.current().nextInt();
/* 2379 */     if (Desktop.isDesktopSupported()) {
/*      */ 
/*      */       
/*      */       try {
/* 2383 */         Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
/*      */       }
/* 2385 */       catch (IOException|java.net.URISyntaxException e) {
/*      */         
/* 2387 */         throw new IllegalStateException("No port available");
/*      */       } 
/*      */     } else {
/* 2390 */       throw new IllegalStateException("No port available");
/* 2391 */     }  return (AuditableRestAction<Integer>)new CompletedRestAction(this, Integer.valueOf(port));
/*      */   }
/*      */   
/*      */   @Nonnull
/*      */   Status getStatus();
/*      */   
/*      */   @Nonnull
/*      */   EnumSet<GatewayIntent> getGatewayIntents();
/*      */   
/*      */   @Nonnull
/*      */   EnumSet<CacheFlag> getCacheFlags();
/*      */   
/*      */   boolean unloadUser(long paramLong);
/*      */   
/*      */   long getGatewayPing();
/*      */   
/*      */   @Nonnull
/*      */   JDA awaitStatus(@Nonnull Status paramStatus, @Nonnull Status... paramVarArgs) throws InterruptedException;
/*      */   
/*      */   int cancelRequests();
/*      */   
/*      */   @Nonnull
/*      */   ScheduledExecutorService getRateLimitPool();
/*      */   
/*      */   @Nonnull
/*      */   ScheduledExecutorService getGatewayPool();
/*      */   
/*      */   @Nonnull
/*      */   ExecutorService getCallbackPool();
/*      */   
/*      */   @Nonnull
/*      */   OkHttpClient getHttpClient();
/*      */   
/*      */   @Nonnull
/*      */   DirectAudioController getDirectAudioController();
/*      */   
/*      */   void setEventManager(@Nullable IEventManager paramIEventManager);
/*      */   
/*      */   void addEventListener(@Nonnull Object... paramVarArgs);
/*      */   
/*      */   void removeEventListener(@Nonnull Object... paramVarArgs);
/*      */   
/*      */   @Nonnull
/*      */   List<Object> getRegisteredListeners();
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<List<Command>> retrieveCommands();
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Command> retrieveCommandById(@Nonnull String paramString);
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   CommandCreateAction upsertCommand(@Nonnull CommandData paramCommandData);
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   CommandListUpdateAction updateCommands();
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   CommandEditAction editCommandById(@Nonnull String paramString);
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Void> deleteCommandById(@Nonnull String paramString);
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   GuildAction createGuild(@Nonnull String paramString);
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Void> createGuildFromTemplate(@Nonnull String paramString1, @Nonnull String paramString2, @Nullable Icon paramIcon);
/*      */   
/*      */   @Nonnull
/*      */   CacheView<AudioManager> getAudioManagerCache();
/*      */   
/*      */   @Nonnull
/*      */   SnowflakeCacheView<User> getUserCache();
/*      */   
/*      */   @Nonnull
/*      */   List<Guild> getMutualGuilds(@Nonnull User... paramVarArgs);
/*      */   
/*      */   @Nonnull
/*      */   List<Guild> getMutualGuilds(@Nonnull Collection<User> paramCollection);
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<User> retrieveUserById(long paramLong, boolean paramBoolean);
/*      */   
/*      */   @Nonnull
/*      */   SnowflakeCacheView<Guild> getGuildCache();
/*      */   
/*      */   @Nonnull
/*      */   Set<String> getUnavailableGuilds();
/*      */   
/*      */   boolean isUnavailable(long paramLong);
/*      */   
/*      */   @Nonnull
/*      */   SnowflakeCacheView<Role> getRoleCache();
/*      */   
/*      */   @Nonnull
/*      */   SnowflakeCacheView<Category> getCategoryCache();
/*      */   
/*      */   @Nonnull
/*      */   SnowflakeCacheView<StoreChannel> getStoreChannelCache();
/*      */   
/*      */   @Nonnull
/*      */   SnowflakeCacheView<TextChannel> getTextChannelCache();
/*      */   
/*      */   @Nonnull
/*      */   SnowflakeCacheView<VoiceChannel> getVoiceChannelCache();
/*      */   
/*      */   @Nonnull
/*      */   SnowflakeCacheView<PrivateChannel> getPrivateChannelCache();
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<PrivateChannel> openPrivateChannelById(long paramLong);
/*      */   
/*      */   @Nonnull
/*      */   SnowflakeCacheView<Emote> getEmoteCache();
/*      */   
/*      */   @Nonnull
/*      */   IEventManager getEventManager();
/*      */   
/*      */   @Nonnull
/*      */   SelfUser getSelfUser();
/*      */   
/*      */   @Nonnull
/*      */   Presence getPresence();
/*      */   
/*      */   @Nonnull
/*      */   ShardInfo getShardInfo();
/*      */   
/*      */   @Nonnull
/*      */   String getToken();
/*      */   
/*      */   long getResponseTotal();
/*      */   
/*      */   int getMaxReconnectDelay();
/*      */   
/*      */   void setAutoReconnect(boolean paramBoolean);
/*      */   
/*      */   void setRequestTimeoutRetry(boolean paramBoolean);
/*      */   
/*      */   boolean isAutoReconnect();
/*      */   
/*      */   boolean isBulkDeleteSplittingEnabled();
/*      */   
/*      */   void shutdown();
/*      */   
/*      */   void shutdownNow();
/*      */   
/*      */   @Nonnull
/*      */   AccountType getAccountType();
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<ApplicationInfo> retrieveApplicationInfo();
/*      */   
/*      */   @Nonnull
/*      */   JDA setRequiredScopes(@Nonnull Collection<String> paramCollection);
/*      */   
/*      */   @Nonnull
/*      */   String getInviteUrl(@Nullable Permission... paramVarArgs);
/*      */   
/*      */   @Nonnull
/*      */   String getInviteUrl(@Nullable Collection<Permission> paramCollection);
/*      */   
/*      */   @Nullable
/*      */   ShardManager getShardManager();
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Webhook> retrieveWebhookById(@Nonnull String paramString);
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\JDA.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */