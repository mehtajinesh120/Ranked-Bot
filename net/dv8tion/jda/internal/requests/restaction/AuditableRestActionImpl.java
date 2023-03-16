/*     */ package net.dv8tion.jda.internal.requests.restaction;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.audit.ThreadLocalReason;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.utils.EncodingUtil;
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
/*     */ public class AuditableRestActionImpl<T>
/*     */   extends RestActionImpl<T>
/*     */   implements AuditableRestAction<T>
/*     */ {
/*  40 */   protected String reason = null;
/*     */ 
/*     */   
/*     */   public AuditableRestActionImpl(JDA api, Route.CompiledRoute route) {
/*  44 */     super(api, route);
/*     */   }
/*     */ 
/*     */   
/*     */   public AuditableRestActionImpl(JDA api, Route.CompiledRoute route, RequestBody data) {
/*  49 */     super(api, route, data);
/*     */   }
/*     */ 
/*     */   
/*     */   public AuditableRestActionImpl(JDA api, Route.CompiledRoute route, DataObject data) {
/*  54 */     super(api, route, data);
/*     */   }
/*     */ 
/*     */   
/*     */   public AuditableRestActionImpl(JDA api, Route.CompiledRoute route, BiFunction<Response, Request<T>, T> handler) {
/*  59 */     super(api, route, handler);
/*     */   }
/*     */ 
/*     */   
/*     */   public AuditableRestActionImpl(JDA api, Route.CompiledRoute route, DataObject data, BiFunction<Response, Request<T>, T> handler) {
/*  64 */     super(api, route, data, handler);
/*     */   }
/*     */ 
/*     */   
/*     */   public AuditableRestActionImpl(JDA api, Route.CompiledRoute route, RequestBody data, BiFunction<Response, Request<T>, T> handler) {
/*  69 */     super(api, route, data, handler);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<T> setCheck(BooleanSupplier checks) {
/*  76 */     return (AuditableRestAction<T>)super.setCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<T> timeout(long timeout, @Nonnull TimeUnit unit) {
/*  83 */     return (AuditableRestAction<T>)super.timeout(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<T> deadline(long timestamp) {
/*  90 */     return (AuditableRestAction<T>)super.deadline(timestamp);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public AuditableRestActionImpl<T> reason(@Nullable String reason) {
/*  97 */     this.reason = reason;
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected CaseInsensitiveMap<String, String> finalizeHeaders() {
/* 104 */     CaseInsensitiveMap<String, String> headers = super.finalizeHeaders();
/*     */     
/* 106 */     if (this.reason == null || this.reason.isEmpty()) {
/*     */       
/* 108 */       String localReason = ThreadLocalReason.getCurrent();
/* 109 */       if (localReason == null || localReason.isEmpty()) {
/* 110 */         return headers;
/*     */       }
/* 112 */       return generateHeaders(headers, localReason);
/*     */     } 
/*     */     
/* 115 */     return generateHeaders(headers, this.reason);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   private CaseInsensitiveMap<String, String> generateHeaders(CaseInsensitiveMap<String, String> headers, String reason) {
/* 121 */     if (headers == null) {
/* 122 */       headers = new CaseInsensitiveMap();
/*     */     }
/* 124 */     headers.put("X-Audit-Log-Reason", uriEncode(reason));
/* 125 */     return headers;
/*     */   }
/*     */ 
/*     */   
/*     */   private String uriEncode(String input) {
/* 130 */     String formEncode = EncodingUtil.encodeUTF8(input);
/* 131 */     return formEncode.replace('+', ' ');
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\AuditableRestActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */