/*     */ package net.dv8tion.jda.api.events.http;
/*     */ 
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.events.Event;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import okhttp3.Headers;
/*     */ import okhttp3.Request;
/*     */ import okhttp3.RequestBody;
/*     */ import okhttp3.Response;
/*     */ import okhttp3.ResponseBody;
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
/*     */ public class HttpRequestEvent
/*     */   extends Event
/*     */ {
/*     */   private final Request<?> request;
/*     */   private final Response response;
/*     */   
/*     */   public HttpRequestEvent(@Nonnull Request<?> request, @Nonnull Response response) {
/*  46 */     super((JDA)request.getJDA());
/*     */     
/*  48 */     this.request = request;
/*  49 */     this.response = response;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Request<?> getRequest() {
/*  55 */     return this.request;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public RequestBody getRequestBody() {
/*  61 */     return this.request.getBody();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getRequestBodyRaw() {
/*  67 */     return this.request.getRawBody();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Headers getRequestHeaders() {
/*  73 */     return (this.response.getRawResponse() == null) ? null : this.response.getRawResponse().request().headers();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Request getRequestRaw() {
/*  79 */     return (this.response.getRawResponse() == null) ? null : this.response.getRawResponse().request();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Response getResponse() {
/*  85 */     return this.response;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ResponseBody getResponseBody() {
/*  91 */     return (this.response.getRawResponse() == null) ? null : this.response.getRawResponse().body();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public DataArray getResponseBodyAsArray() {
/*  97 */     return this.response.getArray();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public DataObject getResponseBodyAsObject() {
/* 103 */     return this.response.getObject();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getResponseBodyAsString() {
/* 109 */     return this.response.getString();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Headers getResponseHeaders() {
/* 115 */     return (this.response.getRawResponse() == null) ? null : this.response.getRawResponse().headers();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Response getResponseRaw() {
/* 121 */     return this.response.getRawResponse();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Set<String> getCFRays() {
/* 127 */     return this.response.getCFRays();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<?> getRestAction() {
/* 133 */     return this.request.getRestAction();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Route.CompiledRoute getRoute() {
/* 139 */     return this.request.getRoute();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRateLimit() {
/* 144 */     return this.response.isRateLimit();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\http\HttpRequestEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */