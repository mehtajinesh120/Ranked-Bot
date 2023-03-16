/*    */ package net.dv8tion.jda.api.exceptions;
/*    */ 
/*    */ import net.dv8tion.jda.internal.requests.Route;
/*    */ import net.dv8tion.jda.internal.utils.Helpers;
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
/*    */ 
/*    */ public class RateLimitedException
/*    */   extends Exception
/*    */ {
/*    */   private final String rateLimitedRoute;
/*    */   private final long retryAfter;
/*    */   
/*    */   public RateLimitedException(Route.CompiledRoute route, long retryAfter) {
/* 32 */     this(route.getBaseRoute().getRoute() + ":" + route.getMajorParameters(), retryAfter);
/*    */   }
/*    */ 
/*    */   
/*    */   public RateLimitedException(String route, long retryAfter) {
/* 37 */     super(Helpers.format("The request was ratelimited! Retry-After: %d  Route: %s", new Object[] { Long.valueOf(retryAfter), route }));
/* 38 */     this.rateLimitedRoute = route;
/* 39 */     this.retryAfter = retryAfter;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getRateLimitedRoute() {
/* 50 */     return this.rateLimitedRoute;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getRetryAfter() {
/* 61 */     return this.retryAfter;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\exceptions\RateLimitedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */