/*    */ package net.dv8tion.jda.api.managers;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.function.BooleanSupplier;
/*    */ import javax.annotation.CheckReturnValue;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.requests.RestAction;
/*    */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*    */ import net.dv8tion.jda.internal.managers.ManagerBase;
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
/*    */ public interface Manager<M extends Manager<M>>
/*    */   extends AuditableRestAction<Void>
/*    */ {
/*    */   static void setPermissionChecksEnabled(boolean enable) {
/* 44 */     ManagerBase.setPermissionChecksEnabled(enable);
/*    */   }
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
/*    */   static boolean isPermissionChecksEnabled() {
/* 60 */     return ManagerBase.isPermissionChecksEnabled();
/*    */   }
/*    */   
/*    */   @Nonnull
/*    */   M setCheck(BooleanSupplier paramBooleanSupplier);
/*    */   
/*    */   @Nonnull
/*    */   M timeout(long paramLong, @Nonnull TimeUnit paramTimeUnit);
/*    */   
/*    */   @Nonnull
/*    */   M deadline(long paramLong);
/*    */   
/*    */   @Nonnull
/*    */   @CheckReturnValue
/*    */   M reset(long paramLong);
/*    */   
/*    */   @Nonnull
/*    */   @CheckReturnValue
/*    */   M reset(long... paramVarArgs);
/*    */   
/*    */   @Nonnull
/*    */   @CheckReturnValue
/*    */   M reset();
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\managers\Manager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */