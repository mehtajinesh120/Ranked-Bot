/*     */ package net.dv8tion.jda.internal.requests.restaction;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.entities.StageChannel;
/*     */ import net.dv8tion.jda.api.entities.StageInstance;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.StageInstanceAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
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
/*     */ public class StageInstanceActionImpl
/*     */   extends RestActionImpl<StageInstance>
/*     */   implements StageInstanceAction
/*     */ {
/*     */   private final StageChannel channel;
/*     */   private String topic;
/*  39 */   private StageInstance.PrivacyLevel level = StageInstance.PrivacyLevel.GUILD_ONLY;
/*     */ 
/*     */   
/*     */   public StageInstanceActionImpl(StageChannel channel) {
/*  43 */     super(channel.getJDA(), Route.StageInstances.CREATE_INSTANCE.compile(new String[0]));
/*  44 */     this.channel = channel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public StageInstanceAction setCheck(BooleanSupplier checks) {
/*  51 */     return (StageInstanceAction)super.setCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public StageInstanceAction timeout(long timeout, @Nonnull TimeUnit unit) {
/*  58 */     return (StageInstanceAction)super.timeout(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public StageInstanceAction deadline(long timestamp) {
/*  65 */     return (StageInstanceAction)super.deadline(timestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public StageInstanceAction setTopic(@Nonnull String topic) {
/*  72 */     Checks.notBlank(topic, "Topic");
/*  73 */     Checks.notLonger(topic, 120, "Topic");
/*  74 */     this.topic = topic;
/*  75 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public StageInstanceAction setPrivacyLevel(@Nonnull StageInstance.PrivacyLevel level) {
/*  82 */     Checks.notNull(level, "PrivacyLevel");
/*  83 */     Checks.check((level != StageInstance.PrivacyLevel.UNKNOWN), "The PrivacyLevel must not be UNKNOWN!");
/*  84 */     this.level = level;
/*  85 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/*  91 */     DataObject body = DataObject.empty();
/*  92 */     body.put("channel_id", this.channel.getId());
/*  93 */     body.put("topic", this.topic);
/*  94 */     body.put("privacy_level", Integer.valueOf(this.level.getKey()));
/*  95 */     return getRequestBody(body);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSuccess(Response response, Request<StageInstance> request) {
/* 101 */     StageInstance instance = this.api.getEntityBuilder().createStageInstance((GuildImpl)this.channel.getGuild(), response.getObject());
/* 102 */     request.onSuccess(instance);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\StageInstanceActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */