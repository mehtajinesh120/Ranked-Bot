/*    */ package net.dv8tion.jda.internal.utils;
/*    */ 
/*    */ import java.util.concurrent.locks.Lock;
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
/*    */ public class UnlockHook
/*    */   implements AutoCloseable
/*    */ {
/*    */   private final Lock lock;
/*    */   
/*    */   public UnlockHook(Lock lock) {
/* 27 */     this.lock = lock;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {
/* 33 */     this.lock.unlock();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\UnlockHook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */