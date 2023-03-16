/*    */ package net.dv8tion.jda.api.events;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
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
/*    */ public class StatusChangeEvent
/*    */   extends Event
/*    */   implements UpdateEvent<JDA, JDA.Status>
/*    */ {
/*    */   public static final String IDENTIFIER = "status";
/*    */   protected final JDA.Status newStatus;
/*    */   protected final JDA.Status oldStatus;
/*    */   
/*    */   public StatusChangeEvent(@Nonnull JDA api, @Nonnull JDA.Status newStatus, @Nonnull JDA.Status oldStatus) {
/* 38 */     super(api);
/* 39 */     this.newStatus = newStatus;
/* 40 */     this.oldStatus = oldStatus;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public JDA.Status getNewStatus() {
/* 51 */     return this.newStatus;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public JDA.Status getOldStatus() {
/* 62 */     return this.oldStatus;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getPropertyIdentifier() {
/* 69 */     return "status";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public JDA getEntity() {
/* 76 */     return getJDA();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public JDA.Status getOldValue() {
/* 83 */     return this.oldStatus;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public JDA.Status getNewValue() {
/* 90 */     return this.newStatus;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 96 */     return "StatusUpdate(" + getOldStatus() + "->" + getNewStatus() + ')';
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\StatusChangeEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */