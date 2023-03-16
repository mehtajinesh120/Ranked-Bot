/*     */ package net.dv8tion.jda.internal.requests;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.exceptions.ErrorResponseException;
/*     */ import net.dv8tion.jda.api.exceptions.RateLimitedException;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.RestFuture;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.JDALogger;
/*     */ import okhttp3.RequestBody;
/*     */ import org.apache.commons.collections4.map.CaseInsensitiveMap;
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
/*     */ public class RestActionImpl<T>
/*     */   implements RestAction<T>
/*     */ {
/*     */   private static Consumer<Object> DEFAULT_SUCCESS = o -> {
/*     */     
/*     */     };
/*  44 */   public static final Logger LOG = JDALogger.getLog(RestAction.class); private static Consumer<? super Throwable> DEFAULT_FAILURE;
/*     */   
/*     */   static {
/*  47 */     DEFAULT_FAILURE = (t -> {
/*     */         if (t instanceof java.util.concurrent.CancellationException || t instanceof java.util.concurrent.TimeoutException) {
/*     */           LOG.debug(t.getMessage());
/*     */         } else if (LOG.isDebugEnabled() || !(t instanceof ErrorResponseException)) {
/*     */           LOG.error("RestAction queue returned failure", t);
/*     */         } else if (t.getCause() != null) {
/*     */           LOG.error("RestAction queue returned failure: [{}] {}", new Object[] { t.getClass().getSimpleName(), t.getMessage(), t.getCause() });
/*     */         } else {
/*     */           LOG.error("RestAction queue returned failure: [{}] {}", t.getClass().getSimpleName(), t.getMessage());
/*     */         } 
/*     */       });
/*     */   }
/*     */   protected static boolean passContext = true;
/*  60 */   protected static long defaultTimeout = 0L;
/*     */   
/*     */   protected final JDAImpl api;
/*     */   
/*     */   private final Route.CompiledRoute route;
/*     */   
/*     */   private final RequestBody data;
/*     */   private final BiFunction<Response, Request<T>, T> handler;
/*     */   private boolean priority = false;
/*  69 */   private long deadline = 0L;
/*     */   
/*     */   private Object rawData;
/*     */   private BooleanSupplier checks;
/*     */   
/*     */   public static void setPassContext(boolean enable) {
/*  75 */     passContext = enable;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isPassContext() {
/*  80 */     return passContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setDefaultFailure(Consumer<? super Throwable> callback) {
/*  85 */     DEFAULT_FAILURE = (callback == null) ? (t -> {  }) : callback;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setDefaultSuccess(Consumer<Object> callback) {
/*  90 */     DEFAULT_SUCCESS = (callback == null) ? (t -> {  }) : callback;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setDefaultTimeout(long timeout, @Nonnull TimeUnit unit) {
/*  95 */     Checks.notNull(unit, "TimeUnit");
/*  96 */     defaultTimeout = unit.toMillis(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getDefaultTimeout() {
/* 101 */     return defaultTimeout;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Consumer<? super Throwable> getDefaultFailure() {
/* 106 */     return DEFAULT_FAILURE;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Consumer<Object> getDefaultSuccess() {
/* 111 */     return DEFAULT_SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   public RestActionImpl(JDA api, Route.CompiledRoute route) {
/* 116 */     this(api, route, (RequestBody)null, (BiFunction<Response, Request<T>, T>)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public RestActionImpl(JDA api, Route.CompiledRoute route, DataObject data) {
/* 121 */     this(api, route, data, (BiFunction<Response, Request<T>, T>)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public RestActionImpl(JDA api, Route.CompiledRoute route, RequestBody data) {
/* 126 */     this(api, route, data, (BiFunction<Response, Request<T>, T>)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public RestActionImpl(JDA api, Route.CompiledRoute route, BiFunction<Response, Request<T>, T> handler) {
/* 131 */     this(api, route, (RequestBody)null, handler);
/*     */   }
/*     */ 
/*     */   
/*     */   public RestActionImpl(JDA api, Route.CompiledRoute route, DataObject data, BiFunction<Response, Request<T>, T> handler) {
/* 136 */     this(api, route, (data == null) ? null : RequestBody.create(Requester.MEDIA_TYPE_JSON, data.toJson()), handler);
/* 137 */     this.rawData = data;
/*     */   }
/*     */ 
/*     */   
/*     */   public RestActionImpl(JDA api, Route.CompiledRoute route, RequestBody data, BiFunction<Response, Request<T>, T> handler) {
/* 142 */     Checks.notNull(api, "api");
/* 143 */     this.api = (JDAImpl)api;
/* 144 */     this.route = route;
/* 145 */     this.data = data;
/* 146 */     this.handler = handler;
/*     */   }
/*     */ 
/*     */   
/*     */   public RestActionImpl<T> priority() {
/* 151 */     this.priority = true;
/* 152 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/* 159 */     return (JDA)this.api;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<T> setCheck(BooleanSupplier checks) {
/* 166 */     this.checks = checks;
/* 167 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BooleanSupplier getCheck() {
/* 174 */     return this.checks;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<T> deadline(long timestamp) {
/* 181 */     this.deadline = timestamp;
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void queue(Consumer<? super T> success, Consumer<? super Throwable> failure) {
/*     */     Consumer<Object> consumer;
/* 188 */     Route.CompiledRoute route = finalizeRoute();
/* 189 */     Checks.notNull(route, "Route");
/* 190 */     RequestBody data = finalizeData();
/* 191 */     CaseInsensitiveMap<String, String> headers = finalizeHeaders();
/* 192 */     BooleanSupplier finisher = getFinisher();
/* 193 */     if (success == null)
/* 194 */       consumer = DEFAULT_SUCCESS; 
/* 195 */     if (failure == null)
/* 196 */       failure = DEFAULT_FAILURE; 
/* 197 */     this.api.getRequester().request(new Request(this, consumer, failure, finisher, true, data, this.rawData, getDeadline(), this.priority, route, headers));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CompletableFuture<T> submit(boolean shouldQueue) {
/* 204 */     Route.CompiledRoute route = finalizeRoute();
/* 205 */     Checks.notNull(route, "Route");
/* 206 */     RequestBody data = finalizeData();
/* 207 */     CaseInsensitiveMap<String, String> headers = finalizeHeaders();
/* 208 */     BooleanSupplier finisher = getFinisher();
/* 209 */     return (CompletableFuture<T>)new RestFuture(this, shouldQueue, finisher, data, this.rawData, getDeadline(), this.priority, route, headers);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public T complete(boolean shouldQueue) throws RateLimitedException {
/* 215 */     if (CallbackContext.isCallbackContext()) {
/* 216 */       throw new IllegalStateException("Preventing use of complete() in callback threads! This operation can be a deadlock cause");
/*     */     }
/*     */     try {
/* 219 */       return submit(shouldQueue).join();
/*     */     }
/* 221 */     catch (CompletionException e) {
/*     */       
/* 223 */       if (e.getCause() != null) {
/*     */         
/* 225 */         Throwable cause = e.getCause();
/* 226 */         if (cause instanceof ErrorResponseException)
/* 227 */           throw (ErrorResponseException)cause.fillInStackTrace(); 
/* 228 */         if (cause instanceof RateLimitedException)
/* 229 */           throw (RateLimitedException)cause.fillInStackTrace(); 
/* 230 */         if (cause instanceof RuntimeException)
/* 231 */           throw (RuntimeException)cause; 
/* 232 */         if (cause instanceof Error)
/* 233 */           throw (Error)cause; 
/*     */       } 
/* 235 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/* 239 */   protected RequestBody finalizeData() { return this.data; }
/* 240 */   protected Route.CompiledRoute finalizeRoute() { return this.route; }
/* 241 */   protected CaseInsensitiveMap<String, String> finalizeHeaders() { return null; } protected BooleanSupplier finalizeChecks() {
/* 242 */     return null;
/*     */   }
/*     */   
/*     */   protected RequestBody getRequestBody(DataObject object) {
/* 246 */     this.rawData = object;
/*     */     
/* 248 */     return (object == null) ? null : RequestBody.create(Requester.MEDIA_TYPE_JSON, object.toJson());
/*     */   }
/*     */ 
/*     */   
/*     */   protected RequestBody getRequestBody(DataArray array) {
/* 253 */     this.rawData = array;
/*     */     
/* 255 */     return (array == null) ? null : RequestBody.create(Requester.MEDIA_TYPE_JSON, array.toJson());
/*     */   }
/*     */ 
/*     */   
/*     */   private CheckWrapper getFinisher() {
/* 260 */     BooleanSupplier pre = finalizeChecks();
/* 261 */     BooleanSupplier wrapped = this.checks;
/* 262 */     return (pre != null || wrapped != null) ? new CheckWrapper(wrapped, pre) : CheckWrapper.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleResponse(Response response, Request<T> request) {
/* 267 */     if (response.isOk()) {
/* 268 */       handleSuccess(response, request);
/*     */     } else {
/* 270 */       request.onFailure(response);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void handleSuccess(Response response, Request<T> request) {
/* 275 */     if (this.handler == null) {
/* 276 */       request.onSuccess(null);
/*     */     } else {
/* 278 */       request.onSuccess(this.handler.apply(response, request));
/*     */     } 
/*     */   }
/*     */   
/*     */   private long getDeadline() {
/* 283 */     return (this.deadline > 0L) ? 
/* 284 */       this.deadline : (
/* 285 */       (defaultTimeout > 0L) ? (
/* 286 */       System.currentTimeMillis() + defaultTimeout) : 
/* 287 */       0L);
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
/*     */   protected static class CheckWrapper
/*     */     implements BooleanSupplier
/*     */   {
/* 302 */     public static final CheckWrapper EMPTY = new CheckWrapper(null, null) {
/*     */         public boolean getAsBoolean() {
/* 304 */           return true;
/*     */         }
/*     */       };
/*     */     
/*     */     protected final BooleanSupplier pre;
/*     */     protected final BooleanSupplier wrapped;
/*     */     
/*     */     public CheckWrapper(BooleanSupplier wrapped, BooleanSupplier pre) {
/* 312 */       this.pre = pre;
/* 313 */       this.wrapped = wrapped;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean pre() {
/* 318 */       return (this.pre == null || this.pre.getAsBoolean());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test() {
/* 323 */       return (this.wrapped == null || this.wrapped.getAsBoolean());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean getAsBoolean() {
/* 329 */       return (pre() && test());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\RestActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */