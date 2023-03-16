/*    */ package net.dv8tion.jda.api.utils;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ import java.util.concurrent.locks.Lock;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.internal.utils.JDALogger;
/*    */ import org.slf4j.Logger;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LockIterator<T>
/*    */   implements ClosableIterator<T>
/*    */ {
/* 54 */   private static final Logger log = JDALogger.getLog(ClosableIterator.class);
/*    */   
/*    */   private final Iterator<? extends T> it;
/*    */   private Lock lock;
/*    */   
/*    */   public LockIterator(@Nonnull Iterator<? extends T> it, Lock lock) {
/* 60 */     this.it = it;
/* 61 */     this.lock = lock;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {
/* 67 */     if (this.lock != null)
/* 68 */       this.lock.unlock(); 
/* 69 */     this.lock = null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasNext() {
/* 75 */     if (this.lock == null)
/* 76 */       return false; 
/* 77 */     boolean hasNext = this.it.hasNext();
/* 78 */     if (!hasNext)
/* 79 */       close(); 
/* 80 */     return hasNext;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public T next() {
/* 87 */     if (this.lock == null)
/* 88 */       throw new NoSuchElementException(); 
/* 89 */     return this.it.next();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   protected void finalize() {
/* 96 */     if (this.lock != null) {
/*    */       
/* 98 */       log.error("Finalizing without closing, performing force close on lock");
/* 99 */       close();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\LockIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */