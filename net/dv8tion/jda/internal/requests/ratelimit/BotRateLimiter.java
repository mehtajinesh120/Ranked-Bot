/*     */ package net.dv8tion.jda.internal.requests.ratelimit;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentLinkedDeque;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.internal.requests.RateLimiter;
/*     */ import net.dv8tion.jda.internal.requests.Requester;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import okhttp3.Headers;
/*     */ import okhttp3.Response;
/*     */ import org.jetbrains.annotations.Contract;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BotRateLimiter
/*     */   extends RateLimiter
/*     */ {
/*     */   private static final String RESET_AFTER_HEADER = "X-RateLimit-Reset-After";
/*     */   private static final String RESET_HEADER = "X-RateLimit-Reset";
/*     */   private static final String LIMIT_HEADER = "X-RateLimit-Limit";
/*     */   private static final String REMAINING_HEADER = "X-RateLimit-Remaining";
/*     */   private static final String GLOBAL_HEADER = "X-RateLimit-Global";
/*     */   private static final String HASH_HEADER = "X-RateLimit-Bucket";
/*     */   private static final String RETRY_AFTER_HEADER = "Retry-After";
/*     */   private static final String UNLIMITED_BUCKET = "unlimited";
/*  79 */   private final ReentrantLock bucketLock = new ReentrantLock();
/*     */   
/*  81 */   private final Set<Route> hitRatelimit = ConcurrentHashMap.newKeySet(5);
/*     */   
/*  83 */   private final Map<Route, String> hashes = new ConcurrentHashMap<>();
/*     */   
/*  85 */   private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
/*     */   
/*  87 */   private final Map<Bucket, Future<?>> rateLimitQueue = new ConcurrentHashMap<>();
/*     */   
/*     */   private Future<?> cleanupWorker;
/*     */   
/*     */   public BotRateLimiter(Requester requester) {
/*  92 */     super(requester);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() {
/*  98 */     this.cleanupWorker = getScheduler().scheduleAtFixedRate(this::cleanup, 30L, 30L, TimeUnit.SECONDS);
/*     */   }
/*     */ 
/*     */   
/*     */   private ScheduledExecutorService getScheduler() {
/* 103 */     return this.requester.getJDA().getRateLimitPool();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int cancelRequests() {
/* 109 */     return ((Integer)MiscUtil.locked(this.bucketLock, () -> { AtomicInteger count = new AtomicInteger(0); this.buckets.values().stream().map(Bucket::getRequests).flatMap(Collection::stream).filter(()).forEach(()); int cancelled = count.get(); if (cancelled == 1) { RateLimiter.log.warn("Cancelled 1 request!"); } else if (cancelled > 1) { RateLimiter.log.warn("Cancelled {} requests!", Integer.valueOf(cancelled)); }  return Integer.valueOf(cancelled); })).intValue();
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
/*     */ 
/*     */   
/*     */   private void cleanup() {
/* 135 */     MiscUtil.locked(this.bucketLock, () -> {
/*     */           int size = this.buckets.size();
/*     */           Iterator<Map.Entry<String, Bucket>> entries = this.buckets.entrySet().iterator();
/*     */           while (entries.hasNext()) {
/*     */             Map.Entry<String, Bucket> entry = entries.next();
/*     */             String key = entry.getKey();
/*     */             Bucket bucket = entry.getValue();
/*     */             bucket.requests.removeIf(Request::isSkipped);
/*     */             if (bucket.isUnlimited() && bucket.requests.isEmpty()) {
/*     */               entries.remove();
/*     */               continue;
/*     */             } 
/*     */             if (bucket.requests.isEmpty() && bucket.reset <= getNow()) {
/*     */               entries.remove();
/*     */             }
/*     */           } 
/*     */           size -= this.buckets.size();
/*     */           if (size > 0) {
/*     */             log.debug("Removed {} expired buckets", Integer.valueOf(size));
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getRouteHash(Route route) {
/* 163 */     return this.hashes.getOrDefault(route, "unlimited+" + route);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean stop() {
/* 169 */     return ((Boolean)MiscUtil.locked(this.bucketLock, () -> { if (this.isStopped) return Boolean.valueOf(false);  super.stop(); if (this.cleanupWorker != null) this.cleanupWorker.cancel(false);  cleanup(); int size = this.buckets.size(); if (!this.isShutdown && size > 0) { int average = (int)Math.ceil(this.buckets.values().stream().map(Bucket::getRequests).mapToInt(Collection::size).average().orElse(0.0D)); log.info("Waiting for {} bucket(s) to finish. Average queue size of {} requests", Integer.valueOf(size), Integer.valueOf(average)); }  return Boolean.valueOf((size < 1)); })).booleanValue();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Long getRateLimit(Route.CompiledRoute route) {
/* 196 */     Bucket bucket = getBucket(route, false);
/* 197 */     return Long.valueOf((bucket == null) ? 0L : bucket.getRateLimit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void queueRequest(Request request) {
/* 205 */     MiscUtil.locked(this.bucketLock, () -> {
/*     */           Bucket bucket = getBucket(request.getRoute(), true);
/*     */           bucket.enqueue(request);
/*     */           runBucket(bucket);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Long handleResponse(Route.CompiledRoute route, Response response) {
/* 215 */     return (Long)MiscUtil.locked(this.bucketLock, () -> {
/*     */           long rateLimit = updateBucket(route, response).getRateLimit();
/*     */           return (response.code() == 429) ? Long.valueOf(rateLimit) : null;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Bucket updateBucket(Route.CompiledRoute route, Response response) {
/* 226 */     return (Bucket)MiscUtil.locked(this.bucketLock, () -> {
/*     */           try {
/*     */             Bucket bucket = getBucket(route, true);
/*     */ 
/*     */             
/*     */             Headers headers = response.headers();
/*     */ 
/*     */             
/*     */             boolean global = (headers.get("X-RateLimit-Global") != null);
/*     */ 
/*     */             
/*     */             boolean cloudflare = (headers.get("via") == null);
/*     */             
/*     */             String hash = headers.get("X-RateLimit-Bucket");
/*     */             
/*     */             long now = getNow();
/*     */             
/*     */             Route baseRoute = route.getBaseRoute();
/*     */             
/*     */             if (hash != null) {
/*     */               if (!this.hashes.containsKey(baseRoute)) {
/*     */                 this.hashes.put(baseRoute, hash);
/*     */                 
/*     */                 log.debug("Caching bucket hash {} -> {}", baseRoute, hash);
/*     */               } 
/*     */               
/*     */               bucket = getBucket(route, true);
/*     */             } 
/*     */             
/*     */             if (response.code() == 429) {
/*     */               String retryAfterHeader = headers.get("Retry-After");
/*     */               
/*     */               long retryAfter = parseLong(retryAfterHeader) * 1000L;
/*     */               
/*     */               if (global) {
/*     */                 this.requester.getJDA().getSessionController().setGlobalRatelimit(now + retryAfter);
/*     */                 
/*     */                 log.error("Encountered global rate limit! Retry-After: {} ms", Long.valueOf(retryAfter));
/*     */               } else if (cloudflare) {
/*     */                 this.requester.getJDA().getSessionController().setGlobalRatelimit(now + retryAfter);
/*     */                 
/*     */                 log.error("Encountered cloudflare rate limit! Retry-After: {} s", Long.valueOf(retryAfter / 1000L));
/*     */               } else {
/* 269 */                 boolean firstHit = (this.hitRatelimit.add(baseRoute) && retryAfter < 60000L);
/*     */                 
/*     */                 bucket.remaining = 0;
/*     */                 
/*     */                 bucket.reset = getNow() + retryAfter;
/*     */                 
/*     */                 if (firstHit) {
/*     */                   log.debug("Encountered 429 on route {} with bucket {} Retry-After: {} ms", new Object[] { baseRoute, Bucket.access$1700(bucket), Long.valueOf(retryAfter) });
/*     */                 } else {
/*     */                   log.warn("Encountered 429 on route {} with bucket {} Retry-After: {} ms", new Object[] { baseRoute, Bucket.access$1700(bucket), Long.valueOf(retryAfter) });
/*     */                 } 
/*     */               } 
/*     */               
/*     */               return bucket;
/*     */             } 
/*     */             
/*     */             if (hash == null) {
/*     */               return bucket;
/*     */             }
/*     */             
/*     */             String limitHeader = headers.get("X-RateLimit-Limit");
/*     */             String remainingHeader = headers.get("X-RateLimit-Remaining");
/*     */             String resetAfterHeader = headers.get("X-RateLimit-Reset-After");
/*     */             String resetHeader = headers.get("X-RateLimit-Reset");
/*     */             bucket.limit = (int)Math.max(1L, parseLong(limitHeader));
/*     */             bucket.remaining = (int)parseLong(remainingHeader);
/*     */             if (this.requester.getJDA().isRelativeRateLimit()) {
/*     */               bucket.reset = now + parseDouble(resetAfterHeader);
/*     */             } else {
/*     */               bucket.reset = parseDouble(resetHeader);
/*     */             } 
/*     */             log.trace("Updated bucket {} to ({}/{}, {})", new Object[] { Bucket.access$1700(bucket), Integer.valueOf(Bucket.access$1500(bucket)), Integer.valueOf(Bucket.access$1800(bucket)), Long.valueOf(Bucket.access$1600(bucket) - now) });
/*     */             return bucket;
/* 302 */           } catch (Exception e) {
/*     */             Bucket bucket = getBucket(route, true);
/*     */             log.error("Encountered Exception while updating a bucket. Route: {} Bucket: {} Code: {} Headers:\n{}", new Object[] { route.getBaseRoute(), bucket, Integer.valueOf(response.code()), response.headers(), e });
/*     */             return bucket;
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract("_,true->!null")
/*     */   private Bucket getBucket(Route.CompiledRoute route, boolean create) {
/* 315 */     return (Bucket)MiscUtil.locked(this.bucketLock, () -> {
/*     */           String hash = getRouteHash(route.getBaseRoute());
/*     */           String bucketId = hash + ":" + route.getMajorParameters();
/*     */           Bucket bucket = this.buckets.get(bucketId);
/*     */           if (bucket == null && create) {
/*     */             this.buckets.put(bucketId, bucket = new Bucket(bucketId));
/*     */           }
/*     */           return bucket;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void runBucket(Bucket bucket) {
/* 331 */     if (this.isShutdown) {
/*     */       return;
/*     */     }
/* 334 */     MiscUtil.locked(this.bucketLock, () -> (Future)this.rateLimitQueue.computeIfAbsent(bucket, ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long parseLong(String input) {
/* 341 */     return (input == null) ? 0L : Long.parseLong(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long parseDouble(String input) {
/* 348 */     return (input == null) ? 0L : (long)(Double.parseDouble(input) * 1000.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getNow() {
/* 353 */     return System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   private class Bucket
/*     */     implements IBucket, Runnable
/*     */   {
/*     */     private final String bucketId;
/* 360 */     private final Deque<Request> requests = new ConcurrentLinkedDeque<>();
/*     */     
/* 362 */     private long reset = 0L;
/* 363 */     private int remaining = 1;
/* 364 */     private int limit = 1;
/*     */ 
/*     */     
/*     */     public Bucket(String bucketId) {
/* 368 */       this.bucketId = bucketId;
/*     */     }
/*     */ 
/*     */     
/*     */     public void enqueue(Request request) {
/* 373 */       this.requests.addLast(request);
/*     */     }
/*     */ 
/*     */     
/*     */     public void retry(Request request) {
/* 378 */       this.requests.addFirst(request);
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean isGlobalRateLimit() {
/* 383 */       return (BotRateLimiter.this.requester.getJDA().getSessionController().getGlobalRatelimit() > BotRateLimiter.this.getNow());
/*     */     }
/*     */ 
/*     */     
/*     */     public long getRateLimit() {
/* 388 */       long now = BotRateLimiter.this.getNow();
/* 389 */       long global = BotRateLimiter.this.requester.getJDA().getSessionController().getGlobalRatelimit();
/*     */       
/* 391 */       if (global > now) {
/* 392 */         return global - now;
/*     */       }
/* 394 */       if (this.reset <= now) {
/*     */ 
/*     */         
/* 397 */         this.remaining = this.limit;
/* 398 */         return 0L;
/*     */       } 
/*     */ 
/*     */       
/* 402 */       return (this.remaining < 1) ? (this.reset - now) : 0L;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getReset() {
/* 407 */       return this.reset;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRemaining() {
/* 412 */       return this.remaining;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getLimit() {
/* 417 */       return this.limit;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean isUnlimited() {
/* 422 */       return this.bucketId.startsWith("unlimited");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void backoff() {
/* 428 */       MiscUtil.locked(BotRateLimiter.this.bucketLock, () -> {
/*     */             BotRateLimiter.this.rateLimitQueue.remove(this);
/*     */             if (!this.requests.isEmpty()) {
/*     */               BotRateLimiter.this.runBucket(this);
/*     */             } else if (BotRateLimiter.this.isStopped) {
/*     */               BotRateLimiter.this.buckets.remove(this.bucketId);
/*     */             } 
/*     */             if (BotRateLimiter.this.isStopped && BotRateLimiter.this.buckets.isEmpty()) {
/*     */               BotRateLimiter.this.requester.getJDA().shutdownRequester();
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public void run() {
/* 442 */       BotRateLimiter.log.trace("Bucket {} is running {} requests", this.bucketId, Integer.valueOf(this.requests.size())); while (true)
/* 443 */       { if (!this.requests.isEmpty()) {
/*     */           
/* 445 */           Long rateLimit = Long.valueOf(getRateLimit());
/* 446 */           if (rateLimit.longValue() > 0L) {
/*     */ 
/*     */             
/* 449 */             Request request1 = this.requests.peekFirst();
/* 450 */             String baseRoute = (request1 != null) ? request1.getRoute().getBaseRoute().toString() : "N/A";
/* 451 */             if (!isGlobalRateLimit() && rateLimit.longValue() >= 1800000L)
/* 452 */               BotRateLimiter.log.warn("Encountered long {} minutes Rate-Limit on route {}", Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(rateLimit.longValue())), baseRoute); 
/* 453 */             BotRateLimiter.log.debug("Backing off {} ms for bucket {} on route {}", new Object[] { rateLimit, this.bucketId, baseRoute });
/*     */             
/*     */             break;
/*     */           } 
/* 457 */           Request request = this.requests.removeFirst();
/* 458 */           if (request.isSkipped())
/*     */             continue; 
/* 460 */           if (isUnlimited()) {
/*     */             
/* 462 */             boolean shouldSkip = ((Boolean)MiscUtil.locked(BotRateLimiter.this.bucketLock, () -> { Bucket bucket = BotRateLimiter.this.getBucket(request.getRoute(), true); if (bucket != this) { bucket.enqueue(request); BotRateLimiter.this.runBucket(bucket); return Boolean.valueOf(true); }  return Boolean.valueOf(false); })).booleanValue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 473 */             if (shouldSkip) {
/*     */               continue;
/*     */             }
/*     */           } 
/*     */           try {
/* 478 */             rateLimit = BotRateLimiter.this.requester.execute(request);
/* 479 */             if (rateLimit != null)
/* 480 */               retry(request); 
/*     */             continue;
/* 482 */           } catch (Throwable ex) {
/*     */             
/* 484 */             BotRateLimiter.log.error("Encountered exception trying to execute request", ex);
/* 485 */             if (ex instanceof Error)
/* 486 */               throw (Error)ex; 
/*     */           } 
/*     */         } else {
/*     */           break;
/*     */         } 
/* 491 */         backoff(); return; }  backoff();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Queue<Request> getRequests() {
/* 497 */       return this.requests;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 503 */       return this.bucketId;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\ratelimit\BotRateLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */