/*    */ package okhttp3.internal.connection;
/*    */ 
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.Set;
/*    */ import okhttp3.Route;
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
/*    */ 
/*    */ public final class RouteDatabase
/*    */ {
/* 29 */   private final Set<Route> failedRoutes = new LinkedHashSet<>();
/*    */ 
/*    */   
/*    */   public synchronized void failed(Route failedRoute) {
/* 33 */     this.failedRoutes.add(failedRoute);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void connected(Route route) {
/* 38 */     this.failedRoutes.remove(route);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized boolean shouldPostpone(Route route) {
/* 43 */     return this.failedRoutes.contains(route);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\connection\RouteDatabase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */