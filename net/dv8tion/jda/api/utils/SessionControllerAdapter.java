/*     */ package net.dv8tion.jda.api.utils;
/*     */ 
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.security.auth.login.LoginException;
/*     */ import net.dv8tion.jda.api.AccountType;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.exceptions.AccountTypeException;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.utils.JDALogger;
/*     */ import net.dv8tion.jda.internal.utils.tuple.Pair;
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
/*     */ public class SessionControllerAdapter
/*     */   implements SessionController
/*     */ {
/*  41 */   protected static final Logger log = JDALogger.getLog(SessionControllerAdapter.class);
/*  42 */   protected final Object lock = new Object();
/*     */   protected Queue<SessionController.SessionConnectNode> connectQueue;
/*     */   protected AtomicLong globalRatelimit;
/*     */   protected Thread workerHandle;
/*  46 */   protected long lastConnect = 0L;
/*     */ 
/*     */   
/*     */   public SessionControllerAdapter() {
/*  50 */     this.connectQueue = new ConcurrentLinkedQueue<>();
/*  51 */     this.globalRatelimit = new AtomicLong(Long.MIN_VALUE);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendSession(@Nonnull SessionController.SessionConnectNode node) {
/*  57 */     removeSession(node);
/*  58 */     this.connectQueue.add(node);
/*  59 */     runWorker();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeSession(@Nonnull SessionController.SessionConnectNode node) {
/*  65 */     this.connectQueue.remove(node);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getGlobalRatelimit() {
/*  71 */     return this.globalRatelimit.get();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGlobalRatelimit(long ratelimit) {
/*  77 */     this.globalRatelimit.set(ratelimit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getGateway(@Nonnull JDA api) {
/*  84 */     Route.CompiledRoute route = Route.Misc.GATEWAY.compile(new String[0]);
/*  85 */     return (String)(new RestActionImpl(api, route, (response, request) -> response.getObject().getString("url")))
/*  86 */       .priority().complete();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public SessionController.ShardedGateway getShardedGateway(@Nonnull JDA api) {
/*  93 */     AccountTypeException.check(api.getAccountType(), AccountType.BOT);
/*  94 */     return (SessionController.ShardedGateway)(new RestActionImpl<SessionController.ShardedGateway>(api, Route.Misc.GATEWAY_BOT.compile(new String[0]))
/*     */       {
/*     */         
/*     */         public void handleResponse(Response response, Request<SessionController.ShardedGateway> request)
/*     */         {
/*  99 */           if (response.isOk()) {
/*     */             
/* 101 */             DataObject object = response.getObject();
/*     */             
/* 103 */             String url = object.getString("url");
/* 104 */             int shards = object.getInt("shards");
/* 105 */             int concurrency = object.getObject("session_start_limit").getInt("max_concurrency", 1);
/*     */             
/* 107 */             request.onSuccess(new SessionController.ShardedGateway(url, shards, concurrency));
/*     */           }
/* 109 */           else if (response.code == 401) {
/*     */             
/* 111 */             this.api.shutdownNow();
/* 112 */             request.onFailure(new LoginException("The provided token is invalid!"));
/*     */           }
/*     */           else {
/*     */             
/* 116 */             request.onFailure(response);
/*     */           } 
/*     */         }
/* 119 */       }).priority().complete();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Pair<String, Integer> getGatewayBot(@Nonnull JDA api) {
/* 127 */     SessionController.ShardedGateway bot = getShardedGateway(api);
/* 128 */     return Pair.of(bot.getUrl(), Integer.valueOf(bot.getShardTotal()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void runWorker() {
/* 133 */     synchronized (this.lock) {
/*     */       
/* 135 */       if (this.workerHandle == null) {
/*     */         
/* 137 */         this.workerHandle = new QueueWorker();
/* 138 */         this.workerHandle.start();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected class QueueWorker
/*     */     extends Thread
/*     */   {
/*     */     protected final long delay;
/*     */     
/*     */     public QueueWorker() {
/* 150 */       this(5);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public QueueWorker(int delay) {
/* 161 */       this(TimeUnit.SECONDS.toMillis(delay));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public QueueWorker(long delay) {
/* 172 */       super("SessionControllerAdapter-Worker");
/* 173 */       this.delay = delay;
/* 174 */       setUncaughtExceptionHandler(this::handleFailure);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void handleFailure(Thread thread, Throwable exception) {
/* 179 */       SessionControllerAdapter.log.error("Worker has failed with throwable!", exception);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 187 */         if (this.delay > 0L) {
/*     */           
/* 189 */           long interval = System.currentTimeMillis() - SessionControllerAdapter.this.lastConnect;
/* 190 */           if (interval < this.delay) {
/* 191 */             Thread.sleep(this.delay - interval);
/*     */           }
/*     */         } 
/* 194 */       } catch (InterruptedException ex) {
/*     */         
/* 196 */         SessionControllerAdapter.log.error("Unable to backoff", ex);
/*     */       } 
/* 198 */       processQueue();
/* 199 */       synchronized (SessionControllerAdapter.this.lock) {
/*     */         
/* 201 */         SessionControllerAdapter.this.workerHandle = null;
/* 202 */         if (!SessionControllerAdapter.this.connectQueue.isEmpty()) {
/* 203 */           SessionControllerAdapter.this.runWorker();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void processQueue() {
/* 209 */       boolean isMultiple = (SessionControllerAdapter.this.connectQueue.size() > 1);
/* 210 */       while (!SessionControllerAdapter.this.connectQueue.isEmpty()) {
/*     */         
/* 212 */         SessionController.SessionConnectNode node = SessionControllerAdapter.this.connectQueue.poll();
/*     */         
/*     */         try {
/* 215 */           node.run((isMultiple && SessionControllerAdapter.this.connectQueue.isEmpty()));
/* 216 */           isMultiple = true;
/* 217 */           SessionControllerAdapter.this.lastConnect = System.currentTimeMillis();
/* 218 */           if (SessionControllerAdapter.this.connectQueue.isEmpty())
/*     */             break; 
/* 220 */           if (this.delay > 0L) {
/* 221 */             Thread.sleep(this.delay);
/*     */           }
/* 223 */         } catch (IllegalStateException e) {
/*     */           
/* 225 */           Throwable t = e.getCause();
/* 226 */           if (t instanceof com.neovisionaries.ws.client.OpeningHandshakeException) {
/* 227 */             SessionControllerAdapter.log.error("Failed opening handshake, appending to queue. Message: {}", e.getMessage());
/* 228 */           } else if (t != null && !JDA.Status.RECONNECT_QUEUED.name().equals(t.getMessage())) {
/* 229 */             SessionControllerAdapter.log.error("Failed to establish connection for a node, appending to queue", e);
/*     */           } else {
/* 231 */             SessionControllerAdapter.log.error("Unexpected exception when running connect node", e);
/* 232 */           }  SessionControllerAdapter.this.appendSession(node);
/*     */         }
/* 234 */         catch (InterruptedException e) {
/*     */           
/* 236 */           SessionControllerAdapter.log.error("Failed to run node", e);
/* 237 */           SessionControllerAdapter.this.appendSession(node);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\SessionControllerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */