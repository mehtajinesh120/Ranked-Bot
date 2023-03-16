/*     */ package net.dv8tion.jda.internal.requests.restaction.operator;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.exceptions.RateLimitedException;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CombineRestAction<I1, I2, O>
/*     */   implements RestAction<O>
/*     */ {
/*     */   private final RestAction<I1> action1;
/*     */   private final RestAction<I2> action2;
/*     */   private final BiFunction<? super I1, ? super I2, ? extends O> accumulator;
/*     */   private volatile boolean failed = false;
/*     */   
/*     */   public CombineRestAction(RestAction<I1> action1, RestAction<I2> action2, BiFunction<? super I1, ? super I2, ? extends O> accumulator) {
/*  45 */     Checks.check((action1 != action2), "Cannot combine a RestAction with itself!");
/*  46 */     this.action1 = action1;
/*  47 */     this.action2 = action2;
/*  48 */     this.accumulator = accumulator;
/*  49 */     BooleanSupplier checks = () -> !this.failed;
/*  50 */     action1.addCheck(checks);
/*  51 */     action2.addCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/*  58 */     return this.action1.getJDA();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<O> setCheck(@Nullable BooleanSupplier checks) {
/*  65 */     BooleanSupplier check = () -> (!this.failed && (checks == null || checks.getAsBoolean()));
/*  66 */     this.action1.setCheck(check);
/*  67 */     this.action2.setCheck(check);
/*  68 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<O> addCheck(@Nonnull BooleanSupplier checks) {
/*  75 */     this.action1.addCheck(checks);
/*  76 */     this.action2.addCheck(checks);
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BooleanSupplier getCheck() {
/*  84 */     BooleanSupplier check1 = this.action1.getCheck();
/*  85 */     BooleanSupplier check2 = this.action2.getCheck();
/*  86 */     return () -> 
/*  87 */       ((check1 == null || check1.getAsBoolean()) && (check2 == null || check2.getAsBoolean()) && !this.failed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<O> deadline(long timestamp) {
/*  96 */     this.action1.deadline(timestamp);
/*  97 */     this.action2.deadline(timestamp);
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void queue(@Nullable Consumer<? super O> success, @Nullable Consumer<? super Throwable> failure) {
/* 104 */     ReentrantLock lock = new ReentrantLock();
/* 105 */     AtomicBoolean done1 = new AtomicBoolean(false);
/* 106 */     AtomicBoolean done2 = new AtomicBoolean(false);
/* 107 */     AtomicReference<I1> result1 = new AtomicReference<>();
/* 108 */     AtomicReference<I2> result2 = new AtomicReference<>();
/* 109 */     Consumer<Throwable> failureCallback = e -> {
/*     */         if (this.failed)
/*     */           return; 
/*     */         this.failed = true;
/*     */         RestActionOperator.doFailure(failure, e);
/*     */       };
/* 115 */     this.action1.queue(s -> MiscUtil.locked(lock, ()), failureCallback);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     this.action2.queue(s -> MiscUtil.locked(lock, ()), failureCallback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public O complete(boolean shouldQueue) throws RateLimitedException {
/* 148 */     if (!shouldQueue) {
/* 149 */       return this.accumulator.apply((I1)this.action1.complete(false), (I2)this.action2.complete(false));
/*     */     }
/*     */     try {
/* 152 */       return submit(true).join();
/*     */     }
/* 154 */     catch (CompletionException e) {
/*     */       
/* 156 */       if (e.getCause() instanceof RuntimeException)
/* 157 */         throw (RuntimeException)e.getCause(); 
/* 158 */       if (e.getCause() instanceof RateLimitedException)
/* 159 */         throw (RateLimitedException)e.getCause(); 
/* 160 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CompletableFuture<O> submit(boolean shouldQueue) {
/* 168 */     return this.action1.submit(shouldQueue).thenCombine(this.action2.submit(shouldQueue), this.accumulator);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\operator\CombineRestAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */