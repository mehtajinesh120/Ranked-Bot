/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.Permission;
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
/*     */ public interface IPermissionHolder
/*     */   extends ISnowflake
/*     */ {
/*     */   @Nonnull
/*     */   Guild getGuild();
/*     */   
/*     */   @Nonnull
/*     */   EnumSet<Permission> getPermissions();
/*     */   
/*     */   @Nonnull
/*     */   EnumSet<Permission> getPermissions(@Nonnull GuildChannel paramGuildChannel);
/*     */   
/*     */   @Nonnull
/*     */   EnumSet<Permission> getPermissionsExplicit();
/*     */   
/*     */   @Nonnull
/*     */   EnumSet<Permission> getPermissionsExplicit(@Nonnull GuildChannel paramGuildChannel);
/*     */   
/*     */   boolean hasPermission(@Nonnull Permission... paramVarArgs);
/*     */   
/*     */   boolean hasPermission(@Nonnull Collection<Permission> paramCollection);
/*     */   
/*     */   boolean hasPermission(@Nonnull GuildChannel paramGuildChannel, @Nonnull Permission... paramVarArgs);
/*     */   
/*     */   boolean hasPermission(@Nonnull GuildChannel paramGuildChannel, @Nonnull Collection<Permission> paramCollection);
/*     */   
/*     */   default boolean hasAccess(@Nonnull GuildChannel channel) {
/* 175 */     Checks.notNull(channel, "Channel");
/* 176 */     return (channel.getType() == ChannelType.VOICE || channel.getType() == ChannelType.STAGE) ? 
/* 177 */       hasPermission(channel, new Permission[] { Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL
/* 178 */         }) : hasPermission(channel, new Permission[] { Permission.VIEW_CHANNEL });
/*     */   }
/*     */   
/*     */   boolean canSync(@Nonnull GuildChannel paramGuildChannel1, @Nonnull GuildChannel paramGuildChannel2);
/*     */   
/*     */   boolean canSync(@Nonnull GuildChannel paramGuildChannel);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\IPermissionHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */