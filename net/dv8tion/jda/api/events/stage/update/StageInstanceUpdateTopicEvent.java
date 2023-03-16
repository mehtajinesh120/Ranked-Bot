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
/*    */ public class StageInstanceUpdateTopicEvent
/*    */   extends GenericStageInstanceUpdateEvent<String>
/*    */ {
/*    */   public static final String IDENTIFIER = "topic";
/*    */   
/*    */   public StageInstanceUpdateTopicEvent(@Nonnull JDA api, long responseNumber, @Nonnull StageInstance stageInstance, String previous) {
/* 38 */     super(api, responseNumber, stageInstance, previous, stageInstance.getTopic(), "topic");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getOldValue() {
/* 45 */     return super.getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getNewValue() {
/* 52 */     return super.getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\stag\\update\StageInstanceUpdateTopicEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */