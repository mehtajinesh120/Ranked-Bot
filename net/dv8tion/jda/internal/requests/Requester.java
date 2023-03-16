/*     */ package net.dv8tion.jda.internal.requests;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.JDAInfo;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.requests.ratelimit.BotRateLimiter;
/*     */ import net.dv8tion.jda.internal.utils.Helpers;
/*     */ import net.dv8tion.jda.internal.utils.JDALogger;
/*     */ import net.dv8tion.jda.internal.utils.config.AuthorizationConfig;
/*     */ import okhttp3.Call;
/*     */ import okhttp3.MediaType;
/*     */ import okhttp3.OkHttpClient;
/*     */ import okhttp3.Request;
/*     */ import okhttp3.RequestBody;
/*     */ import okhttp3.Response;
/*     */ import okhttp3.internal.http.HttpMethod;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.MDC;
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
/*     */ public class Requester
/*     */ {
/*  50 */   public static final Logger LOG = JDALogger.getLog(Requester.class);
/*  51 */   public static final String DISCORD_API_PREFIX = Helpers.format("https://discord.com/api/v%d/", new Object[] { Integer.valueOf(8) });
/*  52 */   public static final String USER_AGENT = "DiscordBot (https://github.com/DV8FromTheWorld/JDA, " + JDAInfo.VERSION + ")";
/*  53 */   public static final RequestBody EMPTY_BODY = RequestBody.create(null, new byte[0]);
/*  54 */   public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
/*  55 */   public static final MediaType MEDIA_TYPE_OCTET = MediaType.parse("application/octet-stream; charset=utf-8");
/*     */   
/*     */   protected final JDAImpl api;
/*     */   
/*     */   protected final AuthorizationConfig authConfig;
/*     */   
/*     */   private final RateLimiter rateLimiter;
/*     */   
/*     */   private final OkHttpClient httpClient;
/*     */   private boolean isContextReady = false;
/*  65 */   private ConcurrentMap<String, String> contextMap = null;
/*     */   
/*     */   private volatile boolean retryOnTimeout = false;
/*     */ 
/*     */   
/*     */   public Requester(JDA api) {
/*  71 */     this(api, ((JDAImpl)api).getAuthorizationConfig());
/*     */   }
/*     */ 
/*     */   
/*     */   public Requester(JDA api, AuthorizationConfig authConfig) {
/*  76 */     if (authConfig == null) {
/*  77 */       throw new NullPointerException("Provided config was null!");
/*     */     }
/*  79 */     this.authConfig = authConfig;
/*  80 */     this.api = (JDAImpl)api;
/*  81 */     this.rateLimiter = (RateLimiter)new BotRateLimiter(this);
/*  82 */     this.httpClient = this.api.getHttpClient();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContextReady(boolean ready) {
/*  87 */     this.isContextReady = ready;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContext() {
/*  92 */     if (!this.isContextReady)
/*     */       return; 
/*  94 */     if (this.contextMap == null)
/*  95 */       this.contextMap = this.api.getContextMap(); 
/*  96 */     this.contextMap.forEach(MDC::put);
/*     */   }
/*     */ 
/*     */   
/*     */   public JDAImpl getJDA() {
/* 101 */     return this.api;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> void request(Request<T> apiRequest) {
/* 106 */     if (this.rateLimiter.isStopped) {
/* 107 */       throw new RejectedExecutionException("The Requester has been stopped! No new requests can be requested!");
/*     */     }
/* 109 */     if (apiRequest.shouldQueue()) {
/* 110 */       this.rateLimiter.queueRequest(apiRequest);
/*     */     } else {
/* 112 */       execute(apiRequest, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isRetry(Throwable e) {
/* 117 */     return (e instanceof java.net.SocketException || e instanceof java.net.SocketTimeoutException || e instanceof javax.net.ssl.SSLPeerUnverifiedException);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long execute(Request<?> apiRequest) {
/* 124 */     return execute(apiRequest, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long execute(Request<?> apiRequest, boolean handleOnRateLimit) {
/* 141 */     return execute(apiRequest, false, handleOnRateLimit);
/*     */   }
/*     */ 
/*     */   
/*     */   public Long execute(Request<?> apiRequest, boolean retried, boolean handleOnRatelimit) {
/* 146 */     Route.CompiledRoute route = apiRequest.getRoute();
/* 147 */     Long retryAfter = this.rateLimiter.getRateLimit(route);
/* 148 */     if (retryAfter != null && retryAfter.longValue() > 0L) {
/*     */       
/* 150 */       if (handleOnRatelimit)
/* 151 */         apiRequest.handleResponse(new Response(retryAfter.longValue(), Collections.emptySet())); 
/* 152 */       return retryAfter;
/*     */     } 
/*     */     
/* 155 */     Request.Builder builder = new Request.Builder();
/*     */     
/* 157 */     String url = DISCORD_API_PREFIX + route.getCompiledRoute();
/* 158 */     builder.url(url);
/*     */     
/* 160 */     String method = apiRequest.getRoute().getMethod().toString();
/* 161 */     RequestBody body = apiRequest.getBody();
/*     */     
/* 163 */     if (body == null && HttpMethod.requiresRequestBody(method)) {
/* 164 */       body = EMPTY_BODY;
/*     */     }
/* 166 */     builder.method(method, body)
/* 167 */       .header("X-RateLimit-Precision", "millisecond")
/* 168 */       .header("user-agent", USER_AGENT)
/* 169 */       .header("accept-encoding", "gzip");
/*     */ 
/*     */ 
/*     */     
/* 173 */     if (url.startsWith(DISCORD_API_PREFIX)) {
/* 174 */       builder.header("authorization", this.api.getToken());
/*     */     }
/*     */ 
/*     */     
/* 178 */     if (apiRequest.getHeaders() != null)
/*     */     {
/* 180 */       for (Map.Entry<String, String> header : (Iterable<Map.Entry<String, String>>)apiRequest.getHeaders().entrySet()) {
/* 181 */         builder.addHeader(header.getKey(), header.getValue());
/*     */       }
/*     */     }
/* 184 */     Request request = builder.build();
/*     */     
/* 186 */     Set<String> rays = new LinkedHashSet<>();
/* 187 */     Response[] responses = new Response[4];
/*     */ 
/*     */     
/* 190 */     Response lastResponse = null;
/*     */     
/*     */     try {
/* 193 */       LOG.trace("Executing request {} {}", apiRequest.getRoute().getMethod(), url);
/* 194 */       int attempt = 0;
/*     */       
/*     */       do {
/* 197 */         if (apiRequest.isSkipped()) {
/* 198 */           return null;
/*     */         }
/* 200 */         Call call = this.httpClient.newCall(request);
/* 201 */         lastResponse = call.execute();
/* 202 */         responses[attempt] = lastResponse;
/* 203 */         String cfRay = lastResponse.header("CF-RAY");
/* 204 */         if (cfRay != null) {
/* 205 */           rays.add(cfRay);
/*     */         }
/* 207 */         if (lastResponse.code() < 500) {
/*     */           break;
/*     */         }
/* 210 */         attempt++;
/* 211 */         LOG.debug("Requesting {} -> {} returned status {}... retrying (attempt {})", new Object[] { apiRequest
/* 212 */               .getRoute().getMethod(), url, 
/* 213 */               Integer.valueOf(lastResponse.code()), Integer.valueOf(attempt) });
/*     */         
/*     */         try {
/* 216 */           Thread.sleep((50 * attempt));
/*     */         }
/* 218 */         catch (InterruptedException interruptedException) {}
/*     */       }
/* 220 */       while (attempt < 3 && lastResponse.code() >= 500);
/*     */       
/* 222 */       LOG.trace("Finished Request {} {} with code {}", new Object[] { route.getMethod(), lastResponse.request().url(), Integer.valueOf(lastResponse.code()) });
/*     */       
/* 224 */       if (lastResponse.code() >= 500) {
/*     */ 
/*     */         
/* 227 */         Response response = new Response(lastResponse, -1L, rays);
/* 228 */         apiRequest.handleResponse(response);
/* 229 */         return null;
/*     */       } 
/*     */       
/* 232 */       retryAfter = this.rateLimiter.handleResponse(route, lastResponse);
/* 233 */       if (!rays.isEmpty()) {
/* 234 */         LOG.debug("Received response with following cf-rays: {}", rays);
/*     */       }
/* 236 */       if (retryAfter == null) {
/* 237 */         apiRequest.handleResponse(new Response(lastResponse, -1L, rays));
/* 238 */       } else if (handleOnRatelimit) {
/* 239 */         apiRequest.handleResponse(new Response(lastResponse, retryAfter.longValue(), rays));
/*     */       } 
/* 241 */       return retryAfter;
/*     */     }
/* 243 */     catch (UnknownHostException e) {
/*     */       
/* 245 */       LOG.error("DNS resolution failed: {}", e.getMessage());
/* 246 */       apiRequest.handleResponse(new Response(e, rays));
/* 247 */       return null;
/*     */     }
/* 249 */     catch (IOException e) {
/*     */       
/* 251 */       if (this.retryOnTimeout && !retried && isRetry(e))
/* 252 */         return execute(apiRequest, true, handleOnRatelimit); 
/* 253 */       LOG.error("There was an I/O error while executing a REST request: {}", e.getMessage());
/* 254 */       apiRequest.handleResponse(new Response(e, rays));
/* 255 */       return null;
/*     */     }
/* 257 */     catch (Exception e) {
/*     */       
/* 259 */       LOG.error("There was an unexpected error while executing a REST request", e);
/* 260 */       apiRequest.handleResponse(new Response(e, rays));
/* 261 */       return null;
/*     */     }
/*     */     finally {
/*     */       
/* 265 */       for (Response r : responses) {
/*     */         
/* 267 */         if (r == null)
/*     */           break; 
/* 269 */         r.close();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void applyBody(Request<?> apiRequest, Request.Builder builder) {
/* 276 */     String method = apiRequest.getRoute().getMethod().toString();
/* 277 */     RequestBody body = apiRequest.getBody();
/*     */     
/* 279 */     if (body == null && HttpMethod.requiresRequestBody(method)) {
/* 280 */       body = EMPTY_BODY;
/*     */     }
/* 282 */     builder.method(method, body);
/*     */   }
/*     */ 
/*     */   
/*     */   private void applyHeaders(Request<?> apiRequest, Request.Builder builder, boolean authorized) {
/* 287 */     builder.header("user-agent", USER_AGENT)
/* 288 */       .header("accept-encoding", "gzip")
/* 289 */       .header("x-ratelimit-precision", "millisecond");
/*     */ 
/*     */ 
/*     */     
/* 293 */     if (authorized) {
/* 294 */       builder.header("authorization", this.authConfig.getToken());
/*     */     }
/*     */ 
/*     */     
/* 298 */     if (apiRequest.getHeaders() != null)
/*     */     {
/* 300 */       for (Map.Entry<String, String> header : (Iterable<Map.Entry<String, String>>)apiRequest.getHeaders().entrySet()) {
/* 301 */         builder.addHeader(header.getKey(), header.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public OkHttpClient getHttpClient() {
/* 307 */     return this.httpClient;
/*     */   }
/*     */ 
/*     */   
/*     */   public RateLimiter getRateLimiter() {
/* 312 */     return this.rateLimiter;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRetryOnTimeout(boolean retryOnTimeout) {
/* 317 */     this.retryOnTimeout = retryOnTimeout;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop() {
/* 322 */     return this.rateLimiter.stop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 327 */     this.rateLimiter.shutdown();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\Requester.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */