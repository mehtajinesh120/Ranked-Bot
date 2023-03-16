/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.entities.WebhookClient;
/*     */ import net.dv8tion.jda.api.interactions.components.ActionRow;
/*     */ import net.dv8tion.jda.api.interactions.components.ComponentLayout;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.WebhookMessageUpdateAction;
/*     */ import net.dv8tion.jda.api.utils.AttachmentOption;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.requests.restaction.WebhookMessageActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.restaction.WebhookMessageUpdateActionImpl;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
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
/*     */ public abstract class AbstractWebhookClient<T>
/*     */   implements WebhookClient<T>
/*     */ {
/*     */   protected final long id;
/*     */   protected final JDA api;
/*     */   protected String token;
/*     */   
/*     */   protected AbstractWebhookClient(long webhookId, String webhookToken, JDA api) {
/*  48 */     this.id = webhookId;
/*  49 */     this.token = webhookToken;
/*  50 */     this.api = api;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageActionImpl<T> sendMessage(@Nonnull String content) {
/*  60 */     return sendRequest().setContent(content);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageActionImpl<T> sendMessageEmbeds(@Nonnull Collection<? extends MessageEmbed> embeds) {
/*  67 */     return sendRequest().addEmbeds(embeds);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageActionImpl<T> sendMessage(@Nonnull Message message) {
/*  74 */     return sendRequest().applyMessage(message);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageActionImpl<T> sendFile(@Nonnull InputStream data, @Nonnull String name, @Nonnull AttachmentOption... options) {
/*  81 */     return sendRequest().addFile(data, name, options);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageUpdateActionImpl<T> editMessageById(@Nonnull String messageId, @Nonnull String content) {
/*  88 */     return (WebhookMessageUpdateActionImpl<T>)editRequest(messageId).setContent(content);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageUpdateAction<T> editMessageComponentsById(@Nonnull String messageId, @Nonnull Collection<? extends ComponentLayout> components) {
/*  95 */     Checks.noneNull(components, "Components");
/*  96 */     if (components.stream().anyMatch(x -> !(x instanceof ActionRow)))
/*  97 */       throw new UnsupportedOperationException("The provided component layout is not supported"); 
/*  98 */     Objects.requireNonNull(ActionRow.class); List<ActionRow> actionRows = (List<ActionRow>)components.stream().map(ActionRow.class::cast).collect(Collectors.toList());
/*  99 */     return editRequest(messageId).setActionRows(actionRows);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageUpdateActionImpl<T> editMessageEmbedsById(@Nonnull String messageId, @Nonnull Collection<? extends MessageEmbed> embeds) {
/* 106 */     return (WebhookMessageUpdateActionImpl<T>)editRequest(messageId).setEmbeds(embeds);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageUpdateActionImpl<T> editMessageById(@Nonnull String messageId, @Nonnull Message message) {
/* 113 */     return (WebhookMessageUpdateActionImpl<T>)editRequest(messageId).applyMessage(message);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageUpdateActionImpl<T> editMessageById(@Nonnull String messageId, @Nonnull InputStream data, @Nonnull String name, @Nonnull AttachmentOption... options) {
/* 120 */     return (WebhookMessageUpdateActionImpl<T>)editRequest(messageId).addFile(data, name, options);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> deleteMessageById(@Nonnull String messageId) {
/* 127 */     Checks.isSnowflake(messageId);
/* 128 */     Route.CompiledRoute route = Route.Webhooks.EXECUTE_WEBHOOK_DELETE.compile(new String[] { Long.toUnsignedString(this.id), this.token, messageId });
/* 129 */     return (RestAction<Void>)new RestActionImpl(this.api, route);
/*     */   }
/*     */   
/*     */   public abstract WebhookMessageActionImpl<T> sendRequest();
/*     */   
/*     */   public abstract WebhookMessageUpdateActionImpl<T> editRequest(String paramString);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\AbstractWebhookClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */