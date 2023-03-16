/*     */ package net.dv8tion.jda.internal.interactions;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.function.Function;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.interactions.Interaction;
/*     */ import net.dv8tion.jda.api.interactions.InteractionHook;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.AbstractWebhookClient;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.requests.restaction.TriggerRestAction;
/*     */ import net.dv8tion.jda.internal.requests.restaction.WebhookMessageActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.restaction.WebhookMessageUpdateActionImpl;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.JDALogger;
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
/*     */ public class InteractionHookImpl
/*     */   extends AbstractWebhookClient<Message>
/*     */   implements InteractionHook
/*     */ {
/*     */   public static final String TIMEOUT_MESSAGE = "Timed out waiting for interaction acknowledgement";
/*     */   private final InteractionImpl interaction;
/*  48 */   private final List<TriggerRestAction<?>> readyCallbacks = new LinkedList<>();
/*     */   private final Future<?> timeoutHandle;
/*  50 */   private final ReentrantLock mutex = new ReentrantLock();
/*     */   
/*     */   private Exception exception;
/*     */   
/*     */   private boolean isReady;
/*     */   
/*     */   private boolean ephemeral;
/*     */   
/*     */   private volatile boolean isAck;
/*     */   
/*     */   public InteractionHookImpl(@Nonnull InteractionImpl interaction, @Nonnull JDA api) {
/*  61 */     super(api.getSelfUser().getApplicationIdLong(), interaction.getToken(), api);
/*  62 */     this.interaction = interaction;
/*     */     
/*  64 */     this.timeoutHandle = api.getGatewayPool().schedule(() -> fail(new TimeoutException("Timed out waiting for interaction acknowledgement")), 10L, TimeUnit.SECONDS);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean ack() {
/*  69 */     boolean wasAck = this.isAck;
/*  70 */     this.isAck = true;
/*  71 */     return wasAck;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean isAck() {
/*  76 */     return this.isAck;
/*     */   }
/*     */ 
/*     */   
/*     */   public void ready() {
/*  81 */     MiscUtil.locked(this.mutex, () -> {
/*     */           this.timeoutHandle.cancel(false);
/*     */           this.isReady = true;
/*     */           this.readyCallbacks.forEach(TriggerRestAction::run);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void fail(Exception exception) {
/*  90 */     MiscUtil.locked(this.mutex, () -> {
/*     */           if (!this.isReady && this.exception == null) {
/*     */             this.exception = exception;
/*     */             if (!this.readyCallbacks.isEmpty()) {
/*     */               if (exception instanceof TimeoutException) {
/*     */                 JDALogger.getLog(InteractionHook.class).warn("Up to {} Interaction Followup Messages Timed out! Did you forget to acknowledge the interaction?", Integer.valueOf(this.readyCallbacks.size()));
/*     */               }
/*     */               this.readyCallbacks.forEach(());
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <T extends TriggerRestAction<R>, R> T onReady(T runnable) {
/* 106 */     return (T)MiscUtil.locked(this.mutex, () -> {
/*     */           if (this.isReady) {
/*     */             runnable.run();
/*     */           } else if (this.exception != null) {
/*     */             runnable.fail(this.exception);
/*     */           } else {
/*     */             this.readyCallbacks.add(runnable);
/*     */           } 
/*     */           return runnable;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Interaction getInteraction() {
/* 121 */     return this.interaction;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public InteractionHook setEphemeral(boolean ephemeral) {
/* 128 */     this.ephemeral = ephemeral;
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/* 136 */     return this.api;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Message> retrieveOriginal() {
/* 143 */     JDAImpl jda = (JDAImpl)getJDA();
/* 144 */     Route.CompiledRoute route = Route.Interactions.GET_ORIGINAL.compile(new String[] { jda.getSelfUser().getApplicationId(), this.interaction.getToken() });
/* 145 */     return (RestAction<Message>)onReady(new TriggerRestAction((JDA)jda, route, (response, request) -> jda.getEntityBuilder().createMessage(response.getObject(), getInteraction().getMessageChannel(), false)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageActionImpl<Message> sendRequest() {
/* 153 */     Route.CompiledRoute route = Route.Interactions.CREATE_FOLLOWUP.compile(new String[] { getJDA().getSelfUser().getApplicationId(), this.interaction.getToken() });
/* 154 */     route = route.withQueryParams(new String[] { "wait", "true" });
/* 155 */     Function<DataObject, Message> transform = json -> ((JDAImpl)this.api).getEntityBuilder().createMessage(json, getInteraction().getMessageChannel(), false).withHook(this);
/* 156 */     return ((WebhookMessageActionImpl)onReady(new WebhookMessageActionImpl(getJDA(), this.interaction.getMessageChannel(), route, transform))).setEphemeral(this.ephemeral);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageUpdateActionImpl<Message> editRequest(String messageId) {
/* 163 */     if (!"@original".equals(messageId))
/* 164 */       Checks.isSnowflake(messageId); 
/* 165 */     Route.CompiledRoute route = Route.Interactions.EDIT_FOLLOWUP.compile(new String[] { getJDA().getSelfUser().getApplicationId(), this.interaction.getToken(), messageId });
/* 166 */     route = route.withQueryParams(new String[] { "wait", "true" });
/* 167 */     Function<DataObject, Message> transform = json -> ((JDAImpl)this.api).getEntityBuilder().createMessage(json, getInteraction().getMessageChannel(), false).withHook(this);
/* 168 */     return onReady(new WebhookMessageUpdateActionImpl(getJDA(), route, transform));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> deleteMessageById(@Nonnull String messageId) {
/* 175 */     if (!"@original".equals(messageId))
/* 176 */       Checks.isSnowflake(messageId); 
/* 177 */     Route.CompiledRoute route = Route.Interactions.DELETE_FOLLOWUP.compile(new String[] { getJDA().getSelfUser().getApplicationId(), this.interaction.getToken(), messageId });
/* 178 */     return (RestAction<Void>)onReady(new TriggerRestAction(getJDA(), route));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\interactions\InteractionHookImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */