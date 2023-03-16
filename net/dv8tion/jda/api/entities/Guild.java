/*      */ package net.dv8tion.jda.api.entities;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
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
/*      */ import javax.annotation.CheckReturnValue;
/*      */ import javax.annotation.Nonnull;
/*      */ import javax.annotation.Nullable;
/*      */ import net.dv8tion.jda.annotations.DeprecatedSince;
/*      */ import net.dv8tion.jda.annotations.ForRemoval;
/*      */ import net.dv8tion.jda.annotations.ReplaceWith;
/*      */ import net.dv8tion.jda.api.JDA;
/*      */ import net.dv8tion.jda.api.Permission;
/*      */ import net.dv8tion.jda.api.Region;
/*      */ import net.dv8tion.jda.api.entities.templates.Template;
/*      */ import net.dv8tion.jda.api.exceptions.HierarchyException;
/*      */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*      */ import net.dv8tion.jda.api.interactions.commands.Command;
/*      */ import net.dv8tion.jda.api.interactions.commands.build.CommandData;
/*      */ import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
/*      */ import net.dv8tion.jda.api.managers.AudioManager;
/*      */ import net.dv8tion.jda.api.managers.GuildManager;
/*      */ import net.dv8tion.jda.api.requests.GatewayIntent;
/*      */ import net.dv8tion.jda.api.requests.RestAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.ChannelAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.CommandEditAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.MemberAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.RoleAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.order.CategoryOrderAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.order.ChannelOrderAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.order.RoleOrderAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.pagination.AuditLogPaginationAction;
/*      */ import net.dv8tion.jda.api.utils.MiscUtil;
/*      */ import net.dv8tion.jda.api.utils.cache.MemberCacheView;
/*      */ import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
/*      */ import net.dv8tion.jda.api.utils.cache.SortedSnowflakeCacheView;
/*      */ import net.dv8tion.jda.api.utils.concurrent.Task;
/*      */ import net.dv8tion.jda.internal.requests.DeferredRestAction;
/*      */ import net.dv8tion.jda.internal.requests.Route;
/*      */ import net.dv8tion.jda.internal.requests.restaction.AuditableRestActionImpl;
/*      */ import net.dv8tion.jda.internal.utils.Checks;
/*      */ import net.dv8tion.jda.internal.utils.concurrent.task.GatewayTask;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public interface Guild
/*      */   extends ISnowflake
/*      */ {
/*      */   public static final String ICON_URL = "https://cdn.discordapp.com/icons/%s/%s.%s";
/*      */   public static final String SPLASH_URL = "https://cdn.discordapp.com/splashes/%s/%s.png";
/*      */   public static final String BANNER_URL = "https://cdn.discordapp.com/banners/%s/%s.png";
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<Command> retrieveCommandById(long id) {
/*  123 */     return retrieveCommandById(Long.toUnsignedString(id));
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default CommandCreateAction upsertCommand(@Nonnull String name, @Nonnull String description) {
/*  168 */     return upsertCommand(new CommandData(name, description));
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default CommandEditAction editCommandById(long id) {
/*  228 */     return editCommandById(Long.toUnsignedString(id));
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<Void> deleteCommandById(long commandId) {
/*  264 */     return deleteCommandById(Long.toUnsignedString(commandId));
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<List<CommandPrivilege>> retrieveCommandPrivilegesById(long commandId) {
/*  307 */     return retrieveCommandPrivilegesById(Long.toUnsignedString(commandId));
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
/*      */   RestAction<List<CommandPrivilege>> updateCommandPrivilegesById(@Nonnull String id, @Nonnull CommandPrivilege... privileges) {
/*  368 */     Checks.noneNull((Object[])privileges, "CommandPrivileges");
/*  369 */     return updateCommandPrivilegesById(id, Arrays.asList(privileges));
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
/*      */   @CheckReturnValue
/*      */   default RestAction<List<CommandPrivilege>> updateCommandPrivilegesById(long id, @Nonnull Collection<? extends CommandPrivilege> privileges) {
/*  395 */     return updateCommandPrivilegesById(Long.toUnsignedString(id), privileges);
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
/*      */   @CheckReturnValue
/*      */   RestAction<List<CommandPrivilege>> updateCommandPrivilegesById(long id, @Nonnull CommandPrivilege... privileges) {
/*  421 */     Checks.noneNull((Object[])privileges, "CommandPrivileges");
/*  422 */     return updateCommandPrivilegesById(id, Arrays.asList(privileges));
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<EnumSet<Region>> retrieveRegions() {
/*  458 */     return retrieveRegions(true);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default MemberAction addMember(@Nonnull String accessToken, @Nonnull User user) {
/*  523 */     Checks.notNull(user, "User");
/*  524 */     return addMember(accessToken, user.getId());
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
/*      */   default MemberAction addMember(@Nonnull String accessToken, long userId) {
/*  552 */     return addMember(accessToken, Long.toUnsignedString(userId));
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
/*      */   @Nullable
/*      */   default String getIconUrl() {
/*  652 */     String iconId = getIconId();
/*  653 */     return (iconId == null) ? null : String.format("https://cdn.discordapp.com/icons/%s/%s.%s", new Object[] { getId(), iconId, iconId.startsWith("a_") ? "gif" : "png" });
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
/*      */   @Nullable
/*      */   default String getSplashUrl() {
/*  693 */     String splashId = getSplashId();
/*  694 */     return (splashId == null) ? null : String.format("https://cdn.discordapp.com/splashes/%s/%s.png", new Object[] { getId(), splashId });
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
/*      */   @Nullable
/*      */   default String getVanityUrl() {
/*  761 */     return (getVanityCode() == null) ? null : ("https://discord.gg/" + getVanityCode());
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
/*      */   @Nullable
/*      */   default String getBannerUrl() {
/*  846 */     String bannerId = getBannerId();
/*  847 */     return (bannerId == null) ? null : String.format("https://cdn.discordapp.com/banners/%s/%s.png", new Object[] { getId(), bannerId });
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
/*      */   default int getMaxBitrate() {
/*  890 */     int maxBitrate = getFeatures().contains("VIP_REGIONS") ? 384000 : 96000;
/*  891 */     return Math.max(maxBitrate, getBoostTier().getMaxBitrate());
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
/*      */   default long getMaxFileSize() {
/*  904 */     return getBoostTier().getMaxFileSize();
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
/*      */   default int getMaxEmotes() {
/*  916 */     int maxEmotes = getFeatures().contains("MORE_EMOJI") ? 200 : 50;
/*  917 */     return Math.max(maxEmotes, getBoostTier().getMaxEmotes());
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
/*      */   @Nonnull
/*      */   default String getOwnerId() {
/* 1043 */     return Long.toUnsignedString(getOwnerIdLong());
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
/*      */   @Deprecated
/*      */   @ForRemoval(deadline = "5.0.0")
/*      */   @ReplaceWith("VoiceChannel.getRegion()")
/*      */   @DeprecatedSince("4.3.0")
/*      */   @Nonnull
/*      */   default Region getRegion() {
/* 1079 */     return Region.fromKey(getRegionRaw());
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
/*      */   @Nullable
/*      */   default Member getMemberById(@Nonnull String userId) {
/* 1173 */     return getMemberCache().getElementById(userId);
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
/*      */   default Member getMemberById(long userId) {
/* 1195 */     return getMemberCache().getElementById(userId);
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
/*      */   @Nullable
/*      */   default Member getMemberByTag(@Nonnull String tag) {
/* 1226 */     User user = getJDA().getUserByTag(tag);
/* 1227 */     return (user == null) ? null : getMember(user);
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
/*      */   default Member getMemberByTag(@Nonnull String username, @Nonnull String discriminator) {
/* 1260 */     User user = getJDA().getUserByTag(username, discriminator);
/* 1261 */     return (user == null) ? null : getMember(user);
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
/*      */   default List<Member> getMembers() {
/* 1283 */     return getMemberCache().asList();
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
/*      */   default List<Member> getMembersByName(@Nonnull String name, boolean ignoreCase) {
/* 1309 */     return getMemberCache().getElementsByUsername(name, ignoreCase);
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
/*      */   default List<Member> getMembersByNickname(@Nullable String nickname, boolean ignoreCase) {
/* 1332 */     return getMemberCache().getElementsByNickname(nickname, ignoreCase);
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
/*      */   default List<Member> getMembersByEffectiveName(@Nonnull String name, boolean ignoreCase) {
/* 1358 */     return getMemberCache().getElementsByName(name, ignoreCase);
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
/*      */   List<Member> getMembersWithRoles(@Nonnull Role... roles) {
/* 1382 */     return getMemberCache().getElementsWithRoles(roles);
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
/*      */   default List<Member> getMembersWithRoles(@Nonnull Collection<Role> roles) {
/* 1406 */     return getMemberCache().getElementsWithRoles(roles);
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
/*      */   @Nullable
/*      */   default GuildChannel getGuildChannelById(@Nonnull String id) {
/* 1448 */     return getGuildChannelById(MiscUtil.parseSnowflake(id));
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
/*      */   @Nullable
/*      */   default GuildChannel getGuildChannelById(long id) {
/* 1471 */     GuildChannel channel = getTextChannelById(id);
/* 1472 */     if (channel == null)
/* 1473 */       channel = getVoiceChannelById(id); 
/* 1474 */     if (channel == null)
/* 1475 */       channel = getStoreChannelById(id); 
/* 1476 */     if (channel == null)
/* 1477 */       channel = getCategoryById(id); 
/* 1478 */     return channel;
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
/*      */   @Nullable
/*      */   default GuildChannel getGuildChannelById(@Nonnull ChannelType type, @Nonnull String id) {
/* 1509 */     return getGuildChannelById(type, MiscUtil.parseSnowflake(id));
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
/*      */   default GuildChannel getGuildChannelById(@Nonnull ChannelType type, long id) {
/* 1535 */     Checks.notNull(type, "ChannelType");
/* 1536 */     switch (type) {
/*      */       
/*      */       case TEXT:
/* 1539 */         return getTextChannelById(id);
/*      */       case VOICE:
/* 1541 */         return getVoiceChannelById(id);
/*      */       case STAGE:
/* 1543 */         return getStageChannelById(id);
/*      */       case STORE:
/* 1545 */         return getStoreChannelById(id);
/*      */       case CATEGORY:
/* 1547 */         return getCategoryById(id);
/*      */     } 
/* 1549 */     return null;
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
/* 1569 */     Objects.requireNonNull(StageChannel.class);
/* 1570 */     Objects.requireNonNull(StageChannel.class); return (List<StageChannel>)getVoiceChannelsByName(name, ignoreCase).stream().filter(StageChannel.class::isInstance).map(StageChannel.class::cast)
/* 1571 */       .collect(Collectors.toList());
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
/*      */   default StageChannel getStageChannelById(@Nonnull String id) {
/* 1592 */     return getStageChannelById(MiscUtil.parseSnowflake(id));
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
/*      */   default StageChannel getStageChannelById(long id) {
/* 1610 */     VoiceChannel channel = getVoiceChannelById(id);
/* 1611 */     return (channel instanceof StageChannel) ? (StageChannel)channel : null;
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
/*      */   default List<StageChannel> getStageChannels() {
/* 1628 */     Objects.requireNonNull(StageChannel.class);
/* 1629 */     Objects.requireNonNull(StageChannel.class); return (List<StageChannel>)getVoiceChannels().stream().filter(StageChannel.class::isInstance).map(StageChannel.class::cast)
/* 1630 */       .collect(Collectors.toList());
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
/*      */   default Category getCategoryById(@Nonnull String id) {
/* 1650 */     return (Category)getCategoryCache().getElementById(id);
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
/*      */   default Category getCategoryById(long id) {
/* 1667 */     return (Category)getCategoryCache().getElementById(id);
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
/*      */   default List<Category> getCategories() {
/* 1684 */     return getCategoryCache().asList();
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
/* 1704 */     return getCategoryCache().getElementsByName(name, ignoreCase);
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
/*      */   default StoreChannel getStoreChannelById(@Nonnull String id) {
/* 1737 */     return (StoreChannel)getStoreChannelCache().getElementById(id);
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
/*      */   default StoreChannel getStoreChannelById(long id) {
/* 1757 */     return (StoreChannel)getStoreChannelCache().getElementById(id);
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
/*      */   @Nonnull
/*      */   default List<StoreChannel> getStoreChannels() {
/* 1776 */     return getStoreChannelCache().asList();
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
/*      */   default List<StoreChannel> getStoreChannelsByName(@Nonnull String name, boolean ignoreCase) {
/* 1796 */     return getStoreChannelCache().getElementsByName(name, ignoreCase);
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
/*      */   default TextChannel getTextChannelById(@Nonnull String id) {
/* 1829 */     return (TextChannel)getTextChannelCache().getElementById(id);
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
/*      */   default TextChannel getTextChannelById(long id) {
/* 1847 */     return (TextChannel)getTextChannelCache().getElementById(id);
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
/*      */   default List<TextChannel> getTextChannels() {
/* 1864 */     return getTextChannelCache().asList();
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
/*      */   default List<TextChannel> getTextChannelsByName(@Nonnull String name, boolean ignoreCase) {
/* 1882 */     return getTextChannelCache().getElementsByName(name, ignoreCase);
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
/*      */   default VoiceChannel getVoiceChannelById(@Nonnull String id) {
/* 1915 */     return (VoiceChannel)getVoiceChannelCache().getElementById(id);
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
/*      */   default VoiceChannel getVoiceChannelById(long id) {
/* 1935 */     return (VoiceChannel)getVoiceChannelCache().getElementById(id);
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
/*      */   @Nonnull
/*      */   default List<VoiceChannel> getVoiceChannels() {
/* 1954 */     return getVoiceChannelCache().asList();
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
/* 1974 */     return getVoiceChannelCache().getElementsByName(name, ignoreCase);
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
/*      */   @Nonnull
/*      */   default List<GuildChannel> getChannels() {
/* 2013 */     return getChannels(true);
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
/*      */   @Nullable
/*      */   default Role getRoleById(@Nonnull String id) {
/* 2060 */     return (Role)getRoleCache().getElementById(id);
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
/*      */   default Role getRoleById(long id) {
/* 2077 */     return (Role)getRoleCache().getElementById(id);
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
/* 2095 */     return getRoleCache().asList();
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
/* 2113 */     return getRoleCache().getElementsByName(name, ignoreCase);
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
/*      */   default Role getRoleByBot(long userId) {
/* 2134 */     return (Role)getRoleCache().applyStream(stream -> (Role)stream.filter(()).findFirst().orElse(null));
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
/*      */   default Role getRoleByBot(@Nonnull String userId) {
/* 2162 */     return getRoleByBot(MiscUtil.parseSnowflake(userId));
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
/*      */   default Role getRoleByBot(@Nonnull User user) {
/* 2186 */     Checks.notNull(user, "User");
/* 2187 */     return getRoleByBot(user.getIdLong());
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
/*      */   default Role getBotRole() {
/* 2205 */     return getRoleByBot(getJDA().getSelfUser());
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
/*      */   default Role getBoostRole() {
/* 2222 */     return (Role)getRoleCache().applyStream(stream -> (Role)stream.filter(()).findFirst().orElse(null));
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
/*      */   @Nullable
/*      */   default Emote getEmoteById(@Nonnull String id) {
/* 2262 */     return (Emote)getEmoteCache().getElementById(id);
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
/*      */   @Nullable
/*      */   default Emote getEmoteById(long id) {
/* 2285 */     return (Emote)getEmoteCache().getElementById(id);
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
/*      */   default List<Emote> getEmotes() {
/* 2308 */     return getEmoteCache().asList();
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
/* 2330 */     return getEmoteCache().getElementsByName(name, ignoreCase);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<ListedEmote> retrieveEmoteById(long id) {
/* 2413 */     return retrieveEmoteById(Long.toUnsignedString(id));
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
/*      */   @CheckReturnValue
/*      */   default RestAction<ListedEmote> retrieveEmote(@Nonnull Emote emote) {
/* 2440 */     Checks.notNull(emote, "Emote");
/* 2441 */     if (emote.getGuild() != null) {
/* 2442 */       Checks.check(emote.getGuild().equals(this), "Emote must be from the same Guild!");
/*      */     }
/* 2444 */     JDA jda = getJDA();
/* 2445 */     return (RestAction<ListedEmote>)new DeferredRestAction(jda, ListedEmote.class, () -> {
/*      */           if (emote instanceof ListedEmote) {
/*      */             ListedEmote listedEmote = (ListedEmote)emote;
/*      */             if (listedEmote.hasUser() || !getSelfMember().hasPermission(new Permission[] { Permission.MANAGE_EMOTES }, )) {
/*      */               return listedEmote;
/*      */             }
/*      */           } 
/*      */           return null;
/*      */         }() -> retrieveEmoteById(emote.getId()));
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<Ban> retrieveBanById(long userId) {
/* 2507 */     return retrieveBanById(Long.toUnsignedString(userId));
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<Ban> retrieveBan(@Nonnull User bannedUser) {
/* 2566 */     Checks.notNull(bannedUser, "bannedUser");
/* 2567 */     return retrieveBanById(bannedUser.getId());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   default Task<List<Member>> loadMembers() {
/* 3002 */     return findMembers(m -> true);
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
/*      */   @CheckReturnValue
/*      */   default Task<List<Member>> findMembers(@Nonnull Predicate<? super Member> filter) {
/* 3029 */     Checks.notNull(filter, "Filter");
/* 3030 */     List<Member> list = new ArrayList<>();
/* 3031 */     CompletableFuture<List<Member>> future = new CompletableFuture<>();
/* 3032 */     Task<Void> reference = loadMembers(member -> {
/*      */           if (filter.test(member))
/*      */             list.add(member); 
/*      */         });
/* 3036 */     Objects.requireNonNull(reference); GatewayTask<List<Member>> task = new GatewayTask(future, reference::cancel);
/*      */     
/* 3038 */     Objects.requireNonNull(future); reference.onSuccess(it -> future.complete(list)).onError(future::completeExceptionally);
/* 3039 */     return (Task<List<Member>>)task;
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default Task<List<Member>> findMembersWithRoles(@Nonnull Collection<Role> roles) {
/* 3068 */     Checks.noneNull(roles, "Roles");
/* 3069 */     for (Role role : roles) {
/* 3070 */       Checks.check(equals(role.getGuild()), "All roles must be from the same guild!");
/*      */     }
/* 3072 */     if (isLoaded() || roles.isEmpty() || roles.contains(getPublicRole())) {
/*      */       
/* 3074 */       CompletableFuture<List<Member>> future = CompletableFuture.completedFuture(getMembersWithRoles(roles));
/* 3075 */       return (Task<List<Member>>)new GatewayTask(future, () -> {
/*      */           
/*      */           });
/* 3078 */     }  return findMembers(member -> member.getRoles().containsAll(roles));
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   Task<List<Member>> findMembersWithRoles(@Nonnull Role... roles) {
/* 3107 */     Checks.noneNull((Object[])roles, "Roles");
/* 3108 */     return findMembersWithRoles(Arrays.asList(roles));
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
/*      */   default RestAction<Member> retrieveMember(@Nonnull User user) {
/* 3168 */     Checks.notNull(user, "User");
/* 3169 */     return retrieveMemberById(user.getId());
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
/*      */   @Nonnull
/*      */   default RestAction<Member> retrieveMemberById(@Nonnull String id) {
/* 3208 */     return retrieveMemberById(MiscUtil.parseSnowflake(id));
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
/*      */   default RestAction<Member> retrieveMemberById(long id) {
/* 3242 */     return retrieveMemberById(id, true);
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
/*      */   default RestAction<Member> retrieveOwner() {
/* 3274 */     return retrieveMemberById(getOwnerIdLong());
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
/*      */   default RestAction<Member> retrieveMember(@Nonnull User user, boolean update) {
/* 3310 */     Checks.notNull(user, "User");
/* 3311 */     return retrieveMemberById(user.getId(), update);
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
/*      */   default RestAction<Member> retrieveMemberById(@Nonnull String id, boolean update) {
/* 3349 */     return retrieveMemberById(MiscUtil.parseSnowflake(id), update);
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
/*      */   default RestAction<Member> retrieveOwner(boolean update) {
/* 3411 */     return retrieveMemberById(getOwnerIdLong(), update);
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
/*      */   @CheckReturnValue
/*      */   default Task<List<Member>> retrieveMembers(@Nonnull Collection<User> users) {
/* 3444 */     Checks.noneNull(users, "Users");
/* 3445 */     if (users.isEmpty())
/* 3446 */       return (Task<List<Member>>)new GatewayTask(CompletableFuture.completedFuture(Collections.emptyList()), () -> {
/*      */           
/* 3448 */           });  long[] ids = users.stream().mapToLong(ISnowflake::getIdLong).toArray();
/* 3449 */     return retrieveMembersByIds(ids);
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
/*      */   @CheckReturnValue
/*      */   default Task<List<Member>> retrieveMembersByIds(@Nonnull Collection<Long> ids) {
/* 3482 */     Checks.noneNull(ids, "IDs");
/* 3483 */     if (ids.isEmpty())
/* 3484 */       return (Task<List<Member>>)new GatewayTask(CompletableFuture.completedFuture(Collections.emptyList()), () -> {
/*      */           
/* 3486 */           });  long[] arr = ids.stream().mapToLong(Long::longValue).toArray();
/* 3487 */     return retrieveMembersByIds(arr);
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
/*      */   @CheckReturnValue
/*      */   Task<List<Member>> retrieveMembersByIds(@Nonnull String... ids) {
/* 3520 */     Checks.notNull(ids, "Array");
/* 3521 */     if (ids.length == 0)
/* 3522 */       return (Task<List<Member>>)new GatewayTask(CompletableFuture.completedFuture(Collections.emptyList()), () -> {
/*      */           
/* 3524 */           });  long[] arr = new long[ids.length];
/* 3525 */     for (int i = 0; i < ids.length; i++)
/* 3526 */       arr[i] = MiscUtil.parseSnowflake(ids[i]); 
/* 3527 */     return retrieveMembersByIds(arr);
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
/*      */   @CheckReturnValue
/*      */   Task<List<Member>> retrieveMembersByIds(@Nonnull long... ids) {
/* 3560 */     boolean presence = getJDA().getGatewayIntents().contains(GatewayIntent.GUILD_PRESENCES);
/* 3561 */     return retrieveMembersByIds(presence, ids);
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
/*      */   @CheckReturnValue
/*      */   default Task<List<Member>> retrieveMembers(boolean includePresence, @Nonnull Collection<User> users) {
/* 3595 */     Checks.noneNull(users, "Users");
/* 3596 */     if (users.isEmpty())
/* 3597 */       return (Task<List<Member>>)new GatewayTask(CompletableFuture.completedFuture(Collections.emptyList()), () -> {
/*      */           
/* 3599 */           });  long[] ids = users.stream().mapToLong(ISnowflake::getIdLong).toArray();
/* 3600 */     return retrieveMembersByIds(includePresence, ids);
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
/*      */   @CheckReturnValue
/*      */   default Task<List<Member>> retrieveMembersByIds(boolean includePresence, @Nonnull Collection<Long> ids) {
/* 3634 */     Checks.noneNull(ids, "IDs");
/* 3635 */     if (ids.isEmpty())
/* 3636 */       return (Task<List<Member>>)new GatewayTask(CompletableFuture.completedFuture(Collections.emptyList()), () -> {
/*      */           
/* 3638 */           });  long[] arr = ids.stream().mapToLong(Long::longValue).toArray();
/* 3639 */     return retrieveMembersByIds(includePresence, arr);
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
/*      */   @CheckReturnValue
/*      */   Task<List<Member>> retrieveMembersByIds(boolean includePresence, @Nonnull String... ids) {
/* 3673 */     Checks.notNull(ids, "Array");
/* 3674 */     if (ids.length == 0)
/* 3675 */       return (Task<List<Member>>)new GatewayTask(CompletableFuture.completedFuture(Collections.emptyList()), () -> {
/*      */           
/* 3677 */           });  long[] arr = new long[ids.length];
/* 3678 */     for (int i = 0; i < ids.length; i++)
/* 3679 */       arr[i] = MiscUtil.parseSnowflake(ids[i]); 
/* 3680 */     return retrieveMembersByIds(includePresence, arr);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<Void> kickVoiceMember(@Nonnull Member member) {
/* 3839 */     return moveVoiceMember(member, null);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditableRestAction<Integer> prune(int days, @Nonnull Role... roles) {
/* 3924 */     return prune(days, true, roles);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default AuditableRestAction<Void> kick(@Nonnull Member member) {
/* 4076 */     return kick(member, (String)null);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default AuditableRestAction<Void> kick(@Nonnull String userId) {
/* 4112 */     return kick(userId, (String)null);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default AuditableRestAction<Void> ban(@Nonnull Member member, int delDays, @Nullable String reason) {
/* 4257 */     Checks.notNull(member, "Member");
/*      */ 
/*      */     
/* 4260 */     return ban(member.getUser(), delDays, reason);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default AuditableRestAction<Void> ban(@Nonnull Member member, int delDays) {
/* 4307 */     return ban(member, delDays, (String)null);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default AuditableRestAction<Void> ban(@Nonnull User user, int delDays) {
/* 4354 */     return ban(user, delDays, (String)null);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default AuditableRestAction<Void> ban(@Nonnull String userId, int delDays) {
/* 4401 */     return ban(userId, delDays, (String)null);
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
/*      */   default AuditableRestAction<Void> unban(@Nonnull User user) {
/* 4431 */     Checks.notNull(user, "User");
/*      */     
/* 4433 */     return unban(user.getId());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   default AuditableRestAction<Void> addRoleToMember(long userId, @Nonnull Role role) {
/* 4627 */     Checks.notNull(role, "Role");
/* 4628 */     Checks.check(role.getGuild().equals(this), "Role must be from the same guild! Trying to use role from %s in %s", new Object[] { role.getGuild().toString(), toString() });
/*      */     
/* 4630 */     Member member = getMemberById(userId);
/* 4631 */     if (member != null)
/* 4632 */       return addRoleToMember(member, role); 
/* 4633 */     if (!getSelfMember().hasPermission(new Permission[] { Permission.MANAGE_ROLES }))
/* 4634 */       throw new InsufficientPermissionException(this, Permission.MANAGE_ROLES); 
/* 4635 */     if (!getSelfMember().canInteract(role))
/* 4636 */       throw new HierarchyException("Can't modify a role with higher or equal highest role than yourself! Role: " + role.toString()); 
/* 4637 */     Route.CompiledRoute route = Route.Guilds.ADD_MEMBER_ROLE.compile(new String[] { getId(), Long.toUnsignedString(userId), role.getId() });
/* 4638 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl(getJDA(), route);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default AuditableRestAction<Void> addRoleToMember(@Nonnull String userId, @Nonnull Role role) {
/* 4685 */     return addRoleToMember(MiscUtil.parseSnowflake(userId), role);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default AuditableRestAction<Void> removeRoleFromMember(long userId, @Nonnull Role role) {
/* 4776 */     Checks.notNull(role, "Role");
/* 4777 */     Checks.check(role.getGuild().equals(this), "Role must be from the same guild! Trying to use role from %s in %s", new Object[] { role.getGuild().toString(), toString() });
/*      */     
/* 4779 */     Member member = getMemberById(userId);
/* 4780 */     if (member != null)
/* 4781 */       return removeRoleFromMember(member, role); 
/* 4782 */     if (!getSelfMember().hasPermission(new Permission[] { Permission.MANAGE_ROLES }))
/* 4783 */       throw new InsufficientPermissionException(this, Permission.MANAGE_ROLES); 
/* 4784 */     if (!getSelfMember().canInteract(role))
/* 4785 */       throw new HierarchyException("Can't modify a role with higher or equal highest role than yourself! Role: " + role.toString()); 
/* 4786 */     Route.CompiledRoute route = Route.Guilds.REMOVE_MEMBER_ROLE.compile(new String[] { getId(), Long.toUnsignedString(userId), role.getId() });
/* 4787 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl(getJDA(), route);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default AuditableRestAction<Void> removeRoleFromMember(@Nonnull String userId, @Nonnull Role role) {
/* 4834 */     return removeRoleFromMember(MiscUtil.parseSnowflake(userId), role);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditableRestAction<Void> modifyMemberRoles(@Nonnull Member member, @Nonnull Role... roles) {
/* 4964 */     return modifyMemberRoles(member, Arrays.asList(roles));
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default ChannelAction<TextChannel> createTextChannel(@Nonnull String name) {
/* 5088 */     return createTextChannel(name, null);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default ChannelAction<VoiceChannel> createVoiceChannel(@Nonnull String name) {
/* 5152 */     return createVoiceChannel(name, null);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default ChannelAction<StageChannel> createStageChannel(@Nonnull String name) {
/* 5216 */     return createStageChannel(name, null);
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
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default <T extends GuildChannel> ChannelAction<T> createCopyOfChannel(@Nonnull T channel) {
/* 5328 */     Checks.notNull(channel, "Channel");
/* 5329 */     return (ChannelAction)channel.createCopy(this);
/*      */   }
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<List<Command>> retrieveCommands();
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Command> retrieveCommandById(@Nonnull String paramString);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   CommandCreateAction upsertCommand(@Nonnull CommandData paramCommandData);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   CommandListUpdateAction updateCommands();
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   CommandEditAction editCommandById(@Nonnull String paramString);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Void> deleteCommandById(@Nonnull String paramString);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<List<CommandPrivilege>> retrieveCommandPrivilegesById(@Nonnull String paramString);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Map<String, List<CommandPrivilege>>> retrieveCommandPrivileges();
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<List<CommandPrivilege>> updateCommandPrivilegesById(@Nonnull String paramString, @Nonnull Collection<? extends CommandPrivilege> paramCollection);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Map<String, List<CommandPrivilege>>> updateCommandPrivileges(@Nonnull Map<String, Collection<? extends CommandPrivilege>> paramMap);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<EnumSet<Region>> retrieveRegions(boolean paramBoolean);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   MemberAction addMember(@Nonnull String paramString1, @Nonnull String paramString2);
/*      */   boolean isLoaded();
/*      */   void pruneMemberCache();
/*      */   boolean unloadMember(long paramLong);
/*      */   int getMemberCount();
/*      */   @Nonnull
/*      */   String getName();
/*      */   @Nullable
/*      */   String getIconId();
/*      */   @Nonnull
/*      */   Set<String> getFeatures();
/*      */   @Nullable
/*      */   String getSplashId();
/*      */   
/*      */   @Nonnull
/*      */   @Deprecated
/*      */   @ForRemoval(deadline = "4.4.0")
/*      */   @DeprecatedSince("4.0.0")
/*      */   @ReplaceWith("getVanityCode()")
/*      */   @CheckReturnValue
/*      */   RestAction<String> retrieveVanityUrl();
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RoleAction createCopyOfRole(@Nonnull Role role) {
/* 5391 */     Checks.notNull(role, "Role");
/* 5392 */     return role.createCopy(this);
/*      */   } @Nullable
/*      */   String getVanityCode(); @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<VanityInvite> retrieveVanityInvite(); @Nullable
/*      */   String getDescription(); @Nonnull
/*      */   Locale getLocale(); @Nullable
/*      */   String getBannerId();
/*      */   @Nonnull
/*      */   BoostTier getBoostTier();
/*      */   int getBoostCount();
/*      */   @Nonnull
/*      */   List<Member> getBoosters();
/*      */   int getMaxMembers();
/*      */   int getMaxPresences();
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<MetaData> retrieveMetaData();
/*      */   @Nullable
/*      */   VoiceChannel getAfkChannel();
/*      */   @Nullable
/*      */   TextChannel getSystemChannel();
/*      */   @Nullable
/*      */   TextChannel getRulesChannel();
/*      */   @Nullable
/*      */   TextChannel getCommunityUpdatesChannel();
/*      */   @Nullable
/*      */   Member getOwner();
/*      */   long getOwnerIdLong();
/*      */   @Nonnull
/*      */   Timeout getAfkTimeout();
/*      */   @Deprecated
/*      */   @ForRemoval(deadline = "5.0.0")
/*      */   @ReplaceWith("VoiceChannel.getRegionRaw()")
/*      */   @DeprecatedSince("4.3.0")
/*      */   @Nonnull
/*      */   String getRegionRaw();
/*      */   boolean isMember(@Nonnull User paramUser);
/*      */   @Nonnull
/*      */   Member getSelfMember();
/*      */   @Nonnull
/*      */   NSFWLevel getNSFWLevel();
/*      */   @Nullable
/*      */   Member getMember(@Nonnull User paramUser);
/*      */   @Nonnull
/*      */   MemberCacheView getMemberCache();
/*      */   @Nonnull
/*      */   SortedSnowflakeCacheView<Category> getCategoryCache();
/*      */   @Nonnull
/*      */   SortedSnowflakeCacheView<StoreChannel> getStoreChannelCache();
/*      */   @Nonnull
/*      */   SortedSnowflakeCacheView<TextChannel> getTextChannelCache();
/*      */   @Nonnull
/*      */   SortedSnowflakeCacheView<VoiceChannel> getVoiceChannelCache();
/*      */   @Nonnull
/*      */   List<GuildChannel> getChannels(boolean paramBoolean);
/*      */   @Nonnull
/*      */   SortedSnowflakeCacheView<Role> getRoleCache();
/*      */   @Nonnull
/*      */   SnowflakeCacheView<Emote> getEmoteCache();
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<List<ListedEmote>> retrieveEmotes();
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<ListedEmote> retrieveEmoteById(@Nonnull String paramString);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<List<Ban>> retrieveBanList();
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Ban> retrieveBanById(@Nonnull String paramString);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Integer> retrievePrunableMemberCount(int paramInt);
/*      */   @Nonnull
/*      */   Role getPublicRole();
/*      */   @Nullable
/*      */   TextChannel getDefaultChannel();
/*      */   @Nonnull
/*      */   GuildManager getManager();
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditLogPaginationAction retrieveAuditLogs();
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Void> leave();
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Void> delete();
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Void> delete(@Nullable String paramString);
/*      */   @Nonnull
/*      */   AudioManager getAudioManager();
/*      */   @Nonnull
/*      */   Task<Void> requestToSpeak();
/*      */   @Nonnull
/*      */   Task<Void> cancelRequestToSpeak();
/*      */   @Nonnull
/*      */   JDA getJDA();
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<List<Invite>> retrieveInvites();
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<List<Template>> retrieveTemplates();
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Template> createTemplate(@Nonnull String paramString1, @Nullable String paramString2);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<List<Webhook>> retrieveWebhooks();
/*      */   @Nonnull
/*      */   List<GuildVoiceState> getVoiceStates();
/*      */   @Nonnull
/*      */   VerificationLevel getVerificationLevel();
/*      */   @Nonnull
/*      */   NotificationLevel getDefaultNotificationLevel();
/*      */   @Nonnull
/*      */   MFALevel getRequiredMFALevel();
/*      */   @Nonnull
/*      */   ExplicitContentLevel getExplicitContentLevel();
/*      */   @Deprecated
/*      */   @ForRemoval(deadline = "4.4.0")
/*      */   @DeprecatedSince("4.2.0")
/*      */   boolean checkVerification();
/*      */   @ForRemoval(deadline = "4.4.0")
/*      */   @Deprecated
/*      */   @DeprecatedSince("4.1.0")
/*      */   @ReplaceWith("getJDA().isUnavailable(guild.getIdLong())")
/*      */   boolean isAvailable();
/*      */   @Nonnull
/*      */   @Deprecated
/*      */   @ForRemoval(deadline = "5.0.0")
/*      */   @DeprecatedSince("4.2.0")
/*      */   @ReplaceWith("loadMembers(Consumer<Member>) or loadMembers()")
/*      */   CompletableFuture<Void> retrieveMembers();
/*      */   @Nonnull
/*      */   Task<Void> loadMembers(@Nonnull Consumer<Member> paramConsumer);
/*      */   @Nonnull
/*      */   RestAction<Member> retrieveMemberById(long paramLong, boolean paramBoolean);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   Task<List<Member>> retrieveMembersByIds(boolean paramBoolean, @Nonnull long... paramVarArgs);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   Task<List<Member>> retrieveMembersByPrefix(@Nonnull String paramString, int paramInt);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Void> moveVoiceMember(@Nonnull Member paramMember, @Nullable VoiceChannel paramVoiceChannel);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditableRestAction<Void> modifyNickname(@Nonnull Member paramMember, @Nullable String paramString);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditableRestAction<Integer> prune(int paramInt, boolean paramBoolean, @Nonnull Role... paramVarArgs);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditableRestAction<Void> kick(@Nonnull Member paramMember, @Nullable String paramString);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditableRestAction<Void> kick(@Nonnull String paramString1, @Nullable String paramString2);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditableRestAction<Void> ban(@Nonnull User paramUser, int paramInt, @Nullable String paramString);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditableRestAction<Void> ban(@Nonnull String paramString1, int paramInt, @Nullable String paramString2);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditableRestAction<Void> unban(@Nonnull String paramString);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditableRestAction<Void> deafen(@Nonnull Member paramMember, boolean paramBoolean);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditableRestAction<Void> mute(@Nonnull Member paramMember, boolean paramBoolean);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditableRestAction<Void> addRoleToMember(@Nonnull Member paramMember, @Nonnull Role paramRole);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditableRestAction<Void> removeRoleFromMember(@Nonnull Member paramMember, @Nonnull Role paramRole);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditableRestAction<Void> modifyMemberRoles(@Nonnull Member paramMember, @Nullable Collection<Role> paramCollection1, @Nullable Collection<Role> paramCollection2);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditableRestAction<Void> modifyMemberRoles(@Nonnull Member paramMember, @Nonnull Collection<Role> paramCollection);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RoleOrderAction modifyRolePositions() {
/* 5585 */     return modifyRolePositions(true);
/*      */   }
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditableRestAction<Void> transferOwnership(@Nonnull Member paramMember);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   ChannelAction<TextChannel> createTextChannel(@Nonnull String paramString, @Nullable Category paramCategory);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   ChannelAction<VoiceChannel> createVoiceChannel(@Nonnull String paramString, @Nullable Category paramCategory);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   ChannelAction<StageChannel> createStageChannel(@Nonnull String paramString, @Nullable Category paramCategory);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   ChannelAction<Category> createCategory(@Nonnull String paramString);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RoleAction createRole();
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditableRestAction<Emote> createEmote(@Nonnull String paramString, @Nonnull Icon paramIcon, @Nonnull Role... paramVarArgs);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   ChannelOrderAction modifyCategoryPositions();
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   ChannelOrderAction modifyTextChannelPositions();
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   ChannelOrderAction modifyVoiceChannelPositions();
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   CategoryOrderAction modifyTextChannelPositions(@Nonnull Category paramCategory);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   CategoryOrderAction modifyVoiceChannelPositions(@Nonnull Category paramCategory);
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RoleOrderAction modifyRolePositions(boolean paramBoolean);
/* 5626 */   public enum Timeout { SECONDS_60(60),
/* 5627 */     SECONDS_300(300),
/* 5628 */     SECONDS_900(900),
/* 5629 */     SECONDS_1800(1800),
/* 5630 */     SECONDS_3600(3600);
/*      */     
/*      */     private final int seconds;
/*      */ 
/*      */     
/*      */     Timeout(int seconds) {
/* 5636 */       this.seconds = seconds;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getSeconds() {
/* 5646 */       return this.seconds;
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
/*      */     
/*      */     @Nonnull
/*      */     public static Timeout fromKey(int seconds) {
/* 5664 */       for (Timeout t : values()) {
/*      */         
/* 5666 */         if (t.getSeconds() == seconds)
/* 5667 */           return t; 
/*      */       } 
/* 5669 */       throw new IllegalArgumentException("Provided key was not recognized. Seconds: " + seconds);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum VerificationLevel
/*      */   {
/* 5685 */     NONE(0),
/* 5686 */     LOW(1),
/* 5687 */     MEDIUM(2),
/* 5688 */     HIGH(3),
/* 5689 */     VERY_HIGH(4),
/* 5690 */     UNKNOWN(-1);
/*      */     
/*      */     private final int key;
/*      */ 
/*      */     
/*      */     VerificationLevel(int key) {
/* 5696 */       this.key = key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getKey() {
/* 5706 */       return this.key;
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
/*      */     @Nonnull
/*      */     public static VerificationLevel fromKey(int key) {
/* 5721 */       for (VerificationLevel level : values()) {
/*      */         
/* 5723 */         if (level.getKey() == key)
/* 5724 */           return level; 
/*      */       } 
/* 5726 */       return UNKNOWN;
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
/*      */   public enum NotificationLevel
/*      */   {
/* 5739 */     ALL_MESSAGES(0),
/* 5740 */     MENTIONS_ONLY(1),
/* 5741 */     UNKNOWN(-1);
/*      */     
/*      */     private final int key;
/*      */ 
/*      */     
/*      */     NotificationLevel(int key) {
/* 5747 */       this.key = key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getKey() {
/* 5757 */       return this.key;
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
/*      */     @Nonnull
/*      */     public static NotificationLevel fromKey(int key) {
/* 5772 */       for (NotificationLevel level : values()) {
/*      */         
/* 5774 */         if (level.getKey() == key)
/* 5775 */           return level; 
/*      */       } 
/* 5777 */       return UNKNOWN;
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
/*      */   public enum MFALevel
/*      */   {
/* 5790 */     NONE(0),
/* 5791 */     TWO_FACTOR_AUTH(1),
/* 5792 */     UNKNOWN(-1);
/*      */     
/*      */     private final int key;
/*      */ 
/*      */     
/*      */     MFALevel(int key) {
/* 5798 */       this.key = key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getKey() {
/* 5808 */       return this.key;
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
/*      */     @Nonnull
/*      */     public static MFALevel fromKey(int key) {
/* 5823 */       for (MFALevel level : values()) {
/*      */         
/* 5825 */         if (level.getKey() == key)
/* 5826 */           return level; 
/*      */       } 
/* 5828 */       return UNKNOWN;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum ExplicitContentLevel
/*      */   {
/* 5838 */     OFF(0, "Don't scan any messages."),
/* 5839 */     NO_ROLE(1, "Scan messages from members without a role."),
/* 5840 */     ALL(2, "Scan messages sent by all members."),
/*      */     
/* 5842 */     UNKNOWN(-1, "Unknown filter level!");
/*      */     
/*      */     private final int key;
/*      */     
/*      */     private final String description;
/*      */     
/*      */     ExplicitContentLevel(int key, String description) {
/* 5849 */       this.key = key;
/* 5850 */       this.description = description;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getKey() {
/* 5860 */       return this.key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nonnull
/*      */     public String getDescription() {
/* 5871 */       return this.description;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nonnull
/*      */     public static ExplicitContentLevel fromKey(int key) {
/* 5877 */       for (ExplicitContentLevel level : values()) {
/*      */         
/* 5879 */         if (level.key == key)
/* 5880 */           return level; 
/*      */       } 
/* 5882 */       return UNKNOWN;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum NSFWLevel
/*      */   {
/* 5894 */     DEFAULT(0),
/*      */ 
/*      */ 
/*      */     
/* 5898 */     EXPLICIT(1),
/*      */ 
/*      */ 
/*      */     
/* 5902 */     SAFE(2),
/*      */ 
/*      */ 
/*      */     
/* 5906 */     AGE_RESTRICTED(3),
/*      */ 
/*      */ 
/*      */     
/* 5910 */     UNKNOWN(-1);
/*      */     
/*      */     private final int key;
/*      */ 
/*      */     
/*      */     NSFWLevel(int key) {
/* 5916 */       this.key = key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getKey() {
/* 5926 */       return this.key;
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
/*      */     @Nonnull
/*      */     public static NSFWLevel fromKey(int key) {
/* 5941 */       for (NSFWLevel level : values()) {
/*      */         
/* 5943 */         if (level.getKey() == key)
/* 5944 */           return level; 
/*      */       } 
/* 5946 */       return UNKNOWN;
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
/*      */   public enum BoostTier
/*      */   {
/* 5962 */     NONE(0, 96000, 50),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5967 */     TIER_1(1, 128000, 100),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5972 */     TIER_2(2, 256000, 150),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5977 */     TIER_3(3, 384000, 250),
/*      */ 
/*      */ 
/*      */     
/* 5981 */     UNKNOWN(-1, 2147483647, 2147483647);
/*      */     
/*      */     private final int key;
/*      */     
/*      */     private final int maxBitrate;
/*      */     private final int maxEmotes;
/*      */     
/*      */     BoostTier(int key, int maxBitrate, int maxEmotes) {
/* 5989 */       this.key = key;
/* 5990 */       this.maxBitrate = maxBitrate;
/* 5991 */       this.maxEmotes = maxEmotes;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getKey() {
/* 6001 */       return this.key;
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
/*      */     public int getMaxBitrate() {
/* 6013 */       return this.maxBitrate;
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
/*      */     public int getMaxEmotes() {
/* 6025 */       return this.maxEmotes;
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
/*      */     public long getMaxFileSize() {
/* 6037 */       if (this.key == 2)
/* 6038 */         return 52428800L; 
/* 6039 */       if (this.key == 3)
/* 6040 */         return 104857600L; 
/* 6041 */       return 8388608L;
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
/*      */     @Nonnull
/*      */     public static BoostTier fromKey(int key) {
/* 6055 */       for (BoostTier tier : values()) {
/*      */         
/* 6057 */         if (tier.key == key)
/* 6058 */           return tier; 
/*      */       } 
/* 6060 */       return UNKNOWN;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Ban
/*      */   {
/*      */     protected final User user;
/*      */ 
/*      */     
/*      */     protected final String reason;
/*      */ 
/*      */ 
/*      */     
/*      */     public Ban(User user, String reason) {
/* 6077 */       this.user = user;
/* 6078 */       this.reason = reason;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nonnull
/*      */     public User getUser() {
/* 6089 */       return this.user;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public String getReason() {
/* 6100 */       return this.reason;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 6106 */       return "GuildBan:" + this.user + ((this.reason == null) ? "" : ('(' + this.reason + ')'));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class MetaData
/*      */   {
/*      */     private final int memberLimit;
/*      */     
/*      */     private final int presenceLimit;
/*      */     
/*      */     private final int approximatePresences;
/*      */     
/*      */     private final int approximateMembers;
/*      */ 
/*      */     
/*      */     public MetaData(int memberLimit, int presenceLimit, int approximatePresences, int approximateMembers) {
/* 6124 */       this.memberLimit = memberLimit;
/* 6125 */       this.presenceLimit = presenceLimit;
/* 6126 */       this.approximatePresences = approximatePresences;
/* 6127 */       this.approximateMembers = approximateMembers;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getMemberLimit() {
/* 6138 */       return this.memberLimit;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getPresenceLimit() {
/* 6149 */       return this.presenceLimit;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getApproximatePresences() {
/* 6159 */       return this.approximatePresences;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getApproximateMembers() {
/* 6169 */       return this.approximateMembers;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\Guild.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */