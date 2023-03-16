/*     */ package net.dv8tion.jda.api.requests;
/*     */ 
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.audit.ThreadLocalReason;
/*     */ import net.dv8tion.jda.api.events.ExceptionEvent;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.events.http.HttpRequestEvent;
/*     */ import net.dv8tion.jda.api.exceptions.ContextException;
/*     */ import net.dv8tion.jda.api.exceptions.ErrorResponseException;
/*     */ import net.dv8tion.jda.api.exceptions.RateLimitedException;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.requests.CallbackContext;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import okhttp3.RequestBody;
/*     */ import org.apache.commons.collections4.map.CaseInsensitiveMap;
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
/*     */ public class Request<T>
/*     */ {
/*     */   private final JDAImpl api;
/*     */   private final RestActionImpl<T> restAction;
/*     */   private final Consumer<? super T> onSuccess;
/*     */   private final Consumer<? super Throwable> onFailure;
/*     */   private final BooleanSupplier checks;
/*     */   private final boolean shouldQueue;
/*     */   private final Route.CompiledRoute route;
/*     */   private final RequestBody body;
/*     */   private final Object rawBody;
/*     */   private final CaseInsensitiveMap<String, String> headers;
/*     */   private final long deadline;
/*     */   private final boolean priority;
/*     */   private final String localReason;
/*     */   private boolean done = false;
/*     */   private boolean isCancelled = false;
/*     */   
/*     */   public Request(RestActionImpl<T> restAction, Consumer<? super T> onSuccess, Consumer<? super Throwable> onFailure, BooleanSupplier checks, boolean shouldQueue, RequestBody body, Object rawBody, long deadline, boolean priority, Route.CompiledRoute route, CaseInsensitiveMap<String, String> headers) {
/*  64 */     this.deadline = deadline;
/*  65 */     this.priority = priority;
/*  66 */     this.restAction = restAction;
/*  67 */     this.onSuccess = onSuccess;
/*  68 */     if (onFailure instanceof ContextException.ContextConsumer) {
/*  69 */       this.onFailure = onFailure;
/*  70 */     } else if (RestActionImpl.isPassContext()) {
/*  71 */       this.onFailure = ContextException.here(onFailure);
/*     */     } else {
/*  73 */       this.onFailure = onFailure;
/*  74 */     }  this.checks = checks;
/*  75 */     this.shouldQueue = shouldQueue;
/*  76 */     this.body = body;
/*  77 */     this.rawBody = rawBody;
/*  78 */     this.route = route;
/*  79 */     this.headers = headers;
/*     */     
/*  81 */     this.api = (JDAImpl)restAction.getJDA();
/*  82 */     this.localReason = ThreadLocalReason.getCurrent();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSuccess(T successObj) {
/*  87 */     if (this.done)
/*     */       return; 
/*  89 */     this.done = true;
/*  90 */     this.api.getCallbackPool().execute(() -> { try { ThreadLocalReason.Closable __ = ThreadLocalReason.closable(this.localReason); try { CallbackContext ___ = CallbackContext.getInstance(); try { this.onSuccess.accept((T)successObj); if (___ != null)
/*     */                   ___.close();  }
/*  92 */               catch (Throwable throwable) { if (___ != null) try { ___.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (__ != null) __.close();  } catch (Throwable throwable) { if (__ != null) try { __.close(); } catch (Throwable throwable1)
/*     */                 { throwable.addSuppressed(throwable1); }
/*     */                  
/*     */               throw throwable; }
/*     */              }
/*  97 */           catch (Throwable t)
/*     */           { RestActionImpl.LOG.error("Encountered error while processing success consumer", t);
/*     */             if (t instanceof Error) {
/*     */               this.api.handleEvent((GenericEvent)new ExceptionEvent((JDA)this.api, t, true));
/*     */               throw (Error)t;
/*     */             }  }
/*     */         
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onFailure(Response response) {
/* 111 */     if (response.code == 429) {
/*     */       
/* 113 */       onFailure((Throwable)new RateLimitedException(this.route, response.retryAfter));
/*     */     }
/*     */     else {
/*     */       
/* 117 */       onFailure((Throwable)ErrorResponseException.create(
/* 118 */             ErrorResponse.fromJSON(response.optObject().orElse(null)), response));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onFailure(Throwable failException) {
/* 124 */     if (this.done)
/*     */       return; 
/* 126 */     this.done = true;
/* 127 */     this.api.getCallbackPool().execute(() -> { try { ThreadLocalReason.Closable __ = ThreadLocalReason.closable(this.localReason); try { CallbackContext ___ = CallbackContext.getInstance(); try { this.onFailure.accept(failException); if (failException instanceof Error)
/*     */                   this.api.handleEvent((GenericEvent)new ExceptionEvent((JDA)this.api, failException, false));  if (___ != null)
/* 129 */                   ___.close();  } catch (Throwable throwable) { if (___ != null) try { ___.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (__ != null) __.close();  } catch (Throwable throwable) { if (__ != null) try { __.close(); } catch (Throwable throwable1)
/*     */                 { throwable.addSuppressed(throwable1); }
/*     */               
/*     */ 
/*     */               
/*     */               throw throwable; }
/*     */              }
/* 136 */           catch (Throwable t)
/*     */           { RestActionImpl.LOG.error("Encountered error while processing failure consumer", t);
/*     */             if (t instanceof Error) {
/*     */               this.api.handleEvent((GenericEvent)new ExceptionEvent((JDA)this.api, t, true));
/*     */               throw (Error)t;
/*     */             }  }
/*     */         
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onCancelled() {
/* 150 */     onFailure(new CancellationException("RestAction has been cancelled"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTimeout() {
/* 155 */     onFailure(new TimeoutException("RestAction has timed out"));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDAImpl getJDA() {
/* 161 */     return this.api;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<T> getRestAction() {
/* 167 */     return (RestAction<T>)this.restAction;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Consumer<? super T> getOnSuccess() {
/* 173 */     return this.onSuccess;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Consumer<? super Throwable> getOnFailure() {
/* 179 */     return this.onFailure;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPriority() {
/* 184 */     return this.priority;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSkipped() {
/* 189 */     if (isTimeout()) {
/*     */       
/* 191 */       onTimeout();
/* 192 */       return true;
/*     */     } 
/* 194 */     boolean skip = runChecks();
/* 195 */     if (skip)
/* 196 */       onCancelled(); 
/* 197 */     return skip;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isTimeout() {
/* 202 */     return (this.deadline > 0L && this.deadline < System.currentTimeMillis());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean runChecks() {
/*     */     try {
/* 209 */       return (isCancelled() || (this.checks != null && !this.checks.getAsBoolean()));
/*     */     }
/* 211 */     catch (Exception e) {
/*     */       
/* 213 */       onFailure(e);
/* 214 */       return true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public CaseInsensitiveMap<String, String> getHeaders() {
/* 221 */     return this.headers;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Route.CompiledRoute getRoute() {
/* 227 */     return this.route;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public RequestBody getBody() {
/* 233 */     return this.body;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getRawBody() {
/* 239 */     return this.rawBody;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldQueue() {
/* 244 */     return this.shouldQueue;
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 249 */     this.isCancelled = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/* 254 */     return this.isCancelled;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleResponse(@Nonnull Response response) {
/* 259 */     this.restAction.handleResponse(response, this);
/* 260 */     this.api.handleEvent((GenericEvent)new HttpRequestEvent(this, response));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\Request.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */