/*     */ package net.dv8tion.jda.internal.requests.restaction;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Icon;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.Webhook;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.WebhookAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.entities.WebhookImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
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
/*     */ 
/*     */ public class WebhookActionImpl
/*     */   extends AuditableRestActionImpl<Webhook>
/*     */   implements WebhookAction
/*     */ {
/*     */   protected final TextChannel channel;
/*     */   protected String name;
/*  44 */   protected Icon avatar = null;
/*     */ 
/*     */   
/*     */   public WebhookActionImpl(JDA api, TextChannel channel, String name) {
/*  48 */     super(api, Route.Channels.CREATE_WEBHOOK.compile(new String[] { channel.getId() }));
/*  49 */     this.channel = channel;
/*  50 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookActionImpl setCheck(BooleanSupplier checks) {
/*  57 */     return (WebhookActionImpl)super.setCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookActionImpl timeout(long timeout, @Nonnull TimeUnit unit) {
/*  64 */     return (WebhookActionImpl)super.timeout(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookActionImpl deadline(long timestamp) {
/*  71 */     return (WebhookActionImpl)super.deadline(timestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public TextChannel getChannel() {
/*  78 */     return this.channel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public WebhookActionImpl setName(@Nonnull String name) {
/*  86 */     Checks.notEmpty(name, "Name");
/*  87 */     Checks.notLonger(name, 100, "Name");
/*     */     
/*  89 */     this.name = name;
/*  90 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public WebhookActionImpl setAvatar(Icon icon) {
/*  98 */     this.avatar = icon;
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestBody finalizeData() {
/* 105 */     DataObject object = DataObject.empty();
/* 106 */     object.put("name", this.name);
/* 107 */     object.put("avatar", (this.avatar != null) ? this.avatar.getEncoding() : null);
/*     */     
/* 109 */     return getRequestBody(object);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSuccess(Response response, Request<Webhook> request) {
/* 115 */     DataObject json = response.getObject();
/* 116 */     WebhookImpl webhookImpl = this.api.getEntityBuilder().createWebhook(json);
/*     */     
/* 118 */     request.onSuccess(webhookImpl);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\WebhookActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */