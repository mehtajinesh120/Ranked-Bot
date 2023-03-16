/*    */ package net.dv8tion.jda.api.events.stage.update;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.StageInstance;
/*    */ import net.dv8tion.jda.api.events.UpdateEvent;
/*    */ import net.dv8tion.jda.api.events.stage.GenericStageInstanceEvent;
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
/*    */ public abstract class GenericStageInstanceUpdateEvent<T>
/*    */   extends GenericStageInstanceEvent
/*    */   implements UpdateEvent<StageInstance, T>
/*    */ {
/*    */   protected final T previous;
/*    */   protected final T next;
/*    */   protected final String identifier;
/*    */   
/*    */   public GenericStageInstanceUpdateEvent(@Nonnull JDA api, long responseNumber, @Nonnull StageInstance stageInstance, T previous, T next, String identifier) {
/* 41 */     super(api, responseNumber, stageInstance);
/* 42 */     this.previous = previous;
/* 43 */     this.next = next;
/* 44 */     this.identifier = identifier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getPropertyIdentifier() {
/* 51 */     return this.identifier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public StageInstance getEntity() {
/* 58 */     return getInstance();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public T getOldValue() {
/* 65 */     return this.previous;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public T getNewValue() {
/* 72 */     return this.next;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\stag\\update\GenericStageInstanceUpdateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */