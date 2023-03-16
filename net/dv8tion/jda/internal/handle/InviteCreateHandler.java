/*     */ package net.dv8tion.jda.internal.handle;
/*     */ 
/*     */ import java.time.OffsetDateTime;
/*     */ import java.util.Optional;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.Invite;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.InviteImpl;
/*     */ import net.dv8tion.jda.internal.entities.UserImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InviteCreateHandler
/*     */   extends SocketHandler
/*     */ {
/*     */   public InviteCreateHandler(JDAImpl api) {
/*  35 */     super(api); } protected Long handleInternally(DataObject content) {
/*     */     InviteImpl.InviteTargetImpl inviteTargetImpl3, inviteTargetImpl2;
/*     */     Invite.InviteTarget target;
/*     */     InviteImpl.InviteTargetImpl inviteTargetImpl1;
/*     */     DataObject targetUserObject, applicationObject;
/*     */     InviteImpl.EmbeddedApplicationImpl embeddedApplicationImpl;
/*  41 */     long guildId = content.getUnsignedLong("guild_id");
/*  42 */     if (getJDA().getGuildSetupController().isLocked(guildId))
/*  43 */       return Long.valueOf(guildId); 
/*  44 */     Guild realGuild = getJDA().getGuildById(guildId);
/*  45 */     if (realGuild == null) {
/*     */       
/*  47 */       EventCache.LOG.debug("Caching INVITE_CREATE for unknown guild with id {}", Long.valueOf(guildId));
/*  48 */       getJDA().getEventCache().cache(EventCache.Type.GUILD, guildId, this.responseNumber, this.allContent, this::handle);
/*  49 */       return null;
/*     */     } 
/*     */     
/*  52 */     long channelId = content.getUnsignedLong("channel_id");
/*  53 */     GuildChannel realChannel = realGuild.getGuildChannelById(channelId);
/*  54 */     if (realChannel == null) {
/*     */       
/*  56 */       EventCache.LOG.debug("Caching INVITE_CREATE for unknown channel with id {} in guild with id {}", Long.valueOf(channelId), Long.valueOf(guildId));
/*  57 */       getJDA().getEventCache().cache(EventCache.Type.CHANNEL, channelId, this.responseNumber, this.allContent, this::handle);
/*  58 */       return null;
/*     */     } 
/*     */     
/*  61 */     String code = content.getString("code");
/*  62 */     boolean temporary = content.getBoolean("temporary");
/*  63 */     int maxAge = content.getInt("max_age", -1);
/*  64 */     int maxUses = content.getInt("max_uses", -1);
/*     */ 
/*     */ 
/*     */     
/*  68 */     OffsetDateTime creationTime = content.opt("created_at").map(String::valueOf).map(OffsetDateTime::parse).orElse(null);
/*     */     
/*  70 */     Optional<DataObject> inviterJson = content.optObject("inviter");
/*  71 */     boolean expanded = (maxUses != -1);
/*     */     
/*  73 */     User inviter = inviterJson.<User>map(json -> getJDA().getEntityBuilder().createUser(json)).orElse(null);
/*  74 */     InviteImpl.ChannelImpl channel = new InviteImpl.ChannelImpl(realChannel);
/*  75 */     InviteImpl.GuildImpl guild = new InviteImpl.GuildImpl(realGuild);
/*     */     
/*  77 */     Invite.TargetType targetType = Invite.TargetType.fromId(content.getInt("target_type", 0));
/*     */ 
/*     */     
/*  80 */     switch (targetType) {
/*     */       
/*     */       case STREAM:
/*  83 */         targetUserObject = content.getObject("target_user");
/*  84 */         inviteTargetImpl3 = new InviteImpl.InviteTargetImpl(targetType, null, (User)getJDA().getEntityBuilder().createUser(targetUserObject));
/*     */         break;
/*     */       case EMBEDDED_APPLICATION:
/*  87 */         applicationObject = content.getObject("target_application");
/*     */ 
/*     */         
/*  90 */         embeddedApplicationImpl = new InviteImpl.EmbeddedApplicationImpl(applicationObject.getString("icon", null), applicationObject.getString("name"), applicationObject.getString("description"), applicationObject.getString("summary"), applicationObject.getLong("id"), applicationObject.getInt("max_participants", -1));
/*     */         
/*  92 */         inviteTargetImpl2 = new InviteImpl.InviteTargetImpl(targetType, (Invite.EmbeddedApplication)embeddedApplicationImpl, null);
/*     */         break;
/*     */       case NONE:
/*  95 */         target = null;
/*     */         break;
/*     */       default:
/*  98 */         inviteTargetImpl1 = new InviteImpl.InviteTargetImpl(targetType, null, null);
/*     */         break;
/*     */     } 
/* 101 */     InviteImpl inviteImpl = new InviteImpl(getJDA(), code, expanded, inviter, maxAge, maxUses, temporary, creationTime, 0, (Invite.Channel)channel, (Invite.Guild)guild, null, (Invite.InviteTarget)inviteTargetImpl1, Invite.InviteType.GUILD);
/*     */ 
/*     */     
/* 104 */     getJDA().handleEvent((GenericEvent)new GuildInviteCreateEvent((JDA)
/*     */           
/* 106 */           getJDA(), this.responseNumber, (Invite)inviteImpl, realChannel));
/*     */     
/* 108 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\InviteCreateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */