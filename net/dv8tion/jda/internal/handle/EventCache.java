/*     */ package net.dv8tion.jda.internal.handle;
/*     */ 
/*     */ import gnu.trove.iterator.TLongObjectIterator;
/*     */ import gnu.trove.map.TLongObjectMap;
/*     */ import gnu.trove.map.hash.TLongObjectHashMap;
/*     */ import java.util.EnumMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.utils.CacheConsumer;
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
/*     */ public class EventCache
/*     */ {
/*  33 */   public static final Logger LOG = JDALogger.getLog(EventCache.class);
/*     */   
/*     */   public static final long TIMEOUT_AMOUNT = 100L;
/*  36 */   private final EnumMap<Type, TLongObjectMap<List<CacheNode>>> eventCache = new EnumMap<>(Type.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void timeout(long responseTotal) {
/*  42 */     if (this.eventCache.isEmpty())
/*     */       return; 
/*  44 */     AtomicInteger count = new AtomicInteger();
/*  45 */     this.eventCache.forEach((type, map) -> {
/*     */           if (map.isEmpty()) {
/*     */             return;
/*     */           }
/*     */ 
/*     */           
/*     */           TLongObjectIterator<List<CacheNode>> iterator = map.iterator();
/*     */ 
/*     */           
/*     */           while (iterator.hasNext()) {
/*     */             iterator.advance();
/*     */ 
/*     */             
/*     */             long triggerId = iterator.key();
/*     */             
/*     */             List<CacheNode> cache = (List<CacheNode>)iterator.value();
/*     */             
/*     */             cache.removeIf(());
/*     */             
/*     */             if (cache.isEmpty()) {
/*     */               iterator.remove();
/*     */             }
/*     */           } 
/*     */         });
/*     */     
/*  70 */     int amount = count.get();
/*  71 */     if (amount > 0) {
/*  72 */       LOG.debug("Removed {} events from cache that were too old to be recycled", Integer.valueOf(amount));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void cache(Type type, long triggerId, long responseTotal, DataObject event, CacheConsumer handler) {
/*  78 */     TLongObjectMap<List<CacheNode>> triggerCache = this.eventCache.computeIfAbsent(type, k -> new TLongObjectHashMap());
/*     */     
/*  80 */     List<CacheNode> items = (List<CacheNode>)triggerCache.get(triggerId);
/*  81 */     if (items == null) {
/*     */       
/*  83 */       items = new LinkedList<>();
/*  84 */       triggerCache.put(triggerId, items);
/*     */     } 
/*     */     
/*  87 */     items.add(new CacheNode(responseTotal, event, handler));
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void playbackCache(Type type, long triggerId) {
/*  92 */     TLongObjectMap<List<CacheNode>> typeCache = this.eventCache.get(type);
/*  93 */     if (typeCache == null) {
/*     */       return;
/*     */     }
/*  96 */     List<CacheNode> items = (List<CacheNode>)typeCache.remove(triggerId);
/*  97 */     if (items != null && !items.isEmpty()) {
/*     */       
/*  99 */       LOG.debug("Replaying {} events from the EventCache for type {} with id: {}", new Object[] {
/* 100 */             Integer.valueOf(items.size()), type, Long.valueOf(triggerId) });
/* 101 */       for (CacheNode item : items) {
/* 102 */         item.execute();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized int size() {
/* 108 */     return 
/*     */ 
/*     */       
/* 111 */       (int)this.eventCache.values().stream().mapToLong(typeMap -> typeMap.valueCollection().stream().mapToLong(List::size).sum()).sum();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void clear() {
/* 116 */     this.eventCache.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void clear(Type type, long id) {
/* 121 */     TLongObjectMap<List<CacheNode>> typeCache = this.eventCache.get(type);
/* 122 */     if (typeCache == null) {
/*     */       return;
/*     */     }
/* 125 */     List<CacheNode> events = (List<CacheNode>)typeCache.remove(id);
/* 126 */     if (events != null)
/* 127 */       LOG.debug("Clearing cache for type {} with ID {} (Size: {})", new Object[] { type, Long.valueOf(id), Integer.valueOf(events.size()) }); 
/*     */   }
/*     */   
/*     */   public enum Type
/*     */   {
/* 132 */     USER, MEMBER, GUILD, CHANNEL, ROLE, RELATIONSHIP, CALL;
/*     */   }
/*     */ 
/*     */   
/*     */   private class CacheNode
/*     */   {
/*     */     private final long responseTotal;
/*     */     private final DataObject event;
/*     */     private final CacheConsumer callback;
/*     */     
/*     */     public CacheNode(long responseTotal, DataObject event, CacheConsumer callback) {
/* 143 */       this.responseTotal = responseTotal;
/* 144 */       this.event = event;
/* 145 */       this.callback = callback;
/*     */     }
/*     */ 
/*     */     
/*     */     void execute() {
/* 150 */       this.callback.execute(this.responseTotal, this.event);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\EventCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */