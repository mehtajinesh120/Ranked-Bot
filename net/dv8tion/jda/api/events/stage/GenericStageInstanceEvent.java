/*    */ package net.dv8tion.jda.api.events.stage;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.StageChannel;
/*    */ import net.dv8tion.jda.api.entities.StageInstance;
/*    */ import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
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
/*    */ public abstract class GenericStageInstanceEvent
/*    */   extends GenericGuildEvent
/*    */ {
/*    */   protected final StageInstance instance;
/*    */   
/*    */   public GenericStageInstanceEvent(@Nonnull JDA api, long responseNumber, @Nonnull StageInstance stageInstance) {
/* 38 */     super(api, responseNumber, stageInstance.getGuild());
/* 39 */     this.instance = stageInstance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public StageInstance getInstance() {
/* 50 */     return this.instance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public StageChannel getChannel() {
/* 61 */     return this.instance.getChannel();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\stage\GenericStageInstanceEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */