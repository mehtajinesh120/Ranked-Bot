/*    */ package net.dv8tion.jda.api.events.self;
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
/*    */ 
/*    */ 
/*    */ public class SelfUpdateMFAEvent
/*    */   extends GenericSelfUpdateEvent<Boolean>
/*    */ {
/*    */   public static final String IDENTIFIER = "mfa_enabled";
/*    */   
/*    */   public SelfUpdateMFAEvent(@Nonnull JDA api, long responseNumber, boolean wasMfaEnabled) {
/* 37 */     super(api, responseNumber, Boolean.valueOf(wasMfaEnabled), Boolean.valueOf(!wasMfaEnabled), "mfa_enabled");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean wasMfaEnabled() {
/* 47 */     return getOldValue().booleanValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Boolean getOldValue() {
/* 54 */     return super.getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Boolean getNewValue() {
/* 61 */     return super.getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\self\SelfUpdateMFAEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */