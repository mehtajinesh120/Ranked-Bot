/*     */ package net.dv8tion.jda.internal.handle;
/*     */ 
/*     */ import gnu.trove.TLongCollection;
/*     */ import gnu.trove.iterator.TLongIterator;
/*     */ import gnu.trove.iterator.TLongObjectIterator;
/*     */ import gnu.trove.map.TLongObjectMap;
/*     */ import gnu.trove.map.hash.TLongObjectHashMap;
/*     */ import gnu.trove.set.TLongSet;
/*     */ import gnu.trove.set.hash.TLongHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.audio.hooks.ConnectionListener;
/*     */ import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.events.guild.GuildAvailableEvent;
/*     */ import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
/*     */ import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
/*     */ import net.dv8tion.jda.api.events.guild.UnavailableGuildJoinedEvent;
/*     */ import net.dv8tion.jda.api.managers.AudioManager;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import net.dv8tion.jda.internal.managers.AudioManagerImpl;
/*     */ import net.dv8tion.jda.internal.utils.UnlockHook;
/*     */ import net.dv8tion.jda.internal.utils.cache.AbstractCacheView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuildSetupNode
/*     */ {
/*     */   private final long id;
/*     */   private final GuildSetupController controller;
/*  50 */   private final List<DataObject> cachedEvents = new LinkedList<>();
/*     */   private TLongObjectMap<DataObject> members;
/*     */   private TLongSet removedMembers;
/*     */   private DataObject partialGuild;
/*  54 */   private int expectedMemberCount = 1;
/*     */   
/*     */   boolean requestedChunk;
/*     */   final Type type;
/*     */   boolean firedUnavailableJoin = false;
/*     */   boolean markedUnavailable = false;
/*  60 */   GuildSetupController.Status status = GuildSetupController.Status.INIT;
/*     */ 
/*     */   
/*     */   GuildSetupNode(long id, GuildSetupController controller, Type type) {
/*  64 */     this.id = id;
/*  65 */     this.controller = controller;
/*  66 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/*  71 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/*  76 */     return Long.toUnsignedString(this.id);
/*     */   }
/*     */ 
/*     */   
/*     */   public GuildSetupController.Status getStatus() {
/*  81 */     return this.status;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public DataObject getGuildPayload() {
/*  87 */     return this.partialGuild;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getExpectedMemberCount() {
/*  92 */     return this.expectedMemberCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCurrentMemberCount() {
/*  97 */     TLongHashSet knownMembers = new TLongHashSet((TLongCollection)this.members.keySet());
/*  98 */     knownMembers.removeAll((TLongCollection)this.removedMembers);
/*  99 */     return knownMembers.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Type getType() {
/* 104 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isJoin() {
/* 109 */     return (this.type == Type.JOIN);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMarkedUnavailable() {
/* 114 */     return this.markedUnavailable;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requestedChunks() {
/* 119 */     return this.requestedChunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsMember(long userId) {
/* 124 */     if (this.members == null || this.members.isEmpty())
/* 125 */       return false; 
/* 126 */     return this.members.containsKey(userId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 132 */     return "GuildSetupNode[" + this.id + "|" + this.status + ']' + '{' + "expectedMemberCount=" + this.expectedMemberCount + ", requestedChunk=" + this.requestedChunk + ", type=" + this.type + ", markedUnavailable=" + this.markedUnavailable + '}';
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
/*     */   public int hashCode() {
/* 144 */     return Long.hashCode(this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 150 */     if (!(obj instanceof GuildSetupNode))
/* 151 */       return false; 
/* 152 */     GuildSetupNode node = (GuildSetupNode)obj;
/* 153 */     return (node.id == this.id);
/*     */   }
/*     */ 
/*     */   
/*     */   private GuildSetupController getController() {
/* 158 */     return this.controller;
/*     */   }
/*     */ 
/*     */   
/*     */   void updateStatus(GuildSetupController.Status status) {
/* 163 */     if (status == this.status) {
/*     */       return;
/*     */     }
/*     */     try {
/* 167 */       (getController()).listener.onStatusChange(this.id, this.status, status);
/*     */     }
/* 169 */     catch (Exception ex) {
/*     */       
/* 171 */       GuildSetupController.log.error("Uncaught exception in status listener", ex);
/*     */     } 
/* 173 */     this.status = status;
/*     */   }
/*     */ 
/*     */   
/*     */   void reset() {
/* 178 */     updateStatus(GuildSetupController.Status.UNAVAILABLE);
/* 179 */     this.expectedMemberCount = 1;
/* 180 */     this.partialGuild = null;
/* 181 */     this.requestedChunk = false;
/* 182 */     if (this.members != null)
/* 183 */       this.members.clear(); 
/* 184 */     if (this.removedMembers != null)
/* 185 */       this.removedMembers.clear(); 
/* 186 */     this.cachedEvents.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   void handleReady(DataObject obj) {}
/*     */   
/*     */   void handleCreate(DataObject obj) {
/* 193 */     if (this.partialGuild == null) {
/*     */       
/* 195 */       this.partialGuild = obj;
/*     */     }
/*     */     else {
/*     */       
/* 199 */       for (String key : obj.keys())
/*     */       {
/* 201 */         this.partialGuild.put(key, obj.opt(key).orElse(null));
/*     */       }
/*     */     } 
/* 204 */     boolean unavailable = this.partialGuild.getBoolean("unavailable");
/* 205 */     boolean wasMarkedUnavailable = this.markedUnavailable;
/* 206 */     this.markedUnavailable = unavailable;
/* 207 */     if (unavailable) {
/*     */       
/* 209 */       if (!this.firedUnavailableJoin && isJoin()) {
/*     */         
/* 211 */         this.firedUnavailableJoin = true;
/* 212 */         JDAImpl api = getController().getJDA();
/* 213 */         api.handleEvent((GenericEvent)new UnavailableGuildJoinedEvent((JDA)api, api.getResponseTotal(), this.id));
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 218 */     ensureMembers();
/*     */   }
/*     */ 
/*     */   
/*     */   void handleSync(DataObject obj) {
/* 223 */     if (this.partialGuild == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 228 */       GuildSetupController.log.debug("Dropping sync update due to unavailable guild");
/*     */       return;
/*     */     } 
/* 231 */     for (String key : obj.keys())
/*     */     {
/* 233 */       this.partialGuild.put(key, obj.opt(key).orElse(null));
/*     */     }
/*     */     
/* 236 */     ensureMembers();
/*     */   }
/*     */ 
/*     */   
/*     */   boolean handleMemberChunk(boolean last, DataArray arr) {
/* 241 */     if (this.partialGuild == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 246 */       GuildSetupController.log.debug("Dropping member chunk due to unavailable guild");
/* 247 */       return true;
/*     */     } 
/* 249 */     for (int index = 0; index < arr.length(); index++) {
/*     */       
/* 251 */       DataObject obj = arr.getObject(index);
/* 252 */       long id = obj.getObject("user").getLong("id");
/* 253 */       this.members.put(id, obj);
/*     */     } 
/*     */     
/* 256 */     if (last || this.members.size() >= this.expectedMemberCount || !getController().getJDA().chunkGuild(this.id)) {
/*     */       
/* 258 */       completeSetup();
/* 259 */       return false;
/*     */     } 
/* 261 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   void handleAddMember(DataObject member) {
/* 266 */     if (this.members == null || this.removedMembers == null)
/*     */       return; 
/* 268 */     this.expectedMemberCount++;
/* 269 */     long userId = member.getObject("user").getLong("id");
/* 270 */     this.members.put(userId, member);
/* 271 */     this.removedMembers.remove(userId);
/*     */   }
/*     */ 
/*     */   
/*     */   void handleRemoveMember(DataObject member) {
/* 276 */     if (this.members == null || this.removedMembers == null)
/*     */       return; 
/* 278 */     this.expectedMemberCount--;
/* 279 */     long userId = member.getObject("user").getLong("id");
/* 280 */     this.members.remove(userId);
/* 281 */     this.removedMembers.add(userId);
/* 282 */     EventCache eventCache = getController().getJDA().getEventCache();
/* 283 */     if (!getController().containsMember(userId, this)) {
/* 284 */       eventCache.clear(EventCache.Type.USER, userId);
/*     */     }
/*     */   }
/*     */   
/*     */   void cacheEvent(DataObject event) {
/* 289 */     GuildSetupController.log.trace("Caching {} event during init. GuildId: {}", event.getString("t"), Long.valueOf(this.id));
/* 290 */     this.cachedEvents.add(event);
/*     */ 
/*     */     
/* 293 */     int cacheSize = this.cachedEvents.size();
/* 294 */     if (cacheSize >= 2000 && cacheSize % 1000 == 0) {
/*     */       
/* 296 */       GuildSetupController controller = getController();
/* 297 */       GuildSetupController.log.warn("Accumulating suspicious amounts of cached events during guild setup, something might be wrong. Cached: {} Members: {}/{} Status: {} GuildId: {} Incomplete: {}/{}", new Object[] {
/*     */ 
/*     */             
/* 300 */             Integer.valueOf(cacheSize), Integer.valueOf(getCurrentMemberCount()), Integer.valueOf(getExpectedMemberCount()), this.status, 
/* 301 */             Long.valueOf(this.id), Integer.valueOf(controller.getChunkingCount()), Integer.valueOf(controller.getIncompleteCount())
/*     */           });
/* 303 */       if (this.status == GuildSetupController.Status.CHUNKING) {
/*     */         
/* 305 */         GuildSetupController.log.debug("Forcing new chunk request for guild: {}", Long.valueOf(this.id));
/* 306 */         controller.sendChunkRequest(Long.valueOf(this.id));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void cleanup() {
/* 313 */     updateStatus(GuildSetupController.Status.REMOVED);
/* 314 */     EventCache eventCache = getController().getJDA().getEventCache();
/* 315 */     eventCache.clear(EventCache.Type.GUILD, this.id);
/* 316 */     if (this.partialGuild == null) {
/*     */       return;
/*     */     }
/* 319 */     Optional<DataArray> channels = this.partialGuild.optArray("channels");
/* 320 */     Optional<DataArray> roles = this.partialGuild.optArray("roles");
/* 321 */     channels.ifPresent(arr -> {
/*     */           for (int i = 0; i < arr.length(); i++) {
/*     */             DataObject json = arr.getObject(i);
/*     */             
/*     */             long id = json.getLong("id");
/*     */             
/*     */             eventCache.clear(EventCache.Type.CHANNEL, id);
/*     */           } 
/*     */         });
/* 330 */     roles.ifPresent(arr -> {
/*     */           for (int i = 0; i < arr.length(); i++) {
/*     */             DataObject json = arr.getObject(i);
/*     */             
/*     */             long id = json.getLong("id");
/*     */             
/*     */             eventCache.clear(EventCache.Type.ROLE, id);
/*     */           } 
/*     */         });
/* 339 */     if (this.members != null)
/*     */     {
/* 341 */       for (TLongObjectIterator<DataObject> it = this.members.iterator(); it.hasNext(); ) {
/*     */         
/* 343 */         it.advance();
/* 344 */         long userId = it.key();
/* 345 */         if (!getController().containsMember(userId, this)) {
/* 346 */           eventCache.clear(EventCache.Type.USER, userId);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void completeSetup() {
/* 353 */     updateStatus(GuildSetupController.Status.BUILDING);
/* 354 */     JDAImpl api = getController().getJDA();
/* 355 */     for (TLongIterator it = this.removedMembers.iterator(); it.hasNext();)
/* 356 */       this.members.remove(it.next()); 
/* 357 */     this.removedMembers.clear();
/* 358 */     GuildImpl guild = api.getEntityBuilder().createGuild(this.id, this.partialGuild, this.members, this.expectedMemberCount);
/* 359 */     updateAudioManagerReference(guild);
/* 360 */     switch (this.type) {
/*     */       
/*     */       case AVAILABLE:
/* 363 */         api.handleEvent((GenericEvent)new GuildAvailableEvent((JDA)api, api.getResponseTotal(), (Guild)guild));
/* 364 */         getController().remove(this.id);
/*     */         break;
/*     */       case JOIN:
/* 367 */         api.handleEvent((GenericEvent)new GuildJoinEvent((JDA)api, api.getResponseTotal(), (Guild)guild));
/* 368 */         if (this.requestedChunk) {
/* 369 */           getController().ready(this.id); break;
/*     */         } 
/* 371 */         getController().remove(this.id);
/*     */         break;
/*     */       default:
/* 374 */         api.handleEvent((GenericEvent)new GuildReadyEvent((JDA)api, api.getResponseTotal(), (Guild)guild));
/* 375 */         getController().ready(this.id);
/*     */         break;
/*     */     } 
/* 378 */     updateStatus(GuildSetupController.Status.READY);
/* 379 */     GuildSetupController.log.debug("Finished setup for guild {} firing cached events {}", Long.valueOf(this.id), Integer.valueOf(this.cachedEvents.size()));
/* 380 */     api.getClient().handle(this.cachedEvents);
/* 381 */     api.getEventCache().playbackCache(EventCache.Type.GUILD, this.id);
/*     */   }
/*     */ 
/*     */   
/*     */   private void ensureMembers() {
/* 386 */     this.expectedMemberCount = this.partialGuild.getInt("member_count");
/* 387 */     this.members = (TLongObjectMap<DataObject>)new TLongObjectHashMap(this.expectedMemberCount);
/* 388 */     this.removedMembers = (TLongSet)new TLongHashSet();
/* 389 */     DataArray memberArray = this.partialGuild.getArray("members");
/* 390 */     if (!getController().getJDA().chunkGuild(this.id)) {
/*     */       
/* 392 */       handleMemberChunk(true, memberArray);
/*     */     }
/* 394 */     else if (memberArray.length() < this.expectedMemberCount && !this.requestedChunk) {
/*     */       
/* 396 */       updateStatus(GuildSetupController.Status.CHUNKING);
/* 397 */       getController().addGuildForChunking(this.id, isJoin());
/* 398 */       this.requestedChunk = true;
/*     */     }
/* 400 */     else if (handleMemberChunk(false, memberArray) && !this.requestedChunk) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 406 */       GuildSetupController.log.trace("Received suspicious members with a guild payload. Attempting to chunk. member_count: {} members: {} actual_members: {} guild_id: {}", new Object[] {
/*     */ 
/*     */             
/* 409 */             Integer.valueOf(this.expectedMemberCount), Integer.valueOf(memberArray.length()), Integer.valueOf(this.members.size()), Long.valueOf(this.id) });
/* 410 */       this.members.clear();
/* 411 */       updateStatus(GuildSetupController.Status.CHUNKING);
/* 412 */       getController().addGuildForChunking(this.id, isJoin());
/* 413 */       this.requestedChunk = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateAudioManagerReference(GuildImpl guild) {
/* 419 */     JDAImpl api = getController().getJDA();
/* 420 */     AbstractCacheView<AudioManager> managerView = api.getAudioManagersView();
/* 421 */     UnlockHook hook = managerView.writeLock();
/*     */     
/* 423 */     try { TLongObjectMap<AudioManager> audioManagerMap = managerView.getMap();
/* 424 */       AudioManagerImpl mng = (AudioManagerImpl)audioManagerMap.get(this.id);
/* 425 */       if (mng == null)
/*     */       
/*     */       { 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 456 */         if (hook != null) hook.close();  return; }  ConnectionListener listener = mng.getConnectionListener(); AudioManagerImpl newMng = new AudioManagerImpl(guild); newMng.setSelfMuted(mng.isSelfMuted()); newMng.setSelfDeafened(mng.isSelfDeafened()); newMng.setQueueTimeout(mng.getConnectTimeout()); newMng.setSendingHandler(mng.getSendingHandler()); newMng.setReceivingHandler(mng.getReceivingHandler()); newMng.setConnectionListener(listener); newMng.setAutoReconnect(mng.isAutoReconnect()); if (mng.isConnected()) { long channelId = mng.getConnectedChannel().getIdLong(); VoiceChannel channel = api.getVoiceChannelById(channelId); if (channel != null) { if (mng.isConnected()) mng.closeAudioConnection(ConnectionStatus.ERROR_CANNOT_RESUME);  } else { api.getClient().removeAudioConnection(this.id); if (listener != null) listener.onStatusChange(ConnectionStatus.DISCONNECTED_CHANNEL_DELETED);  }  }  audioManagerMap.put(this.id, newMng); if (hook != null) hook.close();  }
/*     */     catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 461 */      } public enum Type { INIT, JOIN, AVAILABLE; }
/*     */ 
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\GuildSetupNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */