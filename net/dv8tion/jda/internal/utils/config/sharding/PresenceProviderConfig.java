/*    */ package net.dv8tion.jda.internal.utils.config.sharding;
/*    */ 
/*    */ import java.util.function.IntFunction;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.OnlineStatus;
/*    */ import net.dv8tion.jda.api.entities.Activity;
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
/*    */ public class PresenceProviderConfig
/*    */ {
/*    */   private IntFunction<? extends Activity> activityProvider;
/*    */   private IntFunction<OnlineStatus> statusProvider;
/*    */   private IntFunction<Boolean> idleProvider;
/*    */   
/*    */   @Nullable
/*    */   public IntFunction<? extends Activity> getActivityProvider() {
/* 35 */     return this.activityProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setActivityProvider(@Nullable IntFunction<? extends Activity> activityProvider) {
/* 40 */     this.activityProvider = activityProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public IntFunction<OnlineStatus> getStatusProvider() {
/* 46 */     return this.statusProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setStatusProvider(@Nullable IntFunction<OnlineStatus> statusProvider) {
/* 51 */     this.statusProvider = statusProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public IntFunction<Boolean> getIdleProvider() {
/* 57 */     return this.idleProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setIdleProvider(@Nullable IntFunction<Boolean> idleProvider) {
/* 62 */     this.idleProvider = idleProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public static PresenceProviderConfig getDefault() {
/* 68 */     return new PresenceProviderConfig();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\config\sharding\PresenceProviderConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */