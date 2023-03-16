/*    */ package net.dv8tion.jda.internal.utils;
/*    */ 
/*    */ import java.util.concurrent.Callable;
/*    */ import net.dv8tion.jda.api.audit.ThreadLocalReason;
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
/*    */ public class ContextRunnable<E>
/*    */   implements Runnable, Callable<E>
/*    */ {
/*    */   private final String localReason;
/*    */   private final Runnable runnable;
/*    */   private final Callable<E> callable;
/*    */   
/*    */   public ContextRunnable(Runnable runnable) {
/* 31 */     this.localReason = ThreadLocalReason.getCurrent();
/* 32 */     this.runnable = runnable;
/* 33 */     this.callable = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public ContextRunnable(Callable<E> callable) {
/* 38 */     this.localReason = ThreadLocalReason.getCurrent();
/* 39 */     this.runnable = null;
/* 40 */     this.callable = callable;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 46 */     ThreadLocalReason.Closable __ = ThreadLocalReason.closable(this.localReason);
/*    */     
/* 48 */     try { this.runnable.run();
/* 49 */       if (__ != null) __.close();  }
/*    */     catch (Throwable throwable) { if (__ != null)
/*    */         try { __.close(); }
/*    */         catch (Throwable throwable1)
/*    */         { throwable.addSuppressed(throwable1); }
/*    */           throw throwable; }
/* 55 */      } public E call() throws Exception { ThreadLocalReason.Closable __ = ThreadLocalReason.closable(this.localReason);
/*    */     try {
/* 57 */       E e = this.callable.call();
/* 58 */       if (__ != null) __.close(); 
/*    */       return e;
/*    */     } catch (Throwable throwable) {
/*    */       if (__ != null)
/*    */         try {
/*    */           __.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     }  }
/*    */ 
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\ContextRunnable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */