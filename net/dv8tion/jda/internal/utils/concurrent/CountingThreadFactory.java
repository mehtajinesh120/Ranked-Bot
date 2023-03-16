/*    */ package net.dv8tion.jda.internal.utils.concurrent;
/*    */ 
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import java.util.function.Supplier;
/*    */ import javax.annotation.Nonnull;
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
/*    */ public class CountingThreadFactory
/*    */   implements ThreadFactory
/*    */ {
/*    */   private final Supplier<String> identifier;
/* 27 */   private final AtomicLong count = new AtomicLong(1L);
/*    */   
/*    */   private final boolean daemon;
/*    */   
/*    */   public CountingThreadFactory(@Nonnull Supplier<String> identifier, @Nonnull String specifier) {
/* 32 */     this(identifier, specifier, true);
/*    */   }
/*    */ 
/*    */   
/*    */   public CountingThreadFactory(@Nonnull Supplier<String> identifier, @Nonnull String specifier, boolean daemon) {
/* 37 */     this.identifier = (() -> (String)identifier.get() + " " + specifier);
/* 38 */     this.daemon = daemon;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Thread newThread(@Nonnull Runnable r) {
/* 45 */     Thread thread = new Thread(r, (String)this.identifier.get() + "-Worker " + this.count.getAndIncrement());
/* 46 */     thread.setDaemon(this.daemon);
/* 47 */     return thread;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\concurrent\CountingThreadFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */