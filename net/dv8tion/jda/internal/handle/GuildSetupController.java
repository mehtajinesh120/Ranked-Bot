/*     */ package net.dv8tion.jda.internal.handle;
/*     */ 
/*     */ import gnu.trove.iterator.TLongObjectIterator;
/*     */ import gnu.trove.map.TLongObjectMap;
/*     */ import gnu.trove.map.hash.TLongObjectHashMap;
/*     */ import gnu.trove.set.TLongSet;
/*     */ import gnu.trove.set.hash.TLongHashSet;
/*     */ import java.util.HashSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.events.guild.GuildTimeoutEvent;
/*     */ import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.requests.MemberChunkManager;
/*     */ import net.dv8tion.jda.internal.requests.WebSocketClient;
/*     */ import net.dv8tion.jda.internal.utils.JDALogger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuildSetupController
/*     */ {
/*  46 */   protected static final Logger log = JDALogger.getLog(GuildSetupController.class);
/*     */   
/*     */   private static final long timeoutDuration = 75L;
/*     */   
/*     */   private static final int timeoutThreshold = 60;
/*     */   private final JDAImpl api;
/*  52 */   private final TLongObjectMap<GuildSetupNode> setupNodes = (TLongObjectMap<GuildSetupNode>)new TLongObjectHashMap();
/*  53 */   private final TLongSet chunkingGuilds = (TLongSet)new TLongHashSet();
/*  54 */   private final TLongSet unavailableGuilds = (TLongSet)new TLongHashSet();
/*     */ 
/*     */   
/*  57 */   private int incompleteCount = 0; private Future<?> timeoutHandle;
/*     */   protected StatusListener listener;
/*     */   
/*     */   public GuildSetupController(JDAImpl api) {
/*  61 */     this.listener = ((id, oldStatus, newStatus) -> log.trace("[{}] Updated status {}->{}", new Object[] { Long.valueOf(id), oldStatus, newStatus }));
/*     */ 
/*     */ 
/*     */     
/*  65 */     this.api = api;
/*     */   }
/*     */ 
/*     */   
/*     */   JDAImpl getJDA() {
/*  70 */     return this.api;
/*     */   }
/*     */ 
/*     */   
/*     */   void addGuildForChunking(long id, boolean join) {
/*  75 */     log.trace("Adding guild for chunking ID: {}", Long.valueOf(id));
/*  76 */     if (join || this.incompleteCount <= 0) {
/*     */       
/*  78 */       if (this.incompleteCount <= 0) {
/*     */ 
/*     */         
/*  81 */         sendChunkRequest(Long.valueOf(id));
/*     */         return;
/*     */       } 
/*  84 */       this.incompleteCount++;
/*     */     } 
/*  86 */     this.chunkingGuilds.add(id);
/*  87 */     tryChunking();
/*     */   }
/*     */ 
/*     */   
/*     */   void remove(long id) {
/*  92 */     this.unavailableGuilds.remove(id);
/*  93 */     this.setupNodes.remove(id);
/*  94 */     this.chunkingGuilds.remove(id);
/*  95 */     checkReady();
/*     */   }
/*     */ 
/*     */   
/*     */   public void ready(long id) {
/* 100 */     remove(id);
/* 101 */     this.incompleteCount--;
/* 102 */     checkReady();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkReady() {
/* 108 */     WebSocketClient client = getJDA().getClient();
/*     */     
/* 110 */     if (this.incompleteCount < 1 && !client.isReady()) {
/*     */       
/* 112 */       if (this.timeoutHandle != null)
/* 113 */         this.timeoutHandle.cancel(false); 
/* 114 */       this.timeoutHandle = null;
/* 115 */       client.ready();
/*     */     }
/* 117 */     else if (this.incompleteCount <= 60) {
/*     */       
/* 119 */       startTimeout();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setIncompleteCount(int count) {
/* 125 */     this.incompleteCount = count;
/* 126 */     log.debug("Setting incomplete count to {}", Integer.valueOf(this.incompleteCount));
/* 127 */     checkReady();
/* 128 */     return (count != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onReady(long id, DataObject obj) {
/* 133 */     log.trace("Adding id to setup cache {}", Long.valueOf(id));
/* 134 */     GuildSetupNode node = new GuildSetupNode(id, this, GuildSetupNode.Type.INIT);
/* 135 */     this.setupNodes.put(id, node);
/* 136 */     node.handleReady(obj);
/* 137 */     if (node.markedUnavailable) {
/*     */       
/* 139 */       this.incompleteCount--;
/* 140 */       tryChunking();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCreate(long id, DataObject obj) {
/* 146 */     boolean available = (obj.isNull("unavailable") || !obj.getBoolean("unavailable"));
/* 147 */     log.trace("Received guild create for id: {} available: {}", Long.valueOf(id), Boolean.valueOf(available));
/*     */     
/* 149 */     if (available && this.unavailableGuilds.contains(id) && !this.setupNodes.containsKey(id)) {
/*     */ 
/*     */       
/* 152 */       this.unavailableGuilds.remove(id);
/* 153 */       this.setupNodes.put(id, new GuildSetupNode(id, this, GuildSetupNode.Type.AVAILABLE));
/*     */     } 
/*     */     
/* 156 */     GuildSetupNode node = (GuildSetupNode)this.setupNodes.get(id);
/* 157 */     if (node == null) {
/*     */ 
/*     */       
/* 160 */       node = new GuildSetupNode(id, this, GuildSetupNode.Type.JOIN);
/* 161 */       this.setupNodes.put(id, node);
/*     */     
/*     */     }
/* 164 */     else if (node.markedUnavailable && available && this.incompleteCount > 0) {
/*     */ 
/*     */ 
/*     */       
/* 168 */       this.incompleteCount++;
/*     */     } 
/* 170 */     node.handleCreate(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onDelete(long id, DataObject obj) {
/* 175 */     boolean available = (obj.isNull("unavailable") || !obj.getBoolean("unavailable"));
/* 176 */     if (isUnavailable(id) && available) {
/*     */       
/* 178 */       log.debug("Leaving unavailable guild with id {}", Long.valueOf(id));
/* 179 */       remove(id);
/* 180 */       this.api.getEventManager().handle((GenericEvent)new UnavailableGuildLeaveEvent((JDA)this.api, this.api.getResponseTotal(), id));
/* 181 */       return true;
/*     */     } 
/*     */     
/* 184 */     GuildSetupNode node = (GuildSetupNode)this.setupNodes.get(id);
/* 185 */     if (node == null)
/* 186 */       return false; 
/* 187 */     log.debug("Received guild delete for id: {} available: {}", Long.valueOf(id), Boolean.valueOf(available));
/* 188 */     if (!available) {
/*     */ 
/*     */       
/* 191 */       if (!node.markedUnavailable) {
/*     */         
/* 193 */         node.markedUnavailable = true;
/* 194 */         if (this.incompleteCount > 0) {
/*     */ 
/*     */           
/* 197 */           this.chunkingGuilds.remove(id);
/* 198 */           this.incompleteCount--;
/*     */         } 
/*     */       } 
/* 201 */       node.reset();
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 206 */       node.cleanup();
/* 207 */       if (node.isJoin() && !node.requestedChunk) {
/* 208 */         remove(id);
/*     */       } else {
/* 210 */         ready(id);
/* 211 */       }  this.api.getEventManager().handle((GenericEvent)new UnavailableGuildLeaveEvent((JDA)this.api, this.api.getResponseTotal(), id));
/*     */     } 
/* 213 */     log.debug("Updated incompleteCount to {}", Integer.valueOf(this.incompleteCount));
/* 214 */     checkReady();
/* 215 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMemberChunk(long id, DataObject chunk) {
/* 220 */     DataArray members = chunk.getArray("members");
/* 221 */     int index = chunk.getInt("chunk_index");
/* 222 */     int count = chunk.getInt("chunk_count");
/* 223 */     log.debug("Received member chunk for guild id: {} size: {} index: {}/{}", new Object[] { Long.valueOf(id), Integer.valueOf(members.length()), Integer.valueOf(index), Integer.valueOf(count) });
/* 224 */     GuildSetupNode node = (GuildSetupNode)this.setupNodes.get(id);
/* 225 */     if (node != null) {
/* 226 */       node.handleMemberChunk(MemberChunkManager.isLastChunk(chunk), members);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean onAddMember(long id, DataObject member) {
/* 231 */     GuildSetupNode node = (GuildSetupNode)this.setupNodes.get(id);
/* 232 */     if (node == null)
/* 233 */       return false; 
/* 234 */     log.debug("Received GUILD_MEMBER_ADD during setup, adding member to guild. GuildID: {}", Long.valueOf(id));
/* 235 */     node.handleAddMember(member);
/* 236 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onRemoveMember(long id, DataObject member) {
/* 241 */     GuildSetupNode node = (GuildSetupNode)this.setupNodes.get(id);
/* 242 */     if (node == null)
/* 243 */       return false; 
/* 244 */     log.debug("Received GUILD_MEMBER_REMOVE during setup, removing member from guild. GuildID: {}", Long.valueOf(id));
/* 245 */     node.handleRemoveMember(member);
/* 246 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSync(long id, DataObject obj) {
/* 251 */     GuildSetupNode node = (GuildSetupNode)this.setupNodes.get(id);
/* 252 */     if (node != null) {
/* 253 */       node.handleSync(obj);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isLocked(long id) {
/* 258 */     return this.setupNodes.containsKey(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnavailable(long id) {
/* 263 */     return this.unavailableGuilds.contains(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isKnown(long id) {
/* 269 */     return (isLocked(id) || isUnavailable(id));
/*     */   }
/*     */ 
/*     */   
/*     */   public void cacheEvent(long guildId, DataObject event) {
/* 274 */     GuildSetupNode node = (GuildSetupNode)this.setupNodes.get(guildId);
/* 275 */     if (node != null) {
/* 276 */       node.cacheEvent(event);
/*     */     } else {
/* 278 */       log.warn("Attempted to cache event for a guild that is not locked. {}", event, new IllegalStateException());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clearCache() {
/* 283 */     this.setupNodes.clear();
/* 284 */     this.chunkingGuilds.clear();
/* 285 */     this.unavailableGuilds.clear();
/* 286 */     this.incompleteCount = 0;
/* 287 */     close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 292 */     if (this.timeoutHandle != null)
/* 293 */       this.timeoutHandle.cancel(false); 
/* 294 */     this.timeoutHandle = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsMember(long userId, @Nullable GuildSetupNode excludedNode) {
/* 299 */     for (TLongObjectIterator<GuildSetupNode> it = this.setupNodes.iterator(); it.hasNext(); ) {
/*     */       
/* 301 */       it.advance();
/* 302 */       GuildSetupNode node = (GuildSetupNode)it.value();
/* 303 */       if (node != excludedNode && node.containsMember(userId))
/* 304 */         return true; 
/*     */     } 
/* 306 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public TLongSet getUnavailableGuilds() {
/* 311 */     return this.unavailableGuilds;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<GuildSetupNode> getSetupNodes() {
/* 316 */     return new HashSet<>(this.setupNodes.valueCollection());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<GuildSetupNode> getSetupNodes(Status status) {
/* 321 */     return (Set<GuildSetupNode>)getSetupNodes().stream().filter(node -> (node.status == status)).collect(Collectors.toSet());
/*     */   }
/*     */ 
/*     */   
/*     */   public GuildSetupNode getSetupNodeById(long id) {
/* 326 */     return (GuildSetupNode)this.setupNodes.get(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public GuildSetupNode getSetupNodeById(String id) {
/* 331 */     return getSetupNodeById(MiscUtil.parseSnowflake(id));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStatusListener(StatusListener listener) {
/* 336 */     this.listener = Objects.<StatusListener>requireNonNull(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getIncompleteCount() {
/* 343 */     return this.incompleteCount;
/*     */   }
/*     */ 
/*     */   
/*     */   int getChunkingCount() {
/* 348 */     return this.chunkingGuilds.size();
/*     */   }
/*     */ 
/*     */   
/*     */   void sendChunkRequest(Object obj) {
/* 353 */     log.debug("Sending chunking requests for {} guilds", Integer.valueOf((obj instanceof DataArray) ? ((DataArray)obj).length() : 1));
/*     */     
/* 355 */     getJDA().getClient().sendChunkRequest(
/* 356 */         DataObject.empty()
/* 357 */         .put("guild_id", obj)
/* 358 */         .put("query", "")
/* 359 */         .put("limit", Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void tryChunking() {
/* 365 */     this.chunkingGuilds.forEach(id -> {
/*     */           sendChunkRequest(Long.valueOf(id));
/*     */           return true;
/*     */         });
/* 369 */     this.chunkingGuilds.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   private void startTimeout() {
/* 374 */     if (this.timeoutHandle != null || this.incompleteCount < 1) {
/*     */       return;
/*     */     }
/* 377 */     log.debug("Starting {} second timeout for {} guilds", Long.valueOf(75L), Integer.valueOf(this.incompleteCount));
/* 378 */     this.timeoutHandle = getJDA().getGatewayPool().schedule(this::onTimeout, 75L, TimeUnit.SECONDS);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUnavailable(long id) {
/* 383 */     this.unavailableGuilds.add(id);
/* 384 */     log.debug("Guild with id {} is now marked unavailable. Total: {}", Long.valueOf(id), Integer.valueOf(this.unavailableGuilds.size()));
/*     */   } @FunctionalInterface
/*     */   public static interface StatusListener {
/*     */     void onStatusChange(long param1Long, GuildSetupController.Status param1Status1, GuildSetupController.Status param1Status2); }
/*     */   public void onTimeout() {
/* 389 */     if (this.incompleteCount < 1)
/*     */       return; 
/* 391 */     log.warn("Automatically marking {} guilds as unavailable due to timeout!", Integer.valueOf(this.incompleteCount));
/* 392 */     TLongObjectIterator<GuildSetupNode> iterator = this.setupNodes.iterator();
/* 393 */     while (iterator.hasNext()) {
/*     */       
/* 395 */       iterator.advance();
/* 396 */       GuildSetupNode node = (GuildSetupNode)iterator.value();
/* 397 */       iterator.remove();
/* 398 */       this.unavailableGuilds.add(node.getIdLong());
/*     */       
/* 400 */       getJDA().handleEvent((GenericEvent)new GuildTimeoutEvent((JDA)getJDA(), node.getIdLong()));
/*     */     } 
/* 402 */     this.incompleteCount = 0;
/* 403 */     checkReady();
/*     */   }
/*     */   
/*     */   public enum Status
/*     */   {
/* 408 */     INIT,
/* 409 */     CHUNKING,
/* 410 */     BUILDING,
/* 411 */     READY,
/* 412 */     UNAVAILABLE,
/* 413 */     REMOVED;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\GuildSetupController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */