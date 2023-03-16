/*    */ package net.dv8tion.jda.internal.requests.restaction.operator;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.exceptions.RateLimitedException;
/*    */ import net.dv8tion.jda.api.requests.RestAction;
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
/*    */ public class FlatMapRestAction<I, O>
/*    */   extends RestActionOperator<I, O>
/*    */ {
/*    */   private final Function<? super I, ? extends RestAction<O>> function;
/*    */   private final Predicate<? super I> condition;
/*    */   
/*    */   public FlatMapRestAction(RestAction<I> action, Predicate<? super I> condition, Function<? super I, ? extends RestAction<O>> function) {
/* 37 */     super(action);
/* 38 */     this.function = function;
/* 39 */     this.condition = condition;
/*    */   }
/*    */ 
/*    */   
/*    */   private RestAction<O> supply(I input) {
/* 44 */     return applyContext(this.function.apply(input));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void queue(@Nullable Consumer<? super O> success, @Nullable Consumer<? super Throwable> failure) {
/* 50 */     Consumer<? super Throwable> contextFailure = contextWrap(failure);
/* 51 */     this.action.queue(result -> { if (this.condition != null && !this.condition.test((I)result)) return;  RestAction<O> then = supply((I)result); if (then == null) { doFailure(contextFailure, new IllegalStateException("FlatMap operand is null")); } else { then.queue(success, contextFailure); }  }contextFailure);
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
/*    */   public O complete(boolean shouldQueue) throws RateLimitedException {
/* 65 */     return (O)supply((I)this.action.complete(shouldQueue)).complete(shouldQueue);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public CompletableFuture<O> submit(boolean shouldQueue) {
/* 72 */     return this.action.submit(shouldQueue)
/* 73 */       .thenCompose(result -> supply((I)result).submit(shouldQueue));
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\operator\FlatMapRestAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */