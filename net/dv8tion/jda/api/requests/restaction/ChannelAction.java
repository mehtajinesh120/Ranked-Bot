/*     */ package net.dv8tion.jda.api.requests.restaction;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Category;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.IPermissionHolder;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ChannelAction<T extends net.dv8tion.jda.api.entities.GuildChannel>
/*     */   extends AuditableRestAction<T>
/*     */ {
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default ChannelAction<T> addPermissionOverride(@Nonnull IPermissionHolder target, @Nullable Collection<Permission> allow, @Nullable Collection<Permission> deny) {
/* 250 */     long allowRaw = (allow != null) ? Permission.getRaw(allow) : 0L;
/* 251 */     long denyRaw = (deny != null) ? Permission.getRaw(deny) : 0L;
/*     */     
/* 253 */     return addPermissionOverride(target, allowRaw, denyRaw);
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default ChannelAction<T> addPermissionOverride(@Nonnull IPermissionHolder target, long allow, long deny) {
/* 299 */     Checks.notNull(target, "Override Role/Member");
/* 300 */     if (target instanceof net.dv8tion.jda.api.entities.Role)
/* 301 */       return addRolePermissionOverride(target.getIdLong(), allow, deny); 
/* 302 */     if (target instanceof net.dv8tion.jda.api.entities.Member)
/* 303 */       return addMemberPermissionOverride(target.getIdLong(), allow, deny); 
/* 304 */     throw new IllegalArgumentException("Cannot add override for " + target.getClass().getSimpleName());
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default ChannelAction<T> addMemberPermissionOverride(long memberId, @Nullable Collection<Permission> allow, @Nullable Collection<Permission> deny) {
/* 340 */     long allowRaw = (allow != null) ? Permission.getRaw(allow) : 0L;
/* 341 */     long denyRaw = (deny != null) ? Permission.getRaw(deny) : 0L;
/*     */     
/* 343 */     return addMemberPermissionOverride(memberId, allowRaw, denyRaw);
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default ChannelAction<T> addRolePermissionOverride(long roleId, @Nullable Collection<Permission> allow, @Nullable Collection<Permission> deny) {
/* 379 */     long allowRaw = (allow != null) ? Permission.getRaw(allow) : 0L;
/* 380 */     long denyRaw = (deny != null) ? Permission.getRaw(deny) : 0L;
/*     */     
/* 382 */     return addRolePermissionOverride(roleId, allowRaw, denyRaw);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   default ChannelAction<T> removePermissionOverride(@Nonnull String id) {
/* 490 */     return removePermissionOverride(MiscUtil.parseSnowflake(id));
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default ChannelAction<T> removePermissionOverride(@Nonnull IPermissionHolder holder) {
/* 509 */     Checks.notNull(holder, "PermissionHolder");
/* 510 */     return removePermissionOverride(holder.getIdLong());
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   ChannelAction<T> setCheck(@Nullable BooleanSupplier paramBooleanSupplier);
/*     */   
/*     */   @Nonnull
/*     */   ChannelAction<T> timeout(long paramLong, @Nonnull TimeUnit paramTimeUnit);
/*     */   
/*     */   @Nonnull
/*     */   ChannelAction<T> deadline(long paramLong);
/*     */   
/*     */   @Nonnull
/*     */   Guild getGuild();
/*     */   
/*     */   @Nonnull
/*     */   ChannelType getType();
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelAction<T> setName(@Nonnull String paramString);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelAction<T> setParent(@Nullable Category paramCategory);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelAction<T> setPosition(@Nullable Integer paramInteger);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelAction<T> setTopic(@Nullable String paramString);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelAction<T> setNSFW(boolean paramBoolean);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelAction<T> setSlowmode(int paramInt);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelAction<T> setNews(boolean paramBoolean);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelAction<T> addMemberPermissionOverride(long paramLong1, long paramLong2, long paramLong3);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelAction<T> addRolePermissionOverride(long paramLong1, long paramLong2, long paramLong3);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelAction<T> removePermissionOverride(long paramLong);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelAction<T> clearPermissionOverrides();
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelAction<T> syncPermissionOverrides();
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelAction<T> setBitrate(@Nullable Integer paramInteger);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelAction<T> setUserlimit(@Nullable Integer paramInteger);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\ChannelAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */