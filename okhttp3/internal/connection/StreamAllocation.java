/*     */ package okhttp3.internal.connection;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.net.Socket;
/*     */ import java.util.List;
/*     */ import okhttp3.Address;
/*     */ import okhttp3.Call;
/*     */ import okhttp3.Connection;
/*     */ import okhttp3.ConnectionPool;
/*     */ import okhttp3.EventListener;
/*     */ import okhttp3.Interceptor;
/*     */ import okhttp3.OkHttpClient;
/*     */ import okhttp3.Route;
/*     */ import okhttp3.internal.Internal;
/*     */ import okhttp3.internal.Util;
/*     */ import okhttp3.internal.http.HttpCodec;
/*     */ import okhttp3.internal.http2.ErrorCode;
/*     */ import okhttp3.internal.http2.StreamResetException;
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
/*     */ public final class StreamAllocation
/*     */ {
/*     */   public final Address address;
/*     */   private RouteSelector.Selection routeSelection;
/*     */   private Route route;
/*     */   private final ConnectionPool connectionPool;
/*     */   public final Call call;
/*     */   public final EventListener eventListener;
/*     */   private final Object callStackTrace;
/*     */   private final RouteSelector routeSelector;
/*     */   private int refusedStreamCount;
/*     */   private RealConnection connection;
/*     */   private boolean reportedAcquired;
/*     */   private boolean released;
/*     */   private boolean canceled;
/*     */   private HttpCodec codec;
/*     */   
/*     */   public StreamAllocation(ConnectionPool connectionPool, Address address, Call call, EventListener eventListener, Object callStackTrace) {
/*  97 */     this.connectionPool = connectionPool;
/*  98 */     this.address = address;
/*  99 */     this.call = call;
/* 100 */     this.eventListener = eventListener;
/* 101 */     this.routeSelector = new RouteSelector(address, routeDatabase(), call, eventListener);
/* 102 */     this.callStackTrace = callStackTrace;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpCodec newStream(OkHttpClient client, Interceptor.Chain chain, boolean doExtensiveHealthChecks) {
/* 107 */     int connectTimeout = chain.connectTimeoutMillis();
/* 108 */     int readTimeout = chain.readTimeoutMillis();
/* 109 */     int writeTimeout = chain.writeTimeoutMillis();
/* 110 */     int pingIntervalMillis = client.pingIntervalMillis();
/* 111 */     boolean connectionRetryEnabled = client.retryOnConnectionFailure();
/*     */     
/*     */     try {
/* 114 */       RealConnection resultConnection = findHealthyConnection(connectTimeout, readTimeout, writeTimeout, pingIntervalMillis, connectionRetryEnabled, doExtensiveHealthChecks);
/*     */       
/* 116 */       HttpCodec resultCodec = resultConnection.newCodec(client, chain, this);
/*     */       
/* 118 */       synchronized (this.connectionPool) {
/* 119 */         this.codec = resultCodec;
/* 120 */         return resultCodec;
/*     */       } 
/* 122 */     } catch (IOException e) {
/* 123 */       throw new RouteException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RealConnection findHealthyConnection(int connectTimeout, int readTimeout, int writeTimeout, int pingIntervalMillis, boolean connectionRetryEnabled, boolean doExtensiveHealthChecks) throws IOException {
/*     */     RealConnection candidate;
/*     */     while (true) {
/* 135 */       candidate = findConnection(connectTimeout, readTimeout, writeTimeout, pingIntervalMillis, connectionRetryEnabled);
/*     */ 
/*     */ 
/*     */       
/* 139 */       synchronized (this.connectionPool) {
/* 140 */         if (candidate.successCount == 0) {
/* 141 */           return candidate;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 147 */       if (!candidate.isHealthy(doExtensiveHealthChecks)) {
/* 148 */         noNewStreams(); continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 152 */     return candidate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RealConnection findConnection(int connectTimeout, int readTimeout, int writeTimeout, int pingIntervalMillis, boolean connectionRetryEnabled) throws IOException {
/*     */     Connection releasedConnection;
/*     */     Socket toClose;
/* 162 */     boolean foundPooledConnection = false;
/* 163 */     RealConnection result = null;
/* 164 */     Route selectedRoute = null;
/*     */ 
/*     */     
/* 167 */     synchronized (this.connectionPool) {
/* 168 */       if (this.released) throw new IllegalStateException("released"); 
/* 169 */       if (this.codec != null) throw new IllegalStateException("codec != null"); 
/* 170 */       if (this.canceled) throw new IOException("Canceled");
/*     */ 
/*     */ 
/*     */       
/* 174 */       releasedConnection = this.connection;
/* 175 */       toClose = releaseIfNoNewStreams();
/* 176 */       if (this.connection != null) {
/*     */         
/* 178 */         result = this.connection;
/* 179 */         releasedConnection = null;
/*     */       } 
/* 181 */       if (!this.reportedAcquired)
/*     */       {
/* 183 */         releasedConnection = null;
/*     */       }
/*     */       
/* 186 */       if (result == null) {
/*     */         
/* 188 */         Internal.instance.acquire(this.connectionPool, this.address, this, null);
/* 189 */         if (this.connection != null) {
/* 190 */           foundPooledConnection = true;
/* 191 */           result = this.connection;
/*     */         } else {
/* 193 */           selectedRoute = this.route;
/*     */         } 
/*     */       } 
/*     */     } 
/* 197 */     Util.closeQuietly(toClose);
/*     */     
/* 199 */     if (releasedConnection != null) {
/* 200 */       this.eventListener.connectionReleased(this.call, releasedConnection);
/*     */     }
/* 202 */     if (foundPooledConnection) {
/* 203 */       this.eventListener.connectionAcquired(this.call, result);
/*     */     }
/* 205 */     if (result != null)
/*     */     {
/* 207 */       return result;
/*     */     }
/*     */ 
/*     */     
/* 211 */     boolean newRouteSelection = false;
/* 212 */     if (selectedRoute == null && (this.routeSelection == null || !this.routeSelection.hasNext())) {
/* 213 */       newRouteSelection = true;
/* 214 */       this.routeSelection = this.routeSelector.next();
/*     */     } 
/*     */     
/* 217 */     synchronized (this.connectionPool) {
/* 218 */       if (this.canceled) throw new IOException("Canceled");
/*     */       
/* 220 */       if (newRouteSelection) {
/*     */ 
/*     */         
/* 223 */         List<Route> routes = this.routeSelection.getAll();
/* 224 */         for (int i = 0, size = routes.size(); i < size; i++) {
/* 225 */           Route route = routes.get(i);
/* 226 */           Internal.instance.acquire(this.connectionPool, this.address, this, route);
/* 227 */           if (this.connection != null) {
/* 228 */             foundPooledConnection = true;
/* 229 */             result = this.connection;
/* 230 */             this.route = route;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 236 */       if (!foundPooledConnection) {
/* 237 */         if (selectedRoute == null) {
/* 238 */           selectedRoute = this.routeSelection.next();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 243 */         this.route = selectedRoute;
/* 244 */         this.refusedStreamCount = 0;
/* 245 */         result = new RealConnection(this.connectionPool, selectedRoute);
/* 246 */         acquire(result, false);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 251 */     if (foundPooledConnection) {
/* 252 */       this.eventListener.connectionAcquired(this.call, result);
/* 253 */       return result;
/*     */     } 
/*     */ 
/*     */     
/* 257 */     result.connect(connectTimeout, readTimeout, writeTimeout, pingIntervalMillis, connectionRetryEnabled, this.call, this.eventListener);
/*     */     
/* 259 */     routeDatabase().connected(result.route());
/*     */     
/* 261 */     Socket socket = null;
/* 262 */     synchronized (this.connectionPool) {
/* 263 */       this.reportedAcquired = true;
/*     */ 
/*     */       
/* 266 */       Internal.instance.put(this.connectionPool, result);
/*     */ 
/*     */ 
/*     */       
/* 270 */       if (result.isMultiplexed()) {
/* 271 */         socket = Internal.instance.deduplicate(this.connectionPool, this.address, this);
/* 272 */         result = this.connection;
/*     */       } 
/*     */     } 
/* 275 */     Util.closeQuietly(socket);
/*     */     
/* 277 */     this.eventListener.connectionAcquired(this.call, result);
/* 278 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Socket releaseIfNoNewStreams() {
/* 288 */     assert Thread.holdsLock(this.connectionPool);
/* 289 */     RealConnection allocatedConnection = this.connection;
/* 290 */     if (allocatedConnection != null && allocatedConnection.noNewStreams) {
/* 291 */       return deallocate(false, false, true);
/*     */     }
/* 293 */     return null; } public void streamFinished(boolean noNewStreams, HttpCodec codec, long bytesRead, IOException e) {
/*     */     Socket socket;
/*     */     Connection releasedConnection;
/*     */     boolean callEnd;
/* 297 */     this.eventListener.responseBodyEnd(this.call, bytesRead);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 302 */     synchronized (this.connectionPool) {
/* 303 */       if (codec == null || codec != this.codec) {
/* 304 */         throw new IllegalStateException("expected " + this.codec + " but was " + codec);
/*     */       }
/* 306 */       if (!noNewStreams) {
/* 307 */         this.connection.successCount++;
/*     */       }
/* 309 */       releasedConnection = this.connection;
/* 310 */       socket = deallocate(noNewStreams, false, true);
/* 311 */       if (this.connection != null) releasedConnection = null; 
/* 312 */       callEnd = this.released;
/*     */     } 
/* 314 */     Util.closeQuietly(socket);
/* 315 */     if (releasedConnection != null) {
/* 316 */       this.eventListener.connectionReleased(this.call, releasedConnection);
/*     */     }
/*     */     
/* 319 */     if (e != null) {
/* 320 */       e = Internal.instance.timeoutExit(this.call, e);
/* 321 */       this.eventListener.callFailed(this.call, e);
/* 322 */     } else if (callEnd) {
/* 323 */       Internal.instance.timeoutExit(this.call, null);
/* 324 */       this.eventListener.callEnd(this.call);
/*     */     } 
/*     */   }
/*     */   
/*     */   public HttpCodec codec() {
/* 329 */     synchronized (this.connectionPool) {
/* 330 */       return this.codec;
/*     */     } 
/*     */   }
/*     */   
/*     */   private RouteDatabase routeDatabase() {
/* 335 */     return Internal.instance.routeDatabase(this.connectionPool);
/*     */   }
/*     */   
/*     */   public Route route() {
/* 339 */     return this.route;
/*     */   }
/*     */   
/*     */   public synchronized RealConnection connection() {
/* 343 */     return this.connection;
/*     */   }
/*     */   
/*     */   public void release(boolean callEnd) {
/*     */     Socket socket;
/*     */     Connection releasedConnection;
/* 349 */     synchronized (this.connectionPool) {
/* 350 */       releasedConnection = this.connection;
/* 351 */       socket = deallocate(false, true, false);
/* 352 */       if (this.connection != null) releasedConnection = null; 
/*     */     } 
/* 354 */     Util.closeQuietly(socket);
/* 355 */     if (releasedConnection != null) {
/* 356 */       if (callEnd) {
/* 357 */         Internal.instance.timeoutExit(this.call, null);
/*     */       }
/* 359 */       this.eventListener.connectionReleased(this.call, releasedConnection);
/* 360 */       if (callEnd) {
/* 361 */         this.eventListener.callEnd(this.call);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void noNewStreams() {
/*     */     Socket socket;
/*     */     Connection releasedConnection;
/* 370 */     synchronized (this.connectionPool) {
/* 371 */       releasedConnection = this.connection;
/* 372 */       socket = deallocate(true, false, false);
/* 373 */       if (this.connection != null) releasedConnection = null; 
/*     */     } 
/* 375 */     Util.closeQuietly(socket);
/* 376 */     if (releasedConnection != null) {
/* 377 */       this.eventListener.connectionReleased(this.call, releasedConnection);
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
/*     */   private Socket deallocate(boolean noNewStreams, boolean released, boolean streamFinished) {
/* 389 */     assert Thread.holdsLock(this.connectionPool);
/*     */     
/* 391 */     if (streamFinished) {
/* 392 */       this.codec = null;
/*     */     }
/* 394 */     if (released) {
/* 395 */       this.released = true;
/*     */     }
/* 397 */     Socket socket = null;
/* 398 */     if (this.connection != null) {
/* 399 */       if (noNewStreams) {
/* 400 */         this.connection.noNewStreams = true;
/*     */       }
/* 402 */       if (this.codec == null && (this.released || this.connection.noNewStreams)) {
/* 403 */         release(this.connection);
/* 404 */         if (this.connection.allocations.isEmpty()) {
/* 405 */           this.connection.idleAtNanos = System.nanoTime();
/* 406 */           if (Internal.instance.connectionBecameIdle(this.connectionPool, this.connection)) {
/* 407 */             socket = this.connection.socket();
/*     */           }
/*     */         } 
/* 410 */         this.connection = null;
/*     */       } 
/*     */     } 
/* 413 */     return socket;
/*     */   }
/*     */   
/*     */   public void cancel() {
/*     */     HttpCodec codecToCancel;
/*     */     RealConnection connectionToCancel;
/* 419 */     synchronized (this.connectionPool) {
/* 420 */       this.canceled = true;
/* 421 */       codecToCancel = this.codec;
/* 422 */       connectionToCancel = this.connection;
/*     */     } 
/* 424 */     if (codecToCancel != null) {
/* 425 */       codecToCancel.cancel();
/* 426 */     } else if (connectionToCancel != null) {
/* 427 */       connectionToCancel.cancel();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void streamFailed(IOException e) {
/*     */     Socket socket;
/*     */     Connection releasedConnection;
/* 434 */     boolean noNewStreams = false;
/*     */     
/* 436 */     synchronized (this.connectionPool) {
/* 437 */       if (e instanceof StreamResetException) {
/* 438 */         ErrorCode errorCode = ((StreamResetException)e).errorCode;
/* 439 */         if (errorCode == ErrorCode.REFUSED_STREAM) {
/*     */           
/* 441 */           this.refusedStreamCount++;
/* 442 */           if (this.refusedStreamCount > 1) {
/* 443 */             noNewStreams = true;
/* 444 */             this.route = null;
/*     */           } 
/* 446 */         } else if (errorCode != ErrorCode.CANCEL) {
/*     */           
/* 448 */           noNewStreams = true;
/* 449 */           this.route = null;
/*     */         } 
/* 451 */       } else if (this.connection != null && (
/* 452 */         !this.connection.isMultiplexed() || e instanceof okhttp3.internal.http2.ConnectionShutdownException)) {
/* 453 */         noNewStreams = true;
/*     */ 
/*     */         
/* 456 */         if (this.connection.successCount == 0) {
/* 457 */           if (this.route != null && e != null) {
/* 458 */             this.routeSelector.connectFailed(this.route, e);
/*     */           }
/* 460 */           this.route = null;
/*     */         } 
/*     */       } 
/* 463 */       releasedConnection = this.connection;
/* 464 */       socket = deallocate(noNewStreams, false, true);
/* 465 */       if (this.connection != null || !this.reportedAcquired) releasedConnection = null;
/*     */     
/*     */     } 
/* 468 */     Util.closeQuietly(socket);
/* 469 */     if (releasedConnection != null) {
/* 470 */       this.eventListener.connectionReleased(this.call, releasedConnection);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void acquire(RealConnection connection, boolean reportedAcquired) {
/* 479 */     assert Thread.holdsLock(this.connectionPool);
/* 480 */     if (this.connection != null) throw new IllegalStateException();
/*     */     
/* 482 */     this.connection = connection;
/* 483 */     this.reportedAcquired = reportedAcquired;
/* 484 */     connection.allocations.add(new StreamAllocationReference(this, this.callStackTrace));
/*     */   }
/*     */ 
/*     */   
/*     */   private void release(RealConnection connection) {
/* 489 */     for (int i = 0, size = connection.allocations.size(); i < size; i++) {
/* 490 */       Reference<StreamAllocation> reference = connection.allocations.get(i);
/* 491 */       if (reference.get() == this) {
/* 492 */         connection.allocations.remove(i);
/*     */         return;
/*     */       } 
/*     */     } 
/* 496 */     throw new IllegalStateException();
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
/*     */   public Socket releaseAndAcquire(RealConnection newConnection) {
/* 508 */     assert Thread.holdsLock(this.connectionPool);
/* 509 */     if (this.codec != null || this.connection.allocations.size() != 1) throw new IllegalStateException();
/*     */ 
/*     */     
/* 512 */     Reference<StreamAllocation> onlyAllocation = this.connection.allocations.get(0);
/* 513 */     Socket socket = deallocate(true, false, false);
/*     */ 
/*     */     
/* 516 */     this.connection = newConnection;
/* 517 */     newConnection.allocations.add(onlyAllocation);
/*     */     
/* 519 */     return socket;
/*     */   }
/*     */   
/*     */   public boolean hasMoreRoutes() {
/* 523 */     return (this.route != null || (this.routeSelection != null && this.routeSelection
/* 524 */       .hasNext()) || this.routeSelector
/* 525 */       .hasNext());
/*     */   }
/*     */   
/*     */   public String toString() {
/* 529 */     RealConnection connection = connection();
/* 530 */     return (connection != null) ? connection.toString() : this.address.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class StreamAllocationReference
/*     */     extends WeakReference<StreamAllocation>
/*     */   {
/*     */     public final Object callStackTrace;
/*     */ 
/*     */     
/*     */     StreamAllocationReference(StreamAllocation referent, Object callStackTrace) {
/* 541 */       super(referent);
/* 542 */       this.callStackTrace = callStackTrace;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\connection\StreamAllocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */