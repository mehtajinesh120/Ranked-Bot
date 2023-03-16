/*     */ package net.dv8tion.jda.internal.handle;
/*     */ 
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
/*     */ import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
/*     */ import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
/*     */ import net.dv8tion.jda.api.utils.cache.CacheView;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import net.dv8tion.jda.internal.entities.GuildVoiceStateImpl;
/*     */ import net.dv8tion.jda.internal.entities.MemberImpl;
/*     */ import net.dv8tion.jda.internal.entities.MemberPresenceImpl;
/*     */ import net.dv8tion.jda.internal.entities.UserImpl;
/*     */ import net.dv8tion.jda.internal.entities.VoiceChannelImpl;
/*     */ import net.dv8tion.jda.internal.utils.UnlockHook;
/*     */ import net.dv8tion.jda.internal.utils.cache.SnowflakeCacheViewImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuildMemberRemoveHandler
/*     */   extends SocketHandler
/*     */ {
/*     */   public GuildMemberRemoveHandler(JDAImpl api) {
/*  36 */     super(api);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Long handleInternally(DataObject content) {
/*  43 */     long id = content.getLong("guild_id");
/*  44 */     boolean setup = getJDA().getGuildSetupController().onRemoveMember(id, content);
/*  45 */     if (setup) {
/*  46 */       return null;
/*     */     }
/*  48 */     GuildImpl guild = (GuildImpl)getJDA().getGuildsView().get(id);
/*  49 */     if (guild == null)
/*     */     {
/*     */       
/*  52 */       return null;
/*     */     }
/*     */     
/*  55 */     long userId = content.getObject("user").getUnsignedLong("id");
/*  56 */     if (userId == getJDA().getSelfUser().getIdLong())
/*     */     {
/*     */       
/*  59 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  63 */     guild.onMemberRemove();
/*  64 */     CacheView.SimpleCacheView<MemberPresenceImpl> presences = guild.getPresenceView();
/*  65 */     if (presences != null) {
/*  66 */       presences.remove(userId);
/*     */     }
/*  68 */     UserImpl userImpl = this.api.getEntityBuilder().createUser(content.getObject("user"));
/*  69 */     MemberImpl member = (MemberImpl)guild.getMembersView().remove(userId);
/*     */     
/*  71 */     if (member == null) {
/*     */ 
/*     */ 
/*     */       
/*  75 */       guild.getVoiceChannelsView().forEachUnordered(channel -> {
/*     */             VoiceChannelImpl impl = (VoiceChannelImpl)channel;
/*     */ 
/*     */             
/*     */             Member connected = (Member)impl.getConnectedMembersMap().remove(userId);
/*     */ 
/*     */             
/*     */             if (connected != null) {
/*     */               getJDA().handleEvent((GenericEvent)new GuildVoiceLeaveEvent((JDA)getJDA(), this.responseNumber, connected, channel));
/*     */             }
/*     */           });
/*     */ 
/*     */       
/*  88 */       getJDA().handleEvent((GenericEvent)new GuildMemberRemoveEvent((JDA)
/*     */             
/*  90 */             getJDA(), this.responseNumber, (Guild)guild, (User)userImpl, null));
/*     */       
/*  92 */       return null;
/*     */     } 
/*     */     
/*  95 */     GuildVoiceStateImpl voiceState = (GuildVoiceStateImpl)member.getVoiceState();
/*  96 */     if (voiceState != null && voiceState.inVoiceChannel()) {
/*     */       
/*  98 */       VoiceChannel channel = voiceState.getChannel();
/*  99 */       voiceState.setConnectedChannel(null);
/* 100 */       ((VoiceChannelImpl)channel).getConnectedMembersMap().remove(userId);
/* 101 */       getJDA().handleEvent((GenericEvent)new GuildVoiceLeaveEvent((JDA)
/*     */             
/* 103 */             getJDA(), this.responseNumber, (Member)member, channel));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 108 */     SnowflakeCacheViewImpl<User> userView = getJDA().getUsersView();
/* 109 */     UnlockHook hook = userView.writeLock();
/*     */     
/* 111 */     try { if (userId != getJDA().getSelfUser().getIdLong() && 
/* 112 */         getJDA().getGuildsView().stream()
/* 113 */         .noneMatch(g -> (g.getMemberById(userId) != null))) {
/*     */         
/* 115 */         userView.remove(userId);
/* 116 */         getJDA().getEventCache().clear(EventCache.Type.USER, userId);
/*     */       } 
/* 118 */       if (hook != null) hook.close();  } catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 120 */      getJDA().handleEvent((GenericEvent)new GuildMemberLeaveEvent((JDA)
/*     */           
/* 122 */           getJDA(), this.responseNumber, (Member)member));
/*     */ 
/*     */     
/* 125 */     getJDA().handleEvent((GenericEvent)new GuildMemberRemoveEvent((JDA)
/*     */           
/* 127 */           getJDA(), this.responseNumber, (Guild)guild, (User)userImpl, (Member)member));
/*     */     
/* 129 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\GuildMemberRemoveHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */