/*     */ package net.dv8tion.jda.internal.requests.restaction.interactions;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.exceptions.InteractionFailureException;
/*     */ import net.dv8tion.jda.api.interactions.InteractionHook;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.interactions.InteractionCallbackAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.interactions.InteractionHookImpl;
/*     */ import net.dv8tion.jda.internal.requests.Requester;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.utils.IOUtil;
/*     */ import okhttp3.MultipartBody;
/*     */ import okhttp3.RequestBody;
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
/*     */ public abstract class InteractionCallbackActionImpl
/*     */   extends RestActionImpl<InteractionHook>
/*     */   implements InteractionCallbackAction
/*     */ {
/*     */   protected final InteractionHookImpl hook;
/*  44 */   protected final Map<String, InputStream> files = new HashMap<>();
/*     */ 
/*     */   
/*     */   public InteractionCallbackActionImpl(InteractionHookImpl hook) {
/*  48 */     super(hook.getJDA(), Route.Interactions.CALLBACK.compile(new String[] { hook.getInteraction().getId(), hook.getInteraction().getToken() }));
/*  49 */     this.hook = hook;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract DataObject toData();
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/*  57 */     DataObject json = toData();
/*  58 */     if (this.files.isEmpty()) {
/*  59 */       return getRequestBody(json);
/*     */     }
/*  61 */     MultipartBody.Builder body = (new MultipartBody.Builder()).setType(MultipartBody.FORM);
/*  62 */     int i = 0;
/*  63 */     for (Map.Entry<String, InputStream> file : this.files.entrySet()) {
/*     */       
/*  65 */       RequestBody stream = IOUtil.createRequestBody(Requester.MEDIA_TYPE_OCTET, file.getValue());
/*  66 */       body.addFormDataPart("file" + i++, file.getKey(), stream);
/*     */     } 
/*  68 */     body.addFormDataPart("payload_json", json.toString());
/*  69 */     this.files.clear();
/*  70 */     return (RequestBody)body.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSuccess(Response response, Request<InteractionHook> request) {
/*  76 */     this.hook.ready();
/*  77 */     request.onSuccess(this.hook);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleResponse(Response response, Request<InteractionHook> request) {
/*  83 */     if (!response.isOk())
/*  84 */       this.hook.fail((Exception)new InteractionFailureException()); 
/*  85 */     super.handleResponse(response, request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private IllegalStateException tryAck() {
/*  96 */     return this.hook.ack() ? new IllegalStateException("This interaction has already been acknowledged or replied to. You can only reply or acknowledge an interaction (or slash command) once!") : 
/*  97 */       null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void queue(Consumer<? super InteractionHook> success, Consumer<? super Throwable> failure) {
/* 103 */     IllegalStateException exception = tryAck();
/* 104 */     if (exception != null) {
/*     */       
/* 106 */       if (failure != null) {
/* 107 */         failure.accept(exception);
/*     */       } else {
/* 109 */         RestAction.getDefaultFailure().accept(exception);
/*     */       } 
/*     */       return;
/*     */     } 
/* 113 */     super.queue(success, failure);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CompletableFuture<InteractionHook> submit(boolean shouldQueue) {
/* 120 */     IllegalStateException exception = tryAck();
/* 121 */     if (exception != null) {
/*     */       
/* 123 */       CompletableFuture<InteractionHook> future = new CompletableFuture<>();
/* 124 */       future.completeExceptionally(exception);
/* 125 */       return future;
/*     */     } 
/*     */     
/* 128 */     return super.submit(shouldQueue);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\interactions\InteractionCallbackActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */