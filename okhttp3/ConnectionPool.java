/*     */ package okhttp3;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.net.Socket;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.annotation.Nullable;
/*     */ import okhttp3.internal.Util;
/*     */ import okhttp3.internal.connection.RealConnection;
/*     */ import okhttp3.internal.connection.RouteDatabase;
/*     */ import okhttp3.internal.connection.StreamAllocation;
/*     */ import okhttp3.internal.platform.Platform;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ConnectionPool
/*     */ {
/*  50 */   private static final Executor executor = new ThreadPoolExecutor(0, 2147483647, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), 
/*     */       
/*  52 */       Util.threadFactory("OkHttp ConnectionPool", true));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int maxIdleConnections;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final long keepAliveDurationNs;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Runnable cleanupRunnable;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Deque<RealConnection> connections;
/*     */ 
/*     */ 
/*     */   
/*     */   final RouteDatabase routeDatabase;
/*     */ 
/*     */ 
/*     */   
/*     */   boolean cleanupRunning;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnectionPool() {
/*  84 */     this(5, 5L, TimeUnit.MINUTES); } public ConnectionPool(int maxIdleConnections, long keepAliveDuration, TimeUnit timeUnit) { this.cleanupRunnable = (() -> { while (true) { long waitNanos = cleanup(System.nanoTime()); if (waitNanos == -1L)
/*     */             return;  if (waitNanos > 0L) { long waitMillis = waitNanos / 1000000L; waitNanos -= waitMillis * 1000000L; synchronized (this) { try { wait(waitMillis, (int)waitNanos); } catch (InterruptedException interruptedException) {} }  }
/*     */            }
/*     */       
/*  88 */       }); this.connections = new ArrayDeque<>(); this.routeDatabase = new RouteDatabase(); this.maxIdleConnections = maxIdleConnections;
/*  89 */     this.keepAliveDurationNs = timeUnit.toNanos(keepAliveDuration);
/*     */ 
/*     */     
/*  92 */     if (keepAliveDuration <= 0L) {
/*  93 */       throw new IllegalArgumentException("keepAliveDuration <= 0: " + keepAliveDuration);
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int idleConnectionCount() {
/*  99 */     int total = 0;
/* 100 */     for (RealConnection connection : this.connections) {
/* 101 */       if (connection.allocations.isEmpty()) total++; 
/*     */     } 
/* 103 */     return total;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int connectionCount() {
/* 108 */     return this.connections.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void acquire(Address address, StreamAllocation streamAllocation, @Nullable Route route) {
/* 116 */     assert Thread.holdsLock(this);
/* 117 */     for (RealConnection connection : this.connections) {
/* 118 */       if (connection.isEligible(address, route)) {
/* 119 */         streamAllocation.acquire(connection, true);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   Socket deduplicate(Address address, StreamAllocation streamAllocation) {
/* 130 */     assert Thread.holdsLock(this);
/* 131 */     for (RealConnection connection : this.connections) {
/* 132 */       if (connection.isEligible(address, null) && connection
/* 133 */         .isMultiplexed() && connection != streamAllocation
/* 134 */         .connection()) {
/* 135 */         return streamAllocation.releaseAndAcquire(connection);
/*     */       }
/*     */     } 
/* 138 */     return null;
/*     */   }
/*     */   
/*     */   void put(RealConnection connection) {
/* 142 */     assert Thread.holdsLock(this);
/* 143 */     if (!this.cleanupRunning) {
/* 144 */       this.cleanupRunning = true;
/* 145 */       executor.execute(this.cleanupRunnable);
/*     */     } 
/* 147 */     this.connections.add(connection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean connectionBecameIdle(RealConnection connection) {
/* 155 */     assert Thread.holdsLock(this);
/* 156 */     if (connection.noNewStreams || this.maxIdleConnections == 0) {
/* 157 */       this.connections.remove(connection);
/* 158 */       return true;
/*     */     } 
/* 160 */     notifyAll();
/* 161 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void evictAll() {
/* 167 */     List<RealConnection> evictedConnections = new ArrayList<>();
/* 168 */     synchronized (this) {
/* 169 */       for (Iterator<RealConnection> i = this.connections.iterator(); i.hasNext(); ) {
/* 170 */         RealConnection connection = i.next();
/* 171 */         if (connection.allocations.isEmpty()) {
/* 172 */           connection.noNewStreams = true;
/* 173 */           evictedConnections.add(connection);
/* 174 */           i.remove();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 179 */     for (RealConnection connection : evictedConnections) {
/* 180 */       Util.closeQuietly(connection.socket());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long cleanup(long now) {
/* 192 */     int inUseConnectionCount = 0;
/* 193 */     int idleConnectionCount = 0;
/* 194 */     RealConnection longestIdleConnection = null;
/* 195 */     long longestIdleDurationNs = Long.MIN_VALUE;
/*     */ 
/*     */     
/* 198 */     synchronized (this) {
/* 199 */       for (Iterator<RealConnection> i = this.connections.iterator(); i.hasNext(); ) {
/* 200 */         RealConnection connection = i.next();
/*     */ 
/*     */         
/* 203 */         if (pruneAndGetAllocationCount(connection, now) > 0) {
/* 204 */           inUseConnectionCount++;
/*     */           
/*     */           continue;
/*     */         } 
/* 208 */         idleConnectionCount++;
/*     */ 
/*     */         
/* 211 */         long idleDurationNs = now - connection.idleAtNanos;
/* 212 */         if (idleDurationNs > longestIdleDurationNs) {
/* 213 */           longestIdleDurationNs = idleDurationNs;
/* 214 */           longestIdleConnection = connection;
/*     */         } 
/*     */       } 
/*     */       
/* 218 */       if (longestIdleDurationNs >= this.keepAliveDurationNs || idleConnectionCount > this.maxIdleConnections)
/*     */       
/*     */       { 
/*     */         
/* 222 */         this.connections.remove(longestIdleConnection); }
/* 223 */       else { if (idleConnectionCount > 0)
/*     */         {
/* 225 */           return this.keepAliveDurationNs - longestIdleDurationNs; } 
/* 226 */         if (inUseConnectionCount > 0)
/*     */         {
/* 228 */           return this.keepAliveDurationNs;
/*     */         }
/*     */         
/* 231 */         this.cleanupRunning = false;
/* 232 */         return -1L; }
/*     */     
/*     */     } 
/*     */     
/* 236 */     Util.closeQuietly(longestIdleConnection.socket());
/*     */ 
/*     */     
/* 239 */     return 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int pruneAndGetAllocationCount(RealConnection connection, long now) {
/* 249 */     List<Reference<StreamAllocation>> references = connection.allocations;
/* 250 */     for (int i = 0; i < references.size(); ) {
/* 251 */       Reference<StreamAllocation> reference = references.get(i);
/*     */       
/* 253 */       if (reference.get() != null) {
/* 254 */         i++;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 259 */       StreamAllocation.StreamAllocationReference streamAllocRef = (StreamAllocation.StreamAllocationReference)reference;
/*     */       
/* 261 */       String message = "A connection to " + connection.route().address().url() + " was leaked. Did you forget to close a response body?";
/*     */       
/* 263 */       Platform.get().logCloseableLeak(message, streamAllocRef.callStackTrace);
/*     */       
/* 265 */       references.remove(i);
/* 266 */       connection.noNewStreams = true;
/*     */ 
/*     */       
/* 269 */       if (references.isEmpty()) {
/* 270 */         connection.idleAtNanos = now - this.keepAliveDurationNs;
/* 271 */         return 0;
/*     */       } 
/*     */     } 
/*     */     
/* 275 */     return references.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\ConnectionPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */