/*     */ package net.dv8tion.jda.internal.handle;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.OnlineStatus;
/*     */ import net.dv8tion.jda.api.entities.Activity;
/*     */ import net.dv8tion.jda.api.entities.ClientType;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
/*     */ import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
/*     */ import net.dv8tion.jda.api.events.user.update.UserUpdateActivitiesEvent;
/*     */ import net.dv8tion.jda.api.events.user.update.UserUpdateActivityOrderEvent;
/*     */ import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
/*     */ import net.dv8tion.jda.api.utils.cache.CacheFlag;
/*     */ import net.dv8tion.jda.api.utils.cache.CacheView;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.EntityBuilder;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import net.dv8tion.jda.internal.entities.MemberImpl;
/*     */ import net.dv8tion.jda.internal.entities.MemberPresenceImpl;
/*     */ import net.dv8tion.jda.internal.utils.Helpers;
/*     */ import net.dv8tion.jda.internal.utils.JDALogger;
/*     */ import net.dv8tion.jda.internal.utils.UnlockHook;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PresenceUpdateHandler
/*     */   extends SocketHandler
/*     */ {
/*  47 */   private static final Logger log = JDALogger.getLog(PresenceUpdateHandler.class);
/*     */ 
/*     */   
/*     */   public PresenceUpdateHandler(JDAImpl api) {
/*  51 */     super(api);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Long handleInternally(DataObject content) {
/*  58 */     if (content.isNull("guild_id")) {
/*     */       
/*  60 */       log.debug("Received PRESENCE_UPDATE without guild_id. Ignoring event.");
/*  61 */       return null;
/*     */     } 
/*  63 */     if (this.api.getCacheFlags().stream().noneMatch(CacheFlag::isPresence)) {
/*  64 */       return null;
/*     */     }
/*     */     
/*  67 */     long guildId = content.getUnsignedLong("guild_id");
/*  68 */     if (getJDA().getGuildSetupController().isLocked(guildId))
/*  69 */       return Long.valueOf(guildId); 
/*  70 */     GuildImpl guild = (GuildImpl)getJDA().getGuildById(guildId);
/*  71 */     if (guild == null) {
/*     */       
/*  73 */       getJDA().getEventCache().cache(EventCache.Type.GUILD, guildId, this.responseNumber, this.allContent, this::handle);
/*  74 */       EventCache.LOG.debug("Received a PRESENCE_UPDATE for a guild that is not yet cached! GuildId:{} UserId: {}", 
/*  75 */           Long.valueOf(guildId), content.getObject("user").get("id"));
/*  76 */       return null;
/*     */     } 
/*     */     
/*  79 */     CacheView.SimpleCacheView<MemberPresenceImpl> presences = guild.getPresenceView();
/*  80 */     if (presences == null)
/*  81 */       return null; 
/*  82 */     DataObject jsonUser = content.getObject("user");
/*  83 */     long userId = jsonUser.getUnsignedLong("id");
/*  84 */     MemberImpl member = (MemberImpl)guild.getMemberById(userId);
/*  85 */     MemberPresenceImpl presence = (MemberPresenceImpl)presences.get(userId);
/*  86 */     OnlineStatus status = OnlineStatus.fromKey(content.getString("status"));
/*  87 */     if (status == OnlineStatus.OFFLINE)
/*  88 */       presences.remove(userId); 
/*  89 */     if (presence == null) {
/*     */       
/*  91 */       presence = new MemberPresenceImpl();
/*  92 */       if (status != OnlineStatus.OFFLINE) {
/*     */         
/*  94 */         UnlockHook lock = presences.writeLock();
/*     */         
/*  96 */         try { presences.getMap().put(userId, presence);
/*  97 */           if (lock != null) lock.close();  }
/*     */         catch (Throwable throwable) { if (lock != null)
/*     */             try { lock.close(); }
/*     */             catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */               throw throwable; }
/*     */       
/*     */       } 
/* 104 */     }  DataArray activityArray = (!getJDA().isCacheFlagSet(CacheFlag.ACTIVITY) || content.isNull("activities")) ? null : content.getArray("activities");
/* 105 */     List<Activity> newActivities = new ArrayList<>();
/* 106 */     boolean parsedActivity = parseActivities(userId, activityArray, newActivities);
/*     */     
/* 108 */     if (getJDA().isCacheFlagSet(CacheFlag.CLIENT_STATUS) && !content.isNull("client_status")) {
/* 109 */       handleClientStatus(content, presence);
/*     */     }
/*     */     
/* 112 */     if (parsedActivity) {
/* 113 */       handleActivities(newActivities, member, presence);
/*     */     }
/*     */ 
/*     */     
/* 117 */     if (presence.getOnlineStatus() != status) {
/*     */       
/* 119 */       OnlineStatus oldStatus = presence.getOnlineStatus();
/* 120 */       presence.setOnlineStatus(status);
/* 121 */       if (member != null) {
/*     */         
/* 123 */         getJDA().getEntityBuilder().updateMemberCache(member);
/* 124 */         getJDA().handleEvent((GenericEvent)new UserUpdateOnlineStatusEvent((JDA)
/*     */               
/* 126 */               getJDA(), this.responseNumber, (Member)member, oldStatus));
/*     */       } 
/*     */     } 
/*     */     
/* 130 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean parseActivities(long userId, DataArray activityArray, List<Activity> newActivities) {
/* 135 */     boolean parsedActivity = false;
/*     */     
/*     */     try {
/* 138 */       if (activityArray != null)
/*     */       {
/* 140 */         for (int i = 0; i < activityArray.length(); i++)
/* 141 */           newActivities.add(EntityBuilder.createActivity(activityArray.getObject(i))); 
/* 142 */         parsedActivity = true;
/*     */       }
/*     */     
/* 145 */     } catch (Exception ex) {
/*     */       
/* 147 */       if (EntityBuilder.LOG.isDebugEnabled()) {
/* 148 */         EntityBuilder.LOG.warn("Encountered exception trying to parse a presence! UserID: {} JSON: {}", new Object[] { Long.valueOf(userId), activityArray, ex });
/*     */       } else {
/* 150 */         EntityBuilder.LOG.warn("Encountered exception trying to parse a presence! UserID: {} Message: {} Enable debug for details", Long.valueOf(userId), ex.getMessage());
/*     */       } 
/* 152 */     }  return parsedActivity;
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleActivities(List<Activity> newActivities, @Nullable MemberImpl member, MemberPresenceImpl presence) {
/* 157 */     List<Activity> oldActivities = presence.getActivities();
/* 158 */     presence.setActivities(newActivities);
/* 159 */     if (member == null)
/*     */       return; 
/* 161 */     boolean unorderedEquals = Helpers.deepEqualsUnordered(oldActivities, newActivities);
/* 162 */     if (unorderedEquals) {
/*     */       
/* 164 */       boolean deepEquals = Helpers.deepEquals(oldActivities, newActivities);
/* 165 */       if (!deepEquals)
/*     */       {
/* 167 */         getJDA().handleEvent((GenericEvent)new UserUpdateActivityOrderEvent(
/*     */               
/* 169 */               getJDA(), this.responseNumber, oldActivities, (Member)member));
/*     */       
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 175 */       getJDA().getEntityBuilder().updateMemberCache(member);
/* 176 */       List<Activity> stoppedActivities = new ArrayList<>(oldActivities);
/* 177 */       List<Activity> startedActivities = new ArrayList<>();
/* 178 */       for (Activity activity : newActivities) {
/*     */         
/* 180 */         if (!stoppedActivities.remove(activity)) {
/* 181 */           startedActivities.add(activity);
/*     */         }
/*     */       } 
/* 184 */       for (Activity activity : startedActivities)
/*     */       {
/* 186 */         getJDA().handleEvent((GenericEvent)new UserActivityStartEvent((JDA)
/*     */               
/* 188 */               getJDA(), this.responseNumber, (Member)member, activity));
/*     */       }
/*     */ 
/*     */       
/* 192 */       for (Activity activity : stoppedActivities)
/*     */       {
/* 194 */         getJDA().handleEvent((GenericEvent)new UserActivityEndEvent((JDA)
/*     */               
/* 196 */               getJDA(), this.responseNumber, (Member)member, activity));
/*     */       }
/*     */ 
/*     */       
/* 200 */       getJDA().handleEvent((GenericEvent)new UserUpdateActivitiesEvent((JDA)
/*     */             
/* 202 */             getJDA(), this.responseNumber, (Member)member, oldActivities));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleClientStatus(DataObject content, MemberPresenceImpl presence) {
/* 209 */     DataObject json = content.getObject("client_status");
/* 210 */     EnumSet<ClientType> types = EnumSet.of(ClientType.UNKNOWN);
/* 211 */     for (String key : json.keys()) {
/*     */       
/* 213 */       ClientType type = ClientType.fromKey(key);
/* 214 */       types.add(type);
/* 215 */       String raw = String.valueOf(json.get(key));
/* 216 */       OnlineStatus clientStatus = OnlineStatus.fromKey(raw);
/* 217 */       presence.setOnlineStatus(type, clientStatus);
/*     */     } 
/* 219 */     for (ClientType type : EnumSet.<ClientType>complementOf(types))
/* 220 */       presence.setOnlineStatus(type, null); 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\PresenceUpdateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */