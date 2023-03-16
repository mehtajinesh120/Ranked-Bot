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
/*    */ public class SelfUpdateVerifiedEvent
/*    */   extends GenericSelfUpdateEvent<Boolean>
/*    */ {
/*    */   public static final String IDENTIFIER = "verified";
/*    */   
/*    */   public SelfUpdateVerifiedEvent(@Nonnull JDA api, long responseNumber, boolean wasVerified) {
/* 36 */     super(api, responseNumber, Boolean.valueOf(wasVerified), Boolean.valueOf(!wasVerified), "verified");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean wasVerified() {
/* 46 */     return getOldValue().booleanValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Boolean getOldValue() {
/* 53 */     return super.getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Boolean getNewValue() {
/* 60 */     return super.getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\self\SelfUpdateVerifiedEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */