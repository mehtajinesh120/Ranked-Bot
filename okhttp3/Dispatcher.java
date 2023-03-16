/*     */ package okhttp3;
/*     */ 
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.annotation.Nullable;
/*     */ import okhttp3.internal.Util;
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
/*     */ public final class Dispatcher
/*     */ {
/*  40 */   private int maxRequests = 64;
/*  41 */   private int maxRequestsPerHost = 5;
/*     */   
/*     */   @Nullable
/*     */   private Runnable idleCallback;
/*     */   
/*     */   @Nullable
/*     */   private ExecutorService executorService;
/*  48 */   private final Deque<RealCall.AsyncCall> readyAsyncCalls = new ArrayDeque<>();
/*     */ 
/*     */   
/*  51 */   private final Deque<RealCall.AsyncCall> runningAsyncCalls = new ArrayDeque<>();
/*     */ 
/*     */   
/*  54 */   private final Deque<RealCall> runningSyncCalls = new ArrayDeque<>();
/*     */   
/*     */   public Dispatcher(ExecutorService executorService) {
/*  57 */     this.executorService = executorService;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dispatcher() {}
/*     */   
/*     */   public synchronized ExecutorService executorService() {
/*  64 */     if (this.executorService == null) {
/*  65 */       this
/*  66 */         .executorService = new ThreadPoolExecutor(0, 2147483647, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), Util.threadFactory("OkHttp Dispatcher", false));
/*     */     }
/*  68 */     return this.executorService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxRequests(int maxRequests) {
/*  79 */     if (maxRequests < 1) {
/*  80 */       throw new IllegalArgumentException("max < 1: " + maxRequests);
/*     */     }
/*  82 */     synchronized (this) {
/*  83 */       this.maxRequests = maxRequests;
/*     */     } 
/*  85 */     promoteAndExecute();
/*     */   }
/*     */   
/*     */   public synchronized int getMaxRequests() {
/*  89 */     return this.maxRequests;
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
/*     */   public void setMaxRequestsPerHost(int maxRequestsPerHost) {
/* 104 */     if (maxRequestsPerHost < 1) {
/* 105 */       throw new IllegalArgumentException("max < 1: " + maxRequestsPerHost);
/*     */     }
/* 107 */     synchronized (this) {
/* 108 */       this.maxRequestsPerHost = maxRequestsPerHost;
/*     */     } 
/* 110 */     promoteAndExecute();
/*     */   }
/*     */   
/*     */   public synchronized int getMaxRequestsPerHost() {
/* 114 */     return this.maxRequestsPerHost;
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
/*     */   public synchronized void setIdleCallback(@Nullable Runnable idleCallback) {
/* 130 */     this.idleCallback = idleCallback;
/*     */   }
/*     */   
/*     */   void enqueue(RealCall.AsyncCall call) {
/* 134 */     synchronized (this) {
/* 135 */       this.readyAsyncCalls.add(call);
/*     */ 
/*     */ 
/*     */       
/* 139 */       if (!(call.get()).forWebSocket) {
/* 140 */         RealCall.AsyncCall existingCall = findExistingCallWithHost(call.host());
/* 141 */         if (existingCall != null) call.reuseCallsPerHostFrom(existingCall); 
/*     */       } 
/*     */     } 
/* 144 */     promoteAndExecute();
/*     */   }
/*     */   @Nullable
/*     */   private RealCall.AsyncCall findExistingCallWithHost(String host) {
/* 148 */     for (RealCall.AsyncCall existingCall : this.runningAsyncCalls) {
/* 149 */       if (existingCall.host().equals(host)) return existingCall; 
/*     */     } 
/* 151 */     for (RealCall.AsyncCall existingCall : this.readyAsyncCalls) {
/* 152 */       if (existingCall.host().equals(host)) return existingCall; 
/*     */     } 
/* 154 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void cancelAll() {
/* 162 */     for (RealCall.AsyncCall call : this.readyAsyncCalls) {
/* 163 */       call.get().cancel();
/*     */     }
/*     */     
/* 166 */     for (RealCall.AsyncCall call : this.runningAsyncCalls) {
/* 167 */       call.get().cancel();
/*     */     }
/*     */     
/* 170 */     for (RealCall call : this.runningSyncCalls) {
/* 171 */       call.cancel();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean promoteAndExecute() {
/*     */     boolean isRunning;
/* 183 */     assert !Thread.holdsLock(this);
/*     */     
/* 185 */     List<RealCall.AsyncCall> executableCalls = new ArrayList<>();
/*     */     
/* 187 */     synchronized (this) {
/* 188 */       for (Iterator<RealCall.AsyncCall> iterator = this.readyAsyncCalls.iterator(); iterator.hasNext(); ) {
/* 189 */         RealCall.AsyncCall asyncCall = iterator.next();
/*     */         
/* 191 */         if (this.runningAsyncCalls.size() >= this.maxRequests)
/* 192 */           break;  if (asyncCall.callsPerHost().get() >= this.maxRequestsPerHost)
/*     */           continue; 
/* 194 */         iterator.remove();
/* 195 */         asyncCall.callsPerHost().incrementAndGet();
/* 196 */         executableCalls.add(asyncCall);
/* 197 */         this.runningAsyncCalls.add(asyncCall);
/*     */       } 
/* 199 */       isRunning = (runningCallsCount() > 0);
/*     */     } 
/*     */     
/* 202 */     for (int i = 0, size = executableCalls.size(); i < size; i++) {
/* 203 */       RealCall.AsyncCall asyncCall = executableCalls.get(i);
/* 204 */       asyncCall.executeOn(executorService());
/*     */     } 
/*     */     
/* 207 */     return isRunning;
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized void executed(RealCall call) {
/* 212 */     this.runningSyncCalls.add(call);
/*     */   }
/*     */ 
/*     */   
/*     */   void finished(RealCall.AsyncCall call) {
/* 217 */     call.callsPerHost().decrementAndGet();
/* 218 */     finished(this.runningAsyncCalls, call);
/*     */   }
/*     */ 
/*     */   
/*     */   void finished(RealCall call) {
/* 223 */     finished(this.runningSyncCalls, call);
/*     */   }
/*     */   
/*     */   private <T> void finished(Deque<T> calls, T call) {
/*     */     Runnable idleCallback;
/* 228 */     synchronized (this) {
/* 229 */       if (!calls.remove(call)) throw new AssertionError("Call wasn't in-flight!"); 
/* 230 */       idleCallback = this.idleCallback;
/*     */     } 
/*     */     
/* 233 */     boolean isRunning = promoteAndExecute();
/*     */     
/* 235 */     if (!isRunning && idleCallback != null) {
/* 236 */       idleCallback.run();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized List<Call> queuedCalls() {
/* 242 */     List<Call> result = new ArrayList<>();
/* 243 */     for (RealCall.AsyncCall asyncCall : this.readyAsyncCalls) {
/* 244 */       result.add(asyncCall.get());
/*     */     }
/* 246 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized List<Call> runningCalls() {
/* 251 */     List<Call> result = new ArrayList<>();
/* 252 */     result.addAll((Collection)this.runningSyncCalls);
/* 253 */     for (RealCall.AsyncCall asyncCall : this.runningAsyncCalls) {
/* 254 */       result.add(asyncCall.get());
/*     */     }
/* 256 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */   
/*     */   public synchronized int queuedCallsCount() {
/* 260 */     return this.readyAsyncCalls.size();
/*     */   }
/*     */   
/*     */   public synchronized int runningCallsCount() {
/* 264 */     return this.runningAsyncCalls.size() + this.runningSyncCalls.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\Dispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */