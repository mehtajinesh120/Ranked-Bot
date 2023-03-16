/*     */ package net.dv8tion.jda.api.exceptions;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
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
/*     */ public class InsufficientPermissionException
/*     */   extends PermissionException
/*     */ {
/*     */   private final long guildId;
/*     */   private final long channelId;
/*     */   private final ChannelType channelType;
/*     */   
/*     */   public InsufficientPermissionException(@Nonnull Guild guild, @Nonnull Permission permission) {
/*  43 */     this(guild, (GuildChannel)null, permission);
/*     */   }
/*     */ 
/*     */   
/*     */   public InsufficientPermissionException(@Nonnull Guild guild, @Nonnull Permission permission, @Nonnull String reason) {
/*  48 */     this(guild, null, permission, reason);
/*     */   }
/*     */ 
/*     */   
/*     */   public InsufficientPermissionException(@Nonnull GuildChannel channel, @Nonnull Permission permission) {
/*  53 */     this(channel.getGuild(), channel, permission);
/*     */   }
/*     */ 
/*     */   
/*     */   public InsufficientPermissionException(@Nonnull GuildChannel channel, @Nonnull Permission permission, @Nonnull String reason) {
/*  58 */     this(channel.getGuild(), channel, permission, reason);
/*     */   }
/*     */ 
/*     */   
/*     */   private InsufficientPermissionException(@Nonnull Guild guild, @Nullable GuildChannel channel, @Nonnull Permission permission) {
/*  63 */     super(permission, "Cannot perform action due to a lack of Permission. Missing permission: " + permission.toString());
/*  64 */     this.guildId = guild.getIdLong();
/*  65 */     this.channelId = (channel == null) ? 0L : channel.getIdLong();
/*  66 */     this.channelType = (channel == null) ? ChannelType.UNKNOWN : channel.getType();
/*     */   }
/*     */ 
/*     */   
/*     */   private InsufficientPermissionException(@Nonnull Guild guild, @Nullable GuildChannel channel, @Nonnull Permission permission, @Nonnull String reason) {
/*  71 */     super(permission, reason);
/*  72 */     this.guildId = guild.getIdLong();
/*  73 */     this.channelId = (channel == null) ? 0L : channel.getIdLong();
/*  74 */     this.channelType = (channel == null) ? ChannelType.UNKNOWN : channel.getType();
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
/*     */   public long getGuildId() {
/*  88 */     return this.guildId;
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
/*     */   public long getChannelId() {
/* 102 */     return this.channelId;
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
/*     */   public ChannelType getChannelType() {
/* 115 */     return this.channelType;
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
/*     */   @Nullable
/*     */   public Guild getGuild(@Nonnull JDA api) {
/* 134 */     Checks.notNull(api, "JDA");
/* 135 */     return api.getGuildById(this.guildId);
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
/*     */   @Nullable
/*     */   public GuildChannel getChannel(@Nonnull JDA api) {
/* 154 */     Checks.notNull(api, "JDA");
/* 155 */     return api.getGuildChannelById(this.channelType, this.channelId);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\exceptions\InsufficientPermissionException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */