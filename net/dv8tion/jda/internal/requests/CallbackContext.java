/*    */ package net.dv8tion.jda.internal.requests;
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
/*    */ public class CallbackContext
/*    */   implements AutoCloseable
/*    */ {
/* 21 */   private static final ThreadLocal<Boolean> callback = ThreadLocal.withInitial(() -> Boolean.valueOf(false));
/* 22 */   private static final CallbackContext instance = new CallbackContext();
/*    */ 
/*    */   
/*    */   public static CallbackContext getInstance() {
/* 26 */     startCallback();
/* 27 */     return instance;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isCallbackContext() {
/* 32 */     return ((Boolean)callback.get()).booleanValue();
/*    */   }
/*    */ 
/*    */   
/*    */   private static void startCallback() {
/* 37 */     callback.set(Boolean.valueOf(true));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {
/* 43 */     callback.set(Boolean.valueOf(false));
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\CallbackContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */