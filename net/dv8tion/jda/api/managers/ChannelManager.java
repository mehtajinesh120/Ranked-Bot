/*     */ package net.dv8tion.jda.api.managers;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.Region;
/*     */ import net.dv8tion.jda.api.entities.Category;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.IPermissionHolder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ChannelManager
/*     */   extends Manager<ChannelManager>
/*     */ {
/*     */   public static final long NAME = 1L;
/*     */   public static final long PARENT = 2L;
/*     */   public static final long TOPIC = 4L;
/*     */   public static final long POSITION = 8L;
/*     */   public static final long NSFW = 16L;
/*     */   public static final long USERLIMIT = 32L;
/*     */   public static final long BITRATE = 64L;
/*     */   public static final long PERMISSION = 128L;
/*     */   public static final long SLOWMODE = 256L;
/*     */   public static final long NEWS = 512L;
/*     */   public static final long REGION = 1024L;
/*     */   
/*     */   @Nonnull
/*     */   default ChannelType getType() {
/* 144 */     return getChannel().getType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   default Guild getGuild() {
/* 157 */     return getChannel().getGuild();
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default ChannelManager putPermissionOverride(@Nonnull IPermissionHolder permHolder, @Nullable Collection<Permission> allow, @Nullable Collection<Permission> deny) {
/* 233 */     long allowRaw = (allow == null) ? 0L : Permission.getRaw(allow);
/* 234 */     long denyRaw = (deny == null) ? 0L : Permission.getRaw(deny);
/* 235 */     return putPermissionOverride(permHolder, allowRaw, denyRaw);
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default ChannelManager putRolePermissionOverride(long roleId, @Nullable Collection<Permission> allow, @Nullable Collection<Permission> deny) {
/* 286 */     long allowRaw = (allow == null) ? 0L : Permission.getRaw(allow);
/* 287 */     long denyRaw = (deny == null) ? 0L : Permission.getRaw(deny);
/* 288 */     return putRolePermissionOverride(roleId, allowRaw, denyRaw);
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default ChannelManager putMemberPermissionOverride(long memberId, @Nullable Collection<Permission> allow, @Nullable Collection<Permission> deny) {
/* 339 */     long allowRaw = (allow == null) ? 0L : Permission.getRaw(allow);
/* 340 */     long denyRaw = (deny == null) ? 0L : Permission.getRaw(deny);
/* 341 */     return putMemberPermissionOverride(memberId, allowRaw, denyRaw);
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default ChannelManager sync() {
/* 405 */     if (getChannel().getParent() == null)
/* 406 */       throw new IllegalStateException("sync() requires a parent category"); 
/* 407 */     return sync((GuildChannel)getChannel().getParent());
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   ChannelManager reset(long paramLong);
/*     */   
/*     */   @Nonnull
/*     */   ChannelManager reset(long... paramVarArgs);
/*     */   
/*     */   @Nonnull
/*     */   GuildChannel getChannel();
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelManager clearOverridesAdded();
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelManager clearOverridesRemoved();
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelManager putPermissionOverride(@Nonnull IPermissionHolder paramIPermissionHolder, long paramLong1, long paramLong2);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelManager putRolePermissionOverride(long paramLong1, long paramLong2, long paramLong3);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelManager putMemberPermissionOverride(long paramLong1, long paramLong2, long paramLong3);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelManager removePermissionOverride(@Nonnull IPermissionHolder paramIPermissionHolder);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelManager removePermissionOverride(long paramLong);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelManager sync(@Nonnull GuildChannel paramGuildChannel);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelManager setName(@Nonnull String paramString);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelManager setParent(@Nullable Category paramCategory);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelManager setPosition(int paramInt);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelManager setTopic(@Nullable String paramString);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelManager setNSFW(boolean paramBoolean);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelManager setSlowmode(int paramInt);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelManager setUserLimit(int paramInt);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelManager setBitrate(int paramInt);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelManager setRegion(Region paramRegion);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelManager setNews(boolean paramBoolean);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\managers\ChannelManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */