/*     */ package net.dv8tion.jda.internal.requests.restaction.operator;
/*     */ 
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.exceptions.ContextException;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
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
/*     */ public abstract class RestActionOperator<I, O>
/*     */   implements RestAction<O>
/*     */ {
/*     */   protected BooleanSupplier check;
/*  31 */   protected long deadline = -1L;
/*     */   
/*     */   protected final RestAction<I> action;
/*     */   
/*     */   public RestActionOperator(RestAction<I> action) {
/*  36 */     this.action = action;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static <E> void doSuccess(Consumer<? super E> callback, E value) {
/*  41 */     if (callback == null) {
/*  42 */       RestAction.getDefaultSuccess().accept(value);
/*     */     } else {
/*  44 */       callback.accept(value);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static void doFailure(Consumer<? super Throwable> callback, Throwable throwable) {
/*  49 */     if (callback == null) {
/*  50 */       RestAction.getDefaultFailure().accept(throwable);
/*     */     } else {
/*  52 */       callback.accept(throwable);
/*  53 */     }  if (throwable instanceof Error) {
/*  54 */       throw (Error)throwable;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/*  61 */     return this.action.getJDA();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<O> setCheck(@Nullable BooleanSupplier checks) {
/*  68 */     this.check = checks;
/*  69 */     this.action.setCheck(checks);
/*  70 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BooleanSupplier getCheck() {
/*  77 */     return this.action.getCheck();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<O> deadline(long timestamp) {
/*  84 */     this.deadline = timestamp;
/*  85 */     this.action.deadline(timestamp);
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> RestAction<T> applyContext(RestAction<T> action) {
/*  91 */     if (action == null)
/*  92 */       return null; 
/*  93 */     if (this.check != null)
/*  94 */       action.setCheck(this.check); 
/*  95 */     if (this.deadline >= 0L)
/*  96 */       action.deadline(this.deadline); 
/*  97 */     return action;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Consumer<? super Throwable> contextWrap(@Nullable Consumer<? super Throwable> callback) {
/* 102 */     if (callback instanceof ContextException.ContextConsumer)
/* 103 */       return callback; 
/* 104 */     if (RestAction.isPassContext())
/* 105 */       return ContextException.here((callback == null) ? RestAction.getDefaultFailure() : callback); 
/* 106 */     return callback;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\operator\RestActionOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */