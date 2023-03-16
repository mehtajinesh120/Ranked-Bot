/*    */ package net.dv8tion.jda.api.events.stage.update;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.StageInstance;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StageInstanceUpdatePrivacyLevelEvent
/*    */   extends GenericStageInstanceUpdateEvent<StageInstance.PrivacyLevel>
/*    */ {
/*    */   public static final String IDENTIFIER = "privacy_level";
/*    */   
/*    */   public StageInstanceUpdatePrivacyLevelEvent(@Nonnull JDA api, long responseNumber, @Nonnull StageInstance stageInstance, @Nonnull StageInstance.PrivacyLevel previous) {
/* 38 */     super(api, responseNumber, stageInstance, previous, stageInstance.getPrivacyLevel(), "privacy_level");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public StageInstance.PrivacyLevel getOldValue() {
/* 45 */     return super.getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public StageInstance.PrivacyLevel getNewValue() {
/* 52 */     return super.getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\stag\\update\StageInstanceUpdatePrivacyLevelEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */