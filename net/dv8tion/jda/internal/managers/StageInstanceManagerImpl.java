/*    */ package net.dv8tion.jda.internal.managers;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.entities.StageInstance;
/*    */ import net.dv8tion.jda.api.managers.StageInstanceManager;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.requests.Route;
/*    */ import net.dv8tion.jda.internal.utils.Checks;
/*    */ import okhttp3.RequestBody;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StageInstanceManagerImpl
/*    */   extends ManagerBase<StageInstanceManager>
/*    */   implements StageInstanceManager
/*    */ {
/*    */   private final StageInstance instance;
/*    */   private String topic;
/*    */   private StageInstance.PrivacyLevel privacyLevel;
/*    */   
/*    */   public StageInstanceManagerImpl(StageInstance instance) {
/* 38 */     super(instance.getChannel().getJDA(), Route.StageInstances.UPDATE_INSTANCE.compile(new String[] { instance.getChannel().getId() }));
/* 39 */     this.instance = instance;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public StageInstance getStageInstance() {
/* 46 */     return this.instance;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public StageInstanceManager setTopic(@Nullable String topic) {
/* 53 */     if (topic != null) {
/*    */       
/* 55 */       topic = topic.trim();
/* 56 */       Checks.notLonger(topic, 120, "Topic");
/* 57 */       if (topic.isEmpty())
/* 58 */         topic = null; 
/*    */     } 
/* 60 */     this.topic = topic;
/* 61 */     this.set |= 0x1L;
/* 62 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public StageInstanceManager setPrivacyLevel(@Nonnull StageInstance.PrivacyLevel level) {
/* 69 */     Checks.notNull(level, "PrivacyLevel");
/* 70 */     Checks.check((level != StageInstance.PrivacyLevel.UNKNOWN), "PrivacyLevel must not be UNKNOWN!");
/* 71 */     this.privacyLevel = level;
/* 72 */     this.set |= 0x2L;
/* 73 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected RequestBody finalizeData() {
/* 79 */     DataObject body = DataObject.empty();
/* 80 */     if (shouldUpdate(1L) && this.topic != null)
/* 81 */       body.put("topic", this.topic); 
/* 82 */     if (shouldUpdate(2L))
/* 83 */       body.put("privacy_level", Integer.valueOf(this.privacyLevel.getKey())); 
/* 84 */     return getRequestBody(body);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\managers\StageInstanceManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */