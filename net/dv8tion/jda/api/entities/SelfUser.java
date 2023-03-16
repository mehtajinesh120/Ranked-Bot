/*    */ package net.dv8tion.jda.api.entities;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.managers.AccountManager;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface SelfUser
/*    */   extends User
/*    */ {
/*    */   long getApplicationIdLong();
/*    */   
/*    */   @Nonnull
/*    */   default String getApplicationId() {
/* 47 */     return Long.toUnsignedString(getApplicationIdLong());
/*    */   }
/*    */   
/*    */   boolean isVerified();
/*    */   
/*    */   boolean isMfaEnabled();
/*    */   
/*    */   long getAllowedFileSize();
/*    */   
/*    */   @Nonnull
/*    */   AccountManager getManager();
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\SelfUser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */