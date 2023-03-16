/*    */ package net.dv8tion.jda.internal.requests;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import net.dv8tion.jda.api.requests.Request;
/*    */ import net.dv8tion.jda.internal.utils.JDALogger;
/*    */ import okhttp3.Response;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class RateLimiter
/*    */ {
/* 29 */   protected static final Logger log = JDALogger.getLog(RateLimiter.class);
/*    */   
/*    */   protected final Requester requester;
/*    */   protected volatile boolean isShutdown = false, isStopped = false;
/*    */   
/*    */   protected RateLimiter(Requester requester) {
/* 35 */     this.requester = requester;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isSkipped(Iterator<Request> it, Request request) {
/* 40 */     if (request.isSkipped()) {
/*    */       
/* 42 */       cancel(it, request);
/* 43 */       return true;
/*    */     } 
/* 45 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   private void cancel(Iterator<Request> it, Request request) {
/* 50 */     request.onCancelled();
/* 51 */     it.remove();
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract Long getRateLimit(Route.CompiledRoute paramCompiledRoute);
/*    */ 
/*    */   
/*    */   protected abstract void queueRequest(Request paramRequest);
/*    */ 
/*    */   
/*    */   protected abstract Long handleResponse(Route.CompiledRoute paramCompiledRoute, Response paramResponse);
/*    */   
/*    */   public boolean isRateLimited(Route.CompiledRoute route) {
/* 64 */     Long rateLimit = getRateLimit(route);
/* 65 */     return (rateLimit != null && rateLimit.longValue() > 0L);
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract int cancelRequests();
/*    */ 
/*    */   
/*    */   public void init() {}
/*    */   
/*    */   protected boolean stop() {
/* 75 */     this.isStopped = true;
/* 76 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void shutdown() {
/* 81 */     this.isShutdown = true;
/* 82 */     stop();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\RateLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */