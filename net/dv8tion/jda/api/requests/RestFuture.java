/*    */ package net.dv8tion.jda.api.requests;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.BooleanSupplier;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*    */ import net.dv8tion.jda.internal.requests.Route;
/*    */ import okhttp3.RequestBody;
/*    */ import org.apache.commons.collections4.map.CaseInsensitiveMap;
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
/*    */ public class RestFuture<T>
/*    */   extends CompletableFuture<T>
/*    */ {
/*    */   final Request<T> request;
/*    */   
/*    */   public RestFuture(RestActionImpl<T> restAction, boolean shouldQueue, BooleanSupplier checks, RequestBody data, Object rawData, long deadline, boolean priority, Route.CompiledRoute route, CaseInsensitiveMap<String, String> headers) {
/* 36 */     this.request = new Request<>(restAction, this::complete, this::completeExceptionally, checks, shouldQueue, data, rawData, deadline, priority, route, headers);
/*    */     
/* 38 */     ((JDAImpl)restAction.getJDA()).getRequester().request(this.request);
/*    */   }
/*    */ 
/*    */   
/*    */   public RestFuture(T t) {
/* 43 */     complete(t);
/* 44 */     this.request = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public RestFuture(Throwable t) {
/* 49 */     completeExceptionally(t);
/* 50 */     this.request = null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean cancel(boolean mayInterrupt) {
/* 56 */     if (this.request != null) {
/* 57 */       this.request.cancel();
/*    */     }
/* 59 */     return (!isDone() && !isCancelled() && super.cancel(mayInterrupt));
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\RestFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */