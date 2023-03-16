/*     */ package net.dv8tion.jda.internal.requests;
/*     */ 
/*     */ import gnu.trove.map.TLongObjectMap;
/*     */ import gnu.trove.map.hash.TLongObjectHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.entities.EntityBuilder;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import net.dv8tion.jda.internal.entities.MemberImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MemberChunkManager
/*     */ {
/*     */   private static final long MAX_CHUNK_AGE = 10000L;
/*     */   private final WebSocketClient client;
/*  39 */   private final ReentrantLock lock = new ReentrantLock();
/*  40 */   private final TLongObjectMap<ChunkRequest> requests = (TLongObjectMap<ChunkRequest>)new TLongObjectHashMap();
/*     */   
/*     */   private Future<?> timeoutHandle;
/*     */   
/*     */   public MemberChunkManager(WebSocketClient client) {
/*  45 */     this.client = client;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isLastChunk(DataObject chunk) {
/*  50 */     return (chunk.getInt("chunk_index") + 1 == chunk.getInt("chunk_count"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  55 */     Objects.requireNonNull(this.requests); MiscUtil.locked(this.lock, this.requests::clear);
/*     */   }
/*     */ 
/*     */   
/*     */   private void init() {
/*  60 */     MiscUtil.locked(this.lock, () -> {
/*     */           if (this.timeoutHandle == null) {
/*     */             this.timeoutHandle = this.client.getJDA().getGatewayPool().scheduleAtFixedRate(new TimeoutHandler(), 5L, 5L, TimeUnit.SECONDS);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void shutdown() {
/*  68 */     if (this.timeoutHandle != null) {
/*  69 */       this.timeoutHandle.cancel(false);
/*     */     }
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> chunkGuild(GuildImpl guild, boolean presence, BiConsumer<Boolean, List<Member>> handler) {
/*  74 */     init();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     DataObject request = DataObject.empty().put("guild_id", guild.getId()).put("presences", Boolean.valueOf(presence)).put("limit", Integer.valueOf(0)).put("query", "");
/*     */     
/*  81 */     ChunkRequest chunkRequest = new ChunkRequest(handler, guild, request);
/*  82 */     makeRequest(chunkRequest);
/*  83 */     return chunkRequest;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Void> chunkGuild(GuildImpl guild, String query, int limit, BiConsumer<Boolean, List<Member>> handler) {
/*  88 */     init();
/*     */ 
/*     */ 
/*     */     
/*  92 */     DataObject request = DataObject.empty().put("guild_id", guild.getId()).put("limit", Integer.valueOf(Math.min(100, Math.max(1, limit)))).put("query", query);
/*     */     
/*  94 */     ChunkRequest chunkRequest = new ChunkRequest(handler, guild, request);
/*  95 */     makeRequest(chunkRequest);
/*  96 */     return chunkRequest;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Void> chunkGuild(GuildImpl guild, boolean presence, long[] userIds, BiConsumer<Boolean, List<Member>> handler) {
/* 101 */     init();
/*     */ 
/*     */ 
/*     */     
/* 105 */     DataObject request = DataObject.empty().put("guild_id", guild.getId()).put("presences", Boolean.valueOf(presence)).put("user_ids", userIds);
/*     */     
/* 107 */     ChunkRequest chunkRequest = new ChunkRequest(handler, guild, request);
/* 108 */     makeRequest(chunkRequest);
/* 109 */     return chunkRequest;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handleChunk(long guildId, DataObject response) {
/* 114 */     return ((Boolean)MiscUtil.locked(this.lock, () -> { String nonce = response.getString("nonce", null); if (nonce == null || nonce.isEmpty()) return Boolean.valueOf(false);  long key = Long.parseLong(nonce); ChunkRequest request = (ChunkRequest)this.requests.get(key); if (request == null) return Boolean.valueOf(false);  boolean lastChunk = isLastChunk(response); request.handleChunk(lastChunk, response); if (lastChunk || request.isCancelled()) { this.requests.remove(key); request.complete(null); }  return Boolean.valueOf(true); })).booleanValue();
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
/*     */   public void cancelRequest(ChunkRequest request) {
/* 136 */     MiscUtil.locked(this.lock, () -> this.requests.remove(request.nonce));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void makeRequest(ChunkRequest request) {
/* 143 */     MiscUtil.locked(this.lock, () -> {
/*     */           this.requests.put(request.nonce, request);
/*     */           sendChunkRequest(request.getRequest());
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void sendChunkRequest(DataObject request) {
/* 151 */     this.client.sendChunkRequest(request);
/*     */   }
/*     */   
/*     */   private class ChunkRequest
/*     */     extends CompletableFuture<Void>
/*     */   {
/*     */     private final BiConsumer<Boolean, List<Member>> handler;
/*     */     private final GuildImpl guild;
/*     */     private final DataObject request;
/*     */     private final long nonce;
/*     */     private long startTime;
/*     */     
/*     */     public ChunkRequest(BiConsumer<Boolean, List<Member>> handler, GuildImpl guild, DataObject request) {
/* 164 */       this.handler = handler;
/* 165 */       this.guild = guild;
/* 166 */       this.nonce = ThreadLocalRandom.current().nextLong() & 0xFFFFFFFFFFFFFFFEL;
/* 167 */       this.request = request.put("nonce", getNonce());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isNonce(String nonce) {
/* 172 */       return (this.nonce == Long.parseLong(nonce));
/*     */     }
/*     */ 
/*     */     
/*     */     public String getNonce() {
/* 177 */       return String.valueOf(this.nonce);
/*     */     }
/*     */ 
/*     */     
/*     */     public long getAge() {
/* 182 */       return (this.startTime <= 0L) ? 0L : (System.currentTimeMillis() - this.startTime);
/*     */     }
/*     */ 
/*     */     
/*     */     public DataObject getRequest() {
/* 187 */       this.startTime = System.currentTimeMillis();
/* 188 */       return this.request;
/*     */     }
/*     */ 
/*     */     
/*     */     private List<Member> toMembers(DataObject chunk) {
/* 193 */       EntityBuilder builder = this.guild.getJDA().getEntityBuilder();
/* 194 */       DataArray memberArray = chunk.getArray("members");
/*     */ 
/*     */       
/* 197 */       TLongObjectMap<DataObject> presences = chunk.optArray("presences").map(it -> builder.convertToUserMap((), it)).orElseGet(TLongObjectHashMap::new);
/* 198 */       List<Member> collect = new ArrayList<>(memberArray.length());
/* 199 */       for (int i = 0; i < memberArray.length(); i++) {
/*     */         
/* 201 */         DataObject json = memberArray.getObject(i);
/* 202 */         long userId = json.getObject("user").getUnsignedLong("id");
/* 203 */         DataObject presence = (DataObject)presences.get(userId);
/* 204 */         MemberImpl member = builder.createMember(this.guild, json, null, presence);
/* 205 */         builder.updateMemberCache(member);
/* 206 */         collect.add(member);
/*     */       } 
/* 208 */       return collect;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void handleChunk(boolean last, DataObject chunk) {
/*     */       try {
/* 215 */         if (!isDone()) {
/* 216 */           this.handler.accept(Boolean.valueOf(last), toMembers(chunk));
/*     */         }
/* 218 */       } catch (Throwable ex) {
/*     */         
/* 220 */         completeExceptionally(ex);
/* 221 */         if (ex instanceof Error) {
/* 222 */           throw (Error)ex;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean cancel(boolean mayInterruptIfRunning) {
/* 229 */       MemberChunkManager.this.client.cancelChunkRequest(getNonce());
/* 230 */       MemberChunkManager.this.cancelRequest(this);
/* 231 */       return super.cancel(mayInterruptIfRunning);
/*     */     }
/*     */   }
/*     */   
/*     */   private class TimeoutHandler
/*     */     implements Runnable {
/*     */     private TimeoutHandler() {}
/*     */     
/*     */     public void run() {
/* 240 */       MiscUtil.locked(MemberChunkManager.this.lock, () -> {
/*     */             MemberChunkManager.this.requests.forEachValue(());
/*     */             MemberChunkManager.this.requests.valueCollection().removeIf(CompletableFuture::isDone);
/*     */           });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\MemberChunkManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */