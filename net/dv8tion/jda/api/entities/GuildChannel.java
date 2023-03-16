/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.managers.ChannelManager;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.ChannelAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.InviteAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface GuildChannel
/*     */   extends AbstractChannel, IMentionable, Comparable<GuildChannel>
/*     */ {
/*     */   @Nonnull
/*     */   Guild getGuild();
/*     */   
/*     */   @Nullable
/*     */   Category getParent();
/*     */   
/*     */   @Nonnull
/*     */   List<Member> getMembers();
/*     */   
/*     */   int getPosition();
/*     */   
/*     */   int getPositionRaw();
/*     */   
/*     */   @Nullable
/*     */   PermissionOverride getPermissionOverride(@Nonnull IPermissionHolder paramIPermissionHolder);
/*     */   
/*     */   @Nonnull
/*     */   List<PermissionOverride> getPermissionOverrides();
/*     */   
/*     */   @Nonnull
/*     */   List<PermissionOverride> getMemberPermissionOverrides();
/*     */   
/*     */   @Nonnull
/*     */   List<PermissionOverride> getRolePermissionOverrides();
/*     */   
/*     */   boolean isSynced();
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   ChannelAction<? extends GuildChannel> createCopy(@Nonnull Guild paramGuild);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default ChannelAction<? extends GuildChannel> createCopy() {
/* 242 */     return createCopy(getGuild());
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
/*     */   @Nonnull
/*     */   ChannelManager getManager();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   AuditableRestAction<Void> delete();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   PermissionOverrideAction createPermissionOverride(@Nonnull IPermissionHolder paramIPermissionHolder);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   PermissionOverrideAction putPermissionOverride(@Nonnull IPermissionHolder paramIPermissionHolder);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   default PermissionOverrideAction upsertPermissionOverride(@Nonnull IPermissionHolder permissionHolder) {
/* 360 */     PermissionOverride override = getPermissionOverride(permissionHolder);
/* 361 */     if (override != null)
/* 362 */       return override.getManager(); 
/* 363 */     return putPermissionOverride(permissionHolder);
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   InviteAction createInvite();
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   RestAction<List<Invite>> retrieveInvites();
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\GuildChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */