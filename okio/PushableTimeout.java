/*    */ package okio;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ final class PushableTimeout
/*    */   extends Timeout
/*    */ {
/*    */   private Timeout pushed;
/*    */   private boolean originalHasDeadline;
/*    */   private long originalDeadlineNanoTime;
/*    */   private long originalTimeoutNanos;
/*    */   
/*    */   void push(Timeout pushed) {
/* 33 */     this.pushed = pushed;
/* 34 */     this.originalHasDeadline = pushed.hasDeadline();
/* 35 */     this.originalDeadlineNanoTime = this.originalHasDeadline ? pushed.deadlineNanoTime() : -1L;
/* 36 */     this.originalTimeoutNanos = pushed.timeoutNanos();
/*    */     
/* 38 */     pushed.timeout(minTimeout(this.originalTimeoutNanos, timeoutNanos()), TimeUnit.NANOSECONDS);
/*    */     
/* 40 */     if (this.originalHasDeadline && hasDeadline()) {
/* 41 */       pushed.deadlineNanoTime(Math.min(deadlineNanoTime(), this.originalDeadlineNanoTime));
/* 42 */     } else if (hasDeadline()) {
/* 43 */       pushed.deadlineNanoTime(deadlineNanoTime());
/*    */     } 
/*    */   }
/*    */   
/*    */   void pop() {
/* 48 */     this.pushed.timeout(this.originalTimeoutNanos, TimeUnit.NANOSECONDS);
/*    */     
/* 50 */     if (this.originalHasDeadline) {
/* 51 */       this.pushed.deadlineNanoTime(this.originalDeadlineNanoTime);
/*    */     } else {
/* 53 */       this.pushed.clearDeadline();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okio\PushableTimeout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */